package com.cnqisoft.fastcode;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDialog;
import com.intellij.openapi.fileChooser.ex.FileChooserDialogImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ExceptionUtil;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Binary
 */
public class PathDialog extends JDialog {

    private Project project;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField configFilePath;
    private JTextField packagePath;
    private JButton chooseConfigFile;
    private JButton choosePackage;

    public PathDialog(Project project) {
        this.project = project;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        chooseConfigFile.addActionListener(e -> {
            FileChooserDescriptor fileChooserDescriptor = new FileChooserDescriptor(true, false, false, false, false, false);
            FileChooserDialog fileChooserDialog = new FileChooserDialogImpl(fileChooserDescriptor, project);
            VirtualFile[] virtualFiles = fileChooserDialog.choose(project);
            configFilePath.setText(virtualFiles[0].getPath());
        });

        choosePackage.addActionListener(e -> {
            // 这里不知道发生了什么，明明类是存在的但是编译时就是找不到，只用通过反射来使用
            try {
                // 构建dialog
                Class<?> cls = Class.forName("com.intellij.ide.util.PackageChooserDialog");
                Constructor<?> constructor = cls.getConstructor(String.class, Project.class);
                Object dialog = constructor.newInstance("Package Chooser", project);
                // 打开dialog窗口
                Method show = dialog.getClass().getMethod("show");
                show.invoke(dialog);
                // 获取选中的包信息
                Method getSelectedPackage = dialog.getClass().getMethod("getSelectedPackage");
                Object psiPackage = getSelectedPackage.invoke(dialog);
                // 获取名字
                if (psiPackage != null) {
                    Method getQualifiedName = psiPackage.getClass().getMethod("getQualifiedName");
                    String packageName = (String) getQualifiedName.invoke(psiPackage);
                    packagePath.setText(packageName);
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException | ClassNotFoundException e1) {
                // 抛出异常信息
                ExceptionUtil.rethrow(e1);
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        String configFilePath = this.configFilePath.getText();
        String packagePath = project.getBasePath() + "/src/main/java/" + this.packagePath.getText().replace(".", File.separator);

        try {
            String controllerTemplate = readStringFromStream(this.getClass().getResourceAsStream("/template/controller.java"));
            String mapperJavaTemplate = readStringFromStream(this.getClass().getResourceAsStream("/template/mapper.java"));
            String mapperXmlTemplate = readStringFromStream(this.getClass().getResourceAsStream("/template/mapper.xml"));
            String serviceImplTemplate = readStringFromStream(this.getClass().getResourceAsStream("/template/service.impl.java"));
            String serviceTemplate = readStringFromStream(this.getClass().getResourceAsStream("/template/service.java"));
            String entityTemplate = readStringFromStream(this.getClass().getResourceAsStream("/template/entity.java"));

            JSONObject jsonObject = generateTable(configFilePath);
            String entityName = jsonObject.getString("name").toLowerCase();
            String capitalizeEntityName = StrUtil.upperFirst(entityName);
            Map<String, String> data = new HashMap<>();
            data.put("${packageRoot}", this.packagePath.getText());
            data.put("${Entity}", capitalizeEntityName);
            data.put("${entity}", entityName);
            String controller = render(controllerTemplate, data);
            String mapperJava = render(mapperJavaTemplate, data);
            String mapperXml = render(mapperXmlTemplate, data);
            String serviceImpl = render(serviceImplTemplate, data);
            String service = render(serviceTemplate, data);
            String entity = render(entityTemplate, data);
            entity = entity.replace("${content}", getEntity(jsonObject));
            output(packagePath + File.separator + "controller" + File.separator + capitalizeEntityName + "Controller.java", controller);
            output(packagePath + File.separator + "mapper" + File.separator + capitalizeEntityName + "Mapper.java", mapperJava);
            output(packagePath + File.separator + "mapperxml" + File.separator + capitalizeEntityName + "Mapper.xml", mapperXml);
            output(packagePath + File.separator + "service/impl" + File.separator + capitalizeEntityName + "ServiceImpl.java", serviceImpl);
            output(packagePath + File.separator + "service" + File.separator + capitalizeEntityName + "Service.java", service);
            output(packagePath + File.separator + "entity" + File.separator + capitalizeEntityName + ".java", entity);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getEntity(JSONObject jsonObject) {
        String tableName = jsonObject.getString("name");
        JSONArray fieldJsonArray = jsonObject.getJSONArray("fields");
        StringBuilder stringBuilder = new StringBuilder();
        List<Field> fieldList = new ArrayList<>(fieldJsonArray.size());

        for (int i = 0; i < fieldJsonArray.size(); i++) {
            JSONObject fieldJSONObject = fieldJsonArray.getJSONObject(i);
            String fieldName = fieldJSONObject.getString("field");
            String type = fieldJSONObject.getString("type");
            JSONObject rules = fieldJSONObject.getJSONObject("rules");

            if (rules != null) {
                Boolean required = rules.getBoolean("required");
                Boolean unique = rules.getBoolean("unique");
                DataType dataType = DataType.valueOf(type.toUpperCase());
                Field field = new Field(fieldName, dataType, required, unique);
                String javaType = field.getJavaType();
                stringBuilder.append("    ").append(javaType).append("\r\n\r\n");
                fieldList.add(field);
            }

        }
        Table table = new Table(tableName, fieldList);
        String tableDDL = table.getTableDDL();
        System.out.println(tableDDL);
        return stringBuilder.toString();
    }

    private void output(String path, String data) throws IOException {
        File file = new File(path);
        file.getParentFile().mkdirs();
        try (
                FileOutputStream fileOutputStream = new FileOutputStream(path);
        ){
            fileOutputStream.write(data.getBytes());
        }
    }

    private String render(String template, Map<String, String> data) {
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            template = template.replace(key, value);
        }
        return template;
    }

    private String readStringFromStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append("\r\n");
        }
        return stringBuilder.toString();
    }

    public JSONObject generateTable(String path) {
        String s = FileUtil.readUtf8String(path);
        JSONObject jsonObject = JSON.parseObject(s);
        String tableName = jsonObject.getString("name");
        JSONArray fieldJsonArray = jsonObject.getJSONArray("fields");

        List<Field> fieldList = new ArrayList<>(fieldJsonArray.size());

        for (int i = 0; i < fieldJsonArray.size(); i++) {
            JSONObject fieldJSONObject = fieldJsonArray.getJSONObject(i);
            String fieldName = fieldJSONObject.getString("name");
            String type = fieldJSONObject.getString("type");
            JSONObject rules = fieldJSONObject.getJSONObject("rules");

            if (rules != null) {
                Boolean required = rules.getBoolean("required");
                Boolean unique = rules.getBoolean("unique");
                DataType dataType = DataType.valueOf(type.toUpperCase());
                Field field = new Field(fieldName, dataType, required, unique);
                fieldList.add(field);
            }

        }
        Table table = new Table(tableName, fieldList);
        String tableDDL = table.getTableDDL();
        return jsonObject;
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public void open() {
        this.pack();
        setLocationRelativeTo(null);
        this.setVisible(true);
    }
}

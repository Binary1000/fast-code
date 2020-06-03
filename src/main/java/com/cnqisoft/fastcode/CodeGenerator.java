package com.cnqisoft.fastcode;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.velocity.VelocityContext;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Binary on 2020/6/2
 */
public class CodeGenerator {

    private final String resourcePath;

    public CodeGenerator(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public void generateCode(String configFilePath, String outputPath, String packageName) {
        try {

            JSONObject jsonObject = parseConfigFile(configFilePath);
            String entityName = jsonObject.getString("name").toLowerCase();
            String capitalizeEntityName = StrUtil.upperFirst(entityName);

            List<Field> fieldList = getFieldList(jsonObject);

            VelocityContext velocityContext = new VelocityContext();
            velocityContext.put("packageRoot", packageName);
            velocityContext.put("Entity", capitalizeEntityName);
            velocityContext.put("entity", entityName);
            velocityContext.put("columns", fieldList);

            Table table = new Table(entityName, fieldList);
            String tableDDL = table.getTableDDL();
            System.out.println(tableDDL);

            output(outputPath, capitalizeEntityName, velocityContext, Level.CONTROLLER);
            output(outputPath, capitalizeEntityName, velocityContext, Level.MAPPER_JAVA);
            output(outputPath, capitalizeEntityName, velocityContext, Level.SERVICE_IMPL);
            output(outputPath, capitalizeEntityName, velocityContext, Level.SERVICE);
            output(outputPath, capitalizeEntityName, velocityContext, Level.ENTITY);

            output(resourcePath, capitalizeEntityName, velocityContext, Level.MAPPER_XML);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void output(String packagePath, String capitalizeEntityName, VelocityContext velocityContext, Level level) throws IOException {
        String path = getOutputPath(packagePath, capitalizeEntityName, level);
        File file = new File(path);
        file.getParentFile().mkdirs();

        InputStream inputStream = ResourceUtil.getResourceAsStream("template/" + level.templateName);
        String template = StreamUtil.readString(inputStream, "UTF-8");
        String code = VelocityUtil.render(template, velocityContext);
        FileUtil.writeBytes(code.getBytes(), path);
    }

    private String getOutputPath(String packagePath, String entityName, Level level) {
        return packagePath + File.separator + level.packageName + File.separator + entityName + level.fileName;
    }

    private List<Field> getFieldList(JSONObject jsonObject) {
        JSONArray fieldJsonArray = jsonObject.getJSONArray("fields");
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
                fieldList.add(field);
            }
        }
        return fieldList;
    }

    private JSONObject parseConfigFile(String path) {
        String s = FileUtil.readUtf8String(path);
        return JSON.parseObject(s);
    }
}

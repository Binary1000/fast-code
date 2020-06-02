package com.cnqisoft.fastcode;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDialog;
import com.intellij.openapi.fileChooser.ex.FileChooserDialogImpl;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.velocity.VelocityContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.java.JavaSourceRootType;
import org.junit.Test;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GenerateCodeDialog extends JDialog {

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField configFileText;
    private JTextField outputPathText;
    private JTextField packageNameText;
    private JButton chooseConfigFile;
    private JButton chooseOutputPath;

    private Module module;

    private final String resourcePath;

    public GenerateCodeDialog(Project project) {

        Module[] modules = ModuleManager.getInstance(project).getModules();
        if (modules.length >= 1) {
            this.module = modules[0];
        }

        resourcePath = ModuleUtil.getResourcePath(module);

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        chooseConfigFile.addActionListener(e -> {
            FileChooserDescriptor fileChooserDescriptor = new FileChooserDescriptor(true, false, false, false, false, false);
            FileChooserDialog fileChooserDialog = new FileChooserDialogImpl(fileChooserDescriptor, project);
            VirtualFile[] virtualFiles = fileChooserDialog.choose(project);
            configFileText.setText(virtualFiles[0].getPath());
        });

        chooseOutputPath.addActionListener(e -> {
            FileChooserDescriptor fileChooserDescriptor = new FileChooserDescriptor(false, true, false, false, false, false);
            FileChooserDialog fileChooserDialog = new FileChooserDialogImpl(fileChooserDescriptor, project);
            VirtualFile[] virtualFiles = fileChooserDialog.choose(project);

            String outputPath = virtualFiles[0].getPath();
            outputPathText.setText(outputPath);

            String sourcePath = ModuleUtil.getSourcePath(module);
            if (outputPath.startsWith(sourcePath)) {
                String packageName = outputPath.replace(sourcePath, "");
                if (packageName.startsWith("/")) {
                    packageName = packageName.substring(1);
                }
                packageName = packageName.replace("/", ".");
                packageNameText.setText(packageName);
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

        String configFilePath = configFileText.getText();
        String outputPath = outputPathText.getText();
        String packageName = packageNameText.getText();

        CodeGenerator codeGenerator = new CodeGenerator(resourcePath);
        codeGenerator.generateCode(configFilePath, outputPath, packageName);

        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public void open() {
        this.pack();
        setLocationRelativeTo(null);
        this.setVisible(true);
    }

}

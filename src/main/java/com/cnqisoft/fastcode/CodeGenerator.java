package com.cnqisoft.fastcode;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.cnqisoft.fastcode.util.ResourceUtil;
import com.cnqisoft.fastcode.util.StreamUtil;
import com.cnqisoft.fastcode.util.VelocityUtil;
import org.apache.velocity.VelocityContext;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

            Table table = ConfigFileParser.parse(configFilePath);

            String entityName = table.getName().toLowerCase();
            String capitalizeEntityName = StrUtil.upperFirst(entityName);
            List<Field> fieldList = table.getFields();
            List<String> files = table.getFiles();

            VelocityContext velocityContext = new VelocityContext();
            velocityContext.put("packageRoot", packageName);
            velocityContext.put("Entity", capitalizeEntityName);
            velocityContext.put("entity", entityName);
            velocityContext.put("columns", fieldList);
            velocityContext.put("files", files);

            output(outputPath, capitalizeEntityName, velocityContext, Level.CONTROLLER);
            output(outputPath, capitalizeEntityName, velocityContext, Level.MAPPER_JAVA);
            output(outputPath, capitalizeEntityName, velocityContext, Level.SERVICE_IMPL);
            output(outputPath, capitalizeEntityName, velocityContext, Level.SERVICE);
            output(outputPath, capitalizeEntityName, velocityContext, Level.ENTITY);
            output(outputPath, "", velocityContext, Level.ANNOTATION);
            output(outputPath, "", velocityContext, Level.ADVICE);
            output(outputPath, "", velocityContext, Level.RESPONSE);
            output(resourcePath, capitalizeEntityName, velocityContext, Level.MAPPER_XML);



            for (Field field : fieldList) {
                if (field.isEnumeration()) {
                    velocityContext.put("EnumClassName", field.getCapitalizedName());
                    velocityContext.put("enumClassName", field.getName());
                    velocityContext.put("enums", field.getEnumList());
                    velocityContext.put("valueType", field.getJavaType());

                    output(outputPath, field.getCapitalizedName(), velocityContext, Level.ENUM);
                }
            }


            String path = resourcePath + File.separator + "table" + File.separator + entityName + ".sql";
            File file = new File(path);
            file.getParentFile().mkdirs();
            FileUtil.writeUtf8String(table.getTableDDL(), path);

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
        FileUtil.writeUtf8String(code, path);
    }

    private String getOutputPath(String packagePath, String entityName, Level level) {
        return packagePath + File.separator + level.packageName + File.separator + entityName + level.fileName;
    }

}

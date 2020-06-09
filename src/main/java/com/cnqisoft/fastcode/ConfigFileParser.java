package com.cnqisoft.fastcode;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Binary on 2020/6/3
 */
public class ConfigFileParser {

    public static Table parse(String configFilePath) {
        JSONObject jsonObject = parseConfigFile(configFilePath);

        String name = jsonObject.getString("name");
        Table table = new Table(name, getFieldList(jsonObject));
        table.setFiles(getFileList(jsonObject));
        return table;
    }

    private static List<String> getFileList(JSONObject jsonObject) {
        List<String> files = new ArrayList<>();
        JSONArray fileJsonArray = jsonObject.getJSONArray("files");
        if (fileJsonArray != null) {
            for (int i = 0; i < fileJsonArray.size(); i++) {
                String fileField = fileJsonArray.getString(i);
                files.add(fileField);
            }
        }
        return files;
    }

    private static List<Field> getFieldList(JSONObject jsonObject) {
        JSONArray fieldJsonArray = jsonObject.getJSONArray("fields");
        List<Field> fieldList = new ArrayList<>(fieldJsonArray.size());

        for (int i = 0; i < fieldJsonArray.size(); i++) {
            JSONObject fieldJsonObject = fieldJsonArray.getJSONObject(i);
            String fieldName = fieldJsonObject.getString("field");
            String type = fieldJsonObject.getString("type");
            Boolean required = fieldJsonObject.getBoolean("required");
            Boolean unique = fieldJsonObject.getBoolean("unique");
            DataType dataType = DataType.valueOf(type.toUpperCase());

            Field field = new Field(fieldName, dataType, required, unique);
            fieldList.add(field);
            JSONArray enums = fieldJsonObject.getJSONArray("enum");
            if (enums != null) {
                List<Enum> enumList = parseEnums(enums, type);
                field.setEnumList(enumList);
            }
        }
        return fieldList;
    }

    private static List<Enum> parseEnums(JSONArray enums, String type) {
        List<Enum> list = new ArrayList<>();
        for (int i = 0, len = enums.size(); i < len; i++) {
            JSONObject jsonObject = enums.getJSONObject(i);
            String name = jsonObject.getString("name");
            Object value = jsonObject.get("value");
            list.add(new Enum(name, value, type));
        }
        return list;
    }

    private static JSONObject parseConfigFile(String configFilePath) {
        String s = FileUtil.readUtf8String(configFilePath);
        return JSON.parseObject(s);
    }
}

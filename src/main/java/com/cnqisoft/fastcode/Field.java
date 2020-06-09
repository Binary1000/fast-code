package com.cnqisoft.fastcode;

import cn.hutool.core.util.StrUtil;

import java.util.List;

public class Field {

    private final String name;

    private final String capitalizedName;

    private final String underlineCaseName;

    private final String jdbcType;

    private final String javaType;

    private final Integer length;

    private final Boolean required;

    private final Boolean unique;

    private Boolean enumeration = false;

    private List<Enum> enumList;

    public void setEnumList(List<Enum> enumList) {
        if (!enumList.isEmpty()) {
            this.enumeration = true;
        }
        this.enumList = enumList;
    }

    public List<Enum> getEnumList() {
        return enumList;
    }

    public Field(String name, DataType dataType) {
        this(name, dataType, dataType.getLength());
    }

    public Field(String name, DataType dataType, int length) {
        this(name, dataType, length, false, false);
    }

    public Field(String name, DataType dataType, Boolean required, Boolean unique) {
        this(name, dataType, dataType.getLength(), required, unique);
    }

    public Field(String name, DataType dataType, int length, Boolean required, Boolean unique) {
        this.name = name;
        this.underlineCaseName = StrUtil.toUnderlineCase(name);
        this.capitalizedName = StrUtil.upperFirst(name);
        this.jdbcType = dataType.getJdbcType();
        this.javaType = dataType.getJavaType();
        this.length = length;
        this.required = required == null ? false : required;
        this.unique = unique == null ? false : unique;
    }


    public boolean isUnique() {
        return unique != null && unique;
    }

    public String getString() {
        return String.format("  `%s` %s(%d) %s %s, \n", underlineCaseName
                , jdbcType, length, isUnique() ? "unique" : "", isRequired() ? "NOT NULL" : "NULL");
    }

    public String getJdbcType() {
        return jdbcType;
    }

    public String getJavaType() {
        return javaType;
    }

    public String getName() {
        return name;
    }

    public String getUnderlineCaseName() {
        return underlineCaseName;
    }

    public String getCapitalizedName() {
        return capitalizedName;
    }

    public Boolean isEnumeration() {
        return enumeration;
    }

    public Boolean isRequired() {
        return required;
    }

}

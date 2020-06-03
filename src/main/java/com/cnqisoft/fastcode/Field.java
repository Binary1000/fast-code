package com.cnqisoft.fastcode;

import cn.hutool.core.util.StrUtil;

public class Field {

    private final String name;

    private final String capitalizedName;

    private final String underlineCaseName;

    private final String jdbcType;

    private final String javaType;

    private final Integer length;

    private final Boolean required;

    private final Boolean unique;

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
        this.required = required;
        this.unique = unique;
    }

    public boolean isRequired() {
        return required != null && required;
    }

    public boolean isUnique() {
        return unique != null && unique;
    }

    public String getString() {
        return String.format("  `%s` %s(%d) %s %s, \n", underlineCaseName, jdbcType, length, isUnique() ? "unique" : "", isRequired() ? "NOT NULL" : "NULL");
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
}

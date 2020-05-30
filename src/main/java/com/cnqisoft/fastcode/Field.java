package com.cnqisoft.fastcode;

import cn.hutool.core.util.StrUtil;

public class Field {

    private final String name;

    private final DataType dataType;

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
        this.dataType = dataType;
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
        return String.format("  `%s` %s(%d) %s %s, \n", StrUtil.toUnderlineCase(name), dataType.getDbType(), length, isUnique() ? "unique" : "", isRequired() ? "NOT NULL" : "NULL");
    }

    public String getJavaType() {
        return String.format("private %s %s;", dataType.getJavaType(), name);
    }
}

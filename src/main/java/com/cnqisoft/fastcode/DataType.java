package com.cnqisoft.fastcode;

/**
 * @author Binary
 */

public enum DataType {

    STRING("varchar", "String", 255),
    FILE("varchar", "MultipartFile",255),
    NUMBER("int", "Integer", 11),
    LONG("int", "Long", 11);

    private final int length;

    private final String dbType;

    private final String javaType;

    DataType(String dbType, String javaType, int length) {
        this.dbType = dbType;
        this.javaType = javaType;
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    public String getDbType() {
        return dbType;
    }

    public String getJavaType() {
        return javaType;
    }
}

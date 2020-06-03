package com.cnqisoft.enums;

/**
 * @author Binary on 2020/6/3
 */
public enum FileType {

    SINGLE_MODEL("单模型"),
    INTEGRATE_MODEL("集成模型");

    private String value;

    FileType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static FileType of(String value) {
        FileType[] values = values();

        for (FileType fileType : values) {
            if (fileType.value.equals(value)) {
                return fileType;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}

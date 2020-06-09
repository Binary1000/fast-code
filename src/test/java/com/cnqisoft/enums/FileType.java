package com.cnqisoft.enums;

public enum FileType {

    SINGLE_MODEL("单模型"),
    INTEGRATE_MODEL("集成模型");

    private final String value;

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

    public static boolean isPresent(String value) {
        FileType[] values = values();

        for (FileType fileType : values) {
            if (fileType.value.equals(value)) {
                return true;
            }
        }
        return false;
    }
}

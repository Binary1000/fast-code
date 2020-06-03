package com.cnqisoft.enums;

/**
 * @author Binary on 2020/6/3
 */
public enum TranslateStatus {

    PREPARE("等待转换"),
    SUCCESS("转换成功"),
    PROCESSING("转换中"),
    FAILED("转换失败");

    private String value;

    TranslateStatus(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static TranslateStatus of(String value) {
        TranslateStatus[] values = values();

        for (TranslateStatus translateStatus : values) {
            if (translateStatus.value.equals(value)) {
                return translateStatus;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}

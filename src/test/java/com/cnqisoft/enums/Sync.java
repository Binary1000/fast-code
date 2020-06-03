package com.cnqisoft.enums;

/**
 * @author Binary on 2020/6/3
 */
public enum Sync {

    SYNC(0),
    NOT_SYNC(1),
    SYNCING(2);

    private Integer value;

    Sync(Integer value) {
        this.value = value;
    }

    public Integer value() {
        return value;
    }

    public static Sync of(Integer value) {
        Sync[] values = values();

        for (Sync sync : values) {
            if (sync.value.equals(value)) {
                return sync;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}

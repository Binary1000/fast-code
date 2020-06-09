package com.cnqisoft.enums;

public enum Sync {

    NOT_SYNCHRONIZED(0),
    SYNCHRONIZED(1),
    SYNCHRONIZING(2);

    private final Integer value;

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

    public static boolean isPresent(Integer value) {
        Sync[] values = values();

        for (Sync sync : values) {
            if (sync.value.equals(value)) {
                return true;
            }
        }
        return false;
    }
}

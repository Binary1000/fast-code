package com.cnqisoft.fastcode;

import cn.hutool.core.util.StrUtil;

/**
 * @author Binary on 2020/6/3
 */
public class Enum {

    private final String javaType;

    private final String name;

    private final Object value;

    public Enum(String name, Object value, String javaType) {
        this.name = StrUtil.toUnderlineCase(name).toUpperCase();
        this.value = value;
        this.javaType = javaType;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public String getJavaType() {
        return javaType;
    }
}

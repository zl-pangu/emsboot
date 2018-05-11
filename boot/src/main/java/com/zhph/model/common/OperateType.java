package com.zhph.model.common;

/**
 * Created by zhouliang on 2018/1/16.
 */
public enum OperateType {
    SAVE("0","SAVE"),
    UPDATE("1","UPDATE"),
    IMPORT("3","IMPORT"),
    EXPORT("4","EXPORT"),
    DELETE("2","DELETE");

    private OperateType(String key, String value) {
        this.key = key;
        this.value = value;
    }

    private final String key;
    private final String value;

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}

package com.app.models.enums;

import java.util.Arrays;

public enum ConfigurationNodes {

    DATABASE("database"),
    QUEUE("queue"),

    INPUT("input"),

    INVALID("invalid");

    private final String value;

    ConfigurationNodes(String val) {
        this.value = val;
    }
    public String getVal() {
        return value;
    }
    public static final ConfigurationNodes getByValue(String val){
        return Arrays.stream(ConfigurationNodes.values()).filter(enumRole -> enumRole.value.equals(val)).findFirst().orElse(INVALID);
    }
}

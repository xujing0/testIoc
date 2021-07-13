package com.entity;

/**
 * 用于封装一个property标签
 */
public class Property {
    //属性名称
    private String name;
    //属性值
    private String value;
    //属性的引用
    private String ref;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
}

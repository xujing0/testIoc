package com.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于描述bean的定义
 */
public class BeanDefinition {
    //bean标签的id,唯一标识
    private String beanName;
    //bean所属class的相对路径
    private String className;
    //bean的作用域
    private String scope = "singleton";
    private List<Property> propertyList = new ArrayList<>();

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public List<Property> getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(List<Property> propertyList) {
        this.propertyList = propertyList;
    }
}

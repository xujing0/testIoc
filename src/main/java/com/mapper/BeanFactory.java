package com.mapper;

/**
 * 容器的基础接口
 * 提供最基本的功能
 */
public interface BeanFactory {

    //核心方法,获取bean对象
    Object getBean(String bean);
}

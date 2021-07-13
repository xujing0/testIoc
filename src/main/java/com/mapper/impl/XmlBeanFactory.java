package com.mapper.impl;


import com.entity.XmlBeanDefinitionReader;


/**
 * 继承核心实现类
 */
public class XmlBeanFactory extends DefaultListableBeanFactory {

    /**
     * 将解析配置文件 注册bean的所有工作交给reader对象
     */
    final XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader( this);

    /**
     * 构造器需要传入xml配置文件
     * @param configPath
     */
    public XmlBeanFactory(String configPath){
        //使用reader对象解析配置 注册bean
        this.xmlBeanDefinitionReader.loadBeanDefinitions(configPath);
    }


}

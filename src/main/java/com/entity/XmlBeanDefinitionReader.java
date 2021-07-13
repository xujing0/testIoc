package com.entity;

import com.mapper.impl.DefaultListableBeanFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

/**
 * 解析配置
 * 注册到容器中
 */
public class XmlBeanDefinitionReader {

    //核心beanFactory对象,用于将解析后的bean注册到beanFactory中
    final DefaultListableBeanFactory beanFactory;

    public XmlBeanDefinitionReader(DefaultListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * 根据传递的配置文件
     * 解析配置
     * 注册bean
     *
     * @param configPath
     */
    public void loadBeanDefinitions(String configPath) {
        //1.通过dom4j解析xml文件得到document文档
        Document document = doLoadDocument(configPath);
        //2.便利document每个bean标签
        Element rootElement = document.getRootElement();
        List<Element> list = rootElement.elements();
        for (Element element : list) {
            //解析每一个bean标签,封装成一个BeanDefinition对象
            BeanDefinition beanDefinition = parseBeanDefinition(element);
            //将beanDefinition和property对象封装到容器中
            beanFactory.registerBeanDefinition(beanDefinition);
        }
    }

    /**
     * 解析每一个bean标签,封装成一个BeanDefinition对象
     * 解析每一个bean标签下的property标签,封装成一个Property对象
     *
     * @param element
     * @return
     */
    BeanDefinition parseBeanDefinition(Element element) {
        BeanDefinition beanDefinition = new BeanDefinition();
        String beanName = element.attributeValue("id");
        String className = element.attributeValue("class");
        String scope = element.attributeValue("scope");
        beanDefinition.setBeanName(beanName);
        beanDefinition.setClassName(className);
        if (!"".equals(scope) && scope != null) {
            beanDefinition.setScope(scope);
        }
        List<Element> propertyList = element.elements("property");
        for (Element propertyEle : propertyList) {
            Property property = new Property();
            property.setName(propertyEle.attributeValue("name"));
            property.setValue(propertyEle.attributeValue("value"));
            property.setRef(propertyEle.attributeValue("ref"));
            beanDefinition.getPropertyList().add(property);
        }
        return beanDefinition;
    }

    /**
     * 使用dom4j解析xml文件,返回一个document对象
     *
     * @param configPath
     * @return
     */
    Document doLoadDocument(String configPath) {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(configPath);
        SAXReader sr = new SAXReader();
        try {
            return sr.read(is);
        } catch (DocumentException e) {
            e.printStackTrace();
            System.out.println("解析xml出现异常=====>" + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

    }
}

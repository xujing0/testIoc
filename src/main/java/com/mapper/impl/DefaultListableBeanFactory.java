package com.mapper.impl;

import com.entity.BeanDefinition;
import com.entity.Property;
import com.mapper.BeanFactory;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基础容器的核心实现
 * 定义beanDefinitionMap存储bean的定义
 * 定义singletonObjects存储bean的对象实例(scope为singleton)
 */
public class DefaultListableBeanFactory implements BeanFactory {

    //定义beanDefinitionMap存储bean的定义
    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    //定义singletonObjects存储bean的对象实例(scope为singleton)
    private Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    public void preInstaniceSingletons() {
        beanDefinitionMap.forEach(((beanName, beanDefinition) -> {
            String scope = beanDefinition.getScope();
            //判断单例 非抽象 不懒加载
            if ("singleton".equals(scope)) {
                this.getBean(beanName);
            }
        }));
    }

    /**
     * 实现getBean方法
     *
     * @param beanName
     * @return
     */
    @Override
    public Object getBean(String beanName) {
        //先从单例的map集合中获取,是否有指定的beanName对象
        Object singletonObj = singletonObjects.get(beanName);
        //有的话直接返回
        if (singletonObj != null) {
            return singletonObj;
        }
        //没有的话从注册集合中获取bean的定义对象
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        //没有的话抛异常
        if (beanDefinition == null) {
            throw new RuntimeException("NoSuchBeanDefinition : 你找的 " + beanName + " 对象 不存在");
        }
        //判断bean的作用域
        String scope = beanDefinition.getScope();
        if ("singleton".equals(scope)) {
            //createBean对象
            Object obj = createBean(beanDefinition);
            //存入单例集合
            singletonObjects.put(beanName, obj);
            //返回对象
            return obj;

        } else {
            //多例
            return createBean(beanDefinition);
        }
    }

    /**
     * 将bean注册到容器中去
     *
     * @param beanDefinition
     */
    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanDefinition.getBeanName(), beanDefinition);
    }

    Object createBean(BeanDefinition beanDefinition) {
        String className = beanDefinition.getClassName();
        Class<?> aClass = null;
        try {
            aClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("类未找到" + e.getMessage());
        }
        //创建对象
        Object obj;
        try {
            obj = aClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("创建对象失败" + e.getMessage());
        } catch (IllegalAccessException e) {
            throw new RuntimeException("非法访问" + e.getMessage());
        }
        //依赖注入
        List<Property> propertyList = beanDefinition.getPropertyList();
        for (Property property : propertyList) {
            String name = property.getName();
            String value = property.getValue();
            String ref = property.getRef();
            //属性名不为空,直接注入
            if (!"".equals(name) && name != null) {
                //value不为空,直接注入
                if (!"".equals(value) && value != null) {
                    Map<String, String> params = new HashMap<>();
                    params.put(name, value);
                    try {
                        BeanUtils.populate(obj, params);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new RuntimeException("非法访问" + e.getMessage());
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                        throw new RuntimeException("调用目标对象失败" + e.getMessage());
                    }
                }
                //如果配置的是ref需要获取其他对象注入
                if (ref != null && !"".equals(ref)) {
                    try {
                        BeanUtils.setProperty(obj, name, getBean(ref));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new RuntimeException("非法访问" + e.getMessage());
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                        throw new RuntimeException("调用目标对象失败" + e.getMessage());
                    }
                }
            }
        }
        return obj;
    }
}

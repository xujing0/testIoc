package com.test;


import com.entity.Student;
import com.mapper.BeanFactory;
import com.mapper.impl.XmlBeanFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestDemo {

    @Test
    public void test(){
        ApplicationContext ac=new ClassPathXmlApplicationContext("application.xml");
        Student student =(Student) ac.getBean("student");
        student.sayHello();
    }

    @Test
    public void testIoc(){
        //创建ioc容器
        BeanFactory beanFactory=new XmlBeanFactory("application.xml");
        //通过容器获取对象
        Student student =(Student) beanFactory.getBean("student");
        student.sayHello();
    }
}

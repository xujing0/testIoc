#### 面试加分项-源码

**阅读源码的好处**

```
1.通过源码更容易理解和记忆框架的特点及工作原理，可以避免一些有可能会遇到的坑。

2.体会设计模式，当研究某一个优秀框架源码时，会发现里面使用到了很多设计模式，这样可以提高自己在实际的开发中如何使用设计模式能力，让代码结构质量等有一个质的提高。

3.通过对源码的理解，可以让你在水平相当的面试者当中 脱颖而出.

4.研究源码并不需要源码研究的很透彻，才可以和面试官聊，就算你掌握的没那么扎实 可以虚心请教面试官
```

**建议关注的源码**

```
基础API：
String、StringBuffer、StringBuilder
ArrayList、LinkedList、HashSet、
HashMap、ConcurrentHashMap

框架源码:
SpringIOC  AOP: 1.面向切面编程 2.动态代理 3.怎么使用和场景
SpringMVC  运行流程
Mybatis    运行原理 
```

**源码难点**

```
源码比较枯燥需要极大的耐心和坚持力 
源码内容多且调用关系错综复杂
好的源码需要考虑的设计模式和扩展，会采用各种封装 给阅读带来了极大难度
读源码并不是一朝一夕的事，而是一个长期积累的过程。
通常喜欢读源码的人，都是因为内心有强大的求知欲，同时对自己的能力非常自信。


有这么多难点，这么困难我们是否还需要准备呢， 需要！！正是因为困难 才不是那些通过面试宝典就可以背的话术。而且我们课程中老师会梳理出源码的主线，采用完全和源码一致的类名设计带着大家手写源码的核心流程。 这样只需把老师实现的mini流程记住即可。大大降低了源码难度， 那如果同学经过了这门课程对该框架的源码产生了极大兴趣。 也可以跟深入的去了解源码。 源码会成为你面试中的一个亮点。
```

**学习源码的技巧**

```
1.了解框架的基本使用
2.准备好演示案例（hello world）
3.通过debug跟踪调用流程（梳理出主线流程）
4.合理使用跳转 （F8  F7 F9 ）
5.打印日志
6.大胆猜测再验证（看不懂的方法，先利用搜索引擎看看别人是怎么说的，然后再看内部实现去印证）
```

#### IOC基础回顾

如: 有3个实体类，关系如下:

学生对象  依赖  班级对象  

班级对象 依赖  教师对象

```java
/**
 * 学生类  
 * 学生类依赖班级对象
 * 并提供 sayHello() 方法
 * @作者 itcast
 * @创建日期 2020/3/7 19:46
 **/
public class Student {
    private String name;
    private TClass tClass;
    public void sayHello(){
        System.out.println("大家好,我是" +this.name+" 我的班级是==>"+tClass.getCname() + " 我的老师是"+tClass.getTeacher().getTname());
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public TClass gettClass() {
        return tClass;
    }
    public void settClass(TClass tClass) {
        this.tClass = tClass;
    }
}
```

```java
/**
 * 班级类
 * 班级类依赖教师对象
 * @作者 itcast
 * @创建日期 2020/3/7 19:45
 **/
public class TClass {
    private String cname;// 班级名称
    private Teacher teacher; // 老师
    public String getCname() {
        return cname;
    }
    public void setCname(String cname) {
        this.cname = cname;
    }
    public Teacher getTeacher() {
        return teacher;
    }
    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}
```

```java
/**
 * 教师类
 * @作者 itcast
 * @创建日期 2020/3/7 19:44
 **/
public class Teacher {
    private String tname;// 老师名称
    public String getTname() {
        return tname;
    }
    public void setTname(String tname) {
        this.tname = tname;
    }
}
```

如果需要执行学生对象的sayHello方法, 那么需要我们依次创建 学生对象，所在班级对象，班级的教师对象，并需要将他们的关系维护好。

```java
    /**
     * 不使用IOC的解决方案
     */
    @Test
    public void test01(){
        // 管理学生对象
        Student student = new Student();
        student.setName("xiaoming");

        // 管理班级对象
        TClass tClass = new TClass();
        tClass.setCname("3年2班");

        // 管理教师对象
        Teacher teacher = new Teacher();
        teacher.setTname("陈老师");

        // 设置学生所属班级
        student.settClass(tClass);

        // 设置班级任课老师
        tClass.setTeacher(teacher);

        // 调用学生的sayHello方法
        student.sayHello();
    }
```

可以看到，我们只想调用学生的业务方法，但是我们要准备好所涉及的所有对象， 并且将这些代码全部硬编码到了程序中。  

这样会有两个明显的问题 

1. 工作效率，程序员写程序时对对象的构建及依赖关系编写了大量代码
2. 耦合问题,   后续如果这些对象的配置产生了变化，散落在程序中的代码都需要我们维护

如: 如果我们使用 JDBC 要创建一堆相关的API对象

​     如果我们使用Mybatis 要创建一堆相关的API对象

​    。。。。。

但实际上他们都有一套固定的写法，程序员在写他们的基础创建就是在浪费时间，有没有一种办法来帮助我们管理这些对象呢?  SpringIOC就可以!!

**SpringIOC核心特性:**

```
IoC：Inverse of Control（控制反转）:

将原本在程序中手动创建对象的控制权，交由Spring框架来管理。

DI：Dependency Injection（依赖注入）：

在我们创建对象的过程中，把对象依赖的属性注入到我们的类中。

```



**SpringIOC体系结构:**

Spring在实现IOC容器时，一些重要的类， 黑色部分重点记忆:

| 序号 | 名称                                   |                             描述                             |
| ---- | -------------------------------------- | :----------------------------------------------------------: |
| 1    | **BeanFactory**                        |   【重点】Spring容器的顶级接口，定义了容器的最基本的功能。   |
| 2    | **XmlBeanFactory**                     |                 最基础的容器实现类，现已废弃                 |
| 3    | ListableBeanFactory                    | BeanFactory接口中的getBean方法只能获取单个对象。ListableBeanFactory可以获取多个对象 |
| 4    | HierarchicalBeanFactory                | 在一个spring应用中，支持有多个BeanFactory，并且可以设置为它们的父子关系。比如ssm框架整合中的两个ioc容器 |
| 5    | **ApplicationContext**                 | 【重点】Spring高级容器,是我们经常使用的容器。它同时继承了ListableBeanFactory和HierarchicalBeanFactory接口 |
| 6    | ConfigurableApplicationContext         | 支持更多系统配置的工厂接口。比如：conversionService、environment、systemProperties、systemEnvironment |
| 7    | **DefaultListableBeanFactory**         |                   beanFactory的核心实现类                    |
| 8    | **ClassPathXmlApplicationContext**     | 【重点】项目中，直接使用的工厂实现类。从类的根路径下加载配置文件，创建spring ioc容器 |
| 9    | FileSystemXmlApplicationContext        | 可以加载磁盘任意路径下的配置文件，实现容器的实例化。（必须有访问权限） |
| 10   | **AnnotationConfigApplicationContext** |     【重点】扫描配置注解，根据注解配置实现容器的实例化。     |



**基于Xml的方式配置Bean**

引入依赖:

```xml
<dependency>
     <groupId>org.springframework</groupId>
     <artifactId>spring-context</artifactId>
     <version>4.3.18.RELEASE</version>
</dependency>

```

配置bean:

~~~<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <!-- 配置student -->
    <!-- scope 对象作用域: 单例 原型 -->
    <!-- 对象的构建方式:  默认构造器  有参构造器  工厂方法  -->
    <!-- 依赖的注入方式: setter属性  构造器参数  注解注入 -->
    <bean id="student" class="com.itcast.demo01.bean.Student" scope="singleton" >
        <property name="name" value="小明"></property>
        <property name="tClass" value="tClass"></property>
    </bean>
    <!-- 配置class -->
    <bean id="tClass" class="com.itcast.demo01.bean.TClass" scope="singleton" >
        <property name="cname" value="3年2班"></property>
        <property name="teacher" ref="teacher"></property>
    </bean>
    <!-- 配置teacher -->
    <bean id="teacher" class="com.itcast.demo01.bean.Teacher" scope="singleton" >
        <property name="tname" value="陈老师"></property>
    </bean>
</beans>

创建IOC容器，并获取对象:

```java
    /**
     * IOC 和 DI
     * 使用IOC的解决方案
     */
    @Test
    public void test02(){
        // 构建容器
        ApplicationContext  context = new ClassPathXmlApplicationContext("demo01/applicationContext.xml");
        // 获取对象
        Student student = (Student)context.getBean("student");
        // 调用对象的业务方法
        student.sayHello();
    }

~~~

运行得到了 正确的输出结果, 容器帮助我们管理了对象，让我们使用起来非常爽。 当今的开发中，你可以不用SpringMVC、你可以不用Mybatis  不过还没哪个项目不使用Spring。

不过既然IOC容器的使用这么广泛，那么面试中就会经常出现一些直击灵魂的面试题?

​	如:    你了解IOC容器底层是如何实现的吗?

​                 你了解容器getBean时的工作流程吗?

​                 你知道容器中配置两个相同ID名称的bean会怎样吗?

​                 你知道什么是Bean的循环依赖吗?

​		 你知道IOC容器Bean的完整生命周期吗?



#### 手动实现IOC容器的设计

##### 需要实现的IOC功能：

- 可以通过xml配置bean信息
- 可以通过容器getBean获取对象
- 能够根据Bean的依赖属性实现依赖注入
- 可以配置Bean的单例多例

##### 实现简易IOC设计的类

| 类/接口                    | 说明                                                         |
| -------------------------- | ------------------------------------------------------------ |
| BeanFactory                | IOC容器的基础接口，提供IOC容器的基本功能                     |
| DefaultListableBeanFactory | IOC容器的核心实现类，提供多个map集合用来存储bean的定义对象，提供getBean方法的核心实现， |
| XmlBeanFactory             | IOC容器的实现类，基于xml构建bean信息                         |
| XmlBeanDefinitionReader    | 用于解析xml信息，并提供解析Document文档的方法，并将解析到的BeanDefinition对象注册到核心容器中 |
| BeanDefinition             | 封装Bean的定义对象，如: bean的id class,scope ..等等          |
| Property                   | 封装Bean所关联依赖的属性                                     |

##### 类之间关系模型

![1594192931449](E:/%E5%AD%A6%E4%B9%A0/%E5%8A%A0%E5%BC%BA/day01_IOC%E6%BA%90%E7%A0%81&SSM%E9%9D%A2%E8%AF%95%E9%A2%98/%E6%89%8B%E5%86%99IOC%E5%AE%B9%E5%99%A8/assets/1594192931449.png)

#### 前期准备

##### 创建maven项目引入依赖

```xml
<dependencies>
    <!-- 解析xml -->
    <dependency>
        <groupId>dom4j</groupId>
        <artifactId>dom4j</artifactId>
        <version>1.1</version>
    </dependency>
    <!-- BeanUtils    -->
    <dependency>
        <groupId>commons-beanutils</groupId>
        <artifactId>commons-beanutils</artifactId>
        <version>1.9.3</version>
    </dependency>
</dependencies>

```

##### 准备3个bean的实体类

```java
/**
 * 学生类  
 * 学生类依赖班级对象
 * 并提供 sayHello() 方法
 * @作者 itcast
 * @创建日期 2020/3/7 19:46
 **/
public class Student {
    private String name;
    private TClass tClass;
    public void sayHello(){
        System.out.println("大家好,我是" +this.name+" 我的班级是==>"+tClass.getCname() + " 我的老师是"+tClass.getTeacher().getTname());
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public TClass gettClass() {
        return tClass;
    }
    public void settClass(TClass tClass) {
        this.tClass = tClass;
    }
}

```

```java
/**
 * 班级类
 * 班级类依赖教师对象
 * @作者 itcast
 * @创建日期 2020/3/7 19:45
 **/
public class TClass {
    private String cname;// 班级名称
    private Teacher teacher; // 老师
    public String getCname() {
        return cname;
    }
    public void setCname(String cname) {
        this.cname = cname;
    }
    public Teacher getTeacher() {
        return teacher;
    }
    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}

```

```java
/**
 * 教师类
 * @作者 itcast
 * @创建日期 2020/3/7 19:44
 **/
public class Teacher {
    private String tname;// 老师名称
    public String getTname() {
        return tname;
    }
    public void setTname(String tname) {
        this.tname = tname;
    }
}

```

##### xml配置对象

配置学生对象:  小明

依赖班级对象:  3年2班

依赖教师对象:  陈老师

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans>
    <!-- 配置IOC容器要管理的对象   bean作用域: 单例  原型 -->
    <bean id="student" class="com.itcast.ioc.bean.Student" scope="singleton" lazy-init="true">
        <!-- 依赖注入:   属性注入    构造器注入   注解注入-->
        <property name="name" value="小明"></property>
        <property name="tClass" ref="tclass"></property>
    </bean>
    <bean id="tclass" class="com.itcast.ioc.bean.TClass">
        <property name="cname" value="3年2班"></property>
        <property name="teacher" ref="teacher"></property>
    </bean>
    <bean id="teacher" class="com.itcast.ioc.bean.Teacher">
        <property name="tname" value="陈老师"></property>
    </bean>
</beans>

```

#### mini-IOC容器-定义类

##### 定义BeanFactory

```java
/**
 * 容器的基础接口
 * 提供容器最基本的功能
 */
public interface BeanFactory {
    // 核心方法 获取对象
    Object getBean(String beanName);
}

```

##### 定义DefaultListableBeanFactory

```java
package com.itcast.ioc.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基础容器的核心实现
 * 提供 beanDefinitionMap 存储bean的定义
 * 提供 singletonObjects 存储bean的对象实例
 * @作者 itcast
 * @创建日期 2020/7/8 15:37
 **/
public class DefaultListableBeanFactory implements BeanFactory {
    // 提供 beanDefinitionMap 存储bean的定义
    private Map<String,BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    // 提供 singletonObjects 存储bean的对象实例 (scope为singleton的)
    private Map<String,Object> singletonObjects = new ConcurrentHashMap<>();
    /**
     * 实现getBean方法
     * @param beanName
     * @return
     */
    @Override
    public Object getBean(String beanName) {
        return null;
    }
    /**
     * 将bean注册到容器中
     * @param beanDefinition
     */
    public void registerBeanDefinition(BeanDefinition beanDefinition){
        beanDefinitionMap.put(beanDefinition.getBeanName(),beanDefinition);
    }
}

```

##### 定义BeanDefnition

```java
/**
 * 用于描述Bean的定义
 * @作者 itcast
 * @创建日期 2020/7/8 15:41
 **/
public class BeanDefinition {
    private String beanName; // bean标签的ID 作为bean的唯一标识
    private String className; // bean的所属class
    private String scope = "singleton";  // bean的scope作用域
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

```

##### 定义Property

```java
/**
 * 用于封装一个property标签
 * 属性数据
 * @作者 itcast
 * @创建日期 2020/7/8 15:44
 **/
public class Property {
    private String name; // 属性名称
    private String value; // 属性的值
    private String ref; // 属性的引用
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

```

##### 定义XmlBeanFactory

```java
/**
 * 继承核心实现类
 * 基于xml配置bean的实现类
 * @作者 itcast
 * @创建日期 2020/7/8 15:47
 **/
public class XmlBeanFactory extends DefaultListableBeanFactory {
    /**
     * 将解析配置文件 注册bean的所有工作交给reader对象
     */
    final XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(this);
    /**
     * 构造器需要传入xml配置文件
     * @param configPath
     */
    public XmlBeanFactory(String configPath) {
        // 使用reader对象 解析配置  注册Bean
        this.xmlBeanDefinitionReader.loadBeanDefinitions(configPath);
    }
}

```

##### 定义XmlBeanDefinitionReader

```java
/**
 * 解析配置
 * 注册到容器中
 * @作者 itcast
 * @创建日期 2020/7/8 15:51
 **/
public class XmlBeanDefinitionReader {
    // 核心beanfactory对象 用于将解析后的bean注册到beanfactory中
    final DefaultListableBeanFactory beanFactory;
    public XmlBeanDefinitionReader(DefaultListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
    /**
     * 根据传递的配置文件
     * 解析配置
     * 注册bean
     * @param configPath
     */
    void loadBeanDefinitions(String configPath){

    }
}

```

#### mini-IOC容器--解析注册

##### 实现步骤

```
1. 通过dom4j解析xml得到Document文档
2. 遍历文档所有Bean标签
3. 解析每一个Bean标签 封装一个BeanDefinition对象
4. 解析每一个Bean标签下的所有Property标签 封装一个Property对象
5. 将BeanDefinition和Property对象注册到容器

```

##### 实现xml解析及bean注册

```java
/**
 * 解析配置
 * 注册到容器中
 * @作者 itcast
 * @创建日期 2020/7/8 15:51
 **/
public class XmlBeanDefinitionReader {
    // 核心beanfactory对象 用于将解析后的bean注册到beanfactory中
    final DefaultListableBeanFactory beanFactory;
    public XmlBeanDefinitionReader(DefaultListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
    /**
     * 根据传递的配置文件
     * 解析配置
     * 注册bean
     * @param configPath
     */
    void loadBeanDefinitions(String configPath){
        // 1. 通过dom4j解析xml得到Document文档
        Document document = doLoadDocument(configPath);
        // 2. 遍历文档所有Bean标签
        Element rootElement = document.getRootElement();
        List<Element> list = rootElement.selectNodes("//bean");
        for (Element element : list) {
            // 3. 解析每一个Bean标签 封装一个BeanDefinition对象
            BeanDefinition beanDefinition = parseBeanDefinition(element);
            // 5. 将BeanDefinition和Property对象注册到容器
            beanFactory.registerBeanDefinition(beanDefinition);
        }
    }
    /**
     * 3. 解析每一个Bean标签 封装一个BeanDefinition对象
     * 4. 解析每一个Bean标签下的所有Property标签 封装一个Property对象
     */
    BeanDefinition parseBeanDefinition(Element element){
        BeanDefinition beanDefinition = new BeanDefinition();
        String beanName = element.attributeValue("id");
        String className = element.attributeValue("class");
        String scope = element.attributeValue("scope");
        beanDefinition.setBeanName(beanName);
        beanDefinition.setClassName(className);
        if(scope!=null&&!"".equals(scope)){
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
     * 解析Document文档
     * @param configPath
     * @return
     */
    Document doLoadDocument(String configPath){
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(configPath);
        SAXReader saxReader = new SAXReader();
        try {
            return saxReader.read(inputStream);
        } catch (DocumentException e) {
            e.printStackTrace();
            System.out.println("解析xml出现异常==>"+e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}

```

##### 准备测试类

```java
/**
 * 测试类
 * @作者 itcast
 * @创建日期 2020/7/8 16:19
 **/
public class IocTest {
    public static void main(String[] args) {
        // 创建IOC容器
        BeanFactory beanFactory = new XmlBeanFactory("applicationContext.xml");
        // 通过容器获取对象
        Student student = (Student)beanFactory.getBean("student");
        // 调用对象sayHello方法
        student.sayHello();
    }
}

```

##### 断点查看注册情况

可以看到我们配置的xml内容 已经解析成了BeanDefinition对象，注册到了核心容器的map中

![1594196599561](E:/%E5%AD%A6%E4%B9%A0/%E5%8A%A0%E5%BC%BA/day01_IOC%E6%BA%90%E7%A0%81&SSM%E9%9D%A2%E8%AF%95%E9%A2%98/%E6%89%8B%E5%86%99IOC%E5%AE%B9%E5%99%A8/assets/1594196599561.png)

#### mini-IOC容器-getBean

##### 实现步骤

```
1. 先从单例的map集合中获取 是否有指定beanName的对象
	有直接返回
	没有下一步
2. 从注册集合中获取bean的定义对象
	有下一步
	没有抛异常NoSuchBeanDefinition
3. 判断bean的scope作用域
	singleton单例
		createBean对象
		存入单例集合
		返回对象
	prototype多例
		createBean对象
		返回对象
4. createBean方法
	获取BeanDefinition中的className
	通过反射API得到Class对象
	通过反射API得到bean实例
	获取BeanDefinition中依赖的属性列表
	实现属性的依赖注入

```

##### 实现getBean及createBean方法

```java
/**
 * 基础容器的核心实现
 * 提供 beanDefinitionMap 存储bean的定义
 * 提供 singletonObjects 存储bean的对象实例
 * @作者 itcast
 * @创建日期 2020/7/8 15:37
 **/
public class DefaultListableBeanFactory implements BeanFactory {
    // 提供 beanDefinitionMap 存储bean的定义
    private Map<String,BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    // 提供 singletonObjects 存储bean的对象实例 (scope为singleton的)
    private Map<String,Object> singletonObjects = new ConcurrentHashMap<>();
    /**
     * 实现getBean方法
     * @param beanName
     * @return
     */
    @Override
    public Object getBean(String beanName) {
//        1. 先从单例的map集合中获取 是否有指定beanName的对象
        Object singletonObj = singletonObjects.get(beanName);
//                有直接返回
        if(singletonObj!=null){
            return singletonObj;
        }
//                没有下一步
//        2. 从注册集合中获取bean的定义对象
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
//                有下一步
//        没有抛异常NoSuchBeanDefinition
        if(beanDefinition==null){
            throw new RuntimeException("NoSuchBeanDefinition : 你找的 "+beanName+" 对象 不存在");
        }
//        3. 判断bean的scope作用域
        String scope = beanDefinition.getScope();
//                singleton单例
        if("singleton".equals(scope)){
//        createBean对象
            Object obj = createBean(beanDefinition);
//        存入单例集合
            singletonObjects.put(beanName,obj);
//        返回对象
            return obj;
        }else {
//        prototype多例
//        createBean对象
            return createBean(beanDefinition);
//        返回对象
        }
    }
    /**
     * //4. createBean方法
     * //获取BeanDefinition中的className
     * //通过反射API得到Class对象
     * //通过反射API得到bean实例
     * //获取BeanDefinition中依赖的属性列表
     * //实现属性的依赖注入
     * 创建对象
     * @param beanDefinition
     * @return
     */
    Object createBean(BeanDefinition beanDefinition){
        String className = beanDefinition.getClassName();
        Class<?> aClass;
        try {
            aClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("类未找到"+e.getMessage());
        }
        // 创建对象:
        Object obj;
        try {
            obj = aClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException("创建对象失败"+e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("非法访问"+e.getMessage());
        }
        // 依赖注入
        List<Property> propertyList = beanDefinition.getPropertyList();
        for (Property property : propertyList) {
            String name = property.getName();
            String value = property.getValue();
            String ref = property.getRef();
            // 属性名不为空 进行注入
            if(name!=null&&!"".equals(name)){
                // 如果配置的是value值 直接注入
                if(value!=null&&!"".equals(value)){
                    Map<String,String> params = new HashMap<>();
                    params.put(name,value);
                    try {
                        BeanUtils.populate(obj,params);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new RuntimeException("非法访问"+e.getMessage());
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                        throw new RuntimeException("调用目标对象失败"+e.getMessage());
                    }
                }
                // 如果配置的是ref需要获取其它对象注入
                if(ref!=null&&!"".equals(ref)){
                    try {
                        BeanUtils.setProperty(obj,name,getBean(ref));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new RuntimeException("非法访问"+e.getMessage());
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                        throw new RuntimeException("调用目标对象失败"+e.getMessage());
                    }
                }
            }
        }
        return obj;
    }
    /**
     * 将bean注册到容器中
     * @param beanDefinition
     */
    public void registerBeanDefinition(BeanDefinition beanDefinition){
        beanDefinitionMap.put(beanDefinition.getBeanName(),beanDefinition);
    }
}

```

#### mini-IOC容器-单例对象初始化

##### DefaultListableBeanFactory增加初始化方法

```java
public void preInstaniceSingletons(){
        beanDefinitionMap.forEach((beanName,beanDefinition)->{
            String scope = beanDefinition.getScope();
            // 判断单例  非抽象   不懒加载
            if("singleton".equals(scope)){
                this.getBean(beanName);
            }
        });
    }

```

##### XmlBeanFactory增加单例对象初始化

```java
public XmlBeanFactory(String configPath) {
   // 使用reader对象 解析配置  注册Bean
   this.xmlBeanDefinitionReader.loadBeanDefinitions(configPath);
   // 初始化单例对象
   this.preInstaniceSingletons();
}

```

#### mini-IOC容器-测试和小结

**测试对象能否获取**

![1594198870174](E:/%E5%AD%A6%E4%B9%A0/%E5%8A%A0%E5%BC%BA/day01_IOC%E6%BA%90%E7%A0%81&SSM%E9%9D%A2%E8%AF%95%E9%A2%98/%E6%89%8B%E5%86%99IOC%E5%AE%B9%E5%99%A8/assets/1594198870174.png)

**查看bean的注册及单例集合信息**

可以通过变更scope的值查看对应的变化

![1594198927266](E:/%E5%AD%A6%E4%B9%A0/%E5%8A%A0%E5%BC%BA/day01_IOC%E6%BA%90%E7%A0%81&SSM%E9%9D%A2%E8%AF%95%E9%A2%98/%E6%89%8B%E5%86%99IOC%E5%AE%B9%E5%99%A8/assets/1594198927266.png)



**IOC容器的初始化流程小结**

```
IOC容器初始化的流程: 
	1. 配置bean
		xml配置 或者 注解配置
		如: xml通过bean标签描述一个bean
	2. 容器解析配置
		读取xml内容，并得到一个Document对象
		解析Document对象，遍历bean标签的节点
		将每一个Bean标签封装成一个BeanDefinition对象
	3. 注册到容器
		将bean的定义对象 存储到容器中的map集合中
		map集合:  在容器的核心实现类 DefaultListableBeanFactory中, ConcurrentHashMap 
		以 bean标签中配置的id作为key
		以 beanDefinition对象作为value存储到map集合中
	4. 初始化所有单例对象
	    如果是非抽象 、 并且不是懒加载的单例对象会被立刻创建
		创建出来的单例对象会存储到singletonObjects map集合中 也是一个ConcurrentHashMap

```

**IOC容器  getBean方法流程小结**

```
IOC容器getBean流程: 
	1. 判断单例map集合中是否包含要找的对象
		包含 直接返回不
		
	2. 判断父容器是否包含指定的bean
		如果包含 调用父容器的getBean方法
		
	3. 从bean的注册map中，获取bean的定义对象
		没获取到抛异常 NoSuchBeanDefinition
		
	4. 从beanDefinition获取ClassName,根据ClassName得到Class对象
	5. 创建对象
		判断是否配置 factory-method 如果配置调用工厂方法创建对象
		判断是否配置 构造参数  如果配置根据构造参数选择指定的构造器创建
		如果没配置的话  调用无参构造器创建
	6. 属性依赖注入
		会根据配置的property标签知道有哪些属性	

```



**在看一下开头我们提到过的面试题, 能否有自己的话术去应对呢?**

你了解IOC容器是如何实现的吗?

你了解容器getBean时的工作流程吗?

你知道容器中配置两个相同ID名称的bean会怎样吗?

你知道什么是Bean的循环依赖吗?

你知道IOC容器Bean的完整生命周期吗?


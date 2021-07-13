package com.entity;

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

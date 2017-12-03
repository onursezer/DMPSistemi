package com.bitirme2.onursezer.dmpsistemi;

/**
 * Created by OnurSezer on 31.10.2017.
 */

public class ClassBean {

    private String className;
    private String classBranch;
    private String classId;
    private User teacher;

    public ClassBean() {}

    public ClassBean(String className, String classBranch, String classId, User teacher) {
        this.className = className;
        this.classBranch = classBranch;
        this.classId = classId;
        this.teacher = teacher;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassBranch() {
        return classBranch;
    }

    public void setClassBranch(String classBranch) {
        this.classBranch = classBranch;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

}

package com.bitirme2.onursezer.dmpsistemi;

/**
 * Created by OnurSezer on 27.11.2017.
 */

public class MapClassAndTeacher {

    private String teacherMail;
    private String classID;

    public MapClassAndTeacher() {
    }

    public MapClassAndTeacher(String teacherMail, String classID) {
        this.teacherMail = teacherMail;
        this.classID = classID;
    }

    public String getTeacherMail() {
        return teacherMail;
    }

    public void setTeacherMail(String teacherMail) {
        this.teacherMail = teacherMail;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }
}

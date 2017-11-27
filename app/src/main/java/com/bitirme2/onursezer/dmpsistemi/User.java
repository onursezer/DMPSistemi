package com.bitirme2.onursezer.dmpsistemi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OnurSezer on 27.10.2017.
 */

public class User {

    private String name;
    private String surname;
    private String email;
    private List<ClassBean> studentClasses;
    //private List<String> teacherClasses;

    public User() {}

    public User(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        studentClasses = new ArrayList<>();
        //teacherClasses = new ArrayList<>();
    }

    public List<ClassBean> getStudentClasses() {
        return studentClasses;
    }

    public void setStudentClasses(List<ClassBean> studentClasses) {
        this.studentClasses = studentClasses;
    }

/*
    public List<String> getTeacherClasses() {
        return teacherClasses;
    }

    public void setTeacherClasses(List<String> teacherClasses) {
        this.teacherClasses = teacherClasses;
    }
*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Kullanici{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

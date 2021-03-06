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
    private List<ClassInfo> studentClasses;

    public User() {}

    public User(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        studentClasses = new ArrayList<>();
    }

    public List<ClassInfo> getStudentClasses() {
        return studentClasses;
    }

    public void setStudentClasses(List<ClassInfo> studentClasses) {
        this.studentClasses = studentClasses;
    }
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

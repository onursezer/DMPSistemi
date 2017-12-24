package com.bitirme2.onursezer.dmpsistemi;

import java.util.List;

/**
 * Created by OnurSezer on 3.12.2017.
 */

public class MapClassAndStudents {

    private String classId;
    private List<StudentInfo> list;

    public MapClassAndStudents() {
    }

    public MapClassAndStudents(String classId) {
        this.classId = classId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public List<StudentInfo> getList() {
        return list;
    }

    public void setList(List<StudentInfo> list) {
        this.list = list;
    }
}

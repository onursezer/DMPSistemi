package com.bitirme2.onursezer.dmpsistemi;

import java.util.List;

/**
 * Created by OnurSezer on 23.12.2017.
 */

public class MapClassAndHomework {

    private String classId;
    private List<Homework> list;

    public MapClassAndHomework() {
    }

    public MapClassAndHomework(String classId) {
        this.classId = classId;
        this.list = list;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public List<Homework> getList() {
        return list;
    }

    public void setList(List<Homework> list) {
        this.list = list;
    }

}

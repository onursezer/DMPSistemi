package com.bitirme2.onursezer.dmpsistemi;

import java.util.List;

/**
 * Created by OnurSezer on 31.12.2017.
 */

public class MapAttendanceAndClass {

    private String classId;
    private List<Attendance> list;
    private List<AttendanceStudent> list2;

    public MapAttendanceAndClass() {
    }

    public MapAttendanceAndClass(String classId) {
        this.classId = classId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public List<Attendance> getList() {
        return list;
    }

    public void setList(List<Attendance> list) {
        this.list = list;
    }

    public List<AttendanceStudent> getList2() {
        return list2;
    }

    public void setList2(List<AttendanceStudent> list2) {
        this.list2 = list2;
    }
}

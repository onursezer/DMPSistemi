package com.bitirme2.onursezer.dmpsistemi;

/**
 * Created by OnurSezer on 31.12.2017.
 */

public class AttendanceStudent {

    private StudentInfo student;
    private String attendace;

    public AttendanceStudent() {
    }

    public AttendanceStudent(StudentInfo student, String attendace) {
        this.student = student;
        this.attendace = attendace;
    }

    public StudentInfo getStudent() {
        return student;
    }

    public void setStudent(StudentInfo student) {
        this.student = student;
    }

    public String getAttendace() {
        return attendace;
    }

    public void setAttendace(String attendace) {
        this.attendace = attendace;
    }
}

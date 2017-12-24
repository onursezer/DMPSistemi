package com.bitirme2.onursezer.dmpsistemi;

/**
 * Created by OnurSezer on 24.12.2017.
 */

public class HomeworkInfo {

    private String score;
    private StudentInfo studentInfo;
    private String link;

    public HomeworkInfo() {
    }

    public HomeworkInfo(String score, StudentInfo studentInfo, String link) {
        this.score = score;
        this.studentInfo = studentInfo;
        this.link = link;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public StudentInfo getStudentInfo() {
        return studentInfo;
    }

    public void setStudentInfo(StudentInfo studentInfo) {
        this.studentInfo = studentInfo;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}

package com.bitirme2.onursezer.dmpsistemi;

import java.util.List;

/**
 * Created by OnurSezer on 27.12.2017.
 */

public class MapHomeworkAndStudent {

    private String hwID;
    private List<HomeworkInfo> list;

    public MapHomeworkAndStudent() {
    }

    public MapHomeworkAndStudent(String hwID) {
        this.hwID = hwID;
    }

    public String getHwID() {
        return hwID;
    }

    public void setHwID(String hwID) {
        this.hwID = hwID;
    }

    public List<HomeworkInfo> getList() {
        return list;
    }

    public void setList(List<HomeworkInfo> list) {
        this.list = list;
    }
}

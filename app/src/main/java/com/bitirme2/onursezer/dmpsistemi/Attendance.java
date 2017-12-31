package com.bitirme2.onursezer.dmpsistemi;

import java.util.Calendar;

/**
 * Created by OnurSezer on 31.12.2017.
 */

public class Attendance {

    private String txtQRcode;
    private String openTime;
    private String date;
    private String time;

    public Attendance() {
    }

    public Attendance(String txtQRcode, String openTime) {
        this.txtQRcode = txtQRcode;
        Calendar cal = Calendar.getInstance();
        String second = new Integer(cal.get(Calendar.SECOND)).toString();
        String minute = new Integer(cal.get(Calendar.MINUTE)).toString();
        String hour = new Integer(cal.get(Calendar.HOUR)).toString();
        if(second.length() == 1)
            second = "0" + second;
        if(minute.length() == 1)
            minute = "0" + minute;
        if(hour.length() == 1)
            hour = "0" + hour;
        String time = hour + ":" + minute + ":" +second;

        String month = new Integer(cal.get(Calendar.MONTH) + 1).toString();
        String year = new Integer(cal.get(Calendar.YEAR)).toString();
        String dayofmonth = new Integer(cal.get(Calendar.DAY_OF_MONTH)).toString();
        if(month.length() == 1)
            month = "0" + month;
        if(dayofmonth.length() == 1)
            dayofmonth = "0" + dayofmonth;

        String date = dayofmonth + "/" + month + "/" + year;

        this.date = date;
        this.time = time;
        this.openTime = openTime;

    }

    public String getTxtQRcode() {
        return txtQRcode;
    }

    public void setTxtQRcode(String txtQRcode) {
        this.txtQRcode = txtQRcode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }
}

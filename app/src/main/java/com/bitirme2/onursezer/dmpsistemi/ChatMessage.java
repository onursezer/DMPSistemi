package com.bitirme2.onursezer.dmpsistemi;

import java.util.Calendar;

/**
 * Created by OnurSezer on 20.11.2017.
 */

public class ChatMessage {
    private String messageText;
    private String messageUser;
    private String messageTime;

    public ChatMessage(String messageText, String messageUser) {
        this.messageText = messageText;
        this.messageUser = messageUser;

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

        String month = new Integer(cal.get(Calendar.MONTH)).toString();
        String year = new Integer(cal.get(Calendar.YEAR)).toString();
        String dayofmonth = new Integer(cal.get(Calendar.DAY_OF_MONTH)).toString();
        if(month.length() == 1)
            month = "0" + month;
        if(dayofmonth.length() == 1)
            dayofmonth = "0" + dayofmonth;

        String date = dayofmonth + "/" + month + "/" + year;
        messageTime = date + " (" + time + ")";
       /* TimeZone.setDefault(TimeZone.getTimeZone("Turkey"));
        messageTime = DateFormat.format("dd-MM-yyyy (HH:mm:ss)",new Date().getTime()).toString();*/
    }

    public ChatMessage() {
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }
}

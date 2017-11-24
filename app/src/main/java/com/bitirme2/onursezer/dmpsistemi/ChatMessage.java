package com.bitirme2.onursezer.dmpsistemi;

import android.text.format.DateFormat;

import java.util.Date;
import java.util.TimeZone;

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
        TimeZone.setDefault(TimeZone.getTimeZone("Turkey"));
        messageTime = DateFormat.format("dd-MM-yyyy (HH:mm:ss)",new Date().getTime()).toString();
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

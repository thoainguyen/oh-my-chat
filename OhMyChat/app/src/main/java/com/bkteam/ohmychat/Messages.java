package com.bkteam.ohmychat;

public class Messages {
    private String from, message, type, time;

    public Messages(String from, String message, String type, String time) {
        this.from = from;
        this.message = message;
        this.type = type;
        this.time = time;
    }

    public Messages() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}

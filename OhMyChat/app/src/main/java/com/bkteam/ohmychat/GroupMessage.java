package com.bkteam.ohmychat;

public class GroupMessage {
    private String reName, reMsg, reDate, reTime, seMsg, seDate, seTime;
    public GroupMessage(String reName, String reMsg, String reDate, String reTime, String seMsg, String seDate, String seTime ) {
        this.reName=reName;
        this.reMsg=reMsg;
        this.reDate=reDate;
        this.reTime=reTime;

        this.seMsg=seMsg;
        this.seDate=seDate;
        this.seTime=seTime;
    }

    public String getReName() {
        return reName;
    }

    public String getReMsg() {
        return reMsg;
    }

    public String getReDate() {
        return reDate;
    }

    public String getReTime() {
        return reTime;
    }

    public String getSeMsg() {
        return seMsg;
    }

    public String getSeDate() {
        return seDate;
    }

    public String getSeTime() {
        return seTime;
    }
}

package com.bkteam.ohmychat;

public class GroupMess {
    String re_name;
    String re_mess;
    String re_date;
    String re_time;

    String se_mess;
    String se_date;
    String se_time;

    public GroupMess(String re_name, String re_mess, String re_date, String re_time, String se_mess, String se_date, String se_time ) {
        this.re_name=re_name;
        this.re_mess=re_mess;
        this.re_date=re_date;
        this.re_time=re_time;

        this.se_mess=se_mess;
        this.se_date=se_date;
        this.se_time=se_time;

    }

    public String getRe_name() {
        return re_name;
    }

    public String getRe_mess() {
        return re_mess;
    }

    public String getRe_date() {
        return re_date;
    }

    public String getRe_time() {
        return re_time;
    }

    public String getSe_mess() {
        return se_mess;
    }

    public String getSe_date() {
        return se_date;
    }

    public String getSe_time() {
        return se_time;
    }
}

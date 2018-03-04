package com.app.ahaspora.models;

/**
 * Created by aangjnr on 14/02/2018.
 */

public class Time {


    String start;
    String end;


    String expires;
    String deadline;


    public Time(){}

    public Time(String s, String e){this.start = s; this.end = e;}

    public Time(String e, String d, Boolean nullable){this.expires = e; this.deadline = d;}



    public void setEnd(String end) {
        this.end = end;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public String getStart() {
        return start;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getExpires() {
        return expires;
    }
}

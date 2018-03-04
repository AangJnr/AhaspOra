package com.app.ahaspora.models;


import com.google.firebase.firestore.Exclude;

/**
 * Created by aangjnr on 20/06/2017.
 */

public class PaymentOption {

    String type;
    @Exclude

    int icon;
    String value;
    String pin;
    String userName;
    String uid;

    String month;
    String year;


    public PaymentOption(){}



    public PaymentOption(String uid, String type, String userName, String value, String pin, String month, String year) {
        this.uid = uid;
        this.type = type;
        this.userName = userName;
        this.value = value;
        this.pin = pin;
        this.month = month;
        this.year = year;


    }

     public PaymentOption(String type, int icon) {
        this.type = type;
        this.icon = icon;
    }

    public PaymentOption(String name, String value) {
        this.type = name;
        this.value = value;
    }


    public PaymentOption(String uid, String type, String userName, String value, String pin) {
        this.uid = uid;
        this.type = type;
        this.userName = userName;
        this.value = value;
        this.pin = pin;


    }


    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Exclude

    public int getIcon() {
        return icon;
    }

    @Exclude

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPin() {
        return pin;
    }

    public String getUserName() {
        return userName;
    }

    public String getUid() {
        return uid;
    }
}

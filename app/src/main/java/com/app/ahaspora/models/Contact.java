package com.app.ahaspora.models;

/**
 * Created by aangjnr on 07/02/2018.
 */

public class Contact {


    Integer id;
    String name;
    String phone;
    String email;
    String website;


    public void setName(String name) {
        this.name = name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Integer getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsite() {
        return website;
    }


}

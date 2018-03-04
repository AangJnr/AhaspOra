package com.app.ahaspora.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aangjnr on 20/02/2018.
 */

public class Owner {


    int id;
    int role_id;
    String name;
    String email;
    String website;
    String twitter;
    String linkedin;
    String avatar;

    @SerializedName("created_at")
    String createdAt;

    @SerializedName("updated_at")
    String updated_at;


    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }


    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getWebsite() {
        return website;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatar() {
        return avatar;
    }

    public int getRole_id() {
        return role_id;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getUpdated_at() {
        return updated_at;
    }

}

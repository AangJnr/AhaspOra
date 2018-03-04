package com.app.ahaspora.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aangjnr on 20/02/2018.
 */

public class Company {


    int id;
    int owner_id;
    String name;
    String tagline;
    String description;
    String email;
    String location;
    String phone;
    String website;
    String twitter;
    String video;
    String image;


    @SerializedName("created_at")
    String createdAt;

    @SerializedName("updated_at")
    String updated_at;

    Owner owner;


    public void setImage(String image) {
        this.image = image;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public void setOwner(Owner owner) {
        this.owner = owner;
    }


    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }


    public void setVideo(String video) {
        this.video = video;
    }


    public String getEmail() {
        return email;
    }


    public String getImage() {
        return image;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getWebsite() {
        return website;
    }

    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public String getTagline() {
        return tagline;
    }


    public String getPhone() {
        return phone;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public Owner getOwner() {
        return owner;
    }


    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getVideo() {
        return video;
    }
}

package com.app.ahaspora.models;

import android.support.annotation.Nullable;

import com.google.firebase.firestore.Exclude;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by aangjnr on 05/06/2017.
 */

public class Event {


    @Exclude
    Boolean isGoing;

    int id;
    Author author;
    String title;
    String subtitle;
    String body;
    String image;
    Location location;
    Time time;
    List<Comment> comments;

    int color;

    @SerializedName("created_at")
    String createdAt;

    @SerializedName("updated_at")
    String updatedAt;



    public Event() {

    }







    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setTime(Time time) {
        this.time = time;

    }


    public List<Comment> getComments() {
        return comments;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getImage() {
        return image;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getBody() {
        return body;
    }

    public Author getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public Location getLocation() {
        return location;
    }

    public int getId() {
        return id;
    }


    public String getSubtitle() {
        return subtitle;
    }

    public Time getTime() {
        return time;
    }


    public void setGoing(Boolean going) {
        isGoing = going;
    }

    public Boolean getGoing() {
        return isGoing;
    }
}

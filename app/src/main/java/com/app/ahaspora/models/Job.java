package com.app.ahaspora.models;

import android.support.annotation.Nullable;

import com.google.firebase.firestore.Exclude;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by aangjnr on 06/06/2017.
 */

public class Job {

    Integer id;
    Author author;
    @Exclude
    int categoryId;
    Category category;
    @Exclude
    int companyId;
    Company company;
    String title;
    String body;
    String image;
    String featured;
    Location location;
    Time time;
    List<Comment> comments;

    @SerializedName("created_at")
    String createdAt;

    @SerializedName("updated_at")
    String updated_at;


    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public void setBody(String body) {
        this.body = body;
    }


    public void setImage(String image) {
        this.image = image;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public void setFeatured(String featured) {
        this.featured = featured;
    }


    public String getUpdated_at() {
        return updated_at;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Time getTime() {
        return time;
    }

    public Location getLocation() {
        return location;
    }

    public String getTitle() {
        return title;
    }

    public Author getAuthor() {
        return author;
    }

    public String getBody() {
        return body;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public String getImage() {
        return image;
    }

    public Integer getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public Company getCompany() {
        return company;
    }

    public String getFeatured() {
        return featured;
    }


}
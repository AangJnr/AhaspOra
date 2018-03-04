package com.app.ahaspora.models;

import android.support.v4.content.ContextCompat;

import com.app.ahaspora.R;
import com.google.firebase.firestore.Exclude;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by aangjnr on 05/06/2017.
 */

public class Recommendation {

    Integer id;
    Author author;
    Category category;
    String title;
    String tagline;
    String body;
    String image;
    Contact contact;
    Location location;
    List<Comment> comments;

    @SerializedName("created_at")
    String createdAt;

    @SerializedName("updated_at")
    String updatedAt;

    @Exclude
    int categoryId;

    @Exclude
    String authorName;

    @Exclude
    String authorAvatar;


    @Exclude
    int color;

    @Exclude
    int drawable;

    public Recommendation(){

    }


    public void setAuthorAvatar(String authorAvatar) {
        this.authorAvatar = authorAvatar;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorAvatar() {
        return authorAvatar;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }


    public void setBody(String body) {
        this.body = body;
    }


    public void setCategory(Category category) {
        this.category = category;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
     }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public Author getAuthor() {
        return author;
    }

    public Category getCategory() {
        return category;
    }

    public Contact getContact() {
        return contact;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public Location getLocation() {
        return location;
    }

    public String getBody() {
        return body;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getImage() {
        return image;
    }

    public String getTagline() {
        return tagline;
    }

    public String getTitle() {
        return title;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }


    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

/*
    public void setColor(int color) {
        this.color = color;


    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public int getColor() {
        return color;
    }

    public int getDrawable() {
        return drawable;
    }*/
}

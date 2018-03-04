package com.app.ahaspora.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aangjnr on 12/06/2017.
 */

public class Comment {



    Integer postId;
    Integer id;
    Author author;
    String body;
    String image;
    String type;
    @SerializedName("created_at")
    String createdAt;

    @SerializedName("updated_at")
    String updatedAt;



    public Comment(){}


    public void setAuthor(Author author) {
        this.author = author;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Author getAuthor() {
        return author;
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

    public String getUpdatedAt() {
        return updatedAt;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }
}

package com.app.ahaspora.models;

/**
 * Created by aangjnr on 07/02/2018.
 */

public class Author {

    String name;
    String avatar;

    int authorId;


    public Author(){}

    public Author(String name, String avatar){this.name = name; this.avatar = avatar;}
    public Author(int id, String name, String avatar){this.authorId = id;this.name = name; this.avatar = avatar;}


    public void setName(String name) {
        this.name = name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }


    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }


    public int getAuthorId() {
        return authorId;
    }

}

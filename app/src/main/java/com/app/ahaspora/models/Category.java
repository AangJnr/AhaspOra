package com.app.ahaspora.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aangjnr on 07/02/2018.
 */

public class Category {


    @SerializedName("id")
    Integer id;
    @SerializedName("parent_id")
    Integer parentId;
    @SerializedName("order")
    Integer orderNo;
    @SerializedName("name")
    String name;

    @SerializedName("type")
    String type;
    @SerializedName("created_at")
    String dateCreated;
    @SerializedName("updated_at")
    String lastUpdated;



    public Category(){}


    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public Integer getParentId() {
        return parentId;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }


}


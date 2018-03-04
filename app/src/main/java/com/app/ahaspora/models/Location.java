package com.app.ahaspora.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aangjnr on 07/02/2018.
 */

public class Location {



    Integer id;
    String name;
    String lat;

    @SerializedName("long")
    String lon;

    public Location(){}


    public Location(String lat, String lon){this.lat = lat; this.lon = lon;}


    public void setName(String name) {
        this.name = name;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}

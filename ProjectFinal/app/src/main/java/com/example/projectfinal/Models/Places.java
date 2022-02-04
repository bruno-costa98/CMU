package com.example.projectfinal.Models;

import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Places implements Serializable {

    private Geometry geometry;

    private String name;

    private City plus_code;

    public City getPlus_code() {
        return plus_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Geometry getGeometry() {
        return geometry;
    }
}
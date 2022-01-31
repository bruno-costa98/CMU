package com.example.projectfinal.Models;

import com.google.gson.annotations.SerializedName;

public class Places {

    private String icon;

    private String name;

    private City plus_code;

    public City getPlus_code() {
        return plus_code;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
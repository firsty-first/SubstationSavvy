package com.example.chatme;

import com.google.gson.annotations.SerializedName;
public class Location {
    @SerializedName("name")
    private String name;
    public String getName() {
        return name;
    }
}
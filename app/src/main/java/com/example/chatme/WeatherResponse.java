package com.example.chatme;

import android.location.Location;

import com.google.gson.annotations.SerializedName;

public class WeatherResponse {

    @SerializedName("location")
    private Location location;

    @SerializedName("current")
    private CurrentWeather currentWeather;

    public Location getLocation() {
        return location;
    }

    public CurrentWeather getCurrentWeather() {
        return currentWeather;
    }
}

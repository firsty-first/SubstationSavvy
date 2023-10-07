package com.example.chatme;

import com.google.gson.annotations.SerializedName;

public class CurrentWeather {
    @SerializedName("temperature")
    private int temperature;

    public int getTemperature() {
        return temperature;
    }
}

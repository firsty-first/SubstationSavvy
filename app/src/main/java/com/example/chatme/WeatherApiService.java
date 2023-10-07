package com.example.chatme;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {

    @GET("current") // The endpoint for the current weather information
    Call<WeatherResponse> getCurrentWeather(
            @Query("access_key") String apiKey, // Your API key
            @Query("query") String location // Location (e.g., 'New York')
    );
}


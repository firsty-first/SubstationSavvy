package com.example.chatme;


import java.util.Map;

        import retrofit2.Call;
        import retrofit2.http.Body;
        import retrofit2.http.GET;
        import retrofit2.http.POST;

public interface ApiService {
    @GET("/")
    Call<PredictionResponse> getWelcomeMessage();

    @POST("/prediction")
    Call<Map<String, String>> getPrediction(@Body Map<String, String> input);

    @POST("/prediction")
    Call<Map<String, String>> getPredictionString(@Body Map<String, String> input);
}

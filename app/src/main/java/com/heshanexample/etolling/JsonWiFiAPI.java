package com.heshanexample.etolling;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface JsonWiFiAPI {

    @POST("push_highway_vehicle")
    Call<PostWiFi> createPost(@Body PostWiFi post);
}
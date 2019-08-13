package com.heshanexample.etolling;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JsonWiFiAPI {
    @GET("162rl1")
    Call<List<PostWiFi>> getPosts();

    @POST("posts?id_like=1")
    Call<PostWiFi> createPost(@Body PostWiFi post);
}

package com.heshanexample.etolling;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JsonMacAPI {
    @GET("162rl1")
    Call<List<PostMacAddress>> getPosts();

    @POST("get_mac_addresses")
    Call<PostMacAddress> createPost(@Body PostMacAddress post);
}
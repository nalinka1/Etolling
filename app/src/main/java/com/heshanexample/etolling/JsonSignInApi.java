package com.heshanexample.etolling;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JsonSignInApi {

    @POST("get_user")
    Call<PostSignIn> createPost(@Body PostSignIn post);

    // delete this part later...........
    @GET("dpi8j")
    Call<PostSignIn> getPosts();
    //......................................
}

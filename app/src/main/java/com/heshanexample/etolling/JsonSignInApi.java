package com.heshanexample.etolling;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JsonSignInApi {

    @POST("1h1ctx")
    Call<PostSignIn> createPost(@Body PostSignIn SignIn);

    // delete this part later...........
    @GET("dpi8j")
    Call<PostSignIn> getPosts();
    //......................................
}

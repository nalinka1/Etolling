package com.heshanexample.etolling;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface JsonSignInApi {

    @POST("1h1ctx")
    Call<PostSignIn> createPost(@Body PostSignIn SignIn);
}

package com.heshanexample.etolling;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JsonSignInApi {


    //user SignIn
    @POST("get_user")
    Call<PostSignIn> createPost(@Body PostSignIn post);

    //user data Update
    @POST("update_user")
    Call<getUpdate> getUpdates(@Body getUpdate get);

    //register new vehicle
    @POST("register_vehicle")
    Call<newVehicle> addVehicle(@Body newVehicle New);

    //recharge
    @POST("recharge")
    Call<topup> addPin(@Body topup newPin);

    //get Vehicle classes
    @POST("get_vehicle_classes")
    Call<getVehicleClasses> getClasses(@Body getVehicleClasses get);

}

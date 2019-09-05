package com.heshanexample.etolling;

import com.google.gson.annotations.SerializedName;

public class newVehicle {

    @SerializedName("vehicleNo")
    private String vehicleNo;
    @SerializedName("className")
    private String className;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;

    public newVehicle(String vehicleNo, String className, String email, String password) {
        this.vehicleNo = vehicleNo;
        this.className = className;
        this.email = email;
        this.password = password;
    }
}

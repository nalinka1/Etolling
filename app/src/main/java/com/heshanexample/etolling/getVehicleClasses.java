package com.heshanexample.etolling;

import com.google.gson.annotations.SerializedName;

public class getVehicleClasses {
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("vehicleClasses")
    private Object vehicleClasses;

    public getVehicleClasses(String email, String password){
        this.email = email;
        this.password = password;
    }

    public Object getVehicleClasses() {
        return vehicleClasses;
    }
}

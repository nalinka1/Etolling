package com.heshanexample.etolling;

import com.google.gson.annotations.SerializedName;

public class getVehicle {
    @SerializedName("vehicleNo")
    private String vehicleNo;
    @SerializedName("className")
    private String className;

    public String getVehicleNo() {
        return vehicleNo;
    }

    public String getClassName() {
        return className;
    }
}

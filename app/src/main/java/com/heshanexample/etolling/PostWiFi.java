package com.heshanexample.etolling;

import com.google.gson.annotations.SerializedName;

public class PostWiFi {
    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private  String password;

    @SerializedName("time")
    private String time;

    @SerializedName("vehicleNo")
    private String vehicleNo;

    @SerializedName("macAddress")
    private String macAddress;

    @SerializedName("entrance")
    private String entrance;

    @SerializedName("exit")
    private String exit;

    @SerializedName("toll")
    private String toll;

    public PostWiFi(String email, String password, String time, String vehicleNo, String macAddress) {
        this.email = email;
        this.password = password;
        this.time = time;
        this.vehicleNo = vehicleNo;
        this.macAddress = macAddress;
    }

    public String getEntrance() {
        return entrance;
    }

    public String getExit() {
        return exit;
    }

    public String getToll() {
        return toll;
    }
}

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

    @SerializedName("ssid")
    private String ssid;

    @SerializedName("gps")
    private String gps;

    @SerializedName("entrance")
    private String entrance;

    @SerializedName("exit")
    private String exit;

    @SerializedName("toll")
    private String toll;

    public PostWiFi(String email, String password, String time, String vehicleNo, String macAddress, String ssid, String gps) {
        this.email = email;
        this.password = password;
        this.time = time;
        this.vehicleNo = vehicleNo;
        this.macAddress = macAddress;
        this.ssid = ssid;
        this.gps =gps;
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

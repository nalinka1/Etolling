package com.heshanexample.etolling;

import com.google.gson.annotations.SerializedName;

public class historyCall {

    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;

    @SerializedName("entrance")
    private String entrance;
    @SerializedName("egress")
    private String egress;
    @SerializedName("vehicleNo")
    private String vehicleNo;
    @SerializedName("duration")
    private String duration;
    @SerializedName("toll")
    private String toll;


    public historyCall(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEntrance() {
        return entrance;
    }

    public void setEntrance(String entrance) {
        this.entrance = entrance;
    }

    public String getEgress() {
        return egress;
    }

    public void setEgress(String egress) {
        this.egress = egress;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getToll() {
        return toll;
    }

    public void setToll(String toll) {
        this.toll = toll;
    }
}

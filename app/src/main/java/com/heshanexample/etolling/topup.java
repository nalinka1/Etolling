package com.heshanexample.etolling;

import com.google.gson.annotations.SerializedName;

public class topup {
    @SerializedName("pin")
    private String pin;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("balance")
    private float balance;

    public topup(String pin, String email, String password) {
        this.pin = pin;
        this.email = email;
        this.password = password;
    }

    public float getBalance() {
        return balance;
    }
}

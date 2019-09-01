package com.heshanexample.etolling;

import com.google.gson.annotations.SerializedName;

public class PostMacAddress {
    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("macAddresses")
    private Object macAddresses;


    public PostMacAddress(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public Object getMacAddresses() {
        return macAddresses;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
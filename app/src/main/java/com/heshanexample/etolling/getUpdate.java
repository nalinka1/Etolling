package com.heshanexample.etolling;

import com.google.gson.annotations.SerializedName;

public class getUpdate {
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("revisionNo")
    private int revisionNo;

    public getUpdate(String email, String password, int revisionNo) {
        this.email = email;
        this.password = password;
        this.revisionNo = revisionNo;
    }
}

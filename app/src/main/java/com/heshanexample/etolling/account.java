package com.heshanexample.etolling;

import com.google.gson.annotations.SerializedName;

public class account {
    @SerializedName("accountNo")
    private String accountNo;
    @SerializedName("ownerName")
    private String ownerName;
    @SerializedName("balance")
    private int balance;

    public String getAccountNo() {
        return accountNo;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public int getBalance() {
        return balance;
    }
}
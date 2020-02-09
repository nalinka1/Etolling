package com.heshanexample.etolling;

import com.google.gson.annotations.SerializedName;

public class pendingTransaction {

    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("pay")
    private boolean pay;

    public pendingTransaction(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public boolean isPay() {
        return pay;
    }

    public void setPay(boolean pay) {
        this.pay = pay;

    }

}

package com.caexlogistics.postalapp.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Heinz Keydel on 6/01/2017.
 */

public class Credenciales {

    @SerializedName("usuario")
    private String userId;
    @SerializedName("clave")
    private String password;

    public Credenciales(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

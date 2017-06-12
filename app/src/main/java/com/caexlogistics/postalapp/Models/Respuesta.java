package com.caexlogistics.postalapp.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Usuario on 7/06/2017.
 */

public class Respuesta {

    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;

    public Respuesta(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Respuesta{" +
                "status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}

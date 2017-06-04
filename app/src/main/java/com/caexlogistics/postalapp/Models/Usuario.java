package com.caexlogistics.postalapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Heinz Keydel on 6/01/2017.
 */

public class Usuario {

    @SerializedName("status")
    private String status;
    @SerializedName("login")
    private String login;
    @SerializedName("clave")
    private String clave;
    @SerializedName("nombre")
    private String nombre;
    @SerializedName("ruta")
    private String ruta;
    @SerializedName("estado")
    private boolean estado;

    public Usuario(String login, String clave, String nombre, String ruta, boolean estado) {
        this.login = login;
        this.clave = clave;
        this.nombre = nombre;
        this.ruta = ruta;
        this.estado = estado;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}

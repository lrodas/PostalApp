package com.caexlogistics.postalapp.Models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Heinz Keydel on 9/01/2017.
 */

public class MovilDespachos extends RealmObject {

    @PrimaryKey
    private String pieza;
    @Index
    private String codigoCartero;
    private String ruta;
    @Index
    private Date fechaDespacho;
    private boolean sincronizada;

    public MovilDespachos() {
    }

    public MovilDespachos(String pieza, String codigoCartero, String ruta, Date fechaDespacho, boolean sincronizada) {
        this.pieza = pieza;
        this.codigoCartero = codigoCartero;
        this.ruta = ruta;
        this.fechaDespacho = fechaDespacho;
        this.sincronizada = sincronizada;
    }

    public String getPieza() {
        return pieza;
    }

    public void setPieza(String pieza) {
        this.pieza = pieza;
    }

    public boolean isSincronizada() {
        return sincronizada;
    }

    public String getCodigoCartero() {
        return codigoCartero;
    }

    public void setCodigoCartero(String codigoCartero) {
        this.codigoCartero = codigoCartero;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public Date getFechaDespacho() {
        return fechaDespacho;
    }

    public void setFechaDespacho(Date fechaDespacho) {
        this.fechaDespacho = fechaDespacho;
    }

    public void setSincronizada(boolean sincronizada) {
        this.sincronizada = sincronizada;
    }
}

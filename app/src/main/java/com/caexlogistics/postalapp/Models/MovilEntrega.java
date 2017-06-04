package com.caexlogistics.postalapp.Models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Usuario on 4/06/2017.
 */

public class MovilEntrega extends RealmObject {

    private int transaccion;
    @PrimaryKey
    private String pieza;
    @Index
    private String codigoCartero;
    private String ruta;
    @Index
    private Date fechaEntrega;
    private boolean sincronizada;

    public MovilEntrega() {
    }

    public MovilEntrega(int transaccion, String pieza, String codigoCartero, Date fechaEntrega, boolean sincronizada) {
        this.transaccion = transaccion;
        this.pieza = pieza;
        this.codigoCartero = codigoCartero;
        this.fechaEntrega = fechaEntrega;
        this.sincronizada = sincronizada;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public int getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(int transaccion) {
        this.transaccion = transaccion;
    }

    public String getPieza() {
        return pieza;
    }

    public void setPieza(String pieza) {
        this.pieza = pieza;
    }

    public String getCodigoCartero() {
        return codigoCartero;
    }

    public void setCodigoCartero(String codigoCartero) {
        this.codigoCartero = codigoCartero;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public boolean isSincronizada() {
        return sincronizada;
    }

    public void setSincronizada(boolean sincronizada) {
        this.sincronizada = sincronizada;
    }

    @Override
    public String toString() {
        return "MovilEntrega{" +
                "transaccion=" + transaccion +
                ", pieza='" + pieza + '\'' +
                ", codigoCartero='" + codigoCartero + '\'' +
                ", fechaEntrega=" + fechaEntrega +
                ", sincronizada=" + sincronizada +
                '}';
    }
}

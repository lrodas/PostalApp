package com.caexlogistics.postalapp.Models;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Luis Rodas on 4/06/2017.
 */

public class MovilDevolucion extends RealmObject {

    @PrimaryKey
    private String pieza;
    private MovilTipoDevolucion tipoDevolucion;
    private Date fechaDevolucion;
    @Index
    private String codigoCartero;
    private String ruta;
    private boolean sincronizada;

    public MovilDevolucion() {
    }

    public MovilDevolucion(String pieza, MovilTipoDevolucion tipoDevolucion, Date fechaDevolucion, String codigoCartero, String ruta) {
        this.pieza = pieza;
        this.tipoDevolucion = tipoDevolucion;
        this.fechaDevolucion = fechaDevolucion;
        this.codigoCartero = codigoCartero;
        this.ruta = ruta;
    }

    public boolean isSincronizada() {
        return sincronizada;
    }

    public void setSincronizada(boolean sincronizada) {
        this.sincronizada = sincronizada;
    }

    public String getPieza() {
        return pieza;
    }

    public void setPieza(String pieza) {
        this.pieza = pieza;
    }

    public MovilTipoDevolucion getTipoDevolucion() {
        return tipoDevolucion;
    }

    public void setTipoDevolucion(MovilTipoDevolucion tipoDevolucion) {
        this.tipoDevolucion = tipoDevolucion;
    }

    public Date getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(Date fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
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

    @Override
    public String toString() {
        return "MovilDevolucion{" +
                ", pieza='" + pieza + '\'' +
                ", tipoDevolucion=" + tipoDevolucion +
                ", fechaDevolucion=" + fechaDevolucion +
                ", codigoCartero='" + codigoCartero + '\'' +
                ", ruta='" + ruta + '\'' +
                '}';
    }
}

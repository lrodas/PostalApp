package com.caexlogistics.postalapp.Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Usuario on 4/06/2017.
 */

public class MovilTipoDevolucion extends RealmObject{

    @PrimaryKey
    private int idTipoDevolucion;
    private String descripcion;

    public MovilTipoDevolucion() {
    }

    public MovilTipoDevolucion(int idTipoDevolucion, String descripcion) {
        this.idTipoDevolucion = idTipoDevolucion;
        this.descripcion = descripcion;
    }

    public int getIdTipoDevolucion() {
        return idTipoDevolucion;
    }

    public void setIdTipoDevolucion(int idTipoDevolucion) {
        this.idTipoDevolucion = idTipoDevolucion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}

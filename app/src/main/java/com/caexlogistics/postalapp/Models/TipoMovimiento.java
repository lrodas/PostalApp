package com.caexlogistics.postalapp.Models;

import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 3plgtluisrc on 17/01/2017.
 */

public class TipoMovimiento {


    @PrimaryKey
    private String descripcion;

    public TipoMovimiento(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoMovimiento() {
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

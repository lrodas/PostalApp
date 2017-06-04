package com.caexlogistics.postalapp.Models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.Required;

/**
 * Created by 3plgtluisrc on 20/01/2017.
 */

public class MovimientosPorDia extends RealmObject {

    private int cantidadSincronizado;
    private int cantidadNoSincronizado;
    private int cantidadTotal;
    @Required
    private Date fecha;
    @Index
    private String cartero;
    @Index
    private String tipoMovimiento;


    public MovimientosPorDia() {
    }

    public MovimientosPorDia(int cantidadSincronizado, int cantidadNoSincronizado, int cantidadTotal, Date fecha, String cartero, String tipoMovimiento) {
        this.cantidadSincronizado = cantidadSincronizado;
        this.cantidadNoSincronizado = cantidadNoSincronizado;
        this.cantidadTotal = cantidadTotal;
        this.fecha = fecha;
        this.cartero = cartero;
        this.tipoMovimiento = tipoMovimiento;
    }

    public int getCantidadSincronizado() {
        return cantidadSincronizado;
    }

    public void setCantidadSincronizado(int cantidadSincronizado) {
        this.cantidadSincronizado = cantidadSincronizado;
    }

    public int getCantidadNoSincronizado() {
        return cantidadNoSincronizado;
    }

    public void setCantidadNoSincronizado(int cantidadNoSincronizado) {
        this.cantidadNoSincronizado = cantidadNoSincronizado;
    }

    public int getCantidadTotal() {
        return cantidadTotal;
    }

    public void setCantidadTotal(int cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getCartero() {
        return cartero;
    }

    public void setCartero(String cartero) {
        this.cartero = cartero;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }
}

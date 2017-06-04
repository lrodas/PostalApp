package com.caexlogistics.postalapp.Models;

import android.support.annotation.Nullable;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Heinz Keydel on 9/01/2017.
 */

public class MovilDespachos extends RealmObject {

    private int transaccion;
    private String ciclo;
    @PrimaryKey
    private String pieza;
    private String tipo;
    private String nombre;
    private String direccion;
    private String codigoAgencia;
    @Index
    private String codigoCartero;
    private String codRuta;
    @Index
    private Date fechaDespacho;
    private Date fechaEscaneo;
    private String despacho_tipo;
    private boolean escaneada;
    private boolean sincronizada;

    public MovilDespachos() {
    }

    public MovilDespachos(int transaccion, String ciclo, String pieza, String tipo, String nombre, String direccion, String codigoAgencia, String codigoCartero, String codRuta, Date fechaDespacho, Date fechaEscaneo, String despacho_tipo, boolean escaneada, boolean sincronizada) {
        this.transaccion = transaccion;
        this.ciclo = ciclo;
        this.pieza = pieza;
        this.tipo = tipo;
        this.nombre = nombre;
        this.direccion = direccion;
        this.codigoAgencia = codigoAgencia;
        this.codigoCartero = codigoCartero;
        this.codRuta = codRuta;
        this.fechaDespacho = fechaDespacho;
        this.fechaEscaneo = fechaEscaneo;
        this.despacho_tipo = despacho_tipo;
        this.escaneada = escaneada;
        this.sincronizada = sincronizada;
    }

    public int getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(int transaccion) {
        this.transaccion = transaccion;
    }

    public String getCiclo() {
        return ciclo;
    }

    public void setCiclo(String ciclo) {
        this.ciclo = ciclo;
    }

    public String getPieza() {
        return pieza;
    }

    public void setPieza(String pieza) {
        this.pieza = pieza;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCodigoAgencia() {
        return codigoAgencia;
    }

    public void setCodigoAgencia(String codigoAgencia) {
        this.codigoAgencia = codigoAgencia;
    }

    public String getCodigoCartero() {
        return codigoCartero;
    }

    public void setCodigoCartero(String codigoCartero) {
        this.codigoCartero = codigoCartero;
    }

    public String getCodRuta() {
        return codRuta;
    }

    public void setCodRuta(String codRuta) {
        this.codRuta = codRuta;
    }

    public Date getFechaDespacho() {
        return fechaDespacho;
    }

    public void setFechaDespacho(Date fechaDespacho) {
        this.fechaDespacho = fechaDespacho;
    }

    public Date getFechaEscaneo() {
        return fechaEscaneo;
    }

    public void setFechaEscaneo(Date fechaEscaneo) {
        this.fechaEscaneo = fechaEscaneo;
    }

    public String getDespacho_tipo() {
        return despacho_tipo;
    }

    public void setDespacho_tipo(String despacho_tipo) {
        this.despacho_tipo = despacho_tipo;
    }

    public boolean isEscaneada() {
        return escaneada;
    }

    public void setEscaneada(boolean escaneada) {
        this.escaneada = escaneada;
    }

    public boolean isSincronizada() {
        return sincronizada;
    }

    public void setSincronizada(boolean sincronizada) {
        this.sincronizada = sincronizada;
    }
}

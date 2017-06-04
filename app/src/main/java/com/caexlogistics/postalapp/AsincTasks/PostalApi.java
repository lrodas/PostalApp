package com.caexlogistics.postalapp.AsincTasks;

import com.caexlogistics.postalapp.Models.Credenciales;
import com.caexlogistics.postalapp.Models.MovilTipoDevolucion;
import com.caexlogistics.postalapp.Models.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Luis Rodas on 6/01/2017.
 */

public interface PostalApi {

    public static final String BASE_URL = "http://192.168.1.2/v1/";
    //public static final String BASE_URL = "http://127.0.0.1:8080/V1/";

    @Headers("Content-Type: application/json")
    @POST("usuarios/login/")
    Call<Usuario> login(@Body Credenciales credenciales);
    //Call<Usuario> login(@Field("login") String login, @Field("password") String password);

    @Headers("Content-Type: Application/json")
    @GET("tipoDevolucion/obtenerTipoDevolucion")
    Call<List<MovilTipoDevolucion>> obtenerTipoDevolucion();
}

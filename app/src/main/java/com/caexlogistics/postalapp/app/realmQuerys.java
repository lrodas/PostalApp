package com.caexlogistics.postalapp.app;

import com.caexlogistics.postalapp.Models.MovilDespachos;
import com.caexlogistics.postalapp.Models.MovilEntrega;
import com.caexlogistics.postalapp.Preferencias.SessionPrefs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by 3plgtluisrc on 17/01/2017.
 */

public class realmQuerys {

    private Realm realm;

    private void obtenerInstancia(){
        if(realm == null){
            realm = Realm.getDefaultInstance();
        }
    }

    public static List<MovilDespachos> obtenerPiezasDespachadas(String usuario){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<MovilDespachos> lista = realm.where(MovilDespachos.class)
                .equalTo("codigoCartero", usuario)
                .findAll();
        return lista;
    }

    public static List<MovilEntrega> obtenerPiezasEntregas(String usuario){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<MovilEntrega> lista = realm.where(MovilEntrega.class)
                .equalTo("codigoCartero", usuario)
                .findAll();
        return lista;
    }

    public long obtenerPiezasEscaneadasNoSincronizadas(String usuario){
        long conteo = 0;
        obtenerInstancia();
        conteo = realm.where(MovilDespachos.class)
                .equalTo("sincronizada", false)
                .equalTo("codigoCartero", usuario)
                .count();
        return conteo;
    }

    public long obtenerPiezasEscaneadasSincronizadas(String usuario){
        long noSincronizado = 0;
        long conteo = 0;
        obtenerInstancia();
        noSincronizado = realm.where(MovilDespachos.class)
                .equalTo("sincronizada", false)
                .equalTo("codigoCartero", usuario)
                .count();
        return conteo;
    }

    public static MovilDespachos actualizarDespacho(String pieza, String codCartero){

        Realm realm = Realm.getDefaultInstance();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        MovilDespachos movilDespachos = realm.where(MovilDespachos.class).equalTo("pieza", pieza).equalTo("codigoCartero", codCartero).findFirst();

        if (movilDespachos != null) {

            realm.beginTransaction();

            movilDespachos.setFechaEscaneo(new Date());
            movilDespachos.setEscaneada(true);

            realm.commitTransaction();

        }
        return movilDespachos;
    }

    public static MovilEntrega actualizarEntrega(String pieza, String codCartero){

        Realm realm = Realm.getDefaultInstance();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        MovilEntrega movilEntrega = realm.where(MovilEntrega.class).equalTo("pieza", pieza).equalTo("codigoCartero", codCartero).findFirst();

        if (movilEntrega != null) {

            realm.beginTransaction();

            movilEntrega.setFechaEntrega(new Date());

            realm.commitTransaction();

        }
        return movilEntrega;
    }

    public static void insertarMovilDespacho(MovilDespachos movilDespacho){
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(movilDespacho);
        realm.commitTransaction();
    }


}

package com.caexlogistics.postalapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.caexlogistics.postalapp.Adapters.AdapterDevolucion;
import com.caexlogistics.postalapp.Models.MovilDevolucion;
import com.caexlogistics.postalapp.Models.MovilTipoDevolucion;
import com.caexlogistics.postalapp.Models.MovimientosPorDia;
import com.caexlogistics.postalapp.Preferencias.SessionPrefs;
import com.caexlogistics.postalapp.R;
import com.caexlogistics.postalapp.app.realmQuerys;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


public class DevolucionActivity extends AppCompatActivity {

    private long mLastPress = 0;    // Cuándo se pulsó atrás por última vez
    private final long mTimeLimit = 1000; // Límite de tiempo entre pulsaciones, en ms

    private List<MovilDevolucion> listDevolucion;

    private RecyclerView mRecyclerViewDespachos;
    private TextView lblDevolucionPiezasNoSincronizadas;
    private TextView lblDevolucionPiezasSincronizadas;
    private TextView lblDevolucionTotalPiezas;
    private EditText txtPieza;
    private Spinner spTipoDevolucion;
    private ArrayAdapter<MovilDevolucion> adapterSpinner;

    private Realm realm;

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;
    private int TotalPiezas = 0, PiezasNoSincronizadas = 0, PiezasSincronizadas= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devolucion);

        //verificamos si esta logueado
        if(!SessionPrefs.get(this).isLoggedIn()){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        //obtenemos los despacho del dia
        listDevolucion = this.obtenerPiezasNoEncontradas();

        //Inicializamos los objetos de ls UI
        bindUIElements();

        mAdapter = new AdapterDevolucion(this, listDevolucion, R.layout.recyclerview_item_devolucion, new AdapterDevolucion.OnItemClickListener(){

            @Override
            public void onItemClick(MovilDevolucion movilDevolucion, int position) {
                Toast.makeText(DevolucionActivity.this, "para Futuras Implementaciones", Toast.LENGTH_SHORT).show();
            }
        });

        //Eventos

        txtPieza.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyEvent.getAction() == android.view.KeyEvent.ACTION_UP && keyCode == android.view.KeyEvent.KEYCODE_ENTER) {
                    String pieza = txtPieza.getText().toString();
                    if (!pieza.trim().equals("") && !pieza.trim().equals("<br />")) {
                        MovilDevolucion movilDevolucion = realmQuerys.actualizarDevolucion(pieza, SessionPrefs.get(DevolucionActivity.this).mPrefs.getString(SessionPrefs.PREF_USUARIO_LOGIN, "Sin Usuario"));
                        if (movilDevolucion == null) {
                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String fecha_actual = df.format(c.getTime());
                            Realm realm = Realm.getDefaultInstance();

                            movilDevolucion = new MovilDevolucion();

                            movilDevolucion.setPieza(pieza);
                            movilDevolucion.setCodigoCartero(SessionPrefs.get(DevolucionActivity.this).mPrefs.getString(SessionPrefs.PREF_USUARIO_LOGIN, "Sin Usuario"));
                            movilDevolucion.setRuta(SessionPrefs.get(DevolucionActivity.this).mPrefs.getString(SessionPrefs.PREF_USUARIO_RUTA, "Sin Ruta"));
                            movilDevolucion.setSincronizada(false);
                            movilDevolucion.setFechaDevolucion(new Date());
                            movilDevolucion.setTipoDevolucion((MovilTipoDevolucion) spTipoDevolucion.getSelectedItem());

                            realm.beginTransaction();
                            realm.copyToRealmOrUpdate(movilDevolucion);
                            realm.commitTransaction();

                            aumentarContadorDevolucion();

                            lblDevolucionPiezasNoSincronizadas.setText(PiezasNoSincronizadas + "");
                            lblDevolucionTotalPiezas.setText(TotalPiezas + "");
                            lblDevolucionPiezasSincronizadas.setText(PiezasSincronizadas + "");
                            txtPieza.setText("");
                            txtPieza.requestFocus();
                        }
                        txtPieza.setText("");
                        txtPieza.requestFocus();
                    }
                    return true;
                }
                return false;
            }
        });

        //llenamos los datos en los elementos de la UI
        inicializarContador();
        mRecyclerViewDespachos.setAdapter(mAdapter);
        mRecyclerViewDespachos.setLayoutManager(layoutManager);
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();

        if (currentTime - mLastPress > mTimeLimit) {
            Toast.makeText(this, R.string.str_salir, Toast.LENGTH_SHORT).show();
            mLastPress = currentTime;
        } else {
            super.onBackPressed();
        }
    }

    private void bindUIElements(){
        lblDevolucionPiezasNoSincronizadas = (TextView) findViewById(R.id.lblDevolucionPiezasNoSincronizadas);
        lblDevolucionPiezasSincronizadas = (TextView) findViewById(R.id.lblDevolucionPiezasSincronizadas);
        lblDevolucionTotalPiezas = (TextView) findViewById(R.id.lblDevolucionTotalPiezas);
        mRecyclerViewDespachos = (RecyclerView) findViewById(R.id.recyclerViewDevolucion);
        layoutManager = new LinearLayoutManager(this);
        txtPieza = (EditText) findViewById(R.id.txtDevolucionPieza);
        spTipoDevolucion = (Spinner) findViewById(R.id.spTipoDevolucion);
        realm = Realm.getDefaultInstance();
        RealmResults<MovilTipoDevolucion> listaRealmDevolucion = realm.where(MovilTipoDevolucion.class).findAll();
        adapterSpinner = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaRealmDevolucion);
        spTipoDevolucion.setAdapter(adapterSpinner);
        realm.close();
    }

    private void aumentarContadorDevolucion(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        MovimientosPorDia devolucionPorDia = null;
        Calendar c = Calendar.getInstance();
        String fecha = simpleDateFormat.format(c.getTime()) + " 00:00:00";
        realm = Realm.getDefaultInstance();

        try {
            Date fechaFinal = simpleDateFormat.parse(fecha);
            devolucionPorDia = realm.where(MovimientosPorDia.class)
                    .greaterThanOrEqualTo("fecha", fechaFinal)
                    .equalTo("cartero", SessionPrefs.get(this).mPrefs.getString(SessionPrefs.PREF_USUARIO_LOGIN, "Sin Usuario"))
                    .equalTo("tipoMovimiento", "devolucion")
                    .findFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(devolucionPorDia != null) {

            realm.beginTransaction();
            devolucionPorDia.setCantidadTotal(devolucionPorDia.getCantidadTotal() + 1);
            devolucionPorDia.setCantidadNoSincronizado(devolucionPorDia.getCantidadNoSincronizado() + 1);
            realm.commitTransaction();

            TotalPiezas = devolucionPorDia.getCantidadTotal();
            PiezasNoSincronizadas = devolucionPorDia.getCantidadNoSincronizado();
            PiezasSincronizadas = devolucionPorDia.getCantidadSincronizado();
        }else{
            realm.beginTransaction();
            realm.copyToRealm(new MovimientosPorDia(0, 1, 1, new Date(), SessionPrefs.get(this).mPrefs.getString(SessionPrefs.PREF_USUARIO_LOGIN, "Sin Usuario"), "devolucion"));
            realm.commitTransaction();
            TotalPiezas = 1;
            PiezasNoSincronizadas = 1;
            PiezasSincronizadas = 0;
        }
    }

    private void inicializarContador(){
        MovimientosPorDia devolucionPorDia = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        String fecha = simpleDateFormat.format(c.getTime()) + " 00:00:00";

        try {
            realm = Realm.getDefaultInstance();
            Date fechaFinal = simpleDateFormat.parse(fecha);
            devolucionPorDia = realm.where(MovimientosPorDia.class)
                    .greaterThanOrEqualTo("fecha", fechaFinal)
                    .equalTo("cartero", SessionPrefs.get(this).mPrefs.getString(SessionPrefs.PREF_USUARIO_LOGIN, "Sin Usuario"))
                    .equalTo("tipoMovimiento", "devolucion")
                    .findFirst();
        }catch(Exception e){
            e.printStackTrace();
        }
        if(devolucionPorDia != null) {
            lblDevolucionPiezasSincronizadas.setText(devolucionPorDia.getCantidadSincronizado()+"");
            lblDevolucionPiezasNoSincronizadas.setText(devolucionPorDia.getCantidadNoSincronizado() + "");
            lblDevolucionTotalPiezas.setText(devolucionPorDia.getCantidadTotal() + "");
        }else{
            lblDevolucionPiezasSincronizadas.setText("0");
            lblDevolucionPiezasNoSincronizadas.setText("0");
            lblDevolucionTotalPiezas.setText("0");
        }
    }

    private List<MovilDevolucion> obtenerPiezasNoEncontradas(){
        return realmQuerys.obtenerPiezasDevolucion(SessionPrefs.get(this).mPrefs.getString(SessionPrefs.PREF_USUARIO_LOGIN, ""));
    }
}
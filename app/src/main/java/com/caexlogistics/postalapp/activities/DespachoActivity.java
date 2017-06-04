package com.caexlogistics.postalapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.caexlogistics.postalapp.Adapters.AdapterDespachos;
import com.caexlogistics.postalapp.Models.MovilDespachos;
import com.caexlogistics.postalapp.Models.MovimientosPorDia;
import com.caexlogistics.postalapp.Preferencias.SessionPrefs;
import com.caexlogistics.postalapp.R;
import com.caexlogistics.postalapp.app.realmQuerys;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;

public class DespachoActivity extends AppCompatActivity{

    private long mLastPress = 0;    // Cuándo se pulsó atrás por última vez
    private final long mTimeLimit = 1000; // Límite de tiempo entre pulsaciones, en ms
    
    private List<MovilDespachos> listDespachos;

    private RecyclerView mRecyclerViewDespachos;
    private TextView lblDespachoPiezasNoSincronizadas;
    private TextView lblDespachoPiezasSincronizadas;
    private TextView lblDespachoTotalPiezas;
    private EditText txtPieza;

    private Realm realm;

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;
    private int TotalPiezas = 0, PiezasNoSincronizadas = 0, PiezasSincronizadas= 0;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despacho);

        //verificamos si esta logueado
        if(!SessionPrefs.get(this).isLoggedIn()){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        //obtenemos los despacho del dia
        listDespachos = this.obtenerPiezasNoEncontradas();

        //Inicializamos los objetos de ls UI
        bindUIElements();

        //creamos una nueva instancia del adaptador del recycler view de los despachos encontrados
        mAdapter = new AdapterDespachos(this, listDespachos, R.layout.recyclerview_item_despacho, new AdapterDespachos.OnItemClickListener() {
            @Override
            public void onItemClick(MovilDespachos movilDespachos, int position) {
                //para futuras implementaciones
                Toast.makeText(DespachoActivity.this, "para futuras implementaciones", Toast.LENGTH_SHORT).show();
            }
        });

        //Eventos

        txtPieza.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                    String pieza = txtPieza.getText().toString();
                    if (!pieza.trim().equals("") && !pieza.trim().equals("<br />")) {
                        MovilDespachos movilDespacho = realmQuerys.actualizarDespacho(pieza, SessionPrefs.get(DespachoActivity.this).mPrefs.getString(SessionPrefs.PREF_USUARIO_LOGIN, "Sin Usuario"));
                        if (movilDespacho == null) {
                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String fecha_actual = df.format(c.getTime());
                            Realm realm = Realm.getDefaultInstance();

                            movilDespacho = new MovilDespachos();

                            movilDespacho.setPieza(pieza);
                            movilDespacho.setCodigoCartero(SessionPrefs.get(DespachoActivity.this).mPrefs.getString(SessionPrefs.PREF_USUARIO_LOGIN, "Sin Usuario"));
                            movilDespacho.setCodRuta(SessionPrefs.get(DespachoActivity.this).mPrefs.getString(SessionPrefs.PREF_USUARIO_RUTA, "Sin Ruta"));
                            movilDespacho.setSincronizada(false);
                            movilDespacho.setEscaneada(true);
                            movilDespacho.setFechaDespacho(new Date());
                            movilDespacho.setFechaEscaneo(new Date());

                            realm.beginTransaction();
                            realm.copyToRealmOrUpdate(movilDespacho);
                            realm.commitTransaction();

                            aumentarContadorDespachos();

                            lblDespachoPiezasNoSincronizadas.setText(PiezasNoSincronizadas + "");
                            lblDespachoTotalPiezas.setText(TotalPiezas + "");
                            lblDespachoPiezasSincronizadas.setText(PiezasSincronizadas + "");
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
        lblDespachoPiezasNoSincronizadas = (TextView) findViewById(R.id.lblDespachoPiezasNoSincronizadas);
        lblDespachoPiezasSincronizadas = (TextView) findViewById(R.id.lblDespachoPiezasSincronizadas);
        lblDespachoTotalPiezas = (TextView) findViewById(R.id.lblDespachoTotalPiezas);
        mRecyclerViewDespachos = (RecyclerView) findViewById(R.id.recyclerViewDespachos);
        layoutManager = new LinearLayoutManager(this);
        txtPieza = (EditText) findViewById(R.id.txtDespachoPieza);
    }

    private void aumentarContadorDespachos(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        MovimientosPorDia despachosPorDia = null;
        Calendar c = Calendar.getInstance();
        String fecha = simpleDateFormat.format(c.getTime()) + " 00:00:00";
        realm = Realm.getDefaultInstance();

        try {
            Date fechaFinal = simpleDateFormat.parse(fecha);
            despachosPorDia = realm.where(MovimientosPorDia.class)
                    .greaterThanOrEqualTo("fecha", fechaFinal)
                    .equalTo("cartero", SessionPrefs.get(this).mPrefs.getString(SessionPrefs.PREF_USUARIO_LOGIN, "Sin Usuario"))
                    .equalTo("tipoMovimiento", "despacho")
                    .findFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(despachosPorDia != null) {

            realm.beginTransaction();
            despachosPorDia.setCantidadTotal(despachosPorDia.getCantidadTotal() + 1);
            despachosPorDia.setCantidadNoSincronizado(despachosPorDia.getCantidadNoSincronizado() + 1);
            realm.commitTransaction();

            TotalPiezas = despachosPorDia.getCantidadTotal();
            PiezasNoSincronizadas = despachosPorDia.getCantidadNoSincronizado();
            PiezasSincronizadas = despachosPorDia.getCantidadSincronizado();
        }else{
            realm.beginTransaction();
            realm.copyToRealm(new MovimientosPorDia(0, 1, 1, new Date(), SessionPrefs.get(this).mPrefs.getString(SessionPrefs.PREF_USUARIO_LOGIN, "Sin Usuario"), "despacho"));
            realm.commitTransaction();
            TotalPiezas = 1;
            PiezasNoSincronizadas = 1;
            PiezasSincronizadas = 0;
        }
    }

    private void inicializarContador(){
        MovimientosPorDia despachosPorDia = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        String fecha = simpleDateFormat.format(c.getTime()) + " 00:00:00";

        try {
            realm = Realm.getDefaultInstance();
            Date fechaFinal = simpleDateFormat.parse(fecha);
            despachosPorDia = realm.where(MovimientosPorDia.class)
                    .greaterThanOrEqualTo("fecha", fechaFinal)
                    .equalTo("cartero", SessionPrefs.get(this).mPrefs.getString(SessionPrefs.PREF_USUARIO_LOGIN, "Sin Usuario"))
                    .equalTo("tipoMovimiento", "despacho")
                    .findFirst();
        }catch(Exception e){
            e.printStackTrace();
        }
        if(despachosPorDia != null) {
            lblDespachoPiezasSincronizadas.setText(despachosPorDia.getCantidadSincronizado()+"");
            lblDespachoPiezasNoSincronizadas.setText(despachosPorDia.getCantidadNoSincronizado() + "");
            lblDespachoTotalPiezas.setText(despachosPorDia.getCantidadTotal() + "");
        }else{
            lblDespachoPiezasSincronizadas.setText("0");
            lblDespachoPiezasNoSincronizadas.setText("0");
            lblDespachoTotalPiezas.setText("0");
        }
    }

    private List<MovilDespachos> obtenerPiezasNoEncontradas(){
        return realmQuerys.obtenerPiezasDespachadas(SessionPrefs.get(this).mPrefs.getString(SessionPrefs.PREF_USUARIO_LOGIN, ""));
    }
}

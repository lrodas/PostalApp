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

import com.caexlogistics.postalapp.Adapters.AdapterEntrega;
import com.caexlogistics.postalapp.Models.MovilEntrega;
import com.caexlogistics.postalapp.Models.MovimientosPorDia;
import com.caexlogistics.postalapp.Preferencias.SessionPrefs;
import com.caexlogistics.postalapp.R;
import com.caexlogistics.postalapp.app.realmQuerys;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;


public class EntregaActivity extends AppCompatActivity {

    private long mLastPress = 0;    // Cuándo se pulsó atrás por última vez
    private final long mTimeLimit = 1000; // Límite de tiempo entre pulsaciones, en ms

    private List<MovilEntrega> listEntrega;

    private RecyclerView mRecyclerViewDespachos;
    private TextView lblEntregaPiezasNoSincronizadas;
    private TextView lblEntregaPiezasSincronizadas;
    private TextView lblEntregaTotalPiezas;
    private EditText txtPieza;

    private Realm realm;

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;
    private int TotalPiezas = 0, PiezasNoSincronizadas = 0, PiezasSincronizadas= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrega);

        //verificamos si esta logueado
        if(!SessionPrefs.get(this).isLoggedIn()){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        //obtenemos los despacho del dia
        listEntrega = this.obtenerPiezasNoEncontradas();

        //Inicializamos los objetos de ls UI
        bindUIElements();

        mAdapter = new AdapterEntrega(listEntrega, R.layout.recyclerview_item_entrega, new AdapterEntrega.OnItemClickListener() {
            @Override
            public void onItemClick(MovilEntrega movilEntrega, int position) {
                //para futuras implementaciones
                Toast.makeText(EntregaActivity.this, "para futuras implementaciones", Toast.LENGTH_SHORT).show();
            }
        });

        //Eventos

        txtPieza.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                    String pieza = txtPieza.getText().toString();
                    if (!pieza.trim().equals("") && !pieza.trim().equals("<br />")) {
                        MovilEntrega movilEntrega = realmQuerys.actualizarEntrega(pieza, SessionPrefs.get(EntregaActivity.this).mPrefs.getString(SessionPrefs.PREF_USUARIO_LOGIN, "Sin Usuario"));
                        if (movilEntrega == null) {
                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String fecha_actual = df.format(c.getTime());
                            Realm realm = Realm.getDefaultInstance();

                            movilEntrega = new MovilEntrega();

                            movilEntrega.setPieza(pieza);
                            movilEntrega.setCodigoCartero(SessionPrefs.get(EntregaActivity.this).mPrefs.getString(SessionPrefs.PREF_USUARIO_LOGIN, "Sin Usuario"));
                            movilEntrega.setRuta(SessionPrefs.get(EntregaActivity.this).mPrefs.getString(SessionPrefs.PREF_USUARIO_RUTA, "Sin Ruta"));
                            movilEntrega.setSincronizada(false);
                            movilEntrega.setFechaEntrega(new Date());

                            realm.beginTransaction();
                            realm.copyToRealmOrUpdate(movilEntrega);
                            realm.commitTransaction();

                            aumentarContadorEntrega();

                            lblEntregaPiezasNoSincronizadas.setText(PiezasNoSincronizadas + "");
                            lblEntregaTotalPiezas.setText(TotalPiezas + "");
                            lblEntregaPiezasSincronizadas.setText(PiezasSincronizadas + "");
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
        lblEntregaPiezasNoSincronizadas = (TextView) findViewById(R.id.lblEntregaPiezasNoSincronizadas);
        lblEntregaPiezasSincronizadas = (TextView) findViewById(R.id.lblEntregaPiezasSincronizadas);
        lblEntregaTotalPiezas = (TextView) findViewById(R.id.lblEntregaTotalPiezas);
        mRecyclerViewDespachos = (RecyclerView) findViewById(R.id.recyclerViewEntrega);
        layoutManager = new LinearLayoutManager(this);
        txtPieza = (EditText) findViewById(R.id.txtEntregaPieza);
    }

    private void aumentarContadorEntrega(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        MovimientosPorDia entregaPorDia = null;
        Calendar c = Calendar.getInstance();
        String fecha = simpleDateFormat.format(c.getTime()) + " 00:00:00";
        realm = Realm.getDefaultInstance();

        try {
            Date fechaFinal = simpleDateFormat.parse(fecha);
            entregaPorDia = realm.where(MovimientosPorDia.class)
                    .greaterThanOrEqualTo("fecha", fechaFinal)
                    .equalTo("cartero", SessionPrefs.get(this).mPrefs.getString(SessionPrefs.PREF_USUARIO_LOGIN, "Sin Usuario"))
                    .equalTo("tipoMovimiento", "entrega")
                    .findFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(entregaPorDia != null) {

            realm.beginTransaction();
            entregaPorDia.setCantidadTotal(entregaPorDia.getCantidadTotal() + 1);
            entregaPorDia.setCantidadNoSincronizado(entregaPorDia.getCantidadNoSincronizado() + 1);
            realm.commitTransaction();

            TotalPiezas = entregaPorDia.getCantidadTotal();
            PiezasNoSincronizadas = entregaPorDia.getCantidadNoSincronizado();
            PiezasSincronizadas = entregaPorDia.getCantidadSincronizado();
        }else{
            realm.beginTransaction();
            realm.copyToRealm(new MovimientosPorDia(0, 1, 1, new Date(), SessionPrefs.get(this).mPrefs.getString(SessionPrefs.PREF_USUARIO_LOGIN, "Sin Usuario"), "entrega"));
            realm.commitTransaction();
            TotalPiezas = 1;
            PiezasNoSincronizadas = 1;
            PiezasSincronizadas = 0;
        }
    }

    private void inicializarContador(){
        MovimientosPorDia entregaPorDia = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        String fecha = simpleDateFormat.format(c.getTime()) + " 00:00:00";

        try {
            realm = Realm.getDefaultInstance();
            Date fechaFinal = simpleDateFormat.parse(fecha);
            entregaPorDia = realm.where(MovimientosPorDia.class)
                    .greaterThanOrEqualTo("fecha", fechaFinal)
                    .equalTo("cartero", SessionPrefs.get(this).mPrefs.getString(SessionPrefs.PREF_USUARIO_LOGIN, "Sin Usuario"))
                    .equalTo("tipoMovimiento", "entrega")
                    .findFirst();
        }catch(Exception e){
            e.printStackTrace();
        }
        if(entregaPorDia != null) {
            lblEntregaPiezasSincronizadas.setText(entregaPorDia.getCantidadSincronizado()+"");
            lblEntregaPiezasNoSincronizadas.setText(entregaPorDia.getCantidadNoSincronizado() + "");
            lblEntregaTotalPiezas.setText(entregaPorDia.getCantidadTotal() + "");
        }else{
            lblEntregaPiezasSincronizadas.setText("0");
            lblEntregaPiezasNoSincronizadas.setText("0");
            lblEntregaTotalPiezas.setText("0");
        }
    }

    private List<MovilEntrega> obtenerPiezasNoEncontradas(){
        return realmQuerys.obtenerPiezasEntregas(SessionPrefs.get(this).mPrefs.getString(SessionPrefs.PREF_USUARIO_LOGIN, ""));
    }
}
package com.caexlogistics.postalapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.caexlogistics.postalapp.AsincTasks.PostalApi;
import com.caexlogistics.postalapp.Models.ApiError;
import com.caexlogistics.postalapp.Models.MovilTipoDevolucion;
import com.caexlogistics.postalapp.Preferencias.SessionPrefs;
import com.caexlogistics.postalapp.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.caexlogistics.postalapp.Preferencias.SessionPrefs.PREFS_NAME;
import static com.caexlogistics.postalapp.Preferencias.SessionPrefs.PREF_USUARIO_LOGIN;

public class MenuPrincipalActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private TextView lblVersion;
    private TextView lblUsuario;
    private ImageView imgFondo;
    PackageInfo pInfo;
    private PostalApi postalApi;
    private Retrofit mRestAdapter;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        if(!SessionPrefs.get(this).isLoggedIn()){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("Informacion de version", e.getMessage());
        }

        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        bindUI();
        lblUsuario.setText(prefs.getString(PREF_USUARIO_LOGIN, ""));
        lblVersion.setText(pInfo.versionName);
        //Picasso.with(MenuPrincipalActivity.this).load(R.drawable.ic_fondo).fit().into(imgFondo);

        //Instancia del adaptador e interfaz para la conexion a ws
        Gson gson = new GsonBuilder().create();
        mRestAdapter = new Retrofit.Builder()
                .baseUrl(PostalApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        postalApi = mRestAdapter.create(PostalApi.class);

        cargarTipoDevolucion();
    }

    private void cargarTipoDevolucion(){
        Call<List<MovilTipoDevolucion>> obtenerTipoDevolucion = postalApi.obtenerTipoDevolucion();
        obtenerTipoDevolucion.enqueue(new Callback<List<MovilTipoDevolucion>>() {
            @Override
            public void onResponse(Call<List<MovilTipoDevolucion>> call, Response<List<MovilTipoDevolucion>> response) {
                if(!response.isSuccessful()){
                    String error;
                    if(response.errorBody().contentType().subtype().equals("application/json") ||
                            response.errorBody().contentType().subtype().equals("json")){
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        error = apiError.getMessage();
                        Log.d("LoginActivity", apiError.getMessage());
                    }else{
                        error = response.message();
                        Log.e("LoginActivity", response.message());
                    }
                    showMenuError(error);
                    return;
                }else{
                    List<MovilTipoDevolucion> lista = response.body();
                    realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(lista);
                    realm.commitTransaction();
                }
            }

            @Override
            public void onFailure(Call<List<MovilTipoDevolucion>> call, Throwable t) {

            }
        });
    }

    private void showMenuError(String error) {
        Toast.makeText(MenuPrincipalActivity.this, error, Toast.LENGTH_LONG).show();
    }

    /*
        inicializacion de objetos en la interfaz grafica
     */
    private void bindUI(){
        lblVersion = (TextView) findViewById(R.id.lblVersion);
        lblUsuario = (TextView) findViewById(R.id.lblUsuario);
        imgFondo = (ImageView) findViewById(R.id.imgFondo);
    }

    //Eventos

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     * <p>
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_item_auto_despacho:
                startActivity(new Intent(MenuPrincipalActivity.this, DespachoActivity.class));
                return true;
            case R.id.menu_item_entrega:
                startActivity(new Intent(MenuPrincipalActivity.this, EntregaActivity.class));
                return true;
            case R.id.menu_item_devolucion:

                return true;
            case R.id.menu_item_sincronizar:

                return true;
            case R.id.menu_item_salir:
                SessionPrefs.get(MenuPrincipalActivity.this).logOut();
                startActivity(new Intent(MenuPrincipalActivity.this, LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

package com.caexlogistics.postalapp.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.caexlogistics.postalapp.AsincTasks.PostalApi;
import com.caexlogistics.postalapp.Models.ApiError;
import com.caexlogistics.postalapp.Models.Credenciales;
import com.caexlogistics.postalapp.Models.Usuario;
import com.caexlogistics.postalapp.Preferencias.SessionPrefs;
import com.caexlogistics.postalapp.R;
import com.caexlogistics.postalapp.Utils.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private Retrofit mRestAdapter;
    private PostalApi postalApi;

    private final int PERMISSION_INTERNET = 1;
    private final int ACCESS_NETWORK_STATE = 2;

    private ImageView mLogoView;
    private EditText mUserIdView;
    private EditText mPasswordView;
    private TextInputLayout mFloatLabelUserId;
    private TextInputLayout mFloatLabelPassword;
    private View mProgressView;
    private View mLoginFormView;
    private Button mSingInButton;

    public LoginActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //Instancia del adaptador e interfaz para la conexion a ws
        Gson gson = new GsonBuilder().create();
        mRestAdapter = new Retrofit.Builder()
                .baseUrl(PostalApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        postalApi = mRestAdapter.create(PostalApi.class);

        bindUI();

        //Verificacion de permisos y estado correspondientes para la conexion a internet
        Util.checkForPermission(LoginActivity.this, Manifest.permission.INTERNET, PERMISSION_INTERNET);
        Util.checkForPermission(LoginActivity.this, Manifest.permission.ACCESS_NETWORK_STATE, ACCESS_NETWORK_STATE);


        // Evento Click

        mSingInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Util.hasPermission(LoginActivity.this, Manifest.permission.ACCESS_NETWORK_STATE)) {
                    if (!isOnline()) {
                        showLoginError(getString(R.string.error_network));
                    }else {
                        attemptLogin();
                    }
                }
            }
        });
    }

    //Validaciones

    private boolean isUserIdValid(String userId) {
        return userId.length() == 6;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private void attemptLogin() {

        // Reset errors.
        mFloatLabelUserId.setError(null);
        mFloatLabelPassword.setError(null);

        // Store values at the time of the login attempt.
        String userId = mUserIdView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mFloatLabelPassword.setError(getString(R.string.error_field_required));
            focusView = mFloatLabelPassword;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mFloatLabelPassword.setError(getString(R.string.error_invalid_password));
            focusView = mFloatLabelPassword;
            cancel = true;
        }

        // Verificar si el ID tiene contenido.
        if (TextUtils.isEmpty(userId)) {
            mFloatLabelUserId.setError(getString(R.string.error_field_required));
            focusView = mFloatLabelUserId;
            cancel = true;
        } else if (!isUserIdValid(userId)) {
            mFloatLabelUserId.setError(getString(R.string.error_invalid_user_id));
            focusView = mFloatLabelUserId;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            Call<Usuario> loginCall = postalApi.login(new Credenciales(userId, password));
            //Call<Usuario> loginCall = postalApi.login(userId, password);
            loginCall.enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                    showProgress(false);

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
                        showLoginError(error);
                        return;
                    }else {
                        Usuario usuario = response.body();
                        SessionPrefs.get(LoginActivity.this).saveUser(usuario);
                        showPrincipalMenuScreen();
                    }
                }

                @Override
                public void onFailure(Call<Usuario> call, Throwable t) {
                    showProgress(false);
                    showLoginError(t.getMessage());
                    Log.e("LoginActivity", t.getMessage());
                }
            });
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    //Mensajes

    private void showProgress(boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);

        int visibility = show ? View.GONE : View.VISIBLE;
        mLogoView.setVisibility(visibility);
        mLoginFormView.setVisibility(visibility);
    }

    private void showLoginError(String error) {
        Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
    }

    //Intancia Menu Principal

    private void showPrincipalMenuScreen() {
        startActivity(new Intent(LoginActivity.this, MenuPrincipalActivity.class));
        finish();
    }

    //referencia de objetos en la UI

    private void bindUI(){
        mLogoView = (ImageView) findViewById(R.id.image_logo);
        mUserIdView = (EditText) findViewById(R.id.txtUsuario);
        mPasswordView = (EditText) findViewById(R.id.txtPassword);
        mFloatLabelUserId = (TextInputLayout) findViewById(R.id.float_label_user_id);
        mFloatLabelPassword = (TextInputLayout) findViewById(R.id.float_label_user_password);
        mSingInButton = (Button) findViewById(R.id.cmdIngresar);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }
}

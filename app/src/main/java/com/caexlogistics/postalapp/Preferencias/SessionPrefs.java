package com.caexlogistics.postalapp.Preferencias;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.caexlogistics.postalapp.Models.Usuario;

/**
 * Manejador de preferencias de la sesión del afiliado
 */
public class SessionPrefs {

    public static final String PREFS_NAME = "POSTALAPP_PREFS";
    public static final String PREF_USUARIO_LOGIN = "PREF_USUARIO_LOGIN";
    public static final String PREF_USUARIO_NOMBRE = "PREF_USUARIO_NOMBRE";
    public static final String PREF_USUARIO_CLAVE = "PREF_USUARIO_CLAVE";
    public static final String PREF_USUARIO_RUTA = "PREF_USUARIO_RUTA";
    public static final String PREF_USUARIO_ESTADO = "PREF_USUARIO_ESTADO";

    public final SharedPreferences mPrefs;

    private boolean mIsLoggedIn = false;

    private static SessionPrefs INSTANCE;

    public static SessionPrefs get(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SessionPrefs(context);
        }
        return INSTANCE;
    }

    private SessionPrefs(Context context) {
        mPrefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        mIsLoggedIn = !TextUtils.isEmpty(mPrefs.getString(PREF_USUARIO_LOGIN, "")) && !TextUtils.isEmpty(mPrefs.getString(PREF_USUARIO_CLAVE, ""));
    }

    public boolean isLoggedIn() {
        return mIsLoggedIn;
    }

    public void saveUser(Usuario usuario) {
        if (usuario != null) {
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putString(PREF_USUARIO_LOGIN, usuario.getLogin());
            editor.putString(PREF_USUARIO_NOMBRE, usuario.getNombre());
            editor.putString(PREF_USUARIO_CLAVE, usuario.getClave());
            editor.putString(PREF_USUARIO_RUTA, usuario.getRuta());
            editor.apply();

            mIsLoggedIn = true;
        }
    }

    public void logOut() {
        mIsLoggedIn = false;
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(PREF_USUARIO_LOGIN, null);
        editor.putString(PREF_USUARIO_NOMBRE, null);
        editor.putString(PREF_USUARIO_CLAVE, null);
        editor.putString(PREF_USUARIO_RUTA, null);
        editor.putString(PREF_USUARIO_ESTADO, null);
        editor.apply();
    }
}

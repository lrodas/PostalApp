package com.caexlogistics.postalapp.app;

import android.app.Application;

import com.caexlogistics.postalapp.Models.MovilDespachos;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Heinz Keydel on 9/01/2017.
 */

public class MyApplication extends Application {

    public static AtomicInteger DespachoId = new AtomicInteger();

    /**
     * Called when the application is starting, before any activity, service,
     * or receiver objects (excluding content providers) have been created.
     * Implementations should be as quick as possible (for example using
     * lazy initialization of state) since the time spent in this function
     * directly impacts the performance of starting the first activity,
     * service, or receiver in a process.
     * If you override this method, be sure to call super.onCreate().
     */
    @Override
    public void onCreate() {
        SetUpRealmConfig();

        Realm realm = Realm.getDefaultInstance();
        DespachoId = getIdByTable(realm, MovilDespachos.class);

        realm.close();
    }

    private void SetUpRealmConfig(){
        Realm.init(getApplicationContext());
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .name("PostalApp")
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

    private <T extends RealmObject> AtomicInteger getIdByTable(Realm realm, Class<T> anyClass){
        RealmResults<T> realmResults =  realm.where(anyClass).findAll();
        return (realmResults.size() > 0) ? new AtomicInteger(realmResults.max("transaccion").intValue()) : new AtomicInteger();
    }
}

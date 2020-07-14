package com.caircb.rcbtracegadere;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import androidx.multidex.MultiDex;

import com.caircb.rcbtracegadere.database.AppDatabase;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

public class MyApp extends Application {
    private static MyApp sInstance = null;
    private static Intent sIntent = null;
    private static AppDatabase mDb=null;
    public final static String MY_PROVIDER = BuildConfig.APPLICATION_ID + ".providers.FileProvider";

    public static Intent getIntent() {
        return sIntent;
    }

    public static void setIntent(Intent sIntent) {
        MyApp.sIntent = sIntent;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

        @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

            try {
                // Google Play will install latest OpenSSL
                ProviderInstaller.installIfNeeded(getApplicationContext());
                SSLContext sslContext;
                sslContext = SSLContext.getInstance("TLSv1.2");
                sslContext.init(null, null, null);
                sslContext.createSSLEngine();
            } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException
                    | NoSuchAlgorithmException | KeyManagementException e) {
                e.printStackTrace();
            }

        mDb = AppDatabase.getInstance(sInstance);
    }

    public static MyApp getsInstance(){
        return sInstance;
    }
    public static AppDatabase getDBO(){return mDb;}
}


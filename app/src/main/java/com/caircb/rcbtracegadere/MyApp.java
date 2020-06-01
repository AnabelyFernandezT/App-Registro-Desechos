package com.caircb.rcbtracegadere;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import androidx.multidex.MultiDex;

import com.caircb.rcbtracegadere.database.AppDatabase;

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
        mDb = AppDatabase.getInstance(sInstance);
    }

    public static MyApp getsInstance(){
        return sInstance;
    }
    public static AppDatabase getDBO(){return mDb;}
}


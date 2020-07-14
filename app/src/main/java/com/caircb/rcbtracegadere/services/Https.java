package com.caircb.rcbtracegadere.services;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.security.SecureRandom;


import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;

public class Https {

    public static OkHttpClient.Builder enableTls12OnPreLollipop(OkHttpClient.Builder client) {
        if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT < 22) {
            try {

                InputStream caFileInputStream = MyApp.getsInstance().getResources().openRawResource(R.raw.certificate_qa);

                KeyStore keyStore = KeyStore.getInstance("PKCS12");
                keyStore.load(caFileInputStream, "RCB2020".toCharArray());

                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("X509");
                keyManagerFactory.init(keyStore, "RCB2020".toCharArray());


                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());

                client.sslSocketFactory(sc.getSocketFactory());

            } catch (Exception exc) {
                Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", exc);
            }
        }

        return client;
    }

    /*
    private List<ConnectionSpec> getSpecsBelowLollipopMR1(OkHttpClient.Builder okb) {

        try {

            SSLContext sc = SSLContext.getInstance("TLSv1.2");
            sc.init(null, null, null);
            okb.sslSocketFactory(new Tls12SocketFactory(sc.getSocketFactory()));

            ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                    .tlsVersions(TlsVersion.TLS_1_2)
                    .build();

            List<ConnectionSpec> specs = new ArrayList<>();
            specs.add(cs);
            specs.add(ConnectionSpec.COMPATIBLE_TLS);

            return specs;

        } catch (Exception exc) {
            //Timber.e("OkHttpTLSCompat Error while setting TLS 1.2"+ exc);

            return null;
        }
    }

     */
}

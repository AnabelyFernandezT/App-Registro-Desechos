package com.caircb.rcbtracegadere;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.caircb.rcbtracegadere.generics.MyAppCompatActivity;
import com.caircb.rcbtracegadere.helpers.MyAuthorization;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.utils.Utility;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText user;
    TextInputEditText pass;
    View focusView;
    LinearLayout btnlogin;
    Intent myIntent;

    String userStr,passStr;
    boolean cancel = false;

    MyAuthorization mAuthorization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);

        if(Utility.checkAndRequestPermissions(this)) {
            if (Utility.checkGooglePlayServicesAvailable(this)) {
                if (MySession.isLocalStorage() && MySession.isLogin()) {
                    initMain(true);
                } else {
                    initLogin();
                }
            }
        }
    }

    private void initLogin(){
        init();
        initBotones();
    }

    private void init() {

        user = findViewById(R.id.username);
        pass = findViewById(R.id.password);
        btnlogin = findViewById(R.id.btnlogin);

    }

    private void initBotones() {

        pass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btnlogin.callOnClick();
                }
                return false;
            }
        });

        btnlogin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        login();
                    }
                });


    }

    private void login(){
        //validar entrada de datos
        user.setError(null);
        pass.setError(null);

        userStr = user.getText().toString();
        passStr = pass.getText().toString();

        if (TextUtils.isEmpty(userStr)) {
            user.setError(getString(R.string.error_field_required));
            focusView = user;
            cancel = true;
        } else if (TextUtils.isEmpty(passStr)) {
            pass.setError(getString(R.string.error_field_required));
            focusView = pass;
            cancel = true;
        }

        //verificar si existe impedimento para ejecutar login
        if (!cancel) {

            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

            if (ActivityCompat.checkSelfPermission(LoginActivity.this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            String myAndroidDeviceId = "";
            String myAndroidDeviceSim="";

            TelephonyManager mTelephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (mTelephony.getDeviceId() != null){
                myAndroidDeviceId = mTelephony.getDeviceId();
                myAndroidDeviceSim = mTelephony.getSimSerialNumber();
            }else{
                myAndroidDeviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            }

            //MySession.setIdDevice(telephonyManager.getDeviceId().toString());
            MySession.setIdDevice(myAndroidDeviceId);
            MySession.setIdChip(myAndroidDeviceSim);

            loginUser();
        } else {
            focusView.requestFocus();
            cancel = false;
        }
    }

    private void initMain(boolean exito){

        if (MyApp.getIntent() != null) {
            myIntent = MyApp.getIntent();
        } else {
            myIntent = new Intent(LoginActivity.this, MainActivity.class);
        }
        MyApp.setIntent(myIntent);
        startActivity(myIntent);
        finish();
    }

    private void loginUser(){
        mAuthorization = new MyAuthorization(this);
        mAuthorization.setOnSuccessfulListener(new MyAuthorization.AuthorizationListener() {
            @Override
            public void onSuccessful() {
                initMain(true);
            }
        });
        mAuthorization.loginUser(userStr,passStr);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mAuthorization!=null){
            mAuthorization.Destroy();
        }
    }
}
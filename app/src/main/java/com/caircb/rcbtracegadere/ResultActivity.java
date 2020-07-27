package com.caircb.rcbtracegadere;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    TextView mensaje;
    LinearLayout btnDialogNeutral;
    public Fragment fragment;
    FragmentTransaction fragmentTransaction;
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        mensaje = findViewById(R.id.notificacionDialog);
       /* btnDialogNeutral = findViewById(R.id.btnDialogNeutral);
        btnDialogNeutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/


        if(getIntent().getExtras()!=null){
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                mensaje.append(value);
            }

        }
    }

    private void initFragment(Fragment _fragmentinit) {
        fragment = _fragmentinit;
        getFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_portal, fragment)
                .commit();
        fm = getFragmentManager();
    }
}
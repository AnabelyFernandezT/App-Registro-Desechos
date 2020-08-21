package com.caircb.rcbtracegadere.Notificaciones;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.caircb.rcbtracegadere.MainActivity;
import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.helpers.MyConstant;
import com.google.firebase.auth.FirebaseAuth;

public class ResultCambioChoferActivity extends AppCompatActivity {

    String mensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent().getExtras()!=null){
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                mensaje = (value);
            }

            onCloseApp();
        }

    }

    private void onCloseApp() {
        final DialogBuilder dialogBuilder = new DialogBuilder(ResultCambioChoferActivity.this);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setMessage(mensaje+" SU SESION SE CERRARA ");
        dialogBuilder.setPositiveButton("SI", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
                MyApp.getDBO().rutaInicioFinDao().eliminarInicioFin();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                System.exit(0);
            }
        });
        dialogBuilder.show();
    }
}

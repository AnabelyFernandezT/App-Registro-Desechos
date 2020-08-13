package com.caircb.rcbtracegadere;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.caircb.rcbtracegadere.dialogs.DialogKilometraje;

public class ResultKilometraje extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        abirDialog();
    }

    private void abirDialog(){
        DialogKilometraje kilometraje = new DialogKilometraje(ResultKilometraje.this);
        kilometraje.setCancelable(false);
        kilometraje.show();
    }
}

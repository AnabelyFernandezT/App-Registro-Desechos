package com.caircb.rcbtracegadere.dialogs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.adapters.ListaValoresAdapter;
import com.caircb.rcbtracegadere.generics.MyDialog;
import com.caircb.rcbtracegadere.models.CatalogoItemValor;

import java.util.List;

public class DialogBultosLote extends MyDialog {
    Activity _activity;
    ListView listViewBultos;
    LinearLayout btnIniciaRutaCancel;
    ListaValoresAdapter listaValoresAdapter;
    List<CatalogoItemValor> bultos;
    Integer idManifiesto,idManifiestoDetalle;

    public DialogBultosLote(Context context,Integer idManifiesto,Integer idManifiestoDetalle) {
        super(context, R.layout.dialog_bultos_lote);
        this._activity = (Activity)context;
        this.idManifiesto = idManifiesto;
        this.idManifiestoDetalle = idManifiestoDetalle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getView());
        init();
    }

    private void init() {
        bultos = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarValores(idManifiesto,idManifiestoDetalle);
        listViewBultos = getView().findViewById(R.id.listViewBultos);

        listaValoresAdapter = new ListaValoresAdapter(getActivity(),bultos,idManifiesto,idManifiestoDetalle,0,-1);
        listViewBultos.setAdapter(listaValoresAdapter);

        btnIniciaRutaCancel = getView().findViewById(R.id.btnIniciaRutaCancel);
        btnIniciaRutaCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBultosLote.this.dismiss();
            }
        });
    }
}

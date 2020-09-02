package com.caircb.rcbtracegadere.dialogs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.adapters.ListaValoresAdapter;
import com.caircb.rcbtracegadere.adapters.ManifiestoDetalleGestoresAdapter;
import com.caircb.rcbtracegadere.adapters.ManifiestoHijoGestorAdapter;
import com.caircb.rcbtracegadere.generics.MyDialog;
import com.caircb.rcbtracegadere.models.RowItemManifiestosDetalleGestores;

import java.util.List;

public class DialogManifiestosHijosGestores extends MyDialog {

    Activity _activity;
    Integer idManifiesto,idManifiestoDetalle, idTipoDesecho;
    ManifiestoHijoGestorAdapter recyclerviewAdapter;
    LinearLayout btnIniciaRutaCancel;
    ListView listViewBultos;
    List<RowItemManifiestosDetalleGestores> detalles;

    public DialogManifiestosHijosGestores(@NonNull Context context,Integer idManifiesto,Integer idManifiestoDetalle, Integer idTipoDesecho) {
        super(context, R.layout.dialog_bultos_lote);
        this._activity = (Activity)context;
        this.idManifiesto = idManifiesto;
        this.idManifiestoDetalle = idManifiestoDetalle;
        this.idTipoDesecho = idTipoDesecho;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getView());
        init();
        loadData();
    }

    private void init(){
        listViewBultos = getView().findViewById(R.id.listViewBultos);
        //recyclerviewAdapter = new ManifiestoHijoGestorAdapter(getContext());

        btnIniciaRutaCancel = getView().findViewById(R.id.btnIniciaRutaCancel);
        btnIniciaRutaCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogManifiestosHijosGestores.this.dismiss();
            }
        });
    }

    private void loadData(){
        detalles = MyApp.getDBO().manifiestoDao().manifiestosHijos(idTipoDesecho, idManifiesto);
        recyclerviewAdapter = new ManifiestoHijoGestorAdapter(getActivity(),detalles,idManifiestoDetalle,idTipoDesecho, idManifiesto);
        listViewBultos.setAdapter(recyclerviewAdapter);

    }
}

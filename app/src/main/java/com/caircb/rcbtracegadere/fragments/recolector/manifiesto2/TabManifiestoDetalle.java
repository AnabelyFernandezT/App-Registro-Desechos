package com.caircb.rcbtracegadere.fragments.recolector.manifiesto2;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.adapters.ManifiestoDetalleAdapter;
import com.caircb.rcbtracegadere.models.RowItemManifiesto;

import java.util.List;

public class TabManifiestoDetalle extends LinearLayout {

    private List<RowItemManifiesto> detalles;
    private RecyclerView recyclerView;
    ManifiestoDetalleAdapter recyclerviewAdapter;

    public TabManifiestoDetalle(Context context,Integer manifiestoID) {
        super(context);
        View.inflate(context, R.layout.tab_manifiesto_detalle, this);
        init();
    }

    private void init(){
        recyclerView = this.findViewById(R.id.recyclerManifiestoDetalle);
        recyclerviewAdapter = new ManifiestoDetalleAdapter(getContext());
    }
}

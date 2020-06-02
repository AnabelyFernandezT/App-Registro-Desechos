package com.caircb.rcbtracegadere.fragments.recolector.manifiesto2;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.adapters.ManifiestoDetalleAdapter;
import com.caircb.rcbtracegadere.models.RowItemManifiesto;

import java.util.List;

public class TabManifiestoDetalle extends LinearLayout {

    private List<RowItemManifiesto> detalles;
    private RecyclerView recyclerView;
    ManifiestoDetalleAdapter recyclerviewAdapter;
    Integer idAppManifiesto,tipoManifiesto;

    public TabManifiestoDetalle(Context context,Integer manifiestoID,Integer tipoManifiestoID) {
        super(context);
        this.idAppManifiesto=manifiestoID;
        this.tipoManifiesto=tipoManifiestoID;
        this.detalles = detalles;
        View.inflate(context, R.layout.tab_manifiesto_detalle, this);
        init();
        loadData();
    }

    private void init(){
        recyclerView = this.findViewById(R.id.recyclerManifiestoDetalle);
        recyclerviewAdapter = new ManifiestoDetalleAdapter(getContext());
    }

    private void loadData(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        detalles = MyApp.getDBO().manifiestoDetalleDao().fetchHojaRutaDetallebyIdManifiesto(idAppManifiesto);

        recyclerviewAdapter.setTaskList(detalles);
        recyclerView.setAdapter(recyclerviewAdapter);

        recyclerviewAdapter.setOnItemClickListener(new ManifiestoDetalleAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                int x=0;
            }
        });
    }
}

package com.caircb.rcbtracegadere.fragments.planta;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;

import com.caircb.rcbtracegadere.adapters.PesosAdapterPlantaLote;
import com.caircb.rcbtracegadere.database.entity.ParametroEntity;
import com.caircb.rcbtracegadere.database.entity.RecepcionQrPlantaDetalleEntity;
import com.caircb.rcbtracegadere.database.entity.RecepcionQrPlantaEntity;
import com.caircb.rcbtracegadere.dialogs.DialogListadoManifiestosPlantaQr;
import com.caircb.rcbtracegadere.generics.OnRecyclerTouchListener;
import com.caircb.rcbtracegadere.models.ItemManifiestoSede;
import com.caircb.rcbtracegadere.models.ItemQrDetallePlanta;

import java.util.List;

public class TabGeneralLotePlanta extends LinearLayout {

    Window window;
    private RecyclerView recyclerView;
    private PesosAdapterPlantaLote recyclerviewAdapter;
    TextView txtPesoTotalLote, txtCantidadTotalBultosLote, txtTotalNumeroManifiestosLote, txtNumerosManifiestoLote,txtPruebaLote;
    LinearLayout btnListadoManifiestosPlantaQr;
    private List<ItemQrDetallePlanta> rowItems;
    private RecepcionQrPlantaEntity recepcionQrPlantaEntity;
    private OnRecyclerTouchListener touchListener;

    public TabGeneralLotePlanta(Context context) {
        super(context);
        View.inflate(context, R.layout.recepcion_lote_tab_general, this);
        /*this.idAppManifiesto = manifiestoID;
        this.pesajePendiente = pesajePendiente;
        init();
        loadData();
        validarPesoExtra();
        bloquerAdiciona();*/
        init();
        initItems();
        cargarDatos();
    }

    private void init() {
        recepcionQrPlantaEntity=MyApp.getDBO().recepcionQrPlantaDao().fetchHojaRutaQrPlanta();
        recyclerView = this.findViewById(R.id.recyclerview);
        txtPesoTotalLote = (TextView) this.findViewById(R.id.txtPesoTotalLote);
        txtCantidadTotalBultosLote = (TextView) this.findViewById(R.id.txtCantidadTotalBultosLote);
        txtTotalNumeroManifiestosLote = (TextView) this.findViewById(R.id.txtTotalNumeroManifiestosLote);
        btnListadoManifiestosPlantaQr = (LinearLayout) this.findViewById(R.id.btnListadoManifiestosPlantaQr);
        btnListadoManifiestosPlantaQr.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogListadoManifiestosPlantaQr dialogListadoManifiestosPlantaQr = new DialogListadoManifiestosPlantaQr(getContext(),recepcionQrPlantaEntity);
                dialogListadoManifiestosPlantaQr.setTitle("LISTADO DE MANIFIESTOS");
                dialogListadoManifiestosPlantaQr.setCanceledOnTouchOutside(false);
                dialogListadoManifiestosPlantaQr.setPositiveButton("OK", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogListadoManifiestosPlantaQr.dismiss();
                    }
                });
                dialogListadoManifiestosPlantaQr.show();
            }
        });
        //txtNumerosManifiestoLote = (TextView) this.findViewById(R.id.txtNumerosManifiestoLote);

    }
    private void initItems() {
        rowItems = MyApp.getDBO().recepcionQrPlantaDetalleDao().fetchHojaRutaQrPlantaDetalle2();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerviewAdapter = new PesosAdapterPlantaLote(getContext(),rowItems);
        recyclerviewAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(recyclerviewAdapter);
        recyclerviewAdapter.setTaskList(rowItems);
    }

    private void cargarDatos(){
        txtPesoTotalLote.setText(recepcionQrPlantaEntity.getPesoTotalLote().toString()+" KG");
        txtCantidadTotalBultosLote.setText(recepcionQrPlantaEntity.getCantidadTotalBultos().toString());
        //txtNumerosManifiestoLote.setText(recepcionQrPlantaEntity.getNumerosManifiesto().toString());
        txtTotalNumeroManifiestosLote.setText(recepcionQrPlantaEntity.getCantidadTotalManifiestos().toString());
    }



}

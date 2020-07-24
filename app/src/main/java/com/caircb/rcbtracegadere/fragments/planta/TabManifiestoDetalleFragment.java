package com.caircb.rcbtracegadere.fragments.planta;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.databinding.adapters.ViewBindingAdapter;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.adapters.DialogMenuBaseAdapter;
import com.caircb.rcbtracegadere.adapters.ManifiestoDetalleAdapterPlanta;
import com.caircb.rcbtracegadere.dialogs.DialogBultos;
import com.caircb.rcbtracegadere.dialogs.DialogBultosPlanta;
import com.caircb.rcbtracegadere.fragments.GestorAlterno.RecepcionGestorFragment;
import com.caircb.rcbtracegadere.fragments.recolector.manifiesto2.TabManifiestoAdicional;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnRecyclerTouchListener;
import com.caircb.rcbtracegadere.models.ItemManifiestoDetalleSede;
import com.caircb.rcbtracegadere.models.MenuItem;
import com.caircb.rcbtracegadere.models.RowItemManifiesto;
import com.caircb.rcbtracegadere.tasks.UserRegistarDetalleSedeTask;
import com.caircb.rcbtracegadere.tasks.UserRegistrarPlanta;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabManifiestoDetalleFragment extends LinearLayout {

    private static final String ARG_PARAM1 = "idAppManifiesto";

    View view;
    Window window;
    Integer idAppManifiesto;
    private List<ItemManifiestoDetalleSede> detalles;
    RecyclerView recyclerView;

    RecepcionGestorFragment manifiestoGestor;
    ManifiestoDetalleAdapterPlanta recyclerviewAdapter;
    DialogBultosPlanta dialogBultos;

    public TabManifiestoDetalleFragment(Context context, Integer idAppManifiesto){
        super(context);
        View.inflate(context, R.layout.fragment_hoja_ruta_planta, this);
        this.idAppManifiesto = idAppManifiesto;
        init();
        loadData();
    }

    public void init(){
        recyclerView = this.findViewById(R.id.recyclerview);
        recyclerView.requestFocus();
        recyclerviewAdapter = new ManifiestoDetalleAdapterPlanta(getContext(),idAppManifiesto.toString(),1);
        manifiestoGestor = new RecepcionGestorFragment(getContext(),idAppManifiesto);
    }

    private void loadData(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        detalles = MyApp.getDBO().manifiestoPlantaDetalleDao().fetchManifiestosAsigByClienteOrNumManif(idAppManifiesto);
        //Integer numeroSelecionado = MyApp.getDBO().manifiestoDetalleValorSede().fetchNumeroTotalAsigByManifiesto(idAppManifiesto);
        recyclerviewAdapter.setTaskList(detalles);
        recyclerView.setAdapter(recyclerviewAdapter);

        recyclerviewAdapter.setOnItemClickListener(new ManifiestoDetalleAdapterPlanta.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Integer estadoManifiesto = MyApp.getDBO().manifiestoPlantaDao().obtenerEstadoManifiesto(idAppManifiesto);
                if(estadoManifiesto != 3){
                    openOpcionesItems(detalles.get(position).getIdManifiestoDetalle(),position);
                }
            }
        });
    }

    private void openOpcionesItems(final Integer idManifiestoDetalle,Integer position){
        dialogBultos = new DialogBultosPlanta(getContext(),idManifiestoDetalle);
        dialogBultos.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogBultos.setCancelable(false);
        dialogBultos.setmOnclickSedeListener(new DialogBultosPlanta.onclickSedeListener() {
            @Override
            public void onSucefull() {
                List<ItemManifiestoDetalleSede> detalles;
                detalles = MyApp.getDBO().manifiestoPlantaDetalleDao().fetchManifiestosAsigByClienteOrNumManif(idAppManifiesto);
                //manifiestoAdicional.validarPesoExtra();
                //Integer numeroSelecionado = MyApp.getDBO().manifiestoDetalleValorSede().fetchNumeroTotalAsigByManifiesto(idAppManifiesto);
                recyclerviewAdapter.setTaskList(detalles);
            }
        });
        dialogBultos.show();
        window = dialogBultos.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    }


}

package com.caircb.rcbtracegadere.fragments.planta;


import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.Nullable;
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
public class TabManifiestoDetalleFragment extends Fragment {

    private static final String ARG_PARAM1 = "idAppManifiesto";

    View view;
    Window window;
    Integer idAppManifiesto;
    private List<ItemManifiestoDetalleSede> detalles;
    RecyclerView recyclerView;

    LinearLayout btnManifiestoCancel,btnRegistrar;
    RecepcionGestorFragment manifiestoGestor;
    ManifiestoDetalleAdapterPlanta recyclerviewAdapter;
    UserRegistarDetalleSedeTask detalleSedeTask;
    DialogBultosPlanta dialogBultos;

    public static TabManifiestoDetalleFragment newInstance (Integer manifiestoID){
        TabManifiestoDetalleFragment f = new TabManifiestoDetalleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, manifiestoID);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idAppManifiesto= getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_hoja_ruta_planta, container, false);
        idAppManifiesto = this.getArguments().getInt(ARG_PARAM1);
        init();
        loadData();
        return view;
    }

    public void init(){
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.requestFocus();
        recyclerviewAdapter = new ManifiestoDetalleAdapterPlanta(getActivity(),idAppManifiesto.toString(),1);
        manifiestoGestor = new RecepcionGestorFragment(getActivity(),idAppManifiesto);
    }

    private void loadData(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
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
        dialogBultos = new DialogBultosPlanta(getActivity(),idManifiestoDetalle);
        dialogBultos.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogBultos.setCancelable(false);
        dialogBultos.setmOnclickSedeListener(new DialogBultosPlanta.onclickSedeListener() {
            @Override
            public void onSucefull() {
                List<ItemManifiestoDetalleSede> detalles;
                detalles = MyApp.getDBO().manifiestoPlantaDetalleDao().fetchManifiestosAsigByClienteOrNumManif(idAppManifiesto);

                //Integer numeroSelecionado = MyApp.getDBO().manifiestoDetalleValorSede().fetchNumeroTotalAsigByManifiesto(idAppManifiesto);
                recyclerviewAdapter.setTaskList(detalles);
            }
        });
        dialogBultos.show();
        window = dialogBultos.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    }


}

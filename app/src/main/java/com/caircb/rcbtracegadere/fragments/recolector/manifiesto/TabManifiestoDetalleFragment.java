package com.caircb.rcbtracegadere.fragments.recolector.manifiesto;


import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.adapters.DialogMenuBaseAdapter;
import com.caircb.rcbtracegadere.adapters.ManifiestoDetalleBaseAdapter;
import com.caircb.rcbtracegadere.adapters.ManifiestoDetalleBaseAdapterR;
import com.caircb.rcbtracegadere.dialogs.DialogBultos;
import com.caircb.rcbtracegadere.generics.OnRecyclerTouchListener;
import com.caircb.rcbtracegadere.models.MenuItem;
import com.caircb.rcbtracegadere.models.RowItemManifiesto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabManifiestoDetalleFragment extends Fragment {

    private static final String ARG_PARAM1 = "idAppManifiesto";
    private static final String ARG_PARAM2 = "Manifiestobloqueado";
    private static final String ARG_PARAM3 = "tipoPaquete";

    View view;
    ListView LtsManifiestoDetalle,mDialogMenuItems;

    Integer idAppManifiesto,tipoPaquete;
    boolean bloquear;
    private List<RowItemManifiesto> detalles;
    ManifiestoDetalleBaseAdapter adapterDetalleManifiesto;
    Dialog dialogOpcioneItem;
    DialogMenuBaseAdapter dialogMenuBaseAdapter;
    DialogBultos dialogBultos;
    //DialogPaquetes dialogPaquetes;
    Window window;

    private OnRecyclerTouchListener touchListener;
    RecyclerView recyclerView;
    ManifiestoDetalleBaseAdapterR recyclerManifiestoDetalleBaseAdapterR;

    public static TabManifiestoDetalleFragment newInstance (Integer manifiestoID, Integer tipoPaquete,Boolean bloqueado){
        TabManifiestoDetalleFragment f = new TabManifiestoDetalleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, manifiestoID);
        args.putBoolean(ARG_PARAM2, bloqueado);
        args.putInt(ARG_PARAM3, tipoPaquete!=null?tipoPaquete:0);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idAppManifiesto= getArguments().getInt(ARG_PARAM1);
            bloquear = getArguments().getBoolean(ARG_PARAM2);
            tipoPaquete = getArguments().getInt(ARG_PARAM3);
            if(tipoPaquete==0)tipoPaquete=null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab_manifiesto_detalle, container, false);
        idAppManifiesto = this.getArguments().getInt(ARG_PARAM1);
        bloquear = this.getArguments().getBoolean(ARG_PARAM2);
        init();
        //loadData();
        return view;
    }

    private void init(){
        //LtsManifiestoDetalle = (ListView)view.findViewById(R.id.LtsManifiestoDetalle);
        recyclerView = (RecyclerView)view.findViewById(R.id.LtsManifiestoDetalle);
    }


    private void openOpcionesItems(final Integer positionItem){
        dialogOpcioneItem = new Dialog(this.getContext());
        final ArrayList<MenuItem> myListOfItems = new ArrayList<>();
        myListOfItems.add(new MenuItem("BULTOS"));
        myListOfItems.add(new MenuItem("PAQUETES"));

        dialogMenuBaseAdapter = new DialogMenuBaseAdapter(this.getContext(),myListOfItems);
        View view = getLayoutInflater().inflate(R.layout.dialog_main, null);
        mDialogMenuItems =(ListView) view.findViewById(R.id.custom_list);
        mDialogMenuItems.setAdapter(dialogMenuBaseAdapter);
        mDialogMenuItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenuItem item= myListOfItems.get(position);
                if(item.getNombre().equals("BULTOS")){
                    openDialogBultos(positionItem);
                }

            }
        });
        dialogOpcioneItem.setTitle("OPCIONES");
        dialogOpcioneItem.setContentView(view);
        dialogOpcioneItem.show();
    }


    private void  loadData(){
        detalles = MyApp.getDBO().manifiestoDetalleDao().fetchHojaRutaDetallebyIdManifiesto(idAppManifiesto);

        recyclerManifiestoDetalleBaseAdapterR = new ManifiestoDetalleBaseAdapterR(getActivity(), detalles, bloquear);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerManifiestoDetalleBaseAdapterR);

        touchListener = new OnRecyclerTouchListener(getActivity(),recyclerView);
        touchListener.setClickable(new OnRecyclerTouchListener.OnRowClickListener() {
            @Override
            public void onRowClicked(int position) {
                openOpcionesItems(position);
            }
            @Override
            public void onIndependentViewClicked(int independentViewID, int position) { }
        });
        recyclerView.setAdapter(recyclerManifiestoDetalleBaseAdapterR);


        /*
        adapterDetalleManifiesto = new ManifiestoDetalleBaseAdapter(this.getContext(), detalles, bloquear);
        LtsManifiestoDetalle.setAdapter(adapterDetalleManifiesto);
        LtsManifiestoDetalle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openOpcionesItems(position);
            }
        });
        */


    }

    private void openDialogBultos(Integer position){
        if(dialogBultos==null){
            dialogOpcioneItem.dismiss();
            dialogBultos = new DialogBultos(getActivity(),position,idAppManifiesto,detalles.get(position).getId(),tipoPaquete);
            dialogBultos.setCancelable(false);
            dialogBultos.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogBultos.setOnBultoListener(new DialogBultos.OnBultoListener() {
                @Override
                public void onSuccessful(BigDecimal valor, int position, int cantidad, boolean isClose) {
                    if(isClose){
                        if(dialogBultos!=null){
                            dialogBultos.dismiss();
                            dialogBultos = null;
                        }
                        RowItemManifiesto row = detalles.get(position);
                        row.setPeso(valor.doubleValue());

                        if(row.getTipoItem()==1) row.setCantidadBulto(cantidad); //unidad
                        else if(row.getTipoItem()==2) row.setCantidadBulto(1); //servicio
                        else if(row.getTipoItem()==3) row.setCantidadBulto(row.getPeso());

                        row.setEstado(true);

                        adapterDetalleManifiesto.notifyDataSetChanged();

                    }
                }

                @Override
                public void onCanceled() {
                    if(dialogBultos!=null){
                        dialogBultos.dismiss();
                        dialogBultos=null;
                    }
                }
            });
            dialogBultos.show();

            window = dialogBultos.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if(recyclerView!=null) {
            recyclerView.addOnItemTouchListener(touchListener);
        }
    }

}

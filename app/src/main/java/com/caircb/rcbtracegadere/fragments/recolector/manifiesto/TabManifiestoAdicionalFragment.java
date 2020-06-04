package com.caircb.rcbtracegadere.fragments.recolector.manifiesto;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.adapters.ManifiestoNoRecoleccionBaseAdapter;
import com.caircb.rcbtracegadere.adapters.ManifiestoNoRecoleccionBaseAdapterR;
import com.caircb.rcbtracegadere.adapters.ManifiestoNovedadBaseAdapter;
import com.caircb.rcbtracegadere.adapters.ManifiestoNovedadBaseAdapterR;
import com.caircb.rcbtracegadere.dialogs.DialogAgregarFotografias;
import com.caircb.rcbtracegadere.generics.OnRecyclerTouchListener;
import com.caircb.rcbtracegadere.models.RowItemHojaRutaCatalogo;
import com.caircb.rcbtracegadere.models.RowItemNoRecoleccion;
import com.caircb.rcbtracegadere.utils.Utils;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabManifiestoAdicionalFragment extends Fragment {

    private static final String ARG_PARAM1 = "idAppManifiesto";
    private static final String ARG_PARAM2 = "Manifiestobloqueado";

    View view;
    EditText txtNovedadEncontrada;
    ListView LtsManifiestoObservaciones, LtsMotivoNoRecoleccion;
    DialogAgregarFotografias dialogAgregarFotografias;
    Window window;

    List<RowItemHojaRutaCatalogo> novedadfrecuentes;
    List<RowItemNoRecoleccion> motivoNoRecoleccion;

    ManifiestoNovedadBaseAdapter adapter;
    ManifiestoNoRecoleccionBaseAdapter adapterN;

    Integer idAppManifiesto;
    boolean bloquear;

    private OnRecyclerTouchListener touchListener;
    RecyclerView recyclerViewLtsManifiestoObservaciones, recyclerViewLtsMotivoNoRecoleccion;
    ManifiestoNovedadBaseAdapterR recyclerAdapterNovedades;
    ManifiestoNoRecoleccionBaseAdapterR recyclerAdapterNoRecolecciones;

    public static TabManifiestoAdicionalFragment newInstance (Integer manifiestoID, Boolean bloqueado){
        TabManifiestoAdicionalFragment f = new TabManifiestoAdicionalFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, manifiestoID);
        args.putBoolean(ARG_PARAM2, bloqueado);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idAppManifiesto= getArguments().getInt(ARG_PARAM1);
            bloquear = getArguments().getBoolean(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab_manifiesto_adicional, container, false);
        init();
        loadData();
        return  view;
    }

    private void init(){
        txtNovedadEncontrada = view.findViewById(R.id.txtNovedadEncontrada);

        //LtsManifiestoObservaciones = view.findViewById(R.id.LtsManifiestoObservaciones);
        //LtsMotivoNoRecoleccion = view.findViewById(R.id.LtsMotivoNoRecoleccion);

        recyclerViewLtsMotivoNoRecoleccion = view.findViewById(R.id.LtsMotivoNoRecoleccion);
        recyclerViewLtsManifiestoObservaciones = view.findViewById(R.id.LtsManifiestoObservaciones);

    }

    private void loadData(){
        novedadfrecuentes = MyApp.getDBO().manifiestoObservacionFrecuenteDao().fetchHojaRutaCatalogoNovedaFrecuente(idAppManifiesto);

        /**Obvervaciones (Novedades)**/
        recyclerViewLtsManifiestoObservaciones.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerAdapterNovedades = new ManifiestoNovedadBaseAdapterR(getActivity(), novedadfrecuentes, bloquear,0   );

        /*recyclerAdapterNovedades.setOnClickOpenFotografias(new ManifiestoNovedadBaseAdapterR.OnClickOpenFotografias() {
            @Override
            public void onShow(Integer catalogoID, Integer position) {
                @Override
                public void onShow(Integer catalogoID, final Integer position) {
                    String nombre = "";
                    if(dialogAgregarFotografias==null){
                        dialogAgregarFotografias = new DialogAgregarFotografias(getActivity(),idAppManifiesto,catalogoID,1);
                        dialogAgregarFotografias.setCancelable(false);
                        dialogAgregarFotografias.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialogAgregarFotografias.setOnAgregarFotosListener(new DialogAgregarFotografias.OnAgregarFotosListener() {
                            @Override
                            public void onSuccessful(Integer cantidad) {
                                if(dialogAgregarFotografias!=null && dialogAgregarFotografias.isShowing()){
                                    dialogAgregarFotografias.dismiss();
                                    dialogAgregarFotografias=null;

                                    novedadfrecuentes.get(position).setNumFotos(cantidad);
                                    recyclerAdapterNovedades.notifyDataSetChanged();

                                }
                            }
                        });
                        dialogAgregarFotografias.show();

                        window = dialogAgregarFotografias.getWindow();
                        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    }
                }
            }
        });
        */
        recyclerViewLtsManifiestoObservaciones.setAdapter(recyclerAdapterNovedades);


        motivoNoRecoleccion = MyApp.getDBO().manifiestoMotivosNoRecoleccionDao().fetchHojaRutaMotivoNoRecoleccion(idAppManifiesto);
        recyclerViewLtsMotivoNoRecoleccion.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerAdapterNoRecolecciones = new ManifiestoNoRecoleccionBaseAdapterR(this.getContext(), motivoNoRecoleccion,idAppManifiesto, bloquear);
        recyclerAdapterNoRecolecciones.setOnClickOpenFotografias(new ManifiestoNoRecoleccionBaseAdapterR.OnClickOpenFotografias() {
            @Override
            public void onShow(Integer catalogoID, final Integer position) {
                if(dialogAgregarFotografias==null){
                    dialogAgregarFotografias = new DialogAgregarFotografias(getActivity(),idAppManifiesto,catalogoID,2);
                    dialogAgregarFotografias.setCancelable(false);
                    dialogAgregarFotografias.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialogAgregarFotografias.setOnAgregarFotosListener(new DialogAgregarFotografias.OnAgregarFotosListener() {
                        @Override
                        public void onSuccessful(Integer cantidad) {
                            if(dialogAgregarFotografias!=null && dialogAgregarFotografias.isShowing()){
                                dialogAgregarFotografias.dismiss();
                                dialogAgregarFotografias=null;

                                motivoNoRecoleccion.get(position).setNumFotos(cantidad);

                                recyclerAdapterNoRecolecciones.notifyDataSetChanged();
                            }
                        }
                    });
                    dialogAgregarFotografias.show();

                    window = dialogAgregarFotografias.getWindow();
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                }
            }
        });
        recyclerViewLtsMotivoNoRecoleccion.setAdapter(recyclerAdapterNoRecolecciones);

/*
        adapter = new ManifiestoNovedadBaseAdapter(this.getContext(), novedadfrecuentes, bloquear);
        adapter.setOnClickOpenFotografias(new ManifiestoNovedadBaseAdapter.OnClickOpenFotografias() {
            @Override
            public void onShow(Integer catalogoID, final Integer position) {
                if(dialogAgregarFotografias==null){
                    dialogAgregarFotografias = new DialogAgregarFotografias(getActivity(),idAppManifiesto,catalogoID,1);
                    dialogAgregarFotografias.setCancelable(false);
                    dialogAgregarFotografias.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialogAgregarFotografias.setOnAgregarFotosListener(new DialogAgregarFotografias.OnAgregarFotosListener() {
                        @Override
                        public void onSuccessful(Integer cantidad) {
                            if(dialogAgregarFotografias!=null && dialogAgregarFotografias.isShowing()){
                                dialogAgregarFotografias.dismiss();
                                dialogAgregarFotografias=null;

                                novedadfrecuentes.get(position).setNumFotos(cantidad);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                    dialogAgregarFotografias.show();

                    window = dialogAgregarFotografias.getWindow();
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                }
            }
        });
        LtsManifiestoObservaciones.setAdapter(adapter);
        Utils.setListViewHeightBasedOnChildren(LtsManifiestoObservaciones);

        motivoNoRecoleccion = MyApp.getDBO().manifiestoMotivosNoRecoleccionDao().fetchHojaRutaMotivoNoRecoleccion(idAppManifiesto);
        adapterN = new ManifiestoNoRecoleccionBaseAdapter(this.getContext(),motivoNoRecoleccion,idAppManifiesto,bloquear);
        adapterN.setOnClickOpenFotografias(new ManifiestoNoRecoleccionBaseAdapter.OnClickOpenFotografias() {
            @Override
            public void onShow(Integer catalogoID, final Integer position) {
                if(dialogAgregarFotografias==null){
                    dialogAgregarFotografias = new DialogAgregarFotografias(getActivity(),idAppManifiesto,catalogoID,2);
                    dialogAgregarFotografias.setCancelable(false);
                    dialogAgregarFotografias.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialogAgregarFotografias.setOnAgregarFotosListener(new DialogAgregarFotografias.OnAgregarFotosListener() {
                        @Override
                        public void onSuccessful(Integer cantidad) {
                            if(dialogAgregarFotografias!=null && dialogAgregarFotografias.isShowing()){
                                dialogAgregarFotografias.dismiss();
                                dialogAgregarFotografias=null;

                                motivoNoRecoleccion.get(position).setNumFotos(cantidad);
                                adapterN.notifyDataSetChanged();
                                //adapter.notifyDataSetChanged();
                                //loadData();
                            }
                        }
                    });
                    dialogAgregarFotografias.show();

                    window = dialogAgregarFotografias.getWindow();
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                }
            }
        });
        LtsMotivoNoRecoleccion.setAdapter(adapterN);
        Utils.setListViewHeightBasedOnChildren(LtsMotivoNoRecoleccion);
*/
    }

    public void setMakePhoto(Integer code) {
        if(dialogAgregarFotografias!=null){
            dialogAgregarFotografias.setMakePhoto(code);
        }
    }

}

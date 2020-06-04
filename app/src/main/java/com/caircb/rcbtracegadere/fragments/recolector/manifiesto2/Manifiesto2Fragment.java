package com.caircb.rcbtracegadere.fragments.recolector.manifiesto2;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.Toast;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.fragments.recolector.HojaRutaAsignadaFragment;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnCameraListener;
import com.caircb.rcbtracegadere.models.RowItemHojaRutaCatalogo;
import com.caircb.rcbtracegadere.models.RowItemNoRecoleccion;

import java.util.List;


public class Manifiesto2Fragment extends MyFragment implements OnCameraListener,View.OnClickListener {

    private static final String ARG_PARAM1 = "manifiestoID";

    LinearLayout btnManifiestoCancel, btnManifiestoNext;

    TabManifiestoGeneral tabManifiestoGeneral;
    TabManifiestoDetalle tabManifiestoDetalle;
    TabManifiestoAdicional tabManifiestoAdicional;

    Integer idAppManifiesto;


    public Manifiesto2Fragment() {
    }

    public static Manifiesto2Fragment newInstance(Integer manifiestoID) {
        Manifiesto2Fragment f= new Manifiesto2Fragment();
        Bundle b = new Bundle();
        b.putInt(ARG_PARAM1,manifiestoID);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            idAppManifiesto = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setView(inflater.inflate(R.layout.fragment_hoja_ruta2, container, false));
        init();
        initTab();
        return getView();
    }

    private void init(){

        btnManifiestoCancel = getView().findViewById(R.id.btnManifiestoCancel);
        btnManifiestoCancel.setOnClickListener(this);
        btnManifiestoNext = getView().findViewById(R.id.btnManifiestoNext);
        btnManifiestoNext.setOnClickListener(this);
    }



    private void initTab(){
        TabHost tabs=(TabHost)getView().findViewById(android.R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec=tabs.newTabSpec("GENERAL");
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                tabManifiestoGeneral = new TabManifiestoGeneral(getActivity(),idAppManifiesto);
                return tabManifiestoGeneral;
            }
        });
        spec.setIndicator("GENERAL");
        tabs.addTab(spec);

        spec=tabs.newTabSpec("DETALLE");
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                tabManifiestoDetalle = new TabManifiestoDetalle(getActivity(),idAppManifiesto,tabManifiestoGeneral.getTipoPaquete());
                return tabManifiestoDetalle;
            }
        });
        spec.setIndicator("DETALLE");
        tabs.addTab(spec);

        spec=tabs.newTabSpec("ADICIONALES");
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                tabManifiestoAdicional = new TabManifiestoAdicional(getActivity(),
                        idAppManifiesto,
                        tabManifiestoGeneral.getTipoPaquete(),
                        tabManifiestoGeneral.getAudio(),
                        tabManifiestoGeneral.getTiempoAudio());
                return tabManifiestoAdicional;
            }
        });
        spec.setIndicator("ADICIONALES");
        tabs.addTab(spec);

        tabs.setCurrentTab(0);

        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                if(s.equals("ADICIONALES")){
                    tabManifiestoAdicional.reloadDataPaquetes();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnManifiestoCancel:
                setNavegate(HojaRutaAsignadaFragment.newInstance());
                break;
            case R.id.btnManifiestoNext:
                //vista preliminar...
                if(validaObservacioneswithFotos(idAppManifiesto)){
                    setNavegate(VistaPreliminarFragment.newInstance(idAppManifiesto,tabManifiestoGeneral.getTipoPaquete() ));
                }else{
                    Toast.makeText(getActivity(),"Las novedades o no recolleciones seleccionadas necesitan al menos una fotografía!!", Toast.LENGTH_SHORT).show();
                    //messageBox("Las novedades o no recolleciones seleccionadas necesitan al menos una fotografía!!");
                }
                break;
        }
    }

    private boolean validaObservacioneswithFotos(Integer idManifiesto){
        boolean faltaFotoNovedad = false, faltaFotoNoRec = false, valido = false;
        List<RowItemHojaRutaCatalogo>  novedadfrecuentes = MyApp.getDBO().manifiestoObservacionFrecuenteDao().fetchHojaRutaCatalogoNovedaFrecuente(idManifiesto);
        List<RowItemNoRecoleccion> motivoNoRecoleccion = MyApp.getDBO().manifiestoMotivosNoRecoleccionDao().fetchHojaRutaMotivoNoRecoleccion(idManifiesto);

        for(RowItemHojaRutaCatalogo i : novedadfrecuentes){
            if(i.isEstadoChek()){
                if(i.getNumFotos()== 0){ faltaFotoNovedad = true;}
            }
        }
        for(RowItemNoRecoleccion i : motivoNoRecoleccion){
            if(i.isEstadoChek()){
                if(i.getNumFotos() == 0){ faltaFotoNoRec = true;}
            }
        }

        if(faltaFotoNovedad || faltaFotoNoRec){
            valido= false;
        }else {
            valido = true;
        }

        return valido;
    }

    @Override
    public void onCameraResult(int requestCode, int resultCode, Intent data) {
        if(tabManifiestoAdicional!=null && ((requestCode>=101 && requestCode<=104) ||(requestCode>=201 && requestCode<=204))){
            tabManifiestoAdicional.setMakePhoto(requestCode);
        }
    }
}
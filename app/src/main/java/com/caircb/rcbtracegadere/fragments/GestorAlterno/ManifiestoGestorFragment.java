package com.caircb.rcbtracegadere.fragments.GestorAlterno;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost;

import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.fragments.planta.HojaRutaAsignadaPlantaFragment;
import com.caircb.rcbtracegadere.fragments.planta.RecepcionPlantaFragment;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnCameraListener;
import com.caircb.rcbtracegadere.tasks.UserRegistrarPlanta;


public class ManifiestoGestorFragment extends MyFragment implements OnCameraListener, View.OnClickListener {
    LinearLayout btnManifiestoCancel, btnManifiestoNext;

    private static final String ARG_PARAM1 = "manifiestoID";

    RecepcionGestorFragment manifiestoGestor;

    Integer idAppManifiesto;
    UserRegistrarPlanta userRegistrarPlanta;


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnManifiestoCancel:
                setNavegate(HojaRutaAsignadaGestorFragment.newInstance());
                break;
            case R.id.btnManifiestoNext:
                if(manifiestoGestor.validaNovedadesFrecuentesPendienteFotos()){
                    messageBox("Las novedades frecuentes seleccionadas deben contener al menos una fotografia de evidencia");
                    return;
                }
                if(manifiestoGestor.validaExisteFirma()){
                    messageBox("Se requiere firma  ");
                    return;
                }

                if(manifiestoGestor.validaPeso()){
                    messageBox("Se requiere que ingrese el peso");
                    return;
                }


                double peso = manifiestoGestor.guardar();
                userRegistrarPlanta = new UserRegistrarPlanta(getActivity(),idAppManifiesto,peso);
                userRegistrarPlanta.setOnRegisterListener(new UserRegistrarPlanta.OnRegisterListener() {
                    @Override
                    public void onSuccessful() {
                       messageBox("Datos Guardados");
                       setNavegate(HojaRutaAsignadaGestorFragment.newInstance());
                    }
                });
                userRegistrarPlanta.execute();

                break;
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            idAppManifiesto = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public void onCameraResult(int requestCode, int resultCode, Intent data) {
        if(manifiestoGestor!=null && ((requestCode>=301 && requestCode<=304) ||(requestCode>=201 && requestCode<=204))) {
            manifiestoGestor.setMakePhoto(requestCode);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setView(inflater.inflate(R.layout.fragment_hoja_ruta_gestor, container, false));
        init();
        iniTab();

        return getView();
    }

    private void iniTab() {
        TabHost tabs=(TabHost)getView().findViewById(android.R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec=tabs.newTabSpec("GESTOR ALTERNO");
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return manifiestoGestor;
            }
        });
        spec.setIndicator("GESTOR ALTERNO");
        tabs.addTab(spec);
    }

    public void init(){
        btnManifiestoCancel = getView().findViewById(R.id.btnManifiestoCancel);
        btnManifiestoCancel.setOnClickListener(this);
        btnManifiestoNext = getView().findViewById(R.id.btnManifiestoNext);
        btnManifiestoNext.setOnClickListener(this);

        manifiestoGestor = new RecepcionGestorFragment(getActivity(),idAppManifiesto);
    }



    public static ManifiestoGestorFragment newInstance() {
        return new ManifiestoGestorFragment();
    }

    public static ManifiestoGestorFragment newInstance(Integer manifiestoID) {
        ManifiestoGestorFragment f= new ManifiestoGestorFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_PARAM1,manifiestoID);
        f.setArguments(b);
        return f;
    }
}

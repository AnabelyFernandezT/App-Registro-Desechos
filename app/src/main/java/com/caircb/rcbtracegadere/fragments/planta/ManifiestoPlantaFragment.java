package com.caircb.rcbtracegadere.fragments.planta;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost;

import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnCameraListener;
import com.caircb.rcbtracegadere.tasks.UserRegistrarPlanta;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class ManifiestoPlantaFragment extends MyFragment implements OnCameraListener, View.OnClickListener {
    LinearLayout btnManifiestoCancel, btnManifiestoNext;

    private static final String ARG_PARAM1 = "manifiestoID";

    RecepcionPlantaFragment manifiestoPlanta;

    Integer idAppManifiesto;
    UserRegistrarPlanta userRegistrarPlanta;
    FloatingActionButton mensajes;
    double peso;
    DialogBuilder dialogBuilder;


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnManifiestoCancel:
                setNavegate(HojaRutaAsignadaFragmentNO.newInstance());
                break;
            case R.id.btnManifiestoNext:
              /*  if(manifiestoPlanta.validaNovedadesFrecuentesPendienteFotos()){
                    messageBox("Las novedades frecuentes seleccionadas deben contener al menos una fotografia de evidencia");
                    return;
                }*/
                if(manifiestoPlanta.validaExisteFirma()){
                    messageBox("Se requiere firma  ");
                    return;
                }

                if(manifiestoPlanta.validaPeso()){
                    messageBox("Se requiere que ingrese el peso");
                    return;
                }


                peso = manifiestoPlanta.guardar();

                dialogBuilder = new DialogBuilder(getActivity());
                dialogBuilder.setTitle("");
                dialogBuilder.setMessage("Esta seguro de continuar");
                dialogBuilder.setCancelable(false);
                dialogBuilder.setPositiveButton("SI", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                        userRegistrarPlanta = new UserRegistrarPlanta(getActivity(),idAppManifiesto,peso);
                        userRegistrarPlanta.setOnRegisterListener(new UserRegistrarPlanta.OnRegisterListener() {
                            @Override
                            public void onSuccessful() {

                                setNavegate(HojaRutaAsignadaFragmentNO.newInstance());
                            }
                        });
                        userRegistrarPlanta.execute();
                    }
                });

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
        if(manifiestoPlanta!=null && ((requestCode>=301 && requestCode<=304) ||(requestCode>=201 && requestCode<=204))) {
            manifiestoPlanta.setMakePhoto(requestCode);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setView(inflater.inflate(R.layout.fragment_hoja_ruta2, container, false));
        init();
        iniTab();

        return getView();
    }

    private void iniTab() {
        TabHost tabs=(TabHost)getView().findViewById(android.R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec=tabs.newTabSpec("PLANTA");
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return manifiestoPlanta;
            }
        });
        spec.setIndicator("PLANTA");
        tabs.addTab(spec);
    }

    @SuppressLint("RestrictedApi")
    public void init(){
        btnManifiestoCancel = getView().findViewById(R.id.btnManifiestoCancel);
        btnManifiestoCancel.setOnClickListener(this);
        btnManifiestoNext = getView().findViewById(R.id.btnManifiestoNext);
        btnManifiestoNext.setOnClickListener(this);
        mensajes = getView().findViewById(R.id.fab);
        mensajes.setVisibility(View.INVISIBLE);

        manifiestoPlanta = new RecepcionPlantaFragment(getActivity(),idAppManifiesto);
    }



    public static ManifiestoPlantaFragment newInstance() {
        return new ManifiestoPlantaFragment();
    }

    public static ManifiestoPlantaFragment newInstance(Integer manifiestoID) {
        ManifiestoPlantaFragment f= new ManifiestoPlantaFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_PARAM1,manifiestoID);
        f.setArguments(b);
        return f;
    }
}

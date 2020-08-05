package com.caircb.rcbtracegadere.fragments.recolector.MotivoNoRecoleccion;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.fragments.recolector.HojaRutaAsignadaFragment;
import com.caircb.rcbtracegadere.fragments.recolector.HojaRutaBuscarFragment;
import com.caircb.rcbtracegadere.fragments.recolector.HojaRutaProcesadaFragment;
import com.caircb.rcbtracegadere.fragments.recolector.manifiesto2.TabManifiestoAdicional;
import com.caircb.rcbtracegadere.fragments.recolector.manifiesto2.TabManifiestoDetalle;
import com.caircb.rcbtracegadere.fragments.recolector.manifiesto2.TabManifiestoGeneral;
import com.caircb.rcbtracegadere.fragments.recolector.manifiesto2.VistaPreliminarFragment;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnCameraListener;
import com.caircb.rcbtracegadere.tasks.UserRegistrarNoRecoleccion;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;


public class ManifiestoNoRecoleccionFragment extends MyFragment implements OnCameraListener,View.OnClickListener {

    private static final String ARG_PARAM1 = "manifiestoID";
    private static final String ARG_ESTADO = "estado";
    private static final String ARG_ESTPATALLA = "pantallEst";

    LinearLayout btnManifiestoCancel, btnManifiestoNext;

    TabManifiestoGeneralNoRecoleccion tabManifiestoGeneral;
    TabManifiestoAdicionalNoRecoleccion tabManifiestoAdicional;

    Integer idAppManifiesto,pantallaEstado;
    TabHost tabs;



    public ManifiestoNoRecoleccionFragment() {
    }

    public static ManifiestoNoRecoleccionFragment newInstance(Integer manifiestoID,Integer pantallaEstado) {
        ManifiestoNoRecoleccionFragment f= new ManifiestoNoRecoleccionFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_PARAM1,manifiestoID);
        b.putInt(ARG_ESTPATALLA,pantallaEstado);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            idAppManifiesto = getArguments().getInt(ARG_PARAM1);
            pantallaEstado = getArguments().getInt(ARG_ESTPATALLA);
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

    @SuppressLint("RestrictedApi")
    private void init(){

        btnManifiestoCancel = getView().findViewById(R.id.btnManifiestoCancel);
        btnManifiestoCancel.setOnClickListener(this);
        btnManifiestoNext = getView().findViewById(R.id.btnManifiestoNext);
        btnManifiestoNext.setOnClickListener(this);

    }



    private void initTab(){

        inicializeTab();

        TabHost tabs=(TabHost)getView().findViewById(android.R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec=tabs.newTabSpec("GENERAL");
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return tabManifiestoGeneral;
            }
        });
        spec.setIndicator("GENERAL");
        tabs.addTab(spec);

        spec=tabs.newTabSpec("ADICIONALES");
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
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

                }
            }
        });
    }

    private void inicializeTab(){

        tabManifiestoGeneral = new TabManifiestoGeneralNoRecoleccion(getActivity(),idAppManifiesto,pantallaEstado);

        tabManifiestoAdicional = new TabManifiestoAdicionalNoRecoleccion(getActivity(),
                idAppManifiesto,
                tabManifiestoGeneral.getTipoPaquete(),
                tabManifiestoGeneral.getTiempoAudio(),pantallaEstado);
    }

    @Override
    public void onClick(View view) {
        tabs=(TabHost)getView().findViewById(android.R.id.tabhost);
        switch (view.getId()){
            case R.id.btnManifiestoCancel:
                switch (pantallaEstado){
                    case 1:
                        int i= tabs.getCurrentTab();
                        if (i==0) {
                            setNavegate(HojaRutaAsignadaFragment.newInstance());
                            break;
                        }
                        if (i==1){
                            tabs.setCurrentTab(tabs.getCurrentTab()-1);
                            break;
                        }
                    case 2:
                        int j= tabs.getCurrentTab();
                        if (j==0){
                            setNavegate(HojaRutaAsignadaFragment.newInstance());
                            break;
                        }
                        if (j==1){
                            tabs.setCurrentTab(tabs.getCurrentTab()-1);
                            break;
                        }
                    case 3:
                        setNavegate(HojaRutaProcesadaFragment.newInstance());
                        break;
                }

                break;
            case R.id.btnManifiestoNext:
                //tab genearl...
                MyApp.getDBO().manifiestoDetallePesosDao().deleteTableValoresByIdManifiesto(idAppManifiesto);
                MyApp.getDBO().manifiestoDetalleDao().updateNoRecolectado(idAppManifiesto,0.0,0.0);
                MyApp.getDBO().manifiestoPaqueteDao().deleteTablePaquetes();
                boolean aplicaNoRecoleccion= tabManifiestoAdicional.validaExisteNovedadesNoRecoleccion();
                int i=tabs.getCurrentTab();
                if (i==0){
                    tabs.setCurrentTab(tabs.getCurrentTab()+1);
                }

                if (i==1) {
                    Integer estadoCheck;
                    estadoCheck = MyApp.getDBO().manifiestoMotivosNoRecoleccionDao().fetchHojaRutaMotivoNoRecoleccionEstado(idAppManifiesto);

                    if (estadoCheck > 0) {

                        if (!tabManifiestoGeneral.validaExisteFirmaTransportista()) {
                            messageBox("Se requiere de la firma del transportista");
                            return;
                        }
                   /*
                    if(tabManifiestoGeneral.validaExisteFirmaTransportista() && !aplicaNoRecoleccion){
                        messageBox("Se requiere de la firma del transportista");
                        return;
                    }*/
                        if (tabManifiestoAdicional.validaNovedadNoRecoleccionPendicenteFotos()) {
                            messageBox("Las novedades de no recoleccion seleccionadas deben contener al menos una fotografia de evidencia");
                            return;
                        }
                        setNavegate(VistaPreliminarNoRecolectadoFragment.newInstance(
                                idAppManifiesto,
                                tabManifiestoGeneral.getTipoPaquete()
                        ));
                    } else {
                        messageBox("Ingrese motivo de NO RECOLECCIÓN");
                    }
 /*               if(!tabManifiestoGeneral.validaExisteDatosResponsableEntrega() && !aplicaNoRecoleccion){
                    messageBox("Se require que ingrese los datos del tecnico responsable de la entrega de los residuos recolectados");
                    return;
                }*/
               /* if(tabManifiestoGeneral.validaExisteFirmaTecnicoGenerador() && !aplicaNoRecoleccion){
                    messageBox("Se requiere de la firma del tecnico generador");
                    return;
                }
**/

                    break;
                }
        }
    }


    @Override
    public void onCameraResult(int requestCode, int resultCode, Intent data) {
        if(tabManifiestoAdicional!=null && ((requestCode>=101 && requestCode<=104) ||(requestCode>=201 && requestCode<=204))){
            tabManifiestoAdicional.setMakePhoto(requestCode);
        }
    }
}
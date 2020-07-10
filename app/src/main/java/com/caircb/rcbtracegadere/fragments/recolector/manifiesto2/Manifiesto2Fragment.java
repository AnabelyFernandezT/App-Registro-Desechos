package com.caircb.rcbtracegadere.fragments.recolector.manifiesto2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.caircb.rcbtracegadere.MainActivity;
import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.dialogs.DialogManifiestoCliente;
import com.caircb.rcbtracegadere.dialogs.DialogMensajes;
import com.caircb.rcbtracegadere.fragments.recolector.HojaRutaAsignadaFragment;
import com.caircb.rcbtracegadere.fragments.recolector.HojaRutaBuscarFragment;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnCameraListener;
import com.caircb.rcbtracegadere.models.RowItemHojaRutaCatalogo;
import com.caircb.rcbtracegadere.models.RowItemNoRecoleccion;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class Manifiesto2Fragment extends MyFragment implements OnCameraListener,View.OnClickListener {

    private static final String ARG_PARAM1 = "manifiestoID";
    private static final String ARG_PANTALLA = "pantallaID";

    LinearLayout btnManifiestoCancel, btnManifiestoNext;

    TabManifiestoGeneral tabManifiestoGeneral;
    TabManifiestoDetalle tabManifiestoDetalle;
    TabManifiestoAdicional tabManifiestoAdicional;

    FloatingActionButton mensajes;
    Integer idAppManifiesto,estadoPantalla;

    DialogManifiestoCliente manifiestoCliente;

    public Manifiesto2Fragment() {
    }

    public static Manifiesto2Fragment newInstance(Integer manifiestoID,Integer estadoPantalla) {
        Manifiesto2Fragment f= new Manifiesto2Fragment();
        Bundle b = new Bundle();
        b.putInt(ARG_PARAM1,manifiestoID);
        b.putInt(ARG_PANTALLA,estadoPantalla); /// llega uno de la pantalla de buscar y 2 desde la de manifiestos
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            idAppManifiesto = getArguments().getInt(ARG_PARAM1);
            estadoPantalla = getArguments().getInt(ARG_PANTALLA);
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
        mensajes = getView().findViewById(R.id.fab);
        mensajes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogMensajes dialogMensajes = new DialogMensajes(getActivity());
                dialogMensajes.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogMensajes.show();
            }
        });
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

        spec=tabs.newTabSpec("DETALLE");
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return tabManifiestoDetalle;
            }
        });
        spec.setIndicator("DETALLE");
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
                    tabManifiestoAdicional.reloadDataPaquetes();
                }
            }
        });
    }

    private void inicializeTab(){

        tabManifiestoGeneral = new TabManifiestoGeneral(getActivity(),idAppManifiesto,1);

        tabManifiestoDetalle = new TabManifiestoDetalle(getActivity(),
                idAppManifiesto,
                tabManifiestoGeneral.getTipoPaquete(),
                tabManifiestoGeneral.getNumeroManifiesto(),1);

        tabManifiestoAdicional = new TabManifiestoAdicional(getActivity(),
                idAppManifiesto,
                tabManifiestoGeneral.getTipoPaquete(),
                tabManifiestoGeneral.getTiempoAudio(),
                1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnManifiestoCancel:
                //setNavegate(HojaRutaAsignadaFragment.newInstance());
                switch (estadoPantalla){
                    case 1:
                        setNavegate(HojaRutaAsignadaFragment.newInstance());
                        break;
                    case 2:
                        setNavegate(HojaRutaBuscarFragment.newInstance());
                        break;
                }

                break;
            case R.id.btnManifiestoNext:
                //tab genearl...
                boolean aplicaNoRecoleccion= tabManifiestoAdicional.validaExisteNovedadesNoRecoleccion();

                if(tabManifiestoGeneral.validaExisteFirmaTransportista() && !aplicaNoRecoleccion){
                    messageBox("Se requiere de la firma del transportista");
                    return;
                }
                if(!tabManifiestoGeneral.validaExisteDatosResponsableEntrega() && !aplicaNoRecoleccion){
                    messageBox("Se require que ingrese los datos del tecnico responsable de la entrega de los residuos recolectados");
                    return;
                }
                if(tabManifiestoGeneral.validaExisteFirmaTecnicoGenerador() && !aplicaNoRecoleccion){
                    messageBox("Se requiere de la firma del tecnico generador");
                    return;
                }
                //tab detalle...
                if(!tabManifiestoDetalle.validaExisteDetallesSeleccionados() && !aplicaNoRecoleccion) {
                    messageBox("Se requiere que registre al menos un item como recolectado");
                    return;
                }
                //tab de adicionales...
                if(tabManifiestoAdicional.validaNovedadesFrecuentesPendienteFotos()){
                    messageBox("Las novedades frecuentes seleccionadas deben contener al menos una fotografia de evidencia");
                    return;
                }
               /* if(tabManifiestoAdicional.validaNovedadNoRecoleccionPendicenteFotos()){
                    messageBox("Las novedades de no recoleccion seleccionadas deben contener al menos una fotografia de evidencia");
                    return;
                }*/


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("");
                builder.setMessage("¿El cliente es registro generador?");
                builder.setCancelable(false);
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    manifiestoCliente = new DialogManifiestoCliente(getActivity(),idAppManifiesto,tabManifiestoGeneral.getTipoPaquete());
                    manifiestoCliente.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    manifiestoCliente.setCancelable(false);
                    manifiestoCliente.show();

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setNavegate(VistaPreliminarFragment.newInstance(
                                idAppManifiesto,
                                tabManifiestoGeneral.getTipoPaquete()
                        ));
                    }
                });
                builder.show();



                break;
        }
    }

    @Override
    public void onCameraResult(int requestCode, int resultCode, Intent data) {
        if(tabManifiestoAdicional!=null && ((requestCode>=101 && requestCode<=104) ||(requestCode>=201 && requestCode<=204))){
            tabManifiestoAdicional.setMakePhoto(requestCode);
        }
    }

    private void selectImage(final Integer index) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Agregar Fotografia");

        builder.show();

    }
    public void vistaPrevia(Integer idAppManifiesto, Integer tipoPaquete){
        setNavegate(VistaPreliminarFragment.newInstance(idAppManifiesto,tipoPaquete));
    }
}
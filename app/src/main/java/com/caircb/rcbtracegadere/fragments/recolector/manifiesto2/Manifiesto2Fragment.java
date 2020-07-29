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
import com.caircb.rcbtracegadere.database.entity.RuteoRecoleccionEntity;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.dialogs.DialogBultos;
import com.caircb.rcbtracegadere.dialogs.DialogManifiestoCliente;
import com.caircb.rcbtracegadere.dialogs.DialogMensajes;
import com.caircb.rcbtracegadere.dialogs.DialogNotificacionDetalle;
import com.caircb.rcbtracegadere.fragments.recolector.HojaRutaAsignadaFragment;
import com.caircb.rcbtracegadere.fragments.recolector.HojaRutaBuscarFragment;
import com.caircb.rcbtracegadere.fragments.recolector.HomeTransportistaFragment;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnCameraListener;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.DtoRuteoRecoleccion;
import com.caircb.rcbtracegadere.models.RowItemHojaRutaCatalogo;
import com.caircb.rcbtracegadere.models.RowItemNoRecoleccion;
import com.caircb.rcbtracegadere.models.RowItemPaquete;
import com.caircb.rcbtracegadere.tasks.UserRegistrarRecoleccion;
import com.caircb.rcbtracegadere.tasks.UserRegistrarRuteoRecoleccion;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.List;


public class Manifiesto2Fragment extends MyFragment implements OnCameraListener,View.OnClickListener {

    private static final String ARG_PARAM1 = "manifiestoID";
    private static final String ARG_PANTALLA = "pantallaID";
    private static final String ARG_TIPORECOLECCION = "tipoRecoleccion";

    LinearLayout btnManifiestoCancel, btnManifiestoNext;

    TabManifiestoGeneral tabManifiestoGeneral;
    TabManifiestoDetalle tabManifiestoDetalle;
    TabManifiestoAdicional tabManifiestoAdicional;
    TabHost tabs;
    FloatingActionButton mensajes;
    Integer idAppManifiesto,estadoPantalla,tipoRecoleccion;

    DialogManifiestoCliente manifiestoCliente;
    DialogBuilder dialogBuilder;
    DialogBultos bultos;
    UserRegistrarRecoleccion userRegistrarRecoleccion;
    UserRegistrarRuteoRecoleccion userRegistrarRuteoRecoleccion;

    List<RowItemPaquete> listaPaquetes;

    public Manifiesto2Fragment() {
    }

    public static Manifiesto2Fragment newInstance(Integer manifiestoID,Integer estadoPantalla, Integer tipoRecoleccion) {
        Manifiesto2Fragment f= new Manifiesto2Fragment();
        Bundle b = new Bundle();
        b.putInt(ARG_PARAM1,manifiestoID);
        b.putInt(ARG_PANTALLA,estadoPantalla);
        b.putInt(ARG_TIPORECOLECCION,tipoRecoleccion);/// llega uno de la pantalla de buscar y 2 desde la de manifiestos
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            idAppManifiesto = getArguments().getInt(ARG_PARAM1);
            estadoPantalla = getArguments().getInt(ARG_PANTALLA);
            tipoRecoleccion=getArguments().getInt(ARG_TIPORECOLECCION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setView(inflater.inflate(R.layout.fragment_hoja_ruta2, container, false));
        MyApp.getDBO().parametroDao().saveOrUpdate("current_tab",""+0);
        init();
        initTab();
        return getView();
    }

    private void init(){

        btnManifiestoCancel = getView().findViewById(R.id.btnManifiestoCancel);
        btnManifiestoCancel.setOnClickListener(this);
        btnManifiestoNext = getView().findViewById(R.id.btnManifiestoNext);
        btnManifiestoNext.setOnClickListener(this);
        /*mensajes = getView().findViewById(R.id.fab);
        mensajes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogNotificacionDetalle dialogMensajes = new DialogNotificacionDetalle(getActivity(),idAppManifiesto);
                dialogMensajes.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogMensajes.setCancelable(false);
                dialogMensajes.show();
            }
        });*/
    }



    private void initTab(){

        inicializeTab();

        tabs=(TabHost)getView().findViewById(android.R.id.tabhost);
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
                if(s.equals("GENERAL")){
                    tabManifiestoDetalle.resetParameters();
                }
                else if(s.equals("DETALLE")){
                    tabManifiestoDetalle.resetParameters();
                }
                else if(s.equals("ADICIONALES")){
                    if(tabManifiestoDetalle.validaExisteCambioTotalBultos()){tabManifiestoAdicional.resetDataPaquetesPedientes();}
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
                tabManifiestoGeneral.getNumeroManifiesto(),1,tipoRecoleccion);

        tabManifiestoAdicional = new TabManifiestoAdicional(getActivity(),
                idAppManifiesto,
                tabManifiestoGeneral.getTipoPaquete(),
                tabManifiestoGeneral.getTiempoAudio(),
                1);
    }

    @Override
    public void onClick(View view) {
        tabs=(TabHost)getView().findViewById(android.R.id.tabhost);
        switch (view.getId()){
            case R.id.btnManifiestoCancel:
                //setNavegate(HojaRutaAsignadaFragment.newInstance());
                switch (estadoPantalla){
                    case 1:
                        int i= tabs.getCurrentTab();
                        if (i==0){
                            setNavegate(HojaRutaAsignadaFragment.newInstance());
                            break;
                        }
                        if (i==1){
                            tabs.setCurrentTab(tabs.getCurrentTab()-1);
                            break;
                        }
                        if (i==2){
                            tabs.setCurrentTab(tabs.getCurrentTab()-1);
                            break;
                        }

                    case 2:
                        //System.out.println(tabs.getCurrentTab());
                        int j= tabs.getCurrentTab();
                        if (j==0){
                            setNavegate(HojaRutaAsignadaFragment.newInstance());
                            break;
                        }
                        if (j==1){
                            tabs.setCurrentTab(tabs.getCurrentTab()-1);
                            break;
                        }
                        if (j==2){
                            tabs.setCurrentTab(tabs.getCurrentTab()-1);
                            break;
                        }
                        break;
                }
                break;
            case R.id.btnManifiestoNext:

                final String identifiacion = tabManifiestoGeneral.getIdentificacion();

                int i=tabs.getCurrentTab();
                if (i==0){
                    tabs.setCurrentTab(tabs.getCurrentTab()+1);
                }
                if (i==1){
                    tabs.setCurrentTab(tabs.getCurrentTab()+1);
                }
                if (i==2){
                           //tab genearl...
                boolean aplicaNoRecoleccion= tabManifiestoAdicional.validaExisteNovedadesNoRecoleccion();

                if(tabManifiestoGeneral.validaExisteFirmaTransportista() && !aplicaNoRecoleccion){
                    messageBox("Se requiere de la firma del transportista");
                    return;
                }
                if(tabManifiestoGeneral.validaExisteDatosResponsableEntrega() && !aplicaNoRecoleccion){
                    messageBox("Se require que ingrese los datos del técnico responsable de la entrega de los residuos recolectados");
                    return;
                }
                if(tabManifiestoGeneral.validaExisteFirmaTecnicoGenerador() && !aplicaNoRecoleccion){
                    messageBox("Se requiere de la firma del técnico generador");
                    return;
                }

                if(tabManifiestoGeneral.validarCorreos()&& !aplicaNoRecoleccion){
                    messageBox("Se requiere que ingrese un correo electrónico");
                    return;
                }

                if(MyApp.getDBO().manifiestoDetalleDao().countDetallesSinImprimirByIdManifiesto(idAppManifiesto)>0){
                    messageBox("Existen bultos sin imprimir, Favor verificarlos para continuar");
                    return;
                }
                //tab detalle...
                if(!tabManifiestoDetalle.validaExisteDetallesSeleccionados() && !aplicaNoRecoleccion) {
                    messageBox("Se requiere que registre al menos un item como recolectado");
                    return;
                }
               /* if(bultos.verificarTodosBultosImpresos()&& !aplicaNoRecoleccion){
                    messageBox("Hay bultos pendientes de imprimir");
                    return;
                }*/
                //tab de adicionales...
                if(tabManifiestoAdicional.validaNovedadesFrecuentesPendienteFotos()){
                    messageBox("Las novedades frecuentes seleccionadas deben contener al menos una fotografia de evidencia");
                    return;
                }

                //validacion de lista de paquetes... con sus pendientes..
                listaPaquetes = tabManifiestoAdicional.validaDataListaPaquetes();
                if(listaPaquetes !=null && listaPaquetes.size()>0){
                    int pendientes=0;
                    StringBuilder sb = new StringBuilder();
                    for(RowItemPaquete row:listaPaquetes){
                        pendientes += row.getPendiente();
                        if(row.getTipo()==1) sb.append(""+row.getPendiente()+" infeccioso").append(System.getProperty("line.separator"));
                        if(row.getTipo()==2) sb.append(""+row.getPendiente()+" cortopunzante").append(System.getProperty("line.separator"));
                    }

                    if(pendientes==0){
                        sb = null;
                        dialogBuilder = new DialogBuilder(getActivity());
                        dialogBuilder.setMessage("Se entregaron todos los insumos al cliente?");
                        dialogBuilder.setCancelable(false);
                        dialogBuilder.setTitle("CONFIRMACIÓN");
                        dialogBuilder.setPositiveButton("SI", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogBuilder.dismiss();
                                continuarToVistaPreliminar(identifiacion);
                            }
                        });
                        dialogBuilder.setNegativeButton("NO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogBuilder.dismiss();
                            }
                        });
                        dialogBuilder.show();

                    }else{

                        dialogBuilder = new DialogBuilder(getActivity());
                        dialogBuilder.setMessage("Confirma que quedan pendientes?"+System.getProperty("line.separator")+sb.toString());
                        dialogBuilder.setCancelable(false);
                        dialogBuilder.setTitle("INFO");
                        dialogBuilder.setPositiveButton("SI", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogBuilder.dismiss();
                                continuarToVistaPreliminar(identifiacion);
                            }
                        });
                        dialogBuilder.setNegativeButton("NO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogBuilder.dismiss();
                            }
                        });
                        dialogBuilder.show();

                    }
                }else {
                    continuarToVistaPreliminar(identifiacion);
                }


               /* if(tabManifiestoAdicional.validaNovedadNoRecoleccionPendicenteFotos()){
                    messageBox("Las novedades de no recoleccion seleccionadas deben contener al menos una fotografia de evidencia");
                    return;
                }*/




                }
                System.out.println(tabs.getCurrentTab());
                break;
        }
    }

    private void continuarToVistaPreliminar(final String identifiacion){
        dialogBuilder = new DialogBuilder(getActivity());
        dialogBuilder.setMessage("¿El Cliente Genera Propio Manifiesto?");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("SI", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
                manifiestoCliente = new DialogManifiestoCliente(getActivity(),idAppManifiesto,tabManifiestoGeneral.getTipoPaquete(),identifiacion,tipoRecoleccion);
                manifiestoCliente.requestWindowFeature(Window.FEATURE_NO_TITLE);
                manifiestoCliente.setCancelable(false);
                manifiestoCliente.setmOnRegisterListener(new DialogManifiestoCliente.onRegisterListenner() {
                    @Override
                    public void onSucessfull() {
                        dialogBuilder.dismiss();
                        manifiestoCliente.dismiss();

                        if (tipoRecoleccion==1){
                            setNavegate(VistaPreliminarFragment.newInstance(idAppManifiesto, tabManifiestoGeneral.getTipoPaquete(),identifiacion));
                        }else {
                            final DialogBuilder dialogBuilder2=new DialogBuilder(getActivity());
                            dialogBuilder2.setMessage("¿Está seguro que desea guardar?");
                            dialogBuilder2.setCancelable(false);
                            dialogBuilder2.setTitle("CONFIRMACIÓN");
                            dialogBuilder2.setPositiveButton("SI", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    setNavegate(VistaPreliminarFragment.newInstance(idAppManifiesto, tabManifiestoGeneral.getTipoPaquete(),identifiacion));
                                    dialogBuilder2.dismiss();
                                    //registarDatos();
                                }
                            });
                            dialogBuilder2.setNegativeButton("NO", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogBuilder2.dismiss();
                                }
                            });
                            dialogBuilder2.show();
                        }

                    }
                });
                manifiestoCliente.show();
            }
        });
        dialogBuilder.setNegativeButton("NO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
                MyApp.getDBO().manifiestoDao().updateManifiestoCliente(idAppManifiesto,"");
                if (tipoRecoleccion==1){
                    setNavegate(VistaPreliminarFragment.newInstance(
                            idAppManifiesto,
                            tabManifiestoGeneral.getTipoPaquete(),
                            identifiacion
                    ));
                }else {
                    final DialogBuilder dialogBuilder2=new DialogBuilder(getActivity());
                    dialogBuilder2.setMessage("¿Está seguro que desea guardar?");
                    dialogBuilder2.setCancelable(false);
                    dialogBuilder2.setTitle("CONFIRMACIÓN");
                    dialogBuilder2.setPositiveButton("SI", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder2.dismiss();
                            setNavegate(VistaPreliminarFragment.newInstance(idAppManifiesto, tabManifiestoGeneral.getTipoPaquete(),identifiacion));
                            //registarDatos();
                        }
                    });
                    dialogBuilder2.setNegativeButton("NO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder2.dismiss();
                        }
                    });
                    dialogBuilder2.show();
                }

            }
        });
        dialogBuilder.show();
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

    private void registarDatos(){
        MyApp.getDBO().manifiestoDao().updateManifiestoFechaRecoleccion(idAppManifiesto,new Date());
        userRegistrarRecoleccion = new UserRegistrarRecoleccion(getActivity(),idAppManifiesto,getLocation());
        userRegistrarRecoleccion.setOnRegisterListener(new UserRegistrarRecoleccion.OnRegisterListener() {
            @Override
            public void onSuccessful(final Date fechaRecol) {
                //setNavegate(HojaRutaAsignadaFragment.newInstance());

                //Registro el ruteo en estado en 1
                Integer _id = MyApp.getDBO().ruteoRecoleccion().searchRegistroLlegada(idAppManifiesto);
                RuteoRecoleccionEntity dtoSendServicio = MyApp.getDBO().ruteoRecoleccion().dtoSendServicio(_id, idAppManifiesto);

                userRegistrarRuteoRecoleccion = new UserRegistrarRuteoRecoleccion(getActivity(), dtoSendServicio);
                userRegistrarRuteoRecoleccion.setOnRegisterRuteoRecollecionListenner(new UserRegistrarRuteoRecoleccion.OnRegisterRuteroRecoleecionListener() {
                    @Override
                    public void onSuccessful() {

                        //List<RuteoRecoleccionEntity> enty = MyApp.getDBO().ruteoRecoleccion().searchRuteoRecoleccion(); //////////
                        Integer _id = MyApp.getDBO().ruteoRecoleccion().searchRegistroLlegada(idAppManifiesto);
                        if(_id !=null && _id >=0){
                            MyApp.getDBO().ruteoRecoleccion().updateEstadoByPuntoLLegada(_id, idAppManifiesto);
                        }
                        //List<RuteoRecoleccionEntity> enty2 = MyApp.getDBO().ruteoRecoleccion().searchRuteoRecoleccion(); ///////////

                        if(MyApp.getDBO().manifiestoDao().contarHojaRutaAsignadas() >0 ){

                            dialogBuilder = new DialogBuilder(getActivity());
                            dialogBuilder.setMessage("¿Desea iniciar traslado al próximo punto de recolección ?");
                            dialogBuilder.setCancelable(false);
                            dialogBuilder.setPositiveButton("SI", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogBuilder.dismiss();
                                    //Guardo la nueva fecha de inicio y puntoParitda;
                                    MyApp.getDBO().ruteoRecoleccion().saverOrUpdate(new DtoRuteoRecoleccion(MySession.getIdSubRuta(), fechaRecol,idAppManifiesto,null,null,false));
                                    //List<RuteoRecoleccionEntity> enty3 = MyApp.getDBO().ruteoRecoleccion().searchRuteoRecoleccion(); //////////
                                    setNavegate(HojaRutaAsignadaFragment.newInstance());
                                }
                            });
                            dialogBuilder.setNegativeButton("NO", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogBuilder.dismiss();
                                    //Update parametro en NO para levantar el modal para verificar si empieza con el trazlado
                                    MyApp.getDBO().parametroDao().saveOrUpdate("ruteoRecoleccion", "NO");
                                    MyApp.getDBO().ruteoRecoleccion().saverOrUpdate(new DtoRuteoRecoleccion(MySession.getIdSubRuta(), fechaRecol,idAppManifiesto,null,null,false));
                                    //setNavegate(HojaRutaAsignadaFragment.newInstance());
                                    //Se envia al home ya que el usuario No desea recolectar
                                    setNavegate(HomeTransportistaFragment.create());

                                }
                            });
                            dialogBuilder.show();
                        }else{
                            MyApp.getDBO().parametroDao().saveOrUpdate("ruteoRecoleccion", "NO");
                            MyApp.getDBO().ruteoRecoleccion().saverOrUpdate(new DtoRuteoRecoleccion(MySession.getIdSubRuta(), fechaRecol,idAppManifiesto,null,null,false));
                            setNavegate(HomeTransportistaFragment.create());
                        }
                    }

                    @Override
                    public void onFail() {
                        setNavegate(HojaRutaAsignadaFragment.newInstance());
                    }
                });
                userRegistrarRuteoRecoleccion.execute();
            }

            @Override
            public void onFail() {
                setNavegate(HojaRutaAsignadaFragment.newInstance());
                messageBox("No se encontro impresora, Datos Guardados");
            }
        });
        userRegistrarRecoleccion.execute();
    }

}
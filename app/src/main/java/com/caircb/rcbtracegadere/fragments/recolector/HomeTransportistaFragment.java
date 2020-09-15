package com.caircb.rcbtracegadere.fragments.recolector;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.caircb.rcbtracegadere.CierreLoteActivity;
import com.caircb.rcbtracegadere.MainActivity;
import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.entity.ManifiestoEntity;
import com.caircb.rcbtracegadere.database.entity.ParametroEntity;
import com.caircb.rcbtracegadere.database.entity.RutaInicioFinEntity;
import com.caircb.rcbtracegadere.database.entity.RuteoRecoleccionEntity;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.dialogs.DialogFinRuta;
import com.caircb.rcbtracegadere.dialogs.DialogInicioRuta;
import com.caircb.rcbtracegadere.fragments.Hoteles.HomeHotelFragment;
import com.caircb.rcbtracegadere.dialogs.DialogQrLoteTransportista;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnBarcodeListener;
import com.caircb.rcbtracegadere.generics.OnHome;
import com.caircb.rcbtracegadere.helpers.MyConstant;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.DtoRuteoRecoleccion;
import com.caircb.rcbtracegadere.tasks.UserConsultaCodigoQrTask;
import com.caircb.rcbtracegadere.tasks.UserConsultaCodigoQrValidadorTask;
import com.caircb.rcbtracegadere.models.ItemManifiesto;
import com.caircb.rcbtracegadere.tasks.UserConsultarCatalogosTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarHojaRutaTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarInicioRutaTask;
import com.caircb.rcbtracegadere.tasks.UserNotificacionTask;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeTransportistaFragment extends MyFragment implements OnHome, OnBarcodeListener {
    private static String datoPickUp;
    ImageButton btnSincManifiestos,btnListaAsignadaTransportista,btnMenu, btnInicioRuta, btnFinRuta;
    UserConsultarHojaRutaTask consultarHojaRutaTask;
    TextView lblListaManifiestoAsignado, lblpickUpTransportista, lblDropOffTransportista;
    ImageView btnPickUpTransportista, btnDropOffTransportista, btnScanQr;
    DialogInicioRuta dialogInicioRuta;
    DialogQrLoteTransportista dialogQrLoteTransportista;
    DialogFinRuta dialogFinRuta;
    LinearLayout lnlIniciaRuta,lnlFinRuta,txtQr,sectionQrLote;
    RutaInicioFinEntity rut;
    UserConsultarInicioRutaTask verificarInicioRutaTask;
    Integer idSubRuta, flag=0;
    UserConsultarCatalogosTask consultarCatalogosTask;
    Integer cont=0;


    //public Context mContext;

    Boolean inicioRuta = false;

    int IdTransporteRecolector;
    Integer inicioFinRuta;

    ImageButton regionBuscar;
    TextView txtBuscar, txtSincronizar, txtManifiestos;
    DialogBuilder dialogBuilder;


    UserConsultarHojaRutaTask.TaskListener listenerHojaRuta = new UserConsultarHojaRutaTask.TaskListener() {
        @Override
        public void onSuccessful() {
            loadCantidadManifiestoAsignado();
            loadCantidadManifiestoProcesado();
        }
    };


    public static HomeTransportistaFragment create() {
        return new HomeTransportistaFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setView(inflater.inflate(R.layout.fragment_home_transportista, container, false));

        initBuscador();
        init();


        return getView();

    }

    private void initBuscador() {
        regionBuscar = (ImageButton) getView().findViewById(R.id.regionBuscar);
        regionBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNavegate(HojaRutaBuscarFragment.newInstance());
                //setNavegate(BuscarFragment.create());
                //setNavegate(HomeRecepcionFragment.create());
            }
        });
    }

    private void init() {
        consultarInicioFinRuta();
        //MyApp.getDBO().parametroDao().saveOrUpdate("ruteoRecoleccion", "NO");
        lblListaManifiestoAsignado = getView().findViewById(R.id.lblListaManifiestoAsignado);
        btnSincManifiestos = getView().findViewById(R.id.btnSincManifiestos);
        btnListaAsignadaTransportista = getView().findViewById(R.id.btnListaAsignadaTransportista);
        btnMenu = getView().findViewById(R.id.btnMenu);
        btnPickUpTransportista = getView().findViewById(R.id.btnPickUpTransportista);
        lblpickUpTransportista = getView().findViewById(R.id.lblpickUpTransportista);
        btnDropOffTransportista = getView().findViewById(R.id.btnDropOffTransportista);
        lblDropOffTransportista = getView().findViewById(R.id.lblDropOffTransportista);
        btnInicioRuta = getView().findViewById(R.id.btnInciaRuta);
        btnFinRuta = getView().findViewById(R.id.btnFinRuta);
        lnlIniciaRuta = getView().findViewById(R.id.LnlIniciaRuta);
        lnlFinRuta = getView().findViewById(R.id.LnlFinRuta);
        btnScanQr = getView().findViewById(R.id.btnScanQr);
        txtQr = getView().findViewById(R.id.txtQr);

        txtBuscar = getView().findViewById(R.id.txtBuscar);
        txtSincronizar = getView().findViewById(R.id.txtSincronizar);
        txtManifiestos = getView().findViewById(R.id.txtManifiestos);

        sectionQrLote = getView().findViewById(R.id.sectionQrLote);
        sectionQrLote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogQrLoteTransportista = new DialogQrLoteTransportista(getActivity());
                dialogQrLoteTransportista.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogQrLoteTransportista.setCancelable(false);
                UserConsultaCodigoQrTask consultaCodigoQrTask = new UserConsultaCodigoQrTask(getActivity(), dialogQrLoteTransportista);
                consultaCodigoQrTask.execute();
            }
        });

        //txtinicioRuta = (TextView)getView().findViewById(R.id.txtIniciarRuta);
        //txtFinRuta = (TextView)getView().findViewById(R.id.txtFinRuta);

        //btnInicioRuta = getView().findViewById(R.id.btnInicioRuta);
        //btnFinRuta = getView().findViewById(R.id.btnFinRuta);

        //btnCalculadora = (ImageButton)getView().findViewById(R.id.btnCalculadora);

        int x = MySession.getIdUsuario();
        rut = MyApp.getDBO().rutaInicioFinDao().fechConsultaInicioFinRutasE(MySession.getIdUsuario());
        System.out.println(rut);
        if (rut != null) {
            inicioFinRuta = rut.getEstado();
            IdTransporteRecolector = MySession.getIdUsuario();

            switch (inicioFinRuta) {
                case 1:
                    inicioRuta = true;
                    desbloque_botones();
                    MySession.setIdSubruta(rut.getIdSubRuta());
                    lnlIniciaRuta.setVisibility(View.GONE);
                    lnlFinRuta.setVisibility(View.VISIBLE);
                    break;

                case 2:
                    inicioRuta = false;
                    bloqueo_botones();
                    btnScanQr.setEnabled(false);
                    btnScanQr.setAlpha(0.3f);
                    lnlIniciaRuta.setVisibility(View.VISIBLE);
                    lnlFinRuta.setVisibility(View.GONE);
                    break;
            }

        } else {
            bloqueo_botones();
            btnScanQr.setEnabled(false);
            btnScanQr.setAlpha(0.3f);
        }
        




  /*      if (inicioFinRuta=="0"){
            inicioRuta = true;
            desbloque_botones();
            btnInicioRuta.setEnabled(false);
        }else{
                inicioRuta = false;
                bloqueo_botones();
                btnInicioRuta.setEnabled(true);

        }*/

        // btnInicioRuta.setEnabled(!getMain().getInicioSesion());


        //if (inicioSesion || sesion =="0") {

        //     bloqueo_botones();
        //}

        //------------------------------------------------------------------------------------------
        // EVENTOS....
        //------------------------------------------------------------------------------------------
        /*
        btnCalculadora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBultos = new DialogBultos(getActivity(),listenerInput);
                dialogBultos.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogBultos.setCancelable(false);
                dialogBultos.show();
            }
        });
        */


        btnSincManifiestos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Integer> listaCatalogos = new ArrayList<>();
                listaCatalogos.add(2);

                consultarHojaRutaTask = new UserConsultarHojaRutaTask(getActivity(), listenerHojaRuta);
                try {
                    consultarHojaRutaTask.execute();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                consultarCatalogosTask = new UserConsultarCatalogosTask(getActivity(), listaCatalogos);
                consultarCatalogosTask.execute();
            }
        });

        btnListaAsignadaTransportista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Valido si el parametro esta en NO si es verdadero presento el modal
                rut = MyApp.getDBO().rutaInicioFinDao().fechConsultaInicioFinRutasE(MySession.getIdUsuario());
                ParametroEntity entity = MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_ruta");
                RutaInicioFinEntity rut = MyApp.getDBO().rutaInicioFinDao().fechConsultaInicioFinRutasE(MySession.getIdUsuario());
                String valor = entity == null ? String.valueOf(rut.getIdSubRuta()) : entity.getValor();
                //Integer idRuta = Integer.parseInt(valor.equals("null") ? "-1":valor);

                UserConsultaCodigoQrValidadorTask consultaCodigoQrValidadorTask = new UserConsultaCodigoQrValidadorTask(getActivity());
                consultaCodigoQrValidadorTask.setOnCodigoQrListener(new UserConsultaCodigoQrValidadorTask.OnCodigoQrListener() {
                    @Override
                    public void onSuccessful() {
                        final DialogBuilder dialogBuilder;
                        dialogBuilder = new DialogBuilder(getActivity());
                        dialogBuilder.setMessage("Lote cerrado, no ha descargado en el destino!!");
                        dialogBuilder.setCancelable(false);
                        dialogBuilder.setPositiveButton("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogBuilder.dismiss();
                            }
                        });
                        dialogBuilder.show();
                    }
                    @Override
                    public void onFailure() {
                        // idSubRuta = Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_ruta").getValor());
                        if (MyApp.getDBO().manifiestoDao().contarHojaRutaAsignadasPara(MySession.getIdUsuario()) > 0) {
                            List<RuteoRecoleccionEntity> enty = MyApp.getDBO().ruteoRecoleccion().searchRuteoRecoleccion();
                            if (MyApp.getDBO().parametroDao().fecthParametroValorByNombre("ruteoRecoleccion") != null && MyApp.getDBO().parametroDao().fecthParametroValorByNombre("ruteoRecoleccion").equals("NO")) {

                                dialogBuilder = new DialogBuilder(getActivity());
                                dialogBuilder.setCancelable(false);
                                dialogBuilder.setMessage("¿Iniciar traslado al próximo punto de recolección?");
                                dialogBuilder.setPositiveButton("SI", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogBuilder.dismiss();
                                        MyApp.getDBO().parametroDao().saveOrUpdate("ruteoRecoleccion", "SI");
                                        setNavegate(HojaRutaAsignadaFragment.newInstance());
                                    }
                                });
                                dialogBuilder.setNegativeButton("NO", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogBuilder.dismiss();
                                    }
                                });
                                dialogBuilder.show();

                            } else {
                                setNavegate(HojaRutaAsignadaFragment.newInstance());
                            }
                        } else {
                            messageBox("No dispone de manifiestos para recolectar.!");
                        }
                    }
                });
                consultaCodigoQrValidadorTask.execute();


            }
        });

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).openMenuOpcion();
                }
            }
        });

        btnPickUpTransportista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNavegate(HojaRutaProcesadaFragment.newInstance());
            }
        });

        btnInicioRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserConsultaCodigoQrValidadorTask consultaCodigoQrValidadorTask = new UserConsultaCodigoQrValidadorTask(getActivity());
                consultaCodigoQrValidadorTask.setOnCodigoQrListener(new UserConsultaCodigoQrValidadorTask.OnCodigoQrListener() {
                    @Override
                    public void onSuccessful() {
                        final DialogBuilder dialogBuilder;
                        dialogBuilder = new DialogBuilder(getActivity());
                        dialogBuilder.setMessage("Lote cerrado, no ha descargado en el destino!!");
                        dialogBuilder.setCancelable(false);
                        dialogBuilder.setPositiveButton("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogBuilder.dismiss();
                            }
                        });
                        dialogBuilder.show();
                    }
                    @Override
                    public void onFailure() {
                        dialogInicioRuta = new DialogInicioRuta(getActivity());
                        dialogInicioRuta.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialogInicioRuta.setCancelable(false);
                        dialogInicioRuta.setOnSuccessListener(new DialogInicioRuta.OnSuccessListener() {
                            @Override
                            public void isSuccessful() {
                                dialogInicioRuta.dismiss();
                                dialogInicioRuta = null;
                            }
                        });
                        dialogInicioRuta.show();
                    }
                });
                consultaCodigoQrValidadorTask.execute();

            }
            // openDialog_InicioApp(getMain().getInicioSesion());
                /*
                btnInicioRuta.setVisibility(View.GONE);
                txtinicioRuta.setVisibility(View.GONE);
                btnFinRuta.setVisibility(View.VISIBLE);
                txtFinRuta.setVisibility(View.VISIBLE);*/

        });

        btnFinRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int conteoManifiestos;
                conteoManifiestos = MyApp.getDBO().manifiestoDao().contarHojaRutaAsignadasPara(MySession.getIdUsuario());
                //conteoManifiestos = MyApp.getDBO().manifiestoDao().contarHojaRutaAsignadas();
                if (conteoManifiestos > 0) {
                    messageBox("Existen manifiestos pendientes por recolectar");
                } else {
                    openDialog_Fin_App();
                }
            }
        });

        btnScanQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(flag.equals(1)){
                    desbloque_botones();
                    txtQr.setVisibility(View.INVISIBLE);
                    btnFinRuta.setEnabled(true);
                    btnScanQr.setAlpha(1.0f);
                    flag = 0;
                }else{
                    bloqueo_botones();
                    txtQr.setVisibility(View.VISIBLE);
                    btnFinRuta.setEnabled(false);
                    btnScanQr.setAlpha(0.3f);
                    flag=1;
                }

            }
        });
        loadCantidadManifiestoAsignado();
        loadCantidadManifiestoProcesado();
    }



    private void loadCantidadManifiestoAsignado() {
        //dbHelper.open();
        lblListaManifiestoAsignado.setText("" + MyApp.getDBO().manifiestoDao().contarHojaRutaAsignadasPara(MySession.getIdUsuario()));
        //lblListaManifiestoAsignado.setText(""+ MyApp.getDBO().manifiestoDao().contarHojaRutaAsignadas());
        //dbHelper.close();
    }

    public void loadCantidadManifiestoProcesado() {
        //dbHelper.open();
        //lblpickUpTransportista.setText(""+ MyApp.getDBO().manifiestoDao().contarHojaRutaProcesadaPara(idSubRuta == null ? 0:idSubRuta,MySession.getIdUsuario()));
        //lblpickUpTransportista.setText(""+ MyApp.getDBO().manifiestoDao().contarHojaRutaAsignadasP(MySession.getIdUsuario()));
        //Integer idSubruta = Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_ruta").getValor());
        Integer idSubruta = MySession.getIdSubRuta();
        MyApp.getDBO().parametroDao().saveOrUpdate("current_ruta",idSubruta+"");
        lblpickUpTransportista.setText("" + MyApp.getDBO().manifiestoDao().contarHojaRutaAsignadasByIdConductorAndRuta(MySession.getIdUsuario(), idSubruta));
        //dbHelper.close();
    }


    private void openDialog_Fin_App() {
        dialogFinRuta = new DialogFinRuta(getActivity());
        dialogFinRuta.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogFinRuta.setCancelable(false);
        dialogFinRuta.setmOnFinLotePadreListener(new DialogFinRuta.OnFinLoteHotel() {
            @Override
            public void onSuccessful() {
                setNavegate(HomeHotelFragment.create());
            }
        });
        dialogFinRuta.show();
    }


    private void bloqueo_botones() {

        regionBuscar.setEnabled(false);
        btnSincManifiestos.setEnabled(false);
        btnListaAsignadaTransportista.setEnabled(false);
        btnPickUpTransportista.setEnabled(false);
        btnDropOffTransportista.setEnabled(false);
        lblpickUpTransportista.setText("0");
        lblListaManifiestoAsignado.setText("0");
        //btnFinRuta.setEnabled(false);

        //regionBuscar.setColorFilter(Color.rgb(115, 124, 119 ));
        regionBuscar.setColorFilter(Color.rgb(Integer.valueOf(getString(R.string.btnDisabled1)), Integer.valueOf(getString(R.string.btnDisabled2)), Integer.valueOf(getString(R.string.btnDisabled3))));
        btnSincManifiestos.setColorFilter(Color.rgb(Integer.valueOf(getString(R.string.btnDisabled1)), Integer.valueOf(getString(R.string.btnDisabled2)), Integer.valueOf(getString(R.string.btnDisabled3))));
        btnListaAsignadaTransportista.setColorFilter(Color.rgb(Integer.valueOf(getString(R.string.btnDisabled1)), Integer.valueOf(getString(R.string.btnDisabled2)), Integer.valueOf(getString(R.string.btnDisabled3))));

        txtBuscar.setTextColor(Color.rgb(Integer.valueOf(getString(R.string.btnDisabled1)), Integer.valueOf(getString(R.string.btnDisabled2)), Integer.valueOf(getString(R.string.btnDisabled3))));
        txtManifiestos.setTextColor(Color.rgb(Integer.valueOf(getString(R.string.btnDisabled1)), Integer.valueOf(getString(R.string.btnDisabled2)), Integer.valueOf(getString(R.string.btnDisabled3))));
        txtSincronizar.setTextColor(Color.rgb(Integer.valueOf(getString(R.string.btnDisabled1)), Integer.valueOf(getString(R.string.btnDisabled2)), Integer.valueOf(getString(R.string.btnDisabled3))));

        btnPickUpTransportista.setAlpha(0.3f);
        btnDropOffTransportista.setAlpha(0.3f);
    }


    public void desbloque_botones() {

        regionBuscar.setEnabled(true);
        btnSincManifiestos.setEnabled(true);
        btnSincManifiestos.setEnabled(true);
        btnListaAsignadaTransportista.setEnabled(true);
        btnPickUpTransportista.setEnabled(true);
        btnDropOffTransportista.setEnabled(true);
        //btnFinRuta.setEnabled(true);
        loadCantidadManifiestoAsignado();
        loadCantidadManifiestoProcesado();


        regionBuscar.setColorFilter(Color.TRANSPARENT);
        btnSincManifiestos.setColorFilter(Color.TRANSPARENT);
        btnListaAsignadaTransportista.setColorFilter(Color.TRANSPARENT);

        txtBuscar.setTextColor(Color.WHITE);
        txtManifiestos.setTextColor(Color.WHITE);
        txtSincronizar.setTextColor(Color.WHITE);

        btnPickUpTransportista.setAlpha(1.0f);
        btnDropOffTransportista.setAlpha(1.0f);
    }

    private void consultarInicioFinRuta() {
        verificarInicioRutaTask = new UserConsultarInicioRutaTask(getActivity());
        verificarInicioRutaTask.setOnRegisterListener(new UserConsultarInicioRutaTask.OnRegisterListener() {
            @Override
            public void onSuccessful() {
                // messageBox("Ha iniciado previamente sesion");
                setNavegate(HomeTransportistaFragment.create());
                //  loadCantidadManifiestoAsignado();
                // loadCantidadManifiestoProcesado();
            }
        });
        verificarInicioRutaTask.execute();
    }

    public void bloqueoBotonManifiesto() {
        btnListaAsignadaTransportista.setEnabled(false);
        btnListaAsignadaTransportista.setColorFilter(Color.rgb(Integer.valueOf(getString(R.string.btnDisabled1)), Integer.valueOf(getString(R.string.btnDisabled2)), Integer.valueOf(getString(R.string.btnDisabled3))));
        txtManifiestos.setTextColor(Color.rgb(Integer.valueOf(getString(R.string.btnDisabled1)), Integer.valueOf(getString(R.string.btnDisabled2)), Integer.valueOf(getString(R.string.btnDisabled3))));
    }

    public void desbloqueoBotonManifiesto() {
        btnListaAsignadaTransportista.setEnabled(true);
        btnListaAsignadaTransportista.setColorFilter(Color.TRANSPARENT);
        txtManifiestos.setTextColor(Color.WHITE);
    }



    @Override
    public void reciveData(String data) {
        if(dialogInicioRuta!=null && dialogInicioRuta.isShowing()){
            dialogInicioRuta.setScanCode(data);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println(MyApp.getDBO().parametroDao().fecthParametroValorByNombre("flag_refresh_home")+"");
        String flagHomeCierreLote=MyApp.getDBO().parametroDao().fecthParametroValorByNombre("flag_refresh_home")==null?"false":MyApp.getDBO().parametroDao().fecthParametroValorByNombre("flag_refresh_home");
        if (flagHomeCierreLote.equals("true")){
            List<Integer> listaCatalogos = new ArrayList<>();
            listaCatalogos.add(2);

            consultarHojaRutaTask = new UserConsultarHojaRutaTask(getActivity(), listenerHojaRuta);
            try {
                consultarHojaRutaTask.execute();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            consultarCatalogosTask = new UserConsultarCatalogosTask(getActivity(), listaCatalogos);
            consultarCatalogosTask.execute();
            MyApp.getDBO().parametroDao().saveOrUpdate("flag_refresh_home","false");
        }
    }
}
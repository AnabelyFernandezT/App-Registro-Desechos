package com.caircb.rcbtracegadere.fragments.planta;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.caircb.rcbtracegadere.MainActivity;
import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.entity.CatalogoEntity;
import com.caircb.rcbtracegadere.database.entity.CodigoQrTransportistaEntity;
import com.caircb.rcbtracegadere.database.entity.ConsultarFirmaUsuarioEntity;
import com.caircb.rcbtracegadere.database.entity.ParametroEntity;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.dialogs.DialogBultosPlanta;
import com.caircb.rcbtracegadere.dialogs.DialogInicioRuta;
import com.caircb.rcbtracegadere.dialogs.DialogPlacas;
import com.caircb.rcbtracegadere.fragments.Sede.HojaRutaAsignadaSedeFragment;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnBarcodeListener;
import com.caircb.rcbtracegadere.generics.OnCameraListener;
import com.caircb.rcbtracegadere.generics.OnHome;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.ItemManifiestoDetalleSede;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoPlanta;
import com.caircb.rcbtracegadere.tasks.UserConsultaCodigoQrTask;
import com.caircb.rcbtracegadere.tasks.UserConsultaFirmaUsuarioTask;
import com.caircb.rcbtracegadere.tasks.UserConsultaQrPlantaTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarHojaRutaPlacaTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarHojaRutaPlacaXNoTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarHojaRutaTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarManifiestosPendientesPesarTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarRecolectadosTask;
import com.google.zxing.client.android.BeepManager;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomePlantaFragment extends MyFragment implements OnCameraListener, OnHome, OnBarcodeListener {
    ImageButton btnSincManifiestosPlanta, btnListaAsignadaTransportista, btnMenu, btnInicioRuta, btnFinRuta;
    TextView lblListaManifiestoAsignadoPlanta;
    ImageView btnPickUpTransportista, btnDropOffTransportista;
    DialogPlacas dialogPlacas;
    UserConsultarManifiestosPendientesPesarTask userConsultarManifiestosPendientesPesarTask;
    TextView lblDropOffTransportista;
    public Context mContext;
    private Integer controlDropOff = 0;
    LinearLayout sectionQrLotePlanta;

    UserConsultarHojaRutaPlacaTask consultarHojaRutaTask;
    UserConsultarHojaRutaPlacaXNoTask consultarHojaRutaTaskXNO;
    DialogBuilder builder;
    String placa;

    UserConsultarHojaRutaPlacaTask.TaskListener listenerHojaRuta = new UserConsultarHojaRutaPlacaTask.TaskListener() {
        @Override
        public void onSuccessful() {
            Integer idVehiculoPara = Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_vehiculo").getValor());
            loadCantidadManifiestoAsignadoNO(idVehiculoPara);
        }
    };

    public static HomePlantaFragment create() {
        return new HomePlantaFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setView(inflater.inflate(R.layout.fragment_home_planta, container, false));
  /*      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mContext=getContext();
        }*/
        init();
        //cargarLbael();
        return getView();

    }
    private DialogBultosPlanta.onclickSedeListener mOnclickSedeListener;

    private void init() {

        lblListaManifiestoAsignadoPlanta = (TextView) getView().findViewById(R.id.lblListaManifiestoAsignadoPlanta);
        btnSincManifiestosPlanta = (ImageButton) getView().findViewById(R.id.btnSincManifiestosPlanta);
        btnListaAsignadaTransportista = (ImageButton) getView().findViewById(R.id.btnListaAsignadaTransportistaPlanta);
        btnMenu = (ImageButton) getView().findViewById(R.id.btnMenu);
        btnPickUpTransportista = (ImageView) getView().findViewById(R.id.btnPickUpTransportista);
        btnDropOffTransportista = (ImageView) getView().findViewById(R.id.btnDropOffTransportista);
        btnInicioRuta = getView().findViewById(R.id.btnInciaRuta);
        btnFinRuta = getView().findViewById(R.id.btnFinRuta);
        lblDropOffTransportista = getView().findViewById(R.id.lblDropOffTransportista);
        sectionQrLotePlanta = (LinearLayout) getView().findViewById(R.id.sectionQrLotePlanta);
        cargarLabelCantidad();
        cargarLbael();

        btnDropOffTransportista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlDropOff == 0) {
                    final DialogBuilder builder = new DialogBuilder(v.getContext());
                    builder.setMessage("No existen manifiestos para procesar");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            builder.dismiss();
                        }
                    });
                    builder.show();

                } else {
                    userConsultarManifiestosPendientesPesarTask = new UserConsultarManifiestosPendientesPesarTask(getActivity());
                    userConsultarManifiestosPendientesPesarTask.setmOnListasManifiestosPendientesistener(new UserConsultarManifiestosPendientesPesarTask.OnListasManifiestosPendientesistener() {
                        @Override
                        public void onSuccessful(List<DtoManifiestoPlanta> listaManifiestos) {
                            setNavegate(HojaRutaPlantaPendientesPesoFragment.newInstance());
                            lblDropOffTransportista.setText(String.valueOf(listaManifiestos.size()));
                        }
                    });
                    userConsultarManifiestosPendientesPesarTask.execute();
                }
            }
        });

        btnSincManifiestosPlanta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPlacas = new DialogPlacas(getActivity());
                dialogPlacas.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogPlacas.setCancelable(false);
                dialogPlacas.setmOnclickSedeListener(new DialogBultosPlanta.onclickSedeListener() {
                    @Override
                    public void onSucefull() {
                        cargarLabelCantidad();
                    }
                });
                dialogPlacas.show();
            }
        });


        btnListaAsignadaTransportista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParametroEntity parametro = MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_vehiculo");
                String valor = parametro == null ? "-1" : parametro.getValor();
                Integer idVehiculo = Integer.parseInt(valor.equals("null") ? "-1" : valor);

                //setNavegate(HojaRutaAsignadaPlantaFragment.newInstance());
                Integer banderaUno = MyApp.getDBO().manifiestoDao().contarHojaRutaProcesadaPlanta(idVehiculo);
                Integer banderaDos = MyApp.getDBO().manifiestoPlantaDao().contarHojaRutaProcesada(idVehiculo);
                String bandera = MyApp.getDBO().parametroDao().fecthParametroValor("vehiculo_planta" + idVehiculo);
                if (bandera != null) {
                    if (bandera.equals("1")) {
                        if (banderaDos > 0) {
                            setNavegate(HojaRutaAsignadaPlantaFragment.newInstance());
                        }
                    } else if (bandera.equals("2")) {
                        if (banderaUno > 0) {
                            setNavegate(HojaRutaAsignadaFragmentNO.newInstance());
                        }
                    }
                }

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


        sectionQrLotePlanta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   IntentIntegrator integrator = new IntentIntegrator(getActivity());
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("LECTURA CÓDIGO QR PLANTA");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(false);
                integrator.setOrientationLocked(false);
                integrator.initiateScan();*/
             //setNavegate(RecepcionLotePlantaFragment.newInstance());
            }
        });
        consultarFirmaAndDestinoEspecificoUsuario();
    }

    @Override
    public void reciveData(String data) {
        try {
            Toast.makeText(getActivity(),data,Toast.LENGTH_LONG).show();
            // CODIGO PARA LECTURA DISPOSITIVO HONEYWELL
            String codigoQr=data;
            String[] array= codigoQr.split("\\$");
            MyApp.getDBO().parametroDao().saveOrUpdate("current_idTransportistaRecolector", array[3]);//idTransportistaRecolector
            MyApp.getDBO().parametroDao().saveOrUpdate("current_destino_especifico",array[4]);//destino
            MyApp.getDBO().parametroDao().saveOrUpdate("current_vehiculo",""+array[5]);//idvehiculo
            MyApp.getDBO().parametroDao().saveOrUpdate("current_placa_transportista",""+array[6]);//Placa para consulta de información modulos
            MyApp.getDBO().parametroDao().saveOrUpdate("current_placa_Planta",""+array[6]);
            MyApp.getDBO().parametroDao().saveOrUpdate("current_idSubruta",""+array[7]);
            MyApp.getDBO().parametroDao().saveOrUpdate("current_estadoCodigoQr","1");
            placa=array[6];
            String destinoUsuario=MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_destino_usuario")==null?"":MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_destino_usuario");
            if (array[4].equals(destinoUsuario)){
                UserConsultaQrPlantaTask consultaQrPlantaTask = new UserConsultaQrPlantaTask(getActivity());
                consultaQrPlantaTask.setOnQrPlantaListener(new UserConsultaQrPlantaTask.OnQrPlantaListener() {
                    @Override
                    public void onSuccessful() {
                        setNavegate(RecepcionLotePlantaFragment.newInstance());
                    }
                });
                consultaQrPlantaTask.execute();
            }else {
                final DialogBuilder dialogBuilder = new DialogBuilder(getActivity());
                dialogBuilder.setCancelable(false);
                dialogBuilder.setMessage("Los manifiestos no pertenecen a este destino!!!");
                dialogBuilder.setPositiveButton("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                    }
                });
                dialogBuilder.show();
            }
        }
        catch (Exception e)
        {
            messageBox("El código escaneado no es de tipo Lote.");
        }
    }

    @Override
    public void onCameraResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result!=null){
            if (result.getContents()==null){
                Toast.makeText(getActivity(),"Escaneo Cancelado",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getActivity(), result.getContents(),Toast.LENGTH_LONG).show();
                String codigoQr=result.getContents();
                String[] array= codigoQr.split("\\$");
                MyApp.getDBO().parametroDao().saveOrUpdate("current_destino_especifico",""+array[4]);//destino
                MyApp.getDBO().parametroDao().saveOrUpdate("current_vehiculo",""+array[5]);//idvehiculo
                MyApp.getDBO().parametroDao().saveOrUpdate("current_placa_transportista",""+array[6]);//Placa para consulta de información modulos
                MyApp.getDBO().parametroDao().saveOrUpdate("current_placa_Planta",""+array[6]);
                MyApp.getDBO().parametroDao().saveOrUpdate("current_idSubruta",""+array[7]);
                MyApp.getDBO().parametroDao().saveOrUpdate("current_estadoCodigoQr","1");
                placa=array[6];
                if (array[4].equals(MySession.getDestinoEspecifico())){
                    menuSeleccionCargaManifiestos();
                }else {
                    final DialogBuilder dialogBuilder = new DialogBuilder(getActivity());
                    dialogBuilder.setCancelable(false);
                    dialogBuilder.setMessage("Los manifiestos no pertenecen a este destino!!!");
                    dialogBuilder.setPositiveButton("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismiss();
                        }
                    });
                    dialogBuilder.show();
                }
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void menuSeleccionCargaManifiestos(){
        ParametroEntity parametro = MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_vehiculo");
        String valor = parametro == null ? "-1" : parametro.getValor();
        Integer idVehiculo = Integer.parseInt(valor.equals("null") ? "-1":valor);
        String bandera = MyApp.getDBO().parametroDao().fecthParametroValor("vehiculo_planta"+idVehiculo)==null?"0":MyApp.getDBO().parametroDao().fecthParametroValor("vehiculo_planta"+idVehiculo);
        if(bandera.equals("1")){
            cargarXNOManifiesto();

            //dismiss();
        }else if(bandera.equals("2")){
            try {
                cargarManifiesto();

            } catch (ParseException e) {
                e.printStackTrace();
            }
            //dismiss();
        }else if(bandera.equals("0") || bandera.equals(null)){
            dialogoConfirmacion();
        }
    }

    private void cargarManifiesto() throws ParseException {
        consultarHojaRutaTask = new UserConsultarHojaRutaPlacaTask(getActivity(),listenerHojaRuta2);
        consultarHojaRutaTask.execute();
    }

    private void cargarXNOManifiesto(){
        consultarHojaRutaTaskXNO = new UserConsultarHojaRutaPlacaXNoTask(getActivity(),listenerHojaRutaNo);
        consultarHojaRutaTaskXNO.execute();
    }

    UserConsultarHojaRutaPlacaTask.TaskListener listenerHojaRuta2 = new UserConsultarHojaRutaPlacaTask.TaskListener() {
        @Override
        public void onSuccessful() {
            //loadCantidadManifiestoAsignado();
            if(mOnclickSedeListener!=null){
                mOnclickSedeListener.onSucefull();
            }
            final DialogBuilder dialogBuilder;
            dialogBuilder = new DialogBuilder(getActivity());
            dialogBuilder.setMessage("Manifiestos sincronizados!");
            dialogBuilder.setCancelable(false);
            dialogBuilder.setPositiveButton("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.dismiss();
                }
            });
            dialogBuilder.show();
        }
    };
    UserConsultarHojaRutaPlacaXNoTask.TaskListener listenerHojaRutaNo = new UserConsultarHojaRutaPlacaXNoTask.TaskListener() {
        @Override
        public void onSuccessful() {
            //loadCantidadManifiestoAsignado();
            if(mOnclickSedeListener!=null){
                mOnclickSedeListener.onSucefull();
            }
            final DialogBuilder dialogBuilder;
            dialogBuilder = new DialogBuilder(getActivity());
            dialogBuilder.setMessage("Manifiestos sincronizados!");
            dialogBuilder.setCancelable(false);
            dialogBuilder.setPositiveButton("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.dismiss();
                }
            });
            dialogBuilder.show();
        }
    };

    private void dialogoConfirmacion(){
        builder = new DialogBuilder(getActivity());
        builder.setMessage("¿Realizara revisión de pesajes por desecho?");
        builder.setCancelable(true);
        builder.setPositiveButton("SI", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cargarManifiesto();
                CatalogoEntity c = MyApp.getDBO().catalogoDao().fetchConsultarCatalogoId(placa,4);
                int idVehiculo = c!=null?c.getIdSistema():-1;
                MyApp.getDBO().parametroDao().saveOrUpdate("current_vehiculo",""+idVehiculo);
                MyApp.getDBO().parametroDao().saveOrUpdate("vehiculo_planta","");
                MyApp.getDBO().parametroDao().saveOrUpdate("current_placa_transportista",""+placa);
                MyApp.getDBO().parametroDao().saveOrUpdate("vehiculo_planta"+idVehiculo,""+1);
                cargarXNOManifiesto();
                //cargarLabelCantidad();
                builder.dismiss();
                //dismiss();
            }
        });
        builder.setNegativeButton("NO", new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                CatalogoEntity c = MyApp.getDBO().catalogoDao().fetchConsultarCatalogoId(placa,4);
                int idVehiculo = c!=null?c.getIdSistema():-1;
                MyApp.getDBO().parametroDao().saveOrUpdate("current_vehiculo",""+idVehiculo);
                MyApp.getDBO().parametroDao().saveOrUpdate("vehiculo_planta","");
                MyApp.getDBO().parametroDao().saveOrUpdate("current_placa_transportista",""+placa);
                MyApp.getDBO().parametroDao().saveOrUpdate("vehiculo_planta"+idVehiculo,""+2);
                try {
                    cargarManifiesto();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //cargarLabelCantidad();
                builder.dismiss();
                //dismiss();
            }
        });
        builder.show();
    }

    private void cargarLbael() {
        userConsultarManifiestosPendientesPesarTask = new UserConsultarManifiestosPendientesPesarTask(getActivity());
        userConsultarManifiestosPendientesPesarTask.setmOnListasManifiestosPendientesistener(new UserConsultarManifiestosPendientesPesarTask.OnListasManifiestosPendientesistener() {
            @Override
            public void onSuccessful(List<DtoManifiestoPlanta> listaManifiestos) {
                lblDropOffTransportista.setText(String.valueOf(listaManifiestos.size()));
                controlDropOff = listaManifiestos.size();
            }
        });
        userConsultarManifiestosPendientesPesarTask.execute();

    }

    private void cargarLabelCantidad() {
        ParametroEntity parametro = MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_vehiculo");
        String valor = parametro == null ? "-1" : parametro.getValor();
        Integer idVehiculo = Integer.parseInt(valor.equals("null") ? "-1" : valor);
        String bandera = MyApp.getDBO().parametroDao().fecthParametroValor("vehiculo_planta" + idVehiculo);
        if (bandera != null) {
            if (bandera.equals("1")) {
                loadCantidadManifiestoAsignado(idVehiculo);
            } else if (bandera.equals("2")) {
                loadCantidadManifiestoAsignadoNO(idVehiculo);
            }
        }
    }

    private void loadCantidadManifiestoAsignado(Integer idVehiculo) {
        lblListaManifiestoAsignadoPlanta.setText("" + MyApp.getDBO().manifiestoPlantaDao().contarHojaRutaProcesada(idVehiculo));
    }


    private void loadCantidadManifiestoAsignadoNO(Integer idVehiculo) {
        lblListaManifiestoAsignadoPlanta.setText("" + MyApp.getDBO().manifiestoDao().contarHojaRutaProcesadaPlanta(idVehiculo));
    }

    private void consultarFirmaAndDestinoEspecificoUsuario(){
        UserConsultaFirmaUsuarioTask consultaFirmaUsuarioTask = new UserConsultaFirmaUsuarioTask(getActivity(), MySession.getIdUsuario());
        consultaFirmaUsuarioTask.setOnFirmaListener(new UserConsultaFirmaUsuarioTask.OnFirmaListener() {
            @Override
            public void onSuccessful() {
                ConsultarFirmaUsuarioEntity consultarFirmaUsuarioEntity= MyApp.getDBO().consultarFirmaUsuarioDao().fetchFirmaUsuario2();
                String firmaUsuario=consultarFirmaUsuarioEntity==null?"":(consultarFirmaUsuarioEntity.getFirmaBase64()==null?"":consultarFirmaUsuarioEntity.getFirmaBase64());
                int idDestinoEspecifico=consultarFirmaUsuarioEntity==null?0:(consultarFirmaUsuarioEntity.getIdFinRutaCatalogo()==null?0:consultarFirmaUsuarioEntity.getIdFinRutaCatalogo());
                MyApp.getDBO().parametroDao().saveOrUpdate("current_firma_usuario",firmaUsuario);
                MyApp.getDBO().parametroDao().saveOrUpdate("current_destino_usuario",idDestinoEspecifico+"");
            }
        });
        consultaFirmaUsuarioTask.execute();

    }


}

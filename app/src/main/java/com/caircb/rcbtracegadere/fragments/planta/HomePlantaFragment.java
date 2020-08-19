package com.caircb.rcbtracegadere.fragments.planta;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
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
import com.caircb.rcbtracegadere.database.entity.ParametroEntity;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.dialogs.DialogBultosPlanta;
import com.caircb.rcbtracegadere.dialogs.DialogInicioRuta;
import com.caircb.rcbtracegadere.dialogs.DialogPlacas;
import com.caircb.rcbtracegadere.fragments.Sede.HojaRutaAsignadaSedeFragment;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnCameraListener;
import com.caircb.rcbtracegadere.generics.OnHome;
import com.caircb.rcbtracegadere.models.ItemManifiestoDetalleSede;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoPlanta;
import com.caircb.rcbtracegadere.tasks.UserConsultarHojaRutaPlacaTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarHojaRutaTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarManifiestosPendientesPesarTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarRecolectadosTask;
import com.google.zxing.client.android.BeepManager;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomePlantaFragment extends MyFragment implements OnCameraListener, OnHome {
    ImageButton btnSincManifiestosPlanta, btnListaAsignadaTransportista, btnMenu, btnInicioRuta, btnFinRuta;
    TextView lblListaManifiestoAsignadoPlanta;
    ImageView btnPickUpTransportista, btnDropOffTransportista;
    DialogPlacas dialogPlacas;
    UserConsultarManifiestosPendientesPesarTask userConsultarManifiestosPendientesPesarTask;
    TextView lblDropOffTransportista;
    public Context mContext;
    private Integer controlDropOff = 0;
    LinearLayout sectionQrLotePlanta;

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

        init();
        //cargarLbael();
        return getView();

    }


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
                IntentIntegrator integrator = new IntentIntegrator(getActivity());
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Lectura de Código QR");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(false);
                integrator.setOrientationLocked(false);
                integrator.initiateScan();
            }
        });
    }

    @Override
    public void onCameraResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result!=null){
            if (result.getContents()==null){
                Toast.makeText(getActivity(),"Escaneo Cancelado",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getActivity(), result.getContents(),Toast.LENGTH_LONG).show();
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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


}

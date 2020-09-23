
package com.caircb.rcbtracegadere.fragments.Sede;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.caircb.rcbtracegadere.MainActivity;
import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.entity.ConsultarFirmaUsuarioEntity;
import com.caircb.rcbtracegadere.database.entity.ParametroEntity;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.dialogs.DialogConfirmarCierreLote;
import com.caircb.rcbtracegadere.dialogs.DialogPlacaSede;
import com.caircb.rcbtracegadere.dialogs.DialogPlacaSedeRecolector;
import com.caircb.rcbtracegadere.fragments.planta.HojaRutaAsignadaPlantaFragment;
import com.caircb.rcbtracegadere.fragments.planta.RecepcionLotePlantaFragment;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnBarcodeListener;
import com.caircb.rcbtracegadere.generics.OnCameraListener;
import com.caircb.rcbtracegadere.generics.OnHome;
import com.caircb.rcbtracegadere.helpers.MyConstant;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoSede;
import com.caircb.rcbtracegadere.tasks.UserConsultaFirmaUsuarioTask;
import com.caircb.rcbtracegadere.tasks.UserConsultaLotes;
import com.caircb.rcbtracegadere.tasks.UserConsultaQrPlantaTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarHojaRutaTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarManifiestosSedeTask;
import com.caircb.rcbtracegadere.tasks.UserRegistarFinLoteTask;
import com.caircb.rcbtracegadere.tasks.UserRegistrarLoteInicioTask;
import com.caircb.rcbtracegadere.tasks.UserRegistrarPlanta;
import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.w3c.dom.ls.LSOutput;

import java.util.List;

public class HomeSedeFragment extends MyFragment implements OnCameraListener, OnHome, OnBarcodeListener {
    UserConsultaLotes consultarLotes;
    ImageButton btnSincManifiestos, btnListaAsignadaSede, btnMenu, btnInciaLote, btnFinRuta, btnFinLote;

    Integer inicioLote;
    Integer finLote;
    UserRegistarFinLoteTask registarFinLoteTask;
    TextView lblListaManifiestoAsignado;
    LinearLayout lnlIniciaLote, lnlFinLote;
    ImageButton regionBuscar;
    DialogPlacaSede dialogPlacas;
    DialogPlacaSedeRecolector dialogPlacasRecolector;
    TextView txtMovilizar, txtSincronizar, txtManifiesto;
    DialogConfirmarCierreLote dialogConfirmarCierreLote;
    UserRegistrarLoteInicioTask registrarLoteInicioTask;
    DialogBuilder builder;
    LinearLayout sectionQrLoteSede;
    UserConsultarManifiestosSedeTask consultarHojaRutaTask;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setView(inflater.inflate(R.layout.fragment_home_sede, container, false));
        init();
        initBuscador();
        datosManifiestosAsignados();
        return getView();
    }

    private void datosManifiestosAsignados() {
        //consultarPlacasInicioRutaDisponible = new UserConsultarManifiestosSedeTask(getActivity());
        //consultarPlacasInicioRutaDisponible.execute();
    }

    private void init() {
        regionBuscar = getView().findViewById(R.id.regionBuscar);
        lblListaManifiestoAsignado = getView().findViewById(R.id.lblListaManifiestoAsignado);
        btnSincManifiestos = getView().findViewById(R.id.btnSincManifiestos);
        btnListaAsignadaSede = getView().findViewById(R.id.btnListaAsignadaSede);
        btnMenu = getView().findViewById(R.id.btnMenu);
        lnlIniciaLote = getView().findViewById(R.id.LnlIniciaLote);
        btnInciaLote = getView().findViewById(R.id.btnInciaLote);
        lnlFinLote = getView().findViewById(R.id.LnlFinLote);
        btnFinLote = getView().findViewById(R.id.btnFinLote);
        sectionQrLoteSede = (LinearLayout) getView().findViewById(R.id.sectionQrLoteSede);

        txtSincronizar = getView().findViewById(R.id.txtSincronizar);
        txtManifiesto = getView().findViewById(R.id.txtManifiesto);
        txtMovilizar = getView().findViewById(R.id.txtMovilizar);

        btnSincManifiestos.setColorFilter(Color.rgb(Integer.valueOf(getString(R.string.btnDisabled1)), Integer.valueOf(getString(R.string.btnDisabled2)), Integer.valueOf(getString(R.string.btnDisabled3))));
        btnListaAsignadaSede.setColorFilter(Color.rgb(Integer.valueOf(getString(R.string.btnDisabled1)), Integer.valueOf(getString(R.string.btnDisabled2)), Integer.valueOf(getString(R.string.btnDisabled3))));
        regionBuscar.setColorFilter(Color.rgb(Integer.valueOf(getString(R.string.btnDisabled1)), Integer.valueOf(getString(R.string.btnDisabled2)), Integer.valueOf(getString(R.string.btnDisabled3))));

        txtManifiesto.setTextColor(Color.rgb(Integer.valueOf(getString(R.string.btnDisabled1)), Integer.valueOf(getString(R.string.btnDisabled2)), Integer.valueOf(getString(R.string.btnDisabled3))));
        txtSincronizar.setTextColor(Color.rgb(Integer.valueOf(getString(R.string.btnDisabled1)), Integer.valueOf(getString(R.string.btnDisabled2)), Integer.valueOf(getString(R.string.btnDisabled3))));
        txtMovilizar.setTextColor(Color.rgb(Integer.valueOf(getString(R.string.btnDisabled1)), Integer.valueOf(getString(R.string.btnDisabled2)), Integer.valueOf(getString(R.string.btnDisabled3))));

        btnListaAsignadaSede.setEnabled(false);
        btnSincManifiestos.setEnabled(false);
        regionBuscar.setEnabled(false);

        verificarInicioLote();

        btnListaAsignadaSede.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNavegate(HojaRutaAsignadaSedeFragment.newInstance());
            }
        });

        btnInciaLote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*dialogPlacas = new DialogPlacaSede(getActivity());
                dialogPlacas.setCancelable(false);
                dialogPlacas.setTitle("INICIAR LOTE");
                dialogPlacas.show();*/
                builder = new DialogBuilder(getActivity());
                builder.setCancelable(false);
                builder.setMessage("Se va a abrir un nuevo lote");
                builder.setPositiveButton("SI", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();
                        registrarLote();
                    }
                });
                builder.setNegativeButton("NO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();
                    }
                });

                builder.show();

            }
        });

        btnSincManifiestos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPlacasRecolector = new DialogPlacaSedeRecolector(getActivity());
                dialogPlacasRecolector.setCancelable(false);
                dialogPlacasRecolector.setTitle("TRANSPORTE RECOLECCION");
                dialogPlacasRecolector.setmOnSincronizarListener(new DialogPlacaSedeRecolector.onSincronizarListener() {
                    @Override
                    public void onSuccessfull() {
                        Integer num = MyApp.getDBO().manifiestoDetalleSede().contarHojaRutaAsignadas();
                        lblListaManifiestoAsignado.setText("" + MyApp.getDBO().manifiestoDetalleSede().contarHojaRutaAsignadas());
                    }
                });
                dialogPlacasRecolector.show();
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

        btnFinLote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogConfirmarCierreLote = new DialogConfirmarCierreLote(getActivity());
                dialogConfirmarCierreLote.setCancelable(false);
                dialogConfirmarCierreLote.setTitle("CERRAR LOTE");
                dialogConfirmarCierreLote.setmOnRegisterCerrarLoteTask(new DialogConfirmarCierreLote.onRegisterCerrarLoteTask() {
                    @Override
                    public void onSuccessfull() {

                        dialogConfirmarCierreLote.dismiss();
                        verificarInicioLote();
                    }
                });
                dialogConfirmarCierreLote.show();
            }
        });

        sectionQrLoteSede.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* IntentIntegrator integrator = new IntentIntegrator(getActivity());
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("LECTURA CÓDIGO QR SEDE");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(false);
                integrator.setOrientationLocked(false);
                integrator.initiateScan();*/
            }
        });

        consultarFirmaAndDestinoEspecificoUsuario();
    }

    @Override
    public void reciveData(String data) {
        try {
            Toast.makeText(getActivity(), data, Toast.LENGTH_LONG).show();
            // CODIGO PARA LECTURA DISPOSITIVO HONEYWELL
            ParametroEntity iniciLote = MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_inicio_lote");
            ParametroEntity finLotes = MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_fin_lote");
            if (iniciLote != null) {
                String codigoQr = data;
                String[] array = codigoQr.split("\\$");
                MyApp.getDBO().parametroDao().saveOrUpdate("current_idTransportistaRecolector", array[3]);//idTransportistaRecolector
                MyApp.getDBO().parametroDao().saveOrUpdate("current_destino_especifico", array[4]);//destino
                MyApp.getDBO().parametroDao().saveOrUpdate("current_vehiculo", "" + array[5]);//idvehiculo
                MyApp.getDBO().parametroDao().saveOrUpdate("current_placa_transportista", "" + array[6]);
                MyApp.getDBO().parametroDao().saveOrUpdate("current_idSubruta", "" + array[7]);
                MyApp.getDBO().parametroDao().saveOrUpdate("current_estadoCodigoQr", "1");
                String destinoUsuario = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_destino_usuario") == null ? "" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_destino_usuario");
                if (array[4].equals(destinoUsuario)) {
                    UserConsultaQrPlantaTask consultaQrPlantaTask = new UserConsultaQrPlantaTask(getActivity());
                    consultaQrPlantaTask.setOnQrPlantaListener(new UserConsultaQrPlantaTask.OnQrPlantaListener() {
                        @Override
                        public void onSuccessful() {
                            setNavegate(HojaRutaQrLoteFragment.newInstance());
                        }
                    });
                    consultaQrPlantaTask.execute();
                } else {
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

            } else {
                final DialogBuilder dialogBuilder = new DialogBuilder(getActivity());
                dialogBuilder.setCancelable(false);
                dialogBuilder.setMessage("Antes de sincronizar debe iniciar un lote!!!");
                dialogBuilder.setPositiveButton("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                    }
                });
                dialogBuilder.show();
            }
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            messageBox("El código escaneado no es de tipo Lote.");
        }
    }

    @Override
    public void onCameraResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getActivity(), "Escaneo Cancelado", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), result.getContents(), Toast.LENGTH_LONG).show();
                ParametroEntity iniciLote = MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_inicio_lote");
                ParametroEntity finLotes = MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_fin_lote");
                if (iniciLote != null) {
                    String codigoQr = result.getContents();
                    String[] array = codigoQr.split("\\$");
                    MyApp.getDBO().parametroDao().saveOrUpdate("current_destino_especifico", array[4]);//destino
                    MyApp.getDBO().parametroDao().saveOrUpdate("current_vehiculo", "" + array[5]);//idvehiculo
                    MyApp.getDBO().parametroDao().saveOrUpdate("current_placa_transportista", "" + array[6]);
                    MyApp.getDBO().parametroDao().saveOrUpdate("current_idSubruta", "" + array[7]);
                    MyApp.getDBO().parametroDao().saveOrUpdate("current_estadoCodigoQr", "1");
                    // MySession.setDestinoEspecifico();
                    /*if (array[4].equals(MySession.getDestinoEspecifico())){*/
                    cargarManifiesto();
                   /* }else {
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
                    }*/
                } else {
                    final DialogBuilder dialogBuilder = new DialogBuilder(getActivity());
                    dialogBuilder.setCancelable(false);
                    dialogBuilder.setMessage("Antes de sincronizar debe iniciar un lote!!!");
                    dialogBuilder.setPositiveButton("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismiss();
                        }
                    });
                    dialogBuilder.show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initBuscador() {
        regionBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datosLotesDisponibles();
            }
        });
    }

    private void datosLotesDisponibles() {

        consultarLotes = new UserConsultaLotes(getActivity());
        consultarLotes.setOnRegisterListener(new UserConsultaLotes.TaskListener() {
            @Override
            public void onSuccessful() {
                setNavegate(HojaFragment.newInstance());
            }
        });
        consultarLotes.execute();
    }

    private void verificarInicioLote() {
        lblListaManifiestoAsignado.setText("" + MyApp.getDBO().manifiestoDetalleSede().contarHojaRutaAsignadas());

        ParametroEntity iniciLote = MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_inicio_lote");
        ParametroEntity finLotes = MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_fin_lote");

        if (iniciLote != null) {
            inicioLote = Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_inicio_lote").getValor());
        } else {
            inicioLote = 0;
        }
        if (finLotes != null) {
            finLote = Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_fin_lote").getValor());
        } else {
            finLote = 0;
        }


        if (inicioLote.equals(finLote)) {
            lnlIniciaLote.setVisibility(View.VISIBLE);
            lnlFinLote.setVisibility(View.GONE);
            btnListaAsignadaSede.setEnabled(false);
            btnSincManifiestos.setEnabled(false);
            regionBuscar.setEnabled(true);

            btnListaAsignadaSede.setColorFilter(Color.rgb(Integer.valueOf(getString(R.string.btnDisabled1)), Integer.valueOf(getString(R.string.btnDisabled2)), Integer.valueOf(getString(R.string.btnDisabled3))));
            btnSincManifiestos.setColorFilter(Color.rgb(Integer.valueOf(getString(R.string.btnDisabled1)), Integer.valueOf(getString(R.string.btnDisabled2)), Integer.valueOf(getString(R.string.btnDisabled3))));

            txtManifiesto.setTextColor(Color.rgb(Integer.valueOf(getString(R.string.btnDisabled1)), Integer.valueOf(getString(R.string.btnDisabled2)), Integer.valueOf(getString(R.string.btnDisabled3))));
            txtSincronizar.setTextColor(Color.rgb(Integer.valueOf(getString(R.string.btnDisabled1)), Integer.valueOf(getString(R.string.btnDisabled2)), Integer.valueOf(getString(R.string.btnDisabled3))));

            regionBuscar.setColorFilter(Color.TRANSPARENT);
            txtMovilizar.setTextColor(Color.WHITE);

        } else {

            lnlIniciaLote.setVisibility(View.GONE);
            lnlFinLote.setVisibility(View.VISIBLE);
            btnListaAsignadaSede.setEnabled(true);
            btnSincManifiestos.setEnabled(true);
            regionBuscar.setEnabled(false);

            btnListaAsignadaSede.setColorFilter(Color.TRANSPARENT);
            btnSincManifiestos.setColorFilter(Color.TRANSPARENT);
            txtSincronizar.setTextColor(Color.WHITE);
            txtManifiesto.setTextColor(Color.WHITE);

            regionBuscar.setColorFilter(Color.rgb(Integer.valueOf(getString(R.string.btnDisabled1)), Integer.valueOf(getString(R.string.btnDisabled2)), Integer.valueOf(getString(R.string.btnDisabled3))));
        }

    }

    private void registrarLote() {

        registrarLoteInicioTask = new UserRegistrarLoteInicioTask(getActivity());
        registrarLoteInicioTask.setOnRegisterListener(new UserRegistrarLoteInicioTask.OnRegisterListener() {
            @Override
            public void onSuccessful() {
                messageBox("Lote Registrado");
                MyApp.getDBO().parametroDao().saveOrUpdate("loteBandera_sedes", "true");

                activarFinLote();
            }

            @Override
            public void onFail() {
                messageBox("Lote no registrado");

            }
        });
        registrarLoteInicioTask.execute();
    }

    private void activarFinLote() {
        lnlIniciaLote.setVisibility(View.GONE);
        lnlFinLote.setVisibility(View.VISIBLE);
        btnListaAsignadaSede.setEnabled(true);
        btnSincManifiestos.setEnabled(true);
        regionBuscar.setEnabled(false);

        btnListaAsignadaSede.setColorFilter(Color.TRANSPARENT);
        btnSincManifiestos.setColorFilter(Color.TRANSPARENT);
        txtManifiesto.setTextColor(Color.WHITE);
        txtSincronizar.setTextColor(Color.WHITE);

        regionBuscar.setColorFilter(Color.rgb(Integer.valueOf(getActivity().getString(R.string.btnDisabled1)), Integer.valueOf(getActivity().getString(R.string.btnDisabled1)), Integer.valueOf(getActivity().getString(R.string.btnDisabled1))));
        txtMovilizar.setTextColor(Color.rgb(Integer.valueOf(getActivity().getString(R.string.btnDisabled1)), Integer.valueOf(getActivity().getString(R.string.btnDisabled1)), Integer.valueOf(getActivity().getString(R.string.btnDisabled1))));

    }

    @SuppressLint("SetTextI18n")
    private void cargarManifiesto() {
        consultarHojaRutaTask = new UserConsultarManifiestosSedeTask(getActivity());
        consultarHojaRutaTask.setmOnVehiculoListener(new UserConsultarManifiestosSedeTask.OnPlacaListener() {
            @Override
            public void onSuccessful(List<DtoManifiestoSede> catalogos) {
                Integer num = MyApp.getDBO().manifiestoDetalleSede().contarHojaRutaAsignadas();
                lblListaManifiestoAsignado.setText("" + MyApp.getDBO().manifiestoDetalleSede().contarHojaRutaAsignadas());
            }
        });
        consultarHojaRutaTask.execute();
        //lblListaManifiestoAsignado.setText(""+ MyApp.getDBO().manifiestoDetalleSede().contarHojaRutaAsignadas());
    }

    public static HomeSedeFragment create() {
        return new HomeSedeFragment();
    }


    private void consultarFirmaAndDestinoEspecificoUsuario() {
        UserConsultaFirmaUsuarioTask consultaFirmaUsuarioTask = new UserConsultaFirmaUsuarioTask(getActivity(), MySession.getIdUsuario());
        consultaFirmaUsuarioTask.setOnFirmaListener(new UserConsultaFirmaUsuarioTask.OnFirmaListener() {
            @Override
            public void onSuccessful() {
                ConsultarFirmaUsuarioEntity consultarFirmaUsuarioEntity = MyApp.getDBO().consultarFirmaUsuarioDao().fetchFirmaUsuario2();
                String firmaUsuario = consultarFirmaUsuarioEntity == null ? "" : (consultarFirmaUsuarioEntity.getFirmaBase64() == null ? "" : consultarFirmaUsuarioEntity.getFirmaBase64());
                int idDestinoEspecifico = consultarFirmaUsuarioEntity == null ? 0 : (consultarFirmaUsuarioEntity.getIdFinRutaCatalogo() == null ? 0 : consultarFirmaUsuarioEntity.getIdFinRutaCatalogo());
                MyApp.getDBO().parametroDao().saveOrUpdate("current_firma_usuario", firmaUsuario);
                MyApp.getDBO().parametroDao().saveOrUpdate("current_destino_usuario", idDestinoEspecifico + "");
            }
        });
        consultaFirmaUsuarioTask.execute();

    }
}

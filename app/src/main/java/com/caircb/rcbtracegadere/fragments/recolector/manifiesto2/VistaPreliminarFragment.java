package com.caircb.rcbtracegadere.fragments.recolector.manifiesto2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.app.Fragment;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.dao.ManifiestoFileDao;
import com.caircb.rcbtracegadere.database.entity.ManifiestoDetallePesosEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoEntity;
import com.caircb.rcbtracegadere.database.entity.RuteoRecoleccionEntity;
import com.caircb.rcbtracegadere.dialogs.DialogAgregarFotografias;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.dialogs.DialogNotificacionCapacidadCamion;
import com.caircb.rcbtracegadere.fragments.recolector.HojaRutaAsignadaFragment;
import com.caircb.rcbtracegadere.fragments.recolector.HomeTransportistaFragment;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.MyPrint;
import com.caircb.rcbtracegadere.helpers.MyConstant;
import com.caircb.rcbtracegadere.helpers.MyManifiesto;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.CatalogoItemValor;
import com.caircb.rcbtracegadere.models.DtoRuteoRecoleccion;
import com.caircb.rcbtracegadere.models.ItemManifiesto;
import com.caircb.rcbtracegadere.models.RowItemManifiesto;
import com.caircb.rcbtracegadere.tasks.UserConsultarCatalogosTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarHojaRutaTask;
import com.caircb.rcbtracegadere.tasks.UserNotificacionTask;
import com.caircb.rcbtracegadere.tasks.UserRegistrarRecoleccion;
import com.caircb.rcbtracegadere.tasks.UserRegistrarRuteoRecoleccion;
import com.caircb.rcbtracegadere.generics.OnCameraListener;
import com.joanzapata.pdfview.PDFView;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VistaPreliminarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VistaPreliminarFragment extends MyFragment implements OnCameraListener, View.OnClickListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    DialogBuilder builder;

    Integer idAppManifiesto, idAppTipoPaquete;
    LinearLayout btnVistaPreviaCancelar, btnVistaPreviaGuardar;
    ProgressDialog dialog;
    PDFView pdfView;
    MyManifiesto myManifiesto;
    UserRegistrarRecoleccion userRegistrarRecoleccion;
    UserRegistrarRuteoRecoleccion userRegistrarRuteoRecoleccion;
    DialogBuilder dialogBuilder;
    DialogBuilder dialogBuilder2;
    DialogBuilder dialogBuilderPrint;
    String identifiacion;
    private List<RowItemManifiesto> detalles;
    private List<ManifiestoDetallePesosEntity> itemManifiestoDetalleBultos;
    TextView txtPesoPromedio, txtCountPhoto;
    LinearLayout novedadPesoPromedio, btnEvidenciaNovedadFrecuente, lnlCountPhoto;
    RelativeLayout btnEliminarFotos;
    DialogAgregarFotografias dialogAgregarFotografias;
    Window window;
    UserConsultarHojaRutaTask consultarHojaRutaTask;
    UserConsultarCatalogosTask consultarCatalogosTask;
    MyPrint print;

    public static VistaPreliminarFragment newInstance(Integer manifiestoID, Integer idAppTipoPaquete, String identificacion) {
        VistaPreliminarFragment fragment = new VistaPreliminarFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, manifiestoID);
        args.putString(ARG_PARAM3, identificacion);
        if (idAppTipoPaquete != null) {
            args.putInt(ARG_PARAM2, idAppTipoPaquete);
        } else {
            args.putInt(ARG_PARAM2, 0);
        }

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idAppManifiesto = getArguments().getInt(ARG_PARAM1);
            idAppTipoPaquete = getArguments().getInt(ARG_PARAM2);
            identifiacion = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setView(inflater.inflate(R.layout.fragment_vista_preliminar, container, false));
        init();
        generarPDF();
        return getView();
    }

    private void init() {

        btnVistaPreviaCancelar = getView().findViewById(R.id.btnVistaPreviaCancelar);
        btnVistaPreviaGuardar = getView().findViewById(R.id.btnVistaPreviaGuardar);
        btnVistaPreviaCancelar.setOnClickListener(this);
        btnVistaPreviaGuardar.setOnClickListener(this);

        novedadPesoPromedio = getView().findViewById(R.id.sectionNovedadPesoPromedio);
        String tipoSubruta2 = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta") == null ? "" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta");
        String menuRecoleccion = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("seleccionMenuRecoleccion") == null ? "" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("seleccionMenuRecoleccion");
        if (tipoSubruta2.equals("2") && menuRecoleccion.equals("1")) { //Tipo de ruta es Hospitalario y selección menú recolección es recoleccion normal
            novedadPesoPromedio.setVisibility(View.VISIBLE);
        } else { // Tipo de ruta Industrial y selección menú recolección es por no recolección
            novedadPesoPromedio.setVisibility(View.GONE);
        }

        txtPesoPromedio = getView().findViewById(R.id.txtPesoPromedio);
        lnlCountPhoto = getView().findViewById(R.id.lnlCountPhoto);
        txtCountPhoto = getView().findViewById(R.id.txtCountPhoto);
        int countFotos = MyApp.getDBO().manifiestoFileDao().obtenerCantidadFotografiabyManifiestoCatalogo(idAppManifiesto, 101, 19);
        if (countFotos > 0) {
            lnlCountPhoto.setVisibility(View.VISIBLE);
            txtCountPhoto.setText(countFotos + "");
        } else {
            lnlCountPhoto.setVisibility(View.GONE);
            txtCountPhoto.setText("");
        }


        btnEliminarFotos = getView().findViewById(R.id.btnEliminarFotos);
        btnEliminarFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApp.getDBO().manifiestoFileDao().deleteFotoByIdAppManifistoCatalogo(idAppManifiesto, 101);
                int countFotos = MyApp.getDBO().manifiestoFileDao().obtenerCantidadFotografiabyManifiestoCatalogo(idAppManifiesto, 101, 19);
                if (countFotos > 0) {
                    lnlCountPhoto.setVisibility(View.VISIBLE);
                    txtCountPhoto.setText(countFotos + "");
                } else {
                    lnlCountPhoto.setVisibility(View.GONE);
                    txtCountPhoto.setText("");
                }
            }
        });

        btnEvidenciaNovedadFrecuente = getView().findViewById(R.id.btnEvidenciaNovedadFrecuente);
        btnEvidenciaNovedadFrecuente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAgregarFotografias = new DialogAgregarFotografias(getActivity(), idAppManifiesto, 101, ManifiestoFileDao.FOTO_NOVEDAD_PESO_PROMEDIO, MyConstant.STATUS_RECOLECCION);
                dialogAgregarFotografias.setCancelable(false);
                dialogAgregarFotografias.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogAgregarFotografias.setOnAgregarFotosListener(new DialogAgregarFotografias.OnAgregarFotosListener() {
                    @Override
                    public void onSuccessful(Integer cantidad) {
                           /* if(dialogAgregarFotografias!=null && dialogAgregarFotografias.isShowing()){
                                dialogAgregarFotografias.dismiss();
                                dialogAgregarFotografias=null;
                            }*/
                        lnlCountPhoto.setVisibility(View.VISIBLE);
                        txtCountPhoto.setText(String.valueOf(cantidad));
                    }
                });
                dialogAgregarFotografias.show();
                window = dialogAgregarFotografias.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }
        });


        detalles = MyApp.getDBO().manifiestoDetalleDao().fetchHojaRutaDetallebyIdManifiesto(idAppManifiesto);
        String tipoSubruta = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta") == null ? "" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta");
        if (tipoSubruta.equals("2") && menuRecoleccion.equals("1")) {//Tipo de ruta es Hospitalario y selección menú recolección es recoleccion normal

            double pesoPromedio = MyApp.getDBO().manifiestoDao().selectPesoPromediobyIdManifiesto(idAppManifiesto);
            double pesoTotal = 0.0;
            for (int i = 0; i < detalles.size(); i++) {
                itemManifiestoDetalleBultos = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarBultosManifiestoDet(detalles.get(i).getId());
                for (int j = 0; j < itemManifiestoDetalleBultos.size(); j++) {
                    pesoTotal += itemManifiestoDetalleBultos.get(j).getValor();
                }
            }
            pesoTotal=Double.parseDouble(obtieneDosDecimales(pesoTotal));
            double pesoMostrarMensaje=pesoTotal - pesoPromedio;
            if(pesoPromedio > 0)
            {
                if (pesoTotal > (pesoPromedio + (pesoPromedio * 0.20)) || pesoTotal < (pesoPromedio - (pesoPromedio * 0.20))) {
                    txtPesoPromedio.setText("PESO TOTAL MANIFIESTOS (" + pesoTotal + " KG), DIFERENCIA DE +-20% PROMEDIO (" + obtieneDosDecimales(pesoMostrarMensaje) + " KG)");
                    MyApp.getDBO().parametroDao().saveOrUpdate("textoPesoPromedio", "" + txtPesoPromedio.getText());
                    if (pesoTotal == 0.0) {
                        novedadPesoPromedio.setVisibility(View.GONE);
                        txtPesoPromedio.setText("");
                    } else {
                        novedadPesoPromedio.setVisibility(View.VISIBLE);
                    }
                } else {
                    novedadPesoPromedio.setVisibility(View.GONE);
                    txtPesoPromedio.setText("");
                }
            }else{
                novedadPesoPromedio.setVisibility(View.GONE);
                txtPesoPromedio.setText("");
            }
        } else {// Tipo de ruta Industrial y selección menú recolección es por no recolección
            novedadPesoPromedio.setVisibility(View.GONE);
            txtPesoPromedio.setText("");
        }
    }


    @Override
    public void onCameraResult(int requestCode, int resultCode, Intent data) {
        dialogAgregarFotografias.setMakePhoto(requestCode);
    }

    public boolean validaPesoReferencial() {
        if (novedadPesoPromedio.getVisibility() == View.VISIBLE) {
            int cantidadFotos = MyApp.getDBO().manifiestoFileDao().obtenerCantidadFotografiabyManifiestoCatalogo(idAppManifiesto, 101, 19);
            if (cantidadFotos == 0) {
                return true;//Debe ingresar fotos
            } else {
                return false;//Ya ingresó fotos
            }
        } else {
            return false;//No debe ingresar fotos
        }
    }

    private void generarPDF() {

        MyApp.getDBO().manifiestoDao().updateManifiestoFechaRecoleccion(idAppManifiesto, new Date());

        new AsyncTask<Void, Void, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(getActivity());
                dialog.setMessage("Construyendo " + System.getProperty("line.separator") + "vista preliminar...");
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                myManifiesto = new MyManifiesto(getActivity(), idAppManifiesto, idAppTipoPaquete, identifiacion);
                myManifiesto.create();
                return myManifiesto.getPathFile();
            }

            @Override
            protected void onPostExecute(String path) {
                super.onPostExecute(path);
                dialog.dismiss();
                cargarPDF(path);
            }
        }.execute();
    }

    private void cargarPDF(String path) {
        try {
            pdfView = (PDFView) getView().findViewById(R.id.pdfViewPager);
            File f = new File(path);
            pdfView.fromFile(f).load();

        } catch (Exception ex) {
            int x = 0;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnVistaPreviaCancelar:
                setNavegate(Manifiesto2Fragment.newInstance(idAppManifiesto, 2, 1));
                break;
            case R.id.btnVistaPreviaGuardar:
                builder = new DialogBuilder(getActivity());
                builder.setMessage("¿Esta seguro que desea continuar?");
                builder.setCancelable(false);
                builder.setPositiveButton("SI", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        registarDatos();
                        builder.dismiss();
                    }
                });
                builder.setNegativeButton("NO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();
                    }
                });
                builder.show();
                break;
        }
    }

    private void registarDatos() {
        if (validaPesoReferencial() == true) {
            messageBox("Debe ingresar fotografías, justificando Peso Promedio");
            return;
        } else {
            userRegistrarRecoleccion = new UserRegistrarRecoleccion(getActivity(), idAppManifiesto, getLocation(),null);
            userRegistrarRecoleccion.setOnRegisterListener(new UserRegistrarRecoleccion.OnRegisterListener() {
                @Override
                public void onSuccessful(final Date fechaRecol) {

                    RuteoRecoleccionEntity dto;
                    dto = MyApp.getDBO().ruteoRecoleccion().searchUltimoRegistro();
                    if (dto != null) {
                        MyApp.getDBO().ruteoRecoleccion().updatePuntoLlegadaFechaLlegadaEstado(dto.get_id(), idAppManifiesto, fechaRecol, true);
                    } else {
                        MyApp.getDBO().ruteoRecoleccion().saverOrUpdate(new DtoRuteoRecoleccion(MySession.getIdSubRuta(), fechaRecol, idAppManifiesto, null, null, true));
                        dto = MyApp.getDBO().ruteoRecoleccion().searchUltimoRegistro();
                    }

                    userRegistrarRuteoRecoleccion = new UserRegistrarRuteoRecoleccion(getActivity(), dto);
                    userRegistrarRuteoRecoleccion.setOnRegisterRuteoRecollecionListenner(new UserRegistrarRuteoRecoleccion.OnRegisterRuteroRecoleecionListener() {
                        @Override
                        public void onSuccessful() {
                            final String tipoSubruta = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta") == null ? "" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta");
                            if (tipoSubruta.equals("2")) {
                                builder = new DialogBuilder(getActivity());
                                builder.setMessage("¿Desea volver a imprimir la etiqueta?");
                                builder.setCancelable(false);
                                builder.setPositiveButton("SI", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        imprimirEtiquetaHospitalario(idAppManifiesto);
                                    }
                                });
                                builder.setNegativeButton("NO", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        builder.dismiss();
                                        registrarDatos(fechaRecol, tipoSubruta);
                                    }
                                });
                                builder.show();
                                imprimirEtiquetaHospitalario(idAppManifiesto);
                            }else{
                                registrarDatos(fechaRecol, tipoSubruta);
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

    public void registrarDatos (final Date fechaRecol, final String tipoSubruta){

        if (MyApp.getDBO().manifiestoDao().contarHojaRutaAsignadas() > 0) {
            if (tipoSubruta.equals("2")) {//SI ES TIPO DE RUTA HOSPITALARIA
                        MyApp.getDBO().parametroDao().saveOrUpdate("checkTara", "2");
                        dialogBuilder = new DialogBuilder(getActivity());
                        dialogBuilder.setMessage("¿El camión llegó a su máxima capacidad?");
                        dialogBuilder.setCancelable(false);
                        dialogBuilder.setPositiveButton("SI", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //dialogBuilder.dismiss();
                                DialogNotificacionCapacidadCamion capacidadCamion = new DialogNotificacionCapacidadCamion(getActivity(), idAppManifiesto);
                                capacidadCamion.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                capacidadCamion.setCancelable(false);
                                capacidadCamion.show();
                                capacidadCamion.setOnRegisterListener(new DialogNotificacionCapacidadCamion.OnRegisterListener() {
                                    @Override
                                    public void onSuccessful() {
                                        dialogBuilder.dismiss();
                                        setNavegate(HomeTransportistaFragment.create());

                                    }

                                    @Override
                                    public void onFailure() {

                                    }
                                });
                            }
                        });
                        dialogBuilder.setNegativeButton("NO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogBuilder.dismiss();
                                dialogBuilder2 = new DialogBuilder(getActivity());
                                dialogBuilder2.setMessage("¿Desea iniciar traslado al próximo punto de recolección ?");
                                dialogBuilder2.setCancelable(false);
                                dialogBuilder2.setPositiveButton("SI", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogBuilder2.dismiss();
                                        dialogBuilder.dismiss();
                                        //Guardo la nueva fecha de inicio y puntoParitda;
                                        MyApp.getDBO().parametroDao().saveOrUpdate("ruteoRecoleccion", "SI");
                                        RuteoRecoleccionEntity dto;
                                        dto = MyApp.getDBO().ruteoRecoleccion().searchUltimoRegistro();
                                        if (dto != null) {
                                            MyApp.getDBO().ruteoRecoleccion().saverOrUpdate(new DtoRuteoRecoleccion(MySession.getIdSubRuta(), fechaRecol, dto.getPuntoLlegada(), null, null, false));
                                        }
                                        //List<RuteoRecoleccionEntity> enty3 = MyApp.getDBO().ruteoRecoleccion().searchRuteoRecoleccion(); //////////
                                        MyApp.getDBO().parametroDao().saveOrUpdate("checkTara", "2");
                                        setNavegate(HojaRutaAsignadaFragment.newInstance());
                                    }
                                });
                                dialogBuilder2.setNegativeButton("NO", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogBuilder2.dismiss();
                                        dialogBuilder.dismiss();
                                        //Update parametro en NO para levantar el modal para verificar si empieza con el trazlado
                                        MyApp.getDBO().parametroDao().saveOrUpdate("ruteoRecoleccion", "NO");
                                        RuteoRecoleccionEntity dto;
                                        dto = MyApp.getDBO().ruteoRecoleccion().searchUltimoRegistro();
                                        if (dto != null) {
                                            MyApp.getDBO().ruteoRecoleccion().saverOrUpdate(new DtoRuteoRecoleccion(MySession.getIdSubRuta(), fechaRecol, dto.getPuntoLlegada(), null, null, false));
                                        }
                                        MyApp.getDBO().parametroDao().saveOrUpdate("checkTara", "2");
                                        setNavegate(HomeTransportistaFragment.create());
                                    }
                                });
                                dialogBuilder2.show();
                            }
                        });
                        dialogBuilder.show();
            } else if (tipoSubruta.equals("1")) { // SI ES TIPO DE RUTA INDUSTRIAL
                dialogBuilder = new DialogBuilder(getActivity());
                dialogBuilder.setMessage("¿Desea iniciar traslado al próximo punto de recolección ?");
                dialogBuilder.setCancelable(false);
                dialogBuilder.setPositiveButton("SI", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                        //Guardo la nueva fecha de inicio y puntoParitda;
                        MyApp.getDBO().parametroDao().saveOrUpdate("ruteoRecoleccion", "SI");
                        RuteoRecoleccionEntity dto;
                        dto = MyApp.getDBO().ruteoRecoleccion().searchUltimoRegistro();
                        if (dto != null) {
                            MyApp.getDBO().ruteoRecoleccion().saverOrUpdate(new DtoRuteoRecoleccion(MySession.getIdSubRuta(), fechaRecol, dto.getPuntoLlegada(), null, null, false));
                        }
                        //List<RuteoRecoleccionEntity> enty3 = MyApp.getDBO().ruteoRecoleccion().searchRuteoRecoleccion(); //////////
                        MyApp.getDBO().parametroDao().saveOrUpdate("checkTara", "2");
                        setNavegate(HojaRutaAsignadaFragment.newInstance());
                    }
                });
                dialogBuilder.setNegativeButton("NO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                        //Update parametro en NO para levantar el modal para verificar si empieza con el trazlado
                        MyApp.getDBO().parametroDao().saveOrUpdate("ruteoRecoleccion", "NO");
                        RuteoRecoleccionEntity dto;
                        dto = MyApp.getDBO().ruteoRecoleccion().searchUltimoRegistro();
                        if (dto != null) {
                            MyApp.getDBO().ruteoRecoleccion().saverOrUpdate(new DtoRuteoRecoleccion(MySession.getIdSubRuta(), fechaRecol, dto.getPuntoLlegada(), null, null, false));
                        }
                        MyApp.getDBO().parametroDao().saveOrUpdate("checkTara", "2");
                        setNavegate(HomeTransportistaFragment.create());
                    }
                });
                dialogBuilder.show();
            }

        } else {//Finalizo de recolectar todos los manifiestos
            MyApp.getDBO().parametroDao().saveOrUpdate("ruteoRecoleccion", "NO");
            RuteoRecoleccionEntity dto;
            dto = MyApp.getDBO().ruteoRecoleccion().searchUltimoRegistro();
            if (dto != null) {
                MyApp.getDBO().ruteoRecoleccion().saverOrUpdate(new DtoRuteoRecoleccion(MySession.getIdSubRuta(), fechaRecol, dto.getPuntoLlegada(), null, null, false));
            }
            setNavegate(HomeTransportistaFragment.create());
        }
        String estadotransportista = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("estadoFirmaTransportista");
        String estadoAuxiliar = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("estadoFirmaAuxiliar");
        String estadoOperador = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("estadoFirmaOperador");

        if (estadotransportista.equals("1") || estadoAuxiliar.equals("1") || estadoOperador.equals("1")){
            sincronizarManifiestos();
        }

    }

    private void sincronizarManifiestos(){
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

    UserConsultarHojaRutaTask.TaskListener listenerHojaRuta = new UserConsultarHojaRutaTask.TaskListener() {
        @Override
        public void onSuccessful() {

        }
    };
    private String obtieneDosDecimales(double valor) {
        DecimalFormat format = new DecimalFormat();
        format.setMaximumFractionDigits(2); //Define 2 decimales.
        return format.format(valor);
    }

    private void imprimirEtiquetaHospitalario(final Integer idAppManifiesto) {
        print = new MyPrint(getActivity());
        print.printerHospitalario(idAppManifiesto);
    }



}
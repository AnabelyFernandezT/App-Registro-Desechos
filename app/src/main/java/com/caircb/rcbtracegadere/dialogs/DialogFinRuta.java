package com.caircb.rcbtracegadere.dialogs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.AppDatabase;
import com.caircb.rcbtracegadere.database.entity.CatalogoEntity;
import com.caircb.rcbtracegadere.database.entity.HotelLotePadreEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoPaquetesEntity;
import com.caircb.rcbtracegadere.database.entity.PaqueteEntity;
import com.caircb.rcbtracegadere.database.entity.ParametroEntity;
import com.caircb.rcbtracegadere.database.entity.RutaInicioFinEntity;
import com.caircb.rcbtracegadere.database.entity.RutasEntity;
import com.caircb.rcbtracegadere.generics.MyDialog;
import com.caircb.rcbtracegadere.generics.MyPrint;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.ItemManifiesto;
import com.caircb.rcbtracegadere.models.RowItemFinRuta;
import com.caircb.rcbtracegadere.models.RowItemPaquete;
import com.caircb.rcbtracegadere.models.response.DtoCatalogo;
import com.caircb.rcbtracegadere.models.response.DtoFindRutas;
import com.caircb.rcbtracegadere.tasks.UserConsultarDestinosTask;
import com.caircb.rcbtracegadere.tasks.UserDestinoEspecificoTask;
import com.caircb.rcbtracegadere.tasks.UserObtenerLotePadreHotelTask;
import com.caircb.rcbtracegadere.tasks.UserRegistrarFinLoteHotelTask;
import com.caircb.rcbtracegadere.tasks.UserRegistrarFinRutaTask;
import com.caircb.rcbtracegadere.tasks.UserRegistrarInicioFinLoteHotelTask;
import com.caircb.rcbtracegadere.tasks.UserRegistrarInicioRutaTask;
import com.caircb.rcbtracegadere.tasks.UserRegistrarRecoleccion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DialogFinRuta extends MyDialog {

    TextView txt_placa, txt_kilometraje_inicio;
    LinearLayout btnFinApp, btnCancelarApp;
    Activity _activity;
    Boolean bandera;
    EditText kilometrajeFinal;
    long idRegistro;
    HotelLotePadreEntity lotePadre;
    DialogBuilder builder;

    ParametroEntity parametro;
    LinearLayout lnlIniciaRuta,lnlFinRuta;

    String kilometrajeInicio;
    Integer placaInicio, idInicioFin ,idRuta, finHotel=3,tiposubruta;
    Date diaAnterior;
    Spinner listaDestino, listaDestinoParticular;
    String destino = "", destinos="",inicioHotel;

    int idDestino;
    UserRegistrarFinRutaTask registroFinRuta;
    UserConsultarDestinosTask consultarDetino;
    UserDestinoEspecificoTask consultaDestinoEspecifico;
    UserObtenerLotePadreHotelTask lotePadreHotelTask;
    UserRegistrarInicioFinLoteHotelTask inicioFinLoteHotelTask;
    UserRegistrarFinLoteHotelTask finLotePadreHotelTask;

    List<DtoCatalogo> listaDestinos, destinosEspecificos;

    //Botones Inicio
    ImageButton btnSincManifiestos,btnListaAsignadaTransportista,regionBuscar;
    ImageView btnPickUpTransportista, btnDropOffTransportista,btnScanQr;
    TextView txtBuscar, txtSincronizar, txtManifiestos, lblpickUpTransportista,lblListaManifiestoAsignado;
    ProgressDialog dialog;



    ///////////////////////////// IMPRESION FIN RUTA HOSPITALARIO
    private List<ItemManifiesto> rowItems;
    PaqueteEntity pkg;
    ManifiestoPaquetesEntity manifiestoPkg;
    List<RowItemPaquete> listaPaquetes;
    List<RowItemFinRuta> listaFinRuta;
    Integer fundas50 = 0,fundas63=0, paquetes1=0, paquete2=0, paquete3=0 ;
    private Integer pendienteF55x50,pendienteF63x76,pendienteFPc1,pendienteFPc2,pendienteFPc3;
    MyPrint print;
///////////////////////////// IMPRESION FIN RUTA HOSPITALARIO

    public DialogFinRuta(@NonNull Context context) {
        super(context, R.layout.dialog_final_ruta);
        this._activity = (Activity)context;
    }
    public interface OnFinLoteHotel {
        public void onSuccessful();
    }

    private OnFinLoteHotel mOnFinLotePadreListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getView());

        init();
        initButton();
    }

    private void init(){
        btnScanQr = getActivity().findViewById(R.id.btnScanQr);
        lblpickUpTransportista = getActivity().findViewById(R.id.lblpickUpTransportista);
        lblListaManifiestoAsignado = getActivity().findViewById(R.id.lblListaManifiestoAsignado);
        parametro = MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_hotel");
        if(parametro!=null){
            inicioHotel = parametro.getValor();
        }
        lotePadre = MyApp.getDBO().hotelLotePadreDao().fetchConsultarHotelLote(MySession.getIdUsuario());
        listaDestinos = new ArrayList<>();
        destinosEspecificos = new ArrayList<>();
        txt_placa = (TextView)getView().findViewById(R.id.Txt_placa);
        txt_kilometraje_inicio = (TextView)getView().findViewById(R.id.txt_kilometraje_inicio);
        List<DtoFindRutas> listaPlacasDisponibles;
        btnFinApp = (LinearLayout)getView().findViewById(R.id.btnFinalizarRuta);
        btnCancelarApp = (LinearLayout)getView().findViewById(R.id.btnCancelarApp);

        listaDestino = getView().findViewById(R.id.lista_destino);
        listaDestinoParticular = getView().findViewById(R.id.lista_destino_particular);

        //botones home
        btnPickUpTransportista = (ImageView) getActivity().findViewById(R.id.btnPickUpTransportista);
        btnDropOffTransportista = (ImageView) getActivity().findViewById(R.id.btnDropOffTransportista);
        regionBuscar = (ImageButton) getActivity().findViewById(R.id.regionBuscar);
        btnSincManifiestos = (ImageButton) getActivity().findViewById(R.id.btnSincManifiestos);
        btnListaAsignadaTransportista = (ImageButton) getActivity().findViewById(R.id.btnListaAsignadaTransportista);

        lnlIniciaRuta = getActivity().findViewById(R.id.LnlIniciaRuta);
        lnlFinRuta = getActivity().findViewById(R.id.LnlFinRuta);

        kilometrajeFinal = (EditText)getView().findViewById(R.id.kilometrajeFinal) ;
        kilometrajeFinal.setRawInputType(InputType.TYPE_CLASS_NUMBER);

        txtBuscar = (TextView) getActivity().findViewById(R.id.txtBuscar);
        txtSincronizar = (TextView) getActivity().findViewById(R.id.txtSincronizar);
        txtManifiestos = (TextView) getActivity().findViewById(R.id.txtManifiestos);

        btnCancelarApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFinRuta.this.dismiss();
            }
        });

        listaDestino.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    listaDestinos.get(position-1);
                    MyApp.getDBO().parametroDao().saveOrUpdate("current_destino",""+position);
                    destinos = (String) listaDestino.getSelectedItem();
                    traerDestinoEspecifico();
                    //validarHoteles();
                    //if(destinos.equals("HOTEL")&& parametro==null){finHotel=0;}else{finHotel=3;}
                    if(parametro!=null){
                        if(inicioHotel.equals("1") && destinos.equals("HOTEL")){
                            loteHotelPadre();
                        }
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listaDestinoParticular.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    destinosEspecificos.get(position-1);
                    destino = (String) listaDestinoParticular.getSelectedItem();
                    CatalogoEntity c = MyApp.getDBO().catalogoDao().fetchConsultarCatalogo(destino,12);
                    idDestino = c!=null?c.getIdSistema():-1;
                    MyApp.getDBO().parametroDao().saveOrUpdate("current_destino_especifico",""+idDestino);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        desbloqueoBotones();
        traerDatosAnteriores();
        traerDestinos();
    }

    public void initButton(){
        btnFinApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bandera = true;
                Integer kilo = kilometrajeFinal.getText().length();
                if (kilometrajeFinal.getText().length()<=0){
                    messageBox("Se requiere que digite el kilometraje.");
                    return;
                }else{
                    if (Double.parseDouble(kilometrajeInicio)> Double.parseDouble(kilometrajeFinal.getText().toString()))
                    {
                        messageBox("Kilometraje incorrecto");
                        return;
                    }else{

                        if(destinos.equals("") ){
                            messageBox("Seleccione Destino");
                            return;
                        }else {
                            if(destino.equals("") && parametro==null) {
                                messageBox("Seleccione Destino");
                                return;
                            }
                                 else{
                                     if(destinos.equals("HOTEL")&& parametro==null){
                                        loteHotelPadre();
                                    }
                                }

                                    guardarDatos();
                                    //messageBox("guardado");
                                }
                            }

                    }

                }
        });
    }


    private void guardarDatos()
    {
        bloqueoBotones();
        Date dia = AppDatabase.getDateTime();
        CatalogoEntity c = MyApp.getDBO().catalogoDao().fetchConsultarCatalogo(destino,12);
        final int idDestino = c!=null?c.getIdSistema():-1;


        if(idDestino!=-1){
            MyApp.getDBO().parametroDao().saveOrUpdate("current_destino_especifico",""+idDestino);
        }else {
            MyApp.getDBO().parametroDao().saveOrUpdate("current_destino_especifico",""+0);
        }



        idRegistro = MyApp.getDBO().rutaInicioFinDao().saveOrUpdateInicioRuta(idInicioFin, MySession.getIdUsuario(),placaInicio,diaAnterior,dia,kilometrajeInicio,kilometrajeFinal.getText().toString(),2,tiposubruta);
        MyApp.getDBO().parametroDao().saveOrUpdate("current_vehiculo",""+placaInicio);

        registroFinRuta = new UserRegistrarFinRutaTask(getActivity(),idRegistro);
        registroFinRuta.setOnIniciaRutaListener(new UserRegistrarFinRutaTask.OnIniciaRutaListener() {
            @Override
            public void onSuccessful() {

                if(finHotel.equals(0)){
                    inicioFinLoteHotelTask =new UserRegistrarInicioFinLoteHotelTask(getActivity(),idDestino);
                    inicioFinLoteHotelTask.execute();
                }

               // MyApp.getDBO().parametroDao().saveOrUpdate("current_destino_especifico",""+0);
                MyApp.getDBO().parametroDao().saveOrUpdate("current_destino_info",""+0);
                lblpickUpTransportista.setText("0");
                lblListaManifiestoAsignado.setText("0");

                String tipoSubruta = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta") == null ? "" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta");
                if (tipoSubruta.equals("2")) {//TIPO SUBRUTA HOSPITALARIA
                    final DialogBuilder builderw = new DialogBuilder(getContext());
                    builderw.setMessage("¿Desea volver a imprimir otro recibo?");
                    builderw.setCancelable(false);
                    builderw.setPositiveButton("SI", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imprimirEtiquetaFinRutaHospitalaria();
                        }
                    });
                    builderw.setNegativeButton("NO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            builderw.dismiss();
                            MyApp.getDBO().impresoraDao().updateDisabledAllImpresoraWorked();
                            DialogFinRuta.this.dismiss();
                        }
                    });
                    builderw.show();
                    imprimirEtiquetaFinRutaHospitalaria();
                }else {
                    MyApp.getDBO().impresoraDao().updateDisabledAllImpresoraWorked();
                    DialogFinRuta.this.dismiss();
                }
            }



            @Override
            public void onFailure() {
                desbloqueoBotones();
                DialogFinRuta.this.dismiss();
            }
        });

        registroFinRuta.execute();

        if(finHotel.equals(1)){
            inicioFinLoteHotelTask =new UserRegistrarInicioFinLoteHotelTask(getActivity(),idDestino);
            inicioFinLoteHotelTask.execute();
        }

        if (finHotel.equals(0)){
            inicioFinLoteHotelTask =new UserRegistrarInicioFinLoteHotelTask(getActivity(),0);
            inicioFinLoteHotelTask.setOnRegisterListener(new UserRegistrarInicioFinLoteHotelTask.OnRegisterListener() {
                @Override
                public void onSuccessful() {
                    finLotePadreHotelTask = new UserRegistrarFinLoteHotelTask(getActivity());
                    finLotePadreHotelTask.setOnRegisterListener(new UserRegistrarFinLoteHotelTask.OnRegisterListener() {
                        @Override
                        public void onSuccessful() {
                            if (mOnFinLotePadreListener != null) mOnFinLotePadreListener.onSuccessful();
                        }
                    });
                    finLotePadreHotelTask.execute();
                }
            });
            inicioFinLoteHotelTask.execute();
        }

        finRuta();
    }

    private void traerDatosAnteriores(){
        RutaInicioFinEntity rut = MyApp.getDBO().rutaInicioFinDao().fechConsultaInicioFinRutasE(MySession.getIdUsuario());
        if(rut!=null){
            kilometrajeInicio = rut.getKilometrajeInicio();
            //placaInicio = rut.getIdTransporteVehiculo();
            diaAnterior = rut.getFechaInicio();
            idInicioFin = rut.getIdRutaInicioFin();
            idRuta = rut.getIdSubRuta();
            tiposubruta=rut.getTiposubruta();
        }

        //traer placa
        //Integer idRuta = Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_ruta").getValor());

        RutasEntity r = MyApp.getDBO().rutasDao().fetchConsultarNombre(idRuta);
        //CatalogoEntity c = MyApp.getDBO().catalogoDao().fetchConsultarCatalogoEspecifico(placaInicio,4);
        String subRuta = r!=null?r.getNombre():"";

        txt_kilometraje_inicio.setText(kilometrajeInicio);
        txt_placa.setText(subRuta);

    }

    private void traerDestinos(){
        consultarDetino = new UserConsultarDestinosTask(getActivity());
        consultarDetino.setOnDestinoListener(new UserConsultarDestinosTask.OnDestinoListener() {
            @Override
            public void onSuccessful(List<DtoCatalogo> catalogos) {
                listaDestinos = catalogos;
                listaDestino = cargarSpinnerDestino(listaDestino,catalogos,true);
            }
        });
        consultarDetino.execute();
    }
    public Spinner cargarSpinnerDestino(Spinner spinner, List<DtoCatalogo> catalogos, boolean bhabilitar){
        ArrayAdapter<String> adapter;
        Spinner defaulSpiner = spinner;
        ArrayList<String> listaData = new ArrayList<String>();

        //listaRutas = MyApp.getDBO().rutasDao().fetchConsultarRutas();
        listaData.add("SELECCIONE");
        if(catalogos.size() > 0){
            for (DtoCatalogo r : catalogos){
                listaData.add(r.getNombre());
            }
        }

        adapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_spinner, listaData);
        defaulSpiner.setAdapter(adapter);
        defaulSpiner.setEnabled(bhabilitar);

        return defaulSpiner;
    }

    private void traerDestinoEspecifico(){
        consultaDestinoEspecifico = new UserDestinoEspecificoTask(getActivity());
        consultaDestinoEspecifico.setOnDestinoListener(new UserDestinoEspecificoTask.OnDestinoListener() {
            @Override
            public void onSuccessful(List<DtoCatalogo> catalogos, Integer idDestinoX) {
                destinosEspecificos = catalogos;
                listaDestinoParticular = cargarSpinnerDestino(listaDestinoParticular,catalogos,true);
            }
        });
        consultaDestinoEspecifico.execute();
    }

    private void finRuta(){

        regionBuscar.setEnabled(false);
        btnSincManifiestos.setEnabled(false);
        btnListaAsignadaTransportista.setEnabled(false);
        btnPickUpTransportista.setEnabled(false);
        btnDropOffTransportista.setEnabled(false);
        btnScanQr.setEnabled(false);


        regionBuscar.setColorFilter(Color.rgb(Integer.valueOf(getActivity().getString(R.string.btnDisabled1)), Integer.valueOf(getActivity().getString(R.string.btnDisabled1)), Integer.valueOf(getActivity().getString(R.string.btnDisabled1))));
        btnSincManifiestos.setColorFilter(Color.rgb(Integer.valueOf(getActivity().getString(R.string.btnDisabled1)), Integer.valueOf(getActivity().getString(R.string.btnDisabled1)), Integer.valueOf(getActivity().getString(R.string.btnDisabled1))));
        btnListaAsignadaTransportista.setColorFilter(Color.rgb(Integer.valueOf(getActivity().getString(R.string.btnDisabled1)), Integer.valueOf(getActivity().getString(R.string.btnDisabled1)), Integer.valueOf(getActivity().getString(R.string.btnDisabled1))));

        txtBuscar.setTextColor(Color.rgb(Integer.valueOf(getActivity().getString(R.string.btnDisabled1)), Integer.valueOf(getActivity().getString(R.string.btnDisabled1)), Integer.valueOf(getActivity().getString(R.string.btnDisabled1))));
        txtManifiestos.setTextColor(Color.rgb(Integer.valueOf(getActivity().getString(R.string.btnDisabled1)), Integer.valueOf(getActivity().getString(R.string.btnDisabled1)), Integer.valueOf(getActivity().getString(R.string.btnDisabled1))));
        txtSincronizar.setTextColor(Color.rgb(Integer.valueOf(getActivity().getString(R.string.btnDisabled1)), Integer.valueOf(getActivity().getString(R.string.btnDisabled1)), Integer.valueOf(getActivity().getString(R.string.btnDisabled1))));

        MyApp.getDBO().parametroDao().saveOrUpdate("current_placa_transportista",""+0);
        lnlIniciaRuta.setVisibility(View.VISIBLE);
        lnlFinRuta.setVisibility(View.GONE);
        btnPickUpTransportista.setAlpha(0.3f);
        btnDropOffTransportista.setAlpha(0.3f);
        btnScanQr.setAlpha(0.3f);

    }

    private void loteHotelPadre(){
        if(lotePadre!=null){
            validarHoteles();

        }else {
            finHotel = 0;
            lotePadreHotelTask = new UserObtenerLotePadreHotelTask(getActivity());
            lotePadreHotelTask.setmOnLoteHotelPadreListener(new UserObtenerLotePadreHotelTask.OnLoteHotelPadreListener() {
                @Override
                public void onSuccessful() {
                    if(finHotel.equals(0)){
                        inicioFinLote();
                    }
                    MyApp.getDBO().parametroDao().saveOrUpdate("current_hotel",""+1);
                }
            });
            lotePadreHotelTask.execute();
        }

    }

    private void inicioFinLote(){
        inicioFinLoteHotelTask =new UserRegistrarInicioFinLoteHotelTask(getActivity(),idDestino);
        inicioFinLoteHotelTask.execute();
    }

    private void validarHoteles (){

            builder = new DialogBuilder(getActivity());
            builder.setMessage("¿Es su último día de recolección?");
            builder.setCancelable(false);
            builder.setPositiveButton("Si", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listaDestinoParticular.setEnabled(false);
                    MyApp.getDBO().parametroDao().saveOrUpdate("current_destino_especifico",""+0);
                    finHotel=0;
                    builder.dismiss();
                }
            });
            builder.setNegativeButton("No", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finHotel = 1;
                    //inicioFinLoteHotelTask =new UserRegistrarInicioFinLoteHotelTask(getActivity(),idDestino);
                    //inicioFinLoteHotelTask.execute();

                    builder.dismiss();
                }
            });
            builder.show();
    }
    public void setmOnFinLotePadreListener(@NonNull OnFinLoteHotel l){mOnFinLotePadreListener =l;
    }


    private void imprimirEtiquetaFinRutaHospitalaria(){

        final Handler handler=new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                final ProgressDialog progress = ProgressDialog.show(getActivity(), "", "Imprimiendo...", true);
                progress.setCancelable(false);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Integer idSubruta = MySession.getIdSubRuta();
                        loadDataPaquetes(idSubruta);
                        List<RowItemFinRuta> ListaEnviar=listaFinRuta;
                        System.out.println("");
                        try {
                            dialog = new ProgressDialog(getActivity());
                            print = new MyPrint(getActivity());
                            print.setOnPrinterListener(new MyPrint.OnPrinterListener() {
                                @Override
                                public void onSuccessful() {
                                    //Impresion finalizada
                                    progress.dismiss();
                                    System.out.print("Compleado correctamente");
                                }
                                @Override
                                public void onFailure(String message) {
                                    progress.dismiss();
                                    messageBox(message);
                                }
                            });
                            print.printerFinRuta(idSubruta,ListaEnviar);
                        }catch (Exception e){
                            progress.dismiss();
                            messageBox("No hay conexion con la impresora");
                        }
                        System.out.println("");
                    }
                });
                Looper.loop();
            }
        }).start();
    }

    private void loadDataPaquetes(Integer idSubruta){

        listaFinRuta = new ArrayList<>();
        rowItems = MyApp.getDBO().manifiestoDao().fetchManifiestosAsigandobySubRutaImpresion(idSubruta, MySession.getIdUsuario());
        for (int i=0; i<rowItems.size();i++){
            fundas50 = 0;
            fundas63 = 0;
            paquetes1 = 0;
            paquete2 = 0;
            paquete3 = 0;
            pendienteF55x50=0;
            pendienteF63x76=0;
            pendienteFPc1=0;
            pendienteFPc2=0;
            pendienteFPc3=0;
            if(rowItems.get(i).getTipoPaquete()!=null) {
                pkg = MyApp.getDBO().paqueteDao().fechConsultaPaqueteEspecifico(rowItems.get(i).getTipoPaquete());
                manifiestoPkg = MyApp.getDBO().manifiestoPaqueteDao().fetchConsultarManifiestoPaquetebyId(rowItems.get(i).getIdAppManifiesto(), rowItems.get(i).getTipoPaquete());
                listaPaquetes = new ArrayList<>();
                if(pkg!=null && manifiestoPkg!=null) {
                    if (pkg.getEntregaSoloFundas()) listaPaquetes.add(new RowItemPaquete(pkg.getFunda(),
                            manifiestoPkg != null ? manifiestoPkg.getDatosFundas() : 0,
                            manifiestoPkg != null ? (manifiestoPkg.getDatosFundasPendientes() != null ? manifiestoPkg.getDatosFundasPendientes() : 0) : 0,
                            manifiestoPkg != null ? manifiestoPkg.getDatosFundasPendientes() : null,
                            manifiestoPkg != null ? (manifiestoPkg.getDatosFundasDiferencia() != null ? manifiestoPkg.getDatosFundasDiferencia() : 0) : 0,
                            1));

                    if (pkg.getEntregaSoloGuardianes())
                        listaPaquetes.add(new RowItemPaquete(pkg.getGuardian(),
                                manifiestoPkg != null ? manifiestoPkg.getDatosGuardianes() : 0,
                                manifiestoPkg != null ? (manifiestoPkg.getDatosGuardianesPendientes() != null ? manifiestoPkg.getDatosGuardianesPendientes() : 0) : 0,
                                manifiestoPkg != null ? manifiestoPkg.getDatosGuardianesPendientes() : null,
                                manifiestoPkg != null ? (manifiestoPkg.getDatosGuardianesDiferencia() != null ? manifiestoPkg.getDatosGuardianesDiferencia() : 0) : 0,
                                2));
                }
            }else {
                listaPaquetes=null;
            }
            if(listaPaquetes!=null && listaPaquetes.size()>0) {

                for (RowItemPaquete it : listaPaquetes) {
                    listaPaquetes.get(0);
                    if (it.getNombre().equals("55x50")) {
                        fundas50 = fundas50+ (it.getCantidad() );
                        pendienteF55x50=pendienteF55x50+it.getPendiente();
                    }
                    if (it.getNombre().equals("63x76")){
                        fundas63 = fundas63+(it.getCantidad() );
                        pendienteF63x76=pendienteF63x76+it.getPendiente();
                    }
                    if(it.getNombre().equals("PC 1")){
                        paquetes1 = paquetes1+ (it.getCantidad() );
                        pendienteFPc1=pendienteFPc1+it.getPendiente();
                    }else if(it.getNombre().equals("PC 2")) {
                        paquete2 = paquete2+(it.getCantidad() );
                        pendienteFPc2=pendienteFPc2+it.getPendiente();
                    }else if(it.getNombre().equals("PC 4")) {
                        paquete3 = paquete3+ (it.getCantidad() );
                        pendienteFPc3=pendienteFPc3+it.getPendiente();
                    }
                }
                String fecha=(new SimpleDateFormat("dd/MM/yyyy")).format(new Date());
                listaFinRuta.add(new RowItemFinRuta( fecha,rowItems.get(i).getNumero(),fundas50,fundas63,paquetes1,paquete2,paquete3,pendienteF55x50,pendienteF63x76,pendienteFPc1,pendienteFPc2,pendienteFPc3));
                System.out.println("");
            }
        }
    }
    private void bloqueoBotones(){
        btnFinApp.setEnabled(false);
        listaDestino.setEnabled(false);
        listaDestinoParticular.setEnabled(false);
    }
    private void desbloqueoBotones(){
        btnFinApp.setEnabled(true);
        listaDestino.setEnabled(true);
        listaDestinoParticular.setEnabled(true);
    }
}

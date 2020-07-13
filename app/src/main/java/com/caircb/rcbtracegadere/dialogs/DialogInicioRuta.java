package com.caircb.rcbtracegadere.dialogs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
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


import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.adapters.ListaValoresAdapter;
import com.caircb.rcbtracegadere.database.AppDatabase;
import com.caircb.rcbtracegadere.database.entity.CatalogoEntity;
import com.caircb.rcbtracegadere.database.entity.RutasEntity;
import com.caircb.rcbtracegadere.database.entity.RuteoRecoleccionEntity;
import com.caircb.rcbtracegadere.fragments.recolector.HomeTransportistaFragment;
import com.caircb.rcbtracegadere.generics.MyDialog;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.DtoRuteoRecoleccion;
import com.caircb.rcbtracegadere.models.RowRutas;
import com.caircb.rcbtracegadere.models.response.DtoCatalogo;
import com.caircb.rcbtracegadere.models.response.DtoFindRutas;
import com.caircb.rcbtracegadere.tasks.UserConsultarHojaRutaTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarPlacasInicioRutaDisponible;
import com.caircb.rcbtracegadere.tasks.UserConsultarRutasTask;
import com.caircb.rcbtracegadere.tasks.UserRegistrarInicioRutaTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.annotations.NonNull;

public class DialogInicioRuta extends MyDialog {
    EditText txtKilometraje;
    LinearLayout btnIngresarApp, btnCancelarApp;
    Activity _activity;
    Spinner spinnerPlacas;
    UserConsultarHojaRutaTask consultarHojaRutaTask;
    LinearLayout lnlIniciaRuta,lnlFinRuta;
    TextView lblListaManifiestoAsignado, lblpickUpTransportista,lblPlaca,lblTransportistaRecolector,lblAuxiliarRecoleccion1, lblTituloAuxiliarRecoleccion2,lblAuxiliarRecoleccion2,lblRuta;

    String placa;
    String placaInfoModulos;
    long idRegistro;

    UserRegistrarInicioRutaTask registroInicioRuta;
    UserConsultarRutasTask consultarPlacasInicioRutaDisponible;

    //Botones Inicio
    ImageButton btnSincManifiestos,btnListaAsignadaTransportista, btnFinRuta,regionBuscar;
    ImageView btnPickUpTransportista, btnDropOffTransportista;

    List<DtoFindRutas> listaPlacasDisponibles;
    List<RowRutas> listaRutas;
    TextView txtBuscar, txtSincronizar, txtManifiestos;

    /*
    UserConsultarPlacasInicioRutaDisponible.TaskListener listenerPlacasDisponibles= new UserConsultarPlacasInicioRutaDisponible.TaskListener() {
        @Override
        public void onFinished(List<DtoCatalogo> catalogos) {
            //combo = new ArrayList<DtoCatalogo>();
            listaPlacasDisponibles = catalogos;
            spinnerPlacas = cargarSpinnerPalca(spinnerPlacas,catalogos,true);
        }
    };
    */


    public DialogInicioRuta(@NonNull Context context ) {
        super(context, R.layout.dialog_inicio_ruta);
       // this.taskListener = listener;
        //this.bandera = bandera;
        //dbHelper = new DBAdapter(context);
        this._activity = (Activity)context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getView());

        init();
        initButton();


    }

    private void init(){

        listaPlacasDisponibles = new ArrayList<>();
        lblListaManifiestoAsignado = getActivity().findViewById(R.id.lblListaManifiestoAsignado);
        lblpickUpTransportista = getActivity().findViewById(R.id.lblpickUpTransportista);
        txtKilometraje = (EditText)getView().findViewById(R.id.txtKilometraje);

        lblPlaca = (TextView) getView().findViewById(R.id.lblPlaca);
        lblTransportistaRecolector = (TextView)getView().findViewById(R.id.lblTransportistaRecolector);
        lblAuxiliarRecoleccion1 = (TextView)getView().findViewById(R.id.lblAuxiliarRecoleccion1);
        lblTituloAuxiliarRecoleccion2 = (TextView)getView().findViewById(R.id.lblTituloAuxiliarRecoleccion2);
        lblAuxiliarRecoleccion2 = (TextView)getView().findViewById(R.id.lblAuxiliarRecoleccion2);
        lblRuta = (TextView)getView().findViewById(R.id.lblRuta);
        //txtKilometraje.setRawInputType(InputType.TYPE_CLASS_NUMBER);


        btnCancelarApp = (LinearLayout)getView().findViewById(R.id.btnIniciaRutaCancel);
        btnIngresarApp = (LinearLayout)getView().findViewById(R.id.btnIniciaRutaAplicar);

        //botones home
        btnPickUpTransportista = (ImageView) getActivity().findViewById(R.id.btnPickUpTransportista);
        btnDropOffTransportista = (ImageView) getActivity().findViewById(R.id.btnDropOffTransportista);
        regionBuscar = (ImageButton) getActivity().findViewById(R.id.regionBuscar);
        btnSincManifiestos = (ImageButton) getActivity().findViewById(R.id.btnSincManifiestos);
        btnListaAsignadaTransportista = (ImageButton) getActivity().findViewById(R.id.btnListaAsignadaTransportista);
        //btnInicioRuta = (ImageButton) getActivity().findViewById(R.id.btnInicioRuta);
        btnFinRuta = (ImageButton) getActivity().findViewById(R.id.btnFinRuta);
        //txtinicioRuta = (TextView)getActivity().findViewById(R.id.txtIniciarRuta);
        //txtFinRuta = (TextView)getActivity().findViewById(R.id.txtFinRuta);

        lnlIniciaRuta = getActivity().findViewById(R.id.LnlIniciaRuta);
        lnlFinRuta = getActivity().findViewById(R.id.LnlFinRuta);

        txtBuscar = (TextView) getActivity().findViewById(R.id.txtBuscar);
        txtSincronizar = (TextView) getActivity().findViewById(R.id.txtSincronizar);
        txtManifiestos = (TextView) getActivity().findViewById(R.id.txtManifiestos);

        spinnerPlacas = (Spinner)getView().findViewById(R.id.lista_placas);
        spinnerPlacas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    listaPlacasDisponibles.get(position-1);
                    placa = (String) spinnerPlacas.getSelectedItem();
                    lblPlaca.setText(listaPlacasDisponibles.get(position-1).getPlaca());
                    placaInfoModulos=listaPlacasDisponibles.get(position-1).getPlaca();
                    lblTransportistaRecolector.setText(listaPlacasDisponibles.get(position-1).getNombreChofer());
                    lblAuxiliarRecoleccion1.setText(listaPlacasDisponibles.get(position-1).getNombreAuxiliar());
                    if (listaPlacasDisponibles.get(position-1).getNombreConductor()!=null){
                        lblTituloAuxiliarRecoleccion2.setVisibility(View.VISIBLE);
                        lblAuxiliarRecoleccion2.setVisibility(View.VISIBLE);
                        lblAuxiliarRecoleccion2.setText(listaPlacasDisponibles.get(position-1).getNombreConductor());
                        lblRuta.setText(listaPlacasDisponibles.get(position-1).getNombreRuta());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //spinnerPlacas.setAdapter(listaValoresAdapter);

        btnCancelarApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInicioRuta.this.dismiss();
            }
        });

        datosRutasDisponibles();
       // datosPlacasDisponibles();
    }



    public void initButton(){
    btnIngresarApp.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int validar=2;
            if(txtKilometraje.getText().length()<3){
                messageBox("Se requiere que digite el kilometraje.");
                return;
            }else{
                validar--;
            }

            if (placa=="SELECCIONE"){
                mensaje("Seleccione un placa valida");
            }else{
                validar--;
            }

            if (validar ==0){
                if(spinnerPlacas.getSelectedItem().toString().equals("SELECCIONE")){
                    messageBox("Debe seleccionar una subRuta");
                }else{
                    guardarDatos();
                }
            }
        }
    });


    }

    public void mensaje (String mensaje){
        messageBox(mensaje);
    }

   private void datosRutasDisponibles(){
        consultarPlacasInicioRutaDisponible = new UserConsultarRutasTask(getActivity());
        consultarPlacasInicioRutaDisponible.setOnVehiculoListener(new UserConsultarRutasTask.OnPlacaListener() {
            @Override
            public void onSuccessful(List<DtoFindRutas> catalogos) {
                listaPlacasDisponibles = catalogos;
                spinnerPlacas = cargarSpinnerRuta(spinnerPlacas,catalogos,true);
            }
        });
        consultarPlacasInicioRutaDisponible.execute();
    }



   /* public Spinner cargarSpinnerPalca(Spinner spinner, List<DtoCatalogo> catalogos, boolean bhabilitar){

        ArrayAdapter<String> adapter;
        Spinner defaulSpiner = spinner;
        ArrayList<String> listaData = new ArrayList<String>();

        listaData.add("SELECCIONE");
        if(catalogos.size() > 0){
            for (DtoCatalogo r : catalogos){
                listaData.add(r.getCodigo());
            }
        }

        adapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_spinner, listaData);
        defaulSpiner.setAdapter(adapter);
        defaulSpiner.setEnabled(bhabilitar);

        return defaulSpiner;
    }*/
    public Spinner cargarSpinnerRuta(Spinner spinner, List<DtoFindRutas> catalogos, boolean bhabilitar){

        ArrayAdapter<String> adapter;
        Spinner defaulSpiner = spinner;
        ArrayList<String> listaData = new ArrayList<String>();

        //listaRutas = MyApp.getDBO().rutasDao().fetchConsultarRutas();
        listaData.add("SELECCIONE");
        if(catalogos.size() > 0){
            for (DtoFindRutas r : catalogos){
                listaData.add(r.getNombreRuta());
            }
        }

        adapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_spinner, listaData);
        defaulSpiner.setAdapter(adapter);
        defaulSpiner.setEnabled(bhabilitar);

        return defaulSpiner;
    }



    private void guardarDatos(){

        String kilometrajeInicio = txtKilometraje.getText().toString();


        //CatalogoEntity c = MyApp.getDBO().catalogoDao().fetchConsultarCatalogoId(placa,4);
        RutasEntity r = MyApp.getDBO().rutasDao().fetchConsultarId(placa);
        int idVehiculo = r!=null?r.getCodigo():-1;

        Integer idUsuario = MySession.getIdUsuario();
        Date fechaInicio = AppDatabase.getDateTime();
        idRegistro =  MyApp.getDBO().rutaInicioFinDao().saveOrUpdateInicioRuta(1, MySession.getIdUsuario(),idVehiculo,fechaInicio,null,kilometrajeInicio,null,1,placaInfoModulos);
        MyApp.getDBO().parametroDao().saveOrUpdate("current_ruta",""+idVehiculo);
        MyApp.getDBO().parametroDao().saveOrUpdate("current_placa_transportista",""+placaInfoModulos);
        //MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_placa_transportista");

        //EMPIEZA RUTEO RECOLECCION
        MyApp.getDBO().parametroDao().saveOrUpdate("ruteoRecoleccion", "SI");
        Integer idSubRuta = Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_ruta").getValor());
        MySession.setIdSubruta(idSubRuta);
        MyApp.getDBO().ruteoRecoleccion().saverOrUpdate(new DtoRuteoRecoleccion(MySession.getIdSubRuta(), fechaInicio, 0, null, null, false));
        List<RuteoRecoleccionEntity> enty = MyApp.getDBO().ruteoRecoleccion().searchRuteoRecoleccion();

        //notificar inicia ruta al servidor...
        registroInicioRuta = new UserRegistrarInicioRutaTask(getActivity(),idRegistro);
        registroInicioRuta.setOnIniciaRutaListener(new UserRegistrarInicioRutaTask.OnIniciaRutaListener() {
            @Override
            public void onSuccessful() {
                DialogInicioRuta.this.dismiss();
            }

            @Override
            public void onFailure(int error) {
                mensaje("error "+String.valueOf(error)+" al registrar inicio ruta en el servidor datos registrados en la base de datos local");
                DialogInicioRuta.this.dismiss();
            }
        });
        registroInicioRuta.execute();

        inicioRuta();

    }
    UserConsultarHojaRutaTask.TaskListener listenerHojaRuta = new UserConsultarHojaRutaTask.TaskListener() {
        @Override
        public void onSuccessful() {
            loadCantidadManifiestoAsignado();
            loadCantidadManifiestoProcesado();
        }
    };
    private void loadCantidadManifiestoAsignado(){
        //dbHelper.open();
        lblListaManifiestoAsignado.setText(""+ MyApp.getDBO().manifiestoDao().contarHojaRutaAsignadas());
        //dbHelper.close();
    }

    private void loadCantidadManifiestoProcesado(){
        //dbHelper.open();
        lblpickUpTransportista.setText(""+ MyApp.getDBO().manifiestoDao().contarHojaRutaAsignadasP(MySession.getIdUsuario()));
        //dbHelper.close();
    }

    private void inicioRuta(){

        regionBuscar.setEnabled(true);
        btnSincManifiestos.setEnabled(true);
        btnListaAsignadaTransportista.setEnabled(true);
        btnPickUpTransportista.setEnabled(true);
        btnDropOffTransportista.setEnabled(true);

        regionBuscar.setColorFilter(Color.TRANSPARENT);
        btnSincManifiestos.setColorFilter(Color.TRANSPARENT);
        btnListaAsignadaTransportista.setColorFilter(Color.TRANSPARENT);

        txtBuscar.setTextColor(Color.WHITE);
        txtManifiestos.setTextColor(Color.WHITE);
        txtSincronizar.setTextColor(Color.WHITE);

        lnlIniciaRuta.setVisibility(View.GONE);
        lnlFinRuta.setVisibility(View.VISIBLE);
        consultarHojaRutaTask = new UserConsultarHojaRutaTask(_activity,listenerHojaRuta);
        consultarHojaRutaTask.execute();

    }
}

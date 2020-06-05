package com.caircb.rcbtracegadere.dialogs;

import android.app.Activity;
import android.content.Context;
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
import com.caircb.rcbtracegadere.generics.MyDialog;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.response.DtoCatalogo;
import com.caircb.rcbtracegadere.tasks.UserConsultarPlacasInicioRutaDisponible;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.annotations.NonNull;

public class DialogInicioRuta extends MyDialog {
    EditText txtKilometraje;
    LinearLayout btnIngresarApp, btnCancelarApp;
    Activity _activity;
    Spinner spinnerPlacas;

    LinearLayout lnlIniciaRuta,lnlFinRuta;

    String placa;

    Boolean exito;

    ListaValoresAdapter listaValoresAdapter;
    //UserRegistroInicioFinTask registroInicio;
    UserConsultarPlacasInicioRutaDisponible consultarPlacasInicioRutaDisponible;

    //Botones Inicio
    ImageButton btnSincManifiestos,btnListaAsignadaTransportista, btnInicioRuta, btnFinRuta,regionBuscar;
    ImageView btnPickUpTransportista, btnDropOffTransportista;
    TextView txtinicioRuta, txtFinRuta;


    List<DtoCatalogo> listaPlacasDisponibles;

    /*public interface TaskListener {
        public void onBandera();
    }

    private final TaskListener taskListener;
*/

    UserConsultarPlacasInicioRutaDisponible.TaskListener listenerPlacasDisponibles= new UserConsultarPlacasInicioRutaDisponible.TaskListener() {
        @Override
        public void onFinished(List<DtoCatalogo> catalogos) {
            //combo = new ArrayList<DtoCatalogo>();
            listaPlacasDisponibles = catalogos;
            spinnerPlacas = cargarSpinnerPalca(spinnerPlacas,catalogos,true);
        }
    };

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

        txtKilometraje = (EditText)getView().findViewById(R.id.txtKilometraje);
        txtKilometraje.setRawInputType(InputType.TYPE_CLASS_NUMBER);

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

        spinnerPlacas = (Spinner)getView().findViewById(R.id.lista_placas);
        spinnerPlacas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    listaPlacasDisponibles.get(position);
                    placa = (String) spinnerPlacas.getSelectedItem();
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
        datosPlacasDisponibles();
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

            if (placa=="seleccione"){
                mensaje("Seleccione un placa valida");
            }else{
                validar--;
            }

            if (validar ==0){
                //
                guardarDatos();

            }

        }


    });


    }

    public void mensaje (String mensaje){
        messageBox(mensaje);
    }

    private void datosPlacasDisponibles(){
        consultarPlacasInicioRutaDisponible = new UserConsultarPlacasInicioRutaDisponible(getActivity(),listenerPlacasDisponibles);
        consultarPlacasInicioRutaDisponible.execute();
    }


    public Spinner cargarSpinnerPalca(Spinner spinner, List<DtoCatalogo> catalogos, boolean bhabilitar){

        ArrayAdapter<String> adapter;
        Spinner defaulSpiner = spinner;
        ArrayList<String> listaData = new ArrayList<String>();

        listaData.add("Seleccione...");
        if(catalogos.size() > 0){
            for (DtoCatalogo r : catalogos){
                listaData.add(r.getCodigo());
            }
        }

        adapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_spinner, listaData);
        defaulSpiner.setAdapter(adapter);
        defaulSpiner.setEnabled(bhabilitar);

        return defaulSpiner;
    }

    /*
    private void datosPlacas(){
        combo = new ArrayList<String>();
        combo.add("Seleccione");
        //String placaC;

        //CatalogoItemValor cat;

        List<String> catalogos = MyApp.getDBO().catalogoDao().fetchConsultarNombresCatalogosbyTipo(4);
        if(catalogos.size()>0)combo.addAll(catalogos);

        ArrayAdapter<CharSequence>adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item,combo);
        spinnerPlacas.setAdapter(adapter);

        spinnerPlacas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                placa = (String) spinnerPlacas.getSelectedItem();
                //int placaFinal = Integer.parseInt(placa);
                //Toast.makeText(getContext(),placa,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    */
    private void guardarDatos(){

        String kilometrajeInicio = txtKilometraje.getText().toString();


        CatalogoEntity c = MyApp.getDBO().catalogoDao().fetchConsultarCatalogoId(placa,4);
        int placaInicio = c!=null?c.getIdSistema():-1;

        Date dia;
        dia = AppDatabase.getDateTime();

        MyApp.getDBO().rutaInicioFinDao().saveOrUpdateInicioRuta(1, MySession.getIdUsuario(),placaInicio,dia,null,kilometrajeInicio,null,1);

        inicioRuta();
        DialogInicioRuta.this.dismiss();
        /*
        registroInicio = new UserRegistroInicioFinTask(getActivity(),listener,kilometrajeInicio,placaInicio,1);
        registroInicio.execute();*/
    }

   /* UserRegistroInicioFinTas.TaskListener listener = new UserRegistroInicioFinTask.TaskListener() {
        @Override
        public void onFinished() {

            inicioRuta();
            DialogInicioRuta.this.dismiss();
        }
    };*/

    private void inicioRuta(){

        regionBuscar.setEnabled(true);
        btnSincManifiestos.setEnabled(true);
        btnListaAsignadaTransportista.setEnabled(true);
        btnPickUpTransportista.setEnabled(true);
        btnDropOffTransportista.setEnabled(true);
        lnlIniciaRuta.setVisibility(View.GONE);
        lnlFinRuta.setVisibility(View.VISIBLE);

    }


}

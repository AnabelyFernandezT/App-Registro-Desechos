package com.caircb.rcbtracegadere.dialogs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.AppDatabase;
import com.caircb.rcbtracegadere.database.entity.CatalogoEntity;
import com.caircb.rcbtracegadere.database.entity.RutaInicioFinEntity;
import com.caircb.rcbtracegadere.generics.MyDialog;
import com.caircb.rcbtracegadere.helpers.MySession;

import java.util.Date;

public class DialogFinRuta extends MyDialog {

    TextView txt_placa, txt_kilometraje_inicio;
    LinearLayout btnFinApp, btnCancelarApp;
    Activity _activity;
    Boolean bandera;
    EditText kilometrajeFinal;

    LinearLayout lnlIniciaRuta,lnlFinRuta;

    String kilometrajeInicio;
    Integer placaInicio;
    //UserRegistroInicioFinTask registroFin;
    Date diaAnterior;

    //Botones Inicio
    ImageButton btnSincManifiestos,btnListaAsignadaTransportista, btnInicioRuta, btnFinRuta,regionBuscar;
    ImageView btnPickUpTransportista, btnDropOffTransportista;
    TextView txtinicioRuta, txtFinRuta;
/*
    public interface TaskListener {
        public void onBandera(Boolean bandera);
    }

    private final DialogFinRuta.TaskListener taskListener;*/

    public DialogFinRuta(@NonNull Context context) {
        super(context, R.layout.dialog_final_ruta);
       // this.taskListener = listenerBanderaFin;
        this.bandera = bandera;
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
        txt_placa = (TextView)getView().findViewById(R.id.Txt_placa);
        txt_kilometraje_inicio = (TextView)getView().findViewById(R.id.txt_kilometraje_inicio);

        btnFinApp = (LinearLayout)getView().findViewById(R.id.btnFinalizarRuta);
        btnCancelarApp = (LinearLayout)getView().findViewById(R.id.btnCancelarApp);

        //botones home
        btnPickUpTransportista = (ImageView) getActivity().findViewById(R.id.btnPickUpTransportista);
        btnDropOffTransportista = (ImageView) getActivity().findViewById(R.id.btnDropOffTransportista);
        regionBuscar = (ImageButton) getActivity().findViewById(R.id.regionBuscar);
        btnSincManifiestos = (ImageButton) getActivity().findViewById(R.id.btnSincManifiestos);
        btnListaAsignadaTransportista = (ImageButton) getActivity().findViewById(R.id.btnListaAsignadaTransportista);
        //btnInicioRuta = (ImageButton) getActivity().findViewById(R.id.btnInicioRuta);
        //btnFinRuta = (ImageButton) getActivity().findViewById(R.id.btnFinRuta);
        //txtinicioRuta = (TextView)getActivity().findViewById(R.id.txtIniciarRuta);

        //txtFinRuta = (TextView)getActivity().findViewById(R.id.txtFinRuta);
        lnlIniciaRuta = getActivity().findViewById(R.id.LnlIniciaRuta);
        lnlFinRuta = getActivity().findViewById(R.id.LnlFinRuta);

        kilometrajeFinal = (EditText)getView().findViewById(R.id.kilometrajeFinal) ;
        kilometrajeFinal.setRawInputType(InputType.TYPE_CLASS_NUMBER);

        btnCancelarApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFinRuta.this.dismiss();
            }
        });
        traerDatosAnteriores();
    }

    public void initButton(){
        btnFinApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bandera = true;
                if (kilometrajeFinal.getText().length()<0){
                    messageBox("Se requiere que digite el kilometraje.");
                    return;
                }else{
                    if (Double.parseDouble(kilometrajeInicio)> Double.parseDouble(kilometrajeFinal.getText().toString()))
                    {
                        messageBox("Kilometraje incorrecto");
                        return;
                    }else{
                        guardarDatos();
                    }

                }

            }
        });



    }
    /*
    UserRegistroInicioFinTask.TaskListener listener = new UserRegistroInicioFinTask.TaskListener() {

        @Override
        public void onFinished() {
            finRuta();
            DialogFinRuta.this.dismiss();

        }
    };
    */


    private void guardarDatos()
    {

        Date dia;
        dia = AppDatabase.getDateTime();

       MyApp.getDBO().rutaInicioFinDao().saveOrUpdateInicioRuta(1, MySession.getIdUsuario(),placaInicio,diaAnterior,dia,kilometrajeInicio,kilometrajeFinal.getText().toString(),2);

        finRuta();
        DialogFinRuta.this.dismiss();


    }

    private void traerDatosAnteriores(){
        RutaInicioFinEntity rut = MyApp.getDBO().rutaInicioFinDao().fechConsultaInicioFinRutasE(MySession.getIdUsuario());
        if(rut!=null){
            kilometrajeInicio = rut.getKilometrajeInicio();
            placaInicio = rut.getIdTransporteVehiculo();
            diaAnterior = rut.getFechaInicio();
        }

        //traer placa
        CatalogoEntity c = MyApp.getDBO().catalogoDao().fetchConsultarCatalogoEspecifico(placaInicio,4);
        String placa = c!=null?c.getCodigo():"";

        txt_kilometraje_inicio.setText(kilometrajeInicio);
        txt_placa.setText(placa);

    }

    private void finRuta(){

        regionBuscar.setEnabled(false);
        btnSincManifiestos.setEnabled(false);
        btnListaAsignadaTransportista.setEnabled(false);
        btnPickUpTransportista.setEnabled(false);
        btnDropOffTransportista.setEnabled(false);
        lnlIniciaRuta.setVisibility(View.VISIBLE);
        lnlFinRuta.setVisibility(View.GONE);


    }


}

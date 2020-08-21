package com.caircb.rcbtracegadere.dialogs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.generics.MyDialog;
import com.caircb.rcbtracegadere.models.response.DtoCatalogo;
import com.caircb.rcbtracegadere.tasks.UserConsultarPlacasInicioLoteTask;
import com.caircb.rcbtracegadere.tasks.UserRegistarFinLoteTask;

import java.util.ArrayList;
import java.util.List;

public class DialogConfirmarCierreLote extends MyDialog {

    Activity activity;
    TextView lblNumeroLote;
    LinearLayout  btnIngresarApp, btnCancelarApp;
    Spinner spinnerPlacas;
    List<DtoCatalogo> listaPlacasDisponibles;
    String placa;
    UserConsultarPlacasInicioLoteTask placasInicioLoteTask;
    UserRegistarFinLoteTask registarFinLoteTask;
    Integer idTrasposteVehiLote=0;

    public interface onRegisterCerrarLoteTask{
        public void onSuccessfull();
    }
    public onRegisterCerrarLoteTask mOnRegisterCerrarLoteTask;

    public DialogConfirmarCierreLote (@NonNull Context context){
        super(context, R.layout.dialog_confirmar_cierre_lote);
        this.activity = (Activity)context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getView());
        initBotones();
    }

    private void initBotones(){
        btnCancelarApp = (LinearLayout)getView().findViewById(R.id.btnIniciaRutaCancel);
        btnIngresarApp = (LinearLayout)getView().findViewById(R.id.btnIniciaRutaAplicar);
        lblNumeroLote = (TextView)getView().findViewById(R.id.lblNumeroLote);

        lblNumeroLote.setText(String.valueOf(MyApp.getDBO().parametroDao().fecthParametroValor("current_inicio_lote")));

        spinnerPlacas = (Spinner)getView().findViewById(R.id.lista_placas);


        spinnerPlacas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position>0){
                    listaPlacasDisponibles.get(position-1);
                    placa = (String) spinnerPlacas.getSelectedItem();
                }else{
                    spinnerPlacas.setSelection(obtenerPosicionItem(spinnerPlacas,MyApp.getDBO().parametroDao().fecthParametroValor("current_placa_lote") ));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });


        btnCancelarApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnIngresarApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DialogBuilder dialogBuilder = new DialogBuilder(getActivity());
                dialogBuilder.setCancelable(false);
                dialogBuilder.setMessage("¿Esta seguro de continuar ?");
                dialogBuilder.setPositiveButton("SI", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                            registarFinLoteTask = new UserRegistarFinLoteTask(getActivity(),idTrasposteVehiLote);
                            registarFinLoteTask.setOnRegisterListener(new UserRegistarFinLoteTask.OnRegisterListener() {
                                @Override
                                public void onSuccessful(String numLote) {
                                    MyApp.getDBO().parametroDao().saveOrUpdate("loteBandera_sedes","false");

                                    messageBox("Lote # " + numLote + " se ha cerrado correctamente.");
                                    MyApp.getDBO().parametroDao().saveOrUpdate("current_placa_lote","-10");
                                    MyApp.getDBO().parametroDao().saveOrUpdate("estado_transporte","false");

                                    MyApp.getDBO().manifiestoSedeDao().eliminarManifiestos();
                                    MyApp.getDBO().manifiestoDetalleSede().eliminarDetalle();
                                    MyApp.getDBO().manifiestoDetalleValorSede().eliminarDetalle();

                                    if(mOnRegisterCerrarLoteTask!=null){mOnRegisterCerrarLoteTask.onSuccessfull();}
                                }

                                @Override
                                public void onFail() {
                                    messageBox("Lote no finalizado");
                                }
                            });
                            registarFinLoteTask.execute();
                        dialogBuilder.dismiss();
                    }
                });
                dialogBuilder.setNegativeButton("NO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                    }
                });
                dialogBuilder.show();
                //Verificar si tiene la misma placa

            }
        });

        datosPlacasDisponibles();
        //actualPlacaLote();
        //spinnerPlacas.setSelection(obtenerPosicionItem(spinnerPlacas,MyApp.getDBO().parametroDao().fecthParametroValor("current_placa_lote") ));
    }


    public static int obtenerPosicionItem(Spinner spinner, String nombre) {
        int posicion = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(nombre)) {
                posicion = i;
            }
        }
        return posicion;
    }

    private void datosPlacasDisponibles(){
        placasInicioLoteTask = new UserConsultarPlacasInicioLoteTask(getActivity());
        placasInicioLoteTask.setOnVehiculoListener(new UserConsultarPlacasInicioLoteTask.OnVehiculoListener() {
            @Override
            public void onSuccessful(List<DtoCatalogo> catalogos) {
                listaPlacasDisponibles = catalogos;
                spinnerPlacas = cargarSpinnerPalca(spinnerPlacas,catalogos,true);
            }
        });
        placasInicioLoteTask.execute();
    }

    public Spinner cargarSpinnerPalca(Spinner spinner, List<DtoCatalogo> catalogos, boolean bhabilitar){

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
    }

    public void setmOnRegisterCerrarLoteTask (@Nullable onRegisterCerrarLoteTask l){ mOnRegisterCerrarLoteTask = l;}
}

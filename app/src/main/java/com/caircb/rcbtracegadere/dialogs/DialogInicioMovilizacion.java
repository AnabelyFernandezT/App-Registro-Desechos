package com.caircb.rcbtracegadere.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.entity.CatalogoEntity;
import com.caircb.rcbtracegadere.generics.MyDialog;
import com.caircb.rcbtracegadere.models.response.DtoCatalogo;
import com.caircb.rcbtracegadere.tasks.UserConsultarConductoresTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarDestinosTask;
import com.caircb.rcbtracegadere.tasks.UserDestinoEspecificoTask;
import com.caircb.rcbtracegadere.tasks.UserRegistrarLoteSedeTask;

import java.util.ArrayList;
import java.util.List;


public class DialogInicioMovilizacion extends MyDialog {
    Activity _activity;
    Spinner listaConductor,listaDestino,listaDestinoParticular, listaOperador, listaOperadorAuxiliar;
    LinearLayout btnCancelar,btnMovilizar;
    List<DtoCatalogo> listaDestinos, destinosEspecificos, lstConductor, lstOperador,lstOperadorAuxiliar;
    UserConsultarDestinosTask consultarDetino;
    UserDestinoEspecificoTask consultaDestinoEspecifico;
    UserConsultarConductoresTask conductoresTask;
    String destino = "", destinos="",conductor,operador,operadorAuxiliar;
    UserRegistrarLoteSedeTask registrarLoteSedeTask;
    Integer idLoteContenedor;
    boolean cancel =false;

    public interface onRegisterMOvilizacionListenner{
        public void onSuccessful();
    }

    public onRegisterMOvilizacionListenner mOnRegisterMovilizacionListener;

    public DialogInicioMovilizacion(@NonNull Context context, Integer idLoteContenedor) {
        super(context, R.layout.dialog_movilizar_lote_sede);
        this._activity = (Activity)context;
        this.idLoteContenedor = idLoteContenedor;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getView());
        init();
    }

    private void init() {
        listaConductor = getView().findViewById(R.id.lista_conductor);
        listaOperador = getView().findViewById(R.id.listaOperador);
        listaOperadorAuxiliar = getView().findViewById(R.id.listaOperadorAuxiliar);
        listaDestino = getView().findViewById(R.id.lista_destino);
        listaDestinoParticular = getView().findViewById(R.id.lista_destino_particular);
        btnCancelar = getView().findViewById(R.id.btnCancelarApp);
        btnMovilizar = getView().findViewById(R.id.btnMovilizarLote);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInicioMovilizacion.this.dismiss();
            }
        });

        btnMovilizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogBuilder dialogBuilder = new DialogBuilder(getActivity());
                dialogBuilder.setCancelable(false);
                dialogBuilder.setMessage("¿Esta seguro de movilizar el lote?");
                dialogBuilder.setPositiveButton("SI", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                        cancel = false;
                        validacionesInfo();

                        if(!cancel){
                            guardar();
                        }
                    }
                });
                dialogBuilder.setNegativeButton("NO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                    }
                });
                dialogBuilder.show();
            }
        });
        listaConductor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    lstConductor.get(position-1);
                    conductor = (String) listaConductor.getSelectedItem();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        listaDestinoParticular.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    destinosEspecificos.get(position-1);
                    destino = (String) listaDestinoParticular.getSelectedItem();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        listaOperador.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    lstOperador.get(position-1);
                    operador = (String)listaOperador.getSelectedItem();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        listaOperadorAuxiliar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    lstOperadorAuxiliar.get(position-1);
                    operadorAuxiliar = (String)listaOperadorAuxiliar.getSelectedItem();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        traerDestinos();
        traerConductores();
        traerOperador();
        traerOperadorAuxiliar();
    }

    private void validacionesInfo(){
        if(listaConductor.getSelectedItem().toString().equals("SELECCIONE")){
            cancel = true;
            messageBox("Debe seleccionar un conductor");
            return;
        }else if(listaDestino.getSelectedItem().toString().equals("SELECCIONE")){
            cancel = true;
            messageBox("Debe seleccionar un destino");
            return;
        }else if(listaDestinoParticular.getSelectedItem().toString().equals("SELECCIONE")){
            cancel = true;
            messageBox("Debe seleccionar un destino de llegada");
            return;
        }
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

    private void traerConductores(){
        conductoresTask = new UserConsultarConductoresTask(getActivity());
        conductoresTask.setOnConductorListener(new UserConsultarConductoresTask.OnConductorListener() {
            @Override
            public void onSuccessful(List<DtoCatalogo> catalogos) {
                lstConductor = catalogos;
                listaConductor = cargarSpinnerDestino(listaConductor,catalogos,true);
            }
        });
        conductoresTask.execute();
    }
    private void traerOperador(){
        conductoresTask = new UserConsultarConductoresTask(getActivity());
        conductoresTask.setOnConductorListener(new UserConsultarConductoresTask.OnConductorListener() {
            @Override
            public void onSuccessful(List<DtoCatalogo> catalogos) {
                lstOperador = catalogos;
                listaOperador = cargarSpinnerDestino(listaOperador,catalogos,true);
            }
        });
        conductoresTask.execute();
    }
    private void traerOperadorAuxiliar(){
        conductoresTask = new UserConsultarConductoresTask(getActivity());
        conductoresTask.setOnConductorListener(new UserConsultarConductoresTask.OnConductorListener() {
            @Override
            public void onSuccessful(List<DtoCatalogo> catalogos) {
                lstOperadorAuxiliar = catalogos;
                listaOperadorAuxiliar = cargarSpinnerDestino(listaOperadorAuxiliar,catalogos,true);
            }
        });
        conductoresTask.execute();
    }

    private void guardar(){

        CatalogoEntity c = MyApp.getDBO().catalogoDao().fetchConsultarCatalogo(conductor,14);
        int idConductor = c!=null?c.getIdSistema():-1;

        CatalogoEntity cat = MyApp.getDBO().catalogoDao().fetchConsultarCatalogo(operador,14);
        int idOperador = cat!=null?cat.getIdSistema():-1;

        CatalogoEntity ca = MyApp.getDBO().catalogoDao().fetchConsultarCatalogo(operadorAuxiliar,14);
        int idOperadorAuxiliar = ca!=null?ca.getIdSistema():-1;

        CatalogoEntity ce = MyApp.getDBO().catalogoDao().fetchConsultarCatalogo(destino,12);
        int idDestino = ce!=null?ce.getIdSistema():-1;

        registrarLoteSedeTask = new UserRegistrarLoteSedeTask(getActivity(),idDestino,idConductor,idOperador,idOperadorAuxiliar,idLoteContenedor);
        registrarLoteSedeTask.setOnRegisterListener(new UserRegistrarLoteSedeTask.OnRegisterListener() {
            @Override
            public void onSuccessful() {

                MyApp.getDBO().loteDao().updataMovilizado(idLoteContenedor);
                messageBox("Datos enviados a: " + destino);
                if(mOnRegisterMovilizacionListener != null)mOnRegisterMovilizacionListener.onSuccessful();
                DialogInicioMovilizacion.this.dismiss();

            }
            @Override
            public void onFail() {
                messageBox("Datos no enviados a: " + destino);
                DialogInicioMovilizacion.this.dismiss();
            }
        });
        registrarLoteSedeTask.execute();

    }

    public void setOnRegisterMovilizarListener(@Nullable onRegisterMOvilizacionListenner l){ mOnRegisterMovilizacionListener = l;}
}

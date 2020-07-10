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

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.entity.CatalogoEntity;
import com.caircb.rcbtracegadere.database.entity.ParametroEntity;
import com.caircb.rcbtracegadere.fragments.planta.HojaRutaAsignadaFragmentNO;
import com.caircb.rcbtracegadere.fragments.planta.HojaRutaAsignadaPlantaFragment;
import com.caircb.rcbtracegadere.generics.MyDialog;
import com.caircb.rcbtracegadere.models.response.DtoCatalogo;
import com.caircb.rcbtracegadere.tasks.UserConsultarHojaRutaPlacaTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarManifiestosPlantaTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarVehiculosSedeTask;

import java.util.ArrayList;
import java.util.List;

public class DialogPlacas extends MyDialog {
    Activity _activity;
    Spinner spinnerPlacas;
    List<DtoCatalogo> listaPlacasDisponibles;
    String placa;
    Integer idPlaca;
    //UserConsultarPlacasInicioRutaDisponible consultarPlacasInicioRutaDisponible;
    LinearLayout btnIngresarApp, btnCancelarApp;
    UserConsultarHojaRutaPlacaTask consultarHojaRutaTask;
    TextView lblListaManifiestoAsignado;
    DialogBuilder builder;
    UserConsultarVehiculosSedeTask consultarVehiculos;
    UserConsultarManifiestosPlantaTask consultarManifiestosPlanta;

    public DialogPlacas(@NonNull Context context) {
        super(context, R.layout.dialog_spinner);
        this._activity = (Activity)context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getView());

        init();
    }

    private void init() {
        listaPlacasDisponibles = new ArrayList<>();
        lblListaManifiestoAsignado = getActivity().findViewById(R.id.lblListaManifiestoAsignadoPlanta);
        btnCancelarApp = (LinearLayout)getView().findViewById(R.id.btnIniciaRutaCancel);
        btnIngresarApp = (LinearLayout)getView().findViewById(R.id.btnIniciaRutaAplicar);

        spinnerPlacas = (Spinner)getView().findViewById(R.id.lista_placas);
        spinnerPlacas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    listaPlacasDisponibles.get(position-1);
                    placa = (String) spinnerPlacas.getSelectedItem();
                    System.out.println(placa+"placa");
                    idPlaca = spinnerPlacas.getId();
                    CatalogoEntity c = MyApp.getDBO().catalogoDao().fetchConsultarCatalogoId(placa,4);
                    int idVehiculo = c!=null?c.getIdSistema():-1;
                    MyApp.getDBO().parametroDao().saveOrUpdate("current_vehiculo",""+idVehiculo);
                    MyApp.getDBO().parametroDao().saveOrUpdate("vehiculo_planta",""+idPlaca);
                    MyApp.getDBO().parametroDao().saveOrUpdate("current_placa_transportista",""+placa);//Placa para consulta de información modulos
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnCancelarApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPlacas.this.dismiss();

            }
        });
        btnIngresarApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParametroEntity parametro = MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_vehiculo");
                String valor = parametro == null ? "-1" : parametro.getValor();
                Integer idVehiculo = Integer.parseInt(valor.equals("null") ? "-1" : valor);
                String bandera = MyApp.getDBO().parametroDao().fecthParametroValor("vehiculo_planta" + idVehiculo);
                if (bandera != null)
                {
                    if (bandera.equals("1")) {
                        consultarManifiestosPlanta = new UserConsultarManifiestosPlantaTask(getActivity());
                        consultarManifiestosPlanta.execute();
                        dismiss();
                    } else if (bandera.equals("2")) {
                        cargarManifiesto();
                        dismiss();
                    } else if (bandera.equals("0")) {
                        dialogoConfirmacion();
                    }
                //dismiss();
            }
            }
        });

        datosPlacasDisponibles();
    }


    private void datosPlacasDisponibles(){
        consultarVehiculos = new UserConsultarVehiculosSedeTask(getActivity());
        consultarVehiculos.setOnVehiculoListener(new UserConsultarVehiculosSedeTask.OnVehiculoListener() {
            @Override
            public void onSuccessful(List<DtoCatalogo> catalogos) {
                listaPlacasDisponibles = catalogos;
                spinnerPlacas = cargarSpinnerPalca(spinnerPlacas,catalogos,true);
            }
        });
        consultarVehiculos.execute();
    }

    private void dialogoConfirmacion(){
        builder = new DialogBuilder(getContext());
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
                consultarManifiestosPlanta = new UserConsultarManifiestosPlantaTask(getActivity());
                consultarManifiestosPlanta.execute();
                builder.dismiss();
                dismiss();
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
                cargarManifiesto();
                //consultarManifiestosPlanta = new UserConsultarManifiestosPlantaTask(getActivity());
                //consultarManifiestosPlanta.execute();
                builder.dismiss();
                dismiss();
            }
        });
        builder.show();
        cargarLabelCantidad();
    }

    UserConsultarHojaRutaPlacaTask.TaskListener listenerHojaRuta = new UserConsultarHojaRutaPlacaTask.TaskListener() {
        @Override
        public void onSuccessful() {
            loadCantidadManifiestoAsignado();
        }
    };

    private void cargarManifiesto(){
        consultarHojaRutaTask = new UserConsultarHojaRutaPlacaTask(_activity,listenerHojaRuta);
        consultarHojaRutaTask.execute();
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

    /*private void loadCantidadManifiestoAsignado() {
        lblListaManifiestoAsignado.setText(""+ MyApp.getDBO().manifiestoPlantaDao().contarHojaRutaProcesada());
    }*/


    private void cargarLabelCantidad(){
        ParametroEntity parametro = MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_vehiculo");
        String valor = parametro == null ? "-1" : parametro.getValor();
        Integer idVehiculo = Integer.parseInt(valor.equals("null") ? "-1":valor);

        String bandera = MyApp.getDBO().parametroDao().fecthParametroValor("vehiculo_planta"+idVehiculo);

        if(bandera.equals("1")){
            loadCantidadManifiestoAsignado();
        }else if (bandera.equals("2")){
            loadCantidadManifiestoAsignadoNO();
        }
    }

    private void loadCantidadManifiestoAsignado() {
        lblListaManifiestoAsignado.setText(""+ MyApp.getDBO().manifiestoPlantaDao().contarHojaRutaProcesada());
    }


    private void loadCantidadManifiestoAsignadoNO() {
        lblListaManifiestoAsignado.setText(""+ MyApp.getDBO().manifiestoDao().contarHojaRutaProcesadaPlanta());
    }




}

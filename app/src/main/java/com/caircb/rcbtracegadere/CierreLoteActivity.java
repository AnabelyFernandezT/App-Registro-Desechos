package com.caircb.rcbtracegadere;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;


import com.caircb.rcbtracegadere.database.entity.CatalogoEntity;
import com.caircb.rcbtracegadere.database.entity.HotelLotePadreEntity;
import com.caircb.rcbtracegadere.database.entity.ParametroEntity;
import com.caircb.rcbtracegadere.database.entity.RutaInicioFinEntity;
import com.caircb.rcbtracegadere.database.entity.RutasEntity;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.fragments.recolector.HomeTransportistaFragment;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.response.DtoCatalogo;
import com.caircb.rcbtracegadere.tasks.UserConsultarDestinosTask;
import com.caircb.rcbtracegadere.tasks.UserDestinoEspecificoTask;
import com.caircb.rcbtracegadere.tasks.UserRegistrarFinLoteHospitalesTask;

import java.util.ArrayList;
import java.util.List;

public class CierreLoteActivity extends AppCompatActivity {

    Activity _activity;
    Spinner listaDestino, listaDestinoParticular;
    LinearLayout btnFinApp, btnCancelarApp, btnCerrarDialog;
    TextView txt_placa;
    UserConsultarDestinosTask consultarDetino;
    UserDestinoEspecificoTask consultaDestinoEspecifico;
    UserRegistrarFinLoteHospitalesTask userRegistrarFinLoteHospitales;
    HomeTransportistaFragment homeTransportistaFragment;

    DialogBuilder builder;
    HotelLotePadreEntity lotePadre;
    List<DtoCatalogo> listaDestinos, destinosEspecificos;
    String destino = "", destinos = "", inicioHotel;
    int idDestino;
    Integer finHotel = 3, idRuta;
    ParametroEntity parametro;
    Integer idSubtura;

    public Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_fin_lote_hospitales);
        this._activity = this;
        this.setFinishOnTouchOutside(false);
        init();


    }

    private void init() {
        btnFinApp = (LinearLayout) findViewById(R.id.btnFinalizarLoteHospital);
        btnFinApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (destinos.equals("")) {
                    final DialogBuilder dialogBuilder;
                    dialogBuilder = new DialogBuilder(_activity);
                    dialogBuilder.setMessage("Debe seleccionar el destino");
                    dialogBuilder.setCancelable(false);
                    dialogBuilder.setPositiveButton("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismiss();
                        }
                    });
                    dialogBuilder.show();
                    //message("Seleccione Destino");
                    return;
                } else {
                    if (destino.equals("") && parametro == null) {
                        final DialogBuilder dialogBuilder;
                        dialogBuilder = new DialogBuilder(_activity);
                        dialogBuilder.setMessage("Debe seleccionar el destino");
                        dialogBuilder.setCancelable(false);
                        dialogBuilder.setPositiveButton("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogBuilder.dismiss();
                            }
                        });
                        dialogBuilder.show();
                        return;
                    }
                    guardarDatos();
                    //messageBox("guardado");
                }
            }
        });

        txt_placa = (TextView) findViewById(R.id.Txt_placa);
        listaDestino = findViewById(R.id.lista_destino);
        listaDestinoParticular = findViewById(R.id.lista_destino_particular);
        listaDestino.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    listaDestinos.get(position - 1);
                    MyApp.getDBO().parametroDao().saveOrUpdate("current_destino", "" + position);
                    destinos = (String) listaDestino.getSelectedItem();
                    traerDestinoEspecifico();
                    //validarHoteles();
                   /* if(destinos.equals("HOTEL")&& parametro==null){finHotel=0;}
                    if(parametro!=null){
                        if(inicioHotel.equals("1") && destinos.equals("HOTEL")){
                            loteHotelPadre();
                        }
                    }*/
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listaDestinoParticular.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    destinosEspecificos.get(position - 1);
                    destino = (String) listaDestinoParticular.getSelectedItem();
                    CatalogoEntity c = MyApp.getDBO().catalogoDao().fetchConsultarCatalogo(destino, 12);
                    idDestino = c != null ? c.getIdSistema() : -1;
                    MyApp.getDBO().parametroDao().saveOrUpdate("current_destino_especifico", "" + idDestino);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        traerDatosAnteriores();

        traerDestinos();
    }

   /* public void initButton() {
        btnFinApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (destinos.equals("")) {
                    //message("Seleccione Destino");
                    return;
                } else {
                    if (destino.equals("") && parametro == null) {
                        //messageBox("Seleccione Destino");
                        return;
                    }
                    guardarDatos();
                    //messageBox("guardado");
                }
            }
        });
    }
    public void cancelButton(){
        btnCancelarApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }*/

    private void traerDestinos() {
        consultarDetino = new UserConsultarDestinosTask(_activity);
        consultarDetino.setOnDestinoListener(new UserConsultarDestinosTask.OnDestinoListener() {
            @Override
            public void onSuccessful(List<DtoCatalogo> catalogos) {
                listaDestinos = catalogos;
                listaDestino = cargarSpinnerDestino(listaDestino, catalogos, true);
            }
        });
        consultarDetino.execute();
    }

    public Spinner cargarSpinnerDestino(Spinner spinner, List<DtoCatalogo> catalogos, boolean bhabilitar) {
        ArrayAdapter<String> adapter;
        Spinner defaulSpiner = spinner;
        ArrayList<String> listaData = new ArrayList<String>();

        //listaRutas = MyApp.getDBO().rutasDao().fetchConsultarRutas();
        listaData.add("SELECCIONE");
        if (catalogos.size() > 0) {
            for (DtoCatalogo r : catalogos) {
                listaData.add(r.getNombre());
            }
        }

        adapter = new ArrayAdapter<String>(_activity, R.layout.textview_spinner, listaData);
        defaulSpiner.setAdapter(adapter);
        defaulSpiner.setEnabled(bhabilitar);

        return defaulSpiner;
    }

    private void traerDestinoEspecifico() {
        consultaDestinoEspecifico = new UserDestinoEspecificoTask(_activity);
        consultaDestinoEspecifico.setOnDestinoListener(new UserDestinoEspecificoTask.OnDestinoListener() {
            @Override
            public void onSuccessful(List<DtoCatalogo> catalogos, Integer idDestinoX) {
                destinosEspecificos = catalogos;
                listaDestinoParticular = cargarSpinnerDestino(listaDestinoParticular, catalogos, true);
            }
        });
        consultaDestinoEspecifico.execute();
    }

    private void traerDatosAnteriores() {
        RutaInicioFinEntity rut = MyApp.getDBO().rutaInicioFinDao().fechConsultaInicioFinRutasE(MySession.getIdUsuario());
        if (rut != null) {
/*            kilometrajeInicio = rut.getKilometrajeInicio();
            //placaInicio = rut.getIdTransporteVehiculo();
            diaAnterior = rut.getFechaInicio();
            idInicioFin = rut.getIdRutaInicioFin();*/
            idRuta = rut.getIdSubRuta();

        }
/*        //traer placa
        //Integer idRuta = Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_ruta").getValor());
        //CatalogoEntity c = MyApp.getDBO().catalogoDao().fetchConsultarCatalogoEspecifico(placaInicio,4);
        txt_kilometraje_inicio.setText(kilometrajeInicio);*/

        RutasEntity r = MyApp.getDBO().rutasDao().fetchConsultarNombre(idRuta);
        String subRuta = r != null ? r.getNombre() : "";
        idSubtura = rut != null ? rut.getIdSubRuta() : -1;
        txt_placa.setText(subRuta);
    }

    private void guardarDatos() {

        Integer idTransportistaRecolector = MySession.getIdUsuario();
        userRegistrarFinLoteHospitales = new UserRegistrarFinLoteHospitalesTask(_activity, idSubtura, idDestino,2,idTransportistaRecolector);
        userRegistrarFinLoteHospitales.setOnFinLoteListener(new UserRegistrarFinLoteHospitalesTask.OnFinLoteListener() {
            @Override
            public void onSuccessful() {

                final DialogBuilder dialogBuilder;
                dialogBuilder = new DialogBuilder(_activity);
                dialogBuilder.setMessage("Lote finalizado correctamente!");
                dialogBuilder.setCancelable(false);
                dialogBuilder.setPositiveButton("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                        MyApp.getDBO().notificacionDao().deleteNotificationTipoCierreLote("12");
                        onBackPressed();
                       // finish();
                    }
                });
                dialogBuilder.show();

            }

            @Override
            public void onFailure() {
                final DialogBuilder dialogBuilder;
                dialogBuilder = new DialogBuilder(_activity);
                dialogBuilder.setMessage("Falló el registro de lote!");
                dialogBuilder.setCancelable(false);
                dialogBuilder.setPositiveButton("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                    }
                });
                dialogBuilder.show();
            }
        });
        userRegistrarFinLoteHospitales.execute();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }




}

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
import com.caircb.rcbtracegadere.database.entity.HotelLotePadreEntity;
import com.caircb.rcbtracegadere.database.entity.ParametroEntity;
import com.caircb.rcbtracegadere.database.entity.RutaInicioFinEntity;
import com.caircb.rcbtracegadere.database.entity.RutasEntity;
import com.caircb.rcbtracegadere.generics.MyDialog;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.response.DtoCatalogo;
import com.caircb.rcbtracegadere.tasks.UserConsultarDestinosTask;
import com.caircb.rcbtracegadere.tasks.UserDestinoEspecificoTask;
import com.caircb.rcbtracegadere.tasks.UserObtenerLotePadreHotelTask;
import com.caircb.rcbtracegadere.tasks.UserRegistrarInicioFinLoteHotelTask;

import java.util.ArrayList;
import java.util.List;

public class DialogFinLoteHospitales extends MyDialog {
    Activity _activity;
    Spinner listaDestino, listaDestinoParticular;
    LinearLayout btnFinApp, btnCancelarApp;
    TextView txt_placa;
    UserConsultarDestinosTask consultarDetino;
    UserDestinoEspecificoTask consultaDestinoEspecifico;
    UserObtenerLotePadreHotelTask lotePadreHotelTask;
    UserRegistrarInicioFinLoteHotelTask inicioFinLoteHotelTask;
    DialogBuilder builder;
    HotelLotePadreEntity lotePadre;
    List<DtoCatalogo> listaDestinos, destinosEspecificos;
    String destino = "", destinos = "", inicioHotel;
    int idDestino;
    Integer finHotel = 3, idRuta;
    ParametroEntity parametro;

    public DialogFinLoteHospitales(@NonNull Context context) {
        super(context, R.layout.dialog_fin_lote_hospitales);
        this._activity = (Activity) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getView());

        init();
        initButton();
        cancelButton();
    }

    private void init() {
      /*  parametro = MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_hotel");
        if(parametro!=null){
            inicioHotel = parametro.getValor();
        }*/
        //lotePadre = MyApp.getDBO().hotelLotePadreDao().fetchConsultarHotelLote(MySession.getIdUsuario());
        btnFinApp = (LinearLayout) getView().findViewById(R.id.btnFinalizarRuta);
        txt_placa = (TextView) getView().findViewById(R.id.Txt_placa);
        listaDestino = getView().findViewById(R.id.lista_destino);
        listaDestinoParticular = getView().findViewById(R.id.lista_destino_particular);
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

    public void initButton() {
        btnFinApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (destinos.equals("")) {
                    messageBox("Seleccione Destino");
                    return;
                } else {
                    if (destino.equals("") && parametro == null) {
                        messageBox("Seleccione Destino");
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
                dismiss();
            }
        });
    }

    private void traerDestinos() {
        consultarDetino = new UserConsultarDestinosTask(getActivity());
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

        adapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_spinner, listaData);
        defaulSpiner.setAdapter(adapter);
        defaulSpiner.setEnabled(bhabilitar);

        return defaulSpiner;
    }

    private void traerDestinoEspecifico() {
        consultaDestinoEspecifico = new UserDestinoEspecificoTask(getActivity());
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
        txt_placa.setText(subRuta);
    }

    private void guardarDatos() {
       /* userRegistrarFinLoteHospitales = new UserRegistrarFinLoteHospitales(getActivity(),id);
        userRegistrarFinLoteHospitales.setOnFinLoteListener(new UserRegistrarFinRutaTask.OnIniciaRutaListener() {
            @Override
            public void onSuccessful() {

               *//* if(finHotel.equals(0)){
                    inicioFinLoteHotelTask =new UserRegistrarInicioFinLoteHotelTask(getActivity(),idDestino);
                    inicioFinLoteHotelTask.execute();
                }*//*

                // MyApp.getDBO().parametroDao().saveOrUpdate("current_destino_especifico",""+0);
                MyApp.getDBO().parametroDao().saveOrUpdate("current_destino_info",""+0);
                lblpickUpTransportista.setText("0");
                lblListaManifiestoAsignado.setText("0");
                DialogFinRuta.this.dismiss();
            }



            @Override
            public void onFailure() {
                DialogFinRuta.this.dismiss();
            }
        });

        registroFinRuta.execute();*/
    }

/*    private void inicioFinLote(){
        inicioFinLoteHotelTask =new UserRegistrarInicioFinLoteHotelTask(getActivity(),idDestino);
        inicioFinLoteHotelTask.execute();
    }*/

   /* private void loteHotelPadre(){
        if(lotePadre!=null){
            validarHoteles();

        }else {
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


    }*/

}

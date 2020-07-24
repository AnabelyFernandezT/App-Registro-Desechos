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

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.AppDatabase;
import com.caircb.rcbtracegadere.database.entity.CatalogoEntity;
import com.caircb.rcbtracegadere.database.entity.HotelLotePadreEntity;
import com.caircb.rcbtracegadere.database.entity.ParametroEntity;
import com.caircb.rcbtracegadere.database.entity.RutaInicioFinEntity;
import com.caircb.rcbtracegadere.database.entity.RutasEntity;
import com.caircb.rcbtracegadere.generics.MyDialog;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.response.DtoCatalogo;
import com.caircb.rcbtracegadere.models.response.DtoFindRutas;
import com.caircb.rcbtracegadere.tasks.UserConsultarDestinosTask;
import com.caircb.rcbtracegadere.tasks.UserDestinoEspecificoTask;
import com.caircb.rcbtracegadere.tasks.UserObtenerLotePadreHotelTask;
import com.caircb.rcbtracegadere.tasks.UserRegistrarFinLoteHotelTask;
import com.caircb.rcbtracegadere.tasks.UserRegistrarFinRutaTask;
import com.caircb.rcbtracegadere.tasks.UserRegistrarInicioFinLoteHotelTask;
import com.caircb.rcbtracegadere.tasks.UserRegistrarInicioRutaTask;

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
    Integer placaInicio, idInicioFin ,idRuta, finHotel=3;
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
    ImageView btnPickUpTransportista, btnDropOffTransportista;
    TextView txtBuscar, txtSincronizar, txtManifiestos;

    public DialogFinRuta(@NonNull Context context) {
        super(context, R.layout.dialog_final_ruta);
        this._activity = (Activity)context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getView());

        init();
        initButton();
    }

    private void init(){
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
                    if(destinos.equals("HOTEL")&& parametro==null){finHotel=0;}
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

        Date dia = AppDatabase.getDateTime();
        CatalogoEntity c = MyApp.getDBO().catalogoDao().fetchConsultarCatalogo(destino,12);
        final int idDestino = c!=null?c.getIdSistema():-1;
        if(idDestino!=-1){
            MyApp.getDBO().parametroDao().saveOrUpdate("current_destino_especifico",""+idDestino);
        }else {
            MyApp.getDBO().parametroDao().saveOrUpdate("current_destino_especifico",""+0);
        }



        idRegistro = MyApp.getDBO().rutaInicioFinDao().saveOrUpdateInicioRuta(idInicioFin, MySession.getIdUsuario(),placaInicio,diaAnterior,dia,kilometrajeInicio,kilometrajeFinal.getText().toString(),2);
        MyApp.getDBO().parametroDao().saveOrUpdate("current_vehiculo",""+placaInicio);

        if(finHotel.equals(1)){
            inicioFinLoteHotelTask =new UserRegistrarInicioFinLoteHotelTask(getActivity(),idDestino);
            inicioFinLoteHotelTask.execute();
        }

        registroFinRuta = new UserRegistrarFinRutaTask(getActivity(),idRegistro);
        registroFinRuta.setOnIniciaRutaListener(new UserRegistrarFinRutaTask.OnIniciaRutaListener() {
            @Override
            public void onSuccessful() {

               /* if(finHotel.equals(0)){
                    inicioFinLoteHotelTask =new UserRegistrarInicioFinLoteHotelTask(getActivity(),idDestino);
                    inicioFinLoteHotelTask.execute();
                }*/

               // MyApp.getDBO().parametroDao().saveOrUpdate("current_destino_especifico",""+0);
                MyApp.getDBO().parametroDao().saveOrUpdate("current_destino_info",""+0);
                DialogFinRuta.this.dismiss();
            }

            @Override
            public void onFailure() {
                DialogFinRuta.this.dismiss();
            }
        });

        registroFinRuta.execute();


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
        //btnPickUpTransportista.setEnabled(false);
        btnDropOffTransportista.setEnabled(false);

        regionBuscar.setColorFilter(Color.rgb(Integer.valueOf(getActivity().getString(R.string.btnDisabled1)), Integer.valueOf(getActivity().getString(R.string.btnDisabled1)), Integer.valueOf(getActivity().getString(R.string.btnDisabled1))));
        btnSincManifiestos.setColorFilter(Color.rgb(Integer.valueOf(getActivity().getString(R.string.btnDisabled1)), Integer.valueOf(getActivity().getString(R.string.btnDisabled1)), Integer.valueOf(getActivity().getString(R.string.btnDisabled1))));
        btnListaAsignadaTransportista.setColorFilter(Color.rgb(Integer.valueOf(getActivity().getString(R.string.btnDisabled1)), Integer.valueOf(getActivity().getString(R.string.btnDisabled1)), Integer.valueOf(getActivity().getString(R.string.btnDisabled1))));

        txtBuscar.setTextColor(Color.rgb(Integer.valueOf(getActivity().getString(R.string.btnDisabled1)), Integer.valueOf(getActivity().getString(R.string.btnDisabled1)), Integer.valueOf(getActivity().getString(R.string.btnDisabled1))));
        txtManifiestos.setTextColor(Color.rgb(Integer.valueOf(getActivity().getString(R.string.btnDisabled1)), Integer.valueOf(getActivity().getString(R.string.btnDisabled1)), Integer.valueOf(getActivity().getString(R.string.btnDisabled1))));
        txtSincronizar.setTextColor(Color.rgb(Integer.valueOf(getActivity().getString(R.string.btnDisabled1)), Integer.valueOf(getActivity().getString(R.string.btnDisabled1)), Integer.valueOf(getActivity().getString(R.string.btnDisabled1))));

        MyApp.getDBO().parametroDao().saveOrUpdate("current_placa_transportista",""+0);
        lnlIniciaRuta.setVisibility(View.VISIBLE);
        lnlFinRuta.setVisibility(View.GONE);


    }

    private void loteHotelPadre(){
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

    private void inicioFinLote(){
        inicioFinLoteHotelTask =new UserRegistrarInicioFinLoteHotelTask(getActivity(),idDestino);
        inicioFinLoteHotelTask.execute();
    }

    private void validarHoteles (){

            builder = new DialogBuilder(getActivity());
            builder.setMessage("¿Es su último día de recolección?");
            builder.setCancelable(true);
            builder.setPositiveButton("Si", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listaDestinoParticular.setEnabled(false);
                    MyApp.getDBO().parametroDao().saveOrUpdate("current_destino_especifico",""+0);
                    inicioFinLoteHotelTask =new UserRegistrarInicioFinLoteHotelTask(getActivity(),0);
                    inicioFinLoteHotelTask.setOnRegisterListener(new UserRegistrarInicioFinLoteHotelTask.OnRegisterListener() {
                        @Override
                        public void onSuccessful() {
                            idDestino = 0;
                            finHotel=0;
                            finLotePadreHotelTask = new UserRegistrarFinLoteHotelTask(getActivity());
                            finLotePadreHotelTask.execute();
                        }
                    });
                    inicioFinLoteHotelTask.execute();

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

}

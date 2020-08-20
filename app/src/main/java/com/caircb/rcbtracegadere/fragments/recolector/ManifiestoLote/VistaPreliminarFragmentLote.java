package com.caircb.rcbtracegadere.fragments.recolector.ManifiestoLote;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.entity.RuteoRecoleccionEntity;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.fragments.recolector.HojaRutaAsignadaFragment;
import com.caircb.rcbtracegadere.fragments.recolector.HomeTransportistaFragment;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.helpers.MyManifiesto;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.DtoRuteoRecoleccion;
import com.caircb.rcbtracegadere.tasks.UserRegistrarRecoleccion;
import com.caircb.rcbtracegadere.tasks.UserRegistrarRuteoRecoleccion;
import com.joanzapata.pdfview.PDFView;

import java.io.File;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VistaPreliminarFragmentLote#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VistaPreliminarFragmentLote extends MyFragment implements View.OnClickListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    DialogBuilder builder;

    Integer idAppManifiesto,idAppTipoPaquete;
    LinearLayout btnVistaPreviaCancelar,btnVistaPreviaGuardar;
    ProgressDialog dialog;
    PDFView pdfView;
    MyManifiesto myManifiesto;
    UserRegistrarRecoleccion userRegistrarRecoleccion;
    UserRegistrarRuteoRecoleccion userRegistrarRuteoRecoleccion;
    DialogBuilder dialogBuilder;
    String identifiacion;

    public static VistaPreliminarFragmentLote newInstance(Integer manifiestoID, Integer idAppTipoPaquete, String identificacion) {
        VistaPreliminarFragmentLote fragment = new VistaPreliminarFragmentLote();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, manifiestoID);
        args.putString(ARG_PARAM3,identificacion);
        if(idAppTipoPaquete!=null){
            args.putInt(ARG_PARAM2,idAppTipoPaquete);
        }else{
            args.putInt(ARG_PARAM2,0);
        }

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idAppManifiesto= getArguments().getInt(ARG_PARAM1);
            idAppTipoPaquete = getArguments().getInt(ARG_PARAM2);
            identifiacion = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setView(inflater.inflate(R.layout.fragment_vista_preliminar, container, false));
        init();
        generarPDF();
        return getView();
    }

    private void init(){

        btnVistaPreviaCancelar = getView().findViewById(R.id.btnVistaPreviaCancelar);
        btnVistaPreviaGuardar = getView().findViewById(R.id.btnVistaPreviaGuardar);

        btnVistaPreviaCancelar.setOnClickListener(this);
        btnVistaPreviaGuardar.setOnClickListener(this);

    }

    private void generarPDF(){

        MyApp.getDBO().manifiestoDao().updateManifiestoFechaRecoleccion(idAppManifiesto,new Date());

        new AsyncTask<Void, Void, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(getActivity());
                dialog.setMessage("Construyendo "+ System.getProperty("line.separator")+"vista preliminar...");
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                myManifiesto = new MyManifiesto(getActivity(),idAppManifiesto,idAppTipoPaquete, identifiacion);
                myManifiesto.create();
                return myManifiesto.getPathFile();
            }

            @Override
            protected void onPostExecute(String path) {
                super.onPostExecute(path);
                dialog.dismiss();
                cargarPDF(path);
            }
        }.execute();
    }

    private void cargarPDF (String path){
        try {
            pdfView = (PDFView) getView().findViewById(R.id.pdfViewPager);
            File f = new File(path);
            pdfView.fromFile(f).load();

        }catch (Exception ex){
            int x=0;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnVistaPreviaCancelar:
                setNavegate(ManifiestoLoteFragment.newInstance(idAppManifiesto,2,2));
                break;
            case R.id.btnVistaPreviaGuardar:
               builder = new DialogBuilder(getActivity());
                builder.setMessage("¿Esta seguro que desea continuar?");
                builder.setCancelable(false);
                builder.setPositiveButton("SI", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        registarDatos();
                        builder.dismiss();
                    }
                });
                builder.setNegativeButton("NO", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();
                    }
                });
                builder.show();
                break;
        }
    }

    private void registarDatos(){
        userRegistrarRecoleccion = new UserRegistrarRecoleccion(getActivity(),idAppManifiesto,getLocation(),1);
        userRegistrarRecoleccion.setOnRegisterListener(new UserRegistrarRecoleccion.OnRegisterListener() {
            @Override
            public void onSuccessful(final Date fechaRecol) {

                RuteoRecoleccionEntity dto;
                dto = MyApp.getDBO().ruteoRecoleccion().searchUltimoRegistro();
                if(dto!=null){
                    MyApp.getDBO().ruteoRecoleccion().updatePuntoLlegadaFechaLlegadaEstado(dto.get_id(),idAppManifiesto, fechaRecol, true);
                }else{
                    MyApp.getDBO().ruteoRecoleccion().saverOrUpdate(new DtoRuteoRecoleccion(MySession.getIdSubRuta(), fechaRecol,  idAppManifiesto,null,null,true));
                    dto = MyApp.getDBO().ruteoRecoleccion().searchUltimoRegistro();
                }

                userRegistrarRuteoRecoleccion = new UserRegistrarRuteoRecoleccion(getActivity(), dto);
                userRegistrarRuteoRecoleccion.setOnRegisterRuteoRecollecionListenner(new UserRegistrarRuteoRecoleccion.OnRegisterRuteroRecoleecionListener() {
                    @Override
                    public void onSuccessful() {

                        if(MyApp.getDBO().manifiestoDao().contarHojaRutaAsignadas() >0 ){

                            dialogBuilder = new DialogBuilder(getActivity());
                            dialogBuilder.setMessage("¿Desea iniciar traslado al próximo punto de recolección ?");
                            dialogBuilder.setCancelable(false);
                            dialogBuilder.setPositiveButton("SI", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogBuilder.dismiss();
                                    //Guardo la nueva fecha de inicio y puntoParitda;
                                    MyApp.getDBO().parametroDao().saveOrUpdate("ruteoRecoleccion", "SI");
                                    RuteoRecoleccionEntity dto;
                                    dto = MyApp.getDBO().ruteoRecoleccion().searchUltimoRegistro();
                                    if(dto!=null){
                                        MyApp.getDBO().ruteoRecoleccion().saverOrUpdate(new DtoRuteoRecoleccion(MySession.getIdSubRuta(), fechaRecol,dto.getPuntoLlegada(),null,null,false));
                                    }
                                    //List<RuteoRecoleccionEntity> enty3 = MyApp.getDBO().ruteoRecoleccion().searchRuteoRecoleccion(); //////////
                                    setNavegate(HojaRutaAsignadaFragment.newInstance());
                                }
                            });
                            dialogBuilder.setNegativeButton("NO", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogBuilder.dismiss();
                                    //Update parametro en NO para levantar el modal para verificar si empieza con el trazlado
                                    MyApp.getDBO().parametroDao().saveOrUpdate("ruteoRecoleccion", "NO");
                                    RuteoRecoleccionEntity dto;
                                    dto = MyApp.getDBO().ruteoRecoleccion().searchUltimoRegistro();
                                    if(dto!=null){
                                        MyApp.getDBO().ruteoRecoleccion().saverOrUpdate(new DtoRuteoRecoleccion(MySession.getIdSubRuta(), fechaRecol,dto.getPuntoLlegada(),null,null,false));
                                    }
                                    setNavegate(HomeTransportistaFragment.create());
                                }
                            });
                            dialogBuilder.show();
                        }else{
                            MyApp.getDBO().parametroDao().saveOrUpdate("ruteoRecoleccion", "NO");
                            RuteoRecoleccionEntity dto;
                            dto = MyApp.getDBO().ruteoRecoleccion().searchUltimoRegistro();
                            if(dto!=null){
                                MyApp.getDBO().ruteoRecoleccion().saverOrUpdate(new DtoRuteoRecoleccion(MySession.getIdSubRuta(), fechaRecol,dto.getPuntoLlegada(),null,null,false));
                            }
                            setNavegate(HomeTransportistaFragment.create());
                        }
                    }

                    @Override
                    public void onFail() {
                        setNavegate(HojaRutaAsignadaFragment.newInstance());
                    }
                });
                userRegistrarRuteoRecoleccion.execute();
            }

            @Override
            public void onFail() {
                setNavegate(HojaRutaAsignadaFragment.newInstance());
                messageBox("No se encontro impresora, Datos Guardados");
            }
        });
        userRegistrarRecoleccion.execute();
    }
}
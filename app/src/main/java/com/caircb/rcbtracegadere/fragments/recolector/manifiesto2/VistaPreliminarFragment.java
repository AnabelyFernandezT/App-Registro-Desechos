package com.caircb.rcbtracegadere.fragments.recolector.manifiesto2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.app.Fragment;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.AppDatabase;
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
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VistaPreliminarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VistaPreliminarFragment extends MyFragment implements View.OnClickListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Integer idAppManifiesto,idAppTipoPaquete;
    LinearLayout btnVistaPreviaCancelar,btnVistaPreviaGuardar;
    ProgressDialog dialog;
    PDFView pdfView;
    MyManifiesto myManifiesto;
    UserRegistrarRecoleccion userRegistrarRecoleccion;
    UserRegistrarRuteoRecoleccion userRegistrarRuteoRecoleccion;
    DialogBuilder dialogBuilder;

    public static VistaPreliminarFragment newInstance(Integer manifiestoID, Integer idAppTipoPaquete) {
        VistaPreliminarFragment fragment = new VistaPreliminarFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, manifiestoID);
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
                myManifiesto = new MyManifiesto(getActivity(),idAppManifiesto,idAppTipoPaquete);
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
                setNavegate(Manifiesto2Fragment.newInstance(idAppManifiesto,2));
                break;
            case R.id.btnVistaPreviaGuardar:
                userRegistrarRecoleccion = new UserRegistrarRecoleccion(getActivity(),idAppManifiesto,getLocation());
                userRegistrarRecoleccion.setOnRegisterListener(new UserRegistrarRecoleccion.OnRegisterListener() {
                    @Override
                    public void onSuccessful(final Date fechaRecol) {
                        //setNavegate(HojaRutaAsignadaFragment.newInstance());

                        //Registro el ruteo en estado en 1
                        Integer _id = MyApp.getDBO().ruteoRecoleccion().searchRegistroLlegada(idAppManifiesto);
                        RuteoRecoleccionEntity dtoSendServicio = MyApp.getDBO().ruteoRecoleccion().dtoSendServicio(_id, idAppManifiesto);

                        userRegistrarRuteoRecoleccion = new UserRegistrarRuteoRecoleccion(getActivity(), dtoSendServicio);
                        userRegistrarRuteoRecoleccion.setOnRegisterRuteoRecollecionListenner(new UserRegistrarRuteoRecoleccion.OnRegisterRuteroRecoleecionListener() {
                            @Override
                            public void onSuccessful() {

                                //List<RuteoRecoleccionEntity> enty = MyApp.getDBO().ruteoRecoleccion().searchRuteoRecoleccion(); //////////
                                Integer _id = MyApp.getDBO().ruteoRecoleccion().searchRegistroLlegada(idAppManifiesto);
                                if(_id !=null && _id >=0){
                                    MyApp.getDBO().ruteoRecoleccion().updateEstadoByPuntoLLegada(_id, idAppManifiesto);
                                }
                                //List<RuteoRecoleccionEntity> enty2 = MyApp.getDBO().ruteoRecoleccion().searchRuteoRecoleccion(); ///////////

                                if(MyApp.getDBO().manifiestoDao().contarHojaRutaAsignadas() >0 ){

                                    dialogBuilder = new DialogBuilder(getActivity());
                                    dialogBuilder.setMessage("¿Desea iniciar trazlado al proximo punto de recoleccion ?");
                                    dialogBuilder.setCancelable(false);
                                    dialogBuilder.setPositiveButton("SI", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogBuilder.dismiss();
                                            //Guardo la nueva fecha de inicio y puntoParitda;
                                            MyApp.getDBO().ruteoRecoleccion().saverOrUpdate(new DtoRuteoRecoleccion(MySession.getIdSubRuta(), fechaRecol,idAppManifiesto,null,null,false));
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
                                            MyApp.getDBO().ruteoRecoleccion().saverOrUpdate(new DtoRuteoRecoleccion(MySession.getIdSubRuta(), fechaRecol,idAppManifiesto,null,null,false));
                                            //setNavegate(HojaRutaAsignadaFragment.newInstance());
                                            //Se envia al home ya que el usuario No desea recolectar
                                            setNavegate(HomeTransportistaFragment.create());

                                        }
                                    });
                                    dialogBuilder.show();
                                }else{
                                    MyApp.getDBO().parametroDao().saveOrUpdate("ruteoRecoleccion", "NO");
                                    MyApp.getDBO().ruteoRecoleccion().saverOrUpdate(new DtoRuteoRecoleccion(MySession.getIdSubRuta(), fechaRecol,idAppManifiesto,null,null,false));
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
                break;
        }
    }
}
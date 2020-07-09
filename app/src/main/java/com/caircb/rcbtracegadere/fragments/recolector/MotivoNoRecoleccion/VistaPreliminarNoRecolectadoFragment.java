package com.caircb.rcbtracegadere.fragments.recolector.MotivoNoRecoleccion;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.entity.RuteoRecoleccionEntity;
import com.caircb.rcbtracegadere.fragments.recolector.HojaRutaAsignadaFragment;
import com.caircb.rcbtracegadere.fragments.recolector.HomeTransportistaFragment;
import com.caircb.rcbtracegadere.fragments.recolector.manifiesto2.Manifiesto2Fragment;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.helpers.MyManifiesto;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.DtoRuteoRecoleccion;
import com.caircb.rcbtracegadere.tasks.UserRegistrarNoRecoleccion;
import com.caircb.rcbtracegadere.tasks.UserRegistrarRecoleccion;
import com.caircb.rcbtracegadere.tasks.UserRegistrarRuteoRecoleccion;
import com.joanzapata.pdfview.PDFView;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VistaPreliminarNoRecolectadoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VistaPreliminarNoRecolectadoFragment extends MyFragment implements View.OnClickListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Integer idAppManifiesto,idAppTipoPaquete;
    LinearLayout btnVistaPreviaCancelar,btnVistaPreviaGuardar;
    ProgressDialog dialog;
    PDFView pdfView;
    MyManifiesto myManifiesto;
    UserRegistrarNoRecoleccion noRecoleccion;
    UserRegistrarRuteoRecoleccion userRegistrarRuteoRecoleccion;

    public static VistaPreliminarNoRecolectadoFragment newInstance(Integer manifiestoID, Integer idAppTipoPaquete) {
        VistaPreliminarNoRecolectadoFragment fragment = new VistaPreliminarNoRecolectadoFragment();
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
                setNavegate(ManifiestoNoRecoleccionFragment.newInstance(idAppManifiesto,1));
                break;
            case R.id.btnVistaPreviaGuardar:
                noRecoleccion = new UserRegistrarNoRecoleccion(getActivity(),idAppManifiesto,getLocation());
                noRecoleccion.setOnRegisterListener(new UserRegistrarNoRecoleccion.OnRegisterListener() {
                    @Override
                    public void onSuccessful(final Date fechaRecol) {
                        messageBox("Datos Guardados");

                        Integer _id = MyApp.getDBO().ruteoRecoleccion().searchRegistroLlegada(idAppManifiesto);
                        RuteoRecoleccionEntity dtoSendServicio = MyApp.getDBO().ruteoRecoleccion().dtoSendServicio(_id, idAppManifiesto);

                        userRegistrarRuteoRecoleccion = new UserRegistrarRuteoRecoleccion(getActivity(), dtoSendServicio);
                        userRegistrarRuteoRecoleccion.setOnRegisterRuteoRecollecionListenner(new UserRegistrarRuteoRecoleccion.OnRegisterRuteroRecoleecionListener() {
                            @Override
                            public void onSuccessful() {

                                List<RuteoRecoleccionEntity> enty = MyApp.getDBO().ruteoRecoleccion().searchRuteoRecoleccion(); //////////
                                Integer _id = MyApp.getDBO().ruteoRecoleccion().searchRegistroLlegada(idAppManifiesto);
                                if(_id !=null && _id >=0){
                                    MyApp.getDBO().ruteoRecoleccion().updateEstadoByPuntoLLegada(_id, idAppManifiesto);
                                }
                                List<RuteoRecoleccionEntity> enty2 = MyApp.getDBO().ruteoRecoleccion().searchRuteoRecoleccion(); ///////////

                                if(MyApp.getDBO().manifiestoDao().contarHojaRutaAsignadas() >0 ){
                                    AlertDialog.Builder builderDialog= new AlertDialog.Builder(getActivity());
                                    builderDialog.setMessage("¿Desea iniciar trazlado al proximo punto de recoleccion ?");
                                    builderDialog.setCancelable(false);
                                    builderDialog.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            //Guardo la nueva fecha de inicio y puntoParitda;
                                            MyApp.getDBO().ruteoRecoleccion().saverOrUpdate(new DtoRuteoRecoleccion(MySession.getIdSubRuta(), fechaRecol,idAppManifiesto,null,null,false));
                                            //List<RuteoRecoleccionEntity> enty3 = MyApp.getDBO().ruteoRecoleccion().searchRuteoRecoleccion(); //////////

                                            setNavegate(HojaRutaAsignadaFragment.newInstance());

                                        }
                                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //Update parametro en NO para levantar el modal para verificar si empieza con el trazlado
                                            MyApp.getDBO().parametroDao().saveOrUpdate("ruteoRecoleccion", "NO");
                                            MyApp.getDBO().ruteoRecoleccion().saverOrUpdate(new DtoRuteoRecoleccion(MySession.getIdSubRuta(), fechaRecol,idAppManifiesto,null,null,false));
                                            //setNavegate(HojaRutaAsignadaFragment.newInstance());
                                            //Se envia al home ya que el usuario No desea recolectar
                                            setNavegate(HomeTransportistaFragment.create());
                                            dialog.dismiss();

                                        }
                                    });
                                    AlertDialog dialog = builderDialog.create();
                                    dialog.show();
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
                        //setNavegate(HojaRutaAsignadaFragment.newInstance());
                    }

                    @Override
                    public void onFail() {
                        messageBox("No se guardo en el servidor");

                    }
                });
                noRecoleccion.execute();
                break;
        }
    }
}
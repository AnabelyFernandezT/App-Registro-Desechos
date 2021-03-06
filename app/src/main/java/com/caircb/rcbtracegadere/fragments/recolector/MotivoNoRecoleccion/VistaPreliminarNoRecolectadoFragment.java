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
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.fragments.recolector.HojaRutaAsignadaFragment;
import com.caircb.rcbtracegadere.fragments.recolector.HomeTransportistaFragment;
import com.caircb.rcbtracegadere.fragments.recolector.manifiesto2.Manifiesto2Fragment;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.helpers.MyManifiesto;
import com.caircb.rcbtracegadere.helpers.MyManifiestoNoRecoleccion;
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
    LinearLayout btnVistaPreviaCancelar,btnVistaPreviaGuardar,sectionNovedadPesoPromedio;
    ProgressDialog dialog;
    PDFView pdfView;
    MyManifiestoNoRecoleccion myManifiesto;
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
        sectionNovedadPesoPromedio = getView().findViewById(R.id.sectionNovedadPesoPromedio);
        sectionNovedadPesoPromedio.setVisibility(View.GONE);
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
                myManifiesto = new MyManifiestoNoRecoleccion(getActivity(),idAppManifiesto,idAppTipoPaquete);
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
                System.out.println("click");
                btnVistaPreviaGuardar.setEnabled(false);

                noRecoleccion = new UserRegistrarNoRecoleccion(getActivity(),idAppManifiesto,getLocation());
                noRecoleccion.setOnRegisterListener(new UserRegistrarNoRecoleccion.OnRegisterListener() {
                    @Override
                    public void onSuccessful(final Date fechaRecol) {
                        //messageBox("Datos Guardados");
                        btnVistaPreviaGuardar.setEnabled(true);
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

                                    final DialogBuilder dialogBuilder = new DialogBuilder(getActivity());
                                    dialogBuilder.setCancelable(false);
                                    dialogBuilder.setMessage("??Desea iniciar traslado al pr??ximo punto de recolecci??n ?");
                                    dialogBuilder.setPositiveButton("SI", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogBuilder.dismiss();

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
                        btnVistaPreviaGuardar.setEnabled(true);
                        messageBox("No se guardo en el servidor");
                    }
                });
                noRecoleccion.execute();
                break;
        }
    }
}
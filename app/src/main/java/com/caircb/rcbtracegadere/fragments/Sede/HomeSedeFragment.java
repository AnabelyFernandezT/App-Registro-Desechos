
package com.caircb.rcbtracegadere.fragments.Sede;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.caircb.rcbtracegadere.MainActivity;
import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.entity.ParametroEntity;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.dialogs.DialogPlacaSede;
import com.caircb.rcbtracegadere.dialogs.DialogPlacaSedeRecolector;
import com.caircb.rcbtracegadere.fragments.planta.HojaRutaAsignadaPlantaFragment;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnHome;
import com.caircb.rcbtracegadere.helpers.MyConstant;
import com.caircb.rcbtracegadere.tasks.UserConsultaLotes;
import com.caircb.rcbtracegadere.tasks.UserConsultarHojaRutaTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarManifiestosSedeTask;
import com.caircb.rcbtracegadere.tasks.UserRegistarFinLoteTask;
import com.caircb.rcbtracegadere.tasks.UserRegistrarPlanta;
import com.google.firebase.auth.FirebaseAuth;

public class HomeSedeFragment extends MyFragment implements OnHome {
    UserConsultaLotes consultarLotes;
    ImageButton btnSincManifiestos,btnListaAsignadaSede,btnMenu, btnInciaLote, btnFinRuta,btnFinLote;

    Integer inicioLote;
    Integer finLote;
    UserRegistarFinLoteTask registarFinLoteTask;
    TextView lblListaManifiestoAsignado;
    LinearLayout lnlIniciaLote, lnlFinLote;
    ImageButton regionBuscar;
    DialogPlacaSede dialogPlacas;
    DialogPlacaSedeRecolector dialogPlacasRecolector;
    TextView txtMovilizar , txtSincronizar, txtManifiesto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setView(inflater.inflate(R.layout.fragment_home_sede, container, false));
        init();
        initBuscador();
        datosManifiestosAsignados();
        return getView();
    }

    private void datosManifiestosAsignados(){
        //consultarPlacasInicioRutaDisponible = new UserConsultarManifiestosSedeTask(getActivity());
        //consultarPlacasInicioRutaDisponible.execute();
    }

    private void init() {
        regionBuscar = getView().findViewById(R.id.regionBuscar);
        lblListaManifiestoAsignado = getView().findViewById(R.id.lblListaManifiestoAsignado);
        btnSincManifiestos = getView().findViewById(R.id.btnSincManifiestos);
        btnListaAsignadaSede = getView().findViewById(R.id.btnListaAsignadaSede);
        btnMenu = getView().findViewById(R.id.btnMenu);
        lnlIniciaLote = getView().findViewById(R.id.LnlIniciaLote);
        btnInciaLote = getView().findViewById(R.id.btnInciaLote);
        lnlFinLote = getView().findViewById(R.id.LnlFinLote);
        btnFinLote = getView().findViewById(R.id.btnFinLote);

        txtSincronizar = getView().findViewById(R.id.txtSincronizar);
        txtManifiesto = getView().findViewById(R.id.txtManifiesto);
        txtMovilizar = getView().findViewById(R.id.txtMovilizar);

        btnSincManifiestos.setColorFilter(Color.rgb(Integer.valueOf(getString(R.string.btnDisabled1)), Integer.valueOf(getString(R.string.btnDisabled2)), Integer.valueOf(getString(R.string.btnDisabled3))));
        btnListaAsignadaSede.setColorFilter(Color.rgb(Integer.valueOf(getString(R.string.btnDisabled1)), Integer.valueOf(getString(R.string.btnDisabled2)), Integer.valueOf(getString(R.string.btnDisabled3))));
        regionBuscar.setColorFilter(Color.rgb(Integer.valueOf(getString(R.string.btnDisabled1)), Integer.valueOf(getString(R.string.btnDisabled2)), Integer.valueOf(getString(R.string.btnDisabled3))));

        txtManifiesto.setTextColor(Color.rgb(Integer.valueOf(getString(R.string.btnDisabled1)), Integer.valueOf(getString(R.string.btnDisabled2)), Integer.valueOf(getString(R.string.btnDisabled3))));
        txtSincronizar.setTextColor(Color.rgb(Integer.valueOf(getString(R.string.btnDisabled1)), Integer.valueOf(getString(R.string.btnDisabled2)), Integer.valueOf(getString(R.string.btnDisabled3))));
        txtMovilizar.setTextColor(Color.rgb(Integer.valueOf(getString(R.string.btnDisabled1)), Integer.valueOf(getString(R.string.btnDisabled2)), Integer.valueOf(getString(R.string.btnDisabled3))));

        btnListaAsignadaSede.setEnabled(false);
        btnSincManifiestos.setEnabled(false);
        regionBuscar.setEnabled(false);

        verificarInicioLote();

        btnListaAsignadaSede.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNavegate(HojaRutaAsignadaSedeFragment.newInstance());
            }
        });

        btnInciaLote.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                dialogPlacas = new DialogPlacaSede(getActivity());
                dialogPlacas.setCancelable(false);
                dialogPlacas.setTitle("INICIAR LOTE");
                dialogPlacas.show();
            }
        });

        btnSincManifiestos.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dialogPlacasRecolector = new DialogPlacaSedeRecolector(getActivity());
                dialogPlacasRecolector.setCancelable(false);
                dialogPlacasRecolector.setTitle("TRANSPORTE RECOLECCION");
                dialogPlacasRecolector.setmOnSincronizarListener(new DialogPlacaSedeRecolector.onSincronizarListener() {
                    @Override
                    public void onSuccessfull() {
                        Integer num = MyApp.getDBO().manifiestoDetalleSede().contarHojaRutaAsignadas();
                        lblListaManifiestoAsignado.setText(""+ MyApp.getDBO().manifiestoDetalleSede().contarHojaRutaAsignadas());
                    }
                });
                dialogPlacasRecolector.show();
            }
        });

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() instanceof MainActivity){
                    ((MainActivity)getActivity()).openMenuOpcion();
                }
            }
        });

        btnFinLote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DialogBuilder dialogBuilder = new DialogBuilder(getActivity());
                dialogBuilder.setCancelable(false);
                dialogBuilder.setMessage("¿Esta seguro de continuar ?");
                dialogBuilder.setPositiveButton("SI", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                        registarFinLoteTask = new UserRegistarFinLoteTask(getActivity());
                        registarFinLoteTask.setOnRegisterListener(new UserRegistarFinLoteTask.OnRegisterListener() {
                            @Override
                            public void onSuccessful(String numLote) {
                                MyApp.getDBO().parametroDao().saveOrUpdate("loteBandera_sedes","false");

                                messageBox("Lote # " + numLote + " se ha cerrado correctamente.");
                                MyApp.getDBO().parametroDao().saveOrUpdate("current_placa_lote","-10");
                                MyApp.getDBO().parametroDao().saveOrUpdate("estado_transporte","false");
                                verificarInicioLote();
                                /*
                                lnlIniciaLote.setVisibility(View.VISIBLE);
                                lnlFinLote.setVisibility(View.GONE);*/
                            }

                            @Override
                            public void onFail() {
                                messageBox("Lote no finalizado");
                            }
                        });
                        registarFinLoteTask.execute();
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
    }

    private void initBuscador(){
        regionBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datosLotesDisponibles();
            }
        });
    }

    private void datosLotesDisponibles(){

        consultarLotes = new UserConsultaLotes(getActivity());
        consultarLotes.setOnRegisterListener(new UserConsultaLotes.TaskListener() {
            @Override
            public void onSuccessful() {
                setNavegate(HojaFragment.newInstance());
            }
        });
        consultarLotes.execute();
    }

    private void verificarInicioLote(){
        lblListaManifiestoAsignado.setText(""+ MyApp.getDBO().manifiestoDetalleSede().contarHojaRutaAsignadas());

        ParametroEntity iniciLote = MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_inicio_lote");
        ParametroEntity finLotes = MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_fin_lote");

       if (iniciLote!=null){
           inicioLote= Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_inicio_lote").getValor());
       }else{
           inicioLote=0;
       }
       if (finLotes!=null){
           finLote= Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_fin_lote").getValor());
       }else {
           finLote=0;
       }


            if(inicioLote.equals(finLote)) {
                lnlIniciaLote.setVisibility(View.VISIBLE);
                lnlFinLote.setVisibility(View.GONE);
                btnListaAsignadaSede.setEnabled(false);
                btnSincManifiestos.setEnabled(false);
                regionBuscar.setEnabled(true);

                btnListaAsignadaSede.setColorFilter(Color.rgb(Integer.valueOf(getString(R.string.btnDisabled1)), Integer.valueOf(getString(R.string.btnDisabled2)), Integer.valueOf(getString(R.string.btnDisabled3))));
                btnSincManifiestos.setColorFilter(Color.rgb(Integer.valueOf(getString(R.string.btnDisabled1)), Integer.valueOf(getString(R.string.btnDisabled2)), Integer.valueOf(getString(R.string.btnDisabled3))));

                txtManifiesto.setTextColor(Color.rgb(Integer.valueOf(getString(R.string.btnDisabled1)), Integer.valueOf(getString(R.string.btnDisabled2)), Integer.valueOf(getString(R.string.btnDisabled3))));
                txtSincronizar.setTextColor(Color.rgb(Integer.valueOf(getString(R.string.btnDisabled1)), Integer.valueOf(getString(R.string.btnDisabled2)), Integer.valueOf(getString(R.string.btnDisabled3))));

                regionBuscar.setColorFilter(Color.TRANSPARENT);
                txtMovilizar.setTextColor(Color.WHITE);

            }else{

                lnlIniciaLote.setVisibility(View.GONE);
                lnlFinLote.setVisibility(View.VISIBLE);
                btnListaAsignadaSede.setEnabled(true);
                btnSincManifiestos.setEnabled(true);
                regionBuscar.setEnabled(false);

                btnListaAsignadaSede.setColorFilter(Color.TRANSPARENT);
                btnSincManifiestos.setColorFilter(Color.TRANSPARENT);
                txtSincronizar.setTextColor(Color.WHITE);
                txtManifiesto.setTextColor(Color.WHITE);

                regionBuscar.setColorFilter(Color.rgb(Integer.valueOf(getString(R.string.btnDisabled1)), Integer.valueOf(getString(R.string.btnDisabled2)), Integer.valueOf(getString(R.string.btnDisabled3))));
            }

    }

    public static HomeSedeFragment create(){
        return new HomeSedeFragment();
    }
}

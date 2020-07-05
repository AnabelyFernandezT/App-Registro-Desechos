package com.caircb.rcbtracegadere.fragments.Sede;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.caircb.rcbtracegadere.MainActivity;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.dialogs.DialogPlacaSede;
import com.caircb.rcbtracegadere.dialogs.DialogPlacaSedeRecolector;
import com.caircb.rcbtracegadere.dialogs.DialogPlacas;
import com.caircb.rcbtracegadere.fragments.planta.HojaFragment;
import com.caircb.rcbtracegadere.fragments.planta.HojaRutaAsignadaPlantaFragment;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnBarcodeListener;
import com.caircb.rcbtracegadere.generics.OnHome;
import com.caircb.rcbtracegadere.models.response.DtoFindRutas;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoDetalleSede;
import com.caircb.rcbtracegadere.tasks.UserConsultarHojaRutaTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarManifiestosSedeTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarRecolectadosTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarRutasTask;
import com.caircb.rcbtracegadere.tasks.UserRegistarFinLoteTask;

import java.util.List;

public class HomeSedeFragment extends MyFragment implements OnHome {

    ImageButton btnSincManifiestos,btnListaAsignadaSede,btnMenu, btnInciaLote, btnFinLote;
    UserRegistarFinLoteTask registarFinLoteTask;
    TextView lblListaManifiestoAsignado;
    LinearLayout lnlIniciaLote, lnlFinLote;
    ImageButton regionBuscar;
    DialogPlacaSede dialogPlacas;
    DialogPlacaSedeRecolector dialogPlacasRecolector;
    UserConsultarManifiestosSedeTask consultarPlacasInicioRutaDisponible;

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
        consultarPlacasInicioRutaDisponible = new UserConsultarManifiestosSedeTask(getActivity());
        consultarPlacasInicioRutaDisponible.execute();
    }

    private void init() {
        lblListaManifiestoAsignado = getView().findViewById(R.id.lblListaManifiestoAsignado);
        btnSincManifiestos = getView().findViewById(R.id.btnSincManifiestos);
        btnListaAsignadaSede = getView().findViewById(R.id.btnListaAsignadaSede);
        btnMenu = getView().findViewById(R.id.btnMenu);
        lnlIniciaLote = getView().findViewById(R.id.LnlIniciaLote);
        btnInciaLote = getView().findViewById(R.id.btnInciaLote);
        lnlFinLote = getView().findViewById(R.id.LnlFinLote);
        btnFinLote = getView().findViewById(R.id.btnFinLote);

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
                dialogPlacas.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogPlacas.setCancelable(false);
                dialogPlacas.setTitle("INICIAR LOTE");
                dialogPlacas.show();
            }
        });

        btnSincManifiestos.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dialogPlacasRecolector = new DialogPlacaSedeRecolector(getActivity());
                dialogPlacasRecolector.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogPlacasRecolector.setCancelable(false);
                dialogPlacasRecolector.setTitle("TRANSPORTE RECOLECCION");
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
                registarFinLoteTask = new UserRegistarFinLoteTask(getActivity());
                registarFinLoteTask.setOnRegisterListener(new UserRegistarFinLoteTask.OnRegisterListener() {
                    @Override
                    public void onSuccessful() {
                        messageBox("Lote Cerrado Correctamente");
                        lnlIniciaLote.setVisibility(View.VISIBLE);
                        lnlFinLote.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFail() {
                        messageBox("Lote no finalizado");
                    }
                });
                registarFinLoteTask.execute();
            }
        });


    }



    private void initBuscador(){
        regionBuscar = getView().findViewById(R.id.regionBuscar);
        regionBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNavegate(HojaFragment.newInstance());
            }
        });
    }



    public static HomeSedeFragment create(){
        return new HomeSedeFragment();
    }
}

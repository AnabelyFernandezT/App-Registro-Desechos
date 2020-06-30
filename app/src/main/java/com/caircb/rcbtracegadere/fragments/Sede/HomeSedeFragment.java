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
import com.caircb.rcbtracegadere.fragments.planta.HojaRutaAsignadaPlantaFragment;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnHome;
import com.caircb.rcbtracegadere.tasks.UserConsultarHojaRutaTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarRecolectadosTask;

public class HomeSedeFragment extends MyFragment implements OnHome {

    ImageButton btnSincManifiestos,btnListaAsignadaSede,btnMenu, btnInciaLote, btnFinRuta;
    UserConsultarHojaRutaTask consultarHojaRutaTask;
    TextView lblListaManifiestoAsignado;
    LinearLayout lnlIniciaLote;
    ImageButton regionBuscar;
    DialogPlacaSede dialogPlacas;
    DialogPlacaSedeRecolector dialogPlacasRecolector;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setView(inflater.inflate(R.layout.fragment_home_sede, container, false));
        init();
        initBuscador();
        return getView();
    }

    private void init() {
        lblListaManifiestoAsignado = getView().findViewById(R.id.lblListaManifiestoAsignado);
        btnSincManifiestos = getView().findViewById(R.id.btnSincManifiestos);
        btnListaAsignadaSede = getView().findViewById(R.id.btnListaAsignadaSede);
        btnMenu = getView().findViewById(R.id.btnMenu);
        lnlIniciaLote = getView().findViewById(R.id.LnlIniciaLote);
        btnInciaLote = getView().findViewById(R.id.btnInciaLote);


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
                dialogPlacas.show();
            }
        });

        btnSincManifiestos.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dialogPlacasRecolector = new DialogPlacaSedeRecolector(getActivity());
                dialogPlacasRecolector.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogPlacasRecolector.setCancelable(false);
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
    }

    private void initBuscador(){
        regionBuscar = (ImageButton)getView().findViewById(R.id.regionBuscar);
        regionBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setNavegate(BuscarFragment.create());
                //setNavegate(HomeRecepcionFragment.create());
            }
        });
    }

    public static HomeSedeFragment create(){
        return new HomeSedeFragment();
    }
}

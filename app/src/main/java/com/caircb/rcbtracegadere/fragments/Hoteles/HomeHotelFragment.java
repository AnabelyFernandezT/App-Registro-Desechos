package com.caircb.rcbtracegadere.fragments.Hoteles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.caircb.rcbtracegadere.MainActivity;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.fragments.GestorAlterno.HojaRutaAsignadaGestorFragment;
import com.caircb.rcbtracegadere.fragments.Sede.HomeSedeFragment;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnHome;
import com.caircb.rcbtracegadere.tasks.UserConsultarHojaRutaTask;

public class HomeHotelFragment extends MyFragment implements OnHome {

    ImageButton btnSincManifiestos,btnListaAsignadaTransportista,btnMenu, btnInicioRuta, btnFinRuta;
    UserConsultarHojaRutaTask consultarHojaRutaTask;
    TextView lblListaManifiestoAsignado;
    ImageButton regionBuscar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setView(inflater.inflate(R.layout.fragment_home_hotel, container, false));
        init();
        initBuscador();
        return getView();
    }

    private void init() {
        lblListaManifiestoAsignado = getView().findViewById(R.id.lblListaManifiestoAsignado);
        btnSincManifiestos = getView().findViewById(R.id.btnSincManifiestos);
        btnListaAsignadaTransportista = getView().findViewById(R.id.btnListaAsignadaTransportista);
        btnMenu = getView().findViewById(R.id.btnMenu);

        btnListaAsignadaTransportista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNavegate(HojaRutaHotelFragment.newInstance());
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
    public static HomeHotelFragment create(){
        return new HomeHotelFragment();
    }
}

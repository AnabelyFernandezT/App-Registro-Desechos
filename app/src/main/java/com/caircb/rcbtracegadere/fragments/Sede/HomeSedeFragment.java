package com.caircb.rcbtracegadere.fragments.Sede;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnHome;
import com.caircb.rcbtracegadere.tasks.UserConsultarHojaRutaTask;

public class HomeSedeFragment extends MyFragment implements OnHome {

    ImageButton btnSincManifiestos,btnListaAsignadaTransportista,btnMenu, btnInicioRuta, btnFinRuta;
    UserConsultarHojaRutaTask consultarHojaRutaTask;
    TextView lblListaManifiestoAsignado;
    LinearLayout lnlIniciaLote;
    ImageButton regionBuscar;

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
        btnListaAsignadaTransportista = getView().findViewById(R.id.btnListaAsignadaTransportista);
        btnMenu = getView().findViewById(R.id.btnMenu);
        lnlIniciaLote = getView().findViewById(R.id.LnlIniciaLote);
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
}

package com.caircb.rcbtracegadere.fragments.planta;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.caircb.rcbtracegadere.MainActivity;
import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.entity.RutaInicioFinEntity;
import com.caircb.rcbtracegadere.dialogs.DialogFinRuta;
import com.caircb.rcbtracegadere.dialogs.DialogInicioRuta;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnHome;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.tasks.UserConsultarHojaRutaTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarRecolectadosTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomePlantaFragment extends MyFragment implements OnHome {
    ImageButton btnSincManifiestosPlanta,btnListaAsignadaTransportista,btnMenu, btnInicioRuta, btnFinRuta;
    UserConsultarRecolectadosTask consultarHojaRutaTask;
    TextView lblListaManifiestoAsignado, lblpickUpTransportista, lblDropOffTransportista, txtinicioRuta, txtFinRuta;
    ImageView btnPickUpTransportista, btnDropOffTransportista;
    DialogInicioRuta dialogInicioRuta;
    DialogFinRuta dialogFinRuta;


    public Context mContext;


    int IdTransporteRecolector;
    Integer inicioFinRuta;

    ImageButton regionBuscar;
    //ImageButton btnCalculadora;


    UserConsultarRecolectadosTask.TaskListener listenerHojaRuta = new UserConsultarRecolectadosTask.TaskListener() {
        @Override
        public void onSuccessful() {
           loadCantidadManifiestoAsignado();
        }
    };


    public static HomePlantaFragment create() {
        return new HomePlantaFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setView(inflater.inflate(R.layout.fragment_home_planta, container, false));

        initBuscador();
        init();

        return getView();

    }

    private void initBuscador(){
        regionBuscar = (ImageButton)getView().findViewById(R.id.regionBuscar);
        regionBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void init() {
        lblListaManifiestoAsignado = (TextView) getView().findViewById(R.id.lblListaManifiestoAsignado);
        btnSincManifiestosPlanta = (ImageButton) getView().findViewById(R.id.btnSincManifiestosPlanta);
        btnListaAsignadaTransportista = (ImageButton) getView().findViewById(R.id.btnListaAsignadaTransportista);
        btnMenu = (ImageButton) getView().findViewById(R.id.btnMenu);
        btnPickUpTransportista = (ImageView) getView().findViewById(R.id.btnPickUpTransportista);
        lblpickUpTransportista = (TextView) getView().findViewById(R.id.lblpickUpTransportista);
        btnDropOffTransportista = (ImageView) getView().findViewById(R.id.btnDropOffTransportista);
        lblDropOffTransportista = (TextView) getView().findViewById(R.id.lblDropOffTransportista);
        btnInicioRuta = getView().findViewById(R.id.btnInciaRuta);
        btnFinRuta = getView().findViewById(R.id.btnFinRuta);



        btnSincManifiestosPlanta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarHojaRutaTask = new UserConsultarRecolectadosTask(getActivity(),listenerHojaRuta);
                consultarHojaRutaTask.execute();
            }
        });

        btnListaAsignadaTransportista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNavegate(HojaRutaAsignadaPlantaFragment.newInstance());
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

    private void loadCantidadManifiestoAsignado(){

        lblListaManifiestoAsignado.setText(""+ MyApp.getDBO().manifiestoDao().contarHojaRutaProcesada());

    }

}

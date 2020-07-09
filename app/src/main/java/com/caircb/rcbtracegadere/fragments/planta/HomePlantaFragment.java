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
import android.widget.TextView;

import com.caircb.rcbtracegadere.MainActivity;
import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.entity.ParametroEntity;
import com.caircb.rcbtracegadere.dialogs.DialogInicioRuta;
import com.caircb.rcbtracegadere.dialogs.DialogPlacas;
import com.caircb.rcbtracegadere.fragments.Sede.HojaRutaAsignadaSedeFragment;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnHome;
import com.caircb.rcbtracegadere.tasks.UserConsultarHojaRutaPlacaTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarHojaRutaTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarRecolectadosTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomePlantaFragment extends MyFragment implements OnHome {
    ImageButton btnSincManifiestosPlanta,btnListaAsignadaTransportista,btnMenu, btnInicioRuta, btnFinRuta;
    UserConsultarRecolectadosTask consultarHojaRutaTask;
    TextView lblListaManifiestoAsignadoPlanta;
    ImageView btnPickUpTransportista, btnDropOffTransportista;
    DialogPlacas dialogPlacas;
    UserConsultarHojaRutaPlacaTask consultarHojaRutaPlacaTaskTask;
    UserConsultarHojaRutaPlacaTask.TaskListener listenerHojaRutaPlaca;


    public Context mContext;

    ImageButton regionBuscar;
    //ImageButton btnCalculadora;


    UserConsultarHojaRutaPlacaTask.TaskListener listenerHojaRuta = new UserConsultarHojaRutaPlacaTask.TaskListener() {
        @Override
        public void onSuccessful() {
           loadCantidadManifiestoAsignadoNO();
        }
    };

    private void cargarManifiesto(){
        consultarHojaRutaPlacaTaskTask = new UserConsultarHojaRutaPlacaTask(getActivity(),listenerHojaRuta);
        consultarHojaRutaPlacaTaskTask.execute();
    }




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
        //cargarManifiesto();
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
        lblListaManifiestoAsignadoPlanta = (TextView) getView().findViewById(R.id.lblListaManifiestoAsignadoPlanta);
        btnSincManifiestosPlanta = (ImageButton) getView().findViewById(R.id.btnSincManifiestosPlanta);
        btnListaAsignadaTransportista = (ImageButton) getView().findViewById(R.id.btnListaAsignadaTransportistaPlanta);
        btnMenu = (ImageButton) getView().findViewById(R.id.btnMenu);
        btnPickUpTransportista = (ImageView) getView().findViewById(R.id.btnPickUpTransportista);
        btnDropOffTransportista = (ImageView) getView().findViewById(R.id.btnDropOffTransportista);
        btnInicioRuta = getView().findViewById(R.id.btnInciaRuta);
        btnFinRuta = getView().findViewById(R.id.btnFinRuta);

        cargarLabelCantidad();

        btnSincManifiestosPlanta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPlacas = new DialogPlacas(getActivity());
                dialogPlacas.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogPlacas.setCancelable(false);
                dialogPlacas.show();

                //consultarHojaRutaPlacaTaskTask = new UserConsultarHojaRutaPlacaTask(getActivity(),listenerHojaRuta);
                //consultarHojaRutaPlacaTaskTask.execute();
            }
        });


        btnListaAsignadaTransportista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer idVehiculo = Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_vehiculo").getValor());
                //setNavegate(HojaRutaAsignadaPlantaFragment.newInstance());
                String bandera = MyApp.getDBO().parametroDao().fecthParametroValor(idVehiculo.toString(),"vehiculo_planta"+idVehiculo);
                if(bandera!=null){
                    setNavegate(HojaRutaAsignadaPlantaFragment.newInstance());
                }else{
                    setNavegate(HojaRutaAsignadaFragmentNO.newInstance());
                }


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

    private void cargarLabelCantidad(){
        ParametroEntity parametro = MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_vehiculo");
        String valor = parametro == null ? "-1" : parametro.getValor();
        Integer idVehiculo = Integer.parseInt(valor);
        String bandera = MyApp.getDBO().parametroDao().fecthParametroValor(idVehiculo.toString(),"vehiculo_planta"+idVehiculo);

        if(bandera!=null){
            loadCantidadManifiestoAsignado();
        }else{
            loadCantidadManifiestoAsignadoNO();
        }
    }

    private void loadCantidadManifiestoAsignado() {
        lblListaManifiestoAsignadoPlanta.setText(""+ MyApp.getDBO().manifiestoPlantaDao().contarHojaRutaProcesada());
    }


    private void loadCantidadManifiestoAsignadoNO() {
        lblListaManifiestoAsignadoPlanta.setText(""+ MyApp.getDBO().manifiestoDao().contarHojaRutaProcesadaPlanta());
    }

}

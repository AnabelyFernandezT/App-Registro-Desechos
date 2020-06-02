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
import com.caircb.rcbtracegadere.fragments.recolector.HojaRutaAsignadaFragment;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnHome;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.tasks.UserConsultarHojaRutaTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomePlantaFragment extends MyFragment implements OnHome {
    ImageButton btnSincManifiestos,btnListaAsignadaTransportista,btnMenu, btnInicioRuta, btnFinRuta;
    UserConsultarHojaRutaTask consultarHojaRutaTask;
    TextView lblListaManifiestoAsignado, lblpickUpTransportista, lblDropOffTransportista, txtinicioRuta, txtFinRuta;
    ImageView btnPickUpTransportista, btnDropOffTransportista;
    DialogInicioRuta dialogInicioRuta;
    DialogFinRuta dialogFinRuta;
    LinearLayout lnlIniciaRuta,lnlFinRuta;


    public Context mContext;

    Boolean inicioRuta = false;

    int IdTransporteRecolector;
    Integer inicioFinRuta;

    ImageButton regionBuscar;
    //ImageButton btnCalculadora;


    UserConsultarHojaRutaTask.TaskListener listenerHojaRuta = new UserConsultarHojaRutaTask.TaskListener() {
        @Override
        public void onSuccessful() {
           // loadCantidadManifiestoAsignado();
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
    //        loadCantidadManifiestoAsignado();

        return getView();

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

    private void init() {
        lblListaManifiestoAsignado = (TextView) getView().findViewById(R.id.lblListaManifiestoAsignado);
        btnSincManifiestos = (ImageButton) getView().findViewById(R.id.btnSincManifiestos);
        btnListaAsignadaTransportista = (ImageButton) getView().findViewById(R.id.btnListaAsignadaTransportista);
        btnMenu = (ImageButton) getView().findViewById(R.id.btnMenu);
        btnPickUpTransportista = (ImageView) getView().findViewById(R.id.btnPickUpTransportista);
        lblpickUpTransportista = (TextView) getView().findViewById(R.id.lblpickUpTransportista);
        btnDropOffTransportista = (ImageView) getView().findViewById(R.id.btnDropOffTransportista);
        lblDropOffTransportista = (TextView) getView().findViewById(R.id.lblDropOffTransportista);
        btnInicioRuta = getView().findViewById(R.id.btnInciaRuta);
        btnFinRuta = getView().findViewById(R.id.btnFinRuta);
        lnlIniciaRuta = getView().findViewById(R.id.LnlIniciaRuta);
        lnlFinRuta = getView().findViewById(R.id.LnlFinRuta);

        //txtinicioRuta = (TextView)getView().findViewById(R.id.txtIniciarRuta);
        //txtFinRuta = (TextView)getView().findViewById(R.id.txtFinRuta);

        //btnInicioRuta = getView().findViewById(R.id.btnInicioRuta);
        //btnFinRuta = getView().findViewById(R.id.btnFinRuta);

        //btnCalculadora = (ImageButton)getView().findViewById(R.id.btnCalculadora);

        int x= MySession.getIdUsuario();
        RutaInicioFinEntity rut = MyApp.getDBO().rutaInicioFinDao().fechConsultaInicioFinRutasE(MySession.getIdUsuario());
        if(rut!=null){
            inicioFinRuta = rut.getEstado();
            IdTransporteRecolector = MySession.getIdUsuario();

            switch (inicioFinRuta){
                case 1:
                    inicioRuta = true;
                    desbloque_botones();
                    lnlIniciaRuta.setVisibility(View.GONE);
                    lnlFinRuta.setVisibility(View.VISIBLE);

                    break;

                case 2:
                    inicioRuta = false;
                    bloqueo_botones();
                    lnlIniciaRuta.setVisibility(View.VISIBLE);
                    lnlFinRuta.setVisibility(View.GONE);
                    break;
            }

        }else{
            //bloqueo_botones();
        }
        




  /*      if (inicioFinRuta=="0"){
            inicioRuta = true;
            desbloque_botones();
            btnInicioRuta.setEnabled(false);
        }else{
                inicioRuta = false;
                bloqueo_botones();
                btnInicioRuta.setEnabled(true);

        }*/

       // btnInicioRuta.setEnabled(!getMain().getInicioSesion());


        //if (inicioSesion || sesion =="0") {

       //     bloqueo_botones();
        //}

        //------------------------------------------------------------------------------------------
        // EVENTOS....
        //------------------------------------------------------------------------------------------
        /*
        btnCalculadora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBultos = new DialogBultos(getActivity(),listenerInput);
                dialogBultos.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogBultos.setCancelable(false);
                dialogBultos.show();
            }
        });
        */

        btnSincManifiestos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarHojaRutaTask = new UserConsultarHojaRutaTask(getActivity(),listenerHojaRuta);
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


        btnFinRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int conteoManifiestos;

                //dbHelper.open();
                conteoManifiestos = MyApp.getDBO().manifiestoDao().contarHojaRutaAsignadas();
                //dbHelper.close();

                if(conteoManifiestos<0){

                    messageBox("Existen manifiestos pendientes por finalizar");

                }else{

                    openDialog_Fin_App();
                }
            }
        });

    }

    private void loadCantidadManifiestoAsignado(){
        //dbHelper.open();
        lblListaManifiestoAsignado.setText(""+ MyApp.getDBO().manifiestoDao().contarHojaRutaAsignadas());
        //dbHelper.close();
    }

    private void openDialog_Fin_App(){
        dialogFinRuta = new DialogFinRuta(getActivity()) ;
        dialogFinRuta.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogFinRuta.setCancelable(false);
        dialogFinRuta.show();
    }


    private void bloqueo_botones(){

        regionBuscar.setEnabled(false);
        btnSincManifiestos.setEnabled(false);
        btnListaAsignadaTransportista.setEnabled(false);
        btnPickUpTransportista.setEnabled(false);
        btnDropOffTransportista.setEnabled(false);
        //btnFinRuta.setEnabled(false);
    }

    private void desbloque_botones(){

        regionBuscar.setEnabled(true);
        btnSincManifiestos.setEnabled(true);
        btnListaAsignadaTransportista.setEnabled(true);
        btnPickUpTransportista.setEnabled(true);
        btnDropOffTransportista.setEnabled(true);
        //btnFinRuta.setEnabled(true);


    }





}

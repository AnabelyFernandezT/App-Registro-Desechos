package com.caircb.rcbtracegadere.fragments.recolector;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
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
import com.caircb.rcbtracegadere.database.entity.ManifiestoEntity;
import com.caircb.rcbtracegadere.database.entity.RutaInicioFinEntity;
import com.caircb.rcbtracegadere.database.entity.RuteoRecoleccionEntity;
import com.caircb.rcbtracegadere.dialogs.DialogFinRuta;
import com.caircb.rcbtracegadere.dialogs.DialogInicioRuta;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnHome;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.DtoRuteoRecoleccion;
import com.caircb.rcbtracegadere.tasks.UserConsultarHojaRutaTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarInicioRutaTask;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeTransportistaFragment extends MyFragment implements OnHome {
    ImageButton btnSincManifiestos,btnListaAsignadaTransportista,btnMenu, btnInicioRuta, btnFinRuta;
    UserConsultarHojaRutaTask consultarHojaRutaTask;
    TextView lblListaManifiestoAsignado, lblpickUpTransportista, lblDropOffTransportista;
    ImageView btnPickUpTransportista, btnDropOffTransportista;
    DialogInicioRuta dialogInicioRuta;
    DialogFinRuta dialogFinRuta;
    LinearLayout lnlIniciaRuta,lnlFinRuta;
    RutaInicioFinEntity rut;
    UserConsultarInicioRutaTask verificarInicioRutaTask;
    Integer idSubRuta;

    //public Context mContext;

    Boolean inicioRuta = false;

    int IdTransporteRecolector;
    Integer inicioFinRuta;

    ImageButton regionBuscar;
    //ImageButton btnCalculadora;


    UserConsultarHojaRutaTask.TaskListener listenerHojaRuta = new UserConsultarHojaRutaTask.TaskListener() {
        @Override
        public void onSuccessful() {
            loadCantidadManifiestoAsignado();
            loadCantidadManifiestoProcesado();
        }
    };


    public static HomeTransportistaFragment create() {
        return new HomeTransportistaFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setView(inflater.inflate(R.layout.fragment_home_transportista, container, false));

        initBuscador();
        init();
        loadCantidadManifiestoAsignado();
        loadCantidadManifiestoProcesado();

        return getView();

    }

    private void initBuscador(){
        regionBuscar = (ImageButton)getView().findViewById(R.id.regionBuscar);
        regionBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNavegate(HojaRutaBuscarFragment.newInstance());
                //setNavegate(BuscarFragment.create());
                //setNavegate(HomeRecepcionFragment.create());
            }
        });
    }

    private void init() {
        //consultarInicioFinRuta();
        MyApp.getDBO().parametroDao().saveOrUpdate("ruteoRecoleccion", "NO");
        lblListaManifiestoAsignado = getView().findViewById(R.id.lblListaManifiestoAsignado);
        btnSincManifiestos = getView().findViewById(R.id.btnSincManifiestos);
        btnListaAsignadaTransportista = getView().findViewById(R.id.btnListaAsignadaTransportista);
        btnMenu = getView().findViewById(R.id.btnMenu);
        btnPickUpTransportista = getView().findViewById(R.id.btnPickUpTransportista);
        lblpickUpTransportista = getView().findViewById(R.id.lblpickUpTransportista);
        btnDropOffTransportista = getView().findViewById(R.id.btnDropOffTransportista);
        lblDropOffTransportista =  getView().findViewById(R.id.lblDropOffTransportista);
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
        rut = MyApp.getDBO().rutaInicioFinDao().fechConsultaInicioFinRutasE(MySession.getIdUsuario());
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
            bloqueo_botones();
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
                //Valido si el parametro esta en NO si es verdadero presento el modal
                rut = MyApp.getDBO().rutaInicioFinDao().fechConsultaInicioFinRutasE(MySession.getIdUsuario());
                idSubRuta = Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_ruta").getValor());
                if(MyApp.getDBO().manifiestoDao().contarHojaRutaAsignadasPara(idSubRuta,MySession.getIdUsuario()) >0 ){
                    List<RuteoRecoleccionEntity> enty = MyApp.getDBO().ruteoRecoleccion().searchRuteoRecoleccion();
                    if(MyApp.getDBO().parametroDao().fecthParametroValorByNombre("ruteoRecoleccion").equals("NO")){

                        AlertDialog.Builder builderDialog= new AlertDialog.Builder(getActivity());
                        builderDialog.setMessage("¿Desea iniciar trazlado al proximo punto de recolección ?");
                        builderDialog.setCancelable(false);
                        builderDialog.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                MyApp.getDBO().parametroDao().saveOrUpdate("ruteoRecoleccion", "SI");

                                setNavegate(HojaRutaAsignadaFragment.newInstance());

                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builderDialog.create();
                        dialog.show();

                    }else{
                        setNavegate(HojaRutaAsignadaFragment.newInstance());
                    }
                }else{
                    messageBox("No dispone de manifiestos para recolectar.!");
                }

                //setNavegate(HojaRutaAsignadaFragment.newInstance());
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

        btnPickUpTransportista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNavegate(HojaRutaProcesadaFragment.newInstance());
            }
        });

        btnInicioRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInicioRuta = new DialogInicioRuta(getActivity());
                dialogInicioRuta.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogInicioRuta.setCancelable(false);
                dialogInicioRuta.show();
            }


               // openDialog_InicioApp(getMain().getInicioSesion());
                /*
                btnInicioRuta.setVisibility(View.GONE);
                txtinicioRuta.setVisibility(View.GONE);
                btnFinRuta.setVisibility(View.VISIBLE);
                txtFinRuta.setVisibility(View.VISIBLE);*/



        });

        btnFinRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int conteoManifiestos;

                conteoManifiestos = MyApp.getDBO().manifiestoDao().contarHojaRutaAsignadasPara(rut.getIdSubRuta(),MySession.getIdUsuario());
                if(conteoManifiestos>0){
                    messageBox("Existen manifiestos pendientes por recolectar");
                }else{

                    openDialog_Fin_App();
                }
            }
        });

    }

    private void loadCantidadManifiestoAsignado(){
        //dbHelper.open();
        //lblListaManifiestoAsignado.setText(""+ MyApp.getDBO().manifiestoDao().contarHojaRutaAsignadasPara(idSubRuta == null ? 0:idSubRuta,MySession.getIdUsuario()));
        lblListaManifiestoAsignado.setText(""+ MyApp.getDBO().manifiestoDao().contarHojaRutaAsignadas());
        //dbHelper.close();
    }

    private void loadCantidadManifiestoProcesado(){
        //dbHelper.open();
        //lblpickUpTransportista.setText(""+ MyApp.getDBO().manifiestoDao().contarHojaRutaProcesadaPara(idSubRuta == null ? 0:idSubRuta,MySession.getIdUsuario()));
        lblpickUpTransportista.setText(""+ MyApp.getDBO().manifiestoDao().contarHojaRutaAsignadasP(MySession.getIdUsuario()));
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

    private void consultarInicioFinRuta(){
        verificarInicioRutaTask = new UserConsultarInicioRutaTask(getActivity());
        verificarInicioRutaTask.execute();
    }

}

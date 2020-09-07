package com.caircb.rcbtracegadere.fragments.GestorAlterno;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.caircb.rcbtracegadere.MainActivity;
import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnHome;
import com.caircb.rcbtracegadere.helpers.MyConstant;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.RowItemManifiestosDetalleGestores;
import com.caircb.rcbtracegadere.tasks.UserConsultarHojaRutaTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarLotePadreTask;

import java.util.List;

public class HomeGestorAlternoFragment extends MyFragment implements OnHome {

    ImageButton btnSincManifiestos,btnListaAsignadaTransportista,btnMenu, btnInicioRuta, btnFinRuta;
    UserConsultarHojaRutaTask consultarHojaRutaTask;
    TextView lblListaManifiestoAsignadoGestor;
    ImageButton regionBuscar;
    UserConsultarLotePadreTask consultarLotePadre;
    private List<RowItemManifiestosDetalleGestores> copia;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setView(inflater.inflate(R.layout.fragment_home_gestor_alterno, container, false));
        init();
        initBuscador();
        //datosManifiestosAsignados();
        return getView();
    }

    private void init() {

        lblListaManifiestoAsignadoGestor = getView().findViewById(R.id.lblListaManifiestoAsignadoGestor);
        btnSincManifiestos = getView().findViewById(R.id.btnSincLotePadre);
        btnListaAsignadaTransportista = getView().findViewById(R.id.btnListaAsignadaTransportista);
        btnMenu = getView().findViewById(R.id.btnMenu);
        datosManifiestosAsignados();
        btnListaAsignadaTransportista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNavegate(HojaRutaAsignadaGestorFragment.newInstance());
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

        btnSincManifiestos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datosManifiestosAsignados();
            }
        });

       /* btnSincManifiestos.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                datosManifiestosAsignados();
            }
        });*/
    }

    private void datosManifiestosAsignados(){
        /*consultarLotePadre = new UserConsultarLotePadreTask(getActivity());
        consultarLotePadre.execute();*/

        manifiestosAsignados();
        loadCopiaManifiestos();
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

    private void manifiestosAsignados(){
        lblListaManifiestoAsignadoGestor.setText("" + MyApp.getDBO().manifiestoDao().contarHojaRutaAsignadasParaGestor(MySession.getIdUsuario(), MyConstant.ID_ENTREGA_GESTOR));
    }

    public static HomeGestorAlternoFragment create(){
        return new HomeGestorAlternoFragment();
    }

    private void loadCopiaManifiestos (){
        copia = MyApp.getDBO().manifiestoDao().copiaManifiestosRecolectados(MyConstant.ID_ENTREGA_GESTOR);
        if(copia.size()>0){
                MyApp.getDBO().lotePadreDao().saveOrUpdate(copia);

        }

    }
}

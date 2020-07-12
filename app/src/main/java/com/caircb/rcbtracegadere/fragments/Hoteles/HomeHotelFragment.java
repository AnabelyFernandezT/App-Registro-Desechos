package com.caircb.rcbtracegadere.fragments.Hoteles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.caircb.rcbtracegadere.MainActivity;
import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.dialogs.DialogFinRuta;
import com.caircb.rcbtracegadere.fragments.GestorAlterno.HojaRutaAsignadaGestorFragment;
import com.caircb.rcbtracegadere.fragments.Sede.HomeSedeFragment;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnHome;
import com.caircb.rcbtracegadere.models.ItemLoteHoteles;
import com.caircb.rcbtracegadere.tasks.UserConsultarHojaRutaTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarLotesHotelesTask;

import java.util.List;

public class HomeHotelFragment extends MyFragment implements OnHome {

    ImageButton btnSincManifiestos,btnListaAsignadaTransportista,btnMenu;
    TextView lblListaManifiestoAsignado;
    UserConsultarLotesHotelesTask lotesConsultar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setView(inflater.inflate(R.layout.fragment_home_hotel, container, false));
        buscarLotes();
        init();
        return getView();
    }

    private void init() {
        lblListaManifiestoAsignado = getView().findViewById(R.id.lblListaManifiestoAsignadoPlanta);
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

        List<ItemLoteHoteles>  rowItems = MyApp.getDBO().loteHotelesDao().fetchLotesAsigando();
        if( rowItems !=null && rowItems.size()>0){
            lblListaManifiestoAsignado.setText(String.valueOf(rowItems.size()));
        }
    }

    private void buscarLotes(){
        lotesConsultar = new UserConsultarLotesHotelesTask(getActivity());
        lotesConsultar.setmOnCountListaAsignaadasListeneer(new UserConsultarLotesHotelesTask.onCountListaAsigandasListenner() {
            @Override
            public void onSuccesfull(Integer total) {
                System.out.println("dadada");
            }
        });
        lotesConsultar.execute();
    }
    public static HomeHotelFragment create(){
        return new HomeHotelFragment();
    }
}

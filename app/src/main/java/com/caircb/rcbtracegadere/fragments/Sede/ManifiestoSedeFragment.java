package com.caircb.rcbtracegadere.fragments.Sede;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost;

import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.fragments.GestorAlterno.HojaRutaAsignadaGestorFragment;
import com.caircb.rcbtracegadere.fragments.GestorAlterno.RecepcionGestorFragment;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnCameraListener;
import com.caircb.rcbtracegadere.tasks.UserRegistrarPlanta;


public class ManifiestoSedeFragment extends MyFragment implements OnCameraListener, View.OnClickListener {
    LinearLayout btnManifiestoCancel, btnManifiestoNext;
    private static final String ARG_PARAM1 = "manifiestoID";
    RecepcionGestorFragment manifiestoGestor;
    Integer idAppManifiesto;
    UserRegistrarPlanta userRegistrarPlanta;


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRetornarDetalleSede:
                setNavegate(HojaRutaAsignadaSedeFragment.newInstance());
                break;
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            idAppManifiesto = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public void onCameraResult(int requestCode, int resultCode, Intent data) {
        if(manifiestoGestor!=null && ((requestCode>=301 && requestCode<=304) ||(requestCode>=201 && requestCode<=204))) {
            manifiestoGestor.setMakePhoto(requestCode);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setView(inflater.inflate(R.layout.fragment_hoja_ruta_sede, container, false));
        init();
        return getView();
    }

    public void init(){
        btnManifiestoCancel = getView().findViewById(R.id.btnRetornarDetalleSede);
        btnManifiestoCancel.setOnClickListener(this);
        manifiestoGestor = new RecepcionGestorFragment(getActivity(),idAppManifiesto);
    }



    public static ManifiestoSedeFragment newInstance() {
        return new ManifiestoSedeFragment();
    }

    public static ManifiestoSedeFragment newInstance(Integer manifiestoID) {
        ManifiestoSedeFragment f= new ManifiestoSedeFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_PARAM1,manifiestoID);
        f.setArguments(b);
        return f;
    }
}

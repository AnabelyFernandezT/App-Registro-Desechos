package com.caircb.rcbtracegadere.fragments.recolector.manifiesto2;

import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost;

import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.fragments.recolector.HojaRutaAsignadaFragment;
import com.caircb.rcbtracegadere.generics.MyFragment;


public class Manifiesto2Fragment extends MyFragment implements View.OnClickListener {

    LinearLayout btnManifiestoCancel;
    TabManifiestoGeneral tabManifiestoGeneral;
    public Manifiesto2Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setView(inflater.inflate(R.layout.fragment_hoja_ruta2, container, false));
        init();
        return getView();
    }

    private void init(){

        btnManifiestoCancel = getView().findViewById(R.id.btnManifiestoCancel);
        btnManifiestoCancel.setOnClickListener(this);

        //tabManifiestoGeneral = getView().findViewById(R.id.tab1);
        //tabManifiestoGeneral.setManifiestoID(3);


        TabHost tabs=(TabHost)getView().findViewById(android.R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec=tabs.newTabSpec("GENERAL");
        spec.setContent(new TabHost.TabContentFactory() {
                            public View createTabContent(String tag) {
                                tabManifiestoGeneral = new TabManifiestoGeneral(getActivity(),2);
                                //tv.setText("The Text of " + tag);
                                return tabManifiestoGeneral;
                            }
                        });
        spec.setIndicator("GENERAL");
        tabs.addTab(spec);
/*
        spec=tabs.newTabSpec("DETALLE");
        spec.setContent(R.id.tab2);
        spec.setIndicator("DETALLE");
        tabs.addTab(spec);

        spec=tabs.newTabSpec("ADICIONALES");
        spec.setContent(R.id.tab3);
        spec.setIndicator("ADICIONALES");
        tabs.addTab(spec);
*/
        tabs.setCurrentTab(0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnManifiestoCancel:
                setNavegate(HojaRutaAsignadaFragment.newInstance());
                break;
            case R.id.btnManifiestoNext:
                //vista preliminar...
                //setNavegate(VistaPreliminarFragment.newInstance(idAppManifiesto));
                break;
        }
    }
}
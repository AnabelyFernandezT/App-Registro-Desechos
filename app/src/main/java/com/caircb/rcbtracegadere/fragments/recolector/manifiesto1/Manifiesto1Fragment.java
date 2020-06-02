package com.caircb.rcbtracegadere.fragments.recolector.manifiesto1;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.viewpager.widget.ViewPager;

import com.caircb.rcbtracegadere.MainActivity;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.fragments.recolector.HojaRutaAsignadaFragment;
import com.caircb.rcbtracegadere.fragments.recolector.HomeTransportistaFragment;
import com.caircb.rcbtracegadere.fragments.recolector.manifiesto.VistaPreliminarFragment;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Manifiesto1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Manifiesto1Fragment extends MyFragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "manifiestoID";

    ViewPager viewPager;
    TabLayout tabLayout;
    FragmentAdapter pagerAdapter;
    ArrayList<androidx.fragment.app.Fragment> fragments;
    Integer idAppManifiesto;
    LinearLayout btnManifiestoCancel;


    public static Manifiesto1Fragment newInstance(Integer manifiestoID) {
        Manifiesto1Fragment f= new Manifiesto1Fragment();
        Bundle b = new Bundle();
        b.putInt(ARG_PARAM1,manifiestoID);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            idAppManifiesto = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setView(inflater.inflate(R.layout.fragment_hoja_ruta, container, false));
        setHideHeader();
        init();
        return getView();
    }

    private void init(){
        btnManifiestoCancel = getView().findViewById(R.id.btnManifiestoCancel);
        btnManifiestoCancel.setOnClickListener(this);

        viewPager = (ViewPager) getView().findViewById(R.id.pager);
        tabLayout = (TabLayout) getView().findViewById(R.id.tabLayout);

        fragments =new ArrayList<>();

        fragments.add(new TabFragmentGeneral());
        fragments.add(TabFragmentDetalle.newInstance(idAppManifiesto,0));
        fragments.add(new TabFragmentAdicional());


        pagerAdapter = new FragmentAdapter(((MainActivity)getActivity()).getSupportFragmentManager(), getActivity().getApplicationContext(), fragments);
        viewPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("GENERAL");
        tabLayout.getTabAt(1).setText("DETALLE");
        tabLayout.getTabAt(2).setText("ADICIONAL");

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnManifiestoCancel:
                setNavegate(HojaRutaAsignadaFragment.newInstance());
                break;
            case R.id.btnManifiestoNext:
                //vista preliminar...
                //setNavegate(VistaPreliminarFragment.newInstance(idAppManifiesto));
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //viewPager.destroyDrawingCache();

        tabLayout.removeAllTabs();
    }
}
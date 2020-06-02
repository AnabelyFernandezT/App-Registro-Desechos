package com.caircb.rcbtracegadere.fragments.recolector.manifiesto1;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.caircb.rcbtracegadere.MainActivity;
import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.adapters.ManifiestoAdapter;
import com.caircb.rcbtracegadere.components.SearchView;
import com.caircb.rcbtracegadere.fragments.recolector.HomeTransportistaFragment;
import com.caircb.rcbtracegadere.fragments.recolector.manifiesto.ManifiestoFragment;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnRecyclerTouchListener;
import com.caircb.rcbtracegadere.models.ItemManifiesto;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Manifiesto1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Manifiesto1Fragment extends MyFragment implements View.OnClickListener {

    ViewPager viewPager;
    TabLayout tabLayout;
    ArrayList<androidx.fragment.app.Fragment> fragments;


    public static Manifiesto1Fragment newInstance() {
        return new Manifiesto1Fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setView(inflater.inflate(R.layout.fragment_hoja_ruta, container, false));
        setHideHeader();
        initTab();
        return getView();
    }

    private void initTab(){
        viewPager = (ViewPager) getView().findViewById(R.id.pager);
        tabLayout = (TabLayout) getView().findViewById(R.id.tabLayout);

        fragments =new ArrayList<>();

        fragments.add(new HomeFragment());
        fragments.add(new HomeFragment());
        fragments.add(new HomeFragment());


        FragmentAdapter pagerAdapter = new FragmentAdapter(((MainActivity)getActivity()).getSupportFragmentManager(), getActivity().getApplicationContext(), fragments);
        viewPager.setAdapter(pagerAdapter);


        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("Hola 1");
        tabLayout.getTabAt(1).setText("Hola 2");
        tabLayout.getTabAt(2).setText("Hola 3");

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRetornarListHojaRuta:
                setNavegate(HomeTransportistaFragment.create());
                break;
        }
    }



}
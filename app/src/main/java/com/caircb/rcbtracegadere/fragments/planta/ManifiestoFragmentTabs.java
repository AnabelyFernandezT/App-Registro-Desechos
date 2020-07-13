package com.caircb.rcbtracegadere.fragments.planta;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.entity.CatalogoEntity;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.fragments.recolector.HojaRutaAsignadaFragment;
import com.caircb.rcbtracegadere.fragments.planta.TabManifiestoAdicionalFragment;
import com.caircb.rcbtracegadere.fragments.planta.TabManifiestoDetalleFragment;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnCameraListener;
import com.caircb.rcbtracegadere.models.ItemManifiestoDetalleSede;
import com.caircb.rcbtracegadere.tasks.UserConsultarManifiestosPlantaTask;
import com.caircb.rcbtracegadere.tasks.UserRegisterPlantaDetalleTask;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManifiestoFragmentTabs#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManifiestoFragmentTabs extends MyFragment implements OnCameraListener, View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "idAppManifiesto";
    private static final String ARG_PARAM2 = "Manifiestobloqueado";
    private static final String ARG_PARAM3 = "numeroManifiesto";

    private Integer manifiestoID;
    private Boolean bloqueado;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LinearLayout btnManifiestoCancel,btnManifiestoNext;
    private FragmentActivity myContext;
    UserRegisterPlantaDetalleTask userRegisterPlantaDetalleTask;
    //TabManifiestoGeneralFragment tab1;
    TabManifiestoDetalleFragment tab2;
    TabManifiestoAdicionalFragment tab3;
    boolean firma = false;
    String numeroManifiesto;
    DialogBuilder builder;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param manifiestoID Parameter 1.
     * @return A new instance of fragment ManifiestoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManifiestoFragmentTabs newInstance(Integer manifiestoID, String numeroManifiesto) {
        ManifiestoFragmentTabs fragment = new ManifiestoFragmentTabs();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, manifiestoID);
        args.putString(ARG_PARAM3, numeroManifiesto);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            manifiestoID= getArguments().getInt(ARG_PARAM1);
            numeroManifiesto = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setView(inflater.inflate(R.layout.fragment_manifiesto_planta, container, false));
        init();
        return getView();
    }

    private void init(){

        btnManifiestoCancel= getView().findViewById(R.id.btnManifiestoCancel);
        btnManifiestoNext=getView().findViewById(R.id.btnManifiestoNext);
        btnManifiestoCancel.setOnClickListener(this);
        btnManifiestoNext.setOnClickListener(this);

        tabLayout = (TabLayout) getView().findViewById(R.id.tabLayout);
        //tabLayout.addTab(tabLayout.newTab().setText("GENERAL"));
        tabLayout.addTab(tabLayout.newTab().setText("DETALLE"));
        tabLayout.addTab(tabLayout.newTab().setText("ADICIONALES"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) getView().findViewById(R.id.pager);

        Pager adapter = new Pager(myContext.getSupportFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);

        //Adding onTabSelectedListener to swipe views
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        //viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        Integer estadoManifiesto = MyApp.getDBO().manifiestoPlantaDao().obtenerEstadoManifiesto(manifiestoID);
        if(estadoManifiesto.equals(3)){
            btnManifiestoNext.setEnabled(false);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnManifiestoCancel:
                setNavegate(HojaRutaAsignadaPlantaFragment.newInstance());
                break;
            case R.id.btnManifiestoNext:
                //vista preliminar...
               // setNavegate(VistaPreliminarFragment.newInstance(manifiestoID));
                ItemManifiestoDetalleSede detalles;
                detalles = MyApp.getDBO().manifiestoPlantaDetalleDao().fetchManifiestosValidaInformacion(manifiestoID);
                Integer bultos = detalles.getTotalBultos();
                Integer bultosSeleccionados = detalles.getBultosSelecionado();
                    firma = tab3.validarInformacion();
                    if(!firma){
                        messageBox("Debe registrar la firma.!");
                    }else{
                        if(bultos.equals(bultosSeleccionados)) {
                            registroPlantaDetalle();
                        }
                        else {
                            messageBox("Seleccione todos los bultos.!");
                        }
                    }
                break;
        }
    }

    private void  registroPlantaDetalle(){
        builder = new DialogBuilder(getActivity());
        builder.setMessage("¿Esta seguro que desea continuar?");
        builder.setCancelable(true);
        builder.setPositiveButton("SI", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String observacion = tab3.sendObservacion()==null ? "": tab3.sendObservacion();
                userRegisterPlantaDetalleTask = new UserRegisterPlantaDetalleTask(getActivity(), manifiestoID, observacion, numeroManifiesto);
                userRegisterPlantaDetalleTask.setmOnRegisterPlantaDetalleListener(new UserRegisterPlantaDetalleTask.onRegisterPlantaDetalleListenner() {
                    @Override
                    public void OnSucessfull() {
                        setNavegate(HojaRutaAsignadaPlantaFragment.create());
                    }
                });
                userRegisterPlantaDetalleTask.execute();
                builder.dismiss();
            }
        });
        builder.setNegativeButton("NO", new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCameraResult(int requestCode, int resultCode, Intent data) {
        if(tab3!=null && ((requestCode>=101 && requestCode<=104) ||(requestCode>=201 && requestCode<=204))){
            tab3.setMakePhoto(requestCode);
        }else if( tab3 !=null && (requestCode>=1601 && requestCode<=1604) || (requestCode>=1601 && (requestCode<=1604))){
            tab3.setMakePhoto(requestCode);
        }
    }

    public class Pager extends FragmentStatePagerAdapter {

        //integer to count number of tabs
        int tabCount;

        //Constructor to the class
        public Pager(FragmentManager fm, int tabCount) {
            super(fm);
            //Initializing tab count
            this.tabCount= tabCount;
        }

        //Overriding method getItem
        @Override
        public androidx.fragment.app.Fragment getItem(int position) {
            //Returning the current tabs
            switch (position) {

                case 0:
                    tab2 = TabManifiestoDetalleFragment.newInstance(manifiestoID);
                    return tab2;
                case 1:
                    tab3 = TabManifiestoAdicionalFragment.newInstance(manifiestoID);
                    return tab3;
                default:
                    return null;
            }
        }

        //Overriden method getCount to get the number of tabs
        @Override
        public int getCount() {
            return tabCount;
        }

    }
}
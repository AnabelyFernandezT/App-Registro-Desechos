package com.caircb.rcbtracegadere.fragments.planta;

import android.app.Activity;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.entity.CatalogoEntity;
import com.caircb.rcbtracegadere.database.entity.ConsultarFirmaUsuarioEntity;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.fragments.recolector.HojaRutaAsignadaFragment;
import com.caircb.rcbtracegadere.fragments.planta.TabManifiestoAdicionalFragment;
import com.caircb.rcbtracegadere.fragments.planta.TabManifiestoDetalleFragment;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnCameraListener;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.ItemManifiestoDetalleSede;
import com.caircb.rcbtracegadere.models.ItemManifiestoSede;
import com.caircb.rcbtracegadere.tasks.UserConsultaFirmaUsuarioTask;
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
    private static final String ARG_PARAM4 = "pesajePendiente";

    private Integer manifiestoID;
    private LinearLayout btnManifiestoCancel,btnManifiestoNext;
    private FragmentActivity myContext;
    UserRegisterPlantaDetalleTask userRegisterPlantaDetalleTask;
    TabManifiestoDetalleFragment tab2;
    TabManifiestoAdicionalFragment tab3;
    TabHost tabs;

    boolean firma = false, novedad=false;
    String numeroManifiesto;
    String pesajePendiente;
    DialogBuilder builder;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param manifiestoID Parameter 1.
     * @return A new instance of fragment ManifiestoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManifiestoFragmentTabs newInstance(Integer manifiestoID, String numeroManifiesto, String pesajePendiente) {
        ManifiestoFragmentTabs fragment = new ManifiestoFragmentTabs();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, manifiestoID);
        args.putString(ARG_PARAM3, numeroManifiesto);
        args.putString(ARG_PARAM4, pesajePendiente);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            manifiestoID= getArguments().getInt(ARG_PARAM1);
            numeroManifiesto = getArguments().getString(ARG_PARAM3);
            pesajePendiente = getArguments().getString(ARG_PARAM4);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setView(inflater.inflate(R.layout.fragment_manifiesto_planta, container, false));
        init();
        initTab();
        return getView();
    }

    private void initTab(){
        inicializeTab();

        tabs=(TabHost)getView().findViewById(android.R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec=tabs.newTabSpec("DETALLE");
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return tab2;
            }
        });
        spec.setIndicator("DETALLE");
        tabs.addTab(spec);

        spec=tabs.newTabSpec("FIRMA");
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return tab3;
            }
        });
        spec.setIndicator("FIRMA");
        tabs.addTab(spec);

        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                if(s.equals("FIRMA")){
                    tab3.validarPesoExtra();
                }
            }
        });

    }

    private void inicializeTab(){
        tab2 = new TabManifiestoDetalleFragment(getActivity(), manifiestoID);
        tab3 = new TabManifiestoAdicionalFragment(getActivity(), manifiestoID, pesajePendiente);
    }

    private void init(){

        btnManifiestoCancel= getView().findViewById(R.id.btnManifiestoCancel);
        btnManifiestoNext=getView().findViewById(R.id.btnManifiestoNext);
        btnManifiestoCancel.setOnClickListener(this);
        btnManifiestoNext.setOnClickListener(this);
        Integer estadoManifiesto = MyApp.getDBO().manifiestoPlantaDao().obtenerEstadoManifiesto(manifiestoID);
        if(estadoManifiesto.equals(3)){
            btnManifiestoNext.setEnabled(false);
        }

    }
    @Override
    public void onClick(View v) {
        tabs=(TabHost)getView().findViewById(android.R.id.tabhost);
        switch (v.getId()){
            case R.id.btnManifiestoCancel:
                if(pesajePendiente.equals("NO")){
                    setNavegate(HojaRutaAsignadaPlantaFragment.newInstance());
                }else {
                    setNavegate(HojaRutaPlantaPendientesPesoFragment.newInstance());
                }

                break;
            case R.id.btnManifiestoNext:
                ItemManifiestoSede detalles;
                detalles = MyApp.getDBO().manifiestoPlantaDao().fetchManifiestosAsigByTotal(manifiestoID);
                Integer bultos = detalles.getTotalBultos();
                Integer bultosSeleccionados = detalles.getBultosSelecionado();
                firma = tab3.validarInformacion();
                novedad = tab3.validarNovedad();

                if(pesajePendiente.equals("NO")){
                    if(!firma){
                        messageBox("Debe registrar la firma");
                    }else{
                        if(novedad){
                            if(bultos.equals(bultosSeleccionados)) {
                                registroPlantaDetalle();
                            }
                            else {
                                messageBox("Seleccione todos los bultos");
                            }
                        }else{
                            messageBox("Ingrese una foto");
                        }
                    }
                }else{
                    if(novedad){
                        if(bultos.equals(bultosSeleccionados)) {
                            registroPlantaDetalle();
                        }
                        else {
                            messageBox("Seleccione todos los bultos");
                        }
                    }else{
                        messageBox("Ingrese una foto");
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
                userRegisterPlantaDetalleTask = new UserRegisterPlantaDetalleTask(getActivity(), manifiestoID, observacion, numeroManifiesto, pesajePendiente.equals("NO")?1:3);
                userRegisterPlantaDetalleTask.setmOnRegisterPlantaDetalleListener(new UserRegisterPlantaDetalleTask.onRegisterPlantaDetalleListenner() {
                    @Override
                    public void OnSucessfull() {
                        if(pesajePendiente.equals("NO")){
                            setNavegate(HojaRutaAsignadaPlantaFragment.create());
                        }else{
                            setNavegate(HojaRutaPlantaPendientesPesoFragment.create());
                        }
                        String firmaUsuario = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_firma_usuario") == null ? "" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_firma_usuario");
                        if (firmaUsuario.equals("")){
                            consultarFirma();
                        }

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

    private void consultarFirma(){
        UserConsultaFirmaUsuarioTask consultaFirmaUsuarioTask = new UserConsultaFirmaUsuarioTask(getActivity(), MySession.getIdUsuario());
        consultaFirmaUsuarioTask.execute();
        ConsultarFirmaUsuarioEntity consultarFirmaUsuarioEntity= MyApp.getDBO().consultarFirmaUsuarioDao().fetchFirmaUsuario2();
        String firmaUsuario=consultarFirmaUsuarioEntity==null?"":(consultarFirmaUsuarioEntity.getFirmaBase64()==null?"":consultarFirmaUsuarioEntity.getFirmaBase64());
        System.out.println(firmaUsuario);
        MyApp.getDBO().parametroDao().saveOrUpdate("current_firma_usuario",firmaUsuario);
    }

}
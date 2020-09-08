package com.caircb.rcbtracegadere.fragments.planta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.adapters.PesosAdapterPlantaLote;
import com.caircb.rcbtracegadere.database.entity.ConsultarFirmaUsuarioEntity;
import com.caircb.rcbtracegadere.database.entity.RecepcionQrPlantaEntity;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.fragments.recolector.HojaRutaAsignadaFragment;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnCameraListener;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.ItemManifiestoSede;
import com.caircb.rcbtracegadere.models.ItemQrDetallePlanta;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoPlantaObservacion;
import com.caircb.rcbtracegadere.tasks.UserConsultaFirmaUsuarioTask;
import com.caircb.rcbtracegadere.tasks.UserRegistrarFinLoteHospitalesTask;
import com.caircb.rcbtracegadere.tasks.UserRegistrarPlanta;
import com.caircb.rcbtracegadere.tasks.UserRegistrarPlantaQrTask;

import java.util.List;

public class RecepcionLotePlantaFragment extends MyFragment implements View.OnClickListener, OnCameraListener {

    TabGeneralLotePlanta tabGeneral;
    TabFirmaLotePlanta tabFirma;
    private FragmentActivity myContext;
    private LinearLayout btnManifiestoCancel, btnManifiestoNext;
    private RecepcionQrPlantaEntity recepcionQrPlantaEntity;
    TabHost tabs;
    boolean firma = false, novedad = false;
    UserRegistrarPlantaQrTask userRegistrarPlantaQrTask;
    UserRegistrarFinLoteHospitalesTask userRegistrarFinLoteHospitales;

    public static RecepcionLotePlantaFragment newInstance() {
        RecepcionLotePlantaFragment fragment = new RecepcionLotePlantaFragment();
       /* Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, manifiestoID);
        args.putString(ARG_PARAM3, numeroManifiesto);
        args.putString(ARG_PARAM4, pesajePendiente);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            manifiestoID= getArguments().getInt(ARG_PARAM1);
            numeroManifiesto = getArguments().getString(ARG_PARAM3);
            pesajePendiente = getArguments().getString(ARG_PARAM4);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setView(inflater.inflate(R.layout.recepcion_lote_planta, container, false));
        init();
        initTab();
        //initItems();
        return getView();
    }

    @Override
    public void onCameraResult(int requestCode, int resultCode, Intent data) {
        if (tabFirma != null && ((requestCode >= 101 && requestCode <= 104) || (requestCode >= 201 && requestCode <= 204))) {
            tabFirma.setMakePhoto(requestCode);
        } else if (tabFirma != null && (requestCode >= 1601 && requestCode <= 1604) || (requestCode >= 1601 && (requestCode <= 1604))) {
            tabFirma.setMakePhoto(requestCode);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    private void init() {
        recepcionQrPlantaEntity = MyApp.getDBO().recepcionQrPlantaDao().fetchHojaRutaQrPlanta();
        btnManifiestoCancel = getView().findViewById(R.id.btnManifiestoCancel);
        btnManifiestoNext = getView().findViewById(R.id.btnManifiestoNext);
        btnManifiestoCancel.setOnClickListener(this);
        btnManifiestoNext.setOnClickListener(this);
    }


    private void initTab() {
        inicializeTab();

        tabs = (TabHost) getView().findViewById(android.R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec = tabs.newTabSpec("DETALLE");
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return tabGeneral;
            }
        });
        spec.setIndicator("DETALLE");
        tabs.addTab(spec);

        spec = tabs.newTabSpec("FIRMA");
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return tabFirma;
            }
        });
        spec.setIndicator("FIRMA");
        tabs.addTab(spec);

        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {

            }
        });

    }

    private void inicializeTab() {
        tabGeneral = new TabGeneralLotePlanta(getActivity());
        tabFirma = new TabFirmaLotePlanta(getActivity());
    }

    @Override
    public void onClick(View v) {
        tabs = (TabHost) getView().findViewById(android.R.id.tabhost);
        switch (v.getId()) {
            case R.id.btnManifiestoCancel:
                int i = tabs.getCurrentTab();
                if (i == 0) {
                    //setNavegate(HomePlantaFragment.create());
                    break;
                }
                if (i == 1) {
                    tabs.setCurrentTab(tabs.getCurrentTab() - 1);
                    break;
                }
                /*if(pesajePendiente.equals("NO")){
                    setNavegate(HojaRutaAsignadaPlantaFragment.newInstance());
                }else {
                    setNavegate(HojaRutaPlantaPendientesPesoFragment.newInstance());
                }*/
                break;
            case R.id.btnManifiestoNext:
                int j = tabs.getCurrentTab();
                if (j == 0) {
                    tabs.setCurrentTab(tabs.getCurrentTab() + 1);
                }
                if (j == 1) {

                    firma = tabFirma.validarInformacion();
                    novedad = tabFirma.validarNovedad();

                    if (!firma) {
                        messageBox("Debe registrar la firma");
                    } else {
                        if (novedad) {
                            registroPlantaLote();
                        } else {
                            messageBox("Ingrese una foto");
                        }
                    }
                }

                break;
        }
    }

    private void registroPlantaLote() {

        final DialogBuilder dialogBuilder = new DialogBuilder(myContext);
        dialogBuilder.setMessage("¿Está seguro de continuar?");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("SI", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
                String observacion = tabFirma.sendObservacion() == null ? "" : tabFirma.sendObservacion();
                String observacionPeso = tabFirma.sendObservacionPeso() == null ? "" : tabFirma.sendObservacionPeso();
                userRegistrarPlantaQrTask = new UserRegistrarPlantaQrTask(myContext, recepcionQrPlantaEntity, observacion, observacionPeso);
                userRegistrarPlantaQrTask.setOnRegisterListener(new UserRegistrarPlantaQrTask.OnRegisterListener() {
                    @Override
                    public void onSuccessful() {
                        String[] array = recepcionQrPlantaEntity.getNumerosManifiesto().split(",");
                        int idManifiestoPrimero = Integer.parseInt(array[0]);
                        if (array.length > 1) {
                            for (String s : array) {
                                int idManifiestoConsulta = Integer.parseInt(s.replace(" ", ""));
                                MyApp.getDBO().manifiestoPlantaObservacionesDao().eliminarObtenerObservaciones(idManifiestoConsulta);
                                MyApp.getDBO().manifiestoFileDao().deleteFotoByIdAppManifistoCatalogo(idManifiestoConsulta, -1);
                            }
                        } else {
                            MyApp.getDBO().manifiestoPlantaObservacionesDao().eliminarObtenerObservaciones(idManifiestoPrimero);
                            MyApp.getDBO().manifiestoFileDao().deleteFotoByIdAppManifistoCatalogo(idManifiestoPrimero, -1);
                        }
                        String firmaUsuario = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_firma_usuario") == null ? "" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_firma_usuario");
                        if (firmaUsuario.equals("")) {
                            consultarFirma();
                        }

                        String idSubRuta = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_idSubruta") == null ? "0" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_idSubruta");
                        System.out.println(idSubRuta);
                        if (!idSubRuta.equals("0")) {
                            int idSubRutaEnviar = Integer.parseInt(idSubRuta);
                            userRegistrarFinLoteHospitales = new UserRegistrarFinLoteHospitalesTask(myContext, idSubRutaEnviar, 0, 4);
                            userRegistrarFinLoteHospitales.setOnFinLoteListener(new UserRegistrarFinLoteHospitalesTask.OnFinLoteListener() {
                                @Override
                                public void onSuccessful() {
                                    String idVehiculo = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_vehiculo") == null ? "" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_vehiculo");
                                    MyApp.getDBO().parametroDao().saveOrUpdate("vehiculo_planta" + idVehiculo, "" + 0);
                                    setNavegate(HomePlantaFragment.create());
                                }

                                @Override
                                public void onFailure() {

                                }
                            });
                            userRegistrarFinLoteHospitales.execute();
                        }
                    }
                });
                userRegistrarPlantaQrTask.execute();
            }
        });
        dialogBuilder.setNegativeButton("NO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        });
        dialogBuilder.show();
    }

    private void consultarFirma() {
        UserConsultaFirmaUsuarioTask consultaFirmaUsuarioTask = new UserConsultaFirmaUsuarioTask(myContext, MySession.getIdUsuario());
        consultaFirmaUsuarioTask.execute();
        ConsultarFirmaUsuarioEntity consultarFirmaUsuarioEntity = MyApp.getDBO().consultarFirmaUsuarioDao().fetchFirmaUsuario2();
        String firmaUsuario = consultarFirmaUsuarioEntity == null ? "" : (consultarFirmaUsuarioEntity.getFirmaBase64() == null ? "" : consultarFirmaUsuarioEntity.getFirmaBase64());
        System.out.println(firmaUsuario);
        MyApp.getDBO().parametroDao().saveOrUpdate("current_firma_usuario", firmaUsuario);
    }
}

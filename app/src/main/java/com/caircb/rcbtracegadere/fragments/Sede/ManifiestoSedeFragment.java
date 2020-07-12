package com.caircb.rcbtracegadere.fragments.Sede;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.adapters.DialogMenuBaseAdapter;
import com.caircb.rcbtracegadere.adapters.ManifiestoDetalleAdapter;
import com.caircb.rcbtracegadere.adapters.ManifiestoDetalleAdapterSede;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.dialogs.DialogBultosSede;
import com.caircb.rcbtracegadere.dialogs.DialogPlacaSede;
import com.caircb.rcbtracegadere.fragments.GestorAlterno.HojaRutaAsignadaGestorFragment;
import com.caircb.rcbtracegadere.fragments.GestorAlterno.RecepcionGestorFragment;
import com.caircb.rcbtracegadere.fragments.planta.HojaRutaAsignadaPlantaFragment;
import com.caircb.rcbtracegadere.fragments.recolector.manifiesto2.Manifiesto2Fragment;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnCameraListener;
import com.caircb.rcbtracegadere.models.ItemManifiestoDetalleSede;
import com.caircb.rcbtracegadere.models.MenuItem;
import com.caircb.rcbtracegadere.models.RowItemManifiesto;
import com.caircb.rcbtracegadere.tasks.UserRegistarDetalleSedeTask;
import com.caircb.rcbtracegadere.tasks.UserRegistarFinLoteTask;
import com.caircb.rcbtracegadere.tasks.UserRegistrarPlanta;

import java.util.ArrayList;
import java.util.List;


public class ManifiestoSedeFragment extends MyFragment implements OnCameraListener, View.OnClickListener {
    LinearLayout btnManifiestoCancel,btnRegistrar;
    private RecyclerView recyclerView;
    private static final String ARG_PARAM1 = "manifiestoID";
    RecepcionGestorFragment manifiestoGestor;
    Integer idAppManifiesto,estadoManifiesto;
    UserRegistrarPlanta userRegistrarPlanta;
    ManifiestoDetalleAdapterSede recyclerviewAdapter;
    private List<ItemManifiestoDetalleSede> detalles;
    Dialog dialogOpcioneItem;
    DialogMenuBaseAdapter dialogMenuBaseAdapter;
    ListView LtsManifiestoDetalle,mDialogMenuItems;
    DialogBultosSede dialogBultos;
    UserRegistarDetalleSedeTask detalleSedeTask;

    public  ManifiestoSedeFragment (){
    }

    public static ManifiestoSedeFragment newInstance(Integer manifiestoID) {
        ManifiestoSedeFragment f= new ManifiestoSedeFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_PARAM1,manifiestoID);
        f.setArguments(b);
        return f;

    }

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
        loadData();
        return getView();
    }

    public void init(){
        btnManifiestoCancel = getView().findViewById(R.id.btnRetornarDetalleSede);
        btnManifiestoCancel.setOnClickListener(this);
        recyclerView = getView().findViewById(R.id.recyclerview);
        recyclerviewAdapter = new ManifiestoDetalleAdapterSede(getActivity(),idAppManifiesto.toString(),1);
        manifiestoGestor = new RecepcionGestorFragment(getActivity(),idAppManifiesto);
        btnRegistrar = getView().findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer loteContenedor = Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_inicio_lote").getValor());
                if(loteContenedor!=null){
                    final DialogBuilder dialogBuilder = new DialogBuilder(getActivity());
                    dialogBuilder.setCancelable(false);
                    dialogBuilder.setMessage("¿Esta seguro de continuar ?");
                    dialogBuilder.setPositiveButton("SI", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismiss();
                            detalleSedeTask = new UserRegistarDetalleSedeTask(getActivity());
                            detalleSedeTask.setOnRegisterListener(new UserRegistarDetalleSedeTask.OnRegisterListener() {
                                @Override
                                public void onSuccessful() {
                                    messageBox("Bultos Guardados");
                                }
                    detalleSedeTask = new UserRegistarDetalleSedeTask(getActivity());
                    detalleSedeTask.setOnRegisterListener(new UserRegistarDetalleSedeTask.OnRegisterListener() {
                        @Override
                        public void onSuccessful() {
                            messageBox("Bultos Guardados");
                            setNavegate(HojaRutaAsignadaSedeFragment.newInstance());
                        }


                                @Override
                                public void onFail() {
                                    messageBox("Bultos No Guardados");
                                }
                            });
                            detalleSedeTask.execute();
                        }
                    });
                    dialogBuilder.setNegativeButton("NO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismiss();
                        }
                    });
                    dialogBuilder.show();
                }else {
                    messageBox("Debe Iniciar Lote");
                }

            }
        });
    }

    private void loadData(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        detalles = MyApp.getDBO().manifiestoDetalleSede().fetchManifiestosAsigByClienteOrNumManif(idAppManifiesto);
        //Integer numeroSelecionado = MyApp.getDBO().manifiestoDetalleValorSede().fetchNumeroTotalAsigByManifiesto(idAppManifiesto);
        recyclerviewAdapter.setTaskList(detalles);
        recyclerView.setAdapter(recyclerviewAdapter);

        recyclerviewAdapter.setOnItemClickListener(new ManifiestoDetalleAdapterSede.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                    openOpcionesItems(detalles.get(position).getIdManifiestoDetalle(),position);
            }
        });
    }


    private void openOpcionesItems(final Integer idManifiestoDetalle,Integer position){
        dialogBultos = new DialogBultosSede(getActivity(),idManifiestoDetalle);
        dialogBultos.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogBultos.setCancelable(false);
        dialogBultos.setmOnclickSedeListener(new DialogBultosSede.onclickSedeListener() {
            @Override
            public void onSucefull() {
                List<ItemManifiestoDetalleSede> detalles;
                detalles = MyApp.getDBO().manifiestoDetalleSede().fetchManifiestosAsigByClienteOrNumManif(idAppManifiesto);
                //Integer numeroSelecionado = MyApp.getDBO().manifiestoDetalleValorSede().fetchNumeroTotalAsigByManifiesto(idAppManifiesto);
                recyclerviewAdapter.setTaskList(detalles);

            }
        });
        dialogBultos.show();
    }

    public static ManifiestoSedeFragment newInstance() {
        return new ManifiestoSedeFragment();
    }

}

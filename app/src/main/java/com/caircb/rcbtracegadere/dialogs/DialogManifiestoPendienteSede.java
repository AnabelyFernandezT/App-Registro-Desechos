package com.caircb.rcbtracegadere.dialogs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;

import com.caircb.rcbtracegadere.adapters.ManifiestoPendienteSedeAdapter;
import com.caircb.rcbtracegadere.generics.MyDialog;
import com.caircb.rcbtracegadere.models.ItemManifiestoPendiente;
import com.caircb.rcbtracegadere.models.response.DtoCatalogo;
import com.caircb.rcbtracegadere.tasks.UserRegistrarManifiestosPendientesSedeTask;

import java.util.ArrayList;
import java.util.List;

public class DialogManifiestoPendienteSede extends MyDialog {
    Activity _activity;
    Spinner spnAutorizacion;
    List<String> lstAutorizacion;
    LinearLayout btnAceptar;
    private RecyclerView recyclerView;
    ManifiestoPendienteSedeAdapter recyclerviewAdapter;
    private List<ItemManifiestoPendiente> manifiesto;
    private Integer idManifiesto, idLoteSede;

    public interface onRegister{
        public void onSuccessful();
    }

    public onRegister mOnRegister;

    public DialogManifiestoPendienteSede(Context context, Integer idManifiesto, Integer idLoteSede) {
        super(context, R.layout.dialog_sede_planta);
        this._activity = (Activity)context;
        this.idManifiesto = idManifiesto;
        this.idLoteSede = idLoteSede;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getView());
        init();
        loadData();
    }

    private void init() {
        recyclerView = getView().findViewById(R.id.recyclerview);
        recyclerviewAdapter = new ManifiestoPendienteSedeAdapter(getActivity());
        btnAceptar = getView().findViewById(R.id.btnAceptarIngresoBultos);
        spnAutorizacion = getView().findViewById(R.id.spn_listaAutorizacion);
        spnAutorizacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>0){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        cargarData();
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserRegistrarManifiestosPendientesSedeTask registrarManifiestosPendientesSedeTask = new UserRegistrarManifiestosPendientesSedeTask(getActivity(),idManifiesto, idLoteSede);
                registrarManifiestosPendientesSedeTask.setmOnRegistro(new UserRegistrarManifiestosPendientesSedeTask.OnRegistro() {
                    @Override
                    public void onSuccessful() {
                        if(mOnRegister!=null)mOnRegister.onSuccessful();
                    }
                });
                registrarManifiestosPendientesSedeTask.execute();
                DialogManifiestoPendienteSede.this.dismiss();
            }
        });
    }

    private void loadData(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        manifiesto = MyApp.getDBO().manifiestoSedePlantaDao().fetchManifiestoPendiente(idManifiesto);

        recyclerviewAdapter.setTaskList(manifiesto);
        recyclerView.setAdapter(recyclerviewAdapter);

    }

    public Spinner cargarSpinnerDestino(Spinner spinner, List<DtoCatalogo> catalogos, boolean bhabilitar){
        ArrayAdapter<String> adapter;
        Spinner defaulSpiner = spinner;
        ArrayList<String> listaData = new ArrayList<String>();

        //listaRutas = MyApp.getDBO().rutasDao().fetchConsultarRutas();
        listaData.add("SELECCIONE");
        if(catalogos.size() > 0){
            for (DtoCatalogo r : catalogos){
                listaData.add(r.getNombre());
            }
        }

        adapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_spinner, listaData);
        defaulSpiner.setAdapter(adapter);
        defaulSpiner.setEnabled(bhabilitar);

        return defaulSpiner;
    }

    private void cargarData (){
        List <DtoCatalogo> dtoCatalogo = MyApp.getDBO().catalogoDao().fetchConsultarCatalogobyTipo(17);

        if(dtoCatalogo.size()>0){
            cargarSpinnerDestino(spnAutorizacion,dtoCatalogo,true);
        }

    }

    public void setOnRegisterListener(@Nullable onRegister l){ mOnRegister = l;}

}

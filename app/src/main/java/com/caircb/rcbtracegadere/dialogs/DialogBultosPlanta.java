package com.caircb.rcbtracegadere.dialogs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.adapters.ManifiestoDetalleBultosAdapterPlanta;
import com.caircb.rcbtracegadere.adapters.ManifiestoDetalleBultosAdapterSede;
import com.caircb.rcbtracegadere.fragments.planta.TabManifiestoAdicionalFragment;
import com.caircb.rcbtracegadere.generics.MyDialog;
import com.caircb.rcbtracegadere.models.ItemManifiestoDetalleValorSede;
import com.caircb.rcbtracegadere.models.response.DtoCatalogo;
import com.caircb.rcbtracegadere.tasks.UserConsultarHojaRutaPlacaTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarPlacasInicioRutaDisponible;

import java.util.ArrayList;
import java.util.List;

public class DialogBultosPlanta extends MyDialog {
    Activity _activity;
    List<DtoCatalogo> listaPlacasDisponibles;
    Integer idAppManifiestoDet;
    LinearLayout btnAceptarIngresoBultos;
    UserConsultarHojaRutaPlacaTask consultarHojaRutaTask;
    ManifiestoDetalleBultosAdapterPlanta recyclerviewAdapter;
    TextView lblListaManifiestoAsignado;
    private RecyclerView recyclerView;
    private List<ItemManifiestoDetalleValorSede> detalles;

    public DialogBultosPlanta(@NonNull Context context, Integer idAppManifiestoDet) {
        super(context, R.layout.dialog_bultos_sede);
        this._activity = (Activity)context;
        this.idAppManifiestoDet= idAppManifiestoDet;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getView());
        init();
        loadData();
    }

    public interface onclickSedeListener {
        public void onSucefull();
    }

    private onclickSedeListener mOnclickSedeListener;

    private void init() {
        recyclerView = getView().findViewById(R.id.recyclerview);
        recyclerviewAdapter = new ManifiestoDetalleBultosAdapterPlanta(getActivity(),idAppManifiestoDet,1);

        listaPlacasDisponibles = new ArrayList<>();
        lblListaManifiestoAsignado = getActivity().findViewById(R.id.lblListaManifiestoAsignadoPlanta);
        btnAceptarIngresoBultos = (LinearLayout)getView().findViewById(R.id.btnAceptarIngresoBultos);

        btnAceptarIngresoBultos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean bandera = false;
                List<ItemManifiestoDetalleValorSede> lista = MyApp.getDBO().manifiestoPlantaDetalleValorDao().fetchManifiestosAsigByClienteOrNumManif(idAppManifiestoDet);
                for(ItemManifiestoDetalleValorSede reg: lista){
                    if(reg.getNuevoPeso() != null && !(reg.getEstado())) {
                        messageBox("Debe seleccionar los bultos faltantes");
                        bandera = true;
                        return;
                    }
                }
                if(!bandera){
                    String nn = "";
                    DialogBultosPlanta.this.dismiss();
                    if(mOnclickSedeListener!=null){
                        mOnclickSedeListener.onSucefull();
                    }
                }
            }
        });

        //datosPlacasDisponibles();
    }

    private void loadData(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        detalles = MyApp.getDBO().manifiestoPlantaDetalleValorDao().fetchManifiestosAsigByClienteOrNumManif(idAppManifiestoDet);

        recyclerviewAdapter.setTaskList(detalles);
        recyclerView.setAdapter(recyclerviewAdapter);
    }


    UserConsultarHojaRutaPlacaTask.TaskListener listenerHojaRuta = new UserConsultarHojaRutaPlacaTask.TaskListener() {
        @Override
        public void onSuccessful() {
            loadCantidadManifiestoAsignado();
        }
    };

    private void loadCantidadManifiestoAsignado() {
        lblListaManifiestoAsignado.setText(""+ MyApp.getDBO().manifiestoDao().contarHojaRutaProcesada());
    }

    private void cargarManifiesto(){
        consultarHojaRutaTask = new UserConsultarHojaRutaPlacaTask(_activity,listenerHojaRuta);
        consultarHojaRutaTask.execute();
    }


    public void setmOnclickSedeListener(@NonNull onclickSedeListener l){
        mOnclickSedeListener = l;
    };

}

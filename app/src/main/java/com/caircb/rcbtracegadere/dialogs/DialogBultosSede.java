package com.caircb.rcbtracegadere.dialogs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.adapters.ManifiestoDetalleAdapterSede;
import com.caircb.rcbtracegadere.adapters.ManifiestoDetalleBultosAdapterSede;
import com.caircb.rcbtracegadere.database.entity.CatalogoEntity;
import com.caircb.rcbtracegadere.generics.MyDialog;
import com.caircb.rcbtracegadere.models.ItemManifiestoDetalleSede;
import com.caircb.rcbtracegadere.models.ItemManifiestoDetalleValorSede;
import com.caircb.rcbtracegadere.models.response.DtoCatalogo;
import com.caircb.rcbtracegadere.tasks.UserConsultarHojaRutaPlacaTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarPlacasInicioRutaDisponible;

import java.util.ArrayList;
import java.util.List;

public class DialogBultosSede extends MyDialog {
    Activity _activity;
    Spinner spinnerPlacas;
    List<DtoCatalogo> listaPlacasDisponibles;
    String placa;
    Integer idAppManifiestoDet;
    UserConsultarPlacasInicioRutaDisponible consultarPlacasInicioRutaDisponible;
    LinearLayout btnIngresarApp, btnCancelarApp;
    UserConsultarHojaRutaPlacaTask consultarHojaRutaTask;
    ManifiestoDetalleBultosAdapterSede recyclerviewAdapter;
    TextView lblListaManifiestoAsignado;
    private RecyclerView recyclerView;
    private List<ItemManifiestoDetalleValorSede> detalles;

    public DialogBultosSede(@NonNull Context context,Integer idAppManifiestoDet) {
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
        recyclerviewAdapter = new ManifiestoDetalleBultosAdapterSede(getActivity(),idAppManifiestoDet,1);

        listaPlacasDisponibles = new ArrayList<>();
        lblListaManifiestoAsignado = getActivity().findViewById(R.id.lblListaManifiestoAsignadoPlanta);
        btnCancelarApp = (LinearLayout)getView().findViewById(R.id.btnIniciaRutaCancel);
        btnIngresarApp = (LinearLayout)getView().findViewById(R.id.btnIniciaRutaAplicar);

      btnCancelarApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBultosSede.this.dismiss();
                if(mOnclickSedeListener!=null){
                    mOnclickSedeListener.onSucefull();
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

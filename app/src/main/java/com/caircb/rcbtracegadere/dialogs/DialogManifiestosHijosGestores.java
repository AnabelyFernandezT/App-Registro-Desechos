package com.caircb.rcbtracegadere.dialogs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.adapters.ListaValoresAdapter;
import com.caircb.rcbtracegadere.adapters.ManifiestoDetalleGestoresAdapter;
import com.caircb.rcbtracegadere.adapters.ManifiestoHijoGestorAdapter;
import com.caircb.rcbtracegadere.generics.MyDialog;
import com.caircb.rcbtracegadere.models.RowItemManifiestosDetalleGestores;

import java.text.DecimalFormat;
import java.util.List;

public class DialogManifiestosHijosGestores extends MyDialog {

    Activity _activity;
    Integer idManifiesto,idManifiestoDetalle, idTipoDesecho;
    ManifiestoHijoGestorAdapter recyclerviewAdapter;
    LinearLayout btnIniciaRutaCancel, btnAplicar;
    private RecyclerView listViewBultos;
    List<RowItemManifiestosDetalleGestores> detalles;
    String numeroManifiesto, descripcion;
    Double peso=0.0, cantidadBultos=0.0;
    TextView txtDescripcion;

    public DialogManifiestosHijosGestores(@NonNull Context context,Integer idManifiesto,Integer idManifiestoDetalle, Integer idTipoDesecho, String numeroManifiesto, String descripcion) {
        super(context, R.layout.dialog_manifiesto_detalle_gestores_si);
        this._activity = (Activity)context;
        this.idManifiesto = idManifiesto;
        this.idManifiestoDetalle = idManifiestoDetalle;
        this.idTipoDesecho = idTipoDesecho;
        this.numeroManifiesto = numeroManifiesto;
        this.descripcion = descripcion;
    }

    public interface OnRegistrarBultoListener {
        public void onSuccesfull(String numeroBultos, Integer idManifiestoDetalle, String pesoBulto);
    }
    public interface OnCancelarBultoListener {
        public void onSuccesfull();
    }

    private OnRegistrarBultoListener mOnRegistrarBultoListener;
    private OnCancelarBultoListener mOnCancelarBultoListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getView());
        init();
        loadData();
    }

    private void init(){
        listViewBultos = getView().findViewById(R.id.recyclerview);
        //recyclerviewAdapter = new ManifiestoHijoGestorAdapter(getContext());
        txtDescripcion = getView().findViewById(R.id.txtDescripcion);
        btnIniciaRutaCancel = getView().findViewById(R.id.btnIniciaRutaCancel);
        btnAplicar =  getView().findViewById(R.id.btnAplicar);
        btnIniciaRutaCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCancelarBultoListener.onSuccesfull();
            }
        });

        btnAplicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                asociarManifiestoPadre();
                mOnRegistrarBultoListener.onSuccesfull(cantidadBultos.toString(),idManifiestoDetalle,peso.toString());
            }
        });
    }

    private void loadData(){
        txtDescripcion.setText(descripcion);
        listViewBultos.setLayoutManager(new LinearLayoutManager(getActivity()));
        listViewBultos.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        //detalles = MyApp.getDBO().manifiestoDao().manifiestosHijos(idTipoDesecho, idManifiesto);
        detalles = MyApp.getDBO().lotePadreDao().fetchManifiestosRecolectadosByDetalle(idTipoDesecho);
        recyclerviewAdapter = new ManifiestoHijoGestorAdapter(getActivity(),detalles,idManifiestoDetalle,idTipoDesecho, idManifiesto);
        listViewBultos.setAdapter(recyclerviewAdapter);

    }

    private void asociarManifiestoPadre(){
        detalles =MyApp.getDBO().lotePadreDao().fetchManifiestosRecolectadosByDetalle(idTipoDesecho);
        if(detalles.size()>0){
            for(RowItemManifiestosDetalleGestores reg:detalles){
                if(reg.getCheckHijo()){
                    peso= peso + reg.getPeso();
                    cantidadBultos = cantidadBultos + reg.getBultos();
                    MyApp.getDBO().lotePadreDao().asociarManifiestoPadre(idManifiesto,idManifiestoDetalle,numeroManifiesto,idTipoDesecho);
                }
            }
            obtieneDosDecimales(peso);
        }

    }

    private String obtieneDosDecimales(double valor) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(valor);
    }

    public void setmOnRegistrarBultoListener(@Nullable OnRegistrarBultoListener l) {
        mOnRegistrarBultoListener = l;
    }
    public void setmOnCancelarBultoListener(@Nullable OnCancelarBultoListener l) {
        mOnCancelarBultoListener = l;
    }
}

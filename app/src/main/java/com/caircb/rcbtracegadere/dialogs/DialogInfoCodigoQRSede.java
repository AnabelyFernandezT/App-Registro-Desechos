package com.caircb.rcbtracegadere.dialogs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.generics.MyDialog;
import com.caircb.rcbtracegadere.models.ItemManifiestoPlantaCodigoQR;
import com.caircb.rcbtracegadere.models.response.DtoCatalogo;
import com.caircb.rcbtracegadere.tasks.UserConsultarHojaRutaPlacaTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarManifiestosPlantaTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarVehiculosSedeTask;

import java.util.ArrayList;
import java.util.List;

public class DialogInfoCodigoQRSede extends MyDialog {
    Activity _activity;
    Spinner spinnerPlacas;
    List<DtoCatalogo> listaPlacasDisponibles;
    String placa,codigoQR;
    Integer idPlaca;
    //UserConsultarPlacasInicioRutaDisponible consultarPlacasInicioRutaDisponible;
    LinearLayout btnIngresarApp, btnCancelarApp;
    UserConsultarHojaRutaPlacaTask consultarHojaRutaTask;
    TextView lblListaManifiestoAsignado;
    TextView txtManifiesto,txtPeso,txtNumBultos,txtCliente,txtDescripcion;

    public DialogInfoCodigoQRSede(@NonNull Context context, String codigoQR) {
        super(context, R.layout.dialog_info_codigo_qr_sede);
        this._activity = (Activity)context;
        this.codigoQR = codigoQR;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getView());

        init();
    }

    public interface onclickSedeListener {
        public void onSucefull();
    }

    private DialogBultosPlanta.onclickSedeListener mOnclickSedeListener;

    private void init() {
        txtManifiesto= (TextView)getView().findViewById(R.id.txtNumManifiesto);
        txtPeso= (TextView)getView().findViewById(R.id.txtPeso);
        txtNumBultos= (TextView)getView().findViewById(R.id.txtTotalBultos);
        txtCliente= (TextView)getView().findViewById(R.id.txtCliente);
        txtDescripcion= (TextView)getView().findViewById(R.id.txtItemDescripcion);

        listaPlacasDisponibles = new ArrayList<>();
        lblListaManifiestoAsignado = getActivity().findViewById(R.id.lblListaManifiestoAsignadoPlanta);
        btnCancelarApp = (LinearLayout)getView().findViewById(R.id.btnIniciaRutaCancel);
        btnIngresarApp = (LinearLayout)getView().findViewById(R.id.btnIniciaRutaAplicar);

       btnCancelarApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInfoCodigoQRSede.this.dismiss();

            }
        });
        btnIngresarApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    MyApp.getDBO().manifiestoDetalleValorSede().actualizarBultoEstado(codigoQR,true);
                    DialogInfoCodigoQRSede.this.dismiss();
                    if (mOnclickSedeListener != null) {
                        mOnclickSedeListener.onSucefull();
                    }
            }
        });

        obtenerItemManifiesto();
    }

    private void obtenerItemManifiesto(){
        ItemManifiestoPlantaCodigoQR item = MyApp.getDBO().manifiestoSedeDao().fetchManifiestosBultos(codigoQR);
        System.out.print(item);
        txtCliente.setText(item.getNombreCliente());
        txtManifiesto.setText(item.getNumeroManifiesto());
        txtNumBultos.setText(item.getBultosSelecionado()+"/"+item.getTotalBultos());
        txtPeso.setText(""+item.getPeso());
        txtDescripcion.setText(item.getNombreDesecho());
    }

    UserConsultarHojaRutaPlacaTask.TaskListener listenerHojaRuta = new UserConsultarHojaRutaPlacaTask.TaskListener() {
        @Override
        public void onSuccessful() {
            loadCantidadManifiestoAsignado();
        }
    };


    private void loadCantidadManifiestoAsignado() {
        lblListaManifiestoAsignado.setText(""+ MyApp.getDBO().manifiestoPlantaDao().contarHojaRutaProcesada());
    }



    public void setmOnclickSedeListener(@NonNull DialogBultosPlanta.onclickSedeListener l){
        mOnclickSedeListener = l;
    };



}

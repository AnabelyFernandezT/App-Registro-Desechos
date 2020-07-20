package com.caircb.rcbtracegadere.dialogs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.entity.InformacionModulosEntity;
import com.caircb.rcbtracegadere.generics.MyDialog;

public class DialogInformacionTransportista extends MyDialog {
    Activity _activity;
    TextView lblApertura1, lblApertura2, lblCierre1, lblCierre2, lblTelefonoCliente, lblObservaciones;
    CheckBox chkFiscalizacionNo, chkFiscalizacionSi, chkFiscalizacionArcsa, chkFiscalizacionMi, chkDevolucionRecipienteSi, chkDevolucionRecipienteNo, chkMontacargasSi, chkMontacargasNo, chkBalanzaSi,
            chkBalanzaNo, chkPresenciadoSi, chkPresenciadoNo;
    LinearLayout btnRetornarMenu;
    int position = 0;
    private Integer apertura1 = 0;
    private Integer apertura2 = 0;
    private Integer cierre1 = 0;
    private Integer cierre2 = 0;

    public DialogInformacionTransportista(@NonNull Context context, Integer apertura1, Integer apertura2, Integer cierre1, Integer cierre2) {
        super(context, R.layout.dialog_informacion_transportista);
        this._activity = (Activity) context;
        this.position = position;
        this.apertura1 = apertura1;
        this.apertura2 = apertura2;
        this.cierre1 = cierre1;
        this.cierre2 = cierre2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getView());
        init();
        datosInformacionModulo();
    }

    private void init() {
        lblApertura1 = getView().findViewById(R.id.lblApertura1);
        lblApertura2 = getView().findViewById(R.id.lblApertura2);
        lblCierre1 = getView().findViewById(R.id.lblCierre1);
        lblCierre2 = getView().findViewById(R.id.lblCierre2);
        lblTelefonoCliente = getView().findViewById(R.id.lblTelefonoCliente);
        btnRetornarMenu = getView().findViewById(R.id.btnRetornarMenu);

        chkFiscalizacionNo = getView().findViewById(R.id.chkFiscalizacionNo);
        chkFiscalizacionSi = getView().findViewById(R.id.chkFiscalizacionSi);
        chkFiscalizacionArcsa = getView().findViewById(R.id.chkFiscalizacionArcsa);
        chkFiscalizacionMi = getView().findViewById(R.id.chkFiscalizacionMi);
        chkDevolucionRecipienteSi = getView().findViewById(R.id.chkDevolucionRecipienteSi);
        chkDevolucionRecipienteNo = getView().findViewById(R.id.chkDevolucionRecipienteNo);
        chkMontacargasSi = getView().findViewById(R.id.chkMontacargasSi);
        chkMontacargasNo = getView().findViewById(R.id.chkMontacargasNo);
        chkBalanzaSi = getView().findViewById(R.id.chkBalanzaSi);
        chkBalanzaNo = getView().findViewById(R.id.chkBalanzaNo);
        chkPresenciadoSi = getView().findViewById(R.id.chkPresenciadoSi);
        chkPresenciadoNo = getView().findViewById(R.id.chkPresenciadoNo);
        lblObservaciones = getView().findViewById(R.id.lblObservaciones);

        //datosCabecera();

        btnRetornarMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInformacionTransportista.this.dismiss();
            }
        });

    }

    private void datosCabecera() {
        lblApertura1.setText(apertura1);
        lblApertura2.setText(apertura2);
        lblCierre1.setText(cierre1);
        lblCierre2.setText(cierre2);
    }

    private void datosInformacionModulo() {
        InformacionModulosEntity informacionModulos = MyApp.getDBO().informacionModulosDao().fetchInformacionModulos2();
        if (informacionModulos.getObservacionResiduos().equals(null)) {
            lblObservaciones.setText("");
        } else {
            lblObservaciones.setText(informacionModulos.getObservacionResiduos());
        }
        if (informacionModulos.getResiduoSujetoFiscalizacion() == 0) {
            chkFiscalizacionNo.setChecked(true);
            chkFiscalizacionSi.setSelected(false);
                   /* chkFiscalizacionArcsa.setText();
                    chkFiscalizacionMi.setText();*/
        } else {
            chkFiscalizacionNo.setSelected(false);
            chkFiscalizacionSi.setSelected(true);
        }
        if (informacionModulos.getRequiereDevolucionRecipientes() == 0) {
            chkDevolucionRecipienteSi.setChecked(false);
            chkDevolucionRecipienteNo.setChecked(true);
        } else {
            chkDevolucionRecipienteSi.setChecked(true);
            chkDevolucionRecipienteNo.setChecked(false);
        }
        if (informacionModulos.getTieneDisponibilidadMontacarga() == 0) {
            chkMontacargasSi.setChecked(false);
            chkMontacargasNo.setChecked(true);
        } else {
            chkMontacargasSi.setChecked(true);
            chkMontacargasNo.setChecked(false);
        }
        if (informacionModulos.getTieneDisponibilidadBalanza() == 0) {
            chkBalanzaSi.setChecked(false);
            chkBalanzaNo.setChecked(true);
        } else {
            chkBalanzaSi.setChecked(true);
            chkBalanzaNo.setChecked(false);
        }
        if (informacionModulos.getRequiereIncineracionPresenciada() == 0) {
            chkPresenciadoSi.setChecked(false);
            chkPresenciadoNo.setChecked(true);
        } else {
            chkPresenciadoSi.setChecked(true);
            chkPresenciadoNo.setChecked(false);
        }
    }
}

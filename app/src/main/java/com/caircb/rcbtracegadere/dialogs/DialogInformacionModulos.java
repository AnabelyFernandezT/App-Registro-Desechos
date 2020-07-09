package com.caircb.rcbtracegadere.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MainActivity;
import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.entity.InformacionModulosEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoEntity;
import com.caircb.rcbtracegadere.fragments.Sede.HojaRutaAsignadaSedeFragment;
import com.caircb.rcbtracegadere.fragments.Sede.HomeSedeFragment;
import com.caircb.rcbtracegadere.generics.MyDialog;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnHome;
import com.caircb.rcbtracegadere.helpers.MyConstant;
import com.caircb.rcbtracegadere.models.ItemInformacionModulos;
import com.caircb.rcbtracegadere.models.ItemLote;
import com.caircb.rcbtracegadere.models.response.DtoCatalogo;
import com.caircb.rcbtracegadere.models.response.DtoFindRutas;
import com.caircb.rcbtracegadere.models.response.DtoInformacionModulos;
import com.caircb.rcbtracegadere.tasks.UserConsultaLotes;
import com.caircb.rcbtracegadere.tasks.UserDestinoEspecificoTask;
import com.caircb.rcbtracegadere.tasks.UserInformacionModulosTask;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class DialogInformacionModulos extends MyDialog {
    Activity _activity;
    UserInformacionModulosTask informacionModulosTaskl;
    TextView lblRutaInfo,lblSubrutaInfo,lblPlacaInfo,lblChoferInfo,lblAuxiliarRecoleccion1Info,lblAuxiliarRecoleccion2Info,lblKilometrajeInfo;
    CheckBox chkFiscalizacionNo,chkFiscalizacionSi,chkFiscalizacionArcsa,chkFiscalizacionMi,chkDevolucionRecipienteSi,chkDevolucionRecipienteNo,chkMontacargasSi,chkMontacargasNo
            ,chkBalanzaSi,chkBalanzaNo,chkPresenciadoSi,chkPresenciadoNo;
    LinearLayout btnRetornarMenu;
    List<DtoInformacionModulos> listaInformacionModulos;
    private List<ItemInformacionModulos> rowItems;


    public DialogInformacionModulos(@NonNull Context context) {
        super(context, R.layout.dialog_informacion_modulos);
        this._activity = (Activity)context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getView());
        init();
        datosInformacionModulo();
    }

    private void init() {
        listaInformacionModulos = new ArrayList<>();
        lblRutaInfo = getView().findViewById(R.id.lblRutaInfo);
        lblSubrutaInfo = getView().findViewById(R.id.lblSubrutaInfo);
        lblPlacaInfo = getView().findViewById(R.id.lblPlacaInfo);
        lblChoferInfo = getView().findViewById(R.id.lblChoferInfo);
        lblAuxiliarRecoleccion1Info = getView().findViewById(R.id.lblAuxiliarRecoleccion1Info);
        lblAuxiliarRecoleccion2Info = getView().findViewById(R.id.lblAuxiliarRecoleccion2Info);
        lblKilometrajeInfo = getView().findViewById(R.id.lblKilometrajeInfo);

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
        btnRetornarMenu = getView().findViewById(R.id.btnRetornarMenu);

        btnRetornarMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInformacionModulos.this.dismiss();
            }
        });


    }


    private void datosInformacionModulo(){
        InformacionModulosEntity informacionModulos = MyApp.getDBO().informacionModulosDao().fetchInformacionModulos2();
            if (informacionModulos!=null){
                lblRutaInfo.setText(informacionModulos.getRuta());
                lblSubrutaInfo.setText(informacionModulos.getSubruta());
                lblPlacaInfo.setText(informacionModulos.getPlaca());
                lblChoferInfo.setText(informacionModulos.getChofer());
                lblAuxiliarRecoleccion1Info.setText(informacionModulos.getAuxiliarRecoleccion1());
                lblAuxiliarRecoleccion2Info.setText(informacionModulos.getAuxiliarRecoleccion2());
                lblKilometrajeInfo.setText(informacionModulos.getKilometrajeInicio().toString());

                if (informacionModulos.getResiduoSujetoFiscalizacion()==0){
                    chkFiscalizacionNo.setChecked(true);
                    chkFiscalizacionSi.setSelected(false);
              /*chkFiscalizacionArcsa.setText();
                chkFiscalizacionMi.setText();*/
                }else {
                    chkFiscalizacionNo.setSelected(false);
                    chkFiscalizacionSi.setSelected(true);
                }
                if (informacionModulos.getRequiereDevolucionRecipientes()==0){
                    chkDevolucionRecipienteSi.setChecked(false);
                    chkDevolucionRecipienteNo.setChecked(true);
                }else {
                    chkDevolucionRecipienteSi.setChecked(true);
                    chkDevolucionRecipienteNo.setChecked(false);
                }
                if (informacionModulos.getTieneDisponibilidadMontacarga()==0){
                    chkMontacargasSi.setChecked(false);
                    chkMontacargasNo.setChecked(true);
                }else {
                    chkMontacargasSi.setChecked(true);
                    chkMontacargasNo.setChecked(false);
                }
                if (informacionModulos.getTieneDisponibilidadBalanza()==0){
                    chkBalanzaSi.setChecked(false);
                    chkBalanzaNo.setChecked(true);
                }else {
                    chkBalanzaSi.setChecked(true);
                    chkBalanzaNo.setChecked(false);
                }
                if (informacionModulos.getRequiereIncineracionPresenciada()==0){
                    chkPresenciadoSi.setChecked(false);
                    chkPresenciadoNo.setChecked(true);
                }else {
                    chkPresenciadoSi.setChecked(true);
                    chkPresenciadoNo.setChecked(false);
                }



            }else {

            }
    }
}

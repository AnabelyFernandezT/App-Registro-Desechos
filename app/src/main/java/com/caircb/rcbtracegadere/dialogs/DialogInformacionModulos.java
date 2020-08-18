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
    TextView lblRutaInfo,lblSubrutaInfo,lblPlacaInfo,lblChoferInfo,lblAuxiliarRecoleccion1Info,lblAuxiliarRecoleccion2Info,lblKilometrajeInfo,lblObservaciones,lblTituloRuta
            ,lblTituloRecoleccion,lblPlacaLote,lblPlacaSincronizada,lblNombreChoferSede, lblNombreChoferPlanta,lblPlacaPlanta,lblRutaPlanta,lblSubrutaPlanta,lblChoferPlanta,lblAuxiliarPlanta1,lblLoteTransportista;
    CheckBox chkFiscalizacionNo,chkFiscalizacionSi,chkFiscalizacionArcsa,chkFiscalizacionMi,chkDevolucionRecipienteSi,chkDevolucionRecipienteNo,chkMontacargasSi,chkMontacargasNo
            ,chkBalanzaSi,chkBalanzaNo,chkPresenciadoSi,chkPresenciadoNo;
    LinearLayout btnRetornarMenu,sectionGeneral,sectionSede,sectionPlanta;
    List<DtoInformacionModulos> listaInformacionModulos;
    private List<ItemInformacionModulos> rowItems;
    AlertDialog.Builder builder;

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
        lblLoteTransportista = getView().findViewById(R.id.lblLoteTransportista);
/*        chkFiscalizacionNo = getView().findViewById(R.id.chkFiscalizacionNo);
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
        lblObservaciones = getView().findViewById(R.id.lblObservaciones);*/
        btnRetornarMenu = getView().findViewById(R.id.btnRetornarMenu);
        lblPlacaLote = getView().findViewById(R.id.lblPlacaLote);
        lblPlacaSincronizada = getView().findViewById(R.id.lblPlacaSincronizada);
        lblNombreChoferSede = getView().findViewById(R.id.lblNombreChoferSede);

        lblNombreChoferPlanta = getView().findViewById(R.id.lblNombreChoferPlanta);
        lblPlacaPlanta = getView().findViewById(R.id.lblPlacaPlanta);
        lblRutaPlanta = getView().findViewById(R.id.lblRutaPlanta);
        lblSubrutaPlanta = getView().findViewById(R.id.lblSubrutaPlanta);
        lblChoferPlanta = getView().findViewById(R.id.lblChoferPlanta);
        lblAuxiliarPlanta1 = getView().findViewById(R.id.lblAuxiliarPlanta1);

        lblTituloRuta = getView().findViewById(R.id.lblTituloRuta);
        lblTituloRecoleccion = getView().findViewById(R.id.lblTituloRecoleccion);
        sectionGeneral = getView().findViewById(R.id.sectionGeneral);
        sectionSede = getView().findViewById(R.id.sectionSede);
        sectionPlanta = getView().findViewById(R.id.sectionPlanta);

        btnRetornarMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInformacionModulos.this.dismiss();
            }
        });


    }


    private void datosInformacionModulo(){
        InformacionModulosEntity informacionModulos = MyApp.getDBO().informacionModulosDao().fetchInformacionModulos2();
        int idTipoEspiecifico = Integer.parseInt(MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_destino_info"));

        if (idTipoEspiecifico==1||idTipoEspiecifico==0){ //Transportista
                lblTituloRecoleccion.setVisibility(View.VISIBLE);
                lblTituloRuta.setVisibility(View.GONE);
                sectionGeneral.setVisibility(View.VISIBLE);
                sectionSede.setVisibility(View.GONE);
                sectionPlanta.setVisibility(View.GONE);

                lblRutaInfo.setText(informacionModulos.getRuta());
                lblSubrutaInfo.setText(informacionModulos.getSubruta());
                lblPlacaInfo.setText(informacionModulos.getPlaca());
                lblChoferInfo.setText(informacionModulos.getChofer());
                lblAuxiliarRecoleccion1Info.setText(informacionModulos.getAuxiliarRecoleccion1());
                lblAuxiliarRecoleccion2Info.setText(informacionModulos.getAuxiliarRecoleccion2());
                lblKilometrajeInfo.setText(informacionModulos.getKilometrajeInicio().toString());
                int idLote=informacionModulos.getIdLoteProceso()==null?0:informacionModulos.getIdLoteProceso();
                if (idLote==0){
                    lblLoteTransportista.setVisibility(View.GONE);
                }else {
                    lblLoteTransportista.setVisibility(View.VISIBLE);
                    lblLoteTransportista.setText(informacionModulos.getIdLoteProceso().toString());
                }
        /*    if (informacionModulos.getObservacionResiduos().equals(null)){
                lblObservaciones.setText("");
            }else {
                lblObservaciones.setText(informacionModulos.getObservacionResiduos());
            }*/
               /* if (informacionModulos.getResiduoSujetoFiscalizacion()==0){
                    chkFiscalizacionNo.setChecked(true);
                    chkFiscalizacionSi.setSelected(false);
              *//*chkFiscalizacionArcsa.setText();
                chkFiscalizacionMi.setText();*//*
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
                }*/

        }else if(idTipoEspiecifico>=3 && idTipoEspiecifico<=5) { //Planta o Sede
            lblTituloRecoleccion.setVisibility(View.GONE);
            lblTituloRuta.setVisibility(View.VISIBLE);
            sectionGeneral.setVisibility(View.GONE);
            sectionSede.setVisibility(View.VISIBLE);
            sectionPlanta.setVisibility(View.GONE);

            lblNombreChoferSede.setText(informacionModulos.getChofer());
            lblPlacaLote.setText(MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_placa_lote"));
            lblPlacaSincronizada.setText(MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_placa_transportista"));
/*          lblRutaInfo.setText(informacionModulos.getRuta());
            lblSubrutaInfo.setText(informacionModulos.getSubruta());
            lblPlacaInfo.setText(informacionModulos.getPlaca());
            lblChoferInfo.setText(informacionModulos.getChofer());
            lblAuxiliarRecoleccion1Info.setText(informacionModulos.getAuxiliarRecoleccion1());
            lblAuxiliarRecoleccion2Info.setText(informacionModulos.getAuxiliarRecoleccion2());
            lblKilometrajeInfo.setText(informacionModulos.getKilometrajeInicio().toString());*/
        }else if(idTipoEspiecifico>=6 && idTipoEspiecifico<=7){
            lblTituloRecoleccion.setVisibility(View.GONE);
            lblTituloRuta.setVisibility(View.VISIBLE);
            sectionGeneral.setVisibility(View.GONE);
            sectionSede.setVisibility(View.GONE);
            sectionPlanta.setVisibility(View.VISIBLE);

            lblNombreChoferPlanta.setText(informacionModulos.getChofer());
            lblPlacaPlanta.setText(MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_placa_transportista"));
            lblRutaPlanta.setText(informacionModulos.getRuta());
            lblSubrutaPlanta.setText(informacionModulos.getSubruta());
            lblChoferPlanta.setText(informacionModulos.getChofer());
            lblAuxiliarPlanta1.setText(informacionModulos.getAuxiliarRecoleccion1());
        }


    }
}

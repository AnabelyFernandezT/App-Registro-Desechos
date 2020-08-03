package com.caircb.rcbtracegadere.dialogs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.entity.InformacionModulosEntity;
import com.caircb.rcbtracegadere.generics.MyDialog;
import com.caircb.rcbtracegadere.models.ItemManifiesto;
import com.caircb.rcbtracegadere.models.RowItemManifiesto;
import com.caircb.rcbtracegadere.models.RowItemManifiestoDetalle;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class DialogInformacionTransportista extends MyDialog {
    Activity _activity;
    TextView lblApertura1, lblApertura2, lblCierre1, lblCierre2, lblTelefonoCliente, lblObservaciones,txtReferencia;
    CheckBox chkFiscalizacionNo, chkFiscalizacionSi, chkFiscalizacionArcsa, chkFiscalizacionMi, chkDevolucionRecipienteSi, chkDevolucionRecipienteNo, chkMontacargasSi, chkMontacargasNo, chkBalanzaSi,
            chkBalanzaNo, chkPresenciadoSi, chkPresenciadoNo;
    LinearLayout btnRetornarMenu,sectionCuadro,sectionEspecifica;
    TableLayout tableLayout;
    int position = 0;
    private String apertura1;
    private String apertura2;
    private String cierre1;
    private String cierre2;
    private String telefono = "";
    private Integer idManifiesto;
    private String referencia;
    private String[] header={"Descripción","Código MAE","Estado Físico Desecho","Packing","Cantidad (u)","Peso (Kg)","Tratamiento","Tiene disponibilidad de montacargas","Tiene disponibilidad de balanza"};
    private ArrayList<String[]>rows=new ArrayList<>();
    private List<RowItemManifiestoDetalle> detalles;
    private String frecuencia;

    public DialogInformacionTransportista(@NonNull Context context,
                                          String apertura1, String apertura2, String cierre1,
                                          String cierre2, String telefono,Integer idManifiesto,String frecuencia, String referencia) {
        super(context, R.layout.dialog_informacion_transportista);
        this._activity = (Activity) context;
        this.position = position;
        this.apertura1 = apertura1;
        this.apertura2 = apertura2;
        this.cierre1 = cierre1;
        this.cierre2 = cierre2;
        this.telefono =telefono;
        this.idManifiesto=idManifiesto;
        this.frecuencia=frecuencia;
        this.referencia = referencia;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getView());
        init();

    }

    private void init() {

        detalles = MyApp.getDBO().manifiestoDetalleDao().fetchHojaRutaDetallebyIdManifiesto2(idManifiesto);
        lblApertura1 = getView().findViewById(R.id.lblApertura1);
        lblApertura2 = getView().findViewById(R.id.lblApertura2);
        lblCierre1 = getView().findViewById(R.id.lblCierre1);
        lblCierre2 = getView().findViewById(R.id.lblCierre2);
        lblTelefonoCliente = getView().findViewById(R.id.lblTelefonoCliente);
        btnRetornarMenu = getView().findViewById(R.id.btnRetornarMenu);
        tableLayout = getView().findViewById(R.id.tableDatos);

        chkFiscalizacionNo = getView().findViewById(R.id.chkFiscalizacionNo);
        chkFiscalizacionSi = getView().findViewById(R.id.chkFiscalizacionSi);
        chkFiscalizacionArcsa = getView().findViewById(R.id.chkFiscalizacionArcsa);
        chkFiscalizacionMi = getView().findViewById(R.id.chkFiscalizacionMi);
        chkDevolucionRecipienteSi = getView().findViewById(R.id.chkDevolucionRecipienteSi);
        chkDevolucionRecipienteNo = getView().findViewById(R.id.chkDevolucionRecipienteNo);
/*        chkMontacargasSi = getView().findViewById(R.id.chkMontacargasSi);
        chkMontacargasNo = getView().findViewById(R.id.chkMontacargasNo);
        chkBalanzaSi = getView().findViewById(R.id.chkBalanzaSi);
        chkBalanzaNo = getView().findViewById(R.id.chkBalanzaNo);*/
        chkPresenciadoSi = getView().findViewById(R.id.chkPresenciadoSi);
        chkPresenciadoNo = getView().findViewById(R.id.chkPresenciadoNo);
        lblObservaciones = getView().findViewById(R.id.lblObservaciones);
        sectionCuadro = getView().findViewById(R.id.sectionCuadro);
        sectionEspecifica = getView().findViewById(R.id.sectionEspecifica);
        txtReferencia = getView().findViewById(R.id.txtReferencia);


        datosCabecera();
        datosTabla();

        datosInformacionModulo();

        btnRetornarMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInformacionTransportista.this.dismiss();

            }
        });

    }

    private void datosCabecera() {
        lblApertura1.setText(apertura1==null?"":apertura1.toString());
        lblApertura2.setText(apertura2==null?"":apertura2.toString());
        lblCierre1.setText(cierre1==null?"":cierre1.toString());
        lblCierre2.setText(cierre2==null?"":cierre2.toString());
        lblTelefonoCliente.setText(telefono==null?"":telefono.toString());
        txtReferencia.setText(referencia);
    }

    private  void datosTabla(){
        TableInformacionTransportista tableInformacionTransportista=new TableInformacionTransportista(tableLayout,getContext(),detalles.size());
        tableInformacionTransportista.addHeader(header);
        tableInformacionTransportista.addData(getDatos());
    }

    private ArrayList<String[]> getDatos(){
        for (int i=0; i<detalles.size(); i++){
            rows.add(new String[]{detalles.get(i).getDescripcion(),detalles.get(i).getCodigoMae(),detalles.get(i).getEstadoFisico(),detalles.get(i).getTipoContenedor(),detalles.get(i).getCantidadRefencial()+"",detalles.get(i).getPesoReferencial()+"",detalles.get(i).getTratamiento(),detalles.get(i).getTieneDisponibilidadMontacarga()==1?"SI":"NO",detalles.get(i).getTieneDisponibilidadBalanza()==1?"SI":"NO"});
        }
        return rows;
    }

    private void datosInformacionModulo() {

        String frecuenciaTransform=frecuencia==null?"":frecuencia;
        if (frecuenciaTransform.equals("PUNTUAL")) {
            sectionCuadro.setVisibility(View.VISIBLE);
            sectionEspecifica.setVisibility(View.VISIBLE);
            //InformacionModulosEntity informacionModulos = MyApp.getDBO().informacionModulosDao().fetchInformacionModulos2();

            int contSi=0;
            for (int i=0;i<detalles.size();i++){
                if (detalles.get(i).getResiduoSujetoFiscalizacion() == 1){
                    contSi++;
                }
            }
            if (contSi==0){
                chkFiscalizacionSi.setChecked(false);
                chkFiscalizacionNo.setChecked(true);
            }else {
                chkFiscalizacionSi.setChecked(true);
                chkFiscalizacionNo.setChecked(false);
            }
            for (int i=0;i<detalles.size();i++){
                if (detalles.get(i).getResiduoSujetoFiscalizacion()==3){
                    chkFiscalizacionArcsa.setChecked(true);
                }
                if (detalles.get(i).getResiduoSujetoFiscalizacion()==4){
                    chkFiscalizacionMi.setChecked(true);
                }
                if (detalles.get(i).getObservacionResiduos().equals("")) {
                } else {
                    lblObservaciones.setText(detalles.get(i).getObservacionResiduos()+", ");
                }
            }

          /*  if (detalles.get(0).getResiduoSujetoFiscalizacion() == 0) {
                chkFiscalizacionNo.setChecked(false);
                chkFiscalizacionSi.setSelected(false);
                *//*    chkFiscalizacionArcsa.setText();
                    chkFiscalizacionMi.setText();*//*
            }
            if (detalles.get(0).getResiduoSujetoFiscalizacion() == 1){
                chkFiscalizacionNo.setSelected(false);
                chkFiscalizacionSi.setSelected(true);
            }
            if (detalles.get(0).getResiduoSujetoFiscalizacion() == 2){
                chkFiscalizacionNo.setSelected(true);
                chkFiscalizacionSi.setSelected(false);
            }
            if (detalles.get(0).getResiduoSujetoFiscalizacion()==3){
                chkFiscalizacionSi.setSelected(true);
                chkFiscalizacionNo.setChecked(false);
                chkFiscalizacionMi.setSelected(false);
                chkFiscalizacionArcsa.setSelected(true);
            }
            if (detalles.get(0).getResiduoSujetoFiscalizacion()==4){
                chkFiscalizacionSi.setSelected(true);
                chkFiscalizacionNo.setChecked(false);
                chkFiscalizacionMi.setSelected(true);
                chkFiscalizacionArcsa.setSelected(false);
            }
*/



            if (detalles.get(0).getRequiereDevolucionRecipientes() == 0) {
                chkDevolucionRecipienteSi.setChecked(false);
                chkDevolucionRecipienteNo.setChecked(false);
            }
            if(detalles.get(0).getRequiereDevolucionRecipientes() == 1) {
                chkDevolucionRecipienteSi.setChecked(true);
                chkDevolucionRecipienteNo.setChecked(false);
            }
            if(detalles.get(0).getRequiereDevolucionRecipientes() == 2) {
                chkDevolucionRecipienteSi.setChecked(false);
                chkDevolucionRecipienteNo.setChecked(true);
            }



  /*          if (detalles.get(0).getTieneDisponibilidadMontacarga() == 0) {
                chkMontacargasSi.setChecked(false);
                chkMontacargasNo.setChecked(false);
            }
            if (detalles.get(0).getTieneDisponibilidadMontacarga() == 1){
                chkMontacargasSi.setChecked(true);
                chkMontacargasNo.setChecked(false);
            }
            if (detalles.get(0).getTieneDisponibilidadMontacarga() == 2){
                chkMontacargasSi.setChecked(false);
                chkMontacargasNo.setChecked(true);
            }



            if (detalles.get(0).getTieneDisponibilidadBalanza() == 0) {
                chkBalanzaSi.setChecked(false);
                chkBalanzaNo.setChecked(false);
            }
            if(detalles.get(0).getTieneDisponibilidadBalanza() == 1){
                chkBalanzaSi.setChecked(true);
                chkBalanzaNo.setChecked(false);
            }
            if(detalles.get(0).getTieneDisponibilidadBalanza() == 2){
                chkBalanzaSi.setChecked(false);
                chkBalanzaNo.setChecked(true);
            }*/

            if (detalles.get(0).getRequiereIncineracionPresenciada() == 0) {
                chkPresenciadoSi.setChecked(false);
                chkPresenciadoNo.setChecked(false);
            }
            if (detalles.get(0).getRequiereIncineracionPresenciada() == 1){
                chkPresenciadoSi.setChecked(true);
                chkPresenciadoNo.setChecked(false);
            }
            if (detalles.get(0).getRequiereIncineracionPresenciada() == 2){
                chkPresenciadoSi.setChecked(false);
                chkPresenciadoNo.setChecked(true);
            }


        }else {
            sectionCuadro.setVisibility(View.GONE);
            sectionEspecifica.setVisibility(View.GONE);
        }




    }
}

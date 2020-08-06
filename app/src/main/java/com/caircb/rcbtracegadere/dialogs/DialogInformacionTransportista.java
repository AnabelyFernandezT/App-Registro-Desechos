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
    TextView lblApertura1, lblApertura2, lblCierre1, lblCierre2, lblTelefonoCliente, lblObservaciones,lblReferencia;
    CheckBox chkMontacargaNo, chkMontacargaSi, chkFiscalizacionArcsa, chkFiscalizacionMi, chkBalanzaSi, chkBalanzaNo, chkMontacargasSi, chkMontacargasNo
            , chkPresenciadoSi, chkPresenciadoNo;
    LinearLayout btnRetornarMenu,sectionCuadro,sectionEspecifica;
    TableLayout tableLayout;
    int position = 0;
    private String apertura1;
    private String apertura2;
    private String cierre1;
    private String cierre2;
    private String telefono = "";
    private Integer idManifiesto;
    private String[] header={"Descripción","Código MAE","Estado Físico Desecho","Packing","Cantidad (u)","Peso (Kg)","Tratamiento","Residuo sujeto a fiscalización","Requiere devolución de recipientes"};
    private ArrayList<String[]>rows=new ArrayList<>();
    private List<RowItemManifiestoDetalle> detalles;
    private String frecuencia, referencia;

    public DialogInformacionTransportista(@NonNull Context context, String apertura1, String apertura2, String cierre1, String cierre2, String telefono,Integer idManifiesto,String frecuencia, String referencia) {
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

        chkMontacargaNo = getView().findViewById(R.id.chkMontacargaNo);
        chkMontacargaSi = getView().findViewById(R.id.chkMontacargaSi);

        chkBalanzaSi = getView().findViewById(R.id.chkBalanzaSi);
        chkBalanzaNo = getView().findViewById(R.id.chkBalanzaNo);
/*        chkMontacargasSi = getView().findViewById(R.id.chkMontacargasSi);
        chkMontacargasNo = getView().findViewById(R.id.chkMontacargasNo);
        chkBalanzaSi = getView().findViewById(R.id.chkBalanzaSi);
        chkBalanzaNo = getView().findViewById(R.id.chkBalanzaNo);*/
        chkPresenciadoSi = getView().findViewById(R.id.chkPresenciadoSi);
        chkPresenciadoNo = getView().findViewById(R.id.chkPresenciadoNo);
        lblObservaciones = getView().findViewById(R.id.lblObservaciones);
        sectionCuadro = getView().findViewById(R.id.sectionCuadro);
        sectionEspecifica = getView().findViewById(R.id.sectionEspecifica);
        lblReferencia = getView().findViewById(R.id.lblReferencia);


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
        lblReferencia.setText(referencia);
    }

    private  void datosTabla(){
        TableInformacionTransportista tableInformacionTransportista=new TableInformacionTransportista(tableLayout,getContext(),detalles.size());
        tableInformacionTransportista.addHeader(header);
        tableInformacionTransportista.addData(getDatos());
    }

    private ArrayList<String[]> getDatos(){
        for (int i=0; i<detalles.size(); i++){
            rows.add(new String[]{detalles.get(i).getDescripcion(),detalles.get(i).getCodigoMae(),
                    detalles.get(i).getEstadoFisico(),detalles.get(i).getTipoContenedor(),
                    detalles.get(i).getCantidadRefencial()+"",detalles.get(i).getPesoReferencial()+"",
                    detalles.get(i).getTratamiento(), sujetoFiscalizacion(i),
                    detalles.get(i).getRequiereDevolucionRecipientes()==1?"SI":"NO"});
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
                if (detalles.get(i).getTieneDisponibilidadMontacarga().equals(1)){
                    contSi++;
                }
            }
            if (contSi==0){
                chkMontacargaSi.setChecked(false);
                chkMontacargaNo.setChecked(true);
            }else {
                chkMontacargaSi.setChecked(true);
                chkMontacargaNo.setChecked(false);
            }

            int contBSi=0;

            for (int i=0;i<detalles.size();i++){
                if (detalles.get(i).getTieneDisponibilidadBalanza().equals(1)){
                    contBSi++;
                }
            }
            if (contBSi==0){
                chkBalanzaSi.setChecked(false);
                chkBalanzaNo.setChecked(true);
            }else {
                chkBalanzaSi.setChecked(true);
                chkBalanzaNo.setChecked(false);
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


/*
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
*/


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

    private String sujetoFiscalizacion(Integer i){
        String respuesta="";

            if (detalles.get(i).getResiduoSujetoFiscalizacion().equals(1)){
                respuesta = "SI";
            }
            if (detalles.get(i).getResiduoSujetoFiscalizacion().equals(2)){
                respuesta = "NO";
            }
            if (detalles.get(i).getResiduoSujetoFiscalizacion().equals(3)){
                respuesta = "ARCSA";
            }
            if (detalles.get(i).getResiduoSujetoFiscalizacion().equals(4)){
                respuesta = "Ministerio del Interior";
            }
            if(detalles.get(i).getResiduoSujetoFiscalizacion().equals(6)){
                respuesta = "OTROS";
            }

        return respuesta;
    }

}

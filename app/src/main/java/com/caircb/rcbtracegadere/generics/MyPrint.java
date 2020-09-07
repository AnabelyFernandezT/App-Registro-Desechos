package com.caircb.rcbtracegadere.generics;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.ItemEtiqueta;
import com.caircb.rcbtracegadere.models.ItemEtiquetaHospitalario;
import com.caircb.rcbtracegadere.models.ItemEtiquetaHospitalarioDetalleRecolecion;
import com.zebra.android.comm.BluetoothPrinterConnection;
import com.zebra.android.comm.ZebraPrinterConnection;
import com.zebra.android.comm.ZebraPrinterConnectionException;
import com.zebra.android.printer.PrinterLanguage;
import com.zebra.android.printer.ZebraPrinter;
import com.zebra.android.printer.ZebraPrinterFactory;
import com.zebra.android.printer.ZebraPrinterLanguageUnknownException;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jlsuarez on 8/1/2018.
 */

public class MyPrint {

    private ZebraPrinterConnection zebraPrinterConnection;
    private ZebraPrinter printer;
    private String DEFAULT_PRINTER_MAC;
    private ProgressDialog dialog;
    private Context mContext;
    private Activity activity;
    private boolean connected=false;
    private boolean saltoLinea = false;
    private int totalNumeroEtiquetas = 10; //25 para una sola linea y 10 para doble linea en la descripcion

    public interface OnPrinterListener {
        public void onSuccessful();
        public void onFailure(String message);
    }

    private OnPrinterListener mOnPrinterListener;

    public MyPrint(@NonNull Activity activity) {
        this.mContext=activity.getApplicationContext();
        this.activity = activity;

        //inicialize spinner...
        dialog = new ProgressDialog(activity);
        dialog.setMessage("Imprimiendo "+ System.getProperty("line.separator")+"esto puede tardar varios segundos...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);

        onCreatePrint();
    }

    /***************/

    private boolean checkImpresora(){
        String data = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta");
        return MyApp.getDBO().impresoraDao().existeImpresora(data!=null && data.length()>0?Integer.parseInt(data):0);
    }

    public void printerIndividual(Integer idManifiesto, Integer idManifiestoDetalle, Integer idCatalogo, final Integer numeroBulto){

        if(checkImpresora()) {
            final ItemEtiqueta printEtiqueta = MyApp.getDBO().manifiestoDetallePesosDao().consultaBultoIndividual(idManifiesto, idManifiestoDetalle, idCatalogo);
            if(printEtiqueta != null){
                System.out.println(printEtiqueta);
                dialog.show();

                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        if (Looper.myLooper() == null)
                        {
                            Looper.prepare();
                        }
                        doConnectionTestIndividual(printEtiqueta, numeroBulto);
                        Looper.loop();
                        Looper.myLooper().quit();
                    }
                });

            } else {
                //mensaje...
                Toast.makeText(mContext, "Impresora no encontrada", Toast.LENGTH_SHORT).show();
                disconnect("Impresora no encontrada");
            }
        }else {
            Toast.makeText(mContext, "Impresora no encontrada", Toast.LENGTH_SHORT).show();
            disconnect("Impresora no encontrada");
        }
    }

    public void printerIndividualLote(Integer idManifiesto, Integer idManifiestoDetalle, final Integer totalNumeroEtiquetas){
        if(checkImpresora()) {
            final ItemEtiqueta printEtiqueta = MyApp.getDBO().manifiestoDetallePesosDao().consultaBultoIndividualLote(idManifiesto, idManifiestoDetalle);
            if(printEtiqueta != null){
                System.out.println(printEtiqueta);
                dialog.show();

                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        if (Looper.myLooper() == null)
                        {
                            Looper.prepare();
                        }
                        doConnectionTestIndividualLote(printEtiqueta, totalNumeroEtiquetas);
                        Looper.loop();
                        Looper.myLooper().quit();
                    }
                });

            } else {
                //mensaje...
                Toast.makeText(mContext, "Impresora no encontrada", Toast.LENGTH_SHORT).show();
                disconnect("Impresora no encontrada");
            }
        }else {
            Toast.makeText(mContext, "Impresora no encontrada", Toast.LENGTH_SHORT).show();
            if(!MyApp.getDBO().parametroDao().fecthParametroValor("auto_impresion" + MySession.getIdUsuario()).equals("1")){
                disconnect("Impresora no encontrada");
            }
        }
    }


    public void pinter(Integer idAppManifiesto){
        //varificar si existe alguna impresora conectada...
        if(checkImpresora()) {

            final List<ItemEtiqueta> etiquetas = MyApp.getDBO().manifiestoDetallePesosDao().consultarBultosImpresion(idAppManifiesto);

            if (etiquetas != null && etiquetas.size() > 0) {
                dialog.show();
                //recorrer array para setear numero de bulto...
                Integer manifiestoDetalleID = 0, index = 0;
                for (ItemEtiqueta it : etiquetas) {
                    if (it.getIdAppManifiestoDetalle() != manifiestoDetalleID) {
                        index = 1;
                    }
                    it.setIndexEtiqueta(index);
                    index++;
                }

            /*
            new Thread(new Runnable() {
                public void run() {
                    Looper.prepare();
                    doConnectionTest(etiquetas);
                    Looper.loop();
                    Looper.myLooper().quit();
                }
            }).start();
            */
            /*
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Looper.prepare();
                        doConnectionTest(etiquetas);
                        Looper.loop();
                        Looper.myLooper().quit();
                    }
                });*/

            } else {
                //mensaje...
                Toast.makeText(mContext, "Impresora no encontrada", Toast.LENGTH_SHORT).show();
                disconnect("Impresora no encontrada");

            }
        }else {
            Toast.makeText(mContext, "Impresora no encontrada", Toast.LENGTH_SHORT).show();
            disconnect("Impresora no encontrada");

        }
    }

    private void onCreatePrint(){
        String data = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta");
        DEFAULT_PRINTER_MAC = MyApp.getDBO().impresoraDao().searchMac(data!=null && data.length()>0?Integer.parseInt(data):0);
    }

    public String getMacAddressFieldText() {
        return this.DEFAULT_PRINTER_MAC;
    }

    private void disconnect(String message) {
        String msg="";
        try {

            if (zebraPrinterConnection != null && connected) {
                zebraPrinterConnection.close();
                connected=false;
            }

        } catch (ZebraPrinterConnectionException e) {
            msg = e.getMessage();
        } finally {
            if(mOnPrinterListener!=null)mOnPrinterListener.onFailure(message!=null && message.length()>0?message:msg);
            dialog.dismiss();
        }
    }

    private void finalized() {
        try {
            //setStatus("Desconectando", Color.RED);
            if (zebraPrinterConnection != null) {
                zebraPrinterConnection.close();
            }
            //setStatus("No Conectado", Color.RED);
        } catch (ZebraPrinterConnectionException e) {
            //setStatus("Error de conexión! Desconectando", Color.RED);
        } finally {
            dialog.dismiss();
            if(mOnPrinterListener!=null)mOnPrinterListener.onSuccessful();
        }
    }



    private void doConnectionTest(List<ItemEtiqueta> etiquetas) {
        printer = connect();
        if (printer != null) {
            sendTestLabel(etiquetas);
        } else {
            disconnect("No existe conexion con la impresora");
        }
    }

    private void doConnectionTestIndividual(ItemEtiqueta etiqueta, Integer numeroBulto) {
        printer = connect();
        if (printer != null) {
            sendTestLabelIndividual(etiqueta, numeroBulto);
        } else {
            disconnect("No existe conexion con la impresora");
        }
    }

    private void doConnectionTestIndividualLote(ItemEtiqueta etiqueta, Integer totalNumeroEtiquetas) {
        printer = connect();
        if (printer != null) {
            sendTestLabelIndividualLote(etiqueta, totalNumeroEtiquetas,10);
        } else {
            disconnect("No existe conexion con la impresora");
        }
    }

    private void doConnectionTestHospitalario(ItemEtiquetaHospitalario etiqueta, List<ItemEtiquetaHospitalarioDetalleRecolecion> listaDetalle) {
        printer = connect();
        if (printer != null) {
          sendTestLabelHospitalario(etiqueta,listaDetalle);
        } else {
            disconnect("No existe conexion con la impresora");
        }
    }

    private void sendTestLabelIndividual(ItemEtiqueta etiquetas, Integer numeroBulto) {
        boolean complete=false;
        ItemEtiqueta row = etiquetas;
        try {
            byte[] configLabel;
            configLabel = getTramaBultoIndividual(
                    printer,
                    row.getNumeroManifiesto(),
                    row.getCodigoQr(),
                    row.getCliente(),
                    (new SimpleDateFormat("dd/MM/yyyy")).format(row.getFechaRecoleccion()),
                    row.getPeso(),
                    row.getResiduo(),
                    String.valueOf(numeroBulto),
                    row.getTratamiento(),
                    row.getDestinatario(),
                    false
            );
            zebraPrinterConnection.write(configLabel);
            MyThread.sleep(50);

            complete=true;
        }catch (ZebraPrinterConnectionException e) {
            //setStatus(e.getMessage(), Color.RED);
        } catch (Exception e) {
            // TODO: handle exception
        }finally {
            if(complete)finalized();
            if(complete){
                if(mOnPrinterListener != null){mOnPrinterListener.onSuccessful();}
            }
            else disconnect("Se presento un problema al realizar la estructura de la etiqueta");
        }
    }

    private void sendTestLabelIndividualLote(ItemEtiqueta etiquetas, Integer totalNumeroEtiquetas, Integer loteValor) {
        boolean complete=false;
        ItemEtiqueta row = etiquetas;
        try {
            byte[] configLabel;
            int contador=1;

            for (int i =1 ; i<=totalNumeroEtiquetas ; i++){
                configLabel = getTramaBultoIndividualLote(
                        printer,
                        row.getNumeroManifiesto(),
                        row.getCodigoQr(),
                        eliminarAcentos(row.getCliente()),
                        (new SimpleDateFormat("dd/MM/yyyy")).format(row.getFechaRecoleccion()),
                        row.getPeso(),
                        eliminarAcentos(row.getResiduo()),
                        String.valueOf(i),
                        eliminarAcentos(row.getTratamiento()),
                        eliminarAcentos(row.getDestinatario()),
                        false
                );
                if(configLabel!=null) {
                    zebraPrinterConnection.write(configLabel);
                    if (contador == loteValor ) {
                        contador=1;
                        MyThread.sleep(6000);
                    }
                    contador += 1;
                    MyThread.sleep(600);
                }
            }
            MyThread.sleep(500*totalNumeroEtiquetas);

            complete=true;
        }catch (ZebraPrinterConnectionException e) {
            //setStatus(e.getMessage(), Color.RED);
        } catch (Exception e) {
            // TODO: handle exception
        }finally {
            if(complete)finalized();
            if(complete){
                if(mOnPrinterListener != null){mOnPrinterListener.onSuccessful();}
            }
            else disconnect("Se presento un problema al realizar la estructura de la etiqueta");
        }
    }


    private void sendTestLabelHospitalario(ItemEtiquetaHospitalario etiquetas,List<ItemEtiquetaHospitalarioDetalleRecolecion> listaDetalle ) {
        boolean complete=false;
        ItemEtiquetaHospitalario row = etiquetas;
        try {

            int numeEtiquetas= dividirEtiquetas(listaDetalle.size());
            int inicioList=0;
            int finList=0;
            int totalLista;
            byte[] configLabel;
            for (int i = 1; i <= numeEtiquetas; i++) {
                totalLista = listaDetalle.size();
                inicioList= (i ==1 ? 0: inicioList +totalNumeroEtiquetas);
                finList= finList +totalNumeroEtiquetas;
                finList=(finList<totalLista? finList :totalLista);

                List<ItemEtiquetaHospitalarioDetalleRecolecion> listNew = listaDetalle.subList(inicioList,finList);
                System.out.print(listNew);
                configLabel = getTramaHospitalario(
                        printer,
                        eliminarNulos(row.getNombreGenerador()) ,
                        eliminarNulos(row.getPuntoRecoleccion()),
                        eliminarNulos(row.getRucGenerador()),
                        (new SimpleDateFormat("dd/MM/yyyy")).format(row.getFechaRecolecion()),
                        //row.getFechaRecolecion(),
                        eliminarNulos(row.getClaveManifiestoSap()),
                        eliminarNulos(row.getClaveManifiesto()),
                        eliminarNulos(row.getDireccion()),
                        eliminarNulos(row.getDestinatario()),
                        eliminarNulos(row.getFirmaNombreGenerador()),
                        eliminarNulos(row.getFirmaNombreTransportista()),
                        eliminarNulos(row.getFirmaCedulaGenerador()),
                        eliminarNulos(row.getFirmaCedulaTransportista()),
                        listNew
                );
                zebraPrinterConnection.write(configLabel);
                MyThread.sleep(500);

                complete=true;
            }

        }catch (ZebraPrinterConnectionException e) {
            //setStatus(e.getMessage(), Color.RED);
        } catch (Exception e) {
            // TODO: handle exception
        }finally {
            if(complete)finalized();
            if(complete){
                if(mOnPrinterListener != null){mOnPrinterListener.onSuccessful();}
            }
            else disconnect("Se presento un problema al realizar la estructura de la etiqueta");
        }
    }

    public String eliminarNulos (String valor){
        String caracter ="";
        if(valor!=null){
            caracter = valor;
        }
        return caracter;

    }


    private void sendTestLabel(List<ItemEtiqueta> etiquetas) {
        boolean complete=false;
        try {
            byte[] configLabel;
            //outerloop:
            Integer numEtiquetaImpresa=1;
            for(ItemEtiqueta row: etiquetas){

                configLabel = getConfigLabel(
                        printer,
                        row.getNumeroManifiesto(),
                        row.getCodigoQr(),
                        row.getCliente(),
                        (new SimpleDateFormat("dd/MM/yyyy")).format(row.getFechaRecoleccion()),
                        row.getPeso(),
                        row.getResiduo(),
                        row.getIndexEtiqueta()+" / "+row.getTotalEtiqueta(),
                        row.getTratamiento() == null ? "": row.getTratamiento(),
                        "",
                        false
                        );
                zebraPrinterConnection.write(configLabel);
                MyThread.sleep(50);

                //pausa para imprimir etiquetas en lotes de 50 como maximo...
                if(numEtiquetaImpresa==50){
                    numEtiquetaImpresa=0;
                    MyThread.sleep(500);
                }
                numEtiquetaImpresa++;
            }

           complete=true;

        } catch (ZebraPrinterConnectionException e) {
            //setStatus(e.getMessage(), Color.RED);
        } catch (Exception e) {
            // TODO: handle exception
            //setStatus(e.getMessage(), Color.RED);
        }
        finally {
            if(complete)finalized();
            else disconnect("Se presento un problema al realizar la estructura de la etiqueta");

        }

    }


    private byte[] getTramaBultoIndividual(
            ZebraPrinter printer,
            String manifiesto,
            String codigoQr,
            String cliente,
            String fecha,
            double peso,
            String residuo,
            String numeroBulto,
            String tratamiento,
            String destinatario,
            boolean aplicaDevolucion) {

        PrinterLanguage printerLanguage = printer.getPrinterControlLanguage();
        String cpclConfigLabel="";
        byte[] configLabel = null;
        tratamiento = tratamiento == null ? "" : recorreString(tratamiento, 19, "590");
        String ItemDescripcion = recorreString(residuo,27, saltoLinea ? "710":"680");

        if (DEFAULT_PRINTER_MAC.equals("AC:3F:A4:8D:25:53")){
            cpclConfigLabel = "^XA^LH30,30^FO140,230^BQN,2,10,H^FDMM,A"+codigoQr.trim()+"^FS^FO50,60^AD^FD "+ cliente+"^FS^FO50,90^AD^FD #M.U.E: "+manifiesto.trim()+"^FS^FO50,120^AD^FD FECHA: "+fecha+"^FS^FO50,180^AD^FD RESPONSABLE: "+ MySession.getUsuarioNombre().toUpperCase()+"^FS ^XZ";
        }else{
            //String descripcion = row.getDescripcion().substring(10, 33);

            if(!saltoLinea) {
                cpclConfigLabel =
                        //"^XA^POI^LH30,30^FO125,230^BQN,2,10,H^FDMM,A" + codigoQr.trim() +
                        "^XA^LH30,30^FO125,230^BQN,2,10,H^FDMM,A" + codigoQr.trim() +
                                "^FS^FO60,60^AD^FD " + (cliente.length() > 32 ? cliente.substring(0, 32) : cliente) +
                                "^FS^FO60,90^AD^FD #M.U.E: " + manifiesto.trim() +
                                "^FS^FO60,120^AD^FD FECHA: " + fecha +
                                "^FS^FO60,150^AD^FD PESO:" + peso +
                                //"^FS^FO40,150^AD^FD UNIDAD: "+row.getUnidad()+
                                //"^FS^FO280,150^AD^FD PESO:"+row.getPeso()+
                                "^FS^FO60,180^AD^FD RESPONSABLE: " + MySession.getUsuarioNombre().toUpperCase() +
                                "^FS^FO60,530^AD^FD NO. BULTO:" + numeroBulto +
                                "^FS^FO60,560^AD^FD TRATAMIENTO:" + tratamiento +

                                 "^FS^FO60,590^AD^FD DESTINATARIO:" + destinatario.toUpperCase() +
                                "^FS^FO60,620^AD^FD DEVOLUCION RECIPIENTE:" + (aplicaDevolucion ? "SI" : "NO") +
                                "^FS^FO60,650^AD^FD ITEM:" + ItemDescripcion.toUpperCase() +
                                "^FS ^XZ";
            }else{
                cpclConfigLabel =
                        //"^XA^POI^LH30,30^FO125,230^BQN,2,10,H^FDMM,A" + codigoQr.trim() +
                        "^XA^LH30,30^FO125,230^BQN,2,10,H^FDMM,A" + codigoQr.trim() +
                                "^FS^FO60,60^AD^FD " + (cliente.length() > 32 ? cliente.substring(0, 32) : cliente) +
                                "^FS^FO60,90^AD^FD #M.U.E: " + manifiesto.trim() +
                                "^FS^FO60,120^AD^FD FECHA: " + fecha +
                                "^FS^FO60,150^AD^FD PESO:" + peso +
                                //"^FS^FO40,150^AD^FD UNIDAD: "+row.getUnidad()+
                                //"^FS^FO280,150^AD^FD PESO:"+row.getPeso()+
                                "^FS^FO60,180^AD^FD RESPONSABLE: " + MySession.getUsuarioNombre().toUpperCase() +
                                "^FS^FO60,530^AD^FD NO. BULTO:" + numeroBulto +
                                "^FS^FO60,560^AD^FD TRATAMIENTO:" + tratamiento +

                                "^FS^FO60,620^AD^FD DESTINATARIO:" + destinatario.toUpperCase() +
                                "^FS^FO60,650^AD^FD DEVOLUCION RECIPIENTE:" + (aplicaDevolucion ? "SI" : "NO") +
                                "^FS^FO60,680^AD^FD ITEM:" + ItemDescripcion.toUpperCase() +
                                "^FS ^XZ";
            }
        }

        configLabel = cpclConfigLabel.getBytes();
        return configLabel;

    }

    private byte[] getTramaBultoIndividualLote(
            ZebraPrinter printer,
            String manifiesto,
            String codigoQr,
            String cliente,
            String fecha,
            double peso,
            String residuo,
            String numeroBulto,
            String tratamiento,
            String destinatario,
            boolean aplicaDevolucion) {

        PrinterLanguage printerLanguage = printer.getPrinterControlLanguage();
        String cpclConfigLabel="";
        byte[] configLabel = null;
        tratamiento = tratamiento == null ? "" : recorreString(tratamiento, 19, "590");
        String ItemDescripcion = recorreString(residuo,27, saltoLinea ? "710":"680");

        if (DEFAULT_PRINTER_MAC.equals("AC:3F:A4:8D:25:53")){
            cpclConfigLabel = "^XA^LH30,30^FO140,230^BQN,2,10,H^FDMM,A"+codigoQr.trim()+"^FS^FO50,60^AD^FD "+ cliente+"^FS^FO50,90^AD^FD #M.U.E: "+manifiesto.trim()+"^FS^FO50,120^AD^FD FECHA: "+fecha+"^FS^FO50,180^AD^FD RESPONSABLE: "+ MySession.getUsuarioNombre().toUpperCase()+"^FS ^XZ";
        }else{
            //String descripcion = row.getDescripcion().substring(10, 33);

            if(!saltoLinea) {
                cpclConfigLabel =
                        //"^XA^POI^LH30,30^FO125,230^BQN,2,10,H^FDMM,A" + codigoQr.trim() +
                        "^XA^LH30,30^FO125,230^BQN,2,10,H^FDMM,A" + codigoQr.trim() +
                                "^FS^FO60,60^AD^FD " + (cliente.length() > 32 ? cliente.substring(0, 32) : cliente) +
                                "^FS^FO60,90^AD^FD #M.U.E: " + manifiesto.trim() +
                                "^FS^FO60,120^AD^FD FECHA: " + fecha +
                                "^FS^FO60,150^AD^FD PESO:" +"PESO EN PLANTA"+ //peso +
                                //"^FS^FO40,150^AD^FD UNIDAD: "+row.getUnidad()+
                                //"^FS^FO280,150^AD^FD PESO:"+row.getPeso()+
                                "^FS^FO60,180^AD^FD RESPONSABLE: " + MySession.getUsuarioNombre().toUpperCase() +
                                "^FS^FO60,530^AD^FD NO. BULTO:" + numeroBulto +
                                "^FS^FO60,560^AD^FD TRATAMIENTO:" + tratamiento +

                                "^FS^FO60,590^AD^FD DESTINATARIO:" + destinatario.toUpperCase() +
                                "^FS^FO60,620^AD^FD DEVOLUCION RECIPIENTE:" + (aplicaDevolucion ? "SI" : "NO") +
                                "^FS^FO60,650^AD^FD ITEM:" + ItemDescripcion.toUpperCase() +
                                "^FS ^XZ";
            }else{
                cpclConfigLabel =
                        //"^XA^POI^LH30,30^FO125,230^BQN,2,10,H^FDMM,A" + codigoQr.trim() +
                        "^XA^LH30,30^FO125,230^BQN,2,10,H^FDMM,A" + codigoQr.trim() +
                                "^FS^FO60,60^AD^FD " + (cliente.length() > 32 ? cliente.substring(0, 32) : cliente) +
                                "^FS^FO60,90^AD^FD #M.U.E: " + manifiesto.trim() +
                                "^FS^FO60,120^AD^FD FECHA: " + fecha +
                                "^FS^FO60,150^AD^FD PESO:" +"PESO EN PLANTA"+ //peso +
                                //"^FS^FO40,150^AD^FD UNIDAD: "+row.getUnidad()+
                                //"^FS^FO280,150^AD^FD PESO:"+row.getPeso()+
                                "^FS^FO60,180^AD^FD RESPONSABLE: " + MySession.getUsuarioNombre().toUpperCase() +
                                "^FS^FO60,530^AD^FD NO. BULTO:" + numeroBulto +
                                "^FS^FO60,560^AD^FD TRATAMIENTO:" + tratamiento +

                                "^FS^FO60,620^AD^FD DESTINATARIO:" + destinatario.toUpperCase() +
                                "^FS^FO60,650^AD^FD DEVOLUCION RECIPIENTE:" + (aplicaDevolucion ? "SI" : "NO") +
                                "^FS^FO60,680^AD^FD ITEM:" + ItemDescripcion.toUpperCase() +
                                "^FS ^XZ";
            }
        }

        configLabel = cpclConfigLabel.getBytes();
        return configLabel;

    }

    public String getDetalleRecoleccion (List<ItemEtiquetaHospitalarioDetalleRecolecion> listaDetalle){
        /**  Codigo para imprimir en dos lineas el la descripcion solo entran 10 items en la etiqueta **/

         String detalle ="";
         int valor =620;
         int valorDescripcion1=600;
         for(ItemEtiquetaHospitalarioDetalleRecolecion item : listaDetalle){
         String descripcion1 = recorreStringHospitalario(item.getDescripcionDesecho(),0,45 );
         String descripcion2 = recorreStringHospitalario(item.getDescripcionDesecho(),45,91 );
         valor =valor+75;
         valorDescripcion1=valorDescripcion1+75;
         detalle= detalle +
         "^FS^FO50,"+valorDescripcion1+"^A0,30,18^FD " + eliminarAcentos(descripcion1)+
         "^FS^FO50,"+(valorDescripcion1+30)+"^A0,30,18^FD " + eliminarAcentos(descripcion2)+
         "^FS^FO440,"+valorDescripcion1+"^A0,30,18^FD " + recorreStringHospitalario(eliminarAcentos(item.getCodigoMai()),0,13)+
         "^FS^FO440,"+(valorDescripcion1+30)+"^A0,30,18^FD " + recorreStringHospitalario(eliminarAcentos(item.getCodigoMai()),13,27)+
         "^FS^FO590,"+valorDescripcion1+"^A0,30,18^FD " + item.getNumeroBultos() +
         "^FS^FO700,"+valorDescripcion1+"^A0,30,18^FD " + item.getPeso() ;
         }
         return detalle;


        /**Imprime el detalle en un alinea **/
        /**
        String detalle ="";
        int valor =620;
        for(ItemEtiquetaHospitalarioDetalleRecolecion item : listaDetalle){
            String descripcion = recorreStringHospitalario(item.getDescripcionDesecho(),0,45 );
            valor =valor+30;
            detalle= detalle +
                    "^FS^FO50,"+valor+"^A0,30,18^FD " + eliminarAcentos(descripcion)+
                    "^FS^FO420,"+valor+"^A0,30,18^FD " + eliminarAcentos(item.getCodigoMai())+
                    "^FS^FO590,"+valor+"^A0,30,18^FD " + item.getNumeroBultos() +
                    "^FS^FO700,"+valor+"^A0,30,18^FD " + item.getPeso() ;
        }

        return detalle;
         **/
    }


    private byte[] getTramaHospitalario(
            ZebraPrinter printer,
            String nombreGenerador,
            String puntoRecoleccion,
            String rucGenerador,
            String fechaRecolecion,
            String claveManifiestoSap,
            String claveManifiesto,
            String direccion,
            String destinatario,
            String firmaNombreGenerador,
            String firmaNombreTransportista,
            String firmaCedulaGenerador,
            String firmaCedulaTransportista,
            List<ItemEtiquetaHospitalarioDetalleRecolecion> listaDetalle) {

        PrinterLanguage printerLanguage = printer.getPrinterControlLanguage();
        String cpclConfigLabel="";
        byte[] configLabel = null;
        String DescripcionItem1 = recorreStringHospitalario(eliminarAcentos(direccion),0,65 );
        String DescripcionItem2 = recorreStringHospitalario(eliminarAcentos(direccion),65,131 );
        String DescripcionItem3 = recorreStringHospitalario(eliminarAcentos(direccion),131,196 );
        String detalle = getDetalleRecoleccion(listaDetalle);
        if (DEFAULT_PRINTER_MAC.equals("AC:3F:A4:8D:25:53")){
           // cpclConfigLabel = "^XA^LH30,30^FO140,230^BQN,2,10,H^FDMM,A"+codigoQr.trim()+"^FS^FO50,60^AD^FD "+ cliente+"^FS^FO50,90^AD^FD #M.U.E: "+manifiesto.trim()+"^FS^FO50,120^AD^FD FECHA: "+fecha+"^FS^FO50,180^AD^FD RESPONSABLE: "+ MySession.getUsuarioNombre().toUpperCase()+"^FS ^XZ";
        }else {
                cpclConfigLabel =
                        "^XA^CFD ^POI" +
                                "^FO45,180^A0,32,20^FD Nombre del generador: " +
                                "^FS^FO242,182^A0,30,18^FD " + eliminarAcentos(nombreGenerador).toUpperCase() +
                                "^FS^FO45,235^A0,32,20^FD Punto de recolecion: " +
                                "^FS^FO222,237^A0,30,18^FD " + puntoRecoleccion.toUpperCase() +
                                "^FS^FO45,290^A0,32,20^FD RUC del generador: " +
                                "^FS^FO210,292^A0,30,18^FD " + rucGenerador.toUpperCase() +
                                "^FS^FO430,290^A0,32,20^FD Fecha de recolecion:" +
                                "^FS^FO610,292^A0,30,18^FD " + fechaRecolecion.toUpperCase() +
                                "^FS^FO45,345^A0,32,20^FD No. clave de Manifiesto SAP: " +
                                "^FS^FO290,347^A0,30,18^FD " + claveManifiestoSap.toUpperCase() +
                                "^FS^FO430,345^A0,32,20^FD No. clave de Manifiesto:" +
                                "^FS^FO635,347^A0,30,18^FD " + claveManifiesto.toUpperCase() +
                                "^FS^FO45,400^A0,32,20^FD Direccion de recoleccion:" +
                                "^FS^FO260,402^A0,28,16^FD " + DescripcionItem1.toUpperCase() +
                                "^FS^FO260,432^A0,28,16^FD " + DescripcionItem2.toUpperCase() +
                                "^FS^FO260,462^A0,28,16^FD " + DescripcionItem3.toUpperCase() +

                                detalle +

                                "^FS^FO60,1487^A0,32,20^FD " + destinatario +
                                "^FS^FO100,1715^A0,26,16^FB300,3,0,C,018^FD " + eliminarAcentos(firmaNombreGenerador) +
                                "^FS^FO450,1715^A0,26,16^FB300,3,0,C,018^FD " + eliminarAcentos(firmaNombreTransportista) +
                                "^FS^FO100,1745^A0,26,16^FB300,3,0,C,0^FD " + firmaCedulaGenerador +
                                "^FS^FO450,1745^A0,26,16^FB300,3,0,C,0^FD " + firmaCedulaTransportista +
                                "^FS ^XZ";
            }

        configLabel = cpclConfigLabel.getBytes();
        return configLabel;

    }


    public String eliminarAcentos(String str) {

        final String ORIGINAL = "ÁáÉéÍíÓóÚúÑñÜü";
        final String REEMPLAZO = "AaEeIiOoUuNnUu";

        if (str == null) {
            return null;
        }
        char[] array = str.toCharArray();
        for (int indice = 0; indice < array.length; indice++) {
            int pos = ORIGINAL.indexOf(array[indice]);
            if (pos > -1) {
                array[indice] = REEMPLAZO.charAt(pos);
            }
        }
        return new String(array);
    }

    private byte[] getConfigLabel(
            ZebraPrinter printer,
            String manifiesto,
            String codigoQr,
            String cliente,
            String fecha,
            double peso,
            String residuo,
            String numeroBulto,
            String tratamiento,
            String destinatario,
            boolean aplicaDevolucion
            ){

        PrinterLanguage printerLanguage = printer.getPrinterControlLanguage();
        String cpclConfigLabel="";
        byte[] configLabel = null;
        String ItemDescripcion = recorreString(residuo, 27, "680");

        if (DEFAULT_PRINTER_MAC.equals("AC:3F:A4:8D:25:53")){
            cpclConfigLabel = "^XA^LH30,30^FO140,230^BQN,2,10,H^FDMM,A"+codigoQr.trim()+"^FS^FO50,60^AD^FD "+ cliente+"^FS^FO50,90^AD^FD #M.U.E: "+manifiesto.trim()+"^FS^FO50,120^AD^FD FECHA: "+fecha+"^FS^FO50,180^AD^FD RESPONSABLE: "+ MySession.getUsuarioNombre().toUpperCase()+"^FS ^XZ";
        }else{
            //String descripcion = row.getDescripcion().substring(10, 33);
            cpclConfigLabel =
                    //"^XA^POI^LH30,30^FO125,230^BQN,2,10,H^FDMM,A"+codigoQr.trim()+
                    "^XA^LH30,30^FO125,230^BQN,2,10,H^FDMM,A"+codigoQr.trim()+
                    "^FS^FO60,60^AD^FD "+ (cliente.length()> 32 ? cliente.substring(0,32):cliente) +
                    "^FS^FO60,90^AD^FD #M.U.E: "+manifiesto.trim()+
                    "^FS^FO60,120^AD^FD FECHA: "+fecha+
                    "^FS^FO60,150^AD^FD PESO:"+peso+
                    //"^FS^FO40,150^AD^FD UNIDAD: "+row.getUnidad()+
                    //"^FS^FO280,150^AD^FD PESO:"+row.getPeso()+
                    "^FS^FO60,180^AD^FD RESPONSABLE: "+ MySession.getUsuarioNombre().toUpperCase()+
                    "^FS^FO60,530^AD^FD NO. BULTO:"+ numeroBulto +
                    "^FS^FO60,560^AD^FD TRATAMIENTO:"+tratamiento +
                    "^FS^FO60,590^AD^FD DESTINATARIO:"+ destinatario.toUpperCase() +
                    "^FS^FO60,620^AD^FD DEVOLUCION RECIPIENTE:"+ (aplicaDevolucion?"SI":"NO") +
                    "^FS^FO60,650^AD^FD ITEM:"+ ItemDescripcion.toUpperCase() +
                    "^FS ^XZ";
        }

        configLabel = cpclConfigLabel.getBytes();
        return configLabel;
    }


    private String recorreStringHospitalario (String caracter, int inicio, int fin){
        String result="";
        String residuo = caracter.substring(0,caracter.length());
        Integer totalCaract = residuo.length();

        if( totalCaract >= fin){
            result = caracter.substring(inicio,fin);
        }else if (inicio <= totalCaract){
            result = caracter.substring(inicio,totalCaract);
        }

        System.out.print(result);
        return result;
    }

    private String recorreString(String caracter, int logitud, String salto){
        String impresion="";
        String part1="",  resto ="", parte2="";
        String residuo = caracter.substring(0,caracter.length());
        Integer totalCaract = residuo.length();

        if(totalCaract > logitud){
            part1 = caracter.substring(0,logitud);
            resto = caracter.substring(logitud,totalCaract);

            if(logitud == 19)
                saltoLinea = true;

            if(resto.length() > logitud){
                parte2 = resto.substring(0,logitud);
                impresion = part1 + "^FS^FO60,"+salto+"^AD^FD " + parte2; //impresion = part1 + "^FS^FO60,680^AD^FD " + parte2;
            }else{
                impresion = part1 + "^FS^FO60,"+salto+"^AD^FD " + resto;
            }
        }else{
            impresion = residuo;
            if(logitud == 19)
                saltoLinea = false;
        }
       return impresion;
    }

    private ZebraPrinter connect() {

        zebraPrinterConnection = null;
        zebraPrinterConnection = new BluetoothPrinterConnection(DEFAULT_PRINTER_MAC);

        try {
            zebraPrinterConnection.open();
            connected=true;
        } catch (ZebraPrinterConnectionException e) {
            MyThread.sleep(1000);
            disconnect("No se puede abrir comunicacion con la impresora ");
        }

        ZebraPrinter printer = null;

        if (zebraPrinterConnection.isConnected()) {
            try {

                if(DEFAULT_PRINTER_MAC.equals("AC:3F:A4:7E:DD:1F"))
                {
                    printer = ZebraPrinterFactory.getInstance(PrinterLanguage.CPCL,zebraPrinterConnection);
                }else{
                    printer = ZebraPrinterFactory.getInstance(zebraPrinterConnection);
                }
                //setStatus("Determinando lenguaje de impresora", Color.YELLOW);

                PrinterLanguage pl = printer.getPrinterControlLanguage();
                //setStatus("Lenguaje de impresora " + pl, Color.BLUE);
            }
            catch (ZebraPrinterLanguageUnknownException e) {
                //setStatus("Lenguaje de impresora desconocido, por favor reinicie la impresora", Color.RED);
                printer = null;
                MyThread.sleep(1000);
                disconnect("Lenguaje de impresora desconocido, por favor reinicie la impresora");
            }catch (ZebraPrinterConnectionException e) {
                //setStatus("Falla de conexión, por favor reinicie la impresora", Color.RED);
                printer = null;
                MyThread.sleep(1000);
                disconnect("Falla de conexión, por favor reinicie la impresora");
            }
        }

        return printer;
    }

    public void setOnPrinterListener(@NonNull OnPrinterListener l){
        mOnPrinterListener = l;
    }

    public void printerVerificarImpresora(){
        if(checkImpresora()) {

        }else {
            Toast.makeText(mContext, "Impresora no encontrada", Toast.LENGTH_SHORT).show();
            disconnect("Impresora no encontrada");
        }
    }


    private byte[] printerEtiquetaHospitalario(
            ZebraPrinter printer
    ){

        PrinterLanguage printerLanguage = printer.getPrinterControlLanguage();
        String cpclConfigLabel="";
        byte[] configLabel = null;
        //String ItemDescripcion = recorreString(residuo, 27, "680");

        if (DEFAULT_PRINTER_MAC.equals("AC:3F:A4:8D:25:53")){
           // cpclConfigLabel = "^XA^LH30,30^FO140,230^BQN,2,10,H^FDMM,A"+codigoQr.trim()+"^FS^FO50,60^AD^FD "+ cliente+"^FS^FO50,90^AD^FD #M.U.E: "+manifiesto.trim()+"^FS^FO50,120^AD^FD FECHA: "+fecha+"^FS^FO50,180^AD^FD RESPONSABLE: "+ MySession.getUsuarioNombre().toUpperCase()+"^FS ^XZ";
        }else{
            //String descripcion = row.getDescripcion().substring(10, 33);
            cpclConfigLabel =
                    "^XA^POI^LH30,30^FO125,230^BQN,2,10,H^FDMM,A"+//codigoQr.trim()+
                            "^FS^FO60,60^AD^FD "+ //(cliente.length()> 32 ? cliente.substring(0,32):cliente) +
                            "^FS^FO60,90^AD^FD #M.U.E: "+//manifiesto.trim()+
                            "^FS^FO60,120^AD^FD FECHA: "+//fecha+
                            "^FS^FO60,150^AD^FD PESO:"+//peso+

                            "^FS ^XZ";
        }

        configLabel = cpclConfigLabel.getBytes();
        return configLabel;
    }

    public int dividirEtiquetas (int total){
        int numeroEtiquetas=0;
        int reciduoDivision=0;
        int numeroDetalle=totalNumeroEtiquetas;
        reciduoDivision= total % numeroDetalle;
        numeroEtiquetas= total / numeroDetalle;

        if(reciduoDivision >0){
            numeroEtiquetas=numeroEtiquetas+1;
        }

        return numeroEtiquetas ;
    }


    public void printerHospitalario(Integer idAppManifiesto){
        if(checkImpresora()) {
            final ItemEtiquetaHospitalario printEtiqueta = MyApp.getDBO().manifiestoDetallePesosDao().consultaCabeceraHospitalario(idAppManifiesto);
            final List<ItemEtiquetaHospitalarioDetalleRecolecion> listaDetalle = MyApp.getDBO().manifiestoDetallePesosDao().consultaDetalleHospitalario(idAppManifiesto);
            if(printEtiqueta != null && listaDetalle.size() > 0){
                dialog.show();
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        if (Looper.myLooper() == null)
                        {
                            Looper.prepare();
                        }
                        doConnectionTestHospitalario(printEtiqueta,listaDetalle);
                        Looper.loop();
                        Looper.myLooper().quit();
                    }
                });

            } else {
                //mensaje...
                Toast.makeText(mContext, "Impresora no encontrada", Toast.LENGTH_SHORT).show();
                disconnect("No cumple la estructura");
            }
        }else {
            Toast.makeText(mContext, "Impresora no encontrada", Toast.LENGTH_SHORT).show();
            disconnect("Impresora no encontrada");
        }
    }


}

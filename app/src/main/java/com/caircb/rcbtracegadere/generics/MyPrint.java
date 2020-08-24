package com.caircb.rcbtracegadere.generics;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.ItemEtiqueta;
import com.caircb.rcbtracegadere.models.RowItemManifiestoPrint;
import com.zebra.android.comm.BluetoothPrinterConnection;
import com.zebra.android.comm.ZebraPrinterConnection;
import com.zebra.android.comm.ZebraPrinterConnectionException;
import com.zebra.android.printer.PrinterLanguage;
import com.zebra.android.printer.ZebraPrinter;
import com.zebra.android.printer.ZebraPrinterFactory;
import com.zebra.android.printer.ZebraPrinterLanguageUnknownException;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
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
    public void printerIndividual(Integer idManifiesto, Integer idManifiestoDetalle, Integer idCatalogo, final Integer numeroBulto){
        if(MyApp.getDBO().impresoraDao().existeImpresora()) {
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

    public void pinter(Integer idAppManifiesto){
        //varificar si existe alguna impresora conectada...
        if(MyApp.getDBO().impresoraDao().existeImpresora()) {

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
        DEFAULT_PRINTER_MAC = MyApp.getDBO().impresoraDao().searchMac();
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

    private void doConnectionTestHospitalario() {
        printer = connect();
        if (printer != null) {
            sendTestLabelHospitalario();
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

    private void sendTestLabelHospitalario() {
        boolean complete=false;
        //ItemEtiqueta row = etiquetas;
        try {
            byte[] configLabel;
            configLabel = getTramaHospitalario(
                    printer
                   // row.getNumeroManifiesto(),
                    //row.getCodigoQr(),
                    //row.getCliente(),
                    //(new SimpleDateFormat("dd/MM/yyyy")).format(row.getFechaRecoleccion()),
                    //row.getPeso(),
                    //row.getResiduo(),
                    //String.valueOf(numeroBulto),
                    //row.getTratamiento(),
                    //row.getDestinatario(),
                    //false
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
                        "^XA^POI^LH30,30^FO125,230^BQN,2,10,H^FDMM,A" + codigoQr.trim() +
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
                        "^XA^POI^LH30,30^FO125,230^BQN,2,10,H^FDMM,A" + codigoQr.trim() +
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

    private byte[] getTramaHospitalario(
            ZebraPrinter printer
            /*String manifiesto,
            String codigoQr,
            String cliente,
            String fecha,
            double peso,
            String residuo,
            String numeroBulto,
            String tratamiento,
            String destinatario,
            boolean aplicaDevolucion*/) {

        PrinterLanguage printerLanguage = printer.getPrinterControlLanguage();
        String cpclConfigLabel="";
        byte[] configLabel = null;
        //tratamiento = tratamiento == null ? "" : recorreString(tratamiento, 19, "590");
        //String ItemDescripcion = recorreString(residuo,27, saltoLinea ? "710":"680");

        if (DEFAULT_PRINTER_MAC.equals("AC:3F:A4:8D:25:53")){
           // cpclConfigLabel = "^XA^LH30,30^FO140,230^BQN,2,10,H^FDMM,A"+codigoQr.trim()+"^FS^FO50,60^AD^FD "+ cliente+"^FS^FO50,90^AD^FD #M.U.E: "+manifiesto.trim()+"^FS^FO50,120^AD^FD FECHA: "+fecha+"^FS^FO50,180^AD^FD RESPONSABLE: "+ MySession.getUsuarioNombre().toUpperCase()+"^FS ^XZ";
        }else{
            //String descripcion = row.getDescripcion().substring(10, 33);

            if(!saltoLinea) {
                cpclConfigLabel =
                        "^XA^CFD ^POI" +
                                "^FO50,190^A0,30,18^FD Nombre del generador: " + "TALMA ECUADOR SERVICIOS AEROPORTUARIOS S.A " +
                                "^FS^FO50,240^A0,30,18^FD Punto de recolecion: " + "1791807162001" +
                                "^FS^FO50,290^A0,30,18^FD RUC del generador: " + "1791807162001" +
                                "^FS^FO430,290^A0,30,18^FD Fecha de recolecion:" + "2020-08-06" +
                                "^FS^FO50,340^A0,30,18^FD No. clave de Manifiesto SAP: " + "600033747" +
                                "^FS^FO430,340^A0,30,18^FD No. clave de Manifiesto:" + "1234567891" +
                                "^FS^FO50,390^A0,30,18^FD Direccion de recoleccion:" + "AV. DE LAS AMERICAS No.2000" +

                                "^FS^FO50,440^A0,35,23^FD Detalle de recolecion:" +
                                "^FS^FO50,500^A0,30,18^FD " +"Productos farmaceuticos"+
                                "^FS^FO400,500^A0,30,18^FD " + "GA-TDQP-31" +
                                "^FS^FO400,500^A0,30,18^FD " + "GA-TDQP-31" +
                                "^FS^FO550,500^A0,30,18^FD " + "0111" +
                                "^FS^FO650,500^A0,30,18^FD " + "500.450" +
                                "^FS^FO50,1050^A0,30,18^FD "+ "G&M TRATAMIENTO INTEGRAL DE DESECHOS"+
                                "^FS ^XZ";
            }else{
                cpclConfigLabel = "";
            }
        }

        configLabel = cpclConfigLabel.getBytes();
        return configLabel;

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
                    "^XA^POI^LH30,30^FO125,230^BQN,2,10,H^FDMM,A"+codigoQr.trim()+
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
        if(MyApp.getDBO().impresoraDao().existeImpresora()) {

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


    public void printerHospitalario(){
        if(MyApp.getDBO().impresoraDao().existeImpresora()) {
            //final ItemEtiqueta printEtiqueta = MyApp.getDBO().manifiestoDetallePesosDao().consultaBultoIndividual(idManifiesto, idManifiestoDetalle, idCatalogo);
            //if(printEtiqueta != null){
                dialog.show();

                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        if (Looper.myLooper() == null)
                        {
                            Looper.prepare();
                        }
                        doConnectionTestHospitalario();
                        Looper.loop();
                        Looper.myLooper().quit();
                    }
                });

            } else {
                //mensaje...
                Toast.makeText(mContext, "Impresora no encontrada", Toast.LENGTH_SHORT).show();
                disconnect("Impresora no encontrada");
            }
       /* }else {
            Toast.makeText(mContext, "Impresora no encontrada", Toast.LENGTH_SHORT).show();
            disconnect("Impresora no encontrada");
        }*/
    }


}

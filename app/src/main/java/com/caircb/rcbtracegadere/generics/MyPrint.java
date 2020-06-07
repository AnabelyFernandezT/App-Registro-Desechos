package com.caircb.rcbtracegadere.generics;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Looper;

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
import java.util.List;

/**
 * Created by jlsuarez on 8/1/2018.
 */

public class MyPrint {

    private ZebraPrinterConnection zebraPrinterConnection;
    private ZebraPrinter printer;
    private String DEFAULT_PRINTER_MAC;
    ProgressDialog dialog;
    Context mContext;
    Activity activity;

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

    public void pinter(Integer idAppManifiesto){
        final List<ItemEtiqueta> etiquetas = MyApp.getDBO().manifiestoDetallePesosDao().consultarBultosImpresion(idAppManifiesto);

        if(etiquetas!= null && etiquetas.size()>0){
            dialog.show();
            //recorrer array para setear numero de bulto...
            Integer manifiestoDetalleID=0,index=0;
            for(ItemEtiqueta it:etiquetas){
                if(it.getIdAppManifiestoDetalle()!=manifiestoDetalleID){
                    index=1;
                }
                it.setIndexEtiqueta(index);
                index++;
            }

            new Thread(new Runnable() {
                public void run() {
                    Looper.prepare();
                    doConnectionTest(printer,etiquetas);
                    Looper.loop();
                    Looper.myLooper().quit();
                }
            }).start();

        }else{
            //mensaje...

        }
    }

    private void onCreatePrint(){
        DEFAULT_PRINTER_MAC = MyApp.getDBO().impresoraEntity().searchMac();
    }

    public String getMacAddressFieldText() {
        return this.DEFAULT_PRINTER_MAC;
    }

    /*private void enableTestButton(final boolean enabled) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                //btnImprimir.setEnabled(enabled);
                //btnCancelarImpresion.setEnabled(enabled);
            }
        });
    }*/


    private void disconnect() {
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



    private void doConnectionTest(ZebraPrinter printer,List<ItemEtiqueta> etiquetas) {
        printer = connect();
        if (printer != null) {
            sendTestLabel(printer,etiquetas);
        } else {
            disconnect();
        }
    }

    private void sendTestLabel(ZebraPrinter printer,List<ItemEtiqueta> etiquetas) {
        boolean complete=false;
        try {
            byte[] configLabel;
            //outerloop:
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
                        row.getTratamiento(),
                        "",
                        false
                        );
                zebraPrinterConnection.write(configLabel);
                MyThread.sleep(50);
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
            else disconnect();

        }

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
        String ItemDescripcion = recorreString(residuo);

        if (DEFAULT_PRINTER_MAC.equals("AC:3F:A4:8D:25:53")){
            cpclConfigLabel = "^XA^LH30,30^FO140,230^BQN,2,10,H^FDMM,A"+codigoQr.trim()+"^FS^FO50,60^AD^FD "+ cliente+"^FS^FO50,90^AD^FD #M.U.E: "+manifiesto.trim()+"^FS^FO50,120^AD^FD FECHA: "+fecha+"^FS^FO50,180^AD^FD RESPONSABLE: "+ MySession.getUsuarioNombre().toUpperCase()+"^FS ^XZ";
        }else{
            //String descripcion = row.getDescripcion().substring(10, 33);
            cpclConfigLabel = "^XA^LH30,30^FO120,230^BQN,2,10,H^FDMM,A"+codigoQr.trim()+
                    "^FS^FO40,60^AD^FD "+ (cliente.length()> 32 ? cliente.substring(0,32):cliente) +
                    "^FS^FO40,90^AD^FD #M.U.E: "+manifiesto.trim()+
                    "^FS^FO40,120^AD^FD FECHA: "+fecha+
                    "^FS^FO40,150^AD^FD PESO:"+peso+
                    //"^FS^FO40,150^AD^FD UNIDAD: "+row.getUnidad()+
                    //"^FS^FO280,150^AD^FD PESO:"+row.getPeso()+
                    "^FS^FO40,180^AD^FD RESPONSABLE: "+ MySession.getUsuarioNombre().toUpperCase()+
                    "^FS^FO35,470^AD^FD ITEM:"+ ItemDescripcion.toUpperCase() +
                    "^FS^FO35,500^AD^FD NO. BULTOS:"+ numeroBulto +
                    "^FS^FO35,530^AD^FD TRATAMIENTO:"+tratamiento +
                    "^FS^FO35,560^AD^FD DESTINATARIO:"+ destinatario.toUpperCase() +
                    "^FS^FO35,590^AD^FD DEVOLUCION RECIPIENTE:"+ (aplicaDevolucion?"SI":"NO") + "^FS ^XZ";
        }

        configLabel = cpclConfigLabel.getBytes();
        return configLabel;
    }

    private String recorreString(String caracter){
        String impresion="";
        String part1="",  resto ="", parte2="";
        String residuo = caracter.substring(10,caracter.length());
        Integer totalCaract = residuo.length();

        if(totalCaract > 27){
            part1 = residuo.substring(0,27);
            resto = residuo.substring(27,totalCaract);

            if(resto.length() > 27){
                parte2 = resto.substring(0,27);
                impresion = part1 + "^FS^FO50,560^AD^FD " + parte2;
            }else{
                impresion = part1 + "^FS^FO50,560^AD^FD " + resto;
            }
        }else{
            impresion = residuo;
        }
       return impresion;
    }

    private ZebraPrinter connect() {
        //setStatus("Conectando...", Color.YELLOW);
        zebraPrinterConnection = null;
        zebraPrinterConnection = new BluetoothPrinterConnection(DEFAULT_PRINTER_MAC);

        try {
            zebraPrinterConnection.open();
            //setStatus("Conectado", Color.GREEN);
        } catch (ZebraPrinterConnectionException e) {
            //setStatus("Error de conexión! Desconectando", Color.RED);
            MyThread.sleep(1000);
            disconnect();
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
                disconnect();
            }catch (ZebraPrinterConnectionException e) {
                //setStatus("Falla de conexión, por favor reinicie la impresora", Color.RED);
                printer = null;
                MyThread.sleep(1000);
                disconnect();
            }
        }

        return printer;
    }

    public void setOnPrinterListener(@NonNull OnPrinterListener l){
        mOnPrinterListener = l;
    }
}

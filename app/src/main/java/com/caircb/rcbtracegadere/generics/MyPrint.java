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
                    "",
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
        String ItemDescripcion = recorreString(residuo);

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
                    "^FS^FO60,530^AD^FD NO. BULTOS:"+ numeroBulto +
                    "^FS^FO60,560^AD^FD TRATAMIENTO:"+ tratamiento +
                    "^FS^FO60,590^AD^FD DESTINATARIO:"+ destinatario.toUpperCase() +
                    "^FS^FO60,620^AD^FD DEVOLUCION RECIPIENTE:"+ (aplicaDevolucion?"SI":"NO") +
                    "^FS^FO60,650^AD^FD ITEM:"+ ItemDescripcion.toUpperCase() +
                    "^FS ^XZ";
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
        String ItemDescripcion = recorreString(residuo);

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
                    "^FS^FO60,530^AD^FD NO. BULTOS:"+ numeroBulto +
                    "^FS^FO60,560^AD^FD TRATAMIENTO:"+tratamiento +
                    "^FS^FO60,590^AD^FD DESTINATARIO:"+ destinatario.toUpperCase() +
                    "^FS^FO60,620^AD^FD DEVOLUCION RECIPIENTE:"+ (aplicaDevolucion?"SI":"NO") +
                    "^FS^FO60,650^AD^FD ITEM:"+ ItemDescripcion.toUpperCase() +
                    "^FS ^XZ";
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
}

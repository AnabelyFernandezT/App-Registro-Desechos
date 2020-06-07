package com.caircb.rcbtracegadere.generics;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.RowItemManifiestoPrint;
import com.zebra.android.comm.BluetoothPrinterConnection;
import com.zebra.android.comm.ZebraPrinterConnection;
import com.zebra.android.comm.ZebraPrinterConnectionException;
import com.zebra.android.printer.PrinterLanguage;
import com.zebra.android.printer.ZebraPrinter;
import com.zebra.android.printer.ZebraPrinterFactory;
import com.zebra.android.printer.ZebraPrinterLanguageUnknownException;

import java.util.List;

/**
 * Created by jlsuarez on 8/1/2018.
 */

public class MyPrint {

    private ZebraPrinterConnection zebraPrinterConnection;
    private ZebraPrinter printer;
    private String DEFAULT_PRINTER_MAC;
    private String DEFAULT_TAMA_ETIQ;
    String codigoRec;

    //TextView statusField;
    //Button btnImprimir;
    //Button btnCancelarImpresion;
    ProgressDialog dialog;
    Context mContext;
    Activity activity;

    //public DBAdapter dbHelper;
    public Cursor cursor;

    public MyPrint(@NonNull Activity activity) {
        this.mContext=activity.getApplicationContext();
        this.activity = activity;

        //inicialize spinner...
        dialog = new ProgressDialog(activity);
        dialog.setMessage("Imprimiendo "+ System.getProperty("line.separator")+"esto puede tardar varios segundos...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);

        //this.statusField=statusField;
        //this.btnCancelarImpresion = btnCancelar;
        //this.dbHelper = new DBAdapter(mContext);
        onCreatePrint();
    }

    private void onCreatePrint(){
        DEFAULT_PRINTER_MAC = MyApp.getDBO().impresoraEntity().searchMac();
        //dbHelper.open();
        //ParametroEntity p = MyApp.getDBO().parametroDao().fetchParametroEspecifico("MacPrinter");
        //DEFAULT_PRINTER_MAC = p.getValor();
        //p = MyApp.getDBO().parametroDao().fetchParametroEspecifico("TamaEtiqueta");
        //DEFAULT_TAMA_ETIQ = p.getValor();
        //dbHelper.close();
    }
    /*private void onRefressPrint(){
        dbHelper.open();
        cursor = dbHelper.fetchParametroEspecifico("MacPrinter");
        DEFAULT_PRINTER_MAC = AndroidUtils.getValue(cursor, DataBaseHelper.KEY_VALOR);
        cursor = dbHelper.fetchParametroEspecifico("TamaEtiqueta");
        DEFAULT_TAMA_ETIQ = AndroidUtils.getValue(cursor, DataBaseHelper.KEY_VALOR);
        dbHelper.close();
    }*/

    public String getMacAddressFieldText() {
        return this.DEFAULT_PRINTER_MAC;
    }

    private void enableTestButton(final boolean enabled) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                //btnImprimir.setEnabled(enabled);
                //btnCancelarImpresion.setEnabled(enabled);
            }
        });
    }
    /*
    private void setStatus(final String statusMessage, final int color) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                //statusField.setBackgroundColor(color);
                //statusField.setText(statusMessage);
            }
        });
        MyThread.sleep(1000);
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
            enableTestButton(true);
        }
    }

    public void pinter(final String manifiesto,final String cliente,final String fecha,
                       final List<RowItemManifiestoPrint> detalle){
        dialog.show();
        new Thread(new Runnable() {
            public void run() {
                enableTestButton(false);
                Looper.prepare();
                doConnectionTest(manifiesto,cliente,fecha, detalle);
                Looper.loop();
                Looper.myLooper().quit();
            }
        }).start();
    }

    private void doConnectionTest(String manifiesto,String cliente,String fecha,List<RowItemManifiestoPrint> detalle) {
        printer = connect();
        if (printer != null) {
            sendTestLabel(manifiesto,cliente,fecha, detalle);
        } else {
            disconnect();
        }
    }

    private void sendTestLabel(String manifiesto,String cliente,String fecha,List<RowItemManifiestoPrint> detalle) {
        try {
            byte[] configLabel;
            //outerloop:
            Integer contador = 0 ;
            for(RowItemManifiestoPrint row: detalle){
                if(row.isEstado()) {
                    for (int x = 0; x < row.getCantidadBulto(); x++) {
                        contador = contador + 1;
                        configLabel = getConfigLabel(printer, manifiesto, manifiesto.trim() + "." + row.getId(), cliente, fecha, row,
                                (String.valueOf(detalle.size())+"/"+ contador ));
                        zebraPrinterConnection.write(configLabel);
                        MyThread.sleep(50);
                    }
                }
                ///---------------PRUEBAS DE ETIQUETAS---------------------------////
                /*else {
                    for (int x = 0; x < row.getCantidadBulto(); x++) {
                        contador = contador + 1;
                        configLabel = getConfigLabel(printer, manifiesto, manifiesto.trim() + "." + row.getId(), cliente, fecha, row,
                                (String.valueOf(detalle.size())+"/"+ contador ));
                        zebraPrinterConnection.write(configLabel);
                        MyThread.sleep(50);
                    }
                }*/
            }
                /*
                if (zebraPrinterConnection instanceof BluetoothPrinterConnection) {
                    String friendlyName = ((BluetoothPrinterConnection) zebraPrinterConnection).getFriendlyName();
                    //setStatus(friendlyName, Color.MAGENTA);
                    MyThread.sleep(100);
                }

            } */

                //setStatus("Enviando información", Color.BLUE);


        } catch (ZebraPrinterConnectionException e) {
            //setStatus(e.getMessage(), Color.RED);
        } catch (Exception e) {
            // TODO: handle exception
            //setStatus(e.getMessage(), Color.RED);
        }
        finally {
            disconnect();
        }




/*
        try {

            byte[] configLabel = getConfigLabel(printer,manifiesto,cliente,fecha);
            if(configLabel!=null) {
                zebraPrinterConnection.write(configLabel);

                setStatus("Enviando información", Color.BLUE);
                MyThread.sleep(500);
                if (zebraPrinterConnection instanceof BluetoothPrinterConnection) {
                    String friendlyName = ((BluetoothPrinterConnection) zebraPrinterConnection).getFriendlyName();
                    setStatus(friendlyName, Color.MAGENTA);
                    MyThread.sleep(100);
                }
            }
        } catch (ZebraPrinterConnectionException e) {
            setStatus(e.getMessage(), Color.RED);
        } catch (Exception e) {
            // TODO: handle exception
            setStatus(e.getMessage(), Color.RED);
        }
        finally {
            disconnect();
        }
*/
    }

    private byte[] getConfigLabel(ZebraPrinter printer,String manifiesto,String codigoQr,String cliente,String fecha,RowItemManifiestoPrint row,String numeroItems){
        PrinterLanguage printerLanguage = printer.getPrinterControlLanguage();
        String cpclConfigLabel="";
        byte[] configLabel = null;
        String ItemDescripcion = recorreString(row.getDescripcion());

        if (DEFAULT_PRINTER_MAC.equals("AC:3F:A4:8D:25:53")){
            cpclConfigLabel = "^XA^LH30,30^FO140,230^BQN,2,10,H^FDMM,A"+codigoQr.trim()+"^FS^FO50,60^AD^FD "+ cliente+"^FS^FO50,90^AD^FD #M.U.E: "+manifiesto.trim()+"^FS^FO50,120^AD^FD FECHA: "+fecha+"^FS^FO50,180^AD^FD RESPONSABLE: "+ MySession.getUsuarioNombre().toUpperCase()+"^FS ^XZ";
        }else{
            //String descripcion = row.getDescripcion().substring(10, 33);
            cpclConfigLabel = "^XA^LH30,30^FO120,230^BQN,2,10,H^FDMM,A"+codigoQr.trim()+
                    "^FS^FO40,60^AD^FD "+ (cliente.length()> 32 ? cliente.substring(0,32):cliente) +
                    "^FS^FO40,90^AD^FD #M.U.E: "+manifiesto.trim()+
                    "^FS^FO40,120^AD^FD FECHA: "+fecha+
                    "^FS^FO40,150^AD^FD PESO:"+row.getPeso()+
                    //"^FS^FO40,150^AD^FD UNIDAD: "+row.getUnidad()+
                    //"^FS^FO280,150^AD^FD PESO:"+row.getPeso()+
                    "^FS^FO40,180^AD^FD RESPONSABLE: "+ MySession.getUsuarioNombre().toUpperCase()+
                    "^FS^FO35,470^AD^FD ITEM:"+ ItemDescripcion.toUpperCase() +
                    "^FS^FO35,500^AD^FD NO. BULTOS:"+ numeroItems +
                    "^FS^FO35,530^AD^FD TRATAMIENTO:"+ row.getTratamiento() +
                    "^FS^FO35,560^AD^FD DESTINATARIO:"+ ItemDescripcion.toUpperCase() +
                    "^FS^FO35,590^AD^FD DEVOLUCION RECIPIENTE:"+ row.getDevolucionRecp() + "^FS ^XZ";

            //cpclConfigLabel = "^XA^LH30,30^FO140,230^BQN,2,10,H^FDMM,A"+manifiesto.trim()+"^FS^FO50,60^AD^FD "+ (cliente.length()> 32 ? cliente.substring(0,32):cliente) +"^FS^FO50,90^AD^FD #M.U.E: "+manifiesto+"^FS^FO50,120^AD^FD FECHA: "+fecha+"^FS^FO50,150^AD^FD RESPONSABLE: "+ MySession.getUsuarioNombre().toUpperCase()+"^FS ^XZ";
        }

        configLabel = cpclConfigLabel.getBytes();

        /*
        if (printerLanguage == PrinterLanguage.CPCL){
            //cpclConfigLabel = "! 0 200 200 500 1\r\n"+"VB QR 195 240 M 2 U 9\r\n"+"MA "+codigoQr+"\r\nENDQR\r\n"+"T180 7 0 405 280 "+codigoQr+"\r\nFORM\r\n"+"PRINT\r\n";
            cpclConfigLabel = "! 0 200 200 500 1\r\n"+"VB QR 190 245 M 2 U 9\r\n"+"MA "+codigoQr+"\r\nENDQR\r\n"+"T 7 0 170 20 "+codigoQr+"\r\nFORM\r\n"+"PRINT\r\n";
        }
        else if(printerLanguage == PrinterLanguage.ZPL){
            if(DEFAULT_PRINTER_MAC.equals("00:22:58:35:EB:C9")){
                cpclConfigLabel="^XA^POI^PR2^LH30,30^FO140,1^AC^FD"+codigoQr+"^FS^FO170,30^BQN,2,8,H^FDMM,A"+codigoQr+"^FS^XZ";
            }else {
                cpclConfigLabel = "^XA^POI^PR2^LH30,30^FO140,25^AC^FD" + codigoQr + "^FS^FO170,60^BQN,2,8,H^FDMM,A" + codigoQr + "^FS^XZ";
            }
        }
        */
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
    /*
    public void pinter(final List<RowItemRecepcion> listaPeso, final String cliente, final String manifiesto,final String numeroOT, final String fecha, final boolean isDialog){


        dialog.show();
        new Thread(new Runnable() {
            public void run() {
                enableTestButton(false);
                Looper.prepare();
                doConnectionTest(listaPeso, cliente,manifiesto, numeroOT,fecha,isDialog);
                Looper.loop();
                Looper.myLooper().quit();
            }
        }).start();
    }

    private void sendTestLabel(List<RowItemRecepcion> listaPeso, String cliente, String manifiesto, String numeroOt, String fecha, boolean isDialog) {



        try {
            if (isDialog){
                byte[] configLabel = getConfigLabel(null,cliente,manifiesto,numeroOt,printer,DEFAULT_PRINTER_MAC,fecha, isDialog);
                zebraPrinterConnection.write(configLabel);
            }else {
                for (RowItemRecepcion row:listaPeso){


                    byte[] configLabel = getConfigLabel(row,cliente,manifiesto,numeroOt,printer,DEFAULT_PRINTER_MAC,fecha, isDialog);
                    zebraPrinterConnection.write(configLabel);

                }
            }


            setStatus("Enviando información", Color.BLUE);
            MyThread.sleep(1500 * listaPeso.size());
            if (zebraPrinterConnection instanceof BluetoothPrinterConnection) {
                String friendlyName = ((BluetoothPrinterConnection) zebraPrinterConnection).getFriendlyName();
                setStatus(friendlyName, Color.MAGENTA);
                MyThread.sleep(500);
            }
        } catch (ZebraPrinterConnectionException e) {
            setStatus(e.getMessage(), Color.RED);
        } catch (Exception e) {
            // TODO: handle exception
            setStatus(e.getMessage(), Color.RED);
        }
        finally {
            disconnect();
        }
    }
    */

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



    /*
    private static byte[] getConfigLabel(RowItemRecepcion row, String cliente, String manifiesto, String numeroOt, ZebraPrinter printer, String DEFAULT_PRINTER_MAC, String fecha, boolean isDialog) {

        PrinterLanguage printerLanguage = printer.getPrinterControlLanguage();
        String cpclConfigLabel="";
        byte[] configLabel = null;


        if (isDialog){
            if (printerLanguage == PrinterLanguage.ZPL) {

                if (DEFAULT_PRINTER_MAC.equals("AC:3F:A4:8D:25:53")){
                    cpclConfigLabel = "^XA^LH30,30^FO140,230^BQN,2,10,H^FDMM,A"+numeroOt.trim()+"^FS^FO50,60^AD^FD "+ cliente+"^FS^FO50,90^AD^FD #M.U.E: "+manifiesto+"^FS^FO50,120^AD^FD FECHA: "+fecha+"^FS^FO50,150^AD^FD RESPONSABLE: "+ MySession.getUsuario().toUpperCase()+"^FS ^XZ";
                }else {
                    cpclConfigLabel = "^XA^LH30,30^FO140,230^BQN,2,10,H^FDMM,A"+numeroOt.trim()+"^FS^FO50,60^AD^FD "+ (cliente.length()> 32 ? cliente.substring(0,32):cliente) +"^FS^FO50,90^AD^FD #M.U.E: "+manifiesto+"^FS^FO50,120^AD^FD FECHA: "+fecha+"^FS^FO50,150^AD^FD RESPONSABLE: "+ MySession.getUsuario().toUpperCase()+"^FS ^XZ";
                }

                configLabel = cpclConfigLabel.getBytes();
            }
        }else {
            if (row.getCheckImpresion()==1){
                if (printerLanguage == PrinterLanguage.ZPL) {
                    if (DEFAULT_PRINTER_MAC.equals("AC:3F:A4:8D:25:53"))
                    {
                        cpclConfigLabel = "^XA^PQ"+row.getCantidadRecipiente()+",0,0,N ^LH30,30^FO100,280^BQN,2,10,H^FDMM,A"+ row.getCodeQR().trim().replace("\n", "").replace("\r", "")+ "^FS^FO10,70^AD^FD "+ (cliente.length()> 32 ? cliente.substring(0,32):cliente)+"^FS^FO10,100^AD^FD #M.U.E:"+ manifiesto+"^FS^FO10,130^AD^FD PESO:"+ row.getPeso()+" "+row.getTipoUnidad()+"/ E.F:"+ row.getEstadoFisico() +" ^FS^FO10,160^AD^FD T.RSD:"+ row.getTipoResiduo()+"^FS^FO10,190^AD^FD TRT:"+ row.getCodigoTratamiento()+"^FS^FO10,220^AD^FD FECHA:"+ fecha+"^FS^FO10,250^AD^FD #ORDEN:"+ numeroOt+" / CANT:"+row.getCantidadRecipiente()+"^FS^XZ";
                    }else {
                        cpclConfigLabel = "^XA^PQ"+row.getCantidadRecipiente()+",0,0,N ^LH30,30^FO100,350^BQN,2,10,H^FDMM,A"+ row.getCodeQR().trim().replace("\n", "").replace("\r", "")+ "^FS^FO50,120^AD^FD "+ (cliente.length()> 32 ? cliente.substring(0,32):cliente)+"^FS^FO50,150^AD^FD #M.U.E:"+ manifiesto+"^FS^FO50,180^AD^FD PESO:"+ row.getPeso()+" "+row.getTipoUnidad()+ "/ E.F:"+ row.getEstadoFisico() +" ^FS^FO50,210^AD^FD T.RSD:"+ row.getTipoResiduo()+"^FS^FO50,240^AD^FD TRT:"+ row.getCodigoTratamiento()+"^FS^FO50,270^AD^FD FECHA:"+ fecha+"^FS^FO50,300^AD^FD #ORDEN:"+ numeroOt+" / CANT:"+row.getCantidadRecipiente()+"^FS^XZ";
                    }
                    configLabel = cpclConfigLabel.getBytes();
                }

            }else{
                if (printerLanguage == PrinterLanguage.ZPL) {

                    if (DEFAULT_PRINTER_MAC.equals("AC:3F:A4:8D:25:53")){
                        cpclConfigLabel = "^XA^LH30,30^FO160,310^BQN,2,10,H^FDMM,A"+ row.getCodeQR().trim().replace("\n", "").replace("\r", "")+ "^FS^FO60,70^AD^FD "+ (cliente.length()> 32 ? cliente.substring(0,32):cliente)+"^FS^FO60,100^AD^FD #M.U.E:"+ manifiesto+"^FS^FO60,130^AD^FD PESO:"+ row.getPeso()+" "+row.getNombreUnidad()+"/ E.F:"+ row.getEstadoFisico() +" ^FS^FO60,160^AD^FD T.RSD:"+ row.getTipoResiduo()+"^FS^FO60,190^AD^FD TRT:"+ row.getCodigoTratamiento()+"^FS^FO60,220^AD^FD FECHA:"+ fecha+"^FS^FO60,250^AD^FD #ORDEN:"+ numeroOt+" / CANT:"+row.getCantidadRecipiente()+"^FS^FO60,280^AD^FD RESPONSABLE: "+MySession.getUsuario()+"^FS^XZ";
                    }else {
                        //cpclConfigLabel = "^XA^LH30,30^FO160,280^BQN,2,10,H^FDMM,A"+ row.getCodeQR().trim().replace("\n", "").replace("\r", "")+ "^FS^FO60,70^AD^FD "+ (cliente.length()> 32 ? cliente.substring(0,32):cliente)+"^FS^FO60,100^AD^FD #M.U.E:"+ manifiesto+"^FS^FO60,130^AD^FD PESO:"+ row.getPeso()+" "+row.getTipoUnidad()+"/ E.F:"+ row.getEstadoFisico() +" ^FS^FO60,160^AD^FD T.RSD:"+ row.getTipoResiduo()+"^FS^FO60,190^AD^FD TRT:"+ row.getCodigoTratamiento()+"^FS^FO60,220^AD^FD FECHA:"+ fecha+"^FS^FO60,250^AD^FD #ORDEN:"+ numeroOt+" / CANT:"+row.getCantidadRecipiente()+"^FS^XZ";
                        cpclConfigLabel = "^XA^LH30,30^FO160,310^BQN,2,10,H^FDMM,A"+ row.getCodeQR().trim().replace("\n", "").replace("\r", "")+ "^FS^FO60,70^AD^FD "+ (cliente.length()> 32 ? cliente.substring(0,32):cliente)+"^FS^FO60,100^AD^FD #M.U.E:"+ manifiesto+"^FS^FO60,130^AD^FD PESO:"+ row.getPeso()+" "+row.getNombreUnidad()+"/ E.F:"+ row.getEstadoFisico() +" ^FS^FO60,160^AD^FD T.RSD:"+ row.getTipoResiduo()+"^FS^FO60,190^AD^FD TRT:"+ row.getCodigoTratamiento()+"^FS^FO60,220^AD^FD FECHA:"+ fecha+"^FS^FO60,250^AD^FD #ORDEN:"+ numeroOt+" / CANT:"+row.getCantidadRecipiente()+"^FS^FO60,280^AD^FD RESPONSABLE: "+MySession.getUsuario()+"^FS^XZ";
                    }

                    configLabel = cpclConfigLabel.getBytes();
                }

            }
        }




        return configLabel;

    }
    */

}

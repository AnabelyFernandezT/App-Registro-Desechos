package com.caircb.rcbtracegadere.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.adapters.ListaValoresAdapter;
import com.caircb.rcbtracegadere.database.dao.ManifiestoPaqueteDao;
import com.caircb.rcbtracegadere.database.entity.ManifiestoDetalleEntity;
import com.caircb.rcbtracegadere.database.entity.NotificacionPesoExtraEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoDetallePesosEntity;
import com.caircb.rcbtracegadere.database.entity.PaqueteEntity;
import com.caircb.rcbtracegadere.database.entity.ParametroEntity;
import com.caircb.rcbtracegadere.generics.MyDialog;
import com.caircb.rcbtracegadere.generics.MyPrint;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.CatalogoItemValor;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DialogBultos extends MyDialog implements View.OnClickListener {

    LinearLayout btn_1, btn_2, btn_3, btn_delete, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_0, btn_decimal, btn_cancel, btn_ok, btn_add;
    EditText txtpantalla;
    ListView listViewBultos;
    TextView txtTotal;
    AlertDialog.Builder alertDialog;
    AlertDialog alert;
    String valor = null;
    ParametroEntity para,idDetalleValidacion;
    NotificacionPesoExtraEntity pesoExtraEntity;
    String dato = "0";
    String inputDefault = "0";
    String codigoDetalle = "", vreferencial;
    Double pesoReferencial;
    BigDecimal subtotal = BigDecimal.ZERO;
    ListaValoresAdapter listaValoresAdapter;
    List<CatalogoItemValor> bultos;
    Integer position;
    Integer idManifiesto;
    Integer idManifiestoDetalle;
    Integer tipoPaquete;
    Integer autorizacion=0;
    String idManifiestoValidacion="";
    PaqueteEntity pkg;
    ManifiestoDetalleEntity detalle;
    List<String> itemsCategoriaPaquete;
    Boolean closable = false;
    MyPrint print;
    Integer tipoGestion;
    Integer cantidaBultosInitial;
    DialogBuilder builder;
    boolean faltaImpresos = false;
    Integer registraTara;

    public interface OnBultoListener {
        public void onSuccessful(
                BigDecimal valor,
                int position,
                int cantidad,
                PaqueteEntity pkg,
                boolean isClose,
                boolean faltaImpresiones,
                boolean isChangeTotalBultos);

        void onCanceled(boolean falataImpresos);
    }

    private OnBultoListener mOnBultoListener;

    public DialogBultos(
            @NonNull Context context,
            @NonNull Integer position,
            @NonNull Integer idManifiesto,
            @NonNull Integer idManifiestoDetalle,
            @NonNull Integer tipoPaquete,
            @NonNull String codigoDetalle,
            Integer tipoGestion,
            Integer registraTara) {
        super(context, R.layout.dialog_bultos);

        this.position = position;
        this.idManifiesto = idManifiesto;
        this.idManifiestoDetalle = idManifiestoDetalle;
        this.tipoPaquete = tipoPaquete;
        this.codigoDetalle = codigoDetalle;
        this.tipoGestion = tipoGestion;
        this.registraTara=registraTara;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getView());

        initBotones();
        initCategoriaPaquetes();
        initDataOnOpen();
        detalle = MyApp.getDBO().manifiestoDetalleDao().fecthConsultarManifiestoDetallebyID(idManifiesto, idManifiestoDetalle);
        vreferencial = detalle.getValidadorReferencial();
        pesoReferencial = detalle.getPesoReferencial();
    }

    private void initBotones() {
        para = MyApp.getDBO().parametroDao().fetchParametroEspecifico("notif_value");
        pesoExtraEntity = MyApp.getDBO().pesoExtraDao().fetchPesoExtra(idManifiesto,idManifiestoDetalle);
        idDetalleValidacion = MyApp.getDBO().parametroDao().fetchParametroEspecifico("idValidacion_"+idManifiestoDetalle);
        if(idDetalleValidacion!=null){idManifiestoValidacion = idDetalleValidacion.getValor();}
        listViewBultos = getView().findViewById(R.id.listViewBultos);
        txtpantalla = getView().findViewById(R.id.txtpantalla);
        txtTotal = (TextView) getView().findViewById(R.id.txtTotal);
        btn_0 = getView().findViewById(R.id.btn_0);
        btn_1 = getView().findViewById(R.id.btn_1);
        btn_2 = getView().findViewById(R.id.btn_2);
        btn_3 = getView().findViewById(R.id.btn_3);
        btn_4 = getView().findViewById(R.id.btn_4);
        btn_5 = getView().findViewById(R.id.btn_5);
        btn_6 = getView().findViewById(R.id.btn_6);
        btn_7 = getView().findViewById(R.id.btn_7);
        btn_8 = getView().findViewById(R.id.btn_8);
        btn_9 = getView().findViewById(R.id.btn_9);
        btn_ok = getView().findViewById(R.id.btn_ok);
        btn_cancel = getView().findViewById(R.id.btn_cancel);
        btn_decimal = getView().findViewById(R.id.btn_decimal);
        btn_delete = getView().findViewById(R.id.btn_delete);
        btn_add = getView().findViewById(R.id.btn_add);

        btn_0.setOnClickListener(this);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
        btn_6.setOnClickListener(this);
        btn_7.setOnClickListener(this);
        btn_8.setOnClickListener(this);
        btn_9.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_decimal.setOnClickListener(this);
        btn_add.setOnClickListener(this);

        if(pesoExtraEntity!=null){
            if(pesoExtraEntity.getAutorizacion().toString().equals("0")){
                btn_add.setEnabled(false);
                btn_ok.setEnabled(false);
                autorizacion = 0;
            }else if (pesoExtraEntity.getAutorizacion().toString().equals("1")) {
                autorizacion = 1;
                btn_add.setEnabled(true);
                btn_ok.setEnabled(true);
            }else if(pesoExtraEntity.getAutorizacion().toString().equals("3")){
                btn_add.setEnabled(true);
                btn_ok.setEnabled(true);
            }
        }
    }

    private void initCategoriaPaquetes() {

        itemsCategoriaPaquete = new ArrayList<>();


        if (tipoPaquete != null) {
            pkg = MyApp.getDBO().paqueteDao().fechConsultaPaqueteEspecifico(tipoPaquete);

            if (pkg.getEntregaSoloFundas())
                itemsCategoriaPaquete.add(ManifiestoPaqueteDao.ARG_INFECCIOSO);
            if (pkg.getEntregaSoloGuardianes())
                itemsCategoriaPaquete.add(ManifiestoPaqueteDao.ARG_CORTOPUNZANTE);

            System.out.println("nombre");

            //showTipoPaquete();
            /*if(pkg!=null && (
                    pkg.getPaquetePorRecolccion().toString().equals("1")
                    && !pkg.getFlagAdicionales()
                    && !pkg.getFlagAdicionalFunda()
                    && !pkg.getFlagAdicionalGuardian()
            ))itemsCategoriaPaquete.add(ManifiestoPaqueteDao.ARG_INFECCIOSO_CORTOPUNZANTE);*/
        }
    }

    private void initDataOnOpen() {

        if (MyApp.getDBO().parametroDao().fecthParametroValor("auto_impresion" + MySession.getIdUsuario()).equals("1")) {
            MyApp.getDBO().manifiestoDetallePesosDao().updateBanderaImpresionByIdManifiestoIdDet(idManifiesto, idManifiestoDetalle, true);
            bultos = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarValores(idManifiesto, idManifiestoDetalle);
        } else {
            bultos = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarValores(idManifiesto, idManifiestoDetalle);
        }

        //bultos = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarValores(idManifiesto,idManifiestoDetalle);
        if (bultos == null) bultos = new ArrayList<>();
        cantidaBultosInitial = bultos.size();


        if (bultos.size() > 0) {
            List<ManifiestoDetallePesosEntity> listaPesos = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarBultosManifiestoDet(idManifiestoDetalle);
            double totalPesoTaraManifiestoDetalle=0.0;
            for (int i=0;i<listaPesos.size();i++){
                totalPesoTaraManifiestoDetalle=totalPesoTaraManifiestoDetalle+listaPesos.get(i).getPesoTaraBulto();
            }

            for (CatalogoItemValor r : bultos) {
                subtotal = subtotal.add(new BigDecimal(r.getValor()));
            }

            double pesoTotal=subtotal.doubleValue()-totalPesoTaraManifiestoDetalle;
            double pesoTotalMostrar = Double.parseDouble(obtieneDosDecimales(pesoTotal));
            txtTotal.setText("Peso Neto " + pesoTotalMostrar + " KG");
        }

        initAdapterBultos();

    }

    private void initAdapterBultos(){
        if(pesoExtraEntity!=null){
            listaValoresAdapter = new ListaValoresAdapter(getActivity(),bultos,idManifiesto, idManifiestoDetalle,registraTara,pesoExtraEntity.getAutorizacion(),txtTotal);
        }else {
            listaValoresAdapter = new ListaValoresAdapter(getActivity(),bultos,idManifiesto, idManifiestoDetalle,registraTara,3,txtTotal);
        }

        listaValoresAdapter.setOnItemBultoImpresion(new ListaValoresAdapter.OnItemBultoImpresionListener() {
            @Override
            public void onSendImpresion(Integer pos, double pesoTaraBulto) {
                CatalogoItemValor item = bultos.get(pos);
                ////DESCOMENTAR PARA IMPRIMIR CON IMPRESORA
                String tipoSubRuta = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta") == null ? "" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta");//1 ES INDUSTRIAL, 2 ES HOSPITALARIA
                if (tipoSubRuta.equals("2")){
                    checkEtiquetaTara(idManifiesto, idManifiestoDetalle, item);
                    MyApp.getDBO().manifiestoDetallePesosDao().updatePesoTara(idManifiesto, idManifiestoDetalle, item.getIdCatalogo(), pesoTaraBulto);
                }else {
                    imprimirEtiquetaIndividual(idManifiesto, idManifiestoDetalle, item);
                }



                List<ManifiestoDetallePesosEntity> listaPesos = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarBultosManifiestoDet(idManifiestoDetalle);
                double totalPesoTaraManifiestoDetalle=0.0;
                double pesoTotalValor=0.0;
                for (int i=0;i<listaPesos.size();i++){
                    totalPesoTaraManifiestoDetalle=totalPesoTaraManifiestoDetalle+listaPesos.get(i).getPesoTaraBulto();
                    pesoTotalValor=pesoTotalValor+listaPesos.get(i).getValor();
                }
                double pesoTotal=pesoTotalValor-totalPesoTaraManifiestoDetalle;
                DecimalFormat df = new DecimalFormat("000.00");
                double pesoTotalMostrar = Double.parseDouble(df.format(pesoTotal));
                txtTotal.setText("Peso Neto " + pesoTotalMostrar + " KG");
            }
        });
        listaValoresAdapter.setOnItemBultoListener(new ListaValoresAdapter.OnItemBultoListener() {
            @Override
            public void onEliminar(Integer pos) {
                CatalogoItemValor item = bultos.get(pos);
                MyApp.getDBO().manifiestoDetallePesosDao().deleteTableValoresById(item.getIdCatalogo());

                if (item.getTipo().length() > 0)
                    MyApp.getDBO().manifiestoPaqueteDao().quitarPaquete(idManifiesto, tipoPaquete, item.getTipo());

                List<ManifiestoDetallePesosEntity> listaPesos = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarBultosManifiestoDet(idManifiestoDetalle);
                double totalPesoTaraManifiestoDetalle=0.0;
                double pesoTotalValor=0.0;
                for (int i=0;i<listaPesos.size();i++){
                    totalPesoTaraManifiestoDetalle=totalPesoTaraManifiestoDetalle+listaPesos.get(i).getPesoTaraBulto();
                    pesoTotalValor=pesoTotalValor+listaPesos.get(i).getValor();
                }
                double pesoTotal=pesoTotalValor-totalPesoTaraManifiestoDetalle;


                subtotal = subtotal.subtract(new BigDecimal(item.getValor()));
                bultos.remove(item);

                listaValoresAdapter.filterList(bultos);
                listaValoresAdapter.notifyDataSetChanged();
                double pesoTotalMostrar = Double.parseDouble(obtieneDosDecimales(pesoTotal));
                txtTotal.setText("Peso Neto " + pesoTotalMostrar + " KG");

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        // Code here will run in UI thread
                        if (mOnBultoListener != null)
                            mOnBultoListener.onSuccessful(subtotal, position, bultos.size(), pkg, false, faltaImpresos, cantidaBultosInitial > 0 ? (cantidaBultosInitial != bultos.size() ? true : false) : false);
                    }
                });
            }
        });

        listViewBultos.setAdapter(listaValoresAdapter);
    }

    private void loadData() {
        //bultos = new ArrayList<>();

        if (bultos.size() > 0) {
            for (CatalogoItemValor r : bultos) {
                subtotal = subtotal.add(new BigDecimal(r.getValor()));
            }

            double pesoTotalMostrar = Double.parseDouble(obtieneDosDecimales(subtotal.doubleValue()));
            txtTotal.setText("Peso Neto " + pesoTotalMostrar + " KG");
        }

        bultos = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarValores(idManifiesto, idManifiestoDetalle);
        initAdapterBultos();

    }
    private void checkEtiquetaTara(final Integer idAppManifiesto, final Integer idManifiestoDetalle, final CatalogoItemValor item) {
        MyApp.getDBO().manifiestoDetallePesosDao().updateBanderaImpresion(idAppManifiesto, idManifiestoDetalle, item.getIdCatalogo(), true);
        item.setImpresion(true);
        listaValoresAdapter.notifyDataSetChanged();
    }
    private void imprimirEtiquetaIndividual(final Integer idAppManifiesto, final Integer idManifiestoDetalle, final CatalogoItemValor item) {

        //Probar sin impresiora
        /************************************/

        //bultos.clear();
        //subtotal= BigDecimal.ZERO;

        MyApp.getDBO().manifiestoDetallePesosDao().updateBanderaImpresion(idAppManifiesto, idManifiestoDetalle, item.getIdCatalogo(), true);
        item.setImpresion(true);
        listaValoresAdapter.notifyDataSetChanged();
        //loadData();

        /**************************************/

        //Probar con impresiora
/*
        try {
            print = new MyPrint(getActivity());
            print.setOnPrinterListener(new MyPrint.OnPrinterListener() {
                @Override
                public void onSuccessful() {
                    //Impresion finalizada
                    //bultos.clear();
                    //subtotal= BigDecimal.ZERO;
                    MyApp.getDBO().manifiestoDetallePesosDao().updateBanderaImpresion(idAppManifiesto, idManifiestoDetalle, item.getIdCatalogo(), true);
                    item.setImpresion(true);
                    listaValoresAdapter.notifyDataSetChanged();
                    //loadData();
                }
                @Override
                public void onFailure(String message) { messageBox(message); }
            });
            print.printerIndividual(idAppManifiesto, idManifiestoDetalle, item.getIdCatalogo(), item.getNumeroBulto());

        }catch (Exception e){
            messageBox("No hay conexion con la impresora");
            //if(mOnRegisterListener!=null)mOnRegisterListener.onSuccessful();
        }

*/

    }


    private void setDato(String valor) {

        switch (valor) {
            case "-":
                dato = dato.length() == 1 ? inputDefault : dato.substring(0, dato.length() - 1);
                break;
            case ".":
                dato = dato.indexOf(".") == -1 ? ((!inputDefault.equals(dato) ? dato : inputDefault) + valor) : dato;
                break;
            default:
                dato = dato.equals(valor) ? (inputDefault.equals(dato) ? inputDefault : dato + valor) : (dato.equals(inputDefault) ? "" : dato) + valor;
                break;
        }
        txtpantalla.setText(dato);
    }

    private void createBulto(BigDecimal imput) {
        if (imput.doubleValue() >= 0) {
            //si es tipo paquete .. solicitar escoger un tipo...
            if (tipoPaquete != null) {

                //if (!pkg.getFlagAdicionales() && pkg.getPaquetePorRecolccion().toString().equals("1") && bultos.size()>0){
                    //messageBox("Usted no puede aplicar mas de un paquete para esta recoleción");
                    //return;
                //}else {

                // showTipoPaquete(imput);  //ANTES

                if (tipoGestion == 100) {
                    Integer which = 0;
                    if (existeBultoCategoria(itemsCategoriaPaquete.get(which)) && !pkg.getFlagAdicionales() && !pkg.getFlagAdicionalFunda() && pkg.getPaquetePorRecolccion().toString().equals("1")) {
                        //alert.dismiss();
                        messageBox("Usted no puede agregar otro bulto de la categoria " + itemsCategoriaPaquete.get(which));
                        return;
                    }
                    addBulto(imput, itemsCategoriaPaquete.get(which));
                } else if (tipoGestion == 101) {
                    Integer which = 1;
                    if (existeBultoCategoria(itemsCategoriaPaquete.get(which)) && !pkg.getFlagAdicionales() && !pkg.getFlagAdicionalGuardian() && pkg.getPaquetePorRecolccion().toString().equals("1")) {
                        //alert.dismiss();
                        messageBox("Usted no puede agregar otro bulto de la categoria " + itemsCategoriaPaquete.get(which));
                        return;
                    }
                    addBulto(imput, itemsCategoriaPaquete.get(which));
                } else if (tipoGestion == 102) {
                    Integer which = 2;
                    addBulto(imput, itemsCategoriaPaquete.get(which));
                } else if (tipoGestion == 0) {
                    addBulto(imput, "");
                }

                //}
            } else {
                addBulto(imput, "");
            }
        }
    }



/*
    private void showTipoPaquete(final BigDecimal imput){

        alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("TIPO  PAQUETE");
        alertDialog.setSingleChoiceItems(itemsCategoriaPaquete.toArray(new String[itemsCategoriaPaquete.size()]), -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:

                        if(existeBultoCategoria(itemsCategoriaPaquete.get(which)) && !pkg.getFlagAdicionales() && !pkg.getFlagAdicionalFunda() && pkg.getPaquetePorRecolccion().toString().equals("1")){
                            alert.dismiss();
                            messageBox("Usted no puede agregar otro bulto de la categoria "+itemsCategoriaPaquete.get(which));
                            return;
                        }

                        addBulto(imput,itemsCategoriaPaquete.get(which));
                        alert.dismiss();
                        break;
                    case 1:
                        if(existeBultoCategoria(itemsCategoriaPaquete.get(which)) && !pkg.getFlagAdicionales() && !pkg.getFlagAdicionalGuardian() && pkg.getPaquetePorRecolccion().toString().equals("1")){
                            alert.dismiss();
                            messageBox("Usted no puede agregar otro bulto de la categoria "+itemsCategoriaPaquete.get(which));
                            return;
                        }
                        addBulto(imput, itemsCategoriaPaquete.get(which));
                        alert.dismiss();
                        break;
                    case 2:
                        addBulto(imput, itemsCategoriaPaquete.get(which));
                        alert.dismiss();
                        break;
                }
            }
        });
        alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        //alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alert.show();
    }
*/

    private boolean existeBultoCategoria(String categoria) {
        return MyApp.getDBO().manifiestoDetallePesosDao().existeBultoCategoriaPaquete(idManifiesto, idManifiestoDetalle, categoria);
    }

    public void addBulto(final BigDecimal imput, final String tipo){

        subtotal = subtotal.add(imput);
        Double _subTmp = subtotal.doubleValue();

        if(Double.compare(_subTmp,pesoReferencial) > 0){
            autorizacion = 0;
        }

        if( vreferencial.equals("SI") && subtotal.doubleValue() > pesoReferencial && autorizacion.equals(0)){
            if (builder != null) {
                builder.dismiss();
                builder = null;
            }

            builder = new DialogBuilder(getActivity());
            builder.setMessage("¿Necesita autorización porque el peso excede el peso referencial?");
            builder.setCancelable(false);
            builder.setPositiveButton("SI", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogNotificacionPesoExtra pesoExtra = new DialogNotificacionPesoExtra(getActivity(), idManifiestoDetalle, idManifiesto, subtotal.doubleValue());
                    //DialogNotificacionPesoExtra pesoExtra = new DialogNotificacionPesoExtra(getActivity(),idManifiestoDetalle,idManifiesto,Double.parseDouble(txtpantalla.getText().toString()));
                    pesoExtra.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    pesoExtra.setCancelable(false);
                    pesoExtra.show();
                    pesoExtra.setOnRegisterListener(new DialogNotificacionPesoExtra.OnRegisterListener() {
                        @Override
                        public void onSuccessful() {
                            txtpantalla.setText("0");
                            subtotal = subtotal.subtract(imput);
                            dato="0";
                            BigDecimal imput = new BigDecimal(txtpantalla.getText().toString());
                            //MyApp.getDBO().parametroDao().saveOrUpdate("idValidacion_"+idManifiestoDetalle,""+idManifiestoDetalle);
                            MyApp.getDBO().pesoExtraDao().saveOrUpdate(idManifiesto,idManifiestoDetalle,0.0,0);
                            //createBulto(imput);
                            btn_add.setEnabled(false);
                            btn_ok.setEnabled(false);
                           // DialogBultos.this.dismiss();
                            //addBulto(imput,tipo);
                            if (mOnBultoListener != null) {mOnBultoListener.onCanceled(faltaImpresos);}
                        }

                        @Override
                        public void onFailure() {

                        }
                    });
                    builder.dismiss();
                }
            });
            builder.setNegativeButton("NO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //subtotal = subtotal.subtract(imput);
                    builder.dismiss();
                    txtpantalla.setText("0");
                    subtotal = subtotal.subtract(imput);
                    dato="0";
                }
            });
            builder.show();

            return;
        }

        Integer ultimoBultoByIdDet = MyApp.getDBO().manifiestoDetallePesosDao().countNumeroBultosByIdManifiestoIdDet(idManifiesto, idManifiestoDetalle);
        ultimoBultoByIdDet = ultimoBultoByIdDet + 1;

        Long id;
        boolean banderaImpresion;

        String tipoSubRuta = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta") == null ? "" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta");//1 ES INDUSTRIAL, 2 ES HOSPITALARIA
        if (tipoSubRuta.equals("2")) {
            String checkTara = registraTara.toString();
            if (checkTara.equals("1")) {
                if (MyApp.getDBO().parametroDao().fecthParametroValor("auto_impresion" + MySession.getIdUsuario()).equals("1")) {
                    banderaImpresion = true;
                    id = MyApp.getDBO().manifiestoDetallePesosDao().saveValores(idManifiesto, idManifiestoDetalle, imput.doubleValue(), tipo, tipoPaquete, codigoDetalle, true, ultimoBultoByIdDet, 0.0);
                } else {
                    id = MyApp.getDBO().manifiestoDetallePesosDao().saveValores(idManifiesto, idManifiestoDetalle, imput.doubleValue(), tipo, tipoPaquete, codigoDetalle, false, ultimoBultoByIdDet, 0.0);
                    banderaImpresion = false;
                }
            }else {
                banderaImpresion = true;
                id = MyApp.getDBO().manifiestoDetallePesosDao().saveValores(idManifiesto, idManifiestoDetalle, imput.doubleValue(), tipo, tipoPaquete, codigoDetalle, true, ultimoBultoByIdDet, 0.0);
            }
        }else {
            if (MyApp.getDBO().parametroDao().fecthParametroValor("auto_impresion" + MySession.getIdUsuario()).equals("1")) {
                banderaImpresion = true;
            } else {
                banderaImpresion = false;
            }

            id = MyApp.getDBO().manifiestoDetallePesosDao().saveValores(idManifiesto, idManifiestoDetalle, imput.doubleValue(), tipo, tipoPaquete, codigoDetalle, banderaImpresion, ultimoBultoByIdDet, 0.0);
        }

        //Long id = MyApp.getDBO().manifiestoDetallePesosDao().saveValores(idManifiesto,idManifiestoDetalle,imput.doubleValue(),tipo,tipoPaquete,codigoDetalle, false, ultimoBultoByIdDet);

        if (tipo.length() > 0) {
            MyApp.getDBO().manifiestoPaqueteDao().saveOrUpdate(idManifiesto, tipoPaquete, tipo);
        }

        bultos.add(new CatalogoItemValor(id.intValue(), imput.toString(), tipo, banderaImpresion, ultimoBultoByIdDet));
        //bultos.add(new CatalogoItemValor(id.intValue(), imput.toString(),tipo, false, ultimoBultoByIdDet));
        listaValoresAdapter.notifyDataSetChanged();

        List<ManifiestoDetallePesosEntity> listaPesos = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarBultosManifiestoDet(idManifiestoDetalle);
        double totalPesoTaraManifiestoDetalle=0.0;
        double pesoTotalValor=0.0;
        for (int i=0;i<listaPesos.size();i++){
            totalPesoTaraManifiestoDetalle=totalPesoTaraManifiestoDetalle+listaPesos.get(i).getPesoTaraBulto();
            pesoTotalValor=pesoTotalValor+listaPesos.get(i).getValor();
        }
        double pesoTotal=pesoTotalValor-totalPesoTaraManifiestoDetalle;
        double pesoTotalMostrar = Double.parseDouble(obtieneDosDecimales(pesoTotal));
        txtTotal.setText("Peso Neto " + pesoTotalMostrar + " KG");
        dato = "0";
        txtpantalla.setText("0");

        if (closable && mOnBultoListener != null)
            mOnBultoListener.onSuccessful(subtotal, position, bultos.size() == 0 ? 1 : bultos.size(), pkg, true, faltaImpresos, cantidaBultosInitial > 0 ? (cantidaBultosInitial != bultos.size() ? true : false) : false);
    }

    /////////////////////////
    private void aplicar() {
        BigDecimal imput = new BigDecimal(txtpantalla.getText().toString());
        if (imput.doubleValue() == 0d) { // && subtotal.doubleValue() > 0d) {
            if (mOnBultoListener != null)
                mOnBultoListener.onSuccessful(subtotal, position, bultos.size(), pkg, true, faltaImpresos, cantidaBultosInitial > 0 ? (cantidaBultosInitial != bultos.size() ? true : false) : false);
        } else //if (imput.doubleValue() > 0d) {
            if (bultos.size() >= 0d && pkg == null) {
                createBulto(imput);
                if (mOnBultoListener != null)
                    mOnBultoListener.onSuccessful(subtotal, position, bultos.size() == 0 ? 1 : bultos.size(), pkg, true, faltaImpresos, cantidaBultosInitial > 0 ? (cantidaBultosInitial != bultos.size() ? true : false) : false);
            } else if (bultos.size() >= 0d && pkg != null) {
                //region para paquetes...
                closable = true;
                createBulto(imput);
            }
        //}

        /*
        if(imput.doubleValue()>0 ){
            if(bultos.size()==0) {
                createBulto(imput);
                mOnBultoListener.onSuccessful(subtotal, position, 1, pkg,true);
            }else if(bultos.size()>0){
                //preguntar si agrega el valor a un bulto...
                mOnBultoListener.onSuccessful(subtotal, position, bultos.size(), pkg,true);
            }
        }else if(imput.doubleValue()==0 && subtotal.doubleValue()>0){
            mOnBultoListener.onSuccessful(subtotal, position, bultos.size(), pkg,true);
        }*/
    }

    public void setOnBultoListener(@NonNull OnBultoListener l) {
        mOnBultoListener = l;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_0:
                setDato("0");
                break;
            case R.id.btn_1:
                setDato("1");
                break;
            case R.id.btn_2:
                setDato("2");
                break;
            case R.id.btn_3:
                setDato("3");
                break;
            case R.id.btn_4:
                setDato("4");
                break;
            case R.id.btn_5:
                setDato("5");
                break;
            case R.id.btn_6:
                setDato("6");
                break;
            case R.id.btn_7:
                setDato("7");
                break;
            case R.id.btn_8:
                setDato("8");
                break;
            case R.id.btn_9:
                setDato("9");
                break;
            case R.id.btn_ok:
                bultos = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarValores(idManifiesto, idManifiestoDetalle);
                String tipoSubRuta = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta") == null ? "" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta");//1 ES INDUSTRIAL, 2 ES HOSPITALARIA
                if (tipoSubRuta.equals("2")){
                    String checkTara = registraTara.toString();
                    if (checkTara.equals("1")){
                        List<ManifiestoDetallePesosEntity> listaPesos = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarBultosManifiestoDet(idManifiestoDetalle);
                        int contPesosTara=0;
                        for (int i=0;i<listaPesos.size();i++){
                            if (listaPesos.get(i).getPesoTaraBulto()==0){
                                contPesosTara++;
                            }
                        }
                        if (contPesosTara==0){
                            BigDecimal imputValor = new BigDecimal(txtpantalla.getText().toString());
                            if(!(txtpantalla.getText().toString().equals("0")))
                                createBulto(imputValor);
                            faltaImpresos = verificarTodosBultosImpresos();

                            if (!faltaImpresos){
                                MyApp.getDBO().manifiestoDetalleDao().updateFlagFaltaImpresiones(idManifiesto, idManifiestoDetalle, false);
                                if (mOnBultoListener != null) {
                                    aplicar();
                                    //autorizacion=0;
                                    //MyApp.getDBO().parametroDao().saveOrUpdate("notif_value",""+"0");
                                }
                            } else {
                                messageBox("Debe registrar todas las taras!!!");
                            }
                            break;
                        }else {
                            messageBox("Debe registrar todas las taras!!!");
                        }
                    }else {
                        BigDecimal imputValor = new BigDecimal(txtpantalla.getText().toString());
                        if(!(txtpantalla.getText().toString().equals("0")))
                            createBulto(imputValor);
                        faltaImpresos = verificarTodosBultosImpresos();

                        if (!faltaImpresos) {
                            MyApp.getDBO().manifiestoDetalleDao().updateFlagFaltaImpresiones(idManifiesto, idManifiestoDetalle, false);
                            if (mOnBultoListener != null) {
                                aplicar();
                                //autorizacion=0;
                                //MyApp.getDBO().parametroDao().saveOrUpdate("notif_value",""+"0");
                            }
                        } else {
                            messageBox("Debe imprimir todos los bultos para continuar...!");
                        }
                        break;
                    }
                }else {
                    BigDecimal imputValor = new BigDecimal(txtpantalla.getText().toString());
                    if(!(txtpantalla.getText().toString().equals("0")))
                        createBulto(imputValor);
                    faltaImpresos = verificarTodosBultosImpresos();

                    if (!faltaImpresos) {
                        MyApp.getDBO().manifiestoDetalleDao().updateFlagFaltaImpresiones(idManifiesto, idManifiestoDetalle, false);
                        if (mOnBultoListener != null) {
                            aplicar();
                            //autorizacion=0;
                            //MyApp.getDBO().parametroDao().saveOrUpdate("notif_value",""+"0");
                        }
                    } else {
                        aplicar();
                        messageBox("Debe imprimir todos los bultos para continuar...!");
                    }
                    break;
                }
            case R.id.btn_add:
                /*if(para!=null){
                    valor = para.getValor();
                    if(valor.equals("5")){
                        autorizacion = 1;
                        btn_add.setEnabled(true);
                        btn_ok.setEnabled(true);
                    }
                }*/

                final BigDecimal imput = new BigDecimal(txtpantalla.getText().toString());

                if(imput.compareTo(BigDecimal.ZERO) > 0)
                {
                    createBulto(imput);
                }
                else {
                    final DialogBuilder dialogBuilder2 = new DialogBuilder(getContext());
                    dialogBuilder2.setMessage("¿Desea ingresar un bulto con peso cero?");
                    dialogBuilder2.setTitle("CONFIRMACIÓN");
                    dialogBuilder2.setPositiveButton("SI", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder2.dismiss();
                            createBulto(imput);
                        }
                    });
                    dialogBuilder2.setNegativeButton("NO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder2.dismiss();
                        }
                    });
                    dialogBuilder2.show();
                }
                break;
            case R.id.btn_cancel:
                bultos = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarValores(idManifiesto, idManifiestoDetalle);
                String tipoSubRuta2 = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta") == null ? "" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta");//1 ES INDUSTRIAL, 2 ES HOSPITALARIA
                txtpantalla.setText("0");
                if (tipoSubRuta2.equals("2")) {
                    String checkTara = registraTara.toString();
                    if (checkTara.equals("1")) {
                        List<ManifiestoDetallePesosEntity> listaPesos2 = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarBultosManifiestoDet(idManifiestoDetalle);
                        int contPesosTara2=0;
                        for (int i=0;i<listaPesos2.size();i++){
                            if (listaPesos2.get(i).getPesoTaraBulto()==0){
                                contPesosTara2++;
                            }
                        }
                        if (contPesosTara2==0){
                            //limpiarBultosNoConfirmados();
                            faltaImpresos = verificarTodosBultosImpresos();
                            if (faltaImpresos) {
                                MyApp.getDBO().manifiestoDetalleDao().updateFlagFaltaImpresiones(idManifiesto, idManifiestoDetalle, true);
                            } else {
                                MyApp.getDBO().manifiestoDetalleDao().updateFlagFaltaImpresiones(idManifiesto, idManifiestoDetalle, false);
                            }
                            if (mOnBultoListener != null) {
                                mOnBultoListener.onCanceled(faltaImpresos);
                            }
                            //if(btn_add.isEnabled() && btn_ok.isEnabled()){
                            aplicar();
                            //}
                            break;
                        }else {
                            messageBox("Debe registrar todas las taras!!!");
                            break;
                        }
                    }else {
                        //limpiarBultosNoConfirmados();
                        faltaImpresos = verificarTodosBultosImpresos();
                        if (faltaImpresos) {
                            MyApp.getDBO().manifiestoDetalleDao().updateFlagFaltaImpresiones(idManifiesto, idManifiestoDetalle, true);
                        } else {
                            MyApp.getDBO().manifiestoDetalleDao().updateFlagFaltaImpresiones(idManifiesto, idManifiestoDetalle, false);
                        }
                        if (mOnBultoListener != null) {
                            mOnBultoListener.onCanceled(faltaImpresos);
                        }
                        //if(btn_add.isEnabled() && btn_ok.isEnabled()){
                        aplicar();
                        //}
                        break;
                    }
                }else {
                    //limpiarBultosNoConfirmados();
                    faltaImpresos = verificarTodosBultosImpresos();
                    if (faltaImpresos) {
                        MyApp.getDBO().manifiestoDetalleDao().updateFlagFaltaImpresiones(idManifiesto, idManifiestoDetalle, true);
                    } else {
                        MyApp.getDBO().manifiestoDetalleDao().updateFlagFaltaImpresiones(idManifiesto, idManifiestoDetalle, false);
                    }
                    if (mOnBultoListener != null) {
                        mOnBultoListener.onCanceled(faltaImpresos);
                    }
                    //if(btn_add.isEnabled() && btn_ok.isEnabled()){
                    aplicar();
                    //}
                    break;
                }



            case R.id.btn_decimal:
                setDato(".");
                break;
            case R.id.btn_delete:
                setDato("-");
                break;
            default:
                break;
        }
    }

    /*
    private void limpiarBultosNoConfirmados (){
        MyApp.getDBO().manifiestoDetallePesosDao().deleteTableValoresNoConfirmados(idManifiesto,idManifiestoDetalle);
        double nuevoSubtotal  = MyApp.getDBO().manifiestoDetallePesosDao().sumaPesoFinal(idManifiesto,idManifiestoDetalle);
        List <CatalogoItemValor>bultostTotal = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarValores(idManifiesto,idManifiestoDetalle);

        if(mOnBultoListener!=null)mOnBultoListener.onSuccessful(BigDecimal.valueOf(nuevoSubtotal),position,bultostTotal.size(),pkg,false);
    }
     */
    private boolean verificarTodosBultosImpresos() {
        boolean resul = false;
        if (bultos != null && bultos.size() > 0) {
            for (CatalogoItemValor item : bultos) {
                if (item.isImpresion() == false) {
                    resul = true;
                }
            }
        }
        return resul;
    }

    private String obtieneDosDecimales(double valor) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(valor);
    }

}

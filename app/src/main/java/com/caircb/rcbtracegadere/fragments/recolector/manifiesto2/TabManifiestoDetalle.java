package com.caircb.rcbtracegadere.fragments.recolector.manifiesto2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.strictmode.UnbufferedIoViolation;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.adapters.DialogMenuBaseAdapter;
import com.caircb.rcbtracegadere.adapters.ManifiestoDetalleAdapter;
import com.caircb.rcbtracegadere.database.dao.ManifiestoFileDao;
import com.caircb.rcbtracegadere.database.dao.ManifiestoPaqueteDao;
import com.caircb.rcbtracegadere.database.entity.ManifiestoDetalleEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoDetallePesosEntity;
import com.caircb.rcbtracegadere.database.entity.PaqueteEntity;
import com.caircb.rcbtracegadere.dialogs.DialogAgregarBultos;
import com.caircb.rcbtracegadere.dialogs.DialogAgregarFotografias;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.dialogs.DialogBultos;
import com.caircb.rcbtracegadere.dialogs.DialogBultosNo;
import com.caircb.rcbtracegadere.dialogs.DialogNotificacionDetalle;
import com.caircb.rcbtracegadere.generics.MyPrint;
import com.caircb.rcbtracegadere.helpers.MyCalculoPaquetes;
import com.caircb.rcbtracegadere.helpers.MyConstant;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.CalculoPaqueteResul;
import com.caircb.rcbtracegadere.models.CatalogoItemValor;
import com.caircb.rcbtracegadere.models.ItemManifiesto;
import com.caircb.rcbtracegadere.models.RowItemManifiesto;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TabManifiestoDetalle extends LinearLayout {

    /*private List<ItemManifiesto> rowItems;*/
    private List<RowItemManifiesto> detalles;
    private RecyclerView recyclerView;
    ManifiestoDetalleAdapter recyclerviewAdapter;
    private List<ManifiestoDetallePesosEntity> itemManifiestoDetalleBultos;
    Integer idAppManifiesto, tipoPaquete, estadoManifiesto;
    String numeroManifiesto;
    Window window;
    //ListView mDialogMenuItems;
    private List<ManifiestoDetallePesosEntity> listManifiestoBultos;
    Dialog dialogOpcioneItem;
    DialogBultos dialogBultos;
    LinearLayout novedadPesoPromedio, btnEvidenciaNovedadFrecuente, lnlCountPhoto, sectionRegistrarTara;
    RelativeLayout btnEliminarFotos;
    DialogAgregarFotografias dialogAgregarFotografias;
    TextView txtPesoPromedio, txtCountPhoto;
    //DialogMenuBaseAdapter dialogMenuBaseAdapter;
    MyCalculoPaquetes calculoPaquetes;
    FloatingActionButton mensajes;
    AlertDialog alert;
    AlertDialog.Builder alertDialog;
    List<String> itemsCategoriaPaquete;
    Integer tipoGestion;
    PaqueteEntity pkg;
    Integer tipoBalanza = 0;
    Integer tipoRecoleccion;
    String identificacion;
    String nombreCliente;
    String sucursal;
    private boolean isChangeTotalCreateBultos = false;
    CheckBox chkRegistrarTara;
    MyPrint print;
    /*Integer idSubRuta;*/
    ProgressDialog dialog;


    public TabManifiestoDetalle(Context context,
                                Integer manifiestoID,
                                Integer tipoPaquete,
                                String identificacion,
                                String nombreCliente,
                                String sucursal,
                                String numeroManifiesto,
                                Integer estado,
                                Integer tipoRecoleccion) {
        super(context);
        this.idAppManifiesto = manifiestoID;
        this.tipoPaquete = tipoPaquete;
        //this.detalles = detalles;
        this.identificacion = identificacion;
        this.nombreCliente = nombreCliente;
        this.sucursal = sucursal;
        this.estadoManifiesto = estado;
        this.numeroManifiesto = numeroManifiesto;
        this.tipoRecoleccion = tipoRecoleccion;
        View.inflate(context, R.layout.tab_manifiesto_detalle, this);

        init();
        loadData();
    }

    private void init() {
      /*  idSubRuta = Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_ruta").getValor());
        rowItems=MyApp.getDBO().manifiestoDao().fetchManifiestosAsigandobySubRuta(idSubRuta, MySession.getIdUsuario());*/


        mensajes = this.findViewById(R.id.fab);
        mensajes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogNotificacionDetalle dialogMensajes = new DialogNotificacionDetalle(getContext(), idAppManifiesto, identificacion, nombreCliente, sucursal, numeroManifiesto);
                dialogMensajes.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogMensajes.setCancelable(false);
                dialogMensajes.show();
            }
        });
        sectionRegistrarTara = this.findViewById(R.id.sectionRegistrarTara);
        chkRegistrarTara = this.findViewById(R.id.chkRegistrarTara);

        String tipoSubRuta = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta") == null ? "" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta");//1 ES INDUSTRIAL, 2 ES HOSPITALARIA
        if (tipoSubRuta.equals("2")) {//SI EL TIPO DE SUBRUTA ES HOSPITALARIA
            if (tipoPaquete == null) {
                sectionRegistrarTara.setVisibility(VISIBLE);
                final String checkTara = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("checkTara"+idAppManifiesto) == null ? "" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("checkTara"+idAppManifiesto);


                if (checkTara.equals("1")) {
                    chkRegistrarTara.setChecked(true);
                } else {
                    chkRegistrarTara.setChecked(false);
                }


                // VALIDACI[ON SI HAY PESOS CON TARA INGRESADOS BLOQUEAR CHECK GENERAL DE TARA {
                int contManifiestosPesos = 0;
                List<ManifiestoDetalleEntity> manifiestosDetalle = MyApp.getDBO().manifiestoDetalleDao().fecthConsultarManifiestoDetalleByIdManifiesto(idAppManifiesto);
                for (int i = 0; i < manifiestosDetalle.size(); i++) {
                    List<ManifiestoDetallePesosEntity> pesosDetalle = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarBultosManifiestoDet(manifiestosDetalle.get(i).getIdAppManifiestoDetalle());
                    for (int j = 0; j < pesosDetalle.size(); j++) {
                        if (pesosDetalle.get(j).getPesoTaraBulto() > 0) {
                            contManifiestosPesos++;
                        }
                    }
                }
                if (contManifiestosPesos > 0) {
                    chkRegistrarTara.setEnabled(false);
                } else {
                    chkRegistrarTara.setEnabled(true);
                }
                // VALIDACI[ON SI HAY PESOS CON TARA INGRESADOS BLOQUEAR CHECK GENERAL DE TARA }


                chkRegistrarTara.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (chkRegistrarTara.isChecked()) {
                            MyApp.getDBO().parametroDao().saveOrUpdate("checkTara" + idAppManifiesto, "1");
                            int contManifiestosPesos = 0;
                            List<ManifiestoDetalleEntity> manifiestosDetalle = MyApp.getDBO().manifiestoDetalleDao().fecthConsultarManifiestoDetalleByIdManifiesto(idAppManifiesto);
                            for (int i = 0; i < manifiestosDetalle.size(); i++) {
                                List<ManifiestoDetallePesosEntity> pesosDetalle = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarBultosManifiestoDet(manifiestosDetalle.get(i).getIdAppManifiestoDetalle());
                                for (int j = 0; j < pesosDetalle.size(); j++) {
                                    if (pesosDetalle.get(j).isImpresion() == true) {
                                        contManifiestosPesos++;
                                    }
                                }
                            }
                            if (contManifiestosPesos == 0) {
                                chkRegistrarTara.setChecked(true);
                                MyApp.getDBO().parametroDao().saveOrUpdate("checkTara" + idAppManifiesto, "1");
                            }
                        } else {
                            MyApp.getDBO().parametroDao().saveOrUpdate("checkTara" + idAppManifiesto, "2");

                            int contManifiestosConTara = 0;
                            List<ManifiestoDetalleEntity> manifiestosDetalle = MyApp.getDBO().manifiestoDetalleDao().fecthConsultarManifiestoDetalleByIdManifiesto(idAppManifiesto);
                            for (int i = 0; i < manifiestosDetalle.size(); i++) {
                                List<ManifiestoDetallePesosEntity> pesosDetalle = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarBultosManifiestoDet(manifiestosDetalle.get(i).getIdAppManifiestoDetalle());
                                for (int j = 0; j < pesosDetalle.size(); j++) {
                                    if (pesosDetalle.get(j).getPesoTaraBulto() != 0.0) {
                                        contManifiestosConTara++;
                                    }
                                }
                            }
                            if (contManifiestosConTara > 0) {
                                final DialogBuilder dialogBuilder2 = new DialogBuilder(getContext());
                                dialogBuilder2.setMessage("Ya existen bultos con peso de tara, se eliminarán los pesos de tara!");
                                dialogBuilder2.setTitle("CONFIRMACIÓN");
                                dialogBuilder2.setPositiveButton("SI", new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogBuilder2.dismiss();
                                        List<ManifiestoDetalleEntity> manifiestosDetalle = MyApp.getDBO().manifiestoDetalleDao().fecthConsultarManifiestoDetalleByIdManifiesto(idAppManifiesto);
                                        double pesoTotalSinTara = 0.0;
                                        for (int i = 0; i < manifiestosDetalle.size(); i++) {
                                            MyApp.getDBO().manifiestoDetallePesosDao().updatePesoTaraXManifiestoDetalle(idAppManifiesto, manifiestosDetalle.get(i).getIdAppManifiestoDetalle(), 0.0);
                                            List<ManifiestoDetallePesosEntity> pesosDetalle = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarBultosManifiestoDet(manifiestosDetalle.get(i).getIdAppManifiestoDetalle());
                                            for (int j = 0; j < pesosDetalle.size(); j++) {
                                                pesoTotalSinTara = pesoTotalSinTara + pesosDetalle.get(j).getValor();
                                            }
                                            MyApp.getDBO().manifiestoDetalleDao().updatePesoTotal(manifiestosDetalle.get(i).getIdAppManifiestoDetalle(), pesoTotalSinTara);
                                            pesoTotalSinTara = 0.0;
                                        }
                                        chkRegistrarTara.setChecked(false);
                                        MyApp.getDBO().parametroDao().saveOrUpdate("checkTara" + idAppManifiesto, "2");
                                        reloadData();
                                    }
                                });
                                dialogBuilder2.setNegativeButton("NO", new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogBuilder2.dismiss();
                                        chkRegistrarTara.setChecked(true);
                                        MyApp.getDBO().parametroDao().saveOrUpdate("checkTara" + idAppManifiesto, "1");
                                    }
                                });
                                dialogBuilder2.show();
                            } else {
                                chkRegistrarTara.setChecked(false);
                                MyApp.getDBO().parametroDao().saveOrUpdate("checkTara" + idAppManifiesto, "2");
                            }
                        }
                    }
                });
            } else {
                sectionRegistrarTara.setVisibility(GONE);
            }
        } else {
            sectionRegistrarTara.setVisibility(GONE);
        }


        calculoPaquetes = new MyCalculoPaquetes(idAppManifiesto, tipoPaquete);
        recyclerView = this.findViewById(R.id.recyclerManifiestoDetalle);
       /* novedadPesoPromedio = this.findViewById(R.id.sectionNovedadPesoPromedio);
        String tipoSubruta = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta") == null ? "" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta");
        if (tipoSubruta.equals("2")){
            novedadPesoPromedio.setVisibility(VISIBLE);
        }else {
            novedadPesoPromedio.setVisibility(GONE);
        }*/

        recyclerviewAdapter = new ManifiestoDetalleAdapter(getContext(), numeroManifiesto, estadoManifiesto, idAppManifiesto, tipoRecoleccion);

        //txtPesoPromedio = this.findViewById(R.id.txtPesoPromedio);
       /* lnlCountPhoto = this.findViewById(R.id.lnlCountPhoto);
        txtCountPhoto = this.findViewById(R.id.txtCountPhoto);
        int countFotos=MyApp.getDBO().manifiestoFileDao().obtenerCantidadFotografiabyManifiestoCatalogo(idAppManifiesto, 101, 19);
        if (countFotos>0){
            lnlCountPhoto.setVisibility(VISIBLE);
            txtCountPhoto.setText(countFotos+"");
        }else {
            lnlCountPhoto.setVisibility(GONE);
            txtCountPhoto.setText("");
        }*/

       /* btnEliminarFotos=this.findViewById(R.id.btnEliminarFotos);
        btnEliminarFotos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApp.getDBO().manifiestoFileDao().deleteFotoByIdAppManifistoCatalogo(idAppManifiesto,101);
                int countFotos=MyApp.getDBO().manifiestoFileDao().obtenerCantidadFotografiabyManifiestoCatalogo(idAppManifiesto, 101, 19);
                if (countFotos>0){
                    lnlCountPhoto.setVisibility(VISIBLE);
                    txtCountPhoto.setText(countFotos+"");
                }else {
                    lnlCountPhoto.setVisibility(GONE);
                    txtCountPhoto.setText("");
                }
            }
        });

        btnEvidenciaNovedadFrecuente = this.findViewById(R.id.btnEvidenciaNovedadFrecuente);
        btnEvidenciaNovedadFrecuente.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAgregarFotografias = new DialogAgregarFotografias(getContext(), idAppManifiesto, 101, ManifiestoFileDao.FOTO_NOVEDAD_PESO_PROMEDIO, MyConstant.STATUS_RECOLECCION);
                dialogAgregarFotografias.setCancelable(false);
                dialogAgregarFotografias.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogAgregarFotografias.setOnAgregarFotosListener(new DialogAgregarFotografias.OnAgregarFotosListener() {
                    @Override
                    public void onSuccessful(Integer cantidad) {
                           *//* if(dialogAgregarFotografias!=null && dialogAgregarFotografias.isShowing()){
                                dialogAgregarFotografias.dismiss();
                                dialogAgregarFotografias=null;
                            }*//*
                        lnlCountPhoto.setVisibility(View.VISIBLE);
                        txtCountPhoto.setText(String.valueOf(cantidad));
                    }
                });
                dialogAgregarFotografias.show();
                window = dialogAgregarFotografias.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }
        });*/

    }

/*    public void setMakePhoto(Integer code) {
        if (dialogAgregarFotografias != null) {
            dialogAgregarFotografias.setMakePhoto(code);
        }
    }*/

    @SuppressLint("RestrictedApi")
    public void loadData() {
        if (estadoManifiesto != 1) {
            mensajes.setVisibility(GONE);
            sectionRegistrarTara.setVisibility(GONE);
            sectionRegistrarTara.setEnabled(false);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        if (MyApp.getDBO().parametroDao().fecthParametroValor("auto_impresion" + MySession.getIdUsuario()).equals("1")) {
            MyApp.getDBO().manifiestoDetalleDao().updateFlagFaltaImpresionesByIdManifiesto(idAppManifiesto, false);
        }
        detalles = MyApp.getDBO().manifiestoDetalleDao().fetchHojaRutaDetallebyIdManifiesto(idAppManifiesto);

        recyclerviewAdapter.setTaskList(detalles);
        recyclerView.setAdapter(recyclerviewAdapter);

        recyclerviewAdapter.setOnItemClickListener(new ManifiestoDetalleAdapter.ClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onItemClick(int position, View v) {

                int x = 0;
                if (estadoManifiesto == 1) {
                    if (tipoRecoleccion == 1) {
                        String tipoBalanza = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoBalanza"+idAppManifiesto)==null?"0":MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoBalanza"+idAppManifiesto);
                        Integer manifiestosConPesos=contManifiestosConPesos();
                        if  (tipoBalanza.equals("gadere")&&manifiestosConPesos>0){
                            selectBalanzaGadere(position, detalles.get(position).getId());
                        }else if(tipoBalanza.equals("cliente")&&manifiestosConPesos>0){
                            selectBalanzaCliente(position, detalles.get(position).getId());
                        }else {
                            openOpcionesItems(position, detalles.get(position).getId());
                        }
                    }
                    if (tipoRecoleccion == 2) {
                        final DialogBultosNo dialogBultosNo = new DialogBultosNo(getContext(), idAppManifiesto, detalles.get(position).getId());
                        dialogBultosNo.setCancelable(false);
                        dialogBultosNo.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialogBultosNo.setmOnRegistrarBultoListener(new DialogBultosNo.OnRegistrarBultoListener() {
                            @Override
                            public void onSuccesfull(String numeroBultos, Integer idDetalle) {
                                MyApp.getDBO().manifiestoDetallePesosDao().deleteTableValores(idAppManifiesto, idDetalle);
                                if (numeroBultos.equals("") || numeroBultos.equals("0")) {
                                    final DialogBuilder dialogBuilder2 = new DialogBuilder(getContext());
                                    dialogBuilder2.setMessage("Ingrese un número mayor a cero");
                                    dialogBuilder2.setCancelable(false);
                                    dialogBuilder2.setTitle("CONFIRMACIÓN");
                                    dialogBuilder2.setPositiveButton("OK", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogBuilder2.dismiss();
                                        }
                                    });
                                    //MyApp.getDBO().manifiestoDetalleDao().updateCantidadBultoManifiestoDetalle(idDetalle,0.0,0,0,false);
                                    dialogBuilder2.show();
                                } else {
                                    MyApp.getDBO().manifiestoDetalleDao().updateCantidadBultoManifiestoDetalle(idDetalle, numeroBultos.equals("") ? 0.0 : Double.parseDouble(numeroBultos), 0, 0, true);
                                    for (int i = 1; i <= Integer.parseInt(numeroBultos); i++) {
                                        MyApp.getDBO().manifiestoDetallePesosDao().saveValores(idAppManifiesto, idDetalle, 0.0, "", null, "", false, i, 0.0);
                                    }
                                    //detalles.clear();
                                    reloadData();
                                    dialogBultosNo.dismiss();
                                    if (!MyApp.getDBO().parametroDao().fecthParametroValor("auto_impresion" + MySession.getIdUsuario()).equals("1")) {
                                        imprimirEtiquetaIndividual(idAppManifiesto, idDetalle, Integer.parseInt(numeroBultos));
                                    }
                                }
                            }
                        });
                        dialogBultosNo.setmOnCancelarBultoListener(new DialogBultosNo.OnCancelarBultoListener() {
                            @Override
                            public void onSuccesfull() {
                                dialogBultosNo.dismiss();
                            }
                        });
                        dialogBultosNo.show();


                       /* DialogAgregarBultos dialogAgregarBultos=new DialogAgregarBultos(getContext(),detalles.get(position).getId(),idAppManifiesto);
                        dialogAgregarBultos.show();*/
                    }
                }
            }
        });

      /*  String tipoSubruta = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta") == null ? "" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta");
        if (tipoSubruta.equals("2")){
            double pesoPromedio = MyApp.getDBO().manifiestoDao().selectPesoPromediobyIdManifiesto(idAppManifiesto);
            double pesoTotal = 0.0;
            for (int i = 0; i < detalles.size(); i++) {
                itemManifiestoDetalleBultos = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarBultosManifiestoDet(detalles.get(i).getId());
                for (int j = 0; j < itemManifiestoDetalleBultos.size(); j++) {
                    pesoTotal += itemManifiestoDetalleBultos.get(j).getValor();
                }
            }
            if (pesoTotal > (pesoPromedio + (pesoPromedio * 0.20)) || pesoTotal < (pesoPromedio - (pesoPromedio * 0.20))) {
                txtPesoPromedio.setText("EXISTE DIFERENCIA DE " + (pesoPromedio - pesoTotal) + " KG DEL PESO PROMEDIO");
                MyApp.getDBO().parametroDao().saveOrUpdate("textoPesoPromedio",""+txtPesoPromedio.getText());
                if (pesoTotal == 0.0) {
                    novedadPesoPromedio.setVisibility(GONE);
                    txtPesoPromedio.setText("");
                } else {
                    novedadPesoPromedio.setVisibility(VISIBLE);
                }
            } else {
                novedadPesoPromedio.setVisibility(GONE);
                txtPesoPromedio.setText("");
            }
        }else {
            novedadPesoPromedio.setVisibility(GONE);
        }*/


    }

    public void reloadData() {

        if (MyApp.getDBO().parametroDao().fecthParametroValor("auto_impresion" + MySession.getIdUsuario()).equals("1")) {
            MyApp.getDBO().manifiestoDetalleDao().updateFlagFaltaImpresionesByIdManifiesto(idAppManifiesto, false);
            detalles = MyApp.getDBO().manifiestoDetalleDao().fetchHojaRutaDetallebyIdManifiesto(idAppManifiesto);
        } else {
            detalles = MyApp.getDBO().manifiestoDetalleDao().fetchHojaRutaDetallebyIdManifiesto(idAppManifiesto);
        }
        recyclerviewAdapter.setTaskList(detalles);
        recyclerviewAdapter.notifyDataSetChanged();
    }

    public Integer contManifiestosConPesos(){
        MyApp.getDBO().parametroDao().saveOrUpdate("tipoBalanza"+idAppManifiesto, "gadere");
        int contManifiestos = 0;
        List<ManifiestoDetalleEntity> manifiestosDetalle = MyApp.getDBO().manifiestoDetalleDao().fecthConsultarManifiestoDetalleByIdManifiesto(idAppManifiesto);
        for (int i=0;i<manifiestosDetalle.size();i++){
            int idManifiestoDetalle = manifiestosDetalle.get(i).getIdAppManifiestoDetalle();
            List<ManifiestoDetallePesosEntity> pesosDetalle = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarBultosManifiestoDet(idManifiestoDetalle);
            for (int j = 0; j < pesosDetalle.size(); j++) {
                if (pesosDetalle.get(j).getValor() > 0.0) {
                    contManifiestos++;
                }
            }
        }

        return contManifiestos;
    }

    private void openOpcionesItems(final Integer positionItem, final Integer idDetManifiesto) {

        dialogOpcioneItem = new Dialog(this.getContext());
       /* final ArrayList<MenuItem> myListOfItems = new ArrayList<>();
        myListOfItems.add(new MenuItem("BULTOS"));
        myListOfItems.add(new MenuItem("PAQUETES"));*/

        //dialogMenuBaseAdapter = new DialogMenuBaseAdapter(this.getContext(),myListOfItems);
        View view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.dialog_opciones_detalle, null);
        //LinearLayout txtBultos = view.findViewById(R.id.txtBultos);

        LinearLayout btnCancelar = view.findViewById(R.id.btnCancel);
        LinearLayout btnBalanzaGadere = (LinearLayout) view.findViewById(R.id.btnBalanzaGadere);
        LinearLayout btnBalanzaCliente = (LinearLayout) view.findViewById(R.id.btnBalanzaCliente);
        /*
        txtBultos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //openDialogBultos(positionItem,0);
                showTipoPaquete(positionItem);
            }
        });
         */

        btnBalanzaGadere.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogOpcioneItem.dismiss();
                selectBalanzaGadere(positionItem,idDetManifiesto);
            }
        });
        btnBalanzaCliente.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogOpcioneItem.dismiss();
               selectBalanzaCliente(positionItem,idDetManifiesto);
            }
        });


        btnCancelar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogOpcioneItem.dismiss();
            }
        });

        dialogOpcioneItem.setTitle("OPCIONES");
        dialogOpcioneItem.setContentView(view);
        dialogOpcioneItem.setCancelable(false);
        dialogOpcioneItem.show();
    }

    public void selectBalanzaGadere(final Integer positionItem, final Integer idDetManifiesto){
        MyApp.getDBO().parametroDao().saveOrUpdate("tipoBalanza"+idAppManifiesto, "gadere");
        int contManifiestosConTara = 0;
        List<ManifiestoDetalleEntity> manifiestosDetalle = MyApp.getDBO().manifiestoDetalleDao().fecthConsultarManifiestoDetalleByIdManifiesto(idAppManifiesto);
        int idManifiestoDetalle = manifiestosDetalle.get(positionItem).getIdAppManifiestoDetalle();
        List<ManifiestoDetallePesosEntity> pesosDetalle = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarBultosManifiestoDet(idManifiestoDetalle);
        for (int j = 0; j < pesosDetalle.size(); j++) {
            if (pesosDetalle.get(j).getPesoTaraBulto() > 0.0) {
                contManifiestosConTara++;
            }
        }
        String checkTaraGeneral = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("checkTara"+idAppManifiesto) == null ? "" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("checkTara"+idAppManifiesto);
        if (checkTaraGeneral.equals("1")) {
            if (contManifiestosConTara > 0) {
                MyApp.getDBO().manifiestoDetalleDao().updateTipoBalanzaByDetalleId(idAppManifiesto, idDetManifiesto, 1);
                tipoBalanza = 1;
                if (detalles.get(positionItem).getTipoPaquete() == null || detalles.get(positionItem).getTipoPaquete().equals(0)) {// NORMAL
                    openDialogBultos(positionItem, 0, 1);//Registra tara 1 = SI ; 2 = NO
                } else if (detalles.get(positionItem).getTipoPaquete() == 1) {//INFECCIOSO
                    openDialogBultos(positionItem, 100, 1);//Registra tara 1 = SI ; 2 = NO
                } else if (detalles.get(positionItem).getTipoPaquete() == 2) {// CORTOPUNZANTE
                    openDialogBultos(positionItem, 101, 1);//Registra tara 1 = SI ; 2 = NO
                } else if (detalles.get(positionItem).getTipoPaquete() == 3) {// SELECCIONA SI ES CORTOPUNZANTE O INFECCIOSO
                    showTipoPaquete(positionItem, 1);//Registra tara 1 = SI ; 2 = NO
                }
            } else {
                final DialogBuilder dialogBuilder = new DialogBuilder(getContext());
                dialogBuilder.setMessage("¿Registrar Tara?");
                dialogBuilder.setCancelable(false);
                dialogBuilder.setTitle("CONFIRMACIÓN");
                dialogBuilder.setPositiveButton("SI", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                        MyApp.getDBO().manifiestoDetalleDao().updateTipoBalanzaByDetalleId(idAppManifiesto, idDetManifiesto, 1);
                        tipoBalanza = 1;
                        if (detalles.get(positionItem).getTipoPaquete() == null || detalles.get(positionItem).getTipoPaquete().equals(0)) {// NORMAL
                            openDialogBultos(positionItem, 0, 1);//Registra tara 1 = SI ; 2 = NO
                        } else if (detalles.get(positionItem).getTipoPaquete() == 1) {//INFECCIOSO
                            openDialogBultos(positionItem, 100, 1);//Registra tara 1 = SI ; 2 = NO
                        } else if (detalles.get(positionItem).getTipoPaquete() == 2) {// CORTOPUNZANTE
                            openDialogBultos(positionItem, 101, 1);//Registra tara 1 = SI ; 2 = NO
                        } else if (detalles.get(positionItem).getTipoPaquete() == 3) {// SELECCIONA SI ES CORTOPUNZANTE O INFECCIOSO
                            showTipoPaquete(positionItem, 1);//Registra tara 1 = SI ; 2 = NO
                        }
                    }
                });
                dialogBuilder.setNegativeButton("NO", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                        MyApp.getDBO().manifiestoDetalleDao().updateTipoBalanzaByDetalleId(idAppManifiesto, idDetManifiesto, 1);
                        tipoBalanza = 1;
                        if (detalles.get(positionItem).getTipoPaquete() == null || detalles.get(positionItem).getTipoPaquete().equals(0)) {// NORMAL
                            openDialogBultos(positionItem, 0, 2);//Registra tara 1 = SI ; 2 = NO
                        } else if (detalles.get(positionItem).getTipoPaquete() == 1) {//INFECCIOSO
                            openDialogBultos(positionItem, 100, 2);//Registra tara 1 = SI ; 2 = NO
                        } else if (detalles.get(positionItem).getTipoPaquete() == 2) {// CORTOPUNZANTE
                            openDialogBultos(positionItem, 101, 2);//Registra tara 1 = SI ; 2 = NO
                        } else if (detalles.get(positionItem).getTipoPaquete() == 3) {// SELECCIONA SI ES CORTOPUNZANTE O INFECCIOSO
                            showTipoPaquete(positionItem, 2);//Registra tara 1 = SI ; 2 = NO
                        }
                    }
                });
                dialogBuilder.show();
            }
        } else {
            MyApp.getDBO().manifiestoDetalleDao().updateTipoBalanzaByDetalleId(idAppManifiesto, idDetManifiesto, 1);
            tipoBalanza = 1;
            if (detalles.get(positionItem).getTipoPaquete() == null || detalles.get(positionItem).getTipoPaquete().equals(0)) {// NORMAL
                openDialogBultos(positionItem, 0, 2);//Registra tara 1 = SI ; 2 = NO
            } else if (detalles.get(positionItem).getTipoPaquete() == 1) {//INFECCIOSO
                openDialogBultos(positionItem, 100, 2);//Registra tara 1 = SI ; 2 = NO
            } else if (detalles.get(positionItem).getTipoPaquete() == 2) {// CORTOPUNZANTE
                openDialogBultos(positionItem, 101, 2);//Registra tara 1 = SI ; 2 = NO
            } else if (detalles.get(positionItem).getTipoPaquete() == 3) {// SELECCIONA SI ES CORTOPUNZANTE O INFECCIOSO
                showTipoPaquete(positionItem, 2);//Registra tara 1 = SI ; 2 = NO
            }
        }
    }

    public void selectBalanzaCliente(final Integer positionItem, final Integer idDetManifiesto){
        MyApp.getDBO().parametroDao().saveOrUpdate("tipoBalanza"+idAppManifiesto, "cliente");
        int contManifiestosConTara = 0;
        List<ManifiestoDetalleEntity> manifiestosDetalle = MyApp.getDBO().manifiestoDetalleDao().fecthConsultarManifiestoDetalleByIdManifiesto(idAppManifiesto);
        List<ManifiestoDetallePesosEntity> pesosDetalle = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarBultosManifiestoDet(manifiestosDetalle.get(positionItem).getIdAppManifiestoDetalle());
        for (int j = 0; j < pesosDetalle.size(); j++) {
            if (pesosDetalle.get(j).getPesoTaraBulto() > 0.0) {
                contManifiestosConTara++;
            }
        }
        String checkTaraGeneral = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("checkTara"+idAppManifiesto) == null ? "" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("checkTara"+idAppManifiesto);

        if (checkTaraGeneral.equals("1")) {
            if (contManifiestosConTara > 0) {
                MyApp.getDBO().manifiestoDetalleDao().updateTipoBalanzaByDetalleId(idAppManifiesto, idDetManifiesto, 2);
                tipoBalanza = 2;
                //List<ManifiestoDetalleEntity> lista =  MyApp.getDBO().manifiestoDetalleDao().fecthConsultarManifiestoDetallebyID(idAppManifiesto);
                if (detalles.get(positionItem).getTipoPaquete() == null || detalles.get(positionItem).getTipoPaquete().equals(0)) {// NORMAL
                    openDialogBultos(positionItem, 0, 1);//Registra tara 1 = SI ; 2 = NO
                } else if (detalles.get(positionItem).getTipoPaquete() == 1) {//INFECCIOSO
                    openDialogBultos(positionItem, 100, 1);//Registra tara 1 = SI ; 2 = NO
                } else if (detalles.get(positionItem).getTipoPaquete() == 2) {// CORTOPUNZANTE
                    openDialogBultos(positionItem, 101, 1);//Registra tara 1 = SI ; 2 = NO
                } else if (detalles.get(positionItem).getTipoPaquete() == 3) {// SELECCIONA SI ES CORTOPUNZANTE O INFECCIOSO
                    showTipoPaquete(positionItem, 1);//Registra tara 1 = SI ; 2 = NO
                }
            } else {
                final DialogBuilder dialogBuilder = new DialogBuilder(getContext());
                dialogBuilder.setMessage("¿Registrar Tara?");
                dialogBuilder.setCancelable(false);
                dialogBuilder.setTitle("CONFIRMACIÓN");
                dialogBuilder.setPositiveButton("SI", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                        MyApp.getDBO().manifiestoDetalleDao().updateTipoBalanzaByDetalleId(idAppManifiesto, idDetManifiesto, 2);
                        tipoBalanza = 2;
                        //List<ManifiestoDetalleEntity> lista =  MyApp.getDBO().manifiestoDetalleDao().fecthConsultarManifiestoDetallebyID(idAppManifiesto);
                        if (detalles.get(positionItem).getTipoPaquete() == null || detalles.get(positionItem).getTipoPaquete().equals(0)) {// NORMAL
                            openDialogBultos(positionItem, 0, 1);//Registra tara 1 = SI ; 2 = NO
                        } else if (detalles.get(positionItem).getTipoPaquete() == 1) {//INFECCIOSO
                            openDialogBultos(positionItem, 100, 1);//Registra tara 1 = SI ; 2 = NO
                        } else if (detalles.get(positionItem).getTipoPaquete() == 2) {// CORTOPUNZANTE
                            openDialogBultos(positionItem, 101, 1);//Registra tara 1 = SI ; 2 = NO
                        } else if (detalles.get(positionItem).getTipoPaquete() == 3) {// SELECCIONA SI ES CORTOPUNZANTE O INFECCIOSO
                            showTipoPaquete(positionItem, 1);//Registra tara 1 = SI ; 2 = NO
                        }
                    }
                });
                dialogBuilder.setNegativeButton("NO", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                        MyApp.getDBO().manifiestoDetalleDao().updateTipoBalanzaByDetalleId(idAppManifiesto, idDetManifiesto, 2);
                        tipoBalanza = 2;
                        //List<ManifiestoDetalleEntity> lista =  MyApp.getDBO().manifiestoDetalleDao().fecthConsultarManifiestoDetallebyID(idAppManifiesto);
                        if (detalles.get(positionItem).getTipoPaquete() == null || detalles.get(positionItem).getTipoPaquete().equals(0)) {// NORMAL
                            openDialogBultos(positionItem, 0, 2);//Registra tara 1 = SI ; 2 = NO
                        } else if (detalles.get(positionItem).getTipoPaquete() == 1) {//INFECCIOSO
                            openDialogBultos(positionItem, 100, 2);//Registra tara 1 = SI ; 2 = NO
                        } else if (detalles.get(positionItem).getTipoPaquete() == 2) {// CORTOPUNZANTE
                            openDialogBultos(positionItem, 101, 2);//Registra tara 1 = SI ; 2 = NO
                        } else if (detalles.get(positionItem).getTipoPaquete() == 3) {// SELECCIONA SI ES CORTOPUNZANTE O INFECCIOSO
                            showTipoPaquete(positionItem, 2);//Registra tara 1 = SI ; 2 = NO
                        }
                    }
                });
                dialogBuilder.show();
            }
        } else{
            tipoBalanza = 2;
            //List<ManifiestoDetalleEntity> lista =  MyApp.getDBO().manifiestoDetalleDao().fecthConsultarManifiestoDetallebyID(idAppManifiesto);
            if (detalles.get(positionItem).getTipoPaquete() == null || detalles.get(positionItem).getTipoPaquete().equals(0)) {// NORMAL
                openDialogBultos(positionItem, 0, 2);//Registra tara 1 = SI ; 2 = NO
            } else if (detalles.get(positionItem).getTipoPaquete() == 1) {//INFECCIOSO
                openDialogBultos(positionItem, 100, 2);//Registra tara 1 = SI ; 2 = NO
            } else if (detalles.get(positionItem).getTipoPaquete() == 2) {// CORTOPUNZANTE
                openDialogBultos(positionItem, 101, 2);//Registra tara 1 = SI ; 2 = NO
            } else if (detalles.get(positionItem).getTipoPaquete() == 3) {// SELECCIONA SI ES CORTOPUNZANTE O INFECCIOSO
                showTipoPaquete(positionItem, 2);//Registra tara 1 = SI ; 2 = NO
            }
        }
    }

    private void showTipoPaquete(final Integer positionItem, final Integer registraTara) {

        itemsCategoriaPaquete = new ArrayList<>();
        if (tipoPaquete != null) {
            pkg = MyApp.getDBO().paqueteDao().fechConsultaPaqueteEspecifico(tipoPaquete);

            if (pkg.getEntregaSoloFundas())
                itemsCategoriaPaquete.add(ManifiestoPaqueteDao.ARG_INFECCIOSO);
            if (pkg.getEntregaSoloGuardianes())
                itemsCategoriaPaquete.add(ManifiestoPaqueteDao.ARG_CORTOPUNZANTE);

            alertDialog = new AlertDialog.Builder(getContext());
            alertDialog.setTitle("TIPO  PAQUETE");
            alertDialog.setSingleChoiceItems(itemsCategoriaPaquete.toArray(new String[itemsCategoriaPaquete.size()]), -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            tipoGestion = 100;// infeccioso
                            openDialogBultos(positionItem, tipoGestion, registraTara);
                            alert.dismiss();
                            break;
                        case 1:
                            tipoGestion = 101;//cortopunzante
                            openDialogBultos(positionItem, tipoGestion, registraTara);
                            alert.dismiss();
                            break;
                        case 2:
                            tipoGestion = 102;
                            openDialogBultos(positionItem, tipoGestion, registraTara);
                            alert.dismiss();
                            break;
                    }
                }
            });
            alert = alertDialog.create();
            alert.setCanceledOnTouchOutside(false);
            //alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alert.show();

        } else {
            openDialogBultos(positionItem, 0, registraTara);
        }
    }

    private void openDialogBultos(Integer position, final Integer tipoGestion, final Integer registraTara) {

        if (dialogBultos == null) {
            //dialogOpcioneItem.dismiss(); SE CAE LA APP
            dialogBultos = new DialogBultos(
                    getContext(),
                    position,
                    idAppManifiesto,
                    detalles.get(position).getId(),
                    tipoPaquete,
                    numeroManifiesto + "$" + detalles.get(position).getCodigo(),
                    tipoGestion,
                    registraTara
            );
            dialogBultos.setCancelable(false);
            dialogBultos.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogBultos.setOnBultoListener(new DialogBultos.OnBultoListener() {
                @Override
                public void onSuccessful(BigDecimal valor, int position, int cantidad, PaqueteEntity pkg, boolean isClose, boolean faltaImpresiones, boolean isChangeTotalBultos) {
                    if (isClose && dialogBultos != null) {
                        dialogBultos.dismiss();
                        dialogBultos = null;
                    }

                    RowItemManifiesto row = detalles.get(position);

                    List<ManifiestoDetallePesosEntity> listaPesos = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarBultosManifiestoDet(row.getId());
                    double totalPesoTaraManifiestoDetalle = 0.0;
                    for (int i = 0; i < listaPesos.size(); i++) {
                        totalPesoTaraManifiestoDetalle = totalPesoTaraManifiestoDetalle + listaPesos.get(i).getPesoTaraBulto();
                    }
                    double pesoTotalMenosTara = (valor.doubleValue()) - totalPesoTaraManifiestoDetalle;
                    double pesoTotalMostrar = Double.parseDouble(obtieneDosDecimales(pesoTotalMenosTara));
                    row.setPeso(pesoTotalMostrar);
                    row.setTipoBalanza(tipoBalanza);


                    if (row.getTipoItem() == 1) row.setCantidadBulto(cantidad); //unidad
                    else if (row.getTipoItem() == 2) row.setCantidadBulto(1); //servicio
                    else if (row.getTipoItem() == 3)
                        row.setCantidadBulto(Double.valueOf(cantidad)); //otros cantida = peso...
                    //else if(row.getTipoItem()==3) row.setCantidadBulto(row.getPeso()); //otros cantida = peso...

                    if (cantidad == 0)
                        row.setEstado(false);
                    else
                        row.setEstado(true);
                    row.setFaltaImpresiones(faltaImpresiones);
                    recyclerviewAdapter.notifyDataSetChanged();
                    //actualizar datos en dbo local...
                    MyApp.getDBO().manifiestoDetalleDao().updateCantidadBultoManifiestoDetalle(row.getId(), row.getCantidadBulto(), row.getPeso(), cantidad, row.isEstado());


                    // VALIDACI[ON SI HAY PESOS CON TARA INGRESADOS BLOQUEAR CHECK GENERAL DE TARA {
                    int contManifiestosPesos = 0;
                    List<ManifiestoDetalleEntity> manifiestosDetalle = MyApp.getDBO().manifiestoDetalleDao().fecthConsultarManifiestoDetalleByIdManifiesto(idAppManifiesto);
                    for (int i = 0; i < manifiestosDetalle.size(); i++) {
                        List<ManifiestoDetallePesosEntity> pesosDetalle = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarBultosManifiestoDet(manifiestosDetalle.get(i).getIdAppManifiestoDetalle());
                        for (int j = 0; j < pesosDetalle.size(); j++) {
                            if (pesosDetalle.get(j).getPesoTaraBulto() > 0) {
                                contManifiestosPesos++;
                            }
                        }
                    }
                    if (contManifiestosPesos > 0) {
                        chkRegistrarTara.setEnabled(false);
                    } else {
                        chkRegistrarTara.setEnabled(true);
                    }
                    // VALIDACI[ON SI HAY PESOS CON TARA INGRESADOS BLOQUEAR CHECK GENERAL DE TARA }

                /*    String tipoSubruta = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta") == null ? "" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta");
                    if (tipoSubruta.equals("2")){
                        double pesoPromedio = MyApp.getDBO().manifiestoDao().selectPesoPromediobyIdManifiesto(idAppManifiesto);
                        double pesoTotal = 0.0;
                        for (int i = 0; i < detalles.size(); i++) {
                            itemManifiestoDetalleBultos = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarBultosManifiestoDet(detalles.get(i).getId());
                            for (int j = 0; j < itemManifiestoDetalleBultos.size(); j++) {
                                pesoTotal += itemManifiestoDetalleBultos.get(j).getValor();
                            }
                        }
                        if (pesoTotal > (pesoPromedio + (pesoPromedio * 0.20)) || pesoTotal < (pesoPromedio - (pesoPromedio * 0.20))) {
                            txtPesoPromedio.setText("EXISTE DIFERENCIA DE " + (pesoPromedio - pesoTotal) + " KG DEL PESO PROMEDIO");
                            MyApp.getDBO().parametroDao().saveOrUpdate("textoPesoPromedio",""+txtPesoPromedio.getText());
                            if (pesoTotal == 0.0) {
                                novedadPesoPromedio.setVisibility(GONE);
                                txtPesoPromedio.setText("");
                            } else {
                                novedadPesoPromedio.setVisibility(VISIBLE);
                            }
                        } else {
                            novedadPesoPromedio.setVisibility(GONE);
                            txtPesoPromedio.setText("");
                        }
                    }else {
                        novedadPesoPromedio.setVisibility(GONE);
                    }*/

                    //si cambia los item(add/remove) de la calculadora se resetea los valores pendientes ingresados por el usuario...
                    isChangeTotalCreateBultos = isChangeTotalBultos;
                    //calculo de paquetes...
                    if (pkg != null) {
                        calculoPaquetes.algoritmo(pkg);
                    }
                }

                @Override
                public void onCanceled(boolean faltaImpresos, int position) {
                    if (dialogBultos != null) {
                        if (!faltaImpresos) {
                            //detalles.clear();
                            //reloadData();
                            RowItemManifiesto row = detalles.get(position);
                            List<ManifiestoDetallePesosEntity> listaPesos = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarBultosManifiestoDet(row.getId());

                            double totalPesoTaraManifiestoDetalle = 0.0;
                            double totalPesos=0.0;
                            for (int i = 0; i < listaPesos.size(); i++) {
                                totalPesoTaraManifiestoDetalle = totalPesoTaraManifiestoDetalle + listaPesos.get(i).getPesoTaraBulto();
                                totalPesos=totalPesos+listaPesos.get(i).getValor();
                            }
                            double pesoTotalMenosTara = totalPesos - totalPesoTaraManifiestoDetalle;
                            double pesoTotalMostrar = Double.parseDouble(obtieneDosDecimales(pesoTotalMenosTara));

                            row.setPeso(pesoTotalMostrar);
                            row.setCantidadBulto(Double.valueOf(listaPesos.size()));
                            if (listaPesos.size()==0){
                                row.setEstado(false);
                            }else {
                                row.setEstado(true);
                            }



                            MyApp.getDBO().manifiestoDetalleDao().updateCantidadBultoManifiestoDetalle(row.getId(), listaPesos.size(), pesoTotalMostrar, listaPesos.size(), row.isEstado());
                            detalles = MyApp.getDBO().manifiestoDetalleDao().fetchHojaRutaDetallebyIdManifiesto(idAppManifiesto);
                            recyclerviewAdapter.setTaskList(detalles);
                            recyclerviewAdapter.notifyDataSetChanged();
                            /*recyclerView.removeAllViews();
                            detalles = MyApp.getDBO().manifiestoDetalleDao().fetchHojaRutaDetallebyIdManifiesto(idAppManifiesto);
                            recyclerviewAdapter.setTaskList(detalles);
                            recyclerView.setAdapter(recyclerviewAdapter);*/


                            // VALIDACI[ON SI HAY PESOS CON TARA INGRESADOS BLOQUEAR CHECK GENERAL DE TARA {
                            int contManifiestosPesos = 0;
                            List<ManifiestoDetalleEntity> manifiestosDetalle = MyApp.getDBO().manifiestoDetalleDao().fecthConsultarManifiestoDetalleByIdManifiesto(idAppManifiesto);
                            for (int i = 0; i < manifiestosDetalle.size(); i++) {
                                List<ManifiestoDetallePesosEntity> pesosDetalle = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarBultosManifiestoDet(manifiestosDetalle.get(i).getIdAppManifiestoDetalle());
                                for (int j = 0; j < pesosDetalle.size(); j++) {
                                    if (pesosDetalle.get(j).getPesoTaraBulto() > 0) {
                                        contManifiestosPesos++;
                                    }
                                }
                            }
                            if (contManifiestosPesos > 0) {
                                chkRegistrarTara.setEnabled(false);
                            } else {
                                chkRegistrarTara.setEnabled(true);
                            }

                            // VALIDACI[ON SI HAY PESOS CON TARA INGRESADOS BLOQUEAR CHECK GENERAL DE TARA }


                      /*      String tipoSubruta = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta") == null ? "" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta");
                            if (tipoSubruta.equals("2")){
                                double pesoPromedio = MyApp.getDBO().manifiestoDao().selectPesoPromediobyIdManifiesto(idAppManifiesto);
                                double pesoTotal = 0.0;
                                for (int i = 0; i < detalles.size(); i++) {
                                    itemManifiestoDetalleBultos = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarBultosManifiestoDet(detalles.get(i).getId());
                                    for (int j = 0; j < itemManifiestoDetalleBultos.size(); j++) {
                                        pesoTotal += itemManifiestoDetalleBultos.get(j).getValor();
                                    }
                                }
                                if (pesoTotal > (pesoPromedio + (pesoPromedio * 0.20)) || pesoTotal < (pesoPromedio - (pesoPromedio * 0.20))) {
                                    txtPesoPromedio.setText("EXISTE DIFERENCIA DE " + (pesoPromedio - pesoTotal) + " KG DEL PESO PROMEDIO");
                                    MyApp.getDBO().parametroDao().saveOrUpdate("textoPesoPromedio",""+txtPesoPromedio.getText());
                                    if (pesoTotal == 0.0) {
                                        novedadPesoPromedio.setVisibility(GONE);
                                        txtPesoPromedio.setText("");
                                    } else {
                                        novedadPesoPromedio.setVisibility(VISIBLE);
                                    }
                                } else {
                                    novedadPesoPromedio.setVisibility(GONE);
                                    txtPesoPromedio.setText("");
                                }
                            }else {
                                novedadPesoPromedio.setVisibility(GONE);
                            }
*/
                        }else {
                            RowItemManifiesto row = detalles.get(position);
                            List<ManifiestoDetallePesosEntity> listaPesos = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarBultosManifiestoDet(row.getId());

                            double totalPesoTaraManifiestoDetalle = 0.0;
                            double totalPesos=0.0;
                            for (int i = 0; i < listaPesos.size(); i++) {
                                totalPesoTaraManifiestoDetalle = totalPesoTaraManifiestoDetalle + listaPesos.get(i).getPesoTaraBulto();
                                totalPesos=totalPesos+listaPesos.get(i).getValor();
                            }
                            double pesoTotalMenosTara = totalPesos - totalPesoTaraManifiestoDetalle;
                            double pesoTotalMostrar = Double.parseDouble(obtieneDosDecimales(pesoTotalMenosTara));

                            row.setPeso(pesoTotalMostrar);
                            row.setCantidadBulto(Double.valueOf(listaPesos.size()));
                            if (listaPesos.size()==0){
                                row.setEstado(false);
                            }else {
                                row.setEstado(true);
                            }


                       /*     recyclerviewAdapter.notifyDataSetChanged();*/
                            MyApp.getDBO().manifiestoDetalleDao().updateCantidadBultoManifiestoDetalle(row.getId(), listaPesos.size(), pesoTotalMostrar, listaPesos.size(), row.isEstado());
                            detalles = MyApp.getDBO().manifiestoDetalleDao().fetchHojaRutaDetallebyIdManifiesto(idAppManifiesto);
                            recyclerviewAdapter.setTaskList(detalles);
                            recyclerviewAdapter.notifyDataSetChanged();

                            /*recyclerView.removeAllViews();
                            detalles = MyApp.getDBO().manifiestoDetalleDao().fetchHojaRutaDetallebyIdManifiesto(idAppManifiesto);
                            recyclerviewAdapter.setTaskList(detalles);
                            recyclerView.setAdapter(recyclerviewAdapter);*/


                            // VALIDACI[ON SI HAY PESOS CON TARA INGRESADOS BLOQUEAR CHECK GENERAL DE TARA {
                            int contManifiestosPesos = 0;
                            List<ManifiestoDetalleEntity> manifiestosDetalle = MyApp.getDBO().manifiestoDetalleDao().fecthConsultarManifiestoDetalleByIdManifiesto(idAppManifiesto);
                            for (int i = 0; i < manifiestosDetalle.size(); i++) {
                                List<ManifiestoDetallePesosEntity> pesosDetalle = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarBultosManifiestoDet(manifiestosDetalle.get(i).getIdAppManifiestoDetalle());
                                for (int j = 0; j < pesosDetalle.size(); j++) {
                                    if (pesosDetalle.get(j).getPesoTaraBulto() > 0) {
                                        contManifiestosPesos++;
                                    }
                                }
                            }
                            if (contManifiestosPesos > 0) {
                                chkRegistrarTara.setEnabled(false);
                            } else {
                                chkRegistrarTara.setEnabled(true);
                            }

                        }
                        dialogBultos.dismiss();
                        dialogBultos = null;
                    }
                }
            });
            dialogBultos.show();

            window = dialogBultos.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        }
    }

    public boolean validaExisteDetallesSeleccionados() {
        return MyApp.getDBO().manifiestoDetalleDao().fecthConsultarManifiestoDetalleSeleccionados(idAppManifiesto).size() > 0;
    }

   /* public boolean validaPesoReferencial() {
        if (novedadPesoPromedio.getVisibility() == VISIBLE) {
            int cantidadFotos = MyApp.getDBO().manifiestoFileDao().obtenerCantidadFotografiabyManifiestoCatalogo(idAppManifiesto, 101, 19);
            if (cantidadFotos == 0) {
                return true;//Debe ingresar fotos
            } else {
                return false;//Ya ingresó fotos
            }
        } else {
            return false;//No debe ingresar fotos
        }
    }*/

    public boolean validaExisteCambioTotalBultos() {
        return isChangeTotalCreateBultos;
    }

    public void resetParameters() {
        isChangeTotalCreateBultos = false;
    }

    private String obtieneDosDecimales(double valor) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(valor);
    }

    private void imprimirEtiquetaIndividual(final Integer idAppManifiesto, final Integer idManifiestoDetalle, final Integer totalEtiquetas) {

        final Handler handler=new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                final ProgressDialog progress = ProgressDialog.show(getContext(), "", "Imprimiendo...", true);
                progress.setCancelable(false);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            dialog = new ProgressDialog(getContext());
                            print = new MyPrint((Activity) getContext());
                            print.setOnPrinterListener(new MyPrint.OnPrinterListener() {
                                @Override
                                public void onSuccessful() {
                                    //Impresion finalizada
                                    progress.dismiss();
                                    MyApp.getDBO().manifiestoDetallePesosDao().updateBanderaImpresionLote(idAppManifiesto, idManifiestoDetalle, true);
                                    reloadData();
                                }

                                @Override
                                public void onFailure(String message) {
                                    final DialogBuilder dialogBuilder2 = new DialogBuilder(getContext());
                                    dialogBuilder2.setMessage(message);
                                    dialogBuilder2.setCancelable(false);
                                    dialogBuilder2.setTitle("CONFIRMACIÓN");
                                    dialogBuilder2.setPositiveButton("OK", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            progress.dismiss();
                                            dialogBuilder2.dismiss();
                                        }
                                    });
                                    dialogBuilder2.show();
                                }
                            });
                            print.printerIndividualLote(idAppManifiesto, idManifiestoDetalle, totalEtiquetas);

                        } catch (Exception e) {
                            final DialogBuilder dialogBuilder2 = new DialogBuilder(getContext());
                            dialogBuilder2.setMessage("No hay conexion con la impresora");
                            dialogBuilder2.setCancelable(false);
                            dialogBuilder2.setTitle("CONFIRMACIÓN");
                            dialogBuilder2.setPositiveButton("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    progress.dismiss();
                                    dialogBuilder2.dismiss();
                                }
                            });
                            dialogBuilder2.show();
                        }
                    }
                });
                Looper.loop();
            }
        }).start();

    }


}

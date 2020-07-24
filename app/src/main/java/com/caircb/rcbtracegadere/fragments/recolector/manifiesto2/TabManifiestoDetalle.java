package com.caircb.rcbtracegadere.fragments.recolector.manifiesto2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.strictmode.UnbufferedIoViolation;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.adapters.DialogMenuBaseAdapter;
import com.caircb.rcbtracegadere.adapters.ManifiestoDetalleAdapter;
import com.caircb.rcbtracegadere.database.dao.ManifiestoPaqueteDao;
import com.caircb.rcbtracegadere.database.entity.ManifiestoDetalleEntity;
import com.caircb.rcbtracegadere.database.entity.PaqueteEntity;
import com.caircb.rcbtracegadere.dialogs.DialogAgregarBultos;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.dialogs.DialogBultos;
import com.caircb.rcbtracegadere.dialogs.DialogBultosNo;
import com.caircb.rcbtracegadere.dialogs.DialogNotificacionDetalle;
import com.caircb.rcbtracegadere.helpers.MyCalculoPaquetes;
import com.caircb.rcbtracegadere.models.CalculoPaqueteResul;
import com.caircb.rcbtracegadere.models.CatalogoItemValor;
import com.caircb.rcbtracegadere.models.RowItemManifiesto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TabManifiestoDetalle extends LinearLayout {

    private List<RowItemManifiesto> detalles;
    private RecyclerView recyclerView;

    ManifiestoDetalleAdapter recyclerviewAdapter;

    Integer idAppManifiesto,tipoPaquete,estadoManifiesto;
    String numeroManifiesto;
    Window window;
    ListView mDialogMenuItems;

    Dialog dialogOpcioneItem;
    DialogBultos dialogBultos;
    DialogMenuBaseAdapter dialogMenuBaseAdapter;
    MyCalculoPaquetes calculoPaquetes;
    FloatingActionButton mensajes;
    AlertDialog alert;
    AlertDialog.Builder alertDialog;
    List<String> itemsCategoriaPaquete;
    Integer tipoGestion;
    PaqueteEntity pkg;
    Integer tipoBalanza=0;
    Integer tipoRecoleccion;

    public TabManifiestoDetalle(Context context,Integer manifiestoID,Integer tipoPaquete,String numeroManifiesto,Integer estado, Integer tipoRecoleccion) {
        super(context);
        this.idAppManifiesto=manifiestoID;
        this.tipoPaquete=tipoPaquete;
        //this.detalles = detalles;
        this.estadoManifiesto = estado;
        this.numeroManifiesto=numeroManifiesto;
        this.tipoRecoleccion=tipoRecoleccion;
        View.inflate(context, R.layout.tab_manifiesto_detalle, this);
        init();
        loadData();
    }

    private void init(){
        mensajes = this.findViewById(R.id.fab);
        mensajes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogNotificacionDetalle dialogMensajes = new DialogNotificacionDetalle(getContext(),idAppManifiesto);
                dialogMensajes.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogMensajes.setCancelable(false);
                dialogMensajes.show();
            }
        });

        calculoPaquetes= new MyCalculoPaquetes(idAppManifiesto,tipoPaquete);
        recyclerView = this.findViewById(R.id.recyclerManifiestoDetalle);
        recyclerviewAdapter = new ManifiestoDetalleAdapter(getContext(),numeroManifiesto,estadoManifiesto,idAppManifiesto,tipoRecoleccion);
    }

    @SuppressLint("RestrictedApi")
    private void loadData(){
        if(estadoManifiesto!= 1){mensajes.setVisibility(GONE);}
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        detalles = MyApp.getDBO().manifiestoDetalleDao().fetchHojaRutaDetallebyIdManifiesto(idAppManifiesto);

        recyclerviewAdapter.setTaskList(detalles);
        recyclerView.setAdapter(recyclerviewAdapter);

        recyclerviewAdapter.setOnItemClickListener(new ManifiestoDetalleAdapter.ClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onItemClick(int position, View v) {
                int x=0;
                if(estadoManifiesto == 1){
                    if (tipoRecoleccion == 1){
                        openOpcionesItems(position, detalles.get(position).getId());
                    }
                    if (tipoRecoleccion == 2){
                        final DialogBultosNo dialogBultosNo = new DialogBultosNo(getContext(),idAppManifiesto,detalles.get(position).getId());
                        dialogBultosNo.setCancelable(false);
                        dialogBultosNo.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialogBultosNo.setmOnRegistrarBultoListener(new DialogBultosNo.OnRegistrarBultoListener() {
                            @Override
                            public void onSuccesfull(String numeroBultos, Integer idDetalle) {
                                MyApp.getDBO().manifiestoDetallePesosDao().deleteTableValores(idAppManifiesto,idDetalle);
                                if (numeroBultos.equals("")||numeroBultos.equals("0")){
                                    final DialogBuilder dialogBuilder2=new DialogBuilder(getContext());
                                    dialogBuilder2.setMessage("Ingrese un numero válido");
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
                                }else {
                                    MyApp.getDBO().manifiestoDetalleDao().updateCantidadBultoManifiestoDetalle(idDetalle,numeroBultos.equals("")?0.0:Double.parseDouble(numeroBultos),0,0,true);
                                    for (int i=1;i<=Integer.parseInt(numeroBultos);i++){
                                        MyApp.getDBO().manifiestoDetallePesosDao().saveValores(idAppManifiesto,idDetalle,0.0,"",null,"", false, i);
                                    }
                                    List<CatalogoItemValor> item=MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarValores(idAppManifiesto,idDetalle);
                                    detalles.clear();
                                    loadData();
                                    dialogBultosNo.dismiss();
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
    }

    private void openOpcionesItems(final Integer positionItem, final Integer idDetManifiesto){

        dialogOpcioneItem = new Dialog(this.getContext());
       /* final ArrayList<MenuItem> myListOfItems = new ArrayList<>();
        myListOfItems.add(new MenuItem("BULTOS"));
        myListOfItems.add(new MenuItem("PAQUETES"));*/

        //dialogMenuBaseAdapter = new DialogMenuBaseAdapter(this.getContext(),myListOfItems);
        View view = ((Activity)getContext()).getLayoutInflater().inflate(R.layout.dialog_opciones_detalle, null);
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
                MyApp.getDBO().manifiestoDetalleDao().updateTipoBalanzaByDetalleId(idAppManifiesto, idDetManifiesto, 1);
                tipoBalanza = 1;
                showTipoPaquete(positionItem);
            }
        });
        btnBalanzaCliente.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApp.getDBO().manifiestoDetalleDao().updateTipoBalanzaByDetalleId(idAppManifiesto, idDetManifiesto, 2);
                tipoBalanza = 2;
                //List<ManifiestoDetalleEntity> lista =  MyApp.getDBO().manifiestoDetalleDao().fecthConsultarManifiestoDetallebyID(idAppManifiesto);
                showTipoPaquete(positionItem);
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

    private void showTipoPaquete(final Integer positionItem){

        itemsCategoriaPaquete = new ArrayList<>();
        if(tipoPaquete != null){
            pkg = MyApp.getDBO().paqueteDao().fechConsultaPaqueteEspecifico(tipoPaquete);

            if(pkg.getEntregaSoloFundas())itemsCategoriaPaquete.add(ManifiestoPaqueteDao.ARG_INFECCIOSO);
            if(pkg.getEntregaSoloGuardianes())itemsCategoriaPaquete.add(ManifiestoPaqueteDao.ARG_CORTOPUNZANTE);

            alertDialog = new AlertDialog.Builder(getContext());
            alertDialog.setTitle("TIPO  PAQUETE");
            alertDialog.setSingleChoiceItems(itemsCategoriaPaquete.toArray(new String[itemsCategoriaPaquete.size()]), -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            tipoGestion = 100;
                            openDialogBultos(positionItem, tipoGestion);
                            alert.dismiss();
                            break;
                        case 1:
                            tipoGestion = 101;
                            openDialogBultos(positionItem, tipoGestion);
                            alert.dismiss();
                            break;
                        case 2:
                            tipoGestion = 102;
                            openDialogBultos(positionItem, tipoGestion);
                            alert.dismiss();
                            break;
                    }
                }
            });
            alert = alertDialog.create();
            alert.setCanceledOnTouchOutside(false);
            //alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alert.show();

        }else{
            openDialogBultos(positionItem, 0);
        }
    }

    private void openDialogBultos(Integer position, final Integer tipoGestion){

        if(dialogBultos==null){
            dialogOpcioneItem.dismiss();
            dialogBultos = new DialogBultos(
                    getContext(),
                    position,
                    idAppManifiesto,
                    detalles.get(position).getId(),
                    tipoPaquete,
                    numeroManifiesto+"$"+detalles.get(position).getCodigo(),
                    tipoGestion
            );
            dialogBultos.setCancelable(false);
            dialogBultos.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogBultos.setOnBultoListener(new DialogBultos.OnBultoListener() {
                @Override
                public void onSuccessful(BigDecimal valor, int position, int cantidad, PaqueteEntity pkg, boolean isClose, boolean faltaImpresiones) {
                    if(isClose && dialogBultos!=null){
                            dialogBultos.dismiss();
                            dialogBultos = null;
                    }

                    RowItemManifiesto row = detalles.get(position);
                    row.setPeso(valor.doubleValue());
                    row.setTipoBalanza(tipoBalanza);;

                    if(row.getTipoItem()==1) row.setCantidadBulto(cantidad); //unidad
                    else if(row.getTipoItem()==2) row.setCantidadBulto(1); //servicio
                    else if(row.getTipoItem()==3) row.setCantidadBulto(Double.valueOf(cantidad)); //otros cantida = peso...
                    //else if(row.getTipoItem()==3) row.setCantidadBulto(row.getPeso()); //otros cantida = peso...

                    row.setEstado(true);
                    row.setFaltaImpresiones(faltaImpresiones);
                    recyclerviewAdapter.notifyDataSetChanged();
                    //actualizar datos en dbo local...
                    MyApp.getDBO().manifiestoDetalleDao().updateCantidadBultoManifiestoDetalle(row.getId(),row.getCantidadBulto(),row.getPeso(),cantidad,row.isEstado());

                    //calculo de paquetes...
                    if(pkg!=null){
                       calculoPaquetes.algoritmo(pkg);
                    }
                }

                @Override
                public void onCanceled(boolean faltaImpresos) {
                    if(dialogBultos!=null){
                        if(faltaImpresos){
                            detalles.clear();
                            loadData();
                        }
                        dialogBultos.dismiss();
                        dialogBultos=null;
                    }
                }
            });
            dialogBultos.show();

            window = dialogBultos.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    public boolean validaExisteDetallesSeleccionados(){
        return MyApp.getDBO().manifiestoDetalleDao().fecthConsultarManifiestoDetalleSeleccionados(idAppManifiesto).size()>0;
    }
}

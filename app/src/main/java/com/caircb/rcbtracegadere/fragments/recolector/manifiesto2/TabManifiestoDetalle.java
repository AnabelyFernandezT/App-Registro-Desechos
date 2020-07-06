package com.caircb.rcbtracegadere.fragments.recolector.manifiesto2;

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
import com.caircb.rcbtracegadere.dialogs.DialogBultos;
import com.caircb.rcbtracegadere.helpers.MyCalculoPaquetes;
import com.caircb.rcbtracegadere.models.CalculoPaqueteResul;
import com.caircb.rcbtracegadere.models.RowItemManifiesto;

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

    AlertDialog alert;
    AlertDialog.Builder alertDialog;
    List<String> itemsCategoriaPaquete;
    Integer tipoGestion;
    PaqueteEntity pkg;


    public TabManifiestoDetalle(Context context,Integer manifiestoID,Integer tipoPaquete,String numeroManifiesto,Integer estado) {
        super(context);
        this.idAppManifiesto=manifiestoID;
        this.tipoPaquete=tipoPaquete;
        //this.detalles = detalles;
        this.estadoManifiesto = estado;
        this.numeroManifiesto=numeroManifiesto;
        View.inflate(context, R.layout.tab_manifiesto_detalle, this);
        init();
        loadData();
    }

    private void init(){
        calculoPaquetes= new MyCalculoPaquetes(idAppManifiesto,tipoPaquete);
        recyclerView = this.findViewById(R.id.recyclerManifiestoDetalle);
        recyclerviewAdapter = new ManifiestoDetalleAdapter(getContext(),numeroManifiesto,estadoManifiesto);
    }

    private void loadData(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        detalles = MyApp.getDBO().manifiestoDetalleDao().fetchHojaRutaDetallebyIdManifiesto(idAppManifiesto);

        recyclerviewAdapter.setTaskList(detalles);
        recyclerView.setAdapter(recyclerviewAdapter);

        recyclerviewAdapter.setOnItemClickListener(new ManifiestoDetalleAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                int x=0;
                if(estadoManifiesto == 1){
                    openOpcionesItems(position, detalles.get(position).getId());
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
                showTipoPaquete(positionItem);
            }
        });
        btnBalanzaCliente.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApp.getDBO().manifiestoDetalleDao().updateTipoBalanzaByDetalleId(idAppManifiesto, idDetManifiesto, 2);
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

    private void openDialogBultos(Integer position, Integer tipoGestion){

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
                public void onSuccessful(BigDecimal valor, int position, int cantidad, PaqueteEntity pkg, boolean isClose) {
                    if(isClose && dialogBultos!=null){
                            dialogBultos.dismiss();
                            dialogBultos = null;
                    }

                    RowItemManifiesto row = detalles.get(position);
                    row.setPeso(valor.doubleValue());


                    if(row.getTipoItem()==1) row.setCantidadBulto(cantidad); //unidad
                    else if(row.getTipoItem()==2) row.setCantidadBulto(1); //servicio
                    else if(row.getTipoItem()==3) row.setCantidadBulto(Double.valueOf(cantidad)); //otros cantida = peso...
                    //else if(row.getTipoItem()==3) row.setCantidadBulto(row.getPeso()); //otros cantida = peso...

                    row.setEstado(true);
                    recyclerviewAdapter.notifyDataSetChanged();
                    //actualizar datos en dbo local...
                    MyApp.getDBO().manifiestoDetalleDao().updateCantidadBultoManifiestoDetalle(row.getId(),row.getCantidadBulto(),row.getPeso(),cantidad,row.isEstado());

                    //calculo de paquetes...
                    if(pkg!=null){
                       calculoPaquetes.algoritmo(pkg);
                    }
                }

                @Override
                public void onCanceled() {
                    if(dialogBultos!=null){
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

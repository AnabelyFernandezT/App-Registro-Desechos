package com.caircb.rcbtracegadere.fragments.recolector.manifiesto2;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.adapters.DialogMenuBaseAdapter;
import com.caircb.rcbtracegadere.adapters.ManifiestoDetalleAdapter;
import com.caircb.rcbtracegadere.adapters.ManifiestoDetalleBaseAdapter;
import com.caircb.rcbtracegadere.dialogs.DialogBultos;
import com.caircb.rcbtracegadere.models.MenuItem;
import com.caircb.rcbtracegadere.models.RowItemManifiesto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TabManifiestoDetalle extends LinearLayout {

    private List<RowItemManifiesto> detalles;
    private RecyclerView recyclerView;

    ManifiestoDetalleAdapter recyclerviewAdapter;

    Integer idAppManifiesto,tipoPaquete;
    Window window;
    ListView mDialogMenuItems;

    Dialog dialogOpcioneItem;
    DialogBultos dialogBultos;
    DialogMenuBaseAdapter dialogMenuBaseAdapter;

    public TabManifiestoDetalle(Context context,Integer manifiestoID,Integer tipoPaquete) {
        super(context);
        this.idAppManifiesto=manifiestoID;
        this.tipoPaquete=tipoPaquete;
        this.detalles = detalles;
        View.inflate(context, R.layout.tab_manifiesto_detalle, this);
        init();
        loadData();
    }

    private void init(){
        recyclerView = this.findViewById(R.id.recyclerManifiestoDetalle);
        recyclerviewAdapter = new ManifiestoDetalleAdapter(getContext());
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
                openOpcionesItems(position);
            }
        });
    }

    private void openOpcionesItems(final Integer positionItem){
        dialogOpcioneItem = new Dialog(this.getContext());
       /* final ArrayList<MenuItem> myListOfItems = new ArrayList<>();
        myListOfItems.add(new MenuItem("BULTOS"));
        myListOfItems.add(new MenuItem("PAQUETES"));*/

        //dialogMenuBaseAdapter = new DialogMenuBaseAdapter(this.getContext(),myListOfItems);
        View view = ((Activity)getContext()).getLayoutInflater().inflate(R.layout.dialog_opciones_detalle, null);
        LinearLayout txtBultos = view.findViewById(R.id.txtBultos);
        LinearLayout txtPaquetes = view.findViewById(R.id.txtPaquetes);
        LinearLayout btnCancelar = view.findViewById(R.id.btnCancel);
        txtBultos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogBultos(positionItem);
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

    private void openDialogBultos(Integer position){
        if(dialogBultos==null){
            dialogOpcioneItem.dismiss();
            dialogBultos = new DialogBultos(getContext(),position,idAppManifiesto,detalles.get(position).getId(),tipoPaquete);
            dialogBultos.setCancelable(false);
            dialogBultos.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogBultos.setOnBultoListener(new DialogBultos.OnBultoListener() {
                @Override
                public void onSuccessful(BigDecimal valor, int position, int cantidad, boolean isClose) {
                    if(isClose && dialogBultos!=null){
                            dialogBultos.dismiss();
                            dialogBultos = null;
                    }

                    RowItemManifiesto row = detalles.get(position);
                    row.setPeso(valor.doubleValue());

                    if(row.getTipoItem()==1) row.setCantidadBulto(cantidad); //unidad
                    else if(row.getTipoItem()==2) row.setCantidadBulto(1); //servicio
                    else if(row.getTipoItem()==3) row.setCantidadBulto(row.getPeso()); //otros cantida = peso...

                    row.setEstado(true);
                    recyclerviewAdapter.notifyDataSetChanged();
                    //actualizar datos en dbo local...
                    MyApp.getDBO().manifiestoDetalleDao().updateCantidadBultoManifiestoDetalle(row.getId(),row.getCantidadBulto(),row.getPeso(),row.isEstado());

                    //calculo de paquetes...


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
}

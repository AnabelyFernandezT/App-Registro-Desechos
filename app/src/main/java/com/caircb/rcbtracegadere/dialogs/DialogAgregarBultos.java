package com.caircb.rcbtracegadere.dialogs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.adapters.BultosAdapter;
import com.caircb.rcbtracegadere.fragments.recolector.manifiesto2.Manifiesto2Fragment;
import com.caircb.rcbtracegadere.generics.MyDialog;

import java.math.BigDecimal;
import java.util.ArrayList;

public class DialogAgregarBultos extends MyDialog implements View.OnClickListener {

    Activity _activity;
    LinearLayout btnAgregarBulto,btnCancel;
    private ArrayList<String> itemsBultos;
    private RecyclerView recylcerBultos;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager recyclerManager;
    private int cont=0;
    private EditText pesoBulto;
    private int idManifiestoDetalle;
    private  int idManifiesto;
    DialogBuilder dialogBuilder;
    private TextView totalPesos;
    private  TextView totalBultos;

    public DialogAgregarBultos(@NonNull Context context, int idManifiestoDetalle, int idManifiesto) {
        super(context, R.layout.dialog_agregar_bultos);
        this._activity = (Activity)context;
        this.idManifiestoDetalle = idManifiestoDetalle;
        this.idManifiesto = idManifiesto;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getView());
        init();
    }

    @Override
    public void onClick(View v) {

    }

    private void init() {
        itemsBultos = new ArrayList<>();
        btnAgregarBulto = findViewById(R.id.btnAgregarBulto);
        btnCancel = findViewById(R.id.btnCancel);
        pesoBulto = findViewById(R.id.txtPesoBulto);
        totalBultos = findViewById(R.id.lblTotalBultos);
        totalPesos = findViewById(R.id.lblPesoTotal);
        recylcerBultos = findViewById(R.id.recyclerview);
        recyclerManager = new LinearLayoutManager(getContext());


        btnAgregarBulto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemsBultos.add(pesoBulto.getText().toString());
                cont++;
                totalBultos.setText(String.valueOf(cont));
                BigDecimal imputValor = new BigDecimal(pesoBulto.getText().toString());

                recyclerAdapter = new BultosAdapter(recylcerBultos,itemsBultos, R.layout.item_bulto, new BultosAdapter.OnItemClickListener() {
                    @Override
                    public void OnItemClick(String txtItemPesoBulto, final int position) {
                        dialogBuilder = new DialogBuilder(getActivity());
                        dialogBuilder.setMessage("¿Está seguro de eliminar bulto?");
                        dialogBuilder.setCancelable(false);
                        dialogBuilder.setTitle("CONFIRMACIÓN");
                        dialogBuilder.setPositiveButton("SI", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogBuilder.dismiss();
                                deleteItem(position);
                                cont=cont-1;
                                totalBultos.setText(String.valueOf(cont));
                            }
                        });
                        dialogBuilder.setNegativeButton("NO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogBuilder.dismiss();
                            }
                        });
                        dialogBuilder.show();

                    }
                });

                recylcerBultos.setHasFixedSize(true);
                recylcerBultos.setItemAnimator(new DefaultItemAnimator());
                recylcerBultos.setLayoutManager(recyclerManager);
                recylcerBultos.setAdapter(recyclerAdapter);
                pesoBulto.setText("");
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAgregarBultos.this.dismiss();


            }
        });
    }

    private void deleteItem(int position){
        itemsBultos.remove(position);
        recyclerAdapter.notifyItemRemoved(position);
    }


}

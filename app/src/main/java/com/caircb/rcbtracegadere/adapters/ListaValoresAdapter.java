package com.caircb.rcbtracegadere.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.caircb.rcbtracegadere.MainActivity;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.helpers.MyConstant;
import com.caircb.rcbtracegadere.models.CatalogoItemValor;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ListaValoresAdapter extends ArrayAdapter<CatalogoItemValor> {

    private List<CatalogoItemValor> listaItems;
    private LayoutInflater mInflater;
    Context context;
    AlertDialog.Builder builder;
    DialogBuilder dialogBuilder;

    public interface OnItemBultoListener {
        public void onEliminar(Integer position);
    }
    public interface OnItemBultoImpresionListener{
        public void onSendImpresion(Integer position);
    }

    private OnItemBultoListener mOnItemBultoListener;
    private OnItemBultoImpresionListener mOnImtemBultoImpresionListener;

    public ListaValoresAdapter(Context context, List<CatalogoItemValor> listaCatalogo){
        super(context, R.layout.lista_items_calculadora, listaCatalogo);
        mInflater= LayoutInflater.from(context);
        this.listaItems = listaCatalogo;
        this.context = context;
    }

    public static class ViewHolder{
        TextView txtItem;
        LinearLayout btnEliminar;
        TextView txtItemTipo;
        RelativeLayout btnImpresion;
        RelativeLayout btnImpresionOk;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ListaValoresAdapter.ViewHolder holder =null;
        //final CatalogoItemValor row = getItem(position);
        final CatalogoItemValor row = listaItems.get(position);

        int cont = position+1;
        LayoutInflater minflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = minflater.inflate(R.layout.lista_items_calculadora,null);
            holder = new ListaValoresAdapter.ViewHolder();

            holder.txtItem = (TextView) convertView.findViewById(R.id.txtItem);
            holder.txtItemTipo = (TextView)convertView.findViewById(R.id.txtItemTipo);
            holder.btnEliminar = (LinearLayout) convertView.findViewById(R.id.btnEliminar);
            holder.btnImpresion = (RelativeLayout)convertView.findViewById(R.id.btnImpresion);
            holder.btnImpresionOk = (RelativeLayout)convertView.findViewById(R.id.btnImpresionOk);

            convertView.setTag(holder);
        }else {
            holder = (ListaValoresAdapter.ViewHolder) convertView.getTag();
        }
        holder.txtItem.setText("Bulto "+row.getNumeroBulto()+":           "+row.getValor());
        if(row.getTipo().length()>0){holder.txtItemTipo.setVisibility(View.VISIBLE);holder.txtItemTipo.setText(row.getTipo());}

        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(row.isImpresion()){

                    dialogBuilder = new DialogBuilder(getContext());
                    dialogBuilder.setMessage("BULTO IMPRESO: Seguro que desea eliminarlo ?");
                    dialogBuilder.setCancelable(false);
                    dialogBuilder.setPositiveButton("SI", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismiss();
                            if(mOnItemBultoListener!= null){ mOnItemBultoListener.onEliminar(position); }
                        }
                    });
                    dialogBuilder.setNegativeButton("NO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismiss();
                        }
                    });
                    dialogBuilder.show();

                }else{
                    dialogBuilder = new DialogBuilder(getContext());
                    dialogBuilder.setMessage("Seguro que desea elimarlo ?");
                    dialogBuilder.setCancelable(false);
                    dialogBuilder.setPositiveButton("SI", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismiss();
                            if(mOnItemBultoListener!= null){ mOnItemBultoListener.onEliminar(position); }
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

            }
        });

        if(row.isImpresion()){
            holder.btnImpresionOk.setVisibility(View.VISIBLE);
            holder.btnImpresion.setVisibility(View.GONE);
        }else {
            holder.btnImpresion.setVisibility(View.VISIBLE);
            holder.btnImpresionOk.setVisibility(View.GONE);
        }

        holder.btnImpresion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("iniciando impresion");
                dialogBuilder = new DialogBuilder(getContext());
                dialogBuilder.setMessage("Desea imprimir etiqueta para este bulto?");
                dialogBuilder.setCancelable(false);
                dialogBuilder.setPositiveButton("SI", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                        if(mOnImtemBultoImpresionListener != null){ mOnImtemBultoImpresionListener.onSendImpresion(position); }
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
        holder.btnImpresionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("no hace nada");
            }
        });

        return convertView;
    }

    public void setOnItemBultoListener(@Nullable OnItemBultoListener l){
        mOnItemBultoListener =l;
    }
    public void setOnItemBultoImpresion (@Nullable OnItemBultoImpresionListener l){
        mOnImtemBultoImpresionListener = l;
    }
    public void filterList(List<CatalogoItemValor> updateList) {
        this.listaItems = updateList;
        notifyDataSetChanged();
    }

}

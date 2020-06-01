package com.caircb.rcbtracegadere.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.models.CatalogoItemValor;

import java.util.List;

public class ListaValoresAdapter extends ArrayAdapter<CatalogoItemValor> {

    //private List<CatalogoItemValor> listaItems;
    private LayoutInflater mInflater;
    Context context;

    public interface OnItemBultoListener {
        public void onEliminar(Integer position);
    }
    private OnItemBultoListener mOnItemBultoListener;

    public ListaValoresAdapter(Context context, List<CatalogoItemValor> listaCatalogo){
        super(context, R.layout.lista_items_calculadora, listaCatalogo);
        mInflater= LayoutInflater.from(context);
        this.context = context;
    }

    public static class ViewHolder{
        TextView txtItem;
        LinearLayout btnEliminar;
        TextView txtItemTipo;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ListaValoresAdapter.ViewHolder holder =null;
        final CatalogoItemValor row = getItem(position);
        int cont = position+1;
        LayoutInflater minflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = minflater.inflate(R.layout.lista_items_calculadora,null);
            holder = new ListaValoresAdapter.ViewHolder();

            holder.txtItem = (TextView) convertView.findViewById(R.id.txtItem);
            holder.txtItemTipo = (TextView)convertView.findViewById(R.id.txtItemTipo);
            holder.btnEliminar = (LinearLayout) convertView.findViewById(R.id.btnEliminar);

            convertView.setTag(holder);
        }else {
            holder = (ListaValoresAdapter.ViewHolder) convertView.getTag();
        }
        holder.txtItem.setText("Bulto "+cont+":           "+row.getValor());
        if(row.getTipo().length()>0){holder.txtItemTipo.setVisibility(View.VISIBLE);holder.txtItemTipo.setText(row.getTipo());}

        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemBultoListener!= null){
                    mOnItemBultoListener.onEliminar(position);
                }
            }
        });

        return convertView;
    }

    public void setOnItemBultoListener(@Nullable OnItemBultoListener l){
        mOnItemBultoListener =l;
    }

}

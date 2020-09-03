package com.caircb.rcbtracegadere.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.models.ItemQrDetallePlanta;

import java.util.ArrayList;
import java.util.List;


public class PesosAdapterPlantaLote extends RecyclerView.Adapter<PesosAdapterPlantaLote.MyViewHolder>{

    private Context mContext;
    private List<ItemQrDetallePlanta> rowItems;

    public PesosAdapterPlantaLote(Context context,List<ItemQrDetallePlanta> rowItems) {
        mContext = context;
        this.rowItems = rowItems;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pesos_planta, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull MyViewHolder holder,final int position) {
        final ItemQrDetallePlanta it = rowItems.get(position);
        holder.itmNombreDesecho.setText(it.getNombreDesecho());
        holder.itmPesoDesecho.setText(it.getPesoDesecho()+"");

    }


    @Override
    public int getItemCount() {
        return rowItems.size();
    }

    public void setTaskList(List<ItemQrDetallePlanta> taskList) {
        this.rowItems = taskList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView itmNombreDesecho;
        TextView itmPesoDesecho;

        public MyViewHolder(View itemView) {
            super(itemView);
            itmNombreDesecho = itemView.findViewById(R.id.itmNombreDesecho);
            itmPesoDesecho = itemView.findViewById(R.id.itmPesoDesecho);

        }
    }
}

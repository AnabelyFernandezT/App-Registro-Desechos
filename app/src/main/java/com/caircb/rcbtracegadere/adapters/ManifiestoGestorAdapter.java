package com.caircb.rcbtracegadere.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.models.ItemLotePadre;
import com.caircb.rcbtracegadere.models.ItemManifiesto;

import java.util.ArrayList;
import java.util.List;

public class ManifiestoGestorAdapter extends RecyclerView.Adapter<ManifiestoGestorAdapter.MyViewHolder>  {

    private Context mContext;
    private List<ItemLotePadre> manifiestosList ;

    public ManifiestoGestorAdapter(Context context){
        mContext = context;
        manifiestosList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_lote_padre,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull MyViewHolder holder, int position) {
        final ItemLotePadre it = manifiestosList.get(position);
        holder.txtNumManifiestoPadre.setText(it.getNumeroManifiestoPadre());
        holder.txtManifiestosHijos.setText(it.getManifiestos());
        holder.txtTotal.setText(it.getTotal().toString());
        holder.txtNombreCliente.setText(it.getNombreCliente());
    }

    @Override
    public int getItemCount() {
        return manifiestosList.size();
    }

    public void setTaskList(List<ItemLotePadre> taskList) {
        this.manifiestosList = taskList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtNumManifiestoPadre;
        TextView txtManifiestosHijos;
        TextView txtTotal;
        TextView txtNombreCliente;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtNumManifiestoPadre = itemView.findViewById(R.id.itm_num_manifiesto_padre);
            txtManifiestosHijos = itemView.findViewById(R.id.itm_manifiestos_hijos);
            txtTotal = itemView.findViewById(R.id.itm_total);
            txtNombreCliente = itemView.findViewById(R.id.itm_nombre_cliente);
        }
    }
}

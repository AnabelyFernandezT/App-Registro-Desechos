package com.caircb.rcbtracegadere.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.models.ItemManifiesto;
import com.caircb.rcbtracegadere.models.ItemManifiestoSede;

import java.util.ArrayList;
import java.util.List;

public class ManifiestoAdapterSede extends RecyclerView.Adapter<ManifiestoAdapterSede.MyViewHolder>  {

    private Context mContext;
    int cRojo;
    int cVerde;
    private List<ItemManifiestoSede> manifiestosList ;
    private LinearLayout borderVerificacion;

    public ManifiestoAdapterSede(Context context){
        mContext = context;
        manifiestosList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manifiesto_sede,parent,false);
        cRojo=R.drawable.shape_card_border_red;
        cVerde = R.drawable.shape_card_border;
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull MyViewHolder holder, int position) {
        final ItemManifiestoSede it = manifiestosList.get(position);
        holder.txtNumManifiesto.setText(it.getManifiestos());
        holder.txtCliente.setText(it.getNombreCliente());
        holder.borderVerificacion.setBackgroundResource(cRojo);
    }

    @Override
    public int getItemCount() {
        return manifiestosList.size();
    }

    public void setTaskList(List<ItemManifiestoSede> taskList) {
        this.manifiestosList = taskList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtNumManifiesto;
        TextView txtCliente;
        LinearLayout borderVerificacion;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtNumManifiesto = itemView.findViewById(R.id.itm_num_manifiesto);
            txtCliente = itemView.findViewById(R.id.itm_cliente);
            borderVerificacion= (LinearLayout) itemView.findViewById(R.id.rowFG);
        }
    }
}

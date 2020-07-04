package com.caircb.rcbtracegadere.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.models.ItemLote; //Entity de la Base de Datos LOTE
import com.caircb.rcbtracegadere.models.ItemManifiesto;

import java.util.ArrayList;
import java.util.List;

public class LoteAdapter extends RecyclerView.Adapter<LoteAdapter.MyViewHolder> {

    private Context mContext;
    private List<ItemLote> lotesList;

    public LoteAdapter(Context context){
        mContext = context;
        lotesList = new ArrayList<>();
    }

    @NonNull
    @Override
    public LoteAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_lote,parent,false);
        return new LoteAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull LoteAdapter.MyViewHolder holder, int position) {
        final ItemLote it = lotesList.get(position);
        holder.txtNumLote.setText(it.getNumeroLote());
        holder.txtRutasRecolectadas.setText(it.getRutasRecolectadas());
        holder.txtSubruta.setText(it.getSubRuta());
        holder.txtPlaca.setText(it.getPlaca());
        holder.txtChofer.setText(it.getChofer());
        holder.txtOperadores.setText(it.getOperadores());
    }

    @Override
    public int getItemCount() {
        return lotesList.size();
    }

    public void setTaskList(List<ItemLote> taskList) {
        this.lotesList = taskList;
        notifyDataSetChanged();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtNumLote;
        TextView txtRutasRecolectadas;
        TextView txtSubruta;
        TextView txtPlaca;
        TextView txtChofer;
        TextView txtOperadores;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtNumLote = itemView.findViewById(R.id.itm_num_lote);
            txtRutasRecolectadas = itemView.findViewById(R.id.itm_rutasRecolectadas);
            txtSubruta = itemView.findViewById(R.id.itm_subruta);
            txtPlaca = itemView.findViewById(R.id.itm_Placa);
            txtChofer = itemView.findViewById(R.id.itm_Chofer);
            txtOperadores = itemView.findViewById(R.id.itm_Operadores);
        }
    }
}

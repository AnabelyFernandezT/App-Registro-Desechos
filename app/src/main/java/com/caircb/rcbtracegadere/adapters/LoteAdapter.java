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
import com.caircb.rcbtracegadere.tasks.UserConsultaLotes;

import java.util.ArrayList;
import java.util.List;

public class LoteAdapter extends RecyclerView.Adapter<LoteAdapter.MyViewHolder> {

    private Context mContext;
    private List<ItemLote> lotesList;

    public LoteAdapter(Context context){
        this.mContext = context;
        this.lotesList = new ArrayList<>();
        UserConsultaLotes consultarLotes;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_lote,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull LoteAdapter.MyViewHolder holder, int position) {
        final ItemLote it = lotesList.get(position);

        holder.txtNumLote.setText(it.getCodigoLote());
        holder.txtRutasRecolectadas.setText(it.getNombreDestinatarioFinRutaCatalogo());
        holder.txtSubruta.setText(it.getSubRuta());
        holder.txtPlaca.setText(it.getPlacaVehiculo());
        holder.txtManifiestos.setText(it.getNumeroManifiesto());
        holder.txtRuta.setText(it.getRuta());
        holder.txtCodLote.setText(String.valueOf(it.getIdLoteContenedor()));
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
        TextView txtManifiestos;
        TextView txtRuta;
        TextView txtCodLote;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtNumLote = itemView.findViewById(R.id.itm_num_lote);
            txtRutasRecolectadas = itemView.findViewById(R.id.itm_rutasRecolectadas);
            txtSubruta = itemView.findViewById(R.id.itm_subruta);
            txtPlaca = itemView.findViewById(R.id.itm_Placa);
            txtManifiestos = itemView.findViewById(R.id.itm_manifiestos);
            txtRuta = itemView.findViewById(R.id.itm_ruta);
            txtCodLote = itemView.findViewById(R.id.itm_cod_lote);
        }
    }

}

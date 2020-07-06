package com.caircb.rcbtracegadere.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.models.ItemLoteHoteles;
import com.caircb.rcbtracegadere.models.ItemManifiesto;

import java.util.ArrayList;
import java.util.List;

public class ManifiestoAdapterHoteles extends RecyclerView.Adapter<ManifiestoAdapterHoteles.MyViewHolder>  {

    private Context mContext;
    private List<ItemLoteHoteles> manifiestosList ;

    public ManifiestoAdapterHoteles(Context context){
        mContext = context;
        manifiestosList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_manifiesto_hoteles,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull MyViewHolder holder, int position) {
        final ItemLoteHoteles it = manifiestosList.get(position);
        holder.txtNumLote.setText(it.getCodigoLoteContenedorHotel());
        holder.txtRuta.setText(it.getRuta());
        holder.txtSubRuta.setText(it.getSubRuta());
        holder.txtHoteles.setText(it.getHoteles());
        holder.txtPlaca.setText(it.getPlacaVehiculo());
        holder.txtChofer.setText(it.getChofer());
        holder.txtOperadores.setText(it.getOperador());
    }

    @Override
    public int getItemCount() {
        return manifiestosList.size();
    }

    public void setTaskList(List<ItemLoteHoteles> taskList) {
        this.manifiestosList = taskList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtNumLote;
        TextView txtRuta;
        TextView txtSubRuta;
        TextView txtHoteles;
        TextView txtPlaca;
        TextView txtChofer;
        TextView txtOperadores;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtNumLote = itemView.findViewById(R.id.itm_num_lote);
            txtRuta = itemView.findViewById(R.id.itm_ruta);
            txtSubRuta = itemView.findViewById(R.id.itm_subRuta);
            txtHoteles = itemView.findViewById(R.id.itm_hoteles);
            txtPlaca = itemView.findViewById(R.id.itm_placa);
            txtChofer = itemView.findViewById(R.id.itm_chofer);
            txtOperadores = itemView.findViewById(R.id.itm_operadores);
        }
    }
}

package com.caircb.rcbtracegadere.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.entity.RuteoRecoleccionEntity;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.fragments.planta.HomePlantaFragment;
import com.caircb.rcbtracegadere.fragments.recolector.HojaRutaAsignadaFragment;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.ItemManifiesto;
import com.caircb.rcbtracegadere.models.ItemManifiestoDetalleValorSede;
import com.caircb.rcbtracegadere.models.ItemManifiestoSede;
import com.caircb.rcbtracegadere.tasks.UserConsultaCodigoQrValidadorTask;
import com.caircb.rcbtracegadere.tasks.UserRegistrarFinLoteHospitalesTask;

import java.util.ArrayList;
import java.util.List;

public class ManifiestoAdapterSede extends RecyclerView.Adapter<ManifiestoAdapterSede.MyViewHolder> {

    private Context mContext;
    int cRojo;
    int cVerde;
    int cNaranja;
    private List<ItemManifiestoSede> manifiestosList;
    List<ItemManifiestoDetalleValorSede> detalleValor;
    int cantidadPeso=0;

    public ManifiestoAdapterSede(Context context) {
        mContext = context;
        manifiestosList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manifiesto_sede, parent, false);
        cRojo = R.drawable.shape_card_border_red;
        cNaranja = R.drawable.shape_card_border_naranja;
        cVerde = R.drawable.shape_card_border;
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull MyViewHolder holder, int position) {
        final ItemManifiestoSede it = manifiestosList.get(position);
        holder.txtNumManifiesto.setText(it.getNumeroManifiesto());
        holder.txtCliente.setText(it.getNombreCliente());
        if (it.getBultosSelecionado() > 0) {
            if (it.getEstado().equals(3)) {
                holder.borderVerificacion.setBackgroundResource(cVerde);
            } else {
                holder.borderVerificacion.setBackgroundResource(cNaranja);
            }
        } else {
            holder.borderVerificacion.setBackgroundResource(cRojo);
        }
        detalleValor = MyApp.getDBO().manifiestoPlantaDetalleValorDao().fetchManifiestosAsigByNumManif(it.getIdAppManifiesto());
        for(ItemManifiestoDetalleValorSede dv: detalleValor){
            if((dv.getPeso().equals(0.0))){
                cantidadPeso++;
            }
        }


        if(it.getTotalBultos().equals(cantidadPeso)){
            holder.observacion.setVisibility(View.VISIBLE);
            holder.txtObservacion.setText("Pesaje en Planta");
        }else{
            holder.txtObservacion.setText("");
        }
        cantidadPeso=0;
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
        TextView txtObservacion;
        LinearLayout observacion;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtNumManifiesto = itemView.findViewById(R.id.itm_num_manifiesto);
            txtCliente = itemView.findViewById(R.id.itm_cliente);
            txtObservacion = itemView.findViewById(R.id.itm_observacion);
            borderVerificacion = (LinearLayout) itemView.findViewById(R.id.rowFG);
            observacion = itemView.findViewById(R.id.observacion);
        }
    }
}

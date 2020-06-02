package com.caircb.rcbtracegadere.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.models.RowItemManifiesto;

import java.util.List;

public class ManifiestoDetalleBaseAdapterR extends RecyclerView.Adapter<ManifiestoDetalleBaseAdapterR.MyViewHolder> {

    private Context mContext;
    private boolean desactivaComp;
    private List<RowItemManifiesto> manifiestoDetalle;

    public ManifiestoDetalleBaseAdapterR(Context context, List<RowItemManifiesto> items, boolean desactivaComp){
        mContext = context;
        manifiestoDetalle = items;
        this.desactivaComp = desactivaComp;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_items_manifiesto,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull MyViewHolder holder, int position) {
        final RowItemManifiesto item = manifiestoDetalle.get(position);
        holder.txtUnidad.setText(item.getUnidad());
        holder.txtPeso.setText(String.valueOf(item.getPeso()));
        holder.txtCantidadBulto.setText(String.valueOf(item.getCantidadBulto()));
        holder.txtDescripcion.setText(item.getDescripcion());
        holder.chkEstado.setChecked(item.isEstado());


        if(desactivaComp == true){
            holder.chkEstado.setEnabled(false);
        }

        holder.chkEstado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    v.setSelected(true);
                    item.setEstado(true);
                    MyApp.getDBO().manifiestoDetalleDao().updateManifiestoDetallebyId(item.getId(),true);
                    notifyDataSetChanged();

                }else{
                    v.setSelected(false);
                    item.setEstado(false);
                    MyApp.getDBO().manifiestoDetalleDao().updateManifiestoDetallebyId(item.getId(),false);
                    notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public int getItemCount (){return  manifiestoDetalle.size();}

    public void setTaskList(List<RowItemManifiesto> taskList){
        this.manifiestoDetalle = taskList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{

        TextView txtUnidad;
        TextView txtPeso;
        TextView txtCantidadBulto;
        TextView txtDescripcion;
        LinearLayout btnEleminarItem;
        CheckBox chkEstado;
        TextView txtTratamiento;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtUnidad = itemView.findViewById(R.id.txtItemUnidad);
            txtPeso = itemView.findViewById(R.id.txtItemPeso);
            txtCantidadBulto = itemView.findViewById(R.id.txtItemCantidadBulto);
            txtDescripcion = itemView.findViewById(R.id.txtItemDescripcion);
            btnEleminarItem = itemView.findViewById(R.id.btnEleminarItem);
            chkEstado = itemView.findViewById(R.id.chkEstadoItemDetalle);
            txtTratamiento = itemView.findViewById(R.id.txtItemTratamiento);
        }
    }
}
package com.caircb.rcbtracegadere.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.models.RowItemManifiesto;
import com.caircb.rcbtracegadere.models.RowItemManifiestosDetalleGestores;

import java.util.ArrayList;
import java.util.List;

public class ManifiestoDetalleGestoresAdapter extends RecyclerView.Adapter<ManifiestoDetalleGestoresAdapter.MyViewHolder> {
    private ClickListener mClickListener;
    private Context mContext;
    private List<RowItemManifiesto> manifiestosDtList;

    public ManifiestoDetalleGestoresAdapter (Context context){
        this.mContext = context;
        this.manifiestosDtList = new ArrayList<>();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_detalles_gestores,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final RowItemManifiesto it = manifiestosDtList.get(position);

        holder.txtItemTratamiento.setText(it.getTratamiento());
        holder.txtItemDescripcion.setText(it.getCodigoMAE()+"-"+ it.getDescripcion());
        holder.txtItemCantidadBulto.setText(""+it.getCantidadBulto());
        holder.txtItemPeso.setText(""+it.getPeso());
        holder.chkEstadoItemDetalle.setChecked(it.isEstado());

        holder.chkEstadoItemDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox) v).isChecked()){
                    v.setSelected(true);
                    it.setEstado(true);
                }else {
                    v.setSelected(false);
                    it.setEstado(false);
                }
                MyApp.getDBO().manifiestoDetalleDao().actualizarCheckGestores(it.isEstado(),it.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return manifiestosDtList.size();
    }

    public void setTaskList(List<RowItemManifiesto> taskList) {
        this.manifiestosDtList = taskList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(ManifiestoDetalleAdapter.ClickListener clickListener) {
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtItemTratamiento, txtItemDescripcion,txtItemPeso,txtItemCantidadBulto;
        CheckBox chkEstadoItemDetalle;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtItemTratamiento = itemView.findViewById(R.id.txtItemTratamiento);
            txtItemDescripcion = itemView.findViewById(R.id.txtItemDescripcion);
            chkEstadoItemDetalle = itemView.findViewById(R.id.chkEstadoItemDetalle);
            txtItemPeso = itemView.findViewById(R.id.txtItemPeso);
            txtItemCantidadBulto = itemView.findViewById(R.id.txtItemCantidadBulto);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mClickListener!=null)mClickListener.onItemClick(getAdapterPosition(),v);
                }
            });


        }
    }

    public void setOnItemClickListener(@NonNull ClickListener l) {
        mClickListener= l;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }
}

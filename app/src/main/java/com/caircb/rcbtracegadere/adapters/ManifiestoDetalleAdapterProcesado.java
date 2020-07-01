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
import com.caircb.rcbtracegadere.database.AppDatabase;
import com.caircb.rcbtracegadere.models.RowItemManifiesto;

import java.util.ArrayList;
import java.util.List;

public class ManifiestoDetalleAdapterProcesado extends RecyclerView.Adapter<ManifiestoDetalleAdapterProcesado.MyViewHolder>  {

    private ClickListener mClickListener;
    private Context mContext;
    private List<RowItemManifiesto> manifiestosDtList;
    private String numeroManifiesto;

    public ManifiestoDetalleAdapterProcesado(Context context, String numeroManifiesto){
        this.mContext = context;
        this.manifiestosDtList = new ArrayList<>();
        this.numeroManifiesto=numeroManifiesto;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.lista_items_manifiesto,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull MyViewHolder holder, int position) {
        final RowItemManifiesto it = manifiestosDtList.get(position);
        holder.txtUnidad.setText(it.getUnidad());
        holder.txtPeso.setText(""+it.getPeso());
        holder.txtCantidadBulto.setText(""+it.getCantidadBulto());
        holder.txtDescripcion.setText(it.getDescripcion());
        holder.txtTratamiento.setText(it.getTratamiento());
        holder.chkEstado.setChecked(it.isEstado());
        holder.chkEstado.setClickable(false);

        /*holder.chkEstado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    v.setSelected(true);
                    it.setEstado(true);
                } else {
                    v.setSelected(false);
                    it.setEstado(false);
                }
                MyApp.getDBO().manifiestoDetalleDao().updateManifiestoDetallebyId(it.getId(),it.isEstado(), it.isEstado()? AppDatabase.getUUID(numeroManifiesto):"");
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return manifiestosDtList.size();
    }

    public void setTaskList(List<RowItemManifiesto> taskList) {
        this.manifiestosDtList = taskList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtUnidad;
        TextView txtPeso;
        TextView txtCantidadBulto;
        TextView txtDescripcion;
        TextView txtTratamiento;
        LinearLayout btnEleminarItem;
        CheckBox chkEstado;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtUnidad = itemView.findViewById(R.id.txtItemUnidad);
            txtPeso = itemView.findViewById(R.id.txtItemPeso);
            txtCantidadBulto = itemView.findViewById(R.id.txtItemCantidadBulto);
            txtDescripcion = itemView.findViewById(R.id.txtItemDescripcion);
            txtTratamiento = itemView.findViewById(R.id.txtItemTratamiento);
            btnEleminarItem = itemView.findViewById(R.id.btnEleminarItem);
            chkEstado = itemView.findViewById(R.id.chkEstadoItemDetalle);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mClickListener!=null)mClickListener.onItemClick(getAdapterPosition(),v);
                }
            });*/

        }

    }

    public void setOnItemClickListener(@NonNull ClickListener l) {
        mClickListener= l;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }
}

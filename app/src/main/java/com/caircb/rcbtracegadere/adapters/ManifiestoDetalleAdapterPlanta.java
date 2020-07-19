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
import androidx.room.util.StringUtil;

import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.models.ItemManifiestoDetalleSede;
import com.itextpdf.text.pdf.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ManifiestoDetalleAdapterPlanta extends RecyclerView.Adapter<ManifiestoDetalleAdapterPlanta.MyViewHolder>  {

    private ClickListener mClickListener;
    private Context mContext;
    private List<ItemManifiestoDetalleSede> manifiestosDtList;
    private String numeroManifiesto;
    private Integer estadoManifiesto;

    public ManifiestoDetalleAdapterPlanta(Context context, String numeroManifiesto, Integer estadoManifiesto){
        this.mContext = context;
        this.manifiestosDtList = new ArrayList<>();
        this.numeroManifiesto=numeroManifiesto;
        this.estadoManifiesto =estadoManifiesto;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.lista_items_manifiesto_sede,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull MyViewHolder holder, int position) {
        final ItemManifiestoDetalleSede it = manifiestosDtList.get(position);
        holder.txtCodigoMae.setText(it.getCodigoMae());
        String descripcion = it.getNombreDesecho();
        descripcion = descripcion.substring(0,1).toUpperCase() + descripcion.substring(1).toLowerCase();
        holder.txtDescripcion.setText(""+descripcion);
        holder.totalBultos.setText(""+it.getBultosSelecionado()+" / "+it.getTotalBultos());
        holder.chkEstado.setClickable(false);

        if(it.getBultosSelecionado()== it.getTotalBultos()){
            holder.chkEstado.setChecked(true);
        }else{
            holder.chkEstado.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return manifiestosDtList.size();
    }

    public void setTaskList(List<ItemManifiestoDetalleSede> taskList) {
        this.manifiestosDtList = taskList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtCodigoMae;
        TextView txtDescripcion;
        TextView totalBultos;
        LinearLayout btnEleminarItem;
        CheckBox chkEstado;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtCodigoMae = itemView.findViewById(R.id.txtCodigoMae);
            txtDescripcion = itemView.findViewById(R.id.txtItemDescripcion);
            btnEleminarItem = itemView.findViewById(R.id.btnEleminarItem);
            totalBultos= itemView.findViewById(R.id.txtTotalBultos);
            chkEstado = itemView.findViewById(R.id.chkEstadoItemDetalle);

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

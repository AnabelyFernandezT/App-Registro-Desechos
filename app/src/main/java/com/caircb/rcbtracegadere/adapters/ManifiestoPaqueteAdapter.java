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
import com.caircb.rcbtracegadere.models.RowItemPaquete;

import java.util.ArrayList;
import java.util.List;

public class ManifiestoPaqueteAdapter extends RecyclerView.Adapter<ManifiestoPaqueteAdapter.MyViewHolder>  {

    private Context mContext;
    private List<RowItemPaquete> paquetesList ;

    public ManifiestoPaqueteAdapter(Context context){
        mContext = context;
        paquetesList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.lista_items_paquete,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull MyViewHolder holder, int position) {
        final RowItemPaquete it = paquetesList.get(position);
        holder.txtPkgNombre.setText(it.getNombre());
        holder.txtPkgCantidad.setText(""+it.getCantidad());
        holder.txtPkgPendiente.setText(""+it.getPendiente());

    }

    @Override
    public int getItemCount() {
        return paquetesList.size();
    }

    public void setTaskList(List<RowItemPaquete> taskList) {
        this.paquetesList = taskList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtPkgNombre;
        TextView txtPkgCantidad;
        TextView txtPkgPendiente;


        public MyViewHolder(View itemView) {
            super(itemView);

            txtPkgNombre = itemView.findViewById(R.id.txtItemPaqueteNombre);
            txtPkgCantidad = itemView.findViewById(R.id.txtItemPaqueteCantidad);
            txtPkgPendiente = itemView.findViewById(R.id.txtItemPaquetePendiente);

            /*
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mClickListener!=null)mClickListener.onItemClick(getAdapterPosition(),v);
                }
            });
             */

        }

    }
    /*
    public void setOnItemClickListener(@NonNull ClickListener l) {
        mClickListener= l;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }*/
}

package com.caircb.rcbtracegadere.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.models.ItemLote;
import com.caircb.rcbtracegadere.models.ItemNotificacion;
import com.caircb.rcbtracegadere.tasks.UserConsultaLotes;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private Context mContext;
    private List<ItemNotificacion> notificationList;

    public NotificationAdapter(Context context){
        this.mContext = context;
        this.notificationList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_notificacion,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull NotificationAdapter.MyViewHolder holder, int position) {
        final ItemNotificacion it = notificationList.get(position);
        holder.txtEstado.setText(it.getEstadoNotificacion());
        holder.txtNombre.setText(it.getNombreNotificacion());
    }

    @Override
    public int getItemCount() {
        if(notificationList!=null){
            return  notificationList.size() ;
        }else{
            return 0;
        }

    }

    public void setTaskList(List<ItemNotificacion> taskList) {
        this.notificationList = taskList;
        notifyDataSetChanged();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre;
        TextView txtEstado;
        public MyViewHolder(View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.itm_nombreNotif);
            txtEstado = itemView.findViewById(R.id.itm_estadoNotif);
        }
    }

}

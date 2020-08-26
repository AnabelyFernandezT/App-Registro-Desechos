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
import com.caircb.rcbtracegadere.models.ItemManifiestoDetalleValorSede;
import com.caircb.rcbtracegadere.models.ItemManifiestoPendiente;

import java.util.ArrayList;
import java.util.List;

public class ManifiestoPendienteSedeAdapter extends RecyclerView.Adapter<ManifiestoPendienteSedeAdapter.MyViewHolder> {

    private Context mContext;
    private List<ItemManifiestoPendiente> manifiestosDtList;

    public ManifiestoPendienteSedeAdapter(Context context){
        this.mContext = context;
        this.manifiestosDtList = new ArrayList<>();

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.lista_items_manifiestos_sede,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ItemManifiestoPendiente it = manifiestosDtList.get(position);

        holder.txtItemManifiesto.setText(it.getNumeroManifiesto());
        holder.txtItemBultoTotal.setText(String.valueOf(it.getTotBultos()));
        holder.txtItemCantidadBulto.setText(String.valueOf(it.getTotPendientes()));
        holder.chkEstadoItemDetalle.setChecked(it.getEstadoCheck());

        holder.chkEstadoItemDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    v.setSelected(true);
                    it.setEstadoCheck(true);
                }else{
                    v.setSelected(false);
                    it.setEstadoCheck(false);
                }
                MyApp.getDBO().manifiestoSedePlantaDao().updateCheck(it.getIdManifiesto(),it.getEstadoCheck());
            }
        });

    }

    @Override
    public int getItemCount() {
        return manifiestosDtList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtItemManifiesto;
        TextView txtItemBultoTotal;
        TextView txtItemCantidadBulto;
        CheckBox chkEstadoItemDetalle;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtItemManifiesto = itemView.findViewById(R.id.txtItemManifiesto);
            txtItemBultoTotal = itemView.findViewById(R.id.txtItemBultoTotal);
            txtItemCantidadBulto = itemView.findViewById(R.id.txtItemCantidadBulto);
            chkEstadoItemDetalle = itemView.findViewById(R.id.chkEstadoItemDetalle);
        }
    }

    public void setTaskList(List<ItemManifiestoPendiente> taskList) {
        this.manifiestosDtList = taskList;
        notifyDataSetChanged();
    }

}

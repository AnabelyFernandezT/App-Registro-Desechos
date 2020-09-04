package com.caircb.rcbtracegadere.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.models.RowItemManifiestosDetalleGestores;

import java.util.ArrayList;
import java.util.List;

public class ManifiestoHijoGestorAdapter extends RecyclerView.Adapter<ManifiestoHijoGestorAdapter.MyViewHolder>  {

    private Context mContext;
    private ClickListener mClickListener;
    private List<RowItemManifiestosDetalleGestores> manifiestosDtList;
    private Integer idManifiestoDetalle, idTipoDesecho, idManifiesto;
    private Double cantidadBultos=0.0, pesoBulto=0.0;

    public interface OnRegistrarBultoListener {
        public void onSuccesfull(String numeroBultos, String pesoBulto);
    }

    private OnRegistrarBultoListener mOnRegistrarBultoListener;

    public ManifiestoHijoGestorAdapter(Context context, List<RowItemManifiestosDetalleGestores> listaDetalle, Integer idManifiestoDetalle, Integer idTipoDesecho, Integer idManifiesto) {
        this.mContext = context;
        this.manifiestosDtList = listaDetalle;
        this.idManifiestoDetalle = idManifiestoDetalle;
        this.idTipoDesecho = idTipoDesecho;
        this.idManifiesto = idManifiesto;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.lista_items_manifiesto_gestores,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManifiestoHijoGestorAdapter.MyViewHolder holder, int position) {
        final RowItemManifiestosDetalleGestores it = manifiestosDtList.get(position);
        holder.cliente.setText(it.getCliente());
        holder.numeroManifiesto.setText(it.getNumeroManifiestoHijo());
        holder.numBultos.setText(""+it.getBultos());
        holder.peso.setText(""+it.getPeso());
        holder.checkSeleccion.setChecked(it.getCheckHijo());

        holder.checkSeleccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox) v).isChecked()){
                    v.setSelected(true);
                    it.setCheckHijo(true);
                }else {
                    v.setSelected(false);
                    it.setCheckHijo(false);
                }
                MyApp.getDBO().lotePadreDao().updateCheck(it.getIdManifiestoDetalleHijo(),it.getCheckHijo());
            }
        });
    }

    @Override
    public int getItemCount() {
        return manifiestosDtList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView peso,numBultos,numeroManifiesto,cliente;
        CheckBox checkSeleccion;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            peso = itemView.findViewById(R.id.txtItemPeso);
            numBultos = itemView.findViewById(R.id.txtItemCantidadBulto);
            numeroManifiesto = itemView.findViewById(R.id.txtItemNumManifiesto);
            cliente = itemView.findViewById(R.id.txtItemCliente);
            checkSeleccion = itemView.findViewById(R.id.chkEstadoItemDetalle);
        }
    }

    public void setTaskList(List<RowItemManifiestosDetalleGestores> taskList) {
        this.manifiestosDtList = taskList;
        notifyDataSetChanged();
    }


    public void setOnItemClickListener(@NonNull OnRegistrarBultoListener l) {
        mOnRegistrarBultoListener= l;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }


}

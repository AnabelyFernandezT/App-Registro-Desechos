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

import com.caircb.rcbtracegadere.database.entity.ManifiestoDetalleEntity;
import com.caircb.rcbtracegadere.models.RowItemManifiestosDetalleGestores;

import java.util.ArrayList;
import java.util.List;

public class ManifiestoHijoGestorAdapter extends ArrayAdapter<RowItemManifiestosDetalleGestores> {

    private Context mContext;
    private ClickListener mClickListener;
    private List<RowItemManifiestosDetalleGestores> manifiestosDtList;
    private Integer idManifiestoDetalle, idTipoDesecho, idManifiesto;

    public ManifiestoHijoGestorAdapter(Context context, List<RowItemManifiestosDetalleGestores> listaDetalle, Integer idManifiestoDetalle, Integer idTipoDesecho, Integer idManifiesto) {
        super(context,R.layout.lista_items_manifiesto_gestores,listaDetalle);
        this.mContext = context;
        this.manifiestosDtList = listaDetalle;
        this.idManifiestoDetalle = idManifiestoDetalle;
        this.idTipoDesecho = idTipoDesecho;
        this.idManifiesto = idManifiesto;
    }


    public void setTaskList(List<RowItemManifiestosDetalleGestores> taskList) {
        this.manifiestosDtList = taskList;
        notifyDataSetChanged();
    }

    public class ViewHolder {

        TextView peso,numBultos,numeroManifiesto,cliente;
        CheckBox checkSeleccion;
        }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ManifiestoHijoGestorAdapter.ViewHolder holder = null;
        manifiestosDtList = MyApp.getDBO().manifiestoDao().manifiestosHijos(idTipoDesecho,idManifiesto);
        final RowItemManifiestosDetalleGestores row = manifiestosDtList.get(position);
        LayoutInflater minflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = minflater.inflate(R.layout.lista_items_manifiesto_gestores,null);
        holder = new ManifiestoHijoGestorAdapter.ViewHolder();
            holder.peso = convertView.findViewById(R.id.txtItemPeso);
            holder.numBultos = convertView.findViewById(R.id.txtItemCantidadBulto);
            holder.numeroManifiesto = convertView.findViewById(R.id.txtItemNumManifiesto);
            holder.cliente = convertView.findViewById(R.id.txtItemCliente);
            holder.checkSeleccion = convertView.findViewById(R.id.chkEstadoItemDetalle);

            if(manifiestosDtList.get(position).getPeso()==null){
                holder.peso.setText("0.0");
            }else {
                holder.peso.setText(manifiestosDtList.get(position).getPeso().toString());
            }

            if(manifiestosDtList.get(position).getCantidadBulto()==null){
                holder.numBultos.setText("0");
            }else{
                holder.numBultos.setText(manifiestosDtList.get(position).getCantidadBulto().toString());
            }
            holder.numeroManifiesto.setText(manifiestosDtList.get(position).getNumeroManifiesto());
            holder.cliente.setText(manifiestosDtList.get(position).getCliente());

        return convertView;
    }

    public void setOnItemClickListener(@NonNull ClickListener l) {
        mClickListener= l;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }
}

package com.caircb.rcbtracegadere.adapters;

import android.content.Context;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.models.ItemManifiestoDetalleValorSede;

import java.util.ArrayList;
import java.util.List;

public class ManifiestoDetalleBultosAdapterPlanta extends RecyclerView.Adapter<ManifiestoDetalleBultosAdapterPlanta.MyViewHolder>  {

    private ClickListener mClickListener;
    private Context mContext;
    private List<ItemManifiestoDetalleValorSede> manifiestosDtList;
    private Integer estadoManifiesto,idManifiestoDetalle;

    public ManifiestoDetalleBultosAdapterPlanta(Context context, Integer idManifiestoDetalle, Integer estadoManifiesto){
        this.mContext = context;
        this.manifiestosDtList = new ArrayList<>();
        this.idManifiestoDetalle=idManifiestoDetalle;
        this.estadoManifiesto =estadoManifiesto;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.lista_items_manifiesto_bultos_planta,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull MyViewHolder holder, int position) {
        final ItemManifiestoDetalleValorSede it = manifiestosDtList.get(position);
        holder.txtPeso.setText(it.getPeso().toString());
        holder.txtNombre.setText(it.getNombreBulto());
        holder.chkEstado.setChecked(it.getEstado());
        holder.txtNuevoPeso.setText(it.getNuevoPeso()==null ? "":it.getNuevoPeso().toString());

        holder.txtNuevoPeso.setFocusable(true);
        holder.txtNuevoPeso.setFocusableInTouchMode(true);
        holder.txtNuevoPeso.requestFocus();

          holder.chkEstado.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  String nuevoValor = holder.txtNuevoPeso.getText().toString() == "" ? "0": holder.txtNuevoPeso.getText().toString();
                  Integer valida = 0;
                  if(nuevoValor.equals("")){
                      valida = 0;
                      holder.chkEstado.setChecked(false);
                  }else{
                      valida=1;
                  }

                  if(valida.equals(1) ) {
                  if (((CheckBox) v).isChecked()) {
                      v.setSelected(true);
                      it.setEstado(true);
                  } else {
                      v.setSelected(false);
                      it.setEstado(false);
                  }
                  MyApp.getDBO().manifiestoPlantaDetalleValorDao().updateManifiestoDetalleValorPlantaPesoNuevo(it.getIdManifiestoDetalle(), (holder.txtNuevoPeso.getText().toString()), it.getIdManifiestoDetalleValores());
                  MyApp.getDBO().manifiestoPlantaDetalleValorDao().updateManifiestoDetalleValorSedebyId(it.getIdManifiestoDetalle(), it.getEstado(), it.getIdManifiestoDetalleValores());
              }
              }
          });
      }


    @Override
    public int getItemCount() {
        return manifiestosDtList.size();
    }

    public void setTaskList(List<ItemManifiestoDetalleValorSede> taskList) {
        this.manifiestosDtList = taskList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtNombre;
        TextView txtPeso;
        CheckBox chkEstado;
        EditText txtNuevoPeso;
        public MyViewHolder(View itemView) {
            super(itemView);

            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtPeso = itemView.findViewById(R.id.txtBultos);
            chkEstado = itemView.findViewById(R.id.chkEstadoItemDetalle);
            txtNuevoPeso = (EditText)itemView.findViewById(R.id.txtIgnBultos);
            txtNuevoPeso.requestFocus();
            txtNuevoPeso.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){

                    }
                }
            });

            itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){

                    }
                }
            });

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

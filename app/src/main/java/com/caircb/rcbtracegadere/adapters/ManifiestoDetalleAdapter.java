package com.caircb.rcbtracegadere.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.AppDatabase;
import com.caircb.rcbtracegadere.dialogs.DialogNotificacionPesoExtra;
import com.caircb.rcbtracegadere.models.ItemManifiesto;
import com.caircb.rcbtracegadere.models.RowItemManifiesto;
import com.itextpdf.text.pdf.parser.Line;

import java.util.ArrayList;
import java.util.List;

public class ManifiestoDetalleAdapter extends RecyclerView.Adapter<ManifiestoDetalleAdapter.MyViewHolder>  {

    private ClickListener mClickListener;
    private Context mContext;
    private List<RowItemManifiesto> manifiestosDtList;
    private String numeroManifiesto;
    private Integer estadoManifiesto,idAppManifiesto;
    private Integer tipoProceso;

    public ManifiestoDetalleAdapter(Context context,String numeroManifiesto,Integer estadoManifiesto, Integer idAppManifiesto, Integer tipoProceso){
        this.mContext = context;
        this.manifiestosDtList = new ArrayList<>();
        this.numeroManifiesto=numeroManifiesto;
        this.estadoManifiesto =estadoManifiesto;
        this.idAppManifiesto = idAppManifiesto;
        this.tipoProceso=tipoProceso;
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
        //holder.txtUnidad.setText(it.getUnidad());

        if (it.getTipoMostrar()==3){
            holder.lnlManifiestoDetalle.setEnabled(false);
            holder.chkEstado.setEnabled(false);
        }else {
            holder.lnlManifiestoDetalle.setEnabled(true);
            holder.chkEstado.setEnabled(true);
        }
        holder.txtUnidad.setText("KG");
        holder.txtPeso.setText(""+it.getPeso());
        holder.txtCantidadBulto.setText(""+it.getCantidadBulto());
        holder.txtDescripcion.setText(it.getCodigoMAE()+"-"+ it.getDescripcion());
        holder.txtTratamiento.setText(it.getTratamiento());
        holder.chkEstado.setChecked(it.isEstado());
        holder.txtTipoBalanza.setText(it.getTipoBalanza()== 0 ? "" : it.getTipoBalanza()== 1 ? "Gadere" :"Cliente");
        holder.txtPesoReferencial.setText(String.valueOf(it.getPesoReferencial()));

        if(it.isFaltaImpresiones()){
            holder.imgFaltaImpresiones.setVisibility(View.VISIBLE);
        }else{
            holder.imgFaltaImpresiones.setVisibility(View.GONE);
        }

        if (tipoProceso==1){
            if(holder.txtPeso.getText().toString().equals("0.0")){
                holder.chkEstado.setChecked(false);
            }
        }else {
            holder.chkEstado.setChecked(it.isEstado());
        }



        if(estadoManifiesto !=1) {
            holder.chkEstado.setClickable(false);
        }
        if(estadoManifiesto ==1) {
            holder.chkEstado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((CheckBox) v).isChecked()) {
                        v.setSelected(true);
                        it.setEstado(true);
                    } else {
                        v.setSelected(false);
                        it.setEstado(false);
                    }
                    MyApp.getDBO().manifiestoDetalleDao().updateManifiestoDetallebyId(it.getId(), it.isEstado(), it.isEstado() ? AppDatabase.getUUID(numeroManifiesto) : "");
                }
            });
        }

        /*if(Double.parseDouble(holder.txtPesoReferencial.getText().toString()) > Double.parseDouble(holder.txtPeso.getText().toString())){
            holder.btnNotificador.setVisibility(View.GONE);
        }else {
            holder.btnNotificador.setVisibility(View.VISIBLE);
        }
        holder.btnNotificador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogNotificacionPesoExtra dialogMensajes = new DialogNotificacionPesoExtra(mContext,it.getId(), idAppManifiesto,
                                                                                            it.getPeso());
                dialogMensajes.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogMensajes.setCancelable(false);
                dialogMensajes.show();
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
        TextView txtTipoBalanza;
        TextView txtPesoReferencial;
        ImageView imgFaltaImpresiones;
        RelativeLayout lnlManifiestoDetalle;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtUnidad = itemView.findViewById(R.id.txtItemUnidad);
            txtPeso = itemView.findViewById(R.id.txtItemPeso);
            txtCantidadBulto = itemView.findViewById(R.id.txtItemCantidadBulto);
            txtDescripcion = itemView.findViewById(R.id.txtItemDescripcion);
            txtTratamiento = itemView.findViewById(R.id.txtItemTratamiento);
            btnEleminarItem = itemView.findViewById(R.id.btnEleminarItem);
            chkEstado = itemView.findViewById(R.id.chkEstadoItemDetalle);
            txtTipoBalanza = itemView.findViewById(R.id.txtTipoBalanza);
            txtPesoReferencial = itemView.findViewById(R.id.txtPesoReferencial);
            imgFaltaImpresiones = itemView.findViewById(R.id.imgFaltaImpresiones);
            lnlManifiestoDetalle = itemView.findViewById(R.id.lnlManifiestoDetalle);

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

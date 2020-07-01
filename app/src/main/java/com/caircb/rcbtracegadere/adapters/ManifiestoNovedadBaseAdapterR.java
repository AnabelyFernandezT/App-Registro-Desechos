package com.caircb.rcbtracegadere.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.models.RowItemHojaRutaCatalogo;

import java.util.List;

public class ManifiestoNovedadBaseAdapterR extends RecyclerView.Adapter<ManifiestoNovedadBaseAdapterR.MyViewHolder> {

    private Context mContext;
    private boolean desactivaComp;
    List<RowItemHojaRutaCatalogo> listItems;
    //private Integer tipoUsuario ;
    Integer idManifiesto,estadoManifiesto;
    DialogBuilder builder;
    boolean resp = false;

    public interface OnClickOpenFotografias {
        public void onShow(Integer catalogoID, Integer position);
    }
    public interface OnReloadAdater{
        public void onShowM(Integer catalogoID, Integer position);
    }

    private ManifiestoNovedadBaseAdapterR.OnClickOpenFotografias mOnClickOpenFotografias;
    private ManifiestoNovedadBaseAdapterR.OnReloadAdater mOnReloadAdaptador;

    public ManifiestoNovedadBaseAdapterR(Context context,List<RowItemHojaRutaCatalogo> items,
                                         boolean desactivarComp,Integer idManifiesto, Integer estadoManifiesto){
        mContext = context;
        listItems = items;
        this.desactivaComp = desactivarComp;
        //this.tipoUsuario = tipoUusario;
        this.estadoManifiesto= estadoManifiesto;
        this.idManifiesto = idManifiesto;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_hoja_ruta_novedad,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull MyViewHolder holder, final int position) {
        final RowItemHojaRutaCatalogo item = listItems.get(position);

        holder.txtCatalogo.setText(item.getCatalogo());
        holder.lnlBadge.setVisibility(item.getNumFotos() > 0? View.VISIBLE : View.GONE);
        holder.txtCountPhoto.setText(""+item.getNumFotos());

        holder.chkEstado.setChecked(item.isEstadoChek());
        if(desactivaComp == true){
            holder.chkEstado.setEnabled(false);
        }
        if(estadoManifiesto !=1) {
        holder.chkEstado.setClickable(false);
        }

        if(estadoManifiesto == 1) {
            holder.chkEstado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getNumFotos() > 0) {
                        if (mOnReloadAdaptador != null)
                            mOnReloadAdaptador.onShowM(item.getId(), position);
                    } else {
                        if (((CheckBox) v).isChecked()) {
                            v.setSelected(true);
                            item.setEstadoChek(true);
                            registarCheckItemCatalogo(idManifiesto, item.getId(), true);
                        } else {
                            v.setSelected(false);
                            item.setEstadoChek(false);
                            registarCheckItemCatalogo(idManifiesto, item.getId(), false);
                        }
                    }

                }
            });

            holder.btnEvidenciaNovedadFrecuente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnClickOpenFotografias != null)
                        mOnClickOpenFotografias.onShow(item.getId(), position);
                }
            });
        }
    }

    @Override
    public int getItemCount (){return  listItems.size();}
    public void setTaskList(List<RowItemHojaRutaCatalogo> taskList){
        this.listItems = taskList;
        notifyDataSetChanged();
    }

    public void registarCheckItemCatalogo(Integer idManifiesto,Integer idCatalogo, boolean check){
        //dbHelper.open();
        ///
        //if (tipoUsuario.equals(1)){
        //    MyApp.getDBO().manifiestoObservacionFrecuenteDao().updateManifiestoObservacionRecepcionbyId(idManifiesto,id,check);
        //}else{
            MyApp.getDBO().manifiestoObservacionFrecuenteDao().saveOrUpdateManifiestoNovedadFrecuente(idManifiesto,idCatalogo,check);
        //}
        //dbHelper.close();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtCatalogo;
        CheckBox chkEstado;
        LinearLayout lnlBadge;
        TextView txtCountPhoto;
        LinearLayout btnEvidenciaNovedadFrecuente;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtCatalogo = itemView.findViewById(R.id.itm_observaciones);
            chkEstado = itemView.findViewById(R.id.chkEstado);
            lnlBadge = itemView.findViewById(R.id.lnlCountPhoto);
            txtCountPhoto = itemView.findViewById(R.id.txtCountPhoto);
            btnEvidenciaNovedadFrecuente = itemView.findViewById(R.id.btnEvidenciaNovedadFrecuente);
        }
    }

    public void setOnClickOpenFotografias(@Nullable OnClickOpenFotografias l){
        mOnClickOpenFotografias = l;
    }
    public void setOnClickReaload(@Nullable OnReloadAdater l){
        mOnReloadAdaptador = l;
    }

    public void deleteFotosByItem (final Integer idManifiesto, final Integer idItem, final Integer position){
        //MyApp.getDBO().manifiestoFileDao().deleteFotoByIdAppManifistoCatalogo(idManifiesto, idItem);
    }

}

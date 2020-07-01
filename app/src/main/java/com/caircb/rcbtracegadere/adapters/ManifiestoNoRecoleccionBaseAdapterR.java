package com.caircb.rcbtracegadere.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.models.RowItemHojaRutaCatalogo;
import com.caircb.rcbtracegadere.models.RowItemNoRecoleccion;

import java.util.List;

public class ManifiestoNoRecoleccionBaseAdapterR extends RecyclerView.Adapter<ManifiestoNoRecoleccionBaseAdapterR.MyViewHolder> {

    private Context mContext;
    List<RowItemNoRecoleccion> listItems;
    Integer idAppManifiseto;
    boolean desactivarComp;

    public interface OnClickOpenFotografias {
        public void onShow(Integer catalogoID, Integer cantidad);
    }
    public interface OnReloadAdater{
        public void onShowM(Integer catalogoID, Integer position);
    }

    private OnClickOpenFotografias mOnClickOpenFotografias;
    private ManifiestoNovedadBaseAdapterR.OnReloadAdater mOnReloadAdaptador;

    public ManifiestoNoRecoleccionBaseAdapterR(Context context, List<RowItemNoRecoleccion> items, Integer idAppManifiesto, boolean desactivarComp){
        mContext = context;
        listItems = items;
        this.desactivarComp = desactivarComp;
        this.idAppManifiseto = idAppManifiesto;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_hoja_ruta_novedad,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull MyViewHolder holder, final int position) {
        final RowItemNoRecoleccion item = listItems.get(position);

        holder.txtCatalogo.setText(item.getCatalogo());
        holder.lnlBadge.setVisibility(item.getNumFotos() > 0? View.VISIBLE : View.GONE);
        holder.txtCountPhoto.setText(""+item.getNumFotos());

        holder.chkEstado.setChecked(item.isEstadoChek());
        if(desactivarComp == true){
            holder.chkEstado.setEnabled(false);
        }

        holder.chkEstado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.getNumFotos() >0){
                    if(mOnReloadAdaptador!=null)mOnReloadAdaptador.onShowM(item.getId(),position);
                }else{
                    if(((CheckBox)v).isChecked()){
                        v.setSelected(true);
                        item.setEstadoChek(true);
                        registarCheckObservacion(idAppManifiseto, item.getId(),true);
                    }else{
                        v.setSelected(false);
                        item.setEstadoChek(false);
                        registarCheckObservacion(idAppManifiseto, item.getId(),false);
                    }
                }
                /*
                if(((CheckBox)v).isChecked()){
                    v.setSelected(true);
                    item.setEstadoChek(true);
                    registarCheckObservacion(item.getId(),true);
                }else{
                    v.setSelected(true);
                    item.setEstadoChek(false);
                    registarCheckObservacion(item.getId(),false);
                }
                */
            }
        });

        holder.btnEvidenciaNovedadFrecuente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnClickOpenFotografias!=null)mOnClickOpenFotografias.onShow(item.getId(),position);
            }
        });
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtCatalogo;
        CheckBox chkEstado;
        TextView txtCountPhoto;
        LinearLayout btnEvidenciaNovedadFrecuente;
        LinearLayout lnlBadge;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtCatalogo = itemView.findViewById(R.id.itm_observaciones);
            chkEstado = itemView.findViewById(R.id.chkEstado);
            lnlBadge = itemView.findViewById(R.id.lnlCountPhoto);
            txtCountPhoto = itemView.findViewById(R.id.txtCountPhoto);
            btnEvidenciaNovedadFrecuente = itemView.findViewById(R.id.btnEvidenciaNovedadFrecuente);
        }
    }

    @Override
    public int getItemCount (){return  listItems.size();}

    public void registarCheckObservacion(Integer idAppManifiseto,Integer id, boolean check){
        //dbHelper.open();
        MyApp.getDBO().manifiestoMotivosNoRecoleccionDao().saveOrUpdate(idAppManifiseto,id,check);
        //dbHelper.close();
    }
    public void setOnClickOpenFotografias(@Nullable OnClickOpenFotografias l){
        mOnClickOpenFotografias = l;
    }
    public void setOnClickReaload(@Nullable ManifiestoNovedadBaseAdapterR.OnReloadAdater l){
        mOnReloadAdaptador = l;
    }
    public void deleteFotosByItem (final Integer idManifiesto, final Integer idItem, final Integer position){
        //MyApp.getDBO().manifiestoFileDao().deleteFotoByIdAppManifistoCatalogo(idManifiesto, idItem);
    }
}

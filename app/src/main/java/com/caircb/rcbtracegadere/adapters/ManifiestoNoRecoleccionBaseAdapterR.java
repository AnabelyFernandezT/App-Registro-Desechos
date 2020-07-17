package com.caircb.rcbtracegadere.adapters;

import android.app.AlertDialog;
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
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.models.RowItemHojaRutaCatalogo;
import com.caircb.rcbtracegadere.models.RowItemNoRecoleccion;

import java.util.List;

public class ManifiestoNoRecoleccionBaseAdapterR extends RecyclerView.Adapter<ManifiestoNoRecoleccionBaseAdapterR.MyViewHolder> {

    private Context mContext;
    List<RowItemNoRecoleccion> listItems;
    Integer idAppManifiseto,estadoManifiesto;
    boolean desactivarComp;
    AlertDialog.Builder messageBox;

    public interface OnClickOpenFotografias {
        public void onShow(Integer catalogoID, Integer cantidad);
    }
    public interface OnReloadAdater{
        public void onShowM(Integer catalogoID, Integer position);
    }

    private OnClickOpenFotografias mOnClickOpenFotografias;
    private ManifiestoNovedadBaseAdapterR.OnReloadAdater mOnReloadAdaptador;

    public ManifiestoNoRecoleccionBaseAdapterR(Context context, List<RowItemNoRecoleccion> items, Integer idAppManifiesto, boolean desactivarComp, Integer estadoManifiesto){
        mContext = context;
        listItems = items;
        this.desactivarComp = desactivarComp;
        this.estadoManifiesto = estadoManifiesto;
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

        if(item.isEstadoChek()){
            holder.btnEvidenciaNovedadFrecuente.setVisibility(View.VISIBLE);
        }else{
            holder.btnEvidenciaNovedadFrecuente.setVisibility(View.INVISIBLE);
        }

        if(estadoManifiesto !=1) {
            holder.chkEstado.setClickable(false);
        }

        if(desactivarComp == true){
            holder.chkEstado.setEnabled(false);
        }
        if(estadoManifiesto ==1) {
            holder.chkEstado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getNumFotos() > 0) {
                        if (mOnReloadAdaptador != null)
                            mOnReloadAdaptador.onShowM(item.getId(), position);
                    } else {
                        if (((CheckBox) v).isChecked()) {
                            Integer estadoCheck;
                            estadoCheck = MyApp.getDBO().manifiestoMotivosNoRecoleccionDao().fetchHojaRutaMotivoNoRecoleccionEstado(idAppManifiseto);
                            if(estadoCheck<=0){
                                v.setSelected(true);
                                item.setEstadoChek(true);
                                holder.btnEvidenciaNovedadFrecuente.setVisibility(View.VISIBLE);
                                registarCheckObservacion(idAppManifiseto, item.getId(), true);
                            }else{
                                holder.chkEstado.setChecked(false);
                                DialogBuilder dialogBuilder = new DialogBuilder(mContext);
                                dialogBuilder.setTitle("INFO");
                                dialogBuilder.setMessage("No puede ingresar dos motivos de NO RECOLECCIÓN!");
                                dialogBuilder.setCancelable(false);
                                dialogBuilder.setPositiveButton("OK", null);
                                dialogBuilder.show();

                            }
                        } else {
                            v.setSelected(false);
                            item.setEstadoChek(false);
                            holder.btnEvidenciaNovedadFrecuente.setVisibility(View.INVISIBLE);
                            registarCheckObservacion(idAppManifiseto, item.getId(), false);
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
        MyApp.getDBO().manifiestoFileDao().deleteFotoByIdAppManifistoCatalogo(idManifiesto, idItem);
    }

    private void validarNoRecolecion(){

    }
}

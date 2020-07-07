package com.caircb.rcbtracegadere.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.models.RowItemNoRecoleccion;

import java.util.List;

public class ManifiestoNoRecoleccionBaseAdapter extends ArrayAdapter<RowItemNoRecoleccion> {
    Context context;
    private boolean _noChek;
    private Integer idAppManifiseto;

    boolean desactivarComp;

    public interface OnClickOpenFotografias {
        public void onShow(Integer catalogoID, Integer cantidad);
    }
    private OnClickOpenFotografias mOnClickOpenFotografias;

    public ManifiestoNoRecoleccionBaseAdapter(Context context,
                                              List<RowItemNoRecoleccion> items,
                                              Integer idAppManifiesto,
                                              boolean desactivarComp) {
        super(context, R.layout.list_item_hoja_ruta_novedad, items);
        this.context = context;
        this._noChek=false;
        this.desactivarComp = desactivarComp;
        this.idAppManifiseto=idAppManifiesto;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView txtCatalogo;
        CheckBox chkEstado;
        TextView txtCountPhoto;
        LinearLayout btnEvidenciaNovedadFrecuente;
        LinearLayout lnlBadge;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ManifiestoNoRecoleccionBaseAdapter.ViewHolder holder = null;
        final RowItemNoRecoleccion row = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_hoja_ruta_novedad, null);
            holder = new ManifiestoNoRecoleccionBaseAdapter.ViewHolder();
            holder.txtCatalogo = (TextView) convertView.findViewById(R.id.itm_observaciones);
            holder.chkEstado = (CheckBox)convertView.findViewById(R.id.chkEstado);
            holder.lnlBadge = (LinearLayout)convertView.findViewById(R.id.lnlCountPhoto);
            holder.txtCountPhoto =(TextView)convertView.findViewById(R.id.txtCountPhoto);
            holder.btnEvidenciaNovedadFrecuente =(LinearLayout)convertView.findViewById(R.id.btnEvidenciaNovedadFrecuente);
            convertView.setTag(holder);
        } else
            holder = (ManifiestoNoRecoleccionBaseAdapter.ViewHolder) convertView.getTag();


        if(desactivarComp == true){
            holder.chkEstado.setEnabled(false);
        }

        holder.txtCatalogo.setText(row.getCatalogo());
        holder.chkEstado.setChecked(row.isEstadoChek());
        if(!this._noChek){
            holder.chkEstado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((CheckBox) v).isChecked()) {
                        v.setSelected(true);
                        row.setEstadoChek(true);
                        registarCheckObservacion(row.getId(),true);
                    } if (((CheckBox) v).isChecked()==false) {
                        v.setSelected(false);
                        row.setEstadoChek(false);
                        registarCheckObservacion(row.getId(),false);
                    }
                }
            });
        }

        holder.btnEvidenciaNovedadFrecuente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnClickOpenFotografias!=null)mOnClickOpenFotografias.onShow(row.getId(),position);
            }
        });

        //if(row.getNumFotos()>0){
            holder.lnlBadge.setVisibility(row.getNumFotos()>0? View.VISIBLE: View.GONE);
            holder.txtCountPhoto.setText(""+row.getNumFotos());
        //}

        return convertView;
    }

    private void registarCheckObservacion(Integer id, boolean check){
        //dbHelper.open();
        MyApp.getDBO().manifiestoMotivosNoRecoleccionDao().saveOrUpdate(idAppManifiseto,id,check);
        //dbHelper.close();
    }

    public void setOnClickOpenFotografias(@Nullable OnClickOpenFotografias l){
        mOnClickOpenFotografias = l;
    }
}

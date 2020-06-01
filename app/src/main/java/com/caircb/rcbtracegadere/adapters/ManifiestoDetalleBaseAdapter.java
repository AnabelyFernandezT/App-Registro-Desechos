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

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.models.RowItemManifiesto;

import java.util.List;

public class ManifiestoDetalleBaseAdapter extends ArrayAdapter<RowItemManifiesto> {

    Context context;
    boolean desactivaComp;
    //DBAdapter dbHelper;

    /*private view holder class*/
    private class ViewHolder {
        TextView txtUnidad;
        TextView txtPeso;
        TextView txtCantidadBulto;
        TextView txtDescripcion;
        TextView txtTratamiento;
        LinearLayout btnEleminarItem;
        CheckBox chkEstado;
    }

    public ManifiestoDetalleBaseAdapter(Context context, List<RowItemManifiesto> items, boolean desactivaComp) {
        super(context, R.layout.lista_items_manifiesto,items);
        this.context=context;
        this.desactivaComp = desactivaComp;
        //this.dbHelper = new DBAdapter(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ManifiestoDetalleBaseAdapter.ViewHolder  holder =null;
        final RowItemManifiesto row = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.lista_items_manifiesto, null);
            holder = new ManifiestoDetalleBaseAdapter.ViewHolder();

            holder.txtUnidad = (TextView)convertView.findViewById(R.id.txtItemUnidad);
            holder.txtPeso = (TextView)convertView.findViewById(R.id.txtItemPeso);
            holder.txtCantidadBulto = (TextView)convertView.findViewById(R.id.txtItemCantidadBulto);
            holder.txtDescripcion=(TextView)convertView.findViewById(R.id.txtItemDescripcion);
            holder.btnEleminarItem =(LinearLayout)convertView.findViewById(R.id.btnEleminarItem);
            holder.chkEstado =(CheckBox)convertView.findViewById(R.id.chkEstadoItemDetalle);
            holder.txtTratamiento = (TextView)convertView.findViewById(R.id.txtItemTratamiento);

            convertView.setTag(holder);
        } else
            holder = (ManifiestoDetalleBaseAdapter.ViewHolder) convertView.getTag();

        if(desactivaComp == true){
            holder.chkEstado.setEnabled(false);
        }

        holder.txtUnidad.setText(row.getUnidad());
        holder.txtPeso.setText(""+row.getPeso());
        holder.txtCantidadBulto.setText(""+row.getCantidadBulto());
        holder.txtDescripcion.setText(row.getDescripcion());
        holder.txtTratamiento.setText(row.getTratamiento());

        holder.chkEstado.setChecked(row.isEstado());
        holder.chkEstado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    v.setSelected(true);
                    row.setEstado(true);
                    //dbHelper.open();
                    MyApp.getDBO().manifiestoDetalleDao().updateManifiestoDetallebyId(row.getId(),true);
                    //dbHelper.close();
                    /*if(row.getCantidadBulto()==0){

                        //dbHelper.open();
                        MyApp.getDBO().manifiestoDetalleDao().updateCantidadBultoManifiestoDetalle(row.getId(),1);
                        //dbHelper.close();

                        row.setCantidadBulto(1);

                    }*/
                    notifyDataSetChanged();

                }else{
                    v.setSelected(false);
                    row.setEstado(false);
                    //dbHelper.open();
                    MyApp.getDBO().manifiestoDetalleDao().updateManifiestoDetallebyId(row.getId(),false);
                    //dbHelper.close();
                    /*if(row.getCantidadBulto()>0){

                        //dbHelper.open();
                        MyApp.getDBO().manifiestoDetalleDao().updateCantidadBultoManifiestoDetalle(row.getId(),0);
                        //dbHelper.close();

                        row.setCantidadBulto(0);

                    }*/
                    notifyDataSetChanged();
                }
            }
        });
        /*
        holder.btnEleminarItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mensaje de confirmacion...
                remove(row);
            }
        });
        */

        return convertView;
    }
}

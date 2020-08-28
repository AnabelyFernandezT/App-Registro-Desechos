package com.caircb.rcbtracegadere.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.dialogs.DialogInformacionTransportista;
import com.caircb.rcbtracegadere.models.ItemLotePadre;
import com.caircb.rcbtracegadere.models.ItemManifiesto;

import java.util.ArrayList;
import java.util.List;

public class ManifiestoGestorAdapter extends RecyclerView.Adapter<ManifiestoGestorAdapter.MyViewHolder>  {

    private Context mContext;
    private List<ItemManifiesto> manifiestosList ;

    public ManifiestoGestorAdapter(Context context){
        mContext = context;
        manifiestosList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_manifiesto_new,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull MyViewHolder holder, int position) {
        final ItemManifiesto it = manifiestosList.get(position);
        /*holder.txtNumManifiestoPadre.setText(it.getNumeroManifiestoPadre());
        holder.txtManifiestosHijos.setText(it.getManifiestos());
        holder.txtTotal.setText(it.getTotal().toString());
        holder.txtNombreCliente.setText(it.getNombreCliente());*/
        holder.txtNumManifiesto.setText(it.getNumero());
        holder.txtCliente.setText(it.getCliente());
        holder.txtSucursal.setText(it.getSucursal());
        holder.txtDireccion.setText(it.getDireccion());
        holder.txtProvincia.setText(it.getProvincia());
        holder.txtCiudad.setText(it.getCanton());
        holder.txtReferencia.setText(it.getReferencia());
        String estadoString = "";
        switch (it.getEstado()){
            case 1 :
                estadoString ="ASIGNADO";
                break;
            case 2 :
                estadoString ="RECOLECTADO";
                break;
            case 3:
                estadoString ="NO RECOLECTADO";
                break;
        }

        holder.txtEstado.setText(estadoString.toString());

    }

    @Override
    public int getItemCount() {
        return manifiestosList.size();
    }

    public void setTaskList(List<ItemManifiesto> taskList) {
        this.manifiestosList = taskList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtNumManifiesto;
        TextView txtCliente;
        TextView txtSucursal;
        TextView txtDireccion;
        TextView txtProvincia;
        TextView txtCiudad;
        TextView txtEstado;
        TextView txtReferencia;
        LinearLayout btnInfoCardTransporte;
        LinearLayout btnViewPdfManifiesto;

        public MyViewHolder(final View itemView) {
            super(itemView);
            txtNumManifiesto = itemView.findViewById(R.id.itm_num_manifiesto);
            txtCliente = itemView.findViewById(R.id.itm_cliente);
            txtSucursal = itemView.findViewById(R.id.itm_sucursal);
            txtDireccion = itemView.findViewById(R.id.itm_Direccion);
            txtProvincia = itemView.findViewById(R.id.itm_Provincia);
            txtCiudad = itemView.findViewById(R.id.itm_Ciudad);
            txtEstado = itemView.findViewById(R.id.itm_Estado);
            txtReferencia = itemView.findViewById(R.id.itm_Referencia);
            btnInfoCardTransporte = itemView.findViewById(R.id.btnInfoCardTransporte);
            btnViewPdfManifiesto = itemView.findViewById(R.id.btnViewPdfManifiesto);

           /* if (moduloSeleccionado==2){
                btnInfoCardTransporte.setVisibility(View.GONE);
            }else {
                btnInfoCardTransporte.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int positionSelected= ManifiestoAdapter.MyViewHolder.this.getPosition();
                        ItemManifiesto it = manifiestosList.get(positionSelected);
                        DialogInformacionTransportista dialogInformacionTransportista = new DialogInformacionTransportista(mContext,
                                it.getApertura1(),it.getApertura2(),it.getCierre1(),it.getCierre2(),it.getTelefono(),
                                it.getIdAppManifiesto(),it.getFrecuencia(),it.getReferencia());
                        dialogInformacionTransportista.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        //informacionModulosTaskl = new UserInformacionModulosTask(mContext, dialogInformacionTransportista);
                        //informacionModulosTaskl.execute();
                        dialogInformacionTransportista.show();
                    }
                });
            }

            if(modProcesada == 1){
                btnViewPdfManifiesto.setVisibility(View.GONE);
                btnViewPdfManifiesto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //setNavegate(VisorManifiestoFragment.newInstance(rowItems.get(position).getIdAppManifiesto()));
                        if(mOnViewManifiestPdfListenner!=null){
                            int positionSelected= ManifiestoAdapter.MyViewHolder.this.getPosition();
                            ItemManifiesto it = manifiestosList.get(positionSelected);
                            mOnViewManifiestPdfListenner.onSusscessfull(it.getIdAppManifiesto());
                        }
                    }
                });
            }else{
                btnViewPdfManifiesto.setVisibility(View.GONE);
            }*/
        }
    }
}

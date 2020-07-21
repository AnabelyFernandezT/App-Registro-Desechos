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
import com.caircb.rcbtracegadere.dialogs.DialogInicioRuta;
import com.caircb.rcbtracegadere.fragments.recolector.MotivoNoRecoleccion.ManifiestoNoRecoleccionFragment;
import com.caircb.rcbtracegadere.fragments.recolector.manifiesto2.Manifiesto2FragmentProcesada;
import com.caircb.rcbtracegadere.models.ItemManifiesto;
import com.caircb.rcbtracegadere.tasks.UserInformacionModulosTask;

import java.util.ArrayList;
import java.util.List;

public class ManifiestoAdapter extends RecyclerView.Adapter<ManifiestoAdapter.MyViewHolder>  {

    private Context mContext;
    private List<ItemManifiesto> manifiestosList ;
    UserInformacionModulosTask informacionModulosTaskl;

    public ManifiestoAdapter(Context context){
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
        holder.txtNumManifiesto.setText(it.getNumero());
        holder.txtCliente.setText(it.getCliente());
        holder.txtSucursal.setText(it.getCanton());
        holder.txtDireccion.setText(it.getDireccion());
        holder.txtProvincia.setText(it.getProvincia());
        holder.txtCiudad.setText(it.getCanton());
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
        LinearLayout btnInfoCardTransporte;

        public MyViewHolder(final View itemView) {
            super(itemView);
            txtNumManifiesto = itemView.findViewById(R.id.itm_num_manifiesto);
            txtCliente = itemView.findViewById(R.id.itm_cliente);
            txtSucursal = itemView.findViewById(R.id.itm_sucursal);
            txtDireccion = itemView.findViewById(R.id.itm_Direccion);
            txtProvincia = itemView.findViewById(R.id.itm_Provincia);
            txtCiudad = itemView.findViewById(R.id.itm_Ciudad);
            txtEstado = itemView.findViewById(R.id.itm_Estado);
            btnInfoCardTransporte = itemView.findViewById(R.id.btnInfoCardTransporte);
            btnInfoCardTransporte.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogInformacionTransportista dialogInformacionTransportista = new DialogInformacionTransportista(mContext);
                    informacionModulosTaskl = new UserInformacionModulosTask(mContext, dialogInformacionTransportista);
                    informacionModulosTaskl.execute();
                }
            });
        }
    }
}

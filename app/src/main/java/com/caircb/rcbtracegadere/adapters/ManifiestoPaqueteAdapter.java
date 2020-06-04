package com.caircb.rcbtracegadere.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.entity.ManifiestoPaquetesEntity;
import com.caircb.rcbtracegadere.models.RowItemManifiesto;
import com.caircb.rcbtracegadere.models.RowItemPaquete;

import java.util.ArrayList;
import java.util.List;

public class ManifiestoPaqueteAdapter extends RecyclerView.Adapter<ManifiestoPaqueteAdapter.MyViewHolder>  {

    private Context mContext;
    private List<RowItemPaquete> paquetesList ;
    Integer idPaquete;
    AlertDialog.Builder messageBox;

    public ManifiestoPaqueteAdapter(Context context,Integer idPaquete){
        mContext = context;
        paquetesList = new ArrayList<>();
        this.idPaquete = idPaquete;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.lista_items_paquete,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull MyViewHolder holder, final int position) {
        final RowItemPaquete it = paquetesList.get(position);
        holder.txtPkgNombre.setText(it.getNombre());
        holder.txtPkgCantidad.setText(""+it.getCantidad());
        //holder.txtPkgPendiente.setText(it.getPendiente()== 0 ? "" : String.valueOf(it.getPendiente()));
        holder.txtPkgPendiente.setText(""+it.getPendiente());


        holder.txtPkgPendiente.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    EditText v = (EditText) view;
                    String pendiente = v.getText().toString();
                    if(!pendiente.equals("")){
                        if(Integer.valueOf(pendiente) > it.getCantidad()){
                            messageBox("Valor ingresado no debe ser mayor a la cantidad!!");
                            holder.txtPkgPendiente.setText(String.valueOf(0));
                        }else{
                            Integer valor;
                            if(it.getCantidad() > 0){
                                if(Integer.valueOf(pendiente)>0){
                                    valor = it.getCantidad() - Integer.valueOf(pendiente);
                                    if(position == 0){//fundas
                                        MyApp.getDBO().manifiestoPaqueteDao().UpdatePaquetesPendientesFundas(idPaquete, Integer.valueOf(pendiente), valor);
                                    }else{// guardianes
                                        MyApp.getDBO().manifiestoPaqueteDao().UpdatePaquetesPendientesGuardianes(idPaquete, Integer.valueOf(pendiente), valor);
                                    }
                                }else if(Integer.valueOf(pendiente)<0) {
                                    messageBox("Valor ingresado NO debe ser negativo!!");
                                    holder.txtPkgPendiente.setText(String.valueOf(0));
                                    if(position == 0){MyApp.getDBO().manifiestoPaqueteDao().UpdatePaquetesPendientesFundas(idPaquete, 0, 0);}
                                    if(position == 1){MyApp.getDBO().manifiestoPaqueteDao().UpdatePaquetesPendientesGuardianes(idPaquete, 0, 0);}
                                }
                                if(Integer.valueOf(pendiente)==0){
                                    if(position == 0){MyApp.getDBO().manifiestoPaqueteDao().UpdatePaquetesPendientesFundas(idPaquete, 0, 0);}
                                    if(position == 1){MyApp.getDBO().manifiestoPaqueteDao().UpdatePaquetesPendientesGuardianes(idPaquete, 0, 0);}
                                }
                            }
                        }
                    }else{
                        holder.txtPkgPendiente.setText(""+0);
                        if(position==0) {MyApp.getDBO().manifiestoPaqueteDao().UpdatePaquetesPendientesFundas(idPaquete, 0, 0);}
                        if(position==1) {MyApp.getDBO().manifiestoPaqueteDao().UpdatePaquetesPendientesGuardianes(idPaquete, 0, 0);}
                    }
                }else {
                    if(holder.txtPkgPendiente.getText().toString().equals("")){
                        holder.txtPkgPendiente.setText(""+0);
                    }else {
                        holder.txtPkgPendiente.setText("");
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return paquetesList.size();
    }

    public void messageBox(String message)
    {
        messageBox = new AlertDialog.Builder(mContext);
        messageBox.setTitle("INFO");
        messageBox.setMessage(message);
        messageBox.setCancelable(false);
        messageBox.setNeutralButton("OK", null);
        messageBox.show();
    }

    public void setTaskList(List<RowItemPaquete> taskList) {
        this.paquetesList = taskList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtPkgNombre;
        TextView txtPkgCantidad;
        TextView txtPkgPendiente;


        public MyViewHolder(View itemView) {
            super(itemView);

            txtPkgNombre = itemView.findViewById(R.id.txtItemPaqueteNombre);
            txtPkgCantidad = itemView.findViewById(R.id.txtItemPaqueteCantidad);
            txtPkgPendiente = itemView.findViewById(R.id.txtItemPaquetePendiente);

            /*
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mClickListener!=null)mClickListener.onItemClick(getAdapterPosition(),v);
                }
            });
             */

        }

    }
    /*
    public void setOnItemClickListener(@NonNull ClickListener l) {
        mClickListener= l;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }*/
}

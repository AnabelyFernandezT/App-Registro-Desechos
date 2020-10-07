package com.caircb.rcbtracegadere.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.dialogs.DialogKeyBoardPlanta;
import com.caircb.rcbtracegadere.models.ItemManifiestoDetalleValorSede;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ManifiestoDetalleBultosAdapterPlanta extends RecyclerView.Adapter<ManifiestoDetalleBultosAdapterPlanta.MyViewHolder>  {

    private ClickListener mClickListener;
    private Context mContext;
    private List<ItemManifiestoDetalleValorSede> manifiestosDtList;
    private Integer estadoManifiesto,idManifiestoDetalle;
    DialogKeyBoardPlanta dialogKeyBoardPlanta;
    List<ItemManifiestoDetalleValorSede> detalles = new ArrayList<>();
    int cAzul;

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
        cAzul = R.drawable.shape_card_border_blue;
        return new ManifiestoDetalleBultosAdapterPlanta.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull MyViewHolder holder, final int position) {
        final ItemManifiestoDetalleValorSede it = manifiestosDtList.get(position);

        holder.txtNuevoPeso.setEnabled(!it.getEstado());
        holder.txtNuevoPeso.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                openKeyBoard(position, it.getIdManifiestoDetalle(), it.getIdManifiestoDetalleValores());
                return false;
            }
        });

        holder.chkEstado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nuevoValor = holder.txtNuevoPeso.getText().toString().equals("") ? "0": holder.txtNuevoPeso.getText().toString();
                if(Double.valueOf(nuevoValor)>0){

                    if(((CheckBox) v).isChecked()){
                        v.setSelected(true);
                        holder.txtNuevoPeso.setEnabled(false);
                        MyApp.getDBO().manifiestoPlantaDetalleValorDao().updateManifiestoDetalleValorSedebyId(it.getIdManifiestoDetalle(), true, it.getIdManifiestoDetalleValores());
                    }else{
                        v.setSelected(false);
                        holder.txtNuevoPeso.setEnabled(true);
                        MyApp.getDBO().manifiestoPlantaDetalleValorDao().updateManifiestoDetalleValorSedebyId(it.getIdManifiestoDetalle(), false, it.getIdManifiestoDetalleValores());
                    }

                    //MyApp.getDBO().manifiestoPlantaDetalleValorDao().updateManifiestoDetalleValorPlantaPesoNuevo(it.getIdManifiestoDetalle(), (holder.txtNuevoPeso.getText().toString()), it.getIdManifiestoDetalleValores());
                    //MyApp.getDBO().manifiestoPlantaDetalleValorDao().updateManifiestoDetalleValorSedebyId(it.getIdManifiestoDetalle(), it.getEstado(), it.getIdManifiestoDetalleValores());
                }else{
                    holder.chkEstado.setChecked(false);
                    DialogBuilder dialogBuilder = new DialogBuilder(mContext);
                    dialogBuilder.setTitle("INFO");
                    dialogBuilder.setMessage("Debe ingresar el peso del bulto!");
                    dialogBuilder.setCancelable(false);
                    dialogBuilder.setPositiveButton("OK", null);
                    dialogBuilder.show();
                }
            }
        });

        holder.txtPeso.setText(it.getPeso().toString());
        holder.txtNombre.setText(it.getNombreBulto());
        holder.chkEstado.setChecked(it.getEstado());
        holder.txtNuevoPeso.setText(it.getNuevoPeso()==null ? "":it.getNuevoPeso().toString());
        if(it.getPeso().equals(0.0)){
            holder.borderVerificacion.setBackgroundResource(cAzul);
        }

      }

    @Override
    public int getItemCount() {
        return manifiestosDtList.size();
    }

    public void setNuevoPeso(double valor , int code){
        //manifiestosDtList.get(code).setNuevoPeso(valor);
        //notifyDataSetChanged();
        detalles.clear();
        detalles = MyApp.getDBO().manifiestoPlantaDetalleValorDao().fetchManifiestosAsigByClienteOrNumManif(idManifiestoDetalle);
        setTaskList(detalles);
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
        LinearLayout borderVerificacion;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtPeso = itemView.findViewById(R.id.txtBultos);
            chkEstado = itemView.findViewById(R.id.chkEstadoItemDetalle);
            txtNuevoPeso = (EditText)itemView.findViewById(R.id.txtIgnBultos);
            borderVerificacion = itemView.findViewById(R.id.rowFG);

        }

    }


    public void setOnItemClickListener(@NonNull ClickListener l) {
        mClickListener= l;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    private void openKeyBoard(Integer position, Integer idManifiestoDetalle, final Integer idManifiestoDetalleValores) {
        if(dialogKeyBoardPlanta == null){
            dialogKeyBoardPlanta = new DialogKeyBoardPlanta(mContext, position, idManifiestoDetalle, idManifiestoDetalleValores);
            dialogKeyBoardPlanta.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogKeyBoardPlanta.setCancelable(false);
            dialogKeyBoardPlanta.setmOnRegisterPesoListener(new DialogKeyBoardPlanta.onRegisterPesoListener() {
                @Override
                public void onFinished(double valor, Integer code, Integer idManifiestoDet, Integer idManifiestoDetValores) {

                    if(valor == 0.0){
                        MyApp.getDBO().manifiestoPlantaDetalleValorDao().updateManifiestoDetalleValorPlantaPesoNuevo(idManifiestoDet,null, idManifiestoDetalleValores);
                    }else{
                        MyApp.getDBO().manifiestoPlantaDetalleValorDao().updateManifiestoDetalleValorPlantaPesoNuevo(idManifiestoDet,String.valueOf(valor), idManifiestoDetalleValores);
                    }
                    dialogKeyBoardPlanta.dismiss();
                    dialogKeyBoardPlanta = null;
                    setNuevoPeso(valor, code);
                }

                @Override
                public void onCanceled() {
                    dialogKeyBoardPlanta.dismiss();
                    dialogKeyBoardPlanta = null;
                }
            });
            dialogKeyBoardPlanta.show();

            Window window = dialogKeyBoardPlanta.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }
}

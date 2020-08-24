package com.caircb.rcbtracegadere.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.entity.RuteoRecoleccionEntity;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.fragments.recolector.HojaRutaAsignadaFragment;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.ItemManifiesto;
import com.caircb.rcbtracegadere.models.ItemManifiestoSede;
import com.caircb.rcbtracegadere.tasks.UserConsultaCodigoQrValidadorTask;
import com.caircb.rcbtracegadere.tasks.UserRegistrarFinLoteHospitalesTask;

import java.util.ArrayList;
import java.util.List;

public class ManifiestoAdapterSede extends RecyclerView.Adapter<ManifiestoAdapterSede.MyViewHolder> {

    private Context mContext;
    int cRojo;
    int cVerde;
    int cNaranja;
    private List<ItemManifiestoSede> manifiestosList;
    private LinearLayout borderVerificacion;
    UserRegistrarFinLoteHospitalesTask userRegistrarFinLoteHospitales;

    public ManifiestoAdapterSede(Context context) {
        mContext = context;
        manifiestosList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manifiesto_sede, parent, false);
        cRojo = R.drawable.shape_card_border_red;
        cNaranja = R.drawable.shape_card_border_naranja;
        cVerde = R.drawable.shape_card_border;
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull MyViewHolder holder, int position) {
        final ItemManifiestoSede it = manifiestosList.get(position);
        holder.txtNumManifiesto.setText(it.getNumeroManifiesto());
        holder.txtCliente.setText(it.getNombreCliente());
        if (it.getBultosSelecionado() > 0) {
            if (it.getEstado().equals(3)) {
                holder.borderVerificacion.setBackgroundResource(cVerde);
            } else {
                holder.borderVerificacion.setBackgroundResource(cNaranja);
            }
        } else {
            holder.borderVerificacion.setBackgroundResource(cRojo);
        }
        String banderaValidacion = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_bandera_validacion") == null ? "0" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_bandera_validacion");
        if (banderaValidacion.equals("0")) {
            String estadoCodigoQr = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_estadoCodigoQr") == null ? "0" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_estadoCodigoQr");
            System.out.println(estadoCodigoQr);
            if (estadoCodigoQr.equals("1")) {
                String idSubRuta = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_idSubruta") == null ? "0" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_idSubruta");
                System.out.println(idSubRuta);
                MySession.setIdSubruta(Integer.parseInt(idSubRuta));
                UserConsultaCodigoQrValidadorTask consultaCodigoQrValidadorTask = new UserConsultaCodigoQrValidadorTask(mContext);
                consultaCodigoQrValidadorTask.setOnCodigoQrListener(new UserConsultaCodigoQrValidadorTask.OnCodigoQrListener() {
                    @Override
                    public void onSuccessful() {
                        int contManifiestosCerrados = 0;
                        for (int i = 0; i < manifiestosList.size(); i++) {
                            if (manifiestosList.get(i).getEstado().equals(3)) {
                                contManifiestosCerrados++;
                            }
                        }
                        if (contManifiestosCerrados == manifiestosList.size()) {
                            String placaSinvronizada = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_placa_Planta") == null ? "" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_placa_Planta");
                            final DialogBuilder dialogBuilder;
                            dialogBuilder = new DialogBuilder(mContext);
                            dialogBuilder.setMessage("Ha finalizado la Recolección del vehiculo " + placaSinvronizada);
                            dialogBuilder.setCancelable(false);
                            dialogBuilder.setPositiveButton("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogBuilder.dismiss();
                                    String idSubRuta = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_idSubruta") == null ? "0" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_idSubruta");
                                    System.out.println(idSubRuta);
                                    if (!idSubRuta.equals("0")) {
                                        int idSubRutaEnviar = Integer.parseInt(idSubRuta);
                                        userRegistrarFinLoteHospitales = new UserRegistrarFinLoteHospitalesTask(mContext, idSubRutaEnviar, 0, 4);
                                        userRegistrarFinLoteHospitales.setOnFinLoteListener(new UserRegistrarFinLoteHospitalesTask.OnFinLoteListener() {
                                            @Override
                                            public void onSuccessful() {
                                                MyApp.getDBO().parametroDao().saveOrUpdate("current_bandera_validacion", "1");
                                            }

                                            @Override
                                            public void onFailure() {

                                            }
                                        });
                                        userRegistrarFinLoteHospitales.execute();
                                    }
                                }
                            });
                            dialogBuilder.show();
                        }
                    }

                    @Override
                    public void onFailure() {

                    }
                });
                consultaCodigoQrValidadorTask.execute();

            }
        }

    }

    @Override
    public int getItemCount() {
        return manifiestosList.size();
    }

    public void setTaskList(List<ItemManifiestoSede> taskList) {
        this.manifiestosList = taskList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtNumManifiesto;
        TextView txtCliente;
        LinearLayout borderVerificacion;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtNumManifiesto = itemView.findViewById(R.id.itm_num_manifiesto);
            txtCliente = itemView.findViewById(R.id.itm_cliente);
            borderVerificacion = (LinearLayout) itemView.findViewById(R.id.rowFG);
        }
    }
}

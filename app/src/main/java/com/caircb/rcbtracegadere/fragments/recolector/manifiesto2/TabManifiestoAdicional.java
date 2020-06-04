package com.caircb.rcbtracegadere.fragments.recolector.manifiesto2;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.adapters.ManifiestoNoRecoleccionBaseAdapterR;
import com.caircb.rcbtracegadere.adapters.ManifiestoNovedadBaseAdapter;
import com.caircb.rcbtracegadere.adapters.ManifiestoNovedadBaseAdapterR;
import com.caircb.rcbtracegadere.adapters.ManifiestoPaqueteAdapter;
import com.caircb.rcbtracegadere.database.entity.ManifiestoPaquetesEntity;
import com.caircb.rcbtracegadere.database.entity.PaqueteEntity;
import com.caircb.rcbtracegadere.dialogs.DialogAgregarFotografias;
import com.caircb.rcbtracegadere.dialogs.DialogAudio;
import com.caircb.rcbtracegadere.models.RowItemHojaRutaCatalogo;
import com.caircb.rcbtracegadere.models.RowItemNoRecoleccion;
import com.caircb.rcbtracegadere.models.RowItemPaquete;
import com.caircb.rcbtracegadere.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class TabManifiestoAdicional extends LinearLayout {

    Integer idAppManifiesto,idAppTipoPaquete;
    Window window;
    boolean bloquear;

    EditText txtNovedadEncontrada;
    TextView btnAgregarAudio;
    LinearLayout lnlAdicionales;

    List<RowItemHojaRutaCatalogo> novedadfrecuentes;
    List<RowItemNoRecoleccion> motivoNoRecoleccion;
    List<RowItemPaquete> listaPaquetes;
    PaqueteEntity pkg;
    ManifiestoPaquetesEntity manifiestoPkg;

    DialogAgregarFotografias dialogAgregarFotografias;
    DialogAudio dialogAudio;

    RecyclerView recyclerViewLtsManifiestoObservaciones, recyclerViewLtsMotivoNoRecoleccion, recyclerLtsPaquetes;
    ManifiestoNovedadBaseAdapterR recyclerAdapterNovedades;
    ManifiestoNoRecoleccionBaseAdapterR recyclerAdapterNoRecolecciones;
    ManifiestoPaqueteAdapter manifiestoPaqueteAdapter;

    public TabManifiestoAdicional(Context context,Integer idAppManifiesto,Integer tipoPaquete) {
        super(context);
        this.idAppManifiesto = idAppManifiesto;
        this.idAppTipoPaquete = tipoPaquete;
        View.inflate(context, R.layout.tab_manifiesto_adicional, this);
        init();
        loadDataPaquetes();
        loadData();
    }

    private void init(){
        txtNovedadEncontrada = this.findViewById(R.id.txtNovedadEncontrada);

        recyclerLtsPaquetes = this.findViewById(R.id.LtsPaquetes);
        recyclerViewLtsMotivoNoRecoleccion = this.findViewById(R.id.LtsMotivoNoRecoleccion);
        recyclerViewLtsManifiestoObservaciones = this.findViewById(R.id.LtsManifiestoObservaciones);

        lnlAdicionales = this.findViewById(R.id.lnlAdicionales);

        btnAgregarAudio = this.findViewById(R.id.btnAgregarAudio);
        btnAgregarAudio.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAudio = new DialogAudio(getContext());
                dialogAudio.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogAudio.setCancelable(false);
                dialogAudio.show();
            }
        });
    }

    private void loadDataPaquetes(){
        if(idAppTipoPaquete!=null) {
            pkg = MyApp.getDBO().paqueteDao().fechConsultaPaqueteEspecifico(idAppTipoPaquete);
            manifiestoPkg = MyApp.getDBO().manifiestoPaqueteDao().fetchConsultarManifiestoPaquetebyId(idAppManifiesto,idAppTipoPaquete);
            listaPaquetes = new ArrayList<>();
            listaPaquetes.add(new RowItemPaquete(pkg.getFunda(), manifiestoPkg!=null?manifiestoPkg.getDatosFundas():0, 0));
            listaPaquetes.add(new RowItemPaquete(pkg.getGuardian(), manifiestoPkg!=null?manifiestoPkg.getDatosGuardianes():0, 0));

            recyclerLtsPaquetes.setLayoutManager(new LinearLayoutManager(getContext()));
            manifiestoPaqueteAdapter = new ManifiestoPaqueteAdapter(getContext(),idAppTipoPaquete);
            manifiestoPaqueteAdapter.setTaskList(listaPaquetes);
            recyclerLtsPaquetes.setAdapter(manifiestoPaqueteAdapter);

            //region de adicionales...
            lnlAdicionales.setVisibility(pkg.getFlagAdicionales()?View.VISIBLE:View.GONE);
        }
    }

    public void reloadDataPaquetes(){
        if(listaPaquetes!=null && listaPaquetes.size()>0) {
            manifiestoPkg = MyApp.getDBO().manifiestoPaqueteDao().fetchConsultarManifiestoPaquetebyId(idAppManifiesto, idAppTipoPaquete);
            listaPaquetes.get(0).setCantidad(manifiestoPkg!=null?manifiestoPkg.getDatosFundas():0);
            listaPaquetes.get(1).setCantidad(manifiestoPkg!=null?manifiestoPkg.getDatosGuardianes():0);

            manifiestoPaqueteAdapter.setTaskList(listaPaquetes);
        }
    }

    private void loadData(){
        novedadfrecuentes = MyApp.getDBO().manifiestoObservacionFrecuenteDao().fetchHojaRutaCatalogoNovedaFrecuente(idAppManifiesto);
        recyclerViewLtsManifiestoObservaciones.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerAdapterNovedades = new ManifiestoNovedadBaseAdapterR(getContext(), novedadfrecuentes, bloquear);
        recyclerAdapterNovedades.setOnClickOpenFotografias(new ManifiestoNovedadBaseAdapterR.OnClickOpenFotografias() {
           @Override
           public void onShow(Integer catalogoID, final Integer position) {
               if(dialogAgregarFotografias==null){
                   dialogAgregarFotografias = new DialogAgregarFotografias(getContext(),idAppManifiesto,catalogoID,1);
                   dialogAgregarFotografias.setCancelable(false);
                   dialogAgregarFotografias.requestWindowFeature(Window.FEATURE_NO_TITLE);
                   dialogAgregarFotografias.setOnAgregarFotosListener(new DialogAgregarFotografias.OnAgregarFotosListener() {
                       @Override
                       public void onSuccessful(Integer cantidad) {
                           if(dialogAgregarFotografias!=null && dialogAgregarFotografias.isShowing()){
                               dialogAgregarFotografias.dismiss();
                               dialogAgregarFotografias=null;

                               novedadfrecuentes.get(position).setNumFotos(cantidad);
                               recyclerAdapterNovedades.notifyDataSetChanged();
                           }
                       }
                   });
                   dialogAgregarFotografias.show();

                   window = dialogAgregarFotografias.getWindow();
                   window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
               }
           }
       });
        recyclerViewLtsManifiestoObservaciones.setAdapter(recyclerAdapterNovedades);

        /*****/
        motivoNoRecoleccion = MyApp.getDBO().manifiestoMotivosNoRecoleccionDao().fetchHojaRutaMotivoNoRecoleccion(idAppManifiesto);
        recyclerViewLtsMotivoNoRecoleccion.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerAdapterNoRecolecciones = new ManifiestoNoRecoleccionBaseAdapterR(this.getContext(), motivoNoRecoleccion,idAppManifiesto, bloquear);

        recyclerAdapterNoRecolecciones.setOnClickOpenFotografias(new ManifiestoNoRecoleccionBaseAdapterR.OnClickOpenFotografias() {
            @Override
            public void onShow(Integer catalogoID, final Integer position) {
                if(dialogAgregarFotografias==null){
                    dialogAgregarFotografias = new DialogAgregarFotografias(getContext(),idAppManifiesto,catalogoID,2);
                    dialogAgregarFotografias.setCancelable(false);
                    dialogAgregarFotografias.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialogAgregarFotografias.setOnAgregarFotosListener(new DialogAgregarFotografias.OnAgregarFotosListener() {
                        @Override
                        public void onSuccessful(Integer cantidad) {
                            if(dialogAgregarFotografias!=null && dialogAgregarFotografias.isShowing()){
                                dialogAgregarFotografias.dismiss();
                                dialogAgregarFotografias=null;

                                motivoNoRecoleccion.get(position).setNumFotos(cantidad);
                                recyclerAdapterNoRecolecciones.notifyDataSetChanged();
                            }
                        }
                    });
                    dialogAgregarFotografias.show();

                    window = dialogAgregarFotografias.getWindow();
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                }
            }
        });
        recyclerViewLtsMotivoNoRecoleccion.setAdapter(recyclerAdapterNoRecolecciones);
    }

    public void setMakePhoto(Integer code) {
        if(dialogAgregarFotografias!=null){
            dialogAgregarFotografias.setMakePhoto(code);
        }
    }
}

package com.caircb.rcbtracegadere.fragments.recolector.manifiesto2;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.adapters.ManifiestoNoRecoleccionBaseAdapterR;
import com.caircb.rcbtracegadere.adapters.ManifiestoNovedadBaseAdapter;
import com.caircb.rcbtracegadere.adapters.ManifiestoNovedadBaseAdapterR;
import com.caircb.rcbtracegadere.dialogs.DialogAgregarFotografias;
import com.caircb.rcbtracegadere.dialogs.DialogAudio;
import com.caircb.rcbtracegadere.models.RowItemHojaRutaCatalogo;
import com.caircb.rcbtracegadere.models.RowItemNoRecoleccion;
import com.caircb.rcbtracegadere.utils.Utils;

import java.util.List;

public class TabManifiestoAdicional extends LinearLayout {
    Integer idAppManifiesto;
    Window window;
    boolean bloquear;
    EditText txtNovedadEncontrada;
    LinearLayout btnAudio;

    List<RowItemHojaRutaCatalogo> novedadfrecuentes;
    List<RowItemNoRecoleccion> motivoNoRecoleccion;

    DialogAgregarFotografias dialogAgregarFotografias;
    DialogAudio dialogAudio;

    RecyclerView recyclerViewLtsManifiestoObservaciones, recyclerViewLtsMotivoNoRecoleccion;
    ManifiestoNovedadBaseAdapterR recyclerAdapterNovedades;
    ManifiestoNoRecoleccionBaseAdapterR recyclerAdapterNoRecolecciones;

    public TabManifiestoAdicional(Context context,Integer idAppManifiesto) {
        super(context);
        this.idAppManifiesto = idAppManifiesto;
        View.inflate(context, R.layout.tab_manifiesto_adicional, this);
        init();
        loadData();
    }

    private void init(){
        txtNovedadEncontrada = this.findViewById(R.id.txtNovedadEncontrada);

        recyclerViewLtsMotivoNoRecoleccion = this.findViewById(R.id.LtsMotivoNoRecoleccion);
        recyclerViewLtsManifiestoObservaciones = this.findViewById(R.id.LtsManifiestoObservaciones);
        btnAudio = this.findViewById(R.id.btn_audio);
        btnAudio.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAudio = new DialogAudio(getContext());
                dialogAudio.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogAudio.setCancelable(false);
                dialogAudio.show();
            }
        });
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

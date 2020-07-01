package com.caircb.rcbtracegadere.fragments.recolector.MotivoNoRecoleccion;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.adapters.ManifiestoNoRecoleccionBaseAdapterR;
import com.caircb.rcbtracegadere.adapters.ManifiestoNovedadBaseAdapterR;
import com.caircb.rcbtracegadere.adapters.ManifiestoPaqueteAdapter;
import com.caircb.rcbtracegadere.database.AppDatabase;
import com.caircb.rcbtracegadere.database.dao.ManifiestoFileDao;
import com.caircb.rcbtracegadere.database.entity.ManifiestoEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoPaquetesEntity;
import com.caircb.rcbtracegadere.database.entity.PaqueteEntity;
import com.caircb.rcbtracegadere.dialogs.DialogAgregarFotografias;
import com.caircb.rcbtracegadere.dialogs.DialogAudio;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.helpers.MyConstant;
import com.caircb.rcbtracegadere.models.ItemFile;
import com.caircb.rcbtracegadere.models.RowItemHojaRutaCatalogo;
import com.caircb.rcbtracegadere.models.RowItemNoRecoleccion;
import com.caircb.rcbtracegadere.models.RowItemPaquete;

import java.util.ArrayList;
import java.util.List;

public class TabManifiestoAdicionalNoRecoleccion extends LinearLayout {

    Integer idAppManifiesto,idAppTipoPaquete,estadoManifiesto;
    String mAudio;
    Window window;
    boolean bloquear;

    EditText txtNovedadEncontrada,txtItemPaqueteADFunda,txtItemPaqueteADGuardianes;
    TextView btnAgregarAudio,btnEliminarAudio,txtTimeGrabacion;
    Chronometer mChronometer;
    ImageButton btnReproducirAudio;
    LinearLayout lnlAdicionales;
    ProgressBar progressAudio;
    ValueAnimator animator;

    List<RowItemHojaRutaCatalogo> novedadfrecuentes;
    List<RowItemNoRecoleccion> motivoNoRecoleccion;
    List<RowItemPaquete> listaPaquetes;
    PaqueteEntity pkg;
    ManifiestoPaquetesEntity manifiestoPkg;

    DialogAgregarFotografias dialogAgregarFotografias;
    DialogAudio dialogAudio;

    RecyclerView  recyclerViewLtsMotivoNoRecoleccion;
    ManifiestoNoRecoleccionBaseAdapterR recyclerAdapterNoRecolecciones;
    ManifiestoPaqueteAdapter manifiestoPaqueteAdapter;

    LinearLayout lnlmsgAdicionales;
    DialogBuilder builder;

    View focusView;

    public TabManifiestoAdicionalNoRecoleccion(Context context,
                                               Integer idAppManifiesto,
                                               Integer tipoPaquete,
                                               String tiempoAudio,
                                               Integer estadoManifiesto) {
        super(context);
        this.idAppManifiesto = idAppManifiesto;
        this.idAppTipoPaquete = tipoPaquete;
        this.estadoManifiesto = estadoManifiesto;
        View.inflate(context, R.layout.tab_manifiesto_adicional_no_recoleccion, this);
        init();
        //initData();
        loadData();
        preLoadAudio(tiempoAudio);
    }

    private void init(){
        txtNovedadEncontrada = this.findViewById(R.id.txtNovedadEncontrada);

        recyclerViewLtsMotivoNoRecoleccion = this.findViewById(R.id.LtsMotivoNoRecoleccion);

        lnlAdicionales = this.findViewById(R.id.lnlAdicionales);
        txtItemPaqueteADFunda = this.findViewById(R.id.txtItemPaqueteADFunda);
        txtItemPaqueteADGuardianes = this.findViewById(R.id.txtItemPaqueteADGuardianes);
        lnlmsgAdicionales = this.findViewById(R.id.lnlmsgAdicionales);

        progressAudio = this.findViewById(R.id.progressAudio);
        mChronometer = this.findViewById(R.id.chronometer);
        btnReproducirAudio = this.findViewById(R.id.btnReproducirAudio);
        btnEliminarAudio = this.findViewById(R.id.btnEliminarAudio);
        txtTimeGrabacion = this.findViewById(R.id.txtTimeGrabacion);
        btnAgregarAudio = this.findViewById(R.id.btnAgregarAudio);
        btnAgregarAudio.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAudio = new DialogAudio(getContext());
                dialogAudio.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogAudio.setCancelable(false);
                dialogAudio.setOnAgregarAudioListener(new DialogAudio.OnAgregarAudioListener() {
                    @Override
                    public void onSuccessful(String audio,String tiempo) {

                        MyApp.getDBO().manifiestoFileDao().saveOrUpdate(idAppManifiesto, ManifiestoFileDao.AUDIO_RECOLECCION,AppDatabase.getFieldName("AUDIO")+".mp3",tiempo,audio,MyConstant.STATUS_RECOLECCION);

                        dialogAudio.dismiss();
                        dialogAudio=null;
                        mAudio= audio;
                        txtTimeGrabacion.setText(tiempo);
                        btnAgregarAudio.setVisibility(View.GONE);
                        btnEliminarAudio.setVisibility(View.VISIBLE);
                        btnReproducirAudio.setVisibility(View.VISIBLE);

                        progressAudio.setProgress(0);
                        progressAudio.setMax(timeToMilesecons(tiempo));
                        progressAudio.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCanceled() {

                    }
                });
                dialogAudio.show();
            }
        });


        btnReproducirAudio.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAudio!=null && mAudio.length()>0) {

                    PlayAudio(mAudio);
                    startChronometer();
                    animator = ValueAnimator.ofInt(0, progressAudio.getMax());
                    animator.setDuration(progressAudio.getMax());
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation){
                            progressAudio.setProgress((Integer)animation.getAnimatedValue());
                        }
                    });
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            stopChronometer();
                        }
                    });
                    animator.start();
                }
            }
        });

        btnEliminarAudio.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //funcionalidad para confirmar eliminacion...
                MyApp.getDBO().manifiestoFileDao().saveOrUpdate(idAppManifiesto,  ManifiestoFileDao.AUDIO_RECOLECCION,"",null,"00:00",MyConstant.STATUS_RECOLECCION);
                mAudio="";
                txtTimeGrabacion.setText("00:00");
                btnAgregarAudio.setVisibility(View.VISIBLE);
                btnEliminarAudio.setVisibility(View.GONE);
                btnReproducirAudio.setVisibility(View.GONE);
                progressAudio.setProgress(0);
                progressAudio.setVisibility(View.GONE);
            }
        });



        txtNovedadEncontrada.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    MyApp.getDBO().manifiestoDao().updateNovedadEncontrada(idAppManifiesto, txtNovedadEncontrada.getText().toString());
                }
            }
        });

        ManifiestoEntity manifiesto = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(idAppManifiesto);
        if (manifiesto!=null){
            txtNovedadEncontrada.setText(manifiesto.getNovedadEncontrada());
        }

        visible();
    }



    private void loadData(){
        /*****/
        motivoNoRecoleccion = MyApp.getDBO().manifiestoMotivosNoRecoleccionDao().fetchHojaRutaMotivoNoRecoleccion(idAppManifiesto);
        recyclerViewLtsMotivoNoRecoleccion.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerAdapterNoRecolecciones = new ManifiestoNoRecoleccionBaseAdapterR(this.getContext(), motivoNoRecoleccion,idAppManifiesto, bloquear,estadoManifiesto);
        recyclerAdapterNoRecolecciones.setOnClickReaload(new ManifiestoNovedadBaseAdapterR.OnReloadAdater() {
            @Override
            public void onShowM(final Integer catalogoID, final Integer position) {
                builder = new DialogBuilder(getContext());
                builder.setMessage("¿Seguro que desea desactivar el registro, automáticamente se borrarán las evidencias?");
                builder.setCancelable(true);
                builder.setPositiveButton("OK", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recyclerAdapterNoRecolecciones.registarCheckObservacion(idAppManifiesto,catalogoID,false);
                        recyclerAdapterNoRecolecciones.deleteFotosByItem(idAppManifiesto, catalogoID, position);

                        motivoNoRecoleccion.get(position).setNumFotos(0);
                        motivoNoRecoleccion.get(position).setEstadoChek(false);

                        recyclerAdapterNoRecolecciones.notifyDataSetChanged();
                        recyclerViewLtsMotivoNoRecoleccion.setAdapter(recyclerAdapterNoRecolecciones);
                        builder.dismiss();
                    }
                });
                builder.setNegativeButton("NO", new OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        recyclerAdapterNoRecolecciones.notifyDataSetChanged();
                        recyclerViewLtsMotivoNoRecoleccion.setAdapter(recyclerAdapterNoRecolecciones);
                        builder.dismiss();
                    }
                });
                builder.show();
            }
        });
        recyclerAdapterNoRecolecciones.setOnClickOpenFotografias(new ManifiestoNoRecoleccionBaseAdapterR.OnClickOpenFotografias() {
            @Override
            public void onShow(Integer catalogoID, final Integer position) {
                if(dialogAgregarFotografias==null){
                    dialogAgregarFotografias = new DialogAgregarFotografias(getContext(),idAppManifiesto,catalogoID, ManifiestoFileDao.FOTO_NO_RECOLECCION,MyConstant.STATUS_RECOLECCION);
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
    private void visible (){
        if(estadoManifiesto !=1){
            btnAgregarAudio.setEnabled(false);
            txtNovedadEncontrada.setEnabled(false);
        }
    }

    private void preLoadAudio(String tiempo){
        if(tiempo!=null && (!tiempo.equals("00:00") && tiempo.length()>0)){
            ItemFile f = MyApp.getDBO().manifiestoFileDao().consultarFile(idAppManifiesto, ManifiestoFileDao.AUDIO_RECOLECCION,MyConstant.STATUS_RECOLECCION);
            if(f!=null) {
                mAudio = f.getFile();
                btnAgregarAudio.setVisibility(View.GONE);
                btnEliminarAudio.setVisibility(View.VISIBLE);
                btnReproducirAudio.setVisibility(View.VISIBLE);
                progressAudio.setMax(timeToMilesecons(tiempo));
                progressAudio.setVisibility(View.VISIBLE);
            }
        }
        if(tiempo!=null && tiempo.length()>0) txtTimeGrabacion.setText(tiempo);
    }

    private Integer timeToMilesecons(String tiempo){
        String array[] = tiempo.split(":");
        return (Integer.parseInt(array[0]) * 60 * 1000
                + Integer.parseInt(array[1]) * 1000)+100;
    }

    private void stopChronometer(){
        txtTimeGrabacion.setVisibility(View.VISIBLE);
        mChronometer.setVisibility(View.GONE);
        mChronometer.stop();
        mChronometer.setBase(SystemClock.elapsedRealtime());
    }

    private void startChronometer(){
        txtTimeGrabacion.setVisibility(View.GONE);
        mChronometer.setVisibility(View.VISIBLE);
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();
    }

    private void PlayAudio(String base64EncodedString){
        try
        {
            String url = "data:audio/mp3;base64,"+base64EncodedString;
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }
        catch(Exception ex){
            System.out.print(ex.getMessage());
        }
    }

    public void setMakePhoto(Integer code) {
        if(dialogAgregarFotografias!=null){
            dialogAgregarFotografias.setMakePhoto(code);
        }
    }

    public  boolean validaNovedadNoRecoleccionPendicenteFotos(){
        return  MyApp.getDBO().manifiestoMotivosNoRecoleccionDao().existeNovedadNoRecoleccionPendienteFoto(idAppManifiesto)>0;
    }

    public boolean validaNovedadesFrecuentesPendienteFotos(){
        return MyApp.getDBO().manifiestoObservacionFrecuenteDao().existeNovedadFrecuentePendienteFoto(idAppManifiesto)>0;
    }

    public boolean validaExisteNovedadesNoRecoleccion(){
        return MyApp.getDBO().manifiestoMotivosNoRecoleccionDao().existeNovedadNoRecoleccion(idAppManifiesto);
    }
}

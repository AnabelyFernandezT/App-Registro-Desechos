package com.caircb.rcbtracegadere.fragments.planta;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.adapters.ManifiestoNoRecoleccionBaseAdapter;
import com.caircb.rcbtracegadere.adapters.ManifiestoNoRecoleccionBaseAdapterR;
import com.caircb.rcbtracegadere.adapters.ManifiestoNovedadBaseAdapter;
import com.caircb.rcbtracegadere.adapters.ManifiestoNovedadBaseAdapterR;
import com.caircb.rcbtracegadere.adapters.ManifiestoNovedadBaseAdapterRecepcionR;
import com.caircb.rcbtracegadere.database.dao.ManifiestoFileDao;
import com.caircb.rcbtracegadere.database.dao.ManifiestoPlantaDetalleValorDao;
import com.caircb.rcbtracegadere.database.entity.ManifiestoDetallePesosEntity;
import com.caircb.rcbtracegadere.dialogs.DialogAgregarFotografias;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.dialogs.DialogFirma;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnRecyclerTouchListener;
import com.caircb.rcbtracegadere.helpers.MyConstant;
import com.caircb.rcbtracegadere.models.ItemFile;
import com.caircb.rcbtracegadere.models.ItemManifiestoDetalleValorSede;
import com.caircb.rcbtracegadere.models.RowItemHojaRutaCatalogo;
import com.caircb.rcbtracegadere.models.RowItemNoRecoleccion;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoPlantaObservacion;
import com.caircb.rcbtracegadere.utils.Utils;
import com.itextpdf.text.pdf.languages.ArabicLigaturizer;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabManifiestoAdicionalFragment extends LinearLayout {

    private static final String ARG_PARAM1 = "idAppManifiesto";
    private static final String ARG_PARAM2 = "Manifiestobloqueado";

    View view;
    DialogAgregarFotografias dialogAgregarFotografias;

    Integer idAppManifiesto;
    RecyclerView recyclerViewLtsManifiestoObservaciones;
    Activity _activity;
    ImageView imgFirmaPlanta;
    LinearLayout btnAgregarFirma, btnCancelar, btnGuardar,btnInformacion;
    EditText txtPeso,txtNovedad,txtotraNovedad;
    TextView txtFirmaPlanta, txtPesoRecolectado, txtObservacionPeso;
    DialogFirma dialogFirma;
    private Integer idManifiesto;
    boolean bloquear;
    Window window;
    Bitmap firmaConfirmada;
    List<RowItemHojaRutaCatalogo> novedadfrecuentes;
    ManifiestoNovedadBaseAdapterRecepcionR recyclerAdapterNovedades;
    DialogBuilder builder;
    double pesoT, pesoRecolectado=0;
    private boolean firma = false, observacion = false;
    LinearLayout btnEvidenciaObservacion, lnlCountPhoto;
    TextView txtCountPhoto, txtPesoPlanta;
    boolean info = false;
    DialogBuilder message;
    List<ItemManifiestoDetalleValorSede> bultos = new ArrayList<>();


    public TabManifiestoAdicionalFragment(Context context, Integer manifiestoID){
        super(context);
        View.inflate(context, R.layout.fragment_recoleccion_planta_adicional, this);
        this.idAppManifiesto = manifiestoID;
        init();
        loadData();
        validarPesoExtra();
        bloquerAdiciona();
    }

    private void init(){
        recyclerViewLtsManifiestoObservaciones = this.findViewById(R.id.LtsManifiestoObservaciones);
        txtPeso = this.findViewById(R.id.txtPeso);
        btnGuardar = this.findViewById(R.id.btnGuardar);
        btnCancelar = this.findViewById(R.id.btnCancelar);

        btnInformacion = this.findViewById(R.id.btnInformacion);
        btnAgregarFirma = this.findViewById(R.id.btnAgregarFirma);
        imgFirmaPlanta = this.findViewById(R.id.imgFirmaPlanta);
        txtFirmaPlanta = this.findViewById(R.id.txtFirmaPlanta);
        txtPesoRecolectado = this.findViewById(R.id.txtPesoRecolectado);
        txtPesoPlanta = this.findViewById(R.id.txtPesoPlanta);
        txtotraNovedad = this.findViewById(R.id.txtotraNovedad);
        txtObservacionPeso = this.findViewById(R.id.txtObservacionPeso);

        txtNovedad = this.findViewById(R.id.txtNovedad);
        btnEvidenciaObservacion = this.findViewById(R.id.btnEvidenciaObservacion);
        lnlCountPhoto = this.findViewById(R.id.lnlCountPhoto);
        txtCountPhoto = this.findViewById(R.id.txtCountPhoto);

        btnEvidenciaObservacion.setVisibility(View.GONE);

        Integer numeroFotos = MyApp.getDBO().manifiestoFileDao().obtenerCantidadFotografiabyManifiestoCatalogo(idAppManifiesto, -1, ManifiestoFileDao.FOTO_FOTO_ADICIONAL_PLANTA );
        if(numeroFotos != null && numeroFotos > 0){
            lnlCountPhoto.setVisibility(View.VISIBLE);
            txtCountPhoto.setText(String.valueOf(numeroFotos));
            btnEvidenciaObservacion.setVisibility(View.VISIBLE);
        }

        btnAgregarFirma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialogFirma==null) {
                    dialogFirma = new DialogFirma(getContext());
                    dialogFirma.setTitle("SU FIRMA");
                    dialogFirma.setCancelable(false);
                    dialogFirma.setOnSignaturePadListener(new DialogFirma.OnSignaturePadListener() {
                        @Override
                        public void onSuccessful(Bitmap bitmap) {
                            dialogFirma.dismiss();
                            dialogFirma=null;
                            if(bitmap!=null){
                                txtFirmaPlanta.setVisibility(View.GONE);
                                imgFirmaPlanta.setVisibility(View.VISIBLE);
                                imgFirmaPlanta.setImageBitmap(bitmap);
                                firmaConfirmada = bitmap;
                                firma=true;
                                MyApp.getDBO().manifiestoFileDao().saveOrUpdate(idAppManifiesto, ManifiestoFileDao.FOTO_FIRMA_RECEPCION_ADICIONAL_PLANTA, Utils.encodeTobase64(bitmap), MyConstant.STATUS_RECEPCION_PLANTA);

                            }else{
                                txtFirmaPlanta.setVisibility(View.VISIBLE);
                                imgFirmaPlanta.setVisibility(View.GONE);
                                firma=false;
                                MyApp.getDBO().manifiestoFileDao().saveOrUpdate(idAppManifiesto, ManifiestoFileDao.FOTO_FIRMA_RECEPCION_ADICIONAL_PLANTA, null,MyConstant.STATUS_RECEPCION_PLANTA);
                            }
                        }

                        @Override
                        public void onCanceled() {
                            dialogFirma.dismiss();
                            dialogFirma=null;

                        }
                    });
                    dialogFirma.show();
                }
            }
        });

        btnEvidenciaObservacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAgregarFotografias = new DialogAgregarFotografias(getContext(), idAppManifiesto, -1, ManifiestoFileDao.FOTO_FOTO_ADICIONAL_PLANTA, MyConstant.STATUS_RECEPCION_PLANTA);
                dialogAgregarFotografias.setCancelable(false);
                dialogAgregarFotografias.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogAgregarFotografias.setOnAgregarFotosListener(new DialogAgregarFotografias.OnAgregarFotosListener() {
                    @Override
                    public void onSuccessful(Integer cantidad) {
                        lnlCountPhoto.setVisibility(View.VISIBLE);
                        txtCountPhoto.setText(String.valueOf(cantidad));
                    }
                });
                dialogAgregarFotografias.show();
                window = dialogAgregarFotografias.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }
        });


        txtotraNovedad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().equals("")){
                    btnEvidenciaObservacion.setVisibility(View.VISIBLE);
                }else{
                    btnEvidenciaObservacion.setVisibility(View.GONE);
                }
            }
        });

        txtotraNovedad.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                guardarObservaciones();
            }
        });
        obtenerObservaciones();
    }

    public String sendObservacion(){
        return txtotraNovedad.getText().toString();
    }

    public  boolean validarNovedad (){
        String txtObservacion = txtotraNovedad.getText().toString();
        String numeroFotos = txtCountPhoto.getText().toString();
        if(txtObservacion.equals("")){
           observacion=true;
        }else{
            if(numeroFotos.equals("0")){
                observacion = false;
            }else{
                observacion = true;
            }
        }
        return  observacion;
    }

    public boolean validarInformacion(){
        if(firma){
            info = true;
        }
        return info;
    }
    private void loadData(){
        ItemFile f = MyApp.getDBO().manifiestoFileDao().consultarFile(idAppManifiesto, ManifiestoFileDao.FOTO_FIRMA_RECEPCION_ADICIONAL_PLANTA,MyConstant.STATUS_RECEPCION_PLANTA);
        if(f != null){
            Bitmap imagen = Utils.StringToBitMap(f.getFile());
            txtFirmaPlanta.setVisibility(View.GONE);
            imgFirmaPlanta.setVisibility(View.VISIBLE);
            imgFirmaPlanta.setImageBitmap(imagen);
            firmaConfirmada = imagen;
            firma=true;
        }
    }

    public void validarPesoExtra(){
        bultos.clear();
        pesoT = 0.0;
        pesoRecolectado = 0;
        bultos = MyApp.getDBO().manifiestoPlantaDetalleValorDao().fetchManifiestosAsigByNumManif(idAppManifiesto);
        if(bultos.size()>0){
            for (ItemManifiestoDetalleValorSede p:bultos){
                pesoT= pesoT+ p.getPeso();
                if(p.getNuevoPeso()!=null){
                    pesoRecolectado = pesoRecolectado + p.getNuevoPeso();
                }
            }
        }

        if (txtPesoRecolectado!=null) {
            txtPesoRecolectado.setText(String.valueOf(pesoT));
            txtPesoPlanta.setText(String.valueOf(pesoRecolectado));

            double validacion = (Double.parseDouble(txtPesoRecolectado.getText().toString()) * 0.03) + Double.parseDouble(txtPesoRecolectado.getText().toString());
            double validacionMenor = (Double.parseDouble(txtPesoRecolectado.getText().toString()) - Double.parseDouble(txtPesoRecolectado.getText().toString()) * 0.03);
            double valorIngresado = Double.parseDouble(txtPesoPlanta.getText().toString());

            if (!String.valueOf(valorIngresado).equals(txtPesoRecolectado.getText())) {
                if (valorIngresado > validacion) {
                    //Toast.makeText(getContext(), "El peso es mayor al recolectado", Toast.LENGTH_SHORT).show();
                    btnInformacion.setVisibility(View.VISIBLE);
                    btnInformacion.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            builder = new DialogBuilder(getContext());
                            builder.setMessage("Peso ingresado es mayor al peso Total");
                            builder.setCancelable(true);
                            builder.setNeutralButton("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    builder.dismiss();
                                }
                            });
                            builder.show();
                        }
                    });

                    txtObservacionPeso.setVisibility(View.GONE);
                    txtObservacionPeso.setText("");
                } else if (valorIngresado < validacionMenor) {
                    //Toast.makeText(getContext(), "El peso es menor al recolectado", Toast.LENGTH_SHORT).show();
                    btnInformacion.setVisibility(View.VISIBLE);
                    btnInformacion.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            builder = new DialogBuilder(getContext());
                            builder.setMessage("Peso ingresado es menor al peso Total");
                            builder.setCancelable(true);
                            builder.setNeutralButton("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    builder.dismiss();
                                }
                            });
                            builder.show();
                        }
                    });

                    txtObservacionPeso.setVisibility(View.GONE);
                    txtObservacionPeso.setText("");

                } else {
                    txtObservacionPeso.setText("Observacion de Peso");
                    txtObservacionPeso.setVisibility(View.VISIBLE);
                    btnInformacion.setVisibility(View.GONE);
                }
            } else {
                btnInformacion.setVisibility(View.GONE);
                txtObservacionPeso.setVisibility(View.GONE);
                txtObservacionPeso.setText(" ");
            }
        }
    }


    public void setMakePhoto(Integer code) {
        if(dialogAgregarFotografias!=null){
            dialogAgregarFotografias.setMakePhoto(code);
        }
    }

    public void obtenerObservaciones(){
        DtoManifiestoPlantaObservacion d;
        d = MyApp.getDBO().manifiestoPlantaObservacionesDao().obtenerObservaciones(idAppManifiesto);
        if(d!=null){
            txtotraNovedad.setText(d.getObservacionOtra());
        }
    }


    public void guardarObservaciones(){
        DtoManifiestoPlantaObservacion p = new DtoManifiestoPlantaObservacion();
        p.setIdManifiesto(idAppManifiesto);
        p.setObservacionOtra(txtotraNovedad.getText().toString());
        MyApp.getDBO().manifiestoPlantaObservacionesDao().saveOrUpdate(p);
    }

    public void bloquerAdiciona(){
        Integer estadoManifiesto = MyApp.getDBO().manifiestoPlantaDao().obtenerEstadoManifiesto(idAppManifiesto);
        if(estadoManifiesto == 3){
            txtotraNovedad.setEnabled(false);
            btnAgregarFirma.setEnabled(false);
        }
    }

}

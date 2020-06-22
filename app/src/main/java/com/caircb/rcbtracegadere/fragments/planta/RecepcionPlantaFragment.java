package com.caircb.rcbtracegadere.fragments.planta;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.adapters.ManifiestoNovedadBaseAdapterRecepcionR;
import com.caircb.rcbtracegadere.database.dao.ManifiestoFileDao;
import com.caircb.rcbtracegadere.database.entity.ManifiestoDetallePesosEntity;
import com.caircb.rcbtracegadere.dialogs.DialogAgregarFotografias;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.dialogs.DialogFirma;
import com.caircb.rcbtracegadere.helpers.MyConstant;
import com.caircb.rcbtracegadere.models.ItemFile;
import com.caircb.rcbtracegadere.models.RowItemHojaRutaCatalogo;
import com.caircb.rcbtracegadere.tasks.UserRegistrarPlanta;
import com.caircb.rcbtracegadere.utils.Utils;

import java.util.Date;
import java.util.List;

public class RecepcionPlantaFragment extends LinearLayout  {


    Activity _activity;
    ImageView imgFirmaPlanta;
    LinearLayout btnAgregarFirma, btnCancelar, btnGuardar;
    EditText txtPeso,txtNovedad;
    TextView txtFirmaPlanta, txtPesoRecolectado;
    DialogFirma dialogFirma;
    private Integer idManifiesto;
    boolean bloquear;
    Window window;
    Bitmap firmaConfirmada;
    List<RowItemHojaRutaCatalogo> novedadfrecuentes;
    RecyclerView recyclerViewLtsManifiestoObservaciones;
    ManifiestoNovedadBaseAdapterRecepcionR recyclerAdapterNovedades;
    DialogAgregarFotografias dialogAgregarFotografias;
    DialogBuilder builder;
    double pesoT=0;
    private boolean firma = false;




public RecepcionPlantaFragment(Context context,Integer idAppManifiesto){
    super(context);
    this.idManifiesto = idAppManifiesto;
    View.inflate(context, R.layout.fragment_recoleccion_planta, this);
    init();
    load();

}

    private void init() {
        recyclerViewLtsManifiestoObservaciones = this.findViewById(R.id.LtsManifiestoObservaciones);
        txtPeso = this.findViewById(R.id.txtPeso);
        btnGuardar = this.findViewById(R.id.btnGuardar);
        btnCancelar = this.findViewById(R.id.btnCancelar);

        btnAgregarFirma = this.findViewById(R.id.btnAgregarFirma);
        imgFirmaPlanta = this.findViewById(R.id.imgFirmaPlanta);
        txtFirmaPlanta = this.findViewById(R.id.txtFirmaPlanta);
        txtPesoRecolectado = this.findViewById(R.id.txtPesoRecolectado);

        txtNovedad = this.findViewById(R.id.txtNovedad);

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
                                MyApp.getDBO().manifiestoFileDao().saveOrUpdate(idManifiesto, ManifiestoFileDao.FOTO_FIRMA_RECEPCION_PLATA, Utils.encodeTobase64(bitmap), MyConstant.STATUS_RECEPCION_PLANTA);

                            }else{
                                txtFirmaPlanta.setVisibility(View.VISIBLE);
                                imgFirmaPlanta.setVisibility(View.GONE);
                                firma=false;
                                MyApp.getDBO().manifiestoFileDao().saveOrUpdate(idManifiesto, ManifiestoFileDao.FOTO_FIRMA_RECEPCION_PLATA, null,MyConstant.STATUS_RECEPCION_PLANTA);
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

        txtPeso.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    validarPesos();
                }
            }
        });
    }

    private void load(){
        novedadfrecuentes = MyApp.getDBO().manifiestoObservacionFrecuenteDao().fetchHojaRutaCatalogoNovedaFrecuenteRecepcion(idManifiesto);
        recyclerViewLtsManifiestoObservaciones.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerAdapterNovedades = new ManifiestoNovedadBaseAdapterRecepcionR(getContext(), novedadfrecuentes, bloquear,idManifiesto);

        recyclerAdapterNovedades.setOnClickReaload(new ManifiestoNovedadBaseAdapterRecepcionR.OnReloadAdater() {
            @Override
            public void onShowM(final Integer catalogoID, final Integer position) {
                builder = new DialogBuilder(getContext());
                builder.setMessage("¿Seguro que desea desactivar el registro, automáticamente se borrarán las evidencias?");
                builder.setCancelable(true);
                builder.setPositiveButton("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recyclerAdapterNovedades.registarCheckItemCatalogo(idManifiesto,catalogoID,false);
                        recyclerAdapterNovedades.deleteFotosByItem(idManifiesto, catalogoID, position);

                        novedadfrecuentes.get(position).setNumFotos(0);
                        novedadfrecuentes.get(position).setEstadoChek(false);

                        recyclerAdapterNovedades.notifyDataSetChanged();
                        recyclerViewLtsManifiestoObservaciones.setAdapter(recyclerAdapterNovedades);
                        builder.dismiss();
                    }
                });
                builder.setNegativeButton("NO", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        //novedadfrecuentes.get(position).setEstadoChek(true);
                        //recyclerAdapterNovedades.registarCheckItemCatalogo(idManifiesto,novedadfrecuentes.get(position).getId(),true);
                        recyclerAdapterNovedades.notifyDataSetChanged();
                        recyclerViewLtsManifiestoObservaciones.setAdapter(recyclerAdapterNovedades);
                        builder.dismiss();
                    }
                });
                builder.show();
            }
        });
        recyclerAdapterNovedades.setOnClickOpenFotografias(new ManifiestoNovedadBaseAdapterRecepcionR.OnClickOpenFotografias() {
            @Override
            public void onShow(Integer catalogoID, final Integer position) {
                if(dialogAgregarFotografias==null){
                    dialogAgregarFotografias = new DialogAgregarFotografias(getContext(),idManifiesto,catalogoID, ManifiestoFileDao.FOTO_NOVEDAD_FRECUENTE_RECEPCION, MyConstant.STATUS_RECEPCION_PLANTA);
                    dialogAgregarFotografias.setCancelable(false);
                    dialogAgregarFotografias.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialogAgregarFotografias.setOnAgregarFotosListener(new DialogAgregarFotografias.OnAgregarFotosListener() {
                        @Override
                        public void onSuccessful(Integer cantidad) {
                            if(dialogAgregarFotografias!=null && dialogAgregarFotografias.isShowing()){
                                dialogAgregarFotografias.dismiss();
                                dialogAgregarFotografias=null;

                                novedadfrecuentes.get(position).setNumFotos(cantidad);
                                novedadfrecuentes.get(position).setEstadoChek(true);
                                //poner estado check en true...
                                recyclerAdapterNovedades.registarCheckItemCatalogo(idManifiesto,novedadfrecuentes.get(position).getId(),true);
                                //refress cambios...
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

        ItemFile f = MyApp.getDBO().manifiestoFileDao().consultarFile(idManifiesto, ManifiestoFileDao.FOTO_FIRMA_RECEPCION_PLATA,MyConstant.STATUS_RECEPCION_PLANTA);
        if(f != null){
            Bitmap imagen = Utils.StringToBitMap(f.getFile());
            txtFirmaPlanta.setVisibility(View.GONE);
            imgFirmaPlanta.setVisibility(View.VISIBLE);
            imgFirmaPlanta.setImageBitmap(imagen);
            firmaConfirmada = imagen;
            firma=true;

        }


        List<ManifiestoDetallePesosEntity> bultos = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarBultosManifiesto(idManifiesto);
        if(bultos.size()>0){
            for (ManifiestoDetallePesosEntity p:bultos){
                pesoT= pesoT+ p.getValor();

            }

        }

        txtPesoRecolectado.setText(String.valueOf(pesoT));
    }

    public void setMakePhoto(Integer code) {
        if(dialogAgregarFotografias!=null){
            dialogAgregarFotografias.setMakePhoto(code);
        }
    }
    public boolean validaNovedadesFrecuentesPendienteFotos(){
        return MyApp.getDBO().manifiestoObservacionFrecuenteDao().existeNovedadFrecuentePendienteFotoPlanta(idManifiesto)>0;
    }

    private void validarPesos(){
    double validacion=(Double.parseDouble(txtPesoRecolectado.getText().toString())*0.03)+Double.parseDouble(txtPesoRecolectado.getText().toString());
    double valorIngresado = Double.parseDouble(txtPeso.getText().toString());

        if(valorIngresado>validacion){
        //Toast.makeText(getContext(), "El peso es mayor al recolectado", Toast.LENGTH_SHORT).show();
        txtNovedad.setText("Peso ingresador mayor al peso Total");
    }else{
        //Toast.makeText(getContext(), "El peso es menor al recolectado", Toast.LENGTH_SHORT).show();
        txtNovedad.setText("Peso ingresador menor al peso Total");
    }

    }

    public boolean validaExisteFirma(){
        return !firma;
    }

    private boolean validarPeso = false;
    public boolean validaPeso(){
        if(txtPeso.getText().toString().equals("")){
            return validarPeso = true;
        }else{
            validarPeso = false;
        }
        return  validarPeso;
    }


    public double guardar(){
        MyApp.getDBO().manifiestoDao().updateManifiestoFechaPlanta(idManifiesto,new Date());
        return Double.parseDouble(txtPeso.getText().toString());
    }



}

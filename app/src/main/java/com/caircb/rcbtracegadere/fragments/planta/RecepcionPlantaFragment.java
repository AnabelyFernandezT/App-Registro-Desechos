package com.caircb.rcbtracegadere.fragments.planta;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.caircb.rcbtracegadere.database.entity.ManifiestoDetalleEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoDetallePesosEntity;
import com.caircb.rcbtracegadere.dialogs.DialogAgregarFotografias;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.dialogs.DialogFirma;
import com.caircb.rcbtracegadere.helpers.MyConstant;
import com.caircb.rcbtracegadere.models.ItemFile;
import com.caircb.rcbtracegadere.models.RowItemHojaRutaCatalogo;
import com.caircb.rcbtracegadere.models.RowItemManifiestoDetalle;
import com.caircb.rcbtracegadere.models.response.DtoManifiesto;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoPlantaObservacion;
import com.caircb.rcbtracegadere.tasks.UserRegistrarPlanta;
import com.caircb.rcbtracegadere.utils.Utils;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

public class RecepcionPlantaFragment extends LinearLayout  {

    ImageView imgFirmaPlanta;
    LinearLayout btnAgregarFirma, btnCancelar, btnGuardar;
    EditText txtPeso,txtNovedad,txtotraNovedad;
    TextView txtFirmaPlanta, txtPesoRecolectado;
    DialogFirma dialogFirma;
    private Integer idManifiesto;
    Window window;
    Bitmap firmaConfirmada;
    List<RowItemHojaRutaCatalogo> novedadfrecuentes;
    RecyclerView recyclerViewLtsManifiestoObservaciones;
    ManifiestoNovedadBaseAdapterRecepcionR recyclerAdapterNovedades;
    DialogAgregarFotografias dialogAgregarFotografias;
    DialogBuilder builder;
    double pesoT=0;
    private boolean firma = false, observacion = false;
    LinearLayout btnEvidenciaObservacion, lnlCountPhoto;
    TextView txtCountPhoto;



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
        txtotraNovedad = this.findViewById(R.id.txtotraNovedad);

        txtNovedad = this.findViewById(R.id.txtNovedad);
        btnEvidenciaObservacion = this.findViewById(R.id.btnEvidenciaObservacion);
        lnlCountPhoto = this.findViewById(R.id.lnlCountPhoto);
        txtCountPhoto = this.findViewById(R.id.txtCountPhoto);

        btnEvidenciaObservacion.setVisibility(View.GONE);

        Integer numeroFotos = MyApp.getDBO().manifiestoFileDao().obtenerCantidadFotografiabyManifiestoCatalogo(idManifiesto, -1, ManifiestoFileDao.FOTO_NOVEDAD_FRECUENTE_RECEPCION );
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

        btnEvidenciaObservacion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialogAgregarFotografias = new DialogAgregarFotografias(getContext(), idManifiesto, -2, ManifiestoFileDao.FOTO_FOTO_RECOLECCION_PLANTA, MyConstant.STATUS_RECEPCION_PLANTA);
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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().equals("")){
                    btnEvidenciaObservacion.setVisibility(View.VISIBLE);
                }else {
                    btnEvidenciaObservacion.setVisibility(View.GONE);
                }
            }
        });
        obtenerObservaciones();

        /*
        txtPeso.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){

                    guardarObservaciones();
                }
            }
        });
         */

        /*
        txtPeso.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validarPesos();
            }
        });
         */
        /*
        txtNovedad.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    guardarObservaciones();
                }
            }
        });
         */
        /*
        txtotraNovedad.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    guardarObservaciones();
                }
            }
        });
         */
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

    private void load(){

        ItemFile f = MyApp.getDBO().manifiestoFileDao().consultarFile(idManifiesto, ManifiestoFileDao.FOTO_FIRMA_RECEPCION_PLATA,MyConstant.STATUS_RECEPCION_PLANTA);
        if(f != null){
            Bitmap imagen = Utils.StringToBitMap(f.getFile());
            txtFirmaPlanta.setVisibility(View.GONE);
            imgFirmaPlanta.setVisibility(View.VISIBLE);
            imgFirmaPlanta.setImageBitmap(imagen);
            firmaConfirmada = imagen;
            firma=true;

        }

        List<RowItemManifiestoDetalle> bultos = MyApp.getDBO().manifiestoDetalleDao().fetchHojaRutaDetallebyIdManifiesto2(idManifiesto);

       // ManifiestoDetalleEntity bultos = MyApp.getDBO().manifiestoDetalleDao().fecthConsultarManifiestoDetallebyID(idManifiesto);

        if(bultos.size()>0){
            for (RowItemManifiestoDetalle p:bultos){
                pesoT= pesoT+ p.getPeso();
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

    /*
    private void validarPesos(){
        DecimalFormat df = new DecimalFormat("#.####");
        if(txtPeso.getText().toString().equals("")) {}
            else{
            double validacion = (Double.parseDouble(txtPesoRecolectado.getText().toString()) * 0.03) + Double.parseDouble(txtPesoRecolectado.getText().toString());
            double validacionMenor = ( Double.parseDouble(txtPesoRecolectado.getText().toString()) -Double.parseDouble(txtPesoRecolectado.getText().toString()) * 0.03);
            double valorIngresado = Double.parseDouble(txtPeso.getText().toString());


    if(!String.valueOf(valorIngresado).equals(txtPesoRecolectado.getText())){
        if(valorIngresado>validacion){
            //Toast.makeText(getContext(), "El peso es mayor al recolectado", Toast.LENGTH_SHORT).show();
            txtPeso.setError("MARGEN DE ERROR DE PESO MAYOR AL 3%");
            txtNovedad.setText(" ");
        }else if(valorIngresado<validacionMenor){
            //Toast.makeText(getContext(), "El peso es menor al recolectado", Toast.LENGTH_SHORT).show();
            txtPeso.setError("MARGEN DE ERROR DE PESO MENOR AL 3%");
            txtNovedad.setText(" ");
        }else{
            Double diferencia = Math.abs((Double.parseDouble(txtPesoRecolectado.getText().toString())) - valorIngresado);
            txtNovedad.setText("EXISTE DIFERENCIA DE "+ df.format(diferencia) +" KG" );
        }
    }else{
        txtNovedad.setText(" ");
    }

        }
    }
     */

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

    public String obtenerNovedad(){
        return (txtNovedad.getText().toString()==null?"":txtNovedad.getText().toString());
    }

    public String obtenerOtraNovedad(){
        return (txtotraNovedad.getText().toString()==null? "": txtotraNovedad.getText().toString());
    }

    public void obtenerObservaciones(){
        DtoManifiestoPlantaObservacion d;
        d = MyApp.getDBO().manifiestoPlantaObservacionesDao().obtenerObservaciones(idManifiesto);
        if(d!=null){
            txtPeso.setText(""+d.getPesoPlanta());
            txtNovedad.setText(d.getObservacionPeso());
            txtotraNovedad.setText(d.getObservacionOtra());
        }
    }

    /*
    public void guardarObservaciones(){
        if(txtPeso.getText().toString().equals("")) {}
        else{
            DtoManifiestoPlantaObservacion p = new DtoManifiestoPlantaObservacion();
            p.setIdManifiesto(idManifiesto);
            p.setPesoPlanta(Double.parseDouble(txtPeso.getText().toString()));
            p.setObservacionPeso(txtNovedad.getText().toString());
            p.setObservacionOtra(txtotraNovedad.getText().toString());
            MyApp.getDBO().manifiestoPlantaObservacionesDao().saveOrUpdate(p);
        }
    }
     */

}

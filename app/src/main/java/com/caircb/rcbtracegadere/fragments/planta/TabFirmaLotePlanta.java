package com.caircb.rcbtracegadere.fragments.planta;

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

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.dao.ManifiestoFileDao;
import com.caircb.rcbtracegadere.database.entity.RecepcionQrPlantaEntity;
import com.caircb.rcbtracegadere.dialogs.DialogAgregarFotografias;
import com.caircb.rcbtracegadere.dialogs.DialogFirma;
import com.caircb.rcbtracegadere.helpers.MyConstant;
import com.caircb.rcbtracegadere.models.ItemFile;
import com.caircb.rcbtracegadere.models.ItemFoto;
import com.caircb.rcbtracegadere.models.ItemQrDetallePlanta;
import com.caircb.rcbtracegadere.models.response.DtoManifiestoPlantaObservacion;
import com.caircb.rcbtracegadere.utils.Utils;

import java.util.List;

public class TabFirmaLotePlanta extends LinearLayout {

    DialogAgregarFotografias dialogAgregarFotografias;
    EditText txtPeso, txtNovedad, txtotraNovedad;
    LinearLayout btnEvidenciaObservacion, lnlCountPhoto;
    LinearLayout btnAgregarFirma, btnCancelar, btnGuardar, btnInformacion;
    TextView txtCountPhoto, txtPesoPlanta;
    TextView txtFirmaPlanta, txtPesoRecolectado, txtObservacionPeso;
    ImageView imgFirmaPlanta;
    private List<ItemQrDetallePlanta> rowItems;
    private RecepcionQrPlantaEntity recepcionQrPlantaEntity;
    Bitmap firmaConfirmada;
    private boolean firma = false, observacion = false;
    Window window;
    DialogFirma dialogFirma;
    boolean info = false;

    public TabFirmaLotePlanta(Context context) {
        super(context);
        View.inflate(context, R.layout.recepcion_lote_tab_firma, this);
        /*this.idAppManifiesto = manifiestoID;
        this.pesajePendiente = pesajePendiente;
        init();
        loadData();
        validarPesoExtra();
        bloquerAdiciona();*/
        init();
        loadData();
    }

    private void init() {

        recepcionQrPlantaEntity=MyApp.getDBO().recepcionQrPlantaDao().fetchHojaRutaQrPlanta();

        txtotraNovedad = this.findViewById(R.id.txtotraNovedad);

        btnEvidenciaObservacion = this.findViewById(R.id.btnEvidenciaObservacion);
        lnlCountPhoto = this.findViewById(R.id.lnlCountPhoto);
        txtCountPhoto = this.findViewById(R.id.txtCountPhoto);

        btnAgregarFirma = this.findViewById(R.id.btnAgregarFirma);
        txtFirmaPlanta = this.findViewById(R.id.txtFirmaPlanta);
        imgFirmaPlanta = this.findViewById(R.id.imgFirmaPlanta);

        btnAgregarFirma.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogFirma == null) {
                    dialogFirma = new DialogFirma(getContext());
                    dialogFirma.setTitle("SU FIRMA");
                    dialogFirma.setCancelable(false);
                    dialogFirma.setOnSignaturePadListener(new DialogFirma.OnSignaturePadListener() {
                        @Override
                        public void onSuccessful(Bitmap bitmap) {
                            dialogFirma.dismiss();
                            dialogFirma = null;
                            String[] array= recepcionQrPlantaEntity.getNumerosManifiesto().split(",");
                            int idManifiestoPrimero=Integer.parseInt(array[0]);
                            if (bitmap != null) {
                                txtFirmaPlanta.setVisibility(View.GONE);
                                imgFirmaPlanta.setVisibility(View.VISIBLE);
                                imgFirmaPlanta.setImageBitmap(bitmap);
                                firmaConfirmada = bitmap;
                                firma = true;
                                MyApp.getDBO().manifiestoFileDao().saveOrUpdate(idManifiestoPrimero, ManifiestoFileDao.FOTO_FIRMA_RECEPCION_PLATA, Utils.encodeTobase64(bitmap), MyConstant.STATUS_RECEPCION_PLANTA);

                            } else {
                                txtFirmaPlanta.setVisibility(View.VISIBLE);
                                imgFirmaPlanta.setVisibility(View.GONE);
                                firma = false;
                                MyApp.getDBO().manifiestoFileDao().saveOrUpdate(idManifiestoPrimero, ManifiestoFileDao.FOTO_FIRMA_RECEPCION_PLATA, null, MyConstant.STATUS_RECEPCION_PLANTA);
                            }
                        }

                        @Override
                        public void onCanceled() {
                            dialogFirma.dismiss();
                            dialogFirma = null;

                        }
                    });
                    dialogFirma.show();
                }
            }
        });

        btnEvidenciaObservacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] array= recepcionQrPlantaEntity.getNumerosManifiesto().split(",");
                final int idManifiestoPrimero=Integer.parseInt(array[0]);
                dialogAgregarFotografias = new DialogAgregarFotografias(getContext(), idManifiestoPrimero, -1, ManifiestoFileDao.FOTO_FOTO_ADICIONAL_PLANTA, MyConstant.STATUS_RECEPCION_PLANTA);
                dialogAgregarFotografias.setCancelable(false);
                dialogAgregarFotografias.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogAgregarFotografias.setOnAgregarFotosListener(new DialogAgregarFotografias.OnAgregarFotosListener() {
                    @Override
                    public void onSuccessful(Integer cantidad) {
                        lnlCountPhoto.setVisibility(View.VISIBLE);
                        txtCountPhoto.setText(String.valueOf(cantidad));
                        if (array.length>1){
                            List<ItemFoto> fotos =  MyApp.getDBO().manifiestoFileDao().obtenerFotografiabyManifiestoCatalogo(idManifiestoPrimero,-1,ManifiestoFileDao.FOTO_FOTO_ADICIONAL_PLANTA,MyConstant.STATUS_RECEPCION_PLANTA);
                            for (int j=1;j<array.length;j++){
                                int idManifiestoRecorrido=Integer.parseInt(array[j].replace(" ",""));
                                for (int i=0;i<fotos.size();i++){
                                    MyApp.getDBO().manifiestoFileDao().saveOrUpdate(idManifiestoRecorrido, -1, fotos.get(i).getCode(), fotos.get(i).getTipo(), fotos.get(i).getFoto(),MyConstant.STATUS_RECEPCION_PLANTA);
                                }
                            }
                        }
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
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")) {
                    btnEvidenciaObservacion.setVisibility(View.VISIBLE);
                    guardarObservaciones();
                } else {
                    btnEvidenciaObservacion.setVisibility(View.GONE);
                }
            }
        });


    }
    private void loadData() {
        String firmaUsuario = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_firma_usuario") == null ? "" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_firma_usuario");
        System.out.println(firmaUsuario);
        String[] array= recepcionQrPlantaEntity.getNumerosManifiesto().split(",");
        int idManifiestoPrimero=Integer.parseInt(array[0]);
        if (!firmaUsuario.equals("")) {
            Bitmap imagen = Utils.StringToBitMap(firmaUsuario);
            txtFirmaPlanta.setVisibility(View.GONE);
            imgFirmaPlanta.setVisibility(View.VISIBLE);
            imgFirmaPlanta.setImageBitmap(imagen);
            firmaConfirmada = imagen;
            btnAgregarFirma.setEnabled(false);
            firma = true;
            if (array.length>1){
                for (String s : array) {
                    int idManifiestoConsulta = Integer.parseInt(s.replace(" ",""));
                    MyApp.getDBO().manifiestoFileDao().saveOrUpdate(idManifiestoConsulta, ManifiestoFileDao.FOTO_FIRMA_RECEPCION_PLATA, Utils.encodeTobase64(imagen), MyConstant.STATUS_RECEPCION_PLANTA);
                }
            }else {
                MyApp.getDBO().manifiestoFileDao().saveOrUpdate(idManifiestoPrimero, ManifiestoFileDao.FOTO_FIRMA_RECEPCION_PLATA, Utils.encodeTobase64(imagen), MyConstant.STATUS_RECEPCION_PLANTA);
            }

        } else {
            ItemFile f = MyApp.getDBO().manifiestoFileDao().consultarFile(idManifiestoPrimero, ManifiestoFileDao.FOTO_FIRMA_RECEPCION_PLATA, MyConstant.STATUS_RECEPCION_PLANTA);
            if (f != null) {
                Bitmap imagen = Utils.StringToBitMap(f.getFile());
                txtFirmaPlanta.setVisibility(View.GONE);
                imgFirmaPlanta.setVisibility(View.VISIBLE);
                imgFirmaPlanta.setImageBitmap(imagen);
                firmaConfirmada = imagen;
                firma = true;
            }
        }
    }

    public void setMakePhoto(Integer code) {
        if (dialogAgregarFotografias != null) {
            dialogAgregarFotografias.setMakePhoto(code);
        }
    }

    public void guardarObservaciones() {
        String[] array= recepcionQrPlantaEntity.getNumerosManifiesto().split(",");
        int idManifiestoPrimero=Integer.parseInt(array[0]);
        if (array.length>1){
            for (String s : array) {
                int idManifiestoConsulta = Integer.parseInt(s.replace(" ",""));
                DtoManifiestoPlantaObservacion p = new DtoManifiestoPlantaObservacion();
                p.setIdManifiesto(idManifiestoConsulta);
                p.setObservacionOtra(txtotraNovedad.getText().toString());
                MyApp.getDBO().manifiestoPlantaObservacionesDao().saveOrUpdate(p);
            }
        }else {
            DtoManifiestoPlantaObservacion p = new DtoManifiestoPlantaObservacion();
            p.setIdManifiesto(idManifiestoPrimero);
            p.setObservacionOtra(txtotraNovedad.getText().toString());
            MyApp.getDBO().manifiestoPlantaObservacionesDao().saveOrUpdate(p);
        }
    }

    public boolean validarNovedad() {
        String txtObservacion = txtotraNovedad.getText().toString();
        String numeroFotos = txtCountPhoto.getText().toString();
        if (txtObservacion.equals("")) {
            observacion = true;
        } else {
            if (numeroFotos.equals("0")) {
                observacion = false;
            } else {
                observacion = true;
            }
        }
        return observacion;
    }

    public boolean validarInformacion() {
        if (firma) {
            info = true;
        }
        return info;
    }

    public String sendObservacion() {
        return txtotraNovedad.getText().toString();
    }

}

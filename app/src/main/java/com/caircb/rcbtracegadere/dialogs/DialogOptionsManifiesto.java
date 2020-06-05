package com.caircb.rcbtracegadere.dialogs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.adapters.ManifiestoNovedadBaseAdapterR;
import com.caircb.rcbtracegadere.database.entity.ManifiestoEntity;
import com.caircb.rcbtracegadere.generics.MyDialog;
import com.caircb.rcbtracegadere.models.RowItemHojaRutaCatalogo;
import com.caircb.rcbtracegadere.utils.Utils;

import java.util.List;

public class DialogOptionsManifiesto extends MyDialog {

    Activity _activity;
    ImageView imgFirmaTecnico, imgFirmaTecnicoTrasnsportista;
    LinearLayout btnAgregarFirmaTransportista, btnCancelar, btnGuardar;
    EditText txtPeso;
    TextView txtFirmaMensajeTransportista;
    DialogFirma dialogFirma;
    private Integer idManifiesto;
    boolean bloquear;
    Window window;
    Bitmap firmaConfirmada;
    List<RowItemHojaRutaCatalogo> novedadfrecuentes;
    RecyclerView recyclerViewLtsManifiestoObservaciones;
    ManifiestoNovedadBaseAdapterR recyclerAdapterNovedades;
    DialogAgregarFotografias dialogAgregarFotografias;

    public DialogOptionsManifiesto(@NonNull Context context, Integer idManifiesto){
        super(context, R.layout.dialog_options_manifiesto);
        this._activity = (Activity)context;
        this.idManifiesto = idManifiesto;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getView());
        init();
        load();
    }

    private void init(){
        recyclerViewLtsManifiestoObservaciones = getView().findViewById(R.id.LtsManifiestoObservaciones);
        txtPeso = getView().findViewById(R.id.txtPeso);
        btnGuardar = getView().findViewById(R.id.btnGuardar);
        btnCancelar = getView().findViewById(R.id.btnCancelar);

        btnAgregarFirmaTransportista = getView().findViewById(R.id.btnAgregarFirmaTransportista);
        imgFirmaTecnico= getView().findViewById(R.id.imgFirmaTecnico);
        imgFirmaTecnicoTrasnsportista = getView().findViewById(R.id.imgFirmaTecnicoTrasnsportista);
        txtFirmaMensajeTransportista = getView().findViewById(R.id.txtFirmaMensajeTransportista);

        btnAgregarFirmaTransportista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialogFirma==null) {
                    dialogFirma = new DialogFirma(getActivity());
                    dialogFirma.setTitle("SU FIRMA");
                    dialogFirma.setCancelable(false);
                    dialogFirma.setOnSignaturePadListener(new DialogFirma.OnSignaturePadListener() {
                        @Override
                        public void onSuccessful(Bitmap bitmap) {
                            dialogFirma.dismiss();
                            dialogFirma=null;
                            if(bitmap!=null){
                                txtFirmaMensajeTransportista.setVisibility(View.GONE);
                                imgFirmaTecnicoTrasnsportista.setVisibility(View.VISIBLE);
                                imgFirmaTecnicoTrasnsportista.setImageBitmap(bitmap);
                                firmaConfirmada = bitmap;

                            }else{
                                txtFirmaMensajeTransportista.setVisibility(View.VISIBLE);
                                imgFirmaTecnicoTrasnsportista.setVisibility(View.GONE);
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

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txtPeso.getText().toString().equals("")){
                    messageBox("Debe ingresar un peso!");
                }else {
                    if(firmaConfirmada!=null){
                        String unicodeImg = Utils.encodeTobase64(firmaConfirmada);
                        MyApp.getDBO().manifiestoDao().updateFirmaWithPesoTransportista(idManifiesto, Double.valueOf(txtPeso.getText().toString()), "IMG_FIRMA" + System.currentTimeMillis() + ".jpg", unicodeImg);
                        dismiss();
                        messageBox("Registrado correctamente!!");
                    }else {
                        messageBox("Debe ingresar una firma!");
                    }
                }

            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void load(){
        novedadfrecuentes = MyApp.getDBO().manifiestoObservacionFrecuenteDao().fetchHojaRutaCatalogoNovedaFrecuente(idManifiesto);
        recyclerViewLtsManifiestoObservaciones.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerAdapterNovedades = new ManifiestoNovedadBaseAdapterR(getContext(), novedadfrecuentes, bloquear,1);
        recyclerAdapterNovedades.setOnClickOpenFotografias(new ManifiestoNovedadBaseAdapterR.OnClickOpenFotografias() {
            @Override
            public void onShow(Integer catalogoID, final Integer position) {
                if(dialogAgregarFotografias==null){
                    dialogAgregarFotografias = new DialogAgregarFotografias(getActivity(),idManifiesto,catalogoID,3);
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
    }

    public void setMakePhoto(Integer code) {
        if(dialogAgregarFotografias!=null){
            dialogAgregarFotografias.setMakePhoto(code);
        }
    }
}

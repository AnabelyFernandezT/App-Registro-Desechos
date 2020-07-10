package com.caircb.rcbtracegadere.fragments.planta;


import android.app.Activity;
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
import com.caircb.rcbtracegadere.database.entity.ManifiestoDetallePesosEntity;
import com.caircb.rcbtracegadere.dialogs.DialogAgregarFotografias;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.dialogs.DialogFirma;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnRecyclerTouchListener;
import com.caircb.rcbtracegadere.helpers.MyConstant;
import com.caircb.rcbtracegadere.models.ItemFile;
import com.caircb.rcbtracegadere.models.RowItemHojaRutaCatalogo;
import com.caircb.rcbtracegadere.models.RowItemNoRecoleccion;
import com.caircb.rcbtracegadere.utils.Utils;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabManifiestoAdicionalFragment extends Fragment {

    private static final String ARG_PARAM1 = "idAppManifiesto";
    private static final String ARG_PARAM2 = "Manifiestobloqueado";

    View view;
    DialogAgregarFotografias dialogAgregarFotografias;

    Integer idAppManifiesto;
    RecyclerView recyclerViewLtsManifiestoObservaciones;
    Activity _activity;
    ImageView imgFirmaPlanta;
    LinearLayout btnAgregarFirma, btnCancelar, btnGuardar;
    EditText txtPeso,txtNovedad,txtotraNovedad;
    TextView txtFirmaPlanta, txtPesoRecolectado;
    DialogFirma dialogFirma;
    private Integer idManifiesto;
    boolean bloquear;
    Window window;
    Bitmap firmaConfirmada;
    List<RowItemHojaRutaCatalogo> novedadfrecuentes;
    ManifiestoNovedadBaseAdapterRecepcionR recyclerAdapterNovedades;
    DialogBuilder builder;
    double pesoT=0;
    private boolean firma = false;
    LinearLayout btnEvidenciaObservacion, lnlCountPhoto;
    TextView txtCountPhoto;
    boolean info = false;

    public static TabManifiestoAdicionalFragment newInstance (Integer manifiestoID){
        TabManifiestoAdicionalFragment f = new TabManifiestoAdicionalFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, manifiestoID);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idAppManifiesto= getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_recoleccion_planta_adicional, container, false);
        init();
        loadData();
        return  view;
    }

    private void init(){
        recyclerViewLtsManifiestoObservaciones = view.findViewById(R.id.LtsManifiestoObservaciones);
        txtPeso = view.findViewById(R.id.txtPeso);
        btnGuardar = view.findViewById(R.id.btnGuardar);
        btnCancelar = view.findViewById(R.id.btnCancelar);

        btnAgregarFirma = view.findViewById(R.id.btnAgregarFirma);
        imgFirmaPlanta = view.findViewById(R.id.imgFirmaPlanta);
        txtFirmaPlanta = view.findViewById(R.id.txtFirmaPlanta);
        txtPesoRecolectado = view.findViewById(R.id.txtPesoRecolectado);
        txtotraNovedad = view.findViewById(R.id.txtotraNovedad);

        txtNovedad = view.findViewById(R.id.txtNovedad);
        btnEvidenciaObservacion = view.findViewById(R.id.btnEvidenciaObservacion);
        lnlCountPhoto = view.findViewById(R.id.lnlCountPhoto);
        txtCountPhoto = view.findViewById(R.id.txtCountPhoto);

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
                    dialogFirma = new DialogFirma(getActivity());
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
                                //MyApp.getDBO().manifiestoFileDao().saveOrUpdate(idManifiesto, ManifiestoFileDao.FOTO_FIRMA_RECEPCION_PLATA, Utils.encodeTobase64(bitmap), MyConstant.STATUS_RECEPCION_PLANTA);

                            }else{
                                txtFirmaPlanta.setVisibility(View.VISIBLE);
                                imgFirmaPlanta.setVisibility(View.GONE);
                                firma=false;
                                //MyApp.getDBO().manifiestoFileDao().saveOrUpdate(idManifiesto, ManifiestoFileDao.FOTO_FIRMA_RECEPCION_PLATA, null,MyConstant.STATUS_RECEPCION_PLANTA);
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
                dialogAgregarFotografias = new DialogAgregarFotografias(getActivity(), idAppManifiesto, -1, ManifiestoFileDao.FOTO_FOTO_ADICIONAL_PLANTA, MyConstant.STATUS_RECEPCION_PLANTA);
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

    }
    public boolean validarInformacion(){
        if(!firma){
            info = true;
        }
        return info;
    }
    private void loadData(){
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
    }

    public void setMakePhoto(Integer code) {
        if(dialogAgregarFotografias!=null){
            dialogAgregarFotografias.setMakePhoto(code);
        }
    }

}

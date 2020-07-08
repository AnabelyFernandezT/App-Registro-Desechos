package com.caircb.rcbtracegadere.fragments.GestorAlterno;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.dao.ManifiestoFileDao;
import com.caircb.rcbtracegadere.database.entity.LotePadreEntity;
import com.caircb.rcbtracegadere.dialogs.DialogFirma;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnCameraListener;
import com.caircb.rcbtracegadere.helpers.MyConstant;
import com.caircb.rcbtracegadere.models.ItemFile;
import com.caircb.rcbtracegadere.tasks.UserRegistrarPlanta;
import com.caircb.rcbtracegadere.utils.Utils;


public class ManifiestoGestorFragment extends MyFragment implements OnCameraListener, View.OnClickListener {
    LinearLayout btnManifiestoCancel, btnManifiestoNext;

    private static final String ARG_PARAM1 = "manifiestoID";
    LinearLayout btnAgregarFirma, btnCancelar, btnGuardar;
    RecepcionGestorFragment manifiestoGestor;
    Integer idAppManifiesto;
    UserRegistrarPlanta userRegistrarPlanta;
    ImageView imgFirmaPlanta;
    TextView txtFirmaPlanta, txtPesoTotal;
    DialogFirma dialogFirma;
    Bitmap firmaConfirmada;
    private boolean firma = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnManifiestoCancel:
                setNavegate(HojaRutaAsignadaGestorFragment.newInstance());
                break;
            case R.id.btnManifiestoNext:
                if(manifiestoGestor.validaNovedadesFrecuentesPendienteFotos()){
                    messageBox("Las novedades frecuentes seleccionadas deben contener al menos una fotografia de evidencia");
                    return;
                }
                if(manifiestoGestor.validaExisteFirma()){
                    messageBox("Se requiere firma  ");
                    return;
                }

                if(manifiestoGestor.validaPeso()){
                    messageBox("Se requiere que ingrese el peso");
                    return;
                }


                double peso = manifiestoGestor.guardar();
                userRegistrarPlanta = new UserRegistrarPlanta(getActivity(),idAppManifiesto,peso);
                userRegistrarPlanta.setOnRegisterListener(new UserRegistrarPlanta.OnRegisterListener() {
                    @Override
                    public void onSuccessful() {
                       messageBox("Datos Guardados");
                       setNavegate(HojaRutaAsignadaGestorFragment.newInstance());
                    }
                });
                userRegistrarPlanta.execute();

                break;
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            idAppManifiesto = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public void onCameraResult(int requestCode, int resultCode, Intent data) {
        if(manifiestoGestor!=null && ((requestCode>=301 && requestCode<=304) ||(requestCode>=201 && requestCode<=204))) {
            manifiestoGestor.setMakePhoto(requestCode);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setView(inflater.inflate(R.layout.fragment_hoja_ruta_gestor, container, false));
        init();

        return getView();
    }

    public void init(){

        btnAgregarFirma = getView().findViewById(R.id.btnAgregarFirma);
        imgFirmaPlanta = getView().findViewById(R.id.imgFirmaPlanta);
        txtFirmaPlanta = getView().findViewById(R.id.txtFirmaPlanta);
        txtPesoTotal= getView().findViewById(R.id.txtPesoTotal);
        btnManifiestoCancel = getView().findViewById(R.id.btnManifiestoCancel);
        btnManifiestoCancel.setOnClickListener(this);
        btnManifiestoNext = getView().findViewById(R.id.btnManifiestoNext);
        btnManifiestoNext.setOnClickListener(this);
        manifiestoGestor = new RecepcionGestorFragment(getActivity(),idAppManifiesto);

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
                                MyApp.getDBO().manifiestoFileDao().saveOrUpdate(idAppManifiesto, ManifiestoFileDao.FOTO_FIRMA_GESTORES, Utils.encodeTobase64(bitmap), MyConstant.STATUS_GESTORES);

                            }else{
                                txtFirmaPlanta.setVisibility(View.VISIBLE);
                                imgFirmaPlanta.setVisibility(View.GONE);
                                firma=false;
                                MyApp.getDBO().manifiestoFileDao().saveOrUpdate(idAppManifiesto, ManifiestoFileDao.FOTO_FIRMA_GESTORES, null,MyConstant.STATUS_GESTORES);
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
        loadData();
    }

    private void loadData(){
        LotePadreEntity manifiestoPadre = MyApp.getDBO().lotePadreDao().fetchConsultarCatalogoEspecifico(idAppManifiesto);
        if(manifiestoPadre!=null){
            txtPesoTotal.setText(manifiestoPadre.getTotal().toString());
            ItemFile f = MyApp.getDBO().manifiestoFileDao().consultarFile(idAppManifiesto, ManifiestoFileDao.FOTO_FIRMA_GESTORES,MyConstant.STATUS_GESTORES);
            if(f != null){
                Bitmap imagen = Utils.StringToBitMap(f.getFile());
                txtFirmaPlanta.setVisibility(View.GONE);
                imgFirmaPlanta.setVisibility(View.VISIBLE);
                imgFirmaPlanta.setImageBitmap(imagen);

            }

        }
    }


    public static ManifiestoGestorFragment newInstance() {
        return new ManifiestoGestorFragment();
    }

    public static ManifiestoGestorFragment newInstance(Integer manifiestoID) {
        ManifiestoGestorFragment f= new ManifiestoGestorFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_PARAM1,manifiestoID);
        f.setArguments(b);
        return f;
    }
}

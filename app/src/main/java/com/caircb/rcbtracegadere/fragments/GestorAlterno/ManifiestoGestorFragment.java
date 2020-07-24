package com.caircb.rcbtracegadere.fragments.GestorAlterno;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
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
import com.caircb.rcbtracegadere.database.entity.LotePadreEntity;
import com.caircb.rcbtracegadere.dialogs.DialogAgregarFotografias;
import com.caircb.rcbtracegadere.dialogs.DialogFirma;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnCameraListener;
import com.caircb.rcbtracegadere.helpers.MyConstant;
import com.caircb.rcbtracegadere.models.ItemFile;
import com.caircb.rcbtracegadere.tasks.UserConsultarLotePadreTask;
import com.caircb.rcbtracegadere.tasks.UserRegistrarGestorAlternoTask;
import com.caircb.rcbtracegadere.tasks.UserRegistrarPlanta;
import com.caircb.rcbtracegadere.utils.Utils;


public class ManifiestoGestorFragment extends MyFragment implements OnCameraListener, View.OnClickListener {
    LinearLayout btnManifiestoCancel, btnManifiestoNext;

    private static final String ARG_PARAM1 = "manifiestoID";
    LinearLayout btnAgregarFirma;
    RecepcionGestorFragment manifiestoGestor;
    Integer idAppManifiesto;
    ImageView imgFirmaPlanta;
    TextView txtFirmaPlanta, txtPesoTotal;
    DialogFirma dialogFirma;
    Bitmap firmaConfirmada;
    private boolean firma = false;
    EditText txtCorreo,txtNovedad, txPesoRecolectado;
    UserRegistrarGestorAlternoTask registrarGestorAlterno;
    LinearLayout btnEvidenciaNovedadEncontrada, lnlCountPhoto;
    TextView txtCountPhoto;
    DialogAgregarFotografias dialogAgregarFotografias;
    boolean info = false;
    Window window;
    UserConsultarLotePadreTask consultarLotePadre;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnManifiestoCancel:
                setNavegate(HojaRutaAsignadaGestorFragment.newInstance());
                break;
            case R.id.btnManifiestoNext:
               /* if(manifiestoGestor.validaNovedadesFrecuentesPendienteFotos()){
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
                }*/


               // double peso = manifiestoGestor.guardar();
                validarInformacion();

                break;
        }

    }

    private void validarInformacion(){
        if(txPesoRecolectado.getText().toString().equals("")){
            messageBox("Debe ingresar un peso recolectado.!");
            info = true;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(txtCorreo.getText().toString()).matches()){
            txtCorreo.setError("Ingrese un correo valido.!");
            info = true;
        }
        if(!firma){
            messageBox("Debe registrar la firma.!");
            info = true;
        }

        if(!info){
            String novedad = txtNovedad.getText().toString();
            Double peso = Double.parseDouble(txPesoRecolectado.getText().toString());
            String correo = txtCorreo.getText().toString();

            registrarGestorAlterno = new UserRegistrarGestorAlternoTask(getActivity(),idAppManifiesto,novedad,peso,correo);
            registrarGestorAlterno.setmOnRegisterAlternoListener(new UserRegistrarGestorAlternoTask.onRegisterAlternoListener() {
                @Override
                public void onSussfull() {
                    messageBox("Registrado correctamente!!");
                    setNavegate(HomeGestorAlternoFragment.create());
                    MyApp.getDBO().parametroDao().saveOrUpdate("current_placa_transportista",""+0);
                }
            });
            datosManifiestosAsignados();
            registrarGestorAlterno.execute();

        }
    }

    private void datosManifiestosAsignados(){
        consultarLotePadre = new UserConsultarLotePadreTask(getActivity());
        consultarLotePadre.execute();
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
        }else if ((requestCode>=401 && requestCode<=404) || (requestCode>=401 && requestCode<=404) ){
            setMakePhoto(requestCode);
        }
    }

    public void setMakePhoto(Integer code) {
        if(dialogAgregarFotografias!=null){
            dialogAgregarFotografias.setMakePhoto(code);
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
        txtNovedad = getView().findViewById(R.id.txtNovedad);
        txtCorreo = getView().findViewById(R.id.txtCorreo);
        btnEvidenciaNovedadEncontrada = getView().findViewById(R.id.btnEvidenciaNovedadFrecuente);
        lnlCountPhoto = getView().findViewById(R.id.lnlCountPhoto);
        txtCountPhoto = getView().findViewById(R.id.txtCountPhoto);
        btnEvidenciaNovedadEncontrada.setVisibility(View.GONE);
        txPesoRecolectado = getView().findViewById(R.id.txPesoRecolectado);

        Integer numeroFotos = MyApp.getDBO().manifiestoFileDao().obtenerCantidadFotografiabyManifiestoCatalogo(idAppManifiesto, -1, ManifiestoFileDao.FOTO_NOVEDAD_GESTOR );
        if(numeroFotos != null && numeroFotos > 0){
            lnlCountPhoto.setVisibility(View.VISIBLE);
            txtCountPhoto.setText(String.valueOf(numeroFotos));
            btnEvidenciaNovedadEncontrada.setVisibility(View.VISIBLE);
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

        btnEvidenciaNovedadEncontrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAgregarFotografias = new DialogAgregarFotografias(getActivity(), idAppManifiesto, -1, ManifiestoFileDao.FOTO_NOVEDAD_GESTOR, MyConstant.STATUS_GESTORES);
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

        txtNovedad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().equals("")){
                    btnEvidenciaNovedadEncontrada.setVisibility(View.VISIBLE);
                }else {
                    btnEvidenciaNovedadEncontrada.setVisibility(View.GONE);
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
                firma = true;

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

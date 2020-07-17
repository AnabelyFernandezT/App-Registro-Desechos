package com.caircb.rcbtracegadere.fragments.recolector.MotivoNoRecoleccion;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.dao.ManifiestoFileDao;
import com.caircb.rcbtracegadere.database.entity.ManifiestoEntity;
import com.caircb.rcbtracegadere.database.entity.TecnicoEntity;
import com.caircb.rcbtracegadere.dialogs.DialogFirma;
import com.caircb.rcbtracegadere.helpers.MyConstant;
import com.caircb.rcbtracegadere.models.ItemFile;
import com.caircb.rcbtracegadere.models.response.DtoIdentificacion;
import com.caircb.rcbtracegadere.tasks.UserConsultarCedulaTask;
import com.caircb.rcbtracegadere.utils.ImagenUtils;
import com.caircb.rcbtracegadere.utils.Utils;

public class TabManifiestoGeneralNoRecoleccion extends LinearLayout {

    private Integer idAppManifiesto,estadoManifiesto;

    private Boolean bloquear;
    private  Integer tipoPaquete=null;
    //private String audio="";
    private String tiempoAudio;
    private String numeroManifiesto="";
    private View view;
    private TextView txtNumManifiesto,txtClienteNombre,txtClienteIdentificacion,txtClienteTelefono,txtClienteDireccion,txtClienteProvincia,
            txtClienteCanton,txtTransReco,txtTransRecoAux,txtFirmaOperador1,txtFirmaOperador2,txtOperadorRecolector
            ,txtEmpresaDestinatario,txtempresaTransportista,txtFirmaMensajeTransportista;



    private LinearLayout btnAgregarFirmaTransportista,btnAgregarFirmaOperador1,btmAgregarOperador2;




    private ImageView  imgFirmaTecnicoTrasnsportista, imgFirmaOperador1, imgFirmaOperadorRecolector;
    //private int flag =0, flagT=0;
    private DialogFirma dialogFirma;
    private ImagenUtils imagenUtils;
    private boolean firmaTransportista=false, firmaTecnicoGenerador=false;

    String identificacion, nombre, correo, telefono;
    UserConsultarCedulaTask userConsultarCedulaTask;

    public TabManifiestoGeneralNoRecoleccion(Context context, Integer idAppManifiesto,Integer estado) {
        super(context);
        View.inflate(context, R.layout.tab_manifiesto_general_no_recoleccion, this);
        this.idAppManifiesto=idAppManifiesto;
        this.estadoManifiesto= estado;
        init();
        loadDataManifiesto();
    }

    private void init(){
        txtNumManifiesto = this.findViewById(R.id.txtNumManifiesto) ;
        txtClienteNombre = this.findViewById(R.id.txtClienteNombre);
        txtClienteIdentificacion =this.findViewById(R.id.txtClienteIdentificacion);
        txtClienteTelefono = this.findViewById(R.id.txtClienteTelefono);
        txtClienteDireccion= this.findViewById(R.id.txtClienteDireccion);
        txtClienteProvincia= this.findViewById(R.id.txtClienteProvincia);
        txtClienteCanton= this.findViewById(R.id.txtClienteCanton);
        txtTransReco= this.findViewById(R.id.txtTransReco);
        txtTransRecoAux= this.findViewById(R.id.txtTransRecoAux);
        btnAgregarFirmaTransportista = this.findViewById(R.id.btnAgregarFirmaTransportista);

        imgFirmaTecnicoTrasnsportista = this.findViewById(R.id.imgFirmaTecnicoTrasnsportista);
        txtFirmaMensajeTransportista = this.findViewById(R.id.txtFirmaMensajeTransportista);


        txtEmpresaDestinatario = this.findViewById(R.id.txtEmpresaDestinatario);
        txtempresaTransportista = this.findViewById(R.id.txtempresaTransportista);

        btnAgregarFirmaOperador1=this.findViewById(R.id.btnAgregarFirmaOperador1);
        btmAgregarOperador2=this.findViewById(R.id.btnAgregarFirmaOperadorRecolector);
        txtFirmaOperador1=this.findViewById(R.id.txtFirmaMensajeOperador1);
        txtFirmaOperador2 = this.findViewById(R.id.txtFirmaMensajeOperadorRecolector);
        imgFirmaOperadorRecolector = this.findViewById(R.id.imgFirmaOperadorRecolector);
        imgFirmaOperador1 = this.findViewById(R.id.imgFirmaOperador1);
        txtOperadorRecolector = this.findViewById(R.id.txtOperadorRecolector);


    /*
        btnBuscarIdentificacion.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //MyApp.getDBO().tecnicoDao().deleteTecnico();
                TecnicoEntity tecnico = MyApp.getDBO().tecnicoDao().fechConsultaTecnicobyIdentidad(txtRespEntregaIdentificacion.getText().toString());
                if(tecnico!=null){
                    txtRespEntregaNombre.setText(tecnico.getNombre());
                    txtRespEntregaCorreo.setText(tecnico.getCorreo());
                    txtRespEntregaTelefono.setText(tecnico.getTelefono());
                    txtRespEntregaCorreo.setError(null);
                    txtRespEntregaCorreo.setEnabled(tecnico.getCorreo()!=null && tecnico.getCorreo().length()==0);
                    txtRespEntregaTelefono.setEnabled(tecnico.getTelefono()!=null && tecnico.getTelefono().length()==0);

                    MyApp.getDBO().manifiestoDao().updateGenerador(idAppManifiesto,tecnico.get_id());
                    //txtGenTecIdentificacion.setEnabled(false);


                }else{
                    //consultar en servicio remoto...
                    userConsultarCedulaTask = new UserConsultarCedulaTask(getContext(),txtRespEntregaIdentificacion.getText().toString());
                    userConsultarCedulaTask.setOnResponseListener(new UserConsultarCedulaTask.OnResponseListener() {
                        @Override
                        public void onSuccessful(DtoIdentificacion identificacion) {
                            txtRespEntregaNombre.setText(identificacion.getEcuatorianoNombre());
                            txtRespEntregaCorreo.requestFocus();
                            txtRespEntregaCorreo.setEnabled(true);
                            txtRespEntregaTelefono.setEnabled(true);
                            txtRespEntregaCorreo.setError(null);

                            //insert datos en dbo local...
                            Long idTecnico = MyApp.getDBO().tecnicoDao().saveOrUpdate(idAppManifiesto,txtRespEntregaIdentificacion.getText().toString(),identificacion.getEcuatorianoNombre(),"","");

                            MyApp.getDBO().manifiestoDao().updateGenerador(idAppManifiesto,idTecnico.intValue());
                            //MyApp.getDBO().manifiestoDao().updateGenerador(idAppManifiesto,txtGenTecIdentificacion.getText().toString());

                            if(!Patterns.EMAIL_ADDRESS.matcher(txtRespEntregaCorreo.getText().toString()).matches()){
                                txtRespEntregaCorreo.setError("Ingrese un correo valido");
                            }
                        }

                        @Override
                        public void onFailure() {
                            txtRespEntregaNombre.requestFocus();
                            txtRespEntregaNombre.setEnabled(true);
                            txtRespEntregaCorreo.setEnabled(true);
                            txtRespEntregaTelefono.setEnabled(true);
                            txtRespEntregaCorreo.setError(null);
                            Toast.makeText(getContext(), "El numero de cedula "+txtRespEntregaIdentificacion.getText().toString()+ " no genero resultados", Toast.LENGTH_SHORT).show();
                        }
                    });
                    userConsultarCedulaTask.execute();
                }

            }
        });*/
/*
        btnAgregarFirma.setOnClickListener(new OnClickListener() {
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
                                txtFirmaMensaje.setVisibility(View.GONE);
                                imgFirmaTecnico.setVisibility(View.VISIBLE);
                                imgFirmaTecnico.setImageBitmap(bitmap);
                                MyApp.getDBO().manifiestoFileDao().saveOrUpdate(idAppManifiesto, ManifiestoFileDao.FOTO_FIRMA_TECNICO_GENERADOR, Utils.encodeTobase64(bitmap), MyConstant.STATUS_RECOLECCION);
                                firmaTecnicoGenerador=true;
                            }else{
                                txtFirmaMensaje.setVisibility(View.VISIBLE);
                                imgFirmaTecnico.setVisibility(View.GONE);
                                firmaTecnicoGenerador=false;
                                MyApp.getDBO().manifiestoFileDao().saveOrUpdate(idAppManifiesto, ManifiestoFileDao.FOTO_FIRMA_TECNICO_GENERADOR, null, MyConstant.STATUS_RECOLECCION);
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
        });*/

        btnAgregarFirmaOperador1.setOnClickListener(new OnClickListener() {
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
                                txtFirmaOperador1.setVisibility(View.GONE);
                                imgFirmaOperador1.setVisibility(View.VISIBLE);
                                imgFirmaOperador1.setImageBitmap(bitmap);
                                MyApp.getDBO().manifiestoFileDao().saveOrUpdate(idAppManifiesto, ManifiestoFileDao.FOTO_FIRMA_OPERADOR1, Utils.encodeTobase64(bitmap), MyConstant.STATUS_NO_RECOLECCION);
                                //firmaTecnicoGenerador=true;
                            }else{
                                txtFirmaOperador1.setVisibility(View.VISIBLE);
                                imgFirmaOperador1.setVisibility(View.GONE);
                                //firmaTecnicoGenerador=false;
                                MyApp.getDBO().manifiestoFileDao().saveOrUpdate(idAppManifiesto, ManifiestoFileDao.FOTO_FIRMA_OPERADOR1, null, MyConstant.STATUS_NO_RECOLECCION);
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

        btmAgregarOperador2.setOnClickListener(new OnClickListener() {
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
                                txtFirmaOperador2.setVisibility(View.GONE);
                                imgFirmaOperadorRecolector.setVisibility(View.VISIBLE);
                                imgFirmaOperadorRecolector.setImageBitmap(bitmap);
                                MyApp.getDBO().manifiestoFileDao().saveOrUpdate(idAppManifiesto, ManifiestoFileDao.FOTO_FIRMA_OPERADOR2, Utils.encodeTobase64(bitmap), MyConstant.STATUS_NO_RECOLECCION);
                                // firmaTecnicoGenerador=true;
                            }else{
                                txtFirmaOperador2.setVisibility(View.VISIBLE);
                                imgFirmaOperadorRecolector.setVisibility(View.GONE);
                                //firmaTecnicoGenerador=false;
                                MyApp.getDBO().manifiestoFileDao().saveOrUpdate(idAppManifiesto, ManifiestoFileDao.FOTO_FIRMA_OPERADOR2, null, MyConstant.STATUS_NO_RECOLECCION);
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


        btnAgregarFirmaTransportista.setOnClickListener(new OnClickListener() {
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
                                txtFirmaMensajeTransportista.setVisibility(View.GONE);
                                imgFirmaTecnicoTrasnsportista.setVisibility(View.VISIBLE);
                                imgFirmaTecnicoTrasnsportista.setImageBitmap(bitmap);
                                MyApp.getDBO().manifiestoFileDao().saveOrUpdate(idAppManifiesto, ManifiestoFileDao.FOTO_FIRMA_TRANSPORTISTA, Utils.encodeTobase64(bitmap), MyConstant.STATUS_NO_RECOLECCION);
                                firmaTransportista=true;
                            }else{
                                txtFirmaMensajeTransportista.setVisibility(View.VISIBLE);
                                imgFirmaTecnicoTrasnsportista.setVisibility(View.GONE);
                                MyApp.getDBO().manifiestoFileDao().saveOrUpdate(idAppManifiesto, ManifiestoFileDao.FOTO_FIRMA_TRANSPORTISTA, null, MyConstant.STATUS_NO_RECOLECCION);
                                firmaTransportista=false;
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

/*
        txtRespEntregaCorreo.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                    if(!Patterns.EMAIL_ADDRESS.matcher(txtRespEntregaCorreo.getText().toString()).matches()){
                        txtRespEntregaCorreo.setError("Ingrese un correo valido");
                    }

                if(!hasFocus){
                    MyApp.getDBO().tecnicoDao().saveOrUpdate(idAppManifiesto,txtRespEntregaIdentificacion.getText().toString(),
                            txtRespEntregaNombre.getText().toString(),txtRespEntregaCorreo.getText().toString(),txtRespEntregaTelefono.getText().toString());
                }
            }
        });
        txtRespEntregaTelefono.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    MyApp.getDBO().tecnicoDao().saveOrUpdate(idAppManifiesto,txtRespEntregaIdentificacion.getText().toString(),
                            txtRespEntregaNombre.getText().toString(),txtRespEntregaCorreo.getText().toString(),txtRespEntregaTelefono.getText().toString());
                }
            }
        });
        txtRespEntregaIdentificacion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtRespEntregaNombre.setText("");
                txtRespEntregaCorreo.setText("");
                txtRespEntregaTelefono.setText("");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/

visible();

    }

    /*private void validarTecnico(){
        ManifiestoEntity manifiesto = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(idAppManifiesto);

        if (manifiesto.getTecnicoResponsable().length()<0){
            btnBuscarIdentificacion.setEnabled(false);
            txtGenTecIdentificacion.setEnabled(false);
        }else {
            btnBuscarIdentificacion.setEnabled(true);
            txtGenTecIdentificacion.setEnabled(true);
        }
    }*/

    private void visible (){
        if(estadoManifiesto != 1){

            //btnAgregarFirma.setEnabled(false);
            //btnAgregarFirmaOperador1.setEnabled(false);
            //btmAgregarOperador2.setEnabled(false);
            btnAgregarFirmaTransportista.setEnabled(false);
            //btnBuscarIdentificacion.setEnabled(false);
            //txtRespEntregaIdentificacion.setEnabled(false);
        }
    }

    private void loadDataManifiesto(){
        ManifiestoEntity manifiesto = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(idAppManifiesto);
        TecnicoEntity tecnicoEntity = MyApp.getDBO().tecnicoDao().fechConsultaTecnicobyManifiesto(idAppManifiesto);
        if(manifiesto!=null){
            txtClienteNombre.setText(manifiesto.getNombreCliente());
            txtEmpresaDestinatario.setText(manifiesto.getNombreDestinatario());
            txtNumManifiesto.setText(manifiesto.getNumeroManifiesto());
            txtClienteIdentificacion.setText(manifiesto.getIdentificacionCliente());
            txtClienteDireccion.setText(manifiesto.getDireccionCliente());
            txtClienteProvincia.setText(manifiesto.getProvincia());
            txtClienteCanton.setText(manifiesto.getCanton());
            txtClienteTelefono.setText(manifiesto.getTecnicoTelefono());
            txtTransReco.setText(manifiesto.getConductorNombre());
            txtTransRecoAux.setText(manifiesto.getAuxiliarNombre());


            txtempresaTransportista.setText("GADERE");
            txtOperadorRecolector.setText(manifiesto.getNombreOperadorRecolector());

            if(txtOperadorRecolector.getText().equals("")){
                btmAgregarOperador2.setEnabled(false);
            }
/*
            if(manifiesto.getIdTecnicoGenerador()!=null && manifiesto.getIdTecnicoGenerador()>0){
                TecnicoEntity tec = MyApp.getDBO().tecnicoDao().fechConsultaTecnicobyIdTecnico(manifiesto.getIdTecnicoGenerador());
                if(tec!=null) {
                    txtRespEntregaCorreo.setText(tec.getCorreo());
                    txtRespEntregaTelefono.setText(tec.getTelefono());
                    correo = txtRespEntregaCorreo.getText().toString();
                    txtRespEntregaCorreo.setError(null);
                    if(!correo.isEmpty()){
                        if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
                            txtRespEntregaCorreo.setError("Ingrese un correo valido");
                            txtRespEntregaCorreo.setEnabled(true);

                        }
                    }
                }
            }*/

            //String img = cursor.getString(cursor.getColumnIndex("tecnicoFirmaImg"));
            ItemFile f = MyApp.getDBO().manifiestoFileDao().consultarFile(idAppManifiesto, ManifiestoFileDao.FOTO_FIRMA_TECNICO_GENERADOR,MyConstant.STATUS_RECOLECCION);

           /* if(f != null){
                Bitmap imagen = Utils.StringToBitMap(f.getFile());
                txtFirmaMensaje.setVisibility(View.GONE);
                imgFirmaTecnico.setVisibility(View.VISIBLE);
                imgFirmaTecnico.setImageBitmap(imagen);
                firmaTecnicoGenerador=true;

            }
*/
            f = MyApp.getDBO().manifiestoFileDao().consultarFile(idAppManifiesto, ManifiestoFileDao.FOTO_FIRMA_TRANSPORTISTA,MyConstant.STATUS_NO_RECOLECCION);
            if(f != null){
                Bitmap imagen = Utils.StringToBitMap(f.getFile());
                txtFirmaMensajeTransportista.setVisibility(View.GONE);
                imgFirmaTecnicoTrasnsportista.setVisibility(View.VISIBLE);
                imgFirmaTecnicoTrasnsportista.setImageBitmap(imagen);
                firmaTransportista=true;
            }
            //operador1
            f = MyApp.getDBO().manifiestoFileDao().consultarFile(idAppManifiesto, ManifiestoFileDao.FOTO_FIRMA_OPERADOR1,MyConstant.STATUS_NO_RECOLECCION);
            if(f != null){
                Bitmap imagen = Utils.StringToBitMap(f.getFile());
                txtFirmaOperador1.setVisibility(View.GONE);
                imgFirmaOperador1.setVisibility(View.VISIBLE);
                imgFirmaOperador1.setImageBitmap(imagen);
                //firmaTransportista=true;
            }
//operador2
            f = MyApp.getDBO().manifiestoFileDao().consultarFile(idAppManifiesto, ManifiestoFileDao.FOTO_FIRMA_OPERADOR2,MyConstant.STATUS_NO_RECOLECCION);
            if(f != null){
                Bitmap imagen = Utils.StringToBitMap(f.getFile());
                txtFirmaOperador2.setVisibility(View.GONE);
                imgFirmaOperadorRecolector.setVisibility(View.VISIBLE);
                imgFirmaOperadorRecolector.setImageBitmap(imagen);
                //firmaTransportista=true;
            }

            tipoPaquete = manifiesto.getTipoPaquete();
            //audio = manifiesto.getNovedadAudio();
            tiempoAudio = manifiesto.getTiempoAudio();
            numeroManifiesto =manifiesto.getNumeroManifiesto();
        }

    }


    public Integer getTipoPaquete(){
        return tipoPaquete!=null?(tipoPaquete>0?tipoPaquete:null):null;
    }


    public String getTiempoAudio() {
        return tiempoAudio;
    }

    public String getNumeroManifiesto() {
        return numeroManifiesto;
    }

    public boolean validaExisteFirmaTransportista(){
        boolean resp = firmaTransportista;
        return firmaTransportista;
    }

    //public boolean validaExisteFirmaTecnicoGenerador(){
       // return !firmaTecnicoGenerador;
   // }
/*
    public boolean validaExisteDatosResponsableEntrega(){
        return txtRespEntregaIdentificacion.getText().toString().length()==0?
                (txtRespEntregaIdentificacion.getText().length()>0 && txtRespEntregaNombre.getText().toString().length()>0)
                :true;
    }*/


}

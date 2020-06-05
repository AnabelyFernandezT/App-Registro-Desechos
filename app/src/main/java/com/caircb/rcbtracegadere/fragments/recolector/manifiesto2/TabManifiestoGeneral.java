package com.caircb.rcbtracegadere.fragments.recolector.manifiesto2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.entity.ManifiestoEntity;
import com.caircb.rcbtracegadere.database.entity.TecnicoEntity;
import com.caircb.rcbtracegadere.dialogs.DialogFirma;
import com.caircb.rcbtracegadere.models.response.DtoIdentificacion;
import com.caircb.rcbtracegadere.tasks.UserConsultarCedulaTask;
import com.caircb.rcbtracegadere.utils.ImagenUtils;
import com.caircb.rcbtracegadere.utils.Utils;

public class TabManifiestoGeneral extends LinearLayout {

    private Integer idAppManifiesto;

    private Boolean bloquear;
    private  Integer tipoPaquete=null;
    String audio="";
    String tiempoAudio;
    private View view;
    private TextView txtNumManifiesto,txtClienteNombre,txtClienteIdentificacion,txtClienteTelefono,txtClienteDireccion,txtClienteProvincia,
            txtClienteCanton,txtClienteParroquia,txtTransReco,txtTransRecoAux,txtIdentificacion,txtNombre,txtCorreo,txtTelefono,
            txtGenTecCelular,txtFirmaMensaje,txtEmpresaDestinatario,txtempresaTransportista,txtFirmaMensajeTransportista;

    EditText txtNumManifiestoCliente,txtGenTecIdentificacion,txtGenTecNombre,txtGenTecCorreo,txtGenTecTelefono;

    LinearLayout btnAgregarFirma,btnAgregarFirmaTransportista;

    ImageButton btnNumManifiestoCliente,btnBuscarIdentificacion;

    ImageView imgFirmaTecnico, imgFirmaTecnicoTrasnsportista;
    private int flag =0, flagT=0;
    DialogFirma dialogFirma;
    ImagenUtils imagenUtils;

    String identificacion, nombre, correo, telefono;


    public TabManifiestoGeneral(Context context,Integer idAppManifiesto) {
        super(context);
        View.inflate(context, R.layout.tab_manifiesto_general, this);
        this.idAppManifiesto=idAppManifiesto;
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
        txtClienteParroquia= this.findViewById(R.id.txtClienteParroquia);
        txtTransReco= this.findViewById(R.id.txtTransReco);
        txtTransRecoAux= this.findViewById(R.id.txtTransRecoAux);
        txtIdentificacion= this.findViewById(R.id.txtIdentificacion);
        txtGenTecCelular= this.findViewById(R.id.txtGenTecCelular);
        txtFirmaMensaje= this.findViewById(R.id.txtFirmaMensaje);
        txtGenTecIdentificacion= this.findViewById(R.id.txtGenTecIdentificacion);
        txtGenTecNombre= this.findViewById(R.id.txtGenTecNombre);
        txtGenTecCorreo= this.findViewById(R.id.txtGenTecCorreo);
        txtGenTecTelefono= this.findViewById(R.id.txtGenTecTelefono);
        btnAgregarFirma= this.findViewById(R.id.btnAgregarFirma);
        btnAgregarFirmaTransportista = this.findViewById(R.id.btnAgregarFirmaTransportista);

        btnBuscarIdentificacion= this.findViewById(R.id.btnBuscarIdentificacion);
        imgFirmaTecnico= this.findViewById(R.id.imgFirmaTecnico);
        imgFirmaTecnicoTrasnsportista = this.findViewById(R.id.imgFirmaTecnicoTrasnsportista);
        txtFirmaMensajeTransportista = this.findViewById(R.id.txtFirmaMensajeTransportista);

        txtNumManifiestoCliente =this. findViewById(R.id.txtNumManifiestoCliente);
        btnNumManifiestoCliente = this.findViewById(R.id.btnNumManifiestoCliente);
        txtNumManifiestoCliente.setEnabled(false);

        txtNombre = this.findViewById(R.id.txtNombre);
        txtCorreo = this.findViewById(R.id.txtCorreo);
        txtTelefono=this.findViewById(R.id.txtTelefono);
        txtEmpresaDestinatario = this.findViewById(R.id.txtEmpresaDestinatario);
        txtempresaTransportista = this.findViewById(R.id.txtempresaTransportista);

        btnNumManifiestoCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flag==0){
                    flag = 1;
                    txtNumManifiestoCliente.setEnabled(true);
                }else{
                    flag=0;
                    txtNumManifiestoCliente.setEnabled(false);
                }

            }
        });

        txtGenTecNombre.setEnabled(false);
        txtGenTecCorreo.setEnabled(false);
        txtGenTecTelefono.setEnabled(false);


        btnBuscarIdentificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TecnicoEntity tecnico = MyApp.getDBO().tecnicoDao().fechConsultaTecnicobyIdentidad(txtGenTecIdentificacion.getText().toString());
                if(flagT==0){
                    if (tecnico!=null){
                        txtGenTecNombre.setText(tecnico.getNombre());
                        txtGenTecCorreo.setText(tecnico.getCorreo());
                        txtGenTecTelefono.setText(tecnico.getTelefono());
                        txtGenTecIdentificacion.setEnabled(false);
                        //btnBuscarIdentificacion.setEnabled(false);
                    }else {
                        Toast.makeText(getContext(), "Usuario no registrado", Toast.LENGTH_SHORT).show();
                        txtGenTecNombre.setEnabled(true);
                        txtGenTecCorreo.setEnabled(true);
                        txtGenTecTelefono.setEnabled(true);
                        guardarDatosTecnico();
                    }
                    flagT=1;
                }else {
                    MyApp.getDBO().tecnicoDao().saveOrUpdate(idAppManifiesto,txtGenTecIdentificacion.getText().toString(),nombre,correo,telefono);
                    MyApp.getDBO().manifiestoDao().updateGenerador(idAppManifiesto,txtGenTecIdentificacion.getText().toString());
                    flagT=0;
                }

            }
        });

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
                                txtFirmaMensaje.setVisibility(View.GONE);
                                imgFirmaTecnico.setVisibility(View.VISIBLE);
                                imgFirmaTecnico.setImageBitmap(bitmap);
                                MyApp.getDBO().manifiestoDao().updateFirmaTecnicoGenerador(idAppManifiesto,txtNumManifiesto.getText().toString(),
                                        Utils.encodeTobase64(bitmap));
                            }else{
                                txtFirmaMensaje.setVisibility(View.VISIBLE);
                                imgFirmaTecnico.setVisibility(View.GONE);
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

        btnAgregarFirmaTransportista.setOnClickListener(new View.OnClickListener() {
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
                                MyApp.getDBO().manifiestoDao().updateFirmaTransportsta(idAppManifiesto,txtNumManifiesto.getText().toString(),
                                        Utils.encodeTobase64(bitmap));
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

        validarTecnico();

    }

    private void validarTecnico(){
        ManifiestoEntity manifiesto = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(idAppManifiesto);

        if (manifiesto.getTecnicoResponsable().length()<0){
            btnBuscarIdentificacion.setEnabled(false);
            txtGenTecIdentificacion.setEnabled(false);
        }else {
            btnBuscarIdentificacion.setEnabled(true);
            txtGenTecIdentificacion.setEnabled(true);
        }
    }

    private void guardarDatosTecnico(){

        txtGenTecNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nombre = txtGenTecNombre.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtGenTecCorreo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                correo = txtGenTecCorreo.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtGenTecTelefono.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                telefono = txtGenTecTelefono.getText().toString();
                flagT=1;

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



       //MyApp.getDBO().tecnicoDao().saveOrUpdate(idAppManifiesto,identificacion,nombre,correo,telefono);

    }

    private void loadDataManifiesto(){
        ManifiestoEntity manifiesto = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(idAppManifiesto);
        TecnicoEntity tecnicoEntity = MyApp.getDBO().tecnicoDao().fechConsultaTecnicobyManifiesto(idAppManifiesto);
        if(manifiesto!=null){
            txtClienteNombre.setText(manifiesto.getNombreCliente());
            txtNumManifiestoCliente.setText(manifiesto.getNumManifiestoCliente());
            txtNumManifiesto.setText(manifiesto.getNumeroManifiesto());
            //txtClienteIdentificacion.setText(manifiesto.getIdentificacionCliente());
            txtClienteDireccion.setText(manifiesto.getDireccionCliente());
            txtClienteProvincia.setText(manifiesto.getProvincia());
            txtClienteParroquia.setText(manifiesto.getParroquia());
            txtClienteCanton.setText(manifiesto.getCanton());

            txtTransReco.setText(manifiesto.getConductorNombre());
            txtTransRecoAux.setText(manifiesto.getAuxiliarNombre());

            txtIdentificacion.setText(manifiesto.getTecnicoIdentificacion());
            txtNombre.setText(manifiesto.getTecnicoResponsable());
            txtCorreo.setText(manifiesto.getTecnicoCorreo());
            txtTelefono.setText(manifiesto.getTecnicoTelefono());
            txtGenTecCelular.setText(manifiesto.getTecnicoCelular());

            txtEmpresaDestinatario.setText(manifiesto.getEmpresaDestinataria());
            txtempresaTransportista.setText(manifiesto.getEmpresaTransportista());

            //String tecnicoIdentificacion = cursor.getString(cursor.getColumnIndex("tecnicoIdentificacion"));
           if(manifiesto.getTecnicoIdentificacion() != null){
                txtIdentificacion.setText(manifiesto.getTecnicoIdentificacion());
                txtNombre.setText(manifiesto.getTecnicoResponsable());
                txtGenTecCelular.setText(manifiesto.getTecnicoCelular());
                txtTelefono.setText(manifiesto.getTecnicoTelefono());
                txtCorreo.setText(manifiesto.getTecnicoCorreo());
            }

           if(manifiesto.getIdTecnicoGenerador()!= null){

               txtGenTecIdentificacion.setText(tecnicoEntity.getIdentificacion());
               txtGenTecNombre.setText(tecnicoEntity.getNombre());
               txtGenTecTelefono.setText(tecnicoEntity.getTelefono());
               txtGenTecCorreo.setText(tecnicoEntity.getCorreo());
               btnBuscarIdentificacion.setEnabled(false);
               txtGenTecIdentificacion.setEnabled(false);

           }

            //String img = cursor.getString(cursor.getColumnIndex("tecnicoFirmaImg"));
            if(manifiesto.getTecnicoFirmaImg() != null){
                Bitmap imagen = Utils.StringToBitMap(manifiesto.getTecnicoFirmaImg());
                txtFirmaMensaje.setVisibility(View.GONE);
                imgFirmaTecnico.setVisibility(View.VISIBLE);
                imgFirmaTecnico.setImageBitmap(imagen);

            }

            if(manifiesto.getTransportistaFirmaImg() != null){
                Bitmap imagen = Utils.StringToBitMap(manifiesto.getTransportistaFirmaImg());
                txtFirmaMensajeTransportista.setVisibility(View.GONE);
                imgFirmaTecnicoTrasnsportista.setVisibility(View.VISIBLE);
                imgFirmaTecnicoTrasnsportista.setImageBitmap(imagen);
            }

            tipoPaquete = manifiesto.getTipoPaquete();
            audio = manifiesto.getNovedadAudio();
            tiempoAudio = manifiesto.getTiempoAudio();
        }
    }


    public Integer getTipoPaquete(){
        return tipoPaquete!=null?(tipoPaquete>0?tipoPaquete:null):null;
    }

    public String getAudio() {
        return audio;
    }

    public String getTiempoAudio() {
        return tiempoAudio;
    }

    public Boolean validacionTabGeneral(){
        Boolean validar = true;
        if(txtNumManifiestoCliente.getText().length()>0){
            txtNumManifiestoCliente.setEnabled(false);
        }

        if(txtGenTecNombre.getText().toString()!="" || txtNombre.getText().toString()!=""){
            validar = true;
        }else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setTitle("INFO");
            dialog.setMessage("Necesita registrar al tecnico responsable");
            dialog.setCancelable(false);
            dialog.setNeutralButton("OK", null);
            dialog.show();
            validar = false;

        }

        if(imgFirmaTecnico==null && imgFirmaTecnicoTrasnsportista==null){
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setTitle("INFO");
            dialog.setMessage("Necesita registrar la firma");
            dialog.setCancelable(false);
            dialog.setNeutralButton("OK", null);
            dialog.show();
            validar = false;

        }else{
            validar = true;
        }
        return validar;
    }
}

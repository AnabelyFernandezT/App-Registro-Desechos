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
    private String audio="";
    private String tiempoAudio;
    private String numeroManifiesto="";
    private View view;
    private TextView txtNumManifiesto,txtClienteNombre,txtClienteIdentificacion,txtClienteTelefono,txtClienteDireccion,txtClienteProvincia,
            txtClienteCanton,txtClienteParroquia,txtTransReco,txtTransRecoAux
            ,txtGenTecIdentificacion,txtGenTecNombre,txtGenTecCorreo,txtGenTecTelefono,txtGenTecCelular,txtFirmaMensaje,txtEmpresaDestinatario,txtempresaTransportista,txtFirmaMensajeTransportista;

    private EditText txtNumManifiestoCliente,
            txtRespEntregaIdentificacion,txtRespEntregaNombre,txtRespEntregaCorreo,txtRespEntregaTelefono;

    private LinearLayout btnAgregarFirma,btnAgregarFirmaTransportista;

    private ImageButton btnNumManifiestoCliente,btnBuscarIdentificacion;

    private ImageView imgFirmaTecnico, imgFirmaTecnicoTrasnsportista;
    //private int flag =0, flagT=0;
    private DialogFirma dialogFirma;
    private ImagenUtils imagenUtils;
    private boolean firmaTransportista=false, firmaTecnicoGenerador=false;

    String identificacion, nombre, correo, telefono;
    UserConsultarCedulaTask userConsultarCedulaTask;

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

        txtRespEntregaIdentificacion = this.findViewById(R.id.txtRespEntregaIdentificacion);
        txtRespEntregaNombre = this.findViewById(R.id.txtRespEntregaNombre);
        txtRespEntregaCorreo = this.findViewById(R.id.txtRespEntregaCorreo);
        txtRespEntregaTelefono = this.findViewById(R.id.txtRespEntregaTelefono);
        txtEmpresaDestinatario = this.findViewById(R.id.txtEmpresaDestinatario);
        txtempresaTransportista = this.findViewById(R.id.txtempresaTransportista);

        btnNumManifiestoCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txtNumManifiestoCliente.setEnabled(!txtNumManifiestoCliente.isEnabled());
                if(txtNumManifiestoCliente.isEnabled()) txtNumManifiestoCliente.requestFocus();

            }
        });

        txtRespEntregaNombre.setEnabled(false);
        txtRespEntregaCorreo.setEnabled(false);
        txtRespEntregaTelefono.setEnabled(false);


        btnBuscarIdentificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MyApp.getDBO().tecnicoDao().deleteTecnico();
                TecnicoEntity tecnico = MyApp.getDBO().tecnicoDao().fechConsultaTecnicobyIdentidad(txtRespEntregaIdentificacion.getText().toString());
                if(tecnico!=null){
                    txtRespEntregaNombre.setText(tecnico.getNombre());
                    txtRespEntregaCorreo.setText(tecnico.getCorreo());
                    txtRespEntregaTelefono.setText(tecnico.getTelefono());

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

                            //insert datos en dbo local...
                            Long idTecnico = MyApp.getDBO().tecnicoDao().saveOrUpdate(idAppManifiesto,txtRespEntregaIdentificacion.getText().toString(),identificacion.getEcuatorianoNombre(),"","");

                            MyApp.getDBO().manifiestoDao().updateGenerador(idAppManifiesto,idTecnico.intValue());
                            //MyApp.getDBO().manifiestoDao().updateGenerador(idAppManifiesto,txtGenTecIdentificacion.getText().toString());

                        }

                        @Override
                        public void onFailure() {
                            txtRespEntregaNombre.requestFocus();
                            txtRespEntregaNombre.setEnabled(true);
                            txtRespEntregaCorreo.setEnabled(true);
                            txtRespEntregaTelefono.setEnabled(true);
                            Toast.makeText(getContext(), "El numero de cedula "+txtRespEntregaIdentificacion.getText().toString()+ " no genero resultados", Toast.LENGTH_SHORT).show();
                        }
                    });
                    userConsultarCedulaTask.execute();
                }


                /*if(flagT==0){
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

                    MyApp.getDBO().manifiestoDao().updateGenerador(idAppManifiesto,txtGenTecIdentificacion.getText().toString());
                    flagT=0;
                }
                */

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
                                firmaTecnicoGenerador=true;
                            }else{
                                txtFirmaMensaje.setVisibility(View.VISIBLE);
                                imgFirmaTecnico.setVisibility(View.GONE);
                                firmaTecnicoGenerador=false;
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
                                firmaTransportista=true;
                            }else{
                                txtFirmaMensajeTransportista.setVisibility(View.VISIBLE);
                                imgFirmaTecnicoTrasnsportista.setVisibility(View.GONE);
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

        //validarTecnico();

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
                //flagT=1;

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

            txtGenTecIdentificacion.setText(manifiesto.getTecnicoIdentificacion());
            txtGenTecNombre.setText(manifiesto.getTecnicoResponsable());
            txtGenTecCorreo.setText(manifiesto.getTecnicoCorreo());
            txtGenTecTelefono.setText(manifiesto.getTecnicoTelefono());
            txtGenTecCelular.setText(manifiesto.getTecnicoCelular());

            txtEmpresaDestinatario.setText(manifiesto.getEmpresaDestinataria());
            txtempresaTransportista.setText(manifiesto.getEmpresaTransportista());



            if(manifiesto.getIdTecnicoGenerador()!=null && manifiesto.getIdTecnicoGenerador()>0){
                TecnicoEntity tec = MyApp.getDBO().tecnicoDao().fechConsultaTecnicobyIdTecnico(manifiesto.getIdTecnicoGenerador());
                if(tec!=null) {
                    txtRespEntregaIdentificacion.setText(tec.getIdentificacion());
                    txtRespEntregaNombre.setText(tec.getNombre());
                    txtRespEntregaCorreo.setText(tec.getCorreo());
                    txtRespEntregaTelefono.setText(tec.getTelefono());
                }
            }
            //String tecnicoIdentificacion = cursor.getString(cursor.getColumnIndex("tecnicoIdentificacion"));
           /*if(manifiesto.getTecnicoIdentificacion() != null){
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

           }*/

            //String img = cursor.getString(cursor.getColumnIndex("tecnicoFirmaImg"));
            if(manifiesto.getTecnicoFirmaImg() != null){
                Bitmap imagen = Utils.StringToBitMap(manifiesto.getTecnicoFirmaImg());
                txtFirmaMensaje.setVisibility(View.GONE);
                imgFirmaTecnico.setVisibility(View.VISIBLE);
                imgFirmaTecnico.setImageBitmap(imagen);
                firmaTecnicoGenerador=true;

            }

            if(manifiesto.getTransportistaFirmaImg() != null){
                Bitmap imagen = Utils.StringToBitMap(manifiesto.getTransportistaFirmaImg());
                txtFirmaMensajeTransportista.setVisibility(View.GONE);
                imgFirmaTecnicoTrasnsportista.setVisibility(View.VISIBLE);
                imgFirmaTecnicoTrasnsportista.setImageBitmap(imagen);
                firmaTransportista=true;
            }

            tipoPaquete = manifiesto.getTipoPaquete();
            audio = manifiesto.getNovedadAudio();
            tiempoAudio = manifiesto.getTiempoAudio();
            numeroManifiesto =manifiesto.getNumeroManifiesto();
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

    public String getNumeroManifiesto() {
        return numeroManifiesto;
    }

    public boolean validaExisteFirmaTransportista(){
        return !firmaTransportista;
    }

    public boolean validaExisteFirmaTecnicoGenerador(){
        return !firmaTecnicoGenerador;
    }

    public boolean validaExisteDatosResponsableEntrega(){
        return txtGenTecNombre.getText().toString().length()==0?
                (txtRespEntregaIdentificacion.getText().length()>0 && txtRespEntregaNombre.getText().toString().length()>0)
                :true;
    }

    public boolean validaRequiereNumeroManifiestoCliente(){
        return txtNumManifiestoCliente.isEnabled() && txtNumManifiestoCliente.getText().toString().trim().length()==0;
    }

}

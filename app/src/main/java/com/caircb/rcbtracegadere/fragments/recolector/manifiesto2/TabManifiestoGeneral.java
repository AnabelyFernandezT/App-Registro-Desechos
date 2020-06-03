package com.caircb.rcbtracegadere.fragments.recolector.manifiesto2;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    private Integer manifiestoID,idAppManifiesto;

    private Boolean bloquear;
    private  Integer tipoPaquete=null;
    private View view;
    private TextView txtNumManifiesto,txtClienteNombre,txtClienteIdentificacion,txtClienteTelefono,txtClienteDireccion,txtClienteProvincia,
            txtClienteCanton,txtClienteParroquia,txtTransReco,txtTransRecoAux,txtIdentificacion,txtNombre,txtCorreo,txtTelefono,
            txtGenTecCelular,txtFirmaMensaje,txtEmpresaDestinatario,txtempresaTransportista,txtFirmaMensajeTransportista;

    EditText txtNumManifiestoCliente,txtGenTecIdentificacion,txtGenTecNombre,txtGenTecCorreo,txtGenTecTelefono;

    LinearLayout btnAgregarFirma,btnAgregarFirmaTransportista;

    ImageButton btnNumManifiestoCliente,btnBuscarIdentificacion;

    ImageView imgFirmaTecnico, imgFirmaTecnicoTrasnsportista;
    private int flag =0;
    DialogFirma dialogFirma;
    ImagenUtils imagenUtils;



    public TabManifiestoGeneral(Context context,Integer manifiestoID,Integer tipoManifiestoID) {
        super(context);
        View.inflate(context, R.layout.tab_manifiesto_general, this);
        this.manifiestoID=manifiestoID;
        this.idAppManifiesto=manifiestoID;
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

        btnBuscarIdentificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TecnicoEntity tecnico = MyApp.getDBO().tecnicoDao().fechConsultaTecnicobyIdentidad(txtGenTecIdentificacion.getText().toString());
                if (tecnico!=null){
                    txtGenTecNombre.setText(tecnico.getNombre());
                    txtGenTecCorreo.setText(tecnico.getCorreo());
                    txtGenTecTelefono.setText(tecnico.getTelefono());
                }else {

                    txtGenTecNombre.setText("");
                    txtGenTecCorreo.setText("");
                    txtGenTecTelefono.setText("");

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
                                        bitmap.toString());
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
                                        bitmap.toString());
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


    }
    private void loadDataManifiesto(){
        ManifiestoEntity manifiesto = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(idAppManifiesto);
        if(manifiesto!=null){
            txtClienteNombre.setText(manifiesto.getNombreCliente());
            txtNumManifiestoCliente.setText(manifiesto.getNumManifiestoCliente());
            txtNumManifiesto.setText(manifiesto.getNumeroManifiesto());
            txtClienteIdentificacion.setText(manifiesto.getIdentificacionCliente());
            txtClienteDireccion.setText(manifiesto.getDireccionCliente());
            txtClienteProvincia.setText(manifiesto.getProvincia());
            txtClienteParroquia.setText(manifiesto.getParroquia());
            txtClienteCanton.setText(manifiesto.getCanton());

            txtTransReco.setText(manifiesto.getConductorNombre());
            txtTransRecoAux.setText(manifiesto.getAuxiliarNombre());
            txtIdentificacion.setText(manifiesto.getTecnicoIdentificacion());
            txtNombre.setText(manifiesto.getTecnicoResponsable());
            txtCorreo.setText(manifiesto.getTecnicoCorreo());
            txtTelefono.setText(manifiesto.getTecnicoCorreo());
            txtEmpresaDestinatario.setText(manifiesto.getEmpresaDestinataria());
            txtempresaTransportista.setText(manifiesto.getEmpresaTransportista());

            //String tecnicoIdentificacion = cursor.getString(cursor.getColumnIndex("tecnicoIdentificacion"));
           if(manifiesto.getTecnicoIdentificacion() != null){
                txtGenTecIdentificacion.setText(manifiesto.getTecnicoIdentificacion());
                txtGenTecNombre.setText(manifiesto.getTecnicoResponsable());
                txtGenTecCelular.setText(manifiesto.getTecnicoCelular());
                txtGenTecTelefono.setText(manifiesto.getTecnicoTelefono());
                txtGenTecCorreo.setText(manifiesto.getTecnicoCorreo());
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
        }
    }



}

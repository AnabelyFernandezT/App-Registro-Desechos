package com.caircb.rcbtracegadere.fragments.recolector.manifiesto2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
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
import com.caircb.rcbtracegadere.database.entity.ManifiestoFileEntity;
import com.caircb.rcbtracegadere.database.entity.TecnicoEntity;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.dialogs.DialogFirma;
import com.caircb.rcbtracegadere.helpers.MyConstant;
import com.caircb.rcbtracegadere.models.ItemFile;
import com.caircb.rcbtracegadere.models.response.DtoIdentificacion;
import com.caircb.rcbtracegadere.tasks.UserConsultarCedulaTask;
import com.caircb.rcbtracegadere.utils.ImagenUtils;
import com.caircb.rcbtracegadere.utils.Utils;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.regex.Pattern;

public class TabManifiestoGeneral extends LinearLayout {

    private Integer idAppManifiesto ,estadoManifiesto;
    ManifiestoEntity manifiesto;

    private Boolean bloquear;
    private  Integer tipoPaquete=null;
    //private String audio="";
    private String tiempoAudio;
    private String numeroManifiesto="";
    private View view;
    private TextView txtNumManifiesto,txtClienteNombre,txtClienteIdentificacion,txtClienteTelefono,txtClienteDireccion,txtClienteProvincia,
            txtClienteCanton,txtTransReco,txtTransRecoAux,txtCorreoAlterno, txtOperadorRecolector,txtManifiestoCliente
            ,txtGenTecCorreo,txtGenTecTelefono,txtFirmaMensaje,txtEmpresaDestinatario,txtempresaTransportista,txtFirmaMensajeTransportista, txtFirmaOperador1,txtFirmaOperador2;

    private EditText txtRespEntregaIdentificacion,txtRespEntregaNombre,txtRespEntregaCorreo,txtRespEntregaTelefono;

    private LinearLayout btnAgregarFirma,btnAgregarFirmaTransportista, btnAgregarFirmaOperador1,btmAgregarOperador2;

    private ImageButton btnBuscarIdentificacion;

    private ImageView imgFirmaTecnico, imgFirmaTecnicoTrasnsportista, imgFirmaOperador1, imgFirmaOperadorRecolector;
    //private int flag =0, flagT=0;
    private DialogFirma dialogFirma;
    private ImagenUtils imagenUtils;
    private boolean firmaTransportista=false, firmaTecnicoGenerador=false;
    private CheckBox chkCorreoPrincipal,chkCorreoAlterno;
    String identificacion, nombre, correo, telefono,correoPrincipal ="" ,correoAlterno = "",correoEnvio="";
    UserConsultarCedulaTask userConsultarCedulaTask;
    private FloatingActionButton fab;
    private String sucursal;

    public TabManifiestoGeneral(Context context,Integer idAppManifiesto,Integer estado) {
        super(context);
        View.inflate(context, R.layout.tab_manifiesto_general, this);
        this.idAppManifiesto=idAppManifiesto;
        this.estadoManifiesto= estado;
        init();
        loadDataManifiesto();
    }

    @SuppressLint("RestrictedApi")
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
        txtCorreoAlterno = this.findViewById(R.id.txtCorreoAlterno);
        txtOperadorRecolector = this.findViewById(R.id.txtOperadorRecolector);
        txtManifiestoCliente = this.findViewById(R.id.txtManifiestoCliente);

        txtFirmaMensaje= this.findViewById(R.id.txtFirmaMensaje);
        txtGenTecCorreo= this.findViewById(R.id.txtGenTecCorreo);
        txtGenTecTelefono= this.findViewById(R.id.txtGenTecTelefono);
        btnAgregarFirma= this.findViewById(R.id.btnAgregarFirma);
        btnAgregarFirmaTransportista = this.findViewById(R.id.btnAgregarFirmaTransportista);
        btnAgregarFirmaOperador1=this.findViewById(R.id.btnAgregarFirmaOperador1);
        btmAgregarOperador2=this.findViewById(R.id.btnAgregarFirmaOperadorRecolector);
        txtFirmaOperador1=this.findViewById(R.id.txtFirmaMensajeOperador1);
        txtFirmaOperador2 = this.findViewById(R.id.txtFirmaMensajeOperadorRecolector);



        btnBuscarIdentificacion= this.findViewById(R.id.btnBuscarIdentificacion);
        imgFirmaTecnico= this.findViewById(R.id.imgFirmaTecnico);
        imgFirmaTecnicoTrasnsportista = this.findViewById(R.id.imgFirmaTecnicoTrasnsportista);
        txtFirmaMensajeTransportista = this.findViewById(R.id.txtFirmaMensajeTransportista);
        imgFirmaOperadorRecolector = this.findViewById(R.id.imgFirmaOperadorRecolector);
        imgFirmaOperador1 = this.findViewById(R.id.imgFirmaOperador1);



        txtRespEntregaIdentificacion = this.findViewById(R.id.txtRespEntregaIdentificacion);
        txtRespEntregaNombre = this.findViewById(R.id.txtRespEntregaNombre);
        txtRespEntregaCorreo = this.findViewById(R.id.txtRespEntregaCorreo);
        txtRespEntregaTelefono = this.findViewById(R.id.txtRespEntregaTelefono);
        txtEmpresaDestinatario = this.findViewById(R.id.txtEmpresaDestinatario);
        txtempresaTransportista = this.findViewById(R.id.txtempresaTransportista);

        //txtRespEntregaNombre.setEnabled(true);
        txtRespEntregaCorreo.setEnabled(true);
        txtRespEntregaTelefono.setEnabled(true);

        chkCorreoPrincipal = this.findViewById(R.id.chkCorreoPrincipal);
        chkCorreoAlterno = this.findViewById(R.id.chkCorreoAlterno);

        btnBuscarIdentificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MyApp.getDBO().tecnicoDao().deleteTecnico();
                System.out.println("-->" + txtRespEntregaIdentificacion.getText().toString().equals(""));
                if(txtRespEntregaIdentificacion.getText().toString().equals("")){
                    messageBox("Ingrese una identificación para consultar.");
                }else{
                    InputMethodManager inputMethodManager = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputMethodManager.hideSoftInputFromWindow(txtRespEntregaIdentificacion.getWindowToken(), 0);
                    int estado=0;
                    TecnicoEntity tecnico = MyApp.getDBO().tecnicoDao().fechConsultaTecnicobyIdentidad(txtRespEntregaIdentificacion.getText().toString());
                    //txtRespEntregaNombre.setEnabled(true);
                    if(tecnico!=null && !tecnico.getNombre().equals("")){
                        txtRespEntregaNombre.setText(tecnico.getNombre());
                        txtRespEntregaCorreo.setText(tecnico.getCorreo());
                        txtRespEntregaTelefono.setText(tecnico.getTelefono());
                        txtRespEntregaCorreo.setError(null);
                        txtRespEntregaCorreo.setEnabled(tecnico.getCorreo()!=null && tecnico.getCorreo().length()==0);
                        txtRespEntregaTelefono.setEnabled(tecnico.getTelefono()!=null && tecnico.getTelefono().length()==0);
                        MyApp.getDBO().manifiestoDao().updateGenerador(idAppManifiesto,tecnico.get_id());
                        //txtRespEntregaNombre.setEnabled(false);
                        if (txtRespEntregaNombre.getText().length()<=0){
                            txtRespEntregaNombre.setEnabled(true);
                        }
                        if (txtRespEntregaNombre.getText().length()>0){
                            txtRespEntregaNombre.setEnabled(false);
                        }

                    }else{
                        //consultar en servicio remoto...
                        //boolean estadoCedula = validadorDeCedula(txtRespEntregaIdentificacion.getText().toString());
                        // if(estadoCedula) {
                        userConsultarCedulaTask = new UserConsultarCedulaTask(getContext(), txtRespEntregaIdentificacion.getText().toString());
                        userConsultarCedulaTask.setOnResponseListener(new UserConsultarCedulaTask.OnResponseListener() {
                            @Override
                            public void onSuccessful(DtoIdentificacion identificacion) {
                                txtRespEntregaNombre.setText(identificacion.getEcuatorianoNombre());
                                txtRespEntregaCorreo.requestFocus();
                                txtRespEntregaCorreo.setEnabled(true);
                                txtRespEntregaTelefono.setEnabled(true);
                                txtRespEntregaCorreo.setError(null);

                                if (txtRespEntregaNombre.getText().length() <= 0) {
                                    txtRespEntregaNombre.setEnabled(true);
                                }
                                if (txtRespEntregaNombre.getText().length() > 0) {
                                    txtRespEntregaNombre.setEnabled(false);
                                }
                                //txtRespEntregaNombre.setEnabled(false);
                                //insert datos en dbo local...
                                Long idTecnico = MyApp.getDBO().tecnicoDao().saveOrUpdate(idAppManifiesto, txtRespEntregaIdentificacion.getText().toString(), identificacion.getEcuatorianoNombre(), "", "");

                                MyApp.getDBO().manifiestoDao().updateGenerador(idAppManifiesto, idTecnico.intValue());
                                //MyApp.getDBO().manifiestoDao().updateGenerador(idAppManifiesto,txtGenTecIdentificacion.getText().toString());

                                txtRespEntregaCorreo.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                        if (txtRespEntregaCorreo.getText().length()>1 &&!Patterns.EMAIL_ADDRESS.matcher(txtRespEntregaCorreo.getText().toString()).matches()) {
                                            txtRespEntregaCorreo.setError("Ingrese un correo válido");
                                        }
                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) { }
                                    @Override
                                    public void afterTextChanged(Editable s) { }
                                });

                            }

                            @Override
                            public void onFailure() {
                                txtRespEntregaNombre.requestFocus();
                                //txtRespEntregaNombre.setEnabled(true);
                                txtRespEntregaCorreo.setEnabled(true);
                                txtRespEntregaTelefono.setEnabled(true);
                                txtRespEntregaCorreo.setError(null);
                                Toast.makeText(getContext(), "El numero de cedula " + txtRespEntregaIdentificacion.getText().toString() + " no genero resultados", Toast.LENGTH_SHORT).show();
                            }
                        });
                        userConsultarCedulaTask.execute();
                        // }
                    }
                    if (txtRespEntregaNombre.getText().length()<=0){
                        txtRespEntregaNombre.setEnabled(true);
                    }
                    if (txtRespEntregaNombre.getText().length()>0){
                        txtRespEntregaNombre.setEnabled(false);
                    }
                }
            }
        });

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
                                MyApp.getDBO().manifiestoFileDao().saveOrUpdate(idAppManifiesto, ManifiestoFileDao.FOTO_FIRMA_OPERADOR1, Utils.encodeTobase64(bitmap), MyConstant.STATUS_RECOLECCION);
                                //firmaTecnicoGenerador=true;
                            }else{
                                txtFirmaOperador1.setVisibility(View.VISIBLE);
                                imgFirmaOperador1.setVisibility(View.GONE);
                                //firmaTecnicoGenerador=false;
                                MyApp.getDBO().manifiestoFileDao().saveOrUpdate(idAppManifiesto, ManifiestoFileDao.FOTO_FIRMA_OPERADOR1, null, MyConstant.STATUS_RECOLECCION);
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
                                MyApp.getDBO().manifiestoFileDao().saveOrUpdate(idAppManifiesto, ManifiestoFileDao.FOTO_FIRMA_OPERADOR2, Utils.encodeTobase64(bitmap), MyConstant.STATUS_RECOLECCION);
                               // firmaTecnicoGenerador=true;
                            }else{
                                txtFirmaOperador2.setVisibility(View.VISIBLE);
                                imgFirmaOperadorRecolector.setVisibility(View.GONE);
                                //firmaTecnicoGenerador=false;
                                MyApp.getDBO().manifiestoFileDao().saveOrUpdate(idAppManifiesto, ManifiestoFileDao.FOTO_FIRMA_OPERADOR2, null, MyConstant.STATUS_RECOLECCION);
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
                                MyApp.getDBO().manifiestoFileDao().saveOrUpdate(idAppManifiesto, ManifiestoFileDao.FOTO_FIRMA_TRANSPORTISTA, Utils.encodeTobase64(bitmap), MyConstant.STATUS_RECOLECCION);
                                firmaTransportista=true;
                              /*  ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream .toByteArray();
                                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);*/

                            }else{
                                txtFirmaMensajeTransportista.setVisibility(View.VISIBLE);
                                imgFirmaTecnicoTrasnsportista.setVisibility(View.GONE);
                                MyApp.getDBO().manifiestoFileDao().saveOrUpdate(idAppManifiesto, ManifiestoFileDao.FOTO_FIRMA_TRANSPORTISTA, null, MyConstant.STATUS_RECOLECCION);
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
        txtRespEntregaIdentificacion.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    Long idTecnico =  MyApp.getDBO().tecnicoDao().saveOrUpdate(idAppManifiesto,txtRespEntregaIdentificacion.getText().toString(),
                            txtRespEntregaNombre.getText().toString(),txtRespEntregaCorreo.getText().toString(),txtRespEntregaTelefono.getText().toString());
                    MyApp.getDBO().manifiestoDao().updateGenerador(idAppManifiesto, idTecnico.intValue());
                }

            }
        });

        txtRespEntregaNombre.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    Long idTecnico = MyApp.getDBO().tecnicoDao().saveOrUpdate(idAppManifiesto,txtRespEntregaIdentificacion.getText().toString(),
                            txtRespEntregaNombre.getText().toString(),txtRespEntregaCorreo.getText().toString(),txtRespEntregaTelefono.getText().toString());

                    MyApp.getDBO().manifiestoDao().updateGenerador(idAppManifiesto, idTecnico.intValue());
                }
            }
        });

        txtRespEntregaNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                    Long idTecnico = MyApp.getDBO().tecnicoDao().saveOrUpdate(idAppManifiesto,txtRespEntregaIdentificacion.getText().toString(),
                            txtRespEntregaNombre.getText().toString(),txtRespEntregaCorreo.getText().toString(),txtRespEntregaTelefono.getText().toString());
                    MyApp.getDBO().manifiestoDao().updateGenerador(idAppManifiesto, idTecnico.intValue());
            }
        });

        txtRespEntregaCorreo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                Long idTecnico = MyApp.getDBO().tecnicoDao().saveOrUpdate(idAppManifiesto,txtRespEntregaIdentificacion.getText().toString(),
                        txtRespEntregaNombre.getText().toString(),txtRespEntregaCorreo.getText().toString(),txtRespEntregaTelefono.getText().toString());
                MyApp.getDBO().manifiestoDao().updateGenerador(idAppManifiesto, idTecnico.intValue());
            }
        });

        txtRespEntregaTelefono.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                Long idTecnico = MyApp.getDBO().tecnicoDao().saveOrUpdate(idAppManifiesto,txtRespEntregaIdentificacion.getText().toString(),
                        txtRespEntregaNombre.getText().toString(),txtRespEntregaCorreo.getText().toString(),txtRespEntregaTelefono.getText().toString());
                MyApp.getDBO().manifiestoDao().updateGenerador(idAppManifiesto, idTecnico.intValue());
            }
        });

        txtRespEntregaCorreo.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                    if(!txtRespEntregaCorreo.getText().toString().equals("")&&!Patterns.EMAIL_ADDRESS.matcher(txtRespEntregaCorreo.getText().toString()).matches()){
                        txtRespEntregaCorreo.setError("Ingrese un correo válido");
                    }

                if(!hasFocus){
                    Long idTecnico = MyApp.getDBO().tecnicoDao().saveOrUpdate(idAppManifiesto,txtRespEntregaIdentificacion.getText().toString(),
                            txtRespEntregaNombre.getText().toString(),txtRespEntregaCorreo.getText().toString(),txtRespEntregaTelefono.getText().toString());
                    MyApp.getDBO().manifiestoDao().updateGenerador(idAppManifiesto, idTecnico.intValue());
                }

            }
        });
        txtRespEntregaTelefono.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    Long idTecnico =  MyApp.getDBO().tecnicoDao().saveOrUpdate(idAppManifiesto,txtRespEntregaIdentificacion.getText().toString(),
                            txtRespEntregaNombre.getText().toString(),txtRespEntregaCorreo.getText().toString(),txtRespEntregaTelefono.getText().toString());
                    MyApp.getDBO().manifiestoDao().updateGenerador(idAppManifiesto, idTecnico.intValue());
                }
            }
        });
        txtRespEntregaIdentificacion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtRespEntregaNombre.setText("");
                txtRespEntregaCorreo.setText("");
                txtRespEntregaTelefono.setText("");
            }
            @Override
            public void afterTextChanged(Editable s) {
               /* Long idTecnico =  MyApp.getDBO().tecnicoDao().saveOrUpdate(idAppManifiesto,txtRespEntregaIdentificacion.getText().toString(),
                        txtRespEntregaNombre.getText().toString(),txtRespEntregaCorreo.getText().toString(),txtRespEntregaTelefono.getText().toString());
                MyApp.getDBO().manifiestoDao().updateGenerador(idAppManifiesto, idTecnico.intValue());*/
            }
        });
        visible();
        checkCorreos();
    }

    private void visible (){
        if(estadoManifiesto != 1){
            btnAgregarFirma.setEnabled(false);
            btnAgregarFirmaOperador1.setEnabled(false);
            btmAgregarOperador2.setEnabled(false);
            btnAgregarFirmaTransportista.setEnabled(false);
            btnBuscarIdentificacion.setEnabled(false);
            txtRespEntregaIdentificacion.setEnabled(false);
            chkCorreoAlterno.setClickable(false);
            chkCorreoPrincipal.setEnabled(false);
            chkCorreoPrincipal.setClickable(false);
            chkCorreoAlterno.setEnabled(false);
            txtRespEntregaNombre.setEnabled(false);
            txtRespEntregaCorreo.setEnabled(false);
            txtRespEntregaTelefono.setEnabled(false);
        }
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



    private void loadDataManifiesto(){
        manifiesto = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(idAppManifiesto);
        TecnicoEntity tecnicoEntity = MyApp.getDBO().tecnicoDao().fechConsultaTecnicobyManifiesto(idAppManifiesto);
        if(manifiesto!=null){
            txtClienteNombre.setText(manifiesto.getNombreCliente());
            txtNumManifiesto.setText(manifiesto.getNumeroManifiesto());
            txtClienteIdentificacion.setText(manifiesto.getIdentificacionCliente());
            txtClienteTelefono.setText(manifiesto.getTecnicoTelefono());
            txtClienteDireccion.setText(manifiesto.getDireccionCliente());
            txtClienteProvincia.setText(manifiesto.getProvincia());
            txtClienteCanton.setText(manifiesto.getCanton());
            txtCorreoAlterno.setText(manifiesto.getCorreoAlterno());
            sucursal=manifiesto.getSucursal();
            if(manifiesto.getCorreoAlterno().equals(""))
                chkCorreoAlterno.setEnabled(false);
            if(manifiesto.getCorreos()!=null){
                if(manifiesto.getCorreos().equals("I")){
                    chkCorreoPrincipal.setChecked(true);
                    correoPrincipal = "I";
                }else if (manifiesto.getCorreos().equals("H")){
                    chkCorreoAlterno.setChecked(true);
                    correoAlterno = "H";
                }else  if (manifiesto.getCorreos().equals("H,I")){
                    correoPrincipal = "I";
                    correoAlterno = "H";
                    chkCorreoAlterno.setChecked(true);
                    chkCorreoPrincipal.setChecked(true);
                }
            }

            txtManifiestoCliente.setText(manifiesto.getNumManifiestoCliente());

            txtTransReco.setText(manifiesto.getConductorNombre());
            txtTransRecoAux.setText(manifiesto.getAuxiliarNombre());

            txtGenTecCorreo.setText(manifiesto.getTecnicoCorreo());
            if(manifiesto.getTecnicoCorreo().equals(""))
                chkCorreoPrincipal.setEnabled(false);
            txtGenTecTelefono.setText(manifiesto.getTecnicoTelefono());

            txtEmpresaDestinatario.setText(manifiesto.getEmpresaDestinataria());
            txtempresaTransportista.setText("GADERE");
            txtOperadorRecolector.setText(manifiesto.getNombreOperadorRecolector());
            txtEmpresaDestinatario.setText(manifiesto.getNombreDestinatario());

            if(txtOperadorRecolector.getText().equals("")){btmAgregarOperador2.setEnabled(false);}
            if(txtTransReco.getText().equals("")){btnAgregarFirmaTransportista.setEnabled(false);}
            if(txtTransRecoAux.getText().equals("")){btnAgregarFirmaOperador1.setEnabled(false);}

            if(manifiesto.getIdTecnicoGenerador()!=null && manifiesto.getIdTecnicoGenerador()>0){
                TecnicoEntity tec = MyApp.getDBO().tecnicoDao().fechConsultaTecnicobyIdTecnico(manifiesto.getIdTecnicoGenerador());
                if(tec!=null) {
                    txtRespEntregaIdentificacion.setText(tec.getIdentificacion());
                    txtRespEntregaNombre.setText(tec.getNombre());
                    txtRespEntregaCorreo.setText(tec.getCorreo());
                    txtRespEntregaTelefono.setText(tec.getTelefono());
                    correo = txtRespEntregaCorreo.getText().toString();
                    txtRespEntregaCorreo.setError(null);
                    if(!correo.isEmpty()){
                        if(!txtRespEntregaCorreo.getText().toString().equals("")&&!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
                            txtRespEntregaCorreo.setError("Ingrese un correo válido");
                            txtRespEntregaCorreo.setEnabled(true);

                        }
                    }
                }
            }

            //String img = cursor.getString(cursor.getColumnIndex("tecnicoFirmaImg"));
            ItemFile f = MyApp.getDBO().manifiestoFileDao().consultarFile(idAppManifiesto, ManifiestoFileDao.FOTO_FIRMA_TECNICO_GENERADOR,MyConstant.STATUS_RECOLECCION);
            if(f != null){
                Bitmap imagen = Utils.StringToBitMap(f.getFile());
                txtFirmaMensaje.setVisibility(View.GONE);
                imgFirmaTecnico.setVisibility(View.VISIBLE);
                imgFirmaTecnico.setImageBitmap(imagen);
                firmaTecnicoGenerador=true;
            }
//TRANSPORTISTA
            /*f = MyApp.getDBO().manifiestoFileDao().consultarFile(idAppManifiesto, ManifiestoFileDao.FOTO_FIRMA_TRANSPORTISTA,MyConstant.STATUS_RECOLECCION);
            if(f != null){
                Bitmap imagen = Utils.StringToBitMap(f.getFile());
                txtFirmaMensajeTransportista.setVisibility(View.GONE);
                imgFirmaTecnicoTrasnsportista.setVisibility(View.VISIBLE);
                imgFirmaTecnicoTrasnsportista.setImageBitmap(imagen);
                firmaTransportista=true;
            }*/
            if (manifiesto.getFirmaChoferRecolector()!=null){
                Bitmap imagen = Utils.StringToBitMap(manifiesto.getFirmaChoferRecolector());
                txtFirmaMensajeTransportista.setVisibility(View.GONE);
                imgFirmaTecnicoTrasnsportista.setVisibility(View.VISIBLE);
                imgFirmaTecnicoTrasnsportista.setImageBitmap(imagen);
                btnAgregarFirmaTransportista.setEnabled(false);
                btnAgregarFirmaTransportista.setClickable(false);
                firmaTransportista=true;
                MyApp.getDBO().manifiestoFileDao().saveOrUpdate(idAppManifiesto, ManifiestoFileDao.FOTO_FIRMA_TRANSPORTISTA, Utils.encodeTobase64(imagen), MyConstant.STATUS_RECOLECCION);
                MyApp.getDBO().parametroDao().saveOrUpdate("estadoFirmaTransportista",""+2);//ESTADO  CON FIRMA
            }else {
                f = MyApp.getDBO().manifiestoFileDao().consultarFile(idAppManifiesto, ManifiestoFileDao.FOTO_FIRMA_TRANSPORTISTA, MyConstant.STATUS_RECOLECCION);
                if (f != null) {
                    Bitmap imagen = Utils.StringToBitMap(f.getFile());
                    txtFirmaMensajeTransportista.setVisibility(View.GONE);
                    imgFirmaTecnicoTrasnsportista.setVisibility(View.VISIBLE);
                    imgFirmaTecnicoTrasnsportista.setImageBitmap(imagen);
                    firmaTransportista = true;
                }else {
                    MyApp.getDBO().parametroDao().saveOrUpdate("estadoFirmaTransportista",""+1);//ESTADO  SIN FIRMA
                }
            }
//OPERADOR 1
           /* f = MyApp.getDBO().manifiestoFileDao().consultarFile(idAppManifiesto, ManifiestoFileDao.FOTO_FIRMA_OPERADOR1,MyConstant.STATUS_RECOLECCION);
            if(f != null){
                Bitmap imagen = Utils.StringToBitMap(f.getFile());
                txtFirmaOperador1.setVisibility(View.GONE);
                imgFirmaOperador1.setVisibility(View.VISIBLE);
                imgFirmaOperador1.setImageBitmap(imagen);
                //firmaTransportista=true;
            }*/
            if (manifiesto.getFirmaAuxiliarRecolector()!=null){
                Bitmap imagen = Utils.StringToBitMap(manifiesto.getFirmaAuxiliarRecolector());
                txtFirmaOperador1.setVisibility(View.GONE);
                imgFirmaOperador1.setVisibility(View.VISIBLE);
                imgFirmaOperador1.setImageBitmap(imagen);
                btnAgregarFirmaOperador1.setEnabled(false);
                btnAgregarFirmaOperador1.setClickable(false);
                //firmaTransportista=true;
                MyApp.getDBO().manifiestoFileDao().saveOrUpdate(idAppManifiesto, ManifiestoFileDao.FOTO_FIRMA_OPERADOR1, Utils.encodeTobase64(imagen), MyConstant.STATUS_RECOLECCION);
                MyApp.getDBO().parametroDao().saveOrUpdate("estadoFirmaAuxiliar",""+2);//ESTADO  CON FIRMA
            }else {
                f = MyApp.getDBO().manifiestoFileDao().consultarFile(idAppManifiesto, ManifiestoFileDao.FOTO_FIRMA_OPERADOR1, MyConstant.STATUS_RECOLECCION);
                if (f != null) {
                    Bitmap imagen = Utils.StringToBitMap(f.getFile());
                    txtFirmaOperador1.setVisibility(View.GONE);
                    imgFirmaOperador1.setVisibility(View.VISIBLE);
                    imgFirmaOperador1.setImageBitmap(imagen);
                    //firmaTransportista=true;
                }else{
                    if (manifiesto.getAuxiliarNombre()==null){
                        MyApp.getDBO().parametroDao().saveOrUpdate("estadoFirmaAuxiliar",""+2);//NO HAY AUXILIAR RECOLECTOR
                    }else {
                        MyApp.getDBO().parametroDao().saveOrUpdate("estadoFirmaAuxiliar",""+1);//ESTADO  SIN FIRMA
                    }

                }
            }
//OPERADOR 2
           /* f = MyApp.getDBO().manifiestoFileDao().consultarFile(idAppManifiesto, ManifiestoFileDao.FOTO_FIRMA_OPERADOR2,MyConstant.STATUS_RECOLECCION);
            if(f != null){
                Bitmap imagen = Utils.StringToBitMap(f.getFile());
                txtFirmaOperador2.setVisibility(View.GONE);
                imgFirmaOperadorRecolector.setVisibility(View.VISIBLE);
                imgFirmaOperadorRecolector.setImageBitmap(imagen);
                //firmaTransportista=true;
            }*/
            if (manifiesto.getFirmaOperadorRecolector()!=null){
                Bitmap imagen = Utils.StringToBitMap(manifiesto.getFirmaOperadorRecolector());
                txtFirmaOperador2.setVisibility(View.GONE);
                imgFirmaOperadorRecolector.setVisibility(View.VISIBLE);
                imgFirmaOperadorRecolector.setImageBitmap(imagen);
                btmAgregarOperador2.setEnabled(false);
                btmAgregarOperador2.setClickable(false);
                MyApp.getDBO().manifiestoFileDao().saveOrUpdate(idAppManifiesto, ManifiestoFileDao.FOTO_FIRMA_OPERADOR2, Utils.encodeTobase64(imagen), MyConstant.STATUS_RECOLECCION);
                //firmaTransportista=true;
                MyApp.getDBO().parametroDao().saveOrUpdate("estadoFirmaOperador",""+2);//ESTADO  CON FIRMA
            }else {
                f = MyApp.getDBO().manifiestoFileDao().consultarFile(idAppManifiesto, ManifiestoFileDao.FOTO_FIRMA_OPERADOR2,MyConstant.STATUS_RECOLECCION);
                if(f != null){
                    Bitmap imagen = Utils.StringToBitMap(f.getFile());
                    txtFirmaOperador2.setVisibility(View.GONE);
                    imgFirmaOperadorRecolector.setVisibility(View.VISIBLE);
                    imgFirmaOperadorRecolector.setImageBitmap(imagen);
                    //firmaTransportista=true;
                }else {
                    if(manifiesto.getNombreOperadorRecolector()==null){
                        MyApp.getDBO().parametroDao().saveOrUpdate("estadoFirmaOperador",""+2);//NO HAY OPERADOR RECOLECTOR
                    }else {
                        MyApp.getDBO().parametroDao().saveOrUpdate("estadoFirmaOperador",""+1);//ESTADO  SIN FIRMA
                    }

                }

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

    public void checkCorreos() {
        chkCorreoPrincipal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    v.setSelected(true);
                    correoPrincipal ="I";
                    if(correoPrincipal.equals("I")&&correoAlterno.equals("H")){
                        correoEnvio="H,I";
                    }else if(correoPrincipal.equals("I")){
                        correoEnvio="I";
                    }else if(correoAlterno.equals("H")){
                        correoEnvio="H";
                    }else{
                        correoEnvio="";
                    }
                    MyApp.getDBO().manifiestoDao().updateManifiestoCorreos(idAppManifiesto,correoEnvio);

                } else {
                    correoPrincipal ="";
                    if(correoPrincipal.equals("I")&&correoAlterno.equals("H")){
                        correoEnvio="H,I";
                    }else if(correoPrincipal.equals("I")){
                        correoEnvio="I";
                    }else if(correoAlterno.equals("H")){
                        correoEnvio="H";
                    }else{
                        correoEnvio="";
                    }

                    MyApp.getDBO().manifiestoDao().updateManifiestoCorreos(idAppManifiesto,correoEnvio);
                    v.setSelected(false);
                }
            }
        });

        chkCorreoAlterno.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    v.setSelected(true);
                    correoAlterno ="H";
                    if(correoPrincipal.equals("I")&&correoAlterno.equals("H")){
                        correoEnvio="H,I";
                    }else if(correoPrincipal.equals("I")){
                        correoEnvio="I";
                    }else if(correoAlterno.equals("H")){
                        correoEnvio="H";
                    }else{
                        correoEnvio="";
                    }

                    MyApp.getDBO().manifiestoDao().updateManifiestoCorreos(idAppManifiesto,correoEnvio);
                } else {
                    v.setSelected(false);
                    correoAlterno ="";
                    if(correoPrincipal.equals("I")&&correoAlterno.equals("H")){
                        correoEnvio="H,I";
                    }else if(correoPrincipal.equals("I")){
                        correoEnvio="I";
                    }else if(correoAlterno.equals("H")){
                        correoEnvio="H";
                    }else{
                        correoEnvio="";
                    }

                    MyApp.getDBO().manifiestoDao().updateManifiestoCorreos(idAppManifiesto,correoEnvio);
                }
            }
        });

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

    public boolean validarCorreos(){
        if(txtGenTecCorreo.getText().toString().equals("") && txtCorreoAlterno.getText().toString().equals("") && txtRespEntregaCorreo.getText().toString().equals("")){
            return true;
        }
        if(!txtGenTecCorreo.getText().toString().equals("") || !txtCorreoAlterno.getText().toString().equals("")){
            if((!chkCorreoPrincipal.isChecked() && !chkCorreoAlterno.isChecked()) && txtRespEntregaCorreo.getText().toString().equals("") ){
                return true;
            }
        }

        return false;
    }

    public boolean validaExisteFirmaTecnicoGenerador(){
        return !firmaTecnicoGenerador;
    }

    public boolean validaExisteDatosResponsableEntrega(){
        if (txtRespEntregaIdentificacion.getText().toString().length()==0 || txtRespEntregaNombre.getText().toString().length()==0 ){
            return  true;
        }else {
            return false;
        }
    }



   /* public boolean validadorDeCedula(String cedula) {
        boolean cedulaCorrecta = false;

        try {

            if (cedula.length() == 10) // ConstantesApp.LongitudCedula
            {
                int tercerDigito = Integer.parseInt(cedula.substring(2, 3));
                if (tercerDigito < 6) {
                    // Coeficientes de validación cédula
                    // El decimo digito se lo considera dígito verificador
                    int[] coefValCedula = { 2, 1, 2, 1, 2, 1, 2, 1, 2 };
                    int verificador = Integer.parseInt(cedula.substring(9,10));
                    int suma = 0;
                    int digito = 0;
                    for (int i = 0; i < (cedula.length() - 1); i++) {
                        digito = Integer.parseInt(cedula.substring(i, i + 1))* coefValCedula[i];
                        suma += ((digito % 10) + (digito / 10));
                    }

                    if ((suma % 10 == 0) && (suma % 10 == verificador)) {
                        cedulaCorrecta = true;
                    }
                    else if ((10 - (suma % 10)) == verificador) {
                        cedulaCorrecta = true;
                    } else {
                        cedulaCorrecta = false;
                    }
                } else {
                    cedulaCorrecta = false;
                }
            } else {
                cedulaCorrecta = false;
            }
        } catch (NumberFormatException nfe) {
            cedulaCorrecta = false;
        } catch (Exception err) {
            System.out.println("Una excepcion ocurrio en el proceso de validadcion");
            messageBox("Una excepcion ocurrio en el proceso de validadcion");
            cedulaCorrecta = false;
        }

        if (!cedulaCorrecta) {
            System.out.println("La Cédula ingresada es Incorrecta");
            messageBox("La Cédula ingresada es Incorrecta");
        }
        return cedulaCorrecta;
    }*/

    public void messageBox(String message)
    {

        DialogBuilder dialogBuilder = new DialogBuilder(getContext());
        dialogBuilder.setTitle("INFO");
        dialogBuilder.setMessage(message);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }

    public boolean validarCorreo(){
        String email = txtRespEntregaCorreo.getText().toString().trim();

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()&& !email.equals("")){
            return false;
        }
        return true;
    }

    public String getIdentificacion(){ return manifiesto.getIdentificacionCliente();}
    public String getNombreCliente(){return txtClienteNombre.getText().toString();}
    public String getSucursal(){return sucursal;}

}

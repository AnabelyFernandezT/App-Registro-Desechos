package com.caircb.rcbtracegadere.fragments.recolector.manifiesto;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.entity.ManifiestoEntity;
import com.caircb.rcbtracegadere.database.entity.TecnicoEntity;
import com.caircb.rcbtracegadere.dialogs.DialogFirma;
import com.caircb.rcbtracegadere.utils.ImagenUtils;
import com.caircb.rcbtracegadere.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabManifiestoGeneralFragment extends Fragment {

    private static final String ARG_PARAM1 = "idAppManifiesto";
    private static final String ARG_PARAM2 = "Manifiestobloqueado";

    private  Integer idAppManifiesto;
    private Boolean bloquear;
    private  Integer tipoPaquete=null;
    private View view;
    private TextView txtNumManifiesto,txtClienteNombre,txtClienteIdentificacion,txtClienteTelefono,txtClienteDireccion,txtClienteProvincia,
            txtClienteCanton,txtClienteParroquia,txtTransReco,txtTransRecoAux,txtIdentificacion,txtNombre,txtCorreo,txtTelefono,
            txtGenTecCelular,txtFirmaMensaje,txtEmpresaDestinatario,txtempresaTransportista;

    EditText txtNumManifiestoCliente,txtGenTecIdentificacion,txtGenTecNombre,txtGenTecCorreo,txtGenTecTelefono;

    LinearLayout btnAgregarFirma,btnAgregarFirmaTransportista;

    ImageButton btnNumManifiestoCliente,btnBuscarIdentificacion;

    ImageView imgFirmaTecnico, imgFirmaTecnicoTrasnsportista;
    private int flag =0;
    DialogFirma dialogFirma;
    ImagenUtils imagenUtils;



    public static TabManifiestoGeneralFragment newInstance (Integer manifiestoID, Boolean bloqueado){
        TabManifiestoGeneralFragment f = new TabManifiestoGeneralFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, manifiestoID);
        args.putBoolean(ARG_PARAM2, bloqueado);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idAppManifiesto= getArguments().getInt(ARG_PARAM1);
            bloquear = getArguments().getBoolean(ARG_PARAM2);
        }

    }

    /*
    private void init() {

        txtNumManifiesto = view.findViewById(R.id.txtNumManifiesto) ;
        txtClienteNombre = view.findViewById(R.id.txtClienteNombre);
        txtClienteIdentificacion =view.findViewById(R.id.txtClienteIdentificacion);
        txtClienteTelefono = view.findViewById(R.id.txtClienteTelefono);
        txtClienteDireccion= view.findViewById(R.id.txtClienteDireccion);
        txtClienteProvincia= view.findViewById(R.id.txtClienteProvincia);
        txtClienteCanton= view.findViewById(R.id.txtClienteCanton);
        txtClienteParroquia= view.findViewById(R.id.txtClienteParroquia);
        txtTransReco= view.findViewById(R.id.txtTransReco);
        txtTransRecoAux= view.findViewById(R.id.txtTransRecoAux);
        txtIdentificacion= view.findViewById(R.id.txtIdentificacion);
        txtGenTecCelular= view.findViewById(R.id.txtGenTecCelular);
        txtFirmaMensaje= view.findViewById(R.id.txtFirmaMensaje);
        txtGenTecIdentificacion= view.findViewById(R.id.txtGenTecIdentificacion);
        txtGenTecNombre= view.findViewById(R.id.txtGenTecNombre);
        txtGenTecCorreo= view.findViewById(R.id.txtGenTecCorreo);
        txtGenTecTelefono= view.findViewById(R.id.txtGenTecTelefono);
        btnAgregarFirma= view.findViewById(R.id.btnAgregarFirma);
        btnAgregarFirmaTransportista = view.findViewById(R.id.btnAgregarFirmaTransportista);

        btnBuscarIdentificacion= view.findViewById(R.id.btnBuscarIdentificacion);
        imgFirmaTecnico= view.findViewById(R.id.imgFirmaTecnico);
        imgFirmaTecnicoTrasnsportista = view.findViewById(R.id.imgFirmaTecnicoTrasnsportista);

        txtNumManifiestoCliente =view. findViewById(R.id.txtNumManifiestoCliente);
        btnNumManifiestoCliente = view.findViewById(R.id.btnNumManifiestoCliente);
        txtNumManifiestoCliente.setEnabled(false);

        txtNombre = view.findViewById(R.id.txtNombre);
        txtCorreo = view.findViewById(R.id.txtCorreo);
        txtTelefono=view.findViewById(R.id.txtTelefono);
        txtEmpresaDestinatario = view.findViewById(R.id.txtEmpresaDestinatario);
        txtempresaTransportista = view.findViewById(R.id.txtempresaTransportista);

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
                    dialogFirma = new DialogFirma(getActivity());
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
                    dialogFirma = new DialogFirma(getActivity());
                    dialogFirma.setTitle("SU FIRMA");
                    dialogFirma.setCancelable(false);
                    dialogFirma.setOnSignaturePadListener(new DialogFirma.OnSignaturePadListener() {
                        @Override
                        public void onSuccessful(Bitmap bitmap) {
                            dialogFirma.dismiss();
                            dialogFirma=null;
                            if(bitmap!=null){
                                txtFirmaMensaje.setVisibility(View.GONE);
                                imgFirmaTecnicoTrasnsportista.setVisibility(View.VISIBLE);
                                imgFirmaTecnicoTrasnsportista.setImageBitmap(bitmap);
                            }else{
                                txtFirmaMensaje.setVisibility(View.VISIBLE);
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
    */
    private void loadDataManifiesto(){
        //dbHelper.open();
        ManifiestoEntity manifiesto = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(idAppManifiesto);
        //dbHelper.close();
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
            //txtTransRecoIden.setText(manifiesto.getConductorIdentificacion());
            txtTransRecoAux.setText(manifiesto.getAuxiliarNombre());
            //txtTransRecoAuxIden.setText(manifiesto.getAuxiliarIdentificacion());
            txtIdentificacion.setText(manifiesto.getTecnicoIdentificacion());
            txtNombre.setText(manifiesto.getTecnicoResponsable());
            txtCorreo.setText(manifiesto.getTecnicoCorreo());
            txtTelefono.setText(manifiesto.getTecnicoCorreo());
            txtEmpresaDestinatario.setText(manifiesto.getEmpresaDestinataria());
            txtempresaTransportista.setText(manifiesto.getEmpresaTransportista());



            //String tecnicoIdentificacion = cursor.getString(cursor.getColumnIndex("tecnicoIdentificacion"));
           /* if(manifiesto.getTecnicoIdentificacion() != null){
                txtGenTecIdentificacion.setText(manifiesto.getTecnicoIdentificacion());
                txtGenTecNombre.setText(manifiesto.getTecnicoResponsable());
                txtGenTecCelular.setText(manifiesto.getTecnicoCelular());
                txtGenTecTelefono.setText(manifiesto.getTecnicoTelefono());
                txtGenTecCorreo.setText(manifiesto.getTecnicoCorreo());
            }*/

            //String img = cursor.getString(cursor.getColumnIndex("tecnicoFirmaImg"));
            /*if(manifiesto.getTecnicoFirmaImg() != null){
                Bitmap imagen = Utils.StringToBitMap(manifiesto.getTecnicoFirmaImg());
                txtFirmaMensaje.setVisibility(View.GONE);
                imgFirmaTecnico.setVisibility(View.VISIBLE);
                imgFirmaTecnico.setImageBitmap(imagen);
            }

            if(manifiesto.getTecnicoFirmaImg() != null){
                Bitmap imagen = Utils.StringToBitMap(manifiesto.getTransportistaFirmaImg());
                txtFirmaMensaje.setVisibility(View.GONE);
                imgFirmaTecnicoTrasnsportista.setVisibility(View.VISIBLE);
                imgFirmaTecnicoTrasnsportista.setImageBitmap(imagen);
            }*/

            tipoPaquete = manifiesto.getTipoPaquete();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_tab_manifiesto_general, container, false);
        idAppManifiesto = this.getArguments().getInt(ARG_PARAM1);

        //init();
        //loadDataManifiesto();
        return view;
    }

    public Integer getTipoPaquete(){
        return tipoPaquete!=null?(tipoPaquete>0?tipoPaquete:null):null;
    }


}

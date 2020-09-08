package com.caircb.rcbtracegadere.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MainActivity;
import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.adapters.NotificationAdapter;
import com.caircb.rcbtracegadere.adapters.TextoAdapter;
import com.caircb.rcbtracegadere.database.entity.ManifiestoEntity;
import com.caircb.rcbtracegadere.fragments.recolector.manifiesto2.Manifiesto2Fragment;
import com.caircb.rcbtracegadere.fragments.recolector.manifiesto2.VistaPreliminarFragment;
import com.caircb.rcbtracegadere.generics.MyDialog;
import com.caircb.rcbtracegadere.models.ItemNotificacion;
import com.caircb.rcbtracegadere.tasks.UserRegistrarRecoleccion;

import java.util.ArrayList;
import java.util.List;

public class DialogManifiestoCliente extends MyDialog {
    Activity _activity;
    EditText txtManifiestoCliente;
    LinearLayout btnIniciaRutaCancel,btnIniciaRutaAplicar, btnNewText,btnNumManifiestoClienteAdd;
    VistaPreliminarFragment vistaPreliminarFragment;

    Manifiesto2Fragment principal = new Manifiesto2Fragment();
    Integer idManifiesto, tipoPaquete;
    String identificacion;
    private RecyclerView recyclerViewManifietoCliente;
    private TextoAdapter recyclerviewAdapter;
    private Integer tipoRecoleccion;
    private List<String> textList = new ArrayList<>();
    public interface onRegisterListenner {
        public void onSucessfull();
    }
    public onRegisterListenner mOnRegisterListener;

    public DialogManifiestoCliente(@NonNull Context context, Integer idManifiesto, Integer tipoPaquete, String identificacion, Integer tipoRecoleccion) {
        super(context, R.layout.dialog_manifiesto_cliente);
        this._activity = (Activity)context;
        this.idManifiesto = idManifiesto;
        this.tipoPaquete = tipoPaquete;
        this.identificacion = identificacion;
        this.tipoRecoleccion = tipoRecoleccion;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getView());
        init();
    }

    private void init() {

        ManifiestoEntity manifiesto = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(idManifiesto);

        recyclerViewManifietoCliente = getView().findViewById(R.id.recyclerviewTexto);

        /*txtManifiestoCliente = getView().findViewById(R.id.txtManifiestoCliente);
          int type = InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS| InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS;
        txtManifiestoCliente.setInputType(type);
        txtManifiestoCliente.setText(manifiesto.getNumManifiestoCliente());*/
        btnIniciaRutaAplicar = getView().findViewById(R.id.btnIniciaRutaAplicar);
        btnIniciaRutaCancel = getView().findViewById(R.id.btnIniciaRutaCancel);
        btnNumManifiestoClienteAdd = getView().findViewById(R.id.btnNumManifiestoClienteAdd);
      //  btnNewText = getView().findViewById(R.id.btnAgregarText);
        if(manifiesto.getNumManifiestoCliente()!=null){
            String[] data = manifiesto.getNumManifiestoCliente().split(",");
            if(data.length>0){
                for (int x=0;x<data.length;x++){
                    textList.add(data[x]);
                }
            }else{
                textList.add("");
            }
        }else{
            textList.add("");
        }

        recyclerViewManifietoCliente.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewManifietoCliente.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        recyclerviewAdapter = new TextoAdapter(getActivity());
        recyclerviewAdapter.setTaskList(textList);

        recyclerViewManifietoCliente.setAdapter(recyclerviewAdapter);


        btnIniciaRutaCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogManifiestoCliente.this.dismiss();
            }
        });

        btnIniciaRutaAplicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = recyclerviewAdapter.totalString();
                //validar si falta ingresar datos en los imput...
                if(data!=null && data.trim().length()==0){
                    messageBox("No ingresó un número de manifiesto del cliente!");
                    return;
                }

                boolean isFaltantes=false;
                int pos =1;
                String[] lista = data.split(",",-1);
                for(String txt:lista){
                    if(txt!=null && txt.trim().length()==0){
                        isFaltantes = true;
                        messageBox("en el item ["+pos+"], NO ingresó un número de manifiesto del cliente!");
                        return;
                    }
                    pos++;
                }

                if(!isFaltantes){
                    MyApp.getDBO().manifiestoDao().updateManifiestoCliente(idManifiesto,data);
                    if (mOnRegisterListener!=null){
                        mOnRegisterListener.onSucessfull();
                    }
                }
            }
        });
        btnNumManifiestoClienteAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerviewAdapter.addText("");
            }
        });

    }

    public void setmOnRegisterListener(@Nullable onRegisterListenner l){ mOnRegisterListener = l;}
}

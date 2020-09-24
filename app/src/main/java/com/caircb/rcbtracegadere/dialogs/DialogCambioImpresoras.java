package com.caircb.rcbtracegadere.dialogs;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.generics.MyDialog;
import com.caircb.rcbtracegadere.models.ItemGeneric;
import com.caircb.rcbtracegadere.tasks.UserRegistrarCambioImpresoraIniciaRuta;

import java.util.ArrayList;
import java.util.List;

public class DialogCambioImpresoras extends MyDialog {
    Activity _activity;
    LinearLayout btnAceptar, btnCancelar;
    Spinner spnImpresoras;
    int idTipoSubruta,impresoraID=0;
    List<ItemGeneric> listaImpresoras = new ArrayList<>();
    UserRegistrarCambioImpresoraIniciaRuta registoCambio;
    Cursor cursor;

    public DialogCambioImpresoras(@NonNull Context context) {
        super(context, R.layout.dialog_cambio_impresora);
        this._activity=(Activity)context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getView());
        init();
    }

    public interface OnRegisterListener{
        public void onSuccessful();
    }
    private OnRegisterListener mOnRegisterListener;


    private void init() {
        btnAceptar = getView().findViewById(R.id.btnAplicar);
        btnCancelar = getView().findViewById(R.id.btnCancel);
        spnImpresoras = getView().findViewById(R.id.lista_impresoras);

        idTipoSubruta = Integer.valueOf(MyApp.getDBO().parametroDao().fetchParametroEspecifico("tipoSubRuta").getValor());

        loadImpresorabyRuta(idTipoSubruta);
        spnImpresoras.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cursor = (Cursor) spnImpresoras.getSelectedItem();
                Object s = spnImpresoras.getSelectedItem();
                System.out.print(s);
                System.out.print(cursor);
                if(cursor!=null && cursor.getCount()>0) {
                    impresoraID = cursor.getInt(cursor.getColumnIndex("_id"));
                    System.out.print(impresoraID);
                    MyApp.getDBO().parametroDao().saveOrUpdate("id_impresora",""+impresoraID);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogCambioImpresoras.this.dismiss();
                cambioImpresora();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogCambioImpresoras.this.dismiss();
            }
        });
    }

    private void loadImpresorabyRuta(Integer tipoSubRuta) {
        if (tipoSubRuta != null) {
            listaImpresoras = MyApp.getDBO().impresoraDao().searchListImpresorabyTipoRuta(tipoSubRuta);
            if (listaImpresoras.size() > 0) {
                cargarSpinner(spnImpresoras, listaImpresoras, true);
            }
        }
    }

    private void cambioImpresora(){
        UserRegistrarCambioImpresoraIniciaRuta cambioImpresora = new UserRegistrarCambioImpresoraIniciaRuta(getActivity(),impresoraID);
        cambioImpresora.setOnRegisterListener(new UserRegistrarCambioImpresoraIniciaRuta.OnRegisterListener() {
            @Override
            public void onSuccessful() {
                MyApp.getDBO().impresoraDao().updateDisabledAllImpresoraWorked();
                MyApp.getDBO().impresoraDao().updateDefaulImpresoraWorked(impresoraID);
                if(mOnRegisterListener!=null)mOnRegisterListener.onSuccessful();
            }
        });
        cambioImpresora.execute();
    }

    public void setScanCode(String barcode){
        //programar metodo para consultar y setear valores de inpresora capturada por lector...
        ItemGeneric item = MyApp.getDBO().impresoraDao().searchCodigoUUID(barcode,idTipoSubruta);
        System.out.print(impresoraID);
        if(item ==null){
            messageBox("Impresora no encontrada");
        }else{
            //MatrixCursor extras = new MatrixCursor(new String[] { "_id", "nombre"});
            //extras.addRow(new String[] { item.getId().toString(), item.getNombre() });
            //cursor = (Cursor) extras;
            Integer id = getIndexByname(item.getNombre());
            impresoraID = item.getId();
            spnImpresoras.setSelection(id+1);
        }

    }

    public int getIndexByname(String pName)
    {
        for(ItemGeneric _item : listaImpresoras)
        {
            if(_item.getNombre().equals(pName))
                return listaImpresoras.indexOf(_item);
        }
        return -1;
    }

    public void setOnRegisterListener(@NonNull OnRegisterListener l){
        mOnRegisterListener =l;
    }
}

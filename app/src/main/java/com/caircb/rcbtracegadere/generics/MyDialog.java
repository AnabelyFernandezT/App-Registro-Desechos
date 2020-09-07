package com.caircb.rcbtracegadere.generics;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentHostCallback;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.caircb.rcbtracegadere.MainActivity;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.models.ItemGeneric;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class MyDialog extends Dialog {

    AlertDialog.Builder messageBox;
    //public DBAdapter dbHelper;
    //public Cursor cursor;
    private Activity activity;
    private View view;
    LayoutInflater mInflater;
    public Context context;

    FragmentHostCallback mHost;

    public MyDialog(@NonNull Context context, @NonNull Integer resource) {
        super(context);
        this.activity =((Activity)context);
        this.mInflater = mInflater.from(activity);
        this.view = this.mInflater.inflate(resource,null);

    }

    public void messageBox(String message)
    {
        DialogBuilder dialogBuilder = new DialogBuilder(getContext());
        dialogBuilder.setTitle("INFO");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();

        /*
        messageBox = new AlertDialog.Builder(getContext());
        messageBox.setTitle("INFO");
        messageBox.setMessage(message);
        messageBox.setCancelable(false);
        messageBox.setNeutralButton("OK", null);
        messageBox.show();
        */         
    }

    public void showToast(String mensaje) {
        Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    public View getView() {
        return view;
    }

    public Activity getActivity(){
        return this.activity;
    }

    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), inImage, "firma_e", null);
        return Uri.parse(path);
    }


    public Spinner cargarSpinner(Spinner defaulSpiner, List<ItemGeneric> lista, boolean bhabilitar){

        MatrixCursor extras = new MatrixCursor(new String[] { "_id", "nombre"});
        extras.addRow(new String[] { "-1", "Seleccione..." });
        for(ItemGeneric reg: lista){
            extras.addRow(new String[]{reg.getId().toString(), reg.getNombre()});
        }

        SimpleCursorAdapter adapter1 = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, extras, new String[] { "nombre"}, new int[]{android.R.id.text1});
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //adapter1.setViewBinder(binding);
        defaulSpiner.setAdapter(adapter1);
        defaulSpiner.setEnabled(bhabilitar);

        return defaulSpiner;
    }

    /*
    public Spinner cargarSpinnerToArray(Spinner spinner, Cursor _cursor, boolean bhabilitar){

        //String[] from1=new String[]{"nombre"};
        //int[] to1 = new int[]{android.R.id.text1};
        ArrayAdapter<String> adapter;
        Spinner defaulSpiner = spinner;
        ArrayList<String> listaData = new ArrayList<String>();

        //MatrixCursor extras = new MatrixCursor(new String[] { "_id", "nombre"});
        //extras.addRow(new String[] { "-1", "SELECCIONE" });
        listaData.add(MyConstant.DEFAULD_SELECCIONE);
        if(_cursor.getCount()>0){
            if(_cursor.moveToFirst()){
                do {
                    listaData.add( _cursor.getString(_cursor.getColumnIndex("nombreCatalogo")));
                } while (_cursor.moveToNext());
            }
        }


        adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, listaData);
        //adapter = new CustomCursorAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, extras, new String[] { "nombre"}, to1);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        defaulSpiner.setAdapter(adapter);
        defaulSpiner.setEnabled(bhabilitar);

        return defaulSpiner;
    }
    */
/*
    public Spinner cargarCustomSpinnerToArray(Spinner spinner, Cursor _cursor, boolean bhabilitar){

        ArrayAdapter<String> adapter;
        Spinner defaulSpiner = spinner;
        ArrayList<String> listaData = new ArrayList<String>();

        listaData.add(MyConstant.DEFAULD_SELECCIONE);
        if(_cursor.getCount()>0){
            if(_cursor.moveToFirst()){
                do {
                    listaData.add( _cursor.getString(_cursor.getColumnIndex("nombreCatalogo")));
                } while (_cursor.moveToNext());
            }
        }


        adapter = new ArrayAdapter<String>(activity, R.layout.spinner_item, listaData);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        //adapter = new CustomCursorAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, extras, new String[] { "nombre"}, to1);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        defaulSpiner.setAdapter(adapter);
        defaulSpiner.setEnabled(bhabilitar);

        return defaulSpiner;
    }
*/
/*
    public Spinner cargarSpinner(Spinner spinner, Cursor _cursor, boolean bhabilitar){

        String[] from1=new String[]{"nombre"};
        int[] to1 = new int[]{android.R.id.text1};
        CustomCursorAdapter adapter;
        Spinner defaulSpiner;

        MatrixCursor extras = new MatrixCursor(new String[] { "_id", "nombre"});
        extras.addRow(new String[] { "-1", MyConstant.DEFAULD_SELECCIONE });

        if(_cursor.getCount()>0){
            if(_cursor.moveToFirst()){
                do {
                    extras.addRow(new String[]{_cursor.getString(_cursor.getColumnIndex("idSistema")), _cursor.getString(_cursor.getColumnIndex("nombreCatalogo"))});
                } while (_cursor.moveToNext());
            }
        }

        defaulSpiner = spinner;
        adapter = new CustomCursorAdapter(activity, android.R.layout.simple_spinner_item, extras, new String[] { "nombre"}, to1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        defaulSpiner.setAdapter(adapter);
        defaulSpiner.setEnabled(bhabilitar);

        return defaulSpiner;
    }
*/
    public static int obtenerPosicionItem(Spinner spinner, String nombre) {
        int posicion = 0;
        //Recorre el spinner en busca del ítem que coincida con el parametro

        for (int i = 0; i < spinner.getCount(); i++) {
            //Almacena la posición del ítem que coincida con la búsqueda
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(nombre)) {
                posicion = i;
            }
        }

        //Devuelve un valor entero (
        return posicion;
    }

    public static void updateListViewHeight(ListView myListView) {
        ListAdapter myListAdapter = myListView.getAdapter();
        if (myListAdapter == null) {
            return;
        }
        // get listview height
        int totalHeight = 0;
        int adapterCount = myListAdapter.getCount();
        for (int size = 0; size < adapterCount; size++) {
            View listItem = myListAdapter.getView(size, null, myListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        // Change Height of ListView
        ViewGroup.LayoutParams params = myListView.getLayoutParams();
        params.height = (totalHeight
                + (myListView.getDividerHeight() * (adapterCount)));
        myListView.setLayoutParams(params);
    }


}


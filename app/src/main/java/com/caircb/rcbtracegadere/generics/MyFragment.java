package com.caircb.rcbtracegadere.generics;

import android.app.AlertDialog;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import android.app.Fragment;

import com.caircb.rcbtracegadere.MainActivity;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;

public class MyFragment extends Fragment {
    AlertDialog.Builder messageBox;
    private View view;
    //public DBAdapter dbHelper;
    //public Cursor cursor;
    private Context mContext;

    //public FragmentTransaction _fragmentTransaction;
    //public FragmentManager _fm;
    //public android.support.v4.app.Fragment _fragment;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        //dbHelper = new DBAdapter(mContext);
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void setNavegate(Fragment _fragment){
        if(getActivity() instanceof MainActivity) {
            if(_fragment instanceof OnHome){
                setTitle(getResources().getText(R.string.app_name).toString());
                setShowHeader();
            }

            ((MainActivity) getActivity()).NavegationFragment(_fragment);
        }

    }

    public void setHideHeader(){
        if(getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).findViewById(R.id.f_header).setMinimumHeight(0);
        }
    }

    public void setShowHeader(){
        if(getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).findViewById(R.id.f_header).setMinimumHeight(getResources().getDimensionPixelOffset(R.dimen.dimen_header));
        }
    }

    public void setTitle(String titulo){
        if(getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setTitle(titulo);
        }
    }


    //Se agrega funcionalidad para acceder a los parametros del padre
    public MainActivity getMain(){
        if(getActivity() instanceof MainActivity) {
            return (MainActivity) getActivity();
        }
        return null;
    }

    public void messageBox(String message)
    {

        DialogBuilder dialogBuilder = new DialogBuilder(mContext);
        dialogBuilder.setTitle("INFO");
        dialogBuilder.setMessage(message);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
        /*
        messageBox = new AlertDialog.Builder(mContext);
        messageBox.setTitle("INFO");
        messageBox.setMessage(message);
        messageBox.setCancelable(false);
        messageBox.setPositiveButton("OK", null);
        messageBox.show();
         */
    }

    public void messageBox2(String message, Context context)
    {
        messageBox = new AlertDialog.Builder(context);
        messageBox.setTitle("INFO");
        messageBox.setMessage(message);
        messageBox.setCancelable(false);
        messageBox.setNeutralButton("OK", null);
        messageBox.show();
    }

    public Location getLocation(){
        return ((MainActivity)getActivity()).getLocation();
    }
}

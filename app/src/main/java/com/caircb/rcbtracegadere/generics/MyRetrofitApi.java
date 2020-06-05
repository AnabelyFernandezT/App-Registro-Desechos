package com.caircb.rcbtracegadere.generics;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;

import androidx.annotation.NonNull;


public class MyRetrofitApi {
    private Context mContext;
    private ProgressDialog progressDialog = null;
    AlertDialog.Builder messageBox;

    public MyRetrofitApi(Context context){
        this.mContext =context;
    }

    public Activity getActivity(){
        return (Activity)mContext;
    }

    public void progressShow(String text){
        if(progressDialog!=null && progressDialog.isShowing()){progressDialog.dismiss();progressDialog=null;}
        if(progressDialog==null){
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(text);
            progressDialog.show();
        }
    }

    public void progressHide(){
        if(progressDialog!=null){
            progressDialog.dismiss();
            progressDialog=null;
        }
    }

    public void progressUpdateText(String texto){
        if(progressDialog!=null && progressDialog.isShowing()){
            progressDialog.setMessage(texto);
        }
    }

    public void message(@NonNull String message)
    {
        messageBox =  new AlertDialog.Builder(mContext);
        messageBox.setTitle("INFO");
        messageBox.setMessage(message);
        messageBox.setCancelable(false);
        messageBox.setNeutralButton("OK", null);
        messageBox.show();
    }
    public void ProgressHide(){
        if(progressDialog!=null){
            progressDialog.dismiss();
            progressDialog=null;
        }
    }
}

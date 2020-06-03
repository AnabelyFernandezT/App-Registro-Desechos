package com.caircb.rcbtracegadere.generics;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;

public abstract class MyAsyncTask extends AsyncTask<String, String, Boolean> {
    private Exception exception=null;
    public String[] params;
    public Context mContext;
    DialogBuilder dialogBuilder;
    public boolean exito=false;

    public String getStringR(int ResourceId){
        if(mContext!=null){
            return mContext.getResources().getString(ResourceId);
        }
        return "";
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            this.params=params;
            return doInBackground();
        }
        catch (Exception e) {
            exception = e;
            return false;
        }
    }

    abstract protected Boolean doInBackground() throws Exception;

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }

    public Exception getException(){return exception;}

    public boolean isException(){return exception!=null;}

    public void messageBox(@NonNull String message)
    {
        dialogBuilder = new DialogBuilder(mContext);
        dialogBuilder.setTitle("INFO");
        dialogBuilder.setMessage(message);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setNeutralButton("OK", null);
        dialogBuilder.show();
    }
}

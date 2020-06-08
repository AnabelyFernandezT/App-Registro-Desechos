package com.caircb.rcbtracegadere.tasks;

import android.content.Context;

import com.caircb.rcbtracegadere.generics.MyRetrofitApi;
import com.caircb.rcbtracegadere.generics.RetrofitCallbacks;

public class UserConsultarRecolectadosTask extends MyRetrofitApi implements RetrofitCallbacks {

    public UserConsultarRecolectadosTask(Context context){
        super(context);
    }

    @Override
    public void execute() {

    }
}

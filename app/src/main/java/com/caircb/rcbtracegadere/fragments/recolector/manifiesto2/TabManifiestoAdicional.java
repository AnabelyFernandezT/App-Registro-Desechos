package com.caircb.rcbtracegadere.fragments.recolector.manifiesto2;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.caircb.rcbtracegadere.R;

public class TabManifiestoAdicional extends LinearLayout {
    Integer idAppManifiesto;

    public TabManifiestoAdicional(Context context,Integer idAppManifiesto) {
        super(context);
        this.idAppManifiesto = idAppManifiesto;
        View.inflate(context, R.layout.tab_manifiesto_adicional, this);
        init();
    }

    private void init(){

    }
}

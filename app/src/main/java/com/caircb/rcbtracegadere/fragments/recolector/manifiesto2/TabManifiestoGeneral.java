package com.caircb.rcbtracegadere.fragments.recolector.manifiesto2;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.caircb.rcbtracegadere.R;

public class TabManifiestoGeneral extends LinearLayout {

    private Integer manifiestoID;

    public TabManifiestoGeneral(Context context,Integer manifiestoID) {
        super(context);
        View.inflate(context, R.layout.tab_manifiesto_general, this);
        init();
    }

    private void init(){

    }


}

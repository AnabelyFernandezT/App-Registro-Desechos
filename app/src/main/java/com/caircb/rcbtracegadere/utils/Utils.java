package com.caircb.rcbtracegadere.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.recyclerview.widget.RecyclerView;

public final class Utils {

    public static Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    public static byte[] StringToByte(String encodedString){
        try{
            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
            //Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return encodeByte;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }


    public static boolean isDataConnectivity(Context context){
        boolean ban = ConnectivityUtils.isConnectedWifi(context) || ConnectivityUtils.isConnectedMobile(context);
        return ban;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}

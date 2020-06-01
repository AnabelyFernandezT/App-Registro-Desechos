package com.caircb.rcbtracegadere.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.models.MenuItem;

import java.util.List;

public class DialogMenuBaseAdapter extends ArrayAdapter<MenuItem> {

    Context context;
    LayoutInflater mInflater;

    public DialogMenuBaseAdapter(Context context, List<MenuItem> items) {
        super(context, R.layout.lista_items,items);
        this.context=context;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView txtItem;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        DialogMenuBaseAdapter.ViewHolder holder = null;
        MenuItem row = getItem(position);

        mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.lista_items, null);
            holder = new DialogMenuBaseAdapter.ViewHolder();
            holder.txtItem = (TextView) convertView.findViewById(R.id.txtItem);

            convertView.setTag(holder);
        } else
            holder = (DialogMenuBaseAdapter.ViewHolder) convertView.getTag();

        holder.txtItem.setText(row.getNombre());


        return convertView;
    }
}
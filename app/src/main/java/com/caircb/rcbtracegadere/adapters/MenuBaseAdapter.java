package com.caircb.rcbtracegadere.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.models.RowItem;

import java.util.List;

/**
 * Created by jlsuarez on 07/04/2017.
 */
public class MenuBaseAdapter extends ArrayAdapter<RowItem> {

    Context context;



    public MenuBaseAdapter(Context context, int resourceId,
                           List<RowItem> items) {
        super(context, resourceId, items);
        this.context = context;

    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        RowItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_main_menu, null);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtTitle.setText(rowItem.getNombre());
        holder.imageView.setImageResource(rowItem.getIcono());

        return convertView;
    }
}

package com.caircb.rcbtracegadere.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.caircb.rcbtracegadere.models.RowPrinters;

import java.util.List;
import com.caircb.rcbtracegadere.R;

public class RecyclerViewPrinterAdapter extends RecyclerView.Adapter<RecyclerViewPrinterAdapter.MyViewHolder>  {
    private Context mContext;
    private List<RowPrinters> lstPrinters;
    private String selectedPrinter;

    public RecyclerViewPrinterAdapter(Context context, String selectedPrinter){
        this.mContext = context;
        this.selectedPrinter = selectedPrinter;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_printer,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RowPrinters printers = lstPrinters.get(position);
        holder.tvPrinterName.setTypeface(ResourcesCompat.getFont(mContext,R.font.quicksand_bold));
        holder.tvPrinterName.setText(printers.getName());

        holder.tvPrinterAddress.setTypeface(ResourcesCompat.getFont(mContext,R.font.quicksand_bold));
        holder.tvPrinterAddress.setText(printers.getAddress());

        //holder.tvPrinterName.setTextColor(Color.parseColor("#BCCC1F"));
        if((selectedPrinter != null) && (selectedPrinter.equals(printers.getAddress()))) {
            holder.tvPrinterName.setTextColor(Color.parseColor("#A72027"));
            holder.tvPrinterAddress.setTextColor(Color.parseColor("#A72027"));
        }
    }

    @Override
    public int getItemCount() {
        return lstPrinters.size();
    }

    public void setTaskList(List<RowPrinters> taskList) {
        this.lstPrinters = taskList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPrinterName;
        private TextView tvPrinterAddress;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvPrinterName = itemView.findViewById(R.id.printer_name);
            tvPrinterAddress = itemView.findViewById(R.id.printer_address);
        }
    }
}

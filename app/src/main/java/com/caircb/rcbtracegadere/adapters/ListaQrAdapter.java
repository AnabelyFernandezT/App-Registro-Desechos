package com.caircb.rcbtracegadere.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.models.ItemQrLote;
import com.caircb.rcbtracegadere.models.RowItemManifiestosDetalleGestores;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.List;

public class ListaQrAdapter extends RecyclerView.Adapter<ListaQrAdapter.MyViewHolder> {
    private Context mContext;
    List<ItemQrLote> listaQr;
    String qr;

    public ListaQrAdapter(Context mContext, List<ItemQrLote> listaQr) {
        this.mContext = mContext;
        this.listaQr = listaQr;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.mostar_qr_lote,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaQrAdapter.MyViewHolder holder, int position) {
        final ItemQrLote it = listaQr.get(position);
        holder.txtfecha.setText(it.getFecha());
        if (it.getCodigoQr()!=""){
            qr=it.getCodigoQr();
            holder.imgQrLoteTransportista.setVisibility(View.VISIBLE);
        }else {
            qr="";
            holder.imgQrLoteTransportista.setVisibility(View.GONE);
        }

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        BitMatrix bitMatrix= null;
        try {
            bitMatrix = multiFormatWriter.encode(qr+"", BarcodeFormat.QR_CODE,600,600,null);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
        holder.imgQrLoteTransportista.setImageBitmap(bitmap);

    }

    @Override
    public int getItemCount() {
        return listaQr.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgQrLoteTransportista;
        TextView txtfecha;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgQrLoteTransportista =itemView.findViewById(R.id.imgQrLoteTransportista);
            txtfecha = itemView.findViewById(R.id.txtFecha);
        }
    }
    public void setTaskList(List<ItemQrLote> taskList) {
        this.listaQr = taskList;
        notifyDataSetChanged();
    }
}

package com.caircb.rcbtracegadere.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.R;

import java.util.ArrayList;

public class BultosAdapter extends RecyclerView.Adapter<BultosAdapter.MyViewHolder> {

    private ArrayList<String> pesos;
    private int layout;
    private OnItemClickListener itemClickListener;
    private RecyclerView reciclerView;
    private  RecyclerView.Adapter adapter;
    private int position;
    private LinearLayout btn;


    public BultosAdapter (RecyclerView recicler,ArrayList<String> pesos, int layout, OnItemClickListener listener){
        this.pesos=pesos;
        this.layout=layout;
        this.itemClickListener=listener;
        this.reciclerView=recicler;
    }

    @NonNull
    @Override
    public BultosAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        MyViewHolder vh=new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull BultosAdapter.MyViewHolder holder, int position) {
        holder.bind(pesos.get(position), itemClickListener);
        this.position=position;
    }

    @Override
    public int getItemCount() {
        return pesos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView lblBultoNumero, txtItemPesoBulto;
        public LinearLayout btnDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.lblBultoNumero = itemView.findViewById(R.id.lblBultoNumero);
            this.txtItemPesoBulto = itemView.findViewById(R.id.txtItemPesoBulto);
            this.btnDelete = itemView.findViewById(R.id.btnDeleteItem);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //deleteItem(position);
                }
            });
        }
        public void bind(final String item, final OnItemClickListener listener){
            this.txtItemPesoBulto.setText(item);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(item, getAdapterPosition());
                }
            });
        }

    }

    public interface OnItemClickListener {
        void OnItemClick(String txtItemPesoBulto,int position);
    }

  /*  private void deleteItem(int position){
        pesos.remove(position);
        adapter.notifyItemRemoved(position);
    }
*/
}

package com.caircb.rcbtracegadere.adapters;

import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.models.ItemLote;
import com.caircb.rcbtracegadere.tasks.UserConsultaLotes;

import java.util.ArrayList;
import java.util.List;

public class TextoAdapter extends RecyclerView.Adapter<TextoAdapter.MyViewHolder> {

    private Context mContext;
    private List<String> textList;

    public TextoAdapter(Context context){
        this.mContext = context;
        this.textList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_texto,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull TextoAdapter.MyViewHolder holder, int position) {
        final String it = textList.get(position);
        int type = InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS| InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS;
        holder.txtNumeroAdd.setInputType(type);
        holder.txtNumeroAdd.setText(it);
    }

    @Override
    public int getItemCount() {
        return textList.size();
    }

    public void setTaskList(List<String> taskList) {
        this.textList = taskList;
        notifyDataSetChanged();
    }

    public void addText(String nuevoDato) {
        this.textList.add(nuevoDato);
        notifyDataSetChanged();
    }

  public String totalString(){
        String texto ="";
        for(int a = 0 ; a<this.textList.size(); a++){
            texto+= this.textList.get(a);
        }
        return texto;
  }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtNumeroAdd;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtNumeroAdd = itemView.findViewById(R.id.txtNumeroAdd);
        }
    }

}

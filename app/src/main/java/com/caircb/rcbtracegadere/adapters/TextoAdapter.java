package com.caircb.rcbtracegadere.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
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
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)  {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_texto,parent,false);
        return new MyViewHolder(view, new MyCustomEditTextListener());
    }

    @Override
    public void onViewAttachedToWindow(@NonNull MyViewHolder holder) {
        //super.onViewAttachedToWindow(holder);
        ((MyViewHolder) holder).enableTextWatcher();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull MyViewHolder holder) {
        //super.onViewDetachedFromWindow(holder);
        ((MyViewHolder) holder).disableTextWatcher();
    }


    @Override
    public void onBindViewHolder(@NonNull final TextoAdapter.MyViewHolder holder, int position) {

        holder.myCustomEditTextListener.updatePosition(holder.getAdapterPosition());

        holder.mEditText.setTypeface(ResourcesCompat.getFont(mContext,R.font.quicksand_light));
        //int type = InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS| InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS;
        //holder.txtNumeroAdd.setInputType(type);
        holder.mEditText.setText(textList.get(holder.getAdapterPosition()));
        //holder.txtNumeroAdd.setTextColor(Color.parseColor("#A72027"));

        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textList.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });
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
        return TextUtils.join(",",textList);//separado por comas
  }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        EditText mEditText;
        ImageButton btnRemove;
        public MyCustomEditTextListener myCustomEditTextListener;

        public MyViewHolder(View itemView, MyCustomEditTextListener myCustomEditTextListener) {
            super(itemView);

            mEditText = itemView.findViewById(R.id.txtNumeroAdd);
            btnRemove = itemView.findViewById(R.id.btnNumManifiestoClienteRemove);
            this.myCustomEditTextListener = myCustomEditTextListener;
        }

        void enableTextWatcher() {
            mEditText.addTextChangedListener(myCustomEditTextListener);
        }

        void disableTextWatcher() {
            mEditText.removeTextChangedListener(myCustomEditTextListener);
        }
    }

    // we make TextWatcher to be aware of the position it currently works with
    // this way, once a new item is attached in onBindViewHolder, it will
    // update current position MyCustomEditTextListener, reference to which is kept by ViewHolder
    private class MyCustomEditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            textList.set(position, charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }

}

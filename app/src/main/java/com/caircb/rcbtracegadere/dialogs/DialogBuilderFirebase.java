package com.caircb.rcbtracegadere.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.fragments.recolector.manifiesto2.VistaPreliminarFragment;

public class DialogBuilderFirebase extends Dialog {
    String msg="",title=null;
    private String btYesText, btNoText, btNeutralText;
    LinearLayout btnYes,btnNo,btnNeutral;

    TextView dialogText,dialogTitle,txtBtnNeutral,txtBtnYes,txtBtnNo ;
    private View.OnClickListener btYesListener=null;
    private View.OnClickListener btNoListener=null;
    private View.OnClickListener btNeutralListener=null;


    public DialogBuilderFirebase(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        CardView cardView = (CardView) layoutInflater.inflate(R.layout.dialog, null);
        setContentView(cardView);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        init();
    }

    private void  init(){

        dialogText = findViewById(R.id.DialogText);
        dialogTitle = findViewById(R.id.DialogTitle);

        btnNeutral = findViewById(R.id.btnDialogNeutral);
        btnYes = findViewById(R.id.btnDialogYes);
        btnNo = findViewById(R.id.btnDialogNo);

        txtBtnNeutral = findViewById(R.id.txtBtnNeutral);
        txtBtnYes = findViewById(R.id.txtBtnYes);
        txtBtnNo = findViewById(R.id.txtBtnNo);

        String[] array= msg.split("\\$");
        if (array.length<2){
            dialogText.setText(msg);
        }else {
            for (int i=0;i<array.length;i++){
                if (i==0){
                    String valor =array[i];
                    /*Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
                    dialogText.setTypeface(boldTypeface);*/
                    dialogText.setText(dialogText.getText()+System.getProperty ("line.separator")+ valor);

                }else {
                    dialogText.setTypeface(null, Typeface.NORMAL);
                    dialogText.setText(dialogText.getText()+System.getProperty ("line.separator")+array[i]);

                }
            }
        }
        dialogText.setTextSize(15);
        dialogText.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
        if(title!=null) {
            dialogTitle.setText(title);
            dialogTitle.setVisibility(View.VISIBLE);
        }

        if(btNeutralText!=null){
            btnNeutral.setVisibility(View.VISIBLE);
            btnNeutral.setOnClickListener(btNeutralListener==null?myListener:btNeutralListener);
            txtBtnNeutral.setText(btNeutralText);
        }

        if(btYesText!=null){
            btnYes.setVisibility(View.VISIBLE);
            btnYes.setOnClickListener(btYesListener==null?myListener:btYesListener);
            txtBtnYes.setText(btYesText);
        }

        if(btNoText!=null){
            btnNo.setVisibility(View.VISIBLE);
            btnNo.setOnClickListener(btNoListener==null?myListener:btNoListener);
            txtBtnNo.setText(btNoText);
        }

    }

    public void setMessage(@Nullable CharSequence text){
        msg = (text!=null?text.toString():"");
    }

    @Override
    public void setTitle(@Nullable CharSequence text) {
        title = (text!=null?text.toString():null);
    }

    public void setNeutralButton(@NonNull CharSequence text, View.OnClickListener onClickListener) {
        //dismiss();
        this.btNeutralText=(text!=null?text.toString():null);
        this.btNeutralListener = onClickListener;
    }

    public void setPositiveButton(@NonNull CharSequence text, View.OnClickListener onClickListener) {
        //dismiss();
        this.btYesText=(text!=null?text.toString():null);
        this.btYesListener = onClickListener;
    }

    public void setNegativeButton(@NonNull CharSequence text, View.OnClickListener onClickListener) {
        //dismiss();
        this.btNoText=(text!=null?text.toString():null);
        this.btNoListener = onClickListener;
    }

    private  View.OnClickListener myListener = new View.OnClickListener() {
        public void onClick(View v) {
            // do something when the button is clicked
            dismiss();
        }
    };
}

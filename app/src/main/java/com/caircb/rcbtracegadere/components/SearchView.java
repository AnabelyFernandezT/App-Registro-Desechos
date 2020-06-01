package com.caircb.rcbtracegadere.components;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.R;

import java.util.Timer;
import java.util.TimerTask;

public class SearchView extends LinearLayout {

    EditText lblSearch,txtBuscar;
    TextView btnCancelSearch;


    private Timer timer = new Timer();
    private final long DELAY = 1000; // in ms

    String dataSearh="";

    public interface OnSearchListener {
        void onSearch(String data);
    }

    private OnSearchListener mOnSearchListener;

    public SearchView(@NonNull Context context, AttributeSet attrs){
        super(context,attrs);
        View.inflate(context, R.layout.search_view, this);
        init();
    }

    private void init(){
        lblSearch = this.findViewById(R.id.lblsearch);
        txtBuscar = this.findViewById(R.id.txtBuscar);
        btnCancelSearch = this.findViewById(R.id.btnCancelSearch);

        lblSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                openSearch();
            }
        });

        btnCancelSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                closeSearch();
            }
        });

        txtBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //if(timer != null) timer.cancel();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(mOnSearchListener!=null && !dataSearh.equals(txtBuscar.getText().toString())){
                    dataSearh = txtBuscar.getText().toString();
                    mOnSearchListener.onSearch(dataSearh);
                    /*
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {

                        }
                    }, DELAY);
                     */
                }
            }
        });
    }

    private void openSearch(){
        if(btnCancelSearch.getVisibility()== View.GONE) {
            lblSearch.setVisibility(View.GONE);
            txtBuscar.setVisibility(View.VISIBLE);
            txtBuscar.requestFocus();
            txtBuscar.setText("");
            btnCancelSearch.setVisibility(View.VISIBLE);

            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(txtBuscar, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void closeSearch(){
        lblSearch.setVisibility(View.VISIBLE);
        txtBuscar.setVisibility(View.GONE);
        txtBuscar.requestFocus();
        txtBuscar.setText("");
        dataSearh="";
        btnCancelSearch.setVisibility(View.GONE);
        hideKeyboard();
    }

    private Activity getActivity(){
        return (Activity)getContext();
    }

    public void setOnSearchListener(@NonNull OnSearchListener l){
        mOnSearchListener=l;
    }

    public void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}

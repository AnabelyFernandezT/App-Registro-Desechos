package com.caircb.rcbtracegadere.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.generics.MyDialog;
import com.caircb.rcbtracegadere.tasks.UserRegistrarPlanta;

public class DialogKeyBoardPlanta extends MyDialog implements View.OnClickListener {
    LinearLayout btn_1, btn_2, btn_3, btn_delete, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_0, btn_decimal, btn_cancel, btn_ok;
    TextView txtPantallaCalculadora;

    String dato = "0";
    String inputDefault = "0";
    Integer code;
    boolean activatePoint = true;
    Integer idManifiestoDetalle, idManifiestoDetalleValores;

    public interface onRegisterPesoListener {
        void onFinished(double valor, Integer code, Integer idManifiestoDetalle, Integer idManifiestoDetalleValores);
        void onCanceled();
    }

    private onRegisterPesoListener mOnRegisterPesoListener;

    public DialogKeyBoardPlanta(Context context, Integer code, Integer idManifiestoDetalle, Integer idManifiestoDetalleValores) {
        super(context, R.layout.dialog_key_board_planta);
        this.code = code;
        this.idManifiestoDetalle = idManifiestoDetalle;
        this.idManifiestoDetalleValores = idManifiestoDetalleValores;
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getView());
        initBotones();
    }

    private void initBotones() {
        txtPantallaCalculadora = getView().findViewById(R.id.txtPantallaCalculadora);
        btn_0 = getView().findViewById(R.id.btn_0);
        btn_1 = getView().findViewById(R.id.btn_1);
        btn_2 = getView().findViewById(R.id.btn_2);
        btn_3 = getView().findViewById(R.id.btn_3);
        btn_4 = getView().findViewById(R.id.btn_4);
        btn_5 = getView().findViewById(R.id.btn_5);
        btn_6 = getView().findViewById(R.id.btn_6);
        btn_7 = getView().findViewById(R.id.btn_7);
        btn_8 = getView().findViewById(R.id.btn_8);
        btn_9 = getView().findViewById(R.id.btn_9);
        btn_ok = getView().findViewById(R.id.btn_ok);
        btn_cancel = getView().findViewById(R.id.btn_cancel);
        btn_decimal = getView().findViewById(R.id.btn_decimal);
        btn_delete = getView().findViewById(R.id.btn_delete);

        btn_0.setOnClickListener(this);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
        btn_6.setOnClickListener(this);
        btn_7.setOnClickListener(this);
        btn_8.setOnClickListener(this);
        btn_9.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_decimal.setOnClickListener(this);
    }

    private void setDato(String valor) {

        switch (valor) {
            case "-":
                dato = dato.length() == 1 ? inputDefault : dato.substring(0, dato.length() - 1);
                break;
            case ".":
                dato = dato.indexOf(".") == -1 ? ((!inputDefault.equals(dato) ? dato : inputDefault) + valor) : dato;
                break;
            default:
                dato = dato.equals(valor) ? (inputDefault.equals(dato) ? inputDefault : dato + valor) : (dato.equals(inputDefault) ? "" : dato) + valor;
                break;
        }
        txtPantallaCalculadora.setText(dato);
    }

    public void setActivatePoint(boolean change) {
        this.activatePoint = change;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_0:
                setDato("0");
                break;
            case R.id.btn_1:
                setDato("1");
                break;
            case R.id.btn_2:
                setDato("2");
                break;
            case R.id.btn_3:
                setDato("3");
                break;
            case R.id.btn_4:
                setDato("4");
                break;
            case R.id.btn_5:
                setDato("5");
                break;
            case R.id.btn_6:
                setDato("6");
                break;
            case R.id.btn_7:
                setDato("7");
                break;
            case R.id.btn_8:
                setDato("8");
                break;
            case R.id.btn_9:
                setDato("9");
                break;
            case R.id.btn_ok:
                if(mOnRegisterPesoListener!=null){mOnRegisterPesoListener.onFinished(Double.parseDouble(dato), code, idManifiestoDetalle, idManifiestoDetalleValores);}
                break;
            case R.id.btn_cancel:
                if(mOnRegisterPesoListener!=null){mOnRegisterPesoListener.onCanceled();}
                break;
            case R.id.btn_decimal:
                if (activatePoint)
                    setDato(".");
                break;
            case R.id.btn_delete:
                setDato("-");
                break;
            default:
                break;
        }

    }


    public void setmOnRegisterPesoListener(@Nullable onRegisterPesoListener l ){ mOnRegisterPesoListener = l;}
}
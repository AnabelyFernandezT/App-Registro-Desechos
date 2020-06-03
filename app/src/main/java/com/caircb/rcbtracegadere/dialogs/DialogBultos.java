package com.caircb.rcbtracegadere.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.adapters.ListaValoresAdapter;
import com.caircb.rcbtracegadere.database.dao.ManifiestoPaqueteDao;
import com.caircb.rcbtracegadere.database.entity.PaqueteEntity;
import com.caircb.rcbtracegadere.generics.MyDialog;
import com.caircb.rcbtracegadere.models.CatalogoItemValor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DialogBultos extends MyDialog implements View.OnClickListener {

    LinearLayout btn_1, btn_2, btn_3, btn_delete, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_0, btn_decimal, btn_cancel, btn_ok, btn_add;
    EditText txtpantalla;
    ListView listViewBultos;
    TextView txtTotal;
    AlertDialog.Builder alertDialog;
    AlertDialog alert;

    String dato = "0";
    String inputDefault = "0";
    BigDecimal subtotal= BigDecimal.ZERO;
    ListaValoresAdapter listaValoresAdapter;
    List<CatalogoItemValor> bultos;
    Integer position,idManifiesto,idManifiestoDetalle,tipoPaquete;
    PaqueteEntity pkg;
    List<String> itemsCategoriaPaquete;

    public interface OnBultoListener {
        public void onSuccessful(BigDecimal valor, int position, int cantidad, boolean isClose);

        void onCanceled();
    }

    private OnBultoListener mOnBultoListener;

    public DialogBultos(
            @NonNull Context context,
            @NonNull Integer position,
            @NonNull Integer idManifiesto,
            @NonNull Integer idManifiestoDetalle,
            @NonNull Integer tipoPaquete) {
        super(context, R.layout.dialog_bultos);

        this.position=position;
        this.idManifiesto=idManifiesto;
        this.idManifiestoDetalle=idManifiestoDetalle;
        this.tipoPaquete = tipoPaquete;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(getView());

        initBotones();
        initCategoriaPaquetes();
        loadData();
    }

    private void initBotones() {
        listViewBultos = getView().findViewById(R.id.listViewBultos);
        txtpantalla = getView().findViewById(R.id.txtpantalla);
        txtTotal = getView().findViewById(R.id.txtTotal);
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
        btn_add = getView().findViewById(R.id.btn_add);

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
        btn_add.setOnClickListener(this);
    }

    private void initCategoriaPaquetes(){

        itemsCategoriaPaquete = new ArrayList<>();
        itemsCategoriaPaquete.add(ManifiestoPaqueteDao.ARG_INFECCIOSO);
        itemsCategoriaPaquete.add(ManifiestoPaqueteDao.ARG_CORTOPUNZANTE);

        if(tipoPaquete!=null){
            pkg = MyApp.getDBO().paqueteDao().fechConsultaPaqueteEspecifico(tipoPaquete);
            if(pkg!=null && (
                    pkg.getPaquetePorRecolccion().toString().equals("1")
                    && !pkg.getFlagAdicionales()
                    && !pkg.getFlagAdicionalFunda()
                    && !pkg.getFlagAdicionalGuardian()
            ))itemsCategoriaPaquete.add(ManifiestoPaqueteDao.ARG_INFECCIOSO_CORTOPUNZANTE);
        }
    }

    private void loadData(){
        //bultos = new ArrayList<>();
        bultos = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarValores(idManifiesto,idManifiestoDetalle);
        if(bultos.size()>0){
            for (CatalogoItemValor r:bultos){
                subtotal =  subtotal.add(new BigDecimal(r.getValor()));
            }
            txtTotal.setText("KG "+subtotal);
        }
        listaValoresAdapter = new ListaValoresAdapter(getActivity(),bultos);
        listaValoresAdapter.setOnItemBultoListener(new ListaValoresAdapter.OnItemBultoListener() {
            @Override
            public void onEliminar(Integer position) {
                CatalogoItemValor item = bultos.get(position);
                MyApp.getDBO().manifiestoDetallePesosDao().deleteTableValoresById(item.getIdCatalogo());
                if(item.getTipo().length()>0)MyApp.getDBO().manifiestoPaqueteDao().quitarPaquete(idManifiesto,tipoPaquete,item.getTipo());
                subtotal = subtotal.subtract(new BigDecimal(item.getValor()));
                bultos.remove(item);
                listaValoresAdapter.notifyDataSetChanged();
                txtTotal.setText("KG "+subtotal);
            }
        });
        listViewBultos.setAdapter(listaValoresAdapter);
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
        txtpantalla.setText(dato);
    }

    private void createBulto(BigDecimal imput){
        if(imput.doubleValue()>0) {
            //si es tipo paquete .. solicitar escoger un tipo...
            if(tipoPaquete!=null) {

                if (!pkg.getFlagAdicionales() && pkg.getPaquetePorRecolccion().toString().equals("1") && bultos.size()>0){
                    messageBox("Usted no se puede aplicar mas de un paquete para esta recoleción");
                    return;
                }else {
                    showTipoPaquete(imput);
                }
            }else{
                addBulto(imput,"");
            }
        }
    }

    private void showTipoPaquete(final BigDecimal imput){

        alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("TIPO  PAQUETE");
        alertDialog.setSingleChoiceItems(itemsCategoriaPaquete.toArray(new String[itemsCategoriaPaquete.size()]), -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        if(existeBultoCategoria(itemsCategoriaPaquete.get(which)) && !pkg.getFlagAdicionales() && !pkg.getFlagAdicionalFunda()){
                            alert.dismiss();
                            messageBox("Usted no puede agregar otro bulto de la categoria "+itemsCategoriaPaquete.get(which));
                            return;
                        }

                        addBulto(imput,itemsCategoriaPaquete.get(which));
                        alert.dismiss();
                        break;
                    case 1:
                        if(existeBultoCategoria(itemsCategoriaPaquete.get(which)) && !pkg.getFlagAdicionales() && !pkg.getFlagAdicionalGuardian()){
                            alert.dismiss();
                            messageBox("Usted no puede agregar otro bulto de la categoria "+itemsCategoriaPaquete.get(which));
                            return;
                        }
                        addBulto(imput, itemsCategoriaPaquete.get(which));
                        alert.dismiss();
                        break;
                    case 2:
                        addBulto(imput, itemsCategoriaPaquete.get(which));
                        alert.dismiss();
                        break;
                }
            }
        });
        alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(true);
        //alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alert.show();
    }


    private boolean existeBultoCategoria(String categoria){
        return  MyApp.getDBO().manifiestoDetallePesosDao().existeBultoCategoriaPaquete(idManifiesto,idManifiestoDetalle,categoria);
    }

    public void addBulto(BigDecimal imput, String tipo){

        subtotal = subtotal.add(imput);
        Long id = MyApp.getDBO().manifiestoDetallePesosDao().saveValores(idManifiesto,idManifiestoDetalle,imput.doubleValue(),tipo,tipoPaquete);

        if(tipo.length()>0){MyApp.getDBO().manifiestoPaqueteDao().saveOrUpdate(idManifiesto,tipoPaquete,tipo);}

        bultos.add(new CatalogoItemValor(id.intValue(), imput.toString(),tipo));
        listaValoresAdapter.notifyDataSetChanged();
        txtTotal.setText("KG "+subtotal);
        dato="0";
        txtpantalla.setText("0");
    }

    private void aplicar(){
        BigDecimal imput = new BigDecimal(txtpantalla.getText().toString());
        if(imput.doubleValue()>0 ){
            if(bultos.size()==0) {
                createBulto(imput);
                mOnBultoListener.onSuccessful(subtotal, position, 1, true);
            }else if(bultos.size()>0){
                //preguntar si agrega el valor a un bulto...
                mOnBultoListener.onSuccessful(subtotal, position, bultos.size(), true);
            }
        }else if(imput.doubleValue()==0 && subtotal.doubleValue()>0){
            mOnBultoListener.onSuccessful(subtotal, position, bultos.size(), true);
        }
    }

    public void setOnBultoListener(@NonNull OnBultoListener l){
        mOnBultoListener =l;
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
                if(mOnBultoListener!=null){
                    aplicar();
                }
                break;
            case R.id.btn_add:
                BigDecimal imput = new BigDecimal(txtpantalla.getText().toString());
                createBulto(imput);
                break;
            case R.id.btn_cancel:
                if (mOnBultoListener != null) {
                    mOnBultoListener.onCanceled();
                }
                break;
            case R.id.btn_decimal:
                setDato(".");
                break;
            case R.id.btn_delete:
                setDato("-");
                break;
            default:
                break;
        }
    }
}

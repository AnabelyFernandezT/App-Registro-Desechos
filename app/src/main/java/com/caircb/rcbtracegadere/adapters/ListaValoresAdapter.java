package com.caircb.rcbtracegadere.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.caircb.rcbtracegadere.MainActivity;
import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.database.entity.ManifiestoDetallePesosEntity;
import com.caircb.rcbtracegadere.database.entity.RuteoRecoleccionEntity;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.dialogs.DialogBultos;
import com.caircb.rcbtracegadere.fragments.recolector.HojaRutaAsignadaFragment;
import com.caircb.rcbtracegadere.fragments.recolector.HomeTransportistaFragment;
import com.caircb.rcbtracegadere.helpers.MyConstant;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.CatalogoItemValor;
import com.caircb.rcbtracegadere.models.DtoRuteoRecoleccion;
import com.caircb.rcbtracegadere.tasks.UserRegistrarRecoleccion;
import com.caircb.rcbtracegadere.tasks.UserRegistrarRuteoRecoleccion;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.WildcardType;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ListaValoresAdapter extends ArrayAdapter<CatalogoItemValor> {

    private List<CatalogoItemValor> listaItems;
    private LayoutInflater mInflater;
    Context context;
    AlertDialog.Builder builder;
    DialogBuilder dialogBuilder;
    Integer autorizacion;
    private Integer idManifiesto;
    private Integer idManifiestoDetalle;
    private Integer registraTara;
    DialogBultos dialogBultos;
    TextView pesoNeto;
    int entradaConstructor=0;

    public interface OnItemBultoListener {
        public void onEliminar(Integer position);
    }

    public interface OnItemBultoImpresionListener {
        public void onSendImpresion(Integer position, double pesoTaraBulto);
    }

    private OnItemBultoListener mOnItemBultoListener;
    private OnItemBultoImpresionListener mOnImtemBultoImpresionListener;

    /*public ListaValoresAdapter(Context context, List<CatalogoItemValor> listaCatalogo, Integer autorizacion){
        super(context, R.layout.lista_items_calculadora, listaCatalogo);
        mInflater= LayoutInflater.from(context);
        this.listaItems = listaCatalogo;
        this.context = context;
        this.autorizacion = autorizacion;
    }*/

    public ListaValoresAdapter(Context context, List<CatalogoItemValor> listaCatalogo, Integer idManifiesto, Integer idManifiestoDetalle, Integer registraTara, Integer autorizacion) {
        super(context, R.layout.lista_items_calculadora, listaCatalogo);
        mInflater = LayoutInflater.from(context);
        this.listaItems = listaCatalogo;
        this.context = context;
        this.idManifiesto = idManifiesto;
        this.idManifiestoDetalle = idManifiestoDetalle;
        this.registraTara = registraTara;
        this.autorizacion = autorizacion;
    }

    public ListaValoresAdapter(Context context, List<CatalogoItemValor> listaCatalogo, Integer idManifiesto, Integer idManifiestoDetalle, Integer registraTara, Integer autorizacion, TextView textoPesoNeto) {
        super(context, R.layout.lista_items_calculadora, listaCatalogo);
        mInflater = LayoutInflater.from(context);
        this.listaItems = listaCatalogo;
        this.context = context;
        this.idManifiesto = idManifiesto;
        this.idManifiestoDetalle = idManifiestoDetalle;
        this.registraTara = registraTara;
        this.autorizacion = autorizacion;
        this.pesoNeto = textoPesoNeto;
        this.entradaConstructor = 1;
    }

    public static class ViewHolder {
        TextView txtItem;
        LinearLayout btnEliminar;
        TextView txtItemTipo;
        RelativeLayout btnImpresion;
        RelativeLayout btnImpresionOk;
        LinearLayout sectionTaraBulto;
        EditText txtPesoTara;
        CheckBox chkRegistrarTaraBulto;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ListaValoresAdapter.ViewHolder holder = null;
        //final CatalogoItemValor row = getItem(position);
        listaItems = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarValores(idManifiesto, idManifiestoDetalle);
        final CatalogoItemValor row = listaItems.get(position);
        String tipoSubRuta = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta") == null ? "" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta");//1 ES INDUSTRIAL, 2 ES HOSPITALARIA

        int cont = position + 1;
        LayoutInflater minflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      /*  if (convertView == null) {*/
            convertView = minflater.inflate(R.layout.lista_items_calculadora, null);
            holder = new ListaValoresAdapter.ViewHolder();

            holder.chkRegistrarTaraBulto = (CheckBox) convertView.findViewById(R.id.chkRegistrarTaraBulto);
            holder.txtItem = (TextView) convertView.findViewById(R.id.txtItem);
            holder.txtItemTipo = (TextView) convertView.findViewById(R.id.txtItemTipo);
            holder.btnEliminar = (LinearLayout) convertView.findViewById(R.id.btnEliminar);
            holder.btnImpresion = (RelativeLayout) convertView.findViewById(R.id.btnImpresion);
            holder.btnImpresionOk = (RelativeLayout) convertView.findViewById(R.id.btnImpresionOk);
            holder.sectionTaraBulto = (LinearLayout) convertView.findViewById(R.id.sectionTaraBulto);
            holder.txtPesoTara = (EditText) convertView.findViewById(R.id.txtPesoTara);
            holder.txtPesoTara.setFilters(new InputFilter[]{filter});

            if (tipoSubRuta.equals("2")) {//SI EL TIPO DE SUBRUTA ES HOSPITALARIA
                //holder.chkRegistrarTaraBulto.setVisibility(View.VISIBLE);
                holder.btnImpresion.setVisibility(View.GONE);
                holder.btnImpresionOk.setVisibility(View.GONE);
            } else {
                //holder.chkRegistrarTaraBulto.setVisibility(View.GONE);
                holder.btnImpresion.setVisibility(View.VISIBLE);
                holder.btnImpresionOk.setVisibility(View.VISIBLE);
            }

            String checkTara = registraTara.toString();
            if (checkTara.equals("1")) {
                holder.sectionTaraBulto.setVisibility(View.VISIBLE);
                //holder.chkRegistrarTaraBulto.setVisibility(View.VISIBLE);
            } else if (checkTara.equals("2")) {
                holder.sectionTaraBulto.setVisibility(View.GONE);
                //holder.chkRegistrarTaraBulto.setVisibility(View.GONE);
            }
            holder.txtPesoTara.addTextChangedListener(new MyTextWatcher(convertView,position));
            convertView.setTag(holder);
       /* } else {
            holder = (ListaValoresAdapter.ViewHolder) convertView.getTag();
        }*/
        double valorItem = Double.parseDouble(row.getValor());
        /*holder.txtPesoTara = (EditText) convertView.findViewById(R.id.txtPesoTara);
        holder.txtPesoTara.setFilters(new InputFilter[]{filter});*/
        holder.txtItem.setText("#  " + row.getNumeroBulto() + ":     " + valorItem);
        if (row.getPesoTaraBulto() == 0.0) {
            holder.txtPesoTara.setText("");
            /*holder.txtPesoTara.setEnabled(true);
            holder.chkRegistrarTaraBulto.setEnabled(true);
            holder.chkRegistrarTaraBulto.setChecked(false);*/
        } else { holder.txtPesoTara.setText(row.getPesoTaraBulto() + "");
           /* holder.txtPesoTara.setEnabled(false);
            holder.chkRegistrarTaraBulto.setEnabled(false);
            holder.chkRegistrarTaraBulto.setChecked(true);*/
        }



        final ViewHolder finalHolder = holder;

  /*      holder.txtPesoTara.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
*/
        holder.txtPesoTara.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL){
                    final String peroT = finalHolder.txtPesoTara.getText().toString().equals("") ? "0.0" : finalHolder.txtPesoTara.getText().toString();
                    final double pesoTara = Double.parseDouble(peroT);
                    final double pesoBulto = Double.parseDouble(row.getValor());
                    if (pesoTara == 0.0) {
                        if (entradaConstructor==1){
                            MyApp.getDBO().manifiestoDetallePesosDao().updatePesoTara(idManifiesto, idManifiestoDetalle, row.getIdCatalogo(), 0.0);
                            MyApp.getDBO().manifiestoDetallePesosDao().updateBanderaImpresion(idManifiesto, idManifiestoDetalle, row.getIdCatalogo(), false);
                            row.setImpresion(false);
                            if (entradaConstructor==1){
                                pesoNeto.setText("Peso Neto " + consultarPeso() + " KG");
                            }
                        }

                    } else if (pesoTara < pesoBulto) {
                        MyApp.getDBO().manifiestoDetallePesosDao().updatePesoTara(idManifiesto, idManifiestoDetalle, row.getIdCatalogo(), pesoTara);
                        MyApp.getDBO().manifiestoDetallePesosDao().updateBanderaImpresion(idManifiesto, idManifiestoDetalle, row.getIdCatalogo(), true);
                        row.setImpresion(true);
                        if (entradaConstructor==1){
                            pesoNeto.setText("Peso Neto " + consultarPeso() + " KG");
                        }
                    }
                }else{
                }
                return false;
            }
        });


        if (row.getTipo().length() > 0) {
            holder.txtItemTipo.setVisibility(View.VISIBLE);
            holder.txtItemTipo.setText(row.getTipo());
        }

        if (autorizacion.toString().equals("0")) {
            holder.btnEliminar.setEnabled(false);
        } else if (autorizacion.toString().equals("-1")) {
            holder.btnEliminar.setVisibility(View.GONE);
        } else {
            holder.btnEliminar.setEnabled(true);
        }

        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (row.isImpresion()) {

                    dialogBuilder = new DialogBuilder(getContext());
                    dialogBuilder.setMessage("¿Seguro que desea eliminar el bulto?");
                    dialogBuilder.setCancelable(false);
                    dialogBuilder.setPositiveButton("SI", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismiss();
                            if (mOnItemBultoListener != null) {
                                mOnItemBultoListener.onEliminar(position);
                            }
                        }
                    });
                    dialogBuilder.setNegativeButton("NO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismiss();
                        }
                    });
                    dialogBuilder.show();

                } else {
                    dialogBuilder = new DialogBuilder(getContext());
                    dialogBuilder.setMessage("Seguro que desea eliminarlo?");
                    dialogBuilder.setCancelable(false);
                    dialogBuilder.setPositiveButton("SI", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismiss();
                            if (mOnItemBultoListener != null) {
                                mOnItemBultoListener.onEliminar(position);
                            }
                        }
                    });
                    dialogBuilder.setNegativeButton("NO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismiss();
                        }
                    });
                    dialogBuilder.show();
                }

            }
        });
        if (!tipoSubRuta.equals("2")) {
            if (row.isImpresion()) {
                holder.btnImpresionOk.setVisibility(View.VISIBLE);
                holder.btnImpresion.setVisibility(View.GONE);
            } else {
                holder.btnImpresion.setVisibility(View.VISIBLE);
                holder.btnImpresionOk.setVisibility(View.GONE);
            }
        }


        holder.chkRegistrarTaraBulto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalHolder.chkRegistrarTaraBulto.isChecked()) {
                    final String peroT = finalHolder.txtPesoTara.getText().toString().equals("") ? "0.0" : finalHolder.txtPesoTara.getText().toString();
                    final double pesoTara = Double.parseDouble(peroT);
                    final double pesoBulto = Double.parseDouble(row.getValor());
                    String checkTara = registraTara.toString();
                    if (checkTara.equals("1")) {//Si esta checkeado ingreso de pesos tara
                        if (pesoTara == 0.0) {
                            dialogBuilder = new DialogBuilder(getContext());
                            dialogBuilder.setMessage("Debe ingresar peso tara!");
                            dialogBuilder.setCancelable(false);
                            dialogBuilder.setPositiveButton("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogBuilder.dismiss();
                                    finalHolder.chkRegistrarTaraBulto.setChecked(false);
                                }
                            });
                            dialogBuilder.show();
                        } else if (pesoTara < pesoBulto) {
                            dialogBuilder = new DialogBuilder(getContext());
                            dialogBuilder.setMessage("Desea guardar el peso de tara?");
                            dialogBuilder.setCancelable(false);
                            dialogBuilder.setPositiveButton("SI", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogBuilder.dismiss();
                                    if (mOnImtemBultoImpresionListener != null) {
                                        mOnImtemBultoImpresionListener.onSendImpresion(position, pesoTara);
                                    }
                                    listaItems = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarValores(idManifiesto, idManifiestoDetalle);
                                    finalHolder.chkRegistrarTaraBulto.setEnabled(false);
                                }
                            });
                            dialogBuilder.setNegativeButton("NO", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogBuilder.dismiss();
                                    finalHolder.chkRegistrarTaraBulto.setChecked(false);
                                }
                            });
                            dialogBuilder.show();
                        } else {
                            dialogBuilder = new DialogBuilder(getContext());
                            dialogBuilder.setMessage("El peso de la tara no puede sobrepasar el peso del bulto!");
                            dialogBuilder.setCancelable(false);
                            dialogBuilder.setPositiveButton("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogBuilder.dismiss();
                                    finalHolder.chkRegistrarTaraBulto.setChecked(false);
                                }
                            });
                            dialogBuilder.show();
                        }
                    }
                } else {

                }
            }
        });

        holder.btnImpresion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("iniciando impresion");

                dialogBuilder = new DialogBuilder(getContext());
                dialogBuilder.setMessage("Desea imprimir etiqueta para este bulto?");
                dialogBuilder.setCancelable(false);
                dialogBuilder.setPositiveButton("SI", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                        if (mOnImtemBultoImpresionListener != null) {
                            mOnImtemBultoImpresionListener.onSendImpresion(position, 0.0);
                        }
                        listaItems = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarValores(idManifiesto, idManifiestoDetalle);
                    }
                });
                dialogBuilder.setNegativeButton("NO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                    }
                });
                dialogBuilder.show();

            }
        });
        holder.btnImpresionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("no hace nada");
            }
        });

        return convertView;
    }

    public void setOnItemBultoListener(@Nullable OnItemBultoListener l) {
        mOnItemBultoListener = l;
    }

    public void setOnItemBultoImpresion(@Nullable OnItemBultoImpresionListener l) {
        mOnImtemBultoImpresionListener = l;
    }

    public void filterList(List<CatalogoItemValor> updateList) {
        this.listaItems = updateList;
        notifyDataSetChanged();
    }

    class DecimalDigitsInputFilter implements InputFilter {
        private Pattern mPattern;

        DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
            mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher matcher = mPattern.matcher(dest);
            if (!matcher.matches())
                return "";
            return null;
        }
    }

    InputFilter filter = new InputFilter() {
        final int maxDigitsBeforeDecimalPoint = 4;
        final int maxDigitsAfterDecimalPoint = 2;

        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            StringBuilder builder = new StringBuilder(dest);
            builder.replace(dstart, dend, source
                    .subSequence(start, end).toString());
            if (!builder.toString().matches(
                    "(([0-9]{1})([0-9]{0," + (maxDigitsBeforeDecimalPoint - 1) + "})?)?(\\.[0-9]{0," + maxDigitsAfterDecimalPoint + "})?"

            )) {
                if (source.length() == 0)
                    return dest.subSequence(dstart, dend);
                return "";
            }

            return null;

        }
    };

    private Double consultarPeso(){
        List<ManifiestoDetallePesosEntity> listaPesos = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarBultosManifiestoDet(idManifiestoDetalle);
        double totalPesoTaraManifiestoDetalle=0.0;
        double totalPesoValor=0.0;
        for (int i=0;i<listaPesos.size();i++){
            totalPesoTaraManifiestoDetalle=totalPesoTaraManifiestoDetalle+listaPesos.get(i).getPesoTaraBulto();
            totalPesoValor=totalPesoValor+listaPesos.get(i).getValor();
        }

        double pesoTotal=totalPesoValor-totalPesoTaraManifiestoDetalle;
        double pesoTotalMostrar = Double.parseDouble(obtieneDosDecimales(pesoTotal));
        return pesoTotalMostrar;
    }

    private class MyTextWatcher implements TextWatcher {
        private View view;
        private Integer position;
        private MyTextWatcher(View view,Integer position) {
            this.view = view;
            this.position=position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            EditText txtPesoTara = (EditText) view.findViewById(R.id.txtPesoTara);
            TextView txtItem = (TextView) view.findViewById(R.id.txtItem);
            listaItems = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarValores(idManifiesto, idManifiestoDetalle);
            final CatalogoItemValor row = listaItems.get(position);
            double pesoBulto=Double.parseDouble(row.getValor());
            /*final String peroT = txtPesoTara.getText().toString().equals("") ? "0.0" : txtPesoTara.getText().toString();
            final double pesoTara = Double.parseDouble(peroT);
            final double pesoBulto = Double.parseDouble(row.getValor());*/

            String qtyString = s.toString();
            double pesoTara = qtyString.equals("") ? 0.0:Double.parseDouble(qtyString);


            if (pesoTara == 0.0) {
                if (entradaConstructor==1){
                    pesoNeto.setText("Peso Neto " + consultarPeso() + " KG");
                }

            } else if (pesoTara < pesoBulto) {
                MyApp.getDBO().manifiestoDetallePesosDao().updatePesoTara(idManifiesto, idManifiestoDetalle, row.getIdCatalogo(), pesoTara);
                MyApp.getDBO().manifiestoDetallePesosDao().updateBanderaImpresion(idManifiesto, idManifiestoDetalle, row.getIdCatalogo(), true);
                row.setImpresion(true);
                if (entradaConstructor==1){
                    pesoNeto.setText("Peso Neto " + consultarPeso() + " KG");
                }


            } else if (pesoTara > pesoBulto){
                MyApp.getDBO().manifiestoDetallePesosDao().updatePesoTara(idManifiesto, idManifiestoDetalle, row.getIdCatalogo(), 0.0);
                MyApp.getDBO().manifiestoDetallePesosDao().updateBanderaImpresion(idManifiesto, idManifiestoDetalle, row.getIdCatalogo(), false);
                row.setImpresion(false);
                txtPesoTara.setText("");
                    pesoNeto.setText("Peso Neto " + consultarPeso() + " KG");
                    dialogBuilder = new DialogBuilder(getContext());
                    dialogBuilder.setMessage("El peso de la tara no puede sobrepasar el peso del bulto!");
                    dialogBuilder.setCancelable(false);
                    dialogBuilder.setPositiveButton("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismiss();

                        }
                    });
                    dialogBuilder.show();
            }
        }
    }

    private String obtieneDosDecimales(double valor) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(valor);
    }
}

package com.caircb.rcbtracegadere.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputFilter;
import android.text.Spanned;
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
import com.caircb.rcbtracegadere.database.entity.RuteoRecoleccionEntity;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.fragments.recolector.HojaRutaAsignadaFragment;
import com.caircb.rcbtracegadere.fragments.recolector.HomeTransportistaFragment;
import com.caircb.rcbtracegadere.helpers.MyConstant;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.CatalogoItemValor;
import com.caircb.rcbtracegadere.models.DtoRuteoRecoleccion;
import com.caircb.rcbtracegadere.tasks.UserRegistrarRecoleccion;
import com.caircb.rcbtracegadere.tasks.UserRegistrarRuteoRecoleccion;
import com.google.firebase.auth.FirebaseAuth;

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
    private Integer idManifiesto;
    private Integer idManifiestoDetalle;
    private  Integer registraTara;

    public interface OnItemBultoListener {
        public void onEliminar(Integer position);
    }

    public interface OnItemBultoImpresionListener {
        public void onSendImpresion(Integer position, double pesoTaraBulto);
    }

    private OnItemBultoListener mOnItemBultoListener;
    private OnItemBultoImpresionListener mOnImtemBultoImpresionListener;

    public ListaValoresAdapter(Context context, List<CatalogoItemValor> listaCatalogo, Integer idManifiesto, Integer idManifiestoDetalle, Integer registraTara) {
        super(context, R.layout.lista_items_calculadora, listaCatalogo);
        mInflater = LayoutInflater.from(context);
        this.listaItems = listaCatalogo;
        this.context = context;
        this.idManifiesto = idManifiesto;
        this.idManifiestoDetalle = idManifiestoDetalle;
        this.registraTara=registraTara;
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

        int cont = position + 1;
        LayoutInflater minflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = minflater.inflate(R.layout.lista_items_calculadora, null);
            holder = new ListaValoresAdapter.ViewHolder();

            holder.chkRegistrarTaraBulto = (CheckBox)convertView.findViewById(R.id.chkRegistrarTaraBulto);
            holder.txtItem = (TextView) convertView.findViewById(R.id.txtItem);
            holder.txtItemTipo = (TextView) convertView.findViewById(R.id.txtItemTipo);
            holder.btnEliminar = (LinearLayout) convertView.findViewById(R.id.btnEliminar);
            holder.btnImpresion = (RelativeLayout) convertView.findViewById(R.id.btnImpresion);
            holder.btnImpresionOk = (RelativeLayout) convertView.findViewById(R.id.btnImpresionOk);
            holder.sectionTaraBulto = (LinearLayout) convertView.findViewById(R.id.sectionTaraBulto);
            holder.txtPesoTara = (EditText) convertView.findViewById(R.id.txtPesoTara);
            //holder.txtPesoTara.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});
            holder.txtPesoTara.setFilters(new InputFilter[]{filter});

            String tipoSubRuta = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta") == null ? "" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta");//1 ES INDUSTRIAL, 2 ES HOSPITALARIA
            String checkTara = registraTara.toString();
            if (checkTara.equals("1")) {
                holder.sectionTaraBulto.setVisibility(View.VISIBLE);

                if (tipoSubRuta.equals("2")) {//SI EL TIPO DE SUBRUTA ES HOSPITALARIA
                    holder.chkRegistrarTaraBulto.setVisibility(View.VISIBLE);
                    holder.btnImpresion.setVisibility(View.GONE);
                    holder.btnImpresionOk.setVisibility(View.GONE);
                }else {
                    holder.chkRegistrarTaraBulto.setVisibility(View.GONE);
                }
            } else if (checkTara.equals("2")) {
                holder.sectionTaraBulto.setVisibility(View.GONE);
                if (tipoSubRuta.equals("2")) {//SI EL TIPO DE SUBRUTA ES HOSPITALARIA
                    holder.chkRegistrarTaraBulto.setVisibility(View.VISIBLE);
                    holder.btnImpresion.setVisibility(View.GONE);
                    holder.btnImpresionOk.setVisibility(View.GONE);
                }else {
                    holder.chkRegistrarTaraBulto.setVisibility(View.GONE);
                    holder.btnImpresion.setVisibility(View.VISIBLE);
                    holder.btnImpresionOk.setVisibility(View.VISIBLE);
                }
            }
            convertView.setTag(holder);
        } else {
            holder = (ListaValoresAdapter.ViewHolder) convertView.getTag();
        }
        double valorItem=Double.parseDouble(row.getValor());
        holder.txtItem.setText("#  " + row.getNumeroBulto() + ":     " + valorItem);
        if (row.getPesoTaraBulto()==0.0){
            holder.txtPesoTara.setText("");
            holder.txtPesoTara.setEnabled(true);
        }else {
            holder.txtPesoTara.setText(row.getPesoTaraBulto() + "");
            holder.txtPesoTara.setEnabled(false);
        }

        final ViewHolder finalHolder = holder;

        if (row.getTipo().length() > 0) {
            holder.txtItemTipo.setVisibility(View.VISIBLE);
            holder.txtItemTipo.setText(row.getTipo());
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

        if (row.isImpresion()) {
            holder.btnImpresionOk.setVisibility(View.VISIBLE);
            holder.btnImpresion.setVisibility(View.GONE);
        } else {
            holder.btnImpresion.setVisibility(View.VISIBLE);
            holder.btnImpresionOk.setVisibility(View.GONE);
        }

        holder.btnImpresion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("iniciando impresion");
                final String peroT=finalHolder.txtPesoTara.getText().toString().equals("")?"0.0":finalHolder.txtPesoTara.getText().toString();
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
                            }
                        });
                        dialogBuilder.show();
                    } else if (pesoTara < pesoBulto) {
                        dialogBuilder = new DialogBuilder(getContext());
                        dialogBuilder.setMessage("Desea imprimir etiqueta para este bulto?");
                        dialogBuilder.setCancelable(false);
                        dialogBuilder.setPositiveButton("SI", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogBuilder.dismiss();
                                if (mOnImtemBultoImpresionListener != null) {
                                    mOnImtemBultoImpresionListener.onSendImpresion(position, pesoTara);
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
                    } else {
                        dialogBuilder = new DialogBuilder(getContext());
                        dialogBuilder.setMessage("El peso de la tara no puede sobrepasar el peso del bulto!");
                        dialogBuilder.setCancelable(false);
                        dialogBuilder.setPositiveButton("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogBuilder.dismiss();
                            }
                        });
                        dialogBuilder.show();
                    }
                } else {
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
        final int maxDigitsBeforeDecimalPoint=4;
        final int maxDigitsAfterDecimalPoint=2;

        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            StringBuilder builder = new StringBuilder(dest);
            builder.replace(dstart, dend, source
                    .subSequence(start, end).toString());
            if (!builder.toString().matches(
                    "(([1-9]{1})([0-9]{0,"+(maxDigitsBeforeDecimalPoint-1)+"})?)?(\\.[0-9]{0,"+maxDigitsAfterDecimalPoint+"})?"

            )) {
                if(source.length()==0)
                    return dest.subSequence(dstart, dend);
                return "";
            }

            return null;

        }
    };

}

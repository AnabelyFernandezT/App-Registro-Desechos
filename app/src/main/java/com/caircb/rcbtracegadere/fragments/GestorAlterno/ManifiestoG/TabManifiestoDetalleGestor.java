package com.caircb.rcbtracegadere.fragments.GestorAlterno.ManifiestoG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.adapters.ManifiestoDetalleAdapter;
import com.caircb.rcbtracegadere.adapters.ManifiestoDetalleGestoresAdapter;
import com.caircb.rcbtracegadere.database.dao.ManifiestoPaqueteDao;
import com.caircb.rcbtracegadere.database.entity.ManifiestoDetalleEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoDetallePesosEntity;
import com.caircb.rcbtracegadere.database.entity.PaqueteEntity;
import com.caircb.rcbtracegadere.dialogs.DialogAgregarFotografias;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.dialogs.DialogBultos;
import com.caircb.rcbtracegadere.dialogs.DialogBultosNo;
import com.caircb.rcbtracegadere.dialogs.DialogManifiestosHijosGestores;
import com.caircb.rcbtracegadere.dialogs.DialogNotificacionDetalle;
import com.caircb.rcbtracegadere.helpers.MyCalculoPaquetes;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.RowItemManifiesto;
import com.caircb.rcbtracegadere.models.RowItemManifiestosDetalleGestores;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TabManifiestoDetalleGestor extends LinearLayout {

    private List<RowItemManifiesto> detalles;
    Window window;
    private RecyclerView recyclerView;
    ManifiestoDetalleGestoresAdapter recyclerviewAdapter;
    Integer idAppManifiesto, tipoPaquete, estadoManifiesto;
    String numeroManifiesto;
    Dialog dialogOpcioneItem;
    LinearLayout sectionRegistrarTara;
    MyCalculoPaquetes calculoPaquetes;
    FloatingActionButton mensajes;
    Integer tipoBalanza = 0;
    Integer tipoRecoleccion;
    String identificacion;
    String nombreCliente;
    String sucursal;
    private boolean isChangeTotalCreateBultos = false;
    CheckBox chkRegistrarTara;
    DialogBuilder builder;
    List<RowItemManifiestosDetalleGestores> validador;
    DialogBultos dialogBultos;

    public TabManifiestoDetalleGestor(Context context,
                                      Integer manifiestoID,
                                      Integer tipoPaquete,
                                      String identificacion,
                                      String nombreCliente,
                                      String sucursal,
                                      String numeroManifiesto,
                                      Integer estado,
                                      Integer tipoRecoleccion) {
        super(context);
        this.idAppManifiesto = manifiestoID;
        this.tipoPaquete = tipoPaquete;
        //this.detalles = detalles;
        this.identificacion=identificacion;
        this.nombreCliente=nombreCliente;
        this.sucursal=sucursal;
        this.estadoManifiesto = estado;
        this.numeroManifiesto = numeroManifiesto;
        this.tipoRecoleccion = tipoRecoleccion;
        View.inflate(context, R.layout.tab_manifiesto_detalle, this);

        init();
        loadData();
    }

    private void init() {
        mensajes = this.findViewById(R.id.fab);
        sectionRegistrarTara = this.findViewById(R.id.sectionRegistrarTara);
        chkRegistrarTara = this.findViewById(R.id.chkRegistrarTara);

        String tipoSubRuta = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta") == null ? "" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta");//1 ES INDUSTRIAL, 2 ES HOSPITALARIA
        if (tipoSubRuta.equals("2")) {//SI EL TIPO DE SUBRUTA ES HOSPITALARIA
            sectionRegistrarTara.setVisibility(VISIBLE);
            final String checkTara = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("checkTara") == null ? "" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("checkTara");

            if (checkTara.equals("1")) {
                chkRegistrarTara.setChecked(true);
            } else {
                chkRegistrarTara.setChecked(false);
            }

            // VALIDACI[ON SI HAY PESOS CON TARA INGRESADOS BLOQUEAR CHECK GENERAL DE TARA {
            int contManifiestosPesos = 0;
            List<ManifiestoDetalleEntity> manifiestosDetalle = MyApp.getDBO().manifiestoDetalleDao().fecthConsultarManifiestoDetalleByIdManifiesto(idAppManifiesto);
            for (int i = 0; i < manifiestosDetalle.size(); i++) {
                List<ManifiestoDetallePesosEntity> pesosDetalle = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarBultosManifiestoDet(manifiestosDetalle.get(i).getIdAppManifiestoDetalle());
                for (int j = 0; j < pesosDetalle.size(); j++) {
                    if (pesosDetalle.get(j).getPesoTaraBulto() > 0) {
                        contManifiestosPesos++;
                    }
                }
            }
            if (contManifiestosPesos > 0) {
                chkRegistrarTara.setEnabled(false);
            } else {
                chkRegistrarTara.setEnabled(true);
            }
            // VALIDACI[ON SI HAY PESOS CON TARA INGRESADOS BLOQUEAR CHECK GENERAL DE TARA }


            chkRegistrarTara.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (chkRegistrarTara.isChecked()) {
                        MyApp.getDBO().parametroDao().saveOrUpdate("checkTara", "1");
                        int contManifiestosPesos = 0;
                        List<ManifiestoDetalleEntity> manifiestosDetalle = MyApp.getDBO().manifiestoDetalleDao().fecthConsultarManifiestoDetalleByIdManifiesto(idAppManifiesto);
                        for (int i = 0; i < manifiestosDetalle.size(); i++) {
                            List<ManifiestoDetallePesosEntity> pesosDetalle = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarBultosManifiestoDet(manifiestosDetalle.get(i).getIdAppManifiestoDetalle());
                            for (int j = 0; j < pesosDetalle.size(); j++) {
                                if (pesosDetalle.get(j).isImpresion() == true) {
                                    contManifiestosPesos++;
                                }
                            }
                        }
                        if (contManifiestosPesos == 0) {
                            chkRegistrarTara.setChecked(true);
                            MyApp.getDBO().parametroDao().saveOrUpdate("checkTara", "1");
                        }
                    } else {
                        MyApp.getDBO().parametroDao().saveOrUpdate("checkTara", "2");

                        int contManifiestosConTara = 0;
                        List<ManifiestoDetalleEntity> manifiestosDetalle = MyApp.getDBO().manifiestoDetalleDao().fecthConsultarManifiestoDetalleByIdManifiesto(idAppManifiesto);
                        for (int i = 0; i < manifiestosDetalle.size(); i++) {
                            List<ManifiestoDetallePesosEntity> pesosDetalle = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarBultosManifiestoDet(manifiestosDetalle.get(i).getIdAppManifiestoDetalle());
                            for (int j = 0; j < pesosDetalle.size(); j++) {
                                if (pesosDetalle.get(j).getPesoTaraBulto() != 0.0) {
                                    contManifiestosConTara++;
                                }
                            }
                        }
                        if (contManifiestosConTara > 0) {
                            final DialogBuilder dialogBuilder2 = new DialogBuilder(getContext());
                            dialogBuilder2.setMessage("Ya existen bultos con peso de tara, se eliminarán los pesos de tara!");
                            dialogBuilder2.setTitle("CONFIRMACIÓN");
                            dialogBuilder2.setPositiveButton("SI", new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogBuilder2.dismiss();
                                    List<ManifiestoDetalleEntity> manifiestosDetalle = MyApp.getDBO().manifiestoDetalleDao().fecthConsultarManifiestoDetalleByIdManifiesto(idAppManifiesto);
                                    double pesoTotalSinTara = 0.0;
                                    for (int i = 0; i < manifiestosDetalle.size(); i++) {
                                        MyApp.getDBO().manifiestoDetallePesosDao().updatePesoTaraXManifiestoDetalle(idAppManifiesto, manifiestosDetalle.get(i).getIdAppManifiestoDetalle(), 0.0);
                                        List<ManifiestoDetallePesosEntity> pesosDetalle = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarBultosManifiestoDet(manifiestosDetalle.get(i).getIdAppManifiestoDetalle());
                                        for (int j = 0; j < pesosDetalle.size(); j++) {
                                            pesoTotalSinTara = pesoTotalSinTara + pesosDetalle.get(j).getValor();
                                        }
                                        MyApp.getDBO().manifiestoDetalleDao().updatePesoTotal(manifiestosDetalle.get(i).getIdAppManifiestoDetalle(), pesoTotalSinTara);
                                        pesoTotalSinTara = 0.0;
                                    }
                                    chkRegistrarTara.setChecked(false);
                                    MyApp.getDBO().parametroDao().saveOrUpdate("checkTara", "2");
                                    reloadData();
                                }
                            });
                            dialogBuilder2.setNegativeButton("NO", new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogBuilder2.dismiss();
                                    chkRegistrarTara.setChecked(true);
                                    MyApp.getDBO().parametroDao().saveOrUpdate("checkTara", "1");
                                }
                            });
                            dialogBuilder2.show();
                        } else {
                            chkRegistrarTara.setChecked(false);
                            MyApp.getDBO().parametroDao().saveOrUpdate("checkTara", "2");
                        }
                    }
                }
            });
        }else {
            sectionRegistrarTara.setVisibility(GONE);
        }


        calculoPaquetes = new MyCalculoPaquetes(idAppManifiesto, tipoPaquete);
        recyclerView = this.findViewById(R.id.recyclerManifiestoDetalle);
        recyclerviewAdapter = new ManifiestoDetalleGestoresAdapter(getContext());

    }



    @SuppressLint("RestrictedApi")
    public void loadData() {
        if (estadoManifiesto != 1) {

            sectionRegistrarTara.setVisibility(GONE);
            sectionRegistrarTara.setEnabled(false);
        }
        mensajes.setVisibility(GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        if (MyApp.getDBO().parametroDao().fecthParametroValor("auto_impresion" + MySession.getIdUsuario()).equals("1")) {
            MyApp.getDBO().manifiestoDetalleDao().updateFlagFaltaImpresionesByIdManifiesto(idAppManifiesto, false);
            detalles = MyApp.getDBO().manifiestoDetalleDao().fetchHojaRutaDetallebyIdManifiestoPadre(idAppManifiesto);
        } else {
            detalles = MyApp.getDBO().manifiestoDetalleDao().fetchHojaRutaDetallebyIdManifiestoPadre(idAppManifiesto);
        }


        recyclerviewAdapter.setTaskList(detalles);
        recyclerView.setAdapter(recyclerviewAdapter);



        recyclerviewAdapter.setOnItemClickListener(new ManifiestoDetalleGestoresAdapter.ClickListener() {
            @Override
            public void onItemClick(final int position, View v) {
                int x = 0;
                validador = MyApp.getDBO().manifiestoDao().manifiestosHijos(detalles.get(position).getIdTipoDesecho(), idAppManifiesto);
                 builder = new DialogBuilder(getContext());
                 builder.setMessage("Desea obtener datos de manifiestos");
                 builder.setCancelable(false);
                 builder.setTitle("CONFIRMACIÓN");
                builder.setPositiveButton("SI", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();
                        if(validador.size()>0) {
                            DialogManifiestosHijosGestores dialogGestoresHijos = new DialogManifiestosHijosGestores(getContext(), idAppManifiesto, detalles.get(position).getId(), detalles.get(position).getIdTipoDesecho());
                            dialogGestoresHijos.setCancelable(false);
                            dialogGestoresHijos.show();
                        }else{
                            final DialogBuilder dialog = new DialogBuilder(getContext());
                            dialog.setMessage("No se encontraron datos para este item");
                            dialog.setCancelable(false);
                            dialog.setNeutralButton("OK", new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                  dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                    }
                });
                 builder.setNegativeButton("NO", new OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         openDialogBultos(position,0,2);
                         builder.dismiss();
                     }
                 });
                 builder.show();
            }
        });

    }

    public void reloadData() {

        if (MyApp.getDBO().parametroDao().fecthParametroValor("auto_impresion" + MySession.getIdUsuario()).equals("1")) {
            MyApp.getDBO().manifiestoDetalleDao().updateFlagFaltaImpresionesByIdManifiesto(idAppManifiesto, false);
            detalles = MyApp.getDBO().manifiestoDetalleDao().fetchHojaRutaDetallebyIdManifiestoPadre(idAppManifiesto);
        } else {
            detalles = MyApp.getDBO().manifiestoDetalleDao().fetchHojaRutaDetallebyIdManifiestoPadre(idAppManifiesto);
        }
        recyclerviewAdapter.setTaskList(detalles);
        recyclerviewAdapter.notifyDataSetChanged();
    }

    private void openOpcionesItems(final Integer positionItem, final Integer idDetManifiesto) {

        dialogOpcioneItem = new Dialog(this.getContext());
       /* final ArrayList<MenuItem> myListOfItems = new ArrayList<>();
        myListOfItems.add(new MenuItem("BULTOS"));
        myListOfItems.add(new MenuItem("PAQUETES"));*/

        //dialogMenuBaseAdapter = new DialogMenuBaseAdapter(this.getContext(),myListOfItems);
        View view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.dialog_opciones_detalle, null);
        //LinearLayout txtBultos = view.findViewById(R.id.txtBultos);

        LinearLayout btnCancelar = view.findViewById(R.id.btnCancel);
        LinearLayout btnBalanzaGadere = (LinearLayout) view.findViewById(R.id.btnBalanzaGadere);
        LinearLayout btnBalanzaCliente = (LinearLayout) view.findViewById(R.id.btnBalanzaCliente);



        btnCancelar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogOpcioneItem.dismiss();
            }
        });

        dialogOpcioneItem.setTitle("OPCIONES");
        dialogOpcioneItem.setContentView(view);
        dialogOpcioneItem.setCancelable(false);
        dialogOpcioneItem.show();
    }


    public boolean validaExisteDetallesSeleccionados() {
        return MyApp.getDBO().manifiestoDetalleDao().fecthConsultarManifiestoDetalleSeleccionados(idAppManifiesto).size() > 0;
    }

    public boolean validaExisteCambioTotalBultos() {
        return isChangeTotalCreateBultos;
    }

    public void resetParameters() {
        isChangeTotalCreateBultos = false;
    }

    private void openDialogBultos(Integer position, final Integer tipoGestion, final Integer registraTara) {

        if (dialogBultos == null) {
//            dialogOpcioneItem.dismiss();
            dialogBultos = new DialogBultos(
                    getContext(),
                    position,
                    idAppManifiesto,
                    detalles.get(position).getId(),
                    tipoPaquete,
                    numeroManifiesto + "$" + detalles.get(position).getCodigo(),
                    tipoGestion,
                    registraTara
            );
            dialogBultos.setCancelable(false);
            dialogBultos.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogBultos.setOnBultoListener(new DialogBultos.OnBultoListener() {
                @Override
                public void onSuccessful(BigDecimal valor, int position, int cantidad, PaqueteEntity pkg, boolean isClose, boolean faltaImpresiones, boolean isChangeTotalBultos) {
                    if (isClose && dialogBultos != null) {
                        dialogBultos.dismiss();
                        dialogBultos = null;
                    }

                    RowItemManifiesto row = detalles.get(position);

                    List<ManifiestoDetallePesosEntity> listaPesos = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarBultosManifiestoDet(row.getId());
                    double totalPesoTaraManifiestoDetalle = 0.0;
                    for (int i = 0; i < listaPesos.size(); i++) {
                        totalPesoTaraManifiestoDetalle = totalPesoTaraManifiestoDetalle + listaPesos.get(i).getPesoTaraBulto();
                    }
                    double pesoTotalMenosTara = (valor.doubleValue()) - totalPesoTaraManifiestoDetalle;
                    row.setTipoBalanza(tipoBalanza);


                    if (row.getTipoItem() == 1) row.setCantidadBulto(cantidad); //unidad
                    else if (row.getTipoItem() == 2) row.setCantidadBulto(1); //servicio
                    else if (row.getTipoItem() == 3)
                        row.setCantidadBulto(Double.valueOf(cantidad)); //otros cantida = peso...
                    //else if(row.getTipoItem()==3) row.setCantidadBulto(row.getPeso()); //otros cantida = peso...

                    row.setEstado(true);
                    row.setFaltaImpresiones(faltaImpresiones);
                    recyclerviewAdapter.notifyDataSetChanged();
                    //actualizar datos en dbo local...
                    MyApp.getDBO().manifiestoDetalleDao().updateCantidadBultoManifiestoDetalle(row.getId(), row.getCantidadBulto(), row.getPeso(), cantidad, row.isEstado());


                    // VALIDACI[ON SI HAY PESOS CON TARA INGRESADOS BLOQUEAR CHECK GENERAL DE TARA {
                    int contManifiestosPesos = 0;
                    List<ManifiestoDetalleEntity> manifiestosDetalle = MyApp.getDBO().manifiestoDetalleDao().fecthConsultarManifiestoDetalleByIdManifiesto(idAppManifiesto);
                    for (int i = 0; i < manifiestosDetalle.size(); i++) {
                        List<ManifiestoDetallePesosEntity> pesosDetalle = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarBultosManifiestoDet(manifiestosDetalle.get(i).getIdAppManifiestoDetalle());
                        for (int j = 0; j < pesosDetalle.size(); j++) {
                            if (pesosDetalle.get(j).getPesoTaraBulto() > 0) {
                                contManifiestosPesos++;
                            }
                        }
                    }
                    if (contManifiestosPesos > 0) {
                        chkRegistrarTara.setEnabled(false);
                    } else {
                        chkRegistrarTara.setEnabled(true);
                    }
                    // VALIDACI[ON SI HAY PESOS CON TARA INGRESADOS BLOQUEAR CHECK GENERAL DE TARA }

                /*    String tipoSubruta = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta") == null ? "" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta");
                    if (tipoSubruta.equals("2")){
                        double pesoPromedio = MyApp.getDBO().manifiestoDao().selectPesoPromediobyIdManifiesto(idAppManifiesto);
                        double pesoTotal = 0.0;
                        for (int i = 0; i < detalles.size(); i++) {
                            itemManifiestoDetalleBultos = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarBultosManifiestoDet(detalles.get(i).getId());
                            for (int j = 0; j < itemManifiestoDetalleBultos.size(); j++) {
                                pesoTotal += itemManifiestoDetalleBultos.get(j).getValor();
                            }
                        }
                        if (pesoTotal > (pesoPromedio + (pesoPromedio * 0.20)) || pesoTotal < (pesoPromedio - (pesoPromedio * 0.20))) {
                            txtPesoPromedio.setText("EXISTE DIFERENCIA DE " + (pesoPromedio - pesoTotal) + " KG DEL PESO PROMEDIO");
                            MyApp.getDBO().parametroDao().saveOrUpdate("textoPesoPromedio",""+txtPesoPromedio.getText());
                            if (pesoTotal == 0.0) {
                                novedadPesoPromedio.setVisibility(GONE);
                                txtPesoPromedio.setText("");
                            } else {
                                novedadPesoPromedio.setVisibility(VISIBLE);
                            }
                        } else {
                            novedadPesoPromedio.setVisibility(GONE);
                            txtPesoPromedio.setText("");
                        }
                    }else {
                        novedadPesoPromedio.setVisibility(GONE);
                    }*/

                    //si cambia los item(add/remove) de la calculadora se resetea los valores pendientes ingresados por el usuario...
                    isChangeTotalCreateBultos = isChangeTotalBultos;
                    //calculo de paquetes...
                    if (pkg != null) {
                        calculoPaquetes.algoritmo(pkg);
                    }
                }

                @Override
                public void onCanceled(boolean faltaImpresos) {
                    if (dialogBultos != null) {
                        if (faltaImpresos) {
                            //detalles.clear();
                            reloadData();

                            // VALIDACI[ON SI HAY PESOS CON TARA INGRESADOS BLOQUEAR CHECK GENERAL DE TARA {
                            int contManifiestosPesos = 0;
                            List<ManifiestoDetalleEntity> manifiestosDetalle = MyApp.getDBO().manifiestoDetalleDao().fecthConsultarManifiestoDetalleByIdManifiesto(idAppManifiesto);
                            for (int i = 0; i < manifiestosDetalle.size(); i++) {
                                List<ManifiestoDetallePesosEntity> pesosDetalle = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarBultosManifiestoDet(manifiestosDetalle.get(i).getIdAppManifiestoDetalle());
                                for (int j = 0; j < pesosDetalle.size(); j++) {
                                    if (pesosDetalle.get(j).getPesoTaraBulto() > 0) {
                                        contManifiestosPesos++;
                                    }
                                }
                            }
                            if (contManifiestosPesos > 0) {
                                chkRegistrarTara.setEnabled(false);
                            } else {
                                chkRegistrarTara.setEnabled(true);
                            }
                            // VALIDACI[ON SI HAY PESOS CON TARA INGRESADOS BLOQUEAR CHECK GENERAL DE TARA }


                      /*      String tipoSubruta = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta") == null ? "" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta");
                            if (tipoSubruta.equals("2")){
                                double pesoPromedio = MyApp.getDBO().manifiestoDao().selectPesoPromediobyIdManifiesto(idAppManifiesto);
                                double pesoTotal = 0.0;
                                for (int i = 0; i < detalles.size(); i++) {
                                    itemManifiestoDetalleBultos = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarBultosManifiestoDet(detalles.get(i).getId());
                                    for (int j = 0; j < itemManifiestoDetalleBultos.size(); j++) {
                                        pesoTotal += itemManifiestoDetalleBultos.get(j).getValor();
                                    }
                                }
                                if (pesoTotal > (pesoPromedio + (pesoPromedio * 0.20)) || pesoTotal < (pesoPromedio - (pesoPromedio * 0.20))) {
                                    txtPesoPromedio.setText("EXISTE DIFERENCIA DE " + (pesoPromedio - pesoTotal) + " KG DEL PESO PROMEDIO");
                                    MyApp.getDBO().parametroDao().saveOrUpdate("textoPesoPromedio",""+txtPesoPromedio.getText());
                                    if (pesoTotal == 0.0) {
                                        novedadPesoPromedio.setVisibility(GONE);
                                        txtPesoPromedio.setText("");
                                    } else {
                                        novedadPesoPromedio.setVisibility(VISIBLE);
                                    }
                                } else {
                                    novedadPesoPromedio.setVisibility(GONE);
                                    txtPesoPromedio.setText("");
                                }
                            }else {
                                novedadPesoPromedio.setVisibility(GONE);
                            }
*/
                        }
                        dialogBultos.dismiss();
                        dialogBultos = null;
                    }
                }
            });
            dialogBultos.show();

            window = dialogBultos.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }
}

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
import com.caircb.rcbtracegadere.dialogs.DialogDetallesGestoresNo;
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
    FloatingActionButton btnAsociarManifiesto;
    Integer tipoBalanza = 0;
    Integer tipoRecoleccion;
    String identificacion;
    String nombreCliente;
    String sucursal;
    private boolean isChangeTotalCreateBultos = false;
    CheckBox chkRegistrarTara;
    DialogBuilder builder;
    List<RowItemManifiestosDetalleGestores> validador, lotePadre;
    Double pesoT, cantidadB;

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
        btnAsociarManifiesto = this.findViewById(R.id.fab);
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
        btnAsociarManifiesto.setVisibility(VISIBLE);
        btnAsociarManifiesto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                asociarManifiestoPadreGrupo();
                reloadData();
            }
        });
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
                            final DialogManifiestosHijosGestores dialogGestoresHijos = new DialogManifiestosHijosGestores(getContext(), idAppManifiesto, detalles.get(position).getId(), detalles.get(position).getIdTipoDesecho(),numeroManifiesto);
                            dialogGestoresHijos.setCancelable(false);
                            dialogGestoresHijos.setmOnCancelarBultoListener(new DialogManifiestosHijosGestores.OnCancelarBultoListener() {
                                @Override
                                public void onSuccesfull() {
                                    dialogGestoresHijos.dismiss();
                                }
                            });
                            dialogGestoresHijos.setmOnRegistrarBultoListener(new DialogManifiestosHijosGestores.OnRegistrarBultoListener() {
                                @Override
                                public void onSuccesfull(String numeroBultos, Integer idManifiestoDetalle, String pesoBulto) {
                                    numeroBultos = numeroBultos.substring(0,numeroBultos.length()-2);
                                    MyApp.getDBO().manifiestoDetalleDao().updateCantidadBultoManifiestoDetalle(idManifiestoDetalle, numeroBultos.equals("") ? 0.0 : Double.parseDouble(numeroBultos), pesoBulto.equals("") ? 0.0 : Double.parseDouble(pesoBulto), 0, true);
                                    MyApp.getDBO().manifiestoDetallePesosDao().saveValores(idAppManifiesto,idManifiestoDetalle,pesoBulto.equals("") ? 0.0 : Double.parseDouble(pesoBulto),"",null,"",false,numeroBultos.equals("") ? 0 : Integer.parseInt(numeroBultos),0.0);
                                    reloadData();
                                    dialogGestoresHijos.dismiss();
                                }
                            });
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
                         final DialogDetallesGestoresNo dialogDetallesGestores = new DialogDetallesGestoresNo(getContext(),detalles.get(position).getId(),idAppManifiesto);
                         dialogDetallesGestores.setCancelable(false);
                         dialogDetallesGestores.setmOnCancelarBultoListener(new DialogDetallesGestoresNo.OnCancelarBultoListener() {
                             @Override
                             public void onSuccesfull() {
                                dialogDetallesGestores.dismiss();
                             }
                         });
                         dialogDetallesGestores.setmOnRegistrarBultoListener(new DialogDetallesGestoresNo.OnRegistrarBultoListener() {
                             @Override
                             public void onSuccesfull(String numeroBultos, Integer idManifiestoDetalle, String pesoBulto) {
                                 MyApp.getDBO().manifiestoDetalleDao().updateCantidadBultoManifiestoDetalle(idManifiestoDetalle, numeroBultos.equals("") ? 0.0 : Double.parseDouble(numeroBultos), pesoBulto.equals("") ? 0.0 : Double.parseDouble(pesoBulto), 0, true);
                                 MyApp.getDBO().manifiestoDetallePesosDao().saveValores(idAppManifiesto,idManifiestoDetalle,pesoBulto.equals("") ? 0.0 : Double.parseDouble(pesoBulto),"",null,"",false,numeroBultos.equals("") ? 0 : Integer.parseInt(numeroBultos),0.0);
                                 reloadData();
                                 dialogDetallesGestores.dismiss();

                             }
                         });
                         dialogDetallesGestores.show();
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

    public boolean validaExisteDetallesSeleccionados() {
        return MyApp.getDBO().manifiestoDetalleDao().fecthConsultarManifiestoDetalleSeleccionados(idAppManifiesto).size() > 0;
    }

    public boolean validaExisteCambioTotalBultos() {
        return isChangeTotalCreateBultos;
    }

    public void resetParameters() {
        isChangeTotalCreateBultos = false;
    }

    private void asociarManifiestoPadreGrupo(){
        detalles = MyApp.getDBO().manifiestoDetalleDao().fetchHojaRutaDetallebyIdManifiestoPadre(idAppManifiesto);

        List<Integer> idDesechos = new ArrayList<>();;
        
        if(detalles.size()>0){
            for(RowItemManifiesto reg:detalles){
                if(reg.isEstado() ){
                   // idDesechos.add(reg.getIdTipoDesecho());
                    pesoT=0.0; cantidadB=0.0;
                    lotePadre = MyApp.getDBO().lotePadreDao().fetchManifiestosRecolectadosByDetalle(reg.getIdTipoDesecho());
                    if(lotePadre.size()>0){
                        for (RowItemManifiestosDetalleGestores lp:lotePadre){
                                pesoT = pesoT + lp.getPeso();
                                cantidadB = cantidadB + lp.getBultos();
                                MyApp.getDBO().lotePadreDao().asociarManifiestoPadre(idAppManifiesto, reg.getId(), numeroManifiesto, reg.getIdTipoDesecho());
                        }
                        MyApp.getDBO().manifiestoDetalleDao().updateCantidadBultoManifiestoDetalle(reg.getId(), cantidadB, pesoT, 0, true);
                        MyApp.getDBO().manifiestoDetallePesosDao().saveValores(idAppManifiesto,reg.getId(),pesoT,"",null,"",false,cantidadB.intValue(),0.0);
                    }
                }
            }
           /* for(int i=0;i<idDesechos.size();i++){
                pesoT=0.0; cantidadB=0.0;
                lotePadre = MyApp.getDBO().lotePadreDao().fetchManifiestosRecolectadosByDetalle(idDesechos.get(i));
                if(lotePadre.size()>0){
                    for (RowItemManifiestosDetalleGestores reg:lotePadre){
                        if(reg.getIdDesecho().toString().equals(idDesechos.get(i).toString())) {
                            pesoT = pesoT + reg.getPeso();
                            cantidadB = cantidadB + reg.getBultos();
                            MyApp.getDBO().lotePadreDao().asociarManifiestoPadre(idAppManifiesto, detalles.get(i).getId(), numeroManifiesto, idDesechos.get(i));
                        }
                    }
                    MyApp.getDBO().manifiestoDetalleDao().updateCantidadBultoManifiestoDetalle(detalles.get(i).getId(), cantidadB, pesoT, 0, true);
                    MyApp.getDBO().manifiestoDetallePesosDao().saveValores(idAppManifiesto,detalles.get(i).getId(),pesoT,"",null,"",false,cantidadB.intValue(),0.0);
                }
            }*/
        }


    }

}

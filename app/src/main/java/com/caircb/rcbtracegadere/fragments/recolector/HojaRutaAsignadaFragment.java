package com.caircb.rcbtracegadere.fragments.recolector;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.adapters.DialogMenuBaseAdapter;
import com.caircb.rcbtracegadere.adapters.ManifiestoAdapter;
import com.caircb.rcbtracegadere.components.SearchView;
import com.caircb.rcbtracegadere.database.AppDatabase;
import com.caircb.rcbtracegadere.database.dao.ParametroDao;
import com.caircb.rcbtracegadere.database.entity.ManifiestoDetallePesosEntity;
import com.caircb.rcbtracegadere.database.entity.ManifiestoEntity;
import com.caircb.rcbtracegadere.database.entity.PaqueteEntity;
import com.caircb.rcbtracegadere.database.entity.ParametroEntity;
import com.caircb.rcbtracegadere.database.entity.RutaInicioFinEntity;
import com.caircb.rcbtracegadere.database.entity.RuteoRecoleccionEntity;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.fragments.GestorAlterno.HomeGestorAlternoFragment;
import com.caircb.rcbtracegadere.fragments.impresora.ImpresoraConfigurarFragment;
import com.caircb.rcbtracegadere.fragments.recolector.ManifiestoLote.ManifiestoLoteFragment;
import com.caircb.rcbtracegadere.fragments.recolector.MotivoNoRecoleccion.ManifiestoNoRecoleccionFragment;
import com.caircb.rcbtracegadere.fragments.recolector.manifiesto2.Manifiesto2Fragment;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnBarcodeListener;
import com.caircb.rcbtracegadere.generics.OnRecyclerTouchListener;
import com.caircb.rcbtracegadere.helpers.MyConstant;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.DtoRuteoRecoleccion;
import com.caircb.rcbtracegadere.models.ItemManifiesto;
import com.caircb.rcbtracegadere.models.MenuItem;
import com.caircb.rcbtracegadere.models.RowItemManifiesto;
import com.caircb.rcbtracegadere.models.RowPrinters;
import com.caircb.rcbtracegadere.tasks.UserConsultarHojaRutaTask;
import com.caircb.rcbtracegadere.tasks.UserNotificacionTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HojaRutaAsignadaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HojaRutaAsignadaFragment extends MyFragment implements View.OnClickListener, OnBarcodeListener {


    LinearLayout btnRetornarListHojaRuta;

    private Window window;
    private RecyclerView recyclerView;
    private ManifiestoAdapter recyclerviewAdapter;
    private List<ManifiestoDetallePesosEntity> listManifiestoBultos;
    private OnRecyclerTouchListener touchListener;
    private List<ItemManifiesto> rowItems;
    private List<RowItemManifiesto> rowItemsManifiestoDetalle;
    private SearchView searchView;
    private DialogMenuBaseAdapter dialogMenuBaseAdapter;
    private ListView mDrawerMenuItems, mDialogMenuItems;
    DialogBuilder dialogBuilder, dialogBuilder2;
    RutaInicioFinEntity rut;
    Integer idSubRuta;
    ParametroEntity parametros, parametro_sede_planta;
    ManifiestoEntity entity;
    UserConsultarHojaRutaTask consultarHojaRutaTask;
    Integer cont=-1;
    DialogBuilder dialogBuilderScan;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HojaRutaAsignadaFragment.
     */
    // TODO: Rename and change types and number of parameters

    UserConsultarHojaRutaTask.TaskListener listenerHojaRuta = new UserConsultarHojaRutaTask.TaskListener() {
        @Override
        public void onSuccessful() {
            //loadCantidadManifiestoAsignado();
            //loadCantidadManifiestoProcesado();
            notificacionDetalleExtra();
        }
    };

    public static HojaRutaAsignadaFragment newInstance() {
        return new HojaRutaAsignadaFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setView(inflater.inflate(R.layout.fragment_hoja_ruta_asignada, container, false));
        setHideHeader();
        init();
        initItems();
        return getView();
    }

    private void init() {
        recyclerView = getView().findViewById(R.id.recyclerview);
        //recyclerviewAdapter = new ManifiestoAdapter(getActivity(),1);
        recyclerviewAdapter = new ManifiestoAdapter(getActivity(),1,0);
        btnRetornarListHojaRuta = getView().findViewById(R.id.btnRetornarListHojaRuta);
        btnRetornarListHojaRuta.setOnClickListener(this);
        searchView = getView().findViewById(R.id.searchViewManifiestos);

        searchView.setOnSearchListener(new SearchView.OnSearchListener() {
            @Override
            public void onSearch(String data) {
                filtro(data);
            }
        });

        idSubRuta = Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_ruta").getValor());
        rut = MyApp.getDBO().rutaInicioFinDao().fechConsultaInicioFinRutasE(MySession.getIdUsuario());

    }

    private void filtro(String texto) {
        List<ItemManifiesto> result = new ArrayList<>();
        List<ItemManifiesto> listaItems = new ArrayList<>();
        listaItems = MyApp.getDBO().manifiestoDao().fetchManifiestosAsigByClienteOrNumManif(texto, idSubRuta, MySession.getIdUsuario());
        rowItems = listaItems;
        recyclerviewAdapter.setTaskList(rowItems);
    }


    private void initItems() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        rowItems = MyApp.getDBO().manifiestoDao().fetchManifiestosAsigandobySubRuta(idSubRuta, MySession.getIdUsuario());
        adapterList();

    }

    private void adapterList() {
        recyclerviewAdapter.setTaskList(rowItems);
        recyclerView.setAdapter(recyclerviewAdapter);

        touchListener = new OnRecyclerTouchListener(getActivity(), recyclerView);
        touchListener.setClickable(new OnRecyclerTouchListener.OnRowClickListener() {
            @Override
            public void onRowClicked(int position) {
                Toast.makeText(getActivity(), rowItems.get(position).getNumero(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onIndependentViewClicked(int independentViewID, int position) {

            }
        }).setSwipeOptionViews(R.id.btn_manifiesto_view/*, R.id.btn_manifiesto_more*/).setSwipeable(R.id.rowFG, R.id.rowBG, new OnRecyclerTouchListener.OnSwipeOptionsClickListener() {
            @Override
            public void onSwipeOptionClicked(int viewID, final int position) {
                switch (viewID) {
                    case R.id.btn_manifiesto_view:
                        //setNavegate(ManifiestoFragment.newInstance(rowItems.get(position).getIdAppManifiesto(),false));
                        //setNavegate(Manifiesto2Fragment.newInstance(rowItems.get(position).getIdAppManifiesto()));
                        parametros = MyApp.getDBO().parametroDao().fetchParametroEspecifico("manifiesto_lote_"+rowItems.get(position).getIdAppManifiesto());


                        Integer idManifiesto_aux=0;
                        List<ItemManifiesto> rowItems = MyApp.getDBO().manifiestoDao().fetchManifiestosAsigandobySubRuta(MySession.getIdSubRuta(), MySession.getIdUsuario());
                        if(rowItems.size()>0){ idManifiesto_aux = rowItems.get(position).getIdAppManifiesto(); }
                        final ManifiestoEntity manifiesto = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(idManifiesto_aux);
                        if(manifiesto != null && manifiesto.getMensaje() != null && manifiesto.getMensaje().equals("") && parametros!=null){
                            MyApp.getDBO().parametroDao().saveOrUpdate("manifiesto_SEDE_PLANTA_"+rowItems.get(position).getIdAppManifiesto(),"1");
                        }else{
                            MyApp.getDBO().parametroDao().saveOrUpdate("manifiesto_SEDE_PLANTA_"+rowItems.get(position).getIdAppManifiesto(),"0");
                        }

                        parametro_sede_planta = MyApp.getDBO().parametroDao().fetchParametroEspecifico("manifiesto_SEDE_PLANTA_"+rowItems.get(position).getIdAppManifiesto());


                        entity = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(rowItems.get(position).getIdAppManifiesto());
                        if(parametros!=null && parametro_sede_planta.getValor().equals("1")){
                            setNavegate(ManifiestoLoteFragment.newInstance(rowItems.get(position).getIdAppManifiesto(),1,2));
                        } else if(entity.getCategoria().equals(MyConstant.ID_ENTREGA_GESTOR)) {
                            final DialogBuilder gestoresBuilder = new DialogBuilder(getActivity());
                            gestoresBuilder.setMessage("El manifiesto es de tipo ENTREGA A GESTOR.\n" +
                                    "¿Desea continuar?");
                            gestoresBuilder.setCancelable(false);
                            gestoresBuilder.setPositiveButton("SI", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    gestoresBuilder.dismiss();
                                    setNavegate(HomeGestorAlternoFragment.create());
                                }
                            });
                            gestoresBuilder.setNegativeButton("NO", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    gestoresBuilder.dismiss();
                                }
                            });
                            gestoresBuilder.show();
                        }else {
                            menu(position);
                        }
                        break;

                    /*case R.id.btn_manifiesto_more:
                        break;*/
                }
            }
        });
        //DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        //divider.setDrawable(ContextCompat.getDrawable(getActivity().getBaseContext(), R.drawable.shape_divider));
        //recyclerView.addItemDecoration(divider);
    }

    private void menu(final int position) {
        final CharSequence[] options = {"INICIAR RECOLECCIÓN", "NO RECOLECTAR", "CANCELAR"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(final DialogInterface dialog, int item) {
                if (options[item].equals("INICIAR RECOLECCIÓN")) {

                    MyApp.getDBO().parametroDao().saveOrUpdate("seleccionMenuRecoleccion","1"); // Inicia Recoleccion
                    listManifiestoBultos = MyApp.getDBO().manifiestoDetallePesosDao().fecthConsultarBultosManifiesto(rowItems.get(position).getIdAppManifiesto());
                    if (listManifiestoBultos.size() == 0) {

                        dialogBuilder = new DialogBuilder(getActivity());
                        dialogBuilder.setMessage("¿Está seguro que desea INICIAR RECOLECCIÓN?");
                        dialogBuilder.setCancelable(false);
                        dialogBuilder.setTitle("CONFIRMACIÓN");
                        dialogBuilder.setPositiveButton("SI", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogBuilder.dismiss();

                                if(rowItems.get(position).getIdentificacion().equals(MyConstant.ID_GADERE) ){
                                    final DialogBuilder dialogBuilder;
                                    dialogBuilder = new DialogBuilder(getActivity());
                                    dialogBuilder.setCancelable(false);
                                    dialogBuilder.setMessage("¿El manifiesto es tipo SEDE-PLANTA?");
                                    dialogBuilder.setPositiveButton("SI", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogBuilder.dismiss();

                                            MyApp.getDBO().parametroDao().saveOrUpdate("manifiesto_SEDE_PLANTA_"+rowItems.get(position).getIdAppManifiesto(),"0");

                                            dialogBuilderScan = new DialogBuilder(getActivity());
                                            dialogBuilderScan.setCancelable(false);
                                            dialogBuilderScan.setMessage("Para continuar escanee el CODIGO QR que debe presentarle el responsable de la sede...");
                                            dialogBuilderScan.setNegativeButton("CANCELAR", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialogBuilderScan.dismiss();
                                                }
                                            });
                                            //asociarLoteManifiesto(392);
                                            dialogBuilderScan.show();
                                        }
                                    });
                                    dialogBuilder.setNegativeButton("NO", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogBuilder.dismiss();
                                            boolean flag = false;
                                            String imp = MyApp.getDBO().parametroDao().fecthParametroValor("auto_impresion"+MySession.getIdUsuario());

                                            if (imp.equals("1"))
                                                flag = true;

                                            if (checkImpresora() || flag) {

                                                Date fecha = AppDatabase.getDateTime();
                                                ManifiestoEntity man = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(rowItems.get(position).getIdAppManifiesto());
                                                MyApp.getDBO().manifiestoDao().saveOrUpdateFechaInicioRecoleccion(rowItems.get(position).getIdAppManifiesto(), fecha);

                                                ///////////////////////////////////
                                                List<RuteoRecoleccionEntity> enty = MyApp.getDBO().ruteoRecoleccion().searchRuteoRecoleccion();
                                                //Verifico el estado del primer registro para saber si
                                                RuteoRecoleccionEntity dto = MyApp.getDBO().ruteoRecoleccion().searchUltimoRegistro();
                                                if(dto!=null){
                                                    //si esta en falso es por que solo hay un registro, actualizo el punto de llegada del manifiesto seleccionado
                                                    MyApp.getDBO().ruteoRecoleccion().updatePuntoLlegadaFechaLlegada(dto.get_id(), rowItems.get(position).getIdAppManifiesto(), fecha);
                                                }else{
                                                    MyApp.getDBO().ruteoRecoleccion().saverOrUpdate(new DtoRuteoRecoleccion(MySession.getIdSubRuta(), fecha,  rowItems.get(position).getIdAppManifiesto(),null,null,false));
                                                }
                                                List<RuteoRecoleccionEntity> enty2 = MyApp.getDBO().ruteoRecoleccion().searchRuteoRecoleccion();

                                                ///////////////////////////////////
                                                if (rowItems.get(position).getTipoPaquete() == null || rowItems.get(position).getTipoPaquete() == 0) {
                                                    int tipoSubRuta = Integer.parseInt(MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta"));
                                                    if(tipoSubRuta == 1) { //Industrial
                                                        dialogBuilder2 = new DialogBuilder(getActivity());
                                                        dialogBuilder2.setMessage("¿Va a realizar el pesaje en sitio?");
                                                        dialogBuilder2.setCancelable(false);
                                                        dialogBuilder2.setTitle("CONFIRMACIÓN");
                                                        dialogBuilder2.setPositiveButton("SI", new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                dialogBuilder2.dismiss();
                                                                //ManifiestoEntity man1 = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(rowItems.get(position).getIdAppManifiesto());
                                                                MyApp.getDBO().parametroDao().saveOrUpdate("currentTipoRecoleccion", "1");
                                                                setNavegate(Manifiesto2Fragment.newInstance(rowItems.get(position).getIdAppManifiesto(), 1, 1));

                                                            }
                                                        });
                                                        dialogBuilder2.setNegativeButton("NO", new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                dialogBuilder2.dismiss();
                                                                //ManifiestoEntity man1 = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(rowItems.get(position).getIdAppManifiesto());
                                                                MyApp.getDBO().parametroDao().saveOrUpdate("currentTipoRecoleccion", "2");
                                                                setNavegate(Manifiesto2Fragment.newInstance(rowItems.get(position).getIdAppManifiesto(), 1, 2));
                                                                MyApp.getDBO().manifiestoDao().updateNovedadEncontrada(rowItems.get(position).getIdAppManifiesto(), "Pesaje en planta");
                                                            }
                                                        });
                                                        dialogBuilder2.show();
                                                    }else{
                                                        MyApp.getDBO().parametroDao().saveOrUpdate("currentTipoRecoleccion","1");
                                                        setNavegate(Manifiesto2Fragment.newInstance(rowItems.get(position).getIdAppManifiesto(), 1, 1));
                                                    }
                                                } else {
                                                    Integer idPaquete = rowItems.get(position).getTipoPaquete();
                                                    PaqueteEntity paquetes = MyApp.getDBO().paqueteDao().fechConsultaPaquete(idPaquete);
                                                    dialogBuilder2 = new DialogBuilder(getActivity());
                                                    dialogBuilder2.setMessage("El manifiesto es de tipo paquete " + paquetes.getDescripcion());
                                                    dialogBuilder2.setCancelable(false);
                                                    dialogBuilder2.setTitle("CONFIRMACIÓN");
                                                    dialogBuilder2.setPositiveButton("Ok", new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            dialogBuilder2.dismiss();
                                                            //ManifiestoEntity man1 = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(rowItems.get(position).getIdAppManifiesto());
                                                            setNavegate(Manifiesto2Fragment.newInstance(rowItems.get(position).getIdAppManifiesto(), 1, 1));
                                                        }
                                                    });
                                                    dialogBuilder2.setNegativeButton("Atras", new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            dialogBuilder2.dismiss();
                                                        }
                                                    });
                                                    dialogBuilder2.show();
                                                }
                                            } else {
                                                Toast.makeText(getActivity(), "Impresora no Encontrada, Debe Configurar la Impresora.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    dialogBuilder.show();
                                }else{

                                    boolean flag = false;
                                    String imp = MyApp.getDBO().parametroDao().fecthParametroValor("auto_impresion"+MySession.getIdUsuario());

                                    if (imp.equals("1"))
                                        flag = true;

                                    if (checkImpresora() || flag) {

                                        Date fecha = AppDatabase.getDateTime();
                                        ManifiestoEntity man = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(rowItems.get(position).getIdAppManifiesto());
                                        MyApp.getDBO().manifiestoDao().saveOrUpdateFechaInicioRecoleccion(rowItems.get(position).getIdAppManifiesto(), fecha);

                                        ///////////////////////////////////
                                        List<RuteoRecoleccionEntity> enty = MyApp.getDBO().ruteoRecoleccion().searchRuteoRecoleccion();
                                        //Verifico el estado del primer registro para saber si
                                        RuteoRecoleccionEntity dto = MyApp.getDBO().ruteoRecoleccion().searchUltimoRegistro();
                                        if(dto!=null){
                                            //si esta en falso es por que solo hay un registro, actualizo el punto de llegada del manifiesto seleccionado
                                            MyApp.getDBO().ruteoRecoleccion().updatePuntoLlegadaFechaLlegada(dto.get_id(), rowItems.get(position).getIdAppManifiesto(), fecha);
                                        }else{
                                            MyApp.getDBO().ruteoRecoleccion().saverOrUpdate(new DtoRuteoRecoleccion(MySession.getIdSubRuta(), fecha,  rowItems.get(position).getIdAppManifiesto(),null,null,false));
                                        }
                                        List<RuteoRecoleccionEntity> enty2 = MyApp.getDBO().ruteoRecoleccion().searchRuteoRecoleccion();

                                        ///////////////////////////////////
                                        if (rowItems.get(position).getTipoPaquete() == null || rowItems.get(position).getTipoPaquete() == 0) {
                                            int tipoSubRuta = Integer.parseInt(MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta"));
                                            if(tipoSubRuta == 1) { //Industrial
                                                dialogBuilder2 = new DialogBuilder(getActivity());
                                                dialogBuilder2.setMessage("¿Va a realizar el pesaje en sitio?");
                                                dialogBuilder2.setCancelable(false);
                                                dialogBuilder2.setTitle("CONFIRMACIÓN");
                                                dialogBuilder2.setPositiveButton("SI", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dialogBuilder2.dismiss();
                                                        //ManifiestoEntity man1 = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(rowItems.get(position).getIdAppManifiesto());
                                                        MyApp.getDBO().parametroDao().saveOrUpdate("currentTipoRecoleccion", "1");
                                                        setNavegate(Manifiesto2Fragment.newInstance(rowItems.get(position).getIdAppManifiesto(), 1, 1));

                                                    }
                                                });
                                                dialogBuilder2.setNegativeButton("NO", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dialogBuilder2.dismiss();
                                                        //ManifiestoEntity man1 = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(rowItems.get(position).getIdAppManifiesto());
                                                        MyApp.getDBO().parametroDao().saveOrUpdate("currentTipoRecoleccion", "2");
                                                        setNavegate(Manifiesto2Fragment.newInstance(rowItems.get(position).getIdAppManifiesto(), 1, 2));
                                                        MyApp.getDBO().manifiestoDao().updateNovedadEncontrada(rowItems.get(position).getIdAppManifiesto(), "Pesaje en planta");
                                                    }
                                                });
                                                dialogBuilder2.show();
                                            }else{
                                                MyApp.getDBO().parametroDao().saveOrUpdate("currentTipoRecoleccion","1");
                                                setNavegate(Manifiesto2Fragment.newInstance(rowItems.get(position).getIdAppManifiesto(), 1, 1));
                                            }
                                        } else {
                                            Integer idPaquete = rowItems.get(position).getTipoPaquete();
                                            PaqueteEntity paquetes = MyApp.getDBO().paqueteDao().fechConsultaPaquete(idPaquete);
                                            dialogBuilder2 = new DialogBuilder(getActivity());
                                            dialogBuilder2.setMessage("El manifiesto es de tipo paquete " + paquetes.getDescripcion());
                                            dialogBuilder2.setCancelable(false);
                                            dialogBuilder2.setTitle("CONFIRMACIÓN");
                                            dialogBuilder2.setPositiveButton("Ok", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialogBuilder2.dismiss();
                                                    //ManifiestoEntity man1 = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(rowItems.get(position).getIdAppManifiesto());
                                                    setNavegate(Manifiesto2Fragment.newInstance(rowItems.get(position).getIdAppManifiesto(), 1, 1));
                                                }
                                            });
                                            dialogBuilder2.setNegativeButton("Atras", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialogBuilder2.dismiss();
                                                }
                                            });
                                            dialogBuilder2.show();
                                        }
                                    } else {
                                        Toast.makeText(getActivity(), "Impresora no Encontrada, Debe Configurar la Impresora.", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                /********************************************************/
                                /*
                                boolean flag = false;
                                String imp = MyApp.getDBO().parametroDao().fecthParametroValor("auto_impresion"+MySession.getIdUsuario());

                                if (imp.equals("1"))
                                    flag = true;

                                if (!MyApp.getDBO().impresoraDao().existeImpresora() || flag) {

                                    Date fecha = AppDatabase.getDateTime();
                                    ManifiestoEntity man = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(rowItems.get(position).getIdAppManifiesto());
                                    MyApp.getDBO().manifiestoDao().saveOrUpdateFechaInicioRecoleccion(rowItems.get(position).getIdAppManifiesto(), fecha);

                                    ///////////////////////////////////
                                    List<RuteoRecoleccionEntity> enty = MyApp.getDBO().ruteoRecoleccion().searchRuteoRecoleccion();
                                    //Verifico el estado del primer registro para saber si
                                    RuteoRecoleccionEntity dto = MyApp.getDBO().ruteoRecoleccion().searchUltimoRegistro();
                                    if(dto!=null){
                                        //si esta en falso es por que solo hay un registro, actualizo el punto de llegada del manifiesto seleccionado
                                        MyApp.getDBO().ruteoRecoleccion().updatePuntoLlegadaFechaLlegada(dto.get_id(), rowItems.get(position).getIdAppManifiesto(), fecha);
                                    }else{
                                        MyApp.getDBO().ruteoRecoleccion().saverOrUpdate(new DtoRuteoRecoleccion(MySession.getIdSubRuta(), fecha,  rowItems.get(position).getIdAppManifiesto(),null,null,false));
                                    }
                                    List<RuteoRecoleccionEntity> enty2 = MyApp.getDBO().ruteoRecoleccion().searchRuteoRecoleccion();

                                    ///////////////////////////////////
                                    if (rowItems.get(position).getTipoPaquete() == null || rowItems.get(position).getTipoPaquete() == 0) {
                                        int tipoSubRuta = Integer.parseInt(MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta"));
                                        if(tipoSubRuta == 1) { //Industrial
                                            dialogBuilder2 = new DialogBuilder(getActivity());
                                            dialogBuilder2.setMessage("¿Va a realizar el pesaje en sitio?");
                                            dialogBuilder2.setCancelable(false);
                                            dialogBuilder2.setTitle("CONFIRMACIÓN");
                                            dialogBuilder2.setPositiveButton("SI", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialogBuilder2.dismiss();
                                                    //ManifiestoEntity man1 = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(rowItems.get(position).getIdAppManifiesto());
                                                    MyApp.getDBO().parametroDao().saveOrUpdate("currentTipoRecoleccion", "1");
                                                    setNavegate(Manifiesto2Fragment.newInstance(rowItems.get(position).getIdAppManifiesto(), 1, 1));

                                                }
                                            });
                                            dialogBuilder2.setNegativeButton("NO", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialogBuilder2.dismiss();
                                                    //ManifiestoEntity man1 = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(rowItems.get(position).getIdAppManifiesto());
                                                    MyApp.getDBO().parametroDao().saveOrUpdate("currentTipoRecoleccion", "2");
                                                    setNavegate(Manifiesto2Fragment.newInstance(rowItems.get(position).getIdAppManifiesto(), 1, 2));
                                                    MyApp.getDBO().manifiestoDao().updateNovedadEncontrada(rowItems.get(position).getIdAppManifiesto(), "Pesaje en planta");
                                                }
                                            });
                                            dialogBuilder2.show();
                                        }else{
                                            MyApp.getDBO().parametroDao().saveOrUpdate("currentTipoRecoleccion","1");
                                            setNavegate(Manifiesto2Fragment.newInstance(rowItems.get(position).getIdAppManifiesto(), 1, 1));
                                        }
                                    } else {
                                        Integer idPaquete = rowItems.get(position).getTipoPaquete();
                                        PaqueteEntity paquetes = MyApp.getDBO().paqueteDao().fechConsultaPaquete(idPaquete);
                                        dialogBuilder2 = new DialogBuilder(getActivity());
                                        dialogBuilder2.setMessage("El manifiesto es de tipo paquete " + paquetes.getDescripcion());
                                        dialogBuilder2.setCancelable(false);
                                        dialogBuilder2.setTitle("CONFIRMACIÓN");
                                        dialogBuilder2.setPositiveButton("Ok", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialogBuilder2.dismiss();
                                                //ManifiestoEntity man1 = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(rowItems.get(position).getIdAppManifiesto());
                                                setNavegate(Manifiesto2Fragment.newInstance(rowItems.get(position).getIdAppManifiesto(), 1, 1));
                                            }
                                        });
                                        dialogBuilder2.setNegativeButton("Atras", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialogBuilder2.dismiss();
                                            }
                                        });
                                        dialogBuilder2.show();
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Impresora no Encontrada, Debe Configurar la Impresora.", Toast.LENGTH_SHORT).show();
                                }
                                */
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

                        parametros = MyApp.getDBO().parametroDao().fetchParametroEspecifico("manifiesto_lote_"+rowItems.get(position).getIdAppManifiesto());

                        Integer idManifiesto_aux=0;
                        List<ItemManifiesto> rowItems_aux = MyApp.getDBO().manifiestoDao().fetchManifiestosAsigandobySubRuta(MySession.getIdSubRuta(), MySession.getIdUsuario());
                        if(rowItems_aux.size()>0){ idManifiesto_aux = rowItems_aux.get(position).getIdAppManifiesto(); }
                        final ManifiestoEntity manifiesto = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(idManifiesto_aux);
                        if(manifiesto != null && manifiesto.getMensaje()!=null && parametros!=null){
                            if(manifiesto.getMensaje().equals("")){
                                MyApp.getDBO().parametroDao().saveOrUpdate("manifiesto_SEDE_PLANTA_"+rowItems_aux.get(position).getIdAppManifiesto(),"1");
                            }else{
                                MyApp.getDBO().parametroDao().saveOrUpdate("manifiesto_SEDE_PLANTA_"+rowItems_aux.get(position).getIdAppManifiesto(),"0");
                            }

                        }
                        parametro_sede_planta = MyApp.getDBO().parametroDao().fetchParametroEspecifico("manifiesto_SEDE_PLANTA_"+rowItems_aux.get(position).getIdAppManifiesto());

                        if(parametros!=null && parametro_sede_planta.getValor().equals("1")){
                            /********************************/
                            Date fecha = AppDatabase.getDateTime();
                            RuteoRecoleccionEntity dto = MyApp.getDBO().ruteoRecoleccion().searchUltimoRegistro();
                            if(dto!=null){
                                //si esta en falso es por que solo hay un registro, actualizo el punto de llegada del manifiesto seleccionado
                                MyApp.getDBO().ruteoRecoleccion().updatePuntoLlegadaFechaLlegada(dto.get_id(), rowItems.get(position).getIdAppManifiesto(), fecha);
                            }else{
                                MyApp.getDBO().ruteoRecoleccion().saverOrUpdate(new DtoRuteoRecoleccion(MySession.getIdSubRuta(), fecha,  rowItems.get(position).getIdAppManifiesto(),null,null,false));
                            }
                            //List<RuteoRecoleccionEntity> enty2 = MyApp.getDBO().ruteoRecoleccion().searchRuteoRecoleccion();

                            if (rowItems.get(position).getTipoPaquete() == null || rowItems.get(position).getTipoPaquete() == 0) {
                                int tipoSubRuta = Integer.parseInt(MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta"));
                                if(tipoSubRuta == 1){ //Industrial
                                    dialogBuilder2 = new DialogBuilder(getActivity());
                                    dialogBuilder2.setMessage("¿Va a realizar el pesaje en sitio?");
                                    dialogBuilder2.setCancelable(false);
                                    dialogBuilder2.setTitle("CONFIRMACIÓN");
                                    dialogBuilder2.setPositiveButton("SI", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogBuilder2.dismiss();
                                            String valor = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("currentTipoRecoleccion");
                                            int tipoRecoleccion = Integer.parseInt((valor != null) ? valor : "1");
                                            System.out.println(tipoRecoleccion+" SI");
                                            if (tipoRecoleccion==1){
                                                //ManifiestoEntity man1 = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(rowItems.get(position).getIdAppManifiesto());
                                                MyApp.getDBO().parametroDao().saveOrUpdate("currentTipoRecoleccion","1");
                                                setNavegate(Manifiesto2Fragment.newInstance(rowItems.get(position).getIdAppManifiesto(), 1, 1));
                                            }else if (tipoRecoleccion==2){
                                                dialogBuilder = new DialogBuilder(getActivity());
                                                dialogBuilder.setMessage("Usted seleccionó anteriormente, NO recolección en sitio. ¿Está seguro de continuar, se borrará los pesos?");
                                                dialogBuilder.setCancelable(false);
                                                dialogBuilder.setTitle("CONFIRMACIÓN");
                                                dialogBuilder.setPositiveButton("SI", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dialogBuilder.dismiss();
                                                        MyApp.getDBO().parametroDao().saveOrUpdate("currentTipoRecoleccion","1");
                                                        rowItemsManifiestoDetalle =  MyApp.getDBO().manifiestoDetalleDao().fetchHojaRutaDetallebyIdManifiesto(rowItems.get(position).getIdAppManifiesto());
                                                        for (int i=0;i<rowItemsManifiestoDetalle.size();i++){
                                                            MyApp.getDBO().manifiestoDetalleDao().updateCantidadBultoManifiestoDetalle(rowItemsManifiestoDetalle.get(i).getId(),0,0,0,true);
                                                        }
                                                        MyApp.getDBO().manifiestoDetallePesosDao().deleteTableValoresByIdManifiesto(rowItems.get(position).getIdAppManifiesto());
                                                        setNavegate(Manifiesto2Fragment.newInstance(rowItems.get(position).getIdAppManifiesto(), 1, 1));
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
                                    dialogBuilder2.setNegativeButton("NO", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogBuilder2.dismiss();
                                            int tipoRecoleccion = Integer.parseInt(MyApp.getDBO().parametroDao().fecthParametroValorByNombre("currentTipoRecoleccion"));
                                            System.out.println(tipoRecoleccion+" NO");
                                            if (tipoRecoleccion==2){
                                                //ManifiestoEntity man1 = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(rowItems.get(position).getIdAppManifiesto());
                                                MyApp.getDBO().parametroDao().saveOrUpdate("currentTipoRecoleccion","2");
                                                setNavegate(Manifiesto2Fragment.newInstance(rowItems.get(position).getIdAppManifiesto(), 1, 2));
                                                MyApp.getDBO().manifiestoDao().updateNovedadEncontrada(rowItems.get(position).getIdAppManifiesto(), "Pesaje en planta");
                                            }else if(tipoRecoleccion==1) {
                                                dialogBuilder = new DialogBuilder(getActivity());
                                                dialogBuilder.setMessage("Usted seleccionó anteriormente, SI recolección en sitio. ¿Está seguro de continuar, se borrará los pesos?");
                                                dialogBuilder.setCancelable(false);
                                                dialogBuilder.setTitle("CONFIRMACIÓN");
                                                dialogBuilder.setPositiveButton("SI", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dialogBuilder.dismiss();
                                                        MyApp.getDBO().parametroDao().saveOrUpdate("currentTipoRecoleccion","2");
                                                        MyApp.getDBO().manifiestoDetallePesosDao().deleteTableValoresByIdManifiesto(rowItems.get(position).getIdAppManifiesto());
                                                        rowItemsManifiestoDetalle =  MyApp.getDBO().manifiestoDetalleDao().fetchHojaRutaDetallebyIdManifiesto(rowItems.get(position).getIdAppManifiesto());
                                                        for (int i=0;i<rowItemsManifiestoDetalle.size();i++){
                                                            MyApp.getDBO().manifiestoDetalleDao().updateCantidadBultoManifiestoDetalle(rowItemsManifiestoDetalle.get(i).getId(),0,0,0,false);
                                                        }
                                                        setNavegate(Manifiesto2Fragment.newInstance(rowItems.get(position).getIdAppManifiesto(), 1, 2));
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
                                    dialogBuilder2.show();
                                }else{
                                    MyApp.getDBO().parametroDao().saveOrUpdate("currentTipoRecoleccion","1");
                                    setNavegate(Manifiesto2Fragment.newInstance(rowItems.get(position).getIdAppManifiesto(), 1, 1));
                                }
                            } else {
                                Integer idPaquete = rowItems.get(position).getTipoPaquete();
                                PaqueteEntity paquetes = MyApp.getDBO().paqueteDao().fechConsultaPaquete(idPaquete);
                                dialogBuilder2 = new DialogBuilder(getActivity());
                                dialogBuilder2.setMessage("El manifiesto es de tipo paquete " + paquetes.getDescripcion());
                                dialogBuilder2.setCancelable(false);
                                dialogBuilder2.setTitle("CONFIRMACIÓN");
                                dialogBuilder2.setPositiveButton("Continuar", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogBuilder2.dismiss();
                                        //ManifiestoEntity man1 = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(rowItems.get(position).getIdAppManifiesto());

                                        Date fecha = AppDatabase.getDateTime();
                                        RuteoRecoleccionEntity dto = MyApp.getDBO().ruteoRecoleccion().searchUltimoRegistro();
                                        if(dto!=null){
                                            //si esta en falso es por que solo hay un registro, actualizo el punto de llegada del manifiesto seleccionado
                                            MyApp.getDBO().ruteoRecoleccion().updatePuntoLlegadaFechaLlegada(dto.get_id(), rowItems.get(position).getIdAppManifiesto(), fecha);
                                        }else{
                                            MyApp.getDBO().ruteoRecoleccion().saverOrUpdate(new DtoRuteoRecoleccion(MySession.getIdSubRuta(), fecha,  rowItems.get(position).getIdAppManifiesto(),null,null,false));
                                        }
                                        //List<RuteoRecoleccionEntity> enty2 = MyApp.getDBO().ruteoRecoleccion().searchRuteoRecoleccion();

                                        setNavegate(Manifiesto2Fragment.newInstance(rowItems.get(position).getIdAppManifiesto(), 1, 1));
                                    }
                                });
                                dialogBuilder2.setNegativeButton("Regresar", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogBuilder2.dismiss();
                                    }
                                });
                                dialogBuilder2.show();
                            }
                            /********************************/
                        }else{
                            if(rowItems.get(position).getIdentificacion().equals(MyConstant.ID_GADERE) ){
                                final DialogBuilder dialogBuilder;
                                dialogBuilder = new DialogBuilder(getActivity());
                                dialogBuilder.setCancelable(false);
                                dialogBuilder.setMessage("¿El manifiesto es tipo SEDE-PLANTA?");
                                dialogBuilder.setPositiveButton("SI", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogBuilder.dismiss();

                                        MyApp.getDBO().parametroDao().saveOrUpdate("manifiesto_SEDE_PLANTA_"+rowItems.get(position).getIdAppManifiesto(),"0");

                                        dialogBuilderScan = new DialogBuilder(getActivity());
                                        dialogBuilderScan.setCancelable(false);
                                        dialogBuilderScan.setMessage("Para continuar escanee el CODIGO QR que debe presentarle el responsable de la sede...");
                                        dialogBuilderScan.setNegativeButton("CANCELAR", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialogBuilderScan.dismiss();
                                            }
                                        });
                                        //asociarLoteManifiesto(392);
                                        dialogBuilderScan.show();
                                    }
                                });
                                dialogBuilder.setNegativeButton("NO", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogBuilder.dismiss();
                                        boolean flag = false;
                                        String imp = MyApp.getDBO().parametroDao().fecthParametroValor("auto_impresion"+MySession.getIdUsuario());

                                        if (imp.equals("1"))
                                            flag = true;

                                        if (checkImpresora() || flag) {

                                            Date fecha = AppDatabase.getDateTime();
                                            ManifiestoEntity man = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(rowItems.get(position).getIdAppManifiesto());
                                            MyApp.getDBO().manifiestoDao().saveOrUpdateFechaInicioRecoleccion(rowItems.get(position).getIdAppManifiesto(), fecha);

                                            ///////////////////////////////////
                                            List<RuteoRecoleccionEntity> enty = MyApp.getDBO().ruteoRecoleccion().searchRuteoRecoleccion();
                                            //Verifico el estado del primer registro para saber si
                                            RuteoRecoleccionEntity dto = MyApp.getDBO().ruteoRecoleccion().searchUltimoRegistro();
                                            if(dto!=null){
                                                //si esta en falso es por que solo hay un registro, actualizo el punto de llegada del manifiesto seleccionado
                                                MyApp.getDBO().ruteoRecoleccion().updatePuntoLlegadaFechaLlegada(dto.get_id(), rowItems.get(position).getIdAppManifiesto(), fecha);
                                            }else{
                                                MyApp.getDBO().ruteoRecoleccion().saverOrUpdate(new DtoRuteoRecoleccion(MySession.getIdSubRuta(), fecha,  rowItems.get(position).getIdAppManifiesto(),null,null,false));
                                            }
                                            List<RuteoRecoleccionEntity> enty2 = MyApp.getDBO().ruteoRecoleccion().searchRuteoRecoleccion();

                                            ///////////////////////////////////
                                            if (rowItems.get(position).getTipoPaquete() == null || rowItems.get(position).getTipoPaquete() == 0) {
                                                int tipoSubRuta = Integer.parseInt(MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta"));
                                                if(tipoSubRuta == 1) { //Industrial
                                                    dialogBuilder2 = new DialogBuilder(getActivity());
                                                    dialogBuilder2.setMessage("¿Va a realizar el pesaje en sitio?");
                                                    dialogBuilder2.setCancelable(false);
                                                    dialogBuilder2.setTitle("CONFIRMACIÓN");
                                                    dialogBuilder2.setPositiveButton("SI", new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            dialogBuilder2.dismiss();
                                                            //ManifiestoEntity man1 = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(rowItems.get(position).getIdAppManifiesto());
                                                            MyApp.getDBO().parametroDao().saveOrUpdate("currentTipoRecoleccion", "1");
                                                            setNavegate(Manifiesto2Fragment.newInstance(rowItems.get(position).getIdAppManifiesto(), 1, 1));

                                                        }
                                                    });
                                                    dialogBuilder2.setNegativeButton("NO", new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            dialogBuilder2.dismiss();
                                                            //ManifiestoEntity man1 = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(rowItems.get(position).getIdAppManifiesto());
                                                            MyApp.getDBO().parametroDao().saveOrUpdate("currentTipoRecoleccion", "2");
                                                            setNavegate(Manifiesto2Fragment.newInstance(rowItems.get(position).getIdAppManifiesto(), 1, 2));
                                                            MyApp.getDBO().manifiestoDao().updateNovedadEncontrada(rowItems.get(position).getIdAppManifiesto(), "Pesaje en planta");
                                                        }
                                                    });
                                                    dialogBuilder2.show();
                                                }else{
                                                    MyApp.getDBO().parametroDao().saveOrUpdate("currentTipoRecoleccion","1");
                                                    setNavegate(Manifiesto2Fragment.newInstance(rowItems.get(position).getIdAppManifiesto(), 1, 1));
                                                }
                                            } else {
                                                Integer idPaquete = rowItems.get(position).getTipoPaquete();
                                                PaqueteEntity paquetes = MyApp.getDBO().paqueteDao().fechConsultaPaquete(idPaquete);
                                                dialogBuilder2 = new DialogBuilder(getActivity());
                                                dialogBuilder2.setMessage("El manifiesto es de tipo paquete " + paquetes.getDescripcion());
                                                dialogBuilder2.setCancelable(false);
                                                dialogBuilder2.setTitle("CONFIRMACIÓN");
                                                dialogBuilder2.setPositiveButton("Ok", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dialogBuilder2.dismiss();
                                                        //ManifiestoEntity man1 = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(rowItems.get(position).getIdAppManifiesto());
                                                        setNavegate(Manifiesto2Fragment.newInstance(rowItems.get(position).getIdAppManifiesto(), 1, 1));
                                                    }
                                                });
                                                dialogBuilder2.setNegativeButton("Atras", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dialogBuilder2.dismiss();
                                                    }
                                                });
                                                dialogBuilder2.show();
                                            }
                                        } else {
                                            Toast.makeText(getActivity(), "Impresora no Encontrada, Debe Configurar la Impresora.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                dialogBuilder.show();
                            } else {
                                boolean flag = false;
                                String imp = MyApp.getDBO().parametroDao().fecthParametroValor("auto_impresion"+MySession.getIdUsuario());

                                if (imp.equals("1"))
                                    flag = true;

                                if (checkImpresora() || flag) {

                                    Date fecha = AppDatabase.getDateTime();
                                    ManifiestoEntity man = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(rowItems.get(position).getIdAppManifiesto());
                                    MyApp.getDBO().manifiestoDao().saveOrUpdateFechaInicioRecoleccion(rowItems.get(position).getIdAppManifiesto(), fecha);

                                    ///////////////////////////////////
                                    List<RuteoRecoleccionEntity> enty = MyApp.getDBO().ruteoRecoleccion().searchRuteoRecoleccion();
                                    //Verifico el estado del primer registro para saber si
                                    RuteoRecoleccionEntity dto = MyApp.getDBO().ruteoRecoleccion().searchUltimoRegistro();
                                    if(dto!=null){
                                        //si esta en falso es por que solo hay un registro, actualizo el punto de llegada del manifiesto seleccionado
                                        MyApp.getDBO().ruteoRecoleccion().updatePuntoLlegadaFechaLlegada(dto.get_id(), rowItems.get(position).getIdAppManifiesto(), fecha);
                                    }else{
                                        MyApp.getDBO().ruteoRecoleccion().saverOrUpdate(new DtoRuteoRecoleccion(MySession.getIdSubRuta(), fecha,  rowItems.get(position).getIdAppManifiesto(),null,null,false));
                                    }
                                    List<RuteoRecoleccionEntity> enty2 = MyApp.getDBO().ruteoRecoleccion().searchRuteoRecoleccion();

                                    ///////////////////////////////////
                                    if (rowItems.get(position).getTipoPaquete() == null || rowItems.get(position).getTipoPaquete() == 0) {
                                        int tipoSubRuta = Integer.parseInt(MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta"));
                                        if(tipoSubRuta == 1) { //Industrial
                                            dialogBuilder2 = new DialogBuilder(getActivity());
                                            dialogBuilder2.setMessage("¿Va a realizar el pesaje en sitio?");
                                            dialogBuilder2.setCancelable(false);
                                            dialogBuilder2.setTitle("CONFIRMACIÓN");
                                            dialogBuilder2.setPositiveButton("SI", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialogBuilder2.dismiss();
                                                    //ManifiestoEntity man1 = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(rowItems.get(position).getIdAppManifiesto());
                                                    MyApp.getDBO().parametroDao().saveOrUpdate("currentTipoRecoleccion", "1");
                                                    setNavegate(Manifiesto2Fragment.newInstance(rowItems.get(position).getIdAppManifiesto(), 1, 1));

                                                }
                                            });
                                            dialogBuilder2.setNegativeButton("NO", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialogBuilder2.dismiss();
                                                    //ManifiestoEntity man1 = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(rowItems.get(position).getIdAppManifiesto());
                                                    MyApp.getDBO().parametroDao().saveOrUpdate("currentTipoRecoleccion", "2");
                                                    setNavegate(Manifiesto2Fragment.newInstance(rowItems.get(position).getIdAppManifiesto(), 1, 2));
                                                    MyApp.getDBO().manifiestoDao().updateNovedadEncontrada(rowItems.get(position).getIdAppManifiesto(), "Pesaje en planta");
                                                }
                                            });
                                            dialogBuilder2.show();
                                        }else{
                                            MyApp.getDBO().parametroDao().saveOrUpdate("currentTipoRecoleccion","1");
                                            setNavegate(Manifiesto2Fragment.newInstance(rowItems.get(position).getIdAppManifiesto(), 1, 1));
                                        }
                                    } else {
                                        Integer idPaquete = rowItems.get(position).getTipoPaquete();
                                        PaqueteEntity paquetes = MyApp.getDBO().paqueteDao().fechConsultaPaquete(idPaquete);
                                        dialogBuilder2 = new DialogBuilder(getActivity());
                                        dialogBuilder2.setMessage("El manifiesto es de tipo paquete " + paquetes.getDescripcion());
                                        dialogBuilder2.setCancelable(false);
                                        dialogBuilder2.setTitle("CONFIRMACIÓN");
                                        dialogBuilder2.setPositiveButton("Ok", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialogBuilder2.dismiss();
                                                //ManifiestoEntity man1 = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(rowItems.get(position).getIdAppManifiesto());
                                                setNavegate(Manifiesto2Fragment.newInstance(rowItems.get(position).getIdAppManifiesto(), 1, 1));
                                            }
                                        });
                                        dialogBuilder2.setNegativeButton("Atras", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialogBuilder2.dismiss();
                                            }
                                        });
                                        dialogBuilder2.show();
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Impresora no Encontrada, Debe Configurar la Impresora.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                } else if (options[item].equals("NO RECOLECTAR")) {
                    dialogBuilder = new DialogBuilder(getActivity());
                    dialogBuilder.setMessage("¿Está seguro que NO ES POSIBLE RECOLECTAR?");
                    dialogBuilder.setCancelable(false);
                    dialogBuilder.setTitle("CONFIRMACIÓN");
                    dialogBuilder.setPositiveButton("SI", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MyApp.getDBO().parametroDao().saveOrUpdate("seleccionMenuRecoleccion","2"); // No recoleccion
                            //Guardo la fecha de inicio recoleccion
                            Date fecha = AppDatabase.getDateTime();
                            //ManifiestoEntity man = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(rowItems.get(position).getIdAppManifiesto());
                            MyApp.getDBO().manifiestoDao().saveOrUpdateFechaInicioRecoleccion(rowItems.get(position).getIdAppManifiesto(), fecha);
                            //ManifiestoEntity man1 = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(rowItems.get(position).getIdAppManifiesto());

                            RuteoRecoleccionEntity dto = MyApp.getDBO().ruteoRecoleccion().searchUltimoRegistro();
                            if(dto!=null){
                                //si esta en falso es por que solo hay un registro, actualizo el punto de llegada del manifiesto seleccionado
                                MyApp.getDBO().ruteoRecoleccion().updatePuntoLlegadaFechaLlegada(dto.get_id(), rowItems.get(position).getIdAppManifiesto(), fecha);
                            }else{
                                MyApp.getDBO().ruteoRecoleccion().saverOrUpdate(new DtoRuteoRecoleccion(MySession.getIdSubRuta(), fecha,  rowItems.get(position).getIdAppManifiesto(),null,null,false));
                            }

                            dialogBuilder.dismiss();
                            setNavegate(ManifiestoNoRecoleccionFragment.newInstance(rowItems.get(position).getIdAppManifiesto(), 1));
                        }
                    });
                    dialogBuilder.setNegativeButton("NO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismiss();
                        }
                    });
                    dialogBuilder.show();


/**
 AlertDialog.Builder builderConfgirmaRecol= new AlertDialog.Builder(getActivity());
 builderConfgirmaRecol.setMessage("¿Está seguro que NO ES POSIBLE RECOLECTAR?");
 builderConfgirmaRecol.setCancelable(false);
 builderConfgirmaRecol.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
 public void onClick(DialogInterface dialog, int id) {
 //Guardo la fecha de inicio recoleccion
 Date fecha = AppDatabase.getDateTime();
 //ManifiestoEntity man = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(rowItems.get(position).getIdAppManifiesto());
 MyApp.getDBO().manifiestoDao().saveOrUpdateFechaInicioRecoleccion(rowItems.get(position).getIdAppManifiesto(),fecha);
 //ManifiestoEntity man1 = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(rowItems.get(position).getIdAppManifiesto());

 Boolean estado = MyApp.getDBO().ruteoRecoleccion().verificaEstadoPrimerRegistro(0);
 if(!estado){
 //actualizaria el primer registro
 Integer _id = MyApp.getDBO().ruteoRecoleccion().selectIdByPuntoPartida(0);
 if(_id!=null){
 MyApp.getDBO().ruteoRecoleccion().updatePrimerRegistroRuteoRecoleccion(_id, rowItems.get(position).getIdAppManifiesto(), fecha);
 }
 }else{
 //busco con punto de partida mayor a cer
 Integer idMayor = MyApp.getDBO().ruteoRecoleccion().searchRegistroPuntodePartidaMayorACero();
 if(idMayor >0){
 MyApp.getDBO().ruteoRecoleccion().updatePrimerRegistroRuteoRecoleccion(idMayor, rowItems.get(position).getIdAppManifiesto(), fecha);
 }
 }

 setNavegate(ManifiestoNoRecoleccionFragment.newInstance(rowItems.get(position).getIdAppManifiesto(),1));
 }
 }).setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
 public void onClick(DialogInterface dialog, int id) {
 dialog.dismiss();
 }
 });
 AlertDialog dialogConfirmacion = builderConfgirmaRecol.create();
 dialogConfirmacion.show();
 ***/
                } else if (options[item].equals("CANCELAR")) {
                    MyApp.getDBO().parametroDao().saveOrUpdate("seleccionMenuRecoleccion",""); // Cancelar
                    dialog.dismiss();
                }

            }
        });
        builder.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRetornarListHojaRuta:
                List<RuteoRecoleccionEntity> enty = MyApp.getDBO().ruteoRecoleccion().searchRuteoRecoleccion(); //////////
                Boolean estado = MyApp.getDBO().ruteoRecoleccion().verificaEstadoPrimerRegistro(0);
                if (!estado) {
                    Integer _id = MyApp.getDBO().ruteoRecoleccion().selectIdByPuntoPartida(0);
                    if (_id != null) {
                        MyApp.getDBO().ruteoRecoleccion().updatePrimerRegistroRuteoRecoleccion(_id, null, null);
                    }
                }
                Integer idMayor = MyApp.getDBO().ruteoRecoleccion().searchRegistroPuntodePartidaMayorACero();
                MyApp.getDBO().ruteoRecoleccion().updatePrimerRegistroRuteoRecoleccion(idMayor, null, null);

                List<RuteoRecoleccionEntity> enty2 = MyApp.getDBO().ruteoRecoleccion().searchRuteoRecoleccion(); //////////
                setNavegate(HomeTransportistaFragment.create());
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (recyclerView != null) {
            recyclerView.addOnItemTouchListener(touchListener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //recyclerView.destroyDrawingCache();
    }
    private void notificacionDetalleExtra(){
        Integer idManifiesto=0;
        List<ItemManifiesto> rowItems = MyApp.getDBO().manifiestoDao().fetchManifiestosAsigandobySubRuta(MySession.getIdSubRuta(), MySession.getIdUsuario());
        if(rowItems.size()>0){
            idManifiesto = (cont != -1) ? rowItems.get(cont).getIdAppManifiesto() : 0;
        }
        final ManifiestoEntity manifiesto = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(idManifiesto);
        if(manifiesto!=null && manifiesto.getMensaje()!=null && (!manifiesto.getMensaje().equals(""))) {
            UserNotificacionTask notificaion = new UserNotificacionTask(getActivity(), manifiesto.getIdAppManifiesto(),
                    manifiesto.getMensaje(),
                    2,
                    "1", 0.0);
            notificaion.setOnRegisterListener(new UserNotificacionTask.OnNotificacionListener() {
                @Override
                public void onSuccessful() {
                    messageBox(manifiesto.getMensaje() + ". Espere respuesta de Back office");
                }
            });
            notificaion.execute();
        }
        else
            setNavegate(ManifiestoLoteFragment.newInstance(idManifiesto,1,2));

        cont = -1;
    }

    private void asociarLoteManifiesto(final Integer lote){
        if (dialogBuilder!= null)
        {
            dialogBuilder.dismiss();
            dialogBuilder = null;
        }

        dialogBuilder = new DialogBuilder(getActivity());
        dialogBuilder.setMessage("¿Asociar el manifiesto seleccionado con el lote de sede N° " + lote.toString() + " ?");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("SI", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialogBuilderScan!= null)
                    dialogBuilderScan.dismiss();
                dialogBuilder.dismiss();

                List<Integer> listaManifiesto = new ArrayList<>();

                try{
                    List<ItemManifiesto> rowItems = MyApp.getDBO().manifiestoDao().fetchManifiestosAsigandobySubRuta(MySession.getIdSubRuta(), MySession.getIdUsuario());

                    if(rowItems.size()>0) {
                        for (int i=0; i<rowItems.size();i++) {
                            if(rowItems.get(i).getIdentificacion().equals(MyConstant.ID_GADERE)){
                                listaManifiesto.add(rowItems.get(i).getIdAppManifiesto());
                                cont=i;
                                break;
                            }
                        }
                    }

                    try {
                        if (cont != -1) {
                            consultarHojaRutaTask = new UserConsultarHojaRutaTask(getActivity(), rowItems.get(cont).getIdAppManifiesto(), lote, listenerHojaRuta);
                            consultarHojaRutaTask.execute();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }catch (Exception e){
                    messageBox(e.getMessage());
                }
            }
        });
        dialogBuilder.setNegativeButton("NO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialogBuilderScan!= null)
                    dialogBuilderScan.dismiss();
                dialogBuilder.dismiss();
            }
        });
        dialogBuilder.show();
    }

    private boolean checkImpresora(){
        String data = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta");
        return MyApp.getDBO().impresoraDao().existeImpresora(data!=null && data.length()>0?Integer.parseInt(data):0);
    }

    @Override
    public void reciveData(String data) {
        try {
            asociarLoteManifiesto(Integer.parseInt(data));
        }
        catch (Exception e)
        {
            System.out.println(e.getStackTrace());
            messageBox("El código escaneado no es de tipo Lote.");
        }
    }
}
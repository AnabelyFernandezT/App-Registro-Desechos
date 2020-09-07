package com.caircb.rcbtracegadere.fragments.Sede;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.adapters.DialogMenuBaseAdapter;
import com.caircb.rcbtracegadere.adapters.ManifiestoAdapter;
import com.caircb.rcbtracegadere.adapters.ManifiestoAdapterSede;
import com.caircb.rcbtracegadere.components.SearchView;
import com.caircb.rcbtracegadere.database.entity.ParametroEntity;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.dialogs.DialogBultosPlanta;
import com.caircb.rcbtracegadere.dialogs.DialogInfoCodigoQR;
import com.caircb.rcbtracegadere.dialogs.DialogInfoCodigoQRSede;
import com.caircb.rcbtracegadere.fragments.Hoteles.HomeHotelFragment;
import com.caircb.rcbtracegadere.fragments.planta.HomePlantaFragment;
import com.caircb.rcbtracegadere.fragments.planta.ManifiestoPlantaFragment;
import com.caircb.rcbtracegadere.fragments.recolector.HomeTransportistaFragment;
import com.caircb.rcbtracegadere.fragments.recolector.MotivoNoRecoleccion.ManifiestoNoRecoleccionFragment;
import com.caircb.rcbtracegadere.fragments.recolector.manifiesto2.Manifiesto2Fragment;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnBarcodeListener;
import com.caircb.rcbtracegadere.generics.OnRecyclerTouchListener;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.ItemManifiesto;
import com.caircb.rcbtracegadere.models.ItemManifiestoSede;
import com.caircb.rcbtracegadere.tasks.UserConsultaCodigoQrValidadorTask;
import com.caircb.rcbtracegadere.tasks.UserRegistrarFinLoteHospitalesTask;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HojaRutaAsignadaSedeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HojaRutaAsignadaSedeFragment extends MyFragment implements View.OnClickListener, OnBarcodeListener {


    LinearLayout btnRetornarListHojaRuta;

    private RecyclerView recyclerView;
    private ManifiestoAdapterSede recyclerviewAdapter;
    UserRegistrarFinLoteHospitalesTask userRegistrarFinLoteHospitales;
    private OnRecyclerTouchListener touchListener;
    private List<ItemManifiestoSede> rowItems;
    private SearchView searchView;
    DialogInfoCodigoQRSede dialogCodigoQR;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment HojaRutaAsignadaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HojaRutaAsignadaSedeFragment newInstance() {
        return new HojaRutaAsignadaSedeFragment();
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

    private void init(){
        recyclerView = getView().findViewById(R.id.recyclerview);
        recyclerviewAdapter = new ManifiestoAdapterSede(getActivity());
        btnRetornarListHojaRuta = getView().findViewById(R.id.btnRetornarListHojaRuta);
        btnRetornarListHojaRuta.setOnClickListener(this);
        searchView = getView().findViewById(R.id.searchViewManifiestos);

        searchView.setOnSearchListener(new SearchView.OnSearchListener() {
            @Override
            public void onSearch(String data) {
                filtro(data);
            }
        });
        rowItems = MyApp.getDBO().manifiestoSedeDao().fetchManifiestosAsigByClienteOrNumManif();
        System.out.println("");

        String banderaValidacion = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_bandera_validacion") == null ? "0" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_bandera_validacion");
        if (banderaValidacion.equals("0")) {
            String estadoCodigoQr = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_estadoCodigoQr") == null ? "0" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_estadoCodigoQr");
            System.out.println(estadoCodigoQr);
            if (estadoCodigoQr.equals("1")) {
                String idSubRuta = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_idSubruta") == null ? "0" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_idSubruta");
                System.out.println(idSubRuta);
                MySession.setIdSubruta(Integer.parseInt(idSubRuta));
                UserConsultaCodigoQrValidadorTask consultaCodigoQrValidadorTask = new UserConsultaCodigoQrValidadorTask(getActivity());
                consultaCodigoQrValidadorTask.setOnCodigoQrListener(new UserConsultaCodigoQrValidadorTask.OnCodigoQrListener() {
                    @Override
                    public void onSuccessful() {
                        int contManifiestosCerrados = 0;
                        ParametroEntity parametro = MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_vehiculo");
                        String valor = parametro == null ? "-1" : parametro.getValor();
                        Integer idVehiculo = Integer.parseInt(valor.equals("null") ? "-1":valor);
                        rowItems = MyApp.getDBO().manifiestoSedeDao().fetchManifiestosAsigByClienteOrNumManif();
                        for (int i = 0; i < rowItems.size(); i++) {
                            if (rowItems.get(i).getEstado().equals(1)) {
                                contManifiestosCerrados++;
                            }
                        }
                        if (contManifiestosCerrados == 0) {
                            String placaSinvronizada = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_placa_Planta") == null ? "" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_placa_Planta");
                            final DialogBuilder dialogBuilder;
                            dialogBuilder = new DialogBuilder(getActivity());
                            dialogBuilder.setMessage("Ha finalizado la Recepción del vehiculo " + placaSinvronizada);
                            dialogBuilder.setCancelable(false);
                            dialogBuilder.setPositiveButton("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogBuilder.dismiss();
                                    String idSubRuta = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_idSubruta") == null ? "0" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_idSubruta");
                                    System.out.println(idSubRuta);
                                    if (!idSubRuta.equals("0")) {
                                        int idSubRutaEnviar = Integer.parseInt(idSubRuta);
                                        userRegistrarFinLoteHospitales = new UserRegistrarFinLoteHospitalesTask(getActivity(), idSubRutaEnviar, 0, 4);
                                        userRegistrarFinLoteHospitales.setOnFinLoteListener(new UserRegistrarFinLoteHospitalesTask.OnFinLoteListener() {
                                            @Override
                                            public void onSuccessful() {
                                                MyApp.getDBO().parametroDao().saveOrUpdate("current_bandera_validacion", "1");
                                                String idVehiculo=MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_vehiculo")== null ? "" :MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_vehiculo");
                                                MyApp.getDBO().parametroDao().saveOrUpdate("vehiculo_planta"+idVehiculo,""+0);
                                                setNavegate(HomeSedeFragment.create());
                                            }

                                            @Override
                                            public void onFailure() {

                                            }
                                        });
                                        userRegistrarFinLoteHospitales.execute();
                                    }
                                }
                            });
                            dialogBuilder.show();
                        }
                    }

                    @Override
                    public void onFailure() {

                    }
                });
                consultaCodigoQrValidadorTask.execute();

            }
        }

    }

    private void filtro(String texto){
        Integer idVehiculo = Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_vehiculo").getValor());

        List<ItemManifiestoSede> result = new ArrayList<>();
        List<ItemManifiestoSede> listaItems = new ArrayList<>() ;
        listaItems =  MyApp.getDBO().manifiestoSedeDao().fetchManifiestosAsigByClienteOrNumManif(texto,idVehiculo);
        rowItems=listaItems;
        recyclerviewAdapter.setTaskList(rowItems);
    }


    private void initItems() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        String estado = MyApp.getDBO().parametroDao().fecthParametroValor("loteBandera_sedes");
        System.out.println(estado);

        //String numeroLote = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_inicio_lote");
        //rowItems = MyApp.getDBO().manifiestoSedeDao().fetchManifiestosAsigByClienteOrNumManif();
        /*
        if(rowItems.size()>0){

            for(ItemManifiestoSede reg: rowItems){
                if(reg.getEstado() == 3){
                    MyApp.getDBO().manifiestoSedeDao().updateManifiestoAtado(reg.getIdAppManifiesto(), numeroLote);
                }
            }
        }
         */

        //rowItems = MyApp.getDBO().manifiestoSedeDao().fetchManifiestosAsigByClienteOrNumManifInicioLote(numeroLote);
        rowItems = MyApp.getDBO().manifiestoSedeDao().fetchManifiestosAsigByClienteOrNumManif();

        //rowItems = MyApp.getDBO().manifiestoSedeDao().fetchManifiestosAsigByClienteOrNumManif();
        adapterList();
    }

    private void adapterList(){
        recyclerviewAdapter.setTaskList(rowItems);
        recyclerView.setAdapter(recyclerviewAdapter);

        touchListener = new OnRecyclerTouchListener(getActivity(),recyclerView);
        touchListener.setClickable(new OnRecyclerTouchListener.OnRowClickListener() {
            @Override
            public void onRowClicked(int position) {
                Toast.makeText(getActivity(),rowItems.get(position).getNumeroManifiesto(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onIndependentViewClicked(int independentViewID, int position) {

            }
        }).setSwipeOptionViews(R.id.btn_manifiesto_view, R.id.btn_manifiesto_more).setSwipeable(R.id.rowFG, R.id.rowBG, new OnRecyclerTouchListener.OnSwipeOptionsClickListener() {
            @Override
            public void onSwipeOptionClicked(int viewID, final int position) {
                switch (viewID){
                    case R.id.btn_manifiesto_view:
                        //setNavegate(ManifiestoFragment.newInstance(rowItems.get(position).getIdAppManifiesto(),false));
                        //setNavegate(Manifiesto2Fragment.newInstance(rowItems.get(position).getIdAppManifiesto()));
                        //menu(position);
                        setNavegate(ManifiestoSedeFragment.newInstance(rowItems.get(position).getIdAppManifiesto()));
                        break;
                    case R.id.btn_manifiesto_more:
                        break;
                }
            }
        });
        //DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        //divider.setDrawable(ContextCompat.getDrawable(getActivity().getBaseContext(), R.drawable.shape_divider));
        //recyclerView.addItemDecoration(divider);
    }

    @Override
    public void reciveData(String data) {

        Boolean estadoBulto = MyApp.getDBO().manifiestoDetalleValorSede().verificarBultoEstado(data);

        if(estadoBulto==null){
            messageBox("CODIGO QR NO EXISTE..!");
        }else{
            if (estadoBulto){
                messageBox("EL BULTO YA SE ENCUENTRA REGISTRADO..!");
            }else {
                //MyApp.getDBO().manifiestoDetalleValorSede().actualizarBultoEstado(data);
                dialogCodigoQR = new DialogInfoCodigoQRSede(getActivity(),data);
                dialogCodigoQR.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogCodigoQR.setCancelable(false);
                dialogCodigoQR.setmOnclickSedeListener(new DialogBultosPlanta.onclickSedeListener() {
                    @Override
                    public void onSucefull() {
                        rowItems = MyApp.getDBO().manifiestoSedeDao().fetchManifiestosAsigByClienteOrNumManif();
                        adapterList();
                    }
                });
                dialogCodigoQR.show();
            }
        }

        /*List<DtoDespachoCodigoBarras> listaCodigoBarras = getListBultosByCodigoBarras(data);
        String mensaje;

        if(listaCodigoBarras.size() > 0){
            if(listaCodigoBarras.size() > 1){
                openSeleccionarListaEmpaque(listaCodigoBarras);
            }else{
                DtoDespachoCodigoBarras codigo = listaCodigoBarras.get(0);
                Integer despachoDetalleId = codigo.getDespachoDetalleId();
                Integer cantidad = codigo.getCantidad();
                tratamientoProductoEncontrado(despachoDetalleId, cantidad);
            }
        }else{
            mensaje = "EL ITEM " + data + " NO EXISTE DENTRO DE NINGUN DESPACHO";
            mostrarMensajeBarra(0, mensaje);
        }*/
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRetornarListHojaRuta:
                setNavegate(HomeSedeFragment.create());
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(recyclerView!=null) {
            recyclerView.addOnItemTouchListener(touchListener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //recyclerView.destroyDrawingCache();
    }
}
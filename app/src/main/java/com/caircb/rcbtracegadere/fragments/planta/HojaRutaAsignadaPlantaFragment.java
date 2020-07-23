package com.caircb.rcbtracegadere.fragments.planta;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.adapters.ManifiestoAdapterSede;
import com.caircb.rcbtracegadere.components.SearchView;
import com.caircb.rcbtracegadere.database.entity.ParametroEntity;
import com.caircb.rcbtracegadere.dialogs.DialogBultosPlanta;
import com.caircb.rcbtracegadere.dialogs.DialogInfoCodigoQR;
import com.caircb.rcbtracegadere.dialogs.DialogPlacas;
import com.caircb.rcbtracegadere.fragments.Sede.HomeSedeFragment;
import com.caircb.rcbtracegadere.fragments.Sede.ManifiestoSedeFragment;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnBarcodeListener;
import com.caircb.rcbtracegadere.generics.OnRecyclerTouchListener;
import com.caircb.rcbtracegadere.models.ItemManifiestoSede;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HojaRutaAsignadaPlantaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HojaRutaAsignadaPlantaFragment extends MyFragment implements View.OnClickListener, OnBarcodeListener {


    LinearLayout btnRetornarListHojaRuta;

    private RecyclerView recyclerView;
    private ManifiestoAdapterSede recyclerviewAdapter;

    private OnRecyclerTouchListener touchListener;
    private List<ItemManifiestoSede> rowItems;
    private SearchView searchView;
    DialogInfoCodigoQR dialogCodigoQR;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment HojaRutaAsignadaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HojaRutaAsignadaPlantaFragment newInstance() {
        return new HojaRutaAsignadaPlantaFragment();
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
    }

    private void filtro(String texto){
        List<ItemManifiestoSede> result = new ArrayList<>();
        List<ItemManifiestoSede> listaItems = new ArrayList<>() ;
        ParametroEntity parametro = MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_vehiculo");
        String valor = parametro == null ? "-1" : parametro.getValor();
        Integer idVehiculo = Integer.parseInt(valor.equals("null") ? "-1":valor);
        listaItems =  MyApp.getDBO().manifiestoPlantaDao().fetchManifiestosAsigByClienteOrNumManifPlanta(texto,idVehiculo);
        rowItems=listaItems;
        recyclerviewAdapter.setTaskList(rowItems);
    }


    private void initItems() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        ParametroEntity parametro = MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_vehiculo");
        String valor = parametro == null ? "-1" : parametro.getValor();
        Integer idVehiculo = Integer.parseInt(valor.equals("null") ? "-1":valor);
        rowItems = MyApp.getDBO().manifiestoPlantaDao().fetchManifiestosAsigByClienteOrNumManif(idVehiculo);
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
                        setNavegate(ManifiestoFragmentTabs.newInstance(rowItems.get(position).getIdAppManifiesto(), rowItems.get(position).getNumeroManifiesto()));
                        break;
                    case R.id.btn_manifiesto_more:
                        break;
                }
            }
        });
    }

    @Override
    public void reciveData(String data) {

        Boolean estadoBulto = MyApp.getDBO().manifiestoPlantaDetalleValorDao().verificarBultoEstado(data);

        if(estadoBulto==null){
            messageBox("CODIGO QR NO EXISTE..!");
        }else{
            if (estadoBulto){
                messageBox("EL BULTO YA SE ENCUENTRA REGISTRADO..!");
            }else if (!estadoBulto){
                dialogCodigoQR = new DialogInfoCodigoQR(getActivity(),data);
                dialogCodigoQR.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogCodigoQR.setCancelable(false);
                dialogCodigoQR.setmOnclickSedeListener(new DialogBultosPlanta.onclickSedeListener() {
                    @Override
                    public void onSucefull() {
                        rowItems = MyApp.getDBO().manifiestoPlantaDao().fetchManifiestosAsigByClienteOrNumManifCodigoQR();
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
                setNavegate(HomePlantaFragment.create());
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
    public static HojaRutaAsignadaPlantaFragment create(){
        return new HojaRutaAsignadaPlantaFragment();
    }
}
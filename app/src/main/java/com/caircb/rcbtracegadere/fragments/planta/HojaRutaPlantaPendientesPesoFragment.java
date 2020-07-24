package com.caircb.rcbtracegadere.fragments.planta;

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
import com.caircb.rcbtracegadere.adapters.ManifiestoAdapter;
import com.caircb.rcbtracegadere.adapters.ManifiestoAdapterSede;
import com.caircb.rcbtracegadere.components.SearchView;
import com.caircb.rcbtracegadere.database.entity.ParametroEntity;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnBarcodeListener;
import com.caircb.rcbtracegadere.generics.OnRecyclerTouchListener;
import com.caircb.rcbtracegadere.models.ItemManifiestoSede;

import java.util.ArrayList;
import java.util.List;

public class HojaRutaPlantaPendientesPesoFragment extends MyFragment implements View.OnClickListener, OnBarcodeListener {

    LinearLayout btnRetornarListHojaRuta;
    private Window window;
    private RecyclerView recyclerView;
    private ManifiestoAdapterSede recyclerviewAdapter;
    private SearchView searchView;
    private List<ItemManifiestoSede> rowItems;
    private OnRecyclerTouchListener touchListener;

    public static HojaRutaPlantaPendientesPesoFragment newInstance() {
        return new HojaRutaPlantaPendientesPesoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setView(inflater.inflate(R.layout.fragment_hoja_ruta_procesada, container, false));
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
        List<ItemManifiestoSede> listaItems = new ArrayList<>() ;
        List<ItemManifiestoSede> rowItems = new ArrayList<>();
        //listaItems =  MyApp.getDBO().manifiestoPlantaDao().fetchManifiestosAsigByClienteOrNumManifPlanta(texto,idVehiculo);
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
                        //setNavegate(ManifiestoFragmentTabs.newInstance(rowItems.get(position).getIdAppManifiesto(), rowItems.get(position).getNumeroManifiesto()));
                        break;
                    case R.id.btn_manifiesto_more:
                        break;
                }
            }
        });
    }

    @Override
    public void reciveData(String data) {

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
    }
}

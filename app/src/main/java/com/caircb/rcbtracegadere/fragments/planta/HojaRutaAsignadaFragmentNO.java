package com.caircb.rcbtracegadere.fragments.planta;

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
import com.caircb.rcbtracegadere.components.SearchView;
import com.caircb.rcbtracegadere.database.AppDatabase;
import com.caircb.rcbtracegadere.database.entity.ManifiestoEntity;
import com.caircb.rcbtracegadere.database.entity.RutaInicioFinEntity;
import com.caircb.rcbtracegadere.database.entity.RuteoRecoleccionEntity;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.fragments.recolector.HomeTransportistaFragment;
import com.caircb.rcbtracegadere.fragments.recolector.MotivoNoRecoleccion.ManifiestoNoRecoleccionFragment;
import com.caircb.rcbtracegadere.fragments.recolector.manifiesto2.Manifiesto2Fragment;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnRecyclerTouchListener;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.ItemManifiesto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HojaRutaAsignadaFragmentNO#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HojaRutaAsignadaFragmentNO extends MyFragment implements View.OnClickListener {


    LinearLayout btnRetornarListHojaRuta;

    private Window window;
    private RecyclerView recyclerView;
    private ManifiestoAdapter recyclerviewAdapter;

    private OnRecyclerTouchListener touchListener;
    private List<ItemManifiesto> rowItems;
    private SearchView searchView;
    private DialogMenuBaseAdapter dialogMenuBaseAdapter;
    private ListView mDrawerMenuItems, mDialogMenuItems;
    DialogBuilder dialogBuilder;
    RutaInicioFinEntity rut;
    ManifiestoEntity entity;
    Integer idFinRuta;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment HojaRutaAsignadaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HojaRutaAsignadaFragmentNO newInstance() {
        return new HojaRutaAsignadaFragmentNO();
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
        recyclerviewAdapter = new ManifiestoAdapter(getActivity());
        btnRetornarListHojaRuta = getView().findViewById(R.id.btnRetornarListHojaRuta);
        btnRetornarListHojaRuta.setOnClickListener(this);
        searchView = getView().findViewById(R.id.searchViewManifiestos);

        searchView.setOnSearchListener(new SearchView.OnSearchListener() {
            @Override
            public void onSearch(String data) {
                filtro(data);
            }
        });
        Integer idVehiculo = Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_vehiculo").getValor());
        idFinRuta = Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_destino_especifico").getValor());
        rut = MyApp.getDBO().rutaInicioFinDao().fechConsultaInicioFinRutasE(MySession.getIdUsuario());
        entity = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdTransporte(idVehiculo);

    }

    private void filtro(String texto){
        List<ItemManifiesto> result = new ArrayList<>();
        List<ItemManifiesto> listaItems = new ArrayList<>() ;
        listaItems =  MyApp.getDBO().manifiestoDao().fetchManifiestosAsigByClienteOrNumManif(texto,entity.getIdSubRuta(),MySession.getIdUsuario());
        rowItems=listaItems;
        recyclerviewAdapter.setTaskList(rowItems);
    }


    private void initItems() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        rowItems = MyApp.getDBO().manifiestoDao().fetchManifiestosAsigandoPlanta(entity.getIdSubRuta(),idFinRuta);
        adapterList();

    }

    private void adapterList(){
        recyclerviewAdapter.setTaskList(rowItems);
        recyclerView.setAdapter(recyclerviewAdapter);

        touchListener = new OnRecyclerTouchListener(getActivity(),recyclerView);
        touchListener.setClickable(new OnRecyclerTouchListener.OnRowClickListener() {
            @Override
            public void onRowClicked(int position) {
                Toast.makeText(getActivity(),rowItems.get(position).getNumero(), Toast.LENGTH_SHORT).show();
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
                        setNavegate(ManifiestoPlantaFragment.newInstance(rowItems.get(position).getIdAppManifiesto()));

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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRetornarListHojaRuta:
              /*  List<RuteoRecoleccionEntity> enty = MyApp.getDBO().ruteoRecoleccion().searchRuteoRecoleccion(); //////////
                Boolean estado = MyApp.getDBO().ruteoRecoleccion().verificaEstadoPrimerRegistro(0);
                if(!estado){
                    Integer _id = MyApp.getDBO().ruteoRecoleccion().selectIdByPuntoPartida(0);
                    if(_id!=null){
                        MyApp.getDBO().ruteoRecoleccion().updatePrimerRegistroRuteoRecoleccion(_id, null, null);
                    }
                }
                Integer idMayor = MyApp.getDBO().ruteoRecoleccion().searchRegistroPuntodePartidaMayorACero();
                MyApp.getDBO().ruteoRecoleccion().updatePrimerRegistroRuteoRecoleccion(idMayor, null, null);

                List<RuteoRecoleccionEntity> enty2 = MyApp.getDBO().ruteoRecoleccion().searchRuteoRecoleccion(); //////////*/
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
}
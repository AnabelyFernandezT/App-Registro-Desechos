package com.caircb.rcbtracegadere.fragments.Sede;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.adapters.DialogMenuBaseAdapter;
import com.caircb.rcbtracegadere.adapters.LoteAdapter;
import com.caircb.rcbtracegadere.components.SearchView;

import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnRecyclerTouchListener;
import com.caircb.rcbtracegadere.models.ItemLote;
import com.caircb.rcbtracegadere.tasks.UserConsultaLotes;
import com.caircb.rcbtracegadere.dialogs.DialogInicioMovilizacion;
import com.caircb.rcbtracegadere.tasks.UserReAbrirLoteTask;


import java.util.ArrayList;
import java.util.List;

public class HojaFragment extends MyFragment implements View.OnClickListener{

    LinearLayout btnRetornarListHojaLotes;

    UserConsultaLotes consultarLotes;
    private Window window;
    private RecyclerView recyclerView;
    private LoteAdapter recyclerviewAdapter;
    DialogInicioMovilizacion movilizacion;

    private OnRecyclerTouchListener touchListener;
    private List<ItemLote> rowItems;
    private SearchView searchView;
    private DialogMenuBaseAdapter dialogMenuBaseAdapter;
    private ListView mDrawerMenuItems, mDialogMenuItems;

    public static HojaFragment newInstance() {
        return new HojaFragment();

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        //datosLotesDisponibles();
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setView(inflater.inflate(R.layout.fragment_lotes, container, false));
        setHideHeader();
        init();
        initItems();
        return getView();
    }

    private void init(){

        recyclerView = getView().findViewById(R.id.recyclerviewNotificaciones);
        recyclerviewAdapter = new LoteAdapter(getActivity());
        btnRetornarListHojaLotes = getView().findViewById(R.id.btnRetornarInicio);
        btnRetornarListHojaLotes.setOnClickListener(this);
        searchView = getView().findViewById(R.id.searchViewLotes);
        searchView.setOnSearchListener(new SearchView.OnSearchListener() {
            @Override
            public void onSearch(String data) {
                filtro(data);
            }
        });
    }
    private void filtro(String texto){
        List<ItemLote> listaItems = new ArrayList<>() ;
        listaItems =  MyApp.getDBO().loteDao().fetchLoteSearchView(texto);
        rowItems=listaItems;
        recyclerviewAdapter.setTaskList(rowItems);
    }


    private void initItems() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        rowItems = MyApp.getDBO().loteDao().fetchLote();
        recyclerviewAdapter.setTaskList(rowItems);
        recyclerView.setAdapter(recyclerviewAdapter);

        touchListener = new OnRecyclerTouchListener(getActivity(),recyclerView);
        touchListener.setClickable(new OnRecyclerTouchListener.OnRowClickListener() {
            @Override
            public void onRowClicked(int position) {
                Toast.makeText(getActivity(),String.valueOf(rowItems.get(position).getIdLoteContenedor()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onIndependentViewClicked(int independentViewID, int position) {

            }
        }).setSwipeOptionViews(R.id.btn_lote_view, R.id.btn_lote_openAgain).setSwipeable(R.id.rowFGL, R.id.rowBGL, new OnRecyclerTouchListener.OnSwipeOptionsClickListener() {
            @Override
            public void onSwipeOptionClicked(int viewID, final int position) {
                switch (viewID){
                    case R.id.btn_lote_view:
                        //setNavegate(ManifiestoFragment.newInstance(rowItems.get(position).getIdAppManifiesto(),false));
                        //Toast.makeText(getActivity(),rowItems.get(position).getIdLoteContenedor(), Toast.LENGTH_SHORT).show();
                        menu(position);
                        break;
                    case R.id.btn_lote_openAgain:
                        reAbrirLote(position);
                        break;
                }
            }
        });
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getActivity().getBaseContext(), R.drawable.shape_divider));
        recyclerView.addItemDecoration(divider);
    }

    private void  menu(final int position){

        movilizacion = new DialogInicioMovilizacion(getActivity(),rowItems.get(position).getIdLoteContenedor());
        movilizacion.setOnRegisterMovilizarListener(new DialogInicioMovilizacion.onRegisterMOvilizacionListenner() {
            @Override
            public void onSuccessful() {
                rowItems = MyApp.getDBO().loteDao().fetchLote();
                recyclerviewAdapter.setTaskList(rowItems);
            }
        });
        movilizacion.requestWindowFeature(Window.FEATURE_NO_TITLE);
        movilizacion.setCancelable(false);
        movilizacion.show();
        /*
        final DialogBuilder dialogBuilder = new DialogBuilder(getActivity());
        dialogBuilder.setCancelable(false);
        dialogBuilder.setMessage("¿Esta seguro de movilizar el lote?");
        dialogBuilder.setPositiveButton("SI", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();

            }
        });
        dialogBuilder.setNegativeButton("NO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        });
        dialogBuilder.show();*/
    }

    /*private void datosLotesDisponibles(){

        consultarLotes = new UserConsultaLotes(getActivity());
        consultarLotes.execute();
    }
*/

    private void reAbrirLote(final Integer position){
        final DialogBuilder dialogBuilder = new DialogBuilder(getActivity());
        dialogBuilder.setCancelable(false);
        dialogBuilder.setMessage("¿Esta seguro de volver a abrir el lote?");
        dialogBuilder.setPositiveButton("SI", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
                UserReAbrirLoteTask reAbrirLoteTask = new UserReAbrirLoteTask(getActivity(),rowItems.get(position).getIdLoteContenedor());
                reAbrirLoteTask.setOnRegisterListener(new UserReAbrirLoteTask.OnRegisterListener() {
                    @Override
                    public void onSuccessful() {
                        setNavegate(HomeSedeFragment.create());
                    }

                    @Override
                    public void onFail() {

                    }
                });
                reAbrirLoteTask.execute();

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRetornarInicio:
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

package com.caircb.rcbtracegadere.fragments.GestorAlterno;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.caircb.rcbtracegadere.adapters.ManifiestoGestorAdapter;
import com.caircb.rcbtracegadere.components.SearchView;
import com.caircb.rcbtracegadere.database.AppDatabase;
import com.caircb.rcbtracegadere.database.entity.ManifiestoEntity;
import com.caircb.rcbtracegadere.database.entity.PaqueteEntity;
import com.caircb.rcbtracegadere.database.entity.RuteoRecoleccionEntity;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.fragments.GestorAlterno.ManifiestoG.ManifiestoGestoresFragment;
import com.caircb.rcbtracegadere.fragments.planta.ManifiestoPlantaFragment;
import com.caircb.rcbtracegadere.fragments.recolector.HomeTransportistaFragment;
import com.caircb.rcbtracegadere.fragments.recolector.MotivoNoRecoleccion.ManifiestoNoRecoleccionFragment;
import com.caircb.rcbtracegadere.fragments.recolector.manifiesto2.Manifiesto2Fragment;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnCameraListener;
import com.caircb.rcbtracegadere.generics.OnRecyclerTouchListener;
import com.caircb.rcbtracegadere.helpers.MyConstant;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.DtoRuteoRecoleccion;
import com.caircb.rcbtracegadere.models.ItemLotePadre;
import com.caircb.rcbtracegadere.models.ItemManifiesto;
import com.caircb.rcbtracegadere.tasks.UserRegistrarActualizarEstadoTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HojaRutaAsignadaGestorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HojaRutaAsignadaGestorFragment extends MyFragment implements View.OnClickListener {


    LinearLayout btnRetornarListHojaRuta;
    private RecyclerView recyclerView;
    private ManifiestoGestorAdapter recyclerviewAdapter;

    private OnRecyclerTouchListener touchListener;
    private List<ItemManifiesto> rowItems;
    private SearchView searchView;
    Integer idSubRuta;
    DialogBuilder dialogBuilder, dialogBuilder2;
    UserRegistrarActualizarEstadoTask actualizarEstadoTask;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment HojaRutaAsignadaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HojaRutaAsignadaGestorFragment newInstance() {
        return new HojaRutaAsignadaGestorFragment();
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
        recyclerviewAdapter = new ManifiestoGestorAdapter(getActivity());
        btnRetornarListHojaRuta = getView().findViewById(R.id.btnRetornarListHojaRuta);
        btnRetornarListHojaRuta.setOnClickListener(this);
        searchView = getView().findViewById(R.id.searchViewManifiestos);
        idSubRuta = Integer.parseInt(MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_ruta").getValor());
        searchView.setOnSearchListener(new SearchView.OnSearchListener() {
            @Override
            public void onSearch(String data) {
                filtro(data);
            }
        });
    }

    private void filtro(String texto){
        List<ItemManifiesto> result = new ArrayList<>();
        List<ItemManifiesto> listaItems = new ArrayList<>() ;
        //listaItems =  MyApp.getDBO().manifiestoDao().fetchManifiestosAsigByClienteOrNumManif(texto);
        //rowItems=listaItems;
        //recyclerviewAdapter.setTaskList(rowItems);
    }


    private void initItems() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        rowItems = MyApp.getDBO().manifiestoDao().fetchManifiestoGestor(idSubRuta, MySession.getIdUsuario(), MyConstant.ID_ENTREGA_GESTOR);
        adapterList();

    }

    private void adapterList(){
        recyclerviewAdapter.setTaskList(rowItems);
        recyclerView.setAdapter(recyclerviewAdapter);

        touchListener = new OnRecyclerTouchListener(getActivity(),recyclerView);
        touchListener.setClickable(new OnRecyclerTouchListener.OnRowClickListener() {
            @Override
            public void onRowClicked(int position) {
                //Toast.makeText(getActivity(),rowItems.get(position).getNumeroManifiestoPadre(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onIndependentViewClicked(int independentViewID, int position) {

            }
        }).setSwipeOptionViews(R.id.btn_manifiesto_view, R.id.btn_manifiesto_more).setSwipeable(R.id.rowFG, R.id.rowBG, new OnRecyclerTouchListener.OnSwipeOptionsClickListener() {
            @Override
            public void onSwipeOptionClicked(int viewID, final int position) {
                switch (viewID){
                    case R.id.btn_manifiesto_view:
                        menu(position);
                        break;
                    case R.id.btn_manifiesto_more:
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRetornarListHojaRuta:
                setNavegate(HomeGestorAlternoFragment.create());
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

    private void menu(final int position) {
        final CharSequence[] options = {"INICIAR RECOLECCIÓN", "NO RECOLECTAR", "CANCELAR"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(final DialogInterface dialog, int item) {
                if (options[item].equals("INICIAR RECOLECCIÓN")) {
                        dialogBuilder = new DialogBuilder(getActivity());
                        dialogBuilder.setMessage("¿Está seguro que desea INICIAR RECOLECCIÓN?");
                        dialogBuilder.setCancelable(false);
                        dialogBuilder.setTitle("CONFIRMACIÓN");
                        dialogBuilder.setPositiveButton("SI", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogBuilder.dismiss();
                                setNavegate(ManifiestoGestoresFragment.newInstance(rowItems.get(position).getIdAppManifiesto(),1,1));
                                actualizarEstadoTask = new UserRegistrarActualizarEstadoTask(getActivity(),rowItems.get(position).getIdAppManifiesto(),0);
                                actualizarEstadoTask.execute();
                            }
                        });
                        dialogBuilder.setNegativeButton("NO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogBuilder.dismiss();
                            }
                        });
                        dialogBuilder.show();

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

                            actualizarEstadoTask = new UserRegistrarActualizarEstadoTask(getActivity(),rowItems.get(position).getIdAppManifiesto(),0);
                            actualizarEstadoTask.execute();
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

                } else if (options[item].equals("CANCELAR")) {
                    MyApp.getDBO().parametroDao().saveOrUpdate("seleccionMenuRecoleccion",""); // Cancelar
                    dialog.dismiss();
                }

            }
        });
        builder.show();
    }

}
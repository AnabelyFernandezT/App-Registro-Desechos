package com.caircb.rcbtracegadere.fragments.recolector;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
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
import com.caircb.rcbtracegadere.fragments.recolector.MotivoNoRecoleccion.ManifiestoNoRecoleccionFragment;
import com.caircb.rcbtracegadere.fragments.recolector.manifiesto2.Manifiesto2Fragment;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnRecyclerTouchListener;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.DtoRuteoRecoleccion;
import com.caircb.rcbtracegadere.models.ItemManifiesto;
import com.caircb.rcbtracegadere.models.MenuItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HojaRutaAsignadaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HojaRutaAsignadaFragment extends MyFragment implements View.OnClickListener {


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
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment HojaRutaAsignadaFragment.
     */
    // TODO: Rename and change types and number of parameters
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

        rut = MyApp.getDBO().rutaInicioFinDao().fechConsultaInicioFinRutasE(MySession.getIdUsuario());
    }

    private void filtro(String texto){
        List<ItemManifiesto> result = new ArrayList<>();
        List<ItemManifiesto> listaItems = new ArrayList<>() ;
        listaItems =  MyApp.getDBO().manifiestoDao().fetchManifiestosAsigByClienteOrNumManif(texto,rut.getIdSubRuta(),MySession.getIdUsuario());
        rowItems=listaItems;
        recyclerviewAdapter.setTaskList(rowItems);
    }


    private void initItems() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        rowItems = MyApp.getDBO().manifiestoDao().fetchManifiestosAsigandobySubRuta(rut.getIdSubRuta(),MySession.getIdUsuario());
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
                        //setNavegate(Manifiesto2Fragment.newInstance(rowItems.get(position).getIdAppManifiesto()));
                        menu(position);
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

    private void  menu(final int position){
        final CharSequence[] options = {"INICIAR RECOLECCION", "INGRESAR MOTIVO NO RECOLECCION", "CANCELAR"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("INICIAR RECOLECCION"))
                {
                    dialogBuilder = new DialogBuilder(getActivity());
                    dialogBuilder.setMessage("¿Seguro que desea INICIAR RECOLECCIÓN ?");
                    dialogBuilder.setCancelable(false);
                    dialogBuilder.setTitle("CONFIRMACIÓN");
                    dialogBuilder.setPositiveButton("SI", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismiss();
                            Date fecha = AppDatabase.getDateTime();
                            //ManifiestoEntity man = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(rowItems.get(position).getIdAppManifiesto());
                            MyApp.getDBO().manifiestoDao().saveOrUpdateFechaInicioRecoleccion(rowItems.get(position).getIdAppManifiesto(),fecha);

                            //List<RuteoRecoleccionEntity> enty = MyApp.getDBO().ruteoRecoleccion().searchRuteoRecoleccion();

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
                            List<RuteoRecoleccionEntity> enty2 = MyApp.getDBO().ruteoRecoleccion().searchRuteoRecoleccion();

                            //ManifiestoEntity man1 = MyApp.getDBO().manifiestoDao().fetchHojaRutabyIdManifiesto(rowItems.get(position).getIdAppManifiesto());
                            setNavegate(Manifiesto2Fragment.newInstance(rowItems.get(position).getIdAppManifiesto(),1));
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
                else if (options[item].equals("INGRESAR MOTIVO NO RECOLECCION"))
                {
                    AlertDialog.Builder builderConfgirmaRecol= new AlertDialog.Builder(getActivity());
                    builderConfgirmaRecol.setMessage("¿Seguro que desea INICIAR NO RECOLECCIÓN ?");
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

                }
                else if (options[item].equals("CANCELAR")) {
                    dialog.dismiss();
                }

            }
        });
        builder.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRetornarListHojaRuta:
                List<RuteoRecoleccionEntity> enty = MyApp.getDBO().ruteoRecoleccion().searchRuteoRecoleccion(); //////////
                Boolean estado = MyApp.getDBO().ruteoRecoleccion().verificaEstadoPrimerRegistro(0);
                if(!estado){
                    Integer _id = MyApp.getDBO().ruteoRecoleccion().selectIdByPuntoPartida(0);
                    if(_id!=null){
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
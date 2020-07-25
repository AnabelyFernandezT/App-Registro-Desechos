package com.caircb.rcbtracegadere.fragments.recolector;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.caircb.rcbtracegadere.database.entity.RutaInicioFinEntity;
import com.caircb.rcbtracegadere.database.entity.RuteoRecoleccionEntity;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.fragments.recolector.MotivoNoRecoleccion.ManifiestoNoRecoleccionFragment;
import com.caircb.rcbtracegadere.fragments.recolector.manifiesto.ManifiestoFragment;
import com.caircb.rcbtracegadere.fragments.recolector.manifiesto2.Manifiesto2Fragment;
import com.caircb.rcbtracegadere.fragments.recolector.manifiesto2.Manifiesto2FragmentProcesada;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnRecyclerTouchListener;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.ItemManifiesto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HojaRutaBuscarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HojaRutaBuscarFragment extends MyFragment implements View.OnClickListener {


    LinearLayout btnRetornarListHojaRuta;

    private Window window;
    private RecyclerView recyclerView;
    private ManifiestoAdapter recyclerviewAdapter;

    private OnRecyclerTouchListener touchListener;
    private List<ItemManifiesto> rowItems;
    private SearchView searchView;
    private DialogMenuBaseAdapter dialogMenuBaseAdapter;
    private ListView mDrawerMenuItems, mDialogMenuItems;
    private ImageButton btnBuscarManifiesto;
    private EditText txtManifiesto;
    RutaInicioFinEntity rut;
    DialogBuilder dialogBuilder2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment HojaRutaAsignadaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HojaRutaBuscarFragment newInstance() {
        return new HojaRutaBuscarFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setView(inflater.inflate(R.layout.fragment_hoja_ruta_buscar, container, false));
        setHideHeader();
        init();
        initItems();
        return getView();
    }

    private void init(){
        recyclerView = getView().findViewById(R.id.recyclerview);
        recyclerviewAdapter = new ManifiestoAdapter(getActivity(),1);
        btnRetornarListHojaRuta = getView().findViewById(R.id.btnRetornarListHojaRuta);
        btnRetornarListHojaRuta.setOnClickListener(this);
        txtManifiesto = getView().findViewById(R.id.txtManifiesto);
        btnBuscarManifiesto = getView().findViewById(R.id.btnBuscarManifiesto);
        btnBuscarManifiesto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtro(txtManifiesto.getText().toString());
            }
        });

        /*searchView = getView().findViewById(R.id.searchViewManifiestos);

        searchView.setOnSearchListener(new SearchView.OnSearchListener() {
            @Override
            public void onSearch(String data) {
                filtro(data);
            }
        });*/
        rut = MyApp.getDBO().rutaInicioFinDao().fechConsultaInicioFinRutasE(MySession.getIdUsuario());
    }

    private void filtro(String texto){
        List<ItemManifiesto> result = new ArrayList<>();
        List<ItemManifiesto> listaItems = new ArrayList<>() ;
        listaItems =  MyApp.getDBO().manifiestoDao().fetchManifiestosAsigByClienteOrNumManifSearch(texto,rut.getIdSubRuta(),MySession.getIdUsuario());
        rowItems=listaItems;
        recyclerviewAdapter.setTaskList(rowItems);
    }


    private void initItems() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        rowItems = MyApp.getDBO().manifiestoDao().fetchManifiestosBuscarDataByRuta(rut.getIdSubRuta(),MySession.getIdUsuario());
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
        }).setSwipeOptionViews(R.id.btn_manifiesto_view/*, R.id.btn_manifiesto_more*/).setSwipeable(R.id.rowFG, R.id.rowBG, new OnRecyclerTouchListener.OnSwipeOptionsClickListener() {
            @Override
            public void onSwipeOptionClicked(int viewID, final int position) {
                switch (viewID){
                    case R.id.btn_manifiesto_view:
                        //setNavegate(ManifiestoFragment.newInstance(rowItems.get(position).getIdAppManifiesto(),false));
                        //setNavegate(Manifiesto2Fragment.newInstance(rowItems.get(position).getIdAppManifiesto()));
                        switch (rowItems.get(position).getEstado()){
                            case 1:
                                menu(position);
                                break;
                            case 2:
                                setNavegate(Manifiesto2FragmentProcesada.newInstance(rowItems.get(position).getIdAppManifiesto(),rowItems.get(position).getEstado(),2));
                                break;
                            case 3:
                                setNavegate(ManifiestoNoRecoleccionFragment.newInstance(rowItems.get(position).getIdAppManifiesto(),2));
                                break;
                        }
                        break;
                   /* case R.id.btn_manifiesto_more:
                        break;*/
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
                    dialogBuilder2 = new DialogBuilder(getActivity());
                    dialogBuilder2.setMessage("¿Va a realizar el pesaje en sitio?");
                    dialogBuilder2.setCancelable(false);
                    dialogBuilder2.setTitle("CONFIRMACIÓN");
                    dialogBuilder2.setPositiveButton("SI", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder2.dismiss();
                            setNavegate(Manifiesto2Fragment.newInstance(rowItems.get(position).getIdAppManifiesto(),2,1));
                        }
                    });
                    dialogBuilder2.setNegativeButton("NO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder2.dismiss();
                            setNavegate(Manifiesto2Fragment.newInstance(rowItems.get(position).getIdAppManifiesto(),2,2));
                        }
                    });
                    dialogBuilder2.show();
                }
                else if (options[item].equals("INGRESAR MOTIVO NO RECOLECCION"))
                {
                    setNavegate(ManifiestoNoRecoleccionFragment.newInstance(rowItems.get(position).getIdAppManifiesto(),2));
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
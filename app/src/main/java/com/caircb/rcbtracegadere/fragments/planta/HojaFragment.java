package com.caircb.rcbtracegadere.fragments.planta;

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
import com.caircb.rcbtracegadere.adapters.LoteAdapter;
import com.caircb.rcbtracegadere.adapters.ManifiestoAdapter;
import com.caircb.rcbtracegadere.components.SearchView;
import com.caircb.rcbtracegadere.fragments.recolector.HojaRutaAsignadaFragment;
import com.caircb.rcbtracegadere.fragments.recolector.HomeTransportistaFragment;
import com.caircb.rcbtracegadere.fragments.recolector.MotivoNoRecoleccion.ManifiestoNoRecoleccionFragment;
import com.caircb.rcbtracegadere.fragments.recolector.manifiesto2.Manifiesto2Fragment;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnRecyclerTouchListener;
import com.caircb.rcbtracegadere.models.ItemLote;
import com.caircb.rcbtracegadere.models.ItemManifiesto;
import com.caircb.rcbtracegadere.models.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class HojaFragment extends MyFragment implements View.OnClickListener{

    LinearLayout btnRetornarListHojaLotes;

    private Window window;
    private RecyclerView recyclerView;
    private ManifiestoAdapter recyclerviewAdapter;

    private OnRecyclerTouchListener touchListener;
    private List<ItemLote> rowItems;
    private SearchView searchView;
    private DialogMenuBaseAdapter dialogMenuBaseAdapter;
    private ListView mDrawerMenuItems, mDialogMenuItems;

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
        setView(inflater.inflate(R.layout.fragment_lotes, container, false));
        setHideHeader();
        init();
        return getView();
    }

    private void init(){
        recyclerView = getView().findViewById(R.id.recyclerview);
        recyclerviewAdapter = new ManifiestoAdapter(getActivity());
        btnRetornarListHojaLotes = getView().findViewById(R.id.btnRetornarListHojaLotes);
        btnRetornarListHojaLotes.setOnClickListener(this);
        searchView = getView().findViewById(R.id.searchViewLotes);

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

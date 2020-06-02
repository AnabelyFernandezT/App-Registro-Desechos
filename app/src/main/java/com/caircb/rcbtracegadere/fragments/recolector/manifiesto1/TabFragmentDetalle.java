package com.caircb.rcbtracegadere.fragments.recolector.manifiesto1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.adapters.ManifiestoAdapter;
import com.caircb.rcbtracegadere.adapters.ManifiestoDetalleAdapter;
import com.caircb.rcbtracegadere.models.RowItemManifiesto;

import java.util.List;

public class TabFragmentDetalle extends Fragment {

    private static final String ARG_PARAM1 = "manifiestoID";
    private static final String ARG_PARAM2 = "tipoPaquete";

    private Integer idAppManifiesto,tipoPaquete;
    private String mParam2;
    private List<RowItemManifiesto> detalles;
    private RecyclerView recyclerView;
    ManifiestoDetalleAdapter recyclerviewAdapter;
    View view;

    public TabFragmentDetalle() {
        // Required empty public constructor
    }

    public static TabFragmentDetalle newInstance(Integer manifiestoID, Integer tipoPaquete) {
        TabFragmentDetalle fragment = new TabFragmentDetalle();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, manifiestoID);
        args.putInt(ARG_PARAM2, tipoPaquete);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idAppManifiesto = getArguments().getInt(ARG_PARAM1);
            tipoPaquete = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_tab_detalle, container, false);
        init();
        loadData();
        return view;
    }

    private void init(){
        recyclerView = view.findViewById(R.id.recyclerManifiestoDetalle);
        recyclerviewAdapter = new ManifiestoDetalleAdapter(getActivity());
    }

    private void loadData(){

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        detalles = MyApp.getDBO().manifiestoDetalleDao().fetchHojaRutaDetallebyIdManifiesto(idAppManifiesto);
        recyclerviewAdapter.setTaskList(detalles);
        recyclerView.setAdapter(recyclerviewAdapter);

        recyclerviewAdapter.setOnItemClickListener(new ManifiestoDetalleAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
            int x=0;
            }
        });

    }
}
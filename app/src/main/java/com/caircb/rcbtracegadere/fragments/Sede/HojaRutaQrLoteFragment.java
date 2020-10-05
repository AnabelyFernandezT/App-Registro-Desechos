package com.caircb.rcbtracegadere.fragments.Sede;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.adapters.PesosAdapterPlantaLote;
import com.caircb.rcbtracegadere.database.entity.RecepcionQrPlantaEntity;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.dialogs.DialogListadoManifiestosPlantaQr;
import com.caircb.rcbtracegadere.fragments.planta.HomePlantaFragment;
import com.caircb.rcbtracegadere.fragments.planta.RecepcionLotePlantaFragment;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnRecyclerTouchListener;
import com.caircb.rcbtracegadere.models.ItemQrDetallePlanta;
import com.caircb.rcbtracegadere.tasks.UserRegistrarFinLoteHospitalesTask;
import com.caircb.rcbtracegadere.tasks.UserRegistrarSedeQrTask;

import java.util.List;

public class HojaRutaQrLoteFragment extends MyFragment {

    private FragmentActivity myContext;
    Window window;
    private RecyclerView recyclerView;
    private PesosAdapterPlantaLote recyclerviewAdapter;
    TextView txtPesoTotalLote, txtCantidadTotalBultosLote, txtTotalNumeroManifiestosLote;
    LinearLayout btnRegistrarSede,btnListadoManifiestosSedeQr;
    private List<ItemQrDetallePlanta> rowItems;
    private RecepcionQrPlantaEntity recepcionQrPlantaEntity;
    UserRegistrarSedeQrTask userRegistrarSedeQrTask;
    UserRegistrarFinLoteHospitalesTask userRegistrarFinLoteHospitales;

    public static HojaRutaQrLoteFragment newInstance() {
        HojaRutaQrLoteFragment fragment = new HojaRutaQrLoteFragment();
       /* Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, manifiestoID);
        args.putString(ARG_PARAM3, numeroManifiesto);
        args.putString(ARG_PARAM4, pesajePendiente);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            manifiestoID= getArguments().getInt(ARG_PARAM1);
            numeroManifiesto = getArguments().getString(ARG_PARAM3);
            pesajePendiente = getArguments().getString(ARG_PARAM4);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setView(inflater.inflate(R.layout.hoja_ruta_qr_lote_fragment, container, false));
        init();
        initItems();
        cargarDatos();
        return getView();
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }
    private void init() {
        recepcionQrPlantaEntity= MyApp.getDBO().recepcionQrPlantaDao().fetchHojaRutaQrPlanta();
        recyclerView = getView().findViewById(R.id.recyclerview);
        txtPesoTotalLote = (TextView) getView().findViewById(R.id.txtPesoTotalLote);
        txtCantidadTotalBultosLote = (TextView) getView().findViewById(R.id.txtCantidadTotalBultosLote);
        txtTotalNumeroManifiestosLote = (TextView) getView().findViewById(R.id.txtTotalNumeroManifiestosLote);
        btnRegistrarSede = (LinearLayout) getView().findViewById(R.id.btnManifiestoNext);
        btnRegistrarSede.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DialogBuilder dialogBuilder = new DialogBuilder(myContext);
                dialogBuilder.setMessage("¿Está seguro de continuar?");
                dialogBuilder.setCancelable(false);
                dialogBuilder.setPositiveButton("SI",new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                        userRegistrarSedeQrTask = new UserRegistrarSedeQrTask(myContext,recepcionQrPlantaEntity);
                        userRegistrarSedeQrTask.setOnRegisterListener(new UserRegistrarSedeQrTask.OnRegisterListener() {
                            @Override
                            public void onSuccessful() {
                                String idSubRuta = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_idSubruta") == null ? "0" : MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_idSubruta");
                                if (!idSubRuta.equals("0")) {
                                    int idSubRutaEnviar = Integer.parseInt(idSubRuta);
                                    Integer idTransportistaRecolector = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_idTransportistaRecolector")==null?0:Integer.parseInt(MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_idTransportistaRecolector"));
                                    Integer idLoteCerrado = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_idLoteCerrado")==null?0:Integer.parseInt(MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_idLoteCerrado"));
                                    userRegistrarFinLoteHospitales = new UserRegistrarFinLoteHospitalesTask(myContext, idSubRutaEnviar, 0, 4,idTransportistaRecolector,idLoteCerrado);
                                    userRegistrarFinLoteHospitales.setOnFinLoteListener(new UserRegistrarFinLoteHospitalesTask.OnFinLoteListener() {
                                        @Override
                                        public void onSuccessful() {
                                            String idVehiculo=MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_vehiculo")== null ? "" :MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_vehiculo");
                                            MyApp.getDBO().parametroDao().saveOrUpdate("vehiculo_planta"+idVehiculo,""+0);
                                            setNavegate(HomePlantaFragment.create());
                                        }

                                        @Override
                                        public void onFailure() {

                                        }
                                    });
                                    userRegistrarFinLoteHospitales.execute();
                                }
                            }
                        });
                        userRegistrarSedeQrTask.execute();
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
        });
        btnListadoManifiestosSedeQr = (LinearLayout) getView().findViewById(R.id.btnListadoManifiestosSedeQr);
        btnListadoManifiestosSedeQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogListadoManifiestosPlantaQr dialogListadoManifiestosPlantaQr = new DialogListadoManifiestosPlantaQr(getActivity(),recepcionQrPlantaEntity);
                dialogListadoManifiestosPlantaQr.setTitle("LISTADO DE MANIFIESTOS");
                dialogListadoManifiestosPlantaQr.setCanceledOnTouchOutside(false);
                dialogListadoManifiestosPlantaQr.setPositiveButton("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogListadoManifiestosPlantaQr.dismiss();
                    }
                });
                dialogListadoManifiestosPlantaQr.show();
            }
        });
    }
    private void initItems() {
        rowItems = MyApp.getDBO().recepcionQrPlantaDetalleDao().fetchHojaRutaQrPlantaDetalle2();
        recyclerView.setLayoutManager(new LinearLayoutManager(myContext));
        recyclerviewAdapter = new PesosAdapterPlantaLote(myContext,rowItems);
        recyclerviewAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(recyclerviewAdapter);
        recyclerviewAdapter.setTaskList(rowItems);
    }

    private void cargarDatos(){
        txtPesoTotalLote.setText(recepcionQrPlantaEntity.getPesoTotalLote().toString()+" KG");
        txtCantidadTotalBultosLote.setText(recepcionQrPlantaEntity.getCantidadTotalBultos().toString());
        txtTotalNumeroManifiestosLote.setText(recepcionQrPlantaEntity.getCantidadTotalManifiestos().toString());
    }


}

package com.caircb.rcbtracegadere.fragments.impresora;

import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.adapters.RecyclerViewPrinterAdapter;
import com.caircb.rcbtracegadere.database.entity.ImpresoraEntity;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.fragments.planta.HomePlantaFragment;
import com.caircb.rcbtracegadere.fragments.recolector.HomeTransportistaFragment;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnRecyclerTouchListener;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.RowPrinters;
import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.utils.AndroidUtils;
import com.caircb.rcbtracegadere.utils.UIHelper;
import com.zebra.android.comm.ZebraPrinterConnectionException;
import com.zebra.android.discovery.BluetoothDiscoverer;
import com.zebra.android.discovery.DiscoveredPrinter;
import com.zebra.android.discovery.DiscoveredPrinterBluetooth;
import com.zebra.android.discovery.DiscoveryHandler;

import java.util.ArrayList;
import java.util.List;

public class ImpresoraConfigurarFragment extends MyFragment implements View.OnClickListener {
    LinearLayout btn_search, btn_back,lnlNOImpresora,lnlImpresoraPredeterminada;
    View progressOverlay;
    Button btnCambiarImpresora;
    TextView txtImpresoraCodigo,txtImpresoraAdress;
    DialogBuilder builder;
    private String selectedPrinter;
    private static DiscoveryHandler btDiscoveryHandler = null;
    private ArrayList<RowPrinters> discoveredPrinters ;
    List<RowPrinters> pairedPrinters;
    private RecyclerView rvAvailablePrinters, rvPairedPrinters;
    private RecyclerViewPrinterAdapter rvAvailablePrintersAdapter, rvPairedPrintersAdapter;
    private OnRecyclerTouchListener touchListenerAP,touchListenerPP;
    public static ImpresoraConfigurarFragment create() {
        return new ImpresoraConfigurarFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setView(inflater.inflate(R.layout.fragment_impresora, container, false));
        init();
        initImpresoraPredeterminada();
        initPairedPrinters();
        return getView();
    }

    private void init() {
        progressOverlay = getView().findViewById(R.id.progress_overlay);
        btn_search = getView().findViewById(R.id.btnPrinterSearch);
        btn_back = getView().findViewById(R.id.btnPrinterBack);
        btn_search.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        rvAvailablePrinters = getView().findViewById(R.id.rvAvailablePrinters);
        rvPairedPrinters = getView().findViewById(R.id.rvPairedPrinters);
        touchListenerAP = new OnRecyclerTouchListener(getActivity(),rvAvailablePrinters);
        touchListenerPP = new OnRecyclerTouchListener(getActivity(),rvPairedPrinters);

        lnlNOImpresora =  getView().findViewById(R.id.lnlNOImpresora);
        lnlImpresoraPredeterminada = getView().findViewById(R.id.lnlImpresoraPredeterminada);
        btnCambiarImpresora = getView().findViewById(R.id.btnCambiarImpresora);
        txtImpresoraAdress = getView().findViewById(R.id.txtImpresoraAdress);
        txtImpresoraCodigo = getView().findViewById(R.id.txtImpresoraCodigo);

        btnCambiarImpresora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });
    }

    private boolean checkImpresora(){
        String data = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("tipoSubRuta");
        return MyApp.getDBO().impresoraDao().existeImpresora(data!=null && data.length()>0?Integer.parseInt(data):0);
    }

    private void initImpresoraPredeterminada(){
        if(checkImpresora()){
            //obtener el data de la impresora seleccionada...
            ImpresoraEntity imp = MyApp.getDBO().impresoraDao().searchImpresoraPredeterminada();
            if(imp!=null){
                lnlNOImpresora.setVisibility(View.GONE);
                lnlImpresoraPredeterminada.setVisibility(View.VISIBLE);
                txtImpresoraCodigo.setText(imp.getCode());
                txtImpresoraAdress.setText(imp.getAddress());
                btnCambiarImpresora.setEnabled(true);
            }else{
                txtImpresoraCodigo.setText("");
                txtImpresoraAdress.setText("");
            }
        }else{
            lnlNOImpresora.setVisibility(View.VISIBLE);
            lnlImpresoraPredeterminada.setVisibility(View.GONE);
            txtImpresoraCodigo.setText("");
            txtImpresoraAdress.setText("");
        }
    }

    private void initPairedPrinters(){
        pairedPrinters = MyApp.getDBO().impresoraDao().getListaImpresora();
        selectedPrinter="MacPrinter";

        rvPairedPrinters.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvPairedPrinters.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        rvPairedPrintersAdapter = new RecyclerViewPrinterAdapter(getActivity(), selectedPrinter);
        rvPairedPrintersAdapter.setTaskList(pairedPrinters);
        rvPairedPrinters.setAdapter(rvPairedPrintersAdapter);

        touchListenerPP.setClickable(new OnRecyclerTouchListener.OnRowClickListener() {
            @Override
            public void onRowClicked(int position) {
                final String pName = pairedPrinters.get(position).getName();
                final String pAddress = pairedPrinters.get(position).getAddress();
                final String str = (pName != "not available" ? pName : pAddress);
                builder = new DialogBuilder(getActivity());
                builder.setMessage("¿Quieres desvincular este dispositivo?"+ " ["+ str +"]");
                builder.setCancelable(true);
                builder.setPositiveButton("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try{
                            //MyApp.getDBO().impresoraDao().deleteImpresora();
                            //Toast.makeText(getActivity(),"The device has been unpaired", Toast.LENGTH_SHORT).show();
                            //initPairedPrinters();
                        }catch (Exception ex){}
                        builder.dismiss();
                    }
                });
                builder.setNegativeButton("NO", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(),"Acción cancelada", Toast.LENGTH_SHORT).show();
                        builder.dismiss();
                    }
                });

                builder.show();
            }

            @Override
            public void onIndependentViewClicked(int independentViewID, int position) {

            }
        });

        //dbHelper.close();
    }

    private void searchPrinters(){
        btDiscoveryHandler = new DiscoveryHandler() {
            @Override
            public void foundPrinter(DiscoveredPrinter printer) {
                DiscoveredPrinterBluetooth p = (DiscoveredPrinterBluetooth) printer;
                discoveredPrinters.add(new RowPrinters(((p.friendlyName != null) ? p.friendlyName : "not available"), p.address));
            }

            @Override
            public void discoveryFinished() {
                setBluetoothAdapter();

                touchListenerAP.setClickable(new OnRecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                        Toast.makeText(getActivity(),"Funcionalidad bloqueda",Toast.LENGTH_SHORT);
                        //Toast.makeText(getActivity(),discoveredPrinters.get(position).getName(),Toast.LENGTH_SHORT).show();
                        /*MyApp.getDBO().impresoraDao().deleteImpresora();
                        final String pName = discoveredPrinters.get(position).getName();
                        final String pAddress = discoveredPrinters.get(position).getAddress();

                        final String str = (pName != "not available" ? pName : pAddress);

                        builder = new DialogBuilder(getActivity());
                        builder.setMessage("¿Quieres emparejar este dispositivo?" + " ["+ str +"]");
                        builder.setCancelable(true);
                        builder.setPositiveButton("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try{
                                    MyApp.getDBO().impresoraDao().saveOrUpdateImpresora(pName,pAddress);
                                    Toast.makeText(getActivity(),"The device has been paired", Toast.LENGTH_SHORT).show();
                                    initPairedPrinters();
                                }catch (Exception ex){}
                                builder.dismiss();
                            }
                        });
                        builder.setNegativeButton("NO", new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getActivity(),"Acción cancelada", Toast.LENGTH_SHORT).show();
                                builder.dismiss();
                            }
                        });

                        builder.show();

                         */
                    }

                    @Override
                    public void onIndependentViewClicked(int independentViewID, int position) {

                    }
                });
            }

            @Override
            public void discoveryError(String s) {

            }
        };

        initScanBluetooth();
    }

    private void setBluetoothAdapter(){
        AndroidUtils.animateView(progressOverlay, View.GONE, 0, 200);

        rvAvailablePrinters.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvAvailablePrinters.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        rvAvailablePrintersAdapter = new RecyclerViewPrinterAdapter(getActivity(), selectedPrinter);
        rvAvailablePrintersAdapter.setTaskList(discoveredPrinters);

        rvAvailablePrinters.setAdapter(rvAvailablePrintersAdapter);
    }

    private void initScanBluetooth(){
        discoveredPrinters = new ArrayList<>();
        AndroidUtils.animateView(progressOverlay, View.VISIBLE, 0.4f, 200);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    BluetoothDiscoverer.findPrinters(getActivity(), btDiscoveryHandler);
                } catch (ZebraPrinterConnectionException e) {
                    new UIHelper(getActivity()).showErrorDialogOnGuiThread(e.getMessage());
                } catch (InterruptedException e) {
                    new UIHelper(getActivity()).showErrorDialogOnGuiThread(e.getMessage());
                } finally {
                    Looper.myLooper().quit();
                }
            }
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(rvAvailablePrinters!=null) {
            rvAvailablePrinters.addOnItemTouchListener(touchListenerAP);
        }

        if(rvPairedPrinters!=null) {
            rvPairedPrinters.addOnItemTouchListener(touchListenerPP);
        }
    }

    private void retroceder(){
        switch (MySession.getIdPerfil()){
            case 3136 :
                setNavegate(HomeTransportistaFragment.create());
                break;
            case 3137 :
                setNavegate(HomePlantaFragment.create());
                break;
        }
    }

    @Override
    public void onClick(View v) {
      switch (v.getId()){
            case R.id.btnPrinterSearch:
                searchPrinters();
                break;
            case R.id.btnPrinterBack:
                retroceder();
                break;
        }
    }
}

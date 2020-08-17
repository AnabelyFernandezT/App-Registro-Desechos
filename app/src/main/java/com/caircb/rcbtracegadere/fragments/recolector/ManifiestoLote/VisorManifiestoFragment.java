package com.caircb.rcbtracegadere.fragments.recolector.ManifiestoLote;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.fragments.recolector.HojaRutaProcesadaFragment;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.tasks.UserConsultarNombreManifiestoTask;
import com.joanzapata.pdfview.PDFView;

import java.io.File;

import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;
import es.voghdev.pdfviewpager.library.util.FileUtil;

public class VisorManifiestoFragment extends MyFragment implements View.OnClickListener, DownloadFile.Listener {

    private static final String ARG_PARAM1 = "manifiestoID";
    private static final String ARG_ESTADO = "estado";
    Integer idAppManifiesto, estado;
    LinearLayout btnVistaPreviaCancelar;
    UserConsultarNombreManifiestoTask userConsultarNombreManifiestoTask;

    RemotePDFViewPager remotePDFViewPager;
    PDFPagerAdapter adapter;
    PDFView pdfView;

    public VisorManifiestoFragment(){
    }

    public static VisorManifiestoFragment newInstance(Integer manifiestoID){
        VisorManifiestoFragment f = new VisorManifiestoFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_PARAM1,manifiestoID);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            idAppManifiesto = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setView(inflater.inflate(R.layout.visor_manifiesto_fragment,container,false));
        init();
        return getView();
    }

    @SuppressLint("RestrictedApi")
    private void init(){
        pdfView = (PDFView) getView().findViewById(R.id.pdfViewPager);
        btnVistaPreviaCancelar = getView().findViewById(R.id.btnVistaPreviaCancelar);
        btnVistaPreviaCancelar.setOnClickListener(this);

        cargarPdf();
    }

    private void cargarPdf(){

        userConsultarNombreManifiestoTask = new UserConsultarNombreManifiestoTask(getActivity(),idAppManifiesto,2);
        userConsultarNombreManifiestoTask.setmOnNombreManifiestoListenner(new UserConsultarNombreManifiestoTask.OnNombreManifiestoListenner() {
            @Override
            public void onSuccessful(String nombrePdfManifiesto_url) {
                mostrarPdf(nombrePdfManifiesto_url);
            }
        });
        userConsultarNombreManifiestoTask.execute();
    }

    private void mostrarPdf(String url){
        final DownloadFile.Listener listener = this;
        remotePDFViewPager =  new RemotePDFViewPager(getActivity(), url, listener);
        remotePDFViewPager.setId(R.id.pdfViewPager);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnVistaPreviaCancelar:
                setNavegate(HojaRutaProcesadaFragment.newInstance());
                break;
        }
    }


    @Override
    public void onSuccess(String url, String destinationPath) {
        adapter = new PDFPagerAdapter(getActivity(), FileUtil.extractFileNameFromURL(url));
        File f = new File(destinationPath);
        pdfView.fromFile(f).load();
        userConsultarNombreManifiestoTask.progressHide();
    }

    @Override
    public void onFailure(Exception e) {
        pdfView.fromAsset("not-found.pdf").load();
        userConsultarNombreManifiestoTask.progressHide();
    }

    @Override
    public void onProgressUpdate(int progress, int total) { }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(adapter != null)
            adapter.close();
    }
}
package com.caircb.rcbtracegadere.fragments.Notifaciones;


import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caircb.rcbtracegadere.CierreLoteActivity;
import com.caircb.rcbtracegadere.Firebase.FirebaseMessagingService;
import com.caircb.rcbtracegadere.MainActivity;
import com.caircb.rcbtracegadere.MyApp;
import com.caircb.rcbtracegadere.Notificaciones.ResultCambioChoferActivity;
import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.ResultActivity;
import com.caircb.rcbtracegadere.ResultKilometraje;
import com.caircb.rcbtracegadere.adapters.NotificationAdapter;
import com.caircb.rcbtracegadere.components.SearchView;
import com.caircb.rcbtracegadere.database.entity.NotificacionEntity;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.dialogs.DialogInicioMovilizacion;
import com.caircb.rcbtracegadere.fragments.Sede.HomeSedeFragment;
import com.caircb.rcbtracegadere.fragments.recolector.HomeTransportistaFragment;
import com.caircb.rcbtracegadere.generics.MyFragment;
import com.caircb.rcbtracegadere.generics.OnRecyclerTouchListener;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.ItemNotificacion;
import com.caircb.rcbtracegadere.tasks.UserReAbrirLoteTask;

import java.util.ArrayList;
import java.util.List;

import static androidx.core.content.ContextCompat.getSystemService;

public class  NotificacionesFragment extends MyFragment implements View.OnClickListener{

    LinearLayout btnRetornarInicio;
    private RecyclerView recyclerView;
    private NotificationAdapter recyclerviewAdapter;
    private FirebaseMessagingService firebaseMessagingService = new FirebaseMessagingService();
    private OnRecyclerTouchListener touchListener;
    private List<ItemNotificacion> notificationList = new ArrayList<>();
    private FragmentActivity myContext;
    List<NotificacionEntity> notificacionEntities;


    public static NotificacionesFragment newInstance() {
        return new NotificacionesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //datosLotesDisponibles();
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setView(inflater.inflate(R.layout.fragment_notificaciones, container, false));
        setHideHeader();
        init();
        initItems();
        return getView();
    }

    private void init(){

        recyclerView = getView().findViewById(R.id.recyclerviewNotificaciones);
        recyclerviewAdapter = new NotificationAdapter(getActivity());
        btnRetornarInicio = getView().findViewById(R.id.btnRetornarInicio);
        btnRetornarInicio.setOnClickListener(this);
    }

    private  void cargarNotificaciones(){
           notificationList.clear();
           notificacionEntities=MyApp.getDBO().notificacionDao().fetchNotificaciones();
           if (notificacionEntities.size()>0){
               for(int i = 0; i< MyApp.getDBO().notificacionDao().fetchNotificaciones().size(); i++){
                   ItemNotificacion it = new ItemNotificacion();
                   it.setIdNotificacion(MyApp.getDBO().notificacionDao().fetchNotificaciones().get(i).getIdNotificacion());
                   it.setNombreNotificacion(MyApp.getDBO().notificacionDao().fetchNotificaciones().get(i).getNombreNotificacion());
                   it.setEstadoNotificacion(MyApp.getDBO().notificacionDao().fetchNotificaciones().get(i).getEstadoNotificacion());
                   it.setPeso(MyApp.getDBO().notificacionDao().fetchNotificaciones().get(i).getPeso());
                   it.setIdManifiesto(MyApp.getDBO().notificacionDao().fetchNotificaciones().get(i).getIdManifiesto());
                   it.setTipoNotificacion(MyApp.getDBO().notificacionDao().fetchNotificaciones().get(i).getTipoNotificacion());
                   notificationList.add(it);
               }
           }

        recyclerviewAdapter.setTaskList(notificationList);
        recyclerView.setAdapter(recyclerviewAdapter);
    }

    private void initItems() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        cargarNotificaciones();

        touchListener = new OnRecyclerTouchListener(getActivity(),recyclerView);
        touchListener.setClickable(new OnRecyclerTouchListener.OnRowClickListener() {
            @Override
            public void onRowClicked(int position) {
              //  firebaseMessagingService.showNotification(notificationList.get(position).getNombreNotificacion(), notificationList.get(position).getEstadoNotificacion(), notificationList.get(position).getTipoNotificacion(), MyApp.getsInstance().getApplicationContext());
                //Toast.makeText(getActivity(),String.valueOf(notificationList.get(position).getTipoNotificacion()), Toast.LENGTH_SHORT).show();

                Intent intent;
                if(notificationList.get(position).getTipoNotificacion().equals("2")||notificationList.get(position).getTipoNotificacion().equals("15")){
                    intent = new Intent(myContext, ResultKilometraje.class);
                }else if(notificationList.get(position).getTipoNotificacion().equals("7")){
                    intent = new Intent(myContext, ResultCambioChoferActivity.class);
                }else if(notificationList.get(position).getTipoNotificacion().equals("8")) {
                    intent = new Intent(myContext, MainActivity.class);//solo notificacion
                    MyApp.getDBO().notificacionDao().deleteNotification(notificationList.get(position).getIdNotificacion());
                }else if (notificationList.get(position).getTipoNotificacion().equals("12")){
                    setNavegate(HomeTransportistaFragment.create());
                    intent= new Intent(myContext, CierreLoteActivity.class);

                }else if (notificationList.get(position).getTipoNotificacion().equals("16")){
                    setNavegate(HomeTransportistaFragment.create());
                    intent = new Intent(myContext, ResultActivity.class);//solo notificacion
                    MyApp.getDBO().notificacionDao().deleteNotification(notificationList.get(position).getIdNotificacion());

                }  else{
                    intent = new Intent(myContext, ResultActivity.class);//solo notificacion
                    MyApp.getDBO().notificacionDao().deleteNotification(notificationList.get(position).getIdNotificacion());


                }
                intent.putExtra("notification_data",notificationList.get(position).getEstadoNotificacion());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);


            }


//                Toast.makeText(getActivity(),String.valueOf(notificationList.get(position).getNombreNotificacion()), Toast.LENGTH_SHORT).show();


            @Override
            public void onIndependentViewClicked(int independentViewID, int position) {

            }
        });
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getActivity().getBaseContext(), R.drawable.shape_divider));
        recyclerView.addItemDecoration(divider);
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRetornarInicio:
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

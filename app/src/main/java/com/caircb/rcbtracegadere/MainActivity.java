package com.caircb.rcbtracegadere;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;

import com.caircb.rcbtracegadere.adapters.DialogMenuBaseAdapter;
import com.caircb.rcbtracegadere.adapters.MenuBaseAdapter;
import com.caircb.rcbtracegadere.fragments.impresora.ImpresoraConfigurarFragment;
import com.caircb.rcbtracegadere.fragments.planta.HomePlantaFragment;
import com.caircb.rcbtracegadere.fragments.recolector.HomeTransportistaFragment;
import com.caircb.rcbtracegadere.generics.MyAppCompatActivity;
import com.caircb.rcbtracegadere.generics.OnBackPressed;
import com.caircb.rcbtracegadere.generics.OnCameraListener;
import com.caircb.rcbtracegadere.helpers.MyConstant;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.models.MenuItem;
import com.caircb.rcbtracegadere.models.RowItem;
import com.caircb.rcbtracegadere.models.request.RequestCredentials;
import com.caircb.rcbtracegadere.tasks.PaquetesTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarCatalogosTask;
import com.caircb.rcbtracegadere.tasks.UserUpdateAppTask;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends MyAppCompatActivity implements AdapterView.OnItemClickListener {

    private LinearLayout mDrawerLayout;
    private ListView mDrawerMenuItems, mDialogMenuItems;
    private DrawerLayout mDrawer;
    AlertDialog.Builder builder;
    private TextView txtUserNombre, txtnombreLugarTrabajo;

    private DialogMenuBaseAdapter dialogMenuBaseAdapter;
    private List<RowItem> rowItems;
    final ArrayList selectedItems = new ArrayList();
    final CharSequence[] optionsCatalog = {"OBSERVACIONES","TIPO DESECHOS","TIPO UNIDAD","VEHICULOS","PAQUETES","MOTIVOS NO RECOLECION"};

    //Parametros Globales
    private boolean inicioSesion;

    JSONArray jsonMenus;
    JSONObject json;
    JSONArray jsonLugares;

    //UserUpdateAppTask updateAppTask;

    FragmentTransaction fragmentTransaction;
    FragmentManager fm;

    UserUpdateAppTask userUpdateAppTask;
    UserConsultarCatalogosTask consultarCatalogosTask;
    PaquetesTask paquetesTask;
    PaquetesTask.TaskListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMenuLateral();
        existeCatalogos();
        existePaquetes();
        validateInitFragment();
    }

    private void validateInitFragment(){
        //if(1==1){
           //initFragment((HomeTransportistaFragment.create()));
           //initFragment(HomePlantaFragment.create());
        //}
        switch (MySession.getIdPerfil()){

            case 3136 :
                initFragment((HomeTransportistaFragment.create()));
                break;
            case 3137 :
                initFragment((HomePlantaFragment.create()));
                break;
        }
    }
    private void initFragment(Fragment _fragmentinit) {
        fragment = _fragmentinit;
        getFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_portal, fragment)
                .commit();
        fm = getFragmentManager();
    }

    public void initMenuLateral(){
        RequestCredentials cr = new RequestCredentials();

        mDrawerLayout = (LinearLayout) findViewById(R.id.left_drawer);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerMenuItems = (ListView)findViewById(R.id.menu_items);

        txtUserNombre = (TextView)findViewById(R.id.nombreUsuario);
        txtnombreLugarTrabajo = (TextView)findViewById(R.id.txtNombreLugarTrabajo);

        txtUserNombre.setText(MySession.getUsuarioNombre());
        txtnombreLugarTrabajo.setText(MySession.getLugarNombre());

        rowItems = new ArrayList<>();

        try{
            jsonMenus = new JSONArray(MySession.getMenus());
            jsonLugares = new JSONArray(MySession.getLugares());
            if(jsonMenus.length()>0){
                for (int i = 0; i < jsonMenus.length(); i++){
                    json = jsonMenus.getJSONObject(i);
                    if(json.getString("nombre").equals("Modulos")){
                        if(jsonLugares.length()>1){
                            rowItems.add(new RowItem(
                                    json.getString("nombre"),
                                    getResources(json.getString("icono")),
                                    json.getBoolean("isHabilitado")));
                        }
                    }else{
                        rowItems.add(new RowItem(
                                json.getString("nombre"),
                                getResources(json.getString("icono")),
                                json.getBoolean("isHabilitado")));
                    }
                }
            }

            jsonMenus=null;
            json=null;

        }catch (JSONException e){
            e.printStackTrace();
        }

        MenuBaseAdapter adapter = new MenuBaseAdapter(MainActivity.this,
                R.layout.layout_main_menu, rowItems);

        mDrawerMenuItems.setAdapter(adapter);
        mDrawerMenuItems.setOnItemClickListener(MainActivity.this);
    }



    private void onCloseApp(){
        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Esta usted seguro de salir del sistema!");
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                MainActivity.this.getSharedPreferences(MyConstant.SEG_SP, MainActivity.this.MODE_PRIVATE).edit().clear().apply();
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void onCopyDatabase(){
        /*try {
            if(dbHelper.copyDataBase()) {
                showToast("¡base de datos respaldada!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public void NavegationFragment(Fragment _fragment) {
        navegate(_fragment);
    }

    private void navegate(Fragment _fragment){
        fragment = _fragment;
        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.enter_from_left, R.animator.enter_from_right, 0, 0);
        fragmentTransaction.replace(R.id.fragment_portal, fragment);
        fragmentTransaction.commit();
    }

    public void openMenuOpcion(){
        if (mDrawer.isDrawerOpen(mDrawerLayout)){
            mDrawer.closeDrawers();
        }else{
            mDrawer.openDrawer(mDrawerLayout);
        }
    }

    private void openConfigurar(){
        final Dialog mdialog = new Dialog(this);
        final ArrayList<MenuItem> myListOfItems = new ArrayList<>();
        myListOfItems.add(new MenuItem("Impresora"));
        dialogMenuBaseAdapter = new DialogMenuBaseAdapter(this,myListOfItems);
        View view = getLayoutInflater().inflate(R.layout.dialog_main, null);
        mDialogMenuItems =(ListView) view.findViewById(R.id.custom_list);
        mDialogMenuItems.setAdapter(dialogMenuBaseAdapter);
        mDialogMenuItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenuItem item= myListOfItems.get(position);
                if(!item.isEnabled()){
                     if(item.getNombre().equals("Impresora")){
                        if(mdialog!=null){
                            mdialog.dismiss();
                            openMenuOpcion();
                            NavegationFragment(ImpresoraConfigurarFragment.create());
                        }
                    }
                }
            }
        });


        mdialog.setTitle("Configuraciones");
        mdialog.setContentView(view);
        mdialog.show();
    }

    private void openActualizar(){

        final Dialog mdialog = new Dialog(this);
        final ArrayList<MenuItem> myListOfItems = new ArrayList<>();
        myListOfItems.add(new MenuItem("Aplicacion"));
        //myListOfItems.add(new MenuItem("Catalogos"));
        //myListOfItems.add(new MenuItem("Impresora"));

        dialogMenuBaseAdapter = new DialogMenuBaseAdapter(this,myListOfItems);
        View view = getLayoutInflater().inflate(R.layout.dialog_main, null);
        mDialogMenuItems =(ListView) view.findViewById(R.id.custom_list);
        mDialogMenuItems.setAdapter(dialogMenuBaseAdapter);
        mDialogMenuItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenuItem item= myListOfItems.get(position);
                if(!item.isEnabled()){
                    if (item.getNombre().equals("Aplicacion")){
                        if(mdialog!=null){
                            mdialog.dismiss();
                            onUpdateApp();
                        }
                    }
                }
            }
        });


        mdialog.setTitle("Actualizaciones");
        mdialog.setContentView(view);
        mdialog.show();
    }

    private void onUpdateApp(){
        userUpdateAppTask = new UserUpdateAppTask(this);
        userUpdateAppTask.execute();
    }

    private void existeCatalogos(){
        List<Integer> listaCatalogos = new ArrayList<>();

        if(!MyApp.getDBO().catalogoDao().existeCatalogosEspecifico(1)) listaCatalogos.add(1);
        if(!MyApp.getDBO().catalogoDao().existeCatalogosEspecifico(2)) listaCatalogos.add(2);
        if(!MyApp.getDBO().catalogoDao().existeCatalogosEspecifico(3)) listaCatalogos.add(3);
        if(!MyApp.getDBO().catalogoDao().existeCatalogosEspecifico(4)) listaCatalogos.add(4);
        if(!MyApp.getDBO().catalogoDao().existeCatalogosEspecifico(6)) listaCatalogos.add(6);
        //if(!dbHelper.existeCatalogosEspecifico(4)) listaCatalogos.add(4);

        if(listaCatalogos.size()>0){
            consultarCatalogosTask = new UserConsultarCatalogosTask(this,listaCatalogos);
            consultarCatalogosTask.execute();
        }
    }

    private void existePaquetes(){

       if(!MyApp.getDBO().paqueteDao().existePaquetes()){
                paquetesTask = new PaquetesTask(this, listener);
                paquetesTask.execute();
        }
    }
    /*

    private boolean existeImpresoraVinculada(){
        dbHelper.open();
        Cursor c = dbHelper.fetchParametroEspecifico("MacPrinter");
        dbHelper.close();
        return c.getCount()>0;
    }*/

    private void guardarLugar(String nombreLugar){
        try{
            jsonLugares = new JSONArray(MySession.getLugares());
            for (int i = 0; i < jsonLugares.length(); i++){
                json = jsonLugares.getJSONObject(i);
                if(json.getString("nombre").equals(nombreLugar)){
                    MySession.setIdPerfil(json.getInt("idPerfil"));
                    MySession.setLugarNombre(json.getString("nombre"));
                    if(nombreLugar.equals("TRANSPORTISTA")){
                        navegate((HomeTransportistaFragment.create()));
                    }else{
                        navegate((HomePlantaFragment.create()));
                    }
                    txtnombreLugarTrabajo.setText(MySession.getLugarNombre());
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    //////////////////////////////////////////
    private void openSincronizaCatalogos(){
        selectedItems.clear();
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("CATALOGOS")
                .setMultiChoiceItems(optionsCatalog, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if (isChecked){
                            selectedItems.add(position+1);
                        }else if (selectedItems.contains(position+1)){
                            selectedItems.remove(Integer.valueOf(position+1));
                        }
                    }
                }).setPositiveButton("SINCRONIZAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(selectedItems.size()>0){
                    //String ids = TextUtils.join(",",selectedItems);
                    for (final Object catalogoID :selectedItems) {
                        if (catalogoID.equals(5)){
                            existePaquetes();
                        }else{
                            consultarCatalogosTask = new UserConsultarCatalogosTask(MainActivity.this,selectedItems);
                            consultarCatalogosTask.execute();
                        }
                    }
                }
            }
        }).setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    //////////////////////////////////////////

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(rowItems!=null) {
            switch (rowItems.get(position).getNombre()) {
                case "Salir":
                    onCloseApp();
                    break;
                case "Base de Datos":
                    onCopyDatabase();
                    break;
                case "Actualizar":
                    openActualizar();
                    break;
                case "Modulos":
                    openModulos();
                    break;
                case "Configurar":
                    openConfigurar();
                    break;
            }
        }
    }

    private void openModulos(){
        final Dialog mdialog = new Dialog(MainActivity.this);
        final ArrayList<MenuItem> myListOfItems = new ArrayList<>();

        try{
            jsonLugares = new JSONArray(MySession.getLugares());
            if(jsonLugares.length()>0){
                for (int i = 0; i < jsonLugares.length(); i++){
                    json = jsonLugares.getJSONObject(i);
                    myListOfItems.add(new MenuItem(json.getString("nombre")));
                }
            }
            jsonLugares=null;
            json=null;
        }catch (JSONException e){
            e.printStackTrace();
        }
        dialogMenuBaseAdapter = new DialogMenuBaseAdapter(MainActivity.this, myListOfItems);
        View view = getWindow().getLayoutInflater().inflate(R.layout.dialog_main, null);
        mDialogMenuItems = (ListView) view.findViewById(R.id.custom_list);
        mDialogMenuItems.setAdapter(dialogMenuBaseAdapter);
        mDialogMenuItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenuItem item = myListOfItems.get(position);
                if (!item.isEnabled()) {
                    if (item.getNombre().equals("TRANSPORTISTA")) {
                        if (mdialog != null) {
                            mdialog.dismiss();
                            openMenuOpcion();
                            guardarLugar("TRANSPORTISTA");
                        }
                    } else if (item.getNombre().equals("PLANTA")) {
                        if (mdialog != null) {
                            mdialog.dismiss();
                                openMenuOpcion();
                                guardarLugar("PLANTA");
                        }
                    }
                }
            }
        });
        mdialog.setTitle("Modulos");
        mdialog.setContentView(view);
        mdialog.show();
    }


    @Override
    public void onBackPressed() {
        if(fragment!=null) {
            if (fragment instanceof OnBackPressed) {
                ((OnBackPressed) fragment).onBackPressed();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (fragment != null) {
            if (fragment instanceof OnCameraListener) {
                ((OnCameraListener) fragment).onCameraResult(requestCode, resultCode, data);
            }
        }
    }


}
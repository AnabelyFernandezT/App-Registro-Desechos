package com.caircb.rcbtracegadere;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.caircb.rcbtracegadere.database.entity.InformacionModulosEntity;

import androidx.annotation.Dimension;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.drawerlayout.widget.DrawerLayout;

import com.caircb.rcbtracegadere.adapters.DialogMenuBaseAdapter;
import com.caircb.rcbtracegadere.adapters.MenuBaseAdapter;
import com.caircb.rcbtracegadere.database.entity.CatalogoEntity;
import com.caircb.rcbtracegadere.dialogs.DialogBuilder;
import com.caircb.rcbtracegadere.dialogs.DialogInformacionModulos;
import com.caircb.rcbtracegadere.dialogs.DialogMensajes;
import com.caircb.rcbtracegadere.dialogs.DialogPlacaSede;
import com.caircb.rcbtracegadere.fragments.GestorAlterno.HomeGestorAlternoFragment;
import com.caircb.rcbtracegadere.fragments.Hoteles.HomeHotelFragment;
import com.caircb.rcbtracegadere.fragments.Sede.HomeSedeFragment;
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
import com.caircb.rcbtracegadere.models.response.DtoCatalogo;
import com.caircb.rcbtracegadere.models.response.DtoFindRutas;
import com.caircb.rcbtracegadere.tasks.PaquetesTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarCatalogosTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarDestinosTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarInicioRutaTask;
import com.caircb.rcbtracegadere.tasks.UserConsultarRutasTask;
import com.caircb.rcbtracegadere.tasks.UserDestinoEspecificoTask;
import com.caircb.rcbtracegadere.tasks.UserInformacionModulosTask;
import com.caircb.rcbtracegadere.tasks.UserUpdateAppTask;
import com.google.firebase.auth.FirebaseAuth;
import com.itextpdf.awt.geom.Point;
import com.itextpdf.text.pdf.PdfName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends MyAppCompatActivity implements AdapterView.OnItemClickListener {

    private LinearLayout mDrawerLayout;
    private ListView mDrawerMenuItems, mDialogMenuItems;
    private DrawerLayout mDrawer;
    AlertDialog.Builder builder;
    private TextView txtUserNombre, txtnombreLugarTrabajo, nombreLugarTrabajo;
    UserInformacionModulosTask informacionModulosTaskl;
    List<DtoCatalogo> listaDestinos, destinosEspecificos;
    UserConsultarDestinosTask consultarDetino;
    UserDestinoEspecificoTask consultaDestinoEspecifico;
    DialogInformacionModulos dialogInformacionModulos;
    UserConsultarInicioRutaTask verificarInicioRutaTask;

    private DialogMenuBaseAdapter dialogMenuBaseAdapter;
    private List<RowItem> rowItems;
    final ArrayList selectedItems = new ArrayList();
    final CharSequence[] optionsCatalog = {"OBSERVACIONES", "TIPO DESECHOS", "TIPO UNIDAD", "VEHICULOS", "PAQUETES", "MOTIVOS NO RECOLECION"};

    //Parametros Globales
    private boolean inicioSesion;

    JSONArray jsonMenus;
    JSONObject json;
    JSONArray jsonLugares;

    //UserUpdateAppTask updateAppTask;

    FragmentTransaction fragmentTransaction;
    FragmentManager fm;

    UserInformacionModulosTask userInformacionModulosTask;
    UserUpdateAppTask userUpdateAppTask;
    UserConsultarCatalogosTask consultarCatalogosTask;
    PaquetesTask paquetesTask;
    UserConsultarRutasTask rutasTask;
    PaquetesTask.TaskListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //consultarInicioFinRuta();
        initMenuLateral();
        existeCatalogos();
        existePaquetes();
        cargarRutas();
        validateInitFragment();
        initBorrarCache();

        if(getIntent().getExtras()!=null){
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                message(value);
            }

        }
    }

    private void validateInitFragment() {
        //if(1==1){
        //initFragment((HomeTransportistaFragment.create()));
        //initFragment(HomePlantaFragment.create());
        //}
        switch (MySession.getIdPerfil()) {

            case 3136:
                initFragment((HomeTransportistaFragment.create()));
                break;
            case 3137:
                initFragment((HomePlantaFragment.create()));
                break;
            case 4136:
                initFragment((HomeSedeFragment.create()));
                break;
            case 4137:
                initFragment((HomeHotelFragment.create()));
                break;
            case 4138:
                initFragment((HomeGestorAlternoFragment.create()));
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

    public void initMenuLateral() {
        RequestCredentials cr = new RequestCredentials();

        mDrawerLayout = (LinearLayout) findViewById(R.id.left_drawer);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerMenuItems = (ListView) findViewById(R.id.menu_items);

        txtUserNombre = (TextView) findViewById(R.id.nombreUsuario);
        txtnombreLugarTrabajo = (TextView) findViewById(R.id.txtNombreLugarTrabajo);
        nombreLugarTrabajo = (TextView) findViewById(R.id.nombreLugarTrabajo);

        txtUserNombre.setText(MySession.getUsuarioNombre());
        txtnombreLugarTrabajo.setText(MySession.getLugarNombre());
        nombreLugarTrabajo.setText(MySession.getDestinoEspecifico());


        rowItems = new ArrayList<>();

        try {
            jsonMenus = new JSONArray(MySession.getMenus());
            jsonLugares = new JSONArray(MySession.getLugares());
            if (jsonMenus.length() > 0) {
                for (int i = 0; i < jsonMenus.length(); i++) {
                    json = jsonMenus.getJSONObject(i);
                    if (json.getString("nombre").equals("MÓDULOS")) {
                        if (jsonLugares.length() > 1) {
                            rowItems.add(new RowItem(
                                    json.getString("nombre"),
                                    getResources(json.getString("icono")),
                                    json.getBoolean("isHabilitado")));
                        }
                    } else {
                        rowItems.add(new RowItem(
                                json.getString("nombre"),
                                getResources(json.getString("icono")),
                                json.getBoolean("isHabilitado")));
                    }
                }
            }

            jsonMenus = null;
            json = null;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        MenuBaseAdapter adapter = new MenuBaseAdapter(MainActivity.this,
                R.layout.layout_main_menu, rowItems);

        mDrawerMenuItems.setAdapter(adapter);
        mDrawerMenuItems.setOnItemClickListener(MainActivity.this);
    }


    private void onCloseApp() {
        final DialogBuilder dialogBuilder = new DialogBuilder(MainActivity.this);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setMessage("¿Está usted seguro de salir del sistema ?");
        dialogBuilder.setPositiveButton("SI", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
                MainActivity.this.getSharedPreferences(MyConstant.SEG_SP, MainActivity.this.MODE_PRIVATE).edit().clear().apply();
                FirebaseAuth.getInstance().signOut();
                finish();
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

    private void onCopyDatabase() {
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

    private void navegate(Fragment _fragment) {
        fragment = _fragment;
        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.enter_from_left, R.animator.enter_from_right, 0, 0);
        fragmentTransaction.replace(R.id.fragment_portal, fragment);
        fragmentTransaction.commit();
    }

    public void openMenuOpcion() {
        if (mDrawer.isDrawerOpen(mDrawerLayout)) {
            mDrawer.closeDrawers();
        } else {
            mDrawer.openDrawer(mDrawerLayout);
        }
    }

    private void openConfigurar() {
        final Dialog mdialog = new Dialog(this);
        final ArrayList<MenuItem> myListOfItems = new ArrayList<>();
        myListOfItems.add(new MenuItem("IMPRESORA"));
        dialogMenuBaseAdapter = new DialogMenuBaseAdapter(this, myListOfItems);
        View view = getLayoutInflater().inflate(R.layout.dialog_main, null);
        mDialogMenuItems = (ListView) view.findViewById(R.id.custom_list);
        mDialogMenuItems.setAdapter(dialogMenuBaseAdapter);
        mDialogMenuItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenuItem item = myListOfItems.get(position);
                if (!item.isEnabled()) {
                    if (item.getNombre().equals("IMPRESORA")) {
                        if (mdialog != null) {
                            mdialog.dismiss();
                            openMenuOpcion();
                            NavegationFragment(ImpresoraConfigurarFragment.create());
                        }
                    }
                }
            }
        });


        mdialog.setTitle("CONFIGURACIONES");
        mdialog.setContentView(view);
        mdialog.show();
    }

    private void openActualizar() {

        final Dialog mdialog = new Dialog(this);
        final ArrayList<MenuItem> myListOfItems = new ArrayList<>();
        myListOfItems.add(new MenuItem("APLICACIÓN"));
        //myListOfItems.add(new MenuItem("Catalogos"));
        //myListOfItems.add(new MenuItem("Impresora"));

        dialogMenuBaseAdapter = new DialogMenuBaseAdapter(this, myListOfItems);
        View view = getLayoutInflater().inflate(R.layout.dialog_main, null);
        mDialogMenuItems = (ListView) view.findViewById(R.id.custom_list);
        mDialogMenuItems.setAdapter(dialogMenuBaseAdapter);
        mDialogMenuItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenuItem item = myListOfItems.get(position);
                if (!item.isEnabled()) {
                    if (item.getNombre().equals("APLICACIÓN")) {
                        if (mdialog != null) {
                            mdialog.dismiss();
                            onUpdateApp();
                        }
                    }
                }
            }
        });


        mdialog.setTitle("ACTUALIZACIONES");
        mdialog.setContentView(view);
        mdialog.show();
    }

    private void onUpdateApp() {
        userUpdateAppTask = new UserUpdateAppTask(this);
        userUpdateAppTask.execute();
    }

    private void existeCatalogos() {
        List<Integer> listaCatalogos = new ArrayList<>();

        if (!MyApp.getDBO().catalogoDao().existeCatalogosEspecifico(1)) listaCatalogos.add(1);
        if (!MyApp.getDBO().catalogoDao().existeCatalogosEspecifico(2)) listaCatalogos.add(2);
        if (!MyApp.getDBO().catalogoDao().existeCatalogosEspecifico(3)) listaCatalogos.add(3);
        if (!MyApp.getDBO().catalogoDao().existeCatalogosEspecifico(4)) listaCatalogos.add(4);
        if (!MyApp.getDBO().catalogoDao().existeCatalogosEspecifico(6)) listaCatalogos.add(6);
        if (!MyApp.getDBO().catalogoDao().existeCatalogosEspecifico(7)) listaCatalogos.add(7);//notificaciones
        //if(!MyApp.getDBO().catalogoDao().existeCatalogosEspecifico(8)) listaCatalogos.add(8);//rutas
        if (!MyApp.getDBO().catalogoDao().existeCatalogosEspecifico(9)) listaCatalogos.add(9);//destino
        if (!MyApp.getDBO().catalogoDao().existeCatalogosEspecifico(12)) listaCatalogos.add(12);//
        if (!MyApp.getDBO().catalogoDao().existeCatalogosEspecifico(14)) listaCatalogos.add(14);
        if (!MyApp.getDBO().catalogoDao().existeCatalogosEspecifico(16)) listaCatalogos.add(16);


        if (listaCatalogos.size() > 0) {
            consultarCatalogosTask = new UserConsultarCatalogosTask(this, listaCatalogos);
            consultarCatalogosTask.execute();
        }
    }

    private void existePaquetes() {

        if (!MyApp.getDBO().paqueteDao().existePaquetes()) {
            paquetesTask = new PaquetesTask(this, listener);
            paquetesTask.execute();
        }
    }

    private void cargarRutas() {
        if (!MyApp.getDBO().rutasDao().existeRutas()) {
            rutasTask = new UserConsultarRutasTask(this);
            rutasTask.execute();
        }
    }
    /*

    private boolean existeImpresoraVinculada(){
        dbHelper.open();
        Cursor c = dbHelper.fetchParametroEspecifico("MacPrinter");
        dbHelper.close();
        return c.getCount()>0;
    }*/

    private void guardarLugar(String nombreLugar) {
        try {
            jsonLugares = new JSONArray(MySession.getLugares());
            for (int i = 0; i < jsonLugares.length(); i++) {
                json = jsonLugares.getJSONObject(i);
                if (json.getString("nombre").equals(nombreLugar)) {
                    MySession.setIdPerfil(json.getInt("idPerfil"));
                    MySession.setLugarNombre(json.getString("nombre"));
                    if (nombreLugar.equals("TRANSPORTISTA")) {
                        MySession.setDestinoEspecifico("");
                        initMenuLateral();
                        MyApp.getDBO().parametroDao().saveOrUpdate("current_placa_transportista", "" + 0);
                        MyApp.getDBO().parametroDao().saveOrUpdate("current_destino", "" + 0);
                        MyApp.getDBO().parametroDao().saveOrUpdate("current_destino_especifico", "" + 0);
                        MyApp.getDBO().parametroDao().saveOrUpdate("current_destino_info", "" + 0);
                        navegate((HomeTransportistaFragment.create()));
                    } else {
                        if (nombreLugar.equals("PLANTA")) {
                            MyApp.getDBO().parametroDao().saveOrUpdate("current_placa_transportista", "" + 0);
                            MyApp.getDBO().parametroDao().saveOrUpdate("current_destino_especifico", "" + 0);
                            MyApp.getDBO().parametroDao().saveOrUpdate("current_destino", "" + 2);
                            MyApp.getDBO().parametroDao().saveOrUpdate("current_destino_info", "" + 0);
                            traerDestinoEspecifico();
                            //navegate((HomePlantaFragment.create()));
                        } else {
                            if (nombreLugar.equals("SEDE")) {
                                MyApp.getDBO().parametroDao().saveOrUpdate("current_placa_transportista", "" + 0);
                                MyApp.getDBO().parametroDao().saveOrUpdate("current_destino_especifico", "" + 0);
                                MyApp.getDBO().parametroDao().saveOrUpdate("current_destino", "" + 1);
                                MyApp.getDBO().parametroDao().saveOrUpdate("current_destino_info", "" + 0);
                                traerDestinoEspecifico();
                                //navegate(HomeSedeFragment.create());
                            } else {
                                if (nombreLugar.equals("HOTEL")) {
                                    MyApp.getDBO().parametroDao().saveOrUpdate("current_placa_transportista", "" + 0);
                                    MyApp.getDBO().parametroDao().saveOrUpdate("current_destino_especifico", "" + 20);
                                    MyApp.getDBO().parametroDao().saveOrUpdate("current_destino", "" + 4);
                                    MyApp.getDBO().parametroDao().saveOrUpdate("current_destino_info", "" + 0);
                                    //traerDestinoEspecifico();
                                    navegate(HomeHotelFragment.create());
                                } else {
                                    MyApp.getDBO().parametroDao().saveOrUpdate("current_placa_transportista", "" + 0);
                                    MyApp.getDBO().parametroDao().saveOrUpdate("current_destino_especifico", "" + 0);
                                    MyApp.getDBO().parametroDao().saveOrUpdate("current_destino", "" + 3);
                                    MyApp.getDBO().parametroDao().saveOrUpdate("current_destino_info", "" + 0);
                                    traerDestinoEspecifico();
                                    navegate(HomeGestorAlternoFragment.create());
                                }
                            }
                        }

                    }

                    txtnombreLugarTrabajo.setText(MySession.getLugarNombre());
                    nombreLugarTrabajo.setText(MySession.getDestinoEspecifico());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void traerDestinoEspecifico() {
        consultaDestinoEspecifico = new UserDestinoEspecificoTask(this);
        consultaDestinoEspecifico.setOnDestinoListener(new UserDestinoEspecificoTask.OnDestinoListener() {
            @Override
            public void onSuccessful(List<DtoCatalogo> catalogos, Integer idDestino) {
                destinosEspecificos = catalogos;
                selectDestinoEspecifico(catalogos);
                if (idDestino == 1) {
                    navegate(HomeSedeFragment.create());
                } else if (idDestino == 2) {
                    navegate((HomePlantaFragment.create()));
                } else if (idDestino == 3) {
                    navegate(HomeGestorAlternoFragment.create());
                } else if (idDestino == 4) {
                    navegate(HomeHotelFragment.create());
                }
            }
        });
        consultaDestinoEspecifico.execute();

    }

    private void selectDestinoEspecifico(List<DtoCatalogo> catalogos) {
        final String[] options = new String[catalogos.size()];
        for (int i = 0; i < destinosEspecificos.size(); i++) {
            options[i] = destinosEspecificos.get(i).getNombre();

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccione Destino");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(DialogInterface dialog, int item) {
                MyApp.getDBO().parametroDao().saveOrUpdate("current_destino_especifico", "" + destinosEspecificos.get(item).getId());
                MySession.setDestinoEspecifico(destinosEspecificos.get(item).getNombre());
                initMenuLateral();
                //System.out.println("Acceso a la variable: "+MyApp.getDBO().parametroDao().fetchParametroEspecifico("current_destino_especifico").getValor());
            }
        });
        builder.setCancelable(false);
        builder.show();

    }

    //////////////////////////////////////////
    private void openSincronizaCatalogos() {
        selectedItems.clear();
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("CATALOGOS")
                .setMultiChoiceItems(optionsCatalog, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if (isChecked) {
                            selectedItems.add(position + 1);
                        } else if (selectedItems.contains(position + 1)) {
                            selectedItems.remove(Integer.valueOf(position + 1));
                        }
                    }
                }).setPositiveButton("SINCRONIZAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (selectedItems.size() > 0) {
                    //String ids = TextUtils.join(",",selectedItems);
                    for (final Object catalogoID : selectedItems) {
                        if (catalogoID.equals(5)) {
                            existePaquetes();
                        } else {
                            consultarCatalogosTask = new UserConsultarCatalogosTask(MainActivity.this, selectedItems);
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
        if (rowItems != null) {
            switch (rowItems.get(position).getNombre()) {
                case "SALIR":
                    onCloseApp();
                    break;
                case "BASE DE DATOS":
                    onCopyDatabase();
                    break;
                case "ACTUALIZAR":
                    openActualizar();
                    break;
                case "MÓDULOS":
                    openModulos();
                    break;
                case "CONFIGURAR":
                    openConfigurar();
                    break;
                case "INFORMACIÓN":
                    openInformacion();
                    break;
            }
        }
    }


    private void openInformacion() {


        dialogInformacionModulos = new DialogInformacionModulos(this);
        boolean estadoProceso = Boolean.parseBoolean(MyApp.getDBO().parametroDao().fecthParametroValorByNombre("estado_transporte"));
        int idTipoEspiecifico = Integer.parseInt(MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_destino_info"));
        System.out.println("RUTA " + estadoProceso + "--" + idTipoEspiecifico);

        if (idTipoEspiecifico == 1 || idTipoEspiecifico == 0) {
            if (estadoProceso == true) {
                informacionModulosTaskl = new UserInformacionModulosTask(this, dialogInformacionModulos);
                informacionModulosTaskl.execute();
            } else {
                message("No hay datos para mostrar...");
            }
        }

        if (idTipoEspiecifico >= 3 && idTipoEspiecifico <= 5) {
            String t = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_placa_lote");
            String idInicioLote = t != null ? t.toString() : "-1";
            if (idInicioLote.equals("-1")) {
                message("No ha iniciado lote...");
            } else {
                informacionModulosTaskl = new UserInformacionModulosTask(this, dialogInformacionModulos);
                informacionModulosTaskl.execute();
            }
            if (idInicioLote.equals("-10")) {
                message("Lote finalizado...");
            }
        }

        if (idTipoEspiecifico >= 6 && idTipoEspiecifico <= 7) {
            String t = MyApp.getDBO().parametroDao().fecthParametroValorByNombre("current_placa_Planta");
            String idInicioLote = t != null ? t.toString() : "-1";
            if (idInicioLote.equals("-1")){
                message("No ha sincronizado...");
            }else {
                informacionModulosTaskl = new UserInformacionModulosTask(this, dialogInformacionModulos);
                informacionModulosTaskl.execute();
                //dialogInformacionModulos.show();
            }
        }




    }


    private void openModulos() {
        final Dialog mdialog = new Dialog(MainActivity.this);
        final ArrayList<MenuItem> myListOfItems = new ArrayList<>();

        try {
            jsonLugares = new JSONArray(MySession.getLugares());
            if (jsonLugares.length() > 0) {
                for (int i = 0; i < jsonLugares.length(); i++) {
                    json = jsonLugares.getJSONObject(i);
                    myListOfItems.add(new MenuItem(json.getString("nombre")));
                }
            }
            //MySession.getIdUsuario();


            jsonLugares = null;
            json = null;
            myListOfItems.add(new MenuItem("NOTIFICACIONES"));
        } catch (JSONException e) {
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
                    } else if (item.getNombre().equals("SEDE")) {
                        if (mdialog != null) {
                            mdialog.dismiss();
                            openMenuOpcion();
                            guardarLugar("SEDE");
                        }
                    } else if (item.getNombre().equals("HOTEL")) {
                        if (mdialog != null) {
                            mdialog.dismiss();
                            openMenuOpcion();
                            guardarLugar("HOTEL");
                        }
                    } else if (item.getNombre().equals("GESTOR")) {
                        if (mdialog != null) {
                            mdialog.dismiss();
                            openMenuOpcion();
                            guardarLugar("GESTOR");
                        }
                    } else if (item.getNombre().equals("NOTIFICACIONES")) {
                        if (mdialog != null) {
                            mdialog.dismiss();
                            mensajes();
                        }
                    }
                }
            }
        });
        mdialog.setTitle("MÓDULOS");
        mdialog.setContentView(view);
        mdialog.setCancelable(false);
        mdialog.show();
    }

    private void mensajes() {
        DialogMensajes dialogMensajes = new DialogMensajes(MainActivity.this);
        dialogMensajes.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMensajes.show();
    }

    private void initBorrarCache() {
        File dir = this.getCacheDir();
        if (deleteDir(dir)) {
            System.out.println("CACHE BORRADA");
        } else {
            System.out.println("FALLO BORRADO");
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        if (fragment != null) {
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

    private void consultarInicioFinRuta(){
        verificarInicioRutaTask = new UserConsultarInicioRutaTask(MainActivity.this);
        verificarInicioRutaTask.setOnRegisterListener(new UserConsultarInicioRutaTask.OnRegisterListener() {
            @Override
            public void onSuccessful() {
                //message("Ha iniciado previamente sesion");
                navegate(HomeTransportistaFragment.create());

            }
        });
        verificarInicioRutaTask.execute();
    }


}
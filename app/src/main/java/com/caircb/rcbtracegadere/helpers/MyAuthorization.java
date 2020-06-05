package com.caircb.rcbtracegadere.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;

import com.caircb.rcbtracegadere.R;
import com.caircb.rcbtracegadere.adapters.DialogMenuBaseAdapter;
import com.caircb.rcbtracegadere.fragments.impresora.ImpresoraConfigurarFragment;
import com.caircb.rcbtracegadere.models.MenuItem;
import com.caircb.rcbtracegadere.models.RowItem;
import com.caircb.rcbtracegadere.models.request.RequestCredentials;
import com.caircb.rcbtracegadere.models.request.RequestTokenFCM;
import com.caircb.rcbtracegadere.models.response.DtoUserCredential;
import com.caircb.rcbtracegadere.models.response.DtoUserTokenCredentials;
import com.caircb.rcbtracegadere.services.WebService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAuthorization {
    JSONArray jsonLugares;
    JSONObject json;



    Context mContext;
    ListView mDialogMenuItems;
    ProgressDialog progressDialog;
    AlertDialog.Builder messageBox;
    String userStr;
    FirebaseAuth auth;
    private DialogMenuBaseAdapter dialogMenuBaseAdapter;


    public interface AuthorizationListener {
        public void onSuccessful();
    }
    private AuthorizationListener  mOnAuthorizationListenerListener;

    public MyAuthorization(@NonNull Context context){
        this.mContext = context;

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Ingresando...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
    }


    public void loginUser(final String user, String password){
        if(mOnAuthorizationListenerListener!=null) {
            userStr = user;

            RequestCredentials cr = new RequestCredentials();
            cr.setUsLogin(user);
            cr.setUsClave(password);
            cr.setUsAplicacion("AppGadere");
            cr.setUsImei(MySession.getIdDevice());
            cr.setUsChip(MySession.getIdChip());
            cr.setUsAppVersion(1);

            progressDialog.show();
            WebService.seg().autentication(cr).enqueue(new Callback<DtoUserCredential>() {
                @Override
                public void onResponse(Call<DtoUserCredential> call, Response<DtoUserCredential> response) {
                    if (response.isSuccessful()) {
                        if (response.body().isExcito()) {
                            obtnerTokenAutorizacionFCM(response.body());
                        } else {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                            message(response.body().getMensaje());
                        }
                    } else {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                    }
                }

                @Override
                public void onFailure(Call<DtoUserCredential> call, Throwable t) {
                    progressDialog.dismiss();
                }
            });
        }

    }


    private void lugarTrabajo () throws JSONException {
        final Dialog mdialog = new Dialog(getActivity());
        final ArrayList<MenuItem> myListOfItems = new ArrayList<>();
        jsonLugares = new JSONArray(MySession.getLugares());

        for (int i = 0; i < jsonLugares.length(); i++){
            json = jsonLugares.getJSONObject(i);
            myListOfItems.add(new MenuItem(
                    json.getString("nombre")));
        }

        dialogMenuBaseAdapter = new DialogMenuBaseAdapter(getActivity(),myListOfItems);
        View view = progressDialog.getWindow().getLayoutInflater().inflate(R.layout.dialog_main, null);
        mDialogMenuItems =(ListView) view.findViewById(R.id.custom_list);
        mDialogMenuItems.setAdapter(dialogMenuBaseAdapter);
        mDialogMenuItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenuItem item= myListOfItems.get(position);
                if(!item.isEnabled()){
                    if(item.getNombre().equals("Catalogos")){
                        if(mdialog!=null){
                            mdialog.dismiss();
                        }
                    }
                    else if(item.getNombre().equals("Impresora")){
                        if(mdialog!=null){
                            mdialog.dismiss();
                        }
                    }
                    else if (item.getNombre().equals("Aplicacion")){
                        if(mdialog!=null){
                            mdialog.dismiss();
                        }
                    }
                }
            }
        });


        mdialog.setTitle("MODULOS");
        mdialog.setContentView(view);
        mdialog.show();
    }

    private void obtnerTokenAutorizacionFCM(final DtoUserCredential user){
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(getActivity(), new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                registarAutorizacionFirebase(user,instanceIdResult.getToken());
            }
        }).addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(progressDialog!=null){progressDialog.dismiss();progressDialog=null;}
                message("El sistema no autoriza su acceso, intente nuevamente en 5 minutos...");
            }
        });
    }

    private void registarAutorizacionFirebase(final DtoUserCredential user, final String token){
        if (token != null) {
            try{
                auth = FirebaseAuth.getInstance();
                auth.signInWithEmailAndPassword(user.getCorreo(),userStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            registarTokenOnServer(user,token);
                        }else{
                            createUserFirebase(user,token);
                        }
                    }
                });

            }catch (Exception e){
                if(progressDialog!=null){progressDialog.dismiss();progressDialog=null;}
                message(e.getMessage());
            }
        }
    }

    private void createUserFirebase(final DtoUserCredential user, final String token){
        try {
            auth.createUserWithEmailAndPassword(user.getCorreo(), userStr).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                //auth.createUserWithEmailAndPassword(_userEmail, _userEmail).addOnCompleteListener((Activity) mContext, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        registarTokenOnServer(user,token);
                    } else {
                        if(progressDialog!=null){progressDialog.dismiss();progressDialog=null;}
                        message("Hay problemas cuando sincroniza sus credenciales de acceso, verifique su conexión a Internet");
                    }
                }
            });
        }catch (Exception e){
            if(progressDialog!=null){progressDialog.dismiss();progressDialog=null;}
            message(e.getMessage());
        }
    }

    private void registarTokenOnServer(final DtoUserCredential user, String token){
        try {

            MySession.setIdUsuario(user.getIdUsuario());
            MySession.setId(user.getId());
            MySession.setIdAplicacion( user.getListaEmpresas().get(0).getLugares().get(0).getIdAplicacion());
            MySession.setIdEmpresa(user.getListaEmpresas().get(0).getIdEmpresa());

            RequestTokenFCM credentials = new RequestTokenFCM();
            credentials.setAplicacion(MySession.getIdAplicacion());
            credentials.setCredencial(token);
            credentials.setEmpresa(MySession.getIdEmpresa());
            credentials.setImei(MySession.getIdDevice());
            credentials.setUsuario(MySession.getIdUsuario());

            WebService.seg().registrarSession(credentials).enqueue(new Callback<DtoUserTokenCredentials>() {
                @Override
                public void onResponse(Call<DtoUserTokenCredentials> call, Response<DtoUserTokenCredentials> response) {
                    if(response.isSuccessful()) {
                       // MySession.setLogin(true);
                        //MySession.setMenus(user.getListaEmpresas().get(0).getLugares().get(0).getMenus());
                        //mOnAuthorizationListenerListener.onSuccessful();//initMain(true);

                        MySession.setLugares(user.getListaEmpresas().get(0).getLugares());

                        try {
                            lugarTrabajo();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }else {
                        if(progressDialog!=null){progressDialog.dismiss();progressDialog=null;}
                    }
                }
                @Override
                public void onFailure(Call<DtoUserTokenCredentials> call, Throwable t) {
                    if(progressDialog!=null){progressDialog.dismiss();progressDialog=null;}
                    message(t.getMessage());
                }
            });

        }catch (Exception e){
            //dialog.dismiss();
            message(e.getMessage());
            if(progressDialog!=null){progressDialog.dismiss();progressDialog=null;}
        }
    }

    private void message(String message)
    {
        messageBox = new AlertDialog.Builder(mContext);
        messageBox.setTitle("INFO");
        messageBox.setMessage(message);
        messageBox.setCancelable(false);
        messageBox.setNeutralButton("OK", null);
        messageBox.show();
    }

    private Activity getActivity(){
        return (Activity)mContext;
    }

    public void setOnSuccessfulListener(@NonNull AuthorizationListener l){
        mOnAuthorizationListenerListener =l;
    }

    public void Destroy(){
        if(progressDialog!=null && progressDialog.isShowing()){
            progressDialog.cancel();
            progressDialog.dismiss();
        }
    }
}

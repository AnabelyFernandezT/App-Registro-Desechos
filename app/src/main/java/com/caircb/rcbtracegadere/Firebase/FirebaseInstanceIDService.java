package com.caircb.rcbtracegadere.Firebase;

//import com.caircb.rcbtracegadere.Generic.MyAsyncTask;
//import com.caircb.rcbtracegadere.Helpers.MySession;
//import com.caircb.rcbtracegadere.Utils.WebRest;
import com.caircb.rcbtracegadere.generics.MyAsyncTask;
import com.caircb.rcbtracegadere.helpers.MySession;
import com.caircb.rcbtracegadere.services.WebService;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONObject;

/**
 * Created by jlsuarez on 03/08/2017.
 */

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    UserTokenRefreshTask userTokenRefreshTask;

    @Override
   // @android.support.annotation.WorkerThread
        public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        registerToken(token);
    }
    private void registerToken(String token) {
        if(token!=null) {
            if(MySession.isLocalStorage()) {
                MySession.setToken(token);
                if (MySession.getIdUsuarioToken() > 0) {
                    userTokenRefreshTask = new UserTokenRefreshTask();
                    userTokenRefreshTask.execute(token);
                }
            }
        }

    }

    private class UserTokenRefreshTask extends MyAsyncTask {

        @Override
        protected Boolean doInBackground() throws Exception {
            JSONObject dato = new JSONObject();
            dato.put("usuario", MySession.getIdUsuarioToken());
            dato.put("credencial", this.params[0]);

            //String resStr = WebService.SegPost("Login/registrarTokenRefresh/",dato.toString());
            /*if(resStr!=null){

            }*/
            return null;
        }
    }
}

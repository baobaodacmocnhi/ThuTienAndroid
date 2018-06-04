package vn.com.capnuoctanhoa.thutienandroid.Service;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.Class.CWebservice;

public class ServiceFirebaseInstanceID extends FirebaseInstanceIdService {
    String refreshedToken;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        //Getting registration token
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        SharedPreferences.Editor editor = CLocal.sharedPreferencesre.edit();
        editor.putString("UID", token);
        editor.commit();
        MyAsyncTask myAsyncTask=new MyAsyncTask();
        myAsyncTask.execute();
    }

    public class MyAsyncTask extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... voids) {
            CWebservice ws=new CWebservice();
            ws.updateUID(CLocal.sharedPreferencesre.getString("MaNV",""),CLocal.sharedPreferencesre.getString("UID",""));
            return null;
        }
    }
}



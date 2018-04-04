package vn.com.capnuoctanhoa.thutienandroid;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    String refreshedToken;
    SharedPreferences sharedPreferencesre;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        //Getting registration token
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();
    }

    public class MyAsyncTask extends AsyncTask<Void, Void, Void>
    {
        CWebservice ws = new CWebservice();
        @Override
        protected Void doInBackground(Void... voids) {
            sharedPreferencesre = getSharedPreferences(CLocal.FileName_Local, MODE_PRIVATE);
            ws.UpdateUID(sharedPreferencesre.getString("MaNV", ""),refreshedToken);
            return null;
        }
    }
}



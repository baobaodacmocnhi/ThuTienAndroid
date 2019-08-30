package vn.com.capnuoctanhoa.thutienandroid.Service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import com.google.gson.Gson;

import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;

public class ServiceAppKilled extends Service {
    public ServiceAppKilled() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        try {
            SharedPreferences.Editor editor = CLocal.sharedPreferencesre.edit();
            if (CLocal.listHanhThu != null)
//                editor.putString("jsonHanhThu", CLocal.jsonHanhThu.toString());
                editor.putString("jsonHanhThu", new Gson().toJsonTree(CLocal.listHanhThu).getAsJsonArray().toString());
            if (CLocal.listDongNuoc != null) {
//                editor.putString("jsonDongNuoc", CLocal.jsonDongNuoc.toString());
//                editor.putString("jsonDongNuocChild", CLocal.jsonDongNuocChild.toString());
                editor.putString("jsonDongNuoc", new Gson().toJsonTree(CLocal.listDongNuoc).getAsJsonArray().toString());
            }
            if (CLocal.jsonMessage != null)
                editor.putString("jsonMessage", CLocal.jsonMessage.toString());
            editor.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.stopSelf();
    }
}

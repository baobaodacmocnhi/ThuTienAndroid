package vn.com.capnuoctanhoa.thutienandroid.Service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

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
            if (CLocal.jsonHanhThu != null)
                editor.putString("jsonHanhThu", CLocal.jsonHanhThu.toString());
            if (CLocal.jsonDongNuoc != null) {
                editor.putString("jsonDongNuoc", CLocal.jsonDongNuoc.toString());
                editor.putString("jsonDongNuocChild", CLocal.jsonDongNuocChild.toString());
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

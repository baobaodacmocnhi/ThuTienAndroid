package vn.com.capnuoctanhoa.thutienandroid.Service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import vn.com.capnuoctanhoa.thutienandroid.CLocal;

public class ServiceAppKilled extends Service {
    public ServiceAppKilled() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        try {
            SharedPreferences.Editor editor = CLocal.sharedPreferencesre.edit();
            editor.putString("jsonHanhThu", CLocal.jsonHanhThu.toString());
            editor.putString("jsonDongNuoc", CLocal.jsonDongNuoc.toString());
            editor.commit();
        }catch (Exception ex){}
        this.stopSelf();
    }
}

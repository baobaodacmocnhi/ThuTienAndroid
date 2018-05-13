package vn.com.capnuoctanhoa.thutienandroid.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import vn.com.capnuoctanhoa.thutienandroid.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.DongNuoc.ActivityDanhSachDongNuoc;
import vn.com.capnuoctanhoa.thutienandroid.HanhThu.ActivityDanhSachHanhThu;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class ServiceFirebaseMessaging extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //Calling method to generate notification
        PendingIntent pendingIntent = null;
        if(remoteMessage.getData().get("Action").equals("HanhThu")) {
            CLocal.updateJSON(CLocal.jsonHanhThu,remoteMessage.getData().get("ID"),remoteMessage.getData().get("ActionDetail"),"true");
            Intent intent = new Intent(this, ActivityDanhSachHanhThu.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }
        else
        if(remoteMessage.getData().get("Action").equals("DongNuoc")) {
            CLocal.updateJSON(CLocal.jsonDongNuoc,remoteMessage.getData().get("ID"),remoteMessage.getData().get("ActionDetail"),"true");
            Intent intent = new Intent(this, ActivityDanhSachDongNuoc.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("body"))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(new long[]{0, 1000})
                .setContentIntent(pendingIntent);

        //Added defaults
//        int defaults = 0;
//        defaults |= android.app.Notification.DEFAULT_SOUND;
//        defaults |= android.app.Notification.DEFAULT_VIBRATE;
//        notificationBuilder.setDefaults(-1);

        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT_WATCH) {
            isScreenOn = powerManager.isInteractive();
        }
        if(isScreenOn==false)
            {
                PowerManager.WakeLock wl = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.ON_AFTER_RELEASE,"MyLock");
                wl.acquire();
                wl.release();
//                PowerManager.WakeLock wl_cpu = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyCpuLock");
//                wl_cpu.acquire(10000);
            }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Random random = new Random();
        int id = random.nextInt(9999 - 1000) + 1000;

        notificationManager.notify(id, notificationBuilder.build());
    }

}

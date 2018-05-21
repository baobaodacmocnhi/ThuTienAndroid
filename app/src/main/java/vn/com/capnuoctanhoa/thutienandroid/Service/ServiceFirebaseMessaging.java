package vn.com.capnuoctanhoa.thutienandroid.Service;

import android.app.Notification;
import android.app.NotificationChannel;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
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
        if (remoteMessage.getData().get("Action").equals("HanhThu")) {
            CLocal.updateJSON(CLocal.jsonHanhThu, remoteMessage.getData().get("ID"), remoteMessage.getData().get("ActionDetail"), "true");
            try {
                if( CLocal.jsonMessage==null)
                CLocal.jsonMessage=new JSONArray();
                JSONObject jsonObject = new JSONObject();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                jsonObject.put("NgayNhan", currentDate.format(new Date()));
                jsonObject.put("Title", remoteMessage.getData().get("title"));
                jsonObject.put("Content", remoteMessage.getData().get("body"));
                CLocal.jsonMessage.put(jsonObject);
            } catch (Exception ex) {
            }
            Intent intent = new Intent(this, ActivityDanhSachHanhThu.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        } else if (remoteMessage.getData().get("Action").equals("DongNuoc")) {
            CLocal.updateJSON(CLocal.jsonDongNuoc, remoteMessage.getData().get("ID"), remoteMessage.getData().get("ActionDetail"), "true");
            Intent intent = new Intent(this, ActivityDanhSachDongNuoc.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationBuilder = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = notificationManager.getNotificationChannel("ThuTienNotification_ID");
            if (mChannel == null) {
                mChannel = new NotificationChannel("ThuTienNotification_ID", "ThuTienNotification_Name", NotificationManager.IMPORTANCE_HIGH);
                mChannel.setDescription("ThuTienNotification_Des");
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{0, 1000});
                notificationManager.createNotificationChannel(mChannel);
            }
            notificationBuilder = new NotificationCompat.Builder(this, "ThuTienNotification_ID");

            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(remoteMessage.getData().get("title"))
                    .setContentText(remoteMessage.getData().get("body"))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setVibrate(new long[]{0, 1000})
                    .setContentIntent(pendingIntent);
        } else {
            notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(remoteMessage.getData().get("title"))
                    .setContentText(remoteMessage.getData().get("body"))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setVibrate(new long[]{0, 1000})
                    .setContentIntent(pendingIntent);
        }

        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT_WATCH) {
            isScreenOn = powerManager.isInteractive();
        }
        if (isScreenOn == false) {
            PowerManager.WakeLock wl = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyLock");
            wl.acquire();
            wl.release();
//                PowerManager.WakeLock wl_cpu = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyCpuLock");
//                wl_cpu.acquire(10000);
        }

        Random random = new Random();
        int id = random.nextInt(9999 - 1000) + 1000;

        notificationManager.notify(id, notificationBuilder.build());
    }

}

package vn.com.capnuoctanhoa.thutienandroid.Service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PowerManager;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import vn.com.capnuoctanhoa.thutienandroid.ActivityDangNhap;
import vn.com.capnuoctanhoa.thutienandroid.Class.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.Class.CWebservice;
import vn.com.capnuoctanhoa.thutienandroid.DongNuoc.ActivityDanhSachDongNuoc;
import vn.com.capnuoctanhoa.thutienandroid.HanhThu.ActivityDanhSachHanhThu;
import vn.com.capnuoctanhoa.thutienandroid.LenhHuy.ActivityLenhHuy;
import vn.com.capnuoctanhoa.thutienandroid.MainActivity;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class ServiceFirebaseMessaging extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        sendRegistrationToServer(s);
    }

    public void sendRegistrationToServer(String token) {
        SharedPreferences.Editor editor = CLocal.sharedPreferencesre.edit();
        editor.putString("UID", token);
        editor.commit();
        ServiceFirebaseMessaging.MyAsyncTask myAsyncTask = new ServiceFirebaseMessaging.MyAsyncTask();
        myAsyncTask.execute();
    }

    public class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            CWebservice ws = new CWebservice();
            ws.updateUID(CLocal.sharedPreferencesre.getString("MaNV", ""), CLocal.sharedPreferencesre.getString("UID", ""));
            return null;
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //Calling method to generate notification
//        Random random = new Random();
//        int UNIQUE_INT_VALUE_FOR_EVERY_CALL=random.nextInt(9999 - 1000) + 1000;;
        int UNIQUE_INT_VALUE_FOR_EVERY_CALL = 0;
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, UNIQUE_INT_VALUE_FOR_EVERY_CALL, intent, 0);

        //liên kết hàm [spSendNotificationToClient] sqlserver
        if (remoteMessage.getData().containsKey("Action"))
            if (remoteMessage.getData().get("Action").equals("DangXuat")) {
                CLocal.initialCLocal();
                intent = new Intent(this, ActivityDangNhap.class);
            } else if (remoteMessage.getData().get("Action").equals("HanhThu") && CLocal.listHanhThu != null && CLocal.listHanhThu.size() > 0) {
                //action HanhThu cập nhật GiaiTrach,TamThu,ThuHo cho HanhThu
                CLocal.updateValueChild(CLocal.listHanhThu, remoteMessage.getData().get("NameUpdate"), remoteMessage.getData().get("ValueUpdate"), remoteMessage.getData().get("ID"));
                CLocal.updateValueChild(CLocal.listHanhThuView, remoteMessage.getData().get("NameUpdate"), remoteMessage.getData().get("ValueUpdate"), remoteMessage.getData().get("ID"));
                intent = new Intent(this, ActivityDanhSachHanhThu.class);
            } else if (remoteMessage.getData().get("Action").equals("DongNuoc") && CLocal.listDongNuoc != null && CLocal.listDongNuoc.size() > 0) {
                //action DongNuoc cập nhật GiaiTrach,TamThu,ThuHo cho DongNuoc
                CLocal.updateValueChild(CLocal.listDongNuoc, remoteMessage.getData().get("NameUpdate"), remoteMessage.getData().get("ValueUpdate"), remoteMessage.getData().get("ID"));
                intent = new Intent(this, ActivityDanhSachDongNuoc.class);
            } else if (remoteMessage.getData().get("Action").equals("LenhHuy")) {
                intent = new Intent(this, ActivityLenhHuy.class);
            } else {
                intent = new Intent(this, MainActivity.class);
            }

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, UNIQUE_INT_VALUE_FOR_EVERY_CALL, intent, 0);
        try {
            if (CLocal.jsonMessage == null)
                CLocal.jsonMessage = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            jsonObject.put("NgayNhan", currentDate.format(new Date()));
            jsonObject.put("Title", remoteMessage.getData().get("Title"));
            jsonObject.put("Content", remoteMessage.getData().get("Body"));
            CLocal.jsonMessage.put(jsonObject);
        } catch (Exception ex) {
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
                    .setContentTitle(remoteMessage.getData().get("Title"))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getData().get("Body")))
                    .setContentText(remoteMessage.getData().get("Body"))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setVibrate(new long[]{0, 1000})
//                    .setGroup("vn.com.capnuoctanhoa.thutienandroid")
//                    .setGroupSummary(true)
                    .setContentIntent(pendingIntent);
        } else {
            notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(remoteMessage.getData().get("Title"))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getData().get("Body")))
                    .setContentText(remoteMessage.getData().get("Body"))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setVibrate(new long[]{0, 1000})
//                    .setGroup("vn.com.capnuoctanhoa.thutienandroid")
//                    .setGroupSummary(true)
                    .setContentIntent(pendingIntent);
        }

        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT_WATCH) {
            isScreenOn = powerManager.isInteractive();
        }
        if (isScreenOn == false) {
            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyLock");
            wl.acquire();
            wl.release();
//                PowerManager.WakeLock wl_cpu = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyCpuLock");
//                wl_cpu.acquire(10000);
        }

        Random random = new Random();
        int id = random.nextInt(9999 - 1000) + 1000;
//        int id = 1000;
        notificationManager.notify(id, notificationBuilder.build());
    }

}



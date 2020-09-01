package vn.com.capnuoctanhoa.thutienandroid.Class;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.ArrayList;
import java.util.List;

public class CMarshMallowPermission {
    public static final int APP_PERMISSIONS_REQUEST_CODE = 0;
    private String[] appPermissions = {Manifest.permission.CAMERA
            , Manifest.permission.ACCESS_FINE_LOCATION
            , Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.READ_PHONE_STATE};
    public static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1;
    public static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 2;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 3;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 4;
    public static final int REQUEST_INSTALL_PACKAGES_REQUEST_CODE = 5;
    public static final int REQUEST_READ_PHONE_STATE = 6;
    public static final int REQUEST_ACCESS_WIFI_STATE = 7;
    public static final int GRANTED = 0;
    public static final int DENIED = 1;
    public static final int NEVER = 2;
    private Activity activity;
    private Context mContext;

    public CMarshMallowPermission(Activity activity) {
        this.activity = activity;
        this.mContext = activity;
    }

    public boolean checkVersionMarshmallow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return true;
        else
            return false;
    }

    public boolean checkVersionQ() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            return true;
        else
            return false;
    }

    public boolean checkVersionO() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            return true;
        else
            return false;
    }

    public boolean checkAllPermissionForAPP() {
        if (checkVersionMarshmallow()) {
            //check which permissions are granted
            List<String> lstPermissionsNeed = new ArrayList<>();
            for (String perm : appPermissions)
                if (ContextCompat.checkSelfPermission(mContext, perm) != PackageManager.PERMISSION_GRANTED)
                    lstPermissionsNeed.add(perm);

            //ask for non-granted permissions
            if (lstPermissionsNeed.isEmpty() == false) {
                ActivityCompat.requestPermissions(activity, lstPermissionsNeed.toArray(new String[lstPermissionsNeed.size()]), APP_PERMISSIONS_REQUEST_CODE);
                return false;
            }

            return true;
        } else return false;
    }

    public boolean checkPermissionForInstallPackage() {
        if (checkVersionMarshmallow()) {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.REQUEST_INSTALL_PACKAGES) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else
                return false;
        } else return false;
    }

    public boolean checkPermissionForExternalStorage() {
        if (checkVersionMarshmallow()) {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else
                return false;
        } else
            return false;
    }

    public boolean checkPermissionForCamera() {
        if (checkVersionMarshmallow()) {
            int result = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA);
            if (result == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        } else
            return false;
    }

    public boolean checkPermissionForLocation() {
        if (checkVersionMarshmallow()) {
            int result = ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);
            if (result == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        } else
            return false;
    }

    public boolean checkPermissionForPhoneState() {
        if (checkVersionMarshmallow()) {
            int result = ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE);
            if (result == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        } else
            return false;
    }

    public void requestPermissionForInstallPackage() {
        if (checkVersionMarshmallow()) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.REQUEST_INSTALL_PACKAGES)) {
                Toast.makeText(mContext.getApplicationContext(), "External Storage permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, REQUEST_INSTALL_PACKAGES_REQUEST_CODE);
            }
        }
    }

    public void requestPermissionForExternalStorage() {
        if (checkVersionMarshmallow()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(mContext.getApplicationContext(), "External Storage permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
            }
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(mContext.getApplicationContext(), "External Storage permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_REQUEST_CODE);
            }
        }
    }

    public void requestPermissionForCamera() {
        if (checkVersionMarshmallow()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
                Toast.makeText(mContext.getApplicationContext(), "Camera permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            }
        }
    }

    public void requestPermissionForLocation() {
        if (checkVersionMarshmallow()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                Toast.makeText(mContext.getApplicationContext(), "Location permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
    }

    public void requestPermissionForPhoneState() {
        if (checkVersionMarshmallow()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_PHONE_STATE)) {
                Toast.makeText(mContext.getApplicationContext(), "Phone permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
            }
        }
    }

    public int getPermissionStatus(Activity activity, String permission) {
        if (checkVersionMarshmallow()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return DENIED;
            } else {
                if (ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
                    return GRANTED;
                } else {
                    return NEVER;
                }
            }
        } else
            return 0;
    }

    // check if google play services is installed on the device
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Toast.makeText(activity,
                        "This device is supported. Please download google play services", Toast.LENGTH_LONG)
                        .show();
            } else {
                Toast.makeText(activity,
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
            }
            return false;
        }
        return true;
    }
}

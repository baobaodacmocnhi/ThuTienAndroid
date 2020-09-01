package vn.com.capnuoctanhoa.thutienandroid.Class;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;

import androidx.core.app.ActivityCompat;

public class CLocation implements LocationListener {
    Activity mActivity;
    CMarshMallowPermission cMarshMallowPermission;
    Location gps_loc;
    Location network_loc;
    Location final_loc;
    double longitude;
    double latitude;

    public CLocation(Activity activity) {
        mActivity = activity;
        cMarshMallowPermission = new CMarshMallowPermission(mActivity);
        getLocation();
    }

    public void getLocation() {
        try {
            if (cMarshMallowPermission.checkPermissionForLocation() == false)
                cMarshMallowPermission.requestPermissionForLocation();
            if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                cMarshMallowPermission.requestPermissionForLocation();
            }
            LocationManager locationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
            gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            network_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            final_loc = null;
            if (gps_loc != null) {
                final_loc = gps_loc;
                latitude = final_loc.getLatitude();
                longitude = final_loc.getLongitude();
            } else if (network_loc != null) {
                final_loc = network_loc;
                latitude = final_loc.getLatitude();
                longitude = final_loc.getLongitude();
            } else {
                latitude = 0.0;
                longitude = 0.0;
            }
        } catch (Exception ex) {
        }
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Location getFinal_loc() {
        return final_loc;
    }

    public void setFinal_loc(Location final_loc) {
        this.final_loc = final_loc;
    }

    @Override
    public void onLocationChanged(Location location) {
        final_loc = location;
        latitude = final_loc.getLatitude();
        longitude = final_loc.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

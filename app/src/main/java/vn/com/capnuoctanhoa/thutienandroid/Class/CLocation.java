package vn.com.capnuoctanhoa.thutienandroid.Class;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

public class CLocation implements LocationListener {
    private Activity mActivity;
    private CMarshMallowPermission cMarshMallowPermission;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private Location location;


    private LocationManager locationManager;

    private double longitude;
    private double latitude;
    private static final long MIN_TIME_FOR_UPDATE = 1000 * 60 * 0;
    private static final long MIN_DISTANCE_FOR_UPDATE = 5;


    public CLocation(Activity activity) {
        mActivity = activity;
        cMarshMallowPermission = new CMarshMallowPermission(mActivity);
        getLocation();
    }

    public String getLocation() {
        try {
            if (cMarshMallowPermission.checkPermissionForLocation() == false)
                cMarshMallowPermission.requestPermissionForLocation();
            if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                cMarshMallowPermission.requestPermissionForLocation();
            }
            locationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled) {
                CLocal.openGPSSettings(mActivity);
            } else {
                if (isGPSEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null)
                            return location.getLatitude() + "," + location.getLongitude();
                        else
                            return "";
                    }
                }
//                else if (isNetworkEnabled) {
//                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, this);
//                    if (locationManager != null) {
//                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                    }
//                }
            }
        } catch (Exception ex) {
        }
        return "";
    }

    LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
            setLocation(location);
            locationManager.removeUpdates(this);
            locationManager.removeUpdates(locationListenerNetwork);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            setLocation(location);
            locationManager.removeUpdates(this);
            locationManager.removeUpdates(locationListenerGps);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    public void setLocation(Location location) {
        this.location = location;
    }

    public double getLatitude() {
        if (this.location != null) {
            latitude = this.location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude() {
        if (this.location != null) {
            longitude = this.location.getLongitude();
        }
        return longitude;
    }

    @Override
    public void onLocationChanged(Location location) {
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

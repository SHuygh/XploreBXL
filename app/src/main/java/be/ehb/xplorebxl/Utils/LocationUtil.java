package be.ehb.xplorebxl.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import be.ehb.xplorebxl.View.Activities.MainActivity;

/**
 * Created by huyghstijn on 26/03/2018.
 */

public class LocationUtil {
    private MainActivity context;
    private LocationListener ll;
    private LocationManager lm;


    private static final LocationUtil ourInstance = new LocationUtil();

    public static LocationUtil getInstance() {
        return ourInstance;
    }

    private LocationUtil() {
    }


    /**returns last known location of user*/
    public Location getLocation() {
        Location location = null;
        if (lm != null && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            //if GPS provider hasn't gotten a new update take the one from network, if that doesn't work location is null
            if(location == null){
                location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

        }

        return location;
    }

    /**get distance between POI and user*/
    public float getDistance(Double latitude, Double longitude, Location location){
        Location loc_POI = new Location("location");
        loc_POI.setLatitude(latitude);
        loc_POI.setLongitude(longitude);
        float distance = location.distanceTo(loc_POI);

        distance = distance/1000;
        return distance;
    }

    //setup gps updates
    public void setupLocationServices(MainActivity context) {
        this.context = context;
        lm = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);

        ll = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
            }
        };

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            lm.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 1000, 5, ll);
        }
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(context, "No GPS LOCATION", Toast.LENGTH_LONG).show();
                    } else {
                        lm.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER, 5000, 10, ll);
                    }
                }
        }
    }

    //remove the updates when main activity is closed
    public void removeUpdates() {
        lm.removeUpdates(ll);
    }

}

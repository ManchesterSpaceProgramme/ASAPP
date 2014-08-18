package org.manchesterspaceprogramme.asapp.beacon;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.AndroidException;
import android.util.Log;

/**
 * Class to retrieve and hold our current position.
 *
 * Google maps uses the following format: "https://www.google.com/maps/place/@-2.2355508,53.4840335,21z"
 * latitude,longitude,zoom level.
 * For use with the emulator run: "telnet localhost 5554" and then "geo fix -2.2355508 53.4840335 120"
 * "geo fix -0.916221 53.991192  0"
 *
 * Created by dave on 04/07/14.
 */
public class GPSCoordinates implements LocationListener {

    private static final String GOOGLE_MAPS_URL = "https://www.google.com/maps/place/@%s,%s,%sz";

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    private Location location;

    private LocationManager locationManager;



    public GPSCoordinates(LocationManager locationManager) throws AndroidException {

        // Define the criteria how to select the location provider -> use
        // default
        this.locationManager = locationManager;

        // getting GPS status
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (isGPSEnabled) {
            Log.i("GPSCO-ORD","GPS Enabled");
            getGPSLocation();

        }
        if (isNetworkEnabled) {
            Log.i("GPSCO-ORD","Network Enabled");
            getNetworkLocation();
        }
        if (!isGPSEnabled && !isNetworkEnabled) {
            Log.i("GPSCO-ORD","No Location found");
        }

    }

    private void getGPSLocation() {
        if (location == null) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            Log.d("GPS Enabled", "GPS Enabled");
            if (locationManager != null) {
                location = locationManager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
    }

    private void getNetworkLocation() {

        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        Log.d("Network", "Network");
        if (locationManager != null) {
            location = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            this.location = location;
            Log.d("Location Found", location.getLatitude() + " " + location.getLongitude());
            locationManager.removeUpdates(this);
        }
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

    public String getGoogleMapsUrl(int zoom) {
        return String.format(GOOGLE_MAPS_URL,getLatitude(),getLongitude(), zoom);
    }

    public String getGoogleMapsUrl() {
        return getGoogleMapsUrl(21);
    }

    public String getLatitude() {
        if (location != null) {
            return String.valueOf(location.getLatitude());
        } else {
            throw new ExceptionInInitializerError("Location not obtained");
        }
    }

    public String getLongitude() {
        if (location != null) {
            return String.valueOf(location.getLongitude());
        } else {
            throw new ExceptionInInitializerError("Location not obtained");
        }
    }

    public String getAltitude() {
        if (location != null) {
            return String.valueOf(location.getAltitude());
        } else {
            throw new ExceptionInInitializerError("Location not obtained");
        }
    }

    public boolean isReady() {
        return location != null;
    }
}

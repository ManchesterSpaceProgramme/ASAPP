package org.manchesterspaceprogramme.asapp.beacon;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.AndroidException;

/**
 * Class to retrieve and hold our current position.
 *
 * Google maps uses the following format: "https://www.google.com/maps/place/@53.4840335,-2.2355508,21z"
 * latitude,longitude,zoom level.
 * For use with the emulator run: "telnet localhost 5554" and then "geo fix 53.4840335 -2.2355508 120"
 * "geo fix 53.991192 -0.916221 0"
 *
 * Created by dave on 04/07/14.
 */
public class GPSCoordinates implements LocationListener {

    private static final String GOOGLE_MAPS_URL = "https://www.google.com/maps/place/@%s,%s,%sz";

    private final Location location;

    public GPSCoordinates(LocationManager locationManager) throws AndroidException {

        locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, this);
        // Define the criteria how to select the location provider -> use
        // default
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);
        location = locationManager.getLastKnownLocation(provider);

        if (location == null) {
            throw new AndroidException("Unable to obtain location");
        }
    }


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

    public String getGoogleMapsUrl(int zoom) {
        return String.format(GOOGLE_MAPS_URL,getLongitude(),getLatitude(), zoom);
    }

    public String getGoogleMapsUrl() {
        return getGoogleMapsUrl(21);
    }

    public String getLatitude() {

        return String.valueOf(location.getLatitude());
    }

    public String getLongitude() {
        return String.valueOf(location.getLongitude());
    }

    public String getAltitude() {

        return String.valueOf(location.getAltitude());
    }
}

package org.manchesterspaceprogramme.asapp.beacon;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Class to retrieve and hold our current position.
 *
 * Google maps uses the following format: "https://www.google.com/maps/place/@53.484264,-2.236451,17z"
 * latitude,longitude,zoom level.
 * For use with the emulator run: "telnet localhost 5554" and then "geo fix 53.484264 -2.236451"
 *
 * Created by dave on 04/07/14.
 */
public class GPSCoordinates implements LocationListener {

    private static final String GOOGLE_MAPS_URL = "https://www.google.com/maps/place/@%s,%s,%sz";

    private String latitude;
    private String longitude;
    private String altitude;



    @Override
    public void onLocationChanged(Location location) {
        longitude = String.valueOf(location.getLongitude());
        latitude = String.valueOf(location.getLatitude());
        altitude = String.valueOf(location.getAltitude());
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
        return getGoogleMapsUrl(17);
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getAltitude() {
        return altitude;
    }
}

package org.manchesterspaceprogramme.asapp.beacon;

import android.location.Location;
import android.util.AndroidException;

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
public class GPSCoordinates {

    private static final String GOOGLE_MAPS_URL = "https://www.google.com/maps/place/@%s,%s,%sz";

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    private Location location;


    public GPSCoordinates(Location location) throws AndroidException {

        this.location = location;

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

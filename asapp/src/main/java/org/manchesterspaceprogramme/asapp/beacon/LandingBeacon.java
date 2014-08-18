package org.manchesterspaceprogramme.asapp.beacon;

import android.content.ContentResolver;
import android.location.LocationListener;
import android.location.LocationManager;
import android.telephony.SmsManager;
import android.util.AndroidException;
import android.util.Log;

import java.util.List;

/**
 * Class to send the messages.  Message will be sent using the current co-ordinates to every phone
 * number in the address book.
 *
 * Created by dave on 04/07/14.
 */
public class LandingBeacon {

    private static SmsManager smsManager = SmsManager.getDefault();

    private static LocationListener myLocation;

    /**
     * phone numbers
     */
    private static List<String> phoneNumbers;

    public LandingBeacon(ContentResolver cr, LocationManager locationManager, List<String> phoneNumbers) throws AndroidException {
        myLocation = new GPSCoordinates(locationManager);
        this.phoneNumbers = phoneNumbers;

    }

    public void sendSMSMessage() throws AndroidException {


        if (phoneNumbers.isEmpty()) {
            throw new AndroidException("Error could not send message no contacts set up");
        }

        String message = getLocationMessage();

        for (String phoneNumber : phoneNumbers) {
            Log.i("sendSMSMessage", String.format("Sending to %s: message: %s", phoneNumber, message));
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Log.i("sendSMSMessage", "MessageSent");
        }


    }


    private String getLocationMessage() throws AndroidException {

        if (((GPSCoordinates)myLocation).isReady()) {
            String myMapLocation = ((GPSCoordinates) myLocation).getGoogleMapsUrl();
            String altitude = ((GPSCoordinates) myLocation).getAltitude();
            String message = String.format("Payload altitude is: %s.  Current location is: %s",altitude,myMapLocation);

            return message;

        }

        throw new AndroidException("Location Not Found");
    }

}

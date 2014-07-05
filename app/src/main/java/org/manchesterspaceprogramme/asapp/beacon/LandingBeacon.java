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

    public static void sendSMSMessage(ContentResolver cr, LocationManager locationManager) throws AndroidException {

        String message = getLocationMessage(locationManager);
        List<String> phoneNumbers = AddressBookHandler.getPhoneNumbersFromContacts(cr);

        if (phoneNumbers.isEmpty()) {
            throw new AndroidException("Error could not send message no contacts set up");
        }

        String phoneNumber = phoneNumbers.get(0);
        Log.i("sendSMSMessage", String.format("Sending to %s: message: %s", phoneNumber, message));
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        Log.i("sendSMSMessage","MessageSent");


    }


    private static String getLocationMessage(LocationManager locationManager) throws AndroidException {

        LocationListener locator =  new GPSCoordinates(locationManager);
        String myMapLocation = ((GPSCoordinates)locator).getGoogleMapsUrl();
        String altitude = ((GPSCoordinates) locator).getAltitude();

        String message = String.format("Payload altitude is: %s.  Current location is: %s",altitude,myMapLocation);

        return message;
    }

}

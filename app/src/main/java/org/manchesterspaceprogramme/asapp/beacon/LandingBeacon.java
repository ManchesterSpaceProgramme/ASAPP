package org.manchesterspaceprogramme.asapp.beacon;

import android.telephony.SmsManager;
import android.util.Log;

/**
 * Class
 *
 * Created by dave on 04/07/14.
 */
public class LandingBeacon {



    public static void sendSMSMessage(String phoneNumber, String message) {

        Log.i("sendSMSMessage", String.format("Sending to %s: message: %s",phoneNumber,message));

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }




}

package org.manchesterspaceprogramme.asapp.beacon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.AndroidException;
import android.util.Log;

import com.commonsware.cwac.locpoll.LocationPollerResult;

import java.util.List;

/**
 * Created by dave on 14/07/14.
 */
public class LandingReceiver extends BroadcastReceiver {

    public static final String PHONE_NUMBERS = "PHONE_NUMBERS";

    /**
     * phone numbers <name,number>
     */
    private static  List<String> phoneNumbers;


    public LandingReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle b=intent.getExtras();
        Log.i("onReceive","received intent for location");
        LocationPollerResult locationResult = new LocationPollerResult(b);

        Location loc=locationResult.getLocation();
        String msg;

        if (loc==null) {
            loc=locationResult.getLastKnownLocation();

            if (loc==null) {
                msg=locationResult.getError();
            }
            else {
                msg="TIMEOUT, lastKnown="+loc.toString();
            }
        }
        else {
            msg=loc.toString();
            try {
                List<String> phoneNumbers = AddressBookHandler.getPhoneNumbersFromContacts(context);
                LandingBeacon beacon = new LandingBeacon(loc,phoneNumbers);
                beacon.sendSMSMessage();
            } catch (AndroidException ae) {
                Log.e("LandingService", "Error running landing beacon");
            }
        }

        if (msg==null) {
            msg="Invalid broadcast received!";
        }
    }


}

package org.manchesterspaceprogramme.asapp.beacon;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.AndroidException;
import android.util.Log;

import java.util.List;

/**
 * This class will run in the background and trigger SMS messages every so often.
 */
public class LandingService extends IntentService {

    private static LandingBeacon beacon;


    public LandingService() {
        super("LandingService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        ContentResolver cr = getContentResolver();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            List<String> phoneNumbers = intent.getStringArrayListExtra(LandingReceiver.PHONE_NUMBERS);
            LandingBeacon beacon = new LandingBeacon(cr, locationManager,phoneNumbers);
            beacon.sendSMSMessage();
        } catch (AndroidException ae) {
            Log.e("LandingService","Error running landing beacon");
        }


    }
}

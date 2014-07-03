package org.manchesterspaceprogramme.asapp.beacon;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * This class will run in the background and trigger SMS messages every so often.
 */
public class LandingService extends Service {


    public LandingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

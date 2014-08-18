package org.manchesterspaceprogramme.asapp.beacon;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dave on 14/07/14.
 */
public class LandingReceiver extends WakefulBroadcastReceiver {

    public static final String PHONE_NUMBERS = "PHONE_NUMBERS";

    private static final int ONE_SECOND = 1000;
    private static final int ONE_MINUTE = 60 * ONE_SECOND;
    private static final int FIVE_MINUTES = 5 * ONE_MINUTE;
    private static final int THIRTY_MINUTES = 60 * ONE_MINUTE;

    // The app's AlarmManager, which provides access to the system alarm services.
    private AlarmManager alarmMgr;
    // The pending intent that is triggered when the alarm fires.
    private PendingIntent alarmIntent;

    /**
     * phone numbers <name,number>
     */
    private static  List<String> phoneNumbers;


    public LandingReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, LandingService.class);
        service.putStringArrayListExtra(PHONE_NUMBERS, (ArrayList)phoneNumbers);

        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, service);
    }

    public void setAlarm(Context context, List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, LandingReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                         10 * ONE_SECOND,
                 AlarmManager.INTERVAL_HALF_HOUR, alarmIntent);
    }

    public void cancelAlarm(Context context) {
        // If the alarm has been set, cancel it.
        if (alarmMgr != null) {
            alarmMgr.cancel(alarmIntent);
        }
    }
}

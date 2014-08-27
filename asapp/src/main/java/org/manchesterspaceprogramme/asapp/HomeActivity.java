package org.manchesterspaceprogramme.asapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.commonsware.cwac.locpoll.LocationPoller;
import com.commonsware.cwac.locpoll.LocationPollerParameter;

import org.manchesterspaceprogramme.asapp.beacon.AddressBookHandler;
import org.manchesterspaceprogramme.asapp.beacon.LandingReceiver;

import java.util.ArrayList;
import java.util.Map;


public class HomeActivity extends ActionBarActivity implements LocationListener {

    private static int counter;

    LandingReceiver landingReceiver = new LandingReceiver();

    private static final int ONE_SECOND = 1000;
    private static final int ONE_MINUTE = 60 * ONE_SECOND;
    private static final int TWO_MINUTES = 2 * ONE_MINUTE;
    private static final int FIVE_MINUTES = 5 * ONE_MINUTE;
    private static final int THIRTY_MINUTES = 30 * ONE_MINUTE;

    private static int period = THIRTY_MINUTES;

    // The app's AlarmManager, which provides access to the system alarm services.
    private AlarmManager mgr;
    // The pending intent that is triggered when the alarm fires.
    private PendingIntent pi;

    TextView status;
    Button btnStart, btnCancel;
    Handler locationBeaconHandler = new Handler();

    private Map<String,String> phoneNumbers;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setPeriod();
        updateGPSStatusText();

        phoneNumbers = AddressBookHandler.getPhoneNumbersFromContacts(getContentResolver());
        outputFoundContactsToScreen(phoneNumbers);

        btnStart = (Button) findViewById(R.id.start);
        btnCancel = (Button) findViewById(R.id.cancel);

        if (isAlarmUp()) {
            btnStart.setVisibility(View.GONE);
            btnCancel.setVisibility(View.VISIBLE);
            setAlarmStatusText(R.string.alarm_status_running);
        } else {
            btnStart.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.GONE);
            setAlarmStatusText(R.string.alarm_status_stopped);
        }

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Log.i("starting","user clicked start");

                isGPSEnable();
                setAlarm(new ArrayList(phoneNumbers.values()));
                btnStart.setVisibility(View.GONE);
                btnCancel.setVisibility(View.VISIBLE);
                setAlarmStatusText(R.string.alarm_status_running);
                Log.i("starting", "timer launched");

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                cancelAlarm(HomeActivity.this);
                btnCancel.setVisibility(View.GONE);
                btnStart.setVisibility(View.VISIBLE);
                setAlarmStatusText(R.string.alarm_status_stopped);
            }
        });
    }

    private void setAlarmStatusText(int resId) {
        TextView alarmStatus = (TextView)findViewById(R.id.alarm_status);
        alarmStatus.setText(resId);
    }

    private void outputFoundContactsToScreen(Map<String,String> phoneNumbers) {
        String[] contacts = new String[phoneNumbers.size()];

        int i=0;
        for (Map.Entry<String,String> contact : phoneNumbers.entrySet()) {
            contacts[i++] = String.format("%s (%s)",contact.getKey(),contact.getValue());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(HomeActivity.this,
                android.R.layout.simple_list_item_1, android.R.id.text1, contacts);

        ListView names = (ListView) findViewById(R.id.contacts);
        names.setAdapter(adapter);

    }

    public void setAlarm(ArrayList<String> phoneNumbers) {
        mgr=(AlarmManager)getSystemService(ALARM_SERVICE);

        Intent i=new Intent(this, LocationPoller.class);

        Bundle bundle = new Bundle();
        LocationPollerParameter parameter = new LocationPollerParameter(bundle);
        parameter.setIntentToBroadcastOnCompletion(new Intent(this, LandingReceiver.class));
        // try GPS and fall back to NETWORK_PROVIDER
        parameter.setProviders(new String[] {LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER});
        parameter.setTimeout(TWO_MINUTES);
        i.putExtras(bundle);

        pi= PendingIntent.getBroadcast(this, 0, i, 0);
        mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                period,
                pi);
        Log.i("setAlarm","Alarm active");
    }

    private void isGPSEnable(){
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        updateGPSStatusText();
    }

    private void updateGPSStatusText() {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        TextView gpsStatus = (TextView)findViewById(R.id.gps_status);
        if (enabled) {
            gpsStatus.setText(R.string.gps_status_running);
        } else {
            gpsStatus.setText(R.string.gps_status_stopped);
        }
    }

    private void setPeriod() {
        if (BuildConfig.DEBUG) {
            period = FIVE_MINUTES;
        } else {
            period = THIRTY_MINUTES;
        }

        TextView periodStatus = (TextView)findViewById(R.id.broadcast_interval);
        String unformattedStatus = getText(R.string.broadcast_interval).toString();
        periodStatus.setText(String.format(unformattedStatus,period/ONE_MINUTE));
    }

    private boolean isAlarmUp() {
        return (getCurrentPendingIntent() != null);
    }


    private PendingIntent getCurrentPendingIntent() {
        return PendingIntent.getBroadcast(this, 0,
                new Intent(this, LocationPoller.class),
                PendingIntent.FLAG_NO_CREATE);
    }

    public void cancelAlarm(Context context) {
        // If the alarm has been set, cancel it.
        if (mgr != null) {
            mgr.cancel(pi);
            mgr.cancel(getCurrentPendingIntent());
        }
        Log.i("setAlarm","Alarm cancelled");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.active, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProviderEnabled(String s) {

    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


}

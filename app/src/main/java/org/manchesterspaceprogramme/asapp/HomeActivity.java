package org.manchesterspaceprogramme.asapp;

import android.content.ContentResolver;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.AndroidException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.manchesterspaceprogramme.asapp.beacon.LandingBeacon;


public class HomeActivity extends ActionBarActivity implements LocationListener {

    private static final int ONE_SECOND = 1000;
    private static final int ONE_MINUTE = 60 * ONE_SECOND;
    private static final int FIVE_MINUTES = 5 * ONE_MINUTE;
    private static final int THIRTY_MINUTES = 60 * ONE_MINUTE;

    private static int counter;


    TextView status;
    Button btnStart, btnCancel;
    Handler locationBeaconHandler = new Handler();

    Runnable locationBeaconRunnable = new Runnable() {
        @Override
        public void run() {
            Log.i("SMS", "sending SMS");

            ContentResolver cr = getContentResolver();
            LocationManager locationManager;
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            status = (TextView) findViewById(R.id.textView);
            try {
                LandingBeacon.sendSMSMessage(cr, locationManager);
                status.setText("message sent " + ++counter);
            } catch (AndroidException ae) {
                Log.e("LocationBeaconRunnable","Error sending message: ",ae);
                status.setText("Unable to send message " +ae);
            }

            locationBeaconHandler.postDelayed(this, ONE_MINUTE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnStart = (Button) findViewById(R.id.start);
        btnCancel = (Button) findViewById(R.id.cancel);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Log.i("starting","user clicked start");


                locationBeaconHandler.removeCallbacks(locationBeaconRunnable);

                locationBeaconHandler.postDelayed(locationBeaconRunnable, 0);
                Log.i("starting","timer launched");

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                locationBeaconHandler.removeCallbacks(locationBeaconRunnable);
            }
        });
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

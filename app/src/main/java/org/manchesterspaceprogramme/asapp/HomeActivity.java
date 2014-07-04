package org.manchesterspaceprogramme.asapp;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.manchesterspaceprogramme.asapp.beacon.GPSCoordinates;
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


            Log.i("SMS", "sent SMS");

            status = (TextView) findViewById(R.id.textView);
            status.setText("message sent " +counter++);

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

                sendMessage();
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

    private void sendMessage() {
        LocationManager locationManager;
        String provider;
        LocationListener locator =  new GPSCoordinates();


        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, locator);
        // Define the criteria how to select the location provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");

            locator.onLocationChanged(location);
            String myMapLocation = ((GPSCoordinates)locator).getGoogleMapsUrl();

            LandingBeacon.sendSMSMessage("01010101",myMapLocation);
        } else {
            Log.i("locating","Could not initialise location");
        }
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

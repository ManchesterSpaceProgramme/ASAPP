package org.manchesterspaceprogramme.asapp;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.manchesterspaceprogramme.asapp.beacon.AddressBookHandler;
import org.manchesterspaceprogramme.asapp.beacon.LandingReceiver;

import java.util.ArrayList;
import java.util.Map;


public class HomeActivity extends ActionBarActivity implements LocationListener {

    private static int counter;

    LandingReceiver landingReceiver = new LandingReceiver();

    TextView status;
    Button btnStart, btnCancel;
    Handler locationBeaconHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnStart = (Button) findViewById(R.id.start);
        btnCancel = (Button) findViewById(R.id.cancel);
        btnCancel.setVisibility(View.GONE);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Log.i("starting","user clicked start");
                Map<String,String> phoneNumbers = AddressBookHandler.getPhoneNumbersFromContacts(getContentResolver());
                outputFoundContactsToScreen(phoneNumbers);


                landingReceiver.setAlarm(HomeActivity.this,new ArrayList(phoneNumbers.values()));
                btnStart.setVisibility(View.GONE);
                btnCancel.setVisibility(View.VISIBLE);
                Log.i("starting","timer launched");

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                landingReceiver.cancelAlarm(HomeActivity.this);
                btnCancel.setVisibility(View.GONE);
                btnStart.setVisibility(View.VISIBLE);

            }
        });
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

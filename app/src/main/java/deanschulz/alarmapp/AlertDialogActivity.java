package deanschulz.alarmapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Dean Schulz on 11/26/2017.
 */

public class AlertDialogActivity extends Activity
{
    AlertDialog alert;
    Handler mHandler = new Handler();
    String cur;
    String last;
    String longitude,lattitude;
    LocationManager locationManager2;
    int count;
    private static final int REQUEST_LOCATION = 1;
    AlarmManager alarmManager = AlarmManager.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Location Alarm")
                .setMessage("Get up and move around!")
                .setCancelable(false);

        alert = builder.create();
        alert.show();
        cur = getIntent().getStringExtra("cur");
        last = getIntent().getStringExtra("last");
        count = 0;
        Log.i("count", String.valueOf(count));
        ActivityCompat.requestPermissions(AlertDialogActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        locationManager2 = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager2.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else if (locationManager2.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            getLocation();
        }
        if(!alarmManager.isLocationChanged()) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(!alarmManager.isLocationChanged()) {
                        Log.i("count", String.valueOf(count++));
                        Log.i("cur", cur);
                        Log.i("last", last);
                        cur = alarmManager.getLocation();

                        if (!cur.equals(last)) {
                            Log.i("finished", "!");
                            alert.dismiss();
                            alarmManager.setLocationChanged(true);
                            finish();
                            mHandler.removeCallbacks(this);
                        }
                        getLocation();
                        mHandler.postDelayed(this, 1000);
                    }
                }},1000);

        }
    }
    private void getLocation() {
        locationManager2 = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(AlertDialogActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(AlertDialogActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager2.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager2.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager2.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                alarmManager.setLocation(lattitude + "," + longitude);
                Log.i("setLocation", lattitude + "," + longitude);

            } else if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                alarmManager.setLocation(lattitude + "," + longitude);
                Log.i("setLocation2", lattitude + "," + longitude);

            } else if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                alarmManager.setLocation(lattitude + "," + longitude);
                Log.i("setLocation3", lattitude + "," + longitude);

            } else {

                Toast.makeText(AlertDialogActivity.this, "Unable to Trace your location", Toast.LENGTH_SHORT).show();

            }
        }
    }
    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(AlertDialogActivity.this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

}


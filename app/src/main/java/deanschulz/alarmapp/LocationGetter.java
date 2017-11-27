package deanschulz.alarmapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Dean Schulz on 11/25/2017.
 */

public class LocationGetter extends AppCompatActivity {
    public Context mContext;
    public Activity activity;
    String longitude;
    String lattitude;
    AlarmManager alarmManager = AlarmManager.getInstance();
    LocationManager locationManager2;
    private static final int REQUEST_LOCATION = 1;

    public LocationGetter(){ }
    public LocationGetter(Context context){
        mContext = context;
    }
    public LocationGetter(final Context context, final Activity activity){
        mContext = context;
        this.activity = activity;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_location);
        ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
        locationManager2 = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager2.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else if (locationManager2.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            getLocation();
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

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
                Log.i("setLocation", lattitude + "," + longitude);


            } else if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                alarmManager.setLocation(lattitude + "," + longitude);
                Log.i("setLocation", lattitude + "," + longitude);

            } else {

                Toast.makeText(mContext, "Unable to Trace your location", Toast.LENGTH_SHORT).show();

            }
        }
    }
    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
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


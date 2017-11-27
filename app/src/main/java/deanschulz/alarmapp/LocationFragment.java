package deanschulz.alarmapp;

import android.Manifest;
import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.TimeZone;

import static android.content.Context.LOCATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    LocationListener listener;
    private static final int REQUEST_LOCATION = 1;
    EditText minutes;
    TextView countdown;
    AlarmManager alarmManager = AlarmManager.getInstance();
    String currentLocation;
    String longitude;
    String lattitude;
    String last, cur;
    Location location;
    boolean canGetLocation;
    LocationManager locationManager;
   // Location myLocation = getLastKnownLocation();

    Button TimeZoneButton;
    TextView TimeZoneTextview;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static LocationFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt("argsInstance", instance);
        LocationFragment firstFragment = new LocationFragment();
        firstFragment.setArguments(args);
        return firstFragment;
    }
    public LocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationFragment newInstance(String param1, String param2) {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.fragment_location, container, false);

        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);

        minutes = (EditText) myFragmentView.findViewById(R.id.minutes);
        countdown = (TextView) myFragmentView.findViewById(R.id.countdown);
        FloatingActionButton myFab = (FloatingActionButton) myFragmentView.findViewById(R.id.floatingActionButton);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();
                } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    getLocation();
                    MainActivity main = new MainActivity();
                    String newItem = "Location: " + minutes.getText().toString()+" minutes";
                    main.setArray(newItem);
                    main.setDrawer();
                    alarmManager.setLastLocation(alarmManager.getLocation());
                }


            }
        });
        return myFragmentView;
    }
    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Log.i("else statement", ".......");

            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                last = lattitude + "," + longitude;
                cur = lattitude + "," + longitude;
                alarmManager.setLocation(last);
                Log.i("setLocation", lattitude + "," + longitude);

            } else if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                last = lattitude + "," + longitude;
                cur = lattitude + "," + longitude;
                alarmManager.setLocation(cur);
                Log.i("setLocation", lattitude + "," + longitude);


            } else if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                last = lattitude + "," + longitude;
                cur = lattitude + "," + longitude;
                alarmManager.setLocation(cur);
                Log.i("setLocation", lattitude + "," + longitude);

            } else {

                Toast.makeText(getContext(), "Unable to Trace your location", Toast.LENGTH_SHORT).show();

            }
        }
        long duration;
        if(minutes.getText().toString().length() == 0)
            duration = 2*60*1000;
        else
            duration = Integer.parseInt(minutes.getText().toString())*1*1000;
        new CountDownTimer(duration, 1000) {

            public void onTick(long millisUntilFinished) {
                countdown.setText("seconds remaining: " + millisUntilFinished / 1000);
                //check if location changed, if yes, change boolean
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
                locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();
                }
                getLastKnownLocation();
                currentLocation = cur;
                Log.i("last location!!!!!", last);
                if(!currentLocation.equals(last)) {
                    alarmManager.setLocationChanged(true);
                }
            }

            public void onFinish() {
                Intent intent = new Intent(getContext(), AlarmAct.class);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.putExtra("selection", 3);
                intent.putExtra("cur", cur);
                intent.putExtra("last", last);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                android.app.AlarmManager am = (android.app.AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                am.setExact(android.app.AlarmManager.RTC_WAKEUP, 0, pendingIntent);
                countdown.setText("");
            }

        }.start();
        Toast toast = Toast.makeText(getContext(), "Location alarm set", Toast.LENGTH_SHORT);
        toast.show();
    }
//    private Location getLastKnownLocation() {
//        mLocationManager = (LocationManager)getContext().getSystemService(LOCATION_SERVICE);
//        List<String> providers = mLocationManager.getProviders(true);
//        Location bestLocation = null;
//        for (String provider : providers) {
//            Location l = mLocationManager.getLastKnownLocation(provider);
//            if (l == null) {
//                continue;
//            }
//            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
//                // Found best last known location: %s", l);
//                bestLocation = l;
//            }
//        }
//        return bestLocation;
//    }
private void getLastKnownLocation() {
    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

    } else {
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

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

            Toast.makeText(getContext(), "Unable to Trace your location", Toast.LENGTH_SHORT).show();

        }
    }
}
    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            //When fragment openned
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

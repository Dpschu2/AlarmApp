package deanschulz.alarmapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.*;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.app.NotificationManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.content.PermissionChecker;
import android.support.v4.view.GravityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.ncapdevi.fragnav.FragNavController;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private BottomBar mBottomBar;
    private FragNavController fragNavController;
    Toolbar toolbar = null;
    public Bundle savedInstanceState = null;
    LocationManager locationManager;
    LocationListener listener;
    private static final int REQUEST_LOCATION = 1;
    public boolean check = false;
    Handler mHandler = new Handler();
    String last;
    String cur;
    AlertDialog alertDialog;
    AlertDialog.Builder builder = null;

    //indices to fragments
    private final int TAB_FIRST = FragNavController.TAB1;
    private final int TAB_SECOND = FragNavController.TAB2;
    private final int TAB_THIRD = FragNavController.TAB3;
    public DrawerBuilder drawerBuilder;
    FragmentManager fm;
    AlarmManager alarmManager = AlarmManager.getInstance();


    public void setArray(String newItem){
        alarmManager.setAlarm(newItem);
    }

    public void setDrawer(){
        //adds to alarmlist sidebar
        alarmManager.getDrawer().addDrawerItems(new SectionDrawerItem().withName(alarmManager.getAlarmList().get(alarmManager.getSize()-1)).withDivider(true));
        Log.i("name", alarmManager.getAlarmList().get(alarmManager.getSize()-1));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.root, new DialogFragment());
        fragmentTransaction.commit();

        List<Fragment> fragments = new ArrayList<>(3);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !notificationManager.isNotificationPolicyAccessGranted()) {
            Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(intent);
        }
        //add fragments to list
        fragments.add(AlarmFragment.newInstance(0));
        fragments.add(TimerFragment.newInstance(0));
        fragments.add(LocationFragment.newInstance(0));

        //link fragments to container
        fragNavController = new FragNavController(getSupportFragmentManager(),R.id.container,fragments);
        //End of FragNav

        //BottomBar menu
        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setItems(R.menu.bottombar_menu);
        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                //switch between tabs
                switch (menuItemId) {
                    case R.id.bottomBarItemOne:
                        fragNavController.switchTab(TAB_FIRST);
                        break;
                    case R.id.bottomBarItemSecond:
                        fragNavController.switchTab(TAB_SECOND);
                        break;
                    case R.id.bottomBarItemThird:
                        fragNavController.switchTab(TAB_THIRD);
                        break;
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottomBarItemOne) {
                    fragNavController.clearStack();
                }
            }
        });

        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        //End of BottomBar menu

        //Navigation drawer
        new DrawerBuilder().withActivity(this).build();

        //primary items
        PrimaryDrawerItem home = new PrimaryDrawerItem()
                .withIdentifier(1)
                .withName(R.string.app_list)
                .withIcon(R.drawable.ic_home_black_24dp);


        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
        this.savedInstanceState = savedInstanceState;
        drawerBuilder = new DrawerBuilder();
            drawerBuilder.withActivity(this);
            drawerBuilder.withToolbar(toolbar);
            drawerBuilder.withActionBarDrawerToggleAnimated(true);
            drawerBuilder.withTranslucentStatusBar(false);
            drawerBuilder.withFullscreen(true);
            drawerBuilder.withSavedInstance(savedInstanceState);
            drawerBuilder.addDrawerItems(home);
            drawerBuilder.build();
        alarmManager.setDrawer(drawerBuilder);
        //End of Navigation drawer

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case REQUEST_LOCATION:
                new LocationGetter(MainActivity.this, MainActivity.this);
                break;
        }
    }
    public void resetLocation(){
        new LocationGetter(MainActivity.this, MainActivity.this);
    }
    public void runLocationDetector(){
        //builder = new AlertDialog.Builder(MainActivity.this);
        alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        last = alarmManager.getLastLocation();
        if(!alarmManager.isLocationChanged()) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
//            } else {
//                builder = new AlertDialog.Builder(MainActivity.this);
//            }
            alertDialog.setTitle("Location Alarm");
            alertDialog.setMessage("Get up and walk!");
            alertDialog.setIcon(R.drawable.ic_transfer_within_a_station_black_24dp);
            //alertDialog = builder.create();
            alertDialog.show();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MainActivity mainActivity = new MainActivity();
                    mainActivity.resetLocation();
                    cur = alarmManager.getLocation();
                    if(!cur.equals(last)){
                        alertDialog.cancel();
                    }
                    last = cur;
                    mHandler.postDelayed(this, 1000);
                }},1000);

        }
    }

    public void createNotificationDialog() {
        fm = getSupportFragmentManager();
        DFragment notificationDialog = DFragment.newInstance(null);
        notificationDialog.show(fm, "fragment_edit_name");
    }
    public void createNotificationDialog2() {
        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Get up and walk around!");
        AlertDialog alert = builder.create();
        alert.show();
    }
    @Override
    public void onBackPressed() {
        if (fragNavController.getCurrentStack().size() > 1) {
            fragNavController.pop();
        } else {
            super.onBackPressed();
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }

}

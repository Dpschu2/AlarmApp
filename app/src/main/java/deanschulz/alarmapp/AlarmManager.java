package deanschulz.alarmapp;

import android.content.Context;
import android.location.Location;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Dean Schulz on 11/21/2017.
 */

public final class AlarmManager {
    private static AlarmManager mInstance;
    public ArrayList<String> alarmList = new ArrayList<String>();
    public DrawerBuilder drawer;
    public String location;
    public String lastLocation;
    public boolean locationChanged = false;
    private AlarmManager() {}

    public static AlarmManager getInstance() {
        if(mInstance == null) {
            mInstance = new AlarmManager();
        }
        return mInstance;
    }
    public boolean isLocationChanged(){
        return locationChanged;
    }
    public void setLocationChanged(boolean changed){
        locationChanged = changed;
    }
    public void setLocation(String location){
        this.location = location;
    }

    public DrawerBuilder getDrawer(){
        return drawer;
    }
    public String getLocation(){
        return location;
    }
//    public String getCurLocation(Location location) {
//        //LocationFragment locationFragment = new LocationFragment();
//        MainActivity mainActivity = new MainActivity();
//        return mainActivity.getLocation(location);
//    }
    public String getLastLocation(){
        return lastLocation;
    }
    public void setLastLocation(String lastLocation){
        this.lastLocation = lastLocation;
    }
    public void setDrawer(DrawerBuilder drawer2){
        drawer = drawer2;
    }
    public void setAlarm(String newAlarm){
        alarmList.add(newAlarm);
    }
    public ArrayList<String> getAlarmList(){
        return alarmList;
    }
    public int getSize(){
        return alarmList.size();
    }
}

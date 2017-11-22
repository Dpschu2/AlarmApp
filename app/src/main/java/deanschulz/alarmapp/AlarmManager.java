package deanschulz.alarmapp;

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
    private AlarmManager() {}
    public AlarmManager getmInstance(){
        return mInstance;
    }
    public static AlarmManager getInstance() {
        if(mInstance == null) {
            mInstance = new AlarmManager();
        }
        return mInstance;
    }
    public DrawerBuilder getDrawer(){
        return drawer;
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

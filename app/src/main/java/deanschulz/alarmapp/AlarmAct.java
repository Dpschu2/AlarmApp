package deanschulz.alarmapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;

import javax.xml.transform.Result;

/**
 * Created by Dean Schulz on 11/19/2017.
 * What happens when the alarm goes off
 */

public class AlarmAct extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.print("worked");
        String location = intent.getStringExtra("location");
        String message = intent.getStringExtra("message");
        int selection = intent.getIntExtra("selection", 1);
        if(selection == 1) {
            Notification noti = new Notification.Builder(context)
                    .setContentTitle("AlarmApp")
                    .setContentText(message + "\nLocation: " + location)
                    .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                    .build();
        }
        else if(selection == 2) {
            Notification noti2 = new Notification.Builder(context)
                    .setContentTitle("AlarmApp")
                    .setContentText(message + "\nLocation: " + location)
                    .setSmallIcon(R.drawable.ic_hourglass_empty_black_24dp)
                    .build();
        }
        else if(selection == 3) {
            Notification noti3 = new Notification.Builder(context)
                    .setContentTitle("AlarmApp")
                    .setContentText(message + "\nLocation: " + location)
                    .setSmallIcon(R.drawable.ic_transfer_within_a_station_black_24dp)
                    .build();
        }
    }
}

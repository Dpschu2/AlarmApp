package deanschulz.alarmapp;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;

import javax.xml.transform.Result;

/**
 * Created by Dean Schulz on 11/19/2017.
 * What happens when the alarm goes off
 */

public class AlarmAct extends BroadcastReceiver
{

   AlertDialog.Builder builder;
   AlarmManager alarmManager = AlarmManager.getInstance();
    String last;
    String cur;
    public Context mContext;
    AlertDialog alertDialog;



    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d("broadcast receiver","worked");
        mContext = context;
        builder = new AlertDialog.Builder(context);
        alertDialog = new AlertDialog.Builder(context).create();
        PowerManager pm = (PowerManager) context.getApplicationContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
        wakeLock.acquire();
        cur = intent.getStringExtra("cur");
        last = intent.getStringExtra("last");
        String location = intent.getStringExtra("location");
        String message = intent.getStringExtra("message");
        int selection = intent.getIntExtra("selection", 1);
        NotificationCompat.Builder mBuilder = null;
        if(selection == 1) {
            mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                            .setContentTitle(message)
                            .setContentText("Location: " + location)
                            .setAutoCancel(true)
                            .setDefaults(-1);
        }
        else if(selection == 2) {
            mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_hourglass_empty_black_24dp)
                            .setContentTitle(message)
                            .setContentText("Location: " + location)
                            .setAutoCancel(true)
                            .setDefaults(-1);
        }
        else if(selection == 3) {
            Intent i = new Intent(context, AlertDialogActivity.class);
            i.putExtra("cur",cur);
            Log.i("last", last);
            i.putExtra("last",last);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }

        if(selection == 1 || selection == 2) {
            android.support.v4.app.TaskStackBuilder stackBuilder = android.support.v4.app.TaskStackBuilder.create(context);
            Intent resultIntent = new Intent(context, MainActivity.class);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(0, mBuilder.build());
            wakeLock.release();
        }
    }


}

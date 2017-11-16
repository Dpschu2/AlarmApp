package deanschulz.alarmapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class TimerActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_home:
                    Intent i = new Intent(TimerActivity.this, MainActivity.class);
                    startActivity(i);
                    return true;
                case R.id.navigation_dashboard:
                    Intent i2 = new Intent(TimerActivity.this, TimerActivity.class);
                    startActivity(i2);
                    return true;
                case R.id.navigation_notifications:
                    Intent i3 = new Intent(TimerActivity.this, LocationActivity.class);
                    startActivity(i3);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_main);
        System.out.print("timer");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.timer);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
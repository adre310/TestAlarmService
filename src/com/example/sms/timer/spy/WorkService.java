package com.example.sms.timer.spy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

public class WorkService extends Service {

    public static final int INTERVAL = 15000; // 60 sec
    public static final int FIRST_RUN = 5000; // 5 seconds
    int REQUEST_CODE = 11223344;

    AlarmManager alarmManager;

    @Override
    public void onCreate() {
        super.onCreate();

        startService();
        Log.v(this.getClass().getName(), "onCreate(..)");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.v(this.getClass().getName(), "onBind(..)");
        return null;
    }

    @Override
    public void onDestroy() {
        if (alarmManager != null) {
            Intent intent = new Intent(this, AlarmService.class);
            alarmManager.cancel(PendingIntent.getBroadcast(this, REQUEST_CODE, intent, 0));
        }
        Log.v(this.getClass().getName(), "Service onDestroy(). Stop AlarmManager at " + new java.sql.Timestamp(System.currentTimeMillis()).toString());
    }

    private void startService() {

        Intent intent = new Intent(this, AlarmService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, REQUEST_CODE, intent, 0);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + FIRST_RUN,
                INTERVAL,
                pendingIntent);

        Log.v(this.getClass().getName(), "AlarmManger started at " + new java.sql.Timestamp(System.currentTimeMillis()).toString());
    }
}

package com.aalto.precious.sensing;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class PeriodicUpdateReceiver extends BroadcastReceiver {

    private static final String TAG = "PeriodicUpdateReceiver";
    private static final String INTENT_ACTION = "com.aalto.precious.sensing.PERIODIC_UPDATE_TASK";
    private NodePollingService nodePollingService;


    public PeriodicUpdateReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(INTENT_ACTION)) {
            System.out.println("Alarm Fired \nRestartiing PeriodicUpdate");
            Intent pollingServiceIntent = new Intent(context, NodePollingService.class);
            context.startService(pollingServiceIntent);
        }
    }


    public void reset(Context context) {
        System.out.println("BroadcastRecevier Reset Called");
        Intent alarmIntent = new Intent(context, PeriodicUpdateReceiver.class);
        alarmIntent.setAction(INTENT_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        manager.set(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + (1000 * 5), pendingIntent);
    }

}


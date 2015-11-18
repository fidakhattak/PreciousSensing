package com.aalto.precious.sensing;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class PeriodicBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "BroadcastReceiver";
    //    private static final String INTENT_ACTION = "com.aalto.precious.sensing.PERIODIC_UPDATE_TASK";
    private static final String INTENT_ACTION = "com.aalto.precious.sensing.POLLING_SERVICE_UPDATE";
    private static final int SECOND = 1000;
    //private Context context;

    public PeriodicBroadcastReceiver() {
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        // if (intent.getAction().equals(INTENT_ACTION)) {

        Log.i(TAG, intent.getAction());
            Log.i(TAG, "Alarm Fired \nRestartiing PeriodicUpdate");
            Intent pollingServiceIntent = new Intent(context, BackgroundService.class);
            context.startService(pollingServiceIntent);
        // }
    }


    public void reset(Context context) {
        Log.i(TAG, "BroadcastRecevier Reset Called");
        Intent broadcastIntent = new Intent(context, PeriodicBroadcastReceiver.class);
        broadcastIntent.setAction(INTENT_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (pendingIntent == null)
            Log.i(TAG, "PendingIntent Null");
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + (20 * SECOND), pendingIntent);
        Log.i(TAG, "AlarmManager Set");
    }

}


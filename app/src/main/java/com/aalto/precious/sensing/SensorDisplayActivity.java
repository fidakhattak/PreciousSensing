package com.aalto.precious.sensing;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by fida on 7.11.2015.
 */
public class SensorDisplayActivity extends Activity {

    private static final String TAG = "Sensor Display Activity";
    private static BackgroundService backgroundService;
    private boolean mbound = false;
    private int mInterval = 5000;
    private Handler mHandler;
    private WeatherStation currentWeatherStation;
    Runnable showOnScreen = new Runnable() {
        @Override
        public void run() {
            displaySensors(); //this function can change value of mInterval.
            mHandler.postDelayed(showOnScreen, mInterval);
        }
    };
    protected ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mbound = true;
            BackgroundService.LocalBinder binder = (BackgroundService.LocalBinder) service;
            backgroundService = binder.getService();
            startRepeatingTask();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mbound = false;
            stopRepeatingTask();
        }
    };
    private ArrayList<Sensor> onlineSensorsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        setContentView(R.layout.activity_sensor_display);
    }

    protected void onStart() {
        super.onStart();

        Intent pollingService = new Intent(getApplicationContext(), BackgroundService.class);
        if (!isMyServiceRunning(BackgroundService.class))
            startService(pollingService);
        bindService(pollingService, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        unbindService(serviceConnection);
        super.onStop();
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    void startRepeatingTask() {

        showOnScreen.run();
    }

    void stopRepeatingTask() {

        mHandler.removeCallbacks(showOnScreen);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {

        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void displaySensors() {
        //      String uri = getIntent().getStringExtra("uri");
        currentWeatherStation = backgroundService.getCurrentWeatherStation();
        if (currentWeatherStation != null) {
            ArrayList<Sensor> sensors = currentWeatherStation.getSensorList();
            if (sensors.size() != 0) {
                try {
                    TextView location = (TextView) findViewById(R.id.location);
                    location.setText("Connected to " + currentWeatherStation.getLocation());
                    SensorListAdapter adapter = new SensorListAdapter(getApplicationContext(), sensors);
                    ListView listView = (ListView) findViewById(R.id.SensorLayout);
                    listView.setAdapter(adapter);
                    registerForContextMenu(listView);
                } catch (NullPointerException e) {
                    Log.i(TAG, e.toString());
                }
            } else
                Log.i(TAG, "No Sensors Found");
        }
    }
}
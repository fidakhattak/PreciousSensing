package com.aalto.precious.sensing;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

public class BackgroundService extends Service implements SensorReadTaskCallback {

    private static final String TAG = BackgroundService.class.getSimpleName();
    private static final String ONLINE_NODES = "ONLINE_NODES";
    private static final int DATABASE_VERSION = 1;

    IBinder mBinder = new LocalBinder();
    PeriodicBroadcastReceiver boradcastReceiver = new PeriodicBroadcastReceiver();
    WeatherStationDataBase onLineWeatherStations = new WeatherStationDataBase(this, ONLINE_NODES, DATABASE_VERSION);
    UpnpScanManager upnpManager = new UpnpScanManager(this);
    WeatherStation currentWeatherStation;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Background Service Created");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service Started");
        if (currentWeatherStation != null) {
            Log.i(TAG, "CurrentWeatherStation exists");
            startSensorReadTask(currentWeatherStation.getUri());
        } else if (upnpManager != null) {
            upnpManager.startScan();
            Log.i(TAG, "UPNP Manager exists");
        }
        else {
            upnpManager = new UpnpScanManager(this);
            upnpManager.startScan();
            Log.i(TAG, "Starting UPNP Service");
        }
        boradcastReceiver.reset(this);
        return START_STICKY;
    }

    @Override
    public void sensorReadUpdate(WeatherStation station) {
        Log.i(TAG, "Update completed");
        this.currentWeatherStation = station;
        this.upnpManager.stopScan();
        //   stopSelf();
    }

    public void upnpScanUpdate(ArrayList<WeatherStation> weatherStations) {
        if (weatherStations.size() == 0) {
            onLineWeatherStations.updateTable(weatherStations);
            Log.i(TAG, "No weatherStations Found");
        }
        else {
            onLineWeatherStations.updateTable(weatherStations);
            currentWeatherStation = onLineWeatherStations.getRelevantNode();
            startSensorReadTask(currentWeatherStation.getUri());
        }
    }

    public WeatherStation getWeatherStation(String uri) {
        startSensorReadTask(uri);
        return currentWeatherStation;
    }

    public WeatherStation getCurrentWeatherStation() {
        return currentWeatherStation;
    }

    public ArrayList getAllWeatherStations() {
        return (ArrayList) onLineWeatherStations.getAllStations();
    }

    private void startSensorReadTask(String uri) {
        if (!uri.isEmpty()) {
            Uri nodeUri = Uri.parse(uri);
            SensorReadTask sensorReadTask = new SensorReadTask(this);
            sensorReadTask.execute(nodeUri);
        } else
            Log.i(TAG, "CurrentWeatherStation uri == null");
    }

    public void StopSensingService() {
        stopSelf();
    }

    public class LocalBinder extends Binder {
        public BackgroundService getService() {
            return BackgroundService.this;
        }
    }

}

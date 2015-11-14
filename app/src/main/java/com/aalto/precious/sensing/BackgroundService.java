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

    IBinder mBinder;
    PeriodicBroadcastReceiver boradcastReceiver;
    WeatherStationDataBase onLineWeatherStations;
    WeatherStation currentWeatherStation;
    UpnpScanManager upnpManager;


    @Override
    public void OnUpdateComplete(WeatherStation station) {
        Log.i(TAG, "Update completed");
        this.currentWeatherStation = station;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = new LocalBinder();
        boradcastReceiver = new PeriodicBroadcastReceiver();
        onLineWeatherStations = new WeatherStationDataBase(this, ONLINE_NODES, DATABASE_VERSION);
        upnpManager = new UpnpScanManager(this);
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
        if (currentWeatherStation != null) {
            Log.i(TAG, "CurrentWeatherStation exists");
            startSensorReadTask(currentWeatherStation);
        } else if (upnpManager != null)
            upnpManager.startUpnpService();
        else {
            upnpManager = new UpnpScanManager(this);
            upnpManager.startUpnpService();
        }
        boradcastReceiver.reset(BackgroundService.this);
        return START_STICKY;
    }

    public void updateWeatherStations(ArrayList<WeatherStation> weatherStations) {
        if (weatherStations.size() == 0)
            Log.i(TAG, "No weatherStations Found");
        else {
            onLineWeatherStations.updateTable(weatherStations);
            currentWeatherStation = onLineWeatherStations.getRelevantNode();
            startSensorReadTask(currentWeatherStation);
        }
    }

    public WeatherStation getCurrentWeatherStation() {
        return currentWeatherStation;
    }

    public ArrayList<WeatherStation> getAllWeatherStations() {
        return (ArrayList) onLineWeatherStations.getAllStations();
    }

    private void startSensorReadTask(WeatherStation node) {
        if (node.getUri() != null) {
            Uri nodeUri = Uri.parse(node.getUri());
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

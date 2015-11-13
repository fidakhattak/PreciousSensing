package com.aalto.precious.sensing;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;

import java.util.ArrayList;

public class NodePollingService extends Service implements SensorUpdateInterface {

    private static final String TAG = NodePollingService.class.getSimpleName();
    private static final String ONLINE_NODES = "ONLINE_NODES";
    private static final int DATABASE_VERSION = 1;

    IBinder mBinder = new LocalBinder();
    PeriodicUpdateReceiver boradcastReceiver = new PeriodicUpdateReceiver();
    PreciousServerDB onLineNodesDB;
    UpnpScanManager upnpManager;
    WeatherStation currentWeatherStation;

    @Override
    public void OnUpdateComplete(WeatherStation station) {
        System.out.println("Update completed");
        this.currentWeatherStation = station;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        onLineNodesDB = new PreciousServerDB(this, ONLINE_NODES, DATABASE_VERSION);
        upnpManager = new UpnpScanManager(this);
        System.out.println("NodePolling Service Created");
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
        System.out.println("This is the NodePolling Service");
        if (currentWeatherStation != null) {
            System.out.println("CurrentWeatherStation exists");
            ReadSensors(currentWeatherStation);
        } else if (upnpManager != null)
            upnpManager.startUpnpService();
        else {
            upnpManager = new UpnpScanManager(this);
            upnpManager.startUpnpService();
        }
        boradcastReceiver.reset(NodePollingService.this);
        return START_STICKY;
    }

    public void updateWeatherStations(ArrayList<WeatherStation> weatherStations) {
        if (weatherStations.size() == 0)
            System.out.println("No weatherStations Found");
        else {
            onLineNodesDB.updateTable(weatherStations);
            currentWeatherStation = onLineNodesDB.getRelevantNode();
            ReadSensors(currentWeatherStation);
        }
    }

    public WeatherStation getCurrentWeatherStation() {
        return currentWeatherStation;
    }

    public ArrayList<WeatherStation> getAllWeatherStations() {
        return (ArrayList) onLineNodesDB.getAllStations();
    }

    private void ReadSensors(WeatherStation node) {
        if (node.getUri() != null) {
            Uri nodeUri = Uri.parse(node.getUri());
            ReadTaskParameters taskParameters = new ReadTaskParameters(nodeUri, this);
            SensorReadTask sensorReadTask = new SensorReadTask(this);
            sensorReadTask.execute(taskParameters);
        } else
            System.out.println("currentweatherstation uri == null");
    }

    public void StopSensingService() {
        stopSelf();
    }

    public class LocalBinder extends Binder {
        public NodePollingService getService() {
            return NodePollingService.this;
        }
    }
}

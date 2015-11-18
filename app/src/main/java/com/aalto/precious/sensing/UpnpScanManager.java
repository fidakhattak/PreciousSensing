package com.aalto.precious.sensing;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import org.teleal.cling.android.AndroidUpnpService;
import org.teleal.cling.android.AndroidUpnpServiceImpl;
import org.teleal.cling.model.meta.Device;
import org.teleal.cling.registry.Registry;

import java.util.ArrayList;

/**
 * Created by fida on 4.11.2015.
 */

public class UpnpScanManager {
    private static String TAG = "UPNP_SCAN_MANAGER";
    private Context context;
    private AndroidUpnpService upnpService;
    private ServiceConnection serviceConnection;
    private UpnpRegistryListener registryListener;

    public UpnpScanManager(Context context) {
        this.context = context;
        this.registryListener = new UpnpRegistryListener();
    }

    public boolean startScan() {
        StartServiceConnection();
        doBind();
        return false;
    }

    public void stopScan() {
        if (upnpService != null) {
            boolean isBound;
            isBound = context.bindService(new Intent(context, AndroidUpnpServiceImpl.class), serviceConnection, Context.BIND_AUTO_CREATE);
            if (isBound) {
                upnpService.getRegistry().removeListener(registryListener);
                context.unbindService(serviceConnection);
            }
        }
    }

    private void StartServiceConnection() {
        serviceConnection = new ServiceConnection() {

            public void onServiceConnected(ComponentName className, IBinder service) {
                upnpService = (AndroidUpnpService) service;
                upnpService.getRegistry().addListener(registryListener);
                // Search asynchronously for all devices, they will respond soon
                upnpService.getControlPoint().search();
                // Sleep for 2 seconds while all devices get discovered
                try {
                    Thread.sleep(2 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                updateStationList(upnpService.getRegistry());
                //  stopScan();
            }

            public void onServiceDisconnected(ComponentName className) {
                stopScan();
            }
        };
    }

    private void updateStationList(Registry registry) {
        WeatherStation node;
        ArrayList<WeatherStation> weatherStationList = new ArrayList<WeatherStation>();
        for (Device device : registry.getDevices()) {
            node = isNodePrecious(device);
            if (node != null) {
                weatherStationList.add(node);
            }
        }
        BackgroundService pollingService = (BackgroundService) context;
        pollingService.upnpScanUpdate(weatherStationList);
    }

    private WeatherStation isNodePrecious(Device device) {
        String friendlyName = device.getDetails().getFriendlyName();
        String searchString = "PRECIOUS";
        WeatherStation node = null;
        if (friendlyName.toLowerCase().contains(searchString.toLowerCase())) {
            String uri = device.getDetails().getPresentationURI().toString();
            Log.i(TAG, "The node is Precious");
            Log.i(TAG, "URI is " + uri);
            node = new WeatherStation(friendlyName, uri);
        }
        return node;
    }

    private void doBind() {
    /*Try StartService Instead */
        context.bindService(
                new Intent(context, AndroidUpnpServiceImpl.class),
                serviceConnection,
                Context.BIND_AUTO_CREATE
        );
    }
}


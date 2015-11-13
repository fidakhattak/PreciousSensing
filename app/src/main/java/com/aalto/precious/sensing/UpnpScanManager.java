package com.aalto.precious.sensing;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import org.teleal.cling.android.AndroidUpnpService;
import org.teleal.cling.android.AndroidUpnpServiceImpl;
import org.teleal.cling.model.meta.Device;
import org.teleal.cling.registry.Registry;

import java.util.ArrayList;

/**
 * Created by fida on 4.11.2015.
 */
public class UpnpScanManager {
    Context context;
    AndroidUpnpService upnpService;
    UpnpRegistryListener registryListener = new UpnpRegistryListener();
    ServiceConnection serviceConnection;

    public UpnpScanManager(Context context) {
        this.context = context;
    }

    public boolean startUpnpService() {
        StartServiceConnection();
        doBind();
        return false;
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
                updateWeatherStationList(upnpService.getRegistry());
                //  StopBowsing();
            }

            public void onServiceDisconnected(ComponentName className) {
                StopBowsing();
            }
        };
    }

    private void updateWeatherStationList(Registry registry) {
        WeatherStation node;
        ArrayList<WeatherStation> weatherStationList = new ArrayList<WeatherStation>();
        for (Device device : registry.getDevices()) {
            node = isNodePrecious(device);
            if (node != null) {
                weatherStationList.add(node);
            }
        }
        NodePollingService pollingService = (NodePollingService) context;
        pollingService.updateWeatherStations(weatherStationList);
    }

    private WeatherStation isNodePrecious(Device device) {
        String friendlyName = device.getDetails().getFriendlyName();
        String uri;
        String searchString = "PRECIOUS";
        WeatherStation node = null;
        if (friendlyName.toLowerCase().contains(searchString.toLowerCase())) {
            uri = device.getDetails().getPresentationURI().toString();
            System.out.println("The node is Precious");
            System.out.println("URI is " + uri);
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

    public void StopBowsing() {
        if (upnpService != null) {
            upnpService.getRegistry().removeListener(registryListener);
        }
        context.unbindService(serviceConnection);
    }


}


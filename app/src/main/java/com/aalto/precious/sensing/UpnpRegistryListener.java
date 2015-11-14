package com.aalto.precious.sensing;

import android.util.Log;

import org.teleal.cling.model.meta.Device;
import org.teleal.cling.model.meta.LocalDevice;
import org.teleal.cling.model.meta.RemoteDevice;
import org.teleal.cling.registry.DefaultRegistryListener;
import org.teleal.cling.registry.Registry;

import java.net.URI;
import java.util.ArrayList;

/**
 * Created by fida on 4.11.2015.
 */
public class UpnpRegistryListener extends DefaultRegistryListener {

    private static final String TAG = "RegistryListener";
    private static final String PRECIOUS_NAME = "precious";
    ArrayList<WeatherStation> serverList = new ArrayList<>();

    public void deviceAdded(Device device) {
        String name = device.getDetails().getFriendlyName();

        URI uri;
        if (name.toLowerCase().contains(PRECIOUS_NAME.toLowerCase())) {
            uri = device.getDetails().getPresentationURI();
            System.out.println("The node is Precious");
            System.out.println("URI is " + uri.toString());
            AddToList(device);
        }
    }

    @Override
    public void remoteDeviceAdded(Registry registry, RemoteDevice device) {

        deviceAdded(device);
    }

    @Override
    public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
        deviceRemoved(device);
    }

    @Override
    public void localDeviceAdded(Registry registry, LocalDevice device) {
        deviceAdded(device);
    }

    @Override
    public void localDeviceRemoved(Registry registry, LocalDevice device) {

        deviceRemoved(device);
    }

    public void deviceRemoved(final Device device) {
        Log.i(TAG, "Device removed");
    }


    @Override
    public void remoteDeviceDiscoveryStarted(Registry registry, RemoteDevice device) {
        Log.i(TAG, "Device Discovery Started");
    }

    @Override
    public void remoteDeviceDiscoveryFailed(Registry registry, final RemoteDevice device, final Exception ex) {

        Log.i(TAG, "Device Discovery Failed");
    }


    public void AddToList(Device device) {
        WeatherStation node = new WeatherStation(device.getDetails().getFriendlyName(), device.getDetails().getPresentationURI().toString());
        serverList.add(node);
        Log.i(TAG, "New WeatherStation Added");

    }
}

package com.aalto.precious.sensing;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by fida on 6.11.2015.
 */
public class WeatherStation {

    ArrayList<Sensor> sensors = new ArrayList<Sensor>();
    private String location;
    private String uri;

    public WeatherStation() {
    }

    public WeatherStation(String friendly_name) {
        location = getLocationFromName(friendly_name);
    }

    public WeatherStation(String friendly_name, String uri) {
        this.uri = uri;
        location = getLocationFromName(friendly_name);
    }

    private String getLocationFromName(String friendly_name) {
        String parts[];
        if (friendly_name.contains(":")) {
            parts = friendly_name.split(":");
            return parts[1];
        }
        return friendly_name;
    }

    public ArrayList<Sensor> getSensorList() {
        return sensors;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void addSensor(Sensor sensor) {
        this.sensors.add(sensor);
    }

    public Sensor getNewSensor() {
        return new Sensor();
    }

    public Sensor getSensor(String name) {
        Iterator<Sensor> iterator = this.sensors.iterator();
        while (iterator.hasNext()) {
            Sensor sensor = iterator.next();
            if (name.equalsIgnoreCase(sensor.getName()))
                return sensor;
        }
        return null;
    }

    public boolean hasTemp() {
        String name = "Temperature";
        Iterator<Sensor> iterator = sensors.iterator();
        boolean bool = false;
        while (iterator.hasNext()) {
            Sensor sensor = iterator.next();
            bool = name.equalsIgnoreCase(sensor.getName());
        }
        return bool;
    }

    public boolean hasHumidity() {
        boolean bool = false;
        String name = "Humidity";
        Iterator<Sensor> iterator = sensors.iterator();
        while (iterator.hasNext()) {
            Sensor sensor = iterator.next();
            bool = name.equalsIgnoreCase(sensor.getName());
        }
        return bool;
    }

    public boolean hasLight() {
        boolean bool = false;
        String name = "Light";
        Iterator<Sensor> iterator = sensors.iterator();
        while (iterator.hasNext()) {
            Sensor sensor = iterator.next();
            bool = name.equalsIgnoreCase(sensor.getName());
        }
        return bool;
    }

    public boolean hasSound() {
        boolean bool = false;
        String name = "Sound";
        Iterator<Sensor> iterator = sensors.iterator();
        while (iterator.hasNext()) {
            Sensor sensor = iterator.next();
            bool = name.equalsIgnoreCase(sensor.getName());
        }
        return bool;
    }

    public boolean hasAir() {
        boolean bool = false;
        String name = "Air";
        Iterator<Sensor> iterator = sensors.iterator();
        while (iterator.hasNext()) {
            Sensor sensor = iterator.next();
            bool = name.equalsIgnoreCase(sensor.getName());
        }
        return bool;
    }

    public String prettyPrintSensors() {
        String s = "location of the node is " + this.location + "\n";
        Iterator<Sensor> iterator = sensors.iterator();
        while (iterator.hasNext()) {
            Sensor sensor = iterator.next();
            s += "sensorType  " + sensor.getName() + "\n";
            s += "\t sensorValue  " + sensor.getSensorValue() + "\n";
            s += "\t minThreshold  " + sensor.getMinThreshold() + "\n";
            s += "\t maxThreshold  " + sensor.getMaxThreshold() + "\n";
            s += "\t sampleTime  " + sensor.getSampleTime() + "\n";
        }
        return s;
    }
}

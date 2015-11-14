package com.aalto.precious.sensing;

/**
 * Created by fida on 6.11.2015.
 */
public class Sensor {
    private String name;
    private String sampleTime;
    private String stringSensorValue;
    private String unit;
    private float sensorValue;
    private float maxThreshold;
    private float minThreshold;
    private int progressBarValue;

    public int getProgressBarValue() {

        return progressBarValue;
    }

    public String getStringSensorValue() {

        return stringSensorValue;
    }

    public String getUnit() {

        return unit;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public float getSensorValue() {

        return sensorValue;
    }

    public void setSensorValue(float sensorValue) {

        this.sensorValue = sensorValue;
    }

    public float getMaxThreshold() {

        return maxThreshold;
    }

    public void setMaxThreshold(float maxThreshold) {

        this.maxThreshold = maxThreshold;
    }

    public float getMinThreshold() {

        return minThreshold;
    }

    public void setMinThreshold(float minThreshold) {

        this.minThreshold = minThreshold;
    }

    public String getSampleTime() {

        return sampleTime;
    }

    public void setSampleTime(String sampleTime) {

        this.sampleTime = sampleTime;
    }

    /* Populate other data members of the class from the information received */
    public void updateSensorInformation() {

        if (name.equalsIgnoreCase("temperature")) {
            this.unit = "°C";
            //update later
            this.progressBarValue = (int) sensorValue;
            this.stringSensorValue = Float.toString(sensorValue);
        } else if (name.equalsIgnoreCase("humidity")) {
            this.unit = " %";
            this.progressBarValue = (int) sensorValue;
            this.stringSensorValue = Float.toString(sensorValue);
        } else if (name.equalsIgnoreCase("light")) {
            this.unit = "";
            this.progressBarValue = (int) sensorValue / 5;
            if (this.sensorValue > 400) {
                this.stringSensorValue = "Bright";
            } else if (this.sensorValue < 400 && this.sensorValue > 100) {
                this.stringSensorValue = "Medium";
            } else if (this.sensorValue < 100) {
                this.stringSensorValue = "Dark";
            }
        } else if (name.equalsIgnoreCase("air")) {

            this.progressBarValue = (int) sensorValue / 10;
            if (this.sensorValue > 900) {
                this.stringSensorValue = "Clean";
            } else if (this.sensorValue < 850 && this.sensorValue > 500) {
                this.stringSensorValue = "Low Polution";
            } else if (this.sensorValue < 500) {
                this.stringSensorValue = "High Polution";
            }
        }
    }
}

package com.aalto.precious.sensing;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

/**
 * Created by fida on 6.11.2015.
 */
public class SensorReadTask extends AsyncTask<Uri, Void, Void> {
    private static String TAG = "SENSOR_READ_TASK";
    private WeatherStation station = null;
    private Uri uri;
    private Communicator communicator;
    private SensorReadTaskCallback updateCompleted;

    public SensorReadTask(SensorReadTaskCallback activityContext) {
        this.updateCompleted = activityContext;
    }

    protected Void doInBackground(Uri... taskParameters) {
        try {
            this.uri = taskParameters[0];
            String address = uri.getHost();
            int port = uri.getPort();
            Log.i(TAG, "Address: " + address + " port: " + port);
            this.communicator = new Communicator();
            SensorRead();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        this.updateCompleted.sensorReadUpdate(station);
    }

    private void SensorRead() throws IOException {
        boolean success = communicator.connect(uri);
        if (!success) {
            Log.e(TAG, "Error connecting to host");
            return;
        }
        success = communicator.communicate();
        if (!success) {
            Log.e(TAG, "Error communicating to the host");
            return;
        }
        success = communicator.parse();
        if (!success) {
            Log.e(TAG, "Error Parsing the response");
            return;
        }
        success = communicator.closeConnection();
        if (!success)
            Log.e(TAG, "Error Closing the connecting");

        station = this.communicator.getWeatherStation();
        if (station == null)
            Log.i(TAG, "ReadSensor couldn't parse a node");
    }

}


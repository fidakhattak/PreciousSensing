package com.aalto.precious.sensing;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.IOException;

/**
 * Created by fida on 6.11.2015.
 */
public class SensorReadTask extends AsyncTask<ReadTaskParameters, Void, Void> {
    WeatherStation station = null;
    Uri uri;
    Communicator communicator;
    Context context;
    SensorUpdateInterface updateCompleted;

    public SensorReadTask(SensorUpdateInterface activityContext) {
        this.updateCompleted = activityContext;
    }

    protected Void doInBackground(ReadTaskParameters... taskParameters) {
        try {
            this.uri = taskParameters[0].uri;
            this.context = taskParameters[0].context;
            System.out.println("Background Started");

            String address = uri.getHost();
            int port = uri.getPort();
            System.out.println("Address: " + address + " port: " + port);
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
        this.updateCompleted.OnUpdateComplete(station);
    }

    private void SensorRead() throws IOException {
        boolean success = communicator.connect(uri);
        if (!success) {
            System.err.println("Error connecting to host");
            return;
        }
        success = communicator.communicate();
        if (!success) {
            System.err.println("Error communicating to the host");
            return;
        }
        success = communicator.parse();
        if (!success) {
            System.err.println("Error Parsing the response");
            return;
        }
        success = communicator.closeConnection();
        if (!success)
            System.err.println("Error Closing the connecting");

        station = this.communicator.getWeatherStation();
        if (station == null)
            System.out.println("ReadSensor couldn't parse a node");
    }

}


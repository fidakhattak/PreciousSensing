package com.aalto.precious.sensing;

import android.net.Uri;
import android.util.Log;

import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by fida on 6.11.2015.
 */
public class Communicator {
    private static String TAG = "COMMUNICATOR";
    private String uri;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String stringResponse;
    private WeatherStation station;

    public boolean connect(Uri uri) throws IOException {

        this.uri = String.valueOf(uri);
        Socket socket = createConnection(uri.getHost(), uri.getPort());

        if (socket == null) {
            Log.i(TAG, "Could not connect to " + uri.getHost() + " : " + uri.getPort());
            Log.i(TAG, "Could not connect to " + uri.getHost() + " : " + uri.getPort());
            Log.i(TAG, "Sensor Communicate Operation Failed: Returning 0");
            return false;
        }
        this.socket = socket;
        return true;
    }

    public boolean communicate() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
        } catch (IOException e1) {
            // TODO Auto-generated catch back
            e1.printStackTrace();
            return false;
        }
        Request request = new Request();
        out.println(request.getStringRequest());
        try {
            stringResponse = in.readLine();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return false;
        }
        System.out.println(stringResponse);
        return true;
    }

    public boolean parse() {

        Response response = new Response();
        response.setStringResponse(stringResponse);

        try {
            station = response.parseStringResponse(stringResponse);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            // TODO Auto-generated catch block
            Log.i(TAG, "Error Parsing the response" + e.getMessage());
            return false;
        }
        if (station == null) {
            System.out.println("Could Not initialize weatherStation");
            return false;
        }

        station.setUri(uri);
        System.out.println("Communicator got the node");
        return true;
    }

    public boolean closeConnection() {
        out.close();
        try {
            in.close();
            socket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private Socket createConnection(String address, int port) {

        Log.i(TAG, "Attemping to connect to host " +
                address + " on port " + port);
        try {
            socket = new Socket(address, port);
        } catch (UnknownHostException e) {
            Log.i(TAG, "Don't know about host: " + address);
            return null;
        } catch (IOException e) {
            Log.i(TAG, "Couldn't get I/O for "
                    + "the connection to: " + address + e.getMessage());
            return null;
        }
        return socket;
    }

    public WeatherStation getWeatherStation() {
        Log.i(TAG, "Communicator returning the node");
        return station;
    }
}

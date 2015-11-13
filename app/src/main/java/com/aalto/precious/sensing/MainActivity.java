package com.aalto.precious.sensing;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";
    boolean mbound = false;
    private NodePollingService nodePollingService;

    protected ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mbound = true;

            NodePollingService.LocalBinder binder = (NodePollingService.LocalBinder) service;
            nodePollingService = binder.getService();
            ArrayList<WeatherStation> serverList = nodePollingService.getAllWeatherStations();

            for (int i = 0; i < serverList.size(); i++) {
                System.out.println(serverList.get(i).getLocation());
            }
            displayNodes(getApplicationContext(), serverList);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mbound = false;

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void onStart() {
        super.onStart();

        Intent pollingService = new Intent(getApplicationContext(), NodePollingService.class);
        if (!isMyServiceRunning(NodePollingService.class))
            startService(pollingService);
        bindService(pollingService, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        unbindService(serviceConnection);
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void displayNodes(Context context, ArrayList<WeatherStation> onlineServerList) {
        try {
            ViewListAdapter adapter = new ViewListAdapter(context, onlineServerList);
            ListView listView = (ListView) findViewById(R.id.list);
            listView.setAdapter(adapter);
            setLisViewItemListener(listView);
            registerForContextMenu(listView);
        } catch (NullPointerException e) {
            System.out.println(e.toString());
        }
    }

    public void setLisViewItemListener(ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                WeatherStation serverNode = (WeatherStation) adapter.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), SensorDisplayActivity.class);
                //based on item add info to intent
                startActivity(intent);
            }
        });
    }

/*
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add("Click Here");
        menu.add("Click There");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        super.onContextItemSelected(item);
        if (item.getTitle() == "Click Here") {
            Toast.makeText(this, "Hello World!", Toast.LENGTH_LONG).show();
        } else if (item.getTitle() == "Click There") {
            Toast.makeText(this, "How are you doing today", Toast.LENGTH_LONG).show();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/


}
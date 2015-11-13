package com.aalto.precious.sensing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by fida on 7.11.2015.
 */
public class SensorListAdapter extends ArrayAdapter {

    public SensorListAdapter(Context context, ArrayList<Sensor> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Sensor item = (Sensor) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sensor_layout, parent, false);
        }
        // Lookup view for data population;
        TextView sensorName = (TextView) convertView.findViewById(R.id.sensorName);
        ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
        TextView sensorValue = (TextView) convertView.findViewById(R.id.sensorValue);
        TextView sensorUnit = (TextView) convertView.findViewById(R.id.sensorUnit);

        // Populate the data into the template view using the data object
        // Return the completed view to render on screen
        sensorName.setText(item.getName());
        progressBar.setProgress(item.getProgressBarValue());
        sensorValue.setText(item.getStringSensorValue());
        sensorUnit.setText(item.getUnit());
        return convertView;
    }
}



package com.aalto.precious.sensing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by fida on 24.10.2015.
 */

public class WeatherStationListAdapter extends ArrayAdapter {

    public WeatherStationListAdapter(Context context, ArrayList<WeatherStation> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        WeatherStation item = (WeatherStation) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view, parent, false);
        }
        // Lookup view for data population;
        TextView nodeName = (TextView) convertView.findViewById(R.id.textViewNode);
        TextView nodeStatus = (TextView) convertView.findViewById(R.id.textViewStatus);

        // Populate the data into the template view using the data object
        nodeName.setText(item.getLocation());
        nodeStatus.setText(item.getUri());
        // Return the completed view to render on screen
        return convertView;
    }
}

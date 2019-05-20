package com.example.readsensors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.eclipse.californium.core.WebLink;

import java.util.ArrayList;

public class WebLinksAdapter extends ArrayAdapter<WebLink> {
    public WebLinksAdapter(Context context, ArrayList<WebLink> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
         WebLink webLink = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_topic, parent, false);
        }
        // Lookup view for data population
        TextView tvName = convertView.findViewById(R.id.tv_topic_name);
        TextView tvCT = convertView.findViewById(R.id.tv_ct);
        TextView tvUri = convertView.findViewById(R.id.tv_uri);
        // Populate the data into the template view using the data object
        tvName.setText(Converter.getName(webLink));
        tvCT.setText(Converter.getCT(webLink));
        tvUri.setText(Converter.getUri(webLink));
        // Return the completed view to render on screen
        return convertView;
    }

}

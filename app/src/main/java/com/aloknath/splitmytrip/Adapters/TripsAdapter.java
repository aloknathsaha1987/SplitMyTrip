package com.aloknath.splitmytrip.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aloknath.splitmytrip.Objects.Trip;
import com.aloknath.splitmytrip.R;

import java.util.List;

/**
 * Created by ALOKNATH on 3/10/2015.
 */
public class TripsAdapter extends ArrayAdapter<Trip> {

    private Context context;
    private List<Trip> tripList;
    TextView textView;

    public TripsAdapter(Context context, int resource,  List<Trip> items) {
        super(context, resource , items);
        this.context = context;
        this.tripList = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.trips_list_display, parent, false);

        Trip trip = getItem(position);

        textView = (TextView)convertView.findViewById(R.id.trip_title);
        textView.setText(trip.getTripName());

        textView = (TextView)convertView.findViewById(R.id.edit_person_count);
        textView.setText(String.valueOf(trip.getNoOfPersons()));

        textView = (TextView)convertView.findViewById(R.id.edit_total_cost);
        textView.setText(String.valueOf(trip.getTotalCost()));

        return convertView;
    }
}

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

    public TripsAdapter(Context context, int resource,  List<Trip> items) {
        super(context, resource , items);
        this.context = context;
        this.tripList = items;
    }

    static class ViewHolder{
        TextView trip_title;
        TextView person_count;
        TextView total_cost;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.trips_list_display, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.trip_title = (TextView)convertView.findViewById(R.id.trip_title);
            viewHolder.person_count = (TextView)convertView.findViewById(R.id.edit_person_count);
            viewHolder.total_cost = (TextView)convertView.findViewById(R.id.edit_total_cost);
            convertView.setTag(viewHolder);
        }

        Trip trip = getItem(position);
        ViewHolder holder = (ViewHolder)convertView.getTag();

        holder.trip_title.setText(trip.getTripName());
        holder.person_count.setText(String.valueOf(trip.getNoOfPersons()));
        holder.total_cost.setText(String.valueOf(trip.getTotalCost()));

        return convertView;
    }
}

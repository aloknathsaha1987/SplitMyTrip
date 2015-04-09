package com.aloknath.splitmytrip.Adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aloknath.splitmytrip.Objects.Trip;
import com.aloknath.splitmytrip.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by ALOKNATH on 3/10/2015.
 */
public class TripsAdapter extends ArrayAdapter<Trip> {

    private final Context context;
    private final List<Trip> tripList;
    private String[] tripImages = new String[]{"existing_trip.jpg","trip1.jpg" , "trip2.jpg", "new_trip.jpg", "park.jpg","beach.jpg"};

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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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

        int modPosition = position % 6;
        switch (modPosition){
            case 0:
                tripImageChange(convertView, tripImages[0]);
                break;
            case 1:
                tripImageChange(convertView, tripImages[1]);
                break;
            case 2:
                tripImageChange(convertView, tripImages[2]);
                break;
            case 3:
                tripImageChange(convertView, tripImages[3]);
                break;
            case 4:
                tripImageChange(convertView, tripImages[4]);
                break;
            case 5:
                tripImageChange(convertView, tripImages[5]);
                break;
            default:
                break;
        }
        Trip trip = getItem(position);
        ViewHolder holder = (ViewHolder)convertView.getTag();

        holder.trip_title.setText(trip.getTripName());
        holder.person_count.setText(String.valueOf(trip.getNoOfPersons()));
        holder.total_cost.setText(String.valueOf(trip.getTotalCost()));

        return convertView;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private boolean tripImageChange(View convertView, String imageName) {
        try
    {
        InputStream ims = context.getAssets().open(imageName);
        Drawable d = Drawable.createFromStream(ims, null);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView2);
        imageView.setBackground(d);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

    }catch(IOException ex)
    {
        return true;
    }
        return false;
    }
}

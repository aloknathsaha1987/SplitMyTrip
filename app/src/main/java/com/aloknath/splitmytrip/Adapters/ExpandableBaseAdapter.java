package com.aloknath.splitmytrip.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.aloknath.splitmytrip.Activities.TripActivity;
import com.aloknath.splitmytrip.Objects.Person;
import com.aloknath.splitmytrip.Objects.TripItem;
import com.aloknath.splitmytrip.R;

/**
 * Created by ALOKNATH on 3/11/2015.
 */

public class ExpandableBaseAdapter extends BaseExpandableListAdapter {

    private Context context;
    private final TripActivity.TripItemsPersons tripItemsPersons;
    private TextView textView;

    public ExpandableBaseAdapter(Context context, final TripActivity.TripItemsPersons tripItemsPersons){
        this.context = context;
        this.tripItemsPersons = tripItemsPersons;
    }


    @Override
    public int getGroupCount() {
        return 2;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return (groupPosition == 0 ? tripItemsPersons.getItems().size(): tripItemsPersons.getPersons().size() );
    }

    @Override
    public Object getGroup(int groupPosition) {
        return (groupPosition == 0 ? "TRIP ITEMS" : "PERSONS");
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return (groupPosition == 0 ? tripItemsPersons.getItems().get(childPosition): tripItemsPersons.getPersons().get(childPosition));
    }


    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup viewGroup) {

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.trip_itenary, null);
        }
        textView = (TextView)convertView.findViewById(R.id.trip);

        if(groupPosition == 0){
            textView.setText("TRIP ITEMS");

        }else{
            textView.setText("PERSONS");
        }

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final  int childPosition, boolean isLastChild, View convertView, ViewGroup viewGroup) {

        if(groupPosition == 0){
            TripItem item = (TripItem)getChild(groupPosition, childPosition);
          //  if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.trip_items_list_display, null);

                textView = (TextView)convertView.findViewById(R.id.edit_trip_item);
                textView.setText(item.getItemName());

                textView = (TextView)convertView.findViewById(R.id.edit_item_cost);
                textView.setText(String.valueOf(item.getItemCost()));

            //}
        }else {
            Person person = (Person)getChild(groupPosition,childPosition);
            //if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.person_list_display, null);

                textView = (TextView) convertView.findViewById(R.id.edit_person_name);
                textView.setText(person.getName());

                textView = (TextView) convertView.findViewById(R.id.edit_amount_paid);
                textView.setText("$" + String.valueOf(person.getAmountPaid()));

                textView = (TextView) convertView.findViewById(R.id.edit_amount_owed);
                textView.setText("$" + String.valueOf(person.getAmountOwed()));

                textView = (TextView) convertView.findViewById(R.id.edit_amount_to_get);
                textView.setText("$" + String.valueOf(person.getAmountToGet()));
           // }

        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return true;
    }
}

package com.aloknath.splitmytrip.Adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.aloknath.splitmytrip.Activities.TripActivity;
import com.aloknath.splitmytrip.CustomViews.RoundedImageView;
import com.aloknath.splitmytrip.Objects.Person;
import com.aloknath.splitmytrip.Objects.TripItem;
import com.aloknath.splitmytrip.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by ALOKNATH on 3/11/2015.
 */

public class ExpandableBaseAdapter extends BaseExpandableListAdapter {

    private Context context;
    private final TripActivity.TripItemsPersons tripItemsPersons;
    private TextView textView;
    private HashMap<Integer, Bitmap> itemsHashMap = new HashMap<>();
    private HashMap<Integer, Bitmap> personsHashMap = new HashMap<>();

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

    static class ViewHolderItem{
        private RoundedImageView imageView;
        private TextView item;
        private TextView item_cost;
    }

    static class ViewHolderPerson{
        private RoundedImageView imageView;
        private TextView personName;
        private TextView amount_paid;
        private TextView amount_owed;
        private TextView amount_to_get;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getChildView(final int groupPosition, final  int childPosition, boolean isLastChild, View convertView, ViewGroup viewGroup) {

        if(groupPosition == 0){
            TripItem item = (TripItem)getChild(groupPosition, childPosition);
           // if(convertView == null) {
                LayoutInflater inflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.trip_items_list_display, null);
                ViewHolderItem viewHolderItem = new ViewHolderItem();
                viewHolderItem.imageView = (RoundedImageView)convertView.findViewById(R.id.imageView2);
                viewHolderItem.item = (TextView)convertView.findViewById(R.id.edit_trip_item);
                viewHolderItem.item_cost = (TextView)convertView.findViewById(R.id.edit_item_cost);
                convertView.setTag(viewHolderItem);
          //  }

            ViewHolderItem holderItem = (ViewHolderItem) convertView.getTag();

            if(itemsHashMap.get(childPosition) == null) {

                try {
                    InputStream inputStream = context.getAssets().open("new_trip.jpg");
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                    Bitmap bitmap = BitmapFactory.decodeStream(bufferedInputStream);
                    holderItem.imageView.setImageBitmap(bitmap);
                    itemsHashMap.put(childPosition, bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                holderItem.imageView.setImageBitmap(itemsHashMap.get(childPosition));
            }

            holderItem.item.setText(item.getItemName());

            holderItem.item_cost.setText(String.valueOf(item.getItemCost()));

        }else {
            Person person = (Person)getChild(groupPosition,childPosition);

           // if(convertView == null) {

                LayoutInflater inflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.person_list_display, null);

                ViewHolderPerson viewHolderPerson = new ViewHolderPerson();
                viewHolderPerson.imageView = (RoundedImageView)convertView.findViewById(R.id.imageView2);
                viewHolderPerson.amount_owed = (TextView) convertView.findViewById(R.id.edit_amount_owed);
                viewHolderPerson.amount_paid = (TextView) convertView.findViewById(R.id.edit_amount_paid);
                viewHolderPerson.amount_to_get = (TextView) convertView.findViewById(R.id.edit_amount_to_get);
                viewHolderPerson.personName = (TextView) convertView.findViewById(R.id.edit_person_name);
                convertView.setTag(viewHolderPerson);

          //  }

                ViewHolderPerson holderPerson = (ViewHolderPerson)convertView.getTag();

            if(personsHashMap.get(childPosition) == null) {

                try {
                    InputStream inputStream = context.getAssets().open("splash_screen.jpg");
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                    Bitmap bitmap = BitmapFactory.decodeStream(bufferedInputStream);
                    holderPerson.imageView.setImageBitmap(bitmap);
                    personsHashMap.put(childPosition, bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                holderPerson.imageView.setImageBitmap(personsHashMap.get(childPosition));
            }


            holderPerson.personName.setText(person.getName());


            holderPerson.amount_paid.setText("$" + String.valueOf(person.getAmountPaid()));


            holderPerson.amount_owed.setText("$" + String.valueOf(person.getAmountOwed()));

            holderPerson.amount_to_get.setText("$" + String.valueOf(person.getAmountToGet()));

        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return true;
    }
}

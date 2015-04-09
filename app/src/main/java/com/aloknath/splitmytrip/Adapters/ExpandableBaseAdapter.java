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
import android.widget.ImageView;
import android.widget.TextView;
import com.aloknath.splitmytrip.Activities.TripActivity;
import com.aloknath.splitmytrip.CustomViews.RoundedImageView;
import com.aloknath.splitmytrip.Objects.Person;
import com.aloknath.splitmytrip.Objects.TripItem;
import com.aloknath.splitmytrip.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

import static com.aloknath.splitmytrip.ImageConversion.DbBitmapUtility.getImage;

/**
 * Created by ALOKNATH on 3/11/2015.
 */

public class ExpandableBaseAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private final TripActivity.TripItemsPersons tripItemsPersons;
    private TextView textView;
    private HashMap<Integer, Bitmap> itemsHashMap = new HashMap<>();
    private HashMap<Integer, Bitmap> personsHashMap = new HashMap<>();
    private HashMap<Integer, Bitmap> imagesPassed = new HashMap<>();

    public ExpandableBaseAdapter(Context context, final TripActivity.TripItemsPersons tripItemsPersons, HashMap<Integer, Bitmap> imagesPassed){
        this.context = context;
        this.tripItemsPersons = tripItemsPersons;
        this.imagesPassed = imagesPassed;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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
            try
            {
                InputStream ims = context.getAssets().open("trip_items.jpg");
                Drawable d = Drawable.createFromStream(ims, null);
                ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView2);
                imageView.setBackground(d);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            }catch(IOException ex)
            {
                return null;
            }

        }else{
            textView.setText("PERSONS");
            try
            {
                InputStream ims = context.getAssets().open("trip_persons.jpeg");
                Drawable d = Drawable.createFromStream(ims, null);
                ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView2);
                imageView.setBackground(d);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            }catch(IOException ex)
            {
                return null;
            }
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

                Bitmap bitmap = imagesPassed.get(childPosition);
                holderItem.imageView.setImageBitmap(bitmap);
                itemsHashMap.put(childPosition, bitmap);

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

                    holderPerson.imageView.setImageBitmap(person.getPersonImage());
                    personsHashMap.put(childPosition, person.getPersonImage());

            }else{
                holderPerson.imageView.setImageBitmap(personsHashMap.get(childPosition));
            }

            holderPerson.personName.setText(person.getName());
            holderPerson.amount_paid.setText("" + String.valueOf(round(person.getAmountPaid(),2)));
            holderPerson.amount_owed.setText("" + String.valueOf(round(person.getAmountOwed(),2)));
            holderPerson.amount_to_get.setText("" + String.valueOf(round(person.getAmountToGet(),2)));
        }

        return convertView;
    }



    @Override
    public boolean isChildSelectable(int i, int i2) {
        return true;
    }
}

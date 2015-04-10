package com.aloknath.splitmytrip.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aloknath.splitmytrip.CustomViews.RoundedImageView;
import com.aloknath.splitmytrip.Objects.TripItem;
import com.aloknath.splitmytrip.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ALOKNATH on 3/20/2015.
 */
public class ItemsAdapter  extends ArrayAdapter<TripItem> {

    private final Context context;
    private final List<TripItem> itemList;
    private final HashMap<Integer, Bitmap> itemsHashMap = new HashMap<>();
    private HashMap<Integer, Bitmap> imagesPassed = new HashMap<>();

    public ItemsAdapter(Context context, int resource, List<TripItem> itemList, HashMap<Integer, Bitmap> imagesPassed ){
        super(context, resource, itemList);
        this.context = context;
        this.itemList = itemList;
        this.imagesPassed = imagesPassed;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TripItem item = itemList.get(position);

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.trip_items_list_display, null);
            ViewHolderItem viewHolderItem = new ViewHolderItem();
            viewHolderItem.imageView = (RoundedImageView)convertView.findViewById(R.id.imageView2);
            viewHolderItem.item = (TextView)convertView.findViewById(R.id.edit_trip_item);
            viewHolderItem.item_cost = (TextView)convertView.findViewById(R.id.edit_item_cost);
            convertView.setTag(viewHolderItem);
            //  }

            ViewHolderItem holderItem = (ViewHolderItem) convertView.getTag();

            if(itemsHashMap.get(position) == null) {

                Bitmap bitmap = imagesPassed.get(position);
                holderItem.imageView.setImageBitmap(bitmap);
                itemsHashMap.put(position, bitmap);

            }else{
                holderItem.imageView.setImageBitmap(itemsHashMap.get(position));
            }

            holderItem.item.setText(item.getItemName());

            holderItem.item_cost.setText(String.valueOf(item.getItemCost()));

        return convertView;
    }

    static class ViewHolderItem{
        private RoundedImageView imageView;
        private TextView item;
        private TextView item_cost;
    }
}

package com.aloknath.splitmytrip.Fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.aloknath.splitmytrip.Objects.Person;
import com.aloknath.splitmytrip.Objects.TripItem;
import com.aloknath.splitmytrip.R;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ALOKNATH on 3/14/2015.
 */
public class ChildFragment extends Fragment implements View.OnClickListener {

    public static final String POSITION_KEY = "FragmentPositionKey";
    private int position;
    private int groupPosition;
    private static List<HashMap<String, Object>> result = null;
    private static HashMap<Integer, TripItem> tripItemList;
    private static HashMap<Integer, Person> personsList;
    private static double amountOwed;
    private static double amountToGet;
    private onChildEvent childEventListener;
    private AlertDialog.Builder alertDialog;
    private double entered_amount = 0;
    private ImageView imageView;
    private Button save_item;
    private Button save_person;
    private double person_amount_paid;
    private double item_amount_edited;
    private EditText editTextPerson;
    private EditText editTextItem;


    public interface  onChildEvent {
        public void amountPaid(String tripName, String from, String to, double amount);
        public void editPerson(Person person, double amount, List<HashMap<String, Object>> result);
        public void editItem(TripItem item, double amount);
    }

    public static ChildFragment newInstance(Bundle args) {
        ChildFragment fragment = new ChildFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            childEventListener = (onChildEvent)activity;
        } catch(ClassCastException e)
        {
            Log.e("ClassCastException in ChildFragment ", activity.toString() + " must implement onChildEvent");
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        alertDialog = new AlertDialog.Builder(getActivity());

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        position = getArguments().getInt(POSITION_KEY);
        groupPosition = getArguments().getInt("id");
        View root = null;
        TextView textView;

        if (groupPosition != 0) {

            root = inflater.inflate(R.layout.fragment_child_person, container, false);

            InputMethodManager imm = (InputMethodManager)root.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(root.getWindowToken(), 0);
            }

            try
            {
                InputStream ims = getActivity().getAssets().open("child_item.jpg");
                Drawable d = Drawable.createFromStream(ims, null);
                root.setBackground(d);

            }catch(IOException ex)
            {
                return null;
            }


            personsList = (HashMap<Integer, Person>) getArguments().getSerializable("hashMap");
            result = (List<HashMap<String, Object>>) getArguments().getSerializable("price_split");

            textView = (TextView)root.findViewById(R.id.edit_amount_to_get);
            textView.setText("$" + String.valueOf(personsList.get(position).getAmountToGet()));
            amountToGet = personsList.get(position).getAmountToGet();

            textView = (TextView)root.findViewById(R.id.edit_amount_owed);
            textView.setText("$" + String.valueOf(personsList.get(position).getAmountOwed()));
            amountOwed = personsList.get(position).getAmountOwed();

            textView = (TextView)root.findViewById(R.id.edit_amount_paid);
            textView.setText("$" + String.valueOf(personsList.get(position).getAmountPaid()));

            editTextPerson = (EditText)root.findViewById(R.id.edit_amount);

            save_person = (Button)root.findViewById(R.id.button_save_person);
            final String[] value = new String[1];

            save_person.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    value[0] = editTextPerson.getText().toString();
                    if(value[0].isEmpty()){

                    }else {
                        person_amount_paid = Double.parseDouble(value[0]);
                        if (person_amount_paid != 0) {
                            childEventListener.editPerson(personsList.get(position), person_amount_paid, result);
                        }
                    }
                }
            });

            imageView = (ImageView)root.findViewById(R.id.imageView2);
            imageView.setImageBitmap(personsList.get(position).getPersonImage());
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Split(root, position);
        }
        else{

            root = inflater.inflate(R.layout.fragment_child_item, container, false);

            InputMethodManager imm = (InputMethodManager)root.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(root.getWindowToken(), 0);
            }

            try
            {
                InputStream ims = getActivity().getAssets().open("child_item.jpg");
                Drawable d = Drawable.createFromStream(ims, null);
                root.setBackground(d);

            }catch(IOException ex)
            {
                return null;
            }

            tripItemList = (HashMap<Integer, TripItem>) getArguments().getSerializable("hashMap");

            textView = (TextView)root.findViewById(R.id.edit_trip_name);
            textView.setText(tripItemList.get(position).getTripName());

            textView = (TextView)root.findViewById(R.id.edit_item_cost);
            textView.setText(String.valueOf(tripItemList.get(position).getItemCost()));

            imageView = (ImageView)root.findViewById(R.id.imageView2);
            imageView.setImageBitmap(setItemImage(tripItemList.get(position).getItemName()));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            editTextItem = (EditText)root.findViewById(R.id.editText2);
            final String[] value = new String[1];

            save_item = (Button)root.findViewById(R.id.button_save_item);

            save_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    value[0] = editTextItem.getText().toString();
                    if(value[0].isEmpty()){

                    }else {
                        item_amount_edited = Double.parseDouble(value[0]);
                        if (item_amount_edited != 0) {
                            childEventListener.editItem(tripItemList.get(position), item_amount_edited);
                        }
                    }
                }
            });

        }

        return root;
    }

    private Bitmap setItemImage(String imageName) {

        InputStream inputStream = null;
        Bitmap imageReturned = null;

        if(imageName.equals("Travel") || imageName.equals("travel")||imageName.equals("TRAVEL")){

            try {
                inputStream = getActivity().getAssets().open("travel.png");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equals("Adventure") || imageName.equals("adventure")||imageName.equals("ADVENTURE")){

            try {
                inputStream = getActivity().getAssets().open("adventure.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equals("Hiking") || imageName.equals("hiking")||imageName.equals("HIKING")||imageName.equals("Trekking")||imageName.equals("trekking")||imageName.equals("TREKKING")){

            try {
                inputStream = getActivity().getAssets().open("hiking.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equals("Food") || imageName.equals("food")||imageName.equals("FOOD")){

            try {
                inputStream = getActivity().getAssets().open("food.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equals("Water") || imageName.equals("water")||imageName.equals("WATER")){

            try {
                inputStream = getActivity().getAssets().open("water.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equals("Hotel") || imageName.equals("hotel")||imageName.equals("HOTEL")){

            try {
                inputStream = getActivity().getAssets().open("hotel.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);


        }else if(imageName.equals("Bar") || imageName.equals("bar")||imageName.equals("BAR")||imageName.equals("Drinks")||imageName.equals("drinks")||imageName.equals("DRINKS")||imageName.equals("Disco")||imageName.equals("disco")||imageName.equals("disco")||imageName.equals("Pub")||imageName.equals("pub")||imageName.equals("PUB")||imageName.equals("Drink")||imageName.equals("drink")||imageName.equals("DRINK")){

            try {
                inputStream = getActivity().getAssets().open("bar.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equals("Fuel") || imageName.equals("fuel")||imageName.equals("FUEL")||imageName.equals("Car")||imageName.equals("car")||imageName.equals("CAR")||imageName.equals("Gas")||imageName.equals("gas")||imageName.equals("GAS")){

            try {
                inputStream = getActivity().getAssets().open("fuel.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equals("Museum") || imageName.equals("museum")||imageName.equals("Museum")||imageName.equals("Gallery")||imageName.equals("gallery")||imageName.equals("gallery")){

            try {
                inputStream = getActivity().getAssets().open("museum.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equals("Aquarium") || imageName.equals("aquarium")||imageName.equals("AQUARIUM")){

            try {
                inputStream = getActivity().getAssets().open("aquarium.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equals("Park") || imageName.equals("park")||imageName.equals("PARK")){

            try {
                inputStream = getActivity().getAssets().open("park.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equals("Train") || imageName.equals("train")||imageName.equals("TRAIN")){

            try {
                inputStream = getActivity().getAssets().open("train.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equals("Bus") || imageName.equals("bus")||imageName.equals("BUS")){

            try {
                inputStream = getActivity().getAssets().open("bus.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equals("Shopping") || imageName.equals("shopping")||imageName.equals("SHOPPING")){

            try {
                inputStream = getActivity().getAssets().open("shopping.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equals("Beach") || imageName.equals("beach")||imageName.equals("BEACH")){

            try {
                inputStream = getActivity().getAssets().open("beach.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);
        }

        return imageReturned;
    }


    private void Split(View root, int position) {
        LinearLayout linearLayout = (LinearLayout)root.findViewById(R.id.linearlayout);

        TextView textView2;
        LinearLayout.LayoutParams layoutParams;
        Person person;
        Person sender;
        Person recipient;
        double amount;
        Button give;

        if(amountOwed == 0 && amountToGet > 0){

            //get the person from the list
            person = personsList.get(position);

            //Iterate through the results list returned and find if the recipient name matches,
            // If if does, then display the below String
            // Person Recipient get $money from Person Sender

            Iterator iterator = result.iterator();

            while (iterator.hasNext()) {

                HashMap<String, Object> mapReturned = (HashMap<String, Object>) iterator.next();
                sender = (Person) mapReturned.get("from");
                recipient = (Person) mapReturned.get("to");
                amount = (Double) mapReturned.get("amount");
                amount = Math.round(amount*100)/100.0d;


                if(person.getName().equals(recipient.getName())){

                    textView2 = new TextView(getActivity());
                    layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.gravity = Gravity.TOP;
                    layoutParams.setMargins(10, 10, 10, 10); // (left, top, right, bottom)
                    textView2.setLayoutParams(layoutParams);
                    textView2.setText(recipient.getName() + " has to get $" + String.valueOf(amount) + " from " + sender.getName());
                    textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    textView2.setTextColor(Color.BLACK);
                    linearLayout.addView(textView2);

                }
            }

        }else if (amountOwed > 0){

            //get the person from the list
            person = personsList.get(position);

            // Person X has to pay $money to Person Y
            Iterator iterator = result.iterator();

            while (iterator.hasNext()) {

                HashMap<String, Object> mapReturned = (HashMap<String, Object>) iterator.next();
                sender = (Person) mapReturned.get("from");
                recipient = (Person) mapReturned.get("to");
                amount = (Double) mapReturned.get("amount");
                amount = Math.round(amount*100)/100.0d;

                if (person.getName().equals(sender.getName())) {

                    textView2 = new TextView(getActivity());
                    layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.gravity = Gravity.CENTER;
                    layoutParams.setMargins(10, 10, 10, 10); // (left, top, right, bottom)
                    textView2.setLayoutParams(layoutParams);
                    textView2.setText(sender.getName() + " has to give $" + String.valueOf(amount) + " to " + recipient.getName());
                    textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    textView2.setTextColor(Color.BLACK);
                    linearLayout.addView(textView2);

                    give = new Button(getActivity());
                    layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(50, 10, 10, 10);
                    give.setLayoutParams(layoutParams);
                    give.setText("Give Amount");
                    linearLayout.addView(give);
                    final Person send = sender;
                    final Person receive = recipient;
                    final double amountPaid = amount;
                    give.setOnClickListener(new View.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onClick(View view) {
                            LayoutInflater inflater = getLayoutInflater(null);
                            View dialoglayout = inflater.inflate(R.layout.alert_dialog_entering_amount, null);

                            alertDialog.setTitle("Amount To Pay");
                            alertDialog.setView(dialoglayout);
                            alertDialog.show();

                            TextView textView = (TextView)dialoglayout.findViewById(R.id.textView3);
                            textView.setText(String.valueOf(amountPaid));

                            Button ok = (Button)dialoglayout.findViewById(R.id.button_ok);
                            Button give_full = (Button)dialoglayout.findViewById(R.id.button_full_amount);
                            final EditText amountEntered = (EditText)dialoglayout.findViewById(R.id.editText);
                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(amountEntered.getText().toString().isEmpty()){
                                        childEventListener.amountPaid(send.getTripName(), send.getName(), receive.getName(), 0);
                                    }else{
                                        entered_amount = Double.valueOf(amountEntered.getText().toString());
                                        if(entered_amount > amountPaid || entered_amount < 0){
                                            childEventListener.amountPaid(send.getTripName(), send.getName(), receive.getName(), 0);
                                        }else {
                                            childEventListener.amountPaid(send.getTripName(), send.getName(), receive.getName(), entered_amount);
                                        }
                                    }
                                }
                            });

                            give_full.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    childEventListener.amountPaid(send.getTripName(), send.getName(), receive.getName(), amountPaid);
                                }
                            });
                        }
                    });

                }
            }

        }

    }

    @Override
    public void onClick(View v) {
        Toast.makeText(v.getContext(), "Clicked Position: " + position, Toast.LENGTH_LONG).show();
    }
}

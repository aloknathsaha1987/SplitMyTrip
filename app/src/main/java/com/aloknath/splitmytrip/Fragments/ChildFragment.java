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
import android.text.InputType;
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

import com.aloknath.splitmytrip.Database.TripDataSource;
import com.aloknath.splitmytrip.Objects.Person;
import com.aloknath.splitmytrip.Objects.TripItem;
import com.aloknath.splitmytrip.R;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.aloknath.splitmytrip.Adapters.ExpandableBaseAdapter.round;

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
    private Button update_item_cost;
    private double person_amount_paid;
    private double item_amount_edited;
    private EditText editTextPerson;
    private EditText editTextItem;
    private TextView item_name;
    private TextView cost;
    private List<Person> personList;
    private TripDataSource tripDataSource;
    private HashMap<String, Double> persons_paid = new HashMap<>();
    private List<Person> persons_paid_list = new ArrayList<>();


    public interface  onChildEvent {
        public void amountPaid(String tripName, String from, String to, double amount);
        //public void editPerson(Person person, double amount, List<HashMap<String, Object>> result);
        public void editItem(TripItem item, List<Person> persons_paid_list, double amount);
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

        tripDataSource = new TripDataSource(getActivity());
        tripDataSource.open();



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
            textView.setText("" + String.valueOf(round(personsList.get(position).getAmountToGet(),2)));
            amountToGet = personsList.get(position).getAmountToGet();

            textView = (TextView)root.findViewById(R.id.edit_amount_owed);
            textView.setText("" + String.valueOf(round(personsList.get(position).getAmountOwed(),2)));
            amountOwed = personsList.get(position).getAmountOwed();

            textView = (TextView)root.findViewById(R.id.edit_amount_paid);
            textView.setText("" + String.valueOf(round(personsList.get(position).getAmountPaid(),2)));

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

            update_item_cost = (Button)root.findViewById(R.id.update_item_cost);


            update_item_cost.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Open up a dialog Box that Shows the amount to be added to the existing amount
                    // Display the person's names along with the edit text in their side and a submit button
                    // Check if the amount entered for the item equals the sum of amounts paid by all.
                    // If true, update the database on a separate thread.

                    LayoutInflater inflater = getLayoutInflater(null);
                    View dialoglayout = inflater.inflate(R.layout.alert_dialog_item_update, null);

                    alertDialog.setTitle("ITEM TO UPDATE");
                    alertDialog.setView(dialoglayout);
                    alertDialog.show();

                    item_name = (TextView)dialoglayout.findViewById(R.id.item_name);
                    cost = (TextView)dialoglayout.findViewById(R.id.cost);

                    item_name.setText(tripItemList.get(position).getItemName());
                    cost.setText(String.valueOf(tripItemList.get(position).getItemCost()));

                    personList = tripDataSource.getPersonsInTrip(tripItemList.get(position).getTripName());
                    tripDataSource.close();

                    LinearLayout linearLayout = (LinearLayout)dialoglayout.findViewById(R.id.linearlayout);

                    TextView textView2;
                    LinearLayout.LayoutParams layoutParams;
                    EditText editText;
                    final EditText cost_entered;
                    Button update_cost;
                    final int[] i = {0};
                    final List<EditText> allEds = new ArrayList<>();

                    update_cost = (Button)dialoglayout.findViewById(R.id.button_update_item_cost);
                    cost_entered = (EditText)dialoglayout.findViewById(R.id.edit_item_amount);

                    for(Person person: personList){

                        textView2 = new TextView(getActivity());
                        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.gravity = Gravity.TOP;
                        layoutParams.setMargins(75, 10, 10, 10); // (left, top, right, bottom)
                        textView2.setLayoutParams(layoutParams);
                        textView2.setText(person.getName());
                        textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        textView2.setTextColor(Color.BLACK);
                        linearLayout.addView(textView2);

                        editText = new EditText(getActivity());
                        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.gravity = Gravity.TOP;
                        layoutParams.setMargins(75, 10, 10, 10);
                        editText.setLayoutParams(layoutParams);
                        editText.setHint("Amount Paid");
                        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        editText.setBackgroundColor(Color.CYAN);
                        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                        editText.setId(i[0]);
                        linearLayout.addView(editText);
                        allEds.add(editText);
                        i[0]++;
                    }

                    final double[] totalcost = {0.0};

                    update_cost.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            totalcost[0] = 0.0;

                            for(i[0] =0; i[0] < allEds.size(); i[0]++){

                                if(allEds.get(i[0]).getText().toString().equals("")) {
                                    Log.i("Empty String", " The Person Cost was empty");
                                }else{

                                    Person person = personList.get(i[0]);
                                    Log.i("The Person name: ", person.getName() + String.valueOf(person.getAmountPaid()));
                                    if(allEds.get(i[0]).getText().toString().isEmpty()){
                                        //Do Nothing
//                                        person.setAmountPaid(person.getAmountPaid() + 0.0);
                                    }else{
                                        person.setAmountPaid(person.getAmountPaid() + Double.parseDouble(allEds.get(i[0]).getText().toString()));
                                    }

                                    persons_paid_list.add(person);
                                    totalcost[0] += Double.parseDouble(allEds.get(i[0]).getText().toString());
                                }
                            }

                            if(cost_entered.getText().toString().equals("")){
                                Log.i("Empty String", " The Item Cost was empty");

                            }else{

                                if(Double.parseDouble(cost_entered.getText().toString()) !=  totalcost[0]){

                                    Log.i(" The Amount ", " Total entered Amount" +
                                            String.valueOf( totalcost[0]) + " Does not Match the updated Item's Cost: " + cost.getText().toString());


                                }else{
                                    // Pass In The Updated Item Cost
                                    // Pass In A Map of person Name and the amount paid
                                    TripItem item = tripItemList.get(position);
                                    childEventListener.editItem(item,  persons_paid_list, Double.parseDouble(cost_entered.getText().toString()));

                                }
                            }

                        }
                    });

                }
            });


        }

        return root;
    }

    private Bitmap setItemImage(String imageName) {

        imageName = imageName.trim();
        InputStream inputStream = null;
        Bitmap imageReturned = null;

        if(imageName.equalsIgnoreCase("travel")){

            try {
                inputStream = getActivity().getAssets().open("travel.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equalsIgnoreCase("adventure")){

            try {
                inputStream = getActivity().getAssets().open("adventure.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equalsIgnoreCase("flight")||imageName.equalsIgnoreCase("plane")||imageName.equalsIgnoreCase("airplane")||imageName.equalsIgnoreCase("aeroplane")){

            try {
                inputStream = getActivity().getAssets().open("flight.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }
        else if(imageName.equalsIgnoreCase("hiking")||imageName.equalsIgnoreCase("trekking")){

            try {
                inputStream = getActivity().getAssets().open("hiking.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equalsIgnoreCase("food")){

            try {
                inputStream = getActivity().getAssets().open("food.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equalsIgnoreCase("water")){

            try {
                inputStream = getActivity().getAssets().open("water.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equalsIgnoreCase("hotel")||imageName.equalsIgnoreCase("motel")){

            try {
                inputStream = getActivity().getAssets().open("hotel.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);


        }else if(imageName.equalsIgnoreCase("bar")||imageName.equalsIgnoreCase("drinks")||imageName.equalsIgnoreCase("disco")||imageName.equalsIgnoreCase("pub")||imageName.equalsIgnoreCase("drink")){

            try {
                inputStream = getActivity().getAssets().open("bar.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equalsIgnoreCase("fuel")||imageName.equalsIgnoreCase("car")||imageName.equalsIgnoreCase("gas")){

            try {
                inputStream = getActivity().getAssets().open("fuel.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equalsIgnoreCase("museum")||imageName.equalsIgnoreCase("gallery")){

            try {
                inputStream = getActivity().getAssets().open("museum.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equalsIgnoreCase("aquarium")){

            try {
                inputStream = getActivity().getAssets().open("aquarium.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equalsIgnoreCase("park")){

            try {
                inputStream = getActivity().getAssets().open("park.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equalsIgnoreCase("train")){

            try {
                inputStream = getActivity().getAssets().open("train.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equalsIgnoreCase("bus")){

            try {
                inputStream = getActivity().getAssets().open("bus.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equalsIgnoreCase("shopping")){

            try {
                inputStream = getActivity().getAssets().open("shopping.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);

        }else if(imageName.equalsIgnoreCase("beach")){

            try {
                inputStream = getActivity().getAssets().open("beach.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);
        } else{
            try {
                inputStream = getActivity().getAssets().open("default_item.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageReturned = BitmapFactory.decodeStream(bufferedInputStream);
        }

        return imageReturned;
    }



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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
            // Person Recipient get money from Person Sender

            Iterator iterator = result.iterator();

            while (iterator.hasNext()) {

                HashMap<String, Object> mapReturned = (HashMap<String, Object>) iterator.next();
                sender = (Person) mapReturned.get("from");
                recipient = (Person) mapReturned.get("to");
                amount = (Double) mapReturned.get("amount");
                amount = Math.round(amount*100)/100.0d;

                Log.i("Sender Details from child fragment: ", sender.toString());
                Log.i("Recipient Details from child fragment: ", recipient.toString());


                if(person.getName().equals(recipient.getName())){

                    textView2 = new TextView(getActivity());
                    layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.gravity = Gravity.TOP;
                    layoutParams.setMargins(10, 10, 10, 10); // (left, top, right, bottom)
                    textView2.setLayoutParams(layoutParams);
                    textView2.setText(recipient.getName() + " has to get " + String.valueOf(amount) + " from " + sender.getName());
                    textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    textView2.setTextColor(Color.BLACK);
                    linearLayout.addView(textView2);

                }
            }

        }else if (amountOwed > 0){

            //get the person from the list
            person = personsList.get(position);

            // Person X has to pay money to Person Y
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
                    textView2.setText(sender.getName() + " has to give " + String.valueOf(amount) + " to " + recipient.getName());
                    textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    textView2.setTextColor(Color.BLACK);
                    linearLayout.addView(textView2);

                    give = new Button(getActivity());
                    layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(50, 10, 10, 10);
                    give.setLayoutParams(layoutParams);

                    InputStream ims = null;
                    try {
                        ims = getActivity().getAssets().open("button_give_amount.png");
                        Drawable d = Drawable.createFromStream(ims, null);
                        give.setBackground(d);
                    }catch (IOException e) {
                        e.printStackTrace();
                    }

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
    public void onResume() {
        super.onResume();
        tripDataSource.open();
    }

    @Override
    public void onPause() {
        super.onPause();
        tripDataSource.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tripDataSource.close();
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(v.getContext(), "Clicked Position: " + position, Toast.LENGTH_LONG).show();
    }
}

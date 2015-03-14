package com.aloknath.splitmytrip.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aloknath.splitmytrip.Objects.Person;
import com.aloknath.splitmytrip.Objects.TripItem;
import com.aloknath.splitmytrip.R;

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

    public static ChildFragment newInstance(Bundle args) {
        ChildFragment fragment = new ChildFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        position = getArguments().getInt(POSITION_KEY);
        groupPosition = getArguments().getInt("id");
        View root = null;
        TextView textView;

        if (groupPosition != 0) {

            root = inflater.inflate(R.layout.fragment_child_person, container, false);

            personsList = (HashMap<Integer, Person>) getArguments().getSerializable("hashMap");
            result = (List<HashMap<String, Object>>) getArguments().getSerializable("price_split");

            textView = (TextView)root.findViewById(R.id.person_name);
            textView.setText(personsList.get(position).getName());

            textView = (TextView)root.findViewById(R.id.edit_amount_to_get);
            textView.setText("$" + String.valueOf(personsList.get(position).getAmountToGet()));
            amountToGet = personsList.get(position).getAmountToGet();

            textView = (TextView)root.findViewById(R.id.edit_amount_owed);
            textView.setText("$" + String.valueOf(personsList.get(position).getAmountOwed()));
            amountOwed = personsList.get(position).getAmountOwed();

            textView = (TextView)root.findViewById(R.id.edit_amount_paid);
            textView.setText("$" + String.valueOf(personsList.get(position).getAmountPaid()));

            Split(root, position);
        }
        else{

            root = inflater.inflate(R.layout.fragment_child_item, container, false);

            tripItemList = (HashMap<Integer, TripItem>) getArguments().getSerializable("hashMap");


            textView = (TextView)root.findViewById(R.id.item_name);
            textView.setText(tripItemList.get(position).getItemName());

            textView = (TextView)root.findViewById(R.id.edit_trip_name);
            textView.setText(tripItemList.get(position).getTripName());

            textView = (TextView)root.findViewById(R.id.edit_item_cost);
            textView.setText(String.valueOf(tripItemList.get(position).getItemCost()));

        }



        return root;
    }

    private void Split(View root, int position) {
        LinearLayout linearLayout = (LinearLayout)root.findViewById(R.id.linearlayout);

        TextView textView2;
        LinearLayout.LayoutParams layoutParams;
        Person person;
        Person sender;
        Person recipient;
        double amount;

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

                if(person.getName().equals(recipient.getName())){

                    textView2 = new TextView(getActivity());
                    layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.gravity = Gravity.CENTER;
                    layoutParams.setMargins(10, 10, 10, 10); // (left, top, right, bottom)
                    textView2.setLayoutParams(layoutParams);
                    textView2.setText(recipient.getName() + " has to get $" + String.valueOf(amount) + " from " + sender.getName());
                    textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    textView2.setBackgroundColor(0xffffdbdb);
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

                if (person.getName().equals(sender.getName())) {

                    textView2 = new TextView(getActivity());
                    layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.gravity = Gravity.CENTER;
                    layoutParams.setMargins(10, 10, 10, 10); // (left, top, right, bottom)
                    textView2.setLayoutParams(layoutParams);
                    textView2.setText(sender.getName() + " has to give $" + String.valueOf(amount) + " to " + recipient.getName());
                    textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    textView2.setBackgroundColor(0xffffdbdb);
                    linearLayout.addView(textView2);

                }
            }

        }

    }

    @Override
    public void onClick(View v) {
        Toast.makeText(v.getContext(), "Clicked Position: " + position, Toast.LENGTH_LONG).show();
    }
}

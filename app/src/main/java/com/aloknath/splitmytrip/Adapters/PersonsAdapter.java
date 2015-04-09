package com.aloknath.splitmytrip.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aloknath.splitmytrip.CustomViews.RoundedImageView;
import com.aloknath.splitmytrip.Objects.Person;
import com.aloknath.splitmytrip.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ALOKNATH on 3/20/2015.
 */
public class PersonsAdapter  extends ArrayAdapter<Person> {

    private final Context context;
    private final List<Person> personList;
    private HashMap<Integer, Bitmap> personsHashMap = new HashMap<>();

    public PersonsAdapter(Context context, int resource, List<Person> personList){
        super(context, resource, personList);
        this.context = context;
        this.personList = personList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Person person = personList.get(position);

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.person_list_display, null);

            ViewHolderPerson viewHolderPerson = new ViewHolderPerson();
            viewHolderPerson.imageView = (RoundedImageView)convertView.findViewById(R.id.imageView2);
            viewHolderPerson.amount_owed = (TextView) convertView.findViewById(R.id.edit_amount_owed);
            viewHolderPerson.amount_paid = (TextView) convertView.findViewById(R.id.edit_amount_paid);
            viewHolderPerson.amount_to_get = (TextView) convertView.findViewById(R.id.edit_amount_to_get);
            viewHolderPerson.personName = (TextView) convertView.findViewById(R.id.edit_person_name);
            convertView.setTag(viewHolderPerson);

            ViewHolderPerson holderPerson = (ViewHolderPerson)convertView.getTag();

            if(personsHashMap.get(position) == null) {

                holderPerson.imageView.setImageBitmap(person.getPersonImage());
                personsHashMap.put(position, person.getPersonImage());

            }else{
                holderPerson.imageView.setImageBitmap(personsHashMap.get(position));
            }

            holderPerson.personName.setText(person.getName());
            holderPerson.amount_paid.setText("" + String.valueOf(person.getAmountPaid()));
            holderPerson.amount_owed.setText("" + String.valueOf(person.getAmountOwed()));
            holderPerson.amount_to_get.setText("" + String.valueOf(person.getAmountToGet()));

        }
        return convertView;
    }

    private static class ViewHolderPerson{
        private RoundedImageView imageView;
        private TextView personName;
        private TextView amount_paid;
        private TextView amount_owed;
        private TextView amount_to_get;
    }
}

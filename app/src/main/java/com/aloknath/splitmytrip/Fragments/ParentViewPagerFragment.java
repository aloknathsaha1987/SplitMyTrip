package com.aloknath.splitmytrip.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aloknath.splitmytrip.Objects.Person;
import com.aloknath.splitmytrip.Objects.TripItem;
import com.aloknath.splitmytrip.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ALOKNATH on 3/14/2015.
 */

public class ParentViewPagerFragment extends Fragment {

    private static int count;
    private static int id;
    private static int childPosition;
    private static  HashMap<Integer, Person> personsList;
    private static  HashMap<Integer, TripItem> tripItemList;
    private static List<HashMap<String, Object>> result;

    public static final String TAG = ParentViewPagerFragment.class.getName();

    public static ParentViewPagerFragment newInstance(Bundle args) {
        ParentViewPagerFragment fragment = new ParentViewPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_parent_viewpager, container, false);
        count = getArguments().getInt("count");
        id = getArguments().getInt("groupPosition");
        childPosition = getArguments().getInt("childPosition");
        if(id == 0){
            tripItemList = (HashMap<Integer, TripItem>) getArguments().getSerializable("hashMap");
        }
        else {
            personsList = (HashMap<Integer, Person>) getArguments().getSerializable("hashMap");
            result = (List<HashMap<String, Object>>) getArguments().getSerializable("price_split");
        }
        ViewPager viewPager = (ViewPager) root.findViewById(R.id.viewPager);
        /** Important: Must use the child FragmentManager or you will see side effects. */
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
        viewPager.setCurrentItem(childPosition);

        return root;
    }

    public static class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            //Return the number of persons or items -- Check
            return count;
        }

        @Override
        public Fragment getItem(int position) {
            //Add to the Bundle the Details of the Child Fragment that is to be displayed
            Bundle args = new Bundle();
            args.putSerializable("price_split", (java.io.Serializable) result);
            if(id == 0){
                args.putSerializable("hashMap", tripItemList);
            }else{
                args.putSerializable("hashMap", personsList);
            }
            args.putInt("id", id);
            args.putInt(ChildFragment.POSITION_KEY, position);
            return ChildFragment.newInstance(args);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //Set the Page Title to the Item's Name or the Person's Name -- Check
            String title;
            if(id == 0){
                title = (tripItemList.get(position)).getItemName();
            }else{
                title = (personsList.get(position)).getName();
            }
            return title;
        }

    }
}

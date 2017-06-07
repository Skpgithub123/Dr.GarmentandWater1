package com.uohmac.com.drgarmentandwater.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.uohmac.com.drgarmentandwater.R;

/**
 * Created by System-03 on 10/14/2016.
 */
public class MonthlyPackFragment extends Fragment {

    View myFragmentView;
    Spinner spvendoritems,sptimeslots;


    public MonthlyPackFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Monthly Pack");

        myFragmentView = inflater.inflate(R.layout.fragmentmonthlypack, container, false);
        spvendoritems = (Spinner)myFragmentView.findViewById(R.id.sp_vendoritems);
        sptimeslots = (Spinner)myFragmentView.findViewById(R.id.sp_timeslots);

        String[] vendoritems = {"Select", "Bisleri", "Kinley","Local"};
        String[] timeslots = {"Select"};
        spvendoritems.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,vendoritems));
        sptimeslots.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,timeslots));


        return myFragmentView;
    }
}

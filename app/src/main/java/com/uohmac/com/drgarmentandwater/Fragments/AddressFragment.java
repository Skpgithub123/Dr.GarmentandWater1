package com.uohmac.com.drgarmentandwater.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uohmac.com.drgarmentandwater.R;

/**
 * Created by SUNIL on 9/17/2016.
 */
public class AddressFragment extends Fragment {

     View myFragmentView;
    Toolbar toolbar;

    public AddressFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
        }
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Address");
        myFragmentView = inflater.inflate(R.layout.fragmentaddress, container, false);


        return myFragmentView;
    }


}

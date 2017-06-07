package com.uohmac.com.drgarmentandwater.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.uohmac.com.drgarmentandwater.R;

/**
 * Created by SUNIL on 9/15/2016.
 */
public class HomeFragment extends Fragment {

    private View myFragmentView;

    public HomeFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.fragmenthome, container, false);

       // HomeClass homescreen = (HomeClass) getActivity();

        return  myFragmentView;
    }
}

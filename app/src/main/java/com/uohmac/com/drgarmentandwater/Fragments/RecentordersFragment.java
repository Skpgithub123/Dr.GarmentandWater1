package com.uohmac.com.drgarmentandwater.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uohmac.com.drgarmentandwater.R;

/**
 * Created by SUNIL on 9/17/2016.
 */
public class RecentordersFragment extends Fragment {

    private View myFragmentView;

    public RecentordersFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Recent Orders");
        myFragmentView = inflater.inflate(R.layout.fragmentrecentorders, container, false);

        return myFragmentView;
    }
}

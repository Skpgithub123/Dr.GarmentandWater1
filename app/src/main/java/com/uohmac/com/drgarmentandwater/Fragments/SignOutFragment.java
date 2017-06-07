package com.uohmac.com.drgarmentandwater.Fragments;

/**
 * Created by SUNIL on 9/17/2016.
 */
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uohmac.com.drgarmentandwater.LoginActivity;
import com.uohmac.com.drgarmentandwater.R;

public class SignOutFragment extends Fragment {

    private View myFragmentView;

    public SignOutFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        AlertDialog.Builder ab = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
        ab.setTitle("Logout");
        ab.setMessage("Are you sure, you want to exit?");
        ab.setCancelable(false);
        ab.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /*Intent i_signout = new Intent(getActivity(), LoginActivity.class);
                SharedPreferences preferences =getActivity().getSharedPreferences("mysharedpref_waterlaundry", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                getActivity().startActivity(i_signout);
                getActivity().finish();*/
                SharedPreferences preferences =getActivity().getSharedPreferences("mysharedpref_waterlaundry", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear().commit();

                editor.putString("logout", "logout");
                editor.commit();
                Intent intent = new Intent(getActivity(), LoginActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().finish();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        });
        ab.create();
        ab.show();

        return myFragmentView;
    }

    }


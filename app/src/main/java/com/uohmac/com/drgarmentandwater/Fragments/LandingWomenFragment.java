package com.uohmac.com.drgarmentandwater.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.uohmac.com.drgarmentandwater.Adpater.MenAdapter;
import com.uohmac.com.drgarmentandwater.Adpater.WomenAdapter;
import com.uohmac.com.drgarmentandwater.Api.UohmacAPI;
import com.uohmac.com.drgarmentandwater.Pojos.DryCleaningPojo;
import com.uohmac.com.drgarmentandwater.R;
import com.uohmac.com.drgarmentandwater.Utils.AppNetworkInfo;
import com.uohmac.com.drgarmentandwater.Utils.Constatns;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by System-03 on 11/4/2016.
 */
public class LandingWomenFragment extends Fragment {

    private View myFragmentwomenView;
    List<DryCleaningPojo> dryCleaningPojoList;
    WomenAdapter _WomenAdapter;
    ListView rvWomenRequest;
    ProgressDialog mProgressDialog;

    public static Fragment newInstance(Bundle args) {
        LandingWomenFragment fragment = new LandingWomenFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public LandingWomenFragment() {


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myFragmentwomenView = inflater.inflate(R.layout.fragmentwomen, container, false);
        rvWomenRequest = (ListView) myFragmentwomenView.findViewById(R.id.lvWomenListt);


        if(AppNetworkInfo.isConnectingToInternet(getActivity())){
            getdrycleaningdataforwomen();

        } else{
            Toast.makeText(getActivity(), "No network found.!", Toast.LENGTH_SHORT).show();
        }

        return myFragmentwomenView;
    }

    private void getdrycleaningdataforwomen(){
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.show();
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_drycleaningdataforwomen = adapter.create(UohmacAPI.class);

        api_drycleaningdataforwomen.GetDryCleaningDetails("5", new Callback<List<DryCleaningPojo>>() {
            @Override
            public void success(List<DryCleaningPojo> dryCleaningPojo, Response response) {
                dryCleaningPojoList = dryCleaningPojo;

                if(getActivity() != null) {
                    _WomenAdapter = new WomenAdapter(getActivity(), R.layout.womensingleitem, dryCleaningPojo);
                    rvWomenRequest.setAdapter(_WomenAdapter);
                }

                mProgressDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.e("Errorfound", "ERROR:" + error.getMessage(), error);

            }
        });
    }
}

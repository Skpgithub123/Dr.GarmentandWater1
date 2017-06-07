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

import com.uohmac.com.drgarmentandwater.Adpater.KidAdapter;
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
public class LandingKidsFragment extends Fragment {

    private View myFragmentkidView;
    List<DryCleaningPojo> dryCleaningPojoList;
    KidAdapter _KidAdapter;
    ListView rvKidRequest;
    ProgressDialog mProgressDialog;

    public static Fragment newInstance(Bundle args) {
        LandingKidsFragment fragment = new LandingKidsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public LandingKidsFragment() {


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myFragmentkidView = inflater.inflate(R.layout.fragmentkid, container, false);
        rvKidRequest = (ListView) myFragmentkidView.findViewById(R.id.lvKidList);



        if(AppNetworkInfo.isConnectingToInternet(getActivity())){
            getdrycleaningdataforkid();

        } else{
            Toast.makeText(getContext(), "No network found.!", Toast.LENGTH_SHORT).show();
        }

        return myFragmentkidView;
    }

    private void getdrycleaningdataforkid(){
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.show();
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_drycleaningdataforwomen = adapter.create(UohmacAPI.class);

        api_drycleaningdataforwomen.GetDryCleaningDetails("6", new Callback<List<DryCleaningPojo>>() {
            @Override
            public void success(List<DryCleaningPojo> dryCleaningPojo, Response response) {
                dryCleaningPojoList = dryCleaningPojo;

                if(getActivity() != null) {
                    _KidAdapter = new KidAdapter(getActivity(), R.layout.kidsingleitem, dryCleaningPojo);
                    rvKidRequest.setAdapter(_KidAdapter);
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

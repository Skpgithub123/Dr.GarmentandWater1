/*
package com.uohmac.com.drgarmentandwater.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.squareup.okhttp.Call;
import com.uohmac.com.drgarmentandwater.Activites.RecentOrderHistoryLaundry;
import com.uohmac.com.drgarmentandwater.Api.UohmacAPI;
import com.uohmac.com.drgarmentandwater.Pojos.DryCleaningPojo;
import com.uohmac.com.drgarmentandwater.Pojos.RecentOrderHistoryPojo;
import com.uohmac.com.drgarmentandwater.Pojos.RecentOrderPojo;
import com.uohmac.com.drgarmentandwater.R;
import com.uohmac.com.drgarmentandwater.Utils.AppNetworkInfo;
import com.uohmac.com.drgarmentandwater.Utils.Constatns;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

*/
/**
 * Created by uOhmac Technologies on 24-Jan-17.
 *//*

public class RecentOrderLaundryFragment extends Fragment {

    View view_recentorderwaterfragment;
    ListView rvMyRequest;
    SharedPreferences sp;
    String getauthkey,result_recentorders;
    JSONArray jsonArray_parent;
    JSONObject jsonObject_parent;
    JSONArray jsonArray_child;
    JSONObject jsonObject_child;
    RecentOrderLaundryAdapter _adapter;
    List<RecentOrderPojo> recentOrderPojoList;
    RecentOrderPojo recentOrderPojo;
   // VariableClass variableClass;
   // ArrayList<VariableClass>  arrayList = new ArrayList<>();
    //String itemname = "";
    //String itemqty = "";
    //String orderimage = "";

    public static String orderno,date,pickupdate,pickuptime,itemname,quantity,recentorderpic,totalorderamount,itemid,orderimage;
    ArrayList<String> arraylist_completedata = new ArrayList<>();
    public static final String MYPREFERNCES_LOGIN = "mysharedpref_waterlaundry";


    public static Fragment newInstance(Bundle args) {
        RecentOrderLaundryFragment fragment = new RecentOrderLaundryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public RecentOrderLaundryFragment() {


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getContext().getSharedPreferences(MYPREFERNCES_LOGIN, 0);
        getauthkey = sp.getString("auth_key", "");
        Log.e("getauthkeyforrecentlaundry", getauthkey);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view_recentorderwaterfragment = inflater.inflate(R.layout.fragmentrecentorderlaundry, container, false);


        rvMyRequest = (ListView) view_recentorderwaterfragment.findViewById(R.id.lvRecentOrdersLaundryList);

        if(AppNetworkInfo.isConnectingToInternet(getActivity())){
            getrecentorders();

        } else{
            AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
            ab.setMessage("No network found... Please try again");
            ab.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            ab.setNegativeButton("TryAgain", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    getrecentorders();

                }
            });
            ab.create();
            ab.show();
        }




        rvMyRequest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent i_orderhistory = new Intent(getActivity(), RecentOrderHistoryLaundry.class);
                i_orderhistory.putExtra("recentorderid", recentOrderPojoList.get(position).getId());
                i_orderhistory.putExtra("recentordertotalprice", recentOrderPojoList.get(position).getTotlaorderamount());
                i_orderhistory.putExtra("recentordernumber", recentOrderPojoList.get(position).getOrderno());
                i_orderhistory.putExtra("recentorderaddeddate", recentOrderPojoList.get(position).getDate());
                Log.e("getpos", recentOrderPojoList.get(position).getId());

                startActivity(i_orderhistory);
               // String pos = rvMyReques
                //Log.e("getpos", ""+pos);
            }
        });

        return view_recentorderwaterfragment;
    }


    private void getrecentorders(){
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();


        UohmacAPI api_recentorder = adapter.create(UohmacAPI.class);


        api_recentorder.RecentOrders(getauthkey, new Callback<List<RecentOrderPojo>>() {
            @Override
            public void success(List<RecentOrderPojo> recentOrderHistoryPojos, Response response2) {

                recentOrderPojoList = recentOrderHistoryPojos;

                //_adapter = new RecentOrderLaundryAdapter();
               // rvMyRequest.setAdapter(_adapter);


                    _adapter = new RecentOrderLaundryAdapter(getActivity(), R.layout.recentorderlaundrysingle_item,recentOrderPojoList);

                    rvMyRequest.setAdapter(_adapter);

                */
/*try {
                    jsonArray_parent = new JSONArray(result_recentorders);
                    Log.e("jsonarrayparentdata", "" + jsonArray_parent);
                    for(int i=0;i<jsonArray_parent.length();i++){
                        jsonObject_parent = jsonArray_parent.getJSONObject(i);
                        orderno = jsonObject_parent.getString("order_id");
                        Log.e("orderno", orderno);
                        totalorderamount = jsonObject_parent.getString("total_price");
                        Log.e("amount", totalorderamount);
                        pickupdate = jsonObject_parent.getString("pickup_date");
                        Log.e("pickdate",pickupdate);
                        pickuptime = jsonObject_parent.getString("pickup_time");
                        Log.e("picktime",pickuptime);
                        date = jsonObject_parent.getString("added_date");
                        Log.e("dt",date);


                        jsonArray_child = jsonObject_parent.getJSONArray("item_des");


                        for(int j=0; j<jsonArray_child.length();j++){
                            jsonObject_child = jsonArray_child.getJSONObject(j);
                            itemname = jsonObject_child.getString("item_name");
                            Log.e("itemname",itemname);

                            quantity = jsonObject_child.getString("item_quantity");
                            Log.e("quantity",quantity);

                            recentorderpic = jsonObject_child.getString("item_img");
                            Log.e("recentorderpic",recentorderpic);

                            itemid = jsonObject_child.getString("item_id");
                            Log.e("itemid", itemid);


                        }

                        arraylist_completedata.add(jsonArray_parent.toString());

                    }


                    _adapter = new RecentOrderLaundryAdapter(getActivity(),R.layout.recentorderlaundrysingle_item,arraylist_completedata);
                    rvMyRequest.setAdapter(_adapter);


                }catch (Exception e){
                    e.printStackTrace();
                }*//*

            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("ERROR:", error.getMessage());

            }
        });
    }
}
*/

package com.uohmac.com.drgarmentandwater.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.uohmac.com.drgarmentandwater.Activites.OrderHistoryDetails;
import com.uohmac.com.drgarmentandwater.Api.UohmacAPI;
import com.uohmac.com.drgarmentandwater.Pojos.PaymenthistoryVariable;
import com.uohmac.com.drgarmentandwater.R;
import com.uohmac.com.drgarmentandwater.Utils.AppNetworkInfo;
import com.uohmac.com.drgarmentandwater.Utils.Constatns;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class WaterMY_Order extends Fragment {

    public static final String MYPREFERNCES_LOGIN = "mysharedpref_waterlaundry";
    SharedPreferences sp;
    String getauthkey;
    public WaterMY_Order() {
        // Required empty public constructor
    }



    ListView listView_watermyorder;

    ArrayList<PaymenthistoryVariable> arrayList_water;

    WatermyorderAdapter watermyorderAdapter;

    ProgressDialog progressDialog;
    TextView textView_id_water,watermyordertextview;

    PaymenthistoryVariable paymenthistoryVariable;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sp = getActivity().getSharedPreferences(MYPREFERNCES_LOGIN, 0);
        getauthkey = sp.getString("auth_key", "");

        View v =  inflater.inflate(R.layout.water_my__order_fragment, container, false);





        arrayList_water = new ArrayList<PaymenthistoryVariable>();
        listView_watermyorder = (ListView) v.findViewById(R.id.watermyorderhistory);
        watermyordertextview = (TextView)v.findViewById(R.id.watermyordertextview);

        listView_watermyorder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                paymenthistoryVariable = arrayList_water.get(position);

                Log.d("valuesfsadfsfsadf", "paymenthistoryVariable" + paymenthistoryVariable.getId());

                Intent intent = new Intent(getActivity(), OrderHistoryDetails.class);
                intent.putExtra("ids", paymenthistoryVariable.getWater_id());
                intent.putExtra("activity", "water");
                startActivity(intent);


            }
        });



        if(AppNetworkInfo.isConnectingToInternet(getActivity())){
                Fetch_Watermyorderhistory();
        }else{
            Toast.makeText(getActivity(), "No network found.!", Toast.LENGTH_SHORT).show();
        }



        return v;
    }


    public  void Fetch_Watermyorderhistory()
    {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        arrayList_water.clear();
        progressDialog.show();


      /*  Log.d("auth_keyquizzzzz", "'''''   " + quiz_auth);
        Log.d("testid==quizzzz", "'''''  " + quiz_topicid);*/

        //  Log.d("numberincrease", "+++++++   " + Number);

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API) //Setting the Root URL
                .build();
        UohmacAPI WeekTest_Quiz = adapter.create(UohmacAPI.class);



        WeekTest_Quiz.Get_Orderhistory(getauthkey, new Callback<Response>() {

            @Override
            public void success(Response response, Response response2) {

                BufferedReader reader = null;
                String result = "";

                try {
                    //Initializing buffered reader
                    reader = new BufferedReader(new InputStreamReader(response.getBody().in()));

                    //Reading the output in the string
                    result = reader.readLine();


                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Displaying the output as a toast
                Log.d("watter_txn...", "==== 1...   " + result);


                try {
                    JSONObject jsonObject = new JSONObject(result.toString());

                    String status = jsonObject.getString("status");

                    if (status.equals("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("watter_txn");

                        if (jsonArray.length() > 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                paymenthistoryVariable = new PaymenthistoryVariable();

                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                paymenthistoryVariable.setWater_id(jsonObject1.getString("id"));
                                paymenthistoryVariable.setWater_amount(jsonObject1.getString("total_price"));
                                paymenthistoryVariable.setWater_txn_no(jsonObject1.getString("order_id"));
                                paymenthistoryVariable.setWater_created_date(jsonObject1.getString("added_date"));
                                paymenthistoryVariable.setWater_status(jsonObject1.getString("status"));

                                arrayList_water.add(paymenthistoryVariable);


                            }

                            Log.d("wateraryarray", "size" + arrayList_water.size());
                            if (getActivity() != null) {
                                watermyorderAdapter = new WatermyorderAdapter();
                                listView_watermyorder.setAdapter(watermyorderAdapter);
                            }
                            progressDialog.dismiss();
                        }else{
                            progressDialog.dismiss();
                            listView_watermyorder.setVisibility(View.GONE);
                            watermyordertextview.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(), "Data Not Available", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                progressDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "Please try after sometime.", Toast.LENGTH_SHORT).show();


                progressDialog.dismiss();
            }
        });
    }

    class WatermyorderAdapter extends BaseAdapter
    {
        PaymenthistoryVariable paymenthistoryVariable;
        @Override
        public int getCount() {
            return arrayList_water.size();

        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            paymenthistoryVariable = arrayList_water.get(position);

            convertView = getActivity().getLayoutInflater().inflate(R.layout.waterorderchilditem, null);

            textView_id_water = (TextView) convertView.findViewById(R.id.Water_id);
            TextView textView_status_water = (TextView) convertView.findViewById(R.id.Water_status);
            TextView textView_amount_water = (TextView) convertView.findViewById(R.id.Water_amount);
            TextView textView_transno_water = (TextView) convertView.findViewById(R.id.Water_transactionno);
            TextView textView_createdate_water = (TextView) convertView.findViewById(R.id.Water_careatedate);

            Log.d("valuresare","***"+paymenthistoryVariable.getWater_status());
            Log.d("getWater_id", "***" + paymenthistoryVariable.getWater_id());

            textView_id_water.setText(paymenthistoryVariable.getWater_id());
            textView_status_water.setText(paymenthistoryVariable.getWater_status());
            textView_amount_water.setText("\u20B9"+paymenthistoryVariable.getWater_amount());
            textView_transno_water.setText("Order id:"+paymenthistoryVariable.getWater_txn_no());
            textView_createdate_water.setText(paymenthistoryVariable.getWater_created_date());






            return convertView;
        }
    }

}

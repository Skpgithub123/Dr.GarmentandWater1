package com.uohmac.com.drgarmentandwater.Fragments;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


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
public class MyWallet_History extends Fragment {



    public static final String MYPREFERNCES_LOGIN = "mysharedpref_waterlaundry";
    SharedPreferences sp;
    String getauthkey;
    TextView walletmyordertextview;
    public MyWallet_History() {
        // Required empty public constructor
    }

    ListView listView;

    ArrayList<PaymenthistoryVariable> arrayList;

    MywalletAdapter mywalletAdapter;

    ProgressDialog progressDialog;

    TextView textView_id;
    PaymenthistoryVariable paymenthistoryVariable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sp = getActivity().getSharedPreferences(MYPREFERNCES_LOGIN, 0);
        getauthkey = sp.getString("auth_key", "");
        Log.e("authkey_myorderswallet", getauthkey);
        View v = inflater.inflate(R.layout.my_wallet_fragment, container, false);




        arrayList = new ArrayList<PaymenthistoryVariable>();
        listView = (ListView) v.findViewById(R.id.wallet_recycler_view);
        walletmyordertextview = (TextView)v.findViewById(R.id.walletmyordertextview);

        if(AppNetworkInfo.isConnectingToInternet(getActivity())) {
            Fetch_Wallethistory();
        }else{
            Toast.makeText(getActivity(), "No network found please try again...", Toast.LENGTH_SHORT).show();
        }

        return v;

    }

    public  void Fetch_Wallethistory()
    {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        arrayList.clear();
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
                Log.d("quizzzz...", "==== 1...   " + result);


                try {
                    JSONObject jsonObject = new JSONObject(result.toString());

                    String status = jsonObject.getString("status");

                    if (status.equals("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("wallet_txn");


                        if (jsonArray.length() > 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                paymenthistoryVariable = new PaymenthistoryVariable();

                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                paymenthistoryVariable.setId(jsonObject1.getString("id"));
                                paymenthistoryVariable.setAmount(jsonObject1.getString("amount"));
                                paymenthistoryVariable.setTxn_no(jsonObject1.getString("txn_no"));
                                paymenthistoryVariable.setCreated_date(jsonObject1.getString("created_date"));
                                paymenthistoryVariable.setStatus(jsonObject1.getString("status"));

                                arrayList.add(paymenthistoryVariable);


                            }

                            Log.d("wallethistaryarray", "size" + arrayList.size());
                            if (getActivity() != null) {
                                mywalletAdapter = new MywalletAdapter();
                                listView.setAdapter(mywalletAdapter);
                            }
                            progressDialog.dismiss();
                        } else {
                            progressDialog.dismiss();
                            listView.setVisibility(View.GONE);
                            walletmyordertextview.setVisibility(View.VISIBLE);
                            //Toast.makeText(getActivity(), "Data Not Available", Toast.LENGTH_SHORT).show();
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

    class MywalletAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return arrayList.size();

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

            paymenthistoryVariable = arrayList.get(position);

            convertView = getActivity().getLayoutInflater().inflate(R.layout.walletchielditem, null);

            textView_id = (TextView) convertView.findViewById(R.id.wallet_id);
            TextView textView_status = (TextView) convertView.findViewById(R.id.wallet_status);
            TextView textView_amount = (TextView) convertView.findViewById(R.id.wallet_amount);
            TextView textView_transno = (TextView) convertView.findViewById(R.id.walley_transactionno);
            TextView textView_createdate = (TextView) convertView.findViewById(R.id.wallet_careatedate);


            textView_id.setText(paymenthistoryVariable.getId());
            textView_status.setText(paymenthistoryVariable.getStatus());
            textView_amount.setText("\u20B9"+paymenthistoryVariable.getAmount());
            textView_transno.setText("Order id:"+paymenthistoryVariable.getTxn_no());
            textView_createdate.setText(paymenthistoryVariable.getCreated_date());


            return convertView;
        }
    }





}

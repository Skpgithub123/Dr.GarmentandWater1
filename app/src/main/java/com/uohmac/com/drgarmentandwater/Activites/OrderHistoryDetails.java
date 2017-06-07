package com.uohmac.com.drgarmentandwater.Activites;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;
import com.uohmac.com.drgarmentandwater.Api.UohmacAPI;
import com.uohmac.com.drgarmentandwater.Pojos.ReturncanDetails;
import com.uohmac.com.drgarmentandwater.Pojos.Vieworderdetails_Variables;
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

public class OrderHistoryDetails extends AppCompatActivity {

    ProgressDialog progressDialog;

    String ids="",water="",laundry="";

    public static final String MYPREFERNCES_LOGIN = "mysharedpref_waterlaundry";
    SharedPreferences sp;
    String getauthkey;
    Vieworderdetails_Variables vieworderdetails_variables;

    ArrayList<Vieworderdetails_Variables>  arrayList = new ArrayList<>();

    String promo_benefit="",grand_total_empty_can_amount="",grand_total_amount="";
    String returncan_enaableordisbale="";

    ListView listview_viewwaterfullorderdetails;


    ViewfullorderAdapterFor_Water myadapter;

    TextView promo_benefit_display,grand_total_empty_can_amount_display,grand_total_amount_display,vieworder_returncan_details;

    LinearLayout ll_promobenifit,ll_retruncandetails,ll_grandtotlaamount;


    ReturncanAdapter returncanAdapter;

    ReturncanDetails returncanDetails;

    ListView listview_returncandetails;

    ArrayList<ReturncanDetails> arrayList_returncanadapter = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_history_details);


        Intent i = getIntent();
        Bundle b = i.getExtras();
        ids = b.getString("ids");
        water= b.getString("activity");

        //intent.putExtra("activity","water");
        // intent.putExtra("activity","laundry");

        sp = getSharedPreferences(MYPREFERNCES_LOGIN, 0);
        getauthkey = sp.getString("auth_key", "");


        Log.d("idsidsidsidsids","****"+ids);
        listview_viewwaterfullorderdetails = (ListView) findViewById(R.id.listview_viewwaterfullorderdetails);

        promo_benefit_display = (TextView) findViewById(R.id.promo_benefit_display);
        grand_total_empty_can_amount_display = (TextView) findViewById(R.id.grand_total_empty_can_amount_display);
        grand_total_amount_display = (TextView) findViewById(R.id.grand_total_amount_display);


        vieworder_returncan_details = (TextView) findViewById(R.id.vieworder_returncan_details);






        ll_promobenifit = (LinearLayout) findViewById(R.id.ll_promobenifit);
        ll_retruncandetails = (LinearLayout) findViewById(R.id.ll_retruncandetails);
        ll_grandtotlaamount = (LinearLayout) findViewById(R.id.ll_grandtotlaamount);

        ll_promobenifit.setVisibility(View.GONE);
        ll_retruncandetails.setVisibility(View.VISIBLE);
        ll_grandtotlaamount.setVisibility(View.VISIBLE);

        if (water.equals("water"))
        {
            vieworder_returncan_details.setVisibility(View.VISIBLE);
            ll_retruncandetails.setVisibility(View.VISIBLE);
        }else
        {
            vieworder_returncan_details.setVisibility(View.GONE);
            ll_retruncandetails.setVisibility(View.GONE);
        }



        vieworder_returncan_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(OrderHistoryDetails.this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
                dialog.setContentView(R.layout.dialog_notification);
                dialog.show();


                listview_returncandetails = (ListView) dialog.findViewById(R.id.listview_returncandetails);

                if (AppNetworkInfo.isConnectingToInternet(OrderHistoryDetails.this)) {
                    Fetch_Returnncandetail();
                } else {
                    Toast.makeText(OrderHistoryDetails.this, "No network found.!! please try again", Toast.LENGTH_SHORT).show();
                }


            }
        });


        if(AppNetworkInfo.isConnectingToInternet(OrderHistoryDetails.this)){
            Fetch_Laundrymyorderhistory();
        }else{
            Toast.makeText(OrderHistoryDetails.this, "No network found.!! please try again", Toast.LENGTH_SHORT).show();
        }


    }


    public  void Fetch_Laundrymyorderhistory()
    {
        progressDialog = new ProgressDialog(OrderHistoryDetails.this);
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



        WeekTest_Quiz.View_Orderhistory_fulldetails(getauthkey,ids, new Callback<Response>() {

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
                Log.d("View_Orderhistory...", "==== 1...   " + result);


                try {



                    JSONObject jsonObject = new JSONObject(result.toString());


                    String status = jsonObject.getString("status");

                    if (status.equals("1"))
                    {

                        JSONObject jsonObject1= jsonObject.getJSONObject("result");


                        promo_benefit = jsonObject1.getString("promo_benefit");
                        grand_total_empty_can_amount = jsonObject1.getString("grand_total_empty_can_amount");
                        grand_total_amount = jsonObject1.getString("grand_total_amount");

                        //     promo_benefit_display

                        promo_benefit_display.setText(promo_benefit);
                        grand_total_empty_can_amount_display.setText(grand_total_empty_can_amount);
                        grand_total_amount_display.setText("\u20B9"+grand_total_amount);



                        JSONArray jsonArray = jsonObject1.getJSONArray("item_dtl");

                        if (jsonArray !=null)
                        {

                            for (int i=0;i<jsonArray.length();i++)
                            {

                                JSONObject jsonObject2 =jsonArray.getJSONObject(i);

                                vieworderdetails_variables = new Vieworderdetails_Variables();

                                vieworderdetails_variables.setItem_img(jsonObject2.getString("item_img"));
                                vieworderdetails_variables.setItem_name(jsonObject2.getString("item_name"));
                                vieworderdetails_variables.setItem_price(jsonObject2.getString("item_price"));
                                vieworderdetails_variables.setTotal_item_price(jsonObject2.getString("total_item_price"));
                                vieworderdetails_variables.setItem_quantity(jsonObject2.getString("item_quantity"));

                                arrayList.add(vieworderdetails_variables);
                            }

                            myadapter = new ViewfullorderAdapterFor_Water();

                            listview_viewwaterfullorderdetails.setAdapter(myadapter);


                        }else
                        {
                            Toast.makeText(OrderHistoryDetails.this,"Data Not Available",Toast.LENGTH_LONG).show();
                        }

                        JSONArray jsonArray_emptycan = jsonObject1.getJSONArray("empty_can_dtl");

                        if (jsonArray_emptycan !=null)
                        {
                            returncan_enaableordisbale = "Yes";

                        }else
                        {
                            returncan_enaableordisbale = "no";
                        }



                        // JSONArray jsonObject1 = jsonObject.getJSONArray("item_dtl");






                    }else
                    {
                        Toast.makeText(OrderHistoryDetails.this,"Data Not Available",Toast.LENGTH_LONG).show();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }


                progressDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(OrderHistoryDetails.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.d("View_Orderhistory","***"+error.toString());

                progressDialog.dismiss();
            }
        });
    }


    class ViewfullorderAdapterFor_Water extends BaseAdapter
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

            vieworderdetails_variables = arrayList.get(position);

            convertView = getLayoutInflater().inflate(R.layout.vieworderdetailschilditems,null);


            ImageView img_viewitemimgall = (ImageView) convertView.findViewById(R.id.img_viewitemimgall);
            TextView viewwater_itemname_display = (TextView) convertView.findViewById(R.id.viewwater_itemname_display);
            TextView viewwater_item_quantity_display = (TextView) convertView.findViewById(R.id.viewwater_item_quantity_display);
            TextView viewwater_item_item_price_display = (TextView) convertView.findViewById(R.id.viewwater_item_item_price_display);
            TextView viewwater_total_item_price_display = (TextView) convertView.findViewById(R.id.viewwater_total_item_price_display);


            viewwater_itemname_display.setText(vieworderdetails_variables.getItem_name());
            viewwater_item_quantity_display.setText(vieworderdetails_variables.getItem_quantity());
            viewwater_item_item_price_display.setText(vieworderdetails_variables.getItem_price());
            viewwater_total_item_price_display.setText("\u20B9" + vieworderdetails_variables.getTotal_item_price());


            Picasso.with(OrderHistoryDetails.this)
                    .load(vieworderdetails_variables.getItem_img())// optional
                    .into(img_viewitemimgall);


            if (returncan_enaableordisbale.equals("Yes") && water.equals("water"))
            {
                vieworder_returncan_details.setVisibility(View.VISIBLE);
            }else
            {
                vieworder_returncan_details.setVisibility(View.GONE);
            }

            if (!(promo_benefit.equals("0")))
            {
                ll_promobenifit.setVisibility(View.VISIBLE);
            }
            if (grand_total_empty_can_amount.equals("0"))
            {
                ll_retruncandetails.setVisibility(View.GONE);
            }
            if (grand_total_amount.equals("0"))
            {
                ll_grandtotlaamount.setVisibility(View.GONE);
            }
            return convertView;
        }
    }


    class  ReturncanAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return arrayList_returncanadapter.size();
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

            returncanDetails = arrayList_returncanadapter.get(position);


            convertView = getLayoutInflater().inflate(R.layout.viewreturncan_detailschilditems,null);



            ImageView img_viewitemimgall_returncan = (ImageView) convertView.findViewById(R.id.img_viewitemimgall_returncan);
            TextView viewwater_itemname_display_returncan = (TextView) convertView.findViewById(R.id.viewwater_itemname_display_returncan);
            TextView viewwater_item_quantity_display_returncan = (TextView) convertView.findViewById(R.id.viewwater_item_quantity_display);
            TextView viewwater_item_item_price_display_returncan = (TextView) convertView.findViewById(R.id.viewwater_item_item_price_display_returncan);
            TextView viewwater_total_item_price_display_returncan = (TextView) convertView.findViewById(R.id.viewwater_total_item_price_display_returncan);


            Picasso.with(OrderHistoryDetails.this)
                    .load(returncanDetails.getReturn_can_img())// optional
                    .into(img_viewitemimgall_returncan);

            viewwater_itemname_display_returncan.setText(returncanDetails.getReturn_can_name());

            viewwater_item_quantity_display_returncan.setText(returncanDetails.getReturn_can_quantity());
            viewwater_item_item_price_display_returncan.setText(returncanDetails.getReturn_can_price());
            viewwater_total_item_price_display_returncan.setText(returncanDetails.getTotal_return_can_price());

            return convertView;
        }
    }

    public  void Fetch_Returnncandetail()
    {
        progressDialog = new ProgressDialog(OrderHistoryDetails.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        arrayList_returncanadapter.clear();
        progressDialog.show();


      /*  Log.d("auth_keyquizzzzz", "'''''   " + quiz_auth);
        Log.d("testid==quizzzz", "'''''  " + quiz_topicid);*/

        //  Log.d("numberincrease", "+++++++   " + Number);

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API) //Setting the Root URL
                .build();
        UohmacAPI WeekTest_Quiz = adapter.create(UohmacAPI.class);



        WeekTest_Quiz.View_Orderhistory_fulldetails(getauthkey,ids, new Callback<Response>() {

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
                Log.d("Returncandetail...", "==== 1...   " + result);


                try {



                    JSONObject jsonObject = new JSONObject(result.toString());


                    String status = jsonObject.getString("status");

                    if (status.equals("1"))
                    {

                        JSONObject jsonObject1= jsonObject.getJSONObject("result");


                        JSONArray jsonArray_emptycan = jsonObject1.getJSONArray("empty_can_dtl");

                        if (jsonArray_emptycan.length() > 0)
                        {

                            for (int i=0;i<jsonArray_emptycan.length();i++)
                            {

                                JSONObject jsonObject2 =jsonArray_emptycan.getJSONObject(i);

                                returncanDetails = new ReturncanDetails();

                                returncanDetails.setReturn_can_img(jsonObject2.getString("return_can_img"));
                                returncanDetails.setReturn_can_name(jsonObject2.getString("return_can_name"));
                                returncanDetails.setReturn_can_quantity(jsonObject2.getString("return_can_quantity"));
                                returncanDetails.setReturn_can_price(jsonObject2.getString("return_can_price"));
                                returncanDetails.setTotal_return_can_price(jsonObject2.getString("total_return_can_price"));

                                arrayList_returncanadapter.add(returncanDetails);
                            }

                            Log.d("ReturncanDetails","arraysice"+arrayList_returncanadapter.size());

                            returncanAdapter = new ReturncanAdapter();

                            listview_returncandetails.setAdapter(returncanAdapter);


                        }else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(OrderHistoryDetails.this,"Data Not Available",Toast.LENGTH_LONG).show();
                        }



                    }else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(OrderHistoryDetails.this,"Data Not Available",Toast.LENGTH_LONG).show();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }


                progressDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(OrderHistoryDetails.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.d("Returncandetail","***"+error.toString());

                progressDialog.dismiss();
            }
        });
    }

}

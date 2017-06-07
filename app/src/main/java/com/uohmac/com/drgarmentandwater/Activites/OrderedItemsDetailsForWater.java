package com.uohmac.com.drgarmentandwater.Activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uohmac.com.drgarmentandwater.Adpater.OrderedDetailsAdapter_Laundry;
import com.uohmac.com.drgarmentandwater.Adpater.OrderedDetailsAdapter_Water;
import com.uohmac.com.drgarmentandwater.Api.UohmacAPI;
import com.uohmac.com.drgarmentandwater.Pojos.OrderedDetailsPojo;
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
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by uOhmac Technologies on 23-Mar-17.
 */
public class OrderedItemsDetailsForWater extends AppCompatActivity {
    SharedPreferences sp;
    public static String getauthkey,reponse_totalprice,totalprice;
    public static Button btn_selectingaddress;
    ProgressDialog mProgressDialog;
    public static ListView rvMyRequest_waterorderedlist;
    OrderedDetailsAdapter_Water cart_adapter_water;
    public static TextView tv_totalandfinalprice;
    JSONObject jsonObject;
    ArrayList<OrderedDetailsPojo> orderedDetailsPojoList_water = new ArrayList<OrderedDetailsPojo>();
    public static final String MYPREFERNCES_LOGIN = "mysharedpref_waterlaundry";
    public static TextView nomoreitemfound_water;
    public static LinearLayout linearLayout;
    public static RelativeLayout rr_text_nodata;
    public static final String PREFERENCES_SAVEADDRESS = "save_deliveryaddress";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ordereditemdetailsforwater);
        btn_selectingaddress = (Button)findViewById(R.id.btn_selectaddrforwater);

        rvMyRequest_waterorderedlist = (ListView)findViewById(R.id.lv_orderlistlistviewforwater);
        tv_totalandfinalprice = (TextView)findViewById(R.id.txt_totalpriceforordereditemswater);
        nomoreitemfound_water = (TextView)findViewById(R.id.nomoreitemfound_water);
        linearLayout = (LinearLayout)findViewById(R.id.LL1);
        rr_text_nodata = (RelativeLayout)findViewById(R.id.rr_text_nodata);
        sp = getSharedPreferences(MYPREFERNCES_LOGIN, 0);
        getauthkey = sp.getString("auth_key", "");


        sp = getSharedPreferences(PREFERENCES_SAVEADDRESS, 0);

        if(AppNetworkInfo.isConnectingToInternet(OrderedItemsDetailsForWater.this)){
            getcartdata_water();
            Totalprice();
        }else{
            Toast.makeText(OrderedItemsDetailsForWater.this, "No network found.Please try again later..!!!", Toast.LENGTH_LONG).show();
        }



        btn_selectingaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sp.getString("save_address", "-1").equals("saved")) {
                    Log.d("addrstatus", "success1");
                    Intent i = new Intent(OrderedItemsDetailsForWater.this, DeliveryAddrDetailsforWater.class);
                    startActivity(i);
                } else {
                    Intent i1 = new Intent(OrderedItemsDetailsForWater.this, Getaddressforwater.class);
                    startActivity(i1);
                }
            }
        });

    }



    @Override
    public void onBackPressed() {

        WaterItemDetails.txtdisplaytotalprice.setText(WaterItemDetails.totalprice);
        super.onBackPressed();

    }



    //defineing api to calculate the totalprice
    private void Totalprice(){
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_totalprice = adapter.create(UohmacAPI.class);

        api_totalprice.TotalCartPrice("1", getauthkey, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                BufferedReader reader = null;
                StringBuilder sb = new StringBuilder();
                try {

                    reader = new BufferedReader(new InputStreamReader(response.getBody().in()));

                    String line;

                    try {
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                reponse_totalprice = sb.toString();
                Log.e("getre_totalprice", reponse_totalprice);
                try {

                    jsonObject = new JSONObject(reponse_totalprice);
                    totalprice = jsonObject.getString("price");
                    Log.e("gettotalprice_api", totalprice);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                tv_totalandfinalprice.setText("\u20B9" + totalprice);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(OrderedItemsDetailsForWater.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.d("ERROR:", error.getMessage());

            }
        });

    }


  /*  private void getcartdata_water(){

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.show();
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_getcartdata = adapter.create(UohmacAPI.class);

        api_getcartdata.GetCartData("1",getauthkey, new Callback<List<OrderedDetailsPojo>>() {
            @Override
            public void success(List<OrderedDetailsPojo> orderedDetailsPojos, Response response) {
                // recyclerView.setLayoutManager(new LinearLayoutManager(OrderedItemsDetailsForLaundry.this, LinearLayoutManager.VERTICAL, false));
                // mAdapter = new OrderedDetailsAdapter(OrderedItemsDetailsForLaundry.this, orderedDetailsPojos);
                //recyclerView.setAdapter(mAdapter);
                orderedDetailsPojoList_water = orderedDetailsPojos;
                cart_adapter_water = new OrderedDetailsAdapter_Water(OrderedItemsDetailsForWater.this, R.layout.orderedsingleitem_water, orderedDetailsPojoList_water);
                rvMyRequest_waterorderedlist.setAdapter(cart_adapter_water);

                mProgressDialog.dismiss();

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Errorfound", "ERROR:" + error.getMessage(), error);

            }
        });
    }*/


    public void getcartdata_water(){

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please Wait...");
        orderedDetailsPojoList_water.clear();
        mProgressDialog.show();

        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_getcartdata = adapter.create(UohmacAPI.class);

        api_getcartdata.GetCartData("1",getauthkey, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                // recyclerView.setLayoutManager(new LinearLayoutManager(OrderedItemsDetailsForLaundry.this, LinearLayoutManager.VERTICAL, false));
                // mAdapter = new OrderedDetailsAdapter(OrderedItemsDetailsForLaundry.this, orderedDetailsPojos);
                //recyclerView.setAdapter(mAdapter);


                BufferedReader reader = null;
                String result = "";

                try {
                    //Initializing buffered reader
                    reader = new BufferedReader(new InputStreamReader(response.getBody().in()));

                    //Reading the output in the string
                    result = reader.readLine();


                } catch (IOException e)
                {
                    Log.d("IOExceptiongio", "laundryresponce" + e.toString());
                }
                //Displaying the output as a toast
                Log.d("laundryresponce", "success**   " + result.toString());


                try {
                    JSONArray jsonArray = new JSONArray(result);


                    if (jsonArray.length()>0)
                    {
                        for (int i =0;i<jsonArray.length();i++)
                        {
                            // OrderedDetail_LaundryAdapter orderedAdapter_laundry = new OrderedDetail_LaundryAdapter();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            //sfsdfsfsdfsdfsdfsdfsfsdfdsfsdfds
                            /*@SerializedName("item_id")
                            @SerializedName("item_name")
                            @SerializedName("item_img")
                            @SerializedName("price")
                            @SerializedName("no_of_item")*/

                            OrderedDetailsPojo orderedDetailsPojo = new OrderedDetailsPojo();


                            orderedDetailsPojo.setId(jsonObject.getString("item_id"));
                            orderedDetailsPojo.setItemname(jsonObject.getString("item_name"));
                            orderedDetailsPojo.setItemimage(jsonObject.getString("item_img"));
                            orderedDetailsPojo.setNo_of_item(jsonObject.getString("no_of_item"));
                            orderedDetailsPojo.setPrice(jsonObject.getString("price"));


                            orderedDetailsPojoList_water.add(orderedDetailsPojo);
                            rr_text_nodata.setVisibility(View.GONE);
                            OrderedItemsDetailsForWater.nomoreitemfound_water.setVisibility(View.GONE);
                            rvMyRequest_waterorderedlist.setVisibility(View.VISIBLE);
                            linearLayout.setVisibility(View.VISIBLE);
                            btn_selectingaddress.setVisibility(View.VISIBLE);
                        }


                        Log.d("orderdeatailarray","sizelandury"+orderedDetailsPojoList_water.size());


                       /* orderedDetailsPojoList_laundry = orderedDetailsPojos;*/
                        cart_adapter_water = new OrderedDetailsAdapter_Water(OrderedItemsDetailsForWater.this, R.layout.orderedsingleitem_water, orderedDetailsPojoList_water);
                        rvMyRequest_waterorderedlist.setAdapter(cart_adapter_water);

                        mProgressDialog.dismiss();

                    }else
                    {
                        rvMyRequest_waterorderedlist.setVisibility(View.GONE);

                        linearLayout.setVisibility(View.GONE);

                        rr_text_nodata.setVisibility(View.VISIBLE);
                        OrderedItemsDetailsForWater.nomoreitemfound_water.setVisibility(View.VISIBLE);

                        Toast.makeText(OrderedItemsDetailsForWater.this,"No Data found",Toast.LENGTH_SHORT).show();

                        mProgressDialog.dismiss();

                    }







                } catch (JSONException e) {
                    e.printStackTrace();
                }


                // orderedDetailsPojoList_laundry = orderedDetailsPojos;
                //   cart_adapter = new OrderedDetailsAdapter_Laundry(OrderedItemsDetailsForLaundry.this, R.layout.ordereditemslaundry_single_item, orderedDetailsPojoList_laundry);
                //   rvMyRequest.setAdapter(cart_adapter);

                mProgressDialog.dismiss();





            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(OrderedItemsDetailsForWater.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.e("Errorfound", "ERROR:" + error.getMessage(), error);

            }
        });
    }
}

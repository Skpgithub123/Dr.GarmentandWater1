package com.uohmac.com.drgarmentandwater.Activites;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.annotations.SerializedName;
import com.uohmac.com.drgarmentandwater.Adpater.OrderedDetailsAdapter_Laundry;
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
 * Created by uOhmac Technologies on 02-Jan-17.
 */
public class OrderedItemsDetailsForLaundry extends AppCompatActivity{
    SharedPreferences sp;
   // String getauthkey,response_getcartdata,getcartdata_itemid,getcartdata_no_of_item,getcartdata_price,getcartdata_item_name,getcartdata_item_img;
    //public static final String MYPREFERNCES_REGISTER = "mypreferregister";
    //public static final String MYPREFERNCES_POSTFB = "mypreferpostfb";
    public static final String MYPREFERNCES_LOGIN = "mysharedpref_waterlaundry";
    public static final String PREFERNCES_ADDRESS = "save_laundryaddress";
    JSONObject jsonObject;
    JSONArray jsonArray;
    String getauthkey,reponse_totalprice,promocode,response_promocode,status,msg_promocode,promobenefit;
    public static String totalprice;
    public static ListView rvMyRequest;
    public static OrderedDetailsAdapter_Laundry cart_adapter;
    ArrayList<OrderedDetailsPojo> orderedDetailsPojoList_laundry;
    public static TextView txt_totalprice_fromcart,txt_havepromocode_,txt_getpromo;
    Button btn_deliveryaddress,btn_promocodeapply;
    Dialog dialog_promocode;
    EditText et_promocode;
    ImageView img_close;
    ProgressDialog mProgressDialog;

    public  static  TextView nomoreitemfound_laundry;

    public  static   RelativeLayout relativeLayout6;
    public  static   RelativeLayout relativelayout_laundrytotalprice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ordereditemsdetailsforlaundry);


        nomoreitemfound_laundry  = (TextView) findViewById(R.id.nomoreitemfound_laundry);

        relativeLayout6   = (RelativeLayout) findViewById(R.id.relativeLayout6);
        relativelayout_laundrytotalprice  = (RelativeLayout) findViewById(R.id.relativelayout_laundrytotalprice);

        orderedDetailsPojoList_laundry  = new ArrayList<OrderedDetailsPojo>();

        txt_totalprice_fromcart = (TextView) findViewById(R.id.txt_displaytotalprice_final);
        btn_deliveryaddress = (Button)findViewById(R.id.btn_selectdeliveryaddress);
        txt_havepromocode_ = (TextView)findViewById(R.id.txt_havepromocode);
        txt_getpromo = (TextView)findViewById(R.id.txt_getpromo);

        rvMyRequest = (ListView)findViewById(R.id.lv_orderlistlistview);
       // recyclerView.addItemDecoration(new DividerItemDecoration(OrderedItemsDetailsForLaundry.this, LinearLayoutManager.VERTICAL));
       // sp = getSharedPreferences(MYPREFERNCES_REGISTER, 0);
        //getting authkey from sp

        sp = getSharedPreferences(MYPREFERNCES_LOGIN, 0);
        getauthkey = sp.getString("auth_key", "");


        sp = getSharedPreferences(PREFERNCES_ADDRESS, 0);
       /* sp = getSharedPreferences(MYPREFERNCES_POSTFB, 0);
        getauthkey = sp.getString("auth_key", "");
        Log.e("gettingauthkeyforsettings", getauthkey);*/




        if(AppNetworkInfo.isConnectingToInternet(OrderedItemsDetailsForLaundry.this)){
            Totalprice();

        } else{
            Toast.makeText(OrderedItemsDetailsForLaundry.this,"No network found... Please try again",Toast.LENGTH_SHORT).show();
        }

       // txt_totalprice_fromcart = (TextView)findViewById(R.id.txt_displaytotalprice_final);
       /* if(!TextUtils.isEmpty(Laundryitemdetails.totalprice)) {
            //gettotalpricefordrycleaning = Integer.valueOf(sp.getString("itemprice", ""));
            //Log.e("gettotalpricefordrycleaning", ""+ gettotalpricefordrycleaning);
            txt_totalprice_fromcart.setText(Laundryitemdetails.totalprice);
            Log.d("getavailprice_fromcart", "" + Laundryitemdetails.totalprice);
        }*/


        txt_havepromocode_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_promocode = new Dialog(OrderedItemsDetailsForLaundry.this);
                dialog_promocode.setContentView(R.layout.dialog_promocode);
                et_promocode = (EditText) dialog_promocode.findViewById(R.id.edit_promocode);
                btn_promocodeapply = (Button) dialog_promocode.findViewById(R.id.btn_promocde);
                img_close = (ImageView)dialog_promocode.findViewById(R.id.img_closepromocodepopup);

                dialog_promocode.show();

                promocode = et_promocode.getText().toString().trim();

                btn_promocodeapply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
/*
                        if (et_promocode.getText().toString().equals("")) {
                            et_promocode.setError(getString(R.string.error_field_required));
                            et_promocode.requestFocus();
                        } else if (AppNetworkInfo.isConnectingToInternet(OrderedItemsDetailsForLaundry.this)) {
                            ApplyPromoCode();
                        } else {
                            Toast.makeText(OrderedItemsDetailsForLaundry.this, "No Network Connection", Toast.LENGTH_SHORT).show();
                        }*/
                        if(et_promocode.getText().toString().equals("")){
                            et_promocode.setError(getString(R.string.error_field_required));
                            et_promocode.requestFocus();

                        }else if(AppNetworkInfo.isConnectingToInternet(OrderedItemsDetailsForLaundry.this)){
                            ApplyPromoCode();
                        } else{
                            Toast.makeText(OrderedItemsDetailsForLaundry.this,"No network found... Please try again",Toast.LENGTH_SHORT).show();
                        }

                    }
                });


                img_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog_promocode.dismiss();
                    }
                });


            }
        });

        txt_totalprice_fromcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        txt_getpromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i_callpromocodeslist = new Intent(OrderedItemsDetailsForLaundry.this, PromoCodesListforLaundry.class);
                startActivity(i_callpromocodeslist);
            }
        });



       /* sp = getSharedPreferences(MYPREFERNCES_POSTFB, 0);
        getauthkey = sp.getString("auth_key", "");
        Log.e("gettingauthkeyforsettings", getauthkey);
*/

        if(AppNetworkInfo.isConnectingToInternet(OrderedItemsDetailsForLaundry.this)) {
            getcartdata();
        }else{
            Toast.makeText(OrderedItemsDetailsForLaundry.this,"No network found... Please try again",Toast.LENGTH_SHORT).show();
        }

        btn_deliveryaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sp.getString("save_addressforlaundry", "-1").equals("savedaddressforlaundry")){
                    Intent i_deliveryaddress = new Intent(OrderedItemsDetailsForLaundry.this,SelectingDetails.class);
                    startActivity(i_deliveryaddress);
                }else{
                    Intent i_deliveryaddress = new Intent(OrderedItemsDetailsForLaundry.this,Getaddressforlaundry.class);
                    startActivity(i_deliveryaddress);
                }

            }
        });


    }


    @Override
    public void onBackPressed() {

        Laundryitemdetails.txtdisplaytotalprice.setText(Laundryitemdetails.totalprice);
        super.onBackPressed();

    }


       //defineing api to calculate the totalprice
    private void Totalprice(){
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_totalprice = adapter.create(UohmacAPI.class);

        api_totalprice.TotalCartPrice("2",getauthkey, new Callback<Response>() {
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
                txt_totalprice_fromcart.setText(totalprice);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(OrderedItemsDetailsForLaundry.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.d("ERROR:", error.getMessage());

            }
        });

    }

    public void getcartdata(){

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please Wait...");
        orderedDetailsPojoList_laundry.clear();
        mProgressDialog.show();

        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_getcartdata = adapter.create(UohmacAPI.class);

        api_getcartdata.GetCartData("2",getauthkey, new Callback<Response>() {
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


                            orderedDetailsPojoList_laundry.add(orderedDetailsPojo);
                            nomoreitemfound_laundry.setVisibility(View.GONE);

                            rvMyRequest.setVisibility(View.VISIBLE);
                            txt_havepromocode_.setVisibility(View.VISIBLE);
                            txt_getpromo.setVisibility(View.VISIBLE);
                            relativeLayout6.setVisibility(View.VISIBLE);
                            relativelayout_laundrytotalprice.setVisibility(View.VISIBLE);
                        }


                        Log.d("orderdeatailarray","sizelandury"+orderedDetailsPojoList_laundry.size());


                       /* orderedDetailsPojoList_laundry = orderedDetailsPojos;*/
                           cart_adapter = new OrderedDetailsAdapter_Laundry(OrderedItemsDetailsForLaundry.this, R.layout.ordereditemslaundry_single_item, orderedDetailsPojoList_laundry);
                           rvMyRequest.setAdapter(cart_adapter);

                        mProgressDialog.dismiss();





                    }else
                    {
                        rvMyRequest.setVisibility(View.GONE);
                        txt_havepromocode_.setVisibility(View.GONE);
                        txt_getpromo.setVisibility(View.GONE);
                        relativeLayout6.setVisibility(View.GONE);
                        relativelayout_laundrytotalprice.setVisibility(View.GONE);
                        nomoreitemfound_laundry.setVisibility(View.VISIBLE);
                         Toast.makeText(OrderedItemsDetailsForLaundry.this,"No Data found",Toast.LENGTH_SHORT).show();

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
                Toast.makeText(OrderedItemsDetailsForLaundry.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.e("Errorfound", "ERROR:" + error.getMessage(), error);

            }
        });
    }


    //Promo code api calling
    private void ApplyPromoCode(){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.show();
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_promocode= adapter.create(UohmacAPI.class);

        api_promocode.PromoCode(getauthkey, et_promocode.getText().toString(), txt_totalprice_fromcart.getText().toString(), new Callback<Response>() {
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
                response_promocode = sb.toString();
                Log.e("getre_promocode", response_promocode);

                try{
                    jsonObject = new JSONObject(response_promocode);
                    status = jsonObject.getString("status");
                    msg_promocode = jsonObject.getString("msg");
                    promobenefit = jsonObject.getString("promo_benefit");
                    if(status.equals("1")){
                        Toast.makeText(OrderedItemsDetailsForLaundry.this, msg_promocode, Toast.LENGTH_SHORT).show();
                        dialog_promocode.dismiss();
                        int totalpriceaftercoupan = Integer.parseInt(totalprice) - Integer.parseInt(promobenefit);
                        Log.e("cccc===", ""+totalpriceaftercoupan);
                        txt_totalprice_fromcart.setText(totalpriceaftercoupan);
                    }else if(status.equals("0")){
                        Toast.makeText(OrderedItemsDetailsForLaundry.this, msg_promocode, Toast.LENGTH_SHORT).show();
                        dialog_promocode.dismiss();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                mProgressDialog.dismiss();

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(OrderedItemsDetailsForLaundry.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.e("Errorfound", "ERROR:" + error.getMessage(), error);

            }
        });
    }



   }

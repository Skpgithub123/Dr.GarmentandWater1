package com.uohmac.com.drgarmentandwater.Activites;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.uohmac.com.drgarmentandwater.Api.UohmacAPI;
import com.uohmac.com.drgarmentandwater.R;
import com.uohmac.com.drgarmentandwater.Utils.AppNetworkInfo;
import com.uohmac.com.drgarmentandwater.Utils.Constatns;
import com.uohmac.com.drgarmentandwater.Utils.GlobalUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by uOhmac Technologies on 13-Mar-17.
 */
public class WaterItemDetails extends AppCompatActivity {

    String get_waterid,res_wateritem,itemname,itemimg,itemprice,getauthkey,response_increament,reponse_totalprice,response_decrement,itemid,reponse_addreturncan,response_addemptycan,response_subemptycan;
    public static String totalprice;
    ProgressDialog mProgressDialog;
    String status="", msg="",totalitemquantity="";
    JSONArray jsonArray;
    JSONObject jsonObject;
    TextView tvitemname,tvitemprice,txtquantity,txtreturncan,tvinitialprice,tvdisplay;
    public static TextView txtdisplaytotalprice;
    CircleImageView img_wateritem;
    ImageView img_add,img_sub,img_addreturncan,imgsubreturncan;
    public static int count=0;
    int count_retruncan = 0;
    int total=0,total_sub=0,finalitemquantity=0;
    SharedPreferences sp;
    public static Spinner sp_deliverytypes;
    RelativeLayout relativeLayout;
    String Incrementstatus = "",Decrementstatus = "",Returnemptycanstatus = "",totalcan,price, Addingreturncanstatus="", Subtractreturncanstatus="";
    ArrayList<String> deliverytypes = new ArrayList<>();
    ArrayAdapter<String> delivery_slots;
    String extraamt="10",reponse_returnitemquantity="";
    public static String delivery_type = "";
    LinearLayout LL_pricelayout_water;
    public static final String MYPREFERNCES_LOGIN = "mysharedpref_waterlaundry";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.water_itemdetails);

        get_waterid = (String) getIntent().getExtras().get("sendwaterid");
        Log.e("waterid", get_waterid);
        tvitemname = (TextView)findViewById(R.id.txt_wateritemname);
        tvitemprice = (TextView)findViewById(R.id.txt_waterrs);
        txtquantity = (TextView)findViewById(R.id.txt_incremnetvalue);
        txtdisplaytotalprice = (TextView)findViewById(R.id.txt_displaytotalprice_water);
        tvdisplay = (TextView)findViewById(R.id.txt_fixtotalprice_water);
        img_wateritem = (CircleImageView)findViewById(R.id.water_img);
        img_add = (ImageView)findViewById(R.id.image_add);
        img_sub = (ImageView)findViewById(R.id.image_sub);
        sp_deliverytypes = (Spinner)findViewById(R.id.sp_deltype);
        img_addreturncan = (ImageView)findViewById(R.id.img_addreturncan);
        imgsubreturncan = (ImageView)findViewById(R.id.img_subreturncan);
        txtreturncan = (TextView)findViewById(R.id.txt_valuereturncan);
        tvinitialprice = (TextView)findViewById(R.id.txt_initialprice);
        relativeLayout = (RelativeLayout)findViewById(R.id.rr_wateritemdetails);
        LL_pricelayout_water = (LinearLayout)findViewById(R.id.LL_pricelayout_water);
        sp = getSharedPreferences(MYPREFERNCES_LOGIN, 0);
        getauthkey = sp.getString("auth_key", "");


        deliverytypes.add("Select DeliveryType");
        deliverytypes.add("Normal Delivery");
        deliverytypes.add("Fast Delivery");


        delivery_slots = new ArrayAdapter<String>(WaterItemDetails.this,android.R.layout.simple_list_item_1,deliverytypes);
        sp_deliverytypes.setAdapter(delivery_slots);


        if(AppNetworkInfo.isConnectingToInternet(WaterItemDetails.this)){
            getwateritemdetails();
            Totalprice();
        }else{
            Toast.makeText(WaterItemDetails.this, "No network found.Please try again later", Toast.LENGTH_LONG).show();
        }


        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = count + 1;
                Log.e("increasingquantity", "" + count);
                finalitemquantity = count+Integer.parseInt(totalitemquantity);
                txtquantity.setText("" + finalitemquantity);

                if (AppNetworkInfo.isConnectingToInternet(WaterItemDetails.this)) {
                    Increamentquantity_water();
                } else {
                    AlertDialog.Builder ab = new AlertDialog.Builder(WaterItemDetails.this);
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

                            Increamentquantity_water();
                        }
                    });
                    ab.create();
                    ab.show();
                }

            }
        });


        img_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count > 0) {
                    count = count - 1;
                    txtquantity.setText(count + "");

                    if (AppNetworkInfo.isConnectingToInternet(WaterItemDetails.this)) {
                        DecrementQuantity_water();
                    } else {
                        AlertDialog.Builder ab = new AlertDialog.Builder(WaterItemDetails.this);
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
                                DecrementQuantity_water();
                            }
                        });
                        ab.create();
                        ab.show();
                    }
                }
            }
        });


        LL_pricelayout_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(txtdisplaytotalprice.getText().toString().equals("0")) {
                        GlobalUtils.showSnackBar(relativeLayout, "Please Select Quantity");
                    }else if(sp_deliverytypes.getSelectedItem().equals("Select DeliveryType")){
                        GlobalUtils.showSnackBar(relativeLayout, "Please Select Deliverytype");
                     }
                    else{
                        Intent i = new Intent(WaterItemDetails.this, OrderedItemsDetailsForWater.class);
                        startActivity(i);
                    }

                }


        });

        sp_deliverytypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_deliverytypes.getSelectedItem().equals("Fast Delivery")) {
                    int extra_add = (Integer.parseInt(txtdisplaytotalprice.getText().toString()) + Integer.parseInt(extraamt));
                    txtdisplaytotalprice.setText("" + extra_add);
                    delivery_type = "2";
                }else if(sp_deliverytypes.getSelectedItem().equals("Normal Delivery")){
                    delivery_type = "1";
                } else{

                    txtdisplaytotalprice.setText("" + totalprice);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        img_addreturncan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AppNetworkInfo.isConnectingToInternet(WaterItemDetails.this)) {
                    IncreamentReturnEmptyCan();
                } else {
                    AlertDialog.Builder ab = new AlertDialog.Builder(WaterItemDetails.this);
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
                            IncreamentReturnEmptyCan();
                        }
                    });
                    ab.create();
                    ab.show();
                }

            }
        });


        imgsubreturncan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppNetworkInfo.isConnectingToInternet(WaterItemDetails.this)) {
                    DecrementReturnEmptyCan();
                } else {
                    AlertDialog.Builder ab = new AlertDialog.Builder(WaterItemDetails.this);
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
                            DecrementReturnEmptyCan();
                        }
                    });
                    ab.create();
                    ab.show();
                }

            }
        });

        //for screen animation
        overridePendingTransition(R.anim.lefttoright, R.anim.righttoleft);


    }

    private void getwateritemdetails()
    {

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.show();
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_wateritemdetails = adapter.create(UohmacAPI.class);

        api_wateritemdetails.GetLaundryDetails(get_waterid, new Callback<Response>() {
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

                mProgressDialog.dismiss();
                res_wateritem = sb.toString();
                Log.e("getwateritem", res_wateritem);

                try {
                    jsonArray = new JSONArray(res_wateritem);
                    jsonObject = jsonArray.getJSONObject(0);

                    itemname = jsonObject.getString("item_name");
                    itemimg = jsonObject.getString("item_img");
                    itemprice = jsonObject.getString("item_price");
                    itemid = jsonObject.getString("item_id");
                    Log.e("itemid", itemid);

                    if (!TextUtils.isEmpty(itemname)) {
                        tvitemname.setText(itemname);
                    }
                    if (!TextUtils.isEmpty(itemprice)) {
                        tvitemprice.setText("\u20B9" + itemprice);
                        tvinitialprice.setText(itemprice);
                    }

                    if (!TextUtils.isEmpty(itemimg)) {
                        Picasso.with(WaterItemDetails.this)
                                .load(itemimg)
                                .into(img_wateritem);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                getreturn_itemquantity();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(WaterItemDetails.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.d("ERROR:", error.getMessage());
            }
        });


    }


    private void  Increamentquantity_water(){

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.show();

        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_increamentquantity = adapter.create(UohmacAPI.class);

        api_increamentquantity.IncreamentAPI(getauthkey, itemid, new Callback<Response>() {
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

                response_increament = sb.toString();
                Log.e("getre_increament", response_increament);


                try {
                    jsonObject = new JSONObject(response_increament);
                    Incrementstatus = jsonObject.getString("status");


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // if (Incrementstatus.equals("1")) {
                //Incrementstatus = "1";

                if (Incrementstatus.equals("1")) {
                    Incrementstatus = "0";
                    Totalprice();
                    if (total != 0) {
                        int value = (Integer.parseInt(txtdisplaytotalprice.getText().toString()) + Integer.parseInt(tvinitialprice.getText().toString()));
                        txtdisplaytotalprice.setText("" + value);
                        return;
                    }

                }
                // }
                mProgressDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(WaterItemDetails.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.d("ERROR:", error.getMessage());
            }
        });
    }


    private void  DecrementQuantity_water(){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.show();

        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_decrementquantity = adapter.create(UohmacAPI.class);

        api_decrementquantity.DecrementAPI(getauthkey, itemid, new Callback<Response>() {
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
                response_decrement = sb.toString();
                Log.e("getre_decreament", response_decrement);
                try {
                    jsonObject = new JSONObject(response_decrement);
                    Decrementstatus = jsonObject.getString("status");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // if (Decrementstatus.equals("1")) {
                // Decrementstatus = "1";

                if (Decrementstatus.equals("1")) {
                    Decrementstatus = "0";
                    Totalprice();
                    if (total != 0) {
                        int value = (Integer.parseInt(txtdisplaytotalprice.getText().toString()) - Integer.parseInt(tvinitialprice.getText().toString()));
                        txtdisplaytotalprice.setText("" + value);
                        return;
                    }
                }
                // }
                mProgressDialog.dismiss();

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(WaterItemDetails.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.d("ERROR:", error.getMessage());
            }
        });
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
                txtdisplaytotalprice.setText("" + totalprice);
                /*if (txtdisplaytotalprice != null) {




                    *//*else
                    {

                    }


                    Log.d("ssssss","***  "+value);

                    txtdisplaytotalprice.setText(""+value);

                }*//* else {

                        return;

                    }
                }*/
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(WaterItemDetails.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.d("ERROR:", error.getMessage());

            }
        });

    }


    private void getreturn_itemquantity(){
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_itemquantity = adapter.create(UohmacAPI.class);

        api_itemquantity.getitemquantity(getauthkey, itemid, "1", new Callback<Response>() {
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

                reponse_returnitemquantity = sb.toString();
                Log.e("reponse_returnitemquantity", reponse_returnitemquantity);

                try{
                    JSONObject jsonObject = new JSONObject(reponse_returnitemquantity);
                    status = jsonObject.getString("status");
                    totalitemquantity = jsonObject.getString("total_item");
                    Log.d("totalitemquantity", totalitemquantity);
                    if(jsonObject.has("msg"))
                    msg = jsonObject.getString("msg");

                    if(status.equals("1")){
                        txtquantity.setText(totalitemquantity);

                    }else if(status.equals("0")){
                        //Toast.makeText(WaterItemDetails.this, msg, Toast.LENGTH_SHORT).show();
                        txtquantity.setText(totalitemquantity);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(WaterItemDetails.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.d("ERROR:", error.getMessage());
            }
        });

    }

    //To check how many return empty cans are there
    private void IncreamentReturnEmptyCan(){
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_addreturncan = adapter.create(UohmacAPI.class);

        api_addreturncan.ReturnEmptyCan(getauthkey, itemid, new Callback<Response>() {
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
                reponse_addreturncan = sb.toString();
                Log.e("getre_addreturncan", reponse_addreturncan);
                try {
                    jsonObject = new JSONObject(reponse_addreturncan);
                    Returnemptycanstatus = jsonObject.getString("status");
                    totalcan = jsonObject.getString("total_can");
                    price = jsonObject.getString("price");

                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (totalcan.equals("0")) {
                    //Toast.makeText(WaterItemDetails.this, "You Don't Have Any Cans To Return", Toast.LENGTH_LONG).show();
                    GlobalUtils.showSnackBar(relativeLayout, "You Don't Have Any Cans To Return");
                } else {

                    Log.d("countvalue", "ssssss    " + count_retruncan);
                    AddingEmptyCan();
                    if (Integer.parseInt(totalcan) > count_retruncan) {

                        if (txtreturncan.getText().toString().equals("0")) {
                            count_retruncan = 0;
                            count_retruncan = count_retruncan + 1;
                            txtreturncan.setText("" + count_retruncan);
                            total = Integer.parseInt(totalprice) - Integer.parseInt(price);
                            txtdisplaytotalprice.setText(String.valueOf(total));

                        } else {
                            count_retruncan = Integer.parseInt(txtreturncan.getText().toString());
                            count_retruncan++;
                            txtreturncan.setText("" + count_retruncan);
                            total = Integer.parseInt(txtdisplaytotalprice.getText().toString()) - Integer.parseInt(price);

                            Log.d("pricevalues", "** " + total);
                            txtdisplaytotalprice.setText("");
                            txtdisplaytotalprice.setText(String.valueOf(total));

                        }

                    } else {
                        if (txtreturncan.getText().toString().equals("1")) {
                            Toast.makeText(WaterItemDetails.this, "You have only" + " " + totalcan + " " + "can to return", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(WaterItemDetails.this, "You have only" + " " + totalcan + " " + "cans to return", Toast.LENGTH_LONG).show();
                        }

                    }

                }

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(WaterItemDetails.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.d("ERROR:", error.getMessage());
            }
        });
    }


    //To check how many return cans are there.
    private void DecrementReturnEmptyCan(){
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_addreturncan = adapter.create(UohmacAPI.class);

        api_addreturncan.ReturnEmptyCan(getauthkey,itemid, new Callback<Response>() {
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
                reponse_addreturncan = sb.toString();
                Log.e("getre_addreturncan", reponse_addreturncan);
                try{
                    jsonObject = new JSONObject(reponse_addreturncan);
                    Returnemptycanstatus = jsonObject.getString("status");
                    totalcan = jsonObject.getString("total_can");
                    price = jsonObject.getString("price");
                    if(totalcan.equals("0")){
                        GlobalUtils.showSnackBar(relativeLayout, "You Don't Have Any Cans To Return");
                    }else{

                      //  if(Integer.parseInt(totalcan) != count){
                                SubtractEmptyCan();
                            if(txtreturncan.getText().toString().equals("0"))
                            {   count_retruncan=0;
                               // count = count + 1;
                                txtreturncan.setText("" + count_retruncan);
                                int total = Integer.parseInt(totalprice)+Integer.parseInt(price);
                                txtdisplaytotalprice.setText(String.valueOf(total));
                            }else{
                                count_retruncan = Integer.parseInt(txtreturncan.getText().toString());
                                count_retruncan= count_retruncan-1;
                                txtreturncan.setText("" + count_retruncan);
                                int total = Integer.parseInt(txtdisplaytotalprice.getText().toString())+Integer.parseInt(price);
                                txtdisplaytotalprice.setText(String.valueOf(total));
                            }
/*
                        }else{
                            Toast.makeText(WaterItemDetails.this, "You have only" +"  "+ totalcan +"  "+ "cans to return", Toast.LENGTH_LONG).show();

                        }*/


                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(WaterItemDetails.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.d("ERROR:", error.getMessage());
            }
        });
    }


    //To add return can empty to the server
    private void AddingEmptyCan(){
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_addreturncan = adapter.create(UohmacAPI.class);

        api_addreturncan.AddEmptyCan(getauthkey, itemid, new Callback<Response>() {
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
                response_addemptycan = sb.toString();
                Log.e("response_addemptycan", response_addemptycan);
                try {
                    jsonObject = new JSONObject(reponse_addreturncan);
                    Addingreturncanstatus = jsonObject.getString("status");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(WaterItemDetails.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.d("ERROR:", error.getMessage());
            }
        });
    }


    //To subtract return can empty to the server
    private void SubtractEmptyCan(){
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_addreturncan = adapter.create(UohmacAPI.class);

        api_addreturncan.SubEmptyCan(getauthkey, itemid, new Callback<Response>() {
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
                response_subemptycan = sb.toString();
                Log.e("response_subemptycan", response_subemptycan);
                try {
                    jsonObject = new JSONObject(response_subemptycan);
                    Subtractreturncanstatus = jsonObject.getString("status");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(WaterItemDetails.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.d("ERROR:", error.getMessage());
            }
        });
    }

}

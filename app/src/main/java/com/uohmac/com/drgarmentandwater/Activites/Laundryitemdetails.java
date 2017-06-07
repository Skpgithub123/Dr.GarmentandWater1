package com.uohmac.com.drgarmentandwater.Activites;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.uohmac.com.drgarmentandwater.Api.UohmacAPI;
import com.uohmac.com.drgarmentandwater.Fragments.LandingLaundryFragment;
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

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by System-03 on 11/3/2016.
 */
public class Laundryitemdetails extends AppCompatActivity   {

    String washfoldid,washironid,reponse_returnitemquantity,getwashfoldname,getwashfoldimage,getwashironname,getwashironimage,responselaundryitem,getauthkey,response_increament,response_decrement;
    CircleImageView getimg_washfold;
    public static String reponse_totalprice,totalprice,status_itemprice,item_price;
    TextView txtitemname,txtquantity,txtitemprice,txtestimatedvalue;
    ImageView img_add,img_sub;
    public static int textvalueaddforwashfold=0,finalitemquantity=0,textvalueaddforwashiron,getwashfoldprice,priceforwashfold,priceforwashiron,totalpriceforwashfold,totalpriceforwashiron,getwashironprice,storeestimatedvalue;
    int latestfavItemPrices,appenddatatosp,appendquantitytosp;
    JSONArray jsonArray;
    JSONObject jsonObject;
    RelativeLayout relativeLayout;
    String status,totalitemquantity,msg;
    LinearLayout LL_pricelayout;
    public static TextView txtdisplaytotalprice;
    public static String itemidforwashfold,itemidforwasiron;
    public static int appenddatatoexistingsp,appendquantity;

    //SQLite DB declaration
    public static final String DATABASE_NAME = "laundry.db";
    private static final int DATABASE_VERSION = 1;
    public static final String PERSON_TABLE_NAME = "purchase data";
    public static final String PERSON_COLUMN_ID = "id";
    public static final String PERSON_COLUMN_NAME = "itemname";

    public static final String MYPREFERNCES_TOTALPRICE = "myprefertotalprice";
    public static final String MYPREFERNCES_LOGIN = "mysharedpref_waterlaundry";

    //public static final String MYPREFERNCES_REGISTER = "mypreferregister";
   // public static final String MYPREFERNCES_POSTFB = "mypreferpostfb";

    SharedPreferences sp;
    SharedPreferences.Editor editor ;

    Context context;

    String Incrementstatus = "";
    String Decrementstatus = "";
    ProgressDialog mProgressDialog;
    int value = 250;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laundryitemdetails);
        getimg_washfold = (CircleImageView)findViewById(R.id.imageaddquantity);
        img_add = (ImageView)findViewById(R.id.image_add);
        img_sub = (ImageView)findViewById(R.id.image_sub);
        txtitemname = (TextView)findViewById(R.id.txt_itemname);
        txtitemprice = (TextView)findViewById(R.id.txt_rs);
        txtquantity = (TextView)findViewById(R.id.txt_incremnetvalue);
        txtestimatedvalue = (TextView)findViewById(R.id.txt_fixtotalprice);
        txtdisplaytotalprice = (TextView)findViewById(R.id.txt_displaytotalprice);
        relativeLayout = (RelativeLayout)findViewById(R.id.rr_laundryitemetails);
        LL_pricelayout = (LinearLayout)findViewById(R.id.LL_pricelayout);
        //getting authkey from sp
       // sp = getSharedPreferences(MYPREFERNCES_REGISTER, 0);


        sp = getSharedPreferences(MYPREFERNCES_LOGIN, 0);
        getauthkey = sp.getString("auth_key", "");
        Log.e("gettingauthkeyforsettings", getauthkey);
       /* sp = getSharedPreferences(MYPREFERNCES_POSTFB, 0);
        getauthkey = sp.getString("auth_key", "");
        Log.e("gettingauthkeyforsettings", getauthkey);*/




       /* if(!TextUtils.isEmpty(washfoldid)) {

            Log.e("getwashfoldkey",washfoldid);
        }
        if(!TextUtils.isEmpty(washironid)) {

        }*/

        if(AppNetworkInfo.isConnectingToInternet(Laundryitemdetails.this)){

            washfoldid = (String) getIntent().getExtras().get("washfoldkey");
           // Log.e("cat_id===",washfoldid);
            getlaundryitemdetailsforwashfold();

            washironid = (String) getIntent().getExtras().get("washironkey");
            //Log.e("cat_id===",washironid);
            getlaundryitemdetailsforwashiron();

            Totalprice();



        } else{
            Toast.makeText(Laundryitemdetails.this,"No network found... Please try again",Toast.LENGTH_SHORT).show();
        }
/*
        getlaundryitemdetailsforwashfold();




        getlaundryitemdetailsforwashiron();


        Totalprice();*/

//
//        if(!TextUtils.isEmpty(washfoldid)) {
//
//        }
//
//        if(!TextUtils.isEmpty(washironid)){
//
//        }


        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!TextUtils.isEmpty(washfoldid)) {
                    textvalueaddforwashfold = textvalueaddforwashfold + 1;
                    Log.e("increasingquantity", "" + textvalueaddforwashfold);
                    finalitemquantity = textvalueaddforwashfold+Integer.parseInt(totalitemquantity);
                    txtquantity.setText("" + finalitemquantity);


                    if(AppNetworkInfo.isConnectingToInternet(Laundryitemdetails.this)){
                        Increamentquantity_washfold();
                    } else{
                        Toast.makeText(Laundryitemdetails.this, "No network found.!", Toast.LENGTH_SHORT).show();
                    }

                    //for washandfold


                  /*  if (textvalueaddforwashfold == 1) {
                        Totalprice();


                    } else {
                        Totalprice();
                    }*/



                }

                if (!TextUtils.isEmpty(washironid)) {
                    textvalueaddforwashiron = textvalueaddforwashiron + 1;
                    txtquantity.setText("" + textvalueaddforwashiron);
                    /*if (!TextUtils.isEmpty(washfoldid)) {
                        textvalueaddforwashfold = textvalueaddforwashfold + 1;
                        Log.e("increasingquantity", "" + textvalueaddforwashfold);
                        txtquantity.setText("" + textvalueaddforwashfold);*/

                        if (AppNetworkInfo.isConnectingToInternet(Laundryitemdetails.this)) {
                            Increamentquantity_washiron();
                        } else{
                            Toast.makeText(Laundryitemdetails.this, "No network found.!", Toast.LENGTH_SHORT).show();
                        }




                        //for washandiron
                  /*  priceforwashiron = getwashironprice * textvalueaddforwashiron;
                    if (textvalueaddforwashiron == 1) {
                        Totalprice();


                    } else {
                        Totalprice();

                    }*/


                }

            }
        });

        img_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!TextUtils.isEmpty(washfoldid)) {
                    if (textvalueaddforwashfold > 0) {
                        textvalueaddforwashfold = textvalueaddforwashfold - 1;
                        txtquantity.setText(textvalueaddforwashfold + "");

                        /*if (!TextUtils.isEmpty(washfoldid)) {
                            textvalueaddforwashfold = textvalueaddforwashfold + 1;
                            Log.e("increasingquantity", "" + textvalueaddforwashfold);
                            txtquantity.setText("" + textvalueaddforwashfold);*/

                            if (AppNetworkInfo.isConnectingToInternet(Laundryitemdetails.this)) {
                                DecrementQuantity_washfold();
                            } else{
                                Toast.makeText(Laundryitemdetails.this, "No network found.!", Toast.LENGTH_SHORT).show();
                            }




                      // Totalprice();

                       // dfssfsdfsdafsdfsdfsfsdfsfsdfsdf
                    }


                }

                if (!TextUtils.isEmpty(washironid)) {
                    if (textvalueaddforwashiron > 0) {
                        textvalueaddforwashiron = textvalueaddforwashiron - 1;
                        txtquantity.setText(textvalueaddforwashiron + "");

                        if (AppNetworkInfo.isConnectingToInternet(Laundryitemdetails.this)) {
                            DecrementQuantity_washiron();
                        } else{
                            Toast.makeText(Laundryitemdetails.this, "No network found.!", Toast.LENGTH_SHORT).show();
                        }

                    }


                        // Totalprice();


                }

            }
        });


        LL_pricelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtdisplaytotalprice.getText().toString().equals("0")){
                    GlobalUtils.showSnackBar(relativeLayout, "Please Select Quantity");
                }else if(Integer.parseInt(txtdisplaytotalprice.getText().toString())< value){
                    GlobalUtils.showSnackBar(relativeLayout,"Sorry.!!! Order should be minimum of 250");
                } else {
                    Intent i_estimatedprice = new Intent(Laundryitemdetails.this, OrderedItemsDetailsForLaundry.class);
                    // i_estimatedprice.putExtra("passitemidforwashfold", itemidforwashfold);
                    // i_estimatedprice.putExtra("passitemidforwashiron", itemidforwasiron);

                    // i_estimatedprice.putExtra("passtotalpricefromdb",priceforwashfold);
                    startActivity(i_estimatedprice);
                }
            }
        });


        //for screen animation
        overridePendingTransition(R.anim.lefttoright, R.anim.righttoleft);

    }


   /* @Override
    public void onBackPressed() {

        txtdisplaytotalprice.setText(OrderedItemsDetailsForLaundry.txt_totalprice_fromcart.getText().toString());
        super.onBackPressed();

    }*/

    protected String append(int existing_itemprice, int new_itemprice) {
        latestfavItemPrices = existing_itemprice + new_itemprice ;

        return ""+latestfavItemPrices;
    }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        editor.putInt("storedvalue", priceforwashfold);
        Log.e("enterin", "hello there");
        editor.commit();
        getFragmentManager().popBackStack();
    }*/

    private void getlaundryitemdetailsforwashfold(){

        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_laundryitemdetails = adapter.create(UohmacAPI.class);

        api_laundryitemdetails.GetLaundryDetails(washfoldid, new Callback<Response>() {
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

                responselaundryitem = sb.toString();
                Log.e("getrelaundryitem", responselaundryitem);

                try {
                    jsonArray = new JSONArray(responselaundryitem);
                    jsonObject = jsonArray.getJSONObject(0);

                    itemidforwashfold = jsonObject.getString("item_id");
                    getwashfoldname = jsonObject.getString("item_name");
                    getwashfoldimage = jsonObject.getString("item_img");
                    getwashfoldprice = jsonObject.getInt("item_price");

                    if (!TextUtils.isEmpty(getwashfoldname)) {
                        txtitemname.setText(getwashfoldname);

                    }
                    if (!TextUtils.isEmpty(String.valueOf(getwashfoldprice))) {
                        txtitemprice.setText("\u20B9" +getwashfoldprice );
                    }

                    if (!TextUtils.isEmpty(getwashfoldimage)) {
                        Picasso.with(Laundryitemdetails.this)
                                .load(getwashfoldimage)
                                .into(getimg_washfold);
                    }
                    getreturn_itemquantity_washfold();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(Laundryitemdetails.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.d("ERROR:", error.getMessage());

            }
        });
    }



    //defining the api to increament the quantity for washfold
    private void Increamentquantity_washfold(){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.show();

        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_increamentquantity = adapter.create(UohmacAPI.class);

        api_increamentquantity.IncreamentAPI(getauthkey, itemidforwashfold, new Callback<Response>() {
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
                mProgressDialog.dismiss();
                if (Incrementstatus.equals("1")) {
                    Incrementstatus = "0";
                    Totalprice();

                }

                mProgressDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(Laundryitemdetails.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.d("ERROR:", error.getMessage());

            }
        });
    }



    //defining the api to increament the quantity for washiron
    private void Increamentquantity_washiron(){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.show();
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_increamentquantity = adapter.create(UohmacAPI.class);

        api_increamentquantity.IncreamentAPI(getauthkey, itemidforwasiron, new Callback<Response>() {
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
                mProgressDialog.dismiss();
                if (Incrementstatus.equals("1"))
                {
                    Incrementstatus = "0";
                    Totalprice();
                }

            mProgressDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error)
            {
                Toast.makeText(Laundryitemdetails.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.d("ERROR:", error.getMessage());

            }
        });
    }




    //defining the api to decrement the quantity for wash fold
    private void DecrementQuantity_washfold(){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.show();
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_decrementquantity = adapter.create(UohmacAPI.class);

        api_decrementquantity.DecrementAPI(getauthkey, itemidforwashfold, new Callback<Response>() {
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
                mProgressDialog.dismiss();
                if (Decrementstatus.equals("1")) {
                    Decrementstatus = "0";
                    Totalprice();
                }
                mProgressDialog.dismiss();

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(Laundryitemdetails.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.d("ERROR:", error.getMessage());

            }
        });
    }




    //defining the api to decrement the quantity for washiron
    private void DecrementQuantity_washiron(){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.show();
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_decrementquantity = adapter.create(UohmacAPI.class);

        api_decrementquantity.DecrementAPI(getauthkey, itemidforwasiron, new Callback<Response>() {
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
                mProgressDialog.dismiss();
                if (Decrementstatus.equals("1"))
                {
                    Decrementstatus = "0";
                    Totalprice();
                }
                mProgressDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(Laundryitemdetails.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.d("ERROR:", error.getMessage());

            }
        });
    }


    //defineing api to calculate the totalprice
    private void Totalprice(){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.show();
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_totalprice = adapter.create(UohmacAPI.class);

        api_totalprice.TotalCartPrice("2", getauthkey, new Callback<Response>() {
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

                    mProgressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mProgressDialog.dismiss();
                txtdisplaytotalprice.setText(totalprice);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(Laundryitemdetails.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.d("ERROR:", error.getMessage());

            }
        });

    }




    //getting return quantity for washfold
    private void getreturn_itemquantity_washfold(){
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_itemquantity = adapter.create(UohmacAPI.class);

        api_itemquantity.getitemquantity(getauthkey, itemidforwashfold, "2", new Callback<Response>() {
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

                try {
                    JSONObject jsonObject = new JSONObject(reponse_returnitemquantity);
                    status = jsonObject.getString("status");
                    totalitemquantity = jsonObject.getString("total_item");
                    Log.d("totalitemquantity", totalitemquantity);
                    if(jsonObject.has("msg"))
                    msg = jsonObject.getString("msg");

                    if (status.equals("1")) {
                        txtquantity.setText(totalitemquantity);

                    } else if (status.equals("0")) {
                        //Toast.makeText(Laundryitemdetails.this, msg, Toast.LENGTH_SHORT).show();
                        txtquantity.setText(totalitemquantity);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(Laundryitemdetails.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.d("ERROR:", error.getMessage());
            }
        });

    }




    //getting return quantity for washiron
    private void getreturn_itemquantity_washiron(){
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_itemquantity = adapter.create(UohmacAPI.class);

        api_itemquantity.getitemquantity(getauthkey, itemidforwashfold, "2", new Callback<Response>() {
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

                try {
                    JSONObject jsonObject = new JSONObject(reponse_returnitemquantity);
                    status = jsonObject.getString("status");
                    totalitemquantity = jsonObject.getString("total_item");
                    Log.d("totalitemquantity", totalitemquantity);
                    if(jsonObject.has("msg"))
                    msg = jsonObject.getString("msg");

                    if (status.equals("1")) {
                        txtquantity.setText(totalitemquantity);

                    } else if (status.equals("0")) {
                        Toast.makeText(Laundryitemdetails.this, msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(Laundryitemdetails.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.d("ERROR:", error.getMessage());
            }
        });

    }

    private void getlaundryitemdetailsforwashiron() {

        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_laundryitemdetailsforwashiron = adapter.create(UohmacAPI.class);

        api_laundryitemdetailsforwashiron.GetLaundryDetails(washironid, new Callback<Response>() {
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

                responselaundryitem = sb.toString();
                Log.e("getrelaundryitemforwashiron", responselaundryitem);

                try {
                    jsonArray = new JSONArray(responselaundryitem);
                    jsonObject = jsonArray.getJSONObject(0);

                    itemidforwasiron = jsonObject.getString("item_id");
                    getwashironname = jsonObject.getString("item_name");
                    getwashironimage = jsonObject.getString("item_img");
                    getwashironprice = jsonObject.getInt("item_price");
                        
                    if (!TextUtils.isEmpty(getwashironname)) {
                        txtitemname.setText(getwashironname);

                    }


                    if (!TextUtils.isEmpty(String.valueOf(getwashironprice))) {
                        txtitemprice.setText("" + getwashironprice + "/-");
                    }
                    if (!TextUtils.isEmpty(getwashironimage)) {
                        Picasso.with(Laundryitemdetails.this)
                                .load(getwashironimage)
                                .into(getimg_washfold);
                    }
                    getreturn_itemquantity_washiron();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(Laundryitemdetails.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.d("ERROR:", error.getMessage());

            }
        });

    }



}

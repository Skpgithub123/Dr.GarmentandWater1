package com.uohmac.com.drgarmentandwater.Activites;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uohmac.com.drgarmentandwater.Adpater.LaundryPromoCodeListAdapter;
import com.uohmac.com.drgarmentandwater.Adpater.OrderedDetailsAdapter_Water;
import com.uohmac.com.drgarmentandwater.Api.UohmacAPI;
import com.uohmac.com.drgarmentandwater.Pojos.OrderedDetailsPojo;
import com.uohmac.com.drgarmentandwater.Pojos.PromoCodesPojo;
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

/**
 * Created by uOhmac Technologies on 11-Jan-17.
 */
public class PromoCodesListforLaundry extends AppCompatActivity {

    SharedPreferences sp;
    //public static final String MYPREFERNCES_REGISTER = "mypreferregister";
    //public static final String MYPREFERNCES_POSTFB = "mypreferpostfb";
    public static final String MYPREFERNCES_LOGIN = "mysharedpref_waterlaundry";
    ArrayList<PromoCodesPojo> promocodes_laundry = new ArrayList<PromoCodesPojo>();
    JSONObject jsonObject;
    JSONArray jsonArray;
    String getauthkey;
    ListView rvMyRequest_promocodelist;
    LaundryPromoCodeListAdapter promocodes_adapter;
    List<PromoCodesPojo> promoCodesPojoList;
    ProgressDialog mProgressDialog;
    RelativeLayout rr_nooffers_laundry;
    TextView nooffers_laundry;
    ImageView ivback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.promocodes_list);



        rvMyRequest_promocodelist = (ListView)findViewById(R.id.lv_promocodelistlistview);
        rr_nooffers_laundry = (RelativeLayout)findViewById(R.id.rr_nooffers);
        ivback = (ImageView)findViewById(R.id.ivback);
       // sp = getSharedPreferences(MYPREFERNCES_REGISTER, 0);
        nooffers_laundry = (TextView)findViewById(R.id.nooffers_laundry);

        sp = getSharedPreferences(MYPREFERNCES_LOGIN, 0);
        getauthkey = sp.getString("auth_key", "");


       /* sp = getSharedPreferences(MYPREFERNCES_POSTFB, 0);
        getauthkey = sp.getString("auth_key", "");
        Log.e("gettingauthkeyforsettings", getauthkey);*/

        if(AppNetworkInfo.isConnectingToInternet(PromoCodesListforLaundry.this)){
            GetPromoCodesList();

        } else{
            Toast.makeText(PromoCodesListforLaundry.this, "No network found.!", Toast.LENGTH_SHORT).show();
        }


        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PromoCodesListforLaundry.this, OrderedItemsDetailsForLaundry.class);
                startActivity(i);
            }
        });

    }


   /* private void GetPromoCodesList(){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.show();
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_getpromocodes = adapter.create(UohmacAPI.class);


        api_getpromocodes.GetPromoCodeList(getauthkey, new Callback<List<PromoCodesPojo>>() {
            @Override
            public void success(List<PromoCodesPojo> promoCodesPojos, Response response) {
                promoCodesPojoList = promoCodesPojos;
                promocodes_adapter = new LaundryPromoCodeListAdapter(PromoCodesListforLaundry.this, R.layout.laundrypromocodeviewing_singleitem, promoCodesPojoList);
                rvMyRequest_promocodelist.setAdapter(promocodes_adapter);

                mProgressDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Errorfound", "ERROR:" + error.getMessage(), error);
            }
        });
    }*/
   private void GetPromoCodesList(){
       mProgressDialog = new ProgressDialog(this);
       mProgressDialog.setIndeterminate(true);
       mProgressDialog.setMessage("Please Wait...");
       promocodes_laundry.clear();
       mProgressDialog.show();

       final RestAdapter adapter = new RestAdapter.Builder()
               .setEndpoint(Constatns.UOHMAC_API)
               .build();

       UohmacAPI api_getpromocodes = adapter.create(UohmacAPI.class);

       api_getpromocodes.GetPromoCodeList(getauthkey, new Callback<Response>() {
           @Override
           public void success(Response response, Response response2) {
               BufferedReader reader = null;
               String result = "";

               try {
                   //Initializing buffered reader
                   reader = new BufferedReader(new InputStreamReader(response.getBody().in()));

                   //Reading the output in the string
                   result = reader.readLine();


               } catch (IOException e)
               {
                   Log.d("IOExceptiongio", "laundrypromocoderesponce" + e.toString());
               }
               //Displaying the output as a toast
               Log.d("laundrypromocoderesponce", "success**   " + result.toString());


               if(result.toString().trim().charAt(0)=='[') {

                   try {

                       JSONArray jsonArray = new JSONArray(result);

                       if (jsonArray.length() > 0) {

                           for (int i = 0; i < jsonArray.length(); i++) {
                               JSONObject jsonObject = jsonArray.getJSONObject(i);

                               PromoCodesPojo promoCodesPojo = new PromoCodesPojo();


                               promoCodesPojo.setPromotitle(jsonObject.getString("promo_title"));
                               promoCodesPojo.setMinamount(jsonObject.getString("min_amount"));
                               promoCodesPojo.setPromoamount(jsonObject.getString("promo_amount"));
                               promoCodesPojo.setAmountwise(jsonObject.getString("amount_wise"));
                               promoCodesPojo.setUserpercust(jsonObject.getString("uses_per_cus"));
                               promoCodesPojo.setPromovalidity(jsonObject.getString("promo_validity"));


                               promocodes_laundry.add(promoCodesPojo);
                               rvMyRequest_promocodelist.setVisibility(View.VISIBLE);
                               rr_nooffers_laundry.setVisibility(View.GONE);
                               nooffers_laundry.setVisibility(View.GONE);

                           }

                           Log.d("orderdeatailarray", "sizelandury" + promocodes_laundry.size());

                           promocodes_adapter = new LaundryPromoCodeListAdapter(PromoCodesListforLaundry.this, R.layout.laundrypromocodeviewing_singleitem, promocodes_laundry);
                           rvMyRequest_promocodelist.setAdapter(promocodes_adapter);

                           mProgressDialog.dismiss();
                       }
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }else{
                   try {
                       JSONObject jsonObject_nooffers = new JSONObject(result.toString());

                       String status = jsonObject_nooffers.getString("status");


                       Log.d("statuvalues", "*****  " + status);
                       if (status.equals("0")) {

                           Toast.makeText(PromoCodesListforLaundry.this, jsonObject_nooffers.getString("msg"), Toast.LENGTH_SHORT).show();

                           rvMyRequest_promocodelist.setVisibility(View.GONE);
                           rr_nooffers_laundry.setVisibility(View.VISIBLE);
                           nooffers_laundry.setVisibility(View.VISIBLE);
                       }
                   } catch (Exception e) {
                       e.printStackTrace();
                   }


                   Toast.makeText(PromoCodesListforLaundry.this, "No Offers", Toast.LENGTH_SHORT).show();

                   mProgressDialog.dismiss();

               }

               mProgressDialog.dismiss();

           }

           @Override
           public void failure(RetrofitError error) {
               Toast.makeText(PromoCodesListforLaundry.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

               Log.e("Errorfound", "ERROR:" + error.getMessage(), error);

           }
       });
   }

}

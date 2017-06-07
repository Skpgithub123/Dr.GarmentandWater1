package com.uohmac.com.drgarmentandwater.Activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uohmac.com.drgarmentandwater.Adpater.DryCleaningAdapter;
import com.uohmac.com.drgarmentandwater.Api.UohmacAPI;
import com.uohmac.com.drgarmentandwater.R;
import com.uohmac.com.drgarmentandwater.Utils.Constatns;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by System-03 on 11/4/2016.
 */
public class DryCleaningActivity extends AppCompatActivity {
    Toolbar toolbar;
    private ViewPager viewpager;
    public static TextView tvtotalprice,tvestimatepricetext;
    //public static int storedtotalpricevalue;
    JSONObject jsonObject;
    String reponse_totalprice,getauthkey,totalprice;
    //public static final String MYPREFERNCES_TOTALPRICE = "myprefertotalprice";
    SharedPreferences sp;
    public static final String MYPREFERNCES_LOGIN = "mysharedpref_waterlaundry";
    LinearLayout LL_pricelayout_drycleaning;
    public static int gettotalpricefordrycleaning;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drycleaning);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        sp = getSharedPreferences(MYPREFERNCES_LOGIN, 0);
        getauthkey = sp.getString("auth_key", "");

        Totalprice();

       // sp = getSharedPreferences(MYPREFERNCES_TOTALPRICE, 0);
        tvtotalprice = (TextView)findViewById(R.id.txt_storedtotalprice);
        //tvestimatepricetext = (TextView)findViewById(R.id.txt_estimatedprice);
        LL_pricelayout_drycleaning = (LinearLayout)findViewById(R.id.LL_pricelayout_drycleaning);
       /* if(!TextUtils.isEmpty(Laundryitemdetails.totalprice)) {
            //gettotalpricefordrycleaning = Integer.valueOf(sp.getString("itemprice", ""));
            //Log.e("gettotalpricefordrycleaning", ""+ gettotalpricefordrycleaning);
            tvtotalprice.setText(Laundryitemdetails.totalprice);
            Log.d("getavailprice", "" + Laundryitemdetails.totalprice);
        }*/

      /*  if(!TextUtils.isEmpty(String.valueOf(gettotalpricefordrycleaning))) {
            //storedtotalpricevalue = (Integer) getIntent().getExtras().get("passtotalpricefordry");

            tvtotalprice.setText(String.valueOf(gettotalpricefordrycleaning));
            Log.d("getavailprice", "" + gettotalpricefordrycleaning);
        }*/


        //tablayout
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_layoutfordrycleaning);


        tabLayout.addTab(tabLayout.newTab().setText(Constatns.LANDING_TAB_MEN));
        tabLayout.addTab(tabLayout.newTab().setText(Constatns.LANDING_TAB_WOMEN));
        tabLayout.addTab(tabLayout.newTab().setText(Constatns.LANDING_TAB_KIDS));
        tabLayout.addTab(tabLayout.newTab().setText(Constatns.LANDING_TAB_HOUSEHOLD));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);




        viewpager = (ViewPager) findViewById(R.id.landing_pagerfordrycleaning);

        final DryCleaningAdapter adapter = new DryCleaningAdapter(getSupportFragmentManager(),tabLayout.getTabCount());

        viewpager.setAdapter(adapter);
        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

       /* db = new DataBaseHandler(this);


        db.getAllPets();

        if(!TextUtils.isEmpty(DataBaseHandler.item_price)) {
            tvtotalprice.setText(DataBaseHandler.item_price);
        }*/

        LL_pricelayout_drycleaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i_drycleaning = new Intent(DryCleaningActivity.this,OrderedItemsDetailsForLaundry.class);
                startActivity(i_drycleaning);
            }
        });


        //for screen animation
        overridePendingTransition(R.anim.lefttoright, R.anim.righttoleft);
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
                try{
                    jsonObject = new JSONObject(reponse_totalprice);
                    totalprice = jsonObject.getString("price");
                    Log.e("gettotalprice_api", totalprice);
                }catch (Exception e){
                    e.printStackTrace();
                }

                tvtotalprice.setText(totalprice);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("ERROR:", error.getMessage());

            }
        });

    }

}

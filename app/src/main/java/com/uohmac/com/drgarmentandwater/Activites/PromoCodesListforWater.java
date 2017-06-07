package com.uohmac.com.drgarmentandwater.Activites;

import android.app.ProgressDialog;
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
import com.uohmac.com.drgarmentandwater.Adpater.WaterPromoCodeListAdapter;
import com.uohmac.com.drgarmentandwater.Api.UohmacAPI;
import com.uohmac.com.drgarmentandwater.Pojos.PromoCodesPojo;
import com.uohmac.com.drgarmentandwater.R;
import com.uohmac.com.drgarmentandwater.Utils.AppNetworkInfo;
import com.uohmac.com.drgarmentandwater.Utils.Constatns;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
 * Created by uOhmac Technologies on 22-Mar-17.
 */
public class PromoCodesListforWater extends AppCompatActivity {

    ListView lv_waterpromocodeslist;

    SharedPreferences sp;
    String getauthkey;
    public static final String MYPREFERNCES_LOGIN = "mysharedpref_waterlaundry";
    ProgressDialog mProgressDialog;
    WaterPromoCodeListAdapter promocodes_adapter;
    List<PromoCodesPojo> promoCodesPojoList;
    ArrayList<PromoCodesPojo> promocodes_water = new ArrayList<PromoCodesPojo>();
    RelativeLayout rr_nooffers_promo;
    TextView nooffers_water;
    ImageView ivback_water;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.promocode_listforwater);
        lv_waterpromocodeslist = (ListView)findViewById(R.id.lv_promocodelistlistviewforwater);
        ivback_water = (ImageView)findViewById(R.id.ivback_water);
        rr_nooffers_promo = (RelativeLayout)findViewById(R.id.rr_nodata);
        nooffers_water = (TextView)findViewById(R.id.nooffers_water);
        sp = getSharedPreferences(MYPREFERNCES_LOGIN, 0);
        getauthkey = sp.getString("auth_key", "");


        if(AppNetworkInfo.isConnectingToInternet(PromoCodesListforWater.this)){
            GetPromocodeslistforwater();
        }else{
            Toast.makeText(PromoCodesListforWater.this, "No network found...Please try again later", Toast.LENGTH_LONG).show();
        }

        ivback_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PromoCodesListforWater.this,OrderedItemsDetailsForWater.class);
                startActivity(i);
            }
        });

    }


    /*private void GetPromocodeslistforwater(){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.show();

        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();
        UohmacAPI api_getpromocodesforwater = adapter.create(UohmacAPI.class);
        api_getpromocodesforwater.GetPromoCodeList(getauthkey, new Callback<List<PromoCodesPojo>>() {
            @Override
            public void success(List<PromoCodesPojo> promoCodesPojos, Response response) {
                promoCodesPojoList = promoCodesPojos;
                promocodes_adapter = new WaterPromoCodeListAdapter(PromoCodesListforWater.this, R.layout.waterpromocodeviewing_signleitem, promoCodesPojoList);
                lv_waterpromocodeslist.setAdapter(promocodes_adapter);

                mProgressDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Errorfound", "ERROR:" + error.getMessage(), error);
            }
        });


    }*/



    private void GetPromocodeslistforwater(){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please Wait...");
        promocodes_water.clear();
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


                                promocodes_water.add(promoCodesPojo);
                                lv_waterpromocodeslist.setVisibility(View.VISIBLE);
                                rr_nooffers_promo.setVisibility(View.GONE);
                                nooffers_water.setVisibility(View.GONE);

                            }

                            Log.d("orderdeatailarray", "sizelandury" + promocodes_water.size());

                            promocodes_adapter = new WaterPromoCodeListAdapter(PromoCodesListforWater.this, R.layout.waterpromocodeviewing_signleitem, promocodes_water);
                            lv_waterpromocodeslist.setAdapter(promocodes_adapter);

                            mProgressDialog.dismiss();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }else{

                    try {
                        JSONObject jsonObject = new JSONObject(result.toString());
                        String status = jsonObject.getString("status");


                        Log.d("statuvalues", "*****  " + status);

                        if (status.equals("0")) {

                            Toast.makeText(PromoCodesListforWater.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

                            lv_waterpromocodeslist.setVisibility(View.GONE);
                            rr_nooffers_promo.setVisibility(View.VISIBLE);
                            nooffers_water.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    Toast.makeText(PromoCodesListforWater.this, "No Offers", Toast.LENGTH_SHORT).show();

                    mProgressDialog.dismiss();

                }
                mProgressDialog.dismiss();

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(PromoCodesListforWater.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.e("Errorfound", "ERROR:" + error.getMessage(), error);

            }
        });
    }
}

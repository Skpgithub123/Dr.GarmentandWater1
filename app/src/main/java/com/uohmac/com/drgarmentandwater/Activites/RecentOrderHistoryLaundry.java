/*
package com.uohmac.com.drgarmentandwater.Activites;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.uohmac.com.drgarmentandwater.Adpater.MenAdapter;
import com.uohmac.com.drgarmentandwater.Adpater.RecentOrderHistoryLaundryAdapter;
import com.uohmac.com.drgarmentandwater.Api.UohmacAPI;
import com.uohmac.com.drgarmentandwater.Fragments.RecentOrderLaundryAdapter;
import com.uohmac.com.drgarmentandwater.Fragments.RecentOrderLaundryFragment;
import com.uohmac.com.drgarmentandwater.Pojos.DryCleaningPojo;
import com.uohmac.com.drgarmentandwater.Pojos.RecentOrderHistoryPojo;
import com.uohmac.com.drgarmentandwater.R;
import com.uohmac.com.drgarmentandwater.Utils.AppNetworkInfo;
import com.uohmac.com.drgarmentandwater.Utils.Constatns;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

*/
/**
 * Created by uOhmac Technologies on 30-Jan-17.
 *//*

public class RecentOrderHistoryLaundry extends AppCompatActivity {

    String getrecentorderid,getauthkey,result_recentordershistory,recentordertotalamount,recentorderaddeddate,recentorderordernumber;
    RecentOrderHistoryLaundryAdapter _orderhistoryadapter;
    List<RecentOrderHistoryPojo> recentOrderHistoryPojoList;
    SharedPreferences sp;
    ListView LV_orderhistory;
    TextView tv_passpaymentheading,tv_ordernumber,tv_addeddate;
    ImageView imgback;
    public static final String MYPREFERNCES_LOGIN = "mysharedpref_waterlaundry";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recentorderdetails);

        getrecentorderid = (String) getIntent().getExtras().get("recentorderid");
        Log.e("getrecent", getrecentorderid);
        recentordertotalamount = (String)getIntent().getExtras().get("recentordertotalprice");
        recentorderordernumber = (String)getIntent().getExtras().get("recentordernumber");
        recentorderaddeddate = (String)getIntent().getExtras().get("recentorderaddeddate");

        sp = getSharedPreferences(MYPREFERNCES_LOGIN, 0);
        getauthkey = sp.getString("auth_key", "");

        tv_passpaymentheading = (TextView)findViewById(R.id.txt_paymentheading);
        tv_ordernumber = (TextView)findViewById(R.id.txt_passorderno);
        tv_addeddate = (TextView)findViewById(R.id.txt_passaddedate);
        imgback = (ImageView)findViewById(R.id.ivback_orderhistory);

        LV_orderhistory = (ListView)findViewById(R.id.lvOrderHistoryLaundryList);


        tv_passpaymentheading.setText("Payment of Rs."+recentordertotalamount);
        tv_ordernumber.setText("#"+recentorderordernumber);
        tv_addeddate.setText(recentorderaddeddate);

        if(AppNetworkInfo.isConnectingToInternet(RecentOrderHistoryLaundry.this)){
            RecentOrderHistory();
        }else{
            AlertDialog.Builder ab = new AlertDialog.Builder(RecentOrderHistoryLaundry.this);
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
                    RecentOrderHistory();

                }
            });
            ab.create();
            ab.show();
        }


        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }



    private void RecentOrderHistory(){
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();


        UohmacAPI api_recentorderhistory = adapter.create(UohmacAPI.class);

        api_recentorderhistory.RecentOrdersHistory(getauthkey, getrecentorderid, new Callback<List<RecentOrderHistoryPojo>>() {
            @Override
            public void success(List<RecentOrderHistoryPojo> recentOrderHistoryPojos, Response response) {
                recentOrderHistoryPojoList = recentOrderHistoryPojos;

                _orderhistoryadapter = new RecentOrderHistoryLaundryAdapter(RecentOrderHistoryLaundry.this,R.layout.recentorder_historyitem, recentOrderHistoryPojos);
                LV_orderhistory.setAdapter(_orderhistoryadapter);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("ERROR:", error.getMessage());

            }
        });
    }
}
*/

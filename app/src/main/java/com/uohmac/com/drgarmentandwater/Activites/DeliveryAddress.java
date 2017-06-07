package com.uohmac.com.drgarmentandwater.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.uohmac.com.drgarmentandwater.R;

/**
 * Created by SUNIL on 9/30/2016.
 */
public class DeliveryAddress extends AppCompatActivity {


    public static String tookaddress,tooklandmark,tookcity,tookpincode;
    TextView txt_billingaddr;
    Toolbar toolbar;
    Button btn_placeorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deliveryaddress);


        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        btn_placeorder = (Button)findViewById(R.id.btn_placeorder);
        txt_billingaddr = (TextView)findViewById(R.id.txt_valuebilladdress);

        tookaddress = (String) getIntent().getExtras().get("takeaddress");
        tooklandmark = (String) getIntent().getExtras().get("takeecity");
        tookcity = (String) getIntent().getExtras().get("takelandmark");
        tookpincode = (String) getIntent().getExtras().get("takepincode");


        Log.d("checkvalue","?????"+tookaddress+"2===="+tooklandmark+"3===="+tookcity);


        txt_billingaddr.setText(tookaddress + "" + tookcity + "" + tooklandmark + "" + tookpincode);


        btn_placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i_pay = new Intent(DeliveryAddress.this,OrderConfirmation.class);
                startActivity(i_pay);
            }
        });


    }
}

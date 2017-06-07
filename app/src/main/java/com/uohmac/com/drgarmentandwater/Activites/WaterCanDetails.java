package com.uohmac.com.drgarmentandwater.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.uohmac.com.drgarmentandwater.R;

/**
 * Created by SUNIL on 9/29/2016.
 */
public class WaterCanDetails extends AppCompatActivity {

    Button btn_makepayment;
    Toolbar toolbar;
    ImageView img1,img2,img3,img4,imgbackarrow;
    TextView txt_quantity,txt_returncan;
    int textvalueforquantity,textvalueforreturncan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waterquantityconfirmation);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        btn_makepayment = (Button)findViewById(R.id.btn_proceedtopay);
        img1 = (ImageView) findViewById(R.id.img_addquantity);
        img2 = (ImageView) findViewById(R.id.img_subquantity);
        img3 = (ImageView) findViewById(R.id.img_addreturncan);
        img4 = (ImageView) findViewById(R.id.img_subreturncan);
        txt_quantity = (TextView)findViewById(R.id.txt_valuequantity);
        txt_returncan = (TextView)findViewById(R.id.txt_valuereturncan);
        imgbackarrow = (ImageView)findViewById(R.id.img_backarrow);


        imgbackarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i_arrow = new Intent(WaterCanDetails.this,HomeActivity.class);
                startActivity(i_arrow);
            }
        });

        btn_makepayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i_makepay = new Intent(WaterCanDetails.this,GettingAddress.class);
                startActivity(i_makepay);

            }
        });

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txt_quantity.setText(Integer.toString(textvalueforquantity));
                textvalueforquantity++;

            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_quantity.setText(Integer.toString(textvalueforquantity));
                textvalueforquantity--;
            }
        });


        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_returncan.setText(Integer.toString(textvalueforreturncan));
                textvalueforreturncan++;
            }
        });

        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_returncan.setText(Integer.toString(textvalueforreturncan));
                textvalueforreturncan--;
            }
        });
    }
}

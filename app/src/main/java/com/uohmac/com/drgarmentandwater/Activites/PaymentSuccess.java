package com.uohmac.com.drgarmentandwater.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.uohmac.com.drgarmentandwater.R;

/**
 * Created by uOhmac Technologies on 12-Apr-17.
 */


public class PaymentSuccess extends AppCompatActivity {


    String Amount,TxnID;
    TextView txtamount,txttxnid;
    Button btnorderanother;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paymentsuccess);
        txtamount = (TextView)findViewById(R.id.txtamount);
        txttxnid = (TextView)findViewById(R.id.txttxnid);
        btnorderanother = (Button)findViewById(R.id.btnorderanother);
        Amount = (String)getIntent().getExtras().get("amount");
        TxnID = (String)getIntent().getExtras().get("txnid");


        txtamount.setText(Amount);
        txttxnid.setText(TxnID);


        btnorderanother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i_backtohome = new Intent(PaymentSuccess.this,HomeActivity.class);
                startActivity(i_backtohome);
                finish();
            }
        });
    }
}

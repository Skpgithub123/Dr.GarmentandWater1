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
public class PaymentFailure extends AppCompatActivity {

    String txnid,amt;
    TextView txtamount_failed,txttxanid_failed;
    Button btn_returntoshopping;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paymentfailure);
        txtamount_failed = (TextView)findViewById(R.id.txtamount_failed);
        txttxanid_failed = (TextView)findViewById(R.id.txttxanid_failed);
        btn_returntoshopping = (Button)findViewById(R.id.btn_returntoshopping);
        txnid = (String)getIntent().getExtras().get("txnid_failed");
        amt = (String)getIntent().getExtras().get("amount_failed");

        txtamount_failed.setText(amt);
        txttxanid_failed.setText(txnid);


        btn_returntoshopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i_backtohome = new Intent(PaymentFailure.this,HomeActivity.class);
                startActivity(i_backtohome);
                finish();
            }
        });

    }
}

package com.uohmac.com.drgarmentandwater.Activites;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.uohmac.com.drgarmentandwater.R;
import com.uohmac.com.drgarmentandwater.Utils.AppNetworkInfo;
import com.uohmac.com.drgarmentandwater.Utils.GlobalUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Bharathee SM on 11/24/2016.
 */
public class OTPVerfiyForForgotpwd extends AppCompatActivity {

    EditText et_verfiycode;
    Button btnverifycode;
    String getmobverfiy, sessionid, otpcode, status;
    JSONObject jobjverify;
    RelativeLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verfiyotpforgotpwd);
        et_verfiycode = (EditText) findViewById(R.id.et_forgotpwdotp);
        btnverifycode = (Button) findViewById(R.id.btn_forgotpwdotp);

        sessionid = (String) getIntent().getExtras().get("sessionkey");
        getmobverfiy = (String)getIntent().getExtras().get("mobtoforgotpwd");

        relativeLayout = (RelativeLayout)findViewById(R.id.relative);

        btnverifycode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpcode = et_verfiycode.getText().toString();


                if(otpcode.length()<0 ){
                    et_verfiycode.setError(getString(R.string.error_field_required));
                    et_verfiycode.requestFocus();

                }else if(AppNetworkInfo.isConnectingToInternet(OTPVerfiyForForgotpwd.this)){
                    new VerfiyOTPCode().execute();
                } else{
                    Toast.makeText(OTPVerfiyForForgotpwd.this, "No network found.!", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }


    class VerfiyOTPCode extends AsyncTask<String, String, String> {

        private String VERFIYOTP = "https://2factor.in/API/V1/6af2cf45-8faf-11e6-96db-00163ef91450/SMS/VERIFY/"+sessionid+"/"+otpcode+"";
        String jsonverfiyotpresponce = "";

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(VERFIYOTP);
                String verfiyresponse;

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestProperty("Content-Type",
                        "application/json");
                conn.connect();
                verfiyresponse = conn.getResponseMessage();
                Log.e("resverfiy", verfiyresponse);

                InputStream in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line = null;

                try {
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    in.close();
                    reader.close();
                    jsonverfiyotpresponce = sb.toString();
                    Log.e("jsondata", jsonverfiyotpresponce);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        conn.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonverfiyotpresponce;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                jobjverify = new JSONObject(jsonverfiyotpresponce);
                Log.e("getverifiyotpjsonres", "" + jobjverify);

                status = jobjverify.getString("Details");
                Log.e("getverfiyotpstring", status);
                if (status.equals("OTP Matched")) {
                    Intent i_otpforgotpwd = new Intent(OTPVerfiyForForgotpwd.this, NewPassword.class);
                    i_otpforgotpwd.putExtra("mob", getmobverfiy);
                    startActivity(i_otpforgotpwd);
                    finish();

                }else if(status.equals("OTP Mismatch")){
                    GlobalUtils.showSnackBar(relativeLayout, status);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

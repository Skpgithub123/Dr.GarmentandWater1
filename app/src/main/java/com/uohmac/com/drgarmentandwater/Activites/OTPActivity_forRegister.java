package com.uohmac.com.drgarmentandwater.Activites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.uohmac.com.drgarmentandwater.LoginActivity;
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
 * Created by uOhmac Technologies on 22-Apr-17.
 */
public class OTPActivity_forRegister extends AppCompatActivity {
    Button btn;
    EditText ed_verfiyotp;
    static String sessionid="",code="",status="",pwd,fbmob;
    JSONObject jobjverify;
    SharedPreferences sp;
    SharedPreferences.Editor editor ;
    public static final String MYPREFERNCES_LOGIN = "mysharedpref_waterlaundry";

    private static int  REQUEST_READ_PHONE_STATE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpforregister);
        btn = (Button)findViewById(R.id.btn_verfiycode_reg);
        ed_verfiyotp = (EditText)findViewById(R.id.editverfiycode_reg);

        sp = getSharedPreferences(MYPREFERNCES_LOGIN, Context.MODE_PRIVATE);
        editor = sp.edit();


        sessionid = sp.getString("sessionkey_forregister", "");
        Log.e("getsessionkey_reg", sessionid);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code = ed_verfiyotp.getText().toString();
                if(ed_verfiyotp.getText().toString().equals("")) {
                    ed_verfiyotp.setError(getString(R.string.error_field_required));
                    ed_verfiyotp.requestFocus();


                }else if(AppNetworkInfo.isConnectingToInternet(OTPActivity_forRegister.this)){
                    new VerfiryOTP_ForRegister().execute();
                }else{
                    Toast.makeText(OTPActivity_forRegister.this, "No network found... Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        }
    }


    class VerfiryOTP_ForRegister extends AsyncTask<String,String,String>{
        private  String VERFIYOTP= "https://2factor.in/API/V1/6af2cf45-8faf-11e6-96db-00163ef91450/SMS/VERIFY/"+sessionid+"/"+code+"";

        String jsonverfiyotpresponce="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try{
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
                Log.e("resverfiy_register", verfiyresponse);

                InputStream in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line = null;

                try{
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    in.close();
                    reader.close();
                    jsonverfiyotpresponce = sb.toString();
                    Log.e("jsondata",jsonverfiyotpresponce);
                }catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    try {
                        conn.disconnect();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                }catch (Exception e){
                    e.printStackTrace();
                }


            return jsonverfiyotpresponce;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try{
                jobjverify = new JSONObject(jsonverfiyotpresponce);
                Log.e("getverifiyotpjsonres_reg", ""+jobjverify);

                status = jobjverify.getString("Details");
                Log.e("getverfiyotpstring_reg", status);

            }catch (Exception e){
                e.printStackTrace();
            }

            if (status.equals("OTP Matched")) {

                Intent i_login = new Intent(OTPActivity_forRegister.this, LoginActivity.class);
                editor.putString("otp", code);
                    /*editor.putString("firstName", fn);
                    editor.putString("lastName", ln);
                    editor.putString("email", email);
                    editor.putString("profilePicUrl", profileimage);
                    editor.putString("facebookid", facebook_id);
                    editor.putString("mobile", fbmob);*/
                   /* editor.putBoolean("hasLoggedin", true);*/
                Log.e("entering into OTP SP", "hello otp SP");
                editor.putString("status", "successfully");
                editor.commit();
                startActivity(i_login);
                finish();
                Toast.makeText(getBaseContext(), "OTP Matched", Toast.LENGTH_LONG).show();
            }else if(status.equals("OTP Mismatch")){

                Toast.makeText(getBaseContext(), "OTP Mismatch", Toast.LENGTH_LONG).show();
            }
        }
    }
}

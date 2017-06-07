package com.uohmac.com.drgarmentandwater.Activites;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
 * Created by SUNIL on 9/14/2016.
 */
public class OTPActivity extends AppCompatActivity {

    Button btn;
    EditText ed_verfiyotp;
    static String sessionid="",code="",status="",pwd,fbmob;
    JSONObject jobjverify;
    RelativeLayout rr;
    SharedPreferences sp;
    SharedPreferences.Editor editor ;
    public static final String MYPREFERNCES_LOGIN = "mysharedpref_waterlaundry";

   // public static final String MYPREFERNCES_OTP = "mypreferotp";
  // public static final String MYPREFERNCES_FBLOGIN = "mypreferfblogin";
    String fn,ln,email,enteredmail,fullname,profileimage,facebook_id;
   // public static final String MYPREFERNCES_NORMALLOGIN = "mypreferregister";

    private static int  REQUEST_READ_PHONE_STATE=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        btn = (Button)findViewById(R.id.btn_verfiycode);
        ed_verfiyotp = (EditText)findViewById(R.id.editverfiycode);
        rr = (RelativeLayout)findViewById(R.id.rr);

       // sp = getSharedPreferences(MYPREFERNCES_OTP, 0);
        sp = getSharedPreferences(MYPREFERNCES_LOGIN, Context.MODE_PRIVATE);
        editor = sp.edit();

       // sessionid = (String) getIntent().getExtras().get("sessionkey");


        //checking wheather user logged in or no
       // sp = getSharedPreferences(MYPREFERNCES_OTP,  0);
       /* boolean hasLoggedin = sp.getBoolean("hasLoggedin", false);
        if (hasLoggedin) {
            Intent intent = new Intent(OTPActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }*/
       // sp = getSharedPreferences(MYPREFERNCES_NORMALLOGIN, 0);
       // sp = getSharedPreferences(MYPREFERNCES_FBLOGIN, 0);
        sessionid = sp.getString("sessionkey", "");
        Log.e("getsessionkey", sessionid);
        //facebook login details
       /* fn = sp.getString("firstName", "");
        ln = sp.getString("lastName", "");
        email = sp.getString("email","");
        fbmob = sp.getString("mobile", "");
        profileimage = sp.getString("profilePicUrl", "");
        Log.e("getstorefn", fn);
        Log.e("getstoreln", ln);
        Log.e("getstoreemail", email);
        Log.e("getstorefbmobilenumber", fbmob);*/

       // fullname = fn+ln;

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                code = ed_verfiyotp.getText().toString();
                /*Intent intent=OTPActivity.this.getIntent();
                if(intent !=null) {
                    sessionid = intent.getStringExtra("sessionkey");
                    Log.e("getsessionidforverify","" + sessionid);
                }*/
                if(ed_verfiyotp.getText().toString().equals("")) {
                    ed_verfiyotp.setError(getString(R.string.error_field_required));
                    ed_verfiyotp.requestFocus();


                }else if(AppNetworkInfo.isConnectingToInternet(OTPActivity.this)){
                    new VerfiryOTP().execute();
                }else{
                    Toast.makeText(OTPActivity.this,"No network found... Please try again",Toast.LENGTH_SHORT).show();
                }






            }
        });
        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        }


    }


    class VerfiryOTP extends AsyncTask<String,String,String>{

        private  String VERFIYOTP= "https://2factor.in/API/V1/6af2cf45-8faf-11e6-96db-00163ef91450/SMS/VERIFY/"+sessionid+"/"+code+"";
       // https://2factor.in/API/V1/6af2cf45-8faf-11e6-96db-00163ef91450/SMS/VERIFY/78c8eb52-271a-11e7-929b-00163ef91450/477834
        //https://2factor.in/API/V1/6af2cf45-8faf-11e6-96db-00163ef91450/SMS/VERIFY/f6a7f3a4-9b65-11e6-a40f-00163ef91450/803521
        String jsonverfiyotpresponce="";

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
                Log.e("resverfiy", verfiyresponse);

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
                Log.e("getverifiyotpjsonres", ""+jobjverify);

                status = jobjverify.getString("Details");
                Log.e("getverfiyotpstring", status);


            }catch (Exception e){
                e.printStackTrace();
            }

            if (status.equals("OTP Matched")) {
                Toast.makeText(getBaseContext(), "OTP Matched", Toast.LENGTH_LONG).show();
                Intent i_login = new Intent(OTPActivity.this, HomeActivity.class);
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
                OTPActivity.this.finish();

            }else if(status.equals("OTP Mismatch")){
                GlobalUtils.showSnackBar(rr, status);
                Toast.makeText(getBaseContext(), "OTP Mismatch", Toast.LENGTH_LONG).show();
            }
        }
    }
}

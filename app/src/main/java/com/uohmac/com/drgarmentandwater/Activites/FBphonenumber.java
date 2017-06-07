package com.uohmac.com.drgarmentandwater.Activites;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.uohmac.com.drgarmentandwater.R;
import com.uohmac.com.drgarmentandwater.Utils.AppNetworkInfo;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by System-03 on 10/27/2016.
 */
public class FBphonenumber extends AppCompatActivity {

    EditText et_mob;
    Button btnfbmob;
    public static String getmobforfb;
    JSONObject jobj;
    String fn,ln,email,enteredmail,fullname,profileimage,fbid;
    SharedPreferences sp;
    SharedPreferences.Editor edit;

    public static final String MYPREFERNCES_LOGIN = "mysharedpref_waterlaundry";
    //public static final String MYPREFERNCES_FBLOGIN = "mypreferfblogin";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fbphone);
        et_mob = (EditText)findViewById(R.id.editmobilefb);
        btnfbmob = (Button)findViewById(R.id.btnfbmobile);


        sp = getSharedPreferences(MYPREFERNCES_LOGIN, 0);
        edit = sp.edit();
      //  sp = getSharedPreferences(MYPREFERNCES_FBLOGIN, 0);


        /*//facebook login details
        fn = sp.getString("firstName", "");
        ln = sp.getString("lastName", "");
        email = sp.getString("email","");
        profileimage = sp.getString("profilePicUrl", "");
        fbid = sp.getString("facebookid", "");
        Log.e("getstorefn", fn);
        Log.e("getstoreln", ln);
        Log.e("getstoreemail", email);
        fullname = fn+ln;*/


       // sp = getSharedPreferences(MYPREFERNCES_FBLOGIN, 0);
       /* boolean hasLoggedin = sp.getBoolean("hasLoggedin", false);
        if (hasLoggedin) {
            Intent intent = new Intent(FBphonenumber.this, OTPActivity.class);
            startActivity(intent);
            finish();
        }*/
        btnfbmob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getmobforfb = et_mob.getText().toString();
                Log.e("fbmobile", getmobforfb);



                if(getmobforfb.length() < 0){
                    et_mob.setError(getString(R.string.error_field_required));
                    et_mob.requestFocus();
                } else if(AppNetworkInfo.isConnectingToInternet(FBphonenumber.this)){
                    new getotpforFB().execute();
                }  else{
                    Toast.makeText(FBphonenumber.this, "No network found.!", Toast.LENGTH_SHORT).show();
                }
               /* if(AppNetworkInfo.isConnectingToInternet(FBphonenumber.this)) {
                    if (getmobforfb.length() > 0) {
                        new getotpforFB().execute();
                    } else {
                        et_mob.setError(getString(R.string.error_field_required));
                        et_mob.requestFocus();
                    }
                }*/
            }
        });
    }


    class getotpforFB extends AsyncTask<String,String,String> {
        private  String OTPURLFORFB= "https://2factor.in/API/V1/6af2cf45-8faf-11e6-96db-00163ef91450/SMS/"+getmobforfb+"/AUTOGEN";
        String sessionid="";

        String jsonotpresponceforfb="";
        String check="";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(OTPURLFORFB);
                String response;


                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestProperty("Content-Type",
                        "application/json");
                conn.connect();
                response = conn.getResponseMessage();
                Log.e("res", response);

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
                    jsonotpresponceforfb = sb.toString();
                    Log.e("jsondataforfb",jsonotpresponceforfb);
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



            }catch (IOException e) {

                e.printStackTrace();
            }
            return jsonotpresponceforfb;

        }

        @Override
        protected void onPostExecute(String s) {
            try {
                jobj = new JSONObject(jsonotpresponceforfb);
                Log.e("getotpjsonresforfb", ""+jobj);

                check = jobj.getString("Status");
                Log.e("getotpstringforfb", check);
                String sessionid = jobj.getString("Details");
                Log.e("getsessionidforfb==", sessionid);
                if (check.equals("Success")) {
                    Intent i_login = new Intent(FBphonenumber.this, OTPActivity.class);
                    edit.putString("sessionkey", sessionid);
                    edit.putString("mobile", getmobforfb);
                   // edit.putBoolean("hasLoggedin", true);
                    edit.putString("status", "successfully");
                    Log.e("enteringintoLoginSPfromFBformob", "hello fbgetmobile SP");
                    edit.commit();
                   // i_login.putExtra("sessionkey",sessionid);
                    startActivity(i_login);
                    finish();

                }

                /*else if (status.equals("Username or Password wrong!")) {
                    GlobalUtils.showSnackBar(linearLayout, status);
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }


            super.onPostExecute(s);
        }
    }
}

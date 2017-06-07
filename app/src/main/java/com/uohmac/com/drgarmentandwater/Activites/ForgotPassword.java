package com.uohmac.com.drgarmentandwater.Activites;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.uohmac.com.drgarmentandwater.R;
import com.uohmac.com.drgarmentandwater.Utils.AppNetworkInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by SUNIL on 9/14/2016.
 */
public class ForgotPassword extends AppCompatActivity {

    EditText et_forgotpwdmobile;
    Button btn_forgotpwd;
    JSONObject jsonObject, jobj;
    JSONArray forgotpwdresponse;
    String result, status;
    String sessionid, mobile;
    View focusView = null;
    boolean cancel = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpassword);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        et_forgotpwdmobile = (EditText) findViewById(R.id.editmobile);
        btn_forgotpwd = (Button) findViewById(R.id.btnsubmit);

        btn_forgotpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile = et_forgotpwdmobile.getText().toString();

                if(mobile.length() < 0){
                    et_forgotpwdmobile.setError(getString(R.string.error_field_required));
                    et_forgotpwdmobile.requestFocus();
                }else if(AppNetworkInfo.isConnectingToInternet(ForgotPassword.this)){
                    new getotp().execute();
                }
                else{
                    Toast.makeText(ForgotPassword.this, "No network found.!", Toast.LENGTH_SHORT).show();
                }
                /*if (mobile.length() > 0) {
                    new getotp().execute();
                } else {
                    et_forgotpwdmobile.setError(getString(R.string.error_field_required));
                    et_forgotpwdmobile.requestFocus();
                }*/
            }
        });
    }

        class getotp extends AsyncTask<String, String, String> {
            private String OTPURL = "https://2factor.in/API/V1/6af2cf45-8faf-11e6-96db-00163ef91450/SMS/"+mobile+"/AUTOGEN";


            String jsonotpresponce = "";
            String checkres = "";


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL(OTPURL);
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

                    try {
                        while ((line = reader.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        in.close();
                        reader.close();
                        jsonotpresponce = sb.toString();
                        Log.e("jsondata", jsonotpresponce);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            conn.disconnect();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                } catch (IOException e) {

                    e.printStackTrace();
                }
                return jsonotpresponce;

            }

            @Override
            protected void onPostExecute(String s) {
                try {
                    jobj = new JSONObject(jsonotpresponce);
                    Log.e("getotpjsonres", "" + jobj);

                    checkres = jobj.getString("Status");
                    Log.e("getotpstring", checkres);
                    sessionid = jobj.getString("Details");
                    Log.e("getsessionid==", sessionid);
                    if (checkres.equals("Success")) {
                        Intent i_login = new Intent(ForgotPassword.this, OTPVerfiyForForgotpwd.class);
                        i_login.putExtra("sessionkey",sessionid);
                        i_login.putExtra("mobtoforgotpwd", mobile);
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

    /*private boolean validation() {
        mobile = et_forgotpwdmobile.getText().toString().trim();

        if (!AppNetworkInfo.isConnectingToInternet(ForgotPassword.this)) {
            Toast.makeText(ForgotPassword.this, "No Network found. Please try later.", Toast.LENGTH_LONG).show();
            return false;
        } else {

            if (TextUtils.isEmpty(mobile)) {
                // inputfirstname.setErrorEnabled(true);
                et_forgotpwdmobile.setError(getString(R.string.error_field_required));
                focusView = et_forgotpwdmobile;
                cancel = true;
            }
            if (cancel) {
                // There was an error; don't attempt login and focus the first
                // form field with an error.
                focusView.requestFocus();
                return false;
            }

            return true;
        }
    }*/




}

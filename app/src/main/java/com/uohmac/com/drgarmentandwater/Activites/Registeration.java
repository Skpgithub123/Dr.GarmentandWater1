package com.uohmac.com.drgarmentandwater.Activites;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uohmac.com.drgarmentandwater.Api.UohmacAPI;
import com.uohmac.com.drgarmentandwater.LoginActivity;
import com.uohmac.com.drgarmentandwater.Manifest;
import com.uohmac.com.drgarmentandwater.R;
import com.uohmac.com.drgarmentandwater.Utils.AppNetworkInfo;
import com.uohmac.com.drgarmentandwater.Utils.Constatns;
import com.uohmac.com.drgarmentandwater.Utils.GlobalUtils;
import com.uohmac.com.drgarmentandwater.Utils.ValidateUserInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by SUNIL on 9/8/2016.
 */
public class Registeration extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    EditText et_lastname,et_mobile,et_firstname,et_email,et_pwd,et_confirmpwd,et_refercode;
    Button btn_reg;
    static String mob,firstname,lastname,mobile,email,password,confirmpwd,referandearn,result,status="",firstnamefromfb,lastnamefromfb,emailfromfb,mobileforotp,imei=null,ts;
    View focusView = null;
    boolean cancel = false;
    TextView txt_alreadyhaveacc,tvtermsconditions;
    RelativeLayout relativeLayout;
    TextInputLayout inputfirstname,inputlastname,inputmobilenumber,inputemail,inputpassword,inputconfirmpwd;
    JSONArray Jarray;
    JSONObject jobj;
    JSONObject jobj1;
    CheckBox cb_termscondition;
    private static int  REQUEST_READ_PHONE_STATE=1;
    TelephonyManager mTelephonyMgr;
    SharedPreferences sp;
    SharedPreferences.Editor editor ;
    String sessionid,msg="",authkey,response_termsandcondtions="",test="no";
    ProgressDialog mProgressDialog;
   // public static final String MYPREFERNCES_REGISTER = "mypreferregister";

    public static final String MYPREFERNCES_LOGIN = "mysharedpref_waterlaundry";

    private static String TAG_RESPONSE = "msg";
    private static String TAG_STATUS = "status";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        et_lastname = (EditText)findViewById(R.id.etlastname);
        et_firstname = (EditText)findViewById(R.id.etfirstname);
        et_mobile = (EditText)findViewById(R.id.etmobile);
        et_email = (EditText)findViewById(R.id.etemail);
        et_pwd = (EditText)findViewById(R.id.etpwd);
        et_confirmpwd = (EditText)findViewById(R.id.etconfirmpwd);
        et_refercode = (EditText)findViewById(R.id.etreferalcode);
        txt_alreadyhaveacc = (TextView)findViewById(R.id.txt_alredyhaveaccount);
        tvtermsconditions = (TextView)findViewById(R.id.txt_termsconditions);
        btn_reg = (Button)findViewById(R.id.btnRegister);
        relativeLayout = (RelativeLayout)findViewById(R.id.layoutforreg);
        cb_termscondition = (CheckBox)findViewById(R.id.checkBox_termscondition);

        cb_termscondition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {


                    test = "yes";
                }else
                {
                    test = "no";
                }
            }
        });
        sp = getSharedPreferences(MYPREFERNCES_LOGIN, Context.MODE_PRIVATE);
        editor = sp.edit();


       /* Spannable WordtoSpan = new SpannableString("Terms and Conditions");
        WordtoSpan.setSpan(new ForegroundColorSpan(Color.BLUE), 2, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvtermsconditions.setText(WordtoSpan);*/


        inputfirstname = (TextInputLayout)findViewById(R.id.inputfirstname);
        inputlastname = (TextInputLayout)findViewById(R.id.inputlastname);
        inputmobilenumber = (TextInputLayout)findViewById(R.id.input_mobile);
        inputemail = (TextInputLayout)findViewById(R.id.inputemail);
        inputpassword = (TextInputLayout)findViewById(R.id.input_password);
        inputconfirmpwd = (TextInputLayout)findViewById(R.id.input_confirmpwd);


        //checking wheather user logged in or no
        //sp = getSharedPreferences(MYPREFERNCES_REGISTER,  0);
       /* boolean hasLoggedin = sp.getBoolean("hasLoggedin", false);
        if (hasLoggedin) {
            Intent intent = new Intent(Registeration.this, OTPActivity.class);
            startActivity(intent);
            finish();
        }*/
        // firstnamefromfb = (String) getIntent().getExtras().get("firstName");
        //lastnamefromfb = (String) getIntent().getExtras().get("lastName");
        //emailfromfb = (String) getIntent().getExtras().get("email");


       /* cb_termscondition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    RegisterUser();
                }else{
                    GlobalUtils.showSnackBar(relativeLayout, "Please accept terms and conditions");
                }
            }
        });*/


        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ts = Context.TELEPHONY_SERVICE;
                mTelephonyMgr = (TelephonyManager) getSystemService(ts);
                imei = mTelephonyMgr.getDeviceId();
                Log.e("deviceid===", imei);

                if(validation()){


                    if(AppNetworkInfo.isConnectingToInternet(Registeration.this)){

                        if(test.equals("yes")) {

                            RegisterUser();
                        }else{
                            GlobalUtils.showSnackBar(relativeLayout, "Please accept terms and conditions");
                        }

                    } else{
                        Toast.makeText(Registeration.this,"No network found.! Please try again", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        txt_alreadyhaveacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i_haveaccount = new Intent(Registeration.this, LoginActivity.class);
                startActivity(i_haveaccount);
            }
        });




        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        }


        tvtermsconditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i_terms = new Intent(Registeration.this, TermsandConditions.class);
                startActivity(i_terms);
            }
        });



    }

    private boolean validation() {
        firstname = et_firstname.getText().toString().trim();
        lastname = et_lastname.getText().toString().trim();
        email = et_email.getText().toString().trim();
        password = et_pwd.getText().toString().trim();
        mobile = et_mobile.getText().toString().trim();
        confirmpwd = et_confirmpwd.getText().toString();
        referandearn = et_refercode.getText().toString();

        if (!AppNetworkInfo.isConnectingToInternet(Registeration.this)) {
            Toast.makeText(Registeration.this, "No Network found. Please try later.", Toast.LENGTH_LONG).show();
            return false;
        } else {

            if (TextUtils.isEmpty(firstname)) {
               // inputfirstname.setErrorEnabled(true);
                et_firstname.setError(getString(R.string.error_field_required));
                focusView = et_firstname;
                cancel = true;
            }
            if (TextUtils.isEmpty(lastname)) {
                //inputlastname.setErrorEnabled(true);
                et_lastname.setError(getString(R.string.error_field_required));
                focusView = et_lastname;
                cancel = true;
            }
            if (TextUtils.isEmpty(email)) {
                //inputemail.setErrorEnabled(true);
                et_email.setError(getString(R.string.error_field_required));
                focusView = et_email;
                cancel = true;
            } else if (!ValidateUserInfo.isEmailValid(email)) {
                //inputemail.setErrorEnabled(true);
                et_email.setError(getString(R.string.error_invalid_email));
                focusView = et_email;
                cancel = true;
            }
            if (TextUtils.isEmpty(password)) {
               // inputpassword.setErrorEnabled(true);
                et_pwd.setError(getString(R.string.error_field_required));
                focusView = et_lastname;
                cancel = true;
            }
            if (TextUtils.isEmpty(mobile)) {
               // inputmobilenumber.setErrorEnabled(true);
                et_mobile.setError(getString(R.string.error_field_required));
                focusView = et_lastname;
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
    }

    private void RegisterUser(){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.show();
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_reguser = adapter.create(UohmacAPI.class);

        if(password.equals(confirmpwd)) {

            api_reguser.RegisterUser(firstname, lastname, email, password, mobile,imei,referandearn, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    BufferedReader reader = null;
                    StringBuilder sb = new StringBuilder();
                    try {

                        reader = new BufferedReader(new InputStreamReader(response.getBody().in()));

                        String line;

                        try {
                            while ((line = reader.readLine()) != null) {
                                sb.append(line);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    result = sb.toString();
                   // Toast.makeText(Registeration.this, result, Toast.LENGTH_LONG).show();

                    String check="";
                    try{
                        /* Jarray = new JSONArray(result);
                        Log.e("getres", ""+Jarray);*/

                        JSONObject jobj = new JSONObject(result);
                        msg = jobj.getString("msg");
                        Log.e("msgfroreg", msg);
                        authkey = jobj.getString("auth_key");
                        Log.e("authkeyforreg", authkey);
                        status = jobj.getString("status");


                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    if(status.equals("1")){
                        Intent i = new Intent(Registeration.this, OTPActivity.class);
                        editor.putString("firstName", firstname);
                        editor.putString("lastName", lastname);
                        editor.putString("email", email);
                        editor.putString("password", password);
                        editor.putString("confirmpassword", confirmpwd);
                        editor.putString("mobile", mobile);
                        editor.putString("auth_key", authkey);
                        editor.putString("status", "successfully");
                        Log.e("entering into Register SP", "hello register SP");
                        editor.commit();
                        startActivity(i);
                        finish();

                        new getotp().execute();

                    }else{
                        try{
                            JSONObject jobj1 = new JSONObject(result);
                            mob = jobj1.getString("mobile");

                            Log.d("emailmsg", "==" + mob);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        if(mob.equals("this Mobile is already exist.")) {

                            GlobalUtils.showSnackBar(relativeLayout, "This mobile number is already exist.");
                        }
                        //Toast.makeText(Registeration.this, "This Mobile is already exist.", Toast.LENGTH_LONG).show();

                    }

                    mProgressDialog.dismiss();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(Registeration.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                    Log.d("ERROR:", error.getMessage());
                    mProgressDialog.dismiss();
                }
            });
        }else {
            GlobalUtils.showSnackBar(relativeLayout, getString(R.string.error_passwordnotmatch));
        }
    }


    class getotp extends AsyncTask<String,String,String>{
        private  String OTPURL= "https://2factor.in/API/V1/6af2cf45-8faf-11e6-96db-00163ef91450/SMS/"+mobile+"/AUTOGEN";



        String jsonotpresponce="";
        String checkres="";


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

                try{
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    in.close();
                    reader.close();
                    jsonotpresponce = sb.toString();
                    Log.e("jsondata",jsonotpresponce);
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
            return jsonotpresponce;

        }

        @Override
        protected void onPostExecute(String s) {
            try {
                jobj = new JSONObject(jsonotpresponce);
                Log.e("getotpjsonres", ""+jobj);

                checkres = jobj.getString("Status");
                Log.e("getotpstring", checkres);
                sessionid = jobj.getString("Details");
                Log.e("getsessionid==", sessionid);
                if (checkres.equals("Success")) {
                    Intent i_login = new Intent(Registeration.this, OTPActivity_forRegister.class);
                    //i_login.putExtra("sessionkey",sessionid);
                    editor.putString("sessionkey_forregister", sessionid);
                    editor.putString("status", "successfully");
                    Log.e("entering into OTP SP", "hello otp SP");
                    editor.commit();
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

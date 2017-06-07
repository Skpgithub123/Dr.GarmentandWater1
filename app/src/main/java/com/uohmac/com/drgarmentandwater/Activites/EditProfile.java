package com.uohmac.com.drgarmentandwater.Activites;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.uohmac.com.drgarmentandwater.Api.UohmacAPI;
import com.uohmac.com.drgarmentandwater.R;
import com.uohmac.com.drgarmentandwater.Utils.AppNetworkInfo;
import com.uohmac.com.drgarmentandwater.Utils.Constatns;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by SUNIL on 9/16/2016.
 */
public class EditProfile extends AppCompatActivity {

    Toolbar toolbar;
    ImageView img_back;
    String getfn,getln,getauthkey,getmob,getemail,getmobfromfb,getfnfromfb,getlnfromfb,result,editfirstname,editlastname,editmob,msgs;
    EditText et_firstname,et_lastname,et_mobile,et_email;
    TextView txtsave;
    SharedPreferences sp;
    SharedPreferences.Editor ed;
    JSONObject jobj;
   // public static final String MYPREFERNCES_REGISTER = "mypreferregister";
    //public static final String MYPREFERNCES_POSTFB = "mypreferpostfb";
    //public static final String MYPREFERNCES_FBLOGIN = "mypreferfblogin";
    public static final String MYPREFERNCES_LOGIN = "mysharedpref_waterlaundry";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        img_back = (ImageView)findViewById(R.id.ivHome);
        et_firstname = (EditText) findViewById(R.id.etFirstname);
        et_lastname = (EditText)findViewById(R.id.etLastname);
        et_mobile = (EditText)findViewById(R.id.etMobileNumber);
        et_email = (EditText)findViewById(R.id.etEmail);
        txtsave = (TextView)findViewById(R.id.txt_save);


        /*//getting authkey from sp
        sp = getSharedPreferences(MYPREFERNCES_POSTFB, 0);
        ed = sp.edit();
        getauthkey = sp.getString("auth_key", "");
        Log.e("gettingauthkeyforsettings", getauthkey);


        //this are from registertion class
        sp = getSharedPreferences(MYPREFERNCES_REGISTER, 0);
        ed = sp.edit();
        //getauthkey = sp.getString("auth_key","");
        getfn = sp.getString("firstName", "");
        getln = sp.getString("lastName", "");
        getmob = sp.getString("mobile", "");

        sp = getSharedPreferences(MYPREFERNCES_FBLOGIN, 0);
        ed = sp.edit();
        getmob = sp.getString("getnumberforfb", "");*/

        sp = getSharedPreferences(MYPREFERNCES_LOGIN, 0);
        ed = sp.edit();

        getauthkey = sp.getString("auth_key", "");
        getfn = sp.getString("firstName", "");
        getln = sp.getString("lastName", "");
        getemail = sp.getString("email", "");
        getmob = sp.getString("mobile", "");
       // sp = getSharedPreferences(MYPREFERNCES_EDITPROFILE, 0);





        //for normal login
       /* if(!TextUtils.isEmpty(getmob)&&!TextUtils.isEmpty(getfn)&&!TextUtils.isEmpty(getln)) {
            et_firstname.setText(getfn);
            et_lastname.setText(getln);
            et_mobile.setText(getmob);
        }*/

        //for facebook login
        if(!TextUtils.isEmpty(getmob)&&!TextUtils.isEmpty(getfn)&&!TextUtils.isEmpty(getln)){
            et_firstname.setText(getfn);
            et_lastname.setText(getln);
            et_mobile.setText(getmob);
            et_email.setText(getemail);
        }





        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i_backtohome = new Intent(EditProfile.this,HomeActivity.class);
                startActivity(i_backtohome);
            }
        });


        txtsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(AppNetworkInfo.isConnectingToInternet(EditProfile.this)){
                    editprofile();
                    if(!TextUtils.isEmpty(editfirstname)&&!TextUtils.isEmpty(editlastname)&&!TextUtils.isEmpty(editmob)){


                        et_firstname.setText(editfirstname);
                        et_lastname.setText(editlastname);
                        et_mobile.setText(editmob);
                    }

                } else{
                    Toast.makeText(EditProfile.this,"No network found... Please try again",Toast.LENGTH_SHORT).show();
                }


               /* if(!AppNetworkInfo.isConnectingToInternet(EditProfile.this)) {
                    Toast.makeText(EditProfile.this, "No Network found. Please try later.", Toast.LENGTH_LONG).show();
                }else{

                    editprofile();
                    if(!TextUtils.isEmpty(editfirstname)&&!TextUtils.isEmpty(editlastname)&&!TextUtils.isEmpty(editmob)){


                        et_firstname.setText(editfirstname);
                        et_lastname.setText(editlastname);
                        et_mobile.setText(editmob);
                    }
                }*/
            }
        });

    }



    private void editprofile(){
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_editprofile = adapter.create(UohmacAPI.class);

        api_editprofile.EditProfile(getauthkey, et_firstname.getText().toString(), et_lastname.getText().toString(), et_mobile.getText().toString(), new Callback<Response>() {
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
                Log.e("resforeditprofile", result);
                try{
                    jobj = new JSONObject(result);
                    Log.e("resforedi", ""+jobj);
                    msgs = jobj.getString("msg");
                    Log.e("msgs", msgs);

                    editfirstname = jobj.getString("first_name");
                    Log.e("editfirstname", editfirstname);

                    editlastname = jobj.getString("last_name");
                    Log.e("editlastname", editlastname);

                    editmob = jobj.getString("mobile");
                       Intent i = new Intent(EditProfile.this, HomeActivity.class);
                   /* sp = getSharedPreferences(MYPREFERNCES_LOGIN, 0);
                    ed = sp.edit();

                    sp = getSharedPreferences(MYPREFERNCES_FBLOGIN, 0);
                    ed = sp.edit();

                    sp = getSharedPreferences(MYPREFERNCES_REGISTER, 0);
                    ed = sp.edit();*/

                    ed.putString("firstName", editfirstname);
                    ed.putString("lastName", editlastname);
                    ed.putString("getnumberforfb", editmob);
                    ed.putString("mobile", editmob);
                    Log.e("updating profile", "updated");
                    ed.commit();


                    /*ed.putString("first_name", editfirstname);
                    ed.putString("last_name", editlastname);
                    ed.putString("mobile", editmob);
                    Log.e("updating for normal reg", "updated");
                    ed.apply();*/
                    startActivity(i);

                        finish();

                    Toast.makeText(EditProfile.this, msgs,Toast.LENGTH_SHORT).show();

                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("ERROR:", error.getMessage());

            }
        });

    }
}

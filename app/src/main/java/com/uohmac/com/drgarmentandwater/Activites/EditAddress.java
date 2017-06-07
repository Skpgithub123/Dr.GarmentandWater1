/*
package com.uohmac.com.drgarmentandwater.Activites;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.uohmac.com.drgarmentandwater.Api.UohmacAPI;
import com.uohmac.com.drgarmentandwater.R;
import com.uohmac.com.drgarmentandwater.Utils.AppNetworkInfo;
import com.uohmac.com.drgarmentandwater.Utils.Constatns;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

*/
/**
 * Created by System-03 on 11/14/2016.
 *//*

public class EditAddress extends AppCompatActivity {

    EditText et_editflatno,et_editlandmark,et_editlocality;
    String flatnoforedit,landmarkforedit,localityforedit,getauthkey,pincode,city,state,country,lat,lng,resforupdateaddress;
    Button btn_saveandcontinue;
    SharedPreferences sp;
    TextView txt_addnewaddress;
    SharedPreferences.Editor ed;
    public static final String MYPREFERNCES_LOGIN = "mysharedpref_waterlaundry";

   // public static final String MYPREFERNCES_REGISTER = "mypreferregister";
   // public static final String MYPREFERNCES_POSTFB = "mypreferpostfb";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editmapaddress);

        et_editflatno = (EditText) findViewById(R.id.eteditflatno);
        et_editlandmark = (EditText)findViewById(R.id.eteditlandmark);
        et_editlocality = (EditText)findViewById(R.id.eteditlocality);
        btn_saveandcontinue = (Button)findViewById(R.id.btnsavecontinue);
        txt_addnewaddress = (TextView)findViewById(R.id.txt_addingnewaddress);


        //getting authkey from sp
       */
/* sp = getSharedPreferences(MYPREFERNCES_POSTFB, 0);
        getauthkey = sp.getString("auth_key", "");
        Log.e("gettingauthkeyforsettings", getauthkey);


        sp = getSharedPreferences(MYPREFERNCES_REGISTER, 0);
        getauthkey = sp.getString("auth_key", "");
*//*


        sp = getSharedPreferences(MYPREFERNCES_LOGIN, 0);
        ed = sp.edit();


        getauthkey = sp.getString("auth_key", "");
        Log.e("getting_editaddr", getauthkey);

        flatnoforedit = (String) getIntent().getExtras().get("editflatno");
        landmarkforedit = (String) getIntent().getExtras().get("editlandmark");
        localityforedit = (String) getIntent().getExtras().get("editlocality");

        pincode = Getaddressforlaundry.postalCode;
        city = Getaddressforlaundry.city;
        state = Getaddressforlaundry.state;
        country = Getaddressforlaundry.country;
        lat = String.valueOf(Getaddressforlaundry.latitude);
        lng = String.valueOf(Getaddressforlaundry.longitude);

        et_editlocality.setText(flatnoforedit);
        et_editlandmark.setText(landmarkforedit);
        et_editflatno.setText(localityforedit);


        btn_saveandcontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(AppNetworkInfo.isConnectingToInternet(EditAddress.this)){
                    updateaddress();

                } else{
                    AlertDialog.Builder ab = new AlertDialog.Builder(EditAddress.this);
                    ab.setMessage("No network found... Please try again");
                    ab.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    ab.setNegativeButton("TryAgain", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        updateaddress();
                        }
                    });
                    ab.create();
                    ab.show();
                }


            }
        });


        txt_addnewaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i_addnewaddress = new Intent(EditAddress.this, AddNewAddress.class);
                startActivity(i_addnewaddress);
                finish();
            }
        });



    }

    private void updateaddress(){
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_updateaddress = adapter.create(UohmacAPI.class);

        api_updateaddress.UpdateAddress(getauthkey, pincode, flatnoforedit, flatnoforedit, city, state, landmarkforedit, country, lat, lng, new Callback<Response>() {
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

                resforupdateaddress = sb.toString();
                Log.e("resforupdateaddress", resforupdateaddress);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("ERROR:", error.getMessage());
            }
        });


    }
}
*/

package com.uohmac.com.drgarmentandwater.Activites;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.uohmac.com.drgarmentandwater.Api.UohmacAPI;
import com.uohmac.com.drgarmentandwater.LoginActivity;
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
 * Created by Bharathee SM on 11/24/2016.
 */
public class NewPassword extends AppCompatActivity {

    EditText et_newpwd,et_confirmpwd;
    Button btn_submit;
    String mobverfiy,newpwd,confirmpwd,res,status;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newpassword);

        mobverfiy = (String) getIntent().getExtras().get("mob");
        Log.d("mob_forforgotpwd", mobverfiy);

        et_newpwd = (EditText)findViewById(R.id.Newpassword);
        et_confirmpwd = (EditText)findViewById(R.id.conformpassword);
        btn_submit = (Button)findViewById(R.id.Update_Password);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 if(AppNetworkInfo.isConnectingToInternet(NewPassword.this)){
                    Forgotpassword();

                } else{
                    Toast.makeText(NewPassword.this, "No network found..", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean Forgotpassword(){
        newpwd = et_newpwd.getText().toString();
        confirmpwd = et_confirmpwd.getText().toString();

        boolean isValid = true;

        if(TextUtils.isEmpty(newpwd)){
            et_newpwd.setError(getString(R.string.error_field_required));
            et_newpwd.requestFocus();
        }
        if(TextUtils.isEmpty(confirmpwd)){
            et_confirmpwd.setError(getString(R.string.error_field_required));
            et_confirmpwd.requestFocus();
        }else if(newpwd.equals(confirmpwd)) {

            final RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint(Constatns.UOHMAC_API)
                    .build();

            UohmacAPI api_forgotpwd = adapter.create(UohmacAPI.class);

            api_forgotpwd.ForgotPassword(mobverfiy, newpwd, confirmpwd, new Callback<Response>() {
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


                    res = sb.toString();

                    try {
                        jsonObject = new JSONObject(res);
                        Log.e("getjsonresponse", res);

                        status = jsonObject.getString("status");
                        Log.e("getstring", status);

                        if (status.equals("0")) {
                            Toast.makeText(NewPassword.this, status, Toast.LENGTH_LONG).show();

                        } else if (status.equals("1")) {
                            Intent i_otp = new Intent(NewPassword.this, LoginActivity.class);
                            startActivity(i_otp);
                            finish();
                            Toast.makeText(NewPassword.this, "Successfully Changed", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(NewPassword.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                    Log.d("ERROR:", error.getMessage());

                }
            });
        }else{
            isValid = false;
            et_confirmpwd.setError(getString(R.string.password_does_not_match));
            et_confirmpwd.requestFocus();
        }
        return isValid;
    }
}

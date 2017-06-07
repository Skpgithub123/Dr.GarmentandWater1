package com.uohmac.com.drgarmentandwater.Fragments;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.uohmac.com.drgarmentandwater.Api.UohmacAPI;
import com.uohmac.com.drgarmentandwater.LoginActivity;
import com.uohmac.com.drgarmentandwater.R;
import com.uohmac.com.drgarmentandwater.Utils.AppNetworkInfo;
import com.uohmac.com.drgarmentandwater.Utils.Constatns;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Bharathee SM on 11/23/2016.
 */
public class ChangePasswordFragment extends Fragment implements View.OnClickListener {

    private View myFragmentView;

    private EditText et_currentpwd,et_newpassword,et_confirmpassword;
    private String currentpassword,newpassword,confirmpassword,getauthkey;
    private Button btn_update;
    String result,status;
    JSONObject jsonObject,jobj;
    JSONArray changepwdresponse;
    public static final String MYPREFERNCES_LOGIN = "mysharedpref_waterlaundry";
    SharedPreferences sp;
    public ChangePasswordFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        sp = getActivity().getSharedPreferences(MYPREFERNCES_LOGIN, 0);
        getauthkey = sp.getString("auth_key", "");
        Log.e("gettingauthkeyforchangepwd", getauthkey);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Change Password");
        myFragmentView = inflater.inflate(R.layout.dialogpassword, container, false);
        et_currentpwd = (EditText)myFragmentView.findViewById(R.id.currentpassword);
        et_newpassword = (EditText)myFragmentView.findViewById(R.id.newpassword);
        et_confirmpassword = (EditText)myFragmentView.findViewById(R.id.confirmpassword);
        btn_update = (Button)myFragmentView.findViewById(R.id.updatepassbt);


        btn_update.setOnClickListener(this);

        return myFragmentView;
    }

    public static ChangePasswordFragment newInstance(){
        return new ChangePasswordFragment();
    }



    @Override
    public void onClick(View v) {
        if(AppNetworkInfo.isConnectingToInternet(getActivity())){
            changepassword();

        } else{
            Toast.makeText(getActivity(), "No network found.!", Toast.LENGTH_SHORT).show();
        }

        removetext();
    }

    private boolean changepassword(){
        currentpassword = et_currentpwd.getText().toString().trim();
        newpassword = et_newpassword.getText().toString().trim();
        confirmpassword = et_confirmpassword.getText().toString().trim();

        boolean isValid = true;

        if (TextUtils.isEmpty(currentpassword)) {
            isValid = false;
            et_currentpwd.setError(getString(R.string.error_field_required));
            et_currentpwd.requestFocus();
        }
        if (TextUtils.isEmpty(newpassword)) {
            isValid = false;
            et_newpassword.setError(getString(R.string.error_field_required));
            et_newpassword.requestFocus();
        }
        if (TextUtils.isEmpty(confirmpassword)) {
            isValid = false;
            et_confirmpassword.setError(getString(R.string.error_field_required));
            et_confirmpassword.requestFocus();
        }else if (newpassword.equals(confirmpassword)) {
            final RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint(Constatns.UOHMAC_API)
                    .build();

            UohmacAPI api_changepassword = adapter.create(UohmacAPI.class);

            api_changepassword.ChangePassword(currentpassword, newpassword, confirmpassword, getauthkey, new Callback<Response>() {
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
                    //Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
                    Log.d("chnagepasswordresponse", result);

                    try{
                        jsonObject = new JSONObject(result);
                        Log.e("getjsonresponse", result);

                        status = jsonObject.getString("status");
                        Log.e("getstring", status);

                        if(status.equals("0")){
                            Toast.makeText(getActivity(), status, Toast.LENGTH_LONG).show();

                        }else if(status.equals("1")){
                            Intent i_otp = new Intent(getActivity(), LoginActivity.class);
                            startActivity(i_otp);
                            getActivity().finish();
                            Toast.makeText(getActivity(), "Password Changed Successfully", Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), "Please try after sometime.", Toast.LENGTH_SHORT).show();

                    Log.d("ERROR:", error.getMessage());

                }
            });

        }else{
            isValid = false;
            et_confirmpassword.setError(getString(R.string.password_does_not_match));
            et_confirmpassword.requestFocus();
        }
        return isValid;
    }

    private void removetext(){
        et_currentpwd.setText("");
        et_newpassword.setText("");
        et_confirmpassword.setText("");
    }

}

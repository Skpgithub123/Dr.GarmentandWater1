package com.uohmac.com.drgarmentandwater;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.squareup.picasso.Picasso;
import com.uohmac.com.drgarmentandwater.Activites.FBphonenumber;
import com.uohmac.com.drgarmentandwater.Activites.ForgotPassword;
import com.uohmac.com.drgarmentandwater.Activites.HomeActivity;
import com.uohmac.com.drgarmentandwater.Activites.Registeration;
import com.uohmac.com.drgarmentandwater.Api.UohmacAPI;
import com.uohmac.com.drgarmentandwater.Utils.AppNetworkInfo;
import com.uohmac.com.drgarmentandwater.Utils.Constatns;
import com.uohmac.com.drgarmentandwater.Utils.GlobalUtils;
import com.uohmac.com.drgarmentandwater.Utils.ValidateUserInfo;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by SUNIL on 9/12/2016.
 */
public class LoginActivity extends AppCompatActivity {

    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = LoginActivity.class.getSimpleName();
    private ProgressDialog mProgressDialog;
    private static final int RC_SIGN_IN = 007;
    private Button btnSignIn;
    private Button btnSignOut, btnRevokeAccess;
    private LinearLayout llProfileLayout;
    private AccessTokenTracker mtracker = null;
    private ProfileTracker mprofileTracker = null;
    public static final String PARCEL_KEY = "parcel_key";
    SharedPreferences sp;

    public static final String MYPREFERNCES_LOGIN = "mysharedpref_waterlaundry";

    private LoginButton loginButton;

    EditText editemail,editpassword;
    Button btn_submit;
   public static String email="",password="",result="",status="",responsetocheckfblogin="",statusfb="",fbidexist="",auth_key="",msg="";
    View focusView = null;
    boolean cancel = false;
    TextView txt_register,txt_forgotpwd;
    private LoginButton btnFBlogin;
    private CallbackManager callbackManager;
     SharedPreferences.Editor editor ;
    JSONObject jobj,jobjfb;
    LinearLayout linearLayout;
    private ImageView imgProfilePic;
    private LoginActivity mloginactivity;
    private boolean mSignInClicked;
    private com.facebook.AccessToken _token;
    String ID,firstName,lastName,u,auth_key_fbalreadyexist="",mob_fbalreadyexist="";
    EditText et;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.statusbar));

        }


        btnFBlogin = (LoginButton)findViewById(R.id.btnFBlogin);

        sp = getSharedPreferences(MYPREFERNCES_LOGIN, Context.MODE_PRIVATE);
        editor = sp.edit();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mloginactivity = this;
        editemail = (EditText)findViewById(R.id.txtEmail);
        editpassword = (EditText)findViewById(R.id.etLoginPassword);
        btn_submit = (Button)findViewById(R.id.btnLoginSubmit);
        txt_register = (TextView)findViewById(R.id.tvRegister);
        txt_forgotpwd = (TextView)findViewById(R.id.tvForgot);
        imgProfilePic = (ImageView) findViewById(R.id.imgapplogo);
       // btnSignIn = (Button) findViewById(R.id.btn_sign_in);
       // btnSignOut = (Button) findViewById(R.id.btn_sign_out);
       // btnRevokeAccess = (Button) findViewById(R.id.btn_revoke_access);
        linearLayout = (LinearLayout)findViewById(R.id.login_activity_lay);




       // btnSignIn.setOnClickListener(this);
       // btnSignOut.setOnClickListener(this);
       // btnRevokeAccess.setOnClickListener(this);



      /*  mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();*/

      /*  btnSignIn.setSize(SignInButton.SIZE_STANDARD);
        btnSignIn.setScopes(gso.getScopeArray());*/


        //checking wheather user logged in or no
        sp = getSharedPreferences(MYPREFERNCES_LOGIN,  0);
      /*  boolean hasLoggedin = sp.getBoolean("hasLoggedin", false);
        if (hasLoggedin) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }*/



        btnFBlogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.d("JSON value : ", object.toString());
                                firstName = "";
                                lastName = "";
                                email = "";

                                u = "";
                                ID = "";
                                String profilePicUrl = "";
                                String profilePic = "";
                                try {

                                    firstName = object.getString("first_name");
                                    lastName = object.getString("last_name");
                                    email = object.getString("email");
                                    ID = object.getString("id");
                                    profilePicUrl = object.getString("picture");

                                    JSONObject jsonObject = new JSONObject(profilePicUrl);
                                    Log.e("json", "" + jsonObject);
                                    JSONObject jobj = jsonObject.getJSONObject("data");
                                    u = jobj.getString("url");
                                    Log.e("jsonimage", u);

                                    /*if(!TextUtils.isEmpty(profilePicUrl)) {
                                        Picasso.with(LoginActivity.this)
                                                .load(u)
                                                .into(img_pro);
                                    }*/
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                if(AppNetworkInfo.isConnectingToInternet(LoginActivity.this)){
                                    CheckFBLogin();
                                }else{
                                    Toast.makeText(LoginActivity.this, "No network found.!", Toast.LENGTH_SHORT).show();
                                }
                                /*else{
                                    AlertDialog.Builder ab = new AlertDialog.Builder(LoginActivity.this);
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
                                            CheckFBLogin();

                                        }
                                    });
                                    ab.create();
                                    ab.show();
                                }*/

                                // editor.putString("facebooklogin", "yes");
                                // editor.commit();
                               /* profilePic = ID+firstName+lastName+email+profilePicUrl;
                                Log.e("getstatus", profilePic);
                                Toast.makeText(LoginActivity.this, profilePic, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LoginActivity.this, FBphonenumber.class);
                                editor.putString("firstName", firstName);
                                editor.putString("lastName", lastName);
                                editor.putString("email", email);
                                editor.putString("profilePicUrl", u);
                                editor.putString("facebookid", ID);
                                editor.putBoolean("hasLoggedin", true);
                                Log.e("enteringintoLoginSPfromFB", "hello fblogin SP");
                                editor.commit();
                                *//*intent.putExtra("firstName", firstName);
                                intent.putExtra("lastName", lastName);
                                intent.putExtra("email", email);
                                intent.putExtra("profilePicUrl",u);*//*
                                startActivity(intent);
                                finish();*/
                            }

                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email, picture");
                request.setParameters(parameters);
                request.executeAsync();


            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Login attempt canceled.", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException error) {
                Log.d("FB error : ", error.toString());
                Toast.makeText(LoginActivity.this, "Login attempt failed.", Toast.LENGTH_LONG).show();
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (AppNetworkInfo.isConnectingToInternet(LoginActivity.this)) {
                    try {
                        PackageInfo info = getPackageManager().getPackageInfo(
                                "com.uohmac.com.drgarmentandwater",
                                PackageManager.GET_SIGNATURES);
                        for (Signature signature : info.signatures) {
                            MessageDigest md = MessageDigest.getInstance("SHA");
                            md.update(signature.toByteArray());
                            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                        }
                    } catch (PackageManager.NameNotFoundException e) {

                    } catch (NoSuchAlgorithmException e) {

                    }


                    if (validate()) {
                        if(AppNetworkInfo.isConnectingToInternet(LoginActivity.this)){
                            LoginUser();
                        }
                            else{
                                Toast.makeText(LoginActivity.this, "No network found.!", Toast.LENGTH_SHORT).show();
                            }

                    }


           //     }

            }
        });

        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i_login = new Intent(LoginActivity.this, Registeration.class);
                startActivity(i_login);

            }
        });

        txt_forgotpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i_forgotpwd = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(i_forgotpwd);

            }
        });



    }







    /*private void checkFbGoogleLogin() {
        SharedPreferences pref = getSharedPreferences("SignupDetails", MODE_PRIVATE);
        if(pref.getString("facebooklogin", "").equals("yes")){
            LoginUser mLoginUser = new LoginUser();
            mLoginUser.onloginDetailsListener(mLoginScreen);
            mLoginUser.sendLoginDetailsToServer(mLoginScreen, pref.getString("email_id", ""), pref.getString("password", ""));
        }

        if(pref.getString("googlelogin", "").equals("yes")){
            LoginUser mLoginUser = new LoginUser();
            mLoginUser.onloginDetailsListener(mLoginScreen);
            mLoginUser.sendLoginDetailsToServer(mLoginScreen, pref.getString("email_id", ""), pref.getString("password", ""));
        }
    }*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //handleSignInResult(result);
        }
    }

   /* @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
           // handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    //handleSignInResult(googleSignInResult);
                }
            });
        }
    }*/




   /* @Override
    protected void onStop() {
        super.onStop();

            if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

    }*/


   /* @Override
    public void onConnected(Bundle bundle) {
        mSignInClicked = false;
        Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
        updateUI(true);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
        updateUI(false);
    }*/



   /* public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_sign_in:
                signIn();
                break;

            case R.id.btn_sign_out:
                signOut();
                break;

            case R.id.btn_revoke_access:
                revokeAccess();
                break;
        }
    }*/



    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            //updateUI(true);

            Log.e(TAG, "display name: " + acct.getDisplayName());

            String personName = acct.getDisplayName();
            String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();

            Log.e(TAG, "Name: " + personName + ", email: " + email
                    + ", Image: " + personPhotoUrl);

            // txtName.setText(personName);
            //txtEmail.setText(email);
            Glide.with(getApplicationContext()).load(personPhotoUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProfilePic);

        } else {
            // Signed out, show unauthenticated UI.
            //updateUI(false);
        }
    }




    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                       // updateUI(false);
                    }
                });
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                       // updateUI(false);
                    }
                });
    }


    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            btnSignIn.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);

        } else {
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);

        }
    }


  /*  @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }*/

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }


    private boolean validate(){

        email = editemail.getText().toString();
        password = editpassword.getText().toString();

        boolean isValid = true;

        if(!AppNetworkInfo.isConnectingToInternet(LoginActivity.this)){
            Toast.makeText(LoginActivity.this, "No Network found. Please try later.", Toast.LENGTH_LONG).show();
            return false;
        }else{
            if(TextUtils.isEmpty(email)){
                isValid = false;
                editemail.setError(getString(R.string.error_field_required));
                editemail.requestFocus();
            }/*else if (ValidateUserInfo.isEmailValid(email)) {
                //inputemail.setErrorEnabled(true);
                isValid = false;
                editemail.setError(getString(R.string.error_invalid_email));
                focusView = editemail;
                cancel = true;
            }*/
            if(TextUtils.isEmpty(password)){
                isValid = false;
                editpassword.setError(getString(R.string.error_field_required));
                editpassword.requestFocus();

            }



            return isValid;
        }
    }

    //for normal login email and password
    private void LoginUser(){

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.show();
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_login = adapter.create(UohmacAPI.class);

        api_login.Login(email, password, new Callback<Response>() {
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
                // Toast.makeText(LoginActivity.this, result, Toast.LENGTH_LONG).show();
                try {
                    jobj = new JSONObject(result);
                    Log.e("getres", ""+jobj);

                    status = jobj.getString("status");
                    Log.e("getstring", status);
                    msg = jobj.getString("msg");

                    if (status.equals("1")) {
                        email = jobj.getString("email");
                        auth_key = jobj.getString("auth_key");
                        Intent i_login = new Intent(LoginActivity.this, HomeActivity.class);

                        editor.putString("email", email);
                        editor.putString("pass_word", password);
                        editor.putString("auth_key", auth_key);
                        //editor.putBoolean("hasLoggedin", true);
                        editor.putString("status", "successfully");
                        Log.e("entering into Login SP", "hello login SP");
                        editor.commit();
                        startActivity(i_login);
                        LoginActivity.this.finish();
                        Toast.makeText(LoginActivity.this, msg,Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(LoginActivity.this, msg,Toast.LENGTH_SHORT).show();
                    /*if (status.equals("Username or Password wrong!")) {
                        GlobalUtils.showSnackBar(linearLayout, status);
                    }else if(status.equals("Email or Password incorrect!")){
                        GlobalUtils.showSnackBar(linearLayout, status);

                    }*/
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                mProgressDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("ERROR:", error.getMessage());
                Toast.makeText(LoginActivity.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

            }
        });
    }


    //for fb login checking API service
    private void CheckFBLogin(){
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_checkfblogin = adapter.create(UohmacAPI.class);

        api_checkfblogin.CheckFBLogin(ID, new Callback<Response>() {
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


                responsetocheckfblogin = sb.toString();
                Log.e("checkfblogin", responsetocheckfblogin);



                try{
                    jobjfb = new JSONObject(responsetocheckfblogin);
                    statusfb = jobjfb.getString("status");
                    msg = jobjfb.getString("msg");
                    auth_key_fbalreadyexist = jobjfb.getString("auth_key");
                    mob_fbalreadyexist = jobjfb.getString("mobile");
                   // fbidexist = jobjfb.getString("fb_id");
                    Log.e("getstring", statusfb);

                    if(statusfb.equals("1")){
                        Intent intent = new Intent(LoginActivity.this, FBphonenumber.class);

                        editor.putString("firstName", firstName);
                        editor.putString("lastName", lastName);
                        editor.putString("email", email);
                        editor.putString("profilePicUrl", u);
                        editor.putString("facebookid", ID);
                      /*  editor.putBoolean("hasLoggedin", true);*/
                        editor.putString("status", "successfully");
                        Log.e("enteringintoLoginSPfromFB", "hello fblogin SP");
                        editor.commit();
                        startActivity(intent);
                        finish();

                    }else if(statusfb.equals("0")) {
                        Toast.makeText(LoginActivity.this, "This facebook user is already exists",Toast.LENGTH_SHORT).show();
                        Intent i_directlytohome = new Intent(LoginActivity.this, HomeActivity.class);
                        editor.putString("firstName", firstName);
                        editor.putString("lastName", lastName);
                        editor.putString("email", email);
                        editor.putString("profilePicUrl", u);
                        editor.putString("facebookid", ID);
                        editor.putString("auth_key", auth_key_fbalreadyexist);
                        editor.putString("mobile", mob_fbalreadyexist);
                        editor.putString("status", "successfully");
                      /*  editor.putBoolean("hasLoggedin", true);*/
                        Log.e("enteringintoLoginSPfromFB", "hello fblogin SP");
                        editor.commit();
                        startActivity(i_directlytohome);
                        finish();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(LoginActivity.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.d("ERROR:", error.getMessage());

            }
        });

    }



}

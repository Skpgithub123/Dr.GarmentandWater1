package com.uohmac.com.drgarmentandwater.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
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
 * Created by Bharathee SM on 11/24/2016.
 */
public class ReferandEarnFragment extends Fragment {

    private View myFragmentView;
    //public static final String MYPREFERNCES_REGISTER = "mypreferregister";
    //public static final String MYPREFERNCES_POSTFB = "mypreferpostfb";

    public static final String MYPREFERNCES_LOGIN = "mysharedpref_waterlaundry";

    SharedPreferences sp;
    String getauthkey,result,st,refermsg,refercode,frndearn,youearn;
    Toolbar toolbar;
    TextView txt_refercode,txt_frndearn,txtyouearn;
    JSONObject jsonObject;
    ImageView img_whatsapp,img_email,img_msg,img_fb,img_twitter;

    public ReferandEarnFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        //getting authkey from sp

        sp = getActivity().getSharedPreferences(MYPREFERNCES_LOGIN, 0);
        getauthkey = sp.getString("auth_key", "");
        Log.e("gettingauthkey_referearn", getauthkey);

    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().setTitle("Refer and Earn");
        if(AppNetworkInfo.isConnectingToInternet(getActivity())){
            referandearn();
        }else{
            Toast.makeText(getActivity(), "No network found.!", Toast.LENGTH_SHORT).show();
        }
        /*myFragmentView = inflater.inflate(R.layout.fragmentreferandearn, container, false);

        txt_refercode = (TextView) myFragmentView.findViewById(R.id.txtrefercode);
        txt_frndearn = (TextView)myFragmentView.findViewById(R.id.txtfrndearnvalue);
        txtyouearn = (TextView)myFragmentView.findViewById(R.id.txtyouearnvalue);
        img_whatsapp = (ImageView)myFragmentView.findViewById(R.id.imgwhatsapp);
        img_email = (ImageView)myFragmentView.findViewById(R.id.imgemail);
        img_msg = (ImageView)myFragmentView.findViewById(R.id.imgmsg);
        img_fb = (ImageView)myFragmentView.findViewById(R.id.imgfb);
        img_twitter = (ImageView)myFragmentView.findViewById(R.id.imgtwitter);

        if(!AppNetworkInfo.isConnectingToInternet(getActivity())) {
            Toast.makeText(getActivity(), "No Network found. Please try later.", Toast.LENGTH_LONG).show();
        }else{
            referandearn();
        }


        img_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWhatsApp();
            }
        });

        img_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareEmail();
            }
        });

        img_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWithFB();
            }
        });

        img_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareMessage();
            }
        });

        img_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWithTwitter();
            }
        });

        return myFragmentView;*/


        return null;
    }








    public void shareWhatsApp(){
        PackageManager pm = getActivity().getPackageManager();
        try {

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            String text = "Check out the Sprinklehub App for your all Water and Laundry Services." +
                    " "+ "Download it today from http:\\/\\/sprinklehub.com\\/";

            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.whatsapp");

            waIntent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(waIntent, "Share with"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(getActivity(), "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
        }
    }


    public void shareEmail(){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
                Uri.parse("mailto:" + Uri.encode("address")));

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Download the Sprinklehub today");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hey," +
                "I just downloaded Sprinklehub on my SmartPhone." +
                "It is a app which helps you to get support services of all Water and Laundry" +
                " "+ "Get it now from http:\\/\\/sprinklehub.com\\/ and say good bye to all your worries.");
        startActivity(Intent.createChooser(emailIntent, "Send email via..."));
    }


    public void shareWithFB(){

        Toast.makeText(getActivity(), "Fb Share", Toast.LENGTH_SHORT).show();
        CallbackManager callbackManager;
        ShareDialog shareDialog;
        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentTitle("Sprinklehub")
                .setContentDescription(
                        "Check out Sprinklehub App for your all Water and Laundry Services" +
                                " "+ "Download it today from")
                .setContentUrl(Uri.parse("http:\\/\\/sprinklehub.com\\/"))
                .build();

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {

            @Override
            public void onCancel() {
                Toast.makeText(getActivity(), "Fb Share onCancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Sharer.Result result) {
                Toast.makeText(getActivity(), "Fb Share onSuccess", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getActivity(), "Fb Share onError", Toast.LENGTH_SHORT).show();
            }
        });

        shareDialog.show(linkContent);
    }


    public void shareMessage(){
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");
//        smsIntent.putExtra("address", "12125551212");
        smsIntent.putExtra("sms_body", "Check out Sprinklehub App for your all Water and laundry Services" +
                " " + "Download it today from http:\\/\\/sprinklehub.com\\/");
        startActivity(smsIntent);
    }


    public void shareWithTwitter(){
        String tweeturl = "https://twitter.com/intent/tweet?text=Check out Sprinklehub for your all Water and Laundry Services" +
                " "+ "Download it today from http:\\/\\/sprinklehub.com\\/";
        Uri uri = Uri.parse(tweeturl);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }






    private void referandearn(){
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_referandearn = adapter.create(UohmacAPI.class);

        api_referandearn.ReferandEarn(getauthkey, new Callback<Response>() {
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
                Log.d("referandearnrespose", result);
                try {
                    jsonObject = new JSONObject(result);
                    st = jsonObject.getString("status");
                    refermsg = jsonObject.getString("referral_msg");
                    refercode = jsonObject.getString("referral_code");
                    frndearn = jsonObject.getString("friend_earn");
                    youearn = jsonObject.getString("you_earn");
                    txt_refercode.setText(refercode);
                    txt_frndearn.setText("\u20B9" + " " + frndearn);
                    txtyouearn.setText("\u20B9" + " " + youearn);


                } catch (Exception e) {
                    e.printStackTrace();
                }
                shareApp(getActivity());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("ERROR:", error.getMessage());

            }
        });

    }


    public void shareApp(Context context)
    {
        final String appPackageName = context.getPackageName();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out Water and Laundry App at: https://play.google.com/store/apps/details?id=" + appPackageName + refercode);
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, "Share with..."));
    }

}

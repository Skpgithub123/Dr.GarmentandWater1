package com.uohmac.com.drgarmentandwater.Activites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.uohmac.com.drgarmentandwater.LoginActivity;
import com.uohmac.com.drgarmentandwater.R;

/**
 * Created by uOhmac Technologies on 19-Jan-17.
 */
public class SplashScreen extends AppCompatActivity {


    SharedPreferences sp;

    SharedPreferences.Editor spt;
    public static final String MYPREFERNCES_LOGIN = "mysharedpref_waterlaundry";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        sp = getSharedPreferences(MYPREFERNCES_LOGIN, Context.MODE_PRIVATE);
        spt = sp.edit();


        Log.d("splashscreenvalure","==="+sp.getString("status","-1"));
      /*  new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this,
                        LoginActivity.class));
                finish();
            }
        }, 4000);*/


        Thread timethread = new Thread() {
            public void run() {
                try {
                    sleep(4000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (sp.getString("status","-1").equals("successfully")) {

                        Log.d("splashstatus", "sucess1");

                        Intent mainIntent = new Intent(SplashScreen.this, HomeActivity.class);
                        startActivity(mainIntent);
                        //checkPermission();


                    } else
                    {

                        Log.d("splashstatus", "sucess0");
                        Intent mainIntent = new Intent(SplashScreen.this, LoginActivity.class);
                        startActivity(mainIntent);

                    }

                }

            }
        };
        timethread.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}

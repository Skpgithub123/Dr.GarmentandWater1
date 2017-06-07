package com.uohmac.com.drgarmentandwater.Activites;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.uohmac.com.drgarmentandwater.Adpater.MyPagerAdapter;
import com.uohmac.com.drgarmentandwater.Api.UohmacAPI;
import com.uohmac.com.drgarmentandwater.Fragments.AboutUsFragment;
import com.uohmac.com.drgarmentandwater.Fragments.AddressFragment;

import com.uohmac.com.drgarmentandwater.Fragments.ChangePasswordFragment;
import com.uohmac.com.drgarmentandwater.Fragments.FAQFragment;
import com.uohmac.com.drgarmentandwater.Fragments.HomeFragment;
import com.uohmac.com.drgarmentandwater.Fragments.MonthlyPackFragment;
import com.uohmac.com.drgarmentandwater.Fragments.RecentordersFragment;
import com.uohmac.com.drgarmentandwater.Fragments.ReferandEarnFragment;
import com.uohmac.com.drgarmentandwater.Fragments.SignOutFragment;
import com.uohmac.com.drgarmentandwater.Fragments.WalletFragment;
import com.uohmac.com.drgarmentandwater.R;
import com.uohmac.com.drgarmentandwater.Utils.AppNetworkInfo;
import com.uohmac.com.drgarmentandwater.Utils.Constatns;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import android.os.Handler;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by SUNIL on 9/15/2016.
 */
public class HomeActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private DrawerLayout mDrawerLayout;

    Toolbar toolbar;
    CircleImageView img_profile;
    NavigationView navigationView;
    private ViewPager viewpager;
    TextView txtgetmailid,txtfbname,toolbarTitle;
    String fn,ln,fbemail,enteredmail,profileimage,FBID,getfbmob,deviceid,tm,responseforfb,title;
    public static String fullname;
    static String msg,authkey;
    SharedPreferences sp;
    private Context context;
    int result;
    private static int  REQUEST_READ_PHONE_STATE=1;
    JSONArray jsonArray;
    JSONObject jobj,jobject;
    TelephonyManager mTelephonyMgr;
    private boolean shouldLoadHomeFragOnBackPress = true;
    int count=0;


    public String[] toolbartitles;
    private static int navitemindex=0;


    //public static final String MYPREFERNCES_POSTFB = "mypreferpostfb";
    SharedPreferences.Editor editor ;

    public static final String MYPREFERNCES_LOGIN = "mysharedpref_waterlaundry";
   // public static final String MYPREFERNCES_FBLOGIN = "mypreferfblogin";
   ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.statusbar));

        }
         toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        String title = "";

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbartitles = getResources().getStringArray(R.array.nav_toolbar_titles);

        intiNavationDrawer();

       // sp = getSharedPreferences(MYPREFERNCES_POSTFB, 0);
        sp = getSharedPreferences(MYPREFERNCES_LOGIN, 0);
        editor = sp.edit();


       // enteredmail = sp.getString("email", "");
       // Log.e("getstoreenteredemail", enteredmail);

        //facebook login details
        fn = sp.getString("firstName", "");
        ln = sp.getString("lastName", "");
        fbemail = sp.getString("email", "");
        profileimage = sp.getString("profilePicUrl", "");
        FBID = sp.getString("facebookid", "");
        authkey = sp.getString("auth_key", "");
        Log.e("auth", authkey);

       // sp = getSharedPreferences(MYPREFERNCES_FBLOGIN, 0);
        getfbmob = sp.getString("mobile", "");
        Log.e("getstorefbfn", fn);
        Log.e("getstorefbln", ln);
        Log.e("getstorefbemail", fbemail);
        Log.e("getstorefbprofileurl", profileimage);
        Log.e("getstorefbid", FBID);
        Log.e("getstorefbmobile", getfbmob);

        fullname = fn+ln;


       // int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
/*
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        }*/

       // tm = Context.TELEPHONY_SERVICE;
       // mTelephonyMgr = (TelephonyManager) getSystemService(tm);
        deviceid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.e("deviceidforfb===", deviceid);





        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        TabLayout  tabLayout = (TabLayout)findViewById(R.id.tab_layout);



        tabLayout.addTab(tabLayout.newTab().setText(Constatns.LANDING_TAB_MY_WATER));
        tabLayout.addTab(tabLayout.newTab().setText(Constatns.LANDING_TAB_MY_LAUNDRY));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);



        viewpager = (ViewPager) findViewById(R.id.landing_pager);
        final MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());

        viewpager.setAdapter(adapter);
        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        /*Intent intent = this.getIntent();
        if(intent != null) {
            fn =  intent.getStringExtra("firstName");
            ln =  intent.getStringExtra("lastName");
            email = intent.getStringExtra("email");
            profileimage = intent.getStringExtra("profilePicUrl");


        }*/


        //enteredmail = (String)getIntent().getExtras().get("takeemail");
        //profileimage = (String)getIntent().getExtras().get("profilePicUrl");
//        Log.e("getimage", profileimage);


        View header = navigationView.getHeaderView(0);
        //img_setting = (ImageView)header.findViewById(R.id.imgsetting);
        img_profile = (CircleImageView)header.findViewById(R.id.ic_profile);
        txtgetmailid = (TextView)header.findViewById(R.id.tv_email);
        txtfbname = (TextView)header.findViewById(R.id.tv_fbname);

        if(!TextUtils.isEmpty(profileimage)) {
            Picasso.with(HomeActivity.this)
                    .load(profileimage)
                    .into(img_profile);
        }
            if(fbemail==null) {

                txtfbname.setVisibility(View.VISIBLE);
                txtgetmailid.setVisibility(View.GONE);
            }

            if(fullname==null) {
                txtfbname.setVisibility(View.GONE);
                txtgetmailid.setVisibility(View.VISIBLE);

            }

               txtgetmailid.setText(fbemail);
               txtfbname.setText(fullname);

//            Log.e("usermail", LoginActivity.email);

           // txtgetmailid.setText(fn+ln);


        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i_settings = new Intent(HomeActivity.this, EditProfile.class);
                startActivity(i_settings);

            }
        });


       /* img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i_settings = new Intent(HomeActivity.this, EditProfile.class);
                startActivity(i_settings);
                finish();
            }
        });*/


        if(AppNetworkInfo.isConnectingToInternet(HomeActivity.this)){
            PostFBDetails();
        } else{
            Toast.makeText(HomeActivity.this,"No network found... Please try again",Toast.LENGTH_SHORT).show();
        }

        //for screen animation
        overridePendingTransition(R.anim.lefttoright, R.anim.righttoleft);

    }


    private void PostFBDetails(){

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.show();
            final RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint(Constatns.UOHMAC_API)
                    .build();

            UohmacAPI api_fbdetails = adapter.create(UohmacAPI.class);

            api_fbdetails.FBRegisterDetails(fn, ln, fbemail, getfbmob, profileimage, FBID, deviceid, new Callback<Response>() {
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


                    responseforfb = sb.toString();
                    Log.e("getresponseloginfromfb", responseforfb);
                    try {
                        jsonArray = new JSONArray(responseforfb);
                        Log.e("jsonArray", "" + jsonArray);

                        jobj = jsonArray.optJSONObject(0);
                        msg = jobj.getString("msg");
                        Log.e("msg", msg);
                        authkey = jobj.getString("auth_key");
                        editor.putString("auth_key", authkey);
                        Log.e("entering postfb details", "hello postfbdetails");
                        editor.commit();
                        Log.e("authkey", authkey);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    mProgressDialog.dismiss();

                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(HomeActivity.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                    Log.d("ERROR:", error.getMessage());

                }
            });

        }



   /* public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString, Base64.DEFAULT);
            bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            Log.e("bitmap", ""+bitmap);
            img_profile.setImageBitmap(bitmap);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }*/



    public void intiNavationDrawer() {
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

      /* *//**//* final Spinner spinner = (Spinner) navigationView.getMenu().findItem(R.id.nav_spinnerselect).getActionView();
        final  Menu m;
        final String[] str = { "Water", };
        spinner.setAdapter(new ArrayAdapter<String>(HomeActivity.this,android.R.layout.simple_spinner_dropdown_item,str));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(HomeActivity.this,str[position],Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*//**//**/
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {


            @Override
            public boolean onNavigationItemSelected(MenuItem item) {



                int id = item.getItemId();


                /*if(id == R.id.nav_spinnerselect){
                    if (count == 0) {
                        navigationView.getMenu().findItem(R.id.nav_home).setVisible(true);
                       // navigationView.getMenu().findItem(R.id.nav_address).setVisible(true);
                        navigationView.getMenu().findItem(R.id.nav_recentorders).setVisible(true);
                        navigationView.getMenu().findItem(R.id.nav_mywallet).setVisible(true);
                        navigationView.getMenu().findItem(R.id.nav_faqhelp).setVisible(true);
                        navigationView.getMenu().findItem(R.id.nav_monthlypack).setVisible(true);
                        navigationView.getMenu().findItem(R.id.nav_changepassword).setVisible(true);
                        navigationView.getMenu().findItem(R.id.nav_referearn).setVisible(true);
                        navigationView.getMenu().findItem(R.id.nav_booknow).setVisible(true);
                        navigationView.getMenu().findItem(R.id.nav_monthlypackforlaundry).setVisible(true);
                       // navigationView.getMenu().findItem(R.id.nav_signout).setVisible(true);
                        count++;

                    }else{

                        navigationView.getMenu().findItem(R.id.nav_home).setVisible(false);
                       // navigationView.getMenu().findItem(R.id.nav_address).setVisible(false);
                        navigationView.getMenu().findItem(R.id.nav_mywallet).setVisible(false);
                        navigationView.getMenu().findItem(R.id.nav_recentorders).setVisible(false);
                        navigationView.getMenu().findItem(R.id.nav_faqhelp).setVisible(false);
                        navigationView.getMenu().findItem(R.id.nav_monthlypack).setVisible(false);
                        navigationView.getMenu().findItem(R.id.nav_changepassword).setVisible(false);
                        navigationView.getMenu().findItem(R.id.nav_referearn).setVisible(false);
                        navigationView.getMenu().findItem(R.id.nav_booknow).setVisible(false);
                        navigationView.getMenu().findItem(R.id.nav_monthlypackforlaundry).setVisible(false);
                        //navigationView.getMenu().findItem(R.id.nav_signout).setVisible(false);
                        count=0;
                    }
                } else*/ if (id == R.id.nav_home) {
                   title = getString(R.string.nav_item_home);
                   /*toolbarTitle.setText(title);
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    HomeFragment mProfileFragment = new HomeFragment();
                    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, mProfileFragment, "Home").addToBackStack(null).commitAllowingStateLoss();*/
                    new HomeActivity();
                    mDrawerLayout.closeDrawers();
                   invalidateOptionsMenu();

                } /*else if (id == R.id.nav_address) {

                   title = getString(R.string.nav_item_addresses);
                   toolbarTitle.setText(title);
                    AddressFragment mAddressFragment = new AddressFragment();
                    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, mAddressFragment, "Address").addToBackStack(null).commitAllowingStateLoss();
                    mDrawerLayout.closeDrawers();
                   invalidateOptionsMenu();


                } */else if(id == R.id.nav_mywallet){
                   /* title = getString(R.string.nav_item_mywallet);
                    toolbarTitle.setText(title);
                    WalletFragment mWalletFragment = new WalletFragment();
                    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, mWalletFragment, "Wallet").addToBackStack(null).commitAllowingStateLoss();
                    mDrawerLayout.closeDrawers();
                    invalidateOptionsMenu();*/
                    Intent i_wallet = new Intent(HomeActivity.this,WalletFragment.class);
                    startActivity(i_wallet);
                }
                else if(id == R.id.nav_recentorders){

                    Intent i_recentorders = new Intent(HomeActivity.this, RecentOrdersMain.class);
                    startActivity(i_recentorders);
                   /*title = getString(R.string.nav_item_recentorders);
                   toolbarTitle.setText(title);
                    RecentordersFragment mRecentordersFragment = new RecentordersFragment();
                    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, mRecentordersFragment, "Recent Orders").addToBackStack(null).commitAllowingStateLoss();
                    mDrawerLayout.closeDrawers();
                   invalidateOptionsMenu();*/

                } else if(id == R.id.nav_faqhelp){

                   title = getString(R.string.nav_item_FAQ);
                   toolbarTitle.setText(title);
                    FAQFragment mFaqFragment = new FAQFragment();
                    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, mFaqFragment, "FAQ").addToBackStack(null).commitAllowingStateLoss();
                    mDrawerLayout.closeDrawers();
                   invalidateOptionsMenu();

                } /*else if(id == R.id.nav_monthlypack){

                   title = getString(R.string.nav_item_monthlypack);
                   toolbarTitle.setText(title);
                   MonthlyPackFragment mMonthlypackFragment = new MonthlyPackFragment();
                   android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                   fragmentManager.beginTransaction().replace(R.id.container, mMonthlypackFragment, "Monthly Pack").addToBackStack(null).commitAllowingStateLoss();
                   mDrawerLayout.closeDrawers();
                   invalidateOptionsMenu();

               }*/else if(id == R.id.nav_changepassword){

                   title = getString(R.string.nav_item_changepassword);
                   toolbarTitle.setText(title);
                   ChangePasswordFragment mChangepassword = new ChangePasswordFragment();
                   android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                   fragmentManager.beginTransaction().replace(R.id.container, mChangepassword, "Change Password").addToBackStack(null).commitAllowingStateLoss();
                   mDrawerLayout.closeDrawers();
                   invalidateOptionsMenu();

               }else if(id == R.id.nav_referearn){

                   title = getString(R.string.nav_referandearntitle);
                   toolbarTitle.setText(title);
                   ReferandEarnFragment mReferandEarn = new ReferandEarnFragment();
                   android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                   fragmentManager.beginTransaction().replace(R.id.container, mReferandEarn, "Refer and Earn").addToBackStack(null).commitAllowingStateLoss();
                   mDrawerLayout.closeDrawers();
                   invalidateOptionsMenu();

               }else if(id == R.id.nav_about_us){
                    title = getString(R.string.nav_item_aboutus);
                    toolbarTitle.setText(title);
                    AboutUsFragment mAboutusFragment = new AboutUsFragment();
                    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, mAboutusFragment, "About US").addToBackStack(null).commitAllowingStateLoss();
                    mDrawerLayout.closeDrawers();
                    invalidateOptionsMenu();
                }


                else if(id == R.id.nav_signout){
                    SignOutFragment mSignout = new SignOutFragment();
                    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, mSignout, "Signout").addToBackStack(null).commitAllowingStateLoss();
                    mDrawerLayout.closeDrawers();
                    invalidateOptionsMenu();
                }


               /* if(id == R.id.nav_spinnerselect1){
                    if (count == 0) {
                        navigationView.getMenu().findItem(R.id.nav_home).setVisible(false);
                       // navigationView.getMenu().findItem(R.id.nav_address).setVisible(false);
                        navigationView.getMenu().findItem(R.id.nav_mywallet).setVisible(false);
                        navigationView.getMenu().findItem(R.id.nav_recentorders).setVisible(false);
                        navigationView.getMenu().findItem(R.id.nav_faqhelp).setVisible(false);
                        navigationView.getMenu().findItem(R.id.nav_monthlypack).setVisible(false);
                        navigationView.getMenu().findItem(R.id.nav_changepassword).setVisible(false);
                        navigationView.getMenu().findItem(R.id.nav_referearn).setVisible(false);
                        navigationView.getMenu().findItem(R.id.nav_booknow).setVisible(true);
                        navigationView.getMenu().findItem(R.id.nav_monthlypackforlaundry).setVisible(true);
                       // navigationView.getMenu().findItem(R.id.nav_signout).setVisible(true);
                        count++;
                    }else{

                        navigationView.getMenu().findItem(R.id.nav_booknow).setVisible(false);
                        navigationView.getMenu().findItem(R.id.nav_monthlypackforlaundry).setVisible(false);
                       // navigationView.getMenu().findItem(R.id.nav_signout).setVisible(false);
                        count=0;
                    }
                }*/
                return true;
            }

        });
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


    }



    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
            return;
        }
        title = getString(R.string.nav_home);
        toolbarTitle.setText(title);
        new HomeActivity();
        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        /*if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            new HomeActivity();
            mDrawerLayout.closeDrawers();
            invalidateOptionsMenu();
        }*/

        super.onBackPressed();
    }



}

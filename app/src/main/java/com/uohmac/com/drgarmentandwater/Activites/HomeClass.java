/*
package com.uohmac.com.drgarmentandwater.Activites;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;
import com.uohmac.com.drgarmentandwater.Adpater.MyPagerAdapter;
import com.uohmac.com.drgarmentandwater.Api.UohmacAPI;
import com.uohmac.com.drgarmentandwater.Fragments.AddressFragment;
import com.uohmac.com.drgarmentandwater.Fragments.ChangePasswordFragment;
import com.uohmac.com.drgarmentandwater.Fragments.FAQFragment;
import com.uohmac.com.drgarmentandwater.Fragments.HomeFragment;
import com.uohmac.com.drgarmentandwater.Fragments.MonthlyPackFragment;
import com.uohmac.com.drgarmentandwater.Fragments.RecentordersFragment;
import com.uohmac.com.drgarmentandwater.Fragments.ReferandEarnFragment;
import com.uohmac.com.drgarmentandwater.Fragments.WalletFragment;
import com.uohmac.com.drgarmentandwater.R;
import com.uohmac.com.drgarmentandwater.Utils.AppNetworkInfo;
import com.uohmac.com.drgarmentandwater.Utils.Constatns;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

*/
/**
 * Created by uOhmac Technologies on 20-Feb-17.
 *//*

public class HomeClass extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    SharedPreferences sp;
    private ViewPager viewpager;
    JSONArray jsonArray;
    JSONObject jobj,jobject;
    // index to identify current nav menu item
    public static int navItemIndex = 0;
    int count=0;
    String fn,ln,fbemail,enteredmail,profileimage,FBID,getfbmob,deviceid,tm,responseforfb,title,fullname,authkey,msg;
    SharedPreferences.Editor editor ;
    public static final String MYPREFERNCES_LOGIN = "mysharedpref_waterlaundry";

    private static final String urlNavHeaderBg = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";
    private static final String urlProfileImg = "https://lh3.googleusercontent.com/eCtE_G34M9ygdkmOpYvCag1vBARCmZwnVS6rS5t4JLzJ6QgQSBquM0nuTsCpLhYbKljoyS-txg";

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_ADDRESS = "Address";
    private static final String TAG_RECENTORDERS = "Recent Orders";
    private static final String TAG_WALLET = "My Wallet";
    private static final String TAG_FAQ = "faq";
    private static final String TAG_MONTHLYPACK = "Monthly Pack";
    private static final String TAG_CHANGEPASSWORD = "change password";
    private static final String TAG_REFEREARN = "Refer and earn";
    private static final String TAG_BOOKNOW = "Book Now";
    private static final String TAG_MONTHLYPACKFORLAUNDRY = "Monthly Pack";
    private static final String TAG_SIGNOUT= "Sign Out";
    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;
    CircleImageView img_profile;
    TextView txtgetmailid, txtfbname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mHandler = new Handler();
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        sp = getSharedPreferences(MYPREFERNCES_LOGIN, 0);
        editor = sp.edit();

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        img_profile = (CircleImageView) navHeader.findViewById(R.id.ic_profile);
        txtgetmailid = (TextView) navHeader.findViewById(R.id.tv_email);
        txtfbname = (TextView) navHeader.findViewById(R.id.tv_fbname);


        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_toolbar_titles);


        //imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        //imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        fn = sp.getString("firstName", "");
        ln = sp.getString("lastName", "");
        fbemail = sp.getString("email", "");
        profileimage = sp.getString("profilePicUrl", "");
        FBID = sp.getString("facebookid", "");


        getfbmob = sp.getString("mobile", "");
        Log.e("getstorefbfn", fn);
        Log.e("getstorefbln", ln);
        Log.e("getstorefbemail", fbemail);
        Log.e("getstorefbprofileurl", profileimage);
        Log.e("getstorefbid", FBID);
        Log.e("getstorefbmobile", getfbmob);

        fullname = fn+ln;


        deviceid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.e("deviceidforfb===", deviceid);
        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }


        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_layout);



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





        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i_settings = new Intent(HomeClass.this, EditProfile.class);
                startActivity(i_settings);
                finish();
            }
        });



        if(AppNetworkInfo.isConnectingToInternet(HomeClass.this)){
            PostFBDetails();
        } else{
            AlertDialog.Builder ab = new AlertDialog.Builder(HomeClass.this);
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
                    PostFBDetails();

                }
            });
            ab.create();
            ab.show();
        }

    }


    private void PostFBDetails(){
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

            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("ERROR:", error.getMessage());

            }
        });

    }



    private void loadNavHeader() {
        // name, website
        if(!TextUtils.isEmpty(profileimage)) {
            Picasso.with(HomeClass.this)
                    .load(profileimage)
                    .into(img_profile);
        }
        if(enteredmail==null) {

            txtfbname.setVisibility(View.VISIBLE);
            txtgetmailid.setVisibility(View.GONE);
        }

        if(fullname==null) {
            txtfbname.setVisibility(View.GONE);
            txtgetmailid.setVisibility(View.VISIBLE);

        }

        txtgetmailid.setText(enteredmail);
        txtfbname.setText(fullname);

    }


    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();


            return;
        }

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.container, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }


        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }


    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                new HomeClass();
            case 1:

                new HomeClass();

            case 2:
                RecentordersFragment recentordersFragment = new RecentordersFragment();
                return recentordersFragment;
            case 3:
                // notifications fragment
                AddressFragment addressFragment = new AddressFragment();
                return addressFragment;

            case 4:
                Intent i_wallet = new Intent(HomeClass.this,WalletFragment.class);
                startActivity(i_wallet);
            case 5:
               MonthlyPackFragment monthlyPackFragment = new MonthlyPackFragment();
                return monthlyPackFragment;
            case 6:
                FAQFragment faqFragment = new FAQFragment();
                return faqFragment;
            default:
                return new HomeFragment();
        }
    }


    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    */
/*case R.id.nav_spinnerselect:
                        if (count == 0) {
                            navigationView.getMenu().findItem(R.id.nav_home).setVisible(true);
                            //navigationView.getMenu().findItem(R.id.nav_address).setVisible(true);
                            navigationView.getMenu().findItem(R.id.nav_recentorders).setVisible(true);
                            navigationView.getMenu().findItem(R.id.nav_mywallet).setVisible(true);
                            navigationView.getMenu().findItem(R.id.nav_faqhelp).setVisible(true);
                            navigationView.getMenu().findItem(R.id.nav_monthlypack).setVisible(true);
                            navigationView.getMenu().findItem(R.id.nav_changepassword).setVisible(true);
                            navigationView.getMenu().findItem(R.id.nav_referearn).setVisible(true);
                            navigationView.getMenu().findItem(R.id.nav_booknow).setVisible(true);
                            navigationView.getMenu().findItem(R.id.nav_monthlypackforlaundry).setVisible(true);
                            navigationView.getMenu().findItem(R.id.nav_signout).setVisible(true);
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
                            navigationView.getMenu().findItem(R.id.nav_signout).setVisible(false);
                            count=0;
                        }*//*

                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                   */
/* case R.id.nav_address:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_ADDRESS;
                        break;*//*

                    case R.id.nav_mywallet:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_WALLET;
                        break;
                    case R.id.nav_recentorders:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_RECENTORDERS;
                        break;
                    case R.id.nav_faqhelp:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_FAQ;
                        break;
                    case R.id.nav_monthlypack:
                        // launch new intent instead of loading fragment
                        */
/*startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                        drawer.closeDrawers();*//*

                        navItemIndex = 5;
                        CURRENT_TAG = TAG_MONTHLYPACK;
                        break;
                    case R.id.nav_changepassword:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_CHANGEPASSWORD;
                        break;
                    case R.id.nav_referearn:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_REFEREARN;
                        break;
                    */
/*case R.id.nav_spinnerselect1:
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
                            navigationView.getMenu().findItem(R.id.nav_signout).setVisible(true);
                            count++;
                        }else{

                            navigationView.getMenu().findItem(R.id.nav_booknow).setVisible(false);
                            navigationView.getMenu().findItem(R.id.nav_monthlypackforlaundry).setVisible(false);
                            navigationView.getMenu().findItem(R.id.nav_signout).setVisible(false);
                            count=0;
                        }*//*

                        return true;

                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });



        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }

        // when fragment is notifications, load the menu created for notifications

        return true;
    }
}
*/

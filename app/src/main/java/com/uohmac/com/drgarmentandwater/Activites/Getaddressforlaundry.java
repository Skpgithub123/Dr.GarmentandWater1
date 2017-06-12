package com.uohmac.com.drgarmentandwater.Activites;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.uohmac.com.drgarmentandwater.Api.UohmacAPI;
import com.uohmac.com.drgarmentandwater.Pojos.Timeslotspojo;
import com.uohmac.com.drgarmentandwater.R;
import com.uohmac.com.drgarmentandwater.Utils.AppNetworkInfo;
import com.uohmac.com.drgarmentandwater.Utils.Constatns;
import com.uohmac.com.drgarmentandwater.Utils.GlobalUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by System-03 on 11/4/2016.
 */
public class Getaddressforlaundry extends AppCompatActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    LocationManager lm;
    public static double latitude,longitude;
    Location L;
     String provider,flatno,address,city,state,country,subarea,sublocality,landmark,postalCode,getauthkey,responseforlaundryaddr;
    static  String devaddress;
    EditText etflatno,etaddress,etlandmark,etcity,etpincode,etaddresstype,etcountry,etfullname,etmobile;
    Button btnsave;
    Geocoder geocoder;
    List<Address> addresses = null;
    MapFragment mapFragment;
    GPSTracker gps;
    RelativeLayout relativeLayout;
    GoogleMap mMap;
    LatLng latlng,ltlg;
    MarkerOptions marker;
    Toolbar toolbar;
    ArrayAdapter<String> time_slots;
    List<Timeslotspojo> timeslotspojoslist;
    String[] items;
    String dateString,sp_selecteditem,responsefororderdetails,msg_respostorderdetails,check_addnewaddress,check_editaddress,name_foraddress,mobile;
    String geting_orderid,response_checkdelivery;
    Spinner sptimeslots;
    TextView tvdate;
    SharedPreferences sp;
    CheckBox cb_ischecked;
    View focusView = null;
    JSONObject jsonObject;
    JSONArray jsonArray;
    boolean cancel = false;
    TextView tvaddnewaddress,tvaddcurrentaddress,tvcancel;
    ProgressDialog mProgressDialog;
    //public static final String MYPREFERNCES_REGISTER = "mypreferregister";
    //public static final String MYPREFERNCES_POSTFB = "mypreferpostfb";
    GoogleApiClient mGoogleApiClient_For_Location;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    SharedPreferences.Editor sp_editaddrlaundry;
    public static final String MYPREFERNCES_LOGIN = "mysharedpref_waterlaundry";

    public static final String PREFERNCES_ADDRESS = "save_laundryaddress";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getaddressforlaundry);

        tvdate = (TextView) findViewById(R.id.txt_selectdate);
        sptimeslots = (Spinner) findViewById(R.id.sp_timeslots);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        //tvaddnewaddress = (TextView)findViewById(R.id.txt_addnewaddress);
        tvaddcurrentaddress = (TextView)findViewById(R.id.txt_addcurrentaddress);
        cb_ischecked = (CheckBox)findViewById(R.id.cb_setaddrdefaultlaundry);
        //tvcancel = (TextView) findViewById(R.id.txt_cancel);
        geocoder = new Geocoder(this, Locale.getDefault());
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        gps = new GPSTracker(Getaddressforlaundry.this);




        sp = getSharedPreferences(MYPREFERNCES_LOGIN, 0);
        getauthkey = sp.getString("auth_key", "");
        Log.e("gettingauthkey_forgetaddresslaundry", getauthkey);

        name_foraddress = sp.getString("firstName", "")+ sp.getString("lastName", "");
        Log.e("fullname_forlaudnry", name_foraddress);
        mobile = sp.getString("mobile", "");
        Log.e("mobile_addres", mobile);

       /* //getting authkey from sp
        sp = getSharedPreferences(MYPREFERNCES_POSTFB, 0);
        getauthkey = sp.getString("auth_key", "");
        Log.e("gettingauthkeyforsettings", getauthkey);*/

        sp = getSharedPreferences(PREFERNCES_ADDRESS, 0);
        sp_editaddrlaundry = sp.edit();


        /*sp = getSharedPreferences(MYPREFERNCES_REGISTER, 0);
        getauthkey = sp.getString("authkeyforreg","");*/



        try {
            // Loading map
            intilizemap();

        } catch (Exception e) {
            e.printStackTrace();
        }

        etflatno = (EditText)findViewById(R.id.etflatno);
        etaddress = (EditText)findViewById(R.id.etaddress);
        etlandmark = (EditText)findViewById(R.id.etlandmark);
        etcity = (EditText)findViewById(R.id.etcity);
        etpincode = (EditText)findViewById(R.id.etpincode);
        etaddresstype = (EditText)findViewById(R.id.etaddresstype);
        etcountry = (EditText)findViewById(R.id.etcountry);
        etfullname = (EditText)findViewById(R.id.et_fullname_laundry);
        etmobile = (EditText)findViewById(R.id.et_mobile_laundry);
        btnsave = (Button)findViewById(R.id.btn_save);
        relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayoutfornet);

        if(!TextUtils.isEmpty(flatno)&&!TextUtils.isEmpty(address)&&!TextUtils.isEmpty(city)&&!TextUtils.isEmpty(landmark)&&!TextUtils.isEmpty(country)&&!TextUtils.isEmpty(postalCode)) {
            flatno = (String) getIntent().getExtras().get("editflatno");
            address = (String) getIntent().getExtras().get("editaddress");
            city = (String) getIntent().getExtras().get("editecity");
            landmark = (String) getIntent().getExtras().get("editlandmark");
            postalCode = (String) getIntent().getExtras().get("editpincode");
            country = (String) getIntent().getExtras().get("editcountry");
        }
        //check_addnewaddress = (String)getIntent().getExtras().get("addnewaddress");
        //check_editaddress = (String)getIntent().getExtras().get("editaddress");

       // Log.d("checkeditaddress","******    "+check_editaddress);


        etfullname.setText(name_foraddress);
        etmobile.setText(mobile);


        if(!TextUtils.isEmpty(check_addnewaddress)) {
                if (check_addnewaddress.equals("Add")) {

                    etflatno.setText("");
                    etaddress.setText("");
                    etcity.setText("");
                    etcountry.setText("");
                    etlandmark.setText("");
                    etpincode.setText("");
                }
            }

        if(!TextUtils.isEmpty(flatno)&&!TextUtils.isEmpty(address)&&!TextUtils.isEmpty(city)&&!TextUtils.isEmpty(landmark)&&!TextUtils.isEmpty(country)&&!TextUtils.isEmpty(postalCode)) {
            etflatno.setText(flatno);
            etaddress.setText(address);
            etcity.setText(city);
            etlandmark.setText(landmark);
            etcountry.setText(country);
            etpincode.setText(postalCode);
        }




            /* if(!TextUtils.isEmpty(check_editaddress)) {

            if (check_editaddress.equals("Edit")) {

                if (sp.getString("flatno_laundry",null)!=null && sp.getString("addr1_laundry",null)!=null && sp.getString("city_laundry",null)!=null
                          &&sp.getString("country_laundry",null)!=null && sp.getString("landmark_laundry",null)!=null && sp.getString("pincode_laundry",null)!=null) {
                    etflatno.setText(sp.getString("flatno_laundry", null));
                    etaddress.setText(sp.getString("addr1_laundry", null));
                    etcity.setText(sp.getString("city_laundry", null));
                    etcountry.setText(sp.getString("country_laundry", null));
                    etlandmark.setText(sp.getString("landmark_laundry", null));
                    etpincode.setText(sp.getString("pincode_laundry", null));
                }
                else
                {
                    etflatno.setText("");
                    etaddress.setText("");
                    etcity.setText("");
                    etcountry.setText("");
                    etlandmark.setText("");
                    etpincode.setText("");
                }

            }
        }*/





        /*etflatno.setText(SelectingDetails.tookflatno);
        etaddress.setText(SelectingDetails.tookaddress);
        etcity.setText(SelectingDetails.tookcity);
        etcountry.setText(SelectingDetails.tookcountry);
        etlandmark.setText(SelectingDetails.tooklandmark);
        etpincode.setText(SelectingDetails.tookpincode);*/






        if(AppNetworkInfo.isConnectingToInternet(Getaddressforlaundry.this)) {
            Criteria c = new Criteria();

            provider = lm.getBestProvider(c, true);
            L = lm.getLastKnownLocation(provider);

            if(ContextCompat.checkSelfPermission(Getaddressforlaundry.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(Getaddressforlaundry.this, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED ) {

                if (gps.canGetLocation()) {
                    /*lng = L.getLongitude();
                    lat = L.getLatitude();
                    String strlng = formatter.format(lng);
                    String strlat = formatter.format(lat);
                    latitude = Double.parseDouble(strlat);
                    Log.e("latvalue", "" + latitude);
                    longitude = Double.parseDouble(strlng);
                    Log.e("longvalue", "" + longitude);

                    txtlat.setText(String.valueOf(latitude));
                    txtlong.setText(String.valueOf(longitude));*/
                     latitude = gps.getLatitude();
                     longitude = gps.getLongitude();

                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    // txtlat.setText(String.valueOf(latitude));
                    //txtlong.setText(String.valueOf(longitude));


                    try {
                        addresses = geocoder.getFromLocation(latitude, longitude, 1);

                        if (addresses != null && addresses.size() > 0) {
                            flatno = addresses.get(0).getAddressLine(0);
                            address = addresses.get(0).getPremises();
                            city = addresses.get(0).getLocality();
                            state = addresses.get(0).getAdminArea();
                            country = addresses.get(0).getCountryName();
                            postalCode = addresses.get(0).getPostalCode();
                            subarea = addresses.get(0).getSubAdminArea();
                            landmark = addresses.get(0).getSubLocality();
                            Log.e("getlcoation", address + " " + city + " " + state + " " + country + " " + postalCode + " " + subarea + " " + sublocality);

                            tvaddcurrentaddress.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    etflatno.setText(flatno);
                                    etaddress.setText(address);
                                    etcity.setText(city);
                                    etcountry.setText(country);
                                    etlandmark.setText(landmark);
                                    etpincode.setText(postalCode);
                                }
                            });


                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } else
                    settingsrequest();
                    //gps.showSettingsAlert();



            }
        }else{
            //Toast.makeText(GettingAddress.this, "Network error.Check your network connections and try again.", Toast.LENGTH_LONG).show();
            GlobalUtils.showSnackBar(relativeLayout, "Network error.Check your network connections and try again.");

        }

        //devaddress = flatno + "" + city + "" + landmark + "" + postalCode;

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //SelectingDetails.tvaddressview.setText(devaddress);
                if(Validate()) {
                    if(AppNetworkInfo.isConnectingToInternet(Getaddressforlaundry.this)){

                            if (tvdate.getText().toString().equals("SELECT DATE")) {
                                tvdate.setError("Please Select Pick Up date");
                            } else {
                               // Postorderdetails();
                               checkfordelivery();

                            }
                        }else{
                        AlertDialog.Builder ab = new AlertDialog.Builder(Getaddressforlaundry.this);
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
                                if (tvdate.getText().toString().equals("SELECT DATE")) {
                                    tvdate.setError("Please Select Pick Up date");
                                } else {
                                   // Postorderdetails();

                                    Intent i_save = new Intent(Getaddressforlaundry.this, SelectingDetails.class);
                                    sp_editaddrlaundry.putString("flatno_laundry", etflatno.getText().toString());
                                    sp_editaddrlaundry.putString("addr1_laundry", etaddress.getText().toString());
                                    sp_editaddrlaundry.putString("city_laundry", etcity.getText().toString());
                                    sp_editaddrlaundry.putString("landmark_laundry", etlandmark.getText().toString());
                                    sp_editaddrlaundry.putString("pincode_laundry", etpincode.getText().toString());
                                    sp_editaddrlaundry.putString("country_laundry", etcountry.getText().toString());
                                    sp_editaddrlaundry.putString("savetotalprice", OrderedItemsDetailsForLaundry.totalprice);
                                    sp_editaddrlaundry.putString("savedate", dateString);
                                    sp_editaddrlaundry.putString("savespinnerselecteditem", sp_selecteditem);
                                    sp_editaddrlaundry.putString("save_addressforlaundry", "savedaddressforlaundry");
                                    sp_editaddrlaundry.commit();
                                    startActivity(i_save);

                                }

                            }
                        });
                        ab.create();
                        ab.show();
                    }
                    }

               // finish();
            }
        });


        tvdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();

            }
        });


        /*tvcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i_cancel = new Intent(Getaddressforlaundry.this, OrderedItemsDetailsForLaundry.class);
                startActivity(i_cancel);
                finish();
            }
        });*/

        if(AppNetworkInfo.isConnectingToInternet(Getaddressforlaundry.this)){
            //calling timeslot api
            gettimeslots();
        } else{
            AlertDialog.Builder ab = new AlertDialog.Builder(Getaddressforlaundry.this);
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
                    gettimeslots();

                }
            });
            ab.create();
            ab.show();
        }


       /* tvaddnewaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Getaddressforlaundry.this, OrderedItemsDetailsForLaundry.class);
                startActivity(i);
            }
        });*/


        cb_ischecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sp_editaddrlaundry.clear();
                    sp_editaddrlaundry.commit();
                    sp_editaddrlaundry.putString("flatno_laundry", etflatno.getText().toString());
                    sp_editaddrlaundry.putString("addr1_laundry", etaddress.getText().toString());
                    sp_editaddrlaundry.putString("city_laundry", etcity.getText().toString());
                    sp_editaddrlaundry.putString("landmark_laundry", etlandmark.getText().toString());
                    sp_editaddrlaundry.putString("pincode_laundry", etpincode.getText().toString());
                    sp_editaddrlaundry.putString("country_laundry", etcountry.getText().toString());
                    sp_editaddrlaundry.putString("savetotalprice", OrderedItemsDetailsForLaundry.totalprice);
                    sp_editaddrlaundry.putString("savedate", dateString);
                    sp_editaddrlaundry.putString("savespinnerselecteditem", sp_selecteditem);
                    sp_editaddrlaundry.putString("save_addressforlaundry", "savedaddressforlaundry");
                    sp_editaddrlaundry.commit();
                }else
                {
                    sp_editaddrlaundry.clear();
                    sp_editaddrlaundry.commit();

                }
            }
        });


    }



    /* gowtham code to switch on gps*/

    public void settingsrequest()
    {

        if (mGoogleApiClient_For_Location == null) {
            mGoogleApiClient_For_Location = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            mGoogleApiClient_For_Location.connect();
        }
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient_For_Location, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();

                Log.d("stateandstatus", "statuss    " + status + "  " + state);


                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        // mGoogleApiClient.connect();
                        // buildGoogleApiClient();
                        Log.d("checkingcoont", "callmethods");


                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(Getaddressforlaundry.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
// Check for the integer request code originally supplied to startResolutionForResult().


            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // startLocationUpdates();

                        Log.d("requestcodeand", "resultcodes    " + requestCode + "  *  "+resultCode + "  *  "+data);


                        new Getaddressforlaundry();
                       // mGoogleApiClient_For_Location.connect();
                        //buildGoogleApiClient();
                        // mapFragment.getMapAsync(this);

                        Log.d("checkingcoont", "RESULT_OK--secondone");

                        break;
                    case Activity.RESULT_CANCELED:

                        Log.d("cancelrequestcodeand","canceled    "+requestCode +"  *  "+resultCode + "  *  "+data);

                        settingsrequest();//keep asking if imp or do whatever
                        break;
                }
                break;
        }
    }


    public void openDatePicker() {
        //clearing error if any
        tvdate.setError(null);
        final Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(Getaddressforlaundry.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.US);
                dateString = sdf.format(newDate.getTime());
                if (newCalendar.getTimeInMillis() < newDate.getTimeInMillis()) {
                    tvdate.setText(dateString);
                    tvdate.setTag(null);
                } else {
                    Toast.makeText(Getaddressforlaundry.this, getString(R.string.feature_date_selected), Toast.LENGTH_SHORT).show();
                    tvdate.setTag("false");
                    tvdate.setError(getString(R.string.feature_date_selected));
                }

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(newCalendar.getTimeInMillis());
        datePickerDialog.show();


    }



    private void gettimeslots(){
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_timeslots = adapter.create(UohmacAPI.class);

        api_timeslots.GetTimeSlots(new Callback<List<Timeslotspojo>>() {
            @Override
            public void success(List<Timeslotspojo> timeslotspojo, Response response) {
                timeslotspojoslist = timeslotspojo;
                Timeslots();

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(Getaddressforlaundry.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.e("Errorfound", "ERROR:" + error.getMessage(), error);

            }
        });

    }


    private void Timeslots(){
        items = new String[timeslotspojoslist.size()];

        for( int i=0; i<timeslotspojoslist.size();i++){
            items[i] = timeslotspojoslist.get(i).getTiming();
        }
        time_slots = new ArrayAdapter<String>(Getaddressforlaundry.this,android.R.layout.simple_list_item_1,items);
        sptimeslots.setAdapter(time_slots);

        sptimeslots.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sp_selecteditem = sptimeslots.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    //validation to enter all the fileds
    private boolean Validate(){

        flatno = etflatno.getText().toString();
        address = etaddress.getText().toString();
        city = etcity.getText().toString();
        landmark = etlandmark.getText().toString();
        postalCode = etpincode.getText().toString();

        boolean isValid = true;

            if(TextUtils.isEmpty(flatno)){
                isValid = false;
                etflatno.setError(getString(R.string.error_field_required));
                etflatno.requestFocus();

            }
            if(TextUtils.isEmpty(address)){
                isValid = false;
                etaddress.setError(getString(R.string.error_field_required));
                etaddress.requestFocus();

            }
            if(TextUtils.isEmpty(city)){
                isValid = false;
                etcity.setError(getString(R.string.error_field_required));
                etcity.requestFocus();

            }
            if(TextUtils.isEmpty(landmark)){
                isValid = false;
                etlandmark.setError(getString(R.string.error_field_required));
                etlandmark.requestFocus();

            }
            if(TextUtils.isEmpty(postalCode)){
                isValid = false;
                etpincode.setError(getString(R.string.error_field_required));
                etpincode.requestFocus();

            }


        /*if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            return false;
        }*/

        return isValid;
    }





   /* private void Postorderdetails(){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.show();
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_ordedetails = adapter.create(UohmacAPI.class);

        api_ordedetails.PostDetailsfororder(OrderedItemsDetailsForLaundry.totalprice, dateString, sp_selecteditem, getauthkey, "2", new Callback<Response>() {
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

                responsefororderdetails = sb.toString();
                Log.e("getresfororderdetails", responsefororderdetails);


                try {
                    jsonArray = new JSONArray(responsefororderdetails);
                    // jsonObject = new JSONObject(responsefororderdetails);
                    jsonObject = jsonArray.optJSONObject(0);
                    msg_respostorderdetails = jsonObject.getString("msg");
                    geting_orderid = jsonObject.getString("order_id");
                    Log.e("getstring1112", msg_respostorderdetails);
                    Log.e("getstring222", geting_orderid);
                    if (msg_respostorderdetails.equals("add Successfully.")) {

                        Toast.makeText(Getaddressforlaundry.this, msg_respostorderdetails, Toast.LENGTH_SHORT).show();

                        sp_editaddrlaundry.clear();
                        sp_editaddrlaundry.commit();
                        sp_editaddrlaundry.putString("flatno_laundry", etflatno.getText().toString());
                        sp_editaddrlaundry.putString("addr1_laundry", etaddress.getText().toString());
                        sp_editaddrlaundry.putString("city_laundry", etcity.getText().toString());
                        sp_editaddrlaundry.putString("landmark_laundry", etlandmark.getText().toString());
                        sp_editaddrlaundry.putString("pincode_laundry", etpincode.getText().toString());
                        sp_editaddrlaundry.putString("country_laundry", etcountry.getText().toString());
                        sp_editaddrlaundry.putString("save_addressforlaundry", "savedaddressforlaundry");
                        sp_editaddrlaundry.commit();

                        mProgressDialog.dismiss();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                mProgressDialog.dismiss();

            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("ERROR:", error.getMessage());

            }
        });
    }*/




    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    //mouli method to get latlng
    public class GPSTracker extends Service implements LocationListener {



        private  Context mContext;

        // flag for GPS status
        boolean isGPSEnabled = false;

        // flag for network status
        boolean isNetworkEnabled = false;

        // flag for GPS status
        boolean canGetLocation = false;

        Location location; // location
        double latitude; // latitude
        double longitude; // longitude


        // The minimum distance to change Updates in meters
        private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

        // The minimum time between updates in milliseconds
        private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

        // Declaring a Location Manager
        protected LocationManager locationManager;

        public GPSTracker(Context context) {
            this.mContext = context;
            getLocation();
        }

        public Location getLocation(){
            try{

                locationManager = (LocationManager) mContext
                        .getSystemService(LOCATION_SERVICE);

                // getting GPS status
                isGPSEnabled = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);

                // getting network status
                isNetworkEnabled = locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                if(!isGPSEnabled && !isNetworkEnabled){

                }else {
                    this.canGetLocation = true;
                    // First get location from Network Provider
                    if (isNetworkEnabled) {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("Network", "Network");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }

                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return location;

        }

        public void stopUsingGPS(){
            if(locationManager != null){
                try {
                    locationManager.removeUpdates(GPSTracker.this);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        }





        @Override
        public void onLocationChanged(Location location) {
            if (gps.canGetLocation()) {
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();

//                 Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

            }else{
                gps.showSettingsAlert();
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        /**
         * Function to check GPS/wifi enabled
         * @return boolean
         * */
        public boolean canGetLocation() {
            return this.canGetLocation;
        }

        /**
         * Function to show settings alert dialog
         * On pressing Settings button will lauch Settings Options
         * */
        public void showSettingsAlert(){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

            // Setting Dialog Title
            alertDialog.setTitle("GPS is settings");

            // Setting Dialog Message
            alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

            // On pressing Settings button
            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mContext.startActivity(intent);
                }
            });

            // on pressing cancel button
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            // Showing Alert Message
            alertDialog.show();
        }

        /**
         * Function to get latitude
         * */
        public double getLatitude(){
            if(location != null){
                latitude = location.getLatitude();
            }

            // return latitude
            return latitude;
        }

        /**
         * Function to get longitude
         * */
        public double getLongitude(){
            if(location != null){
                longitude = location.getLongitude();
            }

            // return longitude
            return longitude;
        }
    }



    private void intilizemap() {
        if (mMap == null) {
            mapFragment = ((MapFragment) getFragmentManager().findFragmentById(R.id.fragment1));
            mapFragment.getMapAsync(this);

            if(gps.canGetLocation()){
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
            }else{
               // gps.showSettingsAlert();
                settingsrequest();
            }



        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng latlngforlocation = new LatLng(latitude,longitude);
        mMap.addMarker(new MarkerOptions().position(latlngforlocation).title(landmark).snippet(flatno + city + postalCode));
        CameraPosition yourlocation = new CameraPosition.Builder().target(latlngforlocation).zoom(18).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(yourlocation));


        googleMap.setInfoWindowAdapter(new com.google.android.gms.maps.GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(Getaddressforlaundry.this);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(Getaddressforlaundry.this);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(Getaddressforlaundry.this);
                snippet.setTextColor(Color.BLACK);
                title.setTypeface(null, Typeface.BOLD);
                snippet.setText(marker.getSnippet());


                info.addView(title);
                info.addView(snippet);

                return info;

            }
        });
    }


    private void UpdateAddressforlaundry(){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.show();
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_updateaddrforwater = adapter.create(UohmacAPI.class);

        api_updateaddrforwater.UpdateAddress(getauthkey, postalCode, flatno, address, city, state, landmark, country, latitude, longitude, new Callback<Response>() {
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
                mProgressDialog.dismiss();

                responseforlaundryaddr = sb.toString();
                Log.e("getresforlaundryaddr", responseforlaundryaddr);

                try {
                    JSONObject jsonObject = new JSONObject(responseforlaundryaddr);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");

                    if (status.equals("1")) {
                        Toast.makeText(Getaddressforlaundry.this, msg, Toast.LENGTH_LONG).show();
                    } else if (status.equals("0")) {
                        Toast.makeText(Getaddressforlaundry.this, msg, Toast.LENGTH_LONG).show();
                    }

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



    private void checkfordelivery(){


        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_checkdelivery = adapter.create(UohmacAPI.class);

        api_checkdelivery.CheckForDelivery(postalCode, new Callback<Response>() {
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
                response_checkdelivery = sb.toString();
                Log.d("response_checkdelivery===", response_checkdelivery);
                try {
                    JSONObject jsonObject_checkdelivery = new JSONObject(response_checkdelivery);
                    Log.d("jsonObject_checkdelivery===", "" + jsonObject_checkdelivery);
                    String status = jsonObject_checkdelivery.getString("status");
                    String messgae = jsonObject_checkdelivery.getString("msg");

                    if (status.equals("1")) {
                        Toast.makeText(Getaddressforlaundry.this, "We got you covered", Toast.LENGTH_LONG).show();
                        UpdateAddressforlaundry();
                        Intent i_save = new Intent(Getaddressforlaundry.this, SelectingDetails.class);
                        //i_save.putExtra("savetotalprice", OrderedItemsDetailsForLaundry.totalprice);
                        // i_save.putExtra("savedate", dateString);
                        //i_save.putExtra("savespinnerselecteditem", sp_selecteditem);
                        sp_editaddrlaundry.putString("flatno_laundry", etflatno.getText().toString());
                        sp_editaddrlaundry.putString("addr1_laundry", etaddress.getText().toString());
                        sp_editaddrlaundry.putString("city_laundry", etcity.getText().toString());
                        sp_editaddrlaundry.putString("landmark_laundry", etlandmark.getText().toString());
                        sp_editaddrlaundry.putString("pincode_laundry", etpincode.getText().toString());
                        sp_editaddrlaundry.putString("country_laundry", etcountry.getText().toString());
                        sp_editaddrlaundry.putString("savetotalprice", OrderedItemsDetailsForLaundry.totalprice);
                        sp_editaddrlaundry.putString("savedate", dateString);
                        sp_editaddrlaundry.putString("savespinnerselecteditem", sp_selecteditem);
                        sp_editaddrlaundry.putString("save_addressforlaundry", "savedaddressforlaundry");
                        sp_editaddrlaundry.commit();
                        //sp_editaddrlaundry.clear();
                        // sp_editaddrlaundry.commit();
                                /*sp_editaddrlaundry.putString("flatno_laundry", etflatno.getText().toString());
                                sp_editaddrlaundry.putString("addr1_laundry", etaddress.getText().toString());
                                sp_editaddrlaundry.putString("city_laundry", etcity.getText().toString());
                                sp_editaddrlaundry.putString("landmark_laundry", etlandmark.getText().toString());
                                sp_editaddrlaundry.putString("pincode_laundry", etpincode.getText().toString());
                                sp_editaddrlaundry.putString("country_laundry", etcountry.getText().toString());
                                sp_editaddrlaundry.putString("save_addressforlaundry", "savedaddressforlaundry");
                                sp_editaddrlaundry.commit();*/
                        startActivity(i_save);
                    } else if (status.equals("0")) {
                        Toast.makeText(Getaddressforlaundry.this, "The seller does not ship to this Pincode.", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(Getaddressforlaundry.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.d("ERROR:", "**** " + error.getMessage());
            }
        });


    }


}

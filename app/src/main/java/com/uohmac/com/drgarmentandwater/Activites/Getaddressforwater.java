package com.uohmac.com.drgarmentandwater.Activites;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.uohmac.com.drgarmentandwater.Api.UohmacAPI;
import com.uohmac.com.drgarmentandwater.R;
import com.uohmac.com.drgarmentandwater.Utils.AppNetworkInfo;
import com.uohmac.com.drgarmentandwater.Utils.Constatns;
import com.uohmac.com.drgarmentandwater.Utils.GlobalUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by uOhmac Technologies on 20-Mar-17.
 */
public class Getaddressforwater extends AppCompatActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    EditText etflatno,et_addr1,et_addr2,et_city,et_pincode,et_addrtitle,et_country,et_fullname,et_mobile;
    Button btnsaveaddr;
    CheckBox cb_setdefault;
    SharedPreferences sp;
    String getauthkey,response_checkdelivery,provider,flatno,address,city,state,country,postalCode,subarea,landmark,responseforwateraddr,name_foraddress,mobile;
    Toolbar toolbar;
    LocationManager lm;
    TextView txt_addcurrentlocation_water;
    Geocoder geocoder;
    GPSTracker gps;
    Location L;
    double latitude,longitude;
    LinearLayout LL;
    List<Address> addresses = null;
    GoogleApiClient mGoogleApiClient_For_Location;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    ProgressDialog mProgressDialog;
    public static final String MYPREFERNCES_LOGIN = "mysharedpref_waterlaundry";

    public static final String PREFERENCES_SAVEADDRESS = "save_deliveryaddress";

    SharedPreferences.Editor edit_address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getaddressforwater);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        geocoder = new Geocoder(this, Locale.getDefault());
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        gps = new GPSTracker(Getaddressforwater.this);



        etflatno = (EditText)findViewById(R.id.flatno_water);
        et_addr1 = (EditText)findViewById(R.id.etaddress1_water);
        et_addr2 = (EditText)findViewById(R.id.etaddress2_water);
        et_city = (EditText)findViewById(R.id.etcity_water);
        et_pincode = (EditText)findViewById(R.id.etpincode_water);
        et_country = (EditText)findViewById(R.id.etcountry_water);
        et_addrtitle = (EditText)findViewById(R.id.etaddresstype_water);
        cb_setdefault = (CheckBox)findViewById(R.id.cb_defaultaddr);
        btnsaveaddr = (Button)findViewById(R.id.btn_saveaddrforwater);
        LL = (LinearLayout)findViewById(R.id.linearlayout_wateraddr);
        et_fullname = (EditText)findViewById(R.id.et_fullname);
        et_mobile = (EditText)findViewById(R.id.et_mobilenumber);
        txt_addcurrentlocation_water = (TextView)findViewById(R.id.txt_addcurrentlocation);

        if(!TextUtils.isEmpty(flatno)&&!TextUtils.isEmpty(address)&&!TextUtils.isEmpty(city)&&!TextUtils.isEmpty(landmark)&&!TextUtils.isEmpty(country)&&!TextUtils.isEmpty(postalCode)) {
            flatno = (String) getIntent().getExtras().get("editflatno_water");
            address = (String) getIntent().getExtras().get("editaddress_water");
            city = (String) getIntent().getExtras().get("editecity_water");
            landmark = (String) getIntent().getExtras().get("editlandmark_water");
            postalCode = (String) getIntent().getExtras().get("editpincode_water");
            country = (String) getIntent().getExtras().get("editcountry_water");
        }

        if(!TextUtils.isEmpty(flatno)&&!TextUtils.isEmpty(address)&&!TextUtils.isEmpty(city)&&!TextUtils.isEmpty(landmark)&&!TextUtils.isEmpty(postalCode)){



            etflatno.setText(flatno);
            et_addr1.setText(address);
            et_city.setText(city);
            et_addr2.setText(landmark);
            et_pincode.setText(postalCode);
            et_country.setText(country);

        }

        sp = getSharedPreferences(MYPREFERNCES_LOGIN, 0);
        getauthkey = sp.getString("auth_key", "");
        name_foraddress = sp.getString("firstName", "")+ sp.getString("lastName", "");
        Log.e("getfullname_water", name_foraddress);
        mobile = sp.getString("mobile", "");
        Log.e("getmobile_water", mobile);

        sp = getSharedPreferences(PREFERENCES_SAVEADDRESS, 0);
        edit_address = sp.edit();

        et_fullname.setText(name_foraddress);
        et_mobile.setText(mobile);


        if(AppNetworkInfo.isConnectingToInternet(Getaddressforwater.this)) {
            Criteria c = new Criteria();

            provider = lm.getBestProvider(c, true);
            L = lm.getLastKnownLocation(provider);

            if(ContextCompat.checkSelfPermission(Getaddressforwater.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(Getaddressforwater.this, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED ) {

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
                            Log.e("getlcoation", address + " " + city + " " + state + " " + country + " " + postalCode + " " + subarea);

                            txt_addcurrentlocation_water.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    etflatno.setText(flatno);
                                    et_addr1.setText(address);
                                    et_city.setText(city);
                                    et_country.setText(country);
                                    et_addr2.setText(landmark);
                                    et_pincode.setText(postalCode);
                                }
                            });


                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } //else
                    settingsrequest();
                //gps.showSettingsAlert();

            }
        }else{
            //Toast.makeText(GettingAddress.this, "Network error.Check your network connections and try again.", Toast.LENGTH_LONG).show();
            GlobalUtils.showSnackBar(LL, "Network error.Check your network connections and try again.");

        }


        btnsaveaddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Validate()){
                    if(AppNetworkInfo.isConnectingToInternet(Getaddressforwater.this)){
                        checkfordelivery();


                    }else{
                        Toast.makeText(Getaddressforwater.this, "No Network Connection Please try again later..!!!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        cb_setdefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    edit_address.clear();
                    edit_address.commit();
                    edit_address.putString("flatno", etflatno.getText().toString());
                    edit_address.putString("addr1", et_addr1.getText().toString());
                    edit_address.putString("city", et_city.getText().toString());
                    edit_address.putString("addr2", et_addr2.getText().toString());
                    edit_address.putString("pincode", et_pincode.getText().toString());
                    edit_address.putString("country", et_country.getText().toString());
                    edit_address.putString("namesfor_addr", name_foraddress);
                    edit_address.putString("save_address", "saved");
                    edit_address.commit();

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
                            status.startResolutionForResult(Getaddressforwater.this, REQUEST_CHECK_SETTINGS);
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

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

    //validation to enter all the fileds
    private boolean Validate(){

        flatno = etflatno.getText().toString();
        address = et_addr1.getText().toString();
        city = et_city.getText().toString();
        landmark = et_addr2.getText().toString();
        postalCode = et_pincode.getText().toString();

        boolean isValid = true;

        if(TextUtils.isEmpty(flatno)){
            isValid = false;
            etflatno.setError(getString(R.string.error_field_required));
            etflatno.requestFocus();

        }
        if(TextUtils.isEmpty(address)){
            isValid = false;
            et_addr1.setError(getString(R.string.error_field_required));
            et_addr1.requestFocus();

        }
        if(TextUtils.isEmpty(city)){
            isValid = false;
            et_city.setError(getString(R.string.error_field_required));
            et_city.requestFocus();

        }
        if(TextUtils.isEmpty(landmark)){
            isValid = false;
            et_addr2.setError(getString(R.string.error_field_required));
            et_addr2.requestFocus();

        }
        if(TextUtils.isEmpty(postalCode)){
            isValid = false;
            et_pincode.setError(getString(R.string.error_field_required));
            et_pincode.requestFocus();

        }


        /*if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            return false;
        }*/

        return isValid;
    }




    private void UpdateAddressforwater(){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.show();
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_updateaddrforwater = adapter.create(UohmacAPI.class);

        api_updateaddrforwater.UpdateAddress(getauthkey,postalCode,flatno,address,city,state,landmark,country,latitude,longitude, new Callback<Response>() {
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

                responseforwateraddr = sb.toString();
                Log.e("getresforwateraddr", responseforwateraddr);


                try{
                    JSONObject jsonObject = new JSONObject(responseforwateraddr);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");

                    if(status.equals("1")){
                        Toast.makeText(Getaddressforwater.this, msg, Toast.LENGTH_LONG).show();

                    }else if(status.equals("0")){
                        Toast.makeText(Getaddressforwater.this, msg, Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(Getaddressforwater.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(Getaddressforwater.this, messgae, Toast.LENGTH_LONG).show();
                        UpdateAddressforwater();
                        Intent i = new Intent(Getaddressforwater.this, DeliveryAddrDetailsforWater.class);
                        edit_address.putString("flatno_water", etflatno.getText().toString());
                        edit_address.putString("addr1_water", et_addr1.getText().toString());
                        edit_address.putString("city_water", et_city.getText().toString());
                        edit_address.putString("landmark_water", et_addr2.getText().toString());
                        edit_address.putString("pincode_water", et_pincode.getText().toString());
                        edit_address.putString("country_water", et_country.getText().toString());
                        edit_address.putString("save_address", "saved");
                        edit_address.commit();
                        startActivity(i);
                    } else if (status.equals("0")) {
                        Toast.makeText(Getaddressforwater.this, messgae, Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(Getaddressforwater.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.d("ERROR:", "**** " + error.getMessage());
            }
        });


    }
}

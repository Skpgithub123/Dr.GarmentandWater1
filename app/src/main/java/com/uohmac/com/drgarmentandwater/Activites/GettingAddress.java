package com.uohmac.com.drgarmentandwater.Activites;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.uohmac.com.drgarmentandwater.R;
import com.uohmac.com.drgarmentandwater.Utils.AppNetworkInfo;
import com.uohmac.com.drgarmentandwater.Utils.GlobalUtils;

import java.util.List;
import java.util.Locale;

/**
 * Created by SUNIL on 9/29/2016.
 */
public class GettingAddress extends AppCompatActivity implements OnMapReadyCallback {

    LocationManager lm;
    double latitude,longitude;
    Location L;
    String provider,flatno,address,city,state,country,subarea,sublocality,landmark,postalCode;
    EditText etflatno,etaddress,etlandmark,etcity,etpincode;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gettingaddress);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        geocoder = new Geocoder(this, Locale.getDefault());
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        gps = new GPSTracker(GettingAddress.this);



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
        btnsave = (Button)findViewById(R.id.btn_save);
        relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayoutfornet);



        if(AppNetworkInfo.isConnectingToInternet(GettingAddress.this)) {
            Criteria c = new Criteria();

            provider = lm.getBestProvider(c, true);
            L = lm.getLastKnownLocation(provider);

            if(ContextCompat.checkSelfPermission(GettingAddress.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(GettingAddress.this, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED ) {

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
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

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

                            etflatno.setText(flatno);
                            etaddress.setText(flatno);
                            etcity.setText(city+""+country);
                            etlandmark.setText(landmark);
                            etpincode.setText(postalCode);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } else
                    gps.showSettingsAlert();

            }
        }else{
            //Toast.makeText(GettingAddress.this, "Network error.Check your network connections and try again.", Toast.LENGTH_LONG).show();
            GlobalUtils.showSnackBar(relativeLayout,"Network error.Check your network connections and try again.");

        }

            btnsave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i_save = new Intent(GettingAddress.this,DeliveryAddress.class);
                    i_save.putExtra("takeaddress", address);
                    i_save.putExtra("takeecity", city);
                    i_save.putExtra("takelandmark", landmark);
                    i_save.putExtra("takepincode", postalCode);
                    startActivity(i_save);
                }
            });


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
                gps.showSettingsAlert();
            }



        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng latlngforlocation = new LatLng(latitude,longitude);
        mMap.addMarker(new MarkerOptions().position(latlngforlocation).title(landmark).snippet(address + flatno + city + postalCode));
        CameraPosition yourlocation = new CameraPosition.Builder().target(latlngforlocation).zoom(18).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(yourlocation));


        googleMap.setInfoWindowAdapter(new com.google.android.gms.maps.GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(GettingAddress.this);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(GettingAddress.this);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(GettingAddress.this);
                snippet.setTextColor(Color.BLACK);
                title.setTypeface(null, Typeface.BOLD);
                snippet.setText(marker.getSnippet());


                info.addView(title);
                info.addView(snippet);

                return info;

            }
        });
    }



}

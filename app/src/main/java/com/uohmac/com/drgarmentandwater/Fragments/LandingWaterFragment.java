package com.uohmac.com.drgarmentandwater.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;

import com.squareup.picasso.Picasso;
import com.uohmac.com.drgarmentandwater.Activites.WaterCanDetails;
import com.uohmac.com.drgarmentandwater.Activites.WaterItemDetails;
import com.uohmac.com.drgarmentandwater.Adpater.MenAdapter;

import com.uohmac.com.drgarmentandwater.Api.UohmacAPI;
import com.uohmac.com.drgarmentandwater.Pojos.WaterStrings;
import com.uohmac.com.drgarmentandwater.R;
import com.uohmac.com.drgarmentandwater.Utils.AppNetworkInfo;
import com.uohmac.com.drgarmentandwater.Utils.Constatns;
import com.uohmac.com.drgarmentandwater.Utils.GlobalUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by SUNIL on 9/24/2016.
 */
public class LandingWaterFragment extends Fragment {

    View view_waterfragment;
    ImageView img_bisleri,img_kinley,img_noraml;
    RelativeLayout rr1,rr2,rr3,mainrr;
    ProgressDialog mProgressDialog;
    String res,response_checkdelivery,bisleri,kinley,normal,price_bisleri,price_kinley,price_normal,bis_img,kinley_img,normal_img;
    JSONObject jobj_parent,jobj1,jobj2,jobj3;
    JSONArray jsonArray;
    TextView txt_pincodeheading,txt_bisname,txt_kinleyname,txt_normalname,txt_bisprice,txt_kinleyprice,txt_normalprice;
    private static final int PERMISSION_REQUEST_CODE = 200;
    ListView lv_waterbrands;

    WaterStrings water_strings;
    WaterAdapter _wateradapter;

    ArrayList<WaterStrings> waterStringsArrayList = new ArrayList<>();

    Dialog dialog_tockeckdelivery;
    EditText ed_deliverytocheck;
    Button btn_checkdelivery;
   // ImageView img_closecheckdeliverypopup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }



        if (checkPermission()) {

           // Snackbar.make(view, "Permission already granted.", Snackbar.LENGTH_LONG).show();
           // Toast.makeText(getActivity(),"Permission already granted", Snackbar.LENGTH_LONG).show();

        } else {

           // Snackbar.make(view, "Please request permission.", Snackbar.LENGTH_LONG).show();
           // Toast.makeText(getActivity(),"Please request permission.", Snackbar.LENGTH_LONG).show();

        }

        if (!checkPermission()) {

            requestPermission();

        } else {

            //Snackbar.make(view, "Permission already granted.", Snackbar.LENGTH_LONG).show();
           // Toast.makeText(getActivity(),"Permission already granted.", Toast.LENGTH_LONG).show();

        }
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view_waterfragment = inflater.inflate(R.layout.fragment_water, container, false);


        lv_waterbrands = (ListView)view_waterfragment.findViewById(R.id.lv_waterbrandslist);



        dialog_tockeckdelivery = new Dialog(getActivity());
        dialog_tockeckdelivery.setContentView(R.layout.dialog_tocheckdelivery);
        ed_deliverytocheck = (EditText)dialog_tockeckdelivery.findViewById(R.id.ed_pincodeentry);
        btn_checkdelivery = (Button)dialog_tockeckdelivery.findViewById(R.id.btn_checkdelivery);
        txt_pincodeheading = (TextView)dialog_tockeckdelivery.findViewById(R.id.txt_pincodeheading);
        //img_closecheckdeliverypopup = (ImageView)dialog_tockeckdelivery.findViewById(R.id.img_closecheckdeliverypopup);
            txt_pincodeheading.setText("Enter Pincode");

        dialog_tockeckdelivery.show();

        btn_checkdelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed_deliverytocheck.getText().toString().equals("")){
                    ed_deliverytocheck.setError(getString(R.string.error_field_required));
                    ed_deliverytocheck.requestFocus();
                }else if(AppNetworkInfo.isConnectingToInternet(getActivity())){
                    checkfordelivery();
                }else{
                    Toast.makeText(getActivity(), "No network found.. Please try again later", Toast.LENGTH_SHORT).show();

                }
            }
        });


       /* img_closecheckdeliverypopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_tockeckdelivery.dismiss();
            }
        });*/


        if(AppNetworkInfo.isConnectingToInternet(getActivity())){
            getwaterdata();

        } else{
            Toast.makeText(getActivity(),"No network found... Please try again",Toast.LENGTH_SHORT).show();
        }


        lv_waterbrands.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                WaterStrings waterStrings = waterStringsArrayList.get(position);
                Intent i_waterdetails = new Intent(getActivity(),WaterItemDetails.class);
                i_waterdetails.putExtra("sendwaterid",waterStrings.getId_water());
                startActivity(i_waterdetails);
            }
        });

        return view_waterfragment;

    }


    private void getwaterdata(){
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.show();

        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_waterdata = adapter.create(UohmacAPI.class);

        api_waterdata.GetLaundryData(new Callback<Response>() {
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
                    jobj1 = new JSONObject(res);
                    jsonArray = jobj1.getJSONArray("water");
                    Log.e("waterres", "" + jsonArray);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        water_strings = new WaterStrings();
                        jobj1 = jsonArray.optJSONObject(i);
                        water_strings.setCategory_name(jobj1.getString("category_name"));
                        water_strings.setCategory_img(jobj1.getString("category_img"));
                        water_strings.setItem_price("\u20B9" + jobj1.getString("item_price"));
                        water_strings.setId_water(jobj1.getString("id"));

                        waterStringsArrayList.add(water_strings);
                    }

                    if (getActivity() != null) {

                        _wateradapter = new WaterAdapter();
                        lv_waterbrands.setAdapter(_wateradapter);


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                mProgressDialog.dismiss();


            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "Please try after sometime.", Toast.LENGTH_SHORT).show();
                Log.d("ERROR:", "**** " + error.getMessage());
            }
        });
    }



    private void checkfordelivery(){
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Checking...");
        mProgressDialog.show();


        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_checkdelivery = adapter.create(UohmacAPI.class);

        api_checkdelivery.CheckForDelivery(ed_deliverytocheck.getText().toString().trim(), new Callback<Response>() {
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
                        Toast.makeText(getActivity(), "We got you covered", Toast.LENGTH_LONG).show();
                        dialog_tockeckdelivery.dismiss();
                       // btn_checkdone.setVisibility(View.VISIBLE);
                        //btn_checkdelivery.setVisibility(View.GONE);


                    } else if (status.equals("0")) {
                        Toast.makeText(getActivity(), "The seller does not ship to this Pincode.", Toast.LENGTH_LONG).show();
                        txt_pincodeheading.setText("Try another Pincode");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                mProgressDialog.dismiss();

            }

            @Override
            public void failure(RetrofitError error) {

                mProgressDialog.dismiss();
                Toast.makeText(getActivity(), "Please try after sometime."+error.toString(), Toast.LENGTH_SHORT).show();
                Log.d("ERROR:", "**** " + error.getMessage());
            }
        });


    }



    public class WaterAdapter extends BaseAdapter
    {
        ViewHolder holder;
        public  WaterStrings waterStrings;

        @Override
        public int getCount() {
            return waterStringsArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            waterStrings =waterStringsArrayList.get(position);

            View rowView;

            rowView =  getActivity().getLayoutInflater().inflate(R.layout.waterbrand_listitem, null);
            holder = new ViewHolder();

            holder.txt_brandname = (TextView)rowView.findViewById(R.id.txt_bisleri);
            holder.txt_brandprice = (TextView)rowView.findViewById(R.id.txt_bisprice);
            holder.img_water = (CircleImageView)rowView.findViewById(R.id.ivOrgLogobisleri);

            /*Animation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(50); //You can manage the blinking time with this parameter
            anim.setStartOffset(50);
            anim.setBackgroundColor(Color.RED);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(Animation.INFINITE);
            holder.txt_brandprice.startAnimation(anim);*/

            if(!TextUtils.isEmpty(waterStrings.getCategory_name())&&!TextUtils.isEmpty(waterStrings.getItem_price())){
                holder.txt_brandname.setText(waterStrings.getCategory_name());
                holder.txt_brandprice.setText(waterStrings.getItem_price());
                holder.waterimages = waterStrings.getCategory_img();
                holder.id = waterStrings.getId_water();

                if(!TextUtils.isEmpty(holder.waterimages)){
                    Picasso.with(getContext())
                            .load(waterStrings.getCategory_img())
                            .into(holder.img_water);
                }
            }



            return rowView;
        }

        public class ViewHolder
        {
            CircleImageView img_water;
            String id;
            TextView txt_brandname,txt_brandprice;
            String waterimages;
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(getActivity(), GET_ACCOUNTS);
        int result2 = ContextCompat.checkSelfPermission(getActivity(), READ_EXTERNAL_STORAGE);
        int result3 = ContextCompat.checkSelfPermission(getActivity(), READ_PHONE_STATE);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED;

    }


    private void requestPermission() {

        ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION, GET_ACCOUNTS, READ_EXTERNAL_STORAGE,READ_PHONE_STATE}, PERMISSION_REQUEST_CODE);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted  =   grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean contactsAccepted  =   grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted   =   grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean readphoneaccepted =   grantResults[3] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && contactsAccepted && storageAccepted && readphoneaccepted)
                       // Snackbar.make(mainrr, "Permission Granted, Now you can access location data,contacts and storage.", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(getActivity(),"Permission Granted, Now you can access location data,contacts and storage.", Toast.LENGTH_LONG).show();

                    else {

                       // Snackbar.make(mainrr, "Permission Denied, You cannot access location data and camera.", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(getActivity(),"Permission Denied, You cannot access location data,contacts and storage.", Toast.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION, GET_ACCOUNTS,READ_EXTERNAL_STORAGE,READ_PHONE_STATE},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

}

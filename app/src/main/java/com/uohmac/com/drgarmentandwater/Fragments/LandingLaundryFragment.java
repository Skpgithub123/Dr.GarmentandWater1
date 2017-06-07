package com.uohmac.com.drgarmentandwater.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.squareup.picasso.Picasso;
import com.uohmac.com.drgarmentandwater.Activites.DryCleaningActivity;
import com.uohmac.com.drgarmentandwater.Activites.HomeActivity;
import com.uohmac.com.drgarmentandwater.Activites.Laundryitemdetails;
import com.uohmac.com.drgarmentandwater.Api.UohmacAPI;
import com.uohmac.com.drgarmentandwater.R;
import com.uohmac.com.drgarmentandwater.Utils.AppNetworkInfo;
import com.uohmac.com.drgarmentandwater.Utils.Constatns;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by SUNIL on 9/24/2016.
 */
public class LandingLaundryFragment extends Fragment  {

    private View myFragmentView;
    static String responseforlaundry,washandfold,washandiron,drycleaning,minimumorder,imgwashfold,imgwashiron,imgdrycleaning,imagebg,idwashiron,idwashfold,iddryclecning,pricewashfold,pricewashiron;
    CircleImageView img_washfold,img_washiron,img_drycleaning;
    int value;
    ImageView imgbg;
    JSONObject jobj,j,obj,jobj_parent;
    JSONArray jarray;
    ArrayList<String> alldata = new ArrayList<>();
    SliderLayout mDemoSlider_bike;
    TextView tvwashandfold,tvwashandiron,tvdrycleaning;
    public static TextView tvminimumorder;
    ProgressDialog mProgressDialog;
    LinearLayout LL_wf,LL_wi,LL_drycleaning;



    public static Fragment newInstance(Bundle args) {
        LandingLaundryFragment fragment = new LandingLaundryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public LandingLaundryFragment() {


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        myFragmentView = inflater.inflate(R.layout.fragmentlaundry1, container, false);
        mDemoSlider_bike = (SliderLayout)myFragmentView.findViewById(R.id.sliderlayout);
        HashMap<String, Integer> file_maps_bike = new HashMap<String, Integer>();

        file_maps_bike.put("image1",R.drawable.wf);
        file_maps_bike.put("image3",R.drawable.wi);
        file_maps_bike.put("image4", R.drawable.astorialaundry3);
        // file_maps_bike.put("image4",R.drawable.slidingfoure);
        // file_maps.put("image5",R.drawable.slidingfive);


        Log.d("keyset", "===" + file_maps_bike.keySet());
        for (String getimage: file_maps_bike.keySet()) {

            TextSliderView textSliderView = new TextSliderView(getActivity());
            textSliderView
                    .image(file_maps_bike.get(getimage))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            mDemoSlider_bike.addSlider(textSliderView);
        }
        mDemoSlider_bike.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider_bike.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider_bike.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider_bike.setDuration(3000);
       // mDemoSlider_bike.addOnPageChangeListener(getActivity());
        mDemoSlider_bike.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider_bike.setDuration(1500);
        img_washfold = (CircleImageView)myFragmentView.findViewById(R.id.imagewashfold);
        img_washiron = (CircleImageView)myFragmentView.findViewById(R.id.imagewashiron);
        img_drycleaning = (CircleImageView)myFragmentView.findViewById(R.id.imagedrycleaning);
       // imgbg = (ImageView)myFragmentView.findViewById(R.id.imagebg);

        LL_wf = (LinearLayout)myFragmentView.findViewById(R.id.LL_wf);
        LL_wi = (LinearLayout)myFragmentView.findViewById(R.id.LL_wi);
        LL_drycleaning = (LinearLayout)myFragmentView.findViewById(R.id.LL_dryclecning);

        tvwashandfold = (TextView)myFragmentView.findViewById(R.id.txt_washfold);
        tvwashandiron = (TextView)myFragmentView.findViewById(R.id.txt_washiron);
        tvdrycleaning = (TextView)myFragmentView.findViewById(R.id.txt_drycleaning);
        //tvminimumorder = (TextView)myFragmentView.findViewById(R.id.txtminimum);

        if(AppNetworkInfo.isConnectingToInternet(getActivity())){
            getlaundrydata();

        } else{
            Toast.makeText(getActivity(), "No network found... Please try again", Toast.LENGTH_SHORT).show();
        }

        /*sp = getActivity().getSharedPreferences(MYPREFERNCES_TOTALPRICE, 0);
        if(!TextUtils.isEmpty(String.valueOf(value))) {
            value = sp.getInt("storedvalue", 0);
            Log.e("getstoreprice", ""+value);
        }*/

        LL_wf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i_washfold = new Intent(getActivity(),Laundryitemdetails.class);
               // i_washiron.putExtra("washfoldname",washandfold );
               // i_washiron.putExtra("washfoldimage",imgwashfold);
                //i_washiron.putExtra("washfoldprice", pricewashfold);

                i_washfold.putExtra("washfoldkey", idwashfold);
                startActivity(i_washfold);
            }
        });

        LL_wi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i_washiron = new Intent(getActivity(), Laundryitemdetails.class);
                //i_washiron.putExtra("washironname",washandiron );
                //i_washiron.putExtra("washironimage",imgwashiron);
                // i_washiron.putExtra("washironprice", pricewashiron);
                i_washiron.putExtra("washironkey", idwashiron);
                startActivity(i_washiron);
            }
        });

        LL_drycleaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i_drycleaning = new Intent(getActivity(), DryCleaningActivity.class);
                // i_drycleaning.putExtra("passtotalpricefordry", gettotalpricefordrycleaning);
                startActivity(i_drycleaning);
            }
        });



        myFragmentView.setFocusableInTouchMode(true);
        myFragmentView.requestFocus();

        myFragmentView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i("tag", "keyCode: " + keyCode);
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        getActivity().finish();
                        //Intent intent = new Intent(getActivity(), HomeActivity.class);
                        //startActivity(intent);

                        return true;
                    }
                }
                return false;
            }
            });



        return  myFragmentView;
    }


    private void getlaundrydata(){
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.show();
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_laundrydata = adapter.create(UohmacAPI.class);


        api_laundrydata.GetLaundryData(new Callback<Response>() {
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

                responseforlaundry = sb.toString();
                Log.e("getre", responseforlaundry);

                try{
                    jobj_parent = new JSONObject(responseforlaundry);
                    jarray = jobj_parent.getJSONArray("laundry");
                    Log.e("getres", ""+responseforlaundry);

                        jobj = jarray.optJSONObject(0);
                        washandfold = jobj.getString("category_name");
                        imgwashfold = jobj.getString("category_img");
                        pricewashfold = jobj.getString("item_price");
                        idwashfold = jobj.getString("id");
                        Log.e("getstring", washandfold);
                        Log.e("getimageurl", imgwashfold);
                         Log.e("getid", idwashfold);

                         j = jarray.optJSONObject(1);
                        washandiron = j.getString("category_name");
                        imgwashiron = j.getString("category_img");
                        pricewashiron = j.getString("item_price");
                        idwashiron = j.getString("id");
                        Log.e("getid2222", idwashiron);

                         obj = jarray.optJSONObject(2);
                       drycleaning = obj.getString("category_name");
                       imgdrycleaning = obj.getString("category_img");
                        iddryclecning = obj.getString("id");


                    JSONObject jj = jarray.optJSONObject(3);
                    imagebg = jj.getString("bg_img");
                    minimumorder = jj.getString("min_order");
                   /* if(!TextUtils.isEmpty(imagebg)) {
                        Picasso.with(getActivity())
                                .load(imagebg)
                                .into(imgbg);
                    }*/
                   // tvminimumorder.setText(minimumorder);
                    tvwashandfold.setText(washandfold);
                    tvwashandiron.setText(washandiron);
                    tvdrycleaning.setText(drycleaning);

                    //displaying images from json
                    if(!TextUtils.isEmpty(imgwashfold)) {
                        Picasso.with(getActivity())
                                .load(imgwashfold)
                                .into(img_washfold);
                    }

                     if(!TextUtils.isEmpty(imgwashiron)) {
                        Picasso.with(getActivity())
                                .load(imgwashiron)
                                .into(img_washiron);
                    }

                     if(!TextUtils.isEmpty(imgdrycleaning)) {
                        Picasso.with(getActivity())
                                .load(imgdrycleaning)
                                .into(img_drycleaning);
                    }


                }catch (Exception e){
                    e.printStackTrace();
                }

                mProgressDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error)
            {
                Toast.makeText(getActivity(), "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.d("ERROR:", error.getMessage());

            }
        });
    }





}

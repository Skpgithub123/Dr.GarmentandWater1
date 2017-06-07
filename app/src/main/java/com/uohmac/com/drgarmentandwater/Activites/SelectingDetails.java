package com.uohmac.com.drgarmentandwater.Activites;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.payu.india.CallBackHandler.OnetapCallback;
import com.payu.india.Extras.PayUChecksum;
import com.payu.india.Extras.PayUSdkDetails;
import com.payu.india.Interfaces.OneClickPaymentListener;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuHashes;
import com.payu.india.Model.PostData;
import com.payu.india.Payu.Payu;
import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuErrors;
import com.payu.payuui.Activity.PayUBaseActivity;
import com.uohmac.com.drgarmentandwater.Api.UohmacAPI;
import com.uohmac.com.drgarmentandwater.Fragments.WalletFragment;
import com.uohmac.com.drgarmentandwater.R;
import com.uohmac.com.drgarmentandwater.Utils.AppNetworkInfo;
import com.uohmac.com.drgarmentandwater.Utils.Constatns;
import com.uohmac.com.drgarmentandwater.Utils.GlobalUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by System-03 on 11/15/2016.
 */
public class SelectingDetails extends AppCompatActivity implements OneClickPaymentListener  {

    String merchantKey="",result="",status="",balanceamount="0", userCredentials="",options="0",options_onlinepayment="0",one_wallet="0",sendtotalprice="",senddate="",sendspinneritem="",responsefororderdetails="",msg_respostorderdetails="",status_respostorderdetails="",order_id="",payment_method_forpay="";
    String wallet="0",COD = "0",Onlinepayment = "0",merchantresponse="";

    String Countforonline = "0";
    String Newwallentamount="0";

    // These will hold all the payment parameters
    private PaymentParams mPaymentParams;

    // This sets the configuration
    private PayuConfig payuConfig;
    // Used when generating hash from SDK
    private PayUChecksum checksum;
    private Spinner environmentSpinner;
    TextView tvaddressview,tveditaddress,tvaddressdone,tvdeliveryaddrconfirm,tvdeliveryname,tvcurrentwalletamt,tvaddnewaddress,tvfinalamount,tvwalletamount,tvamtaftercalci;
    public static  String tookaddress,tookcity,tookcountry,tooklandmark,tookstate,tookpincode,tooksubarea,tookflatno,getauthkey,mobilenum_forpayment,email_forpayment,firstname_forpayment,resforgetaddress;
     CheckBox ch_checkwallet;
     RelativeLayout relativeLayout_addr;
     Dialog dialog;
     SharedPreferences sp;
    JSONArray jsonArray;
    JSONObject jsonObject;
    Button btnproceedtopay;
    RadioGroup rg_group;
    RadioButton rd_cod,rd_onlinepayment;
    ProgressDialog mProgressDialog;
    public static final String TAG = "PayUMoneySDK Sample";
    public static final String MYPREFERNCES_LOGIN = "mysharedpref_waterlaundry";
    public static final String PREFERNCES_ADDRESS = "save_laundryaddress";
    int value_subtoal;
    String[] datas;
    String first="", second="", third="";
    String flatno_laundry="",addr1_laundry="",city_laundry="",landmark_laundry="",pincode_laundry="",country_laundry="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectingdetails);


        //TODO Must write this code if integrating One Tap payments
        OnetapCallback.setOneTapCallback(this);

        //TODO Must write below code in your activity to set up initial context for PayU
        Payu.setInstance(this);

        tvaddressview = (TextView) findViewById(R.id.txt_addressview);
        tveditaddress = (TextView)findViewById(R.id.txt_editaddress);
        //tvaddressdone = (TextView)findViewById(R.id.txt_addressdone);
        tvfinalamount = (TextView)findViewById(R.id.txtfinalamount);
        tvwalletamount = (TextView)findViewById(R.id.txtshowwalletamt);
        tvamtaftercalci = (TextView)findViewById(R.id.txtamtaftercalc);
        tvcurrentwalletamt = (TextView)findViewById(R.id.txtcurrentwalletamt);
        ch_checkwallet = (CheckBox)findViewById(R.id.cb_wallet);
        tvaddnewaddress = (TextView)findViewById(R.id.txt_addingnewaddress);
        btnproceedtopay = (Button)findViewById(R.id.btn_proceedtopay);
        //relativeLayout_addr = (RelativeLayout)findViewById(R.id.relativeforaddress);
        rg_group = (RadioGroup)findViewById(R.id.radiogroup_paymentmodes);
        final int id = rg_group.getCheckedRadioButtonId();
        rd_cod = (RadioButton)findViewById(R.id.radio_cod);
        rd_onlinepayment = (RadioButton)findViewById(R.id.radio_onlinepayment);


        sp = getSharedPreferences(MYPREFERNCES_LOGIN, 0);
        getauthkey = sp.getString("auth_key", "");


        sp = getSharedPreferences(PREFERNCES_ADDRESS, 0);
        sendtotalprice = sp.getString("savetotalprice", "");
        senddate = sp.getString("savedate", "");
        sendspinneritem = sp.getString("savespinnerselecteditem", "");
        flatno_laundry = sp.getString("flatno_laundry","");
        addr1_laundry = sp.getString("addr1_laundry", "");
        city_laundry = sp.getString("city_laundry", "");
        landmark_laundry = sp.getString("landmark_laundry","");
        pincode_laundry = sp.getString("pincode_laundry", "");
        country_laundry = sp.getString("country_laundry", "");
        //sendtotalprice = (String) getIntent().getExtras().get("savetotalprice");
        //senddate = (String) getIntent().getExtras().get("savedate");
        //sendspinneritem = (String)getIntent().getExtras().get("savespinnerselecteditem");


        mobilenum_forpayment = sp.getString("mobile", "");
        email_forpayment = sp.getString("email", "");
       // email_forpayment = sp.getString("user_name", "");
        firstname_forpayment = sp.getString("firstName", "");
        Log.e("gettingauthkey_forpayment", getauthkey);
        Log.e("getmob_", mobilenum_forpayment);
        Log.e("getemail_", email_forpayment);
        Log.e("getfirstname", firstname_forpayment);



        tvfinalamount.setText(OrderedItemsDetailsForLaundry.totalprice);

        btnproceedtopay.setText("Proceed To Pay" +"\u20B9" + OrderedItemsDetailsForLaundry.totalprice);
        /*            Wallet amount showing text       */



        if(AppNetworkInfo.isConnectingToInternet(SelectingDetails.this)){
            getaddress();
            checkwalletbalance();
        }else{
            Toast.makeText(SelectingDetails.this, "No Network Found.. Plese try again later.!!!", Toast.LENGTH_LONG).show();
        }



        // lets tell the people what version of sdk we are using
        PayUSdkDetails payUSdkDetails = new PayUSdkDetails();

        Toast.makeText(this, "Build No: " + payUSdkDetails.getSdkBuildNumber() + "\n Build Type: " + payUSdkDetails.getSdkBuildType() + " \n Build Flavor: " + payUSdkDetails.getSdkFlavor() + "\n Application Id: " + payUSdkDetails.getSdkApplicationId() + "\n Version Code: " + payUSdkDetails.getSdkVersionCode() + "\n Version Name: " + payUSdkDetails.getSdkVersionName(), Toast.LENGTH_LONG).show();


        /*if(WalletFragment.txt_testing.getText().toString().equals("0")){


            ch_checkwallet.setVisibility(View.GONE);
            tvcurrentwalletamt.setVisibility(View.GONE);
            tvwalletamount.setVisibility(View.GONE);
            tvamtaftercalci.setVisibility(View.GONE);

        }else*//*if(){*/
            ch_checkwallet.setVisibility(View.VISIBLE);
            tvcurrentwalletamt.setVisibility(View.VISIBLE);
            tvwalletamount.setVisibility(View.VISIBLE);
            tvamtaftercalci.setVisibility(View.VISIBLE);
        //}


        tveditaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i_editaddress = new Intent(SelectingDetails.this, Getaddressforlaundry.class);
                i_editaddress.putExtra("editflatno", flatno_laundry);
                i_editaddress.putExtra("editaddress", addr1_laundry);
                i_editaddress.putExtra("editecity", city_laundry);
                i_editaddress.putExtra("editcountry", country_laundry);
                i_editaddress.putExtra("editlandmark", landmark_laundry);
                i_editaddress.putExtra("editpincode", pincode_laundry);
                //i_editaddress.putExtra("test", "Tested");
               // i_editaddress.putExtra("editaddress", "Edit");

                startActivity(i_editaddress);


            }
        });


        tvaddnewaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i_addnewaddress = new Intent(SelectingDetails.this, Getaddressforlaundry.class);
                /*i_addnewaddress.putExtra("addnewflatno", "");
                i_addnewaddress.putExtra("addnewaddress", "");
                i_addnewaddress.putExtra("addnewecity", "");
                i_addnewaddress.putExtra("addnewlandmark", "");
                i_addnewaddress.putExtra("addnewpincode", "");
                i_addnewaddress.putExtra("addnewcountry", "");*/
                i_addnewaddress.putExtra("addnewaddress", "Add");
                startActivity(i_addnewaddress);

            }
        });


      /*  tvaddressdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    dialog = new Dialog(SelectingDetails.this);
                    dialog.setContentView(R.layout.deliveryaddressconfirmpopup);
                    tvdeliveryaddrconfirm = (TextView)dialog.findViewById(R.id.txt_deliveryaddrconfirmation);
                    tvdeliveryname = (TextView)dialog.findViewById(R.id.txt_deliveryname);


                    tvdeliveryaddrconfirm.setText(tookflatno+" "+tookaddress+" "+tookcity+" "+tooklandmark+" "+tookpincode+" "+tooksubarea);
                    tvdeliveryname.setText(HomeActivity.fullname);
                    dialog.show();

            }
        });*/


        btnproceedtopay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppNetworkInfo.isConnectingToInternet(SelectingDetails.this)){

                    Log.d("valueofoption","****  "+options+"    *****    "+options_onlinepayment + "  "+one_wallet);

                    /*if(options.equals("0")){
                        Toast.makeText(SelectingDetails.this, "Please select payment mode",Toast.LENGTH_SHORT).show();
                    }else */
                    if(options.equals("1")) {
                        Toast.makeText(SelectingDetails.this, "cod selected",Toast.LENGTH_SHORT).show();
                        PostAddressDetails("1");

                        Log.e("pay from cod====","Paying from cod" );
                     //   PostAddressDetails(options, WalletFragment.balanceamount);

                    } else if (one_wallet.equals("wallet"))
                    {
                        if (tvcurrentwalletamt.getText().toString().equals("0"))
                        {
                            //xzcxzc
                            if (options_onlinepayment.equals("both"))
                            {
                                PostAddressDetails("4");
                              //  PostAddressDetails(options, WalletFragment.balanceamount);
                                Toast.makeText(SelectingDetails.this, "Both selected wallet and online",Toast.LENGTH_SHORT).show();
                                Log.e("pay from online====", "Paying from online");
                            }else
                            {

                               // PostAddressDetails(options, WalletFragment.balanceamount);

                                if (Countforonline.equals("1"))
                                {
                                    //PostAddressDetails("2");
                                    PostAddressDetails_Wallet(tvfinalamount.getText().toString(), "2");

                                    Toast.makeText(SelectingDetails.this, "Wallet only selected",Toast.LENGTH_SHORT).show();
                                    Log.e("pay from wallet====", "Paying from wallet");

                                }else
                                {
                                   // PostAddressDetails("3");
                                    Toast.makeText(SelectingDetails.this, "Please Select Online payment",Toast.LENGTH_SHORT).show();
                                    Log.e("pay from online====", "Paying from online");
                                }



                            }

                        }else
                        {
                           // PostAddressDetails("2");
                            PostAddressDetails_Wallet(tvfinalamount.getText().toString(),"2");
                            Toast.makeText(SelectingDetails.this, "Wallet only selected",Toast.LENGTH_SHORT).show();
                            Log.e("pay from wallet====", "Paying from wallet");

                        }


                    }else if (options_onlinepayment.equals("both"))
                    {
                       // PostAddressDetails(options, WalletFragment.balanceamount);
                        PostAddressDetails("3");
                        Toast.makeText(SelectingDetails.this, "Online payment selected",Toast.LENGTH_SHORT).show();
                        Log.e("pay from online====", "Paying from online");
                    }else
                    {
                     
                        Toast.makeText(SelectingDetails.this, "Please select payment mode",Toast.LENGTH_SHORT).show();
                    }
                   /* else if(one_wallet.equals("wallet")&&options_onlinepayment.equals("both")){
                        Toast.makeText(SelectingDetails.this, "online payment and wallet selected",Toast.LENGTH_SHORT).show();
                        //PostAddressDetails(""+4,tvamtaftercalci.getText().toString());
                    }else if (options_onlinepayment.equals("both")){
                        Log.d("valueofthex", "" + tvfinalamount.getText().toString());

                        if (one_wallet.equals("wallet")) {
                            if (Integer.parseInt(tvfinalamount.getText().toString()) >= 100) {
                                   *//*else{*//*
                                // }
                                Toast.makeText(SelectingDetails.this, "wallet only selected", Toast.LENGTH_SHORT).show();

                                //   rd_onlinepayment.setEnabled(false);
                                rd_onlinepayment.setVisibility(View.GONE);

                            }else
                            {

                                Toast.makeText(SelectingDetails.this, "Please choose online payment", Toast.LENGTH_SHORT).show();

                            }
                        }else
                        {
                            Toast.makeText(SelectingDetails.this, "online payment selected", Toast.LENGTH_SHORT).show();
                        }


                    }


                    else if(one_wallet.equals("wallet")){
                      //  Toast.makeText(SelectingDetails.this, "wallet selected",Toast.LENGTH_SHORT).show();

                        Log.d("valueofpricetx",""+tvfinalamount.getText().toString());

                                if(Integer.parseInt(tvfinalamount.getText().toString())>=100){
                                   *//*else{*//*
                                    Toast.makeText(SelectingDetails.this, "wallet only selected",Toast.LENGTH_SHORT).show();
                                        // PostAddressDetails(options,tvamtaftercalci.getText().toString());
                                   // }
                                }else{

                                    Toast.makeText(SelectingDetails.this, "Please choose online payment", Toast.LENGTH_SHORT).show();


                                  *//*  if(options_onlinepayment.equals("0")){*//*
                                     *//*}*//*
                                }
                       *//* if(options_onlinepayment.equals("both")){


                            Log.d("checkingvalue.===", options+""+options_onlinepayment);


                            // PostAddressDetails(options,sendtotalprice);
                        }*//*

                    }*/
                   /* if(options_onlinepayment.equals("both")) {

                        Toast.makeText(SelectingDetails.this, "online payment selected", Toast.LENGTH_SHORT).show();

                    }*/
                    /*if(id==-1){
                        Toast.makeText(SelectingDetails.this, "Please select payment mode",Toast.LENGTH_SHORT).show();
                    }else {
                        PostAddressDetails();
                    }*/
                }else{
                    Toast.makeText(SelectingDetails.this, "No network found.Please try again.!!",Toast.LENGTH_LONG).show();
                }
            }
        });

        ch_checkwallet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    Countforonline = "0";
                    rg_group.clearCheck();
                    tvcurrentwalletamt.setVisibility(View.VISIBLE);

                     value_subtoal = (Integer.parseInt(tvfinalamount.getText().toString()) - Integer.parseInt(balanceamount));

                    tvcurrentwalletamt.setText(" "+value_subtoal);
                    tvwalletamount.setText("-"+balanceamount);
                    btnproceedtopay.setText("Proceed To Pay" + "\u20B9" + value_subtoal);
                    // sendtotalprice=""+value_subtoal;
                    options = "0";
                    tvamtaftercalci.setVisibility(View.VISIBLE);
                    rd_cod.setEnabled(false);

                    if((Integer.parseInt(balanceamount)<=Integer.parseInt(tvfinalamount.getText().toString()))){
                                   /*else{*/
                   //     Toast.makeText(SelectingDetails.this, "wallet only selected",Toast.LENGTH_SHORT).show();
                      //  rd_onlinepayment.setVisibility(View.GONE);
                        rd_onlinepayment.setEnabled(true);
                        rd_cod.setEnabled(false);
                        options_onlinepayment = "0";
                        tvcurrentwalletamt.setVisibility(View.VISIBLE);
                        tvcurrentwalletamt.setText(""+0);

                        options = "0";
                        if (balanceamount.equals(tvfinalamount.getText().toString()))
                        {
                            rd_onlinepayment.setEnabled(false);

                            Countforonline = "1";

                        }/*else
                        {

                        }*/

                        tvamtaftercalci.setVisibility(View.VISIBLE);
                        tvwalletamount.setVisibility(View.VISIBLE);
                        tvamtaftercalci.setText(""+value_subtoal);
                        // PostAddressDetails(options,tvamtaftercalci.getText().toString());
                        // }
                    }else{
                        options = "0";
                        options_onlinepayment="0";
                       /* tvamtaftercalci.setVisibility(View.VISIBLE);
                        tvamtaftercalci.setText("" + value_subtoal);*/
                        /*tvcurrentwalletamt.setVisibility(View.VISIBLE);*/
                        btnproceedtopay.setText("Proceed To Pay");
                        tvcurrentwalletamt.setVisibility(View.VISIBLE);
                        tvwalletamount.setVisibility(View.VISIBLE);
                        int walletamount = (Integer.parseInt(balanceamount) - Integer.parseInt(tvfinalamount.getText().toString()) );
                        tvcurrentwalletamt.setText(""+ walletamount);

                        tvamtaftercalci.setVisibility(View.GONE);
                        rd_onlinepayment.setEnabled(false);
                        rd_cod.setEnabled(false);
                   //     Toast.makeText(SelectingDetails.this, "Please choose online payment", Toast.LENGTH_SHORT).show();

                            /*  if(options_onlinepayment.equals("0")){*/
                                     /*}*/
                    }


                 //   tvcurrentwalletamt.setText(WalletFragment.balanceamount);

                    one_wallet = "wallet";


                } else {
                    btnproceedtopay.setText("Proceed To Pay" + "\u20B9" + OrderedItemsDetailsForLaundry.totalprice);
                    tvamtaftercalci.setText("0");
                    tvamtaftercalci.setVisibility(View.GONE);
                    tvwalletamount.setVisibility(View.GONE);
                    rg_group.clearCheck();
                    options_onlinepayment = "0";
                    tvcurrentwalletamt.setText(""+balanceamount);
                    Log.d("onlinepaymentselectedvalue", "*** " + options_onlinepayment);

                    rd_cod.setEnabled(true);
                    rd_onlinepayment.setEnabled(true);
                    tvcurrentwalletamt.setVisibility(View.VISIBLE);
                    options = "0";
                    one_wallet = "0";
                    Countforonline = "0";
                }
            }
        });

        rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_cod) {
                    options = "1";
                } else if (checkedId == R.id.radio_onlinepayment) {
                    // options = "3";
                    options = "2";

                    options_onlinepayment = "both";
                }
            }
        });


    }


    private void PostAddressDetails(String paymentmethod){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.show();
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_ordedetails = adapter.create(UohmacAPI.class);


        api_ordedetails.PostDetailsfororder_withoutwallet(sendtotalprice, senddate, sendspinneritem, getauthkey, "2", paymentmethod, new Callback<Response>() {
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
                    mProgressDialog.dismiss();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mProgressDialog.dismiss();
                responsefororderdetails = sb.toString();
                Log.e("getresfororderdetails", responsefororderdetails);

                try {
                    jsonObject = new JSONObject(responsefororderdetails);
                    //jsonObject = jsonArray.optJSONObject(0);
                    msg_respostorderdetails = jsonObject.getString("msg");
                    status_respostorderdetails = jsonObject.getString("status");
                    order_id = jsonObject.getString("order_id");
                    payment_method_forpay  = jsonObject.getString("payment_method");


                    if (status_respostorderdetails.equals("1")) {

                        if (status_respostorderdetails.equals("1") &&payment_method_forpay.equals("1"))
                        {

                            Toast.makeText(SelectingDetails.this, msg_respostorderdetails, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SelectingDetails.this,PaymentSuccess.class);
                            intent.putExtra("txnid",order_id);
                            intent.putExtra("amount",tvfinalamount.getText().toString().trim());
                            startActivity(intent);
                            finish();

                        }/*else if (status_respostorderdetails.equals("1") &&payment_method_forpay.equals("2"))
                        {


                            Toast.makeText(SelectingDetails.this, msg_respostorderdetails, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SelectingDetails.this,PaymentSuccess.class);
                            intent.putExtra("txnid",order_id);
                            intent.putExtra("amount",tvfinalamount.getText().toString().trim());
                            startActivity(intent);
                            finish();

                        }*/else if (status_respostorderdetails.equals("1") &&payment_method_forpay.equals("3"))
                        {

                        dialog = new Dialog(SelectingDetails.this);
                        dialog.setContentView(R.layout.deliveryaddressconfirmpopup);
                        tvdeliveryaddrconfirm = (TextView) dialog.findViewById(R.id.txt_deliveryaddrconfirmation);
                        tvdeliveryname = (TextView) dialog.findViewById(R.id.txt_deliveryname);

                        tvdeliveryaddrconfirm.setText(tookflatno + " " + tookaddress + " " + tookcity + " " + tooklandmark + " " + tookpincode + " " + tooksubarea);
                        tvdeliveryname.setText(HomeActivity.fullname);
                        dialog.show();

                        }else if (status_respostorderdetails.equals("1") &&payment_method_forpay.equals("4"))
                        {

                        dialog = new Dialog(SelectingDetails.this);
                        dialog.setContentView(R.layout.deliveryaddressconfirmpopup);
                        tvdeliveryaddrconfirm = (TextView) dialog.findViewById(R.id.txt_deliveryaddrconfirmation);
                        tvdeliveryname = (TextView) dialog.findViewById(R.id.txt_deliveryname);


                        tvdeliveryaddrconfirm.setText(tookflatno + " " + tookaddress + " " + tookcity + " " + tooklandmark + " " + tookpincode + " " + tooksubarea);
                        tvdeliveryname.setText(HomeActivity.fullname);
                        dialog.show();

                        }


                      /*  dialog = new Dialog(SelectingDetails.this);
                        dialog.setContentView(R.layout.deliveryaddressconfirmpopup);
                        tvdeliveryaddrconfirm = (TextView) dialog.findViewById(R.id.txt_deliveryaddrconfirmation);
                        tvdeliveryname = (TextView) dialog.findViewById(R.id.txt_deliveryname);


                        tvdeliveryaddrconfirm.setText(tookflatno + " " + tookaddress + " " + tookcity + " " + tooklandmark + " " + tookpincode + " " + tooksubarea);
                        tvdeliveryname.setText(HomeActivity.fullname);
                        dialog.show();*/
                    } else {
                        Toast.makeText(SelectingDetails.this, msg_respostorderdetails, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(SelectingDetails.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.d("ERROR:", error.getMessage());
            }
        });
    }




    //posting details only for wallet
    private void PostAddressDetails_Wallet(String wallet,String paymentmethod){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.show();
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_ordedetails = adapter.create(UohmacAPI.class);


        api_ordedetails.PostDetailsfororder_wallet(sendtotalprice, senddate, sendspinneritem, getauthkey, "2", wallet,paymentmethod, new Callback<Response>() {
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
                    mProgressDialog.dismiss();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mProgressDialog.dismiss();
                responsefororderdetails = sb.toString();
                Log.e("getresfororderdetails_wallet", responsefororderdetails);
                try {
                    jsonArray = new JSONArray(responsefororderdetails);
                    jsonObject = jsonArray.optJSONObject(0);
                    msg_respostorderdetails = jsonObject.getString("msg");
                    status_respostorderdetails = jsonObject.getString("status");

                    if (status_respostorderdetails.equals("1"))
                    {


                        if(status_respostorderdetails.equals("1")&&payment_method_forpay.equals("2")) {

                            Toast.makeText(SelectingDetails.this, msg_respostorderdetails, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SelectingDetails.this, PaymentSuccess.class);
                            intent.putExtra("txnid", order_id);
                            intent.putExtra("amount", tvfinalamount.getText().toString().trim());
                            startActivity(intent);
                            finish();

                        }

                    }else {
                        Toast.makeText(SelectingDetails.this, msg_respostorderdetails, Toast.LENGTH_SHORT).show();
                    }


                    /*if (status_respostorderdetails.equals("1")) {
                        Toast.makeText(SelectingDetails.this, msg_respostorderdetails, Toast.LENGTH_SHORT).show();
                        dialog = new Dialog(SelectingDetails.this);
                        dialog.setContentView(R.layout.deliveryaddressconfirmpopup);
                        tvdeliveryaddrconfirm = (TextView) dialog.findViewById(R.id.txt_deliveryaddrconfirmation);
                        tvdeliveryname = (TextView) dialog.findViewById(R.id.txt_deliveryname);


                        tvdeliveryaddrconfirm.setText(tookflatno + " " + tookaddress + " " + tookcity + " " + tooklandmark + " " + tookpincode + " " + tooksubarea);
                        tvdeliveryname.setText(HomeActivity.fullname);
                        dialog.show();
                    }*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(SelectingDetails.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.d("ERROR:", error.getMessage());
            }
        });
    }


    private void getaddress(){
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_postaddress = adapter.create(UohmacAPI.class);

        api_postaddress.GetAddress(getauthkey, new Callback<Response>() {
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

                resforgetaddress = sb.toString();
                Log.e("resforgetaddress", resforgetaddress);
                try {
                    jsonArray = new JSONArray(resforgetaddress);
                    jsonObject = jsonArray.getJSONObject(0);
                    tookflatno = jsonObject.getString("flat_no");
                    tooklandmark = jsonObject.getString("landmark");
                    tookaddress = jsonObject.getString("address");
                    tookcity = jsonObject.getString("city");
                    tookcountry = jsonObject.getString("country");
                    tookstate = jsonObject.getString("state");
                    tookpincode = jsonObject.getString("pincode");

                    tvaddressview.setText(tookflatno + " " + tooklandmark + " " + tookaddress + " " + tookcity + " " + tookcountry + " " + tookstate + " " + tookpincode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(SelectingDetails.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.d("ERROR:", error.getMessage());

            }
        });

    }

        //to get wallet balance amount for payment
        private void checkwalletbalance(){
            final RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint(Constatns.UOHMAC_API)
                    .build();

            UohmacAPI api_checkbalancewallet = adapter.create(UohmacAPI.class);

            api_checkbalancewallet.CheckWalletBalance(getauthkey, new Callback<Response>() {
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
                    Log.d("checkwalletbalancerespose", result);
                    try {
                        jsonObject = new JSONObject(result);
                        status = jsonObject.getString("status");
                        balanceamount = jsonObject.getString("balance");


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    tvwalletamount.setText(balanceamount);
                    tvcurrentwalletamt.setText(""+balanceamount);
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(SelectingDetails.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                    Log.d("ERROR:", error.getMessage());

                }
            });
        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PayuConstants.PAYU_REQUEST_CODE) {
            if (data != null) {

                /**
                 * Here, data.getStringExtra("payu_response") ---> Implicit response sent by PayU
                 * data.getStringExtra("result") ---> Response received from merchant's Surl/Furl
                 *
                 * PayU sends the same response to merchant server and in app. In response check the value of key "status"
                 * for identifying status of transaction. There are two possible status like, success or failure
                 * */
                new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setMessage("Payu's Data : " + data.getStringExtra("payu_response") + "\n\n\n Merchant's Data: " + data.getStringExtra("result"))
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }).show();
                Log.e("Payu's Data :", " " + " " + data.getStringExtra("payu_response") + "\n\n\n Merchant's Data: " + data.getStringExtra("result"));


             merchantresponse = data.getStringExtra("result");
                datas = merchantresponse.split(",");
                 first = datas[0];
                Log.e("first", first);
                 second = datas[1];
                Log.e("second", second);
                 third = datas[2];
                Log.e("third", third);
                if(first.equals("success")){
                    Toast.makeText(SelectingDetails.this, first, Toast.LENGTH_SHORT).show();
                    Intent i_paymentsuccess = new Intent(SelectingDetails.this, PaymentSuccess.class);
                    i_paymentsuccess.putExtra("amount", third);
                    i_paymentsuccess.putExtra("txnid", second);
                    startActivity(i_paymentsuccess);
                }else if(first.equals("failure")){
                    Toast.makeText(SelectingDetails.this, first, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, getString(R.string.could_not_receive_data), Toast.LENGTH_LONG).show();
            }
        }
    }


    /*private boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }*/


 /*   public void PayNow(View view){

        Double amount = 0.0;
        if (isDouble(OrderedItemsDetailsForLaundry.totalprice)) {
            amount = Double.parseDouble(OrderedItemsDetailsForLaundry.totalprice);
        }else{
            Toast.makeText(getApplicationContext(), "Enter correct amount", Toast.LENGTH_LONG).show();
            return;
        }

        if (amount <= 0.0) {
            Toast.makeText(getApplicationContext(), "Enter Some amount", Toast.LENGTH_LONG).show();
        } else if (amount > 2222100.00) {
            Toast.makeText(getApplicationContext(), "Amount exceeding the limit : 2222100.00 ", Toast.LENGTH_LONG).show();
        } else{

            PayUmoneySdkInitilizer.PaymentParam.Builder builder = new PayUmoneySdkInitilizer.PaymentParam.Builder();
            builder.setKey("");
            builder.setSalt("");
            builder.setMerchantId("");


            builder.setIsDebug(true);
            builder.setDebugKey("6YLMa0fi");// Debug Key
            builder.setDebugMerchantId("gtKFFx");// Debug Merchant ID
            builder.setDebugSalt("eCwWELxi");// Debug Salt

            builder.setAmount(amount);
            builder.setTnxId("0nf7" + System.currentTimeMillis());
            builder.setPhone("8882434664");
            builder.setProductName("Water and Laundry");
            builder.setFirstName(tvdeliveryname.getText().toString());
            builder.setEmail(email_forpayment);
            builder.setsUrl("https://test.payumoney.com/mobileapp/payumoney/success.php");
            builder.setfUrl("https://test.payumoney.com/mobileapp/payumoney/failure.php");
            builder.setUdf1("");
            builder.setUdf2("");
            builder.setUdf3("");
            builder.setUdf4("");
            builder.setUdf5("");


            PayUmoneySdkInitilizer.PaymentParam paymentParam = builder.build();


            // Recommended
            calculateServerSideHashAndInitiatePayment(paymentParam);

            String salt = "eCwWELxi";
            String serverCalculatedHash=hashCal("eCwWELxi"+"|"+"0nf7"+"|"+amount+"|"+"Water and Laundry"+"|"
                    +tvdeliveryname.getText().toString()+"|"+email_forpayment+"|"+""+"|"+""+"|"+""+"|"+""+"|"+""+"|"+salt);

            paymentParam.setMerchantHash(serverCalculatedHash);


            PayUmoneySdkInitilizer.startPaymentActivityForResult(this, paymentParam);

        }
    }


    public static String hashCal(String str) {
        byte[] hashseq = str.getBytes();
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();
            for (byte aMessageDigest : messageDigest) {
                String hex = Integer.toHexString(0xFF & aMessageDigest);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException ignored) {
        }
        return hexString.toString();
    }


    private void calculateServerSideHashAndInitiatePayment(final PayUmoneySdkInitilizer.PaymentParam paymentParam) {

        // Replace your server side hash generator API URL
        String url = "https://test.payumoney.com/payment/op/calculateHashForTest";

        Toast.makeText(this, "Please wait... Generating hash from server ... ", Toast.LENGTH_LONG).show();
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.has(SdkConstants.STATUS)) {
                        String status = jsonObject.optString(SdkConstants.STATUS);
                        if (status != null || status.equals("1")) {

                            String hash = jsonObject.getString(SdkConstants.RESULT);
                            Log.i("app_activity", "Server calculated Hash :  " + hash);

                           // paymentParam.setMerchantHash(hash);

                            PayUmoneySdkInitilizer.startPaymentActivityForResult(SelectingDetails.this, paymentParam);
                        } else {
                            Toast.makeText(SelectingDetails.this,
                                    jsonObject.getString(SdkConstants.RESULT),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NoConnectionError) {
                    Toast.makeText(SelectingDetails.this,
                            SelectingDetails.this.getString(R.string.connect_to_internet),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SelectingDetails.this,
                            error.getMessage(),
                            Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return paymentParam.getParams();
            }
        };
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }




    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PayUmoneySdkInitilizer.PAYU_SDK_PAYMENT_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                Log.i(TAG, "Success - Payment ID : " + data.getStringExtra(SdkConstants.PAYMENT_ID));
                String paymentId = data.getStringExtra(SdkConstants.PAYMENT_ID);
                showDialogMessage( "Payment Success Id : " + paymentId);
            }
            else if (resultCode == RESULT_CANCELED) {
                Log.i(TAG, "failure");
                showDialogMessage("cancelled");
            }else if (resultCode == PayUmoneySdkInitilizer.RESULT_FAILED) {
                Log.i("app_activity", "failure");

                if (data != null) {
                    if (data.getStringExtra(SdkConstants.RESULT).equals("cancel")) {

                    } else {
                        showDialogMessage("failure");
                    }
                }
                //Write your code if there's no result
            }

            else if (resultCode == PayUmoneySdkInitilizer.RESULT_BACK) {
                Log.i(TAG, "User returned without login");
                showDialogMessage( "User returned without login");
            }
        }

    }


    private void showDialogMessage(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(TAG);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }

*/


    public void PayNow(View view){

        int environment;

        environment = PayuConstants.STAGING_ENV;

        mPaymentParams = new PaymentParams();


        mPaymentParams.setKey("gtKFFx");
        if(status_respostorderdetails.equals("1")&&payment_method_forpay.equals("4")){
            mPaymentParams.setAmount(tvamtaftercalci.getText().toString());
        }else{
            mPaymentParams.setAmount(tvfinalamount.getText().toString());
        }
        mPaymentParams.setProductInfo("Laundry");
        mPaymentParams.setFirstName(firstname_forpayment);
        mPaymentParams.setEmail(email_forpayment);




 /*
        * Transaction Id should be kept unique for each transaction.
        * */

        mPaymentParams.setTxnId("" + System.currentTimeMillis());

        mPaymentParams.setSurl("http://blessindia.in/water_laundry/index.php/api/payment/pay_success");
        mPaymentParams.setFurl("http://blessindia.in/water_laundry/index.php/api/payment/pay_failure");




/*
         * udf1 to udf5 are options params where you can pass additional information related to transaction.
         * If you don't want to use it, then send them as empty string like, udf1=""
         * */

        mPaymentParams.setUdf1(order_id);
        Log.e("getordeeid...", order_id);
        mPaymentParams.setUdf2(getauthkey);
        mPaymentParams.setUdf3("udf3");
        mPaymentParams.setUdf4("udf4");
        mPaymentParams.setUdf5("udf5");


        mPaymentParams.setUserCredentials(userCredentials);

        //TODO Sets the payment environment in PayuConfig object
        payuConfig = new PayuConfig();
        payuConfig.setEnvironment(environment);

        //TODO It is recommended to generate hash from server only. Keep your key and salt in server side hash generation code.
        generateHashFromServer(mPaymentParams);

    }


    public void generateHashFromSDK(PaymentParams mPaymentParams, String salt) {
        PayuHashes payuHashes = new PayuHashes();
        PostData postData = new PostData();

        // payment Hash;
        checksum = null;
        checksum = new PayUChecksum();
        checksum.setAmount(mPaymentParams.getAmount());
        checksum.setKey(mPaymentParams.getKey());
        checksum.setTxnid(mPaymentParams.getTxnId());
        checksum.setEmail(mPaymentParams.getEmail());
        checksum.setSalt(salt);
        checksum.setProductinfo(mPaymentParams.getProductInfo());
        checksum.setFirstname(mPaymentParams.getFirstName());
        checksum.setUdf1(mPaymentParams.getUdf1());
        checksum.setUdf2(mPaymentParams.getUdf2());
        checksum.setUdf3(mPaymentParams.getUdf3());
        checksum.setUdf4(mPaymentParams.getUdf4());
        checksum.setUdf5(mPaymentParams.getUdf5());

        postData = checksum.getHash();
        if (postData.getCode() == PayuErrors.NO_ERROR) {
            payuHashes.setPaymentHash(postData.getResult());
        }

        // checksum for payemnt related details
        // var1 should be either user credentials or default
        String var1 = mPaymentParams.getUserCredentials() == null ? PayuConstants.DEFAULT : mPaymentParams.getUserCredentials();
        String key = mPaymentParams.getKey();

        if ((postData = calculateHash(key, PayuConstants.PAYMENT_RELATED_DETAILS_FOR_MOBILE_SDK, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR) // Assign post data first then check for success
            payuHashes.setPaymentRelatedDetailsForMobileSdkHash(postData.getResult());
        //vas
        if ((postData = calculateHash(key, PayuConstants.VAS_FOR_MOBILE_SDK, PayuConstants.DEFAULT, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
            payuHashes.setVasForMobileSdkHash(postData.getResult());

        // getIbibocodes
        if ((postData = calculateHash(key, PayuConstants.GET_MERCHANT_IBIBO_CODES, PayuConstants.DEFAULT, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
            payuHashes.setMerchantIbiboCodesHash(postData.getResult());

        if (!var1.contentEquals(PayuConstants.DEFAULT)) {
            // get user card
            if ((postData = calculateHash(key, PayuConstants.GET_USER_CARDS, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR) // todo rename storedc ard
                payuHashes.setStoredCardsHash(postData.getResult());
            // save user card
            if ((postData = calculateHash(key, PayuConstants.SAVE_USER_CARD, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
                payuHashes.setSaveCardHash(postData.getResult());
            // delete user card
            if ((postData = calculateHash(key, PayuConstants.DELETE_USER_CARD, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
                payuHashes.setDeleteCardHash(postData.getResult());
            // edit user card
            if ((postData = calculateHash(key, PayuConstants.EDIT_USER_CARD, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
                payuHashes.setEditCardHash(postData.getResult());
        }

        if (mPaymentParams.getOfferKey() != null) {
            postData = calculateHash(key, PayuConstants.OFFER_KEY, mPaymentParams.getOfferKey(), salt);
            if (postData.getCode() == PayuErrors.NO_ERROR) {
                payuHashes.setCheckOfferStatusHash(postData.getResult());
            }
        }

        if (mPaymentParams.getOfferKey() != null && (postData = calculateHash(key, PayuConstants.CHECK_OFFER_STATUS, mPaymentParams.getOfferKey(), salt)) != null && postData.getCode() == PayuErrors.NO_ERROR) {
            payuHashes.setCheckOfferStatusHash(postData.getResult());
        }

        // we have generated all the hases now lest launch sdk's ui
        launchSdkUI(payuHashes);
    }

    // deprecated, should be used only for testing.
    private PostData calculateHash(String key, String command, String var1, String salt) {
        checksum = null;
        checksum = new PayUChecksum();
        checksum.setKey(key);
        checksum.setCommand(command);
        checksum.setVar1(var1);
        checksum.setSalt(salt);
        return checksum.getHash();
    }


    public void generateHashFromServer(PaymentParams mPaymentParams) {
        //nextButton.setEnabled(false); // lets not allow the user to click the button again and again.

        // lets create the post params
        StringBuffer postParamsBuffer = new StringBuffer();
        postParamsBuffer.append(concatParams(PayuConstants.KEY, mPaymentParams.getKey()));
        postParamsBuffer.append(concatParams(PayuConstants.AMOUNT, mPaymentParams.getAmount()));
        postParamsBuffer.append(concatParams(PayuConstants.TXNID, mPaymentParams.getTxnId()));
        postParamsBuffer.append(concatParams(PayuConstants.EMAIL, null == mPaymentParams.getEmail() ? "" : mPaymentParams.getEmail()));
        postParamsBuffer.append(concatParams(PayuConstants.PRODUCT_INFO, mPaymentParams.getProductInfo()));
        postParamsBuffer.append(concatParams(PayuConstants.FIRST_NAME, null == mPaymentParams.getFirstName() ? "" : mPaymentParams.getFirstName()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF1, mPaymentParams.getUdf1() == null ? "" : mPaymentParams.getUdf1()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF2, mPaymentParams.getUdf2() == null ? "" : mPaymentParams.getUdf2()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF3, mPaymentParams.getUdf3() == null ? "" : mPaymentParams.getUdf3()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF4, mPaymentParams.getUdf4() == null ? "" : mPaymentParams.getUdf4()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF5, mPaymentParams.getUdf5() == null ? "" : mPaymentParams.getUdf5()));
        postParamsBuffer.append(concatParams(PayuConstants.USER_CREDENTIALS, mPaymentParams.getUserCredentials() == null ? PayuConstants.DEFAULT : mPaymentParams.getUserCredentials()));

        // for offer_key
        if (null != mPaymentParams.getOfferKey())
            postParamsBuffer.append(concatParams(PayuConstants.OFFER_KEY, mPaymentParams.getOfferKey()));

        String postParams = postParamsBuffer.charAt(postParamsBuffer.length() - 1) == '&' ? postParamsBuffer.substring(0, postParamsBuffer.length() - 1).toString() : postParamsBuffer.toString();

        // lets make an api call
        GetHashesFromServerTask getHashesFromServerTask = new GetHashesFromServerTask();
        getHashesFromServerTask.execute(postParams);
    }


    protected String concatParams(String key, String value) {
        return key + "=" + value + "&";
    }



    private class GetHashesFromServerTask extends AsyncTask<String, String, PayuHashes> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SelectingDetails.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @Override
        protected PayuHashes doInBackground(String... postParams) {
            PayuHashes payuHashes = new PayuHashes();
            try {

                //TODO Below url is just for testing purpose, merchant needs to replace this with their server side hash generation url
                URL url = new URL("https://payu.herokuapp.com/get_hash");

                // get the payuConfig first
                String postParam = postParams[0];

                byte[] postParamsByte = postParam.getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                conn.setDoOutput(true);
                conn.getOutputStream().write(postParamsByte);

                InputStream responseInputStream = conn.getInputStream();
                StringBuffer responseStringBuffer = new StringBuffer();
                byte[] byteContainer = new byte[1024];
                for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                    responseStringBuffer.append(new String(byteContainer, 0, i));
                }

                JSONObject response = new JSONObject(responseStringBuffer.toString());

                Iterator<String> payuHashIterator = response.keys();
                while (payuHashIterator.hasNext()) {
                    String key = payuHashIterator.next();
                    switch (key) {
                        //TODO Below three hashes are mandatory for payment flow and needs to be generated at merchant server
                        /**
                         * Payment hash is one of the mandatory hashes that needs to be generated from merchant's server side
                         * Below is formula for generating payment_hash -
                         *
                         * sha512(key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5||||||SALT)
                         *
                         */

                        case "payment_hash":
                            payuHashes.setPaymentHash(response.getString(key));
                            break;


                        /**
                         * vas_for_mobile_sdk_hash is one of the mandatory hashes that needs to be generated from merchant's server side
                         * Below is formula for generating vas_for_mobile_sdk_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be "default"
                         *
                         */

                        case "vas_for_mobile_sdk_hash":
                            payuHashes.setVasForMobileSdkHash(response.getString(key));
                            break;

                        /**
                         * payment_related_details_for_mobile_sdk_hash is one of the mandatory hashes that needs to be generated from merchant's server side
                         * Below is formula for generating payment_related_details_for_mobile_sdk_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         *
                         */

                        case "payment_related_details_for_mobile_sdk_hash":
                            payuHashes.setPaymentRelatedDetailsForMobileSdkHash(response.getString(key));
                            break;

                        //TODO Below hashes only needs to be generated if you are using Store card feature

/**
 * delete_user_card_hash is used while deleting a stored card.
 * Below is formula for generating delete_user_card_hash -
 *
 * sha512(key|command|var1|salt)
 *
 * here, var1 will be user credentials. If you are not using user_credentials then use "default"
 *
 */

                        case "delete_user_card_hash":
                            payuHashes.setDeleteCardHash(response.getString(key));
                            break;

/**
 * get_user_cards_hash is used while fetching all the cards corresponding to a user.
 * Below is formula for generating get_user_cards_hash -
 *
 * sha512(key|command|var1|salt)
 *
 * here, var1 will be user credentials. If you are not using user_credentials then use "default"
 *
 */

                        case "get_user_cards_hash":
                            payuHashes.setStoredCardsHash(response.getString(key));
                            break;

                        /**
                         * edit_user_card_hash is used while editing details of existing stored card.
                         * Below is formula for generating edit_user_card_hash -
                         *
                         * sha512(key|command|var1|salt)
                         *
                         * here, var1 will be user credentials. If you are not using user_credentials then use "default"
                         *
                         */

                        case "edit_user_card_hash":
                            payuHashes.setEditCardHash(response.getString(key));
                            break;

/**
 * save_user_card_hash is used while saving card to the vault
 * Below is formula for generating save_user_card_hash -
 *
 * sha512(key|command|var1|salt)
 *
 * here, var1 will be user credentials. If you are not using user_credentials then use "default"
 *
 */

                        case "save_user_card_hash":
                            payuHashes.setSaveCardHash(response.getString(key));
                            break;

                        //TODO This hash needs to be generated if you are using any offer key

/**
 * check_offer_status_hash is used while using check_offer_status api
 * Below is formula for generating check_offer_status_hash -
 *
 * sha512(key|command|var1|salt)
 *
 * here, var1 will be Offer Key.
 *
 */

                        case "check_offer_status_hash":
                            payuHashes.setCheckOfferStatusHash(response.getString(key));
                            break;
                        default:
                            break;
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return payuHashes;
        }

        @Override
        protected void onPostExecute(PayuHashes payuHashes) {
            super.onPostExecute(payuHashes);

            progressDialog.dismiss();
            launchSdkUI(payuHashes);
        }
    }


    public void launchSdkUI(PayuHashes payuHashes) {

        Intent intent = new Intent(this, PayUBaseActivity.class);
        intent.putExtra(PayuConstants.PAYU_CONFIG, payuConfig);
        intent.putExtra(PayuConstants.PAYMENT_PARAMS, mPaymentParams);
        intent.putExtra(PayuConstants.PAYU_HASHES, payuHashes);

        //Lets fetch all the one click card tokens first
        fetchMerchantHashes(intent);

    }
//TODO This method is used if integrating One Tap Payments

    /**
     * This method stores merchantHash and cardToken on merchant server.
     *
     * @param cardToken    card token received in transaction response
     * @param merchantHash merchantHash received in transaction response
     */

    private void storeMerchantHash(String cardToken, String merchantHash) {

        final String postParams = "merchant_key=" + merchantKey + "&user_credentials=" + userCredentials + "&card_token=" + cardToken + "&merchant_hash=" + merchantHash;

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {

                    //TODO Deploy a file on your server for storing cardToken and merchantHash nad replace below url with your server side file url.
                    URL url = new URL("https://payu.herokuapp.com/store_merchant_hash");

                    byte[] postParamsByte = postParams.getBytes("UTF-8");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                    conn.setDoOutput(true);
                    conn.getOutputStream().write(postParamsByte);

                    InputStream responseInputStream = conn.getInputStream();
                    StringBuffer responseStringBuffer = new StringBuffer();
                    byte[] byteContainer = new byte[1024];
                    for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                        responseStringBuffer.append(new String(byteContainer, 0, i));
                    }

                    JSONObject response = new JSONObject(responseStringBuffer.toString());
                    Log.e("paymentresponse", ""+response);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                this.cancel(true);
            }
        }.execute();
    }


    private void fetchMerchantHashes(final Intent intent) {
        // now make the api call.
        final String postParams = "merchant_key=" + merchantKey + "&user_credentials=" + userCredentials;
        final Intent baseActivityIntent = intent;
        new AsyncTask<Void, Void, HashMap<String, String>>() {

            @Override
            protected HashMap<String, String> doInBackground(Void... params) {
                try {
                    //TODO Replace below url with your server side file url.
                    URL url = new URL("https://payu.herokuapp.com/get_merchant_hashes");

                    byte[] postParamsByte = postParams.getBytes("UTF-8");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                    conn.setDoOutput(true);
                    conn.getOutputStream().write(postParamsByte);

                    InputStream responseInputStream = conn.getInputStream();
                    StringBuffer responseStringBuffer = new StringBuffer();
                    byte[] byteContainer = new byte[1024];
                    for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                        responseStringBuffer.append(new String(byteContainer, 0, i));
                    }

                    JSONObject response = new JSONObject(responseStringBuffer.toString());

                    HashMap<String, String> cardTokens = new HashMap<String, String>();
                    JSONArray oneClickCardsArray = response.getJSONArray("data");
                    int arrayLength;
                    if ((arrayLength = oneClickCardsArray.length()) >= 1) {
                        for (int i = 0; i < arrayLength; i++) {
                            cardTokens.put(oneClickCardsArray.getJSONArray(i).getString(0), oneClickCardsArray.getJSONArray(i).getString(1));
                        }
                        return cardTokens;
                    }
                    // pass these to next activity

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(HashMap<String, String> oneClickTokens) {
                super.onPostExecute(oneClickTokens);

                baseActivityIntent.putExtra(PayuConstants.ONE_CLICK_CARD_TOKENS, oneClickTokens);
                startActivityForResult(baseActivityIntent, PayuConstants.PAYU_REQUEST_CODE);
            }
        }.execute();
    }

    private void deleteMerchantHash(String cardToken) {

        final String postParams = "card_token=" + cardToken;

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    //TODO Replace below url with your server side file url.
                    URL url = new URL("https://payu.herokuapp.com/delete_merchant_hash");

                    byte[] postParamsByte = postParams.getBytes("UTF-8");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                    conn.setDoOutput(true);
                    conn.getOutputStream().write(postParamsByte);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                this.cancel(true);
            }
        }.execute();
    }


    public HashMap<String, String> getAllOneClickHashHelper(String merchantKey, String userCredentials) {

        // now make the api call.
        final String postParams = "merchant_key=" + merchantKey + "&user_credentials=" + userCredentials;
        HashMap<String, String> cardTokens = new HashMap<String, String>();

        try {
            //TODO Replace below url with your server side file url.
            URL url = new URL("https://payu.herokuapp.com/get_merchant_hashes");

            byte[] postParamsByte = postParams.getBytes("UTF-8");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postParamsByte);

            InputStream responseInputStream = conn.getInputStream();
            StringBuffer responseStringBuffer = new StringBuffer();
            byte[] byteContainer = new byte[1024];
            for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                responseStringBuffer.append(new String(byteContainer, 0, i));
            }

            JSONObject response = new JSONObject(responseStringBuffer.toString());
                Log.e("getoneclickresponse", ""+response);
            JSONArray oneClickCardsArray = response.getJSONArray("data");
            int arrayLength;
            if ((arrayLength = oneClickCardsArray.length()) >= 1) {
                for (int i = 0; i < arrayLength; i++) {
                    cardTokens.put(oneClickCardsArray.getJSONArray(i).getString(0), oneClickCardsArray.getJSONArray(i).getString(1));
                }

            }
            // pass these to next activity

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cardTokens;
    }

    @Override
    public HashMap<String, String> getAllOneClickHash(String userCreds) {
        // 1. GET http request from your server
        // GET params - merchant_key, user_credentials.
        // 2. In response we get a
        // this is a sample code for fetching one click hash from merchant server.
        return getAllOneClickHashHelper(merchantKey, userCreds);
    }

    @Override
    public void getOneClickHash(String cardToken, String merchantKey, String userCredentials) {

    }


    @Override
    public void saveOneClickHash(String cardToken, String oneClickHash) {
        // 1. POST http request to your server
        // POST params - merchant_key, user_credentials,card_token,merchant_hash.
        // 2. In this POST method the oneclickhash is stored corresponding to card token in merchant server.
        // this is a sample code for storing one click hash on merchant server.

        storeMerchantHash(cardToken, oneClickHash);

    }


    @Override
    public void deleteOneClickHash(String cardToken, String userCredentials) {

        // 1. POST http request to your server
        // POST params  - merchant_hash.
        // 2. In this POST method the oneclickhash is deleted in merchant server.
        // this is a sample code for deleting one click hash from merchant server.

        deleteMerchantHash(cardToken);

    }



   /* private void Addpaymenthistory(){
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_paymenthistory = adapter.create(UohmacAPI.class);

        api_paymenthistory.AddPaymentHistory(getauthkey,);
    }*/


}

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
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.RadioButton;
        import android.widget.RadioGroup;
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
        import com.uohmac.com.drgarmentandwater.R;
        import com.uohmac.com.drgarmentandwater.Utils.AppNetworkInfo;
        import com.uohmac.com.drgarmentandwater.Utils.Constatns;

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
 * Created by uOhmac Technologies on 21-Mar-17.
 */
public class DeliveryAddrDetailsforWater extends AppCompatActivity implements OneClickPaymentListener {

    TextView txt_editaddress_water,tvgetaddress,tvtotalprice,tvclickpromocode,tvpromocodelist,tvaddnewaddrforwater,txtnameforaddr,txtfinalamount_water,txtshowwalletamt_water,txtcurrentwalletamt_water,txtamtaftercalc_water,tvdeliveryaddrconfirm,tvdeliveryname;
    ProgressDialog mProgressDialog;
    String getauthkey,resforgetaddress,getflatno,getlandmark,getaddress,getcity,getcountry,getstate,getpincode,status,response_promocode,msg_promocode,promobenefit,name_foraddr,responsefororderdetails,mobilenum_forpayment;
    String msg_respostorderdetails,status_respostorderdetails,order_id,email_forpayment,firstname_forpayment,userCredentials,merchantKey;
    SharedPreferences sp;
    String options="0", options_onlinepayment="0",one_wallet="0",merchantresponse="",payment_method_forpay="";
    JSONObject jsonObject;
    JSONArray jsonArray;
    Dialog dialog_applypromowater;
    Dialog dialog;
    EditText et_promotitleforwater;
    CheckBox ch_checkwallet;
    Button btn_applypromocode,btnwaterplaceorder;
    RadioGroup radiogroup_paymentmodes_water;
    RadioButton rb_cod_water,rb_online_water;
    String Countforonline = "0";
    ImageView img_closedialog;
    private PaymentParams mPaymentParams;
    String result,balanceamount="0";
            // This sets the configuration
            private PayuConfig payuConfig;
            // Used when generating hash from SDK
            private PayUChecksum checksum;
    String[] datas;
    String first="",second="",third="";
    String flatno_water="",addr1_water="",city_water="",landmark_water="",pincode_water="",country_water="";
    String cb_checkedorno="no";
    public static final String MYPREFERNCES_LOGIN = "mysharedpref_waterlaundry";
    int value_subtoal;
    //static String delivery_type ="";
    public static final String PREFERENCES_SAVEADDRESS = "save_deliveryaddress";
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.watersave_addrforpayment);

                //TODO Must write this code if integrating One Tap payments
                OnetapCallback.setOneTapCallback(this);

                //TODO Must write below code in your activity to set up initial context for PayU
                Payu.setInstance(this);
                tvgetaddress = (TextView)findViewById(R.id.txtaddrview);
                tvtotalprice = (TextView)findViewById(R.id.tv_totalprice);
                tvclickpromocode = (TextView)findViewById(R.id.txt_clickpromocode);
                tvpromocodelist = (TextView)findViewById(R.id.txt_promocodelist);
                tvaddnewaddrforwater = (TextView)findViewById(R.id.addnewaddrforwater);
                btnwaterplaceorder = (Button)findViewById(R.id.btn_waterplaceorder);
                txtnameforaddr = (TextView)findViewById(R.id.txtnameforaddr);
                ch_checkwallet = (CheckBox)findViewById(R.id.cb_wallet_water);
                txtfinalamount_water = (TextView)findViewById(R.id.txtfinalamount_water);
                txtshowwalletamt_water = (TextView)findViewById(R.id.txtshowwalletamt_water);
                txtcurrentwalletamt_water = (TextView)findViewById(R.id.txtcurrentwalletamt_water);
                txtamtaftercalc_water = (TextView)findViewById(R.id.txtamtaftercalc_water);
                txt_editaddress_water = (TextView)findViewById(R.id.txt_editaddress_water);
                radiogroup_paymentmodes_water = (RadioGroup)findViewById(R.id.radiogroup_paymentmodes_water);
                rb_cod_water = (RadioButton)findViewById(R.id.radio_cod_water);
                rb_online_water = (RadioButton)findViewById(R.id.radio_onlinepayment_water);
                sp = getSharedPreferences(MYPREFERNCES_LOGIN, 0);
                getauthkey = sp.getString("auth_key", "");

                mobilenum_forpayment = sp.getString("mobile", "");
                email_forpayment = sp.getString("email", "");
                // email_forpayment = sp.getString("user_name", "");
                firstname_forpayment = sp.getString("firstName", "");
                tvtotalprice.setText("\u20B9" +WaterItemDetails.totalprice);
                sp = getSharedPreferences(PREFERENCES_SAVEADDRESS, 0);
                name_foraddr = sp.getString("namesfor_addr", "");
                txtnameforaddr.setText(name_foraddr);
                flatno_water = sp.getString("flatno_water","");
                addr1_water = sp.getString("addr1_water", "");
                city_water = sp.getString("city_water","");
                landmark_water = sp.getString("landmark_water","");
                pincode_water = sp.getString("pincode_water","");
                country_water = sp.getString("country_water","");

                txtfinalamount_water.setText(OrderedItemsDetailsForWater.totalprice);


                btnwaterplaceorder.setText("Proceed To Pay" +"\u20B9" + OrderedItemsDetailsForWater.totalprice);

               /* //getting the value of selected del_type
                if(WaterItemDetails.sp_deliverytypes.getSelectedItem().equals("Normal Delivery")){
                    delivery_type = "1";
                }else if(WaterItemDetails.sp_deliverytypes.getSelectedItem().equals("Fast Delivery")){
                    delivery_type = "2";
                }*/

                if(AppNetworkInfo.isConnectingToInternet(DeliveryAddrDetailsforWater.this)){
                    GetAddressForDelivery();
                    checkwalletbalance();
                }else{
                    Toast.makeText(DeliveryAddrDetailsforWater.this, "No Network Found.. Plese try again later.!!!", Toast.LENGTH_LONG).show();
                }


                // lets tell the people what version of sdk we are using
                PayUSdkDetails payUSdkDetails = new PayUSdkDetails();

                Toast.makeText(this, "Build No: " + payUSdkDetails.getSdkBuildNumber() + "\n Build Type: " + payUSdkDetails.getSdkBuildType() + " \n Build Flavor: " + payUSdkDetails.getSdkFlavor() + "\n Application Id: " + payUSdkDetails.getSdkApplicationId() + "\n Version Code: " + payUSdkDetails.getSdkVersionCode() + "\n Version Name: " + payUSdkDetails.getSdkVersionName(), Toast.LENGTH_LONG).show();




                ch_checkwallet.setVisibility(View.VISIBLE);
                txtcurrentwalletamt_water.setVisibility(View.VISIBLE);
                txtcurrentwalletamt_water.setVisibility(View.VISIBLE);
                txtamtaftercalc_water.setVisibility(View.VISIBLE);


                tvclickpromocode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_applypromowater = new Dialog(DeliveryAddrDetailsforWater.this);
                        dialog_applypromowater.setContentView(R.layout.dialog_promocodeforwater);
                        et_promotitleforwater = (EditText)dialog_applypromowater.findViewById(R.id.edit_promocodeforwater);
                        btn_applypromocode = (Button)dialog_applypromowater.findViewById(R.id.btn_promocdeforwater);
                        img_closedialog = (ImageView)dialog_applypromowater.findViewById(R.id.img_closepromocodepopupwater);

                        dialog_applypromowater.show();


                        btn_applypromocode.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (et_promotitleforwater.getText().toString().equals("")) {
                                    et_promotitleforwater.setError(getString(R.string.error_field_required));
                                    et_promotitleforwater.requestFocus();
                                } else if (AppNetworkInfo.isConnectingToInternet(DeliveryAddrDetailsforWater.this)) {
                                    Promocodeapply();
                                } else {
                                    Toast.makeText(DeliveryAddrDetailsforWater.this, "No network found.. Please try again later..!!!", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                        img_closedialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog_applypromowater.dismiss();
                            }
                        });
                    }
                });


                tvpromocodelist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i_promo = new Intent(DeliveryAddrDetailsforWater.this, PromoCodesListforWater.class);
                        startActivity(i_promo);
                    }
                });

                tvaddnewaddrforwater.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i_addnewaddr = new Intent(DeliveryAddrDetailsforWater.this, Getaddressforwater.class);
                        /*i_addnewaddr.putExtra("editflatno_water", "");
                        i_addnewaddr.putExtra("editaddress_water", "");
                        i_addnewaddr.putExtra("editecity_water", "");
                        i_addnewaddr.putExtra("editlandmark_water", "");
                        i_addnewaddr.putExtra("editpincode_water", "");
                        i_addnewaddr.putExtra("editcountry_water", "");*/
                        startActivity(i_addnewaddr);
                    }
                });


                btnwaterplaceorder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(AppNetworkInfo.isConnectingToInternet(DeliveryAddrDetailsforWater.this)){

                            Log.d("valueofoption","****  "+options+"    *****    "+options_onlinepayment + "  "+one_wallet);


                            if(options.equals("1")) {
                                Toast.makeText(DeliveryAddrDetailsforWater.this, "cod selected",Toast.LENGTH_SHORT).show();
                                PostAddressDetails_water_payment("1",WaterItemDetails.delivery_type);
                                Log.e("pay from cod====","Paying from cod" );

                                                            //   PostAddressDetails(options, WalletFragment.balanceamount);

                            } else if (one_wallet.equals("wallet"))
                            {
                                if (txtcurrentwalletamt_water.getText().toString().equals("0"))
                                {
                                    if (options_onlinepayment.equals("both"))
                                    {
                                        PostAddressDetails_water_payment("4",WaterItemDetails.delivery_type);
                                        //  PostAddressDetails(options, WalletFragment.balanceamount);
                                        Toast.makeText(DeliveryAddrDetailsforWater.this, "Both selected wallet and online",Toast.LENGTH_SHORT).show();
                                        Log.e("pay from online====", "Paying from online");
                                    }else
                                    {

                                        // PostAddressDetails(options, WalletFragment.balanceamount);

                                        if (Countforonline.equals("1"))
                                        {
                                            //PostAddressDetails_water_payment("2");
                                            PostAddressDetails_water_wallet(txtfinalamount_water.getText().toString(),"2",WaterItemDetails.delivery_type);
                                            Toast.makeText(DeliveryAddrDetailsforWater.this, "Wallet only selected",Toast.LENGTH_SHORT).show();
                                            Log.e("pay from wallet====", "Paying from wallet");

                                        }else
                                        {
                                            //PostAddressDetails_water_payment("3");
                                            Toast.makeText(DeliveryAddrDetailsforWater.this, "Please Select Online payment",Toast.LENGTH_SHORT).show();
                                            Log.e("pay from online====", "Paying from online");
                                        }

                                    }

                                }else
                                {
                                    //PostAddressDetails_water_payment("2");
                                    PostAddressDetails_water_wallet(txtfinalamount_water.getText().toString(),"2",WaterItemDetails.delivery_type);
                                    Toast.makeText(DeliveryAddrDetailsforWater.this, "Wallet only selected",Toast.LENGTH_SHORT).show();
                                    Log.e("pay from wallet====", "Paying from wallet");

                                }


                            }else if (options_onlinepayment.equals("both"))
                            {
                                // PostAddressDetails(options, WalletFragment.balanceamount);
                                PostAddressDetails_water_payment("3",WaterItemDetails.delivery_type);
                                Toast.makeText(DeliveryAddrDetailsforWater.this, "Online payment selected",Toast.LENGTH_SHORT).show();
                                Log.e("pay from online====", "Paying from online");
                            }else
                            {

                                Toast.makeText(DeliveryAddrDetailsforWater.this, "Please select payment mode",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(DeliveryAddrDetailsforWater.this, "No network found.Please try again.!!",Toast.LENGTH_LONG).show();
                        }
                    }
                });


                ch_checkwallet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            cb_checkedorno = "yes";
                            Countforonline = "0";
                            radiogroup_paymentmodes_water.clearCheck();
                            txtcurrentwalletamt_water.setVisibility(View.VISIBLE);
                            txtshowwalletamt_water.setText("-" + balanceamount);
                            value_subtoal = (Integer.parseInt(txtfinalamount_water.getText().toString()) - Integer.parseInt(balanceamount));

                            txtcurrentwalletamt_water.setText( ""+value_subtoal);

                            btnwaterplaceorder.setText("Proceed To Pay" + "\u20B9" + value_subtoal);
                            // sendtotalprice=""+value_subtoal;
                            options = "0";
                            txtamtaftercalc_water.setVisibility(View.VISIBLE);
                            rb_cod_water.setEnabled(false);

                            if ((Integer.parseInt(balanceamount) <= Integer.parseInt(txtfinalamount_water.getText().toString()))) {
                                           /*else{*/
                                //     Toast.makeText(SelectingDetails.this, "wallet only selected",Toast.LENGTH_SHORT).show();
                                //  rd_onlinepayment.setVisibility(View.GONE);
                                rb_online_water.setEnabled(true);
                                rb_cod_water.setEnabled(false);
                                options_onlinepayment = "0";
                                txtcurrentwalletamt_water.setVisibility(View.VISIBLE);
                                txtcurrentwalletamt_water.setText(""+0);

                                options = "0";
                                if (balanceamount.equals(txtfinalamount_water.getText().toString())) {
                                    rb_online_water.setEnabled(false);

                                    Countforonline = "1";

                                }/*else
                                {

                                }*/

                                txtamtaftercalc_water.setVisibility(View.VISIBLE);
                                txtshowwalletamt_water.setVisibility(View.VISIBLE);
                                txtamtaftercalc_water.setText("" + value_subtoal);
                                // PostAddressDetails(options,tvamtaftercalci.getText().toString());
                                // }
                            } else {
                                options = "0";
                                options_onlinepayment = "0";
                               /* tvamtaftercalci.setVisibility(View.VISIBLE);
                                tvamtaftercalci.setText("" + value_subtoal);*/
                                /*tvcurrentwalletamt.setVisibility(View.VISIBLE);*/
                                txtshowwalletamt_water.setVisibility(View.VISIBLE);
                                txtcurrentwalletamt_water.setVisibility(View.VISIBLE);
                                int walletamount = (Integer.parseInt(balanceamount) - Integer.parseInt(txtfinalamount_water.getText().toString()));
                                txtcurrentwalletamt_water.setText(""+walletamount);
                                btnwaterplaceorder.setText("Proceed To Pay");
                                txtamtaftercalc_water.setVisibility(View.GONE);
                                rb_online_water.setEnabled(false);
                                rb_cod_water.setEnabled(false);
                                //     Toast.makeText(SelectingDetails.this, "Please choose online payment", Toast.LENGTH_SHORT).show();

                                    /*  if(options_onlinepayment.equals("0")){*/
                                             /*}*/
                            }


                            //   tvcurrentwalletamt.setText(WalletFragment.balanceamount);

                            one_wallet = "wallet";


                        } else {
                            cb_checkedorno="no";
                            btnwaterplaceorder.setText("Proceed To Pay" + "\u20B9" + OrderedItemsDetailsForWater.totalprice);
                            txtamtaftercalc_water.setText("0");
                            txtamtaftercalc_water.setVisibility(View.GONE);
                            txtshowwalletamt_water.setVisibility(View.GONE);
                            radiogroup_paymentmodes_water.clearCheck();
                            options_onlinepayment = "0";
                            txtcurrentwalletamt_water.setText(""+balanceamount);
                            Log.d("onlinepaymentselectedvalue", "*** " + options_onlinepayment);

                            rb_cod_water.setEnabled(true);
                            rb_online_water.setEnabled(true);
                            txtcurrentwalletamt_water.setVisibility(View.VISIBLE);
                            options = "0";
                            one_wallet = "0";
                            Countforonline = "0";
                        }
                    }
                });





                radiogroup_paymentmodes_water.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (checkedId == R.id.radio_cod_water) {
                            options = "1";
                        } else if (checkedId == R.id.radio_onlinepayment_water) {
                            // options = "3";
                            options = "2";

                            options_onlinepayment = "both";
                        }
                    }
                });



                //editing the present address
                txt_editaddress_water.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i_editaddress = new Intent(DeliveryAddrDetailsforWater.this,Getaddressforwater.class);
                        i_editaddress.putExtra("editflatno_water", flatno_water);
                        i_editaddress.putExtra("editaddress_water", addr1_water);
                        i_editaddress.putExtra("editecity_water", city_water);
                        i_editaddress.putExtra("editcountry_water", country_water);
                        i_editaddress.putExtra("editlandmark_water", landmark_water);
                        i_editaddress.putExtra("editpincode_water", pincode_water);
                        startActivity(i_editaddress);

                    }
                });
            }


            private void GetAddressForDelivery(){
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Please Wait...");
                mProgressDialog.show();
                final RestAdapter adapter = new RestAdapter.Builder()
                        .setEndpoint(Constatns.UOHMAC_API)
                        .build();

                UohmacAPI api_getaddress = adapter.create(UohmacAPI.class);

                api_getaddress.GetAddress(getauthkey, new Callback<Response>() {
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
                        resforgetaddress = sb.toString();
                        Log.e("resforgetaddress", resforgetaddress);
                        try {
                            jsonArray = new JSONArray(resforgetaddress);
                            jsonObject = jsonArray.getJSONObject(0);
                            getflatno = jsonObject.getString("flat_no");
                            getlandmark = jsonObject.getString("landmark");
                            getaddress = jsonObject.getString("address");
                            getcity = jsonObject.getString("city");
                            getcountry = jsonObject.getString("country");
                            getstate = jsonObject.getString("state");
                            getpincode = jsonObject.getString("pincode");
                            tvgetaddress.setText(getflatno + " " + getlandmark + " " + getaddress + " " + getcity + " " + getcountry + " " + getstate + " " + getpincode);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(DeliveryAddrDetailsforWater.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                        Log.d("ERROR:", error.getMessage());
                    }
                });
            }

            private void Promocodeapply(){
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Please Wait...");
                mProgressDialog.show();

                final RestAdapter adapter = new RestAdapter.Builder()
                        .setEndpoint(Constatns.UOHMAC_API)
                        .build();

                UohmacAPI api_promocode= adapter.create(UohmacAPI.class);

                api_promocode.PromoCode(getauthkey, et_promotitleforwater.getText().toString(), WaterItemDetails.totalprice, new Callback<Response>() {
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
                        response_promocode = sb.toString();
                        Log.e("getre_promocodeforwater", response_promocode);

                        try {
                            jsonObject = new JSONObject(response_promocode);
                            status = jsonObject.getString("status");
                            msg_promocode = jsonObject.getString("msg");
                            promobenefit = jsonObject.getString("promo_benefit");
                            if (status.equals("1")) {

                                Toast.makeText(DeliveryAddrDetailsforWater.this, msg_promocode, Toast.LENGTH_SHORT).show();
                                dialog_applypromowater.dismiss();
                                int totalpriceaftercoupan = Integer.parseInt(tvtotalprice.getText().toString()) - Integer.parseInt(promobenefit);
                                tvtotalprice.setText(totalpriceaftercoupan);
                            } else if (status.equals("0")) {
                                Toast.makeText(DeliveryAddrDetailsforWater.this, msg_promocode, Toast.LENGTH_SHORT).show();
                                dialog_applypromowater.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        mProgressDialog.dismiss();

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(DeliveryAddrDetailsforWater.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                        Log.e("Errorfound", "ERROR:" + error.getMessage(), error);

                    }
                });
            }


            //posting order details for water to pay only with wallet
            private void PostAddressDetails_water_wallet(String wallet,String paymentmethod,String del_type){
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Please Wait...");
                mProgressDialog.show();
                final RestAdapter adapter = new RestAdapter.Builder()
                        .setEndpoint(Constatns.UOHMAC_API)
                        .build();

                UohmacAPI api_ordedetails = adapter.create(UohmacAPI.class);


                api_ordedetails.PostDetailsfororder_waterwallet(OrderedItemsDetailsForWater.totalprice, getauthkey, "1", wallet,paymentmethod,del_type, new Callback<Response>() {
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
                            if (status_respostorderdetails.equals("1")) {


                                if(status_respostorderdetails.equals("1")&&payment_method_forpay.equals("2")){

                                    Toast.makeText(DeliveryAddrDetailsforWater.this, msg_respostorderdetails, Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(DeliveryAddrDetailsforWater.this, PaymentSuccess.class);
                                    intent.putExtra("txnid", order_id);
                                    intent.putExtra("amount", txtfinalamount_water.getText().toString().trim());
                                    startActivity(intent);
                                    finish();



                                }
                               /* Toast.makeText(DeliveryAddrDetailsforWater.this, msg_respostorderdetails, Toast.LENGTH_SHORT).show();
                                dialog = new Dialog(DeliveryAddrDetailsforWater.this);
                                dialog.setContentView(R.layout.deliveryaddressconfirmpopup);
                                tvdeliveryaddrconfirm = (TextView) dialog.findViewById(R.id.txt_deliveryaddrconfirmation_water);
                                tvdeliveryname = (TextView) dialog.findViewById(R.id.txt_deliveryname_water);


                                tvdeliveryaddrconfirm.setText(getflatno + " " + getaddress + " " + getcity + " " + getlandmark + " " + getpincode + " " + getcountry);
                                tvdeliveryname.setText(HomeActivity.fullname);
                                dialog.show();*/
                            } else {
                                Toast.makeText(DeliveryAddrDetailsforWater.this, msg_respostorderdetails, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(DeliveryAddrDetailsforWater.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

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
                        txtshowwalletamt_water.setText(balanceamount);
                        txtcurrentwalletamt_water.setText(""+balanceamount);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(DeliveryAddrDetailsforWater.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(DeliveryAddrDetailsforWater.this, first, Toast.LENGTH_SHORT).show();
                            Intent i_paymentsuccess = new Intent(DeliveryAddrDetailsforWater.this, PaymentSuccess.class);
                            i_paymentsuccess.putExtra("amount", third);
                            i_paymentsuccess.putExtra("txnid", second);
                            startActivity(i_paymentsuccess);
                        }else if(first.equals("failure")){
                            Toast.makeText(DeliveryAddrDetailsforWater.this, first, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.could_not_receive_data), Toast.LENGTH_LONG).show();
                    }
                }
            }

            //posting order details for water to pay only with online
            private void PostAddressDetails_water_payment(String paymentmethod,String del_type){
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Please Wait...");
                mProgressDialog.show();
                final RestAdapter adapter = new RestAdapter.Builder()
                        .setEndpoint(Constatns.UOHMAC_API)
                        .build();

                UohmacAPI api_ordedetails = adapter.create(UohmacAPI.class);


                api_ordedetails.PostDetailsfororder_waterwihoutwallet(OrderedItemsDetailsForWater.totalprice, getauthkey, "1", paymentmethod,del_type, new Callback<Response>() {
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
                                Toast.makeText(DeliveryAddrDetailsforWater.this, msg_respostorderdetails, Toast.LENGTH_SHORT).show();

                                if(status_respostorderdetails.equals("1") && payment_method_forpay.equals("1")){
                                    Toast.makeText(DeliveryAddrDetailsforWater.this, msg_respostorderdetails, Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(DeliveryAddrDetailsforWater.this,PaymentSuccess.class);
                                    intent.putExtra("txnid",order_id);
                                    intent.putExtra("amount",txtfinalamount_water.getText().toString().trim());
                                    startActivity(intent);
                                    finish();
                                }else if(status_respostorderdetails.equals("1") && payment_method_forpay.equals("3")){
                                    dialog = new Dialog(DeliveryAddrDetailsforWater.this);
                                    dialog.setContentView(R.layout.deliveryaddressconfirm_water);
                                    tvdeliveryaddrconfirm = (TextView) dialog.findViewById(R.id.txt_deliveryaddrconfirmation_water);
                                    tvdeliveryname = (TextView) dialog.findViewById(R.id.txt_deliveryname_water);
                                      tvdeliveryaddrconfirm.setText(getflatno + " " + getaddress + " " + getcity + " " + getlandmark + " " + getpincode + " " + getcountry);
                                    tvdeliveryname.setText(HomeActivity.fullname);
                                    dialog.show();



                                }else if(status_respostorderdetails.equals("1")&& payment_method_forpay.equals("4")){
                                    dialog = new Dialog(DeliveryAddrDetailsforWater.this);
                                    dialog.setContentView(R.layout.deliveryaddressconfirm_water);
                                    tvdeliveryaddrconfirm = (TextView) dialog.findViewById(R.id.txt_deliveryaddrconfirmation_water);
                                    tvdeliveryname = (TextView) dialog.findViewById(R.id.txt_deliveryname_water);
                                    tvdeliveryaddrconfirm.setText(getflatno + " " + getaddress + " " + getcity + " " + getlandmark + " " + getpincode + " " + getcountry);
                                    tvdeliveryname.setText(HomeActivity.fullname);
                                    dialog.show();
                                }


                            } else {
                                Toast.makeText(DeliveryAddrDetailsforWater.this, msg_respostorderdetails, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(DeliveryAddrDetailsforWater.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                        Log.d("ERROR:", error.getMessage());
                    }
                });
            }

        //proceeding to pay
        public void ProceedToPay(View view){

            int environment;

            environment = PayuConstants.STAGING_ENV;

            mPaymentParams = new PaymentParams();


            mPaymentParams.setKey("gtKFFx");
            if(status_respostorderdetails.equals("1")&&payment_method_forpay.equals("4")){
                mPaymentParams.setAmount(txtamtaftercalc_water.getText().toString());
            }else{
                mPaymentParams.setAmount(txtfinalamount_water.getText().toString());
            }
            mPaymentParams.setProductInfo("Water");
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
                    progressDialog = new ProgressDialog(DeliveryAddrDetailsforWater.this);
                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();
                }

                @Override
                protected PayuHashes doInBackground(String... postParams) {
                    PayuHashes payuHashes = new PayuHashes();
                    try {

                        //TODO Below url is just for testing purpose, merchant needs to replace this with their server side hash generation url
                        URL url = new URL(" https://payu.herokuapp.com/get_hash");

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
        }

    package com.uohmac.com.drgarmentandwater.Fragments;

    import android.app.AlertDialog;
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
    import android.widget.EditText;
    import android.widget.ImageView;
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
    import com.uohmac.com.drgarmentandwater.Activites.PaymentFailure;
    import com.uohmac.com.drgarmentandwater.Activites.PaymentSuccess;
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
     * Created by uOhmac Technologies on 20-Jan-17.
     */
    public class WalletFragment extends AppCompatActivity implements OneClickPaymentListener {

        View myFragmentView;
        //public static final String MYPREFERNCES_REGISTER = "mypreferregister";
        //public static final String MYPREFERNCES_POSTFB = "mypreferpostfb";
        public static final String MYPREFERNCES_LOGIN = "mysharedpref_waterlaundry";

        SharedPreferences sp;
        String getauthkey,result,status,response_addmoney,mobilenum_forpayment,email_forpayment,firstname_forpayment;
        String transaction_no="",message="";
        public static String balanceamount;
        JSONObject jsonObject;
        public static TextView tv_walletbalance,txt_testing;
        Button btn_addmoneytowallet;
        EditText et_addmoney;
        private PaymentParams mPaymentParams;
        PayuHashes payuHashes;
        String merchantKey, userCredentials;
        ImageView imgback;
        String merchantresponse = "";
        // This sets the configuration
        private PayuConfig payuConfig;
        // Used when generating hash from SDK
        private PayUChecksum checksum;
        public WalletFragment() {
        }
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.fragmentwallet);

            //TODO Must write this code if integrating One Tap payments
            OnetapCallback.setOneTapCallback(this);

            //TODO Must write below code in your activity to set up initial context for PayU
            Payu.setInstance(this);
            sp = getSharedPreferences(MYPREFERNCES_LOGIN, 0);
            getauthkey = sp.getString("auth_key", "");
            Log.e("gettingauthkey_wallet", getauthkey);

            mobilenum_forpayment = sp.getString("mobile", "");
            email_forpayment = sp.getString("email", "");
            // email_forpayment = sp.getString("user_name", "");
            firstname_forpayment = sp.getString("firstName", "");

            // lets tell the people what version of sdk we are using
            PayUSdkDetails payUSdkDetails = new PayUSdkDetails();

            //Toast.makeText(this, "Build No: " + payUSdkDetails.getSdkBuildNumber() + "\n Build Type: " + payUSdkDetails.getSdkBuildType() + " \n Build Flavor: " + payUSdkDetails.getSdkFlavor() + "\n Application Id: " + payUSdkDetails.getSdkApplicationId() + "\n Version Code: " + payUSdkDetails.getSdkVersionCode() + "\n Version Name: " + payUSdkDetails.getSdkVersionName(), Toast.LENGTH_LONG).show();


            tv_walletbalance = (TextView)findViewById(R.id.txt_walletbalance);
            btn_addmoneytowallet = (Button)findViewById(R.id.btn_addmoneytowallet);
            et_addmoney = (EditText)findViewById(R.id.editwalletmoney);
            txt_testing = (TextView)findViewById(R.id.txt_testing);
            imgback = (ImageView)findViewById(R.id.ivback_wallet);

            if(AppNetworkInfo.isConnectingToInternet(WalletFragment.this)){
                checkwalletbalance();
            }else{
                Toast.makeText(WalletFragment.this, "No network found.!", Toast.LENGTH_SHORT).show();
            }



            imgback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

        }


       /* @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
           // getActivity().setTitle("Add Money To Wallet");

            myFragmentView = inflater.inflate(R.layout.fragmentwallet, container, false);
            tv_walletbalance = (TextView)myFragmentView.findViewById(R.id.txt_walletbalance);
            btn_addmoneytowallet = (Button)myFragmentView.findViewById(R.id.btn_addmoneytowallet);
            et_addmoney = (EditText)myFragmentView.findViewById(R.id.editwalletmoney);

            if(!AppNetworkInfo.isConnectingToInternet(WalletFragment.this)) {
                Toast.makeText(WalletFragment.this, "No Network found. Please try later.", Toast.LENGTH_SHORT).show();
            }else{
                checkwalletbalance();
            }


            btn_addmoneytowallet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!AppNetworkInfo.isConnectingToInternet(WalletFragment.this)) {
                        Toast.makeText(WalletFragment.this, "No Netwaork found. Please try later.", Toast.LENGTH_SHORT).show();
                    } else if (et_addmoney.getText().toString().length() < 0) {
                        et_addmoney.setError(getString(R.string.error_field_required));
                        et_addmoney.requestFocus();
                    } else {
                       // Addmoneytowallet();
                    }

                    AddMoneyWallet(view);
                }
            });
            return myFragmentView;
        }
    */

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                   /* new AlertDialog.Builder(WalletFragment.this)
                            .setCancelable(false)
                            .setMessage("Payu's Data : " + data.getStringExtra("payu_response") + "\n\n\n Merchant's Data: " + data.getStringExtra("result"))
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                }
                            }).show();*/

                    Log.e("Payu's Data :", " " + " " + data.getStringExtra("payu_response") + "\n\n\n Merchant's Data: " + data.getStringExtra("result"));
                    Log.e("TAG","Merchant data:"+data.getStringExtra("result"));

                    merchantresponse = data.getStringExtra("result");
                    String[] datas = merchantresponse.split(",");
                    String first = datas[0];
                    Log.e("first", first);
                    String second = datas[1];
                    Log.e("second", second);
                    String third = datas[2];
                    Log.e("third", third);
                    if(first.equals("success")){
                        Toast.makeText(WalletFragment.this, first, Toast.LENGTH_SHORT).show();
                        Intent i_paymentsuccess = new Intent(WalletFragment.this, PaymentSuccess.class);
                        i_paymentsuccess.putExtra("amount", third);
                        i_paymentsuccess.putExtra("txnid", second);
                        startActivity(i_paymentsuccess);
                    }else if(first.equals("failure")){
                        Toast.makeText(WalletFragment.this, first, Toast.LENGTH_SHORT).show();
                        Intent i_paymentsuccess = new Intent(WalletFragment.this, PaymentFailure.class);
                        i_paymentsuccess.putExtra("amount_failed", third);
                        i_paymentsuccess.putExtra("txnid_failed", second);
                        startActivity(i_paymentsuccess);
                    }

                } else {
                    Toast.makeText(WalletFragment.this, getString(R.string.could_not_receive_data), Toast.LENGTH_LONG).show();
                }
            }
        }


        public void AddMoney(View view){

            if(et_addmoney.getText().toString().equals("")){
                et_addmoney.setError(getString(R.string.error_field_required));
                et_addmoney.requestFocus();
            }else if(AppNetworkInfo.isConnectingToInternet(WalletFragment.this)){
                Addmoneytowallet();
            }else{
                AlertDialog.Builder ab = new AlertDialog.Builder(WalletFragment.this);
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
                        if(AppNetworkInfo.isConnectingToInternet(WalletFragment.this)){
                            Addmoneytowallet();
                        }

                    }
                });
                ab.create();
                ab.show();
            }



        }




        public void generateHashFromSDK(PaymentParams mPaymentParams, String salt) {
            payuHashes = new PayuHashes();
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
            Log.e("amunt",mPaymentParams.getAmount());
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
                progressDialog = new ProgressDialog(WalletFragment.this);
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
                    Log.e("postparam", postParam);

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
                    Log.e("res", ""+response);

                    Iterator<String> payuHashIterator = response.keys();
                    while (payuHashIterator.hasNext()) {
                        String key = payuHashIterator.next();
                        Log.e("key", key);
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
                                Log.e("getpayment_hash",response.getString(key));
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
                                Log.e("vas_for_mobile_sdk_hash", response.getString(key));
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
                                Log.e("payment_related_details_for_mobile_sdk_hash", response.getString(key));
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

            Intent intent = new Intent(WalletFragment.this, PayUBaseActivity.class);
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

                        tv_walletbalance.setText(balanceamount);
                        txt_testing.setText(balanceamount);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(WalletFragment.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                    Log.d("ERROR:", error.getMessage());

                }
            });
        }


        private void Addmoneytowallet(){

            final RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint(Constatns.UOHMAC_API)
                    .build();

            UohmacAPI api_addmoneytowallet = adapter.create(UohmacAPI.class);

            api_addmoneytowallet.AddMoneyToWallet(getauthkey, et_addmoney.getText().toString(), new Callback<Response>() {
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
                    response_addmoney = sb.toString();
                    Log.d("walletaddmoneyresponse", response_addmoney);
                    try {
                        jsonObject = new JSONObject(response_addmoney);
                        status = jsonObject.getString("status");
                        transaction_no = jsonObject.getString("txn_no");
                        message = jsonObject.getString("msg");
                        if(status.equals("1")){
                            int environment;

                            environment = PayuConstants.STAGING_ENV;

                            mPaymentParams = new PaymentParams();

                            mPaymentParams.setKey("gtKFFx");
                            mPaymentParams.setAmount(et_addmoney.getText().toString());
                            mPaymentParams.setProductInfo("WalletAmount");
                            mPaymentParams.setFirstName(firstname_forpayment);
                            mPaymentParams.setEmail(email_forpayment);


                                  /*
                                * Transaction Id should be kept unique for each transaction.
                                * */
                            mPaymentParams.setTxnId("" + System.currentTimeMillis());

                            mPaymentParams.setSurl("http://blessindia.in/water_laundry/index.php/api/payment/wallet_success");
                            mPaymentParams.setFurl("http://blessindia.in/water_laundry/index.php/api/payment/wallet_failure");


                            mPaymentParams.setUdf1(transaction_no);
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
                        }else{
                            Toast.makeText(WalletFragment.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(WalletFragment.this, "Please try after sometime.", Toast.LENGTH_SHORT).show();

                    Log.d("ERROR:", error.getMessage());

                }
            });
        }




    }

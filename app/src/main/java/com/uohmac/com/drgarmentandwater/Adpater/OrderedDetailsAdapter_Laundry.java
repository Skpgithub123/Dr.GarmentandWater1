package com.uohmac.com.drgarmentandwater.Adpater;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.uohmac.com.drgarmentandwater.Activites.HomeActivity;
import com.uohmac.com.drgarmentandwater.Activites.Laundryitemdetails;
import com.uohmac.com.drgarmentandwater.Activites.OrderedItemsDetailsForLaundry;
import com.uohmac.com.drgarmentandwater.Api.UohmacAPI;
import com.uohmac.com.drgarmentandwater.Pojos.OrderedDetailsPojo;
import com.uohmac.com.drgarmentandwater.R;
import com.uohmac.com.drgarmentandwater.Utils.AppNetworkInfo;
import com.uohmac.com.drgarmentandwater.Utils.Constatns;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by uOhmac Technologies on 07-Jan-17.
 */
public class OrderedDetailsAdapter_Laundry extends ArrayAdapter {

    private Context _context;
     ViewHolder holder;
    int _resource = R.layout.ordereditemslaundry_single_item;
    String item_id_fromcart,price_fromcart,item_name_fromcart,item_img_fromcart;
    int no_of_item_fromcart;
    String totalprice,getauthkey,remove_status,remove_message,response_increament,reponse_totalprice,response_decrement,reponse_removeitem,status_itemprice,item_price_add,item_price_sub;
    JSONObject jsonObject;
    List<OrderedDetailsPojo> orderedDetailsPojoList=null;
    public static OrderedDetailsPojo orderedDetailsPojo;
    int Cared_Incrementcount = 0;
    private LayoutInflater inflater ;
    public static final String MYPREFERNCES_LOGIN = "mysharedpref_waterlaundry";
    ProgressDialog pd;
    ArrayList<OrderedDetailsPojo> orderedDetailsPojoList_laundry = new ArrayList<OrderedDetailsPojo>();

    //public static final String MYPREFERNCES_REGISTER = "mypreferregister";
   // public static final String MYPREFERNCES_POSTFB = "mypreferpostfb";
    SharedPreferences sp;

    public OrderedDetailsAdapter_Laundry(Context context, int resource, ArrayList<OrderedDetailsPojo> orderedDetailsPojoList) {
        super(context, resource,orderedDetailsPojoList);

        _resource = resource;
        _context = context;
        this.orderedDetailsPojoList = orderedDetailsPojoList;



        sp = context.getSharedPreferences(MYPREFERNCES_LOGIN, 0);
        getauthkey = sp.getString("auth_key", "");
        Log.e("gettingauthkey_orderdetails", getauthkey);



    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        //orderedDetailsPojo = (OrderedDetailsPojo) getItem(position);


        orderedDetailsPojo = orderedDetailsPojoList.get(position);
        View row;


        if(convertView==null){
            inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(_resource, parent, false);
            holder = new ViewHolder();


            holder.tv_itemname = (TextView)row.findViewById(R.id.txt_menitemname_fromcart);
            holder.tv_itemprice = (TextView) row.findViewById(R.id.txt_menitemprice_fromcart);
            holder.image_men = (CircleImageView) row.findViewById(R.id.menimagedrycleaning_fromcart);
            holder.tv_itemquantity = (TextView)row.findViewById(R.id.txt_itemqtyincrement_fromcart);
            // tv_totalprice_fromcart = (TextView)row.findViewById(R.id.totalprice_fromcart);
           // holder.txt_totalprice_fromcart = (TextView)row.findViewById(R.id.txt_storedtotalprice_fromcart);
            holder.tvitemid = (TextView)row.findViewById(R.id.textView57_fromcart);
            holder.imgaddmen = (ImageView)row.findViewById(R.id.img_menadd_fromcart);
            holder.imgsubmen = (ImageView)row.findViewById(R.id.img_mensub_fromcart);
            holder.btn_removeitem = (Button)row.findViewById(R.id.bthremoveitem);

            row.setTag(holder);
        }else {
            row=convertView;
            holder = (ViewHolder) row.getTag();
        }
        if(!TextUtils.isEmpty(orderedDetailsPojo.getId())&&!TextUtils.isEmpty(orderedDetailsPojo.getNo_of_item())&&!TextUtils.isEmpty(orderedDetailsPojo.getPrice())&&!TextUtils.isEmpty(orderedDetailsPojo.getItemname())&&!TextUtils.isEmpty(orderedDetailsPojo.getItemimage())){
            holder.tvitemid.setText(orderedDetailsPojo.getId());
            holder.tv_itemquantity.setText(orderedDetailsPojo.getNo_of_item());
            holder.tv_itemprice.setText(orderedDetailsPojo.getPrice());
            holder.tv_itemname.setText(orderedDetailsPojo.getItemname());


            item_id_fromcart = orderedDetailsPojo.getId();

            no_of_item_fromcart = Integer.parseInt(orderedDetailsPojo.getNo_of_item());
            Log.e("no_of_items", "" + no_of_item_fromcart);

            price_fromcart = orderedDetailsPojo.getPrice();
            //setting previous totalprice for ordereditems
            // holder.tv_totalprice_fromcart.setText(totalprice);



            if(!TextUtils.isEmpty(Laundryitemdetails.totalprice)) {
                //gettotalpricefordrycleaning = Integer.valueOf(sp.getString("itemprice", ""));
                //Log.e("gettotalpricefordrycleaning", ""+ gettotalpricefordrycleaning);
                //holder.txt_totalprice_fromcart.setText(Laundryitemdetails.totalprice);
                Log.d("getavailprice_fromcart", "" + Laundryitemdetails.totalprice);
            }


            if(!TextUtils.isEmpty(orderedDetailsPojo.getItemimage())) {
                Log.d("DateFound", "Post:" + orderedDetailsPojo.getItemimage());

                Picasso.with(_context)
                        .load(orderedDetailsPojo.getItemimage())
                        .into(holder.image_men);
            }
        }


        holder.imgaddmen.setOnClickListener(    new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (holder.tv_itemquantity.getText().toString().equals("0"))
                {
                    Cared_Incrementcount = 1;

                }else
                {
                    Cared_Incrementcount = Integer.parseInt(holder.tv_itemquantity.getText().toString());
                    Cared_Incrementcount++;
                }



                holder.tv_itemquantity.setText("" + Cared_Incrementcount);

                Log.d("getitemsidsvalues0", "====" + holder.tvitemid.getText().toString());


                if(AppNetworkInfo.isConnectingToInternet(_context)){
                    Increamentquantity("" + holder.tvitemid.getText().toString());


                } else{
                    Toast.makeText(getContext(), "No network found.!", Toast.LENGTH_SHORT).show();
                }



            }
        });

        holder.imgsubmen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                if (holder.tv_itemquantity.getText().toString().equals("0"))
                {
                    return;

                }else
                {
                    Cared_Incrementcount = Integer.parseInt(holder.tv_itemquantity.getText().toString());
                    Cared_Incrementcount--;
                }


                if(Cared_Incrementcount==1){
                    holder.imgsubmen.setEnabled(false);
                    holder.imgsubmen.requestFocus();
                }
                holder.tv_itemquantity.setText("" + Cared_Incrementcount);

                Log.d("getitemsidsvalues0", "====" + holder.tvitemid.getText().toString());

                if(AppNetworkInfo.isConnectingToInternet(_context)){
                    DecrementQuantity(("" + holder.tvitemid.getText().toString()));


                } else{
                    Toast.makeText(getContext(), "No network found.!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        holder.btn_removeitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder ab = new AlertDialog.Builder(_context);
                ab.setMessage("Remove" + " " + holder.tv_itemname.getText().toString() + " " + "from your cart item?");
                ab.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        orderedDetailsPojo = orderedDetailsPojoList.get(position);


                        RemoveItem(orderedDetailsPojo.getId());
                    }
                });
                ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();


                    }
                });
                ab.create();
                ab.show();
            }
        });



        return row;
    }




    //defining the api to increament the quantity for washfold
    private void Increamentquantity(String card_itemID){
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_increamentquantity = adapter.create(UohmacAPI.class);

        api_increamentquantity.IncreamentAPI(getauthkey, card_itemID, new Callback<Response>() {
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
                response_increament = sb.toString();
                Log.e("getre_increament", response_increament);

                try {
                    jsonObject = new JSONObject(response_increament);
                    status_itemprice = jsonObject.getString("status");
                    item_price_add = jsonObject.getString("item_price");

                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (status_itemprice.equals("1")) {
                    holder.tv_itemprice.setText(item_price_add);
                    if (AppNetworkInfo.isConnectingToInternet(_context)) {
                        Totalprice();


                    } else {
                        AlertDialog.Builder ab = new AlertDialog.Builder(_context);
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
                                Totalprice();


                            }
                        });
                        ab.create();
                        ab.show();
                    }
                }


                Log.d("statusitemprice", "====" + status_itemprice + "  " + item_price_add);


            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("ERROR:", error.getMessage());

            }
        });
    }


    //defining the api to decrement the quantity for wash fold
    private void DecrementQuantity(String card_itemID){
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_decrementquantity = adapter.create(UohmacAPI.class);

        api_decrementquantity.DecrementAPI(getauthkey, card_itemID, new Callback<Response>() {
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

                response_decrement = sb.toString();
                Log.e("getre_decreament", response_decrement);

                try {
                    jsonObject = new JSONObject(response_decrement);
                    status_itemprice = jsonObject.getString("status");
                    item_price_sub = jsonObject.getString("item_price");
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (status_itemprice.equals("1")) {
                    holder.tv_itemprice.setText(item_price_sub);
                    Totalprice();
                }


            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("ERROR:", error.getMessage());

            }
        });
    }


    //defineing api to calculate the totalprice
    private void Totalprice(){
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_totalprice = adapter.create(UohmacAPI.class);

        api_totalprice.TotalCartPrice("2", getauthkey, new Callback<Response>() {
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

                reponse_totalprice = sb.toString();
                Log.e("getre_totalprice", reponse_totalprice);
                try {

                    jsonObject = new JSONObject(reponse_totalprice);
                    totalprice = jsonObject.getString("price");
                    Log.e("gettotalprice_api", totalprice);
                    Log.d("getauthkey_api", "*******     " + getauthkey);



                } catch (Exception e) {
                    e.printStackTrace();
                }
                OrderedItemsDetailsForLaundry.txt_totalprice_fromcart.setText(totalprice);
                getcartdata();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("ERROR:", error.getMessage());

            }
        });

    }



    private void RemoveItem(String id){
        pd = new ProgressDialog(getContext());
        pd.setMessage("Please wait...");
        pd.show();
         RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_removeitem = adapter.create(UohmacAPI.class);

        api_removeitem.RemoveItem(getauthkey, id, new Callback<Response>() {
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

                reponse_removeitem = sb.toString();
                Log.e("getre_removeitem", reponse_removeitem);

                try {
                    jsonObject = new JSONObject(reponse_removeitem);
                    remove_status = jsonObject.getString("status");
                    remove_message = jsonObject.getString("msg");

                } catch (Exception e) {
                    e.printStackTrace();
                }




                if (remove_status.equals("1")) {
                    Toast.makeText(_context, remove_message, Toast.LENGTH_SHORT).show();
                    //OrderedItemsDetailsForLaundry oder = new OrderedItemsDetailsForLaundry();
                    //oder.getcartdata();
                    //refreshEvents(orderedDetailsPojoList);
                    Totalprice();




                } else {
                    Toast.makeText(_context, remove_message, Toast.LENGTH_SHORT).show();
                }

                pd.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("ERROR:", error.getMessage());

            }
        });

    }

    public class ViewHolder{
        CircleImageView image_men;
        TextView tv_itemname, tv_itemprice, tv_itemquantity, tvitemid, tv_totalprice_fromcart;
        String status, message;
        int itempriceforcal;
        ImageView imgsubmen, imgaddmen;
        Button btn_removeitem;
        RelativeLayout relativeLayout;
    }

   /* public void getcartdata(){
        pd = new ProgressDialog(getContext());
        pd.setIndeterminate(true);
        pd.setMessage("Please Wait...");
        pd.show();
       // orderedDetailsPojoList.clear();

        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_getcartdata = adapter.create(UohmacAPI.class);

        api_getcartdata.GetCartData("2",getauthkey, new Callback<List<OrderedDetailsPojo>>() {
            @Override
            public void success(List<OrderedDetailsPojo> orderedDetailsPojos, Response response) {
                // recyclerView.setLayoutManager(new LinearLayoutManager(OrderedItemsDetailsForLaundry.this, LinearLayoutManager.VERTICAL, false));
                // mAdapter = new OrderedDetailsAdapter(OrderedItemsDetailsForLaundry.this, orderedDetailsPojos);
                //recyclerView.setAdapter(mAdapter);

                orderedDetailsPojoList_laundry = orderedDetailsPojos;

                 //Toast.makeText(getContext(),"orderedDetailsPojoList_laundry"+orderedDetailsPojos.size(), Toast.LENGTH_SHORT).show();

                 OrderedDetailsAdapter_Laundry cart_adapter = new OrderedDetailsAdapter_Laundry(getContext(), R.layout.ordereditemslaundry_single_item, orderedDetailsPojoList_laundry);
                OrderedItemsDetailsForLaundry.rvMyRequest.setAdapter(cart_adapter);


                    *//*Intent i = new Intent(getContext(), HomeActivity.class);
                    getContext().startActivity(i);
                    Toast.makeText(getContext(), "start shopping", Toast.LENGTH_SHORT).show();
*//*



                 pd.dismiss();


                //Log.d("removeitem", "arraysizw" + orderedDetailsPojoList_laundry.size());


               *//* if (orderedDetailsPojoList_laundry.size() == 0) {
                    orderedDetailsPojoList.clear();
                    Intent i = new Intent(getContext(), HomeActivity.class);
                    getContext().startActivity(i);
                    Toast.makeText(getContext(),"start shopping", Toast.LENGTH_SHORT).show();
                }*//*
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Errorfound", "ERROR:" + error.getMessage(), error);

            }
        });
    }*/




    public void getcartdata(){


       final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait.....");
        orderedDetailsPojoList_laundry.clear();
        progressDialog.show();


        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_getcartdata = adapter.create(UohmacAPI.class);

        api_getcartdata.GetCartData("2",getauthkey, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                // recyclerView.setLayoutManager(new LinearLayoutManager(OrderedItemsDetailsForLaundry.this, LinearLayoutManager.VERTICAL, false));
                // mAdapter = new OrderedDetailsAdapter(OrderedItemsDetailsForLaundry.this, orderedDetailsPojos);
                //recyclerView.setAdapter(mAdapter);


                BufferedReader reader = null;
                String result = "";

                try {
                    //Initializing buffered reader
                    reader = new BufferedReader(new InputStreamReader(response.getBody().in()));

                    //Reading the output in the string
                    result = reader.readLine();


                } catch (IOException e)
                {
                    Log.d("IOExceptiongio", "laundryresponce" + e.toString());
                }
                //Displaying the output as a toast
                Log.d("laundryresponceremover", "success**   " + result.toString());

                if (result.toString().trim().charAt(0) == '[') {

                    try {
                        JSONArray jsonArray = new JSONArray(result);


                  /*  if(server_response.trim().charAt(0) == '[') {
                        Log.e("Response is : " , "JSONArray");
                    } else if(server_response.trim().charAt(0) == '{') {
                        Log.e("Response is : " , "JSONObject");
                    } */


                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                // OrderedDetail_LaundryAdapter orderedAdapter_laundry = new OrderedDetail_LaundryAdapter();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                //sfsdfsfsdfsdfsdfsdfsfsdfdsfsdfds
                            /*@SerializedName("item_id")
                            @SerializedName("item_name")
                            @SerializedName("item_img")
                            @SerializedName("price")
                            @SerializedName("no_of_item")*/

                                orderedDetailsPojo = new OrderedDetailsPojo();


                                orderedDetailsPojo.setId(jsonObject.getString("item_id"));
                                orderedDetailsPojo.setItemname(jsonObject.getString("item_name"));
                                orderedDetailsPojo.setItemimage(jsonObject.getString("item_img"));
                                orderedDetailsPojo.setNo_of_item(jsonObject.getString("no_of_item"));
                                orderedDetailsPojo.setPrice(jsonObject.getString("price"));


                                orderedDetailsPojoList_laundry.add(orderedDetailsPojo);
                                OrderedItemsDetailsForLaundry.nomoreitemfound_laundry.setVisibility(View.GONE);

                                OrderedItemsDetailsForLaundry.rvMyRequest.setVisibility(View.VISIBLE);
                                OrderedItemsDetailsForLaundry.txt_havepromocode_.setVisibility(View.VISIBLE);
                                OrderedItemsDetailsForLaundry.txt_getpromo.setVisibility(View.VISIBLE);
                                OrderedItemsDetailsForLaundry.relativeLayout6.setVisibility(View.VISIBLE);
                                OrderedItemsDetailsForLaundry.relativelayout_laundrytotalprice.setVisibility(View.VISIBLE);
                            }


                            Log.d("orderdeatailarray", "sizelandury" + orderedDetailsPojoList_laundry.size());


                       /* orderedDetailsPojoList_laundry = orderedDetailsPojos;*/
                            OrderedDetailsAdapter_Laundry cart_adapter = new OrderedDetailsAdapter_Laundry(getContext(), R.layout.ordereditemslaundry_single_item, orderedDetailsPojoList_laundry);
                            OrderedItemsDetailsForLaundry.rvMyRequest.setAdapter(cart_adapter);

                            // mProgressDialog.dismiss();


                        } else {
                        /*    OrderedItemsDetailsForLaundry.rvMyRequest.setVisibility(View.GONE);
                            OrderedItemsDetailsForLaundry.txt_havepromocode_.setVisibility(View.GONE);
                            OrderedItemsDetailsForLaundry.txt_getpromo.setVisibility(View.GONE);
                            OrderedItemsDetailsForLaundry.relativeLayout6.setVisibility(View.GONE);
                            OrderedItemsDetailsForLaundry.relativelayout_laundrytotalprice.setVisibility(View.GONE);
                            OrderedItemsDetailsForLaundry.nomoreitemfound_laundry.setVisibility(View.VISIBLE);
                            Toast.makeText(getContext(), "No Data found", Toast.LENGTH_SHORT).show();*/

                            // mProgressDialog.dismiss();

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {

                    try {
                        JSONObject jsonObject_nodatacard = new JSONObject(result.toString());


                        String status = jsonObject_nodatacard.getString("status");

                        Log.d("statuvalues","*****  "+status);


                        if (status.equals("0"))
                        {
                            Toast.makeText(getContext(),jsonObject_nodatacard.getString("msg"),Toast.LENGTH_SHORT).show();

                            OrderedItemsDetailsForLaundry.rvMyRequest.setVisibility(View.GONE);
                            OrderedItemsDetailsForLaundry.txt_havepromocode_.setVisibility(View.GONE);
                            OrderedItemsDetailsForLaundry.txt_getpromo.setVisibility(View.GONE);
                            OrderedItemsDetailsForLaundry.relativeLayout6.setVisibility(View.GONE);
                            OrderedItemsDetailsForLaundry.relativelayout_laundrytotalprice.setVisibility(View.GONE);
                            OrderedItemsDetailsForLaundry.nomoreitemfound_laundry.setVisibility(View.VISIBLE);
                          //  Toast.makeText(getContext(), "No Data found", Toast.LENGTH_SHORT).show();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                // orderedDetailsPojoList_laundry = orderedDetailsPojos;
                //   cart_adapter = new OrderedDetailsAdapter_Laundry(OrderedItemsDetailsForLaundry.this, R.layout.ordereditemslaundry_single_item, orderedDetailsPojoList_laundry);
                //   rvMyRequest.setAdapter(cart_adapter);

               // mProgressDialog.dismiss();

                progressDialog.dismiss();



            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Errorfound", "ERROR:" + error.getMessage(), error);
                progressDialog.dismiss();

            }
        });
    }

}

package com.uohmac.com.drgarmentandwater.Adpater;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.uohmac.com.drgarmentandwater.Activites.DryCleaningActivity;
import com.uohmac.com.drgarmentandwater.Api.UohmacAPI;
import com.uohmac.com.drgarmentandwater.Pojos.DryCleaningPojo;
import com.uohmac.com.drgarmentandwater.R;
import com.uohmac.com.drgarmentandwater.Utils.AppNetworkInfo;
import com.uohmac.com.drgarmentandwater.Utils.Constatns;

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
 * Created by System-03 on 11/8/2016.
 */
public class HouseHoldAdapter extends ArrayAdapter {


    /******************       gowthaman      *****************/

    int HouseHoldIncrementcount = 0, finalHouseHoldIncrementcount = 0;


    String men_status ="";

    /******************       gowthaman      *****************/

    private Context _context;
    List<DryCleaningPojo> dryCleaningPojos;
    private LayoutInflater inflater ;
    private int resourceId;
    String totalprice="",getauthkey="",response_increament="",reponse_totalprice="",response_decrement="",reponse_returnitemquantity="",status="",msg="",totalitemquantity="";
    JSONObject jsonObject;
    int textaddquantity_household = 0,totalpriceforeachid_household;
    DryCleaningPojo dryCleaningPojo;
    int _resource = R.layout.householdsingleitem;
    //public static final String MYPREFERNCES_REGISTER = "mypreferregister";
    //public static final String MYPREFERNCES_POSTFB = "mypreferpostfb";
    public static final String MYPREFERNCES_LOGIN = "mysharedpref_waterlaundry";
    ViewHolder viewHolder;
    SharedPreferences sp;

    ArrayList<String> arrayList_count_household = new ArrayList<String>();
    public ArrayList<Integer> quantity_household = new ArrayList<Integer>();
    public HouseHoldAdapter(Context context, int resource,List<DryCleaningPojo> dryCleaningPojos) {
        super(context, resource, dryCleaningPojos);
        _resource = resource;
        _context = context;
        this.dryCleaningPojos = dryCleaningPojos;
        for(int i=0; i<dryCleaningPojos.size();i++){
            quantity_household.add(0);
        }

       // sp = getContext().getSharedPreferences(MYPREFERNCES_REGISTER, 0);
        //getting authkey from sp

        sp = getContext().getSharedPreferences(MYPREFERNCES_LOGIN, 0);
        getauthkey = sp.getString("auth_key", "");


        /*sp = getContext().getSharedPreferences(MYPREFERNCES_POSTFB, 0);
        getauthkey = sp.getString("auth_key", "");
        Log.e("gettingauthkeyforsettings", getauthkey);*/
       // totalprice = DataBaseHandler.item_price;




    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

         dryCleaningPojo = (DryCleaningPojo) getItem(position);

        dryCleaningPojo = dryCleaningPojos.get(position);
        View row;

        if(convertView == null){
            inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(_resource, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.tv_itemname = (TextView) row.findViewById(R.id.txt_householditemname);
            viewHolder.tv_itemprice = (TextView) row.findViewById(R.id.txt_householditemprice);
            viewHolder.image_household = (CircleImageView) row.findViewById(R.id.householdimagedrycleaning);
            viewHolder.tv_itemquantity_household = (TextView)row.findViewById(R.id.txt_itempriceincrementforhousehold);
            viewHolder.tvitemid_household = (TextView)row.findViewById(R.id.textView60);
            viewHolder.imgaddhousehold = (ImageView)row.findViewById(R.id.img_householdadd);
            viewHolder.imgsubhousehold = (ImageView)row.findViewById(R.id.img_householdsub);
            row.setTag(viewHolder);
        }else{
            row=convertView;
            viewHolder = (ViewHolder) row.getTag();
        }

        if(!TextUtils.isEmpty(dryCleaningPojo.getId())&&!TextUtils.isEmpty(dryCleaningPojo.getItemname())&&!TextUtils.isEmpty(dryCleaningPojo.getItemprice())&&!TextUtils.isEmpty(dryCleaningPojo.getCategoryid())){
            viewHolder.tv_itemname.setText(dryCleaningPojo.getItemname());
            viewHolder.tv_itemprice.setText("\u20B9" +dryCleaningPojo.getItemprice());
            viewHolder.category_id = dryCleaningPojo.getCategoryid();
           viewHolder.itemid_household = dryCleaningPojo.getId();
            viewHolder.tvitemid_household.setText(dryCleaningPojo.getId());
            viewHolder.itempriceforcal = Integer.parseInt(dryCleaningPojo.getItemprice());
            Log.e("gethouseholditemname===", dryCleaningPojo.getItemname());
            Log.d("gethouseholditemprice", "Post:" + dryCleaningPojo.getItemprice());
            Log.e("getitemidforhouse===", viewHolder.itemid_household);

            if(!TextUtils.isEmpty(dryCleaningPojo.getItemimage())){
                Log.d("DateFound", "Post:" + dryCleaningPojo.getItemimage());

                Picasso.with(_context)
                        .load(dryCleaningPojo.getItemimage())
                        .into(viewHolder.image_household);
            }
        }
        try{

            viewHolder.tv_itemquantity_household.setText(quantity_household.get(position) + "");

        }catch(Exception e){

            Log.d("sqlexeception","exectpoin"+e.toString());
            e.printStackTrace();
        }


        viewHolder.imgaddhousehold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                if (viewHolder.tv_itemquantity_household.getText().toString().equals("0"))
                {
                    HouseHoldIncrementcount = 1;

                }else
                {
                    HouseHoldIncrementcount = Integer.parseInt(viewHolder.tv_itemquantity_household.getText().toString());
                    finalHouseHoldIncrementcount= HouseHoldIncrementcount+Integer.parseInt(totalitemquantity);
                }



                viewHolder.tv_itemquantity_household.setText("" + finalHouseHoldIncrementcount);

                Log.d("getitemsidsvalues0", "====" + viewHolder.tvitemid_household.getText().toString());

                if(AppNetworkInfo.isConnectingToInternet(_context)){
                    Increamentquantity("" + viewHolder.tvitemid_household.getText().toString());


                } else{
                    Toast.makeText(_context, "No network found.. Please try again", Toast.LENGTH_SHORT).show();
                }





            }
        });


        viewHolder.imgsubhousehold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {






                if (viewHolder.tv_itemquantity_household.getText().toString().equals("0"))
                {
                    HouseHoldIncrementcount = 1;

                }else
                {
                    finalHouseHoldIncrementcount = Integer.parseInt(viewHolder.tv_itemquantity_household.getText().toString());
                    finalHouseHoldIncrementcount--;
                }



                viewHolder.tv_itemquantity_household.setText("" + finalHouseHoldIncrementcount);

                Log.d("getitemsidsvalues0", "====" + viewHolder.tvitemid_household.getText().toString());

                if(AppNetworkInfo.isConnectingToInternet(_context)){
                    DecrementQuantity("" + viewHolder.tvitemid_household.getText().toString());


                } else{
                    Toast.makeText(_context, "No network found.. Please try again", Toast.LENGTH_SHORT).show();
                }







            }
        });


        if(AppNetworkInfo.isConnectingToInternet(getContext())){
            getreturn_itemquantity(""+ viewHolder.tvitemid_household.getText().toString());
        }else{
            Toast.makeText(_context,"No network found.. Please try again", Toast.LENGTH_SHORT).show();
        }

    /*    viewHolder.imgaddhousehold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity_household.set(position, quantity_household.get(position) + 1);
                Log.d("PositiveNumber", "==" + viewHolder.tv_itemquantity_household.getText().toString());
                Log.d("PositiveNumber", "......" + viewHolder.tvitemid_household.getText().toString());

                if (arrayList_count_household.contains(viewHolder.tvitemid_household.getText().toString()))
                {
                    textaddquantity_household = textaddquantity_household + 1;
                    viewHolder.tv_itemquantity_household.setText("" + textaddquantity_household);
                   *//* totalpriceforeachid_household =  viewHolder.itempriceforcal* textaddquantity_household;
                    Log.e("toalp", "" + totalpriceforeachid_household);*//*
                    //editor.putInt("addingquantity", textaddquantity);
                    //Log.e("adding", "added quantity");
                    //editor.commit();


                    if(textaddquantity_household==1){
                        Increamentquantity();
                        Totalprice();
                        //DryCleaningActivity.tvtotalprice.setText(""+viewHolder.itempriceforcal);
                    }else{
                        Increamentquantity();
                        Totalprice();
                        //DryCleaningActivity.tvtotalprice.setText(""+totalpriceforeachid_household);

                    }




                }else
                {
                    //  if (!(Arraylist_Add_itemID.size() == 1))
                    textaddquantity_household = 0;
                    textaddquantity_household++;
                    viewHolder.tv_itemquantity_household.setText("" + textaddquantity_household);
                    totalpriceforeachid_household =  viewHolder.itempriceforcal* textaddquantity_household;
                    Log.e("toalpriceforelse",""+totalpriceforeachid_household);

                    if(textaddquantity_household==1){
                        Increamentquantity();
                        Totalprice();
                       // DryCleaningActivity.tvtotalprice.setText(""+viewHolder.itempriceforcal);
                    }else{
                        Increamentquantity();
                        Totalprice();
                       // DryCleaningActivity.tvtotalprice.setText(""+totalpriceforeachid_household);

                    }
                    arrayList_count_household.add(viewHolder.tvitemid_household.getText().toString());
                }
            }
        });

        viewHolder.imgsubhousehold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity_household.get(position) > 0)
                    quantity_household.set(position, quantity_household.get(position) - 1);

                if (arrayList_count_household.contains(viewHolder.tvitemid_household.getText().toString())) {
                    textaddquantity_household = textaddquantity_household - 1;
                    viewHolder.tv_itemquantity_household.setText("" + textaddquantity_household);
                    DecrementQuantity();
                    Totalprice();
                   *//* totalpriceforeachid_household =  viewHolder.itempriceforcal* textaddquantity_household;
                    Log.e("totalforsub", "" + totalpriceforeachid_household);

                    DryCleaningActivity.tvtotalprice.setText("" +totalpriceforeachid_household);*//*
                }else
                {
                    //  if (!(Arraylist_Add_itemID.size() == 1))
                    textaddquantity_household = 0;
                    textaddquantity_household--;
                    viewHolder.tv_itemquantity_household.setText("" + textaddquantity_household);
                    *//*arrayList_count_household.add(viewHolder.tvitemid_household.getText().toString());
                    totalpriceforeachid_household =  viewHolder.itempriceforcal* textaddquantity_household;
                    Log.e("totalforsub1", "" + totalpriceforeachid_household);
                    DryCleaningActivity.tvtotalprice.setText("" + totalpriceforeachid_household);*//*
                    DecrementQuantity();
                    Totalprice();

                }

            }
        });*/
        return row;
    }



    private void getreturn_itemquantity(String itemid){
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_itemquantity = adapter.create(UohmacAPI.class);

        api_itemquantity.getitemquantity(getauthkey, itemid, "2", new Callback<Response>() {
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

                reponse_returnitemquantity = sb.toString();
                Log.e("reponse_returnitemquantity", reponse_returnitemquantity);

                try{
                    JSONObject jsonObject = new JSONObject(reponse_returnitemquantity);
                    status = jsonObject.getString("status");
                    totalitemquantity = jsonObject.getString("total_item");
                    Log.d("totalitemquantity_men", totalitemquantity);
                    if(jsonObject.has("msg"))
                        msg = jsonObject.getString("msg");

                    if(status.equals("1")){
                        viewHolder.tvitemid_household.setText(totalitemquantity);

                    }else if(status.equals("0")){
                        //Toast.makeText(WaterItemDetails.this, msg, Toast.LENGTH_SHORT).show();
                        viewHolder.tvitemid_household.setText(totalitemquantity);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getContext(), "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.d("ERROR:", error.getMessage());
            }
        });

    }


    //defining the api to increament the quantity for washfold
    private void Increamentquantity(String Household_itemID){
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_increamentquantity = adapter.create(UohmacAPI.class);

        api_increamentquantity.IncreamentAPI(getauthkey, Household_itemID, new Callback<Response>() {
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
                    JSONObject jsonObject = new JSONObject(response_increament);

                    men_status = jsonObject.getString("status");



                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (men_status.equals("1"))
                {
                    if(AppNetworkInfo.isConnectingToInternet(_context)){
                        Totalprice();


                    } else{
                        Toast.makeText(_context, "No network found.. Please try again", Toast.LENGTH_SHORT).show();
                    }



                }

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getContext(), "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.d("ERROR:", error.getMessage());

            }
        });
    }



    //defining the api to decrement the quantity for wash fold
    private void DecrementQuantity(String household_decre_ItemID){
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_decrementquantity = adapter.create(UohmacAPI.class);

        api_decrementquantity.DecrementAPI(getauthkey, household_decre_ItemID, new Callback<Response>() {
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
                    JSONObject jsonObject = new JSONObject(response_decrement);

                    men_status = jsonObject.getString("status");



                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (men_status.equals("1"))
                {
                    if(AppNetworkInfo.isConnectingToInternet(_context)){
                        Totalprice();


                    } else{
                        Toast.makeText(_context, "No network found.. Please try again", Toast.LENGTH_SHORT).show();
                    }



                }





            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getContext(), "Please try after sometime.", Toast.LENGTH_SHORT).show();

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

        api_totalprice.TotalCartPrice("2",getauthkey, new Callback<Response>() {
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
                } catch (Exception e) {
                    e.printStackTrace();
                }

                DryCleaningActivity.tvtotalprice.setText(totalprice);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getContext(), "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.d("ERROR:", error.getMessage());

            }
        });

    }

    private static class ViewHolder{
        CircleImageView image_household;
        TextView tv_itemname,tv_itemprice,tv_itemquantity_household,tvitemid_household;
        String category_id,itemid_household;
        int itempriceforcal;
        ImageView imgaddhousehold,imgsubhousehold;

    }
}

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
public class WomenAdapter extends ArrayAdapter {



    /******************       gowthaman      *****************/

    int WomenIncrementcount = 0,finalWomenIncrementcount = 0;


    String men_status ="";

    /******************       gowthaman      *****************/


    private Context _context;
    List<DryCleaningPojo> dryCleaningPojos;
    private LayoutInflater inflater ;
    private int resourceId;
    String totalprice="",getauthkey="",status="",msg="",totalitemquantity="",response_increament="",reponse_totalprice="",response_decrement="",reponse_returnitemquantity="";
    JSONObject jsonObject;
    int textaddquantity_women = 0,totalpriceforeachid_women;
    DryCleaningPojo dryCleaningPojo;
    int _resource = R.layout.womensingleitem;
    //public static final String MYPREFERNCES_REGISTER = "mypreferregister";
    //public static final String MYPREFERNCES_POSTFB = "mypreferpostfb";
    public static final String MYPREFERNCES_LOGIN = "mysharedpref_waterlaundry";
    SharedPreferences sp;
    ViewHolder viewHolder;
    ArrayList<String> arrayList_count_women = new ArrayList<String>();
    public ArrayList<Integer> quantity_women = new ArrayList<Integer>();
    public WomenAdapter(Context context, int resource,List<DryCleaningPojo> dryCleaningPojos) {
        super(context, resource, dryCleaningPojos);
        _resource = resource;
        _context = context;
        this.dryCleaningPojos = dryCleaningPojos;
        for(int i=0; i<dryCleaningPojos.size();i++){
            quantity_women.add(0);
        }

      //  sp = getContext().getSharedPreferences(MYPREFERNCES_REGISTER, 0);
        //getting authkey from sp


        sp = getContext().getSharedPreferences(MYPREFERNCES_LOGIN, 0);
        getauthkey = sp.getString("auth_key", "");


       /* sp = getContext().getSharedPreferences(MYPREFERNCES_POSTFB, 0);
        getauthkey = sp.getString("auth_key", "");
        Log.e("gettingauthkeyforsettings", getauthkey);*/
        //totalprice = DataBaseHandler.item_price;

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

            viewHolder.tv_itemname = (TextView) row.findViewById(R.id.txt_womenitemname);
            viewHolder.tv_itemprice = (TextView) row.findViewById(R.id.txt_womenitemprice);
            viewHolder.image_women = (CircleImageView) row.findViewById(R.id.womenimagedrycleaning);
            viewHolder.tv_itemquantity_women = (TextView)row.findViewById(R.id.txt_itempriceincrementforwomen);
            viewHolder.tvitemid_women = (TextView)row.findViewById(R.id.textView58);
            viewHolder.imgaddwomen = (ImageView)row.findViewById(R.id.img_womenadd);
            viewHolder.imgsubwomen = (ImageView)row.findViewById(R.id.img_womensub);
            row.setTag(viewHolder);

        }else{
            row=convertView;
            viewHolder = (ViewHolder) row.getTag();
        }

        if(!TextUtils.isEmpty(dryCleaningPojo.getId())&&!TextUtils.isEmpty(dryCleaningPojo.getItemname())&&!TextUtils.isEmpty(dryCleaningPojo.getItemprice())&&!TextUtils.isEmpty(dryCleaningPojo.getCategoryid())){
            viewHolder.tv_itemname.setText(dryCleaningPojo.getItemname());
            viewHolder.tv_itemprice.setText("\u20B9" +dryCleaningPojo.getItemprice());
            viewHolder.category_id = dryCleaningPojo.getCategoryid();
            viewHolder.itemid_women = dryCleaningPojo.getId();
            viewHolder.tvitemid_women.setText(dryCleaningPojo.getId());
            viewHolder.itempriceforcal = Integer.parseInt(dryCleaningPojo.getItemprice());
            Log.e("getwomenitemname===", dryCleaningPojo.getItemname());
            Log.d("getwomenitemprice", "Post:" + dryCleaningPojo.getItemprice());
            Log.e("getitemidforwomen===", viewHolder.itemid_women);

            if(!TextUtils.isEmpty(dryCleaningPojo.getItemimage())){
                Log.d("DateFound", "Post:" + dryCleaningPojo.getItemimage());

                Picasso.with(_context)
                        .load(dryCleaningPojo.getItemimage())
                        .into(viewHolder.image_women);
            }
        }


        try{

            viewHolder.tv_itemquantity_women.setText(quantity_women.get(position) + "");

        }catch(Exception e){

            Log.d("sqlexeception","exectpoin"+e.toString());
            e.printStackTrace();
        }


        viewHolder.imgaddwomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if ( viewHolder.tv_itemquantity_women.getText().toString().equals("0"))
                {
                    WomenIncrementcount = 1;

                }else
                {
                    WomenIncrementcount = Integer.parseInt(viewHolder.tv_itemquantity_women.getText().toString());
                    finalWomenIncrementcount=WomenIncrementcount+Integer.parseInt(totalitemquantity);
                }



                viewHolder.tv_itemquantity_women.setText("" + finalWomenIncrementcount);

                Log.d("getitemsidsvalues0", "====" + viewHolder.tvitemid_women.getText().toString());


                if(AppNetworkInfo.isConnectingToInternet(_context)){
                    Increamentquantity("" + viewHolder.tvitemid_women.getText().toString());

                } else{
                    Toast.makeText(_context, "No network found.. Please try again", Toast.LENGTH_SHORT).show();
                }


            }
        });


        viewHolder.imgsubwomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (viewHolder.tv_itemquantity_women.getText().toString().equals("0"))
                {
                    return;

                }else
                {
                    finalWomenIncrementcount = Integer.parseInt(viewHolder.tv_itemquantity_women.getText().toString());
                    finalWomenIncrementcount--;
                }
                viewHolder.tv_itemquantity_women.setText("" + finalWomenIncrementcount);

                Log.d("getitemsidsvalues0", "====" + viewHolder.tvitemid_women.getText().toString());
                if(AppNetworkInfo.isConnectingToInternet(_context)){
                    DecrementQuantity("" + viewHolder.tvitemid_women.getText().toString());


                } else{
                    Toast.makeText(_context, "No network found.. Please try again", Toast.LENGTH_SHORT).show();
                }

             //   sdfgdgdsgds


            }
        });

        if(AppNetworkInfo.isConnectingToInternet(getContext())) {
            getreturn_itemquantity("" + viewHolder.tvitemid_women.getText().toString());
        }else{
            Toast.makeText(_context, "No network found.. Please try again", Toast.LENGTH_SHORT).show();

        }

  /*      viewHolder.imgaddwomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity_women.set(position, quantity_women.get(position) + 1);
                Log.d("PositiveNumber", "==" + viewHolder.tv_itemquantity_women.getText().toString());
                Log.d("PositiveNumber", "......" + viewHolder.tvitemid_women.getText().toString());

                if (arrayList_count_women.contains(viewHolder.tvitemid_women.getText().toString()))
                {
                    textaddquantity_women = textaddquantity_women + 1;
                    viewHolder.tv_itemquantity_women.setText("" + textaddquantity_women);
                   *//* totalpriceforeachid_women =  viewHolder.itempriceforcal* textaddquantity_women;
                    Log.e("toalp", "" + totalpriceforeachid_women);*//*
                    //editor.putInt("addingquantity", textaddquantity);
                    //Log.e("adding", "added quantity");
                    //editor.commit();


                    if(textaddquantity_women==1){
                        Increamentquantity();
                        Totalprice();
                        //DryCleaningActivity.tvtotalprice.setText(""+viewHolder.itempriceforcal);
                    }else{
                        Totalprice();
                        Increamentquantity();
                       // DryCleaningActivity.tvtotalprice.setText(""+totalpriceforeachid_women);

                    }




                }else
                {
                    //  if (!(Arraylist_Add_itemID.size() == 1))
                    textaddquantity_women = 0;
                    textaddquantity_women++;
                    viewHolder.tv_itemquantity_women.setText("" + textaddquantity_women);
                    *//*totalpriceforeachid_women =  viewHolder.itempriceforcal* textaddquantity_women;
                    Log.e("toalpriceforelse",""+totalpriceforeachid_women);*//*

                    if(textaddquantity_women==1){
                        Increamentquantity();
                        Totalprice();
                        //DryCleaningActivity.tvtotalprice.setText(""+viewHolder.itempriceforcal);
                    }else{
                        Increamentquantity();
                        Totalprice();
                       // DryCleaningActivity.tvtotalprice.setText(""+totalpriceforeachid_women);

                    }
                    arrayList_count_women.add(viewHolder.tvitemid_women.getText().toString());
                }
            }
        });


        viewHolder.imgsubwomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity_women.get(position) > 0)
                    quantity_women.set(position, quantity_women.get(position) - 1);

                if (arrayList_count_women.contains(viewHolder.tvitemid_women.getText().toString())) {
                    textaddquantity_women = textaddquantity_women - 1;
                    viewHolder.tv_itemquantity_women.setText("" + textaddquantity_women);
                    DecrementQuantity();
                    Totalprice();
                   *//* totalpriceforeachid_women =  viewHolder.itempriceforcal* textaddquantity_women;
                    Log.e("totalforsub", "" + totalpriceforeachid_women);*//*

                    //DryCleaningActivity.tvtotalprice.setText("" +totalpriceforeachid_women);
                }else
                {
                    //  if (!(Arraylist_Add_itemID.size() == 1))
                    textaddquantity_women = 0;
                    textaddquantity_women--;
                    viewHolder.tv_itemquantity_women.setText("" + textaddquantity_women);

                    DecrementQuantity();
                    Totalprice();
                    arrayList_count_women.add(viewHolder.tvitemid_women.getText().toString());
                    *//*totalpriceforeachid_women =  viewHolder.itempriceforcal* textaddquantity_women;
                    Log.e("totalforsub1", "" + totalpriceforeachid_women);
                    DryCleaningActivity.tvtotalprice.setText("" + totalpriceforeachid_women);*//*

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
                        viewHolder.tvitemid_women.setText(totalitemquantity);

                    }else if(status.equals("0")){
                        //Toast.makeText(WaterItemDetails.this, msg, Toast.LENGTH_SHORT).show();
                        viewHolder.tvitemid_women.setText(totalitemquantity);
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
    private void Increamentquantity(String womenitem_id){
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_increamentquantity = adapter.create(UohmacAPI.class);

        api_increamentquantity.IncreamentAPI(getauthkey, womenitem_id, new Callback<Response>() {
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
    private void DecrementQuantity(String women_decrement_itemID){
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_decrementquantity = adapter.create(UohmacAPI.class);

        api_decrementquantity.DecrementAPI(getauthkey, women_decrement_itemID, new Callback<Response>() {
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
        CircleImageView image_women;
        TextView tv_itemname,tv_itemprice,tv_itemquantity_women,tvitemid_women;
         String category_id,itemid_women;
        int itempriceforcal;
        ImageView imgaddwomen,imgsubwomen;

    }
}

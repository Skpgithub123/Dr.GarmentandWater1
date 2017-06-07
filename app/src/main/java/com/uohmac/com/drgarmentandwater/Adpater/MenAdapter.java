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
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import org.w3c.dom.Text;

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
 * Created by System-03 on 11/4/2016.
 */
public class MenAdapter  extends ArrayAdapter {


    /****************** gowthaman *****************/

    int MenIncrementcount = 0,finalMenIncrementcount = 0;


    String men_status ="";



    private Context _context;
    List<DryCleaningPojo> dryCleaningPojos=null;
    private LayoutInflater inflater ;
    private int resourceId;
    int textaddquantity = 0,textsubquantity,totalpriceforeachid;
    int _resource = R.layout.mensingleitem;
    int appendtotalpriceformen;
    public static DryCleaningPojo dryCleaningPojo;
    String totalprice="",status="",totalitemquantity="",msg="",getauthkey="",response_increament="",reponse_totalprice="",response_decrement="",reponse_returnitemquantity="";
    JSONObject jsonObject;
    ImageView imgaddmen,imgsubmen;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
     ViewHolder viewHolder;
    //public static final String MYPREFERNCES_REGISTER = "mypreferregister";
    //public static final String MYPREFERNCES_POSTFB = "mypreferpostfb";
    public static final String MYPREFERNCES_LOGIN = "mysharedpref_waterlaundry";

    //public static final String MYPREFERNCES_TOTALPRICE = "myprefertotalprice";


    ArrayList<String> arrayList_count = new ArrayList<String>();
    public ArrayList<Integer> quantity = new ArrayList<Integer>();

    public MenAdapter(Context context, int resource,List<DryCleaningPojo> dryCleaningPojos) {
        super(context, resource, dryCleaningPojos);
        _resource = resource;
        _context = context;
        this.dryCleaningPojos = dryCleaningPojos;
        for(int i=0; i<dryCleaningPojos.size();i++){
            quantity.add(0);
        }

       // sp = getContext().getSharedPreferences(MYPREFERNCES_REGISTER, 0);
        //getting authkey from sp

        sp = getContext().getSharedPreferences(MYPREFERNCES_LOGIN, 0);
        getauthkey = sp.getString("auth_key", "");

       /* sp = getContext().getSharedPreferences(MYPREFERNCES_POSTFB, 0);
        getauthkey = sp.getString("auth_key", "");
        Log.e("gettingauthkeyforsettings", getauthkey);*/





        // totalprice = DataBaseHandler.item_price;
       /* //calling database
        db = new DataBaseHandler(getContext());*/

        /*if(!TextUtils.isEmpty(sp.getString("itemprice", ""))) {
            sp.getString("itemprice", "");

            DryCleaningActivity.tvtotalprice.setText(sp.getString("itemprice", ""));
        }*/

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

            viewHolder.tv_itemname = (TextView)row.findViewById(R.id.txt_menitemname);
            viewHolder.tv_itemprice = (TextView) row.findViewById(R.id.txt_menitemprice);
            viewHolder.image_men = (CircleImageView) row.findViewById(R.id.menimagedrycleaning);
            viewHolder.tv_itemquantity = (TextView)row.findViewById(R.id.txt_itemqtyincrement);
            viewHolder.tvitemid = (TextView)row.findViewById(R.id.textView57);
            viewHolder.imgaddmen = (ImageView)row.findViewById(R.id.img_menadd);
            viewHolder.imgsubmen = (ImageView)row.findViewById(R.id.img_mensub);


            row.setTag(viewHolder);

        }else {
            row=convertView;
            viewHolder = (ViewHolder) row.getTag();
        }
        if(!TextUtils.isEmpty(dryCleaningPojo.getId())&&!TextUtils.isEmpty(dryCleaningPojo.getItemname())&&!TextUtils.isEmpty(dryCleaningPojo.getItemprice())&&!TextUtils.isEmpty(dryCleaningPojo.getCategoryid())){
            viewHolder.tv_itemname.setText(dryCleaningPojo.getItemname());
            viewHolder.tv_itemprice.setText("\u20B9" +dryCleaningPojo.getItemprice());
            viewHolder.tvitemid.setText(dryCleaningPojo.getId());
            viewHolder.category_id = dryCleaningPojo.getCategoryid();
            viewHolder.itemid = dryCleaningPojo.getId();
            viewHolder.itempriceforcal = Integer.parseInt(dryCleaningPojo.getItemprice());
            Log.e("getitemname===", dryCleaningPojo.getItemname());
            Log.d("getitemprice", "Post:" + dryCleaningPojo.getItemprice());
            //Log.e("getcategoryid===", dryCleaningPojo.getCategoryid());
            Log.e("getitemidformen===", dryCleaningPojo.getId());
            if(!TextUtils.isEmpty(dryCleaningPojo.getItemimage())){
                Log.d("DateFound", "Post:" + dryCleaningPojo.getItemimage());

                Picasso.with(_context)
                        .load(dryCleaningPojo.getItemimage())
                        .into(viewHolder.image_men);
            }
        }

        // final TextView tv_itemquantity = (TextView)convertView.findViewById(R.id.txt_itemqtyincrement);

        //imgaddmen.setTag(new Integer(position));

        try{

            viewHolder.tv_itemquantity.setText(quantity.get(position) + "");
/*
                String valuequantity = positiveNumbers.get(listViewHolder.tv_itemid.getText().toString());
                Log.d("valu+++e", "++++" + valuequantity);
                if (valuequantity != null)
                {
                    Log.d("getite", "******1*****" + positiveNumbers.get(listViewHolder.tv_itemid.getText().toString()));
                    Log.d("getite","******22*****"+positiveNumbers.size());

                    listViewHolder.tv_noof_qty.setText(positiveNumbers.get(listViewHolder.tv_itemid));
                }*/

        }catch(Exception e){

            Log.d("sqlexeception","exectpoin"+e.toString());
            e.printStackTrace();
        }

        viewHolder.imgaddmen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                if ( viewHolder.tv_itemquantity.getText().toString().equals("0"))
                {
                    MenIncrementcount = 1;

                }else
                {
                    MenIncrementcount = Integer.parseInt(viewHolder.tv_itemquantity.getText().toString());
                    finalMenIncrementcount = MenIncrementcount+Integer.parseInt(totalitemquantity);
                }



                viewHolder.tv_itemquantity.setText("" + finalMenIncrementcount);

                Log.d("getitemsidsvalues0", "====" + viewHolder.tvitemid.getText().toString());

                if(AppNetworkInfo.isConnectingToInternet(_context)){
                    Increamentquantity("" + viewHolder.tvitemid.getText().toString());


                } else{
                    Toast.makeText(_context, "No network found.. Please try again", Toast.LENGTH_SHORT).show();
                }

              //  Totalprice();
            }
        });

        viewHolder.imgsubmen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (viewHolder.tv_itemquantity.getText().toString().equals("0")) {
                    return;

                } else {
                    finalMenIncrementcount = Integer.parseInt(viewHolder.tv_itemquantity.getText().toString());
                    finalMenIncrementcount--;
                }
                viewHolder.tv_itemquantity.setText("" + finalMenIncrementcount);

                Log.d("getitemsidsvalues0", "====" + viewHolder.tvitemid.getText().toString());

                if (AppNetworkInfo.isConnectingToInternet(_context)) {
                    DecrementQuantity("" + viewHolder.tvitemid.getText().toString());


                } else {
                    Toast.makeText(_context, "No network found.. Please try again", Toast.LENGTH_SHORT).show();
                }


            }
        });

                if(AppNetworkInfo.isConnectingToInternet(getContext())) {
                    //retrofit to get return_itemquantity
                    getreturn_itemquantity("" + viewHolder.tvitemid.getText().toString());
                }else{
                    Toast.makeText(_context, "No network found.. Please try again", Toast.LENGTH_SHORT).show();
                }
/*

        viewHolder.imgaddmen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(), "ImageView clicked for the row = " + v.getTag().toString(), Toast.LENGTH_SHORT).show();

                quantity.set(position, quantity.get(position) + 1);
                Log.d("PositiveNumber", "==" + viewHolder.tv_itemquantity.getText().toString());
                Log.d("PositiveNumber", "......" + viewHolder.tvitemid.getText().toString());


                if (arrayList_count.contains(viewHolder.tvitemid.getText().toString())) {
                    textaddquantity = textaddquantity + 1;
                    viewHolder.tv_itemquantity.setText(String.valueOf(textaddquantity));
                    Increamentquantity();
                    Totalprice();
                    // editor.putInt("addingquantity", textaddquantity);
                    //Log.e("adding", "added quantity");
                    // editor.commit();


                    if (textaddquantity == 1) {



                        Totalprice();
                        */
/*DryCleaningActivity.tvtotalprice.setText("" + viewHolder.itempriceforcal);
                        editor.putString("itemprice", String.valueOf(viewHolder.itempriceforcal));
                        editor.putString("menitemid",viewHolder.itemid);
                       // db.add(new StoredItems("itemname", String.valueOf(viewHolder.itempriceforcal)));
                       // db.add(new StoredItems("itemid", viewHolder.itemid));
                        Log.e("inserting..", "insertingformensingle");
                        editor.commit();


                        appendtotalpriceformen = DryCleaningActivity.gettotalpricefordrycleaning+viewHolder.itempriceforcal;
                        Log.e("appendpriceformen", "" + appendtotalpriceformen);
                        editor.putString("itemprice", ""+appendtotalpriceformen).commit();
                        DryCleaningActivity.tvtotalprice.setText("" + appendtotalpriceformen);*//*

                        // db.updateContact();
                    } else {


                        Totalprice();
                        */
/*totalpriceforeachid =  viewHolder.itempriceforcal* textaddquantity;
                        Log.e("toalp", "" + totalpriceforeachid);
                        //DryCleaningActivity.tvtotalprice.setText("" +totalpriceforeachid);
                       // editor.putString("itempriceformultiple", String.valueOf(totalpriceforeachid));
                       // editor.putString("menitemidformultiple",viewHolder.itemid);
                       // db.add(new StoredItems("itemname", String.valueOf(totalpriceforeachid)));
                        //db.add(new StoredItems("itemid", viewHolder.itemid));
                       // Log.e("inserting..", "insertingformenmulitple");
                       // editor.commit();

                        appendtotalpriceformen = DryCleaningActivity.gettotalpricefordrycleaning+totalpriceforeachid;
                        Log.e("appendpriceformen", "" + appendtotalpriceformen);
                        editor.putString("itemprice", ""+appendtotalpriceformen).commit();
                        DryCleaningActivity.tvtotalprice.setText("" +appendtotalpriceformen);
                       //db.updateContact();
*//*

                    }



                            */
/*if ()
                            if (!(Arraylist_Add_itemID.size() == 1))
                            Arraylist_Add_itemID.remove(listViewHolder.tv_itemid.getText().toString());*//*

                    // Arraylist_Add_itemID.add(listViewHolder.tv_itemid.getText().toString());
                } else {
                    //  if (!(Arraylist_Add_itemID.size() == 1))
                    textaddquantity = 0;
                    textaddquantity++;
                    viewHolder.tv_itemquantity.setText("" + textaddquantity);
                    int totalpriceforacctoitemid = viewHolder.itempriceforcal + totalpriceforeachid;
                    Log.e("toalpriceforelse", "" + totalpriceforacctoitemid);
                    Increamentquantity();
                    if (textaddquantity == 1) {
                        // DryCleaningActivity.tvtotalprice.setText("" + viewHolder.itempriceforcal);
                        */
/*db.add(new StoredItems("itemname", String.valueOf(viewHolder.itempriceforcal)));
                        db.add(new StoredItems("itemid", viewHolder.itemid));
                        db.updateContact();*//*

                        Totalprice();
                    } else {
                        // DryCleaningActivity.tvtotalprice.setText(""+totalpriceforacctoitemid);
                        */
/*db.add(new StoredItems("itemname", String.valueOf(viewHolder.itempriceforcal)));
                        db.add(new StoredItems("itemid", viewHolder.itemid));
                        db.updateContact();*//*

                        Totalprice();
                    }
                    arrayList_count.add(viewHolder.tvitemid.getText().toString());
                }


               */
/* Log.d("ïtemsidvalies","******"+viewHolder.itemid);
                arrayList_count.add(viewHolder.itemid);


                Log.d("sizeofarray","*****"+arrayList_count.size());


                if (arrayList_count.contains( viewHolder.itemid))
                {
                    //arrayList_count.remove()
                }else
                {
                    tv_itemquantity.setText("" + textaddquantity);
                    textaddquantity = 0;
                    textaddquantity++;
                    tv_itemquantity.setText("" + textaddquantity);

                }*//*



                // quantity.add(textaddquantity);

            }
        });
*/


       /* viewHolder.imgsubmen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity.get(position) > 0)
                    quantity.set(position, quantity.get(position) - 1);

                if (arrayList_count.contains(viewHolder.tvitemid.getText().toString())) {
                    textaddquantity = textaddquantity - 1;
                    viewHolder.tv_itemquantity.setText(String.valueOf(textaddquantity));

                    DecrementQuantity();
                    Totalprice();
                    *//*totalpriceforeachid =  viewHolder.itempriceforcal* textaddquantity;
                    Log.e("totalforsub", "" + totalpriceforeachid);

                    DryCleaningActivity.tvtotalprice.setText("" + totalpriceforeachid);*//*
                    *//*db.add(new StoredItems("itemname", String.valueOf(totalpriceforeachid)));
                    db.add(new StoredItems("itemid", viewHolder.itemid));
                    db.updateContact();*//*
                }else
                {
                    //  if (!(Arraylist_Add_itemID.size() == 1))
                    textaddquantity = 0;
                    textaddquantity--;
                    viewHolder.tv_itemquantity.setText(String.valueOf(textaddquantity));

                    DecrementQuantity();
                    Totalprice();
                   *//* arrayList_count.add(viewHolder.tvitemid.getText().toString());
                    totalpriceforeachid =  viewHolder.itempriceforcal* textaddquantity;
                    Log.e("totalforsub1", "" + totalpriceforeachid);
                    DryCleaningActivity.tvtotalprice.setText("" + totalpriceforeachid);*//*
                   *//* db.add(new StoredItems("itemname", String.valueOf(totalpriceforeachid)));
                    db.add(new StoredItems("itemid", viewHolder.itemid));
                    db.updateContact();*//*
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
                        viewHolder.tv_itemquantity.setText(totalitemquantity);

                    }else if(status.equals("0")){
                        //Toast.makeText(WaterItemDetails.this, msg, Toast.LENGTH_SHORT).show();
                        viewHolder.tv_itemquantity.setText(totalitemquantity);
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
    private void Increamentquantity(String getitem_id){
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_increamentquantity = adapter.create(UohmacAPI.class);

        api_increamentquantity.IncreamentAPI(getauthkey, getitem_id, new Callback<Response>() {
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
    private void DecrementQuantity(String item_id){
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_decrementquantity = adapter.create(UohmacAPI.class);

        api_decrementquantity.DecrementAPI(getauthkey, item_id, new Callback<Response>() {
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


   /* public void myClickHandler(View v){

        viewHolder.rr = (RelativeLayout)v.getParent();

        viewHolder.tv_itemquantity = (TextView)viewHolder.rr.getChildAt(0);
        viewHolder.tv_itemquantity.setText("1");

        v.getTag().toString();

    }*/

    public class ViewHolder{
        CircleImageView image_men;
        TextView tv_itemname,tv_itemprice,tv_itemquantity,tvitemid;
        String category_id,itemid;
        int itempriceforcal;
        ImageView imgsubmen,imgaddmen;
        RelativeLayout rr;
        /*public ViewHolder(View row) {
            tv_itemname = (TextView) row.findViewById(R.id.txt_menitemname);
            tv_itemprice = (TextView) row.findViewById(R.id.txt_menitemprice);
            image_men = (CircleImageView) row.findViewById(R.id.menimagedrycleaning);
            tvitemid = (TextView)row.findViewById(R.id.textView57);
            rr = (RelativeLayout)row.findViewById(R.id.relative);
            imgsubmen = (ImageView)row.findViewById(R.id.img_mensub);
        }*/


    }
}

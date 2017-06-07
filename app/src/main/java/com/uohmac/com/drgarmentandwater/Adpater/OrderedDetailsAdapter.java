/*
package com.uohmac.com.drgarmentandwater.Adpater;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uohmac.com.drgarmentandwater.Activites.DryCleaningActivity;
import com.uohmac.com.drgarmentandwater.Activites.Laundryitemdetails;
import com.uohmac.com.drgarmentandwater.Activites.OrderedItemsDetails;
import com.uohmac.com.drgarmentandwater.Api.UohmacAPI;
import com.uohmac.com.drgarmentandwater.Fragments.ChangePasswordFragment;
import com.uohmac.com.drgarmentandwater.Pojos.OrderedDetailsPojo;
import com.uohmac.com.drgarmentandwater.R;
import com.uohmac.com.drgarmentandwater.Utils.Constatns;

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

*/
/**
 * Created by uOhmac Technologies on 02-Jan-17.
 *//*

public class OrderedDetailsAdapter extends RecyclerView.Adapter<OrderedDetailsAdapter.ViewHolder> {


    */
/******************       gowthaman      *****************//*


    int Cared_Incrementcount = 0;


    String card_status ="";


    */
/******************       gowthaman      *****************//*






    Context context;
    List<OrderedDetailsPojo> orderedDetailsPojoList;
    String item_id_fromcart,price_fromcart,item_name_fromcart,item_img_fromcart;
    int no_of_item_fromcart;
    String totalprice,getauthkey,response_increament,reponse_totalprice,response_decrement,reponse_removeitem,status_itemprice,item_price_add,item_price_sub;
    OrderedDetailsPojo orderedDetailsPojo;
    int txt_itemqtyincrement_fromcart;
    JSONObject jsonObject;
    ArrayList<String> arrayList_count_women = new ArrayList<String>();
    public ArrayList<Integer> quantity_fromcart = new ArrayList<Integer>();
    public static final String MYPREFERNCES_REGISTER = "mypreferregister";
    public static final String MYPREFERNCES_POSTFB = "mypreferpostfb";
    SharedPreferences sp;
    String totalprice_globally;
    int _resource = R.layout.ordereditems_single_item;

    public OrderedDetailsAdapter(Context context,int resource, List<OrderedDetailsPojo> orderedDetailsPojoList) {
        this.orderedDetailsPojoList = orderedDetailsPojoList;
        this.context = context;

    }

    @Override
    public OrderedDetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        sp = context.getSharedPreferences(MYPREFERNCES_REGISTER, 0);
        //getting authkey from sp

        sp = context.getSharedPreferences(MYPREFERNCES_POSTFB, 0);
        getauthkey = sp.getString("auth_key", "");
        Log.e("gettingauthkeyforsettings", getauthkey);


        Totalprice();


        View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.ordereditems_single_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final OrderedDetailsAdapter.ViewHolder holder, final int position) {
        orderedDetailsPojo = orderedDetailsPojoList.get(position);
        if(!TextUtils.isEmpty(orderedDetailsPojo.getId())&&!TextUtils.isEmpty(orderedDetailsPojo.getNo_of_item())&&!TextUtils.isEmpty(orderedDetailsPojo.getPrice())&&!TextUtils.isEmpty(orderedDetailsPojo.getItemname())&&!TextUtils.isEmpty(orderedDetailsPojo.getItemimage())){
            holder.tvitemid.setText(orderedDetailsPojo.getId());
            holder.tv_itemquantity.setText(orderedDetailsPojo.getNo_of_item());
            holder.tv_itemprice.setText(orderedDetailsPojo.getPrice());
            holder.tv_itemname.setText(orderedDetailsPojo.getItemname());

            item_id_fromcart = orderedDetailsPojo.getId();

            no_of_item_fromcart = Integer.parseInt(orderedDetailsPojo.getNo_of_item());
            Log.e("no_of_items", ""+no_of_item_fromcart);

            price_fromcart = orderedDetailsPojo.getPrice();
            //setting previous totalprice for ordereditems
           // holder.tv_totalprice_fromcart.setText(totalprice);



            if(!TextUtils.isEmpty(Laundryitemdetails.totalprice)) {
                //gettotalpricefordrycleaning = Integer.valueOf(sp.getString("itemprice", ""));
                //Log.e("gettotalpricefordrycleaning", ""+ gettotalpricefordrycleaning);
                holder.txt_totalprice_fromcart.setText(Laundryitemdetails.totalprice);
                Log.d("getavailprice_fromcart", "" + Laundryitemdetails.totalprice);
            }


            if(!TextUtils.isEmpty(orderedDetailsPojo.getItemimage())) {
                Log.d("DateFound", "Post:" + orderedDetailsPojo.getItemimage());

                Picasso.with(context)
                        .load(orderedDetailsPojo.getItemimage())
                        .into(holder.image_men);
            }
        }



        holder.imgaddmen.setOnClickListener(new View.OnClickListener() {
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
                Increamentquantity("" + holder.tvitemid.getText().toString());




          //        sdffgdggfdgfdfg


            }
        });


        holder.imgsubmen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {









            }
        });

      */
/*  holder.imgaddmen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                quantity_fromcart.set(position, quantity_fromcart.get(position) + 1);
                Log.d("PositiveNumber", "==" + holder.tv_itemquantity.getText().toString());
                Log.d("PositiveNumber", "......" + holder.tvitemid.getText().toString());


                if (arrayList_count_women.contains(holder.tvitemid.getText().toString()))
                {
                    no_of_item_fromcart = no_of_item_fromcart + 1;
                    Log.d("quantity_increament", "" + no_of_item_fromcart);
                    holder.tv_itemquantity.setText(String.valueOf(no_of_item_fromcart));

                    Increamentquantity();
                   // Totalprice();
                    holder.tv_itemprice.setText(item_price_add);
                    holder.txt_totalprice_fromcart.setText(totalprice);

                        // holder.tv_totalprice_fromcart.setText(totalprice);
                        // DryCleaningActivity.tvtotalprice.setText(""+totalpriceforeachid_women);


                        //holder.tv_totalprice_fromcart.setText(totalprice);



                }*//*
*/
/*else{
                    //no_of_item_fromcart = 0;
                   // no_of_item_fromcart++;
                    //holder.tv_itemquantity.setText(String.valueOf(no_of_item_fromcart));
                    *//*
*/
/**//*
*/
/*totalpriceforeachid_women =  viewHolder.itempriceforcal* textaddquantity_women;
                    Log.e("toalpriceforelse",""+totalpriceforeachid_women);*//*
*/
/**//*
*/
/*


                    //Increamentquantity();
                   // Totalprice();
                    Increamentquantity();
                    holder.tv_itemprice.setText(item_price_add);
                    holder. txt_totalprice_fromcart.setText(totalprice);

                }*//*
*/
/*
                arrayList_count_women.add(holder.tvitemid.getText().toString());
            }
        });
*//*


*/
/*
        holder.imgsubmen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               *//*
*/
/* if (quantity_fromcart.get(position) > 0)
                    quantity_fromcart.set(position, quantity_fromcart.get(position) - 1);
*//*
*/
/*

                if (no_of_item_fromcart > 0) {
                    if (arrayList_count_women.contains(holder.tvitemid.getText().toString())) {
                        no_of_item_fromcart = no_of_item_fromcart - 1;
                        holder.tv_itemquantity.setText(String.valueOf(no_of_item_fromcart));
                        DecrementQuantity();
                       // Totalprice();
                        holder.tv_itemprice.setText(item_price_sub);
                        holder.txt_totalprice_fromcart.setText(totalprice);


                        //holder.tv_totalprice_fromcart.setText(totalprice);
                    } else {
                        no_of_item_fromcart = 0;
                        no_of_item_fromcart--;
                        holder.tv_itemquantity.setText(String.valueOf(no_of_item_fromcart));

                        DecrementQuantity();
                       //Totalprice();
                        holder.tv_itemprice.setText(item_price_sub);
                        holder.txt_totalprice_fromcart.setText(totalprice);

                    }
                }


            }
        });*//*



        holder.btn_removeitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemoveItem();
            }
        });


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

                }catch (Exception e){
                    e.printStackTrace();
                }


                if (status_itemprice.equals("1"))
                {
                   // tv_itemprice

                   // fgfgfggfgfggffddffgfgfggffggffg
                }



                Log.d("statusitemprice","===="+status_itemprice + "  "+item_price_add);





            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("ERROR:", error.getMessage());

            }
        });
    }



    //defining the api to decrement the quantity for wash fold
    private void DecrementQuantity(){
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_decrementquantity = adapter.create(UohmacAPI.class);

        api_decrementquantity.DecrementAPI(getauthkey, orderedDetailsPojo.getId(), new Callback<Response>() {
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
                }catch (Exception e){
                    e.printStackTrace();
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

        api_totalprice.TotalCartPrice(getauthkey, new Callback<Response>() {
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
                    //txt_totalprice_fromcart.setText(totalprice);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("ERROR:", error.getMessage());

            }
        });

    }


    private void RemoveItem(){
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_removeitem = adapter.create(UohmacAPI.class);

        api_removeitem.RemoveItem(getauthkey, orderedDetailsPojo.getId(), new Callback<Response>() {
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
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("ERROR:", error.getMessage());

            }
        });

    }



    @Override
    public int getItemCount() {
        return orderedDetailsPojoList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image_men;
        TextView tv_itemname,tv_itemprice,tv_itemquantity,tvitemid,tv_totalprice_fromcart,txt_totalprice_fromcart;
        String category_id,itemid;
        int itempriceforcal;
        ImageView imgsubmen,imgaddmen;
        Button btn_removeitem;
        RelativeLayout relativeLayout;

        public ViewHolder(View row) {
            super(row);
            tv_itemname = (TextView)row.findViewById(R.id.txt_menitemname_fromcart);
            tv_itemprice = (TextView) row.findViewById(R.id.txt_menitemprice_fromcart);
            image_men = (CircleImageView) row.findViewById(R.id.menimagedrycleaning_fromcart);
            tv_itemquantity = (TextView)row.findViewById(R.id.txt_itemqtyincrement_fromcart);
           // tv_totalprice_fromcart = (TextView)row.findViewById(R.id.totalprice_fromcart);
            txt_totalprice_fromcart = (TextView)row.findViewById(R.id.txt_storedtotalprice_fromcart);
            tvitemid = (TextView)row.findViewById(R.id.textView57_fromcart);
            imgaddmen = (ImageView)row.findViewById(R.id.img_menadd_fromcart);
            imgsubmen = (ImageView)row.findViewById(R.id.img_mensub_fromcart);
            btn_removeitem = (Button)row.findViewById(R.id.bthremoveitem);
        }
    }

    }
*/

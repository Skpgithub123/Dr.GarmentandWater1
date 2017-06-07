package com.uohmac.com.drgarmentandwater.Adpater;

import android.app.AlertDialog;
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

import com.uohmac.com.drgarmentandwater.Activites.OrderedItemsDetailsForLaundry;
import com.uohmac.com.drgarmentandwater.Api.UohmacAPI;
import com.uohmac.com.drgarmentandwater.Pojos.PromoCodesPojo;
import com.uohmac.com.drgarmentandwater.R;
import com.uohmac.com.drgarmentandwater.Utils.AppNetworkInfo;
import com.uohmac.com.drgarmentandwater.Utils.Constatns;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by uOhmac Technologies on 11-Jan-17.
 */
public class LaundryPromoCodeListAdapter extends ArrayAdapter {


    private Context _context;
    ViewHolder holder;
    int _resource = R.layout.laundrypromocodeviewing_singleitem;
    List<PromoCodesPojo> promoCodesPojoList=null;
    public static PromoCodesPojo promoCodesPojo;
    //public static final String MYPREFERNCES_REGISTER = "mypreferregister";
   // public static final String MYPREFERNCES_POSTFB = "mypreferpostfb";
    public static final String MYPREFERNCES_LOGIN = "mysharedpref_waterlaundry";
    SharedPreferences sp;
    private LayoutInflater inflater ;
    String getauthkey,response_promocode,status,msg_promocode,promobenefit;
    JSONObject jsonObject;

    public LaundryPromoCodeListAdapter(Context context, int resource, List<PromoCodesPojo> promoCodesPojoList) {
        super(context, resource,promoCodesPojoList);

        _resource = resource;
        _context = context;
        this.promoCodesPojoList = promoCodesPojoList;



        sp = getContext().getSharedPreferences(MYPREFERNCES_LOGIN, 0);
        getauthkey = sp.getString("auth_key", "");
        Log.e("gettingauthkey_promocodelist", getauthkey);
       /* sp = context.getSharedPreferences(MYPREFERNCES_POSTFB, 0);
        getauthkey = sp.getString("auth_key", "");
        Log.e("gettingauthkeyforsettings", getauthkey);*/


    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

       // promoCodesPojo = (PromoCodesPojo) getItem(position);
        promoCodesPojo = promoCodesPojoList.get(position);
        View row;

        if(convertView == null){
            inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(_resource, parent, false);
            holder = new ViewHolder();


            holder.tv_promotitle = (TextView)row.findViewById(R.id.txt_promocodetitle);
            holder.tv_minamount = (TextView) row.findViewById(R.id.txt_minamount);
            holder.tv_promoamount = (TextView)row.findViewById(R.id.txt_promoamount);
            holder.btn_apply_directly = (Button)row.findViewById(R.id.btnpromoapply_directly);
            holder.txt_percentagevalue = (TextView)row.findViewById(R.id.txt_percentagevalue);


            holder.btn_apply_directly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(AppNetworkInfo.isConnectingToInternet(_context)){
                        ApplyLaundryPromoCodes_List();


                    } else{
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
                                ApplyLaundryPromoCodes_List();


                            }
                        });
                        ab.create();
                        ab.show();
                    }
                }
            });


            row.setTag(holder);

        }else{
            row=convertView;
            holder = (ViewHolder) row.getTag();
        }
        if(!TextUtils.isEmpty(promoCodesPojo.getPromotitle())&&!TextUtils.isEmpty(promoCodesPojo.getMinamount())&&!TextUtils.isEmpty(promoCodesPojo.getPromoamount())&&!TextUtils.isEmpty(promoCodesPojo.getAmountwise())&&!TextUtils.isEmpty(promoCodesPojo.getUserpercust())&&!TextUtils.isEmpty(promoCodesPojo.getPromovalidity())){
            holder.tv_promotitle.setText(promoCodesPojo.getPromotitle());
            holder.tv_minamount.setText(promoCodesPojo.getMinamount());
            holder.tv_promoamount.setText(promoCodesPojo.getPromoamount());
            holder.txt_percentagevalue.setText(promoCodesPojo.getAmountwise());
            Log.d("promotitle", promoCodesPojo.getPromotitle());

            holder.promotitle = promoCodesPojo.getPromotitle();

            holder.amountwise = promoCodesPojo.getAmountwise();
            holder.percusotmer = promoCodesPojo.getUserpercust();
            holder.promovalidity = promoCodesPojo.getPromovalidity();


            if(holder.txt_percentagevalue.getText().toString().equals("p")){
                holder.txt_percentagevalue.setText("Get Discount of" +" "+holder.tv_promoamount.getText().toString());
            }else if(holder.txt_percentagevalue.getText().toString().equals("f")){
                holder.txt_percentagevalue.setText("Get Flat Discount of" +" "+holder.tv_promoamount.getText().toString());
            }

           /* String checktotalprice = OrderedItemsDetailsForLaundry.totalprice;
            Log.d("checktotlaprice", checktotalprice);*/
        }
        return row;
    }


    public class ViewHolder{
        TextView tv_promotitle, tv_minamount, tv_promoamount, tvitemid, tv_totalprice_fromcart,txt_percentagevalue;
        String promotitle, minamount,promoamount,amountwise,percusotmer,promovalidity;
        int itempriceforcal;
        ImageView imgsubmen, imgaddmen;
        Button btn_apply_directly;
        RelativeLayout relativeLayout;
    }




    //Promo code api calling
    private void ApplyLaundryPromoCodes_List(){
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_promocode= adapter.create(UohmacAPI.class);

        api_promocode.PromoCode(getauthkey,  holder.tv_promotitle.getText().toString(), OrderedItemsDetailsForLaundry.totalprice, new Callback<Response>() {
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
                Log.e("getre_promocodeslist", response_promocode);

                try{
                    jsonObject = new JSONObject(response_promocode);
                    status = jsonObject.getString("status");
                    msg_promocode = jsonObject.getString("msg");
                    promobenefit = jsonObject.getString("promo_benefit");
                    if(status.equals("1")){
                        Toast.makeText(_context, msg_promocode, Toast.LENGTH_SHORT).show();
                        int totalpriceafter_coupon = Integer.parseInt(OrderedItemsDetailsForLaundry.totalprice) - Integer.parseInt(promobenefit);
                        OrderedItemsDetailsForLaundry.txt_totalprice_fromcart.setText(totalpriceafter_coupon);

                        Intent i = new Intent(getContext(),OrderedItemsDetailsForLaundry.class);
                        getContext().startActivity(i);
                    }else if(status.equals("0")){
                        Toast.makeText(_context, msg_promocode, Toast.LENGTH_SHORT).show();

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getContext(), "Please try after sometime.", Toast.LENGTH_SHORT).show();

                Log.e("Errorfound", "ERROR:" + error.getMessage(), error);

            }
        });
    }

}

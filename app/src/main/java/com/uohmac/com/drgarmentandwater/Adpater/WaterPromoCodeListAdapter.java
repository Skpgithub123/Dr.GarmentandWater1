package com.uohmac.com.drgarmentandwater.Adpater;

import android.content.Context;
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

import com.uohmac.com.drgarmentandwater.Activites.DeliveryAddrDetailsforWater;
import com.uohmac.com.drgarmentandwater.Activites.OrderedItemsDetailsForLaundry;
import com.uohmac.com.drgarmentandwater.Activites.OrderedItemsDetailsForWater;
import com.uohmac.com.drgarmentandwater.Activites.WaterItemDetails;
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
 * Created by uOhmac Technologies on 22-Mar-17.
 */
public class WaterPromoCodeListAdapter extends ArrayAdapter {


    private Context _context;
    ViewHolder holder;
    int _resource = R.layout.waterpromocodeviewing_signleitem;
    List<PromoCodesPojo> promoCodesPojoList=null;
    public static PromoCodesPojo promoCodesPojo;
    public static final String MYPREFERNCES_LOGIN = "mysharedpref_waterlaundry";
    SharedPreferences sp;
    private LayoutInflater inflater ;
    String getauthkey,response_promocode,status,msg_promocode,promobenefit;
    JSONObject jsonObject;

    public WaterPromoCodeListAdapter(Context context, int resource,List<PromoCodesPojo> promoCodesPojoList) {
        super(context, resource, promoCodesPojoList);
        _resource = resource;
        _context = context;
        this.promoCodesPojoList = promoCodesPojoList;

        sp = getContext().getSharedPreferences(MYPREFERNCES_LOGIN, 0);
        getauthkey = sp.getString("auth_key", "");

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

            holder.tv_promotitle = (TextView)row.findViewById(R.id.txt_waterpromocodetitle);
            holder.tv_minamount = (TextView) row.findViewById(R.id.txt_waterminamount);
            holder.tv_promoamount = (TextView)row.findViewById(R.id.txt_waterpromoamount);
            holder.btn_apply_directly = (Button)row.findViewById(R.id.btnwaterpromoapply_directly);


            holder.btn_apply_directly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(AppNetworkInfo.isConnectingToInternet(getContext())){
                        ApplyWaterPromoCodes_List();
                    }else{
                        Toast.makeText(_context, "No network found... Please try again later.!!!", Toast.LENGTH_LONG).show();
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


            holder.promotitle = promoCodesPojo.getPromotitle();
            holder.amountwise = promoCodesPojo.getAmountwise();
            holder.percusotmer = promoCodesPojo.getUserpercust();
            holder.promovalidity = promoCodesPojo.getPromovalidity();
        }
        return row;
    }

    public class ViewHolder{
        TextView tv_promotitle, tv_minamount, tv_promoamount, tvitemid, tv_totalprice_fromcart;
        String promotitle, minamount,promoamount,amountwise,percusotmer,promovalidity;
        int itempriceforcal;
        ImageView imgsubmen, imgaddmen;
        Button btn_apply_directly;
        RelativeLayout relativeLayout;
    }

    //Promo code api calling
    private void ApplyWaterPromoCodes_List(){
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constatns.UOHMAC_API)
                .build();

        UohmacAPI api_promocode= adapter.create(UohmacAPI.class);

        api_promocode.PromoCode(getauthkey,  holder.promotitle, WaterItemDetails.totalprice, new Callback<Response>() {
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
                        Intent i = new Intent(getContext(), DeliveryAddrDetailsforWater.class);
                        int totalpriceafter_coupon = Integer.parseInt(OrderedItemsDetailsForWater.totalprice) - Integer.parseInt(promobenefit);
                        OrderedItemsDetailsForWater.tv_totalandfinalprice.setText(totalpriceafter_coupon);
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

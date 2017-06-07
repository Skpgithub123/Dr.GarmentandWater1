/*
package com.uohmac.com.drgarmentandwater.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uohmac.com.drgarmentandwater.Api.UohmacAPI;
import com.uohmac.com.drgarmentandwater.Pojos.DryCleaningPojo;
import com.uohmac.com.drgarmentandwater.Pojos.RecentOrderPojo;
import com.uohmac.com.drgarmentandwater.R;
import com.uohmac.com.drgarmentandwater.Utils.Constatns;

import org.json.JSONArray;
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
 * Created by uOhmac Technologies on 24-Jan-17.
 *//*

public class RecentOrderLaundryAdapter extends ArrayAdapter{


    Context _context;
    private LayoutInflater inflater ;

   // ArrayList<VariableClass>  arrayList = new ArrayList<>();
    //VariableClass variableClass;
    List<RecentOrderPojo> recentOrderPojos=null;
    RecentOrderPojo recentOrderPojo;
    int _resource = R.layout.recentorderlaundrysingle_item;
    public static String status;

    public RecentOrderLaundryAdapter(Context context, int resource,  List<RecentOrderPojo> recentOrderPojos) {
        super(context, resource,recentOrderPojos);
        _resource = resource;
        _context = context;
        this.recentOrderPojos = recentOrderPojos;
        //sp = getContext().getSharedPreferences(MYPREFERNCES_LOGIN, 0);
       // getauthkey = sp.getString("auth_key", "");



    }

    @Override
    public int getCount() {
        return recentOrderPojos.size();
    }

    @Override
    public Object getItem(int i) {
        return recentOrderPojos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {



        recentOrderPojo = (RecentOrderPojo) getItem(i);

        recentOrderPojo = recentOrderPojos.get(i);

        View view;
        final ViewHolder viewHolder;

        if(convertView == null) {
            inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(_resource, viewGroup, false);

           // Context context = viewGroup.getContext();
           // view = LayoutInflater.from(context).inflate(R.layout.recentorderlaundrysingle_item, viewGroup);
            viewHolder = new ViewHolder();

            viewHolder.tv_orderno = (TextView)view.findViewById(R.id.txt_ordernovalue);
            viewHolder.tv_date = (TextView)view.findViewById(R.id.date_value);
            viewHolder.tv_pickupdate = (TextView)view.findViewById(R.id.txt_pickupdatevalue);
            viewHolder.tv_pickuptime = (TextView)view.findViewById(R.id.txt_pickuptimevalue);
            viewHolder.tv_status = (TextView)view.findViewById(R.id.txt_status);
            viewHolder.tv_totalorderamt = (TextView)view.findViewById(R.id.txt_totalorderprice);

            view.setTag(viewHolder);

        }else{
            view=convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tv_orderno.setText("#"+recentOrderPojo.getOrderno());
        viewHolder.tv_date.setText(recentOrderPojo.getDate());
        viewHolder.tv_pickupdate.setText(recentOrderPojo.getPickupdate());
        viewHolder.tv_pickuptime.setText(recentOrderPojo.getPickuptime());
        viewHolder.tv_totalorderamt.setText(recentOrderPojo.getTotlaorderamount());
        status = recentOrderPojo.getStatus();
        if(status.equals("0")){
            viewHolder.tv_status.setText("Payment Failure");
        }else if(status.equals("1")){
            viewHolder.tv_status.setText("Payment Successful");
        }




        return view;
    }



    public class ViewHolder{

        CircleImageView image_recentorder;
        TextView tv_orderno,tv_date,tv_pickupdate,tv_pickuptime,tv_itemname,tv_quantity,tv_totalorderamt,tv_status;
        String picture;

        int itempriceforcal;

    }
}
*/

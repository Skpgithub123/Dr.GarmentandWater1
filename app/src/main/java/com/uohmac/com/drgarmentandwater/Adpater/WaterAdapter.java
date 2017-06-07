/*
package com.uohmac.com.drgarmentandwater.Adpater;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uohmac.com.drgarmentandwater.Pojos.DryCleaningPojo;
import com.uohmac.com.drgarmentandwater.Pojos.WaterStrings;
import com.uohmac.com.drgarmentandwater.R;

import java.util.ArrayList;

*/
/**
 * Created by uOhmac Technologies on 13-Mar-17.
 *//*

public class WaterAdapter extends BaseAdapter {


    ViewHolder holder = null;

    private Context _context;
    private LayoutInflater inflater ;
    //WaterStrings data[] = null;
    ArrayList<WaterStrings> waterStringses = new ArrayList<>();
    public static WaterStrings waterStrings;
    int _resource = R.layout.waterbrand_listitem;
    public WaterAdapter(Context context, ArrayList<WaterStrings> waterStringses) {


        this._context = context;
        this.waterStringses = waterStringses;
        //this.data = data;
    }


    @Override
    public int getCount() {
        Log.d("sizeofarray",""+waterStringses.size());
        return waterStringses.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        waterStrings = waterStringses.get(position);
       // View row = convertView;


        View rowView;

        rowView = getLayoutInflater().inflate( R.layout.waterbrand_listitem, null);
        if(convertView == null){
            LayoutInflater inflater =    (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(_resource,parent,false);

            holder = new ViewHolder();
            holder.txt_brandname = (TextView)row.findViewById(R.id.txt_bisleri);
            holder.txt_brandprice = (TextView)row.findViewById(R.id.txt_bisprice);
            holder.img_water = (ImageView)row.findViewById(R.id.ivOrgLogobisleri);

            row.setTag(holder);


        }else{
            holder = (ViewHolder)row.getTag();
        }
        if(!TextUtils.isEmpty(waterStrings.getCategory_name())&&!TextUtils.isEmpty(waterStrings.getItem_price())){
            holder.txt_brandname.setText(waterStrings.getCategory_name());
            holder.txt_brandprice.setText(waterStrings.getItem_price());
            holder.waterimages = waterStrings.getCategory_img();

            if(!TextUtils.isEmpty(holder.waterimages)){
                Picasso.with(getContext())
                        .load(waterStrings.getCategory_img())
                        .into(holder.img_water);
            }
        }

       // WaterStrings waterStrings = data[position];
        return row;
    }


    static class ViewHolder
    {
        ImageView img_water;
        TextView txt_brandname,txt_brandprice;
        String waterimages;
    }
}
*/

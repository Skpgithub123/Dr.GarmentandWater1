package com.uohmac.com.drgarmentandwater.Adpater;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uohmac.com.drgarmentandwater.Pojos.DryCleaningPojo;
import com.uohmac.com.drgarmentandwater.Pojos.RecentOrderHistoryPojo;
import com.uohmac.com.drgarmentandwater.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by uOhmac Technologies on 30-Jan-17.
 */
public class RecentOrderHistoryLaundryAdapter extends ArrayAdapter {


    Context _context;
    private LayoutInflater inflater ;
    List<RecentOrderHistoryPojo> recentOrderHistoryPojos=null;
    int _resource = R.layout.recentorder_historyitem;;

    public static RecentOrderHistoryPojo recentOrderHistoryPojo;



    public RecentOrderHistoryLaundryAdapter(Context context,  int resource,List<RecentOrderHistoryPojo> recentOrderHistoryPojos) {
        super(context, resource,recentOrderHistoryPojos);
        _resource = resource;
        _context = context;
        this.recentOrderHistoryPojos = recentOrderHistoryPojos;

    }


    @Override
    public int getCount() {
        return recentOrderHistoryPojos.size();
    }

    @Override
    public Object getItem(int i) {
        return recentOrderHistoryPojos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        recentOrderHistoryPojo = (RecentOrderHistoryPojo) getItem(position);
        recentOrderHistoryPojo = recentOrderHistoryPojos.get(position);
        View row;
        final ViewHolder viewHolder;

        if(convertView == null){
            inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(_resource, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.image_recentorder = (CircleImageView)row.findViewById(R.id.ic_recentorderimage);

            viewHolder.tv_itemname = (TextView)row.findViewById(R.id.txt_itemnamevalue);
            viewHolder.tv_quantity = (TextView)row.findViewById(R.id.txt_qtyvalue);


            row.setTag(viewHolder);

        }else{
            row=convertView;
            viewHolder = (ViewHolder) row.getTag();
        }if(!TextUtils.isEmpty(recentOrderHistoryPojo.getItemname())&&!TextUtils.isEmpty(recentOrderHistoryPojo.getItemqty())){
            viewHolder.tv_itemname.setText(recentOrderHistoryPojo.getItemname());
            viewHolder.tv_quantity.setText(recentOrderHistoryPojo.getItemqty());
            viewHolder.picture = recentOrderHistoryPojo.getItemimg();

            if(!TextUtils.isEmpty(recentOrderHistoryPojo.getItemimg())){
                Log.d("DateFound", "Post:" + recentOrderHistoryPojo.getItemimg());

                Picasso.with(_context)
                        .load(recentOrderHistoryPojo.getItemimg())
                        .into(viewHolder.image_recentorder);
            }
        }
        return row;
    }


    public class ViewHolder{

        CircleImageView image_recentorder;
        TextView tv_orderno,tv_date,tv_pickupdate,tv_pickuptime,tv_itemname,tv_quantity,tv_totalorderamt;
        String picture,itemid;

        int itempriceforcal;

    }
}

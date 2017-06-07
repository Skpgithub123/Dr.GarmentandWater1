package com.uohmac.com.drgarmentandwater.Adpater;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.uohmac.com.drgarmentandwater.R;

/**
 * Created by uOhmac Technologies on 24-Jan-17.
 */
public class RecentOrderWaterAdapter extends ArrayAdapter {


    private Context _context;
    int _resource = R.layout.recentorderwatersingle_item;

    public RecentOrderWaterAdapter(Context context, int resource) {
        super(context, resource);
    }
}

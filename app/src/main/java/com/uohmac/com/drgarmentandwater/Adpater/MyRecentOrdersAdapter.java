package com.uohmac.com.drgarmentandwater.Adpater;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.uohmac.com.drgarmentandwater.Fragments.Laundry_Myorder;
import com.uohmac.com.drgarmentandwater.Fragments.MyWallet_History;

import com.uohmac.com.drgarmentandwater.Fragments.WaterMY_Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by uOhmac Technologies on 24-Jan-17.
 */
public class MyRecentOrdersAdapter extends FragmentStatePagerAdapter {


    int tabCount;
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();


    public MyRecentOrdersAdapter(FragmentManager fm,int tabCount) {
        super(fm);
        this.tabCount= tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:

                MyWallet_History tab1 = new MyWallet_History();
                return tab1;

            case 1:
                WaterMY_Order tab2 = new WaterMY_Order();
                return tab2;

            case 2:
                Laundry_Myorder tab3 = new Laundry_Myorder();
                return tab3;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return  tabCount;
    }

    public void addFrag(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}

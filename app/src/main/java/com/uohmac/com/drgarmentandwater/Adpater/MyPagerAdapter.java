package com.uohmac.com.drgarmentandwater.Adpater;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.uohmac.com.drgarmentandwater.Fragments.LandingLaundryFragment;
import com.uohmac.com.drgarmentandwater.Fragments.LandingWaterFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SUNIL on 21/08/15.
 */
public class MyPagerAdapter extends FragmentStatePagerAdapter {

    int tabCount;
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public MyPagerAdapter(FragmentManager fm,int tabCount) {
        super(fm);
        this.tabCount= tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                LandingWaterFragment tab1 = new LandingWaterFragment();
                return tab1;
            case 1:
                LandingLaundryFragment tab2 = new LandingLaundryFragment();
                return tab2;
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

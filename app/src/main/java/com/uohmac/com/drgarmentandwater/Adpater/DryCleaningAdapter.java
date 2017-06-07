package com.uohmac.com.drgarmentandwater.Adpater;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.uohmac.com.drgarmentandwater.Fragments.LandingHouseholdFragment;
import com.uohmac.com.drgarmentandwater.Fragments.LandingKidsFragment;
import com.uohmac.com.drgarmentandwater.Fragments.LandingLaundryFragment;
import com.uohmac.com.drgarmentandwater.Fragments.LandingMenFragment;
import com.uohmac.com.drgarmentandwater.Fragments.LandingWaterFragment;
import com.uohmac.com.drgarmentandwater.Fragments.LandingWomenFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by System-03 on 11/4/2016.
 */
public class DryCleaningAdapter extends FragmentStatePagerAdapter {

    int tabCount;
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public DryCleaningAdapter(FragmentManager fm,int tabCount) {
        super(fm);
        this.tabCount= tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                LandingMenFragment tab1 = new LandingMenFragment();
                return tab1;
            case 1:
                LandingWomenFragment tab2 = new LandingWomenFragment();
                return tab2;
            case 2:
                LandingKidsFragment tab3 = new LandingKidsFragment();
                return tab3;
            case 3:
                LandingHouseholdFragment tab4 = new LandingHouseholdFragment();
                return tab4;
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

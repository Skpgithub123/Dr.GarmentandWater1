package com.uohmac.com.drgarmentandwater.Activites;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.uohmac.com.drgarmentandwater.Adpater.MyPagerAdapter;
import com.uohmac.com.drgarmentandwater.Adpater.MyRecentOrdersAdapter;
import com.uohmac.com.drgarmentandwater.R;
import com.uohmac.com.drgarmentandwater.Utils.Constatns;

/**
 * Created by uOhmac Technologies on 24-Jan-17.
 */
public class RecentOrdersMain extends AppCompatActivity {


    public static final String MYPREFERNCES_LOGIN = "mysharedpref_waterlaundry";
    String getauthkey;
    private ViewPager viewpager;
    SharedPreferences sp;
    SharedPreferences.Editor editor ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recent_orders_main);


        sp = getSharedPreferences(MYPREFERNCES_LOGIN, 0);
        editor = sp.edit();




        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_layout);


        tabLayout.addTab(tabLayout.newTab().setText(Constatns.LANDING_TAB_MY_WALLET));
        tabLayout.addTab(tabLayout.newTab().setText(Constatns.LANDING_TAB_MY_WATER));
        tabLayout.addTab(tabLayout.newTab().setText(Constatns.LANDING_TAB_MY_LAUNDRY));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        viewpager = (ViewPager) findViewById(R.id.landing_pager);
        final MyRecentOrdersAdapter adapter = new MyRecentOrdersAdapter(getSupportFragmentManager(),tabLayout.getTabCount());

        viewpager.setAdapter(adapter);
        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}

package com.nati.zinab.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nati.zinab.R;
import com.nati.zinab.fragments.CurrentFragment;
import com.nati.zinab.fragments.DailyFragment;
import com.nati.zinab.fragments.HourlyFragment;

/**
 * Created by Nati on 8/28/2015.
 */
public class HomeTabsAdapter extends FragmentPagerAdapter {

    private static String[] TABS;
    private Context context;
    private CurrentFragment currentFragment;
    private HourlyFragment hourlyFragment;
    private DailyFragment dailyFragment;

    public HomeTabsAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
        currentFragment = CurrentFragment.newInstance();
        hourlyFragment = HourlyFragment.newInstance();
        dailyFragment = DailyFragment.newInstance();
        TABS = context.getResources().getStringArray(R.array.tabs);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return currentFragment;
            case 1:
                return hourlyFragment;
            case 2:
                return dailyFragment;
        }

        return null;
    }

    @Override
    public int getCount() {
        // number of tabs
        return TABS.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return TABS[position];
    }
}

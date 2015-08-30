package com.nati.zinab.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nati.zinab.fragments.CurrentlyFragment;
import com.nati.zinab.fragments.DailyFragment;
import com.nati.zinab.fragments.HourlyFragment;

/**
 * Created by Nati on 8/28/2015.
 */
public class HomeTabsAdapter extends FragmentPagerAdapter {

    private static String[] TABS = { "አሁን", "Hourly", "Daily" };
    private CurrentlyFragment currentlyFragment;
    private HourlyFragment hourlyFragment;
    private DailyFragment dailyFragment;

    public HomeTabsAdapter(FragmentManager fm) {
        super(fm);
        currentlyFragment = CurrentlyFragment.newInstance();
        hourlyFragment = HourlyFragment.newInstance();
        dailyFragment = DailyFragment.newInstance();
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return currentlyFragment;
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

package com.xebia.xtime.weekoverview;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.HashMap;
import java.util.Map;

public class WeekOverviewAdapter extends FragmentPagerAdapter {

    private Map<Integer, Fragment> mFragments;

    public WeekOverviewAdapter(FragmentManager fm) {
        super(fm);

        mFragments = new HashMap<Integer, Fragment>();
    }

    @Override
    public Fragment getItem(int i) {
        Fragment f = mFragments.get(i);
        if (null == f) {
            f = WeekOverviewFragment.newInstance(Integer.toString(i), "param2");
            mFragments.put(i, f);
        }
        return f;
    }

    @Override
    public int getCount() {
        return 8;
    }
}

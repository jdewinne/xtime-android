package com.xebia.xtime.weekoverview;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WeekOverviewAdapter extends FragmentPagerAdapter {

    public static final int START_INDEX = 4;

    private Map<Integer, Fragment> mFragments;

    public WeekOverviewAdapter(FragmentManager fm) {
        super(fm);

        mFragments = new HashMap<Integer, Fragment>();
    }

    @Override
    public Fragment getItem(int i) {
        Fragment f = mFragments.get(i);
        if (null == f) {
            Date date = getFragmentParam(i);
            f = WeekOverviewFragment.newInstance(date);
            mFragments.put(i, f);
        }
        return f;
    }

    private Date getFragmentParam(int index) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, index - START_INDEX);
        return calendar.getTime();
    }

    @Override
    public int getCount() {
        return 8;
    }
}

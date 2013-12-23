package com.xebia.xtime.weekoverview;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;

import java.util.Calendar;
import java.util.Date;

public class WeekOverviewAdapter extends FragmentPagerAdapter {

    public static final int START_INDEX = 4;
    private SparseArray<Fragment> mFragments;

    public WeekOverviewAdapter(FragmentManager fm) {
        super(fm);

        mFragments = new SparseArray<Fragment>();
    }

    @Override
    public Fragment getItem(int i) {
        Fragment f = mFragments.get(i);
        if (null == f) {
            Date date = getStartDate(i);
            f = WeekOverviewFragment.newInstance(date);
            mFragments.put(i, f);
        }
        return f;
    }

    @Override
    public int getCount() {
        return 8;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Date startDate = getStartDate(position);
        return WeekOverviewFragment.getTitle(startDate);
    }

    private Date getStartDate(int index) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, index - START_INDEX);

        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DAY_OF_WEEK, -1);
        }

        return calendar.getTime();
    }
}

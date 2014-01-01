package com.xebia.xtime.weekoverview;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;

import java.util.Calendar;
import java.util.Date;

/**
 * PagerAdapter for switching between {@link DailyHoursListFragment}.
 * <p/>
 * After initialization the adapter should be set to START_INDEX. The page at START_INDEX
 * represents the current week, so you can move a couple of weeks back in time and also into the
 * future.
 */
public class WeekPagerAdapter extends FragmentPagerAdapter {

    public static final int START_INDEX = 4;
    public static final int COUNT = 8;
    private SparseArray<Fragment> mFragments;

    public WeekPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragments = new SparseArray<Fragment>();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = mFragments.get(position);
        if (null == fragment) {
            Date date = getStartDate(position);
            fragment = DailyHoursListFragment.newInstance(date);
            mFragments.put(position, fragment);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Date startDate = getStartDate(position);
        return DailyHoursListFragment.getTitle(startDate);
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

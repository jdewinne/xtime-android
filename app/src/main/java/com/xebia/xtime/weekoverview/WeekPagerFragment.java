package com.xebia.xtime.weekoverview;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xebia.xtime.R;
import com.xebia.xtime.shared.FragmentWithChildFragmentManager;

public class WeekPagerFragment extends FragmentWithChildFragmentManager {

    public WeekPagerFragment() {
        // required default constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_week_pager, container, false);
        if (view != null) {
            ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
            pager.setAdapter(new WeekPagerAdapter(getChildFragmentManager()));
            pager.setCurrentItem(WeekPagerAdapter.START_INDEX);
        }
        return view;
    }
}

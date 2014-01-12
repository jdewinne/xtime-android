package com.xebia.xtime.monthoverview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xebia.xtime.R;

public class MonthOverviewFragment extends Fragment {

    public MonthOverviewFragment() {
        // required default constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_month_overview, container, false);
    }
}

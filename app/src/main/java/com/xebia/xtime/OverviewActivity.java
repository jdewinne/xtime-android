package com.xebia.xtime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.widget.SimpleAdapter;

import com.xebia.xtime.dayoverview.DayOverviewActivity;
import com.xebia.xtime.login.LoginActivity;
import com.xebia.xtime.monthoverview.MonthOverviewFragment;
import com.xebia.xtime.shared.model.DayOverview;
import com.xebia.xtime.weekoverview.DailyHoursListFragment;
import com.xebia.xtime.weekoverview.WeekPagerFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OverviewActivity extends ActionBarActivity implements DailyHoursListFragment.Listener {

    private static final int REQ_CODE_LOGIN = 1;
    private static final String KEY_LOGGED_IN = "logged_in";
    private boolean mLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null != savedInstanceState) {
            mLoggedIn = savedInstanceState.getBoolean(KEY_LOGGED_IN, false);
        }

        if (!isLoggedIn()) {
            Intent login = new Intent(this, LoginActivity.class);
            startActivityForResult(login, REQ_CODE_LOGIN);
        }

        if (null == savedInstanceState) {
            setListNavigation();
        }
    }

    private void setListNavigation() {

        final List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", getString(R.string.title_week_overview));
        map.put("fragment", Fragment.instantiate(this, WeekPagerFragment.class.getName()));
        data.add(map);
        map = new HashMap<String, Object>();
        map.put("title", getString(R.string.title_month_overview));
        map.put("fragment", Fragment.instantiate(this, MonthOverviewFragment.class.getName()));
        data.add(map);
        SimpleAdapter adapter = new SimpleAdapter(this, data,
                R.layout.ab_nav_dropdown, new String[]{"title"},
                new int[]{android.R.id.text1});

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setListNavigationCallbacks(adapter,
                new ActionBar.OnNavigationListener() {
                    @Override
                    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                        Map<String, Object> map = data.get(itemPosition);
                        Object o = map.get("fragment");
                        if (o instanceof Fragment) {
                            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                            tx.replace(android.R.id.content, (Fragment) o);
                            tx.commit();
                        }
                        return true;
                    }
                });
    }

    @Override
    public void onItemClicked(DayOverview overview) {
        Intent intent = new Intent(this, DayOverviewActivity.class);
        intent.putExtra(DayOverviewActivity.EXTRA_DAY_OVERVIEW, overview);
        startActivity(intent);
    }

    private boolean isLoggedIn() {
        return mLoggedIn;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_LOGIN) {
            mLoggedIn = resultCode == RESULT_OK;
            if (!isLoggedIn()) {
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // remember logged in status across configuration changes
        outState.putBoolean(KEY_LOGGED_IN, mLoggedIn);
        super.onSaveInstanceState(outState);
    }
}

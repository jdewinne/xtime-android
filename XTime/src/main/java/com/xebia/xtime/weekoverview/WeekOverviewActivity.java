package com.xebia.xtime.weekoverview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.xebia.xtime.R;
import com.xebia.xtime.login.LoginActivity;

public class WeekOverviewActivity extends ActionBarActivity implements WeekOverviewFragment.OnFragmentInteractionListener {

    private static final int REQ_CODE_LOGIN = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_LOGIN) {
            if (resultCode != RESULT_OK) {
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isLoggedIn()) {
            Intent login = new Intent(this, LoginActivity.class);
            startActivityForResult(login, REQ_CODE_LOGIN);
        }

        setContentView(R.layout.activity_week_overview);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new WeekOverviewAdapter(getSupportFragmentManager()));
        pager.setCurrentItem(WeekOverviewAdapter.START_INDEX);
    }

    public boolean isLoggedIn() {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.week_overview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

package com.xebia.xtime.weekoverview;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.xebia.xtime.R;
import com.xebia.xtime.shared.model.DayOverview;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Adapter that displays DayOverview objects in a list. Each list row contains a date indicator
 * and a view that displays the total number of work totalHours registered.
 */
public class DailyHoursListAdapter extends ArrayAdapter<DayOverview> {

    private final DateFormat mDateFormat;
    private final int mXebiaPurple;

    public DailyHoursListAdapter(Context context, List<DayOverview> data) {
        super(context, R.layout.row_daily_hours, R.id.hours, data);

        mDateFormat = new SimpleDateFormat("EEE d MMM");
        mDateFormat.setTimeZone(TimeZone.getTimeZone("CET"));

        Resources res = context.getResources();
        mXebiaPurple = res.getColor(R.color.xebia_purple);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // try to re-use the view instead of inflating a new one
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            row = inflater.inflate(R.layout.row_daily_hours, parent, false);
        }

        // find the view
        if (row != null) {
            TextView dateView = (TextView) row.findViewById(R.id.date);
            TextView hoursView = (TextView) row.findViewById(R.id.hours);
            TextView hoursLabelView = (TextView) row.findViewById(R.id.hours_label);

            // update the view content
            DayOverview item = getItem(position);
            Date date = item.getDate();
            dateView.setText(mDateFormat.format(date));
            hoursView.setText(NumberFormat.getNumberInstance().format(item.getTotalHours()));
            if (DateUtils.isToday(date.getTime())) {
                row.setBackgroundColor(mXebiaPurple);
                dateView.setTextColor(Color.WHITE);
                hoursView.setTextColor(Color.WHITE);
                hoursLabelView.setTextColor(Color.WHITE);
            } else if (date.getTime() > new Date().getTime()) {
                dateView.setTextColor(Color.GRAY);
                hoursView.setTextColor(Color.GRAY);
                hoursLabelView.setTextColor(Color.GRAY);
            }
        }

        return row;
    }
}

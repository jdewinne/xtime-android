package com.xebia.xtime.weekoverview;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.xebia.xtime.R;
import com.xebia.xtime.weekoverview.model.DailyHours;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Adapter that displays DailyHours objects in a list. Each list row contains a date indicator
 * and a view that displays the total number of work hours registered.
 */
public class DailyHoursListAdapter extends ArrayAdapter<DailyHours> {

    public DailyHoursListAdapter(Context context, List<DailyHours> data) {
        super(context, R.layout.row_daily_hours, R.id.hours, data);
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
            TextView monthView = (TextView) row.findViewById(R.id.month);
            TextView hoursView = (TextView) row.findViewById(R.id.hours);

            // update the view content
            DailyHours item = getItem(position);
            dateView.setText(new SimpleDateFormat("dd").format(item.date));
            monthView.setText(new SimpleDateFormat("MMM").format(item.date));
            hoursView.setText(NumberFormat.getNumberInstance().format(item.hours));
        }

        return row;
    }
}

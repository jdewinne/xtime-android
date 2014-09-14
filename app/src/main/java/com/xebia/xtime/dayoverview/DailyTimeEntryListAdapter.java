package com.xebia.xtime.dayoverview;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.xebia.xtime.R;
import com.xebia.xtime.shared.model.TimeEntry;

import java.text.NumberFormat;
import java.util.List;

public class DailyTimeEntryListAdapter extends ArrayAdapter<TimeEntry> {

    public DailyTimeEntryListAdapter(Context context, List<TimeEntry> objects) {
        super(context, R.layout.row_time_entry, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // try to re-use the view instead of inflating a new one
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            row = inflater.inflate(R.layout.row_time_entry, parent, false);
        }

        // find the view
        if (row != null) {
            TextView projectView = (TextView) row.findViewById(R.id.project);
            TextView workTypeView = (TextView) row.findViewById(R.id.work_type);
            TextView descriptionView = (TextView) row.findViewById(R.id.description);
            TextView hoursView = (TextView) row.findViewById(R.id.hours);

            // update the view content
            TimeEntry item = getItem(position);
            projectView.setText(item.getTask().getProject().getName());
            workTypeView.setText(item.getTask().getWorkType().getDescription());
            descriptionView.setText(item.getTask().getDescription());
            double hours = item.getHours();
            hoursView.setText(NumberFormat.getNumberInstance().format(hours));

            descriptionView.setVisibility(
                    TextUtils.isEmpty(item.getTask().getDescription()) ? View.GONE : View.VISIBLE);
        }

        return row;
    }
}

package com.xebia.xtime.monthoverview;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.xebia.xtime.R;

import java.text.NumberFormat;
import java.util.List;

/**
 * List adapter that takes a list of time sheet rows and shows a summary of each row in the view.
 * <p>
 * The summary contains the project name, work type and optional description,
 * along with the total number of hours that were spent on the project.
 * </p>
 */
public class TaskOverviewAdapter extends ArrayAdapter<TaskOverview> {

    public TaskOverviewAdapter(Context context, List<TaskOverview> objects) {
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

        // update the view content
        TaskOverview item = getItem(position);
        if (row != null) {
            TextView projectView = (TextView) row.findViewById(R.id.project);
            projectView.setText(item.getTask().getProject().getName());

            TextView workTypeView = (TextView) row.findViewById(R.id.work_type);
            workTypeView.setText(item.getTask().getWorkType().getDescription());

            TextView descriptionView = (TextView) row.findViewById(R.id.description);
            descriptionView.setText(item.getTask().getDescription());
            descriptionView.setVisibility(TextUtils.isEmpty(
                    item.getTask().getDescription()) ? View.GONE : View.VISIBLE);

            TextView hoursView = (TextView) row.findViewById(R.id.hours);
            hoursView.setText(NumberFormat.getNumberInstance().format(item.getTotalHours()));
        }

        return row;
    }
}

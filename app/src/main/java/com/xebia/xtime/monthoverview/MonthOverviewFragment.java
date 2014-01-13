package com.xebia.xtime.monthoverview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.xebia.xtime.R;
import com.xebia.xtime.monthoverview.loader.MonthOverviewLoader;
import com.xebia.xtime.shared.model.TimeCell;
import com.xebia.xtime.shared.model.TimeSheetRow;
import com.xebia.xtime.shared.model.WeekOverview;

import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MonthOverviewFragment extends Fragment implements LoaderManager
        .LoaderCallbacks<WeekOverview> {

    private TableLayout mTable;
    private WeekOverview mOverview;

    public MonthOverviewFragment() {
        // required default constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_month_overview, container, false);
        if (null != root) {
            mTable = (TableLayout) root.findViewById(R.id.table);
        }
        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<WeekOverview> onCreateLoader(int id, Bundle args) {
        return new MonthOverviewLoader(getActivity(), new Date());
    }

    @Override
    public void onLoadFinished(Loader<WeekOverview> weekOverviewLoader, WeekOverview weekOverview) {
        mOverview = weekOverview;
        if (null != weekOverview) {
            updateTable();
        }
    }

    @Override
    public void onLoaderReset(Loader<WeekOverview> weekOverviewLoader) {
        // nothing to do
    }

    /**
     * Updates the table view with rows for each project in the overview.
     */
    private void updateTable() {
        if (mTable.getChildCount() > 1) {
            // rows are already added
            return;
        }

        for (TimeSheetRow timeSheet : mOverview.getTimeSheetRows()) {
            // create new row to add to the table
            View row = getActivity().getLayoutInflater().inflate(R.layout
                    .table_month_row, null, false);
            if (null != row) {
                // set the project name in the first cell of the row
                TextView project = (TextView) row.findViewById(R.id.project);
                project.setText(timeSheet.getProject().getName());

                // set the hours in the cells for the correct days
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("CET"));
                NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
                for (TimeCell timeCell : timeSheet.getTimeCells()) {
                    calendar.setTime(timeCell.getEntryDate());
                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    int resId = getCellViewId(dayOfMonth);
                    String hours = numberFormat.format(timeCell.getHours());
                    ((TextView) row.findViewById(resId)).setText(hours);
                }

                highlightWeekends(row);

                mTable.addView(row);
            }
        }
    }

    /**
     * Gets the resource ID of the cell in the table row view for the given day of the month.
     *
     * @param dayOfMonth The day of the month (1-based)
     * @return The resource ID, or -1 if there is an error.
     */
    private int getCellViewId(int dayOfMonth) {
        try {
            Field field = R.id.class.getField("cell_" + dayOfMonth);
            return field.getInt(null);
        } catch (NoSuchFieldException e) {
            return -1;
        } catch (IllegalAccessException e) {
            return -1;
        }
    }

    private void highlightWeekends(View row) {
        final int highlightColor = getResources().getColor(R.color.month_weekend_background);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("CET"));
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        for (int i = 1; i < 32; i++) {
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            if (weekDay == Calendar.SATURDAY || weekDay == Calendar.SUNDAY) {
                row.findViewById(getCellViewId(i)).setBackgroundColor(highlightColor);
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }
}

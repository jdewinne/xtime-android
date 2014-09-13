package com.xebia.xtime.monthoverview;

import com.xebia.xtime.shared.model.TimeCell;
import com.xebia.xtime.shared.model.TimeSheetRow;
import com.xebia.xtime.shared.model.XTimeOverview;

public final class MonthOverviewUtils {

    private MonthOverviewUtils() {
        // do not instantiate
    }

    public static double getTotalHours(TimeSheetRow row) {
        double total = 0;
        for (TimeCell timeCell : row.getTimeCells()) {
            total += timeCell.getHours();
        }
        return total;
    }

    public static double getGrandTotalHours(XTimeOverview overview) {
        double total = 0;
        for (TimeSheetRow row : overview.getTimeSheetRows()) {
            total += getTotalHours(row);
        }
        return total;
    }
}

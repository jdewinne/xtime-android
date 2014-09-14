package com.xebia.xtime.sync;

import android.text.TextUtils;
import android.util.Log;

import com.xebia.xtime.shared.model.Project;
import com.xebia.xtime.shared.model.Task;
import com.xebia.xtime.shared.model.TimeEntry;
import com.xebia.xtime.shared.model.WorkType;
import com.xebia.xtime.shared.model.XTimeOverview;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for the response to a overview request from XTime. Uses regular expression acrobatics
 * to parse the JavaScript that is returned.
 */
public class DwrOverviewParser {

    private static final String TAG = "DwrResponseParser";

    /**
     * Parses the input from a WeekOverviewRequest or MonthOverviewRequest into a {@link
     * com.xebia.xtime.shared.model.XTimeOverview}.
     *
     * @param input String with the JavaScript code that is returned to an overview request.
     * @return The overview, or <code>null</code> when the input could not be parsed
     */
    public static XTimeOverview parse(final String input) {
        if (TextUtils.isEmpty(input)) {
            Log.d(TAG, "No input to parse");
            return null;
        }

        XTimeOverview.Builder builder = new XTimeOverview.Builder();

        // parse the JSONP callback argument
        String regex = "dwr\\.engine\\._remoteHandleCallback\\(";
        regex += ".*lastTransferredDate:new Date\\(([^\\)]*)\\)";
        regex += ",.*monthDaysCount:(\\d*)";
        regex += ",.*monthlyDataApproved:(\\w*)";
        regex += ",.*monthlyDataTransferred:(\\w*)";
        regex += ",.*userName:\"?([\\w\\s]*)\"?";
        regex += ",.*weekEndDates:(\\w*)";
        regex += ",.*weekStart:(\\w*)\\}\\);";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            // Note: not all data that is returned is actually used in the app
            builder.setLastTransferred(new Date(Long.parseLong(matcher.group(1))))
                    .setMonthlyDataApproved(Boolean.parseBoolean(matcher.group(3)))
                    .setUsername(matcher.group(5));
            parseProjects(input, builder);
            parseTimeSheetRows(input, builder);
            return builder.build();

        } else {
            Log.d(TAG, "Failed to parse input '" + input + "'");
            return null;
        }
    }

    private static void parseTimeSheetRows(String input, XTimeOverview.Builder builder) {

        // match the response for patterns like:
        // xx.clientName="$1"; xx.description="$2"; xx.projectId="$3"; ...
        String regex = "(\\w*)\\.clientName=\"([^\"]*)\";" +
                ".*\\1\\.description=\"([^\"]*)\";" +
                ".*\\1\\.projectId=\"([^\"]*)\";" +
                ".*\\1\\.projectName=\"([^\"]*)\";" +
                ".*\\1\\.timeCells=([^;]*);" +
                ".*\\1\\.userId=\"([^\"]*)\";" +
                ".*\\1\\.workTypeDescription=\"([^\"]*)\";" +
                ".*\\1\\.workTypeId=\"([^\"]*)\";";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            // Note: not all data that is returned is actually used in the app
            String description = matcher.group(3);
            String workTypeDescription = matcher.group(8);
            if (description.equals(workTypeDescription)) {
                // XTime returns incorrect description for time sheet rows that come from Afas
                description = "";
            }
            String workTypeId = matcher.group(9);

            Task task = new Task.Builder()
                    .setDescription(description)
                    .setProject(new Project(matcher.group(4), matcher.group(5)))
                    .setWorkType(new WorkType(workTypeId, workTypeDescription))
                    .build();
            parseTimeCells(input, matcher.group(6), task, builder);
        }
    }

    private static void parseTimeCells(String input, String varName, Task task,
                                       XTimeOverview.Builder builder) {
        List<String> timeCellVarNames = parseTimeCellVars(input, varName);
        for (String timeCellVarName : timeCellVarNames) {
            builder.addTimeEntry(parseTimeCellDetails(input, timeCellVarName, task));
        }
    }

    private static TimeEntry parseTimeCellDetails(String input, String varName, Task task) {

        // match the response for patterns like:
        // xx.approved=$1; xx.entryDate=new Date($2); xx.fromAfas=$3; x.hour="$4"; ...
        String regex = varName + "\\.approved=([^;]*);";
        regex += ".*" + varName + "\\.entryDate=new Date\\(([^\\)]*)\\);";
        regex += ".*" + varName + "\\.fromAfas=([^;]*);";
        regex += ".*" + varName + "\\.hour=\"([^\"]*)\";";
        regex += ".*" + varName + "\\.transferredToAfas=([^;]*);";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            // not all data that is returned is actually used in the app

            // boolean approved = "true".equals(matcher.group(1));
            long entryDate = Long.parseLong(matcher.group(2));
            boolean fromAfas = "true".equals(matcher.group(3));
            double hour = Double.parseDouble(matcher.group(4));
            // boolean transferredToAfas = "true".equals(matcher.group(5));
            return new TimeEntry(task, new Date(entryDate), hour, fromAfas);
        }

        return null;
    }

    private static List<String> parseTimeCellVars(String input, String varName) {
        List<String> varNames = new ArrayList<>();

        // match the response for patterns like:
        // xx[0]=$1; xx[1]=$2; ...
        String regex = varName + "\\[\\d*\\]=([^;,]*);";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            if (matcher.groupCount() == 1) {
                varNames.add(matcher.group(1));
            }
        }

        return varNames;
    }

    private static void parseProjects(String input, XTimeOverview.Builder builder) {

        // match the response for patterns like:
        // xx.description="$1"; xx.id="$2";
        String regex = "(\\w*)\\.description=\"([^\"]*)\";" +
                ".*\\1\\.id=\"([^\"]*)\";";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            String description = matcher.group(2);
            String id = matcher.group(3);
            builder.addProject(new Project(id, description));
        }
    }
}

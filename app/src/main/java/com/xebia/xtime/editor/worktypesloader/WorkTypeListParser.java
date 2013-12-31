package com.xebia.xtime.editor.worktypesloader;

import android.text.TextUtils;

import com.xebia.xtime.shared.model.WorkType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WorkTypeListParser {

    /**
     * @param input String to parse
     * @return A list of work types, or <code>null</code> if the input was not parsed correctly
     */
    public static List<WorkType> parse(String input) {
        String arrayName = parseWorkTypeArrayName(input);
        List<String> arrayEntries = parseArrayEntries(input, arrayName);
        return parseWorkTypeList(input, arrayEntries);
    }

    private static List<WorkType> parseWorkTypeList(String input, List<String> entries) {
        if (TextUtils.isEmpty(input) || null == entries) {
            return null;
        }

        List<WorkType> workTypes = new ArrayList<WorkType>();

        for (String varName : entries) {
            String regex = varName + "\\.workType=\"([^\"]*)\";" +
                    varName + "\\.workTypeDescription=\"([^\"]*)\";";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(input);
            if (matcher.find()) {
                String id = matcher.group(1);
                String description = matcher.group(2);
                workTypes.add(new WorkType(id, description));
            } else {
                // parse error
                return null;
            }
        }
        return workTypes;
    }

    private static List<String> parseArrayEntries(String input, String arrayName) {
        if (TextUtils.isEmpty(input) || TextUtils.isEmpty(arrayName)) {
            return null;
        }

        List<String> varNames = new ArrayList<String>();

        // match the response for patterns like:
        // xx[0]=$1; xx[1]=$2; ...
        String regex = arrayName + "\\[\\d\\]=([^;,]*);";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            if (matcher.groupCount() == 1) {
                varNames.add(matcher.group(1));
            }
        }

        return varNames;
    }

    private static String parseWorkTypeArrayName(String input) {
        if (TextUtils.isEmpty(input)) {
            return null;
        }

        String regex = "dwr\\.engine\\._remoteHandleCallback\\(.*workTypeWorkGroupList:([^\\}," +
                "]*)\\}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }
}

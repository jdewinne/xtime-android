package com.xebia.xtime.editor.delete;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeleteEntryResponseParser {

    /**
     * @param input String to parse
     * @return <code>true</code> if the entry was deleted successfully
     */
    public static boolean parse(String input) {
        if (TextUtils.isEmpty(input)) {
            return false;
        }

        String regex = "dwr\\.engine\\._remoteHandleCallback\\('\\d*','\\d*',(\\d*)\\);";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            try {
                return Integer.parseInt(matcher.group(1)) > 0;
            } catch (NumberFormatException e) {
                // cannot parse the number of deleted entries?
                return false;
            }
        }

        // no match
        return false;
    }
}

package com.htphong.mylife.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class Helper {

    public static String saveBirthDay(String time) {
        SimpleDateFormat oldFormat = new SimpleDateFormat("dd-MM-yyyy");
        oldFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
        newFormat.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
        try {
            String reformattedStr = newFormat.format(oldFormat.parse(time));
            return reformattedStr;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static String setBirthDay(String time) {
        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd");
        oldFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
        newFormat.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
        try {
            String reformattedStr = newFormat.format(oldFormat.parse(time));
            return reformattedStr;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }
}

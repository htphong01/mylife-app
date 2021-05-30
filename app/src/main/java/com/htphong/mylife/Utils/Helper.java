package com.htphong.mylife.Utils;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

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

    public static String timeDifferent(String dataDate) {

        String convTime = "";

        String prefix = "";
        String suffix = "trước";

        try {
            dataDate = changeTimeZone(dataDate);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S");
            Date pasTime = dateFormat.parse(dataDate);

            Date nowTime = new Date();
            long dateDiff = nowTime.getTime() - pasTime.getTime();

            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour   = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day  = TimeUnit.MILLISECONDS.toDays(dateDiff);

            if(second < 10) {
                return "Vừa xong";
            }
            else if (second < 60) {
                convTime = second + " giây " + suffix;
            } else if (minute < 60) {
                convTime = minute + " phút "+suffix;
            } else if (hour < 24) {
                convTime = hour + " giờ "+suffix;
            } else if (day >= 7) {
                if (day > 360) {
                    convTime = (day / 360) + " năm " + suffix;
                } else if (day > 30) {
                    convTime = (day / 30) + " tháng " + suffix;
                } else {
                    convTime = (day / 7) + " tuần " + suffix;
                }
            } else if (day < 7) {
                convTime = day+" ngày "+suffix;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("ConvTimeE", e.getMessage());
        }

        return convTime;
    }

    private static String changeTimeZone(String time) {
        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S");
        oldFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S");
        newFormat.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
        try {
            String reformattedStr = newFormat.format(oldFormat.parse(time));
            return reformattedStr;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static String bitmapToString(Bitmap bitmap) {
        if (bitmap!=null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            byte [] array = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(array,Base64.NO_WRAP);
        }

        return "";
    }

    public static String convertDateTime(String time) {
        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S");
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

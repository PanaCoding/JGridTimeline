package com.panacoding.gridtimeline;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class JUtils {

    public static Calendar getCalendarFromDateString(String date_string, String format){
        Calendar cal_f_sel = Calendar.getInstance();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = sdf.parse(date_string);
            cal_f_sel.setTime(date);
        }catch (ParseException e){
            e.printStackTrace();
        }

        return cal_f_sel;
    }


    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public static long getDiffDays(long currentTime, long endDateTime) {

        Calendar endDateCalendar;
        Calendar currentDayCalendar;


        //expiration day
        endDateCalendar = Calendar.getInstance(TimeZone.getTimeZone("EST"));
        endDateCalendar.setTimeInMillis(endDateTime);
        endDateCalendar.set(Calendar.MILLISECOND, 0);
        endDateCalendar.set(Calendar.MINUTE, 0);
        endDateCalendar.set(Calendar.HOUR, 0);

        //current day
        currentDayCalendar = Calendar.getInstance(TimeZone.getTimeZone("EST"));
        currentDayCalendar.setTimeInMillis(currentTime);
        currentDayCalendar.set(Calendar.MILLISECOND, 0);
        currentDayCalendar.set(Calendar.MINUTE, 0);
        currentDayCalendar.set(Calendar.HOUR, 0);

        long remainingDays = Math.round((float) (endDateCalendar.getTimeInMillis() - currentDayCalendar.getTimeInMillis()) / (24 * 60 * 60 * 1000));

        return remainingDays;
    }

}

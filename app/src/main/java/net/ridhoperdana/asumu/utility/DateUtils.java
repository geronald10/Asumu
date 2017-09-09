package net.ridhoperdana.asumu.utility;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtils {

    public static String getCurrentDate(Context context) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd");
        return newDate.format(calendar.getTime());
    }
}

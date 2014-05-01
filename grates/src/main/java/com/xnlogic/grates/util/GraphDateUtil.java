package com.xnlogic.grates.util;

import java.util.Calendar;

public class GraphDateUtil {

    public static long getUnixTime(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        
        cal.set(year, month, day);
        
        return cal.getTimeInMillis() / 1000L;
    } // getUnixTime
}

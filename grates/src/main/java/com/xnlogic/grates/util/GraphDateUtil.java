package com.xnlogic.grates.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.xnlogic.grates.exceptions.InvalidDateException;

public class GraphDateUtil {

    public static long getUnixTime(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();

        cal.set(year, month, day);
        
        return cal.getTimeInMillis() / 1000L;
    } // getUnixTime
    
    public static void valiDate(int year, int month, int day) throws InvalidDateException {
        String inDate = String.format("%d-%02d-%02d", year, month, day);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        
        try {        
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            throw new InvalidDateException("Illegal date specified.");
        } // try       
    }
} // GraphDateUtil

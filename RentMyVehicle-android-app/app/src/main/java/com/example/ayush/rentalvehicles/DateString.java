package com.example.ayush.rentalvehicles;

import android.util.Log;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by HP on 29-04-2017.
 */
public class DateString
{
    int day,month,year;
    SimpleDateFormat dateFormat;
    public DateString()
    {
        dateFormat=new SimpleDateFormat("yyyy-MM-dd");
    }
    public DateString(int year, int month, int day)
    {
        this.year=year;
        this.month=month;
        this.day=day;
        dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Log.d("date format",dateFormat.toString());
    }
    public String formattedDate()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, day);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        java.util.Date d=cal.getTime();
        String formattedDate=dateFormat.format(d);
        return formattedDate;

    }
}

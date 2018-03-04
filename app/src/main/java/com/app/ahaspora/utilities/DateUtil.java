package com.app.ahaspora.utilities;


import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by AangJnr on 9/20/16.
 */
public class DateUtil {


    public static String getDateInMillisToString() {

        long now = System.currentTimeMillis();

        return String.valueOf(now);

    }


    public static String getFormattedDate() {


        DateFormat df = new SimpleDateFormat("yyyy.MM.dd", Locale.US);
        return df.format(Calendar.getInstance().getTime());


    }


    public static String getFormattedTime() {


        DateFormat df = new SimpleDateFormat(" '@' hh:mm:ss", Locale.US);
        return df.format(Calendar.getInstance().getTime());


    }


    public static String convertStringToPrettyTime(String date) {

        try {
            PrettyTime prettyTime = new PrettyTime();
            long l = fromSimpleDateFormatToLong(date);

            return prettyTime.format(new Date(l));
        } catch (Exception e) {
            e.printStackTrace();

            return date;
        }
    }



    private static long fromSimpleDateFormatToLong(String dateString){
        long startDate = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-mm-dd hh:mm:ss", Locale.getDefault());
            Date date = sdf.parse(dateString);
            startDate = date.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return startDate;
    }


    public static String reformatDate(String dateString){

        if(dateString == null)
            return "";

        String startDate = dateString.substring(0, 9);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-mm-dd hh:mm:ss", Locale.getDefault());
            Date date = sdf.parse(dateString);
            startDate = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault()).format(date.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return startDate;
    }



    public static String reformatDateYYYYMMDD(String yyyymmdd){

        if(yyyymmdd == null)
            return "";

        String startDate = yyyymmdd.substring(0, 9);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-mm-dd", Locale.getDefault());
            Date date = sdf.parse(yyyymmdd);
            startDate = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault()).format(date.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return startDate;
    }

}









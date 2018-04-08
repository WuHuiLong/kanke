package com.kanke.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class DateTimeUtil {

    public static final String STANDARD_TIME="yyyy-MM-dd HH:ss:hh";
    public static Date strToDate(String dateTimeStr,String formatStr){
        DateTimeFormatter dateTimeFormat=DateTimeFormat.forPattern(formatStr);
        DateTime dateTime=dateTimeFormat.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }
    public static String DateTostr(Date date,String formatStr){
        if(date==null){
            return StringUtils.EMPTY;
        }
        else {
            DateTime dateTime=new DateTime(date);
            return dateTime.toString(formatStr);
        }
    }

    public static Date strToDate(String dateTimeStr){
        DateTimeFormatter dateTimeFormat=DateTimeFormat.forPattern(STANDARD_TIME);
        DateTime dateTime=dateTimeFormat.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }
    public static String DateTostr(Date date){
        if(date==null){
            return StringUtils.EMPTY;
        }
        else {
            DateTime dateTime=new DateTime(date);
            return dateTime.toString(STANDARD_TIME);
        }
    }
}

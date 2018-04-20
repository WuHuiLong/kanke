package com.kanke.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

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
    public static String dateToint(Date time,Long time1,int movieLength){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:ss:hh");
        String s= simpleDateFormat.format(new Date(time.getTime()+time1+movieLength*60*1000L));
        return s;
    }

//    public static void main(String[] args) {
//        System.out.println(DateTimeUtil.DateTostr(new Date()));
//        System.out.println(DateTimeUtil.dateToint(new Date(),new Date(),120));
//    }
}

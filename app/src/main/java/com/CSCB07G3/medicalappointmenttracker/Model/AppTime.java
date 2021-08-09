package com.CSCB07G3.medicalappointmenttracker.Model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class AppTime implements Serializable, Comparable<AppTime> {
    int month;
    int day;
    int year;
    int hour;
    int minute;
    public AppTime(){}
    public AppTime(int year, int month, int day, int hour, int minute){
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
    }

    public Date convertToDate(){
        Calendar c = Calendar.getInstance();
        c.set(year,month-1,day,hour,minute);
        return c.getTime();
    }

    public int getYear(){ return year; }
    public int getMonth(){ return month; }
    public int getDay(){ return day; }
    public int getHour(){ return hour; }
    public int getMinute(){ return minute; }

    @Override
    public int compareTo(AppTime appTime) {
       return this.convertToDate().compareTo(appTime.convertToDate());
    }
}

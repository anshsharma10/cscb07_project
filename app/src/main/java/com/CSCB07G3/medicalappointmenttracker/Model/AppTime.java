package com.CSCB07G3.medicalappointmenttracker.Model;

import java.io.Serializable;

public class AppTime implements Serializable, Comparable<AppTime> {
    int month;
    int day;
    int year;
    int hour;
    int minute;

    public AppTime(int year, int month, int day, int hour, int minute){
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
    }

    public int getYear(){ return year; }
    public int getMonth(){ return month; }
    public int getDay(){ return day; }
    public int getHour(){ return hour; }
    public int getMinute(){ return minute; }

    @Override
    public int compareTo(AppTime appTime) {
        if (year < appTime.year){
            return -1;
        } else if (year > appTime.year){
            return 1;
        } else{
            if (month < appTime.month){
                return -1;
            }
            else if (month > appTime.month){
                return 1;
            } else{
                if (day < appTime.day){
                    return -1;
                }
                else if (day > appTime.day){
                    return 1;
                } else{
                    if (hour < appTime.hour){
                        return -1;
                    }
                    else if (hour > appTime.hour){
                        return 1;
                    } else{
                        if (minute < appTime.minute){
                            return -1;
                        }
                        else if (minute > appTime.minute){
                            return 1;
                        } else{
                            return 0;
                        }
                    }
                }
            }
        }
    }
}

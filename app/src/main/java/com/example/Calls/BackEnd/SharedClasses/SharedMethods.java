package com.example.Calls.BackEnd.SharedClasses;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Objects;

public class SharedMethods {

    /**
     * check string
     * @param str string
     * @return true if == null
     */
    public static boolean isNullOrWhiteSpace(String str){
        if(str == null) return true;
        return str.trim().isEmpty();
    }
    public static String secondsToTime(int count){
        String timeLabel = "";
        int min = count / 60;
        int sec = count  % 60;

        timeLabel = min + ":";
        if (sec < 10 ) timeLabel += "0";
        timeLabel += sec;
        return timeLabel;
    }

}

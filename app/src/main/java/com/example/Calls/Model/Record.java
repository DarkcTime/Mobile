package com.example.Calls.Model;

import android.annotation.SuppressLint;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Record implements Comparable<Record> {
    public String Path;
    public String FullName;
    public String Contact;
    public String NumberPhone;
    public String Date;
    public String Time;
    //public String duration;

    //region filtering by DateTime
    private final String format = "dd.MM.yyyy hh:mm:ss";
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
    private java.util.Date getDateTime() throws Exception{
        try{
            String dateTime = Date + " " + Time;
            return simpleDateFormat.parse(dateTime);
        }
        catch (Exception ex){
            throw new Exception("getDateTime Parse".concat(ex.getMessage()));
        }

    }
    @Override
    public int compareTo(Record o){
        try{
            return getDateTime().compareTo(o.getDateTime());
        }
        catch (Exception ex){
            return 0;
        }

    }
    //endregion
}

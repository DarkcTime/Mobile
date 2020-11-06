package com.example.Calls.Model;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.util.Log;

import com.example.Calls.BackEnd.Media.MediaPlayerClass;
import com.example.Calls.BackEnd.Services.RecordsService;

import java.text.SimpleDateFormat;



public class Record implements Comparable<Record> {
    public String Path;
    public String FullName;
    public String Contact;
    public String NumberPhone;
    public String Date;
    public String Time;
    //public String duration;

    //region duration
    /*
    private MediaPlayer mediaPlayer = new MediaPlayer();
    public void mp(){
        try{
            mediaPlayer.setDataSource(Path);
            mediaPlayer.prepare();
            Log.d("time", MediaPlayerClass.createTimeLabel(
                    mediaPlayer.getDuration()));
        }
        catch (Exception ex){
            Log.d("media", ex.getMessage());
        }
    }

     */
    //endregion

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

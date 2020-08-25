package com.example.Calls.BackEnd.CutterFiles;

import android.util.Log;

import com.example.Calls.BackEnd.SharedClasses.SharedMethods;

import java.io.File;

//object for work with FFmpegCutter
public class FileForCutter {

    private int duration = 0;
    private final String bitrate = "32k";
    private File source;
    private File destination;

    public int getDuration(){
        return duration;
    }
    public File getSource(){
        return source;
    }
    public File getDestination(){
        return destination;
    }
    public String getBitrate(){
        return bitrate;
    }

    public FileForCutter(int _duration, File _source, File _destination){
        boolean isNull = _source != null && _destination != null && duration != 0;
        if(isNull){
            duration = _duration;
            source = _source;
            destination = _destination;
        }
        else{
            Log.d("FileForCutter", "null");
        }

    }

}

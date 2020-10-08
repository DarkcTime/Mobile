package com.example.Calls.BackEnd.CutterFiles;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.Calls.BackEnd.Files.FileSystem;
import com.example.Calls.BackEnd.Files.FileSystemParameters;
import com.example.Calls.BackEnd.Records.Records;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Данный класс содержит в себе методы для управления интервалами
 * и запускает обрезку файлов
 */

public class Cutter {

    private List<CutterInterval> intervalList;

    private CutterInterval interval;

    //записи длиной более 19 секунд
    //не обработываются api
    private final int MAX_DURATION = 19;

    public void AddInterval(int start){

        Log.d("start", String.valueOf(start));
        interval = new CutterInterval(start);
    }

    public void StopInterval(int end) {

        Log.d("end", String.valueOf(end));

        if(checkDurationInterval(end)){
            splitTheInterval(end);
        }
        else{
            interval.setEnd(end);
            intervalList.add(interval);
        }
    }

    private boolean checkDurationInterval(int end){
        int duration = end - interval.getStart();
        return duration > MAX_DURATION;
    }

    private void splitTheInterval(int end){
        StopInterval(interval.getStart() + MAX_DURATION);
        AddInterval(interval.getStart() + MAX_DURATION);
        StopInterval(end);
    }


    public Cutter(){
        intervalList = new ArrayList<CutterInterval>();
    }

    //полный путь с именем записи
    //контакт

    public void startCutFileIntervals(Context _context) throws Exception {

        FFmpegCutter fFmpegCutter = new FFmpegCutter(_context);

        fFmpegCutter.executeCommandForCutFileAfterPlay(FileSystem.getFilesForCutter(intervalList));

    }



}

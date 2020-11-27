package com.example.Calls.BackEnd.CutterFiles;

import android.content.Context;
import android.util.Log;

import com.example.Calls.BackEnd.Files.FileSystem;

import java.util.ArrayList;
import java.util.List;

/**
 * Данный класс содержит в себе методы для управления интервалами
 * и запускает обрезку файлов
 */

public class Cutter {

    private List<CutterInterval> intervalList;

    public List<CutterInterval> getIntervalList() {
        return intervalList;
    }

    private CutterInterval interval;

    //record have duration > 19 sec
    //don't work with Api
    private final int MAX_DURATION = 19;

    public void AddInterval(int start){
        Log.d("start", String.valueOf(start));
        interval = new CutterInterval(start);
    }

    public void StopInterval(int end) {
        Log.d("end", String.valueOf(end));
        interval.setEnd(end);
        intervalList.add(interval);
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

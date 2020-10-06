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

public class Cutter {

    private List<CutterInterval> intervalList;

    private CutterInterval interval;

    public void AddInterval(int start){
        interval = new CutterInterval(start);
    }

    public void StopInterval(int end) {
        interval.setEnd(end);
        intervalList.add(interval);
    }

    public Cutter(){
        intervalList = new ArrayList<CutterInterval>();
    }

    //полный путь с именем записи
    //контакт
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startCutFileIntervals(Context _context) throws Exception {

        FFmpegCutter fFmpegCutter = new FFmpegCutter(_context);

        fFmpegCutter.executeCommandForCutFileAfterPlay(FileSystem.getFilesForCutter(intervalList));

    }



}

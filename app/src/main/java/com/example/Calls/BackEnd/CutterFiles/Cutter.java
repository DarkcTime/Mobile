package com.example.Calls.BackEnd.CutterFiles;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.Calls.BackEnd.Files.FileSystemParameters;

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

    private String nameRecord;

    public Cutter(String _nameRecord){
        intervalList = new ArrayList<CutterInterval>();
        nameRecord = _nameRecord;
    }

    //полный путь с именем записи
    //контакт
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startCutFileIntervals(Context _context) throws Exception {

        //TODO refactor
        if(!new File(FileSystemParameters.getPathForSelectedRecordsForCutter()).mkdir()){
            Log.d("File", "file no mkdir");
        };


        FFmpegCutter fFmpegCutter = new FFmpegCutter(_context);

        fFmpegCutter.executeCommandForCutFileAfterPlay(getFilesForCutter(), FileSystemParameters.getPathForSelectedRecordApi());

    }

    private List<FileForCutter> getFilesForCutter(){
        WorkWithFileForCutter workWithFileForCutter = new WorkWithFileForCutter(nameRecord);
        int i = 0;
        List<FileForCutter> fileForCutterList = new ArrayList<FileForCutter>();
        for (CutterInterval interval : intervalList) {
            int duration = interval.getEnd() - interval.getStart();
            File targetFile = new File(FileSystemParameters.getPathForSelectedRecordsForCutter().concat(String.valueOf(i)).concat(".mp3"));
            Log.d("targetFile", targetFile.getAbsolutePath());
            FileForCutter fileForCutter = new FileForCutter(interval.getStart(),duration, workWithFileForCutter.getSourceFile(),targetFile);
            fileForCutterList.add(fileForCutter);
            i++;
        }
        return fileForCutterList;
    }

}

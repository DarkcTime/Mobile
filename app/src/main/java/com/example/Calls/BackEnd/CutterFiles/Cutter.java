package com.example.Calls.BackEnd.CutterFiles;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.Calls.BackEnd.CheapSound.SoundFileCutter;
import com.example.Calls.BackEnd.FilesWork;
import com.example.Calls.BackEnd.Records;
import com.example.Calls.BackEnd.SharedVariables;

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

        //создание файловой директории для записи
        getDirForRecord().mkdir();

        //создание файловой директории для сохранения отдельных записей
        getDirForRecords().mkdir();

        //create dir for work with api
        Copy.createDirForWorkWithApi(nameRecord);

        FFmpegCutter fFmpegCutter = new FFmpegCutter(_context);

        fFmpegCutter.executeCommandForCutFileAfterPlay(getFilesForCutter(), FilesWork.getPathForWorkWithApi(nameRecord));

    }

    private File getDirForRecord(){
        return new File(FilesWork.getPathForOnlyRecord(nameRecord));
    }

    private File getDirForRecords(){
        return  new File(FilesWork.getPathForListRecord(nameRecord));
    }

    private File getSourceFile(){
        return  new File(Records.pathForFindRecords.concat(nameRecord));
    }


    private List<FileForCutter> getFilesForCutter(){
        int i = 0;
        List<FileForCutter> fileForCutterList = new ArrayList<FileForCutter>();
        for (CutterInterval interval : intervalList) {
            int duration = interval.getEnd() - interval.getStart();
            File targetFile = new File(getDirForRecords().getAbsolutePath().concat("/").concat(String.valueOf(i)).concat(".mp3"));
            Log.d("targetFile", targetFile.getAbsolutePath());
            FileForCutter fileForCutter = new FileForCutter(interval.getStart(),duration, getSourceFile(),targetFile);
            fileForCutterList.add(fileForCutter);
            i++;
        }
        return fileForCutterList;
    }

}

package com.example.Calls.BackEnd.CheapSound;

import android.util.Log;

import com.example.Calls.BackEnd.Contacts;
import com.example.Calls.BackEnd.Records;
import com.example.Calls.BackEnd.SharedVariables;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Cut {
    private static Logger log = Logger.getLogger(Utilities.class.getName());

    private List<FriendInterval> intervalList;

    private FriendInterval interval;

    public void AddInterval(int start){
        interval = new FriendInterval(start);
    }

    public void StopInterval(int end) {
        interval.setEnd(end);
        intervalList.add(interval);
    }

    public List<FriendInterval> getIntervalList(){
        return intervalList;
    }

    public Cut(){
        intervalList = new ArrayList<FriendInterval>();

    }

    //полный путь с именем записи
    //контакт
    public void Cutter(String nameRec) throws IOException {
        int i = 0;
        //create folder for records

        //creating a File object
        File file = new File(getPathCut(nameRec));
        //creating a Folder for files
        boolean folder = file.mkdir();
        Log.d("folder", String.valueOf(folder));

        for (FriendInterval interval : intervalList) {
            FileCutter(nameRec,interval.getStart(),interval.getEnd(),String.valueOf(i));
            i++;
        }
    }


    private void FileCutter(String nameRec, double start, double end, String stage) throws IOException {
        //получение mp записи
        String pathFull = Records.pathForFindRecords + nameRec;

        //TODO??? class for create mp3 file
        CheapSoundFile soundFile = CheapSoundFile.create(pathFull);

        //create a File object for mp3 file
        File fileName = new File(getPathCut(nameRec)+stage+".mp3");


        int mSampleRate = soundFile.getSampleRate();
        int mSamplesPerFrame = soundFile.getSamplesPerFrame();
        int startFrame = Utilities.secondsToFrames(start,mSampleRate, mSamplesPerFrame);
        int endFrame = Utilities.secondsToFrames(end, mSampleRate,mSamplesPerFrame);

        Log.d("startFrame", String.valueOf(startFrame));
        Log.d("endFrame", String.valueOf(endFrame));
        Log.d("path", fileName.getAbsolutePath());
        soundFile.WriteFile(fileName.getAbsoluteFile(), startFrame, endFrame-startFrame);

        File file = new File(SharedVariables.getPathApplicationFileSystem() + "17.mp3");
        boolean exist = file.exists();
        Log.d("existfile", String.valueOf(exist));
    }

    //path for create dir
    private String getPathCut(String nameRec){
        return SharedVariables.getPathApplicationFileSystem() + nameRec.replace(".mp3", "");
    }

}

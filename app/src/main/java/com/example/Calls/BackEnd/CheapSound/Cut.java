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
    public void Cutter(String nameRec) throws Exception {
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


    private void FileCutter(String nameRec, float start, float end, String stage) throws Exception {
        //получение mp записи
        String pathFull = Records.pathForFindRecords + nameRec;
        Log.d("pathFull", pathFull);

        SoundFileCutter soundFile;
        soundFile = SoundFileCutter.create(pathFull);

        Log.d("soundfile", "yes");

        //create a File object for mp3 file
        File fileName = new File("/storage/emulated/0/Android/data/mp4test/".concat("2.mp3"));

        File papka = new File("/storage/emulated/0/Android/data/mp4test");

        boolean exitsDir = papka.mkdir();

        Log.d("papka", String.valueOf(exitsDir));

        int startFrame = soundFile.getFrameFromSeconds(start);
        int endFrame = soundFile.getFrameFromSeconds(end);

        Log.d("startFrame", String.valueOf(startFrame));
        Log.d("endFrame", String.valueOf(endFrame));
        Log.d("path", fileName.getAbsolutePath());


        //File file = new File(pathFull);
        //boolean exist = file.exists();

        Log.d("write file", "yes");

        soundFile.WriteFile(fileName.getAbsoluteFile(), (int)startFrame, (int) endFrame - startFrame);

        //Log.d("existfile", String.valueOf(exist));
    }

    //path for create dir
    private String getPathCut(String nameRec){
        return SharedVariables.getPathApplicationFileSystem() + nameRec.replace(".mp3", "");
    }

}

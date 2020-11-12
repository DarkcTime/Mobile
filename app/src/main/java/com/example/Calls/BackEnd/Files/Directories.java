package com.example.Calls.BackEnd.Files;

import android.util.Log;

import com.example.Calls.Model.Record;

import java.io.File;
import java.io.IOException;

public class Directories {

    private Record record;
    public Directories(Record record){
        this.record = record;
    }

    public void createDirectories(){
        createDirectoryApplication();
        createDirectoryForContact();
        createDirectoryForSelectedRecord();
        createDirectoryForRecordsInSelectedRecord();
        createDirectoryForWorkWithApi();
    }

    private void createDirectoryApplication(){
        if(!new File(FileSystemParameters.getPathApplicationFileSystem()).mkdir())
            Log.d("createDirApp", "dir no create");
    }

    private void createDirectoryForContact(){
        if(!new File(FileSystemParameters.getPathForSelectedContact()).mkdir())
            Log.d("createDirForCon", "dir no create");
    }


    private void createDirectoryForRecordsInSelectedRecord(){
        if(!new File(FileSystemParameters.getPathForSelectedRecordsForCutter()).mkdir())
            Log.d("createDirForRecords", "dir no create");
    }

    private void createDirectoryForSelectedRecord(){
        if(!new File(FileSystemParameters.getPathForSelectedRecord()).mkdir())
            Log.d("createDirForSelRec", "dir no create");
    }

    private void createDirectoryForWorkWithApi(){
       if(!new File(FileSystemParameters.getPathForSelectedRecordApi()).mkdir())
           Log.d("createDirForApi", "dir no create");
    }

}

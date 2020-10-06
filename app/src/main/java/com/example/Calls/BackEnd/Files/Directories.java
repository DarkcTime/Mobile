package com.example.Calls.BackEnd.Files;

import android.util.Log;

import java.io.File;
import java.io.IOException;

public class Directories {


    public static void createDirectories(){
        createDirectoryApplication();
        createDirectoryForContact();
        createDirectoryForSelectedRecord();
        createDirectoryForRecordsInSelectedRecord();
        createDirectoryForWorkWithApi();
    }

    private static void createDirectoryApplication(){
        if(!new File(FileSystemParameters.getPathApplicationFileSystem()).mkdir())
            Log.d("createDirApp", "dir no create");
    }

    private static void createDirectoryForContact(){
        if(!new File(FileSystemParameters.getPathForSelectedContact()).mkdir())
            Log.d("createDirForCon", "dir no create");
    }


    private static void createDirectoryForRecordsInSelectedRecord(){
        if(!new File(FileSystemParameters.getPathForSelectedRecordsForCutter()).mkdir())
            Log.d("createDirForRecords", "dir no create");
    }

    private static void createDirectoryForSelectedRecord(){
        if(!new File(FileSystemParameters.getPathForSelectedRecord()).mkdir())
            Log.d("createDirForSelRec", "dir no create");
    }

    private static void createDirectoryForWorkWithApi(){
       if(!new File(FileSystemParameters.getPathForSelectedRecordApi()).mkdir())
           Log.d("createDirForApi", "dir no create");
    }

}

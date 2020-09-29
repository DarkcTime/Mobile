package com.example.Calls.BackEnd;

import android.annotation.SuppressLint;

import java.io.File;

public class FileSystemParameters {

    //region file system in application

    @SuppressLint("SdCardPath")
    private static final String PathApplicationFileSystem = "/storage/emulated/0/Android/data/com.Calls/";

    private static String getPathApplicationFileSystem(){
        return PathApplicationFileSystem;
    }

    //example: storage/emulated/0/Android/data/com.Calls/Миха/
    public static String getPathForSelectedContact(){
        return getPathApplicationFileSystem().concat(new Contacts().getNameCurrentContact()).concat("/");
    }

    //example: storage/emulated/0/Android/data/com.Calls/Миха/Call@4321432/
    public static String getPathForSelectedRecord(String nameRecord){
        return getPathForSelectedContact().concat(nameRecord.replace(".mp3", "").concat("/"));
    }

    public static String getPathForSelectedRecordApi(String nameRecord){
        return getPathForSelectedContact().concat(nameRecord.replace(".mp3", "").concat("/api/"));
    }

    //example: storage/emulated/0/Android/data/com.Calls/Миха/Call@4321432/result.txt
    public static String getPathFileResultForRecord(String nameRecord){
        return getPathForSelectedRecord(nameRecord).concat("result.txt");
    }

    //example: storage/emulated/0/Android/data/com.Calls/Миха/result.txt
    public static String getPathFileResultForSelectedContact(){
        return getPathForSelectedContact().concat("result.txt");
    }


}

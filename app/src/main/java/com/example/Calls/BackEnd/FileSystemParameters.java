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
        return getPathApplicationFileSystem()
                .concat(new Contacts().getNameCurrentContact())
                .concat("/");
    }

    //example: storage/emulated/0/Android/data/com.Calls/Миха/Call@4321432/
    public static String getPathForSelectedRecord(){
        return getPathForSelectedContact()
                .concat(Records.getNameSelectedRecord()
                        .replace(".mp3", "").concat("/"));
    }

    public static String getPathForSelectedRecordApi(){
        return getPathForSelectedContact().concat(Records
                .getNameSelectedRecord()
                .replace(".mp3", "")
                .concat("/api/"));
    }

    //example: storage/emulated/0/Android/data/com.Calls/Миха/Call@4321432/result.txt
    public static String getPathFileResultForRecord(){
        return getPathForSelectedRecord().concat("result.txt");
    }

    //example: storage/emulated/0/Android/data/com.Calls/Миха/result.txt
    public static String getPathFileResultForSelectedContact(){
        return getPathForSelectedContact().concat("result.txt");
    }


}

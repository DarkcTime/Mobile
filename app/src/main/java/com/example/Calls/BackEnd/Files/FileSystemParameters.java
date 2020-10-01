package com.example.Calls.BackEnd.Files;

import android.annotation.SuppressLint;

import com.example.Calls.BackEnd.Contacts.Contacts;
import com.example.Calls.BackEnd.Records.Records;

public class FileSystemParameters {

    //region file system in application

    @SuppressLint("SdCardPath")
    private static final String PathApplicationFileSystem = "/storage/emulated/0/Android/data/com.Calls/";

    public static String getPathApplicationFileSystem(){
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

    //example: storage/emulated/0/Android/data/com.Calls/Миха/Call@4321432/records
    public static String getPathForSelectedRecordsForCutter(){
        return getPathForSelectedContact()
                .concat(Records.getNameSelectedRecord()
                        .replace(".mp3", "")
                .concat("/records/"));
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

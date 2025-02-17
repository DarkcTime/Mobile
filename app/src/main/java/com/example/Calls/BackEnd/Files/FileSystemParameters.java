package com.example.Calls.BackEnd.Files;

import android.annotation.SuppressLint;

import com.example.Calls.BackEnd.Services.ContactsService;
import com.example.Calls.BackEnd.Services.RecordsService;
import com.example.Calls.Model.Repositories.ContactRepository;
import com.example.Calls.Model.Repositories.RecordRepository;

public class FileSystemParameters {

    //region file system in application

    @SuppressLint("SdCardPath")
    private static final String PathApplicationFileSystem = "/storage/emulated/0/Android/data/com.Calls/";
    public static String getPathApplicationFileSystem(){
        return PathApplicationFileSystem;
    }

    public final static String RESULTFILE = "result.txt";

    //example: storage/emulated/0/Android/data/com.Calls/Миха/
    public static String getPathForSelectedContact(){
        try{
            return getPathApplicationFileSystem()
                    .concat(ContactRepository.getSelectedContact().Name)
                    .concat("/");
        }
        catch (Exception ex){
            //TODO make catch
            return "";
        }
    }


    //example: storage/emulated/0/Android/data/com.Calls/Миха/Call@4321432/
    public static String getPathForSelectedRecord(){
        return getPathForSelectedContact()
                .concat(RecordRepository.getSelectedRecord().FullName
                        .replace(".mp3", "").concat("/"));
    }


    public static String getPathForSelectedRecordDir(){
        return getPathForSelectedContact()
                .concat(RecordRepository.getSelectedRecord().FullName
                        .replace(".mp3", ""));
    }

    //example: storage/emulated/0/Android/data/com.Calls/Миха/Call@4321432/records
    public static String getPathForSelectedRecordsForCutter(){
        return getPathForSelectedContact()
                .concat(RecordRepository.getSelectedRecord().FullName
                        .replace(".mp3", "")
                .concat("/records/"));
    }

    public static String getPathForSelectedRecordApi(){
        return getPathForSelectedContact().concat(
                RecordRepository.getSelectedRecord().FullName
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

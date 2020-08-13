package com.example.Calls.BackEnd;

import android.annotation.SuppressLint;

public class SharedVariables {

    //region file system in application
    @SuppressLint("SdCardPath")
    private static final String PathApplicationFileSystem = "/data/data/com.example.Calls/cache/";

    public static String getPathApplicationFileSystem()
    {
        return PathApplicationFileSystem;
    }

    //endregion


}

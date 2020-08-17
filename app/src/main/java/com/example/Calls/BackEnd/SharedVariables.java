package com.example.Calls.BackEnd;

import android.annotation.SuppressLint;

public class SharedVariables {


    //region file system in application
    @SuppressLint("SdCardPath")
    private static final String BufferPathForApplicationFileSystem = "/data/data/com.example.Calls/cache/";

    private static final String PathApplicationFileSystem = "/storage/emulated/0/Android/data/com.Calls/";

    public static String getPathApplicationFileSystem()
    {
        return PathApplicationFileSystem;
    }

    public static String getBufferPathForApplicationFileSystem(){
        return BufferPathForApplicationFileSystem;
    }


    //endregion

    //region DialogWindows
    private static final String ClickPositiveButton = "Positive";

    private static final String ClickNegativeButton = "Negative";

    private static final String ClickNetralButton = "Netral";

    public static String getClickPositiveButton(){
        return ClickPositiveButton;
    }

    public static String getClickNegativeButton(){
        return ClickNegativeButton;
    }
    //endregion
}

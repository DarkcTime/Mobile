package com.example.Calls.BackEnd.CutterFiles;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.Calls.BackEnd.FilesWork;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

//class for copy files in dir, for work with api
// /User/Record/api/
public class Copy {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void CopyFile(File sourceFile, File targetFile) throws IOException {
        Files.copy(sourceFile.toPath(), targetFile.toPath());
    }

    public static void createDirForWorkWithApi(String nameRecord) throws IOException {
        File dir = new File(FilesWork.getPathForOnlyRecord(nameRecord).concat("/api/"));
        if(!dir.exists()) if(dir.mkdir()) Log.d("FileCopy", "dir created");
    }


}

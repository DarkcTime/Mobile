package com.example.Calls.BackEnd.CutterFiles;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.Calls.BackEnd.Files.FileSystemParameters;
import com.example.Calls.BackEnd.Records.Records;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class WorkWithFileForCutter {

    private String nameRecord;

    public WorkWithFileForCutter(String _nameRecord){
        nameRecord = _nameRecord;
    }

    public void createDirsForCutter(){

        try{
            new File(FileSystemParameters.getPathForSelectedRecord()).mkdir();
            new File(FileSystemParameters.getPathForSelectedRecord()).mkdir();
            createDirForWorkWithApi();
        }
        catch (Exception ex){
            Log.d("ExceptionCreateDir", ex.getMessage());
        }

    }


    public File getSourceFile(){
        return  new File(Records.pathForFindRecords.concat(nameRecord));
    }

    private void createDirForWorkWithApi() throws IOException {
        File dir = new File(FileSystemParameters.getPathForSelectedRecordApi());
        if(!dir.exists()) if(dir.mkdir()) Log.d("FileCopy", "dir created");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void CopyFile(File sourceFile, File targetFile) throws IOException {
        Files.copy(sourceFile.toPath(), targetFile.toPath());
    }



}

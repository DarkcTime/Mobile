package com.example.Calls.BackEnd.CutterFiles;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.Calls.BackEnd.FileSystemParameters;
import com.example.Calls.BackEnd.FilesWork;
import com.example.Calls.BackEnd.Records;

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
            getDirForRecords().mkdir();
            createDirForWorkWithApi();
        }
        catch (Exception ex){
            Log.d("ExceptionCreateDir", ex.getMessage());
        }

    }


    public File getPathDirSelectedRecord(String nameRecord){
        return new File(FilesWork.getPathForOnlyRecord(nameRecord));
    }

    public File getDirForRecords(){
        return  new File(FilesWork.getPathForListRecord(nameRecord));
    }

    public File getSourceFile(){
        return  new File(Records.pathForFindRecords.concat(nameRecord));
    }


    private void createDirForWorkWithApi() throws IOException {
        File dir = new File(FilesWork.getPathForOnlyRecord(nameRecord).concat("/api/"));
        if(!dir.exists()) if(dir.mkdir()) Log.d("FileCopy", "dir created");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void CopyFile(File sourceFile, File targetFile) throws IOException {
        Files.copy(sourceFile.toPath(), targetFile.toPath());
    }



}

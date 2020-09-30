package com.example.Calls.BackEnd.Api;

//common class for work with Api

import android.util.Log;

import com.example.Calls.BackEnd.CutterFiles.WorkWithFileForCutter;
import com.example.Calls.BackEnd.FileSystem;
import com.example.Calls.BackEnd.FileSystemParameters;
import com.example.Calls.BackEnd.FilesWork;
import com.example.Calls.BackEnd.Records;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ApiMain{

    private static String nameRecord;

    private void setNameRecord(String _nameRecord){
        nameRecord = _nameRecord;
    }

    private String getNameRecord(){
        return nameRecord;
    }


    public ApiMain() throws IOException{
        try{
            setNameRecord(Records.getNameSelectedRecord());
        }
        catch (Exception e){
            Log.d("ApiMainEx", e.getMessage());
        }
    }

    //запускает перевод всех файлов для записи
    public void startApiTranslate() throws IOException{
        try{
            List<File> listRecords = ApiSpeech.getFiles(FilesWork.getPathForOnlyRecord(getNameRecord()).concat("/api/"), ".mp3");
            for (File rec : listRecords){
                new ApiSpeech(rec.getAbsolutePath()).SpeechToText();
            }
        }
        catch (Exception ex){
            Log.d("ExceptionStartApi", ex.getMessage());
        }
    }

    //добавляет данные в общий результат пользователя
    public void addTextInFullFileSelectedContact() throws IOException{
        FileSystem.WriteFile(FileSystemParameters.getPathForSelectedContact(),
                FileSystemParameters.getPathFileResultForRecord(nameRecord),true);
    }

    //читает полный файл пользователя
    public String readFullFileSelectedContact() throws IOException{
        return FileSystem.ReadFile(FileSystemParameters.getPathFileResultForSelectedContact());
    }

    //читает полный файл записи
    public String readFullFileSelectedRecord() throws IOException{
        return FileSystem.ReadFile(FileSystemParameters.getPathFileResultForRecord(nameRecord));
    }

    //создание файла с результатами для записи
    public void createResultFileForSelectedRecord() throws IOException{

        for (File file : ApiSpeech.getFiles(FileSystemParameters.getPathForSelectedRecordApi(nameRecord), ".txt")){
            Log.d("createResultFile", file.getAbsolutePath());
            ApiSpeech.WriteFile(FileSystemParameters.getPathFileResultForRecord(nameRecord), FileSystem.ReadFile(file.getAbsolutePath()),true);
        }
    }
}

package com.example.Calls.BackEnd.Api;

//common class for work with Api

import android.util.Log;

import com.example.Calls.BackEnd.Files.FileSystem;
import com.example.Calls.BackEnd.Files.FileSystemParameters;
import com.example.Calls.BackEnd.Records.Records;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ApiMain{

    //запускает перевод всех файлов для записи
    public void startApiTranslate() throws IOException{
        try{

            List<File> listRecords = FileSystem.getFilesWithSelectedExt(
                    FileSystemParameters.getPathForSelectedRecordApi(), ".mp3");
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
        FileSystem.WriteFile(FileSystemParameters.getPathFileResultForSelectedContact(),
                readFullFileSelectedRecord(),true);
    }

    //читает полный файл пользователя
    public static String readFullFileSelectedContact() throws IOException{
        return FileSystem.ReadFile(FileSystemParameters.getPathFileResultForSelectedContact());
    }

    //читает полный файл записи
    public String readFullFileSelectedRecord() throws IOException{
        return FileSystem.ReadFile(FileSystemParameters.getPathFileResultForRecord());
    }

    //создание файла с результатами для записи
    public void createResultFileForSelectedRecord() throws IOException{
        for (File file : FileSystem.getFilesWithSelectedExt(FileSystemParameters.getPathForSelectedRecordApi(), ".txt")){
            Log.d("createResultFile", file.getAbsolutePath());

            FileSystem.WriteFile(FileSystemParameters.getPathFileResultForRecord(),
                    FileSystem.ReadFile(file.getAbsolutePath()),true);
        }
    }
}

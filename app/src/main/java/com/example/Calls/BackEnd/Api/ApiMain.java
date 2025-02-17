package com.example.Calls.BackEnd.Api;

//common class for work with Api

import android.util.Log;

import com.example.Calls.BackEnd.Files.FileSystem;
import com.example.Calls.BackEnd.Files.FileSystemParameters;
import com.example.Calls.BackEnd.Services.ContactsService;
import com.example.Calls.Model.Contact;
import com.example.Calls.Model.Record;
import com.example.Calls.Model.Repositories.ContactRepository;
import com.example.Calls.Model.Repositories.RecordRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ApiMain{

    //запускает перевод всех файлов для записи
    public void startApiTranslate() throws IOException{
        try{

            List<File> listRecords = FileSystem.getFilesWithSelectedExtWithFilter(
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
    public void addTextInFullFileSelectedContact(String message) throws IOException{
        Contact selectContact = ContactRepository.getSelectedContact();
        String pathInFileResultSelectedContact = ContactsService.getPathWithFileResultForContact(selectContact);
        FileSystem.WriteFile(pathInFileResultSelectedContact,message,true);
    }

    //читает полный файл пользователя
    public String readFullFileSelectedContact() throws IOException{
        return FileSystem.ReadFile(FileSystemParameters.getPathFileResultForSelectedContact());
    }

    //читает полный файл записи
    public String readFullFileSelectedRecord() throws IOException{
        return FileSystem.ReadFile(FileSystemParameters.getPathFileResultForRecord());
    }

    //создание файла с результатами для записи
    public void createResultFileForSelectedRecord() throws IOException{
        for (File file : FileSystem.getFilesWithSelectedExtWithFilter(FileSystemParameters.getPathForSelectedRecordApi(), ".txt")){
            Log.d("createResultFile", file.getAbsolutePath());

            FileSystem.WriteFile(FileSystemParameters.getPathFileResultForRecord(),
                    FileSystem.ReadFile(file.getAbsolutePath()),true);
        }
    }
}

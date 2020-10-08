package com.example.Calls.Dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Debug;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;

import com.example.Calls.AboutContact;
import com.example.Calls.BackEnd.Contacts.Contacts;
import com.example.Calls.BackEnd.Debug.DebugMessages;
import com.example.Calls.BackEnd.Files.FileSystem;
import com.example.Calls.BackEnd.Files.FileSystemParameters;
import com.example.Calls.BackEnd.Records.Records;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Формирование файлового диалога
 * в окне AboutContact
*/
@SuppressLint("ValidFragment")
public class FilesDialog extends AppCompatDialogFragment {

    public AboutContact context;

    private List<File> listFiles = new ArrayList<>();

    private String[] listRecords;

    /**
     * Заполняет список для вывода файлового диалога
     * @param _context объект для вывода окна
     */
    @SuppressLint("ValidFragment")
    public FilesDialog(AboutContact _context) throws IOException{
        context = _context;
        listFiles.addAll(FileSystem.getFilesWithSelectedExtWithFilter(Records.getPathForFindRecords(), ".mp3"));
        setRecordsForContact();
    }

    /**
     * Фильтруте по контакту и наличию переведенной записи
     * файлами записей
     * @throws IOException ошибка может возникнуть при получении файлов
     */
    public void setRecordsForContact() throws IOException {

        filteringByContact();

        List<String> bufferList = new ArrayList<String>();

        for(File nameRecord : listFiles){

            if(isFilteringByHaveTranslating(nameRecord)) continue;

            bufferList.add(nameRecord.getName());
        }

        setListRecords(bufferList);

    }

    /**
     * фильтрует записи по выбранному контакту
     */
    private void filteringByContact(){
        Records.getFilterRecords(listFiles, Contacts.getNameCurrentContact());
    }

    private boolean isFilteringByHaveTranslating(File nameRecord) throws IOException{
        List<File> translatedRecords = FileSystem.getFiles(FileSystemParameters.getPathForSelectedContact());

        for(File nameFile : translatedRecords){
            if(isHaveRecord(nameRecord, nameFile)) return true;
        }
        return false;

    }

    private boolean isHaveRecord(File nameRecord, File nameFile){
        return nameRecord.getName()
                .replace(".mp3","")
                .equals(nameFile.getName());
    }

    private void setListRecords(List<String> bufferList){
        listRecords = new String[bufferList.size()];

        for (int i = 0; i < bufferList.size(); i++){
            listRecords[i] = bufferList.get(i);
        }
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        try{

            builder.setTitle("Количество записей: " + listRecords.length)
                    .setItems(listRecords, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            context.startGame(listRecords[which]);
                        }
                    });

            return builder.create();
        }
        catch (Exception ex){
            DebugMessages.ErrorMessage(ex, context, "onCreateFilesDialog");
            return builder.create();
        }


    }



}

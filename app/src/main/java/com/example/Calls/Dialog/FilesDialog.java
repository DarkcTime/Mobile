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
import com.example.Calls.MainActivity;

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

    private List<File> listFiles = new ArrayList<>();

    private String[] listRecords;

    private AboutContact aboutContact;

    /**
     * Заполняет список для вывода файлового диалога
     */
    @SuppressLint("ValidFragment")
    public FilesDialog(AboutContact _aboutContact) throws Exception{
        try{
            aboutContact = _aboutContact;
            listFiles.addAll(FileSystem.getFilesWithSelectedExtWithFilter(Records.getPathForFindRecords(), ".mp3"));
            setRecordsForContact();
        }
        catch (Exception ex){
            throw new Exception("FilesDialog - ".concat(ex.getMessage()));
        }
    }

    /**
     * Фильтруте по контакту и наличию переведенной записи
     * файлами записей
     * @throws IOException ошибка может возникнуть при получении файлов
     */
    public void setRecordsForContact() throws Exception {

        try{
            filteringByContact();

            List<String> bufferList = new ArrayList<String>();

            for(File nameRecord : listFiles){
                try{

                    //check dir for records at the user
                    if(Contacts.isHaveDirectoryForCurrentContact()){
                        if(isFilteringByHaveTranslating(nameRecord)) continue;
                    }

                    bufferList.add(nameRecord.getName());
                }
                catch (NullPointerException nullPointException){
                    //check next record
                }
            }

            setListRecords(bufferList);
        }
        catch (NullPointerException nulEx){
            throw new Exception("FilesDialog/setRecordsForContactNullPointException - 0".concat(nulEx.getMessage()));
        }
        catch (Exception ex){
            throw new Exception("FilesDialog/setRecordsForContact - ".concat(ex.getMessage()));
        }

    }

    /**
     * фильтрует записи по выбранному контакту
     */
    private void filteringByContact() throws Exception{
        try{
            Records.getFilterRecords(listFiles);
        }
        catch (Exception ex){
            throw new Exception("FilesDialog/filteringByContact - ".concat(ex.getMessage()));
        }

    }

    /**
     *
     * @param nameRecord
     * @return
     * @throws Exception
     */
    private boolean isFilteringByHaveTranslating(File nameRecord) throws Exception{
        try{
            List<File> listFilesForContact = FileSystem
                    .getFiles(FileSystemParameters
                            .getPathForSelectedContact());

            Log.d("listFilesSize", String.valueOf(listFilesForContact.size()));

            //TODO test
            for(File nameFile : listFilesForContact){
                Log.d("nameFileisFilter", nameFile.getName());
            }

            for(File nameFile : listFilesForContact){
                try{
                    if(nameFile.equals("")) continue;

                    if(isHaveRecord(nameRecord, nameFile)) return true;
                }
                catch (NullPointerException nullPoint){
                    //filter next file
                }
            }

            return false;
        }
        catch (Exception ex){
            throw new Exception("FilesDialog/isFilteringByHaveTranslating - ".concat(ex.getMessage()));
        }
    }


    private boolean isHaveRecord(File nameRecord, File nameFile) throws Exception{
        try{
            return nameRecord.getName()
                    .replace(".mp3","")
                    .equals(nameFile.getName());
        }
        catch (Exception ex){
            throw new Exception("FilesDialog/isHaveRecord - ".concat(ex.getMessage()));
        }
    }

    private void setListRecords(List<String> bufferList) throws Exception{
        try{
            listRecords = new String[bufferList.size()];

            for (int i = 0; i < bufferList.size(); i++){
                listRecords[i] = bufferList.get(i);
            }
        }
        catch (Exception ex){
            throw new Exception("FilesDialog/setListRecords".concat(ex.getMessage()));
        }

    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("Количество записей: " + listRecords.length)
                    .setItems(listRecords, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //show list record for contact
                            Log.d("startGame", "startGame");
                            aboutContact.startGame(listRecords[which]);
                        }
                    });

            return builder.create();
        } catch (Exception ex) {
            Log.d("FilesDialog", ex.getMessage());

            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("Exception").setMessage(ex.getMessage());

            return builder.create();

        }

    }

}

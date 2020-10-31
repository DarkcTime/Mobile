package com.example.Calls.BackEnd.Records;

import android.util.Log;

import com.example.Calls.BackEnd.Contacts.Contacts;
import com.example.Calls.BackEnd.Files.FileSystem;
import com.example.Calls.BackEnd.Files.FileSystemParameters;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListRecords {

    private ArrayList<Records> listRecords;

    //buffer list for generating
    private List<File> listFiles = new ArrayList<File>();

    public ListRecords() throws Exception{
        generateListRecords();
    }


    /**
     * set list for Records
     * @throws Exception all type errors
     */
    private void generateListRecords() throws Exception{
        try{
            listFiles.addAll(FileSystem.getFilesWithSelectedExtWithFilter(Records.getPathForFindRecords(), ".mp3"));


            listRecords = getFilteredArrayFiles();
        }
        catch (Exception ex){
            throw new Exception("fillRecFilDialog - ".concat(ex.getMessage()));
        }
    }

    /**
     * Фильтруте по контакту и наличию переведенной записи
     * файлами записей
     * @throws IOException ошибка может возникнуть при получении файлов
     */
    private ArrayList<String> getFilteredArrayFiles() throws Exception {

        try{
            //filter by Contact
            Records.getFilterRecords(listFiles);

            ArrayList<String> bufferList = new ArrayList<String>();

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

            return bufferList;
        }
        catch (NullPointerException nulEx){
            throw new Exception("FilesDialog/setRecordsForContactNullPointException - 0".concat(nulEx.getMessage()));
        }
        catch (Exception ex){
            throw new Exception("FilesDialog/setRecordsForContact - ".concat(ex.getMessage()));
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

}

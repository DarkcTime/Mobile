package com.example.Calls.BackEnd.Services;

import android.util.Log;

import com.example.Calls.BackEnd.Files.FileSystem;
import com.example.Calls.BackEnd.SharedClasses.SharedMethods;
import com.example.Calls.MainActivity;
import com.example.Calls.Model.Record;
import com.example.Calls.Model.Repositories.RecordRepository;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class RecordsService {
    final public static String currentPathForRecordsXiomi = "/storage/emulated/0/MIUI/sound_recorder/call_rec/";
    final public static String currentPathForZTE = "/sdcard/PhoneRecord/";

    final public String EnglishLanguageFilter = "Call@";
    final public String RussiaLanguageFilter = "Вызов@";

    public static boolean checkPath(String str) {
        File file = new File(str);
        return file.exists();
    }

    private static String pathForFindRecords = "";
    public static void setPathForFindRecords(String _pathForFindRecords) throws Exception {
        if (SharedMethods.isNullOrWhiteSpace(_pathForFindRecords)) {
            throw new Exception("setPathForFindRecords == null");
        }
        pathForFindRecords = _pathForFindRecords;
    }
    public static String getPathForFindRecords() throws Exception {
        if (SharedMethods.isNullOrWhiteSpace(pathForFindRecords)) {
            throw new Exception("getPathForFindRecords == null");
        }
        return pathForFindRecords;
    }

    private MainActivity mainActivity;
    public RecordsService(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    private ArrayList<File> listFiles = new ArrayList<File>();
    public void create() throws Exception{

        Log.d("pathFindRec", getPathForFindRecords());
        listFiles.addAll(FileSystem.getFilesWithSelectedExtWithFilter(getPathForFindRecords(), ".mp3"));
        /*
        if(!isExistingPathRecord()){
            //TODO if not exist
            return;
        }
        if(!isHavingRecords()){
                //TODO if not records
        }

         */

    }
    private boolean isExistingPathRecord() throws Exception{
        return new File(getPathForFindRecords()).exists();
    }
    private boolean isHavingRecords() throws Exception{
        return listFiles.size() > 0;
    }

    public void generateListRecords(){
        ArrayList<Record> bufferListRecords = new ArrayList<Record>();
        for(File file : listFiles){
            Record newRecord = new Record();
            newRecord.Path = file.getAbsolutePath();
            newRecord.FullName = file.getName();
            newRecord.Contact = getNameContactInRecord(newRecord.FullName);
            //no add record, if only number phone
            if(Character.isDigit(newRecord.Contact.charAt(0))) continue;
            newRecord.NumberPhone = getPhoneContactInRecord(newRecord.FullName);
            newRecord.Date = getDateInRecord(newRecord.FullName);
            newRecord.Time = getTimeInRecord(newRecord.FullName);
            //newRecord.mp();
            bufferListRecords.add(newRecord);
        }
        RecordRepository.setListRecords(bufferListRecords);
    }
    //get information from list files
    private String getNameContactInRecord(String nameRecord) {
        if (nameRecord.contains("@"))
            return nameRecord.substring(nameRecord.indexOf("@") + 1, nameRecord.lastIndexOf("("));
        else
            return nameRecord.substring(0, nameRecord.lastIndexOf("("));

    }
    private String getPhoneContactInRecord(String nameRecord){
        return nameRecord.substring(nameRecord.lastIndexOf("(") + 1, nameRecord.lastIndexOf(")"));
    }
    private String getDateInRecord(String nameRecord){
        //example 20200810
        int start = nameRecord.lastIndexOf("_") + 1;
        int end = start + 8;
        String date = nameRecord.substring(start, end);
        //number + month + year
        String res = date.substring(6,8) + "." + date.substring(4,6) + "." + date.substring(0,4);
        Log.d("date", res);
        return res;

    }
    private String getTimeInRecord(String nameRecord){
        int start = nameRecord.lastIndexOf("_") + 9;
        int end = start + 6;
        //example 233616
        String time = nameRecord.substring(start, end);
        //hours + minutes + seconds
        String res = time.substring(0,2) + ":" + time.substring(2,4) + ":" + time.substring(4,6);
        Log.d("time", time);
        return  res;
    }

    /**
     * Определяет включает ли имя контакта!!!, в свой состав имя записи!!!
     *
     * @param nameRecord  имя записи
     * @param nameContact имя контакта
     * @return true если вклюает
     */

    public static boolean isConstrainNameRecord(String nameContact, String nameRecord) {
        return nameContact.contains(nameRecord);
    }

    public static class MyFileNameFilter implements FilenameFilter {

        private String ext;

        public MyFileNameFilter(String ext) {
            this.ext = ext.toLowerCase();
        }

        @Override
        public boolean accept(File dir, String name) {
            return name.toLowerCase().endsWith(ext);
        }
    }


}

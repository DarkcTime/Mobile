package com.example.Calls.BackEnd.Records;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import com.example.Calls.BackEnd.Contacts.Contacts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

//работа с записями звонков
public class Records {

    //местонахождение записей
    private static String pathForFindRecords = "";

    public static void setPathForFindRecords(String _pathForFindRecords){
        pathForFindRecords = _pathForFindRecords;
    }

    public static String getPathForFindRecords(){
        return pathForFindRecords;
    }

    //данные о выбранной записи
    private static String NameSelectedRecord;

    public static String getNameSelectedRecord(){
        return NameSelectedRecord;
    }

    public static void setNameSelectedRecord(String _nameRecord){
        NameSelectedRecord = _nameRecord;
    }

    public static String getFullPathSelectedRecord(){
        return getPathForFindRecords().concat(getNameSelectedRecord());
    }


    final public static String currentPathForRecordsXiomi = "/storage/emulated/0/MIUI/sound_recorder/call_rec/";
    final public static String currentPathForZTE = "/sdcard/PhoneRecord/";

    final public String EnglishLanguageFilter = "Call@";
    final public String RussiaLanguageFilter = "Вызов@";

    final public static String pathFileFilterRecords = "/data/data/com.example.Calls/cache/trRecord";

    public static boolean checkPath(String str){
        File file = new File(str);
        return file.exists();
    }

    public static void resetFile(String phone){
        try{
            String path = pathFileFilterRecords + phone + ".txt";
            File file = new File(path);
            PrintWriter pw = new PrintWriter(new FileWriter(file,false));
            pw.println("");
            pw.close();
        }
        catch (Exception ex){
            ex.toString();
        }

    }

    public static void writeRecord(String nameRecord) throws Exception{
        String path = pathFileFilterRecords + ".txt";
        File file = new File(path);
        PrintWriter pw = new PrintWriter(new FileWriter(file,true));
        pw.println(nameRecord);
        pw.close();
    }

    //TODO Влад: разобраться с фильтрацией записей
    public static void getFilterRecords(List<File> list, String filter){
        Iterator<File> iterator = list.iterator();
        Log.d("list", String.valueOf(list.size()));
        String nameRecord = "";
        while (iterator.hasNext())
        {
            nameRecord = getNameContactInRecord(iterator.next().getName());
            Log.d("nameRecord", nameRecord);
            Log.d("filter", filter);


            if (isConstrainNameRecord(nameRecord, filter)){
                Log.d("isConstrain", "true");
            }
            else if(!nameRecord.equals(filter)){
                Log.d("nameRec", "true");
                iterator.remove();
            }

        }

    }


    /**
     * Определяет включает ли имя контакта, в свой состав имя записи
     * @param nameRecord имя записи
     * @param nameContact имя конктас
     * @return true если вклюает
     */

    public static boolean isConstrainNameRecord(String nameRecord, String nameContact){
        return nameContact.contains(nameRecord);
    }

    public static boolean isFileExists(String path){
        File file = new File(path);
        return file.exists();
    }

    public static boolean isRecordExist(String nameRecord) throws Exception{
        List<String> listRecords = new ArrayList<String>();
        listRecords = Records.readRecord();
        boolean check = false;
        for(String record : listRecords){
            check = record.equals(nameRecord);
            if(check)
                break;
        }
        return check;
    }


    public static List<String> readRecord() throws IOException {
        String path = pathFileFilterRecords + ".txt";
        List<String> result = new ArrayList<>();
        String str;
        try{
            File file = new File(path);
            boolean create = file.createNewFile();
            BufferedReader br = new BufferedReader(new FileReader(path));
            while ((str = br.readLine()) != null){
                result.add(str);
            }
            br.close();
            return result;
        }
        catch (Exception ex){

            return result;
        }

    }

    public ListView createListView(Context context) {
        ListView listView = new ListView(context);

        return listView;
    }

    /**
     * Выделяет имя контакта из записи
     * @param fullPath путь к записи
     * @return имя контакта
     */
    public static String getNameContactInRecord(String fullPath){
        String fileName = new File(fullPath).getName();
        return fileName.substring(fileName.indexOf("@") + 1,fileName.lastIndexOf("("));
    }



    public String getPhoneNumberSelectedRecord(String path){
        return path.substring(path.lastIndexOf("(") + 1, path.lastIndexOf(")"));
    }

    public String getDateSelectedRecord(String path){
        String resultStr = path.substring(path.indexOf(")") + 1);
        return resultStr;
    }

    public String getFileName(String path){
        String resultStr = path.substring(path.indexOf("@") - 3);
        return resultStr;
    }


    public static class MyFileNameFilter implements FilenameFilter {

        private String ext;

        public MyFileNameFilter(String ext){
            this.ext = ext.toLowerCase();
        }

        @Override
        public boolean accept(File dir, String name) {
            return name.toLowerCase().endsWith(ext);
        }
    }




}

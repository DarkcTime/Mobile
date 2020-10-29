package com.example.Calls.BackEnd.Records;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.Calls.BackEnd.Contacts.Contacts;
import com.example.Calls.BackEnd.Files.FileSystem;
import com.example.Calls.BackEnd.SharedClasses.SharedMethods;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

//работа с записями звонков
public class Records {


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

    public static boolean isExistingPathRecord() throws Exception{
        return new File(getPathForFindRecords()).exists();
    }

    public static boolean isHavingRecords() throws Exception{
        List<File> files = FileSystem.getFilesWithSelectedExtWithFilter(getPathForFindRecords(), ".mp3");
        return files.size() != 0;
    }


    //данные о выбранной записи
    private static String NameSelectedRecord;

    public static String getNameSelectedRecord() {
        return NameSelectedRecord;
    }

    public static void setNameSelectedRecord(String _nameRecord) {
        NameSelectedRecord = _nameRecord;
    }

    public static String getFullPathSelectedRecord() {
        try {
            return getPathForFindRecords().concat(getNameSelectedRecord());
        } catch (Exception ex) {
            //TODO make catcher
            return "";
        }
    }


    final public static String currentPathForRecordsXiomi = "/storage/emulated/0/MIUI/sound_recorder/call_rec/";
    final public static String currentPathForZTE = "/sdcard/PhoneRecord/";

    final public String EnglishLanguageFilter = "Call@";
    final public String RussiaLanguageFilter = "Вызов@";

    public static boolean checkPath(String str) {
        File file = new File(str);
        return file.exists();
    }

    /**
     * filter record for selected contact
     *
     * @param list list records
     */
    public static void getFilterRecords(List<File> list) {

        try {
            String nameContact = Contacts.getNameCurrentContact();
            //create object iterator
            Iterator<File> iterator = list.iterator();
            int i = 0;
            while (iterator.hasNext()) {
                try {

                    String nameRecord = getNameContactInRecord(iterator.next().getName());

                    //check contact for this record
                    if (isConstrainNameRecord(nameContact, nameRecord)) {
                        continue;
                    }
                    if (!nameRecord.equals(nameContact))
                        iterator.remove();
                } catch (NullPointerException nullPointEx) {
                    //check next iterator
                }
            }
        } catch (Exception ex) {
            //TODO make catcher
        }


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

    /**
     * Выделяет имя контакта из записи
     *
     * @param fullPath путь к записи
     * @return имя контакта
     */
    public static String getNameContactInRecord(String fullPath) {
        String fileName = new File(fullPath).getName();
        if (fileName.contains("@")) {
            String name = fileName.substring(fileName.indexOf("@") + 1, fileName.lastIndexOf("("));
            Log.d("@", name);
            return name;
        } else {
            String name = fileName.substring(0, fileName.lastIndexOf("("));
            Log.d("not", name);
            return name;
        }

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

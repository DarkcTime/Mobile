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

    public static String durationSelectRecord;

    public static String pathForFindRecords = "";

    final public static String currentPathForRecordsXiomi = "/storage/emulated/0/MIUI/sound_recorder/call_rec/";
    final public static String currentPathForZTE = "/sdcard/PhoneRecord/";

    final public String EnglishLanguageFilter = "Call@";
    final public String RussiaLanguageFilter = "Вызов@";

    final public static String pathFileFilterRecords = "/data/data/com.example.Calls/cache/trRecord";

    private static String NameSelectedRecord;

    public static String getNameSelectedRecord(){
        return NameSelectedRecord;
    }

    public static void setNameSelectedRecord(String _nameRecord){
        NameSelectedRecord = _nameRecord;
    }


    //выдает список файлов для xiaomi
    public static List<File> getFiles(String currentPath){
        File directory = new File(currentPath);
        String ext = ".mp3";
            List<File> fileList = Arrays.asList(directory.listFiles(new MyFileNameFilter(ext)));
            Collections.sort(fileList, new Comparator<File>() {
                @Override
                public int compare(File file, File file2) {
                    if (file.isDirectory() && file2.isFile())
                        return -1;
                    else if (file.isFile() && file2.isDirectory())
                        return 1;
                    else
                        return file.getPath().compareTo(file2.getPath());
                }
            });
            return fileList;

    }

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
            nameRecord = getNameSelectedRecord(iterator.next().getName());
            Log.d("nameRecord", nameRecord);
            Log.d("filter", filter);


            if (isConstrainNameRecord(nameRecord, filter, 10)){
                Log.d("isConstrain", "true");
            }
            else if(!nameRecord.equals(filter)){
                Log.d("nameRec", "true");
                iterator.remove();
            }

        }

    }

    public static boolean isConstrainNameRecord(String nameRecord, String nameContact, int numberSym){
        if(nameRecord.length() >= numberSym){
            return nameContact.contains(nameRecord);
        }
        return false;
    }



    public static String[] getUniqueList(List<File> listFiles){
        List<String> listNames = new ArrayList<String>();
        for (File var : listFiles){
            listNames.add(getNameSelectedRecord(var.getPath()));
        }
        HashSet<String> hashSetListNames = new HashSet<>(listNames);
        String[] strListNames = new String[hashSetListNames.size()];
        hashSetListNames.toArray(strListNames);
        return  strListNames;
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

    public static String getNameSelectedRecord(String path){
        return path.substring(path.indexOf("@") + 1, path.lastIndexOf("("));

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

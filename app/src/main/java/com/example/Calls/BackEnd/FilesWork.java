package com.example.Calls.BackEnd;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Scanner;

public class FilesWork {

    private final String pathFile = "/data/data/com.example.Calls/cache/";

    private String pathSelectedRecord;

    public static String getPathForSelectedUser(){
        return SharedVariables.getPathApplicationFileSystem().concat(new Contacts().getNameCurrentContact());
    }

    public static String getPathForWorkWithApi(String nameRecord){
        return getPathForOnlyRecord(nameRecord).concat("/api/");
    }

    public static String getPathForOnlyRecord(String nameRecord){
        return getPathForSelectedUser().concat("/").concat(nameRecord.replace(".mp3", "").concat("/"));
    }

    public static String getPathForListRecord(String nameRecord){
        return getPathForOnlyRecord(nameRecord).concat("/records");
    }




    /*
    public void writeFile(String content, String number,String nameRecord) throws Exception{
        //генерация пути
        boolean append = true;
        String path = pathFile + number;
        if(SelectMethodSaveText.oneMessage == selMet)
        {
            path += nameRecord;
            append = false;
        }

        path += ".txt";

        File file = new File(path);
        PrintWriter pw = new PrintWriter(new FileWriter(file,append));

        if(SelectMethodSaveText.allText == selMet){
            pw.println("------------------------");
        }
        pw.println(content);
        pw.close();

    }

     */
    public void writeFile(String content, String number) throws Exception{
        //генерация пути
        String path = pathFile + number + ".txt";

        File file = new File(path);
        PrintWriter pw = new PrintWriter(new FileWriter(file, false));

        pw.println(content);
        pw.close();

    }

    /*
    public String readFile(String number, SelectMethodSaveText selMet, String nameRecord) throws Exception{
        String result = "";
        String path = pathFile + number;
        if(SelectMethodSaveText.oneMessage == selMet)
        {
            path += nameRecord;
        }
        path += ".txt";
        File file = new File(path);
        if(!file.exists())
            writeFile("",number, selMet,nameRecord);

        FileReader fr = new FileReader(file);
        Scanner scan = new Scanner(fr);
        while (scan.hasNextLine()){
            result += "\n" + scan.nextLine();
        }
        fr.close();
        return result;
    }

     */
}

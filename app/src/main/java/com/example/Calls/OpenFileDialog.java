package com.example.Calls;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import android.content.Intent;
import android.app.Activity;



public class OpenFileDialog  extends AlertDialog.Builder{

   private MediaPlayer mPlayer;

   private String nameSelectedRecord , phoneNumberSelectedRecord , pathSelectedRecord , dateSelectedRecord, fileName;

   public String getNameSelectedRecord (){
       return  nameSelectedRecord;
   }

   public String getPhoneNumberSelectedRecord(){
       return  phoneNumberSelectedRecord;
   }

   public String dateSelectedRecord(){
       return  dateSelectedRecord;
   }

   public String getPathSelectedRecord(){
       return  pathSelectedRecord;
   }

   public MediaPlayer getRecord(){
       return mPlayer;
   }

   public String getFileName(){
       return fileName;
   }

   private List<File> files = new ArrayList<File>();

   public  ListView listView;

    public OpenFileDialog(final Context context, final  TextView textViewSelectRecorder) {
        super(context);


        listView = createListView(context);
        FileAdapter adapter = new FileAdapter(context , files);
        //adapter.getFilter().filter();
        listView.setAdapter(adapter);
        setView(listView)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton(android.R.string.cancel , null);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                final ArrayAdapter<File> adapter = (FileAdapter) adapterView.getAdapter();
                File file = adapter.getItem(index);
                pathSelectedRecord = file.getPath();
                nameSelectedRecord = getNameSelectedRecord(pathSelectedRecord);
                phoneNumberSelectedRecord = getPhoneNumberSelectedRecord(pathSelectedRecord);
                dateSelectedRecord = getDateSelectedRecord(pathSelectedRecord);
                fileName = getFileName(pathSelectedRecord);

                Toast.makeText(getContext() , "Выбрана запись: " + getFileName(pathSelectedRecord), Toast.LENGTH_SHORT).show();

            }
        });

    }


    //получаем список файлов
    private List<File> getFiles(String directoryPath){
        File directory = new File(directoryPath);
        List<File> fileList = Arrays.asList(directory.listFiles());
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

    public ListView createListView(Context context) {
        ListView listView = new ListView(context);

        return listView;
    }

    private String getNameSelectedRecord(String path){
        String resultStr = path.substring(path.indexOf("@") + 1, path.indexOf("("));
        return resultStr;
    }

    private String getPhoneNumberSelectedRecord(String path){
        String resultStr = path.substring(path.indexOf("(") + 1, path.indexOf(")"));
        return resultStr;
    }

    private String getDateSelectedRecord(String path){
        String resultStr = path.substring(path.indexOf(")") + 1);
        return resultStr;
    }

    private String getFileName(String path){
        String resultStr = path.substring(path.indexOf("@") - 3);
        return resultStr;
    }



}



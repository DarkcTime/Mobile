package com.example.Calls.BackEnd;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FileSystem {

    //запись данных в файл
    public static void WriteFile(String path, String str, boolean append) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path, append));
        writer.write(str);
        writer.close();
    }

    //чтение данных из файла
    public static String ReadFile(String path) throws IOException {
        File file = new File(path);
        if (file.exists()) {
            int length = (int) file.length();
            byte[] bytes = new byte[length];
            FileInputStream in = new FileInputStream(file);
            try {
                in.read(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                in.close();
            }
            return new String(bytes);
        } else {
            Log.d("FileSystemReadFile","File no exist");
            return "";
        }
    }

    //получить список файлов из выбранной директории
    public static List<File> getFilesWithSelectedExt(String path, String ext) throws IOException{
        File directory = new File(path);
        return Arrays.asList(directory.listFiles(new MyFileNameFilter(ext)));

        //TODO test
        /*
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

         */
    }

}

class MyFileNameFilter implements FilenameFilter {

    private String ext;

    public MyFileNameFilter(String ext){
        this.ext = ext.toLowerCase();
    }

    @Override
    public boolean accept(File dir, String name) {
        return name.toLowerCase().endsWith(ext);
    }
}

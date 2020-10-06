package com.example.Calls.BackEnd.Files;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.Calls.BackEnd.CutterFiles.CutterInterval;
import com.example.Calls.BackEnd.CutterFiles.FileForCutter;
import com.example.Calls.BackEnd.Records.Records;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
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

    /**
    Генерирует список файлов для записи
     **/
    public static List<FileForCutter> getFilesForCutter(List<CutterInterval> intervalList){
        int i = 0;
        List<FileForCutter> fileForCutterList = new ArrayList<FileForCutter>();

        for (CutterInterval interval : intervalList) {
            // генерация параметров
            int duration = interval.getEnd() - interval.getStart();
            File targetFile = new File(FileSystemParameters.getPathForSelectedRecordsForCutter().concat(String.valueOf(i)).concat(".mp3"));
            File sourceFile = new File(Records.getFullPathSelectedRecord());

            //генерация объекта
            FileForCutter fileForCutter = new FileForCutter(interval.getStart(),
                    duration,
                    sourceFile,
                    targetFile);

            //заполнение листа
            fileForCutterList.add(fileForCutter);
            i++;
        }

        return fileForCutterList;
    }

    /**
     * Копирует файл
     * @param sourceFile файл для копирования
     * @param targetFile новый файл
     * @throws IOException
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void CopyFile(File sourceFile, File targetFile) throws IOException {
        Files.copy(sourceFile.toPath(), targetFile.toPath());
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

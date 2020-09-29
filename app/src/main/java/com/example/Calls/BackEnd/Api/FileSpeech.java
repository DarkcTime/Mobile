package com.example.Calls.BackEnd.Api;

import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.widget.Toast;

import com.example.Calls.BackEnd.Contacts;
import com.example.Calls.BackEnd.CutterFiles.WorkWithFileForCutter;
import com.example.Calls.BackEnd.FilesWork;
import com.example.Calls.BackEnd.Records;
import com.example.Calls.BackEnd.SharedVariables;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Класс файловой системы перевода аудио в текст
 */
public class FileSpeech {



    /**
     * Метод для записи в файл
     *
     * @param path Путь к файлу
     * @param str Данные для записи
     * @param append Создавать новый файл, или дописывать в текущий
     * @throws IOException
     */
    public static void WriteFile(String path, String str, boolean append) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path, append));
        writer.write(str);
        writer.close();
    }


    /**
     * Чтение файла
     *
     * @param file путь к файлу
     * @return Возврат текста содержащегося в файле
     * @throws IOException
     */
    protected static String ReadFile(File file) throws IOException {
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
            return null;
        }
    }




    protected static List<File> getFiles(String path, String ext) throws IOException{
        File directory = new File(path);
        List<File> fileList = Arrays.asList(directory.listFiles(new Records.MyFileNameFilter(ext)));
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

    //зачем?

    /**
     * Получение длины аудио
     *
     * @param path Путь к аудио
     * @return Длина аудио
     */
    public static int getLengthAudio(String path) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);
        String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        return Integer.parseInt(durationStr);
    }


}


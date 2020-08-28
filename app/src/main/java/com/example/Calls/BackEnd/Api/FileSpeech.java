package com.example.Calls.BackEnd.Api;

import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.Calls.BackEnd.Contacts;
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
     * @param path Путь к файлу
     * @param data Данные для записи
     * @throws IOException
     */
    protected static void WriteFile(String path, byte[] data) throws IOException {
        FileOutputStream stream = new FileOutputStream(path);
        try {
            stream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stream.close();
        }
    }

    /**
     * Чтение файла
     * @param file путь к файлу
     * @return Возврат текста содержащегося в файле
     * @throws IOException
     */
    protected static String ReadFile(File file) throws IOException {
        if (file.exists()){
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
        }
        else {
            return null;
        }
    }


    //выдает список файлов для xiaomi
    //TODO переписать метод для получения .mp и .txt
    public static List<File> getFiles(String currentPath){
        File directory = new File(currentPath);
        String ext = ".mp3";
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

    //write result files in .txt files
    public static void WriteFileOnSpeech(Contacts contact, String content, int stage, String pathRecord, String pathDir) throws IOException {
        //путь с файлами
        String file = String.valueOf(stage).concat(".mp3.txt");
        String path = pathDir.concat(file);

        Log.d("pathWriteAfterApi", path);

       // String fullContent = content;

        File inputStream = new File(path);

        //if (inputStream.exists()) fullContent = ReadFileSpeech(contact) + "\n" + content;

        Log.d("content", content);

        WriteFile(path,content.getBytes());
    }


    //зачем?
    /**
     * Получение длины аудио
     * @param path Путь к аудио
     * @return Длина аудио
     */
    public static int getLengthAudio(String path){
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);
        String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        return Integer.parseInt(durationStr);
    }

    /**
     * Чтение полного файла разговра.
     * @param contact Конткакт у которого необходимо прочитать файл разгвора
     * @return текст файла разговора
     * @throws IOException
     * file = path + phoneNumber + one + .txt
     */
    /*
    public static String ReadFileSpeech(Contacts contact) throws IOException {
        String path = FilesWork.getPathForSelectedUser() + "/result.txt";
        File file = new File(path);
        if(!file.exists())
            WriteFileOnSpeech(contact,"");
        return ReadFile(file);
    }

     */

}


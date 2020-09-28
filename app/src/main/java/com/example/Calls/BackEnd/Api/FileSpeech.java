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

    private static String nameRecord;

    /**
     * Метод для записи в файл
     *
     * @param path Путь к файлу
     * @param str Данные для записи
     * @throws IOException
     */
    public static void WriteFile(String path, String str) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
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

    //TODO rewrite method
    public static void WriteFullFile() throws IOException{
        WorkWithFileForCutter workWithFileForCutter = new WorkWithFileForCutter(nameRecord);

        for (File file : getFiles(FilesWork.getPathForOnlyRecord(nameRecord).concat("/api/"), ".txt")){
            WriteFileAddData(workWithFileForCutter.getDirForRecord().getAbsolutePath().concat("/result.txt"), ReadFile(file));
        }

    }

    private static void WriteFileAddData(String targetFilePath, String str) throws IOException{
        BufferedWriter writer = new BufferedWriter(new FileWriter(targetFilePath,true));
        writer.write(str);
        writer.close();
    }



    private static List<File> getFiles(String path, String ext) throws IOException{
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



    //TODO decomposition func
    public static void startApiTranslate(String _nameRecord) throws IOException{
        //TODO catch error
        if(_nameRecord.isEmpty()) return;

        nameRecord = _nameRecord;

        List<File> records = getFiles(FilesWork.getPathForOnlyRecord(nameRecord).concat("/api/"), ".mp3");

        try{

            ApiSpeech api = new ApiSpeech();

            for (File rec : records){
                Log.d("SpeechToText", rec.getAbsolutePath());
                api.SpeechToText(rec.getAbsolutePath());
            }

        }
        catch (Exception ex){
            Log.d("ExceptionStartApi", ex.getMessage());
        }
    }

    //TODO to rewrite the method
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

        WriteFile(path, content);
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


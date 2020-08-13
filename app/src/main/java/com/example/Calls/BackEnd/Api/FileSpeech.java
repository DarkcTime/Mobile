package com.example.Calls.BackEnd.Api;

import android.media.MediaMetadataRetriever;
import android.util.Log;

import com.example.Calls.BackEnd.Contacts;
import com.example.Calls.BackEnd.SharedVariables;

import java.io.*;

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
            String contents = new String(bytes);
            return contents;
        }
        else {
            return null;
        }
    }

    /**
     * Запись в файл с полным ответом
     * @param contact контакт к токорому закреплен этот ответ
     * @param content Сущесвующие данные
     * @throws IOException
     * file //path + phoneNumber + one + .txt
     */
    public static void WriteFileOnSpeech(Contacts contact, String content,
                                         SelectMethodSaveText selectMethodSaveText) throws IOException {
        //путь с файлами
        String path = SharedVariables.getPathApplicationFileSystem();
        //переменная с контенотом
        String fullContent = content;
        //если one == "" добавляет one к имени файла
        String one = SelectMethodSaveText.oneMessage == selectMethodSaveText ? "one" : "";

        File inputStream = new File(path + contact.getPhoneNumberCurrentContact()+one+".txt");

        if (inputStream.exists()) fullContent = ReadFileSpeech(contact, selectMethodSaveText) + " " + content;

        //записывает контент в общий файл
        WriteFile(path + contact.getPhoneNumberCurrentContact()+one+".txt",fullContent.getBytes());
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
    public static String ReadFileSpeech(Contacts contact, SelectMethodSaveText selectMethodSaveText) throws IOException {
        String path = SharedVariables.getPathApplicationFileSystem();
        String one = SelectMethodSaveText.oneMessage == selectMethodSaveText ? "one" : "";
        File file = new File(path + contact.getPhoneNumberCurrentContact()+one+".txt");
        if(!file.exists())
            WriteFileOnSpeech(contact,"",SelectMethodSaveText.allText);
        
        String  readFile = ReadFile(file);
        if(one.equals("one"))
            file.delete();

        return readFile;
    }

    /**
     * !ОПАСНО! Производит сброс данных приложения
     */
    public static void ClearAllDataApp(){
        File file = new File(SharedVariables.getPathApplicationFileSystem());
        file.delete();
        file.mkdir();
    }

    //зачем?
    /**
     * Отчистка временного файла текста
     * @param contact контакт временный файл котрого нужно отчиситить
     * @throws IOException
     */
    public static void OneFileClear(Contacts contact) throws IOException {
        String path = "/data/data/com.example.Calls/cache/";
        WriteFile(path + contact.getPhoneNumberCurrentContact()+"one"+".txt","".getBytes());

    }
}


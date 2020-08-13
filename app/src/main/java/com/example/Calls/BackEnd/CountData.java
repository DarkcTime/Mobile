package com.example.Calls.BackEnd;

import android.media.MediaPlayer;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CountData {

    public static String getCountMinute(Contacts contact){
        /*
        MediaPlayerForRecords mpf = new MediaPlayerForRecords();
        int duration = 0;
        try{
            List<String> array = Records.readRecord(contact.getPhoneNumberCurrentContact());
            for (String elem : array){
                File file = new File(elem);
                if(file.exists()){
                    MediaPlayer mp = new MediaPlayer();
                    mp.setDataSource(elem);

                    mp.setLooping(true);
                    mp.setVolume(0.5f, 0.5f);
                    mp.prepare();

                    duration += mp.getDuration();
                }
            }
            //return String.valueOf(duration);
            return mpf.createTimeLabel(duration);
        }
        catch (IOException ex){
            return mpf.createTimeLabel(0);
        }

         */
        return "";
    }

    public static String getCountMinuteStr(String countMinute){
        return "Длительность переведенных записей: " + countMinute;
    }

    public static int getCountWords(String text){
        int result = 0;
            //Проверяем каждый символ, не пробел ли это
            for (int i = 0; i < text.length(); i++) {
                if(text.charAt(i) == ' '){
                    //Если пробел - увеличиваем количество слов на 1
                    result ++;
                }
            }
            return result;
    }

    public static String getCountWordsStr(int count){
        return "Количество слов: " + String.valueOf(count) + "/1000";
    }





}

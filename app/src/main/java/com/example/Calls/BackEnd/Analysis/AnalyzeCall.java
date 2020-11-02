package com.example.Calls.BackEnd.Analysis;

import android.media.MediaPlayer;

import com.example.Calls.BackEnd.Services.RecordsService;

import java.io.File;

//TODO refactor class
public class AnalyzeCall{
    private int MinutesLeft;
    private int WordsLeft;

    public int getMinutesLeft() {
        return MinutesLeft;
    }

    public void setMinutesLeft(int minutesLeft) {
        MinutesLeft = minutesLeft;
    }

    public int getWordsLeft() {
        return WordsLeft;
    }

    public void setWordsLeft(int wordsLeft) {
        WordsLeft = wordsLeft;
    }

    /**
     * СОздание анализа звонка
     * @param MinutesLeft осталось минут
     * @param WordsLeft Осталось слов
     */
    public AnalyzeCall(int MinutesLeft,int WordsLeft){
        setMinutesLeft(MinutesLeft);
        setWordsLeft(WordsLeft);
    }

    public static  String createTimeLabel(int time){
        String timeLabel = "";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;

        timeLabel = min + ":";
        if (sec < 10 ) timeLabel += "0";
        timeLabel += sec;
        return timeLabel;

    }

    public static String setTimeTranslated(String time){
        return "Примерное время перевода записи: " + time;
    }

    public static String setTimeTranslated(String time, String all){
        return "Примерное время перевода выбранных записей: " + time;
    }

    public static int countSecond(int time){
        double sec = time / 1000 * 0.2;
        return (int)Math.round(sec);
    }



    public static int getDurationRecord(File file) throws Exception{
        int result = 0;
        MediaPlayer mp = new MediaPlayer();
        mp.setDataSource(RecordsService.getPathForFindRecords() + file.getName());
        mp.prepare();
        result += AnalyzeCall.countSecond(mp.getDuration());
        return result;
    }

    public static String setDurationRecords(int duration){
        return AnalyzeCall.setTimeTranslated(AnalyzeCall.timeNeed(duration), "all");
    }

    public static String timeNeed(int sec){
        int time = sec * 1000;
        return createTimeLabel(time);
    }


    public static String setNumberWords(int count){
        return  "Количество слов: " + String.valueOf(count);
    }

    public static  int getCountWords(String str){
        int count = 1;
        if(str.equals("") || str.equals("------------------------")) return 0;
        char[] chars = str.toCharArray();
        for(char sym : chars){
            if(sym == ' ')
                count++;
        }
        return count;
    }

    public static String setCountWordsNeed(int count){
        if(count < 1000){
            int num = 1000 - count;
            return "Для получения псих. портрета необходимо набрать дополнительные слова: " + num;
        }
        else{
            return  "Поздравляем! Вы набрали необходимое количество слов: " + count;
        }

    }





}
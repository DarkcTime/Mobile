package com.example.Calls.BackEnd.Media;


import android.media.MediaPlayer;

import com.example.Calls.BackEnd.Analysis.AnalyzeCall;
import com.example.Calls.PaintGame.SamplePlayer;

//TODO включить код логики media в данный класс
public class MediaPlayerClass {

    public MediaPlayerClass(){

    }


    public static int getCurrentPositionSec(SamplePlayer mp){
        return (int)Math.round(mp.getCurrentPosition() / 1000);
    }

    //region helperMethods
    public static String setDurationStr(MediaPlayer mp){
        return createTimeLabel(mp.getCurrentPosition()).concat(" - ").concat(AnalyzeCall.createTimeLabel(mp.getDuration()));
    }

    public static String createTimeLabel(int currentPosition){
        //создание пустой строки
        String timeLabel = "";
        //выделяет кол-во минут и секунд
        int min = currentPosition / 1000 / 60;
        int sec = currentPosition / 1000 % 60;
        //генерируем строку в правильном формате
        timeLabel = min + ":";
        if(sec < 10) timeLabel += "0";
        timeLabel += sec;
        return timeLabel;
    }


}

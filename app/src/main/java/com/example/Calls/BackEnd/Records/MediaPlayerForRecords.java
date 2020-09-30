package com.example.Calls.BackEnd.Records;


//TODO включить код логики media в данный класс
public class MediaPlayerForRecords {

    public MediaPlayerForRecords(){

    }

    public String createTimeLabel(int currentPosition){
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

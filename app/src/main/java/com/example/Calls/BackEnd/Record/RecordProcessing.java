package com.example.Calls.BackEnd.Record;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.Calls.BackEnd.Api.ApiMain;
import com.example.Calls.WaitInEndPlay;

public class RecordProcessing {

    private static int maxDurationProcessing;

    private static int maxDurationTranslation;

    private static int durationProcessing;

    private static int durationTranslating;

    @SuppressLint("StaticFieldLeak")
    private static WaitInEndPlay waitInEndPlay;

    public static void setMaxDurationProcessing(int _durationProcessing){
        maxDurationProcessing = _durationProcessing;
        durationProcessing = 0;
    }

    private static void setMaxDurationTranslating(int _durationTranslating){
        maxDurationTranslation = _durationTranslating;
        durationTranslating = 0;
    }

    public static int getMaxDurationProcessing(){
        return maxDurationProcessing;
    }

    public static int getMaxDurationTranslation(){
        return maxDurationTranslation;
    }

    public static int getDurationProcessing(){
        return durationProcessing;
    }

    public static int getDurationTranslating(){
        return durationTranslating;
    }

    public RecordProcessing(WaitInEndPlay _waitInEndPlay){
        waitInEndPlay = _waitInEndPlay;
    }

    public static void changeDurationProcessingAndStartApi(){
        durationProcessing++;
        waitInEndPlay.setTextViewProcessing();
        if(durationProcessing == maxDurationProcessing){
           setMaxDurationTranslating(maxDurationProcessing);
           try{
               ApiMain apiMain = new ApiMain();
               apiMain.startApiTranslate();
           }
           catch (Exception ex){
               Log.d("RecordProcessingEx", ex.getMessage());
           }
        }
    }

    public static void changeDurationTranslatingAndEndTranslation(){
        durationTranslating++;
        waitInEndPlay.setTextViewTranslation();
        if(durationTranslating == maxDurationTranslation){

        }
    }



}

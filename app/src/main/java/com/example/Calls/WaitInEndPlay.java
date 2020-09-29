package com.example.Calls;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Calls.BackEnd.Api.ApiMain;
import com.example.Calls.BackEnd.Api.ApiSpeech;
import com.example.Calls.BackEnd.Api.FileSpeech;
import com.example.Calls.BackEnd.Contacts;
import com.example.Calls.BackEnd.CutterFiles.Cutter;
import com.example.Calls.BackEnd.CutterFiles.WorkWithFileForCutter;
import com.example.Calls.BackEnd.FilesWork;
import com.example.Calls.BackEnd.Record.RecordProcessing;
import com.example.Calls.BackEnd.Records;
import com.example.Calls.Dialog.DialogMain;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WaitInEndPlay extends AppCompatActivity {

    TextView textViewProgress;

    ProgressBar progressBarTranslate;

    private String nameRecord;

    private WorkWithFileForCutter workWithFileForCutter;

    private ApiMain apiMain;

    public ApiMain getApiMain(){
        return apiMain;
    }

    private RecordProcessing recordProcessing;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wait_in_end_play);

        //textViewProgress = findViewById(R.id.textViewProgress);
        progressBarTranslate = findViewById(R.id.progressBarTranslate);

        textViewProgress = findViewById(R.id.textViewProgress);

        recordProcessing = new RecordProcessing(this);

        try{
            //get cutter obj
            Cutter cutter = Play.getCutter();

            apiMain = new ApiMain();

            //create dirs
            new WorkWithFileForCutter(Records.getNameSelectedRecord()).createDirsForCutter();

            //cut files in intervals
            cutter.startCutFileIntervals(this);

        }
        catch (NullPointerException ex){
            Log.d("NullPointWait", ex.getMessage());
        }
        catch (Exception ex){
            Log.d("ExceptionStartCut", ex.getMessage());
        }

    }

    public void onClickReadyTranslate(View view){
        DialogMain.startAlertDialog(this, 4);
    }

    public void onClickCancelTranslate(View view){

    }

    public void onClickTestStartApi(View view) throws IOException {
        apiMain.startApiTranslate();
    }

    public void onClickTestFullApi(View view) throws Exception{
        apiMain.createResultFileForSelectedRecord();
        Log.d("TestFull", apiMain.readFullFileSelectedRecord());
    }

    public void setTextViewProcessing(){
        textViewProgress.setText(getTextViewProcessingStr());
    }

    private String getTextViewProcessingStr(){
        return "Обработка...  "
                .concat(String.valueOf(RecordProcessing.getDurationProcessing()))
                .concat("/")
                .concat(String.valueOf(RecordProcessing.getMaxDurationProcessing()));
    }

    public void setTextViewTranslation(){
        textViewProgress.setText(getTextViewTranslationStr());
    }

    private String getTextViewTranslationStr(){
        return "Перевод..."
                .concat(String.valueOf(RecordProcessing.getDurationTranslating()))
                .concat("/")
                .concat(String.valueOf(RecordProcessing.getMaxDurationTranslation()));
    }

    public void setProgressBar(int progressBar){
        progressBarTranslate.setProgress(progressBar);
    }



}

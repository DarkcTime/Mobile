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

import com.example.Calls.BackEnd.Api.ApiSpeech;
import com.example.Calls.BackEnd.Api.FileSpeech;
import com.example.Calls.BackEnd.Contacts;
import com.example.Calls.BackEnd.CutterFiles.Cutter;
import com.example.Calls.BackEnd.CutterFiles.WorkWithFileForCutter;
import com.example.Calls.BackEnd.FilesWork;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WaitInEndPlay extends AppCompatActivity {

    TextView textViewProgress;

    ProgressBar progressBarTranslate;

    private String nameRecord;

    private WorkWithFileForCutter workWithFileForCutter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wait_in_end_play);

        textViewProgress = findViewById(R.id.textViewProgress);
        progressBarTranslate = findViewById(R.id.progressBarTranslate);


        try{
            //get nameRecord
            nameRecord = getIntent().getExtras().getString("nameRecord");

            //get cutter obj
            Cutter cutter = Play.getCutter();

            //create dirs
            new WorkWithFileForCutter(nameRecord).createDirsForCutter();

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

    }

    public void onClickCancelTranslate(View view){

    }

    public void onClickTestStartApi(View view){
        FileSpeech.startApiTranslate(nameRecord);
    }


    public void setProgressBar(int progressBar){
        progressBarTranslate.setProgress(progressBar);
    }


    /*
    public void onClickButtonApiStart(View view) {
        Toast.makeText(this, "clickToats", Toast.LENGTH_SHORT).show();
        List<File> listFiles = new ArrayList<File>(FileSpeech.getFiles(FilesWork.getPathForWorkWithApi(nameRecord)));
        Log.d("pathApi", FilesWork.getPathForWorkWithApi(nameRecord));
        Log.d("listFiles", String.valueOf(listFiles.size()));
        try{
            ApiSpeech api = new ApiSpeech();

            int i = 0;
            for(File file : listFiles){
                api.SpeechToText(file.getAbsolutePath(), new Contacts(),i, FilesWork.getPathForWorkWithApi(nameRecord));
                i++;
            }
        }
        catch (Exception ex){
            Log.d("api", ex.getMessage());
        }

    }

     */
}

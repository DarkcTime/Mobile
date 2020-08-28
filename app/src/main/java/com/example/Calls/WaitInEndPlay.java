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
import com.example.Calls.BackEnd.FilesWork;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WaitInEndPlay extends AppCompatActivity {

    TextView textViewProgressWaitEndCutter, textViewProgressWaitEndApi;

    ProgressBar progressBarWaitEndCutter, progressBarWaitEndApi;

    private Cutter cutter;

    private String nameRecord;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wait_in_end_play);

        setVariablesForWorkWithFrontend();

        setVariablesFromPlay();

        try{
            nameRecord = getIntent().getExtras().getString("nameRecord");

            cutter = Play.getCutter();

            cutter.startCutFileIntervals(this);
        }
        catch (NullPointerException ex){
            Log.d("NullPointWait", ex.getMessage());
        }
        catch (Exception ex){
            Log.d("ExceptionStartCut", ex.getMessage());
        }

    }

    private void setVariablesForWorkWithFrontend(){
        textViewProgressWaitEndCutter = (TextView) findViewById(R.id.textViewProgressWaitEndCutter);
        textViewProgressWaitEndApi = (TextView) findViewById(R.id.textViewProgressWaitEndApi);
        progressBarWaitEndCutter = (ProgressBar) findViewById(R.id.progressBarWaitEndCutter);
        progressBarWaitEndApi = (ProgressBar) findViewById(R.id.progressBarWaitEndApi);
    }

    private void setVariablesFromPlay(){

    }

    public void setProgressBarWaitEndCutter(String progress){
        textViewProgressWaitEndApi.setText(progress);
    }


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
}

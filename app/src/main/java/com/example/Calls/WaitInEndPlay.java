package com.example.Calls;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.Calls.BackEnd.CutterFiles.Cutter;

public class WaitInEndPlay extends AppCompatActivity {

    TextView textViewProgressWaitEndCutter, textViewProgressWaitEndApi;

    ProgressBar progressBarWaitEndCutter, progressBarWaitEndApi;

    private Cutter cutter;

    private String nameRecord;

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






}

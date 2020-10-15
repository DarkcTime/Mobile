package com.example.Calls;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.Calls.BackEnd.Api.ApiMain;
import com.example.Calls.BackEnd.CutterFiles.Cutter;
import com.example.Calls.BackEnd.Debug.DebugMessages;
import com.example.Calls.BackEnd.Files.Directories;
import com.example.Calls.BackEnd.Records.RecordProcessing;
import com.example.Calls.Dialog.DialogMain;
import com.example.Calls.Dialog.MyDialogHelp;

import java.io.IOException;

public class WaitInEndPlay extends AppCompatActivity {

    //dialog windows
    final DialogMain dialogMain = new DialogMain(this, DialogMain.Activities.WaitInEndPlay);

    TextView textViewProgress;

    ProgressBar progressBarTranslate;

    private ApiMain apiMain;

    private RecordProcessing recordProcessing;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.wait_in_end_play);

            //textViewProgress = findViewById(R.id.textViewProgress);
            progressBarTranslate = findViewById(R.id.progressBarTranslate);

            textViewProgress = findViewById(R.id.textViewProgress);

            recordProcessing = new RecordProcessing(this);


                Cutter cutter = Play.getCutter();

                //create dirs
                Directories.createDirectories();


                apiMain = new ApiMain();


                //cut files in intervals
                cutter.startCutFileIntervals(this);

        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "onCreateWaitEndPlay");
        }

    }

    public void dialogResultAdd(){
        try{
            apiMain.addTextInFullFileSelectedContact();
            startActivityAboutContact();
        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "dialogResultAdd");
        }
    }

    public void dialogResultCancel(){
        try{
            startActivityAboutContact();
        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "dialogResultCancel");
        }
    }

    private void startActivityAboutContact() throws Exception{
        Intent WaitEndPlay = new Intent(WaitInEndPlay.this, AboutContact.class);
        startActivity(WaitEndPlay);
    }


    public void finishProcessingAndTranslating(){
        try{
            apiMain.createResultFileForSelectedRecord();
            new DialogMain().startAlertDialog(this, MyDialogHelp.Windows.API);
        }
        catch (IOException ex) {
            DebugMessages.ErrorMessage(ex,this,"finishProcessing");
        }
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

}

package com.example.Calls;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Calls.BackEnd.SavedSettings;

import org.w3c.dom.Text;

public class SettingsForPlay extends AppCompatActivity {

    private static int secRewind;

    private static int secPause;

    private int secRewindPrivate;
    private int secPausePrivate;

    private TextView textViewRewindSec;
    private TextView textViewPauseSec;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_for_play);

        textViewRewindSec = (TextView) findViewById(R.id.textViewRewindSec);
        textViewPauseSec = (TextView) findViewById(R.id.textViewPauseSec);

        secRewindPrivate = secRewind;
        secPausePrivate = secPause;

        setTextViewRewindSec(secRewindPrivate);
        setTextViewPauseSec(secPausePrivate);


    }

    //region buttons clicks
    public void onClickMinusRewind(View view){
        setTextViewRewindSec(--secRewindPrivate);
    }
    public void onClickPlusRewind(View view){
        setTextViewRewindSec(++secRewindPrivate);
    }
    public void onClickMinusPause(View view){
        setTextViewPauseSec(--secPausePrivate);
    }
    public void onClickPlusPause(View view){
        setTextViewPauseSec(++secPausePrivate);
    }


    public void onClickButtonSaveSettingsForPlay(View view){
        SavedSettings.setSettingsTime(secRewindPrivate, secPausePrivate);
        Intent play = new Intent(SettingsForPlay.this, Play.class);
        startActivity(play);
    }

    public void onClickCancelSaveSettingsForPlay(View view){
        this.finish();
    }


    //endregion

    //region helper method
    private void setTextViewRewindSec(int sec){

        String str = String.valueOf(sec) + " sec";
        textViewRewindSec.setText(str);
    }

    private void setTextViewPauseSec(int sec){
        String str = String.valueOf(sec) + " sec";
        textViewPauseSec.setText(str);
    }

    public static void setSecRewind(int sec){
        secRewind = sec;
    }

    public static void setSecPause(int sec){
        secPause = sec;
    }

    public static int getSecRewind(){return secRewind;}

    public static int getSecPause(){return secPause;}

    //endregion
}

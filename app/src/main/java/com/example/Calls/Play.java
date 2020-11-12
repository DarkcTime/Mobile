package com.example.Calls;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Calls.BackEnd.Analysis.AnalyzeCall;
import com.example.Calls.BackEnd.CutterFiles.Cutter;
import com.example.Calls.BackEnd.Media.MediaPlayerClass;
import com.example.Calls.BackEnd.Services.HistoryTranslateService;
import com.example.Calls.BackEnd.Services.RecordsService;
import com.example.Calls.BackEnd.Settings.SavedSettings;
import com.example.Calls.Dialog.DialogMain;
import com.example.Calls.Dialog.HelpDialog;
import com.example.Calls.Model.Repositories.RecordRepository;

import org.tritonus.share.sampled.file.TAudioFileFormat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;

import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;

//TODO декомпозировать код после определения интерфейса и планов на переработку

public class Play extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    //dialog windows
    final DialogMain dialogMain = new DialogMain(this, DialogMain.Activities.Play);

    private LinearLayout startLayoutPage;

    private LinearLayout playLinerLayout;
    private TextView textViewSelectedRecPlay, textViewStartPositionPlay;
    private SeekBar seekBarPositionPlay;
    private MediaPlayer mp;

    private Handler handler;
    private Runnable runnable;

    private Button buttonStartPlay,buttonMyPlay, buttonExit, buttonCompanion;

    private static boolean checkPlayCycle = false;

    private boolean endRecord = false;

    private boolean hearing;

    private static int checkPlaying;

    //значение паузы и перемотки
    private int secRewind, secPause;
    private SharedPreferences mSettings;

    private static Cutter cutter;

    public static Cutter getCutter(){
        return cutter;
    }

    private Button buttonBackRewind, buttonFordRewind;

    //endregion

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.play);

            startLayoutPage = (LinearLayout) (findViewById(R.id.startLayoutPage));

            playLinerLayout = (LinearLayout) (findViewById(R.id.playLinerLayout));



            /*

            setVariablesForPlay();

            setSettingsForRewards();

            createMediaPlayer();


            buttonExit.setVisibility(View.GONE);

            textViewSelectedRecPlay.setText("Выбранная запись: ".concat(Records.getNameSelectedRecord()));

            handler = new Handler();

            hearing = false;

            cutter = new Cutter();


            //endregion

            /*
            if(!SavedSettings.isExpert()){
                dialogMain.showHelpDialog(HelpDialog.Helps.PlayHelp);
            }




            //region button I and Other

            buttonMyPlay.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    try{
                        if(!mp.isPlaying()) return false;

                        if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE)
                        {
                            updateGame();

                            setIntervalStop();
                        }
                    }
                    catch (Exception ex){
                        dialogMain.showErrorDialogAndTheOutputLogs(ex, "buttonMyPlay");
                    }
                    return false;
                }
            });

            buttonCompanion.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    try {
                        if(!mp.isPlaying()) return false;

                        if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE)
                        {
                            updateGame();
                            setIntervalAdd();
                        }
                    }
                    catch (Exception ex){
                        dialogMain.showErrorDialogAndTheOutputLogs(ex, "buttonCompanion");
                    }

                    return false;
                }
            });


            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    endRecord = true;
                }
            });
            //endregion



             */

        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "onCreatePlay");
        }

    }

    public void onClickStartPlay(View view){
        try{
            //TODO test code
            cutter = new Cutter();
            cutter.AddInterval(0);
            cutter.StopInterval(2);
            cutter.AddInterval(4);
            cutter.StopInterval(6);

            Intent intent = new Intent(Play.this, WaitInEndPlay.class);
            startActivity(intent);

            /* region LastCode
            checkPlayCycle = true;
            mp.start();

            playCycle();

            buttonStartPlay.setVisibility(View.GONE);
            */
        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "onClickStartPlay");
        }

    }



    //region loadPage
    private void setVariablesForPlay(){
        try{
            //region first create variables
            textViewSelectedRecPlay = (TextView) findViewById(R.id.textViewSelectedRecPlay);
            textViewStartPositionPlay = (TextView) findViewById(R.id.textViewStartPositionPlay);

            seekBarPositionPlay = (SeekBar) findViewById(R.id.seekBarPositionPlay);

            buttonStartPlay = (Button) findViewById(R.id.buttonStartPlay);
            buttonMyPlay = (Button) findViewById(R.id.buttonMyPlay);
            buttonExit = (Button) findViewById(R.id.buttonExit);
            buttonCompanion = (Button) findViewById(R.id.buttonCompanion);

            //buttonBackRewind = (Button) findViewById(R.id.buttonBackRewind);
            buttonFordRewind = (Button) findViewById(R.id.buttonFordRewind);
        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "Play/setVariablesForPlay");
        }
    }

    private void setSettingsForRewards(){
        try{
            //region set Settings from file
            mSettings = getSharedPreferences(SavedSettings.APP_PREFERENCES, Context.MODE_PRIVATE);

            //default values
            SettingsForPlay.setSecRewind(mSettings.getInt("rewind_time", 3));
            SettingsForPlay.setSecPause(mSettings.getInt("pause_time", 1));

            secRewind = SettingsForPlay.getSecRewind();
            secPause = SettingsForPlay.getSecPause();

            checkPlaying = secPause + 1;

            buttonFordRewind.setText("вперёд ".concat(String.valueOf(secRewind)).concat(" сек"));
            //buttonBackRewind.setText("назад ".concat(String.valueOf(secRewind).concat(" сек")));

            //endregion
        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "Play/SettingsForRewards");
        }

    }

    //TODO move the entity to a separate class
    private void createMediaPlayer() {
        try {
            mp = new MediaPlayer();
            try{
                mp.setDataSource(RecordRepository.getSelectedRecord().Path);
                mp.prepare();
            }
            catch (IOException ioEx){
                Toast.makeText(this, "Ошибка при получении записи: " + ioEx.toString(), Toast.LENGTH_SHORT).show();
            }

            mp.setLooping(false);

            textViewStartPositionPlay.setText(MediaPlayerClass.setDurationStr(mp));

            seekBarPositionPlay.setMax(mp.getDuration());

            seekBarPositionPlay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(fromUser){
                        mp.seekTo(progress);
                        textViewStartPositionPlay.setText(MediaPlayerClass.setDurationStr(mp));
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        } catch (Exception ex) {
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "Play/createMediaPlayer");
        }
    }

    //endregion

    //region Rewards

    public void onClickButtonForwardSecond(View view){
        try{
            if(!mp.isPlaying()) return;
            RewardMedia(true);
        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "onClickButtonForwardSecond");
        }
    }

    public void onClickButtonBackSecond(View view){
        try{
            RewardMedia(false);
        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "onClickButtonBackSecond");
        }
    }

    private void RewardMedia(boolean isForward){
        try{
            if(isForward) ForwardReward();
            else BackReward();
            textViewStartPositionPlay.setText(MediaPlayerClass.setDurationStr(mp));
            updateGame();
        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "Play/RewardMedia");
        }

    }

    private void ForwardReward() throws Exception{
        mp.seekTo(mp.getCurrentPosition() + secRewind*1000);
    }

    private void BackReward() throws Exception{
        mp.seekTo(mp.getCurrentPosition() - secRewind*1000);
    }

    //endregion

    //region PopupMenu
    public void onClickButtonSettingsPlay(View view){
        try{
            com.example.Calls.Dialog.PopupMenu.showPopupMenu(this, view,R.menu.menu_settings);
        }
        catch (Exception ex){
           dialogMain.showErrorDialogAndTheOutputLogs(ex, "onClickButtonSettingsPlay");
        }
    }

    //actions for click in menu settings
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        try{
            switch (item.getItemId()){
                case R.id.settings:
                    Intent settingsForPlay = new Intent(Play.this, SettingsForPlay.class);
                    startActivity(settingsForPlay);
                    break;
                case R.id.help:
                    dialogMain.showHelpDialog(HelpDialog.Helps.PlayHelp);
                    break;
                case  R.id.reset:
                    Intent reset = getIntent();
                    finish();
                    startActivity(reset);
                    break;
                case R.id.exit:
                    //Intent aboutContacts = new Intent(Play.this, AboutContact.class);
                    //startActivity(aboutContacts);
                    break;
            }
        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "Play/onMenuItemClick");
        }

        return true;
    }
    //endregion

    //region Start and Exit play
    public void onClickButtonStopGame(View view){
        try{
            StopGame();
        }
        catch (Exception ex){
           dialogMain.showErrorDialogAndTheOutputLogs(ex, "onClickButtonStopGame");
        }
    }

    public void startCutterAndTranslateRecord() {
        try{
            Intent WaitEndPlay = new Intent(Play.this, WaitInEndPlay.class);
            startActivity(WaitEndPlay);
        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "startCutterAndTranslateRecord");
        }
    }


    public void startAboutContact(){
        try{
            //Intent AboutContact = new Intent(Play.this, com.example.Calls.AboutContact.class);
            //startActivity(AboutContact);
        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "startAboutContact");
        }
    }



    public void StopGame(){
        startCutterAndTranslateRecord();
    }

    //endregion

    //region Interval

    private void setIntervalStop() throws Exception{
        //stop interval media
        if(hearing){
            cutter.StopInterval(MediaPlayerClass.getCurrentPositionSec(mp));
            hearing = false;
        }
    }

    private void setIntervalAdd() throws Exception{
        if(!hearing){
            cutter.AddInterval(MediaPlayerClass.getCurrentPositionSec(mp));
            hearing = true;
        }
    }


    private void stopPlaying(){
        try{
            checkPlaying--;
            if(checkPlaying == 0)
            {
                modeWait(true);
            }
        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "stopPlaying");
        }

    }

    private void updateGame() throws Exception{

        if(endRecord) {
           dialogMain.showMediaEnd();
           return;
        }


        if(checkPlaying <= 0){
            modeWait(false);
        }
        checkPlaying = secPause + 1;
    }

    private void modeWait(boolean isSet) throws Exception{
        try{
            if(isSet){
                checkPlayCycle = false;
                mp.pause();
                buttonExit.setVisibility(View.VISIBLE);
            }
            else{
                checkPlayCycle = true;
                mp.start();
                buttonExit.setVisibility(View.GONE);
            }

            playCycle();
        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "modeWait");
        }
    }

    private void playCycle(){
        try{
            if(checkPlayCycle){
                seekBarPositionPlay.setProgress(mp.getCurrentPosition());
                textViewStartPositionPlay.setText(MediaPlayerClass.setDurationStr(mp));

                if(mp.isPlaying()){
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            stopPlaying();
                            playCycle();
                        }
                    };
                }

                handler.postDelayed(runnable, 1000);
            }
        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "playCycle");
        }
    }


    //endregion

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mp.release();
        //handler.removeCallbacks(runnable);
    }


}

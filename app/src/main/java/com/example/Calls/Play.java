package com.example.Calls;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Calls.BackEnd.Api.AnalyzeCall;
import com.example.Calls.BackEnd.Api.ApiSpeech;
import com.example.Calls.BackEnd.Api.FileSpeech;
import com.example.Calls.BackEnd.Api.SelectMethodSaveText;
import com.example.Calls.BackEnd.CheapSound.CheapMP3;
import com.example.Calls.BackEnd.CheapSound.Cut;
import com.example.Calls.BackEnd.CheapSound.FriendInterval;
import com.example.Calls.BackEnd.Contacts;
import com.example.Calls.BackEnd.Records;
import com.example.Calls.BackEnd.SavedSettings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Play extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {


    public static String nameRecordStatic;

    //text get from Api
    private String textApi;

    private TextView textViewSelectedRecPlay, textViewCountMinuteMake, textViewLevelUser, textViewStartPositionPlay, textViewLinerForMedia;

    private SeekBar seekBarPositionPlay;

    private Button buttonStartPlay, buttonCut;

    private String pathRecord, nameRecord;

    private MediaPlayer mp;

    private Contacts contacts;

    private Handler handler;

    private Runnable runnable;

    private Button buttonMyPlay, buttonExit, buttonCompanion;

    private static boolean checkPlayCycle = false;

    private static int checkPlaying;

    private String linerMedia, spaces = "";

    //значение паузы и перемотки
    private int secRewind, secPause;

    private SharedPreferences mSettings;

    private Button buttonBackRewind, buttonFordRewind;

    //аpi
    private ApiSpeech api;

    //нарезка записи
    private Cut cutMedia;
    private boolean hearing;

    private static int levelGame = 5000;

    ProgressTextView progressTextView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play);

        //region first create variables
        textViewCountMinuteMake = (TextView) findViewById(R.id.textViewCountMinuteMake);
        textViewSelectedRecPlay = (TextView) findViewById(R.id.textViewSelectedRecPlay);
        textViewLevelUser = (TextView) findViewById(R.id.textViewLevelUser);
        textViewStartPositionPlay = (TextView) findViewById(R.id.textViewStartPositionPlay);

        seekBarPositionPlay = (SeekBar) findViewById(R.id.seekBarPositionPlay);

        buttonStartPlay = (Button) findViewById(R.id.buttonStartPlay);
        buttonMyPlay = (Button) findViewById(R.id.buttonMyPlay);
        buttonExit = (Button) findViewById(R.id.buttonExit);
        buttonCompanion = (Button) findViewById(R.id.buttonCompanion);

        buttonBackRewind = (Button) findViewById(R.id.buttonBackRewind);
        buttonFordRewind = (Button) findViewById(R.id.buttonFordRewind);
        //buttonCut = (Button) findViewById(R.id.buttonCut);

        buttonExit.setVisibility(View.GONE);

        //get Name Selected Record
        nameRecord = nameRecordStatic;

        pathRecord = Records.pathForFindRecords + nameRecord;

        //endregion

        linerMedia = "";

        progressTextView = (ProgressTextView) findViewById(R.id.progressTextView);

        progressTextView.setValue(5000);

        textViewSelectedRecPlay.setText("Выбранная запись: ".concat(nameRecord));

        textViewCountMinuteMake.setText("Количество минут: 02:00");

        textViewLevelUser.setText("Уровень пользователя: не определен");

        handler = new Handler();

        contacts = new Contacts();

        cutMedia = new Cut();

        //create class ApiSpeech
        try{
            api = new ApiSpeech();
        }
        catch (Exception ex){
            Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
        }

        hearing = false;

        if(!SavedSettings.isExpert()){
            startAlertDialog(1);
        }

        //region create MediaPlayer
        mp = new MediaPlayer();
        try{
            mp.setDataSource(pathRecord);
            mp.prepare();
        }
        catch (IOException ioEx){
            Toast.makeText(this, "Ошибка при получении записи: " + ioEx.toString(), Toast.LENGTH_SHORT).show();
        }

        mp.setLooping(true);

        textViewStartPositionPlay.setText(setDurationStr());

        seekBarPositionPlay.setMax(mp.getDuration());

        seekBarPositionPlay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mp.seekTo(progress);
                    textViewStartPositionPlay.setText(setDurationStr());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //endregion

        //region set Settings from file
        mSettings = getSharedPreferences(SavedSettings.APP_PREFERENCES, Context.MODE_PRIVATE);

        SettingsForPlay.setSecRewind(mSettings.getInt("rewind_time", 5));
        SettingsForPlay.setSecPause(mSettings.getInt("pause_time", 2));

        secRewind = SettingsForPlay.getSecRewind();
        secPause = SettingsForPlay.getSecPause();

        checkPlaying = secPause + 1;

        buttonFordRewind.setText("вперёд ".concat(String.valueOf(secRewind)).concat(" сек"));
        buttonBackRewind.setText("назад ".concat(String.valueOf(secRewind).concat(" сек")));

        //endregion

        //region button I and Other

        //TODO test api spaeech
        //логика при нажатии кнопки Я
        buttonMyPlay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    try{
                        api.SpeechToText(pathRecord, contacts);
                    }
                    catch (Exception ex){
                        Log.d("StartSpeechToText", ex.toString());
                    }
                }

                //region
                //TODO refactor code
                /*
                if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE)
                {
                    updateGame();
                    levelGame--;
                    progressTextView.setValue(levelGame);

                    //add interval media
                    if(!hearing){
                        cutMedia.AddInterval(AnalyzeCall.getCurrentPositionSec(mp));
                        hearing = true;
                    }
                }
                return false;

                 */
                //endregion
                return false;

            }
        });
        //TODO test api spaeech
        //логика при нажатии кнопки Собеседник
        buttonCompanion.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    try{
                        textApi = FileSpeech.ReadFileSpeech(contacts,SelectMethodSaveText.oneMessage);
                    }
                    catch (Exception ex){
                        Log.d("StartSpeechToText", ex.toString());
                    }
                }
                //region
                //TODO refactor code
                /*
                if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE)
                {
                    updateGame();
                    levelGame++;
                    progressTextView.setValue(levelGame);
                    //stop interval media
                    if(hearing){
                        cutMedia.StopInterval(AnalyzeCall.getCurrentPositionSec(mp));
                        hearing = false;
                    }
                }
                return false;

                 */
                //endregion
                return false;
            }
        });

        //endregion

    }

    //region buttons Click

    //TODO test
    public void onClickButtonForwardSecond(View view){

        Cut cut = new Cut();
        cut.AddInterval(6);
        cut.StopInterval(12);

        List<FriendInterval> friendIntervalList = new ArrayList<>();
        friendIntervalList = cut.getIntervalList();
        Log.d("interval", String.valueOf(friendIntervalList.get(0).getStart()));
        try{
            cut.Cutter(nameRecord);
        }
        catch (Exception ex){
            Log.d("cutter", ex.toString());
        }



        /*
        List<File> listFiles = new ArrayList<File>();
        listFiles.addAll(Records.getFiles(cut.getPathCut(nameRecord)));
        Log.d("count file", String.valueOf(listFiles.size()));

         */


        /*
        mp.seekTo(mp.getCurrentPosition() + secRewind*1000);
        textViewStartPositionPlay.setText(setDurationStr());
        updateGame();

         */
    }

    public void onClickButtonSettingsPlay(View view){
        /*
        try{
            PopupMenu popup = new PopupMenu(this, view);
            popup.setOnMenuItemClickListener(this);
            popup.inflate(R.menu.menu_settings);
            popup.show();
        }
        catch (Exception ex){
            Toast.makeText(this, "Ошибка при открытии меню" + ex.toString(), Toast.LENGTH_SHORT).show();
        }

         */
    }

    public void onClickButtonStopGame(View view){
        try{
            //cutMedia.Cutter(nameRecord, contacts);
            //тестовый перевод
            String pathRec = ApiSpeech.pathForNewFile + nameRecord.replace(".mp3", "") + "1.mp3";
            Toast.makeText(this, pathRec, Toast.LENGTH_SHORT).show();
            api.SpeechToText(pathRec, contacts);
        }
        catch (Exception ex){
            Toast.makeText(this, "Ошибка при обрезке записи" + ex.toString(), Toast.LENGTH_SHORT).show();
        }

        /*
        Intent aboutContact = new Intent(Play.this, AboutContact.class);
        startActivity(aboutContact);

         */
    }

    //TODO зачем?
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                Intent settingsForPlay = new Intent(Play.this, SettingsForPlay.class);
                startActivity(settingsForPlay);
                return true;
            case R.id.help:
                startAlertDialog(2);
                return true;
            case  R.id.reset:
                Intent reset = getIntent();
                finish();
                startActivity(reset);
                return true;
            case R.id.exit:
                Intent aboutContacts = new Intent(Play.this, AboutContact.class);
                startActivity(aboutContacts);
                return true;
        }
        return false;
    }

    public void onClickButtonBackSecond(View view){

        List<FriendInterval> friendInterval = new ArrayList<FriendInterval>();
        friendInterval = cutMedia.getIntervalList();
        int start = friendInterval.get(0).getStart();
        Toast.makeText(this, start, Toast.LENGTH_SHORT).show();
        /*
        mp.seekTo(mp.getCurrentPosition() - secRewind*1000);
        textViewStartPositionPlay.setText(setDurationStr());
        updateGame();

         */
    }

    private void startAlertDialog(int numButton){
        FragmentManager manager = getSupportFragmentManager();
        MyDialogHelp.getButton = numButton;
        MyDialogHelp myDialogHelp = new MyDialogHelp();
        myDialogHelp.show(manager, "myDialog");
    }


    public void onClickStartPlay(View view){
        try{

            checkPlayCycle = true;
            mp.start();

            playCycle();

            buttonStartPlay.setVisibility(View.GONE);

        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }

    //endregion

    //region helperMethods
    private String setDurationStr(){
        return AnalyzeCall.createTimeLabel(mp.getCurrentPosition()).concat(" - ").concat(AnalyzeCall.createTimeLabel(mp.getDuration()));
    }



    private void setLinerMedia(int time){
        linerMedia += spaces + "|";
        spaces = "";
        textViewLinerForMedia.setText(linerMedia);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.release();
        handler.removeCallbacks(runnable);
    }

    private void stopPlaying(){
        checkPlaying--;
        if(checkPlaying == 0)
        {
            modeWait(true);
        }
    }

    private void updateGame(){
        if(checkPlaying <= 0){
            modeWait(false);
        }
        checkPlaying = secPause + 1;
    }

    private void modeWait(boolean isSet){
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

    private void playCycle(){
        if(checkPlayCycle){
            seekBarPositionPlay.setProgress(mp.getCurrentPosition());
            textViewStartPositionPlay.setText(setDurationStr());

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

    //endregion


}

package com.example.Calls;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Calls.BackEnd.Api.AnalyzeCall;
import com.example.Calls.BackEnd.Api.ApiSpeech;
import com.example.Calls.BackEnd.Api.FileSpeech;
import com.example.Calls.BackEnd.Api.SelectMethodSaveText;
import com.example.Calls.BackEnd.Contacts;
import com.example.Calls.BackEnd.CountData;
import com.example.Calls.BackEnd.FilesWork;
import com.example.Calls.BackEnd.Listener.AListener;
import com.example.Calls.BackEnd.MediaPlayerForRecords;
import com.example.Calls.BackEnd.Records;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class EditRec extends AppCompatActivity {


    private EditText editTextEditRec;


    private ProgressBar progressBarTranslatedText;


    private MediaPlayerForRecords mediaPlayerForRecords;

    private int totalTime, progressTranslated, totalTimeSecond;

    //TODO тест
    boolean check = true;

    private ApiSpeech api;

    private static boolean checkPlayCycle = false;

    //содержат переведенный и отредактированный текст
    public String translatedText,finalText;


    private TextView textViewSelectedRecEditRec,textViewCountWordsEditRec, textViewTimeTranslate, textViewStartPositionEditRec;

    private Button buttonPlayEditRec;

    private String nameRecord, nameUser, pathRecord;

    private MediaPlayer mp;

    private SeekBar seekBarPositionEditRec;

    private Handler handler;

    private Runnable runnable;

    private RelativeLayout relativeLayoutWait;

    private Button buttonTranslatedText;

    private Contacts contacts;

    private Records records;

    private FilesWork filesWork;

    //  public EditRec() throws IOException {
    //}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_rec);

        //region id
        textViewSelectedRecEditRec = (TextView)findViewById(R.id.textViewSelectedRecEditRec);
        textViewCountWordsEditRec = (TextView)findViewById(R.id.textViewCountWordsEditRec);
        textViewTimeTranslate = (TextView) findViewById(R.id.textViewTimeTranslate);
        textViewStartPositionEditRec = (TextView) findViewById(R.id.textViewStartPositionEditRec);

        relativeLayoutWait = (RelativeLayout) findViewById(R.id.relativeLayoutWait);

        buttonPlayEditRec = (Button)findViewById(R.id.buttonPlayEditRec);

        seekBarPositionEditRec = (SeekBar) findViewById(R.id.seekBarPositionEditRec);

        editTextEditRec = (EditText) findViewById(R.id.editTextEditRec);


        filesWork = new FilesWork();

        records = new Records();

        //endregion

        //region Upload page

        //получаем имя записи из главного окна
        nameRecord = getIntent().getStringExtra("nameRecord");
        pathRecord = Records.currentPathForRecordsXiomi + nameRecord;

        textViewSelectedRecEditRec.setText("Выбранная запись: " + nameRecord);

        //Contacts.informationAboutUser = "Имя: " + records.getNameSelectedRecord(nameRecord) + "\nТелефон: " + records.getPhoneNumberSelectedRecord(nameRecord);

        //nameUser = records.getNameSelectedRecord(nameRecord);
        //setTitle("Имя: " + nameUser);

        handler = new Handler();

        contacts = new Contacts();

        //create api class
        try {
            api = new ApiSpeech();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        try{
            editTextEditRec.setText(Records.getDateFile(contacts,  nameRecord));
        }
        catch (Exception ex){
            Toast.makeText(this, "Ошибка чтения файла" + ex.toString(), Toast.LENGTH_SHORT).show();
        }

        textViewCountWordsEditRec.setText(AnalyzeCall.setNumberWords(AnalyzeCall.getCountWords(editTextEditRec.getText().toString().trim())));

        //endregion

        //region MediaPlayer

        mp = new MediaPlayer();
        try{
            mp.setDataSource(Records.currentPathForRecordsXiomi + nameRecord);
            mp.prepare();
        }
        catch (IOException ioEx){
            Toast.makeText(this, "Ошибка при получении записи: " + ioEx.toString(), Toast.LENGTH_SHORT).show();
        }

        textViewStartPositionEditRec.setText(setDurationStr());

        seekBarPositionEditRec.setMax(mp.getDuration());

        seekBarPositionEditRec.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mp.seekTo(progress);
                    textViewStartPositionEditRec.setText(setDurationStr());
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

    }

    //region media player
    public void onClickPlayEditRec(View view){
        try{
            if(!mp.isPlaying()){
                //Stopping
                checkPlayCycle = true;
                mp.start();
                buttonPlayEditRec.setBackgroundResource(R.drawable.ic_pause_circle_filled_black_24dp);

            }
            else{
                //Playing
                checkPlayCycle = false;
                mp.pause();
                buttonPlayEditRec.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
            }
            playCycle();

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.release();
        handler.removeCallbacks(runnable);
    }

    private void playCycle(){
                if(checkPlayCycle){
                    seekBarPositionEditRec.setProgress(mp.getCurrentPosition());
                    textViewStartPositionEditRec.setText(setDurationStr());

                    if(mp.isPlaying()){

                        runnable = new Runnable() {
                            @Override
                            public void run() {
                                playCycle();
                            }
                        };
                    }

                    handler.postDelayed(runnable, 1000);
                }

    }





    //endregion


    //region helper buttons

    //copy text in clipboard
    public void onClickButtonCopyEditRec(View view){
        try{
            if(!check){
                Toast.makeText(this, "Выполняется перевод в текст", Toast.LENGTH_SHORT).show();
                return;
            }
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clipData;
            clipData = ClipData.newPlainText("text", editTextEditRec.getText());
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(this, "текст скопирован в буфер обмена" , Toast.LENGTH_LONG).show();


        }
        catch (Exception ex){
            Toast.makeText(this, "ошибка копирования текста  " + ex.toString() , Toast.LENGTH_LONG).show();
        }
    }


    //add text in file
    public void onClickButtonSaveEditRec(View view){
        try{
            finalText = editTextEditRec.getText().toString();
            Records.saveTranslatedRecord(finalText, contacts, nameRecord);
            Toast.makeText(this, "Данные успешно сохранены", Toast.LENGTH_SHORT).show();

        }
        catch (Exception ex){
            Toast.makeText(this, "Ошибка при сохранении в файл", Toast.LENGTH_SHORT).show();
        }
    }
    //endregion


    //region translate audio in text

    //запускает перевод записи в текст
    public void onClickButtonTranslatedText(View view) throws IOException,Exception {

       try{
           if(!check){
               Toast.makeText(this, "Выполняется перевод в текст", Toast.LENGTH_SHORT).show();
               return;
           }

           AsyncApi asyncApi = new AsyncApi();

           setModeWait(true);

           api.SpeechToText(pathRecord, contacts);

           asyncApi.execute();

       }
       catch (Exception ex){
           Toast.makeText(this, "Ошибка при запуске перевода записи" + ex.toString(), Toast.LENGTH_SHORT).show();
       }

    }

    //region helper method


    private String setDurationStr(){
        return AnalyzeCall.createTimeLabel(mp.getCurrentPosition()).concat(" - ").concat(AnalyzeCall.createTimeLabel(mp.getDuration()));
    }

    private void setModeWait(boolean setWait){
        if(setWait){
            editTextEditRec.setVisibility(View.GONE);

        }
        else{
            editTextEditRec.setVisibility(View.VISIBLE);

        }
    }



    //endregion



    // translate audio in text and change editText
    class AsyncApi extends AsyncTask<Void,Integer,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            textViewTimeTranslate.setText(AnalyzeCall.setTimeTranslated(AnalyzeCall.timeNeed(AnalyzeCall.countSecond(mp.getDuration()))));
        }

        @Override
        protected Void doInBackground(Void... voids) {

            //TODO тестируем только с использованием api
            //избавляемся от лишней ассинхронности
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                setModeWait(false);
                translatedText = FileSpeech.ReadFileSpeech(contacts);
                editTextEditRec.setText(translatedText);

                if(!Records.isRecordExist(nameRecord))
                    Records.writeRecord(nameRecord);

                if(translatedText.equals(""))
                    return;

                //textViewCountWordsEditRec.setText(CountData.getCountWordsStr(CountData.getCountWords(translatedText)));
            } catch (Exception ex) {
                ex.printStackTrace();
            }


        }
    }


    //endregion


    //region menu
    //create menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_main_menu, menu);
        return true;
    }

    //change menu item
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.item_help:
                Intent intent1 = new Intent(EditRec.this , Help.class);
                startActivity(intent1);
                return  true;
            case R.id.item_settings:
                Intent intent2 = new Intent(EditRec.this , Settings.class);
                startActivity(intent2);
                return true;
            default:
                return false;
        }

    }

    //button back
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        this.finish();
    }

    //endregion

}

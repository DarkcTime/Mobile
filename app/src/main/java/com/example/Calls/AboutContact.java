package com.example.Calls;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.Calls.BackEnd.Api.ApiMain;
import com.example.Calls.BackEnd.Contacts.Contacts;
import com.example.Calls.BackEnd.Debug.DebugMessages;
import com.example.Calls.BackEnd.Files.FileSystem;
import com.example.Calls.BackEnd.Records.Records;
import com.example.Calls.Dialog.DialogMain;
import com.example.Calls.Dialog.FilesDialog;
import com.example.Calls.Dialog.MyDialogHelp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AboutContact extends AppCompatActivity {

    //TODO продумать и проработать статистические данные
    private TextView textViewNameContact, textViewPhoneContact, textViewCountReady, textViewNeedMin, textViewNeedWords;

    private EditText editTextEditRec;

    //dialog windows
    final DialogMain dialogMain = new DialogMain(this, DialogMain.Activities.AboutContact);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.about_contact);

            setParametersForView();

            setEditTextRecToFileContact();
        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "onCreateAboutContact");
        }
    }

    //объявление переменных для AboutContact
    private void setParametersForView(){
        try{
            textViewNameContact = (TextView) findViewById(R.id.textViewNameContact);
            textViewPhoneContact = (TextView) findViewById(R.id.textViewPhoneContact);
            textViewCountReady = (TextView) findViewById(R.id.textViewCountReady);
            textViewNeedMin = (TextView) findViewById(R.id.textViewNeedMin);
            textViewNeedWords = (TextView) findViewById(R.id.textViewNeedWords);

            //region setTextView
            textViewNameContact.setText("Имя: " + Contacts.getNameCurrentContact());
            textViewPhoneContact.setText("Телефон: " + Contacts.getPhoneNumberCurrentContact());
            textViewCountReady.setText("Процент готовкности: 20%");
            textViewNeedMin.setText("Предположительно осталось: 10 минут разговора");
            textViewNeedWords.setText("Количество полученных слов: 201 из 1000");

            //endregion

            editTextEditRec = (EditText) findViewById(R.id.editTextEditRec);
        }
        catch (Exception ex){
           dialogMain.showErrorDialogAndTheOutputLogs(ex, "setParametersForView");
        }
    }

    //загрузка текста из общего файла пользователя в окно для изменения
    private void setEditTextRecToFileContact(){
        try{
            editTextEditRec.setText(ApiMain.readFullFileSelectedContact());
        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "AboutContact/setEditTextRecToFileContact");
        }

    }


    //открывает диалоговое окно со списком записей для контакта
    public void onClickButtonSelectRecord(View view){
        try{
            dialogMain.showFilesDialog();
        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "onClickButtonSelectRecord");
        }
    }

    public void onClickButtonCancel(View view){
        try{
            Intent main = new Intent(AboutContact.this, MainActivity.class);
            startActivity(main);
        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "onClickButtonCancel");
        }
    }

    //TODO дописать справку для окна выбора контакта
    public void onClickButtonHelpAboutContact(View view){

    }

    public void startGame(String nameRecord){
        try{
            Intent play = new Intent(AboutContact.this, Play.class);
            //set name Selected record for Application
            Records.setNameSelectedRecord(nameRecord);
            startActivity(play);
        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "AboutContact/startGame");
        }
    }



}

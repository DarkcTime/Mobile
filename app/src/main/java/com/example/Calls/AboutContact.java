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
import com.example.Calls.BackEnd.Contacts;
import com.example.Calls.BackEnd.Records;
import com.example.Calls.Dialog.FilesDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AboutContact extends AppCompatActivity {


    private Contacts contacts;

    private TextView textViewNameContact, textViewPhoneContact, textViewCountReady, textViewNeedMin, textViewNeedWords;

    private EditText editTextEditRec;

    private List<File> listFiles = new ArrayList<File>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_contact);

        try{
            setParametersForView();

            setEditTextRecToFileContact();
        }
        catch (IOException ex){
            Log.d("AboutContact", ex.getMessage());
        }


    }

    private void setParametersForView(){
        textViewNameContact = (TextView) findViewById(R.id.textViewNameContact);
        textViewPhoneContact = (TextView) findViewById(R.id.textViewPhoneContact);
        textViewCountReady = (TextView) findViewById(R.id.textViewCountReady);
        textViewNeedMin = (TextView) findViewById(R.id.textViewNeedMin);
        textViewNeedWords = (TextView) findViewById(R.id.textViewNeedWords);

        contacts = new Contacts();

        //region setTextView
        textViewNameContact.setText("Имя: " + contacts.getNameCurrentContact());
        textViewPhoneContact.setText("Телефон: " + contacts.getPhoneNumberCurrentContact());
        textViewCountReady.setText("Процент готовкности: 20%");
        textViewNeedMin.setText("Предположительно осталось: 10 минут разговора");
        textViewNeedWords.setText("Количество полученных слов: 201 из 1000");

        //endregion

        editTextEditRec = (EditText) findViewById(R.id.editTextEditRec);
    }

    private void setEditTextRecToFileContact() throws IOException{
        editTextEditRec.setText(ApiMain.readFullFileSelectedContact());
    }




    private void startFilesDialog(List<File> listFiles){
        FragmentManager manager = getSupportFragmentManager();
        FilesDialog filesDialog = new FilesDialog();
        filesDialog.setContext(this);
        filesDialog.setListRecords(listFiles);
        filesDialog.show(manager, "myDialog");

    }

    public void startGame(String nameRecord){
        Intent play = new Intent(AboutContact.this, Play.class);
        //set NameSelectedRecordForApp
        Records.setNameSelectedRecord(nameRecord);
        startActivity(play);
    }


    public void onClickButtonSelectRecord(View view){
        listFiles.clear();
        listFiles.addAll(Records.getFiles(Records.pathForFindRecords));
        startFilesDialog(listFiles);
    }

    public void onClickButtonCancel(View view){
        Intent main = new Intent(AboutContact.this, MainActivity.class);
        startActivity(main);
    }

    public void onClickButtonHelpAboutContact(View view){

    }




}

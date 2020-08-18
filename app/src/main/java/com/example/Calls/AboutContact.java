package com.example.Calls;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Calls.BackEnd.Api.AnalyzeCall;
import com.example.Calls.BackEnd.Api.SelectMethodSaveText;
import com.example.Calls.BackEnd.Contacts;
import com.example.Calls.BackEnd.FilesWork;
import com.example.Calls.BackEnd.Records;
import com.example.Calls.BackEnd.SavedSettings;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AboutContact extends AppCompatActivity {


    private Contacts contacts;

    private TextView textViewNameContact, textViewPhoneContact, textViewCountReady, textViewNeedMin, textViewNeedWords;

    private List<File> listFiles = new ArrayList<File>();

    public  String nameRecord;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_contact);

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
        Play.nameRecordStatic = nameRecord;
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

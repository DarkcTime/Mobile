package com.example.Calls;

//region import

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.system.Os;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Calls.BackEnd.Files.FileSystem;
import com.example.Calls.BackEnd.Services.ContactsService;
import com.example.Calls.BackEnd.Permissions.Permissions;
import com.example.Calls.BackEnd.Services.RecordsService;
import com.example.Calls.BackEnd.Settings.SavedSettings;
import com.example.Calls.Dialog.DialogMain;
import com.example.Calls.Dialog.PopupMenu;
import com.example.Calls.Dialog.SelectFileDialog;
import com.example.Calls.Model.Contact;
import com.example.Calls.Model.Repositories.ContactRepository;
import com.example.Calls.Model.Repositories.RecordRepository;
import com.example.Calls.Views.AllRecordsAdapter;
import com.example.Calls.Views.ContactAdapter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//endregion

//логика для главного окна в приложении
public class MainActivity extends AppCompatActivity {

    final DialogMain dialogMain = new DialogMain(this, DialogMain.Activities.MainActivity);
    final Permissions permissions = new Permissions(MainActivity.this);
    final SavedSettings savedSettings = new SavedSettings();
    final RecordRepository recordRepository = new RecordRepository();
    final ContactRepository contactRepository = new ContactRepository();

    private LinearLayout linerLayoutNoRecords;
    private LinearLayout linerLayoutListRecords;

    private EditText editTextSearchContacts;
    private ListView listViewContactsMA;
    private ArrayAdapter<Contact> contactAdapter;
    private EditText editTextSearchRecords;
    private ListView listViewRecordsMA;
    private ArrayAdapter<File> recordsAdapter;

    private Button buttonListContacts;
    private LinearLayout linearLayoutContacts;
    private Button buttonListRecords;
    private LinearLayout linearLayoutRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            linerLayoutNoRecords = (LinearLayout) (findViewById(R.id.linerLayoutNoRecords));
            linerLayoutListRecords = (LinearLayout) (findViewById(R.id.linerLayoutListRecords));

            editTextSearchContacts = (EditText) findViewById(R.id.editTextSearchContacts);
            listViewContactsMA = (ListView) (findViewById(R.id.listViewContactsMA));

            editTextSearchRecords = (EditText) findViewById(R.id.editTextSearchRecords);
            listViewRecordsMA = (ListView) (findViewById(R.id.listViewRecordsMA));

            buttonListContacts = (Button) (findViewById(R.id.buttonListContacts));
            linearLayoutContacts = (LinearLayout)(findViewById(R.id.linearLayoutContacts));
            buttonListRecords = (Button) (findViewById(R.id.buttonListRecords));
            linearLayoutRecords = (LinearLayout) (findViewById(R.id.linearLayoutRecords));

            savedSettings.setmSettings(getSharedPreferences(SavedSettings.APP_PREFERENCES, Context.MODE_PRIVATE));
            RecordsService.setPathForFindRecords(savedSettings.getmSettings()
                    .getString("path", RecordsService.currentPathForRecordsXiomi));

            boolean isVisited = savedSettings.getmSettings().getBoolean(SavedSettings.APP_PREFERENCES_HASVISITED, false);

            if (!isVisited) {
                savedSettings.setVisited(savedSettings.getmSettings());
                dialogMain.showHelpDialogFirstLaunch();
            }
            else{
                askPermission();
            }

            if(!RecordsService.checkPath(RecordsService.getPathForFindRecords()))
                noExistingPath();

            editTextSearchRecords.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if(count != 0)
                        contactAdapter.getFilter().filter(s.toString());
                    else
                        contactAdapter.getFilter().filter("");

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            listViewContactsMA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try{
                        Contact selectedContact = (Contact) parent.getItemAtPosition(position);
                        ContactRepository.setSelectedContact(selectedContact);

                        if(contactRepository.getProgressAsPercentage(ContactRepository.getSelectedContact())
                                >= ContactRepository.MAX_PERCENTAGE){
                            Intent psychologicalPortrait = new Intent(MainActivity.this, PsychologicalPortrait.class);
                            startActivity(psychologicalPortrait);
                        }
                        else{
                            Intent selectRecord = new Intent(MainActivity.this, SelectRecord.class);
                            startActivity(selectRecord);
                        }
                    }
                    catch (Exception ex){
                        dialogMain.showErrorDialogAndTheOutputLogs(ex, "listViewContactsMA");
                    }
                }
            });


        } catch (Exception ex) {
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "onCreateMainActivity");
        }
    }
    //region permissions
    public void askPermission() {
        try {
            if (permissions.isEnablePermissions()){
                loadListRecords();
            }
        } catch (Exception ex) {
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "askPermission");
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        try {
            if (grantResults.length > 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadListRecords();
            } else {
                // permission denied
                dialogMain.showPermissionDialog();
            }

        } catch (Exception ex) {
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "onRequestPermissionsResult");
        }

    }
    //endregion

    //region loadPage

    private void noExistingPath(){
        loadNoRecordsPage();
        Toast.makeText(this, "Директория не найдена на устройстве", Toast.LENGTH_LONG).show();
    }

    private void loadNoRecordsPage(){
        linerLayoutNoRecords.setVisibility(View.VISIBLE);
    }

    private void loadListRecords() {
        try {
            //fill records to RecordRepository
            RecordsService recordService = new RecordsService(this);
            recordService.create();
            recordService.generateListRecords();

            linerLayoutListRecords.setVisibility(View.VISIBLE);
            ContactsService.generateFilteredListContacts(this);

            ArrayList<Contact> contacts = ContactRepository.getListContacts();
            //if count records empty, return
            if(contacts.isEmpty()){
                loadNoRecordsPage();
                return;
            }

            contactAdapter = new ContactAdapter(this, R.layout.list_contacts, ContactRepository.getListContacts());
            listViewContactsMA.setAdapter(contactAdapter);

        } catch (Exception ex) {
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "loadMain");
        }
    }

    private void loadListAllRecords(){
        try{
            List<File> recordsBuffer = FileSystem.getFilesWithSelectedExtWithFilter(RecordsService.getPathForFindRecords(), ".mp3");
            ArrayList<File> records = new ArrayList<>(recordsBuffer);


            recordsAdapter = new AllRecordsAdapter(this, R.layout.list_all_records, records);
            listViewRecordsMA.setAdapter(recordsAdapter);
        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "loadListAllRecords");
        }
    }

    //endregion



    //region ifNotHaveRecords

    //open page for settings
    public void onClickSelectPath(View view){
        try{
            Intent settings = new Intent(MainActivity.this, Settings.class);
            startActivity(settings);
        } catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "onClickSelectPath");
        }
    }

    //open page for help
    public void onClickHelpIfNotRecords(View view){
        try{
            Intent help = new Intent(MainActivity.this, Help.class);
            startActivity(help);
        }catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "onClickHelpIfNotRecords");
        }
    }
    //endregion

    //region header menu
    public void onClickListContacts(View view){
        linearLayoutContacts.setVisibility(View.VISIBLE);
        linearLayoutRecords.setVisibility(View.GONE);
    }

    public void onClickListRecords(View view){
        if(recordsAdapter == null)
            loadListAllRecords();
        linearLayoutRecords.setVisibility(View.VISIBLE);
        linearLayoutContacts.setVisibility(View.GONE);

    }

    public void onClickButtonMainWindowMenu(View view) {
        try {
            showPopupMenu(this, view, R.menu.popupmenu_mainwindow);
        } catch (Exception ex) {
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "onClickButtonSettings");
        }
    }

    public void showPopupMenu(Context context, View view, int resource){
        android.widget.PopupMenu popup = new android.widget.PopupMenu(context, view);
        //popup.setOnMenuItemClickListener((android.widget.PopupMenu.OnMenuItemClickListener) context);
        popup.inflate(resource);

        popup.setOnMenuItemClickListener(new android.widget.PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.settings_main_menu:
                        Intent settings = new Intent(MainActivity.this, Settings.class);
                        startActivity(settings);
                        return true;
                    case R.id.help_main_menu:
                        Intent help = new Intent(MainActivity.this, Help.class);
                        startActivity(help);
                        return true;
                    case R.id.about_us_main_menu:
                        Intent about_us = new Intent(MainActivity.this, AboutUs.class);
                        startActivity(about_us);
                        return true;
                }
                return false;
            }
        });
        popup.show();
    }

    //endregion

    //endregion

}

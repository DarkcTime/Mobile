package com.example.Calls;

//region import

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.Calls.Views.ContactAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private EditText editTextSearch;
    private ListView listViewContactsMA;
    private ArrayAdapter<Contact> contactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            linerLayoutNoRecords = (LinearLayout) (findViewById(R.id.linerLayoutNoRecords));
            linerLayoutListRecords = (LinearLayout) (findViewById(R.id.linerLayoutListRecords));
            editTextSearch = (EditText) findViewById(R.id.editTextSearch);
            listViewContactsMA = (ListView) (findViewById(R.id.listViewContactsMA));

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


            editTextSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(count != 0)
                        contactAdapter.getFilter().filter(s);
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

                        Intent selectRecord = new Intent(MainActivity.this, SelectRecord.class);
                        startActivity(selectRecord);
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
            Log.d("per", "no");
            if (permissions.isEnablePermissions()){
                loadListRecords();
                Log.d("per", "true");
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

    private void noExistingPathForRecords(){
        loadNoRecordsPage();
        Toast.makeText(this, "Данная директория не найдена", Toast.LENGTH_LONG).show();
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

            contactAdapter = new ContactAdapter(this, R.layout.list_contacts, ContactRepository.getListContacts());

            listViewContactsMA.setAdapter(contactAdapter);

        } catch (Exception ex) {
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "loadMain");
        }

    }

    //endregion

    //region UIActions

    public void onClickSelectPath(View view){
        try{
            SelectFileDialog selectFileDialog = new SelectFileDialog(this);
            selectFileDialog.show();
        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "onClickSelectPath");
        }
    }

    //открывает окно настроек
    public void onClickButtonMainWindowMenu(View view) {
        try {
            PopupMenu.showPopupMenu(this, view, R.menu.popupmenu_mainwindow);
        } catch (Exception ex) {
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "onClickButtonSettings");
        }
    }


    //endregion


    //endregion


}

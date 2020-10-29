package com.example.Calls;

//region import

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Visibility;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Calls.BackEnd.Contacts.Contacts;
import com.example.Calls.BackEnd.Permissions.Permissions;
import com.example.Calls.BackEnd.Records.Records;
import com.example.Calls.BackEnd.Settings.SavedSettings;
import com.example.Calls.BackEnd.SharedClasses.SharedMethods;
import com.example.Calls.Dialog.DialogMain;
import com.example.Calls.Dialog.HelpDialog;

import java.io.File;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;
//endregion

//логика для главного окна в приложении
public class MainActivity extends AppCompatActivity {

    final DialogMain dialogMain = new DialogMain(this, DialogMain.Activities.MainActivity);
    final Permissions permissions = new Permissions(MainActivity.this);
    final SavedSettings savedSettings = new SavedSettings();

    private LinearLayout linerLayoutNoRecords;
    private LinearLayout linerLayoutListRecords;
    private EditText editTextSearch;
    private ListView listViewContactsMA;
    private ArrayAdapter<String> adapterContacts;

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
            Records.setPathForFindRecords(savedSettings.getmSettings().getString("path", Records.currentPathForRecordsXiomi));

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
                        adapterContacts.getFilter().filter(s);
                    else
                        adapterContacts.getFilter().filter("");
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            listViewContactsMA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try{
                        TextView text = (TextView) view;
                        Contacts.setInformationAboutUser(text.getText().toString());
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
                LoadActivity();
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
                LoadActivity();

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
    private void LoadActivity(){
        try{

            if(!Records.isExistingPathRecord()){
                noExistingPathForRecords();
            }
            else{
                if(Records.isHavingRecords()) loadListRecords();
                else loadNoRecordsPage();
            }
        }
        catch (Exception ex){
            Log.d("LoadActivityEx", ex.getMessage());
        }
    }
    private void noExistingPathForRecords(){
        loadNoRecordsPage();
        Toast.makeText(this, "Данная директория не найдена", Toast.LENGTH_LONG).show();
    }
    private void loadNoRecordsPage(){
        linerLayoutNoRecords.setVisibility(View.VISIBLE);
    }
    private void loadListRecords() {
        try {
            linerLayoutListRecords.setVisibility(View.VISIBLE);

            //make adapter

             adapterContacts = new ArrayAdapter<String>(this,
                            android.R.layout.simple_list_item_1,
                            Contacts.getFilteredListContacts(this));

            listViewContactsMA.setAdapter(adapterContacts);

        } catch (Exception ex) {
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "loadMain");
        }

    }

    //endregion

    //region ButtonsClick

    //открывает окно настроек
    public void onCLickButtonSettings(View view) {
        try {
            Intent settings = new Intent(MainActivity.this, Settings.class);
            startActivity(settings);
        } catch (Exception ex) {
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "onClickButtonSettings");
        }
    }

    //endregion


    //endregion


}

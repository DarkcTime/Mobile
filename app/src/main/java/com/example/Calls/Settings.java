package com.example.Calls;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Calls.BackEnd.Records.Records;
import com.example.Calls.BackEnd.Settings.SavedSettings;
import com.example.Calls.Dialog.SelectFileDialog;

import java.io.File;


public class Settings extends AppCompatActivity {


    private SharedPreferences mSettings;

    private EditText editTextPath;

    private Button buttonChangeRole;

    private boolean typeUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings);

        editTextPath = (EditText) findViewById(R.id.editTextPath);

        buttonChangeRole = (Button) findViewById(R.id.buttonChangeRole);

        mSettings = getSharedPreferences(SavedSettings.APP_PREFERENCES, Context.MODE_PRIVATE);

        editTextPath.setText(mSettings.getString("path", Records.currentPathForRecordsXiomi));

        typeUser = SavedSettings.isExpert();

        if(SavedSettings.isExpert()){
            buttonChangeRole.setBackgroundResource(R.drawable.expert);
        }
        else{
            buttonChangeRole.setBackgroundResource(R.drawable.begin);
        }

    }


    //save settings for application and open mainActivity
    public void onClickButtonSaveSettings(View view){
        try{
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putString(SavedSettings.APP_PREFERENCES_PATH , editTextPath.getText().toString());
            editor.apply();
            SavedSettings.setTypeUser(typeUser);
            Intent main = new Intent(Settings.this, MainActivity.class);
            startActivity(main);
        }
        catch (Exception ex){
            Toast.makeText(this, "Ошибка сохранения файла" + ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    //don't save settings
    public void onClickCancelSaveSettings(View view){
        Intent main = new Intent( Settings.this, MainActivity.class);
        startActivity(main);
    }

    //автоматическая генерация пути по марке телефона
    public void onClickButtonAuto(View view){
        setTextPath(AutoGeneratePathForRecords());
    }

    private String AutoGeneratePathForRecords(){
        //Xiaomi
        File xiaomi = new File(Records.currentPathForRecordsXiomi);
        if(xiaomi.exists()){
            Toast.makeText(this, "Марка вашего устройства - Xiaomi", Toast.LENGTH_SHORT).show();
            return Records.currentPathForRecordsXiomi;
        }
        return "";
    }

    //open dialog window for select path
    public void onClickButtonHand(View view){
        try{
            com.example.Calls.Dialog.SelectFileDialog selectFileDialog = new SelectFileDialog(this);
            selectFileDialog.show();
        }
        catch (Exception ex){
            Log.d("Exception,opFiDi", ex.getMessage());
        }
    }

    public void onClickButtonChangeRoleSetting(View view){
        if(SavedSettings.isExpert()){
            typeUser = false;
            buttonChangeRole.setBackgroundResource(R.drawable.begin);
        }
        else{
            typeUser = true;
            buttonChangeRole.setBackgroundResource(R.drawable.expert);
        }
    }

    public void setTextPath(String path){
        editTextPath.setText(path);
    }

    //region menu
    //create menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);
        return true;
    }

    //endregion


}

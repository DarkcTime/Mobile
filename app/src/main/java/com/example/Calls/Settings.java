package com.example.Calls;

import android.app.Activity;
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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.Calls.BackEnd.Records;
import com.example.Calls.BackEnd.SavedSettings;

import java.io.File;
import java.util.Set;


public class Settings extends AppCompatActivity {


    private SharedPreferences mSettings;

    final private static String pathSettings = "/data/data/com.example.Calls/cache/settings.txt";

    final private static String pathRecords = "/data/data/com.example.Calls/cache/path.txt";

    public static String selectedPathRecords = "";

    private EditText editTextPath;

    private Button buttonChangeRole;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings);

        editTextPath = (EditText) findViewById(R.id.editTextPath);

        buttonChangeRole = (Button) findViewById(R.id.buttonChangeRole);

        mSettings = getSharedPreferences(SavedSettings.APP_PREFERENCES, Context.MODE_PRIVATE);

        editTextPath.setText(mSettings.getString("path", Records.currentPathForRecordsXiomi));

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
            Intent main = new Intent(Settings.this, MainActivity.class);
            startActivity(main);
        }
        catch (Exception ex){
            Toast.makeText(this, "Ошибка сохранения файла" + ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickCancelSaveSettings(View view){
        Intent main = new Intent( Settings.this, MainActivity.class);
        startActivity(main);
    }

    //автоматическая генерация пути по марке телефона
    public void onClickButtonAuto(View view){
        editTextPath.setText(AutoGeneratePathForRecords());
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

    public void onClickButtonHand(View view){
        Toast.makeText(this, "Здесь будет диалоговое окно", Toast.LENGTH_SHORT).show();
    }

    public void onClickButtonChangeRoleSetting(View view){
        if(SavedSettings.isExpert()){
            SavedSettings.setTypeUser(false);
            buttonChangeRole.setBackgroundResource(R.drawable.begin);
        }
        else{
            SavedSettings.setTypeUser(true);
            buttonChangeRole.setBackgroundResource(R.drawable.expert);
        }
    }


    //region menu
    //create menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);
        return true;
    }

    //change menu item
    //button back
    @Override
    public void onBackPressed() {
        // super.onBackPressed();

    }

    //endregion


}

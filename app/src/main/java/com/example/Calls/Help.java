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
import android.widget.Toast;

import com.example.Calls.BackEnd.Records;
import com.example.Calls.BackEnd.SavedSettings;

import java.io.File;

public class Help extends AppCompatActivity {


    private SharedPreferences mSettings;

    private SavedSettings savedSettings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);

        mSettings = getSharedPreferences(SavedSettings.APP_PREFERENCES, Context.MODE_PRIVATE);

        savedSettings = new SavedSettings(mSettings);



    }

    private void startMainActivity(){
        Intent main = new Intent(Help.this, MainActivity.class);
        startActivity(main);
    }

    //region onClickButtons


    //утанавлиеает пользователю уровень начинающего
    public void onClickButtonSetBegin(View view){
        try{
            savedSettings.setTypeUser(false);
            startMainActivity();
        }
        catch (Exception ex){
            Toast.makeText(this, "Ошибка при установке уровня" + ex.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    //устанавливает пользователю уровень эксперта
    public void onClickButtonSetExpert(View view){
        try{
            savedSettings.setTypeUser(true);
            startMainActivity();
        }
        catch (Exception ex){
            Toast.makeText(this, "Ошибка при установке уровня", Toast.LENGTH_SHORT).show();
        }

    }

    //endregion

    //button back
    @Override
    public void onBackPressed() {
        // super.onBackPressed();

    }

}

package com.example.Calls;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.Calls.BackEnd.Settings.SavedSettings;

public class Help extends AppCompatActivity {


    private SharedPreferences mSettings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);

        mSettings = getSharedPreferences(SavedSettings.APP_PREFERENCES, Context.MODE_PRIVATE);

    }

    private void startMainActivity(){
        Intent main = new Intent(Help.this, MainActivity.class);
        startActivity(main);
    }

    //region onClickButtons


    //утанавлиеает пользователю уровень начинающего
    public void onClickButtonSetBegin(View view){
        try{
            SavedSettings.setTypeUser(false);
            startMainActivity();
        }
        catch (Exception ex){
            Toast.makeText(this, "Ошибка при установке уровня" + ex.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    //устанавливает пользователю уровень эксперта
    public void onClickButtonSetExpert(View view){
        try{
            SavedSettings.setTypeUser(true);
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

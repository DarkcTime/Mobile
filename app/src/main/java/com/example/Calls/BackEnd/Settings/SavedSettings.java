package com.example.Calls.BackEnd.Settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.Calls.Settings;

public class SavedSettings {

    private SharedPreferences mSettings;

    public void setmSettings(SharedPreferences mSettings){
        this.mSettings = mSettings;
    }

    public SharedPreferences getmSettings(){
        return mSettings;
    }

    public SavedSettings(){}

    public static final String APP_PREFERENCES = "mysettings";

    public static final String APP_PREFERENCES_PATH = "path";

    //время перемотки
    public static final String APP_PREFERENCES_REWIND_TIME = "rewind_time";

    //время до наступления паузы
    public static final String APP_PREFERENCES_PAUSE_TIME = "pause_time";

    //уровень выбранный пользователем
    public static final String APP_PREFERENCES_ISEXPERT = "is_expert";

    public static final String APP_PREFERENCES_HASVISITED = "has_visited";


    public boolean isNull(String type){
        try{

            Log.d("isExpert", String.valueOf(mSettings.contains(APP_PREFERENCES_ISEXPERT)));
            return mSettings.contains(type);

        }
        catch (NullPointerException ex) {
            return false;
        }

    }

    public  boolean isExpert(){
        return mSettings.getBoolean(APP_PREFERENCES_ISEXPERT, false);
    }

    public  void setTypeUser(boolean isExpert){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_ISEXPERT, isExpert);
        editor.apply();

    }

    public  void setSettingsTime(int rewind, int pause){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(APP_PREFERENCES_REWIND_TIME, rewind);
        editor.putInt(APP_PREFERENCES_PAUSE_TIME, pause);
        editor.apply();

    }

    public  void setVisited(SharedPreferences sharedPreferences){
        SharedPreferences.Editor e = mSettings.edit();
        e.putBoolean(SavedSettings.APP_PREFERENCES_HASVISITED, true);
        e.apply();
    }


}

package com.example.Calls.BackEnd;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.Calls.Settings;

public class SavedSettings {

    private static SharedPreferences mSettings;

    public static final String APP_PREFERENCES = "mysettings";

    public static final String APP_PREFERENCES_PATH = "path";

    //время перемотки
    public static final String APP_PREFERENCES_REWIND_TIME = "rewind_time";

    //время до наступления паузы
    public static final String APP_PREFERENCES_PAUSE_TIME = "pause_time";

    //уровень выбранный пользователем
    public static final String APP_PREFERENCES_ISEXPERT = "is_expert";

    public SavedSettings(SharedPreferences set){
        mSettings = set;
    }

    public boolean isNull(String type){
        try{
            return mSettings.contains(type);
        }
        catch (NullPointerException ex) {
            return false;
        }

    }

    public static boolean isExpert(){
        return mSettings.getBoolean(APP_PREFERENCES_ISEXPERT, false);
    }

    public static void setTypeUser(boolean isExpert){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_ISEXPERT, isExpert);
        editor.apply();

    }

    public static void setSettingsTime(int rewind, int pause){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(APP_PREFERENCES_REWIND_TIME, rewind);
        editor.putInt(APP_PREFERENCES_PAUSE_TIME, pause);
        editor.apply();

    }

}

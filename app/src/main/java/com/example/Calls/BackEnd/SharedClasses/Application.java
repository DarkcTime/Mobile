package com.example.Calls.BackEnd.SharedClasses;

import android.content.Context;

public class Application extends android.app.Application {

    private static Context appContext;

    public static Context getAppContext(){
        return appContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
    }
}

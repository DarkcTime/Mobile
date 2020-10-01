package com.example.Calls.Dialog;

import android.support.v4.app.FragmentManager;

import com.example.Calls.MainActivity;
import com.example.Calls.Play;
import com.example.Calls.WaitInEndPlay;

public class DialogMain {


    public static void startAlertDialog(MainActivity mainActivity, MyDialogHelp.Windows window){
        FragmentManager manager = mainActivity.getSupportFragmentManager();
        //MyDialogHelp.setContextMain(this);
        MyDialogHelp myDialogHelp = new MyDialogHelp(mainActivity, window);
        myDialogHelp.show(manager, "myDialog");
    }

    public static void startAlertDialog(WaitInEndPlay waitInEndPlay, MyDialogHelp.Windows window){
        FragmentManager manager = waitInEndPlay.getSupportFragmentManager();
        //MyDialogHelp.setContextMain(this);
        MyDialogHelp myDialogHelp = new MyDialogHelp(waitInEndPlay, window);
        myDialogHelp.show(manager, "myDialog");
    }

    public static void startAlertDialog(Play play, MyDialogHelp.Windows window){
        FragmentManager manager = play.getSupportFragmentManager();
        //MyDialogHelp.setContextMain(this);
        MyDialogHelp myDialogHelp = new MyDialogHelp(play, window);
        myDialogHelp.show(manager, "myDialog");
    }

}

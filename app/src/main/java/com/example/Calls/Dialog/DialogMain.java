package com.example.Calls.Dialog;

import android.support.v4.app.FragmentManager;

import com.example.Calls.MainActivity;
import com.example.Calls.WaitInEndPlay;

public class DialogMain {

    public static void startAlertDialog(MainActivity mainActivity, int _getButton){
        FragmentManager manager = mainActivity.getSupportFragmentManager();
        //MyDialogHelp.setContextMain(this);
        MyDialogHelp myDialogHelp = new MyDialogHelp(mainActivity, _getButton);
        myDialogHelp.show(manager, "myDialog");
    }

    public static void startAlertDialog(WaitInEndPlay waitInEndPlay, int _getButton){
        FragmentManager manager = waitInEndPlay.getSupportFragmentManager();
        //MyDialogHelp.setContextMain(this);
        MyDialogHelp myDialogHelp = new MyDialogHelp(waitInEndPlay, _getButton);
        myDialogHelp.show(manager, "myDialog");
    }

}

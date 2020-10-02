package com.example.Calls.Dialog;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.example.Calls.AboutContact;
import com.example.Calls.MainActivity;
import com.example.Calls.Play;
import com.example.Calls.WaitInEndPlay;

import java.io.File;
import java.util.List;

public class DialogMain {

    public static void startFilesDialog(AboutContact aboutContact, List<File> listFiles){
        FragmentManager manager = aboutContact.getSupportFragmentManager();
        FilesDialog filesDialog = new FilesDialog();
        filesDialog.setContext(aboutContact);
        filesDialog.setListRecords(listFiles);
        filesDialog.show(manager, "FilesDialog");
    }

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

    public static void startErrorDialog(MainActivity mainActivity,Exception ex){
        FragmentManager manager = mainActivity.getSupportFragmentManager();
        ErrorDialog errorDialog = new ErrorDialog(mainActivity,ex);
        errorDialog.show(manager, "ErrorDialog");
    }

}

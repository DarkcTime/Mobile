package com.example.Calls.Dialog;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.example.Calls.AboutContact;
import com.example.Calls.BackEnd.SharedClasses.Application;
import com.example.Calls.MainActivity;
import com.example.Calls.Play;
import com.example.Calls.WaitInEndPlay;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DialogMain {

    public DialogMain(){

    }

    public static void startErrorDialog(String typeMessage, String ErrorMessage){

    }

    public void startFilesDialog(AboutContact aboutContact) throws IOException {
        FragmentManager manager = aboutContact.getSupportFragmentManager();
        FilesDialog filesDialog = new FilesDialog(aboutContact);
        filesDialog.show(manager, "FilesDialog");
    }

    public void startAlertDialog(MainActivity mainActivity, MyDialogHelp.Windows window){
        FragmentManager manager = mainActivity.getSupportFragmentManager();
        MyDialogHelp myDialogHelp = new MyDialogHelp(mainActivity, window);
        myDialogHelp.setCancelable(false);
        myDialogHelp.show(manager, "MainActivity");
    }

    public void startAlertDialog(WaitInEndPlay waitInEndPlay, MyDialogHelp.Windows window){
        FragmentManager manager = waitInEndPlay.getSupportFragmentManager();
        MyDialogHelp myDialogHelp = new MyDialogHelp(waitInEndPlay, window);
        myDialogHelp.setCancelable(false);
        myDialogHelp.show(manager, "WaitEndPlay");
    }

    public void startAlertDialog(Play play, MyDialogHelp.Windows window){
        FragmentManager manager = play.getSupportFragmentManager();
        MyDialogHelp myDialogHelp = new MyDialogHelp(play, window);
        myDialogHelp.setCancelable(false);
        myDialogHelp.show(manager, "Play");
    }

    public static void startErrorDialog(MainActivity mainActivity,Exception ex){
        FragmentManager manager = mainActivity.getSupportFragmentManager();
        //ErrorDialog errorDialog = new ErrorDialog(mainActivity,ex);

        //errorDialog.show(manager, "ErrorDialog");
    }

    public static void startErrorDialog(Play play,Exception ex){
        FragmentManager manager = play.getSupportFragmentManager();
        Context context = new Play();

        //ErrorDialog errorDialog = new ErrorDialog(play,ex);
        //errorDialog.show(manager, "ErrorDialog");
    }
    public static void startErrorDialog(AboutContact aboutContact,Exception ex){
        FragmentManager manager = aboutContact.getSupportFragmentManager();
        //ErrorDialog errorDialog = new ErrorDialog(aboutContact,ex);
        //errorDialog.show(manager, "ErrorDialog");
    }
    public static void startErrorDialog(WaitInEndPlay waitInEndPlay,Exception ex){
        FragmentManager manager = waitInEndPlay.getSupportFragmentManager();
        //ErrorDialog errorDialog = new ErrorDialog(waitInEndPlay,ex);
        //errorDialog.show(manager, "ErrorDialog");
    }



}

package com.example.Calls.Dialog;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.example.Calls.AboutContact;
import com.example.Calls.BackEnd.SharedClasses.Application;
import com.example.Calls.MainActivity;
import com.example.Calls.Play;
import com.example.Calls.Settings;
import com.example.Calls.WaitInEndPlay;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * call dialog windows
 */

public class DialogMain {


    private FragmentManager manager;

    public DialogMain(){

    }


    /**
     * create object for show dialog
     * @param context context current activity
     * @param _activity call current activity
     */
    public DialogMain(Context context, Activities _activity){
        switch (_activity){
            case MainActivity:
                MainActivity mainActivity = (MainActivity)context;
                manager = mainActivity.getSupportFragmentManager();
                break;
            case AboutContact:
                AboutContact aboutContact = (AboutContact)context;
                manager = aboutContact.getSupportFragmentManager();
                break;
            case Settings:
                Settings settings = (Settings) context;
                manager = settings.getSupportFragmentManager();
                break;
            case Play:
                Play play = (Play) context;
                manager = play.getSupportFragmentManager();
                break;
            case WaitInEndPlay:
                WaitInEndPlay waitInEndPlay = (WaitInEndPlay)context;
                manager = waitInEndPlay.getSupportFragmentManager();
                break;
            default:
                //TODO make default use
                break;
        }


    }


    /**
     * show MyDialogHel[
     * @param window selected window with help
     */
    public void startMyDialogHelp(MyDialogHelp.Windows window){
        //create object dialog
        MyDialogHelp myDialogHelp = new MyDialogHelp(window);
        //whether the dialog - not.
        myDialogHelp.setCancelable(false);
        //show dialog
        myDialogHelp.show(manager, "MainActivity");
    }

    /**
     * show MyDialogHel[
     * @param window selected window with help
     */
    public void startAlertDialog(MyDialogHelp.Windows window){
        //create object dialog
        MyDialogHelp myDialogHelp = new MyDialogHelp(window);
        //whether the dialog - not.
        myDialogHelp.setCancelable(false);
        //show dialog
        myDialogHelp.show(manager, "MainActivity");
    }

    /**
     *
     */
    public void startErrorDialog(){

    }


    /**
     *
     * @throws IOException
     */
    public void startFilesDialog() throws IOException {
        FilesDialog filesDialog = new FilesDialog();
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

    /**
     * enum for select type Activity
     */
    public enum Activities {
        MainActivity, Play, Settings, AboutContact, WaitInEndPlay
    }


}

package com.example.Calls.Dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import android.support.v7.app.AppCompatDialogFragment;

import com.example.Calls.AboutContact;
import com.example.Calls.BackEnd.Debug.DebugMessages;
import com.example.Calls.BackEnd.Mail.Mailer;
import com.example.Calls.MainActivity;
import com.example.Calls.Play;
import com.example.Calls.R;
import com.example.Calls.Settings;
import com.example.Calls.WaitInEndPlay;

/**
 * Show user exception
 * and send messages with use Email
 * to team developers
 */

@SuppressLint("ValidFragment")
public class ErrorDialog extends AppCompatDialogFragment {

   private String typeException;
   private String errorMessage;
    /**
     * Activity values
     */
    private Activities activity;
    private MainActivity mainActivity;
    private Play play;
    private Settings settings;
    private AboutContact aboutContact;
    private WaitInEndPlay waitInEndPlay;


    /**
     * defines the activity
     * @param _context activity
     * @param _activity explicitly specifying the activity name
     */
    @SuppressLint("ValidFragment")
    public ErrorDialog(Context _context,Activities _activity){
        try{

            activity = _activity;

            switch (activity){
                case  MainActivity:
                    mainActivity = (MainActivity)_context;
                    break;
                case Play:
                    play = (Play)_context;
                    break;
                case Settings:
                    settings = (Settings)_context;
                    break;
                case AboutContact:
                    aboutContact = (AboutContact)_context;
                    break;
                case WaitInEndPlay:
                    waitInEndPlay = (WaitInEndPlay) _context;
                    break;
                default:
                    throw new NullPointerException("UnknownActivity");
            }
        }
        catch (NullPointerException nullPointException){
            Log.d("ErrorDialog", nullPointException.getMessage());
        }
        catch (Exception ex){
            //TODO create catch for this Error
        }

    }

    /**
     *
     * @param savedInstanceState
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(typeException)
                .setIcon(R.drawable.unavailable)
                .setMessage(errorMessage)
                .setPositiveButton("Отправить разработчику на мыло", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Mailer mailer = new Mailer(builder.getContext());
                        mailer.SendMail(typeException, errorMessage);

                        //TODO debug test
                        Log.d("Activity", builder.getContext().getPackageCodePath());

                        dialog.cancel();
                    }
                })
                .setNegativeButton("Не хочу помогать", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });


        return builder.create();
    }


    /**
     * enum for select type Activity
     */
    public enum Activities {
        MainActivity, Play, Settings, AboutContact, WaitInEndPlay
    }
}



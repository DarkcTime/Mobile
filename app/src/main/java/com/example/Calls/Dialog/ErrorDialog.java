package com.example.Calls.Dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import android.support.v7.app.AppCompatDialogFragment;

import com.example.Calls.BackEnd.Mail.Mailer;
import com.example.Calls.R;

/**
 * Show user exception
 * and send messages with use Email
 * to team developers
 */

@SuppressLint("ValidFragment")
public class ErrorDialog extends AppCompatDialogFragment {

   private String typeException;
   private String errorMessage;
   private String tag;

    /**
     * Create Parameters for error dialog
     * @param _typeException example: NullPointException
     * @param _errorMessage example: divide by zero
     * @param _tag example: onCreateMainActivity
     */
    public ErrorDialog(String _typeException, String _errorMessage, String _tag){
        try{
            typeException = _typeException;
            errorMessage = _errorMessage;
            tag = _tag;
        }
        catch (Exception ex){
            Log.d("ErrorDialog", ex.getMessage());
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        try{
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            final String message = "Tag: ".concat(tag).concat("\n").concat("Description: ").concat(errorMessage);

            builder.setTitle(typeException)
                    .setIcon(R.drawable.unavailable)
                    .setMessage(message)
                    .setPositiveButton("Отправить разработчику на мыло", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            Mailer mailer = new Mailer(builder.getContext());
                            mailer.SendMail(typeException, message);

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
        catch (Exception ex){
            Log.d("onCreateError", ex.getMessage());
            final  AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("Error on create dialog")
            .setMessage(ex.getMessage());

            return builder.create();
        }

    }


    /**
     * enum for select type Activity
     */
    public enum Activities {
        MainActivity, Play, Settings, AboutContact, WaitInEndPlay
    }
}



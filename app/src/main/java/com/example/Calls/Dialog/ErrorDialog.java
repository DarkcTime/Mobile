package com.example.Calls.Dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import android.support.v7.app.AppCompatDialogFragment;

import com.example.Calls.BackEnd.Debug.DebugMessages;
import com.example.Calls.BackEnd.Mail.Mailer;
import com.example.Calls.MainActivity;
import com.example.Calls.R;

@SuppressLint("ValidFragment")
public class ErrorDialog extends AppCompatDialogFragment {


    private Exception ex;
    private MainActivity mainActivity;

    @SuppressLint("ValidFragment")
    public ErrorDialog(MainActivity _mainActivity,Exception _ex){
        mainActivity = _mainActivity;
        ex = _ex;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Возникло исключение")
                .setIcon(R.drawable.que)
                .setMessage(ex.toString())
                .setPositiveButton("Отправить разработчику на мыло", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Mailer mailer = new Mailer();
                        mailer.SendMessageAboutError(ex, mainActivity);
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
}

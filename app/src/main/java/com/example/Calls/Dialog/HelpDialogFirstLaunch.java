package com.example.Calls.Dialog;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;

import com.example.Calls.MainActivity;
import com.example.Calls.R;


public class HelpDialogFirstLaunch extends AppCompatDialogFragment {

    private MainActivity MainActivity;

    public void setMainActivity(MainActivity _MainActivity){
        MainActivity = _MainActivity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        try{
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("Справка")
                    .setIcon(R.drawable.que)
                    .setMessage("Для работы приложения необходимы права: " +
                            "\n1) На получение списка контактов (для определения портрета) " +
                            "\n2) На чтение/запись данных в папки")
                    .setPositiveButton("Ок, понял", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if(MainActivity != null) MainActivity.askPermission();
                            dialog.cancel();
                        }
                    });
            return builder.create();
        }
        catch (Exception ex){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Exception")
                    .setMessage(ex.getMessage());
            return builder.create();
        }
    }
}

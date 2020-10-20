package com.example.Calls.Dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.example.Calls.MainActivity;
import com.example.Calls.Play;
import com.example.Calls.R;

public class PermissionDialog  extends DialogMain{

    private AlertDialog.Builder builder;

    private com.example.Calls.MainActivity mainActivity;

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        try{

            builder = new AlertDialog.Builder(getActivity());

            generateDialog();

            return builder.create();
        }
        catch (Exception ex){
            showErrorDialogAndTheOutputLogs(ex, "PermissionDialog");
            return null;
        }
    }

    private void generateDialog(){
        builder.setTitle(getString(R.string.give_permission_rus))
                .setIcon(R.drawable.unavailable)
                .setMessage(getString(R.string.give_permission_mes_rus))
                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mainActivity.finish();
                        dialog.cancel();
                        mainActivity.askPermission();
                    }
                })
                .setNegativeButton("Закрыть", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mainActivity.finish();
                        dialog.cancel();
                    }
                });

    }
}

package com.example.Calls.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import com.example.Calls.AboutContact;
import com.example.Calls.BackEnd.Contacts;
import com.example.Calls.BackEnd.Records;

import java.io.File;
import java.util.List;

public class FilesDialog extends AppCompatDialogFragment {

    public AboutContact context;

    public void setContext(AboutContact context1){
        context = context1;
    }

    private String[] listRecords;

    public void setListRecords(List<File> listFiles){
        Contacts contacts = new Contacts();
        Records.getFilterRecords(listFiles, contacts.getNameCurrentContact());
        listRecords = new String[listFiles.size()];
        int i = 0;
        for(File file : listFiles){
            listRecords[i] = file.getName();
            i += 1;
        }
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Количество записей: " + listRecords.length)
                .setItems(listRecords, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.startGame(listRecords[which]);
                    }
                });

        return builder.create();
    }
}

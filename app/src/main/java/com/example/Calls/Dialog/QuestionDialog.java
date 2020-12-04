package com.example.Calls.Dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;

import com.example.Calls.BackEnd.Files.Directories;
import com.example.Calls.BackEnd.Files.FileSystemParameters;
import com.example.Calls.BackEnd.Mail.Mailer;
import com.example.Calls.EditTextRecord;
import com.example.Calls.MainActivity;
import com.example.Calls.Model.Repositories.RecordRepository;
import com.example.Calls.R;
import com.example.Calls.SelectRecord;
import com.example.Calls.WaitInEndPlay;

import java.io.File;

@SuppressLint("ValidFragment")
public class QuestionDialog extends AppCompatDialogFragment {

    private EditTextRecord editTextRecord;

    @SuppressLint("ValidFragment")
    public QuestionDialog(EditTextRecord editTextRecord){
        this.editTextRecord = editTextRecord;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //final String message = "Tag: ".concat(tag).concat("\n").concat("Description: ").concat(errorMessage);

        builder.setTitle("")
                .setIcon(R.drawable.unavailable)
                .setMessage("Вы уверены, что хотите закрыть полученный результат?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Directories directories = new Directories(RecordRepository.getSelectedRecord());
                        directories.deleteDirectory(new File(FileSystemParameters.getPathForSelectedRecord()));

                        Intent intent = new Intent(editTextRecord, MainActivity.class);
                        startActivity(intent);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }
}

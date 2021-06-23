package com.example.Calls.Dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import com.example.Calls.R;

public class HelpDialog extends AppCompatDialogFragment {

    private AlertDialog.Builder builder;

    private Helps help;

    public void setHelp(Helps help) {
        this.help = help;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity());

        switch (help){
            case FirstHelp:
                generateDialog(getString(R.string.help_rus), getString(R.string.firstHelp_message_rus), getString(R.string.understand_rus));
                break;
            case PlayHelp:
                generateDialog(getString(R.string.help_rus), getString(R.string.helpPlay_message_rus), getString(R.string.understand_rus));
        }

        return builder.create();
    }


    private void generateDialog(String title, String message, String titleButton){
        builder.setTitle(title)
                .setIcon(R.drawable.que)
                .setMessage(message)
                .setPositiveButton(titleButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
    }

    public enum Helps{
        FirstHelp, PlayHelp
    }
}

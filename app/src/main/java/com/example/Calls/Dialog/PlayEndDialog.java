package com.example.Calls.Dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.example.Calls.Play;
import com.example.Calls.R;

public class PlayEndDialog extends DialogMain{

    private AlertDialog.Builder builder;

    private Play Play;

    public void setPlay(Play play) {
        this.Play = play;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        try{
            builder = new AlertDialog.Builder(getActivity());


            builder.setTitle(getString(R.string.selected_cutter_rus))
                    .setIcon(R.drawable.media)
                    .setPositiveButton(getString(R.string.continue_rus), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Play.StopGame();
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton(getString(R.string.return_selected_record_rus), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Play.startAboutContact();
                            dialog.cancel();
                        }
                    });


            return builder.create();
        }
        catch (Exception ex){
            showErrorDialogAndTheOutputLogs(ex, "PlayEndDialog");
            return null;
        }

    }
}

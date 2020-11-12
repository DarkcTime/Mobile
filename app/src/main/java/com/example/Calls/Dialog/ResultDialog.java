package com.example.Calls.Dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import com.example.Calls.BackEnd.Api.ApiMain;
import com.example.Calls.BackEnd.SharedClasses.SharedMethods;
import com.example.Calls.R;
import com.example.Calls.WaitInEndPlay;

public class ResultDialog extends DialogMain {

    private WaitInEndPlay waitInEndPlayActivity;

    public void setWaitInEndPlayActivity(WaitInEndPlay waitInEndPlayActivity) {
        this.waitInEndPlayActivity = waitInEndPlayActivity;
    }

    private AlertDialog.Builder builder;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        try{

            builder = new AlertDialog.Builder(getActivity());

            ApiMain apiMain = new ApiMain();

            String message = apiMain.readFullFileSelectedRecord();


            if(SharedMethods.isNullOrWhiteSpace(message)){
                message = "Текст для данной записи не удалось получить";
            }
            else{
                generateBuilderAddPositiveButton();
            }

            generateBuilder(message);


            return builder.create();
        }
        catch (Exception ex){
            showErrorDialogAndTheOutputLogs(ex, "ResultDialog");
            return null;
        }
    }

    private void generateBuilder(String message){
        builder.setTitle(getString(R.string.translated_text_res))
                .setIcon(R.drawable.success)
                .setMessage(message)
                .setNegativeButton(getString(R.string.cancel_rus), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });
    }

    private void generateBuilderAddPositiveButton(){
        builder.setPositiveButton(getString(R.string.add_rus), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
    }



}

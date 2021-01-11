package com.example.Calls;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.Calls.BackEnd.Api.ApiMain;
import com.example.Calls.BackEnd.Services.HistoryTranslateService;
import com.example.Calls.BackEnd.SharedClasses.SharedMethods;
import com.example.Calls.Dialog.DialogMain;
import com.example.Calls.Model.Repositories.RecordRepository;

@SuppressLint("Registered")
public class EditTextRecord extends AppCompatActivity {

    //dialog windows
    final DialogMain dialogMain = new DialogMain(this, DialogMain.Activities.EditTextRecord);

    private EditText editTextTranslatedResult;
    private ApiMain apiMain;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.edit_text_record);

            String noText = "текст не получен для данной записи";

            apiMain = new ApiMain();
            String result = apiMain.readFullFileSelectedRecord();

            editTextTranslatedResult = (EditText) findViewById(R.id.editTextTranslatedResult);
            if (SharedMethods.isNullOrWhiteSpace(result)){
                result = noText;
                editTextTranslatedResult.setEnabled(false);
            }

            editTextTranslatedResult.setText(result);

        } catch (Exception ex) {
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "onCreateEditText");
        }
    }

    //region UI Button
    public void onClickButtonSaveEdit(View view) {
        try {
            String message = editTextTranslatedResult.getText().toString();
            apiMain.addTextInFullFileSelectedContact(message);
            //add record in history
            HistoryTranslateService.writeRecord(RecordRepository.getSelectedRecord());

            Intent selectRecord = new Intent(EditTextRecord.this, com.example.Calls.SelectRecord.class);
            startActivity(selectRecord);
        } catch (Exception ex) {
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "onClickButtonSaveEdit");
        }
    }

    //endregion


    @Override
    public void onBackPressed() {
        dialogMain.showQuestionDialogEditText();
    }
}
